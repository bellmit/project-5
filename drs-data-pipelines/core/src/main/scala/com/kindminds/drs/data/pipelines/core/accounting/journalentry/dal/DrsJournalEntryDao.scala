package com.kindminds.drs.data.pipelines.core.accounting.journalentry.dal

import java.math.BigDecimal
import java.sql.Timestamp
import java.time.OffsetDateTime
import java.util
import java.util.stream.Collectors
import java.util.{Date, List, Map}

import com.kindminds.drs.data.pipelines.core.accounting.journalentry._
import com.kindminds.drs.data.pipelines.core.util.jooq.DrsDriver
import com.kindminds.drs.api.v1.model.accounting.SettlementPeriod
import com.kindminds.drs.v1.model.impl.accounting.SettlementPeriodImpl
import org.jooq.{DSLContext, Record, Result, SQLDialect}
import org.jooq.impl.DSL
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.util.Assert

import scala.collection.convert.ImplicitConversions.`collection AsScalaIterable`


class  DrsJournalEntryDao {

  private val dsl: DSLContext = DSL.using(
    DrsDriver.getConnection(),
    SQLDialect.POSTGRES
  )


  def queryServiceExpenseInfo(supplierCode:String, periodStart: java.util.Date,
     periodEnd:java.util.Date) : java.util.List[ServiceExpenseRow] = {



      val sql = "SELECT tlt.name as transaction_line_type, dtli.amount as line_amount  " +
      " FROM domestic_transaction_line_item dtli " +
      " inner join domestic_transaction dt on dtli.transaction_id = dt.id " +
      " inner join company cpy on cpy.id = dt.splr_company_id " +
      " inner join transactionlinetype tlt on tlt.id = dtli.type_id " +
      " where cpy.k_code = ? " +
      " and transaction_date >= ? " +
      " and transaction_date < ? " +
      " order by transaction_date desc "

    val st = new java.sql.Timestamp(periodStart.getTime)
    val et = new java.sql.Timestamp(periodEnd.getTime)

    val result: util.List[ServiceExpenseRow] = dsl.fetch(sql , supplierCode ,  st, et).map(r =>{
      ServiceExpenseRow(r.get(0).toString , new java.math.BigDecimal(r.get(1).toString))
    })


    if(result  != null){result}else{new util.ArrayList[ServiceExpenseRow]}

  }

  def queryServiceExpenseTaxInfo(supplierCode:String, periodStart: Date,
    periodEnd:Date) : java.util.List[ServiceExpenseTaxRow] = {


    val sql = "SELECT amount_tax  " +
    " FROM public.domestic_transaction dt " +
    " inner join company cpy on cpy.id = dt.splr_company_id " +
    " where cpy.k_code = ?  " +
    " and transaction_date >= ?  " +
    " and transaction_date < ?  "

    val st = new java.sql.Timestamp(periodStart.getTime)
    val et = new java.sql.Timestamp(periodEnd.getTime)

    val result: util.List[ServiceExpenseTaxRow] = dsl.fetch(sql, supplierCode,  st, et).map { r =>
      ServiceExpenseTaxRow( new java.math.BigDecimal(r.get(0).toString))
    }


    if(result  != null)
      result
    else{new util.ArrayList[ServiceExpenseTaxRow]}

  }

  def querySellBackInfo(statementName: String) : java.util.List[SellBackRow] = {


    val sql = "SELECT slt.name as statement_line_type, statement_amount as sum_amount , reference , " +
      " ps.code_by_drs as sku ,  bsli.quantity as quantity  " +
    " FROM bill_statementlineitem bsli " +
    " inner join bill_statement bs on bs.id = bsli.statement_id " +
    " inner join statement_line_type slt on slt.id = bsli.stlmnt_line_item_type_id " +
      " inner join product_sku ps on ps.id = bsli.product_sku_id " +
    " where bs.name = ? " +
    " and bsli.stlmnt_line_item_type_id in (23,26,27,28,30) " +
    " order by slt.name "
    //" group by slt.name"


    /*
    SS2SP_ProductInventorySellBack
SS2SP_InventorySellBackTaiwan
SS2SP_InventorySellBackOther
SS2SP_InventorySellBackDispose
     */

    val queryUnitAmtAndSalesTaxRate = " " +
      "select sli.unit_amount , s2.sales_tax_rate from shipment_line_item sli  " +
      "inner join shipment s2 on s2.id = sli.shipment_id " +
      "inner join product_sku ps on ps.id = sli.sku_id " +
      "where ps.code_by_drs = ? " +
      "and s2.name = ?  "

    val rawResult : util.List[SellBackRow] = dsl.fetch(sql,statementName).map { r =>{

      val result: Result[Record] = dsl.fetch(queryUnitAmtAndSalesTaxRate,r.get(3).toString, r.get(2).toString)

      SellBackRow(r.get(0).toString(),
        new java.math.BigDecimal( result.getValues("unit_amount").get(0).toString).multiply(
          new BigDecimal(r.get(4).toString))
        , r.get(2).toString ,
        r.get(3).toString ,
        new java.math.BigDecimal( result.getValues("sales_tax_rate").get(0).toString()))
    }}


    import scala.collection.JavaConverters._

    var result = new util.ArrayList[SellBackRow]()
    if(rawResult != null && rawResult.size()>0){

      val r1 =  rawResult.asScala.toList.filter(_.statementLineType == ("SS2SP_InventorySellBackDispose"))

      var sumAmt = java.math.BigDecimal.ZERO
      var sumTaxAmt  = java.math.BigDecimal.ZERO
      r1.foreach(x=>{
        sumAmt = sumAmt.add(x.sumAmount)
      })
      if(sumAmt.compareTo(java.math.BigDecimal.ZERO) == 1) sumTaxAmt = sumAmt.multiply(r1(0).salesTaxRate)

      result.add(SellBackRow("SS2SP_InventorySellBackDispose",
        sumAmt.add(sumTaxAmt.setScale(0,java.math.BigDecimal.ROUND_HALF_UP)),"","",BigDecimal.ZERO))

      sumAmt  = java.math.BigDecimal.ZERO
      sumTaxAmt  = java.math.BigDecimal.ZERO
      val r2 =  rawResult.asScala.toList.filter(_.statementLineType == ("SS2SP_ProductInventorySellBack"))

      r2.foreach(x=>{
        sumAmt = sumAmt.add(x.sumAmount)
      })
      if(sumAmt.compareTo(java.math.BigDecimal.ZERO) == 1) sumTaxAmt = sumAmt.multiply(r2(0).salesTaxRate)

      result.add(SellBackRow("SS2SP_ProductInventorySellBack",
        sumAmt.add(sumTaxAmt.setScale(0,java.math.BigDecimal.ROUND_HALF_UP)),"" ,"",BigDecimal.ZERO))

      sumAmt  = java.math.BigDecimal.ZERO
      sumTaxAmt  = java.math.BigDecimal.ZERO
      val r3 =  rawResult.asScala.toList.filter(_.statementLineType == ("SS2SP_InventorySellBackTaiwan"))
      r3.foreach(x=>{
        sumAmt = sumAmt.add(x.sumAmount)
      })
      if(sumAmt.compareTo(java.math.BigDecimal.ZERO) == 1) sumTaxAmt = sumAmt.multiply(r3(0).salesTaxRate)
      result.add(SellBackRow("SS2SP_InventorySellBackTaiwan",
        sumAmt.add(sumTaxAmt.setScale(0,java.math.BigDecimal.ROUND_HALF_UP)),"","",BigDecimal.ZERO))

      sumAmt  = java.math.BigDecimal.ZERO
      sumTaxAmt  = java.math.BigDecimal.ZERO
      val r4 =  rawResult.asScala.toList.filter(_.statementLineType == ("SS2SP_InventorySellBackOther"))

      r4.foreach(x=>{
        sumAmt = sumAmt.add(x.sumAmount)
      })
      if(sumAmt.compareTo(java.math.BigDecimal.ZERO) == 1) sumTaxAmt = sumAmt.multiply(r4(0).salesTaxRate)

      result.add(SellBackRow("SS2SP_InventorySellBackOther",
        sumAmt.add(sumTaxAmt.setScale(0,java.math.BigDecimal.ROUND_HALF_UP)),"","",BigDecimal.ZERO))

      sumAmt  = java.math.BigDecimal.ZERO
      sumTaxAmt  = java.math.BigDecimal.ZERO
      val r5 =  rawResult.asScala.toList.filter(_.statementLineType == ("SS2SP_InventorySellBackRecovery"))

      r5.foreach(x=>{
        sumAmt = sumAmt.add(x.sumAmount)
      })
      if(sumAmt.compareTo(java.math.BigDecimal.ZERO) == 1) sumTaxAmt = sumAmt.multiply(r5(0).salesTaxRate)

      result.add(SellBackRow("SS2SP_InventorySellBackRecovery",
        sumAmt.add(sumTaxAmt.setScale(0,java.math.BigDecimal.ROUND_HALF_UP)),"","",BigDecimal.ZERO))

    }


    if(result  != null){result}else{new util.ArrayList[SellBackRow]}



  }



  def queryIVSPaymentRefundInfo(statementName:String) : java.util.List[IVSPaymentRefundRow] =  {


    val sql = "SELECT reference, sum(statement_amount) as sum_amount " +
    " FROM bill_statementlineitem bsli " +
    " inner join bill_statement bs on bs.id = bsli.statement_id " +
    " where bs.name = ?  " +
    " and bsli.stlmnt_line_item_type_id in (12,13) " +
    " group by reference"



    val result : util.List[IVSPaymentRefundRow] = dsl.fetch(sql, statementName ).map { r =>
      IVSPaymentRefundRow(r.get(0).toString, new java.math.BigDecimal(r.get(1).toString))
    }


    if(result  != null){result}else{new util.ArrayList[IVSPaymentRefundRow]}




  }

  def  queryKMISupplierPaymentInfo(companyCode:String, periodStart:Date, periodEnd:Date)
    : java.util.List[KMISupplierPaymentRow] = {


    val sql = "SELECT date_sent, sd.k_code as sender_code, rc.k_code as receiver_code, amount " +
    " FROM remittance rm " +
    " INNER JOIN company sd ON sd.id = rm.sender_company_id " +
    " INNER JOIN company rc ON rc.id = rm.receiver_company_id " +
    " where (sd.k_code = ? " +
    " OR rc.k_code = ?) " +
    " AND date_sent >= ?  " +
    " AND date_sent < ? "


    val st = new java.sql.Timestamp(periodStart.getTime)
    val et = new java.sql.Timestamp(periodEnd.getTime)


    val result : util.List[KMISupplierPaymentRow] = dsl.fetch(sql, companyCode, companyCode, st, et).map { r =>
      KMISupplierPaymentRow(r.get(0).toString,r.get(1).toString,r.get(2).toString,
        new java.math.BigDecimal(r.get(3).toString))
    }


    if(result  != null){result}else{new util.ArrayList[KMISupplierPaymentRow]}



  }

  def queryPreviousBalanceInfo(statementName:String) : PreviousBalanceRow = {


    val sql = " SELECT sequence, period_start - interval '14 days' as period_start, " +
    " period_end - interval '14 days' as period_end, previous_balance  " +
    " FROM bill_statement " +
    " where name = ? " +
    " ORDER BY id desc "



    val result : util.List[PreviousBalanceRow] = dsl.fetch(sql, statementName).map { r =>
      PreviousBalanceRow(Integer.parseInt(r.get(0).toString) , r.get(1).toString,r.get(2).toString,
        new java.math.BigDecimal(r.get(3).toString))
    }

    if(result  != null){result.get(0)}else{PreviousBalanceRow(0,"","",java.math.BigDecimal.ZERO)}
  }

  def queryCurrentBalanceInfo(statementName: String) : CurrentBalanceRow =  {


    val sql = "SELECT balance as current_balance" +
    " FROM public.bill_statement " +
    " where name = ? "


    val result : util.List[CurrentBalanceRow] = dsl.fetch(sql,statementName ).map { r =>
      CurrentBalanceRow(new java.math.BigDecimal(r.get(0).toString))
    }

    if(result  != null){result.get(0)}else{CurrentBalanceRow(java.math.BigDecimal.ZERO)}

  }

  def queryProfitCostShareInfo(statementName:String) : java.util.List[ProfitCostShareRow] = {


    val sql = "SELECT cty.code as country_code, statement_amount_untaxed " +
    " FROM bill_statement_profitshare_item bspi " +
    " INNER JOIN bill_statement bs on bs.id = bspi.statement_id " +
    " INNER JOIN country cty on cty.id = bspi.source_country_id " +
    " where bs.name = :statementName "



    val result : util.List[ProfitCostShareRow] = dsl.fetch(sql,statementName).map { r =>
      ProfitCostShareRow(r.get(0).toString, new java.math.BigDecimal(r.get(1).toString))
    }

    if(result  != null){result}else{new util.ArrayList[ProfitCostShareRow]()}


  }

  def queryProfitCostShareTaxInfo(statementName: String) : ProfitCostShareTaxRow = {


    val sql = "SELECT statement_amount  " +
    " FROM bill_statementlineitem bsli " +
    " INNER JOIN bill_statement bs on bs.id = bsli.statement_id " +
    " AND transactionlinetype_id = 63 " +
    " WHERE bs.name = ? "



    val result = dsl.fetch(sql,statementName).map { r =>
      ProfitCostShareTaxRow(new java.math.BigDecimal(r.get(0).toString))
    }

    if(!result.isEmpty){result.get(0)}else{ProfitCostShareTaxRow(java.math.BigDecimal.ZERO) }

  }


  def queryCompanyCodeStatements(periodStart: Date) : util.List[CompanyCodeStatement] = {


    val sql = "SELECT cpy.k_code, name from bill_statement bs" +
      " inner join company cpy on cpy.id = bs.receiving_company_id  where period_start = ? " +
      " and receiving_company_id != 2  order by name"

    val result: util.List[CompanyCodeStatement] = dsl.fetch(sql,
      new Timestamp(periodStart.getTime)).map { r =>
      CompanyCodeStatement(r.get(0).toString,r.get(1).toString)
    }


    result
  }


  def queryRecentPeriods(): util.List[SettlementPeriod] = {

    val sqlSb = new StringBuilder().append("select      sp.id as id, ")
      .append("  sp.period_start as start, ")
      .append("  sp.period_end   as end ")
      .append("from settlement_period sp ")
      .append("order by period_start desc ")
      .append("limit 1 offset 0 ")


     val result: util.List[SettlementPeriod] = dsl.fetch(sqlSb.toString()).map { r =>

       new SettlementPeriodImpl(r.get(0).toString.toInt,
         new Date( r.get(1).asInstanceOf[OffsetDateTime].toInstant().toEpochMilli()),
         new Date( r.get(2).asInstanceOf[OffsetDateTime].toInstant().toEpochMilli()))

     }


     result


  }


  def queryLatestPeriodEnd(): Date = {

    val sql = "select max(period_end) from bill_statement "

    val resultList: Result[Record] = dsl.fetch(sql)

    if (resultList.isEmpty) {
      null
    } else {
      new Date( resultList.get(0).getValue(0).asInstanceOf[OffsetDateTime].toInstant.toEpochMilli)
    }
  }



  def queryRemittanceList(transactionDate:Date, transactionDateEnd:Date): util.List[RemittanceInfo] = {

    val sql = "SELECT date_sent, cpy.k_code as company_code, amount, reference, " +
      " fee_amount, fee_included, bank_payment, statement_name " +
      " FROM remittance rm " +
      " INNER JOIN company cpy on cpy.id = rm.receiver_company_id " +
      " WHERE date_sent >= ? " +
      " and date_sent < ? " +
      " and sender_company_id = 2 " +
      " order by date_sent, company_code "


    val result: util.List[RemittanceInfo] = dsl.fetch(sql,
      new Timestamp(transactionDate.getTime), new Timestamp(transactionDateEnd.getTime))
      .map { r =>
      RemittanceInfo(new Date(r.get(0).asInstanceOf[OffsetDateTime].toInstant().toEpochMilli()),
        r.get(1).toString, new java.math.BigDecimal(r.get(2).toString()), r.get(3).toString,
        new java.math.BigDecimal(r.get(4).toString()), java.lang.Boolean.valueOf(r.get(5).toString),
        new java.math.BigDecimal(r.get(6).toString()), r.get(7).toString())
    }


    result

  }


}



