package com.kindminds.drs.persist.data.access.usecase.accounting;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShipmentType;
import com.kindminds.drs.persist.data.access.rdb.Dao;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.kindminds.drs.Currency;
import com.kindminds.drs.TransactionLineType;
import com.kindminds.drs.api.data.access.usecase.accounting.DoMs2ssSettlementDao;
import com.kindminds.drs.enums.BillStatementType;
import com.kindminds.drs.api.v1.model.accounting.InternationalTransaction.CashFlowDirection;
import com.kindminds.drs.api.v1.model.close.BillStatement;
import com.kindminds.drs.api.v1.model.close.BillStatement.BillStatementLineItem;
import com.kindminds.drs.api.v1.model.close.Remittance;
import com.kindminds.drs.persist.v1.model.mapping.close.BillStatementImpl;
import com.kindminds.drs.persist.v1.model.mapping.close.BillStatementLineItemImpl;
import com.kindminds.drs.persist.v1.model.mapping.close.RemittenceImpl;
import com.kindminds.drs.persist.data.access.rdb.util.PostgreSQLHelper;
import com.kindminds.drs.util.DateHelper;

@Repository
public class DoMs2ssSettlementDaoImpl extends Dao implements  DoMs2ssSettlementDao {


	
	@Override @Transactional("transactionManager")
	public int insertStatement(int nextSerialId,String newStmntName, String rcvrKcode, String isurKcode, Date startDate, Date endDate, int versionNumber,BillStatementType type) {
		String bsTable = type.getDbTableStatement();
		int dbId = PostgreSQLHelper.getNextVal(getNamedParameterJdbcTemplate(),bsTable,"id");
		String sql = "insert into "+bsTable+" "
				+ "(  id, sequence,          name, period_start, period_end, issuing_company_id, receiving_company_id, balance, previous_balance, version_number ) "
				+ "select "
				+ "  :id,     :seq, :newStmntName,         :sdt,       :edt,            isur.id,              rcvr.id,    :bal,            :pbal, :versionNumber "
				+ "from       company rcvr "
				+ "inner join company isur on isur.k_code=:isurName "
				+ "where rcvr.k_code=:rcvrName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("id", dbId);
		q.addValue("seq", nextSerialId);
		q.addValue("newStmntName", newStmntName);
		q.addValue("sdt", startDate);
		q.addValue("edt", endDate);
		q.addValue("bal",BigDecimal.ZERO);
		q.addValue("pbal",BigDecimal.ZERO);
		q.addValue("rcvrName",rcvrKcode);
		q.addValue("isurName",isurKcode);
		q.addValue("versionNumber",versionNumber);

		getNamedParameterJdbcTemplate().update(sql,q);
		return dbId;
	}
	
	@Override @Transactional("transactionManager")
	public int doShipmentRelatedItem(int statementId, int beginLineSeq, int startIndex, int endIndex, TransactionLineType sItem,BillStatementType type) {
		String bsTable = type.getDbTableStatement();
		String bsliTable = type.getDbTableStatementLineItem();
		String sql = "insert into " + bsliTable
				+ "( statement_id, line_seq, stlmnt_line_item_type_id, original_currency_id, original_amount, product_sku_id, quantity, transactionlinetype_id, reference, country_id ) "
				+ "select "
				+ "  statement_id, line_seq, stlmnt_line_item_type_id, original_currency_id, original_amount, product_sku_id, quantity, transactionlinetype_id, reference, country_id "
				+ "from("
				+ "    select "
				+ "                                    (select id from "+bsTable+" where id = :sid) as statement_id, "
				+ "    row_number() over (order by r.product_sku_id, r.transactionlinetype_id) + :beginLineSeq as line_seq, "
				+ "    r.* "
				+ "    from ( "
				+ "        select "
				+ "        tlt.ms2ss_stmnt_line_type_id as stlmnt_line_item_type_id, "
				+ "                    dtli.currency_id as original_currency_id, "
				+ "                    sum(dtli.amount) as original_amount, "
				+ "                   dt.product_sku_id as product_sku_id, "
				+ "                    sum(dt.quantity) as quantity, "
				+ "                   dtli.item_type_id as transactionlinetype_id, "
				+ "                dt.shipment_uns_name as reference, "
				+ "                        m.country_id as country_id "
				+ "        from drs_transaction dt "
				+ "        inner join drs_transaction_line_item  dtli on dt.id = dtli.drs_transaction_id "
				+ "        inner join transactionlinetype tlt on dtli.item_type_id = tlt.id "
				+ "        inner join marketplace m on m.id = dt.marketplace_id "
				+ "        inner join "+bsTable+" bs on bs.id = :sid "
			    + "        where tlt.name = :iname "
			    + "        and dtli.isur_company_id = bs.issuing_company_id "
			    + "        and dtli.rcvr_company_id = bs.receiving_company_id "
			    + "        and dt.transaction_date >= bs.period_start "
			    + "        and dt.transaction_date  < bs.period_end   "
				+ "        group by dt.shipment_uns_name, dtli.item_type_id, tlt.ms2ss_stmnt_line_type_id, dt.product_sku_id, dtli.currency_id, m.country_id "
				+ "        order by dt.shipment_uns_name "
				+ "    ) as r "
				+ ") as r1 "
				+ "where r1.line_seq >= :start and r1.line_seq <= :end ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("sid", statementId);
		q.addValue("iname", sItem.getName());
		q.addValue("start", startIndex + beginLineSeq);
		q.addValue("end", endIndex + beginLineSeq);
		q.addValue("beginLineSeq", beginLineSeq);
		return getNamedParameterJdbcTemplate().update(sql,q);
	}
	
	@Override @Transactional("transactionManager")
	public int doInternationalTransactionItem(BillStatementType bsType,int beginIndex,int startIndex,int endIndex,int statementId,Date start,Date end,String isurKcode,String rcvrKcode,CashFlowDirection direction,boolean isMsdcPayOnBehalfOfSsdc) {
		String signForSum = direction==CashFlowDirection.MS2SP?"":"-";
		String bsTable = bsType.getDbTableStatement();
		String bsliTable = bsType.getDbTableStatementLineItem();
		String sql = "insert into "+bsliTable
				+ "( statement_id, line_seq, stlmnt_line_item_type_id, original_currency_id, original_amount, transactionlinetype_id, reference, country_id ) "
				+ "select "
				+ "  statement_id, line_seq, stlmnt_line_item_type_id, original_currency_id, original_amount, transactionlinetype_id, reference, country_id "
				+ "from("
				+ "    select                   (select id from "+bsTable+" where id = :sid) as statement_id, "
				+ "    row_number() over (order by r.transactionlinetype_id,uns_serial_id) + :beginLineSeq as line_seq, "
				+ "    r.* "
				+ "    from ( "
				+ "        select "
				+ "         tlt.ms2ss_stmnt_line_type_id as stlmnt_line_item_type_id, "
				+ "                       it.currency_id as original_currency_id, "
		        + signForSum + "      sum(itli.subtotal) as original_amount, "
				+ "                               tlt.id as transactionlinetype_id, "
				+ "        latest_arrival_info.serial_id as uns_serial_id, "
				+ "         latest_arrival_info.uns_name as reference, "
				+ "                      msdc.country_id as country_id "
				+ "        from international_transaction it "
				+ "        inner join international_transaction_line_item itli on itli.transaction_id = it.id "
				+ "        inner join transactionlinetype tlt on tlt.id = itli.type_id "
				+ "        inner join company msdc on msdc.id = it.msdc_company_id "
				+ "        inner join company ssdc on ssdc.id = it.ssdc_company_id "
				// The SQL join below is for retrieving latest arrival UNS group by supplier of international transaction  
				+ "        inner join ( "
				+ "		       select distinct on (ivs.seller_company_id) uns.serial_id, uns.name as uns_name, ui.arrival_date as arrival_date, ivs.seller_company_id as ivs_seller_company_id "
				+ "	           from company isur "
				+ "            inner join country ct on ct.id = isur.country_id "
				+ "            inner join marketplace w on w.id = ct.warehouse_id "
				+ "            inner join shipment uns on uns.warehouse_id = w.id "
				+ "	           inner join shipment_uns_info ui on ui.shipment_id = uns.id "
				+ "	           inner join shipment_line_item unsli on unsli.shipment_id = uns.id "
				+ "	           inner join shipment ivs on ivs.id = unsli.source_shipment_id "
				+ "            where ui.arrival_date < :stmntEnd and isur.k_code = :isurKcode "
				+ "	           order by ivs.seller_company_id, arrival_date desc "
				+ "        ) latest_arrival_info on latest_arrival_info.ivs_seller_company_id = it.splr_company_id "
				// ------------------------------------------------------------------------------------------------------------
			    + "        where tlt.class = :tltClass "
			    + "        and tlt.cash_flow_direction_key = :cash_flow_direction_key "
			    + "        and tlt.is_msdc_payment_on_behalf_of_ssdc_item = :is_msdc_payment_on_behalf_of_ssdc_item "
			    + "        and msdc.k_code = :isurKcode "
			    + "        and ssdc.k_code = :rcvrKcode "
			    + "        and it.transaction_date >= :stmntStart "
			    + "        and it.transaction_date <  :stmntEnd "
				+ "        group by tlt.id, tlt.ms2ss_stmnt_line_type_id, it.currency_id, latest_arrival_info.serial_id, latest_arrival_info.uns_name, msdc.country_id "
				+ "    ) as r "
				+ ") as r1 "
				+ "where r1.line_seq >= :start and r1.line_seq <= :end ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("sid", statementId);
		q.addValue("start", startIndex + beginIndex);
		q.addValue("end", endIndex + beginIndex);
		q.addValue("beginLineSeq",beginIndex);
		q.addValue("isurKcode",isurKcode);
		q.addValue("rcvrKcode",rcvrKcode);
		q.addValue("stmntStart",start);
		q.addValue("stmntEnd",end);
		q.addValue("tltClass", "International");
		q.addValue("cash_flow_direction_key",direction.getKey());
		q.addValue("is_msdc_payment_on_behalf_of_ssdc_item",isMsdcPayOnBehalfOfSsdc);
		int insertedRows = getNamedParameterJdbcTemplate().update(sql,q);
		return insertedRows;
	}
	
	@Override @Transactional("transactionManager")
	public int doInternationalTransactionItemMsdcPaymentOnBehalfOfSsdc(BillStatementType bsType,int beginIndex,int startIndex,int endIndex,int statementId,Date start,Date end,String isurKcode,String rcvrKcode,CashFlowDirection direction,boolean isMsdcPayOnBehalfOfSsdc) {
		String signForSum = direction==CashFlowDirection.MS2SP?"":"-";
		String bsTable = bsType.getDbTableStatement();
		String bsliTable = bsType.getDbTableStatementLineItem();
		String sql = "insert into "+bsliTable
				+ "( statement_id, line_seq, stlmnt_line_item_type_id, original_currency_id, original_amount, transactionlinetype_id, country_id ) "
				+ "select "
				+ "  statement_id, line_seq, stlmnt_line_item_type_id, original_currency_id, original_amount, transactionlinetype_id, country_id "
				+ "from("
				+ "    select                   (select id from "+bsTable+" where id = :sid) as statement_id, "
				+ "    row_number() over (order by r.transactionlinetype_id) + :beginLineSeq as line_seq, "
				+ "    r.* "
				+ "    from ( "
				+ "        select "
				+ "         tlt.ms2ss_stmnt_line_type_id as stlmnt_line_item_type_id, "
				+ "                       it.currency_id as original_currency_id, "
		        + signForSum + "      sum(itli.subtotal) as original_amount, "
				+ "                               tlt.id as transactionlinetype_id, "
				+ "                      msdc.country_id as country_id "
				+ "        from international_transaction it "
				+ "        inner join international_transaction_line_item itli on itli.transaction_id = it.id "
				+ "        inner join transactionlinetype tlt on tlt.id = itli.type_id "
				+ "        inner join company msdc on msdc.id = it.msdc_company_id "
				+ "        inner join company ssdc on ssdc.id = it.ssdc_company_id "
			    + "        where tlt.class = :tltClass "
			    + "        and tlt.cash_flow_direction_key = :cash_flow_direction_key "
			    + "        and tlt.is_msdc_payment_on_behalf_of_ssdc_item = :is_msdc_payment_on_behalf_of_ssdc_item "
			    + "        and msdc.k_code = :isurKcode "
			    + "        and ssdc.k_code = :rcvrKcode "
			    + "        and it.transaction_date >= :stmntStart "
			    + "        and it.transaction_date <  :stmntEnd "
				+ "        group by tlt.id, tlt.ms2ss_stmnt_line_type_id, it.currency_id, msdc.country_id "
				+ "    ) as r "
				+ ") as r1 "
				+ "where r1.line_seq >= :start and r1.line_seq <= :end ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("sid", statementId);
		q.addValue("start", startIndex + beginIndex);
		q.addValue("end", endIndex + beginIndex);
		q.addValue("beginLineSeq",beginIndex);
		q.addValue("isurKcode",isurKcode);
		q.addValue("rcvrKcode",rcvrKcode);
		q.addValue("stmntStart",start);
		q.addValue("stmntEnd",end);
		q.addValue("tltClass", "International");
		q.addValue("cash_flow_direction_key",direction.getKey());
		q.addValue("is_msdc_payment_on_behalf_of_ssdc_item",isMsdcPayOnBehalfOfSsdc);
		int insertedRows = getNamedParameterJdbcTemplate().update(sql,q);
		return insertedRows;
	}
	
	@Override @Transactional("transactionManager")
	public int doCustomerCareCaseItem(int statementId, int beginLineSeq, int startIndex, int endIndex, String rcvrKcode, String isurKcode,BillStatementType bsType) {
		String bsTable = bsType.getDbTableStatement();
		String bsliTable = bsType.getDbTableStatementLineItem();
		String sql = "insert into "+bsliTable
				+ "( statement_id, line_seq, stlmnt_line_item_type_id, original_currency_id, original_amount, product_sku_id, quantity, transactionlinetype_id, reference, country_id ) "
				+ "select "
				+ "  statement_id, line_seq, stlmnt_line_item_type_id, original_currency_id, original_amount, product_sku_id,     :qty, transactionlinetype_id, reference, country_id "
				+ "from("
				+ "    select "
				+ "                                    (select id from "+bsTable+" where id = :sid) as statement_id, "
				+ "    row_number() over (order by r.product_sku_id, r.transactionlinetype_id) + :beginLineSeq as line_seq, "
				+ "    r.* "
				+ "    from ( "
				+ "        select "
				+ "         tlt.ms2ss_stmnt_line_type_id as stlmnt_line_item_type_id, "
				+ "                                 c.id as original_currency_id, "
			    + "           -sum(cccmri.charge_by_drs) as original_amount, "
		        + "        cccmri.related_product_sku_id as product_sku_id, "
				+ "                               tlt.id as transactionlinetype_id, "
				+ "                    pss.shipment_name as reference, "
				+ "                      msdc.country_id as country_id "
				+ "        from customercarecase ccc "
				+ "        inner join customercarecase_message cccm on cccm.case_id = ccc.id "
				+ "        inner join customercarecase_message_reply_info cccmri on cccmri.msg_id = cccm.id "
				+ "        inner join transactionlinetype tlt on tlt.name = :transactionLineTypeName "
				+ "        inner join currency c on c.name = :usdCurrencyName "
				+ "        inner join company msdc on msdc.id = ccc.drs_company_id "
				+ "        inner join company splr on splr.id = ccc.supplier_company_id "
				+ "        inner join company hdlr on hdlr.id = splr.handler_company_id "
				+ "        inner join product_sku_stock pss on pss.product_sku_id = cccmri.related_product_sku_id "
				+ "        inner join shipment shp on shp.name = pss.shipment_name "
				+ "        inner join shipment_uns_info shpui on shpui.shipment_id = shp.id "
				+ "        inner join "+bsTable+" bs on bs.id = :sid "
				+ "        inner join ( " // get the newest shipments of the skus move from stmnt rvcr to stmnt isur 
				+ "            select shp.seller_company_id, shp.buyer_company_id,pss.product_sku_id, max(shpui.arrival_date) as latest from product_sku_stock pss "
				+ "            inner join shipment shp on shp.name = pss.shipment_name "
				+ "            inner join shipment_uns_info shpui on shpui.shipment_id = shp.id "
				+ "            where shp.type = :unsTypeName group by  shp.seller_company_id, shp.buyer_company_id,pss.product_sku_id ) newest_shp_datas on ( "
				+ "                shpui.arrival_date  = newest_shp_datas.latest "
				+ "                and msdc.id                  = newest_shp_datas.buyer_company_id "
				+ "                and hdlr.id                  = newest_shp_datas.seller_company_id "
				+ "                and cccmri.related_product_sku_id = newest_shp_datas.product_sku_id "
				+ "            ) "
			    + "        where  msdc.k_code = :isurKcode "
		        + "        and    hdlr.k_code = :rcvrKcode "
			    + "        and cccmri.date_finish >= bs.period_start "
			    + "        and cccmri.date_finish  < bs.period_end "
			    + "        and cccmri.is_free_of_charge = false "
				+ "        group by tlt.id, tlt.ms2ss_stmnt_line_type_id, cccmri.related_product_sku_id, c.id, pss.shipment_name, msdc.country_id "
				+ "    ) as r "
				+ ") as r1 "
				+ "where r1.line_seq >= :start and r1.line_seq <= :end ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("qty", 1);
		q.addValue("sid", statementId);
		q.addValue("transactionLineTypeName",TransactionLineType.MS2SS_PURCHASE_ALWNC_CUSTOMERCARE.getName());
		q.addValue("usdCurrencyName", Currency.USD.name());
		q.addValue("rcvrKcode", rcvrKcode);
		q.addValue("isurKcode", isurKcode);
		q.addValue("unsTypeName", ShipmentType.UNIFIED.getValue());
		q.addValue("start", startIndex + beginLineSeq);
		q.addValue("end", endIndex + beginLineSeq);
		q.addValue("beginLineSeq", beginLineSeq);
		int insertedRows = getNamedParameterJdbcTemplate().update(sql,q);
		return insertedRows;
	}
	
	@Override
	public void updateMs2ssNameOfCustomerCareCaseMsdcMessage(int statementId) {
		String sql = "update customercarecase_message_reply_info cccmri set ms2ss_statement_name = bs.name "
				+ "from bill_statement bs "
				+ "where bs.id = :statementId and cccmri.id in ( "
				+ "    select cccmri.id from customercarecase_message_reply_info cccmri "
				+ "    inner join customercarecase_message cccm on cccm.id = cccmri.msg_id "
				+ "    inner join customercarecase ccc on ccc.id = cccm.case_id "
				+ "    inner join company splr on splr.id = ccc.supplier_company_id "
				+ "    inner join company hdlr on hdlr.id = splr.handler_company_id "
				+ "    inner join bill_statement bs on bs.id = :statementId "
				+ "    where hdlr.id = bs.receiving_company_id "
				+ "    and cccmri.date_finish >= bs.period_start "
			    + "    and cccmri.date_finish  < bs.period_end "
			    + "    and cccmri.is_free_of_charge = false "
			    + ")";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementId", statementId);
		getNamedParameterJdbcTemplate().update(sql,q);
	}
	
	@Override @Transactional("transactionManager")
	public int doProductInventoryReturnItem(int statementId, int beginIndex, int startIndex, int endIndex, TransactionLineType type, BillStatementType bsType) {
		String bsTable = bsType.getDbTableStatement();
		String bsliTable = bsType.getDbTableStatementLineItem();
		String sql = "insert into "+bsliTable
				+ "( statement_id, line_seq, stlmnt_line_item_type_id, original_currency_id, original_amount, product_sku_id, quantity, transactionlinetype_id, reference, country_id ) "
				+ "select "
				+ "  statement_id, line_seq, stlmnt_line_item_type_id, original_currency_id, original_amount, product_sku_id, quantity, transactionlinetype_id, reference, country_id "
				+ "from("
				+ "    select "
				+ "                          (select id from "+bsTable+" where id = :sid) as statement_id, "
				+ "    row_number() over (order by r.transactionlinetype_id) + :beginLineSeq as line_seq, "
				+ "    r.* "
				+ "    from ( "
				+ "        select "
				+ "        tlt.ms2ss_stmnt_line_type_id as stlmnt_line_item_type_id, "
				+ "                    dtli.currency_id as original_currency_id, "
				+ "                    sum(dtli.amount) as original_amount, "
				+ "                   dt.product_sku_id as product_sku_id, "
				+ "                    sum(dt.quantity) as quantity, "
				+ "                   dtli.item_type_id as transactionlinetype_id, "
				+ "                dt.shipment_uns_name as reference, "
				+ "                        m.country_id as country_id "
				+ "        from drs_transaction_line_item  dtli "
				+ "        inner join drs_transaction dt on dt.id = dtli.drs_transaction_id "
				+ "        inner join transactionlinetype tlt on dtli.item_type_id = tlt.id "
				+ "        inner join marketplace m on m.id = dt.marketplace_id "
				+ "        inner join "+bsTable+" bs on bs.id = :sid "
			    + "        where tlt.name = :iname "
		        + "        and dtli.isur_company_id = bs.issuing_company_id "
		        + "        and dtli.rcvr_company_id = bs.receiving_company_id "
			    + "        and dt.transaction_date >= bs.period_start "
			    + "        and dt.transaction_date  < bs.period_end "
				+ "        group by dtli.item_type_id, tlt.ms2ss_stmnt_line_type_id, dtli.currency_id,dt.product_sku_id, dt.shipment_uns_name,m.country_id "
				+ "    ) as r "
				+ ") as r1 "
				+ "where r1.line_seq >= :start and r1.line_seq <= :end ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("sid", statementId);
		q.addValue("iname", type.getName());
		q.addValue("start", startIndex + beginIndex);
		q.addValue("end", endIndex + beginIndex);
		q.addValue("beginLineSeq", beginIndex);
		return getNamedParameterJdbcTemplate().update(sql,q);
	}

	@Override @SuppressWarnings("unchecked")
	public BigDecimal getAllTransactionTotal(int statementId,BillStatementType bsType) {
		String bsliTable = bsType.getDbTableStatementLineItem();
		String sql = "select sum(statement_amount) from "+bsliTable+" where statement_id = :sid";

		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("sid",statementId);

		List<BigDecimal> resultRateList= getNamedParameterJdbcTemplate().queryForList(sql , q, BigDecimal.class);

		Assert.isTrue(resultRateList.size()==1,"resultRateList.size()==1");
		BigDecimal result = resultRateList.get(0);
		if(result==null) return BigDecimal.ZERO;
		return result;
	}
	
	@Override @SuppressWarnings("unchecked")
	public BigDecimal queryPreviousBalance(int statementId,BillStatementType bsType) {
		String bsTable = bsType.getDbTableStatement();
		String sql = "select bs_previous.balance from bill_statement bs_previous "
				+ "inner join "+bsTable+" bs_this on ("
				+ "        bs_this.receiving_company_id = bs_previous.receiving_company_id "
				+ "    and bs_this.issuing_company_id   = bs_previous.issuing_company_id "
				+ "    and bs_this.period_start         = bs_previous.period_end "
				+ ")"
				+ "where bs_this.id = :statementId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementId", statementId);
		List<BigDecimal> resultBalanceList= getNamedParameterJdbcTemplate().queryForList(sql , q, BigDecimal.class);
		if(resultBalanceList.size()==0) return BigDecimal.ZERO;
		Assert.isTrue(resultBalanceList.size()==1,"More than one or No balance found.");
		return resultBalanceList.get(0);
	}
	
	@Override @SuppressWarnings("unchecked")
	public BillStatement getStatement(int statementId,BillStatementType bsType) {
		String bsTable = bsType.getDbTableStatement();
		String sql = "select        bs.id as id, "
				+ "               bs.name as display_name, "
				+ "          period_start as date_start, "
				+ "            period_end as date_end, "
				+ "                 total as total, "
				+ "      previous_balance as previous_balance, "
				+ "               balance as balance, "
				+ "           rcvr.k_code as rcvr_kcode, "
				+ "           isur.k_code as isur_kcode, "
				+ "       remittance_sent as rmtnce_sent, "
				+ "   remittance_received as rmtnce_received, "
				+ "                c.name as currency_name "
				+ "from "+bsTable+" bs "
				+ "inner join company rcvr on rcvr.id=bs.receiving_company_id "
				+ "inner join company isur on isur.id=bs.issuing_company_id "
				+ "left  join currency c   on    c.id=bs.currency_id "
				+ "where bs.id = :sid ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("sid", statementId);

		List<BillStatementImpl> resultList = getNamedParameterJdbcTemplate().query(sql,q ,
				(rs , rowNum) -> new BillStatementImpl(
						rs.getInt("id"), rs.getString("display_name"), rs.getDate("date_start"),
						rs.getDate("date_end") , rs.getString("isur_kcode"), rs.getString("rcvr_kcode"),
						rs.getBigDecimal("rmtnce_sent"), rs.getBigDecimal("rmtnce_received"),
						rs.getString("currency_name"), rs.getBigDecimal("total"), rs.getBigDecimal("previous_balance"),
						rs.getBigDecimal("balance")
				));
		Assert.isTrue(resultList.size()==1,"resultList.size()==1");
		BillStatementImpl statement = resultList.get(0);
		return statement;
	}
	
	@Override @SuppressWarnings("unchecked")
	public List<BillStatementLineItem> queryLineItems(BillStatementType bsType,int statementId) {
		String bsliTable = bsType.getDbTableStatementLineItem();
		String sql = "select     bsli.id as id, "
				+ "    bsli.statement_id as statement_id, "
				+ "        bsli.line_seq as line_seq, "
				+ "             slt.name as statement_line_type, "
				+ "   currency_orig.name as original_currency, "
				+ " bsli.original_amount as original_amount, "
				+ "   currency_stmt.name as statement_currency, "
				+ "bsli.statement_amount as statement_amount, "
				+ "       pb.code_by_drs as product_base_code, "
				+ "       ps.code_by_drs as product_name, "
				+ "        bsli.quantity as quantity, "
				+ "             tlt.name as settleable_item, "
				+ "       bsli.reference as reference, "
				+ "           cntry.code as country_name "
				+ "from "+bsliTable+" bsli "
				+ "inner join statement_line_type slt on slt.id = bsli.stlmnt_line_item_type_id "
				+ "inner join currency currency_orig on currency_orig.id =bsli.original_currency_id "
				+ "left  join currency currency_stmt on currency_stmt.id =bsli.statement_currency_id "
				+ "left  join product_base pb on pb.id = bsli.product_base_id "
				+ "left  join product_sku ps on ps.id = bsli.product_sku_id "
				+ "inner join transactionlinetype tlt on tlt.id = bsli.transactionlinetype_id "
				+ "inner join country cntry on cntry.id = bsli.country_id "
				+ "where statement_id = :sid ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("sid", statementId);


		List rList =  getNamedParameterJdbcTemplate().query(sql,q ,(rs , rowNum) ->
				new BillStatementLineItemImpl(
						rs.getInt("id"),rs.getInt("statement_id"),rs.getInt("line_seq"),
						rs.getString("statement_line_type"),rs.getString("original_currency"),rs.getBigDecimal("original_amount"),
						rs.getString("statement_currency"),rs.getBigDecimal("statement_amount"),rs.getString("product_base_code"),
						rs.getString("product_name"),rs.getInt("quantity"),rs.getString("settleable_item"),
						rs.getString("reference"),rs.getString("country_name")
						));

		return rList;
	}

	@Override @SuppressWarnings("unchecked")
	public List<BillStatementLineItem> queryLineItems(BillStatementType bsType, int statementId,List<String> typeNameList) {
		String bsliTable = bsType.getDbTableStatementLineItem();
		String sql = "select     bsli.id as id, "
				+ "    bsli.statement_id as statement_id, "
				+ "        bsli.line_seq as line_seq, "
				+ "             slt.name as statement_line_type, "
				+ "   currency_orig.name as original_currency, "
				+ " bsli.original_amount as original_amount, "
				+ "   currency_stmt.name as statement_currency, "
				+ "bsli.statement_amount as statement_amount, "
				+ "       pb.code_by_drs as product_base_code, "
				+ "       ps.code_by_drs as product_name, "
				+ "        bsli.quantity as quantity, "
				+ "             tlt.name as settleable_item, "
				+ "       bsli.reference as reference, "
				+ "           cntry.code as country_name "
				+ "from "+bsliTable+" bsli "
				+ "inner join statement_line_type slt on slt.id = bsli.stlmnt_line_item_type_id "
				+ "inner join currency currency_orig on currency_orig.id =bsli.original_currency_id "
				+ "left  join currency currency_stmt on currency_stmt.id =bsli.statement_currency_id "
				+ "left  join product_base pb on pb.id = bsli.product_base_id "
				+ "left  join product_sku ps on ps.id = bsli.product_sku_id "
				+ "inner join transactionlinetype tlt on tlt.id = bsli.transactionlinetype_id "
				+ "inner join country cntry on cntry.id = bsli.country_id "
				+ "where statement_id = :sid and tlt.name in (:typeNameList) ";

		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("sid", statementId);
		q.addValue("typeNameList", typeNameList);

		List rList =  getNamedParameterJdbcTemplate().query(sql,q ,(rs , rowNum) ->
				new BillStatementLineItemImpl(
						rs.getInt("id"),rs.getInt("statement_id"),rs.getInt("line_seq"),
						rs.getString("statement_line_type"),rs.getString("original_currency"),rs.getBigDecimal("original_amount"),
						rs.getString("statement_currency"),rs.getBigDecimal("statement_amount"),rs.getString("product_base_code"),
						rs.getString("product_name"),rs.getInt("quantity"),rs.getString("settleable_item"),
						rs.getString("reference"),rs.getString("country_name")
				));

		return rList;
	}

	@Override @Transactional("transactionManager")
	public void updateStatement(BillStatement statement,BillStatementType bsType){
		String bsTable = bsType.getDbTableStatement();
		String sql = "update "+bsTable+" bs set "
				+ "              total = :total, "
				+ "   previous_balance = :previousBalance, "
				+ "    remittance_sent = :sent, "
				+ "remittance_received = :received, "
				+ "            balance = :balance, "
				+ "        currency_id = c.id "
				+ "from currency c "
				+ "where bs.id = :sid and c.name=:currency ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("total", statement.getTotal());
		q.addValue("previousBalance", statement.getPreviousBalance());
		q.addValue("sent", statement.getRemittanceIsurToRcvr());
		q.addValue("received", statement.getRemittanceRcvrToIsur());
		q.addValue("currency", Currency.valueOf(statement.getCurrency()).name());
		q.addValue("balance", statement.getBalance());
		q.addValue("sid", statement.getId());

		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1,"q.executeUpdate()==1");
	}
	
	@Override @Transactional("transactionManager")
	public void updateStatementLineItemAmount(BillStatementLineItem item,BillStatementType bsType) {
		String bsliTable = bsType.getDbTableStatementLineItem();
		String sql = "update "+bsliTable+" set "
				+ "statement_amount = :amt, "
				+ "statement_currency_id = c.id "
				+ "from currency c  "
				+ "where c.name=:currency and statement_id = :sid and line_seq = :seq";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("amt", item.getStatementAmount());
		q.addValue("currency", Currency.valueOf(item.getStatementCurrency()).name());
		q.addValue("sid", item.getStatementId());
		q.addValue("seq", item.getLineSeq());
		getNamedParameterJdbcTemplate().update(sql,q);
	}
	
	@Override @Transactional("transactionManager")
	public void updateStatementLineItemReference(BillStatementLineItem item, BillStatementType type) {
		String bsliTable = type.getDbTableStatementLineItem();
		String sql = "update "+bsliTable+" set reference = :reference where statement_id = :sid and line_seq = :seq ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("reference", item.getSourcePoId());
		q.addValue("sid", item.getStatementId());
		q.addValue("seq", item.getLineSeq());
		getNamedParameterJdbcTemplate().update(sql,q);
	}

	@Override @SuppressWarnings("unchecked")
	public Remittance getRemittanceIsur2RcvrTotal(Date startDate, Date endDate,String isurKcode, String rcvrKcode) {
		String sql = "select 'Sent' as type, "
				+ "          c.name as currency, "
				+ "     sum(amount) as amount "
				+ "from remittance rmt "
				+ "inner join currency   c on    c.id = rmt.currency_id "
				+ "inner join company sndr on sndr.id = rmt.sender_company_id "
				+ "inner join company rcvr on rcvr.id = rmt.receiver_company_id "
				+ "where date_sent >=:sdate "
				+ "and   date_sent <=:edate "
				+ "and   sndr.k_code = :isurKcode "
				+ "and   rcvr.k_code = :rcvrKcode "
				+ "group by currency ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("sdate", startDate);
		q.addValue("edate", endDate);
		q.addValue("isurKcode", isurKcode);
		q.addValue("rcvrKcode", rcvrKcode);


		List<Remittance> resultList = (List) getNamedParameterJdbcTemplate().query(sql,q,
				(rs, rowNum) -> new RemittenceImpl(rs.getString("type") , rs.getString("currency") ,rs.getBigDecimal("amount")));

		if ((resultList == null) || (resultList.isEmpty())) return null;
		Assert.isTrue(resultList.size()==1,"Multi Currency in Rmt Isur to Rcvr");
		return resultList.get(0);
	}
	
	@Override @SuppressWarnings("unchecked")
	public Remittance getRemittanceRcvr2IsurTotal(Date startDate, Date endDate, String isurKcode, String rcvrKcode) {
		String sql = "select 'Received' as type, "
				+ "              c.name as currency, "
				+ "         sum(amount) as amount "
				+ "from remittance rmt "
				+ "inner join currency c on c.id = rmt.currency_id "
				+ "inner join company sndr on sndr.id = rmt.sender_company_id "
				+ "inner join company rcvr on rcvr.id = rmt.receiver_company_id "
				+ "where date_received >= :sdate "
				+ "and   date_received <= :edate "
				+ "and   sndr.k_code = :sndr "
				+ "and   rcvr.k_code = :rcvr "
				+ "group by currency ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("sdate", startDate);
		q.addValue("edate", endDate);
		q.addValue("sndr", rcvrKcode);
		q.addValue("rcvr", isurKcode);

		List<Remittance> resultList = (List) getNamedParameterJdbcTemplate().query(sql,q,
				(rs, rowNum) -> new RemittenceImpl(rs.getString("type") , rs.getString("currency") ,rs.getBigDecimal("amount")));

		if ((resultList == null) || (resultList.isEmpty())) return null;
		Assert.isTrue(resultList.size()==1,"Multi Currency in Rmt Rcvr to Isur");
		return resultList.get(0);
	}
	
	@Override @SuppressWarnings("unchecked")
	public String queryNewestRelatedShipmentNameInLineItem(int statementId) {
		String sql = "select pss.shipment_name from bill_statementlineitem bsli "
				+ "inner join product_sku_stock pss on pss.shipment_name = bsli.reference "
				+ "inner join shipment shp on shp.name = pss.shipment_name "
				+ "inner join shipment_uns_info sui on sui.shipment_id = shp.id "
				+ "where bsli.statement_id = :statementId "
				+ "order by sui.arrival_date desc "
				+ "offset 0 limit 1 ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementId", statementId);
		List<String> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		Assert.isTrue(resultList.size()==1,""+resultList.size());
		return resultList.get(0);
	}
	
	@Override @SuppressWarnings("unchecked")
	public BillStatement getExistingStatement(int statementId,BillStatementType bsType) {
		String bsTable = bsType.getDbTableStatement();
		String sql = "select        bs.id as id, "
				+ "               bs.name as display_name, "
				+ "          period_start as date_start, "
				+ "            period_end as date_end, "
				+ "                 total as total, "
				+ "      previous_balance as previous_balance, "
				+ "               balance as balance, "
				+ "           rcvr.k_code as rcvr_kcode, "
				+ "           isur.k_code as isur_kcode, "
				+ "       remittance_sent as rmtnce_sent, "
				+ "   remittance_received as rmtnce_received, "
				+ "                c.name as currency_name "
				+ "from "+bsTable+" bs "
				+ "inner join company rcvr on rcvr.id=bs.receiving_company_id "
				+ "inner join company isur on isur.id=bs.issuing_company_id "
				+ "left  join currency c   on    c.id=bs.currency_id "
				+ "where bs.id = :sid ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("sid", statementId);


		List<BillStatementImpl> resultList = getNamedParameterJdbcTemplate().query(sql,q ,
				(rs , rowNum) -> new BillStatementImpl(
						rs.getInt("id"), rs.getString("display_name"), rs.getDate("date_start"),
						rs.getDate("date_end") , rs.getString("isur_kcode"), rs.getString("rcvr_kcode"),
						rs.getBigDecimal("rmtnce_sent"), rs.getBigDecimal("rmtnce_received"),
						rs.getString("currency_name"), rs.getBigDecimal("total"), rs.getBigDecimal("previous_balance"),
						rs.getBigDecimal("balance")
				));


		Assert.isTrue(resultList.size()==1||resultList.size()==0,"size:"+resultList.size());
		if(resultList.size()==0) return null;
		BillStatementImpl statement = resultList.get(0);
		List<BillStatementLineItem> lineItems = this.getExistingStatementLineItems(statementId,bsType);
		statement.setLineItems(lineItems);
		return statement;
	}
	
	@Override
	public int queryMaxStatementSeq(String rcvrKcode,String isurKcode,BillStatementType bsType) {
		String bsTable = bsType.getDbTableStatement();
		String sql = "select max(sequence) from "+bsTable+" bs "
				+ "inner join company rcvr on rcvr.id = bs.receiving_company_id "
				+ "inner join company isur on isur.id = bs.issuing_company_id "
				+ "where rcvr.k_code= :rcvrKcode "
				+ "and   isur.k_code= :isurKcode ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("rcvrKcode", rcvrKcode);
		q.addValue("isurKcode", isurKcode);
		Integer o = getNamedParameterJdbcTemplate().queryForObject(sql,q ,Integer.class);
		if (o == null)
			return 0;
		return o;
	}
	
	@Transactional("transactionManager")
	private int getNextStatementId(String rcvrName,String isurName) {
		String sql = "select max(sequence) from bill_statement bs "
				+ "inner join company rcvr on rcvr.id = bs.receiving_company_id "
				+ "inner join company isur on isur.id = bs.issuing_company_id "
				+ "where rcvr.k_code= :rcvrName "
				+ "and   isur.k_code= :isurName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("rcvrName", rcvrName);
		q.addValue("isurName", isurName);
		Integer o = getNamedParameterJdbcTemplate().queryForObject(sql,q ,Integer.class);
		if (o == null)
			return 1;
		return o+1;
	}
	
	@SuppressWarnings("unchecked")
	private List<BillStatementLineItem> getExistingStatementLineItems(int statementId,BillStatementType bsType) {
		String bsliTable = bsType.getDbTableStatementLineItem();
		String sql = "select     bsli.id as id, "
				+ "    bsli.statement_id as statement_id, "
				+ "        bsli.line_seq as line_seq, "
				+ "             slt.name as statement_line_type, "
				+ "   currency_orig.name as original_currency, "
				+ " bsli.original_amount as original_amount, "
				+ "   currency_stmt.name as statement_currency, "
				+ "bsli.statement_amount as statement_amount, "
				+ "       pb.code_by_drs as product_base_code, "
				+ "       ps.code_by_drs as product_name, "
				+ "        bsli.quantity as quantity, "
				+ "              tlt.name as settleable_item, "
				+ "    bsli.reference as reference, "
				+ "           cntry.code as country_name "
				+ "from "+bsliTable+" bsli "
				+ "inner join statement_line_type slt on slt.id = bsli.stlmnt_line_item_type_id "
				+ "inner join currency currency_orig on currency_orig.id =bsli.original_currency_id "
				+ "left  join currency currency_stmt on currency_stmt.id =bsli.statement_currency_id "
				+ "left join product_base pb on pb.id = bsli.product_base_id "
				+ "left join product_sku ps on ps.id = bsli.product_sku_id "
				+ "inner join transactionlinetype tlt on tlt.id = bsli.transactionlinetype_id "
				+ "inner join country cntry on cntry.id = bsli.country_id "
				+ "where statement_id = :sid "
				+ "order by bsli.line_seq ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("sid", statementId);

		List<BillStatementLineItemImpl> resultList =  getNamedParameterJdbcTemplate().query(sql,q ,(rs , rowNum) ->
				new BillStatementLineItemImpl(
						rs.getInt("id"),rs.getInt("statement_id"),rs.getInt("line_seq"),
						rs.getString("statement_line_type"),rs.getString("original_currency"),rs.getBigDecimal("original_amount"),
						rs.getString("statement_currency"),rs.getBigDecimal("statement_amount"),rs.getString("product_base_code"),
						rs.getString("product_name"),rs.getInt("quantity"),rs.getString("settleable_item"),
						rs.getString("reference"),rs.getString("country_name")
				));

		if(resultList.size()==0) return null;
		return (List) resultList;
	}
	
	@Override
	public Date queryPreviousPeriodEndDate(String isurKcode, String rcvrKcode) {
		String sql = "select to_char(max(period_end) at time zone :timeZoneText, :dateTimeFormatText ) from bill_statement bs "
				+ "inner join company isur on isur.id = bs.issuing_company_id "
				+ "inner join company rcvr on rcvr.id = bs.receiving_company_id "
				+ "where isur.k_code = :isurKcode "
				+ "and   rcvr.k_code = :rcvrKcode ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("isurKcode", isurKcode);
		q.addValue("rcvrKcode", rcvrKcode);
		q.addValue("timeZoneText","UTC");
		q.addValue("dateTimeFormatText","YYYY-MM-DD HH24:MI OF00");
		String dateStr = getNamedParameterJdbcTemplate().queryForObject(sql,q,String.class);
		if (dateStr == null) return null;
		return DateHelper.toDate(dateStr,"yyyy-MM-dd HH:mm z");
	}
	
	@Override @Transactional("transactionManager")
	public void delete(BillStatementType type, String statementName) {
		String bsTable = type.getDbTableStatement();
		this.deleteLineItems(type, statementName);
		String sql = "delete from "+bsTable+" where name = :statementName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1,"q.executeUpdate()==1");
	}
	
	@Transactional("transactionManager")
	private void deleteLineItems(BillStatementType type, String statementName){
		String bsTable = type.getDbTableStatement();
		String bsliTable = type.getDbTableStatementLineItem();
		String sql = "delete from "+bsliTable+" bsli using "+bsTable+" bs "
				+ "where bs.id = bsli.statement_id and bs.name = :statementName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		getNamedParameterJdbcTemplate().update(sql,q);
	}
	
	@Override @SuppressWarnings("unchecked")
	public String queryIsurKcode(BillStatementType type, String statementName) {
		String bsTable = type.getDbTableStatement();
		String sql = "select com.k_code from "+bsTable+" bs "
				+ "inner join company com on com.id = bs.issuing_company_id "
				+ "where bs.name = :statementName and com.is_drs_company = TRUE ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		List<String> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		Assert.isTrue(resultList.size()==1,"resultList.size()==1");
		return resultList.get(0);
	}
	
	@Override @SuppressWarnings("unchecked")
	public String queryRcvrKcode(BillStatementType type, String statementName) {
		String bsTable = type.getDbTableStatement();
		String sql = "select com.k_code from "+bsTable+" bs "
				+ "inner join company com on com.id = bs.receiving_company_id "
				+ "where bs.name = :statementName and com.is_drs_company = TRUE ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		List<String> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		Assert.isTrue(resultList.size()==1,"resultList.size()==1");
		return resultList.get(0);
	}
	
	@Override @SuppressWarnings("unchecked")
	public Date queryStartDateOfDraft(String statementName) {
		String sql = "select dbs.period_start from draft_bill_statement dbs where dbs.name = :statementName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		List<Date> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,Date.class);
		Assert.isTrue(resultList.size()==1,"resultList.size()==1");
		return resultList.get(0);
	}

	@Override @SuppressWarnings("unchecked")
	public Date queryEndDateOfDraft(String statementName) {
		String sql = "select dbs.period_end from draft_bill_statement dbs where dbs.name = :statementName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		List<Date> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,Date.class);
		Assert.isTrue(resultList.size()==1,"resultList.size()==1");
		return resultList.get(0);
	}

	@Override
	public String queryNewestStatementName(String isurKcode, String rcvrKcode) {
		String sql = "select bs.name from bill_statement bs "
				+ "inner join company isur on isur.id = bs.issuing_company_id "
				+ "inner join company rcvr on rcvr.id = bs.receiving_company_id "
				+ "where isur.k_code = :isurKcode and rcvr.k_code = :rcvrKcode "
				+ "and period_start = ( "
				+ "    select max(period_start) from bill_statement bs "
				+ "    inner join company isur on isur.id = bs.issuing_company_id "
				+ "    inner join company rcvr on rcvr.id = bs.receiving_company_id "
				+ "    where isur.k_code = :isurKcode and rcvr.k_code = :rcvrKcode "
				+ ")";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("isurKcode", isurKcode);
		q.addValue("rcvrKcode", rcvrKcode);
		return getNamedParameterJdbcTemplate().queryForObject(sql,q,String.class);
	}

	@Override @SuppressWarnings("unchecked")
	public Map<String,String> querySkuToNewestArrivalUnsNameMap(BillStatementType type, int statementId) {
		String bsTable = type.getDbTableStatement();
		String sql = "select sku_latest_arrival.code_by_drs, uns.name "
				+ "from shipment uns "
				+ "inner join shipment_uns_info shpui on shpui.shipment_id = uns.id "
				+ "inner join ("
				+ "    select ps.code_by_drs, max(shpui.arrival_date) "
				+ "    from shipment uns "
				+ "    inner join shipment_uns_info shpui on shpui.shipment_id = uns.id "
				+ "    inner join shipment_line_item unsli on unsli.shipment_id = uns.id "
				+ "    inner join product_sku ps on ps.id = unsli.sku_id "
				+ "    inner join "+bsTable+" bs on bs.id = :statementId "
				+ "    where shpui.arrival_date < bs.period_end "
				+ "    and bs.issuing_company_id = uns.buyer_company_id "
				+ "    and bs.receiving_company_id = uns.seller_company_id "
				+ "    group by ps.code_by_drs "
				+ ") sku_latest_arrival on sku_latest_arrival.max = shpui.arrival_date ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementId", statementId);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,super.objArrayMapper);
		Map<String,String> resultMap = new HashMap<String,String>();
		for(Object[] columns:columnsList){
			String sku = (String)columns[0];
			String unsName = (String)columns[1];
			resultMap.put(sku, unsName);
		}
		return resultMap;
	}
	
	@Override @SuppressWarnings("unchecked")
	public List<String> queryDraftStatementNameList() {
		String sql = "select name from draft_bill_statement bs "
				+ "inner join company rcvr on rcvr.id=bs.receiving_company_id "
				+ "inner join company isur on isur.id=bs.issuing_company_id "
				+ "where rcvr.is_drs_company=TRUE "
				+ "and isur.is_drs_company=TRUE "
				+ "order by name ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		return getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
	}
	
	@Override
	public boolean existMs2ssStatement(Date startPoint, Date endPoint) {
		StringBuilder sqlSb = new StringBuilder()
				.append("select exists( ")
				.append("    select 1 from bill_statement bs ")
				.append("    inner join company isur on isur.id = bs.issuing_company_id ")
				.append("    inner join company rcvr on rcvr.id = bs.receiving_company_id ")
				.append("    where true ")
				.append("    and isur.is_drs_company = true ")
				.append("    and rcvr.is_drs_company = true ")
				.append("    and bs.period_start = :startDateTimePoint ")
				.append("    and bs.period_end   = :endDateTimePoint ")
				.append(") ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("startDateTimePoint", startPoint);
		q.addValue("endDateTimePoint", endPoint);
		Boolean result = getNamedParameterJdbcTemplate().queryForObject(sqlSb.toString(), q,Boolean.class);
		Assert.notNull(result,"result null");
		return (boolean)result;
	}
	
	@Override @Transactional("transactionManager")
	public int doImportDutyItems(BillStatementType bsType, int statementId, int beginIndex, int startIndex, int endIndex) {
		String bsTable = bsType.getDbTableStatement();
		String bsliTable = bsType.getDbTableStatementLineItem();
		String sql = "insert into "+bsliTable
				+ "( statement_id, line_seq, stlmnt_line_item_type_id, original_currency_id, original_amount, product_sku_id, quantity, transactionlinetype_id, reference, country_id ) "
				+ "select "
				+ "  statement_id, line_seq, stlmnt_line_item_type_id, original_currency_id, original_amount, product_sku_id, quantity, transactionlinetype_id, reference, country_id "
				+ "from("
				+ "    select "
				+ "                                    (select id from "+bsTable+" where id = :sid) as statement_id, "
				+ "    row_number() over (order by r.product_sku_id, r.transactionlinetype_id) + :beginLineSeq as line_seq, "
				+ "    r.* "
				+ "    from ( "
				+ "        select "
				+ "        tlt.ms2ss_stmnt_line_type_id as stlmnt_line_item_type_id, "
				+ "                     idt.currency_id as original_currency_id, "
		        + "                  -sum(idtli.amount) as original_amount, "
		        + "                        unsli.sku_id as product_sku_id, "
				+ "                sum(unsli.qty_order) as quantity, "
				+ "                              tlt.id as transactionlinetype_id, "
				+ "                            uns.name as reference, "
				+ "          uns.destination_country_id as country_id "
				+ "        from import_duty_transaction idt "
				+ "        inner join import_duty_transaction_line_item idtli on idtli.transaction_id = idt.id "
				+ "        inner join shipment_line_item unsli on unsli.id = idtli.shipment_uns_line_item_id "
				+ "        inner join shipment uns on uns.id = unsli.shipment_id "
				+ "        inner join company uns_seller on uns_seller.id = uns.seller_company_id "
				+ "        inner join transactionlinetype tlt on tlt.name = :importDutyTypeName "
				+ "        inner join "+bsTable+" bs on bs.id = :sid "
			    + "        where true "
			    + "        and bs.issuing_company_id = uns.buyer_company_id "
		        + "        and bs.receiving_company_id = uns.seller_company_id "
			    + "        and idt.transaction_date >= bs.period_start "
			    + "        and idt.transaction_date  < bs.period_end "
			    + "        and uns.serial_id <= 123 "     // old import duty transaction that is need to be settled exclude UNS-K2-120
			    + "        and uns.serial_id != 120 "     // old import duty transaction that is need to be settled exclude UNS-K2-120
			    + "        and uns_seller.k_code = 'K2' " // old import duty transaction that is need to be settled exclude UNS-K2-120
				+ "        group by tlt.id, tlt.ms2ss_stmnt_line_type_id, unsli.sku_id, idt.currency_id, uns.name, uns.destination_country_id "
				+ "    ) as r "
				+ ") as r1 "
				+ "where r1.line_seq >= :start and r1.line_seq <= :end ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("sid", statementId);
		q.addValue("importDutyTypeName", TransactionLineType.MS2SS_PURCHASE_ALWNC_IMPORT_DUTY.getName());
		q.addValue("start", startIndex + beginIndex);
		q.addValue("end", endIndex + beginIndex);
		q.addValue("beginLineSeq", beginIndex);
		int insertedRows = getNamedParameterJdbcTemplate().update(sql,q);
		return insertedRows;
	}
	
	@Override @Transactional("transactionManager")
	public int doPaymentOnBehalfRelatedItemImportDuty(int statementId, int beginLineSeq, int startIndex, int endIndex,String bsRcvrKcode, String bsIsurKcode, BillStatementType bsType) {
		String bsTable = bsType.getDbTableStatement();
		String bsliTable = bsType.getDbTableStatementLineItem();
		String sql = "insert into "+bsliTable
				+ "( statement_id, line_seq, stlmnt_line_item_type_id, original_currency_id, original_amount, transactionlinetype_id, country_id ) "
				+ "select "
				+ "  statement_id, line_seq, stlmnt_line_item_type_id, original_currency_id, original_amount, transactionlinetype_id, country_id "
				+ "from("
				+ "    select "
				+ "                          (select id from "+bsTable+" where id = :sid) as statement_id, "
				+ "    row_number() over (order by r.transactionlinetype_id) + :beginLineSeq as line_seq, "
				+ "    r.* "
				+ "    from ( "
				+ "        select "
				+ "        tlt.ms2ss_stmnt_line_type_id as stlmnt_line_item_type_id, "
				+ "                     idt.currency_id as original_currency_id, "
				+ "                   sum(idtli.amount) as original_amount, "
				+ "                              tlt.id as transactionlinetype_id, "
				+ "                            uns.name as reference, "
				+ "                     ssdc.country_id as country_id "
				+ "        from import_duty_transaction idt "
				+ "        inner join import_duty_transaction_line_item idtli on idtli.transaction_id = idt.id "
				+ "        inner join shipment_line_item unsli on unsli.id = idtli.shipment_uns_line_item_id "
				+ "        inner join shipment uns on uns.id = unsli.shipment_id "
				+ "        inner join company uns_seller on uns_seller.id = uns.seller_company_id "
				+ "        inner join transactionlinetype tlt on tlt.name = :importDutyTypeName "
				+ "        inner join "+bsTable+" bs on bs.id = :sid "
				+ "        inner join company ssdc on ssdc.id = bs.receiving_company_id "
			    + "        where idt.pay_by_ssdc = TRUE "
			    + "        and bs.issuing_company_id = uns.buyer_company_id "
		        + "        and bs.receiving_company_id = uns.seller_company_id "
			    + "        and idt.transaction_date >= bs.period_start "
			    + "        and idt.transaction_date <  bs.period_end "
			    + "        and uns.serial_id <= 123 "     // old import duty transaction that is need to be settled exclude UNS-K2-120
			    + "        and uns.serial_id != 120 "     // old import duty transaction that is need to be settled exclude UNS-K2-120
			    + "        and uns_seller.k_code = 'K2' " // old import duty transaction that is need to be settled exclude UNS-K2-120
				+ "        group by tlt.id, tlt.ms2ss_stmnt_line_type_id, idt.currency_id, uns.name, ssdc.country_id "
				+ "    ) as r "
				+ ") as r1 "
				+ "where r1.line_seq >= :start and r1.line_seq <= :end ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("sid", statementId);
		q.addValue("importDutyTypeName", TransactionLineType.MS2SS_SSDC_PAYMENT_ONBEHALF_OF_MSDC_FOR_IMPORT_DUTY.getName());
		q.addValue("start", startIndex + beginLineSeq);
		q.addValue("end", endIndex + beginLineSeq);
		q.addValue("beginLineSeq", beginLineSeq);
		return getNamedParameterJdbcTemplate().update(sql,q);
	}

}
