package com.kindminds.drs.data.pipelines.core.accounting.journalentry.dal

import java.math.BigDecimal
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util
import java.util.{Date, List}

import com.kindminds.drs.data.pipelines.core.accounting.journalentry._
import com.kindminds.drs.data.pipelines.core.util.jooq.{DrsDriver, ErpDriver}
import org.jooq.impl.DSL
import org.jooq._
import org.springframework.util.Assert


class  ErpJournalEntryDao {

  private val dsl: DSLContext = DSL.using(
    ErpDriver.getConnection(),
    SQLDialect.POSTGRES
  )


   def deleteJournalEntriesByDate(transactionDate: Date): Unit = {

     DSL.using(dsl.configuration()).transaction(c =>{

       var sql = "delete from account_move_line where date = :transactionDate and journal_id = 11 "

       val td = new Timestamp(transactionDate.getTime)

       val deleteCount =  c.dsl().query(sql , td).execute()

       sql = "delete from account_move where date = :transactionDate and journal_id = 11 "
       val deleteCount2 =  c.dsl().query(sql , td).execute()


     })


  }


  def insertJournalEntry(entry: JournalEntry, ref: String, journalId: Integer,
                         companyId: Integer, currencyId: Integer): Unit = {

    val sql = "INSERT INTO account_move(  name, date, ref, state, type, to_check, journal_id, " +
      " company_id, currency_id, amount_untaxed, amount_tax, amount_total, " +
      "amount_residual, amount_untaxed_signed, amount_tax_signed, amount_total_signed, " +
      " amount_residual_signed, auto_post,  invoice_user_id, invoice_sent, " +
      " create_uid, create_date, write_uid, write_date, team_id,  amount_total_signed_string) " +
      " VALUES ('/', ?, ?, 'draft', 'entry', false, ? , " +
      " ?, ?, 0, 0, ?,  " +
      " 0, 0, 0, ?, " +
      " 0, false,  11 , false, " +
      " 11 , now(), 11 , now(), 1,  trim(to_char(? ,'999,999,999,999,990D99')))"

    DSL.using(dsl.configuration()).transaction(c =>{

      val insertCount =  c.dsl().query(sql , new java.sql.Timestamp(entry.getTransactionDate.getTime) ,ref ,
        journalId ,companyId ,currencyId,entry.getTotalDebit
        , entry.getTotalDebit, entry.getTotalDebit).execute()

      Assert.isTrue(insertCount == 1, "Insert count != 1, count: " + insertCount)

    })


  }

  def insertJournalItem(moveId: Integer, transactionDate: Date, ref: String,
                        journalId: Integer, companyId: Integer, currencyId: Integer,
                        partnerId: Integer, accountId: Integer,
                        internalType: String, rootId: Integer, classify1: String,
                        classify2: String, classify3: String, entryItem: JournalEntryItem,
                        sequence:Integer, accountName:String): Unit = {


    val sql = "INSERT INTO account_move_line( " +
      " move_id, move_name, date, ref, parent_state, journal_id, company_id, company_currency_id, " +
      " account_id, account_internal_type, account_root_id, sequence, name, quantity, price_unit, discount," +
      "  debit, credit, balance, reconciled, blocked," +
      " partner_id,  tax_exigible, tax_audit, amount_residual, amount_residual_currency,  " +
      " create_uid, create_date, write_uid, write_date, asset_mrr, classify1, classify2, classify3, " +
      " market, type_1, type_2, department, chart_account_code, " +
      " credit_string, debit_string, " +
      " account_code_name) " +
      " VALUES (?, '/', ?, ?, 'draft', ?, ?, ?, " +
      " ?, ?, ?, ?, ?, 1, ?, 0,  " +
      " ?, ?, ?, false, false, " +
      " ?, true, '', ?, 0,  " +
      " 11, now(), 11, now(), 0, ?, ?, ?,  " +
      " ?, ?, ?, ?, ?, " +
      " trim(to_char(?, '999,999,999,999,990D99')), trim(to_char(?, '999,999,999,999,990D99')), " +
      " ?)"


    DSL.using(dsl.configuration()).transaction(c =>{

      var priceUnit = java.math.BigDecimal.ZERO
      var balance  = java.math.BigDecimal.ZERO
      var amountResidual = java.math.BigDecimal.ZERO

      if (entryItem.getDebit.compareTo(BigDecimal.ZERO) > 0) {
        priceUnit = entryItem.getDebit
        balance = entryItem.getDebit
      }
      else {
        priceUnit = entryItem.getCredit
        balance = entryItem.getCredit.multiply(BigDecimal.valueOf(-1))
      }

      if(!internalType.equals("other")) {
        amountResidual = balance
      }

      println(sql)

      val insertCount =  c.dsl().query(sql ,
        moveId, new Timestamp(transactionDate.getTime) , ref , journalId,companyId,currencyId,
        accountId, internalType, rootId, sequence, entryItem.getLabel, priceUnit,
        entryItem.getDebit, entryItem.getCredit, balance,
        partnerId, amountResidual,
        classify1, classify2, classify3,
        entryItem.getMarket, entryItem.getType1,entryItem.getType2, entryItem.getDepartment, entryItem.getAccountCode,
        entryItem.getCredit, entryItem.getDebit, accountName).execute()

      Assert.isTrue(insertCount == 1, "Insert count != 1, count: " + insertCount)


    })

  }

  def queryRefJournalEntry(transactionDate: Date): String = {

    val sql = "SELECT ref  FROM account_move  where date = ? " +
      " ORDER BY ref desc limit 10"


    val resultList: Result[Record] = dsl.fetch(sql , new Timestamp(transactionDate.getTime))

    if (resultList.isEmpty) return new SimpleDateFormat("yyyyMMdd").format(transactionDate) + "001"
    val ref = resultList.get(0).getValue(0).toString
    val refLong = ref.toLong + 1
    String.valueOf(refLong)


  }

  def queryJournalIdSettlement: Integer = {

    val sql = "SELECT id FROM account_journal where name = 'Settlement biweekly'"

    val resultList: Result[Record] = dsl.fetch(sql)

    if (resultList.isEmpty) 0 else Integer.parseInt(resultList.get(0).getValue(0).toString)


  }


  def queryCurrencyIdTWD: Integer = {

    val sql = "SELECT id FROM res_currency WHERE name = 'TWD'"

    val resultList: Result[Record] = dsl.fetch(sql)

    if (resultList.isEmpty) 0 else resultList.get(0).getValue(0).toString.toInt
  }

  def queryPartnerIdDataMigrator: Integer = {

    val sql = "SELECT id FROM res_partner  WHERE name = 'DataMigrator'"
    val resultList: Result[Record] = dsl.fetch(sql)

    if (resultList.isEmpty) 0 else resultList.get(0).getValue(0).toString.toInt

  }

   def queryCompanyIdKindMinds: Integer = {
    val sql = "SELECT id  FROM res_company WHERE name = 'KindMinds'"
    val resultList: Result[Record] = dsl.fetch(sql)

    if (resultList.isEmpty) 0 else resultList.get(0).getValue(0).toString.toInt
  }


   def queryAccountMoveIdByRef(ref: String): Integer = {

    val sql = "SELECT id  FROM  account_move WHERE ref = ? "

    val resultList: Result[Record] = dsl.fetch(sql , ref)

    if (resultList.isEmpty) 0 else resultList.get(0).getValue(0).toString.toInt
  }

  def queryPartnerIdByCode(companyCode: String): Integer = {
    val sql = "SELECT id  FROM res_partner  WHERE k_code = ?"

    val resultList: Result[Record] = dsl.fetch(sql , companyCode)

    if (resultList.isEmpty) 0 else resultList.get(0).getValue(0).toString.toInt
  }

  def queryPartnerIdByName(partnerName: String): Integer = {
    val sql = "SELECT id  FROM res_partner  WHERE name = ? "

    val resultList: Result[Record] = dsl.fetch(sql , partnerName)

    if (resultList.isEmpty) 0 else resultList.get(0).getValue(0).toString.toInt
  }

  def queryAccountCodeInfo(accountCode: String): AccountCodeInfo = {

    val sql = "SELECT id, internal_type, root_id, classify1, classify2, classify3, name " +
      "FROM account_account " +
      " where code = ? "


    val resultList: util.List[AccountCodeInfo] = dsl.fetch(sql , accountCode).map(r =>{
      AccountCodeInfo(r.get(0).toString.toInt, r.get(1).toString,r.get(2).toString.toInt,
        r.get(3).toString,r.get(4).toString, r.get(5).toString, r.get(6).toString)
    })

    if (resultList.isEmpty)  null else resultList.get(0)
  }


}



