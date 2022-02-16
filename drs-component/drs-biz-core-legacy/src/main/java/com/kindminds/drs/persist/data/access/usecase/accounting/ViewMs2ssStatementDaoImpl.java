package com.kindminds.drs.persist.data.access.usecase.accounting;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


import com.kindminds.drs.api.data.access.usecase.accounting.ViewMs2ssStatementDao;
import com.kindminds.drs.persist.data.access.rdb.Dao;
import com.kindminds.drs.persist.v1.model.mapping.statement.*;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.kindminds.drs.Currency;
import com.kindminds.drs.TransactionLineType;

import com.kindminds.drs.enums.BillStatementType;
import com.kindminds.drs.api.v1.model.accounting.StatementLineType;
import com.kindminds.drs.api.v1.model.report.Ms2ssPaymentAndRefundReport.Ms2ssPaymentAndRefundReportItem;
import com.kindminds.drs.api.v1.model.report.Ms2ssProductInventoryReturnReport.Ms2ssProductInventoryReturnReportItem;
import com.kindminds.drs.api.v1.model.report.Ms2ssPurchaseAllowanceReport.Ms2ssPurchaseAllowanceReportRawItem;
import com.kindminds.drs.api.v1.model.report.Ms2ssSettleableItemReport.Ms2ssSettleableItemReportLineItem;
import com.kindminds.drs.api.v1.model.report.Ms2ssStatement.Ms2ssStatementItem;
import com.kindminds.drs.api.v1.model.report.Ms2ssStatement.Ms2ssStatementItem.Ms2ssStatementItemType;
import com.kindminds.drs.api.v1.model.report.Ms2ssStatement.Ms2ssStatementItemPaymentOnBehalf;
import com.kindminds.drs.api.v1.model.report.Ms2ssStatement.Ms2ssStatementItemProductInventoryReturn;
import com.kindminds.drs.api.v1.model.report.RemittanceReport.RemittanceReportItem;
import com.kindminds.drs.persist.v1.model.mapping.accounting.RemittanceReportItemImpl;


@Repository
public class ViewMs2ssStatementDaoImpl extends Dao implements ViewMs2ssStatementDao {
	

	
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
		return columns;

	}
	
	@Override @SuppressWarnings("unchecked")
	public String querySettleableItemDrsName(int sid) {
		String sql = "select si.drs_name from transactionlinetype si where si.id = :sid ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("sid", sid);
		List<String> kCodeList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		Assert.isTrue(kCodeList.size()==1,"No or more than one kcode found.");
		return kCodeList.get(0);
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
		List<BigDecimal> rmtList = getNamedParameterJdbcTemplate().queryForList(sql,q,BigDecimal.class);
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
	
	@Override @SuppressWarnings("unchecked")
	public TransactionLineType queryItemById(int id) {
		String sql = "select name from transactionlinetype where id = :id ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("id", id);
		List<String> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		Assert.isTrue(resultList.size()==1);
		TransactionLineType si = TransactionLineType.fromName(resultList.get(0));
		Assert.notNull(si);
		return si;
	}
	


	@Override @SuppressWarnings("unchecked")
	public List<Object []> queryStatementsRcvd(BillStatementType type,String userCompanyKcode,Currency currency) {
		String sql = "select                                                   bs.name as statement_id, "
				+ "                      to_char(bs.period_start at time zone :tz,:fm) as period_start_utc, "
				+ "  to_char((bs.period_end-interval '1 second') at time zone :tz,:fm) as period_end_utc, "
				+ "                                                     bs.currency_id as currency_id, "
				+ "                                                           bs.total as total, "
				+ "                                                         bs.balance as balance, "
				+ "                                                         bs.invoice_from_ssdc as invoice_from_ssdc, "
				+ "                                                         bs.invoice_from_supplier as invoice_from_supplier "
				+ "from "+type.getDbTableStatement()+" bs "
				+ "inner join company isur on isur.id = bs.issuing_company_id "
				+ "inner join company rcvr on rcvr.id = bs.receiving_company_id "
				+ "where rcvr.k_code = :userCompanyKcode "
				+ "and bs.currency_id = :currencyId "
				+ "and isur.is_drs_company is TRUE order by bs.period_start desc, isur.id ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("userCompanyKcode", userCompanyKcode);
		q.addValue("currencyId", currency.getKey());
		q.addValue("tz","UTC");
		q.addValue("fm","YYYY-MM-DD HH24:MI:SS");
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		return columnsList;
	}

	@Override @SuppressWarnings("unchecked")
	public List<Object []> queryStatementsIsud(BillStatementType type,int drsCompanyId) {
		String sql = "select                                                   bs.name as statement_id, "
				+ "                      to_char(bs.period_start at time zone :tz,:fm) as period_start_utc, "
				+ "  to_char((bs.period_end-interval '1 second') at time zone :tz,:fm) as period_end_utc, "
				+ "                                                         bs.balance as balance, "
				+ "                                                             c.name as currency "
				+ "from "+type.getDbTableStatement()+" bs "
				+ "inner join company rcvr on rcvr.id = bs.receiving_company_id "
				+ "inner join currency c on c.id=bs.currency_id "
				+ "where bs.issuing_company_id = :drsCompanyId "
				+ "and rcvr.is_drs_company is TRUE order by bs.period_start desc ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("drsCompanyId", drsCompanyId);
		q.addValue("tz", "UTC");
		q.addValue("fm","YYYY-MM-DD HH24:MI:SS");
		List<Object[]> columnsList =  getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		return (columnsList);
	}
	
	@Override @SuppressWarnings("unchecked")
	public List<Ms2ssStatementItem> queryStatementItems(BillStatementType type,String statementDispName) {
		// The bad smell code is for the requirement that, order by shipmentName, itemType
		// Try not to re-factor it.
		String sql = "select "
				+ "CASE slt.name "
				+ "    WHEN :sltNamePayment THEN :itemTypeNamePaymentRefune "
				+ "    WHEN :sltNameRefund  THEN :itemTypeNamePaymentRefune "
				+ "    WHEN :sltNameAlwnc   THEN :itemTypeNamePurchaseAlwnc "
				+ "    ELSE slt.name "
				+ "END                         as item_type, "
				+ "             bsli.reference as shipment_name, "
				+ " sum(bsli.statement_amount) as amount, "
				+ "         shp.invoice_number as invoice_number "
				+ "from "+type.getDbTableStatementLineItem()+" bsli "
				+ "inner join statement_line_type slt on slt.id=bsli.stlmnt_line_item_type_id "
				+ "inner join shipment shp on shp.name = bsli.reference "
				+ "inner join        "+type.getDbTableStatement()+" bs on bs.id=bsli.statement_id "
				+ "where bs.name=:statementName "
				+ "and slt.name in ( :sltNamePayment, :sltNameRefund, :sltNameAlwnc ) "
				+ "and bsli.reference is not null "
				+ "group by bsli.reference, item_type, shp.invoice_number "
				+ "order by bsli.reference, item_type ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("sltNamePayment", StatementLineType.MS2SS_PRODUCT_INVENTORY_PAYMENT.getValue());
		q.addValue("sltNameRefund", StatementLineType.MS2SS_PRODUCT_INVENTORY_REFUND.getValue());
		q.addValue("sltNameAlwnc", StatementLineType.MS2SS_PURCHASE_ALLOWANCE.getValue());
		q.addValue("itemTypeNamePaymentRefune", Ms2ssStatementItemType.PAYMENT_REFUND.name());
		q.addValue("itemTypeNamePurchaseAlwnc", Ms2ssStatementItemType.PURCHASE_ALWNC.name());
		q.addValue("statementName", statementDispName);

		/*
		 public Ms2ssStatementItemImpl(String type, String shipmentName, BigDecimal amount, String invoiceNumber) {
		 */

		return (List) getNamedParameterJdbcTemplate().query(sql,q, (rs , rowNum) -> new Ms2ssStatementItemImpl(
				rs.getString("item_type"),rs.getString("shipment_name"),
				rs.getBigDecimal("amount"),rs.getString("invoice_number")
		));
	}

	@Override @SuppressWarnings("unchecked")
	public List<Ms2ssStatementItemPaymentOnBehalf> queryStatementItemsPaymentOnBehalf(BillStatementType type, String statementName) {
		String sql = "select            bs.id as id, "
				+ "                 slt.en_us as display_name, "
				+ "sum(bsli.statement_amount) as amount "
				+ "from "+type.getDbTableStatementLineItem()+" bsli "
				+ "inner join statement_line_type slt on slt.id=bsli.stlmnt_line_item_type_id "
				+ "inner join       "+type.getDbTableStatement()+" bs on bs.id=bsli.statement_id "
				+ "where bs.name = :statementName "
				+ "and slt.name = :statementLineTypeOnBehalf "
				+ "group by bs.id, slt.en_us ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementLineTypeOnBehalf", StatementLineType.MS2SS_SSDC_PAYMENT_ON_BEHALF_OF_MSDC.getValue());
		q.addValue("statementName", statementName);

		return(List) getNamedParameterJdbcTemplate().query(sql,q,(rs,rowNum) -> new Ms2ssStatementItemPaymentOnBehalfImpl(
				rs.getInt("id"),rs.getString("display_name"),rs.getBigDecimal("amount")
		));
	}
	
	@Override @SuppressWarnings("unchecked")
	public Object [] queryStatementItemMsdcPaymentOnBehalf(BillStatementType type,String statementName) {
		String sql = "select    slt.en_us as name, "
				+ " bsli.statement_amount as amount "
				+ "from "+type.getDbTableStatementLineItem()+" bsli "
				+ "inner join "+type.getDbTableStatement()+" bs on bs.id=bsli.statement_id "
				+ "inner join statement_line_type slt on slt.id=bsli.stlmnt_line_item_type_id "
				+ "where bs.name = :statementName "
				+ "and slt.name = :sltName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName",statementName);
		q.addValue("sltName", StatementLineType.MSDC_PAYMENT_ON_BEHALF_OF_SSDC.getValue());
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		Assert.isTrue(columnsList.size()<=1);
		if(columnsList.size()==0) return null;
		Object[] columns = columnsList.get(0);
		return  columns;
	}
	
	@Override @SuppressWarnings("unchecked")
	public List<Ms2ssPaymentAndRefundReportItem> queryPaymentAndRefundReportItems(BillStatementType type,String statementId, String sourcePoId) {
		String sql ="select                               ps.code_by_drs as sku, "
				+ "                                   p.name_by_supplier as sku_name, "
				+ "                                            slt.en_us as type, "
				+ "round(sum(bsli.original_amount)/sum(bsli.quantity),2) as original_amount_per_unit, "
				+ "                                   sum(bsli.quantity) as quantity, "
				+ "                                               c.name as original_currency_name, "
				+ "                            sum(bsli.original_amount) as original_amount, "
				+ "                              bsli.transactionlinetype_id as transactionlinetype_id "
				+ "from "+type.getDbTableStatementLineItem()+" bsli "
				+ "inner join "+type.getDbTableStatement()+" bs on bs.id=bsli.statement_id "
				+ "inner join product_sku ps on ps.id=bsli.product_sku_id "
				+ "inner join product p on p.id = ps.product_id "
				+ "inner join statement_line_type slt on slt.id = bsli.stlmnt_line_item_type_id "
				+ "inner join currency c on bsli.original_currency_id = c.id "
				+ "where bs.name = :statementId "
				+ "and bsli.reference = :sourcePoId "
				+ "and slt.name in ( :paymentTypeName, :refundTypeName ) "
				+ "group by ps.code_by_drs, p.name_by_supplier, slt.id, c.name, bsli.transactionlinetype_id "
				+ "order by ps.code_by_drs, slt.id, c.name ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementId", statementId);
		q.addValue("sourcePoId", sourcePoId);
		q.addValue("paymentTypeName", StatementLineType.MS2SS_PRODUCT_INVENTORY_PAYMENT.getValue());
		q.addValue("refundTypeName", StatementLineType.MS2SS_PRODUCT_INVENTORY_REFUND.getValue());


		return (List) getNamedParameterJdbcTemplate().query(sql,q,(rs,rowNum) -> new Ms2ssPaymentAndRefundReportItemImpl(
				rs.getString("sku"),rs.getString("sku_name"),rs.getString("type"),rs.getBigDecimal("original_amount_per_unit"),
				rs.getInt("quantity"), rs.getBigDecimal("original_amount"),rs.getString("original_currency_name"),
				rs.getInt("transactionlinetype_id")
		));
	}
	
	@Override @SuppressWarnings("unchecked")
	public List<Ms2ssPurchaseAllowanceReportRawItem> queryPurchaseAllowanceReportRawItems(BillStatementType type,String statementName,String shipmentName) {
		String sql = "select          bsli.id as id, "
				+ "            ps.code_by_drs as sku, "
				+ "              tlt.drs_name as item_name, "
				+ "                    c.name as currency_name, "
				+ "sum(bsli.statement_amount) as amount, "
				+ "                    tlt.id as settleable_item_id "
				+ "from "+type.getDbTableStatementLineItem()+" bsli "
				+ "inner join "+type.getDbTableStatement()+" bs on bs.id = bsli.statement_id "
				+ "inner join transactionlinetype tlt on tlt.id = bsli.transactionlinetype_id "
				+ "inner join currency c on c.id = bsli.statement_currency_id "
				+ "inner join statement_line_type slt on slt.id = bsli.stlmnt_line_item_type_id "
				+ "inner join product_sku ps on ps.id = bsli.product_sku_id "
				+ "where bs.name = :statementName "
				+ "and bsli.reference = :shipmentName "
				+ "and slt.name = :sltName "
				+ "group by bsli.id, ps.code_by_drs, tlt.drs_name, c.name, tlt.id "
				+ "order by sku, item_name ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		q.addValue("shipmentName", shipmentName);
		q.addValue("sltName", StatementLineType.MS2SS_PURCHASE_ALLOWANCE.getValue());



		return (List) getNamedParameterJdbcTemplate().query(sql,q,(rs,rowNum) -> new Ms2ssPurchaseAllowanceReportRawItemImpl(
				rs.getInt("id"),rs.getString("sku"),rs.getString("item_name"),rs.getString("currency_name"),
				rs.getBigDecimal("amount") , rs.getInt("settleable_item_id")
		));
	}
	
	@Override @SuppressWarnings("unchecked")
	public Map<String,BigDecimal> queryPurchaseAllowanceUnRelatedToSku(BillStatementType type,String statementName, String shipmentName) {
		String sql = "select     tlt.drs_name as item_name, "
				+ "sum(bsli.statement_amount) as amount "
				+ "from "+type.getDbTableStatementLineItem()+" bsli "
				+ "inner join "+type.getDbTableStatement()+" bs on bs.id = bsli.statement_id "
				+ "inner join transactionlinetype tlt on tlt.id = bsli.transactionlinetype_id "
				+ "inner join currency c on c.id = bsli.statement_currency_id "
				+ "inner join statement_line_type slt on slt.id = bsli.stlmnt_line_item_type_id "
				+ "where bs.name = :statementName "
				+ "and bsli.reference = :shipmentName "
				+ "and slt.name = :sltName "
				+ "and bsli.product_sku_id is null "
				+ "group by tlt.drs_name "
				+ "order by item_name ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		q.addValue("shipmentName", shipmentName);
		q.addValue("sltName", StatementLineType.MS2SS_PURCHASE_ALLOWANCE.getValue());
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		System.out.println(columnsList.size());
		Map<String,BigDecimal> result = new LinkedHashMap<>();
		for(Object[] columns:columnsList){
			String name = (String)columns[0];
			BigDecimal amount = (BigDecimal)columns[1];
			result.put(name, amount);
		}
		return result;
	}

	@Override @SuppressWarnings("unchecked")
	public List<RemittanceReportItem> queryRemittanceReportItemsIsurToRcvr(BillStatementType type,String statementName) {
		String sql = "select  rmt.id as id, "
				+ "    rmt.date_sent as date_sent, "
				+ "rmt.date_received as date_received, "
				+ "           c.name as currency_name, "
				+ "       rmt.amount as amount "
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
				rs.getString("date_received"),rs.getString("currency_name"),rs.getBigDecimal("amount"),""
		));
	}
	
	@Override @SuppressWarnings("unchecked")
	public List<RemittanceReportItem> queryRemittanceReportItemsRcvrToIsur(BillStatementType type,String statementName) {
		String sql = "select  rmt.id as id, "
				+ "    rmt.date_sent as date_sent, "
				+ "rmt.date_received as date_received, "
				+ "           c.name as currency_name, "
				+ "       rmt.amount as amount "
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
				rs.getString("date_received"),rs.getString("currency_name"),rs.getBigDecimal("amount"),""
		));
	}
	
	@Override @SuppressWarnings("unchecked")
	public List<Ms2ssSettleableItemReportLineItem> querySettleableItemReportLineItem(BillStatementType type,String statementName, String sourcePoId, String sku, int settleableItemId) {
		String sql = "select          dtli.id as id, "
				+ "dt.transaction_date at time zone 'UTC' as time_utc, "
				+ "            ps.code_by_drs as sku, "
				+ "        p.name_by_supplier as sku_name, "
				+ "              tlt.drs_name as name, "
				+ "                   dt.type as description, "
				+ "  dt.source_transaction_id as sourceName, "
				+ "                    c.name as original_currency_name, "
				+ "               dtli.amount as original_amount "
				+ "from drs_transaction dt "
				+ "inner join drs_transaction_line_item dtli on dtli.drs_transaction_id = dt.id "
				+ "inner join product_sku ps on ps.id = dt.product_sku_id "
				+ "inner join product p on p.id = ps.product_id "
				+ "inner join transactionlinetype tlt on dtli.item_type_id = tlt.id "
				+ "inner join currency c on c.id = dtli.currency_id "
				+ "inner join "+type.getDbTableStatement()+" bs on bs.name = :statementName "
				+ "where ps.code_by_drs=:sku "
				+ "and dtli.isur_company_id = bs.issuing_company_id "
				+ "and dt.transaction_date >= bs.period_start "
				+ "and dt.transaction_date <  bs.period_end "
				+ "and tlt.id = :settleableItemId "
				+ "and dt.shipment_uns_name = :sourcePoId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		q.addValue("sku", sku);
		q.addValue("settleableItemId", settleableItemId);
		q.addValue("sourcePoId", sourcePoId);
		return (List) getNamedParameterJdbcTemplate().query(sql,q,(rs,rowNum) -> new Ms2ssSettleableItemReportLineItemImpl(
				rs.getInt("id"),rs.getString("time_utc"),rs.getString("sku"),rs.getString("sku_name"),
				rs.getString("name"), rs.getString("description"), rs.getString("sourceName"),
				rs.getString("original_currency_name"),rs.getBigDecimal("original_amount")
		));
	}
	
	@Override @SuppressWarnings("unchecked")
	public List<Ms2ssSettleableItemReportLineItem> querySettleableItemReportLineItemSpecial(BillStatementType type,String statementName, String sku, int settleableItemId) {
		String sql = "select          stli.id as id, "
				+ "st.transaction_date at time zone 'UTC' as time_utc, "
				+ "            ps.code_by_drs as sku, "
				+ "        p.name_by_supplier as sku_name, "
				+ "              tlt.drs_name as name, "
				+ "                        '' as description, "
				+ "             stli.uns_name as sourceName, "
				+ "                    c.name as original_currency_name, "
				+ "      stli.amount_subtotal as original_amount "
				+ "from settleabletransaction st "
				+ "inner join settleabletransactionlineitem stli on stli.transaction_id=st.id "
				+ "inner join product_sku ps on ps.id=stli.product_sku_id "
				+ "inner join product p on p.id = ps.product_id "
				+ "inner join transactionlinetype tlt on stli.transactionlinetype_id=tlt.id "
				+ "inner join currency c on c.id=st.currency_id "
				+ "inner join "+type.getDbTableStatement()+" bs on bs.name = :statementName "
				+ "where ps.code_by_drs = :sku "
				+ "and st.buyer_company_id = bs.receiving_company_id "
				+ "and st.seller_company_id = bs.issuing_company_id "
				+ "and st.transaction_date >= bs.period_start "
				+ "and st.transaction_date <  bs.period_end "
				+ "and tlt.id=:settleableItemId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		q.addValue("sku", sku);
		q.addValue("settleableItemId", settleableItemId);


		return (List) getNamedParameterJdbcTemplate().query(sql,q,(rs,rowNum)-> new Ms2ssSettleableItemReportLineItemImpl(
				rs.getInt("id"),rs.getString("time_utc"),rs.getString("sku"),rs.getString("sku_name"),rs.getString("name"),
				rs.getString("description"),rs.getString("sourceName"),rs.getString("original_currency_name"),
				rs.getBigDecimal("original_amount")
		));
	}

	@Override @SuppressWarnings("unchecked")
	public List<Ms2ssSettleableItemReportLineItem> querySettleableItemReportLineItemForImpoortDuty(BillStatementType type,String statementName, String shipmentName, String sku,int settleableItemId) {
		String sql = "select          stli.id as id, "
				+ "st.transaction_date at time zone 'UTC' as time_utc, "
				+ "            ps.code_by_drs as sku, "
				+ "        p.name_by_supplier as sku_name, "
				+ "              tlt.drs_name as name, "
				+ "                        '' as description, "
				+ "             stli.uns_name as sourceName, "
				+ "                    c.name as original_currency_name, "
				+ "     -stli.amount_subtotal as original_amount "
				+ "from settleabletransaction st "
				+ "inner join settleabletransactionlineitem stli on stli.transaction_id=st.id "
				+ "inner join product_sku ps on ps.id=stli.product_sku_id "
				+ "inner join product p on p.id = ps.product_id "
				+ "inner join transactionlinetype tlt on stli.transactionlinetype_id=tlt.id "
				+ "inner join currency c on c.id=st.currency_id "
				+ "inner join "+type.getDbTableStatement()+" bs on bs.name = :statementName "
				+ "where st.transaction_date >= bs.period_start "
				+ "and   st.transaction_date <  bs.period_end "
				+ "and stli.uns_name = :unsShpName "
				+ "and ps.code_by_drs=:sku "
				+ "and tlt.id=:settleableItemId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		q.addValue("unsShpName", shipmentName);
		q.addValue("sku", sku);
		q.addValue("settleableItemId", settleableItemId);

		return (List) getNamedParameterJdbcTemplate().query(sql,q,(rs,rowNum)-> new Ms2ssSettleableItemReportLineItemImpl(
				rs.getInt("id"),rs.getString("time_utc"),rs.getString("sku"),rs.getString("sku_name"),rs.getString("name"),
				rs.getString("description"),rs.getString("sourceName"),rs.getString("original_currency_name"),
				rs.getBigDecimal("original_amount")
		));
	}
	
	@Override @SuppressWarnings("unchecked")
	public List<Ms2ssSettleableItemReportLineItem> querySettleableItemReportLineItemForCustomerCareCase(BillStatementType type,String statementName, String sku, int settleableItemId) {
		String sql = "select          cccm.id as id, "
				+ " cccmri.date_finish at time zone 'UTC' as time_utc, "
				+ "            ps.code_by_drs as sku, "
				+ "        p.name_by_supplier as sku_name, "
				+ "              tlt.drs_name as name, "
				+ "                        '' as description, "
				+ " ccc.market_place_order_id as sourceName, "
				+ "                    c.name as original_currency_name, "
				+ "      cccmri.charge_by_drs as original_amount "
				+ "from customercarecase ccc "
				+ "inner join customercarecase_message cccm on cccm.case_id = ccc.id "
				+ "inner join customercarecase_message_reply_info cccmri on cccmri.msg_id = cccm.id "
				+ "inner join product_sku ps on ps.id = cccmri.related_product_sku_id "
				+ "inner join product p on p.id = ps.product_id "
				+ "inner join transactionlinetype tlt on tlt.id = :settleableItemId "
				+ "inner join currency c on c.id = cccmri.charge_currency_id "
				+ "inner join "+type.getDbTableStatement()+" bs on bs.name = :statementName "
				+ "inner join company msdc on msdc.id = ccc.drs_company_id "
				+ "inner join company splr on splr.id = ccc.supplier_company_id "
				+ "inner join company hdlr on hdlr.id = splr.handler_company_id "
				+ "where msdc.id = bs.issuing_company_id "
				+ "and   hdlr.id = bs.receiving_company_id "
				+ "and cccmri.date_finish >= bs.period_start "
				+ "and cccmri.date_finish <  bs.period_end "
				+ "and ps.code_by_drs=:sku ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		q.addValue("sku", sku);
		q.addValue("settleableItemId", settleableItemId);

		return (List) getNamedParameterJdbcTemplate().query(sql,q,(rs,rowNum)-> new Ms2ssSettleableItemReportLineItemImpl(
				rs.getInt("id"),rs.getString("time_utc"),rs.getString("sku"),rs.getString("sku_name"),rs.getString("name"),
				rs.getString("description"),rs.getString("sourceName"),rs.getString("original_currency_name"),
				rs.getBigDecimal("original_amount")
		));
	}

	@Override @SuppressWarnings("unchecked")
	public List<Ms2ssSettleableItemReportLineItem> querySettleableItemReportLineItemPaymentOnBehalfOfImportDuty(BillStatementType type,String statementName) {
		String sql = "select                      idt.id as id, "
				+ "idt.transaction_date at time zone :tz as time_utc, "
				+ "                                 NULL as sku, "
				+ "                                 NULL as sku_name, "
				+ "                         tlt.drs_name as name, "
				+ "                                 NULL as description, "
				+ "                             uns.name as sourceName, "
				+ "                             cur.name as original_currency_name, "
				+ "                     idt.amount_total as original_amount "
				+ "from import_duty_transaction idt "
				+ "inner join shipment uns on uns.id = idt.shipment_uns_id "
				+ "inner join currency cur on cur.id = idt.currency_id "
				+ "inner join "+type.getDbTableStatement()+" bs on bs.issuing_company_id = uns.buyer_company_id "
				+ "inner join transactionlinetype tlt on tlt.name = :importDutyPaymentOnBehalfName "
				+ "where bs.name = :statementName "
				+ "and idt.transaction_date >= bs.period_start "
				+ "and idt.transaction_date <  bs.period_end "
				+ "order by idt.transaction_date, sourceName  ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("importDutyPaymentOnBehalfName",TransactionLineType.MS2SS_SSDC_PAYMENT_ONBEHALF_OF_MSDC_FOR_IMPORT_DUTY.getName());
		q.addValue("statementName", statementName);
		q.addValue("tz","UTC");
		return (List) getNamedParameterJdbcTemplate().query(sql,q,(rs,rowNum)-> new Ms2ssSettleableItemReportLineItemImpl(
				rs.getInt("id"),rs.getString("time_utc"),rs.getString("sku"),rs.getString("sku_name"),rs.getString("name"),
				rs.getString("description"),rs.getString("sourceName"),rs.getString("original_currency_name"),
				rs.getBigDecimal("original_amount")
		));
	}
	
	@Override @SuppressWarnings("unchecked")
	public List<Ms2ssSettleableItemReportLineItem> querySettleableItemReportLineItemMsdcPaymentOnBehalf(BillStatementType type,String statementName){
		String sql = "select                     itli.id as id, "
				+ " it.transaction_date at time zone :tz as time_utc, "
				+ "                                 NULL as sku, "
				+ "                                 NULL as sku_name, "
				+ "                         tlt.drs_name as name, "
				+ "                                 NULL as description, "
				+ "                          splr.k_code as sourceName, "
				+ "                             cur.name as original_currency_name, "
				+ "                        itli.subtotal as original_amount "
				+ "from international_transaction it "
				+ "inner join company splr on splr.id = it.splr_company_id "
				+ "inner join currency cur on cur.id = it.currency_id "
				+ "inner join international_transaction_line_item itli on itli.transaction_id=it.id "
				+ "inner join transactionlinetype tlt on tlt.id = itli.type_id "
				+ "inner join "+type.getDbTableStatement()+" bs on (bs.issuing_company_id=it.msdc_company_id and bs.receiving_company_id=it.ssdc_company_id) "
				+ "where bs.name = :statementName "
				+ "and it.transaction_date >= bs.period_start "
				+ "and it.transaction_date <  bs.period_end "
				+ "and tlt.class = 'International' "
				+ "and tlt.is_msdc_payment_on_behalf_of_ssdc_item = TRUE "
				+ "order by it.transaction_date, sourceName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		q.addValue("tz","UTC");
		return (List) getNamedParameterJdbcTemplate().query(sql,q,(rs,rowNum)-> new Ms2ssSettleableItemReportLineItemImpl(
				rs.getInt("id"),rs.getString("time_utc"),rs.getString("sku"),rs.getString("sku_name"),rs.getString("name"),
				rs.getString("description"),rs.getString("sourceName"),rs.getString("original_currency_name"),
				rs.getBigDecimal("original_amount")
		));
	}

	@Override @SuppressWarnings("unchecked")
	public Ms2ssStatementItemProductInventoryReturn queryProductInventoryReturnItem(BillStatementType type,String statementName) {
		String sql = "select 'Product Inventory Return' as display_name, "
				+ "          sum(bsli.statement_amount) as amount, "
				+ "          bsli.statement_currency_id as currency_id "
				+ "from "+type.getDbTableStatementLineItem()+" bsli "
				+ "inner join "+type.getDbTableStatement()+" bs on bs.id = bsli.statement_id "
				+ "inner join statement_line_type slt on slt.id = bsli.stlmnt_line_item_type_id "
				+ "where bs.name = :statementName and slt.name = :productInventoryReturnSlt "
				+ "group by bsli.statement_currency_id ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		q.addValue("productInventoryReturnSlt", StatementLineType.MS2SS_PRODUCT_INVENTORY_RETURN.getValue());
		List<Ms2ssStatementItemProductInventoryReturnImpl> resultList =
				getNamedParameterJdbcTemplate().query(sql,q, (rs,rowNum) -> new Ms2ssStatementItemProductInventoryReturnImpl(
						rs.getString("display_name"),rs.getBigDecimal("amount"),rs.getInt("currency_id")
				));
		Assert.isTrue(resultList.size()<=1);
		if(resultList.size()==0) return null;
		return resultList.get(0);
	}

	@Override @SuppressWarnings("unchecked")
	public List<Ms2ssProductInventoryReturnReportItem> queryMs2ssProductInventoryReturnReportItems(BillStatementType type,String statementName) {
		String sql = "select bsli.id as id, "
				+ "   bsli.reference as shipment_name, "
				+ "   ps.code_by_drs as product_sku, "
				+ "    bsli.quantity as quantity "
				+ "from "+type.getDbTableStatementLineItem()+" bsli "
				+ "inner join "+type.getDbTableStatement()+" bs on bs.id = bsli.statement_id "
				+ "inner join statement_line_type slt on slt.id = bsli.stlmnt_line_item_type_id "
				+ "inner join product_sku ps on ps.id = bsli.product_sku_id "
				+ "inner join shipment shp on shp.name = bsli.reference "
				+ "where bs.name = :statementName and slt.name = :productInventoryReturnSlt "
				+ "order by shp.serial_id, ps.code_by_drs ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		q.addValue("productInventoryReturnSlt", StatementLineType.MS2SS_PRODUCT_INVENTORY_RETURN.getValue());
		List<Ms2ssProductInventoryReturnReportItemImpl> resultList =
				getNamedParameterJdbcTemplate().query(sql,q, (rs,rowNum) -> new Ms2ssProductInventoryReturnReportItemImpl(
						rs.getString("shipment_name"),rs.getString("product_sku"),rs.getInt("quantity")
				));
		return (List)resultList;
	}

	@Override
	public Currency queryIssuerCurrency(BillStatementType type,String statementName) {
		String sql = "select isur.currency_id "
				+ "from "+type.getDbTableStatement()+" bs "
				+ "inner join company isur on isur.id = bs.issuing_company_id "
				+ "where bs.name = :statementName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		Integer currencyId = getNamedParameterJdbcTemplate().queryForObject(sql,q,Integer.class);
		return Currency.fromKey(currencyId);
	}
	
	@Override @SuppressWarnings("unchecked")
	public Currency queryShipmentCurrency(String shipmentName) {
		String sql = "select shp.currency_id from shipment shp where shp.name = :shipmentName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("shipmentName",shipmentName);
		List<Integer> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,Integer.class);
		Assert.isTrue(resultList.size()==1);
		return Currency.fromKey(resultList.get(0));
	}
}





















