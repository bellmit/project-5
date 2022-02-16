package com.kindminds.drs.persist.data.access.usecase.accounting;

import com.kindminds.drs.Country;
import com.kindminds.drs.Currency;
import com.kindminds.drs.TransactionLineType;
import com.kindminds.drs.api.data.access.usecase.accounting.ViewSs2spStatementDao;
import com.kindminds.drs.enums.BillStatementType;

import com.kindminds.drs.persist.data.access.rdb.Dao;
import com.kindminds.drs.api.v1.model.accounting.StatementLineType;
import com.kindminds.drs.api.v1.model.report.RemittanceReport.RemittanceReportItem;
import com.kindminds.drs.api.v1.model.report.Ss2spCustomerCareCostReport.Ss2spCustomerCareCostReportItem;
import com.kindminds.drs.api.v1.model.report.Ss2spServiceExpenseReport.Ss2spServiceExpenseReportItem;
import com.kindminds.drs.api.v1.model.report.Ss2spSettleableItemReport.Ss2spSettleableItemReportLineItem;
import com.kindminds.drs.api.v1.model.report.Ss2spStatementReport.Ss2spStatementItemProfitShare;
import com.kindminds.drs.api.v1.model.report.Ss2spStatementReport.Ss2spStatementItemServiceExpense;
import com.kindminds.drs.api.v1.model.report.Ss2spStatementReport.Ss2spStatementItemShipmentRelated;
import com.kindminds.drs.enums.AmazonTransactionType;
import com.kindminds.drs.persist.v1.model.mapping.accounting.RemittanceReportItemImpl;
import com.kindminds.drs.persist.v1.model.mapping.statement.*;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;




import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;
import java.util.Map.Entry;

@Repository
public class ViewSs2spStatementDaoImpl extends Dao implements ViewSs2spStatementDao {
	

	@Override
	public String queryRcvrKcode(String name) {
		StringBuilder sqlSb = new StringBuilder()
				.append("select rcvr.k_code ")
				.append("from bill_statement bs ")
				.append("inner join company rcvr on rcvr.id=bs.receiving_company_id ")
				.append("where bs.name=:name ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("name", name);
		return getNamedParameterJdbcTemplate().queryForObject(sqlSb.toString(),q,String.class);
	}

	@Override @SuppressWarnings("unchecked") 
	public Object [] queryInfo(BillStatementType type,String name){
		String sql = "select bs.period_start as start, bs.period_end as end, isur.k_code as isur_kcode, rcvr.k_code as rcvr_kcode "
				+ "from "+type.getDbTableStatement()+" bs "
				+ "inner join company isur on isur.id = bs.issuing_company_id "
				+ "inner join company rcvr on rcvr.id = bs.receiving_company_id "
				+ "where bs.name = :name ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("name", name);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		Assert.isTrue(columnsList.size()==1);
		Object[] columns = columnsList.get(0);
		return  columns;
	}

	@Override
	public Boolean queryIsCompanyBiSettlement(String companyCode) {
		String sql = "SELECT bisettlement from company " +
				" where k_code = :companyCode ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("companyCode", companyCode);
		return getNamedParameterJdbcTemplate().queryForObject(sql,q,Boolean.class);
	}

	@Override
	public Boolean queryIsPeriodBisettlement(Date periodStart) {
		String sql = "SELECT bisettlement FROM settlement_period " +
				" WHERE period_start = :periodStart ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("periodStart", periodStart);
		return getNamedParameterJdbcTemplate().queryForObject(sql,q,Boolean.class);
	}

	@Override
	public int queryStatementVersionNumber(BillStatementType type,String statementName) {
		String sql = "select version_number from "+type.getDbTableStatement()+" bs "
				+ "where bs.name = :statementName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		Integer o = getNamedParameterJdbcTemplate().queryForObject(sql,q,Integer.class);
		if (o == null) return 0;
		return o;
	}
	
	@Override @SuppressWarnings("unchecked")
	public String queryClassOfTransactionLineType(String name) {
		String sql = "select class from transactionlinetype tlt where tlt.name = :name ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("name", name);
		List<String> classList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		Assert.isTrue(classList.size()==1,"No or more than one item found.");
		return classList.get(0);
	}

	@Override @SuppressWarnings("unchecked")
	public TransactionLineType querySettleableItem(int sid) {
		String sql = "select tlt.name from transactionlinetype tlt where tlt.id = :sid ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("sid", sid);
		List<String> kCodeList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		Assert.isTrue(kCodeList.size()==1,"No or more than one kcode found.");
		return TransactionLineType.fromName(kCodeList.get(0));
	}
	
	@Override @SuppressWarnings("unchecked")
	public BigDecimal queryPreviousBalance(BillStatementType type,String statementName) {
		String sql = "select previous_balance from "+type.getDbTableStatement()+" bs where bs.name = :statementName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		List<BigDecimal> rmtList = getNamedParameterJdbcTemplate().queryForList(sql,q,BigDecimal.class);
		Assert.isTrue(rmtList.size()==1,"No or more than one kcode found.");
		return rmtList.get(0);
	}
	@Override @SuppressWarnings("unchecked")
	public BigDecimal queryRemittanceIsurToRcvr(BillStatementType type,String statementName) {
		String sql = "select remittance_sent from "+type.getDbTableStatement()+" bs where bs.name = :statementName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		List<BigDecimal> rmtList =  getNamedParameterJdbcTemplate().queryForList(sql,q,BigDecimal.class);
		Assert.isTrue(rmtList.size()==1,"No or more than one kcode found.");
		return rmtList.get(0);
	}
	
	@Override @SuppressWarnings("unchecked")
	public BigDecimal queryRemittanceRcvrToIsur(BillStatementType type,String statementName) {
		String sql = "select remittance_received from "+type.getDbTableStatement()+" bs where bs.name = :statementName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		List<BigDecimal> rmtList =  getNamedParameterJdbcTemplate().queryForList(sql,q,BigDecimal.class);
		Assert.isTrue(rmtList.size()==1,"No or more than one kcode found.");
		return rmtList.get(0);
	}
	
	@Override @SuppressWarnings("unchecked")
	public BigDecimal queryBalance(BillStatementType type,String statementName) {
		String sql = "select balance from "+type.getDbTableStatement()+" bs where bs.name = :statementName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		List<BigDecimal> rmtList =  getNamedParameterJdbcTemplate().queryForList(sql,q,BigDecimal.class);
		Assert.isTrue(rmtList.size()==1,"No or more than one kcode found.");
		return rmtList.get(0);
	}
	
	@Override
	public BigDecimal queryServiceExpenseTaxRate(String domesticTransactionInvoice) {
		String sql = "select distinct(tax_rate) from domestic_transaction dt where dt.invoice_number = :domesticTransactionInvoice ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("domesticTransactionInvoice", domesticTransactionInvoice);
		BigDecimal taxRate = getNamedParameterJdbcTemplate().queryForObject(sql,q,BigDecimal.class);
		return taxRate;
	}

	@Override
	public BigDecimal queryAllServiceExpenseTaxRate(String statementName) {
		String sql = "select distinct(dt.tax_rate) "
				+ "from domestic_transaction dt "
				+ "inner join bill_statement bst on bst.period_start <= dt.transaction_date and bst.period_end >= dt.transaction_date "
				+ "where bst.name = :statementName";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		BigDecimal taxRate = getNamedParameterJdbcTemplate().queryForObject(sql,q,BigDecimal.class);
		return taxRate;
	}

	@Override @SuppressWarnings("unchecked")
	public BigDecimal queryProfitShareTaxV2(BillStatementType type,String statementName) {
		String sql = "select statement_amount from "+type.getDbTableStatementLineItem()+" bsli "
				+ "inner join "+type.getDbTableStatement()+" bs on bs.id = bsli.statement_id "
				+ "inner join transactionlinetype tlt on tlt.id = bsli.transactionlinetype_id "
				+ "where bs.name = :statementName and tlt.name = :ssiVatName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		q.addValue("ssiVatName", TransactionLineType.SSI_VAT.getName());
		List<BigDecimal> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,BigDecimal.class);
		Assert.isTrue(resultList.size()==1||resultList.size()==0);
		if(resultList.size()==0) return null;
		return resultList.get(0);
	}
	
	@Override @SuppressWarnings("unchecked")
	public Currency queryStatementCurrency(BillStatementType type,String statementId) {
		String sql = "select c.name "
				+ "from "+type.getDbTableStatement()+" bs "
				+ "inner join currency c on bs.currency_id=c.id "
				+ "where bs.name = :statementId ";

		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementId", statementId);
		List<String> resultList= getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		Assert.isTrue(resultList.size()==1,"More than one Currency or No Currency found.");
		return Currency.valueOf(resultList.get(0));
	}
	
	private final String queryStatementSqlPartOfSelect = "select bs.name as statement_id, "
			+ "                   to_char( bs.period_start at time zone :tz,:fm) as period_start_utc, "
			+ "to_char((bs.period_end-interval '1 second') at time zone :tz,:fm) as period_end_utc, "
			+ "                                                  bs.currency_id as currency_id, "
			+ "                                                        bs.total as total, "
			+ "                                                      bs.balance as balance, "
			+ "                                                        bs.invoice_from_ssdc as invoice_from_ssdc, "
			+ "                                                        bs.invoice_from_supplier as invoice_from_supplier ";
	

	
	@Override @SuppressWarnings("unchecked")
	public List<Object []> queryNewestStatementsSentByDrsCompany(BillStatementType type,String drsCompanyKcode) {
		String sql = this.queryStatementSqlPartOfSelect
				+ "from "+type.getDbTableStatement()+" as bs "
				+ "inner join company isur on isur.id = bs.issuing_company_id "
				+ "inner join company rcvr on rcvr.id = bs.receiving_company_id "
				+ "where isur.k_code = :drsCompanyKcode and rcvr.is_supplier = TRUE and bs.currency_id = isur.currency_id "
				+ "and bs.period_start = ("
				+ "    select max(bs.period_start) from "+type.getDbTableStatement()+" bs "
				+ "    inner join company isur on isur.id = bs.issuing_company_id "
				+ "    inner join company rcvr on rcvr.id = bs.receiving_company_id "
				+ "    where isur.k_code = :drsCompanyKcode and rcvr.is_supplier = TRUE "
				+ ")"
				+ "order by bs.period_start desc, bs.name ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("tz", "UTC");
		q.addValue("fm","YYYY-MM-DD HH24:MI:SS");
		q.addValue("drsCompanyKcode", drsCompanyKcode);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		return columnsList;
	}
	
	@Override @SuppressWarnings("unchecked")
	public List<Object []> queryStatementsSentByDrsCompany(BillStatementType type,String drsCompanyKcode) {
		String sql = this.queryStatementSqlPartOfSelect
				+ "from "+type.getDbTableStatement()+" as bs "
				+ "inner join company isur on isur.id = bs.issuing_company_id "
				+ "inner join company rcvr on rcvr.id = bs.receiving_company_id "
				+ "where isur.k_code = :drsCompanyKcode and rcvr.is_supplier = TRUE and bs.currency_id = isur.currency_id "
				+ "order by bs.period_start desc, bs.name ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("tz", "UTC");
		q.addValue("fm","YYYY-MM-DD HH24:MI:SS");
		q.addValue("drsCompanyKcode", drsCompanyKcode);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		return columnsList;
	}
	
	@Override @SuppressWarnings("unchecked")
	public List<Object []> queryStatementsReceivedBySupplier(BillStatementType type,String supplierKcode) {
		String sql = this.queryStatementSqlPartOfSelect
				+ "from "+type.getDbTableStatement()+" as bs "
				+ "inner join currency c on c.id = bs.currency_id "
				+ "inner join company rcvr on rcvr.id = bs.receiving_company_id "
				+ "where rcvr.k_code = :supplierKcode and rcvr.is_supplier = TRUE and bs.currency_id = rcvr.currency_id "
				+ "order by bs.period_start desc, bs.name ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("tz", "UTC");
		q.addValue("fm","YYYY-MM-DD HH24:MI:SS");
		q.addValue("supplierKcode", supplierKcode);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		return columnsList;
	}
	
	@Override @SuppressWarnings("unchecked")
	public List<Object []> queryDraftStatements(String drsCompanyKcode) {
		String sql = this.queryStatementSqlPartOfSelect
				+ "from draft_bill_statement bs "
				+ "inner join company isur on isur.id = bs.issuing_company_id "
				+ "inner join company rcvr on rcvr.id = bs.receiving_company_id "
				+ "where isur.k_code = :drsCompanyKcode and rcvr.is_supplier = TRUE and bs.currency_id = isur.currency_id "
				+ "order by bs.period_start desc, bs.name ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("tz", "UTC");
		q.addValue("fm","YYYY-MM-DD HH24:MI:SS");
		q.addValue("drsCompanyKcode", drsCompanyKcode);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		return columnsList;
	}

	@Override @SuppressWarnings("unchecked")
	public List<Ss2spStatementItemProfitShare> queryStatementItemProfitShareV2(BillStatementType type,String statementId) {
		String sql = "select      slit.name as name, "
				+ "           currency.name as currency_name, "
				+ "   sum(statement_amount) as amount "
				+ "from "+type.getDbTableStatementLineItem()+" bsli "
				+ "inner join "+type.getDbTableStatement()+" bs on bs.id=bsli.statement_id "
				+ "inner join statement_line_type slit on slit.id=bsli.stlmnt_line_item_type_id "
				+ "inner join currency on currency.id = bsli.statement_currency_id "
				+ "where bs.name=:statementId ";
		String foreignLineTypesClause = "'"+StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP.getValue()+"'";
		sql +=    "and slit.name in ( "+foreignLineTypesClause+" ) "
				+ "group by slit.name, currency.name ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementId", statementId);
		return (List) getNamedParameterJdbcTemplate().query(sql,q,(rs,rowNum) -> new Ss2spStatementItemProfitShareImpl(
				rs.getString("name"),rs.getBigDecimal("amount"),rs.getString("currency_name")
		));
	}

	/*
	@Override
	public List<Ss2spStatementItemProfitShare> queryStatementItemProfitShareV3(BillStatementType type,String statementName) {
		List<Ss2spStatementItemProfitShare> resultList = new ArrayList<Ss2spStatementItemProfitShare>();
		if(this.queryProfitShareItemCounts(type,statementName)>0){
			Ss2spStatementItemProfitShareImplV3 item = new Ss2spStatementItemProfitShareImplV3();
			item.setCurrency(this.queryProfitShareStatementCurrency(type,statementName));
			item.setAmountUntaxed(this.querySumOfProfitShareStatementAmountUntaxed(type,statementName));
			item.setAmountTax(this.queryProfitShareTaxV3(type,statementName));
			resultList.add(item);
		}
		return resultList;
	}
	*/
	
	@SuppressWarnings("unchecked")
	@Override
	public int queryProfitShareItemCounts(BillStatementType type,String statementName){
		String sql = "select count(*) from "+type.getDbTableStatementProfitShareItem()+" bspi "
				+ "inner join "+type.getDbTableStatement()+" bs on bs.id = bspi.statement_id "
				+ "where bs.name = :statementName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		List<BigInteger> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,BigInteger.class);
		Assert.isTrue(resultList.size()==1);
		return resultList.get(0).intValue();
	}
	
	@SuppressWarnings("unchecked")
	public Currency queryProfitShareStatementCurrency(BillStatementType type,String statementName){
		String sql = "select distinct statement_currency_id from "+type.getDbTableStatementProfitShareItem()+" bspi "
				+ "inner join "+type.getDbTableStatement()+" bs on bs.id = bspi.statement_id "
				+ "where bs.name = :statementName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		List<Integer> currencyIdList =  getNamedParameterJdbcTemplate().queryForList(sql,q,Integer.class);
		Assert.isTrue(currencyIdList.size()==1);
		Assert.notNull(currencyIdList.get(0));
		return Currency.fromKey(currencyIdList.get(0));
	}
	
	@SuppressWarnings("unchecked")
	public BigDecimal querySumOfProfitShareStatementAmountUntaxed(BillStatementType type,String statementName){
		String sql = "select sum(statement_amount_untaxed) from "+type.getDbTableStatementProfitShareItem()+" bspi "
				+ "inner join "+type.getDbTableStatement()+" bs on bs.id = bspi.statement_id "
				+ "where bs.name = :statementName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		List<BigDecimal> resultList =  getNamedParameterJdbcTemplate().queryForList(sql,q,BigDecimal.class);
		Assert.isTrue(resultList.size()==1);
		Assert.notNull(resultList.get(0));
		return resultList.get(0);
	}
	
	@SuppressWarnings("unchecked")
	public BigDecimal queryProfitShareTaxV3(BillStatementType type,String statementName){
		String sql = "select statement_amount from "+type.getDbTableStatementLineItem()+" bsli "
				+ "inner join "+type.getDbTableStatement()+" bs on bs.id = bsli.statement_id "
				+ "inner join statement_line_type slt on slt.id = bsli.stlmnt_line_item_type_id "
				+ "inner join transactionlinetype tlt on tlt.id = bsli.transactionlinetype_id "
				+ "where bs.name = :statementName "
				+ "and slt.name = :ss2spProfitShareName "
				+ "and tlt.name = :vatTaxName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		q.addValue("ss2spProfitShareName", StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP.getValue());
		q.addValue("vatTaxName", TransactionLineType.SSI_VAT.getName());
		List<BigDecimal> resultList =  getNamedParameterJdbcTemplate().queryForList(sql,q,BigDecimal.class);
		Assert.isTrue(resultList.size()==1);
		Assert.notNull(resultList.get(0));
		return resultList.get(0);
	}
	
	@Override @SuppressWarnings("unchecked")
	public List<Ss2spStatementItemShipmentRelated> queryStatementItemsShipmentRelated(BillStatementType type,String statementId) {
		String sql = "select    bsli.reference as shipment_name, "
				+ "         shp.invoice_number as shipment_invoice, "
				+ " bsli.statement_currency_id as currency_id, "
				+ " sum(bsli.statement_amount) as amount "
				+ "from "+type.getDbTableStatementLineItem()+" bsli "
				+ "inner join shipment shp on shp.name = bsli.reference "
				+ "inner join statement_line_type slit on slit.id=bsli.stlmnt_line_item_type_id "
				+ "inner join           product_sku ps on ps.id=bsli.product_sku_id "
				+ "inner join "+type.getDbTableStatement()+" bs on bs.id=bsli.statement_id "
				+ "where bs.name=:statementId "
				+ "and slit.name in ( :ss2spPaymentType, :ss2spRefundType ) "
				+ "group by bsli.reference, shp.invoice_number, bsli.statement_currency_id "
				+ "order by bsli.reference ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementId", statementId);
		q.addValue("ss2spPaymentType", StatementLineType.SS2SP_PRODUCT_INVENTORY_PAYMENT.getValue());
		q.addValue("ss2spRefundType", StatementLineType.SS2SP_PRODUCT_INVENTORY_REFUND.getValue());

		return  (List) getNamedParameterJdbcTemplate().query(sql,q,(rs,rowNum) -> new Ss2spStatementItemShipmentRelatedImpl(
				rs.getString("shipment_name"),rs.getString("shipment_invoice"),rs.getInt("currency_id"),rs.getBigDecimal("amount")
		));
	}
	
	@Override @SuppressWarnings("unchecked")
	public List<Object []> queryStatementItemsSellBackRelated(BillStatementType type,String statementName) {
		String sql = "select bsli.statement_currency_id as currency_id, "
				+ "          sum(bsli.statement_amount) as amount, "
				+ "        bsli.temp_column_for_invoice as invoice_number "
				+ "from "+type.getDbTableStatementLineItem()+" bsli "
				+ "inner join "+type.getDbTableStatement()+" bs on bs.id = bsli.statement_id "
				+ "inner join statement_line_type slit on slit.id = bsli.stlmnt_line_item_type_id "
				+ "where bs.name = :statementName "
				+ "and slit.name in (:ss2spSellBackType) "
				+ "group by bsli.temp_column_for_invoice, bsli.statement_currency_id ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		q.addValue("ss2spSellBackType", StatementLineType.getSellbackTypes());
//		List<Object[]> resultObjectArrayList = q.getResultList();
//		if(resultObjectArrayList.isEmpty()) return null;


		return getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
	}

	@Override @SuppressWarnings("unchecked")
	public List<Ss2spStatementItemServiceExpense> queryStatementItemServiceExpense(BillStatementType type,String statementId) {
		String sql = "select         slt.name as name, "
				+ "bsli.statement_currency_id as currency_id, "
				+ "     sum(statement_amount) as amount, "
				+ "         dt.invoice_number as invoice_number "
				+ "from "+type.getDbTableStatementLineItem()+" bsli "
				+ "inner join "+type.getDbTableStatement()+" bs on bs.id=bsli.statement_id "
				+ "inner join statement_line_type slt on bsli.stlmnt_line_item_type_id=slt.id "
				+ "inner join domestic_transaction dt on dt.id\\:\\:varchar = bsli.reference "
				+ "where bs.name=:statementId "
				+ "and slt.name = :serviceSaleType "
				+ "group by slt.name, bsli.transactionlinetype_id, bsli.statement_currency_id, dt.invoice_number "
				+ "order by dt.invoice_number ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementId", statementId);
		q.addValue("serviceSaleType", StatementLineType.SS_SERVICE_SALE_FOR_SP.getValue());

		return (List) getNamedParameterJdbcTemplate().query(sql,q,(rs,rowNUm) -> new Ss2spStatementItemServiceExpenseImpl(
				rs.getString("name"),rs.getInt("currency_id"),rs.getBigDecimal("amount"),rs.getString("invoice_number")
		));
	}
	
	@Override @SuppressWarnings("unchecked")
	public List<Object []> queryProfitShareReportLineItemV1(BillStatementType type,String statementId) {
		String sql="select         country.id as source_country_id, "
				+ "          orig_currency.id as source_currency_id, "
				+ " sum(round(bsli.original_amount,orig_currency.rounding_scale)) as source_amount, "
				+ "          stmt_currency.id as statement_currency_id, "
				+ "sum(bsli.statement_amount) as statement_amount "
				+ "from "+type.getDbTableStatementLineItem()+" bsli "
				+ "inner join "+type.getDbTableStatement()+" bs on bs.id=bsli.statement_id "
				+ "inner join country on bsli.country_id = country.id "
				+ "inner join currency orig_currency on orig_currency.id = bsli.original_currency_id "
				+ "inner join currency stmt_currency on stmt_currency.id = bsli.statement_currency_id "
				+ "inner join statement_line_type slt on slt.id=bsli.stlmnt_line_item_type_id "
				+ "where bs.name = :statementId "
				+ "and slt.name = :sltName "
				+ "group by country.id,orig_currency.id,stmt_currency.id ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementId", statementId);
		q.addValue("sltName", StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP.getValue());
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);

		return columnsList;
	}
	
	@Override @SuppressWarnings("unchecked")
	public List<Object []> queryProfitShareReportLineItemV3(BillStatementType type,String statementName) {
		String sql = "select      rr.revenue_in_usd as revenue_in_usd, "
				+ "                         rr.rate as effective_retainment_rate, "
				+ "          bspi.source_country_id as source_country_id, "
				+ "         bspi.source_currency_id as source_currency_id, "
				+ "      bspi.source_amount_untaxed as source_amount, "
				+ "      bspi.statement_currency_id as statement_currency_id, "
				+ "   bspi.statement_amount_untaxed as statement_amount, "
				+ "              bspi.exchange_rate as exchange_rate "
				+ "from "+type.getDbTableStatementProfitShareItem()+" bspi "
				+ "inner join "+type.getDbTableStatement()+" bs on bs.id = bspi.statement_id "
				+ "left join retainment_rate rr on (rr.date_start=bs.period_start and rr.date_end=bs.period_end and rr.supplier_company_id=bs.receiving_company_id and rr.country_id=bspi.source_country_id) "
				+ "where bs.name = :statementName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);

		return columnsList;
	}

	@Override @SuppressWarnings("unchecked")
	public List<Object []> querySkuProfitShareItems(BillStatementType type,String statementName,Country country, List<TransactionLineType> relatedItems) {
		String sql = "select ps.code_by_drs as sku, "
				+ "      p.name_by_supplier as sku_name, "
				+ "sum(case when tlt.name =  :shippedItems then bsli.quantity else 0 end) as  shipped_quantity, "
				+ "sum(case when tlt.name = :refundedItems then bsli.quantity else 0 end) as refunded_quantity, "
				+ "sum(bsli.original_amount) "
				+ "from "+type.getDbTableStatementLineItem()+" bsli "
				+ "inner join "+type.getDbTableStatement()+" bs on bs.id = bsli.statement_id "
				+ "inner join product_sku ps on ps.id = bsli.product_sku_id "
				+ "inner join product p on p.id = ps.product_id "
				+ "inner join transactionlinetype tlt on tlt.id = bsli.transactionlinetype_id "
				+ "where bs.name = :statementName "
				+ "and bsli.country_id = :country_id "
				+ "and tlt.name in (:relatedItems) "
				+ "group by ps.code_by_drs, p.name_by_supplier order by ps.code_by_drs ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		q.addValue("country_id", country.getKey());
		q.addValue("shippedItems", TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_ADDITION.getName());
		q.addValue("refundedItems", TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_SUBTRACTION.getName());
		q.addValue("relatedItems", this.toStringList(relatedItems));
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);

		return columnsList;
	}

	@Override @SuppressWarnings("unchecked")
	public List<Object []> queryProfitShareDetailItemsV1(BillStatementType type,String statementName, Country country,List<TransactionLineType> excludeTltNames) {
		String sql = "select  ps.code_by_drs as related_sku, "
				+ "       p.name_by_supplier as sku_name, "
				+ "                 tlt.name as item_name, "
				+ "     bsli.original_amount as amount "
				+ "from "+type.getDbTableStatementLineItem()+" bsli "
				+ "inner join "+type.getDbTableStatement()+" bs on bs.id = bsli.statement_id "
				+ "inner join product_sku ps on bsli.product_sku_id = ps.id "
				+ "inner join product p on p.id = ps.product_id "
				+ "inner join statement_line_type slt on slt.id = bsli.stlmnt_line_item_type_id "
				+ "inner join transactionlinetype tlt on tlt.id = bsli.transactionlinetype_id "
				+ "where bs.name = :statementName "
				+ "and slt.name = :sltName "
				+ "and bsli.country_id = :countryId "
				+ "and bsli.original_currency_id = :currencyId "
				+ "and tlt.name not in (:excludeTltNames) "
				+ "order by ps.code_by_drs ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		q.addValue("sltName", StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP.getValue());
		q.addValue("countryId", country.getKey());
		q.addValue("currencyId", country.getCurrency().getKey());
		q.addValue("excludeTltNames", this.toStringList(excludeTltNames));
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		//return (List<Ss2spProfitShareDetail>)((List<?>)this.toSs2spProfitShareDetailListV1(columnsList));
		return  columnsList;
	}
	
	@Override @SuppressWarnings("unchecked")
	public List<Object []> queryProfitShareDetailItemsV3(BillStatementType type,String statementName, Country country,List<TransactionLineType> excludeTltNames) {
		String sql = "select  ps.code_by_drs as sku, "
				+ "       p.name_by_supplier as sku_name, "
				+ "                 tlt.name as item_name, "
				+ "     sum(original_amount) as amount "
				+ "from "+type.getDbTableStatementLineItem()+" bsli "
				+ "inner join "+type.getDbTableStatement()+" bs on bs.id=bsli.statement_id "
				+ "inner join product_sku ps on bsli.product_sku_id=ps.id "
				+ "inner join product p on p.id = ps.product_id "
				+ "inner join currency on bsli.original_currency_id = currency.id "
				+ "inner join statement_line_type slt on slt.id=bsli.stlmnt_line_item_type_id "
				+ "inner join transactionlinetype tlt on tlt.id=bsli.transactionlinetype_id "
				+ "where bs.name=:statementId "
				+ "and slt.name= :sltName "
				+ "and bsli.country_id = :countryId "
				+ "and bsli.original_currency_id = :currencyId "
				+ "and tlt.name not in (:excludeTltNames) "
				+ "group by ps.code_by_drs, p.name_by_supplier, tlt.name, currency.name, tlt.id "
				+ "order by ps.code_by_drs ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementId", statementName);
		q.addValue("sltName", StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP.getValue());
		q.addValue("countryId", country.getKey());
		q.addValue("currencyId", country.getCurrency().getKey());
		q.addValue("excludeTltNames", this.toStringList(excludeTltNames));
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		return columnsList;
		//return (List<Ss2spProfitShareDetail>)((List<?>)this.toSs2spProfitShareDetailListV1(columnsList));
	}

	/*
	private List<Ss2spProfitShareDetailImplV1> toSs2spProfitShareDetailListV1(List<Object[]> columnsList){
		Map<String,Ss2spProfitShareDetailImplV1> skuToDetailItemMap = new TreeMap<String,Ss2spProfitShareDetailImplV1>();
		for(Object[] columns:columnsList){
			String sku = (String)columns[0];
			String skuName = (String)columns[1];
			String itemName = (String)columns[2];
			BigDecimal amount = (BigDecimal)columns[3];
			if(!skuToDetailItemMap.containsKey(sku)){
				skuToDetailItemMap.put(sku, new Ss2spProfitShareDetailImplV1(sku,skuName,new TreeMap<String,BigDecimal>()));
			}
			skuToDetailItemMap.get(sku).getRawItemNameToAmountMap().put(itemName,amount);
		}
		return new ArrayList<Ss2spProfitShareDetailImplV1>(skuToDetailItemMap.values());
	}*/

	@Override @SuppressWarnings("unchecked")
	public List<Entry<String,BigDecimal>> queryProfitShareItemsAmountExcludedRetailAndInternational(BillStatementType type,String statementName, Country country,List<String> itemNames) {
		String sql ="select tlt.name, sum(bsli.original_amount) "
				+ "from "+type.getDbTableStatementLineItem()+" bsli "
				+ "inner join "+type.getDbTableStatement()+" bs on bs.id = bsli.statement_id "
				+ "inner join transactionlinetype tlt on tlt.id = bsli.transactionlinetype_id "
				+ "where bs.name = :statementName "
				+ "and bsli.country_id = :country_id "
				+ "and tlt.name in (:itemNames) "
				+ "group by tlt.name ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		q.addValue("country_id",country.getKey());
		q.addValue("itemNames", itemNames);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		return this.createItemAmountEntryList(columnsList);
	}

	@Override
	public List<Entry<String,BigDecimal>> queryIvsForImportDutyTransaction(BillStatementType type, String statementName, Country country) {
		String sql1 ="select  tlt.name, sum(bsli.original_amount) "
				+ "from "+type.getDbTableStatementLineItem()+" bsli "
				+ "inner join "+type.getDbTableStatement()+" bs on bs.id = bsli.statement_id "
				+ "inner join transactionlinetype tlt on tlt.id = bsli.transactionlinetype_id "
				+ "where bs.name = :statementName "
				+ "and bsli.country_id = :country_id "
				+ "and tlt.id=1 "
				+ "group by tlt.name ";
		String sql2 ="select distinct ivs.name "
				+ "from import_duty_transaction idt "
				+ "inner join shipment uns on uns.id = idt.shipment_uns_id "
				+ "inner join import_duty_transaction_line_item  idtl on idt.id = idtl.transaction_id "
				+ "inner join shipment_line_item  unsline  on unsline.id = idtl.shipment_uns_line_item_id "
				+ "inner join shipment ivs on ivs.id = unsline.source_shipment_id "
				+ "inner join "+type.getDbTableStatementLineItem()+" bsli on bsli.product_sku_id = unsline.sku_id "
				+ "inner join "+type.getDbTableStatement()+" bs on bs.id = bsli.statement_id "
				+ "where bsli.transactionlinetype_id = 1 "
				+ "and  bs.name= :statementName "
				+ "and bsli.country_id = :country_id "
				+ "and uns.actual_destination_id = :country_id "
				+ "and idt.transaction_date > bs.period_start "
				+ "and idt.transaction_date < bs.period_end ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		q.addValue("country_id",country.getKey());
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql1,q,objArrayMapper);
		List<Object[]> ivsList = getNamedParameterJdbcTemplate().query(sql2,q,objArrayMapper);

		StringBuffer sb= new StringBuffer();
		for(Object[] ivs: ivsList){
			String iv=((String)ivs[0])+" ";
			sb.append(iv);
		}
		String ivsResult="( " + sb.toString() +")";


		for(Object[] columns:columnsList){
			String itemName = (String)columns[0];
			columns[0]=itemName+"&"+ivsResult;

		}

		return this.createItemAmountEntryList(columnsList);
	}
	
	@Override @SuppressWarnings("unchecked")
	public List<Entry<String,BigDecimal>> queryInternationalTransactionItems(BillStatementType type,String statementName, Country country){
		String sql ="select tlt.name, sum(bsli.original_amount) "
				+ "from "+type.getDbTableStatementLineItem()+" bsli "
				+ "inner join "+type.getDbTableStatement()+" bs on bs.id = bsli.statement_id "
				+ "inner join transactionlinetype tlt on tlt.id = bsli.transactionlinetype_id "
				+ "where bs.name = :statementName "
				+ "and bsli.country_id = :country_id "
				+ "and tlt.class = :tltClass "
				+ "group by tlt.name ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		q.addValue("country_id",country.getKey());
		q.addValue("tltClass", "International");
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		return this.createItemAmountEntryList(columnsList);
	}
	
	private List<Entry<String,BigDecimal>> createItemAmountEntryList(List<Object[]> columnsList){
		List<Entry<String,BigDecimal>> resultItemAmountList = new ArrayList<>();
		for(Object[] columns:columnsList){
			String itemName = (String)columns[0];
			BigDecimal amount = (BigDecimal)columns[1];
			resultItemAmountList.add(new AbstractMap.SimpleEntry<>(itemName,amount));
		}
		return resultItemAmountList;
	}

	@Override @SuppressWarnings("unchecked")
	public List<Object []> queryProfitShareDetailV4ofProductSku(BillStatementType type,String statementName,Country country,List<TransactionLineType> excludeTltNames) {
		String sql = "select ps.code_by_drs as product_sku_code, "
				+ "    pps.name_by_supplier as product_sku_name, "
				+ "                tlt.name as item_name, "
				+ "    bsli.original_amount as item_amount "
				+ "from "+type.getDbTableStatementLineItem()+" bsli "
				+ "inner join "+type.getDbTableStatement()+" bs on bs.id = bsli.statement_id "
				+ "inner join product_sku ps on ps.id = bsli.product_sku_id "
				+ "inner join product pps on pps.id = ps.product_id "
				+ "inner join product_base pb on pb.id = ps.product_base_id "
				+ "inner join product ppb on ppb.id = pb.product_id "
				+ "inner join statement_line_type slt on slt.id=bsli.stlmnt_line_item_type_id "
				+ "inner join transactionlinetype tlt on tlt.id=bsli.transactionlinetype_id "
				+ "where bs.name = :statementName "
				+ "and slt.name = :sltName "
				+ "and bsli.country_id = :countryId "
				+ "and bsli.original_currency_id = :currencyId "
				+ "and tlt.name not in (:excludeTltNames) ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		q.addValue("sltName", StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP.getValue());
		q.addValue("countryId", country.getKey());
		q.addValue("currencyId", country.getCurrency().getKey());
		q.addValue("excludeTltNames", this.toStringList(excludeTltNames));
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);

		return columnsList;
	//	return (List<Ss2spProfitShareDetailOfProductSku>)((List<?>)this.toSs2spProfitShareDetailV4ListOfProductSku(columnsList));
	}
	
	private List<String> toStringList(List<TransactionLineType> tlts){
		List<String> result = new ArrayList<>();
		for(TransactionLineType tlt:tlts)
			result.add(tlt.getName());
		return result;
	}

	/*
	private List<Ss2spProfitShareDetailOfProductSkuImplV4> toSs2spProfitShareDetailV4ListOfProductSku(List<Object[]> columnsList){
		Map<String,Ss2spProfitShareDetailOfProductSkuImplV4> skuToDetailItemMap = new TreeMap<String,Ss2spProfitShareDetailOfProductSkuImplV4>();
		for(Object[] columns:columnsList){
			String skuCode = (String)columns[0];
			String skuName = (String)columns[1];
			String itemName = (String)columns[2];
			BigDecimal amount = (BigDecimal)columns[3];
			if(!skuToDetailItemMap.containsKey(skuCode)){
				skuToDetailItemMap.put(skuCode,new Ss2spProfitShareDetailOfProductSkuImplV4(skuCode,skuName,new TreeMap<String,BigDecimal>()));
			}
			skuToDetailItemMap.get(skuCode).getRawItemNameToAmountMap().put(itemName,amount);
		}
		return new ArrayList<Ss2spProfitShareDetailOfProductSkuImplV4>(skuToDetailItemMap.values());
	}
	*/

	@Override @SuppressWarnings("unchecked")
	public List<Object []> queryPaymentAndRefundReportItems(BillStatementType type,String statementName,String sourceIvsName){
		String sql ="select     ps.code_by_drs as sku, "
				+ "         p.name_by_supplier as sku_name, "
				+ "                   slt.name as type, "
                + "         sum(bsli.quantity) as quantity, "
				+ "  bsli.original_currency_id as original_currency_id, "
				+ " sum(bsli.statement_amount) as original_amount, "
				+ "bsli.transactionlinetype_id as transactionlinetype_id "
				+ "from "+type.getDbTableStatementLineItem()+" bsli "
				+ "inner join "+type.getDbTableStatement()+" bs on bs.id=bsli.statement_id "
				+ "inner join product_sku ps on ps.id=bsli.product_sku_id "
				+ "inner join product p on p.id = ps.product_id "
				+ "inner join statement_line_type slt on slt.id = bsli.stlmnt_line_item_type_id "
				+ "inner join currency c on bsli.original_currency_id = c.id "
				+ "where bs.name = :statementName "
				+ "and bsli.reference = :sourceIvsName "
				+ "and slt.name in ( :paymentTypeName, :refundTypeName ) "
				+ "group by ps.code_by_drs, p.name_by_supplier, slt.id, bsli.original_currency_id, bsli.transactionlinetype_id "
				+ "order by ps.code_by_drs, slt.id, bsli.original_currency_id ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		q.addValue("sourceIvsName", sourceIvsName);
		q.addValue("paymentTypeName", StatementLineType.SS2SP_PRODUCT_INVENTORY_PAYMENT.getValue());
		q.addValue("refundTypeName", StatementLineType.SS2SP_PRODUCT_INVENTORY_REFUND.getValue());

		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);

		return columnsList;
	}
	
	@Override @SuppressWarnings("unchecked")
	public BigDecimal queryPaymentAndRefundUnitAmount(BillStatementType type, String statementName, String ivsName,String sku,int transactionLineTypeId){
		String sql = "select distinct bsli.original_amount, bsli.quantity "
				+ "from "+type.getDbTableStatementLineItem()+" bsli "
				+ "inner join "+type.getDbTableStatement()+" bs on bs.id = bsli.statement_id "
				+ "inner join product_sku ps on ps.id = bsli.product_sku_id "
				+ "where bs.name = :statementName "
				+ "and bsli.reference = :ivsName "
				+ "and ps.code_by_drs = :sku "
				+ "and bsli.transactionlinetype_id = :transactionLineTypeId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		q.addValue("ivsName", ivsName);
		q.addValue("sku", sku);
		q.addValue("transactionLineTypeId", transactionLineTypeId);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		//Assert.isTrue(columnsList.size()==1,columnsList.size()+"");
		Object[] columns = columnsList.get(0);
		BigDecimal originalSubtotal = (BigDecimal)columns[0];
		Integer quantity = (Integer)columns[1];
		return originalSubtotal.divide(new BigDecimal(quantity), 6, RoundingMode.HALF_UP);
	}
	
	@Override @SuppressWarnings("unchecked")
	public List<Ss2spSettleableItemReportLineItem> querySettleableItemReportLineItem(BillStatementType type,String statementName,Country country,String sku, String itemName) {
		String sql = "select                    dtli.id as id, "
				+ "dt.transaction_date at time zone :tz as time_utc, "
				+ "                      ps.code_by_drs as sku, "
				+ "                  p.name_by_supplier as sku_name, "
				+ "                            tlt.name as name, "
				+ "                             dt.type as description, "
				+ "            dt.source_transaction_id as sourceName, "
				+ "                            cur.name as original_currency_name, "
				+ "                         dtli.amount as original_amount "
				+ "from drs_transaction dt "
				+ "inner join drs_transaction_line_item dtli on dtli.drs_transaction_id = dt.id "
				+ "inner join product_sku ps on ps.id = dt.product_sku_id "
				+ "inner join product p on p.id = ps.product_id "
				+ "inner join transactionlinetype tlt on dtli.item_type_id = tlt.id "
				+ "inner join currency cur on cur.id = dtli.currency_id "
				+ "inner join marketplace m on m.id = dt.marketplace_id "
				+ "inner join country ct on ct.id = m.country_id "
				+ "inner join "+type.getDbTableStatement()+" bs on ( bs.period_start<=dt.transaction_date and dt.transaction_date<bs.period_end ) "
				+ "where bs.name = :statementName "
				+ "and cur.id = :currencyId "
				+ "and ct.id = :countryId "
				+ "and ps.code_by_drs=:sku "
				+ "and tlt.name = :transactionLineTypeName "
				+ "order by dt.transaction_date";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("tz", "UTC");
		q.addValue("statementName", statementName);
		q.addValue("currencyId", country.getCurrency().getKey());
		q.addValue("countryId", country.getKey());
		q.addValue("sku", sku);
		q.addValue("transactionLineTypeName", itemName);


		return (List) getNamedParameterJdbcTemplate().query(sql,q, (rs,rowNum) -> new Ss2spSettleableItemReportLineItemImpl(
				rs.getInt("id"),rs.getString("time_utc"),rs.getString("sku"),rs.getString("sku_name"),rs.getString("name"),
				rs.getString("description"),rs.getString("sourceName"),rs.getString("original_currency_name"),rs.getBigDecimal("original_amount")
		));
	}

	@Override @SuppressWarnings("unchecked")
	public List<Ss2spSettleableItemReportLineItem> querySettleableItemReportLineItemProfitShareSettleableRelated(BillStatementType type,String statementName, Country country, String sku, String itemName) {
		String sql = "select                    stli.id as id, "
				+ "st.transaction_date at time zone :tz as time_utc, "
				+ "                      ps.code_by_drs as sku, "
				+ "                  p.name_by_supplier as sku_name, "
				+ "                            tlt.name as name, "
				+ "                                NULL as description, "
				+ "                       stli.ivs_name as sourceName, "
				+ "                              c.name as original_currency_name, "
				+ "                stli.amount_subtotal as original_amount "
				+ "from settleabletransaction st "
				+ "inner join settleabletransactionlineitem stli on stli.transaction_id = st.id "
				+ "inner join product_sku ps on ps.id = stli.product_sku_id "
				+ "inner join product p on p.id = ps.product_id "
				+ "inner join transactionlinetype tlt on stli.transactionlinetype_id=tlt.id "
				+ "inner join currency c on c.id = st.currency_id "
				+ "inner join country on country.id = st.source_country_id "
				+ "inner join "+type.getDbTableStatement()+" bs on ( bs.period_start<=st.transaction_date and st.transaction_date<bs.period_end ) "
				+ "where bs.name = :statementName "
				+ "and country.id = :countryId "
				+ "and c.name = :currencyName "
				+ "and ps.code_by_drs = :sku "
				+ "and tlt.name = :transactionLineTypeName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("tz", "UTC");
		q.addValue("statementName", statementName);
		q.addValue("countryId", country.getKey());
		q.addValue("currencyName", country.getCurrency().name());
		q.addValue("sku", sku);
		q.addValue("transactionLineTypeName", itemName);

		return (List) getNamedParameterJdbcTemplate().query(sql,q, (rs,rowNum) -> new Ss2spSettleableItemReportLineItemImpl(
				rs.getInt("id"),rs.getString("time_utc"),rs.getString("sku"),rs.getString("sku_name"),rs.getString("name"),
				rs.getString("description"),rs.getString("sourceName"),rs.getString("original_currency_name"),rs.getBigDecimal("original_amount")
		));
	}

	@Override @SuppressWarnings("unchecked")
	public List<Ss2spSettleableItemReportLineItem> querySettleableItemReportLineItemImportDutyRelated(BillStatementType type,String statementName, Country country, String sku) {
		String sql = "select                    idtli.id as id, "
				+ "idt.transaction_date at time zone :tz as time_utc, "
				+ "                       ps.code_by_drs as sku, "
				+ "                   p.name_by_supplier as sku_name, "
				+ "                      :importDutyName as name, "
				+ "                                 NULL as description, "
				+ "                             ivs.name as sourceName, "
				+ "                               c.name as original_currency_name, "
				+ "                         idtli.amount as original_amount "
				+ "from import_duty_transaction idt "
				+ "inner join import_duty_transaction_line_item idtli on idtli.transaction_id = idt.id "
				+ "inner join shipment_line_item unsli on unsli.id = idtli.shipment_uns_line_item_id "
				+ "inner join shipment uns on uns.id = unsli.shipment_id "
				+ "inner join shipment ivs on ivs.id = unsli.source_shipment_id "
				+ "inner join product_sku ps on ps.id = unsli.sku_id "
				+ "inner join product p on p.id = ps.product_id "
				+ "inner join currency c on c.id = idt.currency_id "
				+ "inner join "+type.getDbTableStatement()+" bs on ( bs.period_start<=idt.transaction_date and transaction_date<bs.period_end ) "
				+ "where uns.destination_country_id = :countryId "
				+ "and ps.code_by_drs = :sku "
				+ "and bs.name = :statementName ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("tz","UTC");
		q.addValue("statementName",statementName);
		q.addValue("countryId",country.getKey());
		q.addValue("sku",sku);
		q.addValue("importDutyName", TransactionLineType.MS2SS_PURCHASE_ALWNC_IMPORT_DUTY.getName());

		return (List) getNamedParameterJdbcTemplate().query(sql,q, (rs,rowNum) -> new Ss2spSettleableItemReportLineItemImpl(
				rs.getInt("id"),rs.getString("time_utc"),rs.getString("sku"),rs.getString("sku_name"),rs.getString("name"),
				rs.getString("description"),rs.getString("sourceName"),rs.getString("original_currency_name"),rs.getBigDecimal("original_amount")
		));
	}
	
	@Override @SuppressWarnings("unchecked")
	public List<Object []> querySs2spSkuProfitShareDetailReportShippedItem(Date start,Date end, Country country, String sku) {
		String sql = "select to_char(dt.transaction_date at time zone :tz,:fm) as utcDate, "
				+ "                                          dt.marketplace_id as marketplace_id, "
				+ "                                   dt.source_transaction_id as order_id, "
				+ "                               dtlis.pretax_principal_price as pretax_principal_price, "
				+ "                                      dtlis.msdc_retainment as msdc_retainment, "
				+ "                                      dtlis.marketplace_fee as marketplace_fee, "
				+ "                       dtlis.marketplace_fee_non_refundable as marketplace_fee_non_refundable, "
				+ "                                      dtlis.fulfillment_fee as fulfillment_fee, "
				+ "                                      dtlis.ssdc_retainment as ssdc_retainment, "
				+ "                           dtlis.fca_in_marketside_currency as fca_in_marketside_currency, "
				+ "                                      dtlis.sp_profit_share as sp_profit_share "
				+ "from drs_transaction dt "
				+ "inner join drs_transaction_line_item_source dtlis on dtlis.drs_transaction_id = dt.id "
				+ "inner join product_sku ps on ps.id = dt.product_sku_id "
				+ "inner join marketplace m on m.id = dt.marketplace_id "
				+ "where m.country_id = :countryId "
				+ "and ps.code_by_drs = :sku "
				+ "and dt.transaction_date >= :start "
				+ "and dt.transaction_date < :end "
				+ "and dt.type in (:types) "
				+ "order by dt.transaction_date ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("tz", "UTC");
		q.addValue("fm","YYYY-MM-DD HH24:MI:SS");
		q.addValue("countryId",country.getKey());
		q.addValue("sku",sku);
		q.addValue("start",start);
		q.addValue("end",end);
		q.addValue("types", Arrays.asList(AmazonTransactionType.ORDER.getValue(),AmazonTransactionType.OTHER.getValue()));
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);

		return columnsList;
	}

	@Override @SuppressWarnings("unchecked")
	public List<Object []> querySs2spSkuProfitShareDetailReportShippedItem(Date start,Date end, Country country, String sku  ,
																		   List<String> types) {
		String sql = "select to_char(dt.transaction_date at time zone :tz,:fm) as utcDate, "
				+ "                                          dt.marketplace_id as marketplace_id, "
				+ "                                   dt.source_transaction_id as order_id, "
				+ "                               dtlis.pretax_principal_price as pretax_principal_price, "
				+ "                                      dtlis.msdc_retainment as msdc_retainment, "
				+ "                                      dtlis.marketplace_fee as marketplace_fee, "
				+ "                       dtlis.marketplace_fee_non_refundable as marketplace_fee_non_refundable, "
				+ "                                      dtlis.fulfillment_fee as fulfillment_fee, "
				+ "                                      dtlis.ssdc_retainment as ssdc_retainment, "
				+ "                           dtlis.fca_in_marketside_currency as fca_in_marketside_currency, "
				+ "                                      dtlis.sp_profit_share as sp_profit_share "
				+ "from drs_transaction dt "
				+ "inner join drs_transaction_line_item_source dtlis on dtlis.drs_transaction_id = dt.id "
				+ "inner join product_sku ps on ps.id = dt.product_sku_id "
				+ "inner join marketplace m on m.id = dt.marketplace_id "
				+ "where m.country_id = :countryId "
				+ "and ps.code_by_drs = :sku "
				+ "and dt.transaction_date >= :start "
				+ "and dt.transaction_date < :end "
				+ "and dt.type in (:types) "
				+ "order by dt.transaction_date ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("tz", "UTC");
		q.addValue("fm","YYYY-MM-DD HH24:MI:SS");
		q.addValue("countryId",country.getKey());
		q.addValue("sku",sku);
		q.addValue("start",start);
		q.addValue("end",end);
		q.addValue("types",types);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);

		return columnsList;
	}

	@Override @SuppressWarnings("unchecked")
	public List<Object []> querySs2spSkuProfitShareDetailReportRefundedItem(Date start, Date end, Country country, String sku) {
		String sql = "select to_char(dt.transaction_date at time zone :tz,:fm) as utcDate, "
				+ "                                          dt.marketplace_id as marketplace_id, "
				+ "                                   dt.source_transaction_id as order_id, "
				+ "                               dtlis.pretax_principal_price as pretax_principal_price, "
				+ "                                      dtlis.msdc_retainment as msdc_retainment, "
				+ "                                      dtlis.marketplace_fee as marketplace_fee, "
				+ "                       dtlis.marketplace_fee_non_refundable as marketplace_fee_non_refundable, "
				+ "                                      dtlis.fulfillment_fee as fulfillment_fee, "
				+ "                                      dtlis.ssdc_retainment as ssdc_retainment, "
				+ "                           dtlis.fca_in_marketside_currency as fca_in_marketside_currency, "
				+ "                                      dtlis.sp_profit_share as sp_profit_share, "
				+ "                                           dtlis.refund_fee as refund_fee "
				+ "from drs_transaction dt "
				+ "inner join drs_transaction_line_item_source dtlis on dtlis.drs_transaction_id = dt.id "
				+ "inner join product_sku ps on ps.id = dt.product_sku_id "
				+ "inner join marketplace m on m.id = dt.marketplace_id "
				+ "where m.country_id = :countryId "
				+ "and ps.code_by_drs = :sku "
				+ "and dt.transaction_date >= :start "
				+ "and dt.transaction_date < :end "
				+ "and dt.type in (:types) "
				+ "order by dt.transaction_date ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("tz", "UTC");
		q.addValue("fm","YYYY-MM-DD HH24:MI:SS");
		q.addValue("countryId",country.getKey());
		q.addValue("sku",sku);
		q.addValue("start",start);
		q.addValue("end",end);
		q.addValue("types", Arrays.asList(AmazonTransactionType.REFUND.getValue()));
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);

		return columnsList;
	}

	@Override @SuppressWarnings("unchecked")
	public List<Ss2spCustomerCareCostReportItem> querySs2spCustomerCareCostReportItemByProdcutBase(BillStatementType type,String statementName,String country) {
		String sql = "select                     cm.id as id, "
				+ "cmri.date_finish at time zone 'UTC' as date_time_utc, "
				+ "                     ps.code_by_drs as product_sku, "
				+ "                               c.id as case_id, "
				+ "                        cm.line_seq as message_id, "
				+ "                           cur.name as currency, "
				+ "          cmri.actual_charge_by_drs as amount "
				+ "from customercarecase_message_reply_info cmri "
				+ "inner join customercarecase_message cm on cm.id = cmri.msg_id "
				+ "inner join customercarecase c on c.id = cm.case_id "
				+ "inner join marketplace m on m.id = c.marketplace_id "
				+ "inner join country ct on ct.id = m.country_id "
				+ "inner join currency cur on cur.id = cmri.actual_charge_currency_id "
				+ "inner join product_sku ps on ps.id = cmri.related_product_sku_id "
				+ "inner join product_base pb on pb.id = ps.product_base_id "
				+ "inner join company splr on splr.id = pb.supplier_company_id "
				+ "inner join "+type.getDbTableStatement()+" bs on bs.name = :statementName "
				+ "where splr.id = bs.receiving_company_id "
				+ "and ct.code = :country "
				+ "and cmri.is_free_of_charge is FALSE "
				+ "and cmri.date_finish >= bs.period_start "
			    + "and cmri.date_finish < bs.period_end "
			    + "order by cmri.date_finish ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		q.addValue("country", country);

		return (List) getNamedParameterJdbcTemplate().query(sql,q,(rs,rowNum) -> new Ss2spCustomerCareCostReportItemImpl(
				rs.getInt("id"),rs.getString("date_time_utc"),rs.getString("product_sku"),
				rs.getInt("case_id"),rs.getInt("message_id"),rs.getString("currency"),
				rs.getBigDecimal("amount")
		));
	}

	@Override @SuppressWarnings("unchecked")
	public List<Ss2spCustomerCareCostReportItem> querySs2spCustomerCareCostReportItemBySku(BillStatementType type,String statementName, String country, String sku) {
		String sql = "select                     cm.id as id, "
				+ "cmri.date_finish at time zone 'UTC' as date_time_utc, "
				+ "                     ps.code_by_drs as product_sku, "
				+ "                               c.id as case_id, "
				+ "                        cm.line_seq as message_id, "
				+ "                           cur.name as currency, "
				+ "          cmri.actual_charge_by_drs as amount "
				+ "from customercarecase_message_reply_info cmri "
				+ "inner join customercarecase_message cm on cm.id = cmri.msg_id "
				+ "inner join customercarecase c on c.id = cm.case_id "
				+ "inner join marketplace m on m.id = c.marketplace_id "
				+ "inner join country ct on ct.id = m.country_id "
				+ "inner join currency cur on cur.id = cmri.actual_charge_currency_id "
				+ "inner join product_sku ps on ps.id = cmri.related_product_sku_id "
				+ "inner join product_base pb on pb.id = ps.product_base_id "
				+ "inner join company splr on splr.id = pb.supplier_company_id "
				+ "inner join "+type.getDbTableStatement()+" bs on bs.name = :statementName "
				+ "where splr.id = bs.receiving_company_id "
				+ "and ps.code_by_drs = :sku "
				+ "and ct.code = :country "
				+ "and cmri.is_free_of_charge is FALSE "
				+ "and cmri.date_finish >= bs.period_start "
			    + "and cmri.date_finish < bs.period_end "
			    + "order by cmri.date_finish ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		q.addValue("sku", sku);
		q.addValue("country", country);

		return (List) getNamedParameterJdbcTemplate().query(sql,q,(rs,rowNum) -> new Ss2spCustomerCareCostReportItemImpl(
				rs.getInt("id"),rs.getString("date_time_utc"),rs.getString("product_sku"),
				rs.getInt("case_id"),rs.getInt("message_id"),rs.getString("currency"),
				rs.getBigDecimal("amount")
		));
	}

	@Override @SuppressWarnings("unchecked")
	public List<Object []> querySellBackReportItems(BillStatementType type,String statementName) {
		String sql ="select    bsli.reference as ivs_name, "
				+ "            ps.code_by_drs as sku, "
				+ "        p.name_by_supplier as sku_name, "
				+ "                  slt.name as type, "
				+ " bsli.original_currency_id as original_currency_id, "
				+ "bsli.statement_currency_id as statement_currency_id, "
				+ "      bsli.original_amount as original_amount, "
				+ "     bsli.statement_amount as statement_amount, "
                + "             bsli.quantity as quantity ,"
				+ "  bs.period_end  "
				+ "from "+type.getDbTableStatementLineItem()+" bsli "
				+ "inner join "+type.getDbTableStatement()+" bs on bs.id = bsli.statement_id "
				+ "inner join product_sku ps on ps.id = bsli.product_sku_id "
				+ "inner join product p on p.id = ps.product_id "
				+ "inner join statement_line_type slt on slt.id = bsli.stlmnt_line_item_type_id "
				+ "inner join currency c on bsli.original_currency_id = c.id "
				+ "where bs.name = :statementName "
				+ "and slt.name in (:sellBackTypeName) "
				+ "order by bsli.stlmnt_line_item_type_id, bsli.reference, ps.code_by_drs ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		q.addValue("sellBackTypeName", StatementLineType.getSellbackTypes());
		List<Object[]> resultColumnArrayList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);

		return resultColumnArrayList;
	}

	@Override @SuppressWarnings("unchecked")
	public List<Ss2spSettleableItemReportLineItem> querySettleableItemReportLineItem(BillStatementType type,String statementId, String sourcePoId, String sku,int settleableItemId) {
		String sql = "select                      dtli.id as id, "
				+ "dt.transaction_date at time zone 'UTC' as time_utc, "
				+ "                        ps.code_by_drs as sku, "
				+ "                    p.name_by_supplier as sku_name, "
				+ "                              tlt.name as name, "
				+ "                               dt.type as description, "
				+ "              dt.source_transaction_id as sourceName, "
				+ "                                c.name as original_currency_name, "
				+ "                           dtli.amount as original_amount "
				+ "from drs_transaction dt "
				+ "inner join drs_transaction_line_item dtli on dtli.drs_transaction_id = dt.id "
				+ "inner join product_sku ps on ps.id = dt.product_sku_id "
				+ "inner join product p on p.id = ps.product_id "
				+ "inner join transactionlinetype tlt on dtli.item_type_id = tlt.id "
				+ "inner join currency c on c.id = dtli.currency_id "
				+ "where dt.transaction_date>=(select period_start from "+type.getDbTableStatement()+" where name=:statementId) "
				+ "and   dt.transaction_date< (select period_end   from "+type.getDbTableStatement()+" where name=:statementId) "
				+ "and dt.shipment_ivs_name = :sourcePoId "
				+ "and ps.code_by_drs = :sku "
				+ "and tlt.id = :settleableItemId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementId", statementId);
		q.addValue("sourcePoId", sourcePoId);
		q.addValue("sku", sku);
		q.addValue("settleableItemId", settleableItemId);

		return (List) getNamedParameterJdbcTemplate().query(sql,q, (rs,rowNum) -> new Ss2spSettleableItemReportLineItemImpl(
				rs.getInt("id"),rs.getString("time_utc"),rs.getString("sku"),rs.getString("sku_name"),rs.getString("name"),
				rs.getString("description"),rs.getString("sourceName"),rs.getString("original_currency_name"),rs.getBigDecimal("original_amount")
		));
	}
	
	@Override @SuppressWarnings("unchecked")
	public List<Ss2spServiceExpenseReportItem> queryServiceExpenseReportItems(String domesticTransactionInvoice) {
		String sql = "select    dtli.id as id, "
				+ "                null as sku, "
				+ "                null as sku_name, "
				+ "            tlt.name as item_name , "
				+ "           dtli.note as note , "
				+ "                null as quantity , "
				+ "         dtli.amount as total "
				+ "from domestic_transaction_line_item dtli "
				+ "inner join domestic_transaction dt on dt.id = dtli.transaction_id "
				+ "inner join transactionlinetype tlt on tlt.id = dtli.type_id "
				+ "where dt.invoice_number = :domesticTransactionInvoice ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("domesticTransactionInvoice", domesticTransactionInvoice);


		return (List) getNamedParameterJdbcTemplate().query(sql,q,
				(rs,rowNum)-> new Ss2spServiceExpenseReportItemImpl(
						rs.getInt("id"),rs.getString("sku"),rs.getString("sku_name"),rs.getString("item_name"),
						rs.getString("note"),rs.getInt("quantity"),rs.getBigDecimal("total")
				));
	}

	@Override @SuppressWarnings("unchecked")
	public List<Ss2spServiceExpenseReportItem> queryAllServiceExpenseReportItems(String statementName){

		String sql = "select    dtli.id as id, "
				+ "                null as sku, "
				+ "                null as sku_name, "
				+ "            tlt.name as item_name , "
				+ "           dtli.note as note , "
				+ "                null as quantity , "
				+ "         dtli.amount as total "
				+ "from domestic_transaction_line_item dtli "
				+ "inner join domestic_transaction dt on dt.id = dtli.transaction_id "
				+ "inner join transactionlinetype tlt on tlt.id = dtli.type_id "
				+ "inner join bill_statement bst on bst.period_start <= dt.transaction_date and bst.period_end >= dt.transaction_date "
				+ "and dt.splr_company_id = bst.receiving_company_id "
				+ "where bst.name = :statementName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);

		return (List) getNamedParameterJdbcTemplate().query(sql,q,
				(rs,rowNum)-> new Ss2spServiceExpenseReportItemImpl(
						rs.getInt("id"),rs.getString("sku"),rs.getString("sku_name"),rs.getString("item_name"),
						rs.getString("note"),rs.getInt("quantity"),rs.getBigDecimal("total")
				));
	}
	
	@Override @SuppressWarnings("unchecked")
	public List<RemittanceReportItem> queryRemittanceReportItemsIsurToRcvr(BillStatementType type,String statementName) {
		String sql = "select  rmt.id as id, "
				+ "    rmt.date_sent as date_sent, "
				+ "rmt.date_received as date_received, "
				+ "           c.name as currency_name, "
				+ "       rmt.amount as amount, "
				+ "    rmt.reference as reference "
				+ "from remittance rmt "
				+ "inner join currency c on c.id = rmt.currency_id "
				+ "inner join "+type.getDbTableStatement()+" bs on bs.name = :statementName "
				+ "where rmt.date_sent >= bs.period_start "
				+ "and   rmt.date_sent <  bs.period_end "
				+ "and   rmt.sender_company_id = bs.issuing_company_id "
				+ "and rmt.receiver_company_id = bs.receiving_company_id ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);

		return (List) getNamedParameterJdbcTemplate().query(sql,q, (rs , rowNum ) -> new RemittanceReportItemImpl(
				rs.getInt("id"),rs.getString("date_sent"),
				rs.getString("date_received"),rs.getString("currency_name"),rs.getBigDecimal("amount"),
				rs.getString("reference")
		));
	}
	
	@Override @SuppressWarnings("unchecked")
	public List<RemittanceReportItem> queryRemittanceReportItemsRcvrToIsur(BillStatementType type,String statementName) {
		String sql = "select  rmt.id as id, "
				+ "    rmt.date_sent as date_sent, "
				+ "rmt.date_received as date_received, "
				+ "           c.name as currency_name, "
				+ "       rmt.amount as amount, "
				+ "    rmt.reference as reference "
				+ "from remittance rmt "
				+ "inner join currency c on c.id = rmt.currency_id "
				+ "inner join "+type.getDbTableStatement()+" bs on bs.name = :statementName "
				+ "where rmt.date_sent >= bs.period_start "
				+ "and   rmt.date_sent <  bs.period_end "
				+ "and   rmt.sender_company_id = bs.receiving_company_id "
				+ "and rmt.receiver_company_id = bs.issuing_company_id ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);

		return (List) getNamedParameterJdbcTemplate().query(sql,q, (rs , rowNum ) -> new RemittanceReportItemImpl(
				rs.getInt("id"),rs.getString("date_sent"),
				rs.getString("date_received"),rs.getString("currency_name"),rs.getBigDecimal("amount"),
				rs.getString("reference")
		));
	}


	@Override
	public List<Object []> queryAdvertisingCostReportItems(String statementName, String countryCode) {
		String sql = "select itli.note as item_note, " +
				"  c.\"name\" as currency_name, " +
				"  itli.subtotal as total," +
				"  itli.vat_rate as vat_rate," +
				"  itli.vat_amount as vat_amount " +
				"from international_transaction_line_item itli " +
				"inner join international_transaction it on itli.transaction_id = it.id " +
				"inner join currency c on it.currency_id = c.id " +
				"inner join company cp on it.msdc_company_id = cp.id " +
				"inner join country ct on cp.country_id = ct.id " +
				"inner join bill_statement bs on :statementName = bs.\"name\" " +
				"inner join company cssdc on it.ssdc_company_id = cssdc.id " +
				"where ct.code = :countryCode and itli.type_id = 5 " +
				"  and it.transaction_date >= bs.period_start and it.transaction_date < bs.period_end " +
				"  and it.splr_company_id = bs.receiving_company_id" +
				"  and cssdc.k_code = 'K2'";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		q.addValue("countryCode", countryCode);
		List<Object[]> resultColumnArrayList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);


		return resultColumnArrayList;
	}

	@Override
	public List<Object []> queryOtherRefundReportItems(String statementName, String countryCode) {
		String sql = "select itli.note as item_note, " +
				"  c.\"name\" as currency_name, " +
				"  itli.subtotal as total " +
				"from international_transaction_line_item itli " +
				"inner join international_transaction it on itli.transaction_id = it.id " +
				"inner join currency c on it.currency_id = c.id " +
				"inner join company cp on it.msdc_company_id = cp.id " +
				"inner join country ct on cp.country_id = ct.id " +
				"inner join bill_statement bs on :statementName = bs.\"name\" " +
				"inner join company cssdc on it.ssdc_company_id = cssdc.id " +
				"where ct.code = :countryCode and itli.type_id = 89 " +
				"  and it.transaction_date >= bs.period_start and it.transaction_date < bs.period_end " +
				"  and it.splr_company_id = bs.receiving_company_id" +
				"  and cssdc.k_code = 'K2'";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		q.addValue("countryCode", countryCode);
		List<Object[]> resultColumnArrayList =  getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);


		return resultColumnArrayList;
	}

	@Override
	public List<Object []> queryMarketingActivityExpenseReportItems(String statementName, String countryCode) {
		String sql = "select itli.note as item_note, " +
				"  c.\"name\" as currency_name, " +
				"  itli.subtotal as total " +
				"from international_transaction_line_item itli " +
				"inner join international_transaction it on itli.transaction_id = it.id " +
				"inner join currency c on it.currency_id = c.id " +
				"inner join company cp on it.msdc_company_id = cp.id " +
				"inner join country ct on cp.country_id = ct.id " +
				"inner join bill_statement bs on :statementName = bs.\"name\" " +
				"inner join company cssdc on it.ssdc_company_id = cssdc.id " +
				"where ct.code = :countryCode and itli.type_id = 76 " +
				"  and it.transaction_date >= bs.period_start and it.transaction_date < bs.period_end " +
				"  and it.splr_company_id = bs.receiving_company_id" +
				"  and cssdc.k_code = 'K2'";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		q.addValue("countryCode", countryCode);
		List<Object[]> resultColumnArrayList =  getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);

		return resultColumnArrayList;
	}

	@Override
	public List<Object []> queryPartialRefundItems(String statementName, String countryCode){
		String sql = "select itli.note as item_note, " +
				"  c.\"name\" as currency_name, " +
				"  itli.subtotal as total " +
				"from international_transaction_line_item itli " +
				"inner join international_transaction it on itli.transaction_id = it.id " +
				"inner join currency c on it.currency_id = c.id " +
				"inner join company cp on it.msdc_company_id = cp.id " +
				"inner join country ct on cp.country_id = ct.id " +
				"inner join bill_statement bs on :statementName = bs.\"name\" " +
				"inner join company cssdc on it.ssdc_company_id = cssdc.id " +
				"where ct.code = :countryCode and itli.type_id = 101 " +
				"  and it.transaction_date >= bs.period_start and it.transaction_date < bs.period_end " +
				"  and it.splr_company_id = bs.receiving_company_id" +
				"  and cssdc.k_code = 'K2'";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		q.addValue("countryCode", countryCode);
		List<Object[]> resultColumnArrayList =  getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		return resultColumnArrayList;
	}
}
