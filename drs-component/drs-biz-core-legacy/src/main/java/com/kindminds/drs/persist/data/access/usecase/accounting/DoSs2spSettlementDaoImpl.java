package com.kindminds.drs.persist.data.access.usecase.accounting;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.kindminds.drs.api.data.access.usecase.accounting.DoSs2spSettlementDao;
import com.kindminds.drs.enums.BillStatementType;

import com.kindminds.drs.persist.data.access.rdb.Dao;
import com.kindminds.drs.persist.data.access.rdb.util.PostgreSQLHelper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.kindminds.drs.Country;
import com.kindminds.drs.Currency;
import com.kindminds.drs.TransactionLineType;

import com.kindminds.drs.api.v1.model.accounting.StatementLineType;
import com.kindminds.drs.api.v1.model.close.BillStatement;
import com.kindminds.drs.api.v1.model.close.BillStatement.BillStatementLineItem;
import com.kindminds.drs.api.v1.model.close.BillStatementLineItemCustomerCaseExemptionInfo;
import com.kindminds.drs.api.v1.model.close.BillStatementProfitShareItem;
import com.kindminds.drs.api.v1.model.close.CustomerCaseMsdcMsgChargeInfo;
import com.kindminds.drs.persist.v1.model.mapping.close.BillStatementImpl;
import com.kindminds.drs.persist.v1.model.mapping.close.BsliCustomerCaseExemptionInfoImpl;
import com.kindminds.drs.persist.v1.model.mapping.close.BillStatementLineItemImpl;
import com.kindminds.drs.persist.v1.model.mapping.close.BillStatementProfitShareItemImpl;

import com.kindminds.drs.util.DateHelper;

@Repository
public class DoSs2spSettlementDaoImpl extends Dao implements DoSs2spSettlementDao {
	
	
	
	@Override
	public Date queryPeriodStartDate(BillStatementType bsType, int statementId) {
		String bsTable = bsType.getDbTableStatement();
		String sql = "select bs.period_start from "+bsTable+" bs where bs.id = :statementId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementId", statementId);
		Date result = getNamedParameterJdbcTemplate().queryForObject(sql,q,Date.class);
		Assert.notNull(result,"result null");
		return result;
	}

	@Override
	public Date queryPeriodEndDate(BillStatementType bsType,int statementId) {
		String bsTable = bsType.getDbTableStatement();
		String sql = "select bs.period_end from "+bsTable+" bs where bs.id = :statementId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementId", statementId);
		Date result =  getNamedParameterJdbcTemplate().queryForObject(sql,q,Date.class);
		Assert.notNull(result,"result null");
		return result;
	}
	
	@Override
	public Date queryPreviousPeriodEndDate(String supplierKcode) {
		String sql = "select to_char(max(period_end) at time zone :timeZoneText, :dateTimeFormatText ) from bill_statement bs "
				+ "inner join company c on c.id = bs.receiving_company_id "
				+ "where c.k_code = :supplierKcode ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("supplierKcode", supplierKcode);
		q.addValue("timeZoneText","UTC");
		q.addValue("dateTimeFormatText","YYYY-MM-DD HH24:MI OF00");
		String dateStr = getNamedParameterJdbcTemplate().queryForObject(sql,q,String.class);
		if (dateStr == null) return null;
		return DateHelper.toDate(dateStr,"yyyy-MM-dd HH:mm z");
	}

	@Override
	public Integer queryMaxStatementSeq(String supplierKcode,BillStatementType bsType) {
		String bsTable = bsType.getDbTableStatement();
		String sql = "select max(sequence) from "+bsTable+" bs "
				+ "inner join company c on c.id = bs.receiving_company_id "
				+ "where c.k_code = :supplierKcode ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("supplierKcode", supplierKcode);
		Integer o = null;
		try{
			o = getNamedParameterJdbcTemplate().queryForObject(sql,q,Integer.class);
		}catch (EmptyResultDataAccessException ex){}

		if (o == null)
			return 0;
		return Integer.parseInt(o.toString());
	}

	@Override @Transactional("transactionManager")
	public int insertStatement(Integer stmntSeq, String stmntName, String supplierKcode, String isurKcode, Date startDate, Date endDate,
							   int versionNumber,BillStatementType bsType) {
		String bsTable = bsType.getDbTableStatement();

		int dbId = PostgreSQLHelper.getNextVal(getNamedParameterJdbcTemplate(),"bill_statement","id");
		String sql = "insert into "+bsTable
				+ "(  id,  sequence,       name, period_start, period_end, issuing_company_id, receiving_company_id, balance, version_number) "
				+ "select "
				+ "  :id, :stmntSeq, :stmntName,         :sdt,       :edt,            isur.id,              splr.id,    :bal, :versionNumber "
				+ "from company splr "
				+ "inner join company isur on isur.k_code = :isurKcode "
				+ "where splr.k_code = :splrKcode ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("id", dbId);
		q.addValue("stmntSeq", stmntSeq);
		q.addValue("stmntName", stmntName);
		q.addValue("sdt", startDate);
		q.addValue("edt", endDate);
		q.addValue("isurKcode", isurKcode);
		q.addValue("splrKcode", supplierKcode);
		q.addValue("bal",BigDecimal.ZERO);
		q.addValue("versionNumber",versionNumber);
		getNamedParameterJdbcTemplate().update(sql,q);
		return dbId;
	}
	
	@Override @Transactional("transactionManager")
	public int doDrsTransactionItem(int statementId, int beginLineSeq, String supplierKcode, int startIndex, int endIndex, TransactionLineType sItem, BillStatementType bsType) {
		String bsTable = bsType.getDbTableStatement();
		String bsliTable = bsType.getDbTableStatementLineItem();
		String sql = "insert into "+bsliTable
				+ "( statement_id, line_seq, stlmnt_line_item_type_id, original_currency_id, original_amount, product_sku_id, quantity, transactionlinetype_id, country_id ) "
				+ "select "
				+ "  statement_id, line_seq, stlmnt_line_item_type_id, original_currency_id, original_amount, product_sku_id, quantity, transactionlinetype_id, country_id "
				+ "from("
				+ "    select "
				+ "                                    (select id from "+bsTable+" where id = :sid) as statement_id, "
				+ "    row_number() over (order by r.country_id, r.product_sku_id, r.transactionlinetype_id) + :beginLineSeq as line_seq, "
				+ "    r.* "
				+ "    from ( "
				+ "        select "
				+ "        tlt.ss2sp_stmnt_line_type_id as stlmnt_line_item_type_id, "
				+ "                    dtli.currency_id as original_currency_id, "
				+ "                    sum(dtli.amount) as original_amount, "
				+ "                   dt.product_sku_id as product_sku_id, "
				+ "                    sum(dt.quantity) as quantity, "
				+ "                   dtli.item_type_id as transactionlinetype_id, "
				+ "                        m.country_id as country_id "
				+ "        from drs_transaction dt "
				+ "        inner join drs_transaction_line_item  dtli on dt.id = dtli.drs_transaction_id "
				+ "        inner join transactionlinetype tlt on dtli.item_type_id=tlt.id "
				+ "        inner join company rcvr on rcvr.id = dtli.rcvr_company_id "
				+ "        inner join marketplace m on m.id = dt.marketplace_id "
				+ "        inner join "+bsTable+" bs on bs.id = :sid "
				+ "        where tlt.name = :iname "
				+ "        and rcvr.k_code = :supplierKcode "
				+ "        and dt.transaction_date >= bs.period_start "
				+ "        and dt.transaction_date  < bs.period_end "
				+ "        group by dtli.item_type_id, tlt.ss2sp_stmnt_line_type_id, dt.product_sku_id, dtli.currency_id, m.country_id "
				+ "    ) as r "
				+ ") as r1 "
				+ "where r1.line_seq >= :start and r1.line_seq <= :end ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("sid", statementId);
		q.addValue("iname", sItem.getName());
		q.addValue("supplierKcode", supplierKcode);
		q.addValue("start", startIndex + beginLineSeq);
		q.addValue("end", endIndex + beginLineSeq);
		q.addValue("beginLineSeq", beginLineSeq);
		return 	getNamedParameterJdbcTemplate().update(sql,q);
	}
	
	@Override @Transactional("transactionManager")
	public int doImportDutyItem(int statementId, int beginLineSeq, int startIndex, int endIndex,TransactionLineType type, BillStatementType bsType) {
		String bsTable = bsType.getDbTableStatement();
		String bsliTable = bsType.getDbTableStatementLineItem();
		String sql = "insert into "+bsliTable
				+ "( statement_id, line_seq, stlmnt_line_item_type_id, original_currency_id, original_amount, product_sku_id, quantity, transactionlinetype_id, country_id ) "
				+ "select "
				+ "  statement_id, line_seq, stlmnt_line_item_type_id, original_currency_id, original_amount, product_sku_id, quantity, transactionlinetype_id, country_id "
				+ "from("
				+ "    select "
				+ "                                    (select id from "+bsTable+" where id = :sid) as statement_id, "
				+ "    row_number() over (order by r.country_id, r.product_sku_id, r.transactionlinetype_id) + :beginLineSeq as line_seq, "
				+ "    r.* "
				+ "    from ( "
				+ "        select "
				+ "        tlt.ss2sp_stmnt_line_type_id as stlmnt_line_item_type_id, "
				+ "                     idt.currency_id as original_currency_id, "
				+ "                  -sum(idtli.amount) as original_amount, "
				+ "                        unsli.sku_id as product_sku_id, "
				+ "                sum(unsli.qty_order) as quantity, "
				+ "                              tlt.id as transactionlinetype_id, "
				+ "          uns.actual_destination_id as country_id "
				+ "        from import_duty_transaction idt "
				+ "        inner join import_duty_transaction_line_item idtli on idtli.transaction_id = idt.id "
				+ "        inner join shipment_line_item unsli on unsli.id = idtli.shipment_uns_line_item_id "
				+ "        inner join shipment uns on uns.id = unsli.shipment_id "
				+ "        inner join transactionlinetype tlt on tlt.name = :importDutyName "
				+ "        inner join product_sku ps on ps.id = unsli.sku_id "
				+ "        inner join product_base pb on pb.id = ps.product_base_id "
				+ "        inner join company rcvr on rcvr.id = pb.supplier_company_id "
				+ "        inner join "+bsTable+" bs on bs.id = :sid "
				+ "        where pb.supplier_company_id = bs.receiving_company_id "
				+ "        and idt.transaction_date >= bs.period_start "
				+ "        and idt.transaction_date  < bs.period_end "
				+ "        group by tlt.id, tlt.ss2sp_stmnt_line_type_id, unsli.sku_id, idt.currency_id, uns.actual_destination_id "
				+ "    ) as r "
				+ ") as r1 "
				+ "where r1.line_seq >= :start and r1.line_seq <= :end ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("sid", statementId);
		q.addValue("importDutyName", type.getName());
		q.addValue("start", startIndex + beginLineSeq);
		q.addValue("end", endIndex + beginLineSeq);
		q.addValue("beginLineSeq", beginLineSeq);
		return getNamedParameterJdbcTemplate().update(sql,q);
	}
	
	@Override @Transactional("transactionManager")
	public int doInternationalTransactionItems(BillStatementType bsType,int statementId,int beginLineSeq,int startIndex,int endIndex,int cashFlowDirectionkey){
		String bsTable = bsType.getDbTableStatement();
		String bsliTable = bsType.getDbTableStatementLineItem();
		String signForSum = cashFlowDirectionkey==1?"-":"";
		String sql = "insert into "+bsliTable
				+ "( statement_id, line_seq, stlmnt_line_item_type_id, original_currency_id, original_amount, transactionlinetype_id, country_id ) "
				+ "select "
				+ "  statement_id, line_seq, stlmnt_line_item_type_id, original_currency_id, original_amount, transactionlinetype_id, country_id "
				+ "from("
				+ "    select "
				+ "                                                             (select id from "+bsTable+" where id = :sid) as statement_id, "
				+ "    row_number() over (order by r.country_id, r.transactionlinetype_id) + :beginLineSeq as line_seq, "
				+ "    r.* "
				+ "    from ( "
				+ "        select "
				+ "        tlt.ss2sp_stmnt_line_type_id as stlmnt_line_item_type_id, "
				+ "                      it.currency_id as original_currency_id, "
				+ signForSum + "    -(sum(itli.subtotal) + sum(vat_amount)) as original_amount, "
				+ "                              tlt.id as transactionlinetype_id, "
				+ "                     msdc.country_id as country_id "
				+ "        from international_transaction it "
				+ "        inner join international_transaction_line_item itli on itli.transaction_id = it.id "
				+ "        inner join company msdc on msdc.id = it.msdc_company_id "
				+ "        inner join transactionlinetype tlt on tlt.id = itli.type_id "
				+ "        inner join "+bsTable+" bs on bs.id = :sid "
				+ "        where tlt.class = :tltClassName "
				+ "        and it.splr_company_id = bs.receiving_company_id "
				+ "        and it.ssdc_company_id = bs.issuing_company_id "
				+ "        and it.transaction_date >= bs.period_start "
				+ "        and it.transaction_date <  bs.period_end "
				+ "        and it.cash_flow_direction_key = :cash_flow_direction_key "
				+ "        group by tlt.id, tlt.ss2sp_stmnt_line_type_id, it.currency_id, msdc.country_id "
				+ "    ) as r "
				+ ") as r1 "
				+ "where r1.line_seq >= :start and r1.line_seq <= :end ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("sid", statementId);
		q.addValue("tltClassName", "International");
		q.addValue("cash_flow_direction_key",cashFlowDirectionkey);
		q.addValue("start", startIndex + beginLineSeq);
		q.addValue("end", endIndex + beginLineSeq);
		q.addValue("beginLineSeq", beginLineSeq);
		return getNamedParameterJdbcTemplate().update(sql,q);
	}

	@Override @Transactional("transactionManager")
	public int doCustomerCareCaseItem(int statementId, int beginLineSeq, String supplierKcode, int startIndex,int endIndex,BillStatementType bsType) {
		String bsTable = bsType.getDbTableStatement();
		String bsliTable = bsType.getDbTableStatementLineItem();
		String sql = "insert into "+bsliTable
				+ "( statement_id, line_seq, stlmnt_line_item_type_id, original_currency_id, original_amount, product_sku_id, quantity, transactionlinetype_id, country_id ) "
				+ "select "
				+ "  statement_id, line_seq, stlmnt_line_item_type_id, original_currency_id, original_amount, product_sku_id, quantity, transactionlinetype_id, country_id "
				+ "from("
				+ "    select "
				+ "                                    (select id from "+bsTable+" where id = :sid) as statement_id, "
				+ "    row_number() over (order by r.product_sku_id, r.transactionlinetype_id) + :beginLineSeq as line_seq, "
				+ "    r.* "
				+ "    from ( "
				+ "        select "
				+ "         tlt.ss2sp_stmnt_line_type_id as stlmnt_line_item_type_id, "
				+ "                          currency.id as original_currency_id, "
				+ "           -sum(cccmri.charge_by_drs) as original_amount, "
				+ "        cccmri.related_product_sku_id as product_sku_id, "
				+ "                                    1 as quantity, "
				+ "                               tlt.id as transactionlinetype_id, "
				+ "                      hdlr.country_id as country_id "
				+ "        from customercarecase_message cccm "
				+ "        inner join currency on currency.name = :currencyName "
				+ "        inner join transactionlinetype tlt on tlt.name = :transactionLineTypeName "
				+ "        inner join customercarecase ccc on ccc.id = cccm.case_id "
				+ "        inner join customercarecase_message_reply_info cccmri on cccmri.msg_id = cccm.id "
				+ "        inner join company splr on splr.id = ccc.supplier_company_id "
				+ "        inner join company hdlr on hdlr.id = ccc.drs_company_id "
				+ "        inner join product_sku ps on ps.id = cccmri.related_product_sku_id "
				+ "        inner join "+bsTable+" bs on bs.id = :sid "
		        + "        where splr.k_code = :supplierKcode "
			    + "        and cccmri.date_finish >= bs.period_start "
			    + "        and cccmri.date_finish  < bs.period_end "
			    + "        and cccmri.is_free_of_charge = false "
			    + "        group by tlt.ss2sp_stmnt_line_type_id, currency.id, cccmri.related_product_sku_id, tlt.id, hdlr.country_id, ps.code_by_drs "
			    + "        order by ps.code_by_drs "
			+ "        ) as r "
			+ "    ) as r1 "
			+ "    where r1.line_seq >= :start and r1.line_seq <= :end ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("sid", statementId);
		q.addValue("transactionLineTypeName", TransactionLineType.MS2SS_PURCHASE_ALWNC_CUSTOMERCARE.getName());
		q.addValue("currencyName", Currency.USD.name());
		q.addValue("supplierKcode", supplierKcode);
		q.addValue("start", startIndex + beginLineSeq);
		q.addValue("end", endIndex + beginLineSeq);
		q.addValue("beginLineSeq", beginLineSeq);
		return getNamedParameterJdbcTemplate().update(sql,q);
	}

	@Override @Transactional("transactionManager")
	public void updateSs2spNameOfCustomerCareCaseMsdcMessage(int statementId) {
		String sql = "update customercarecase_message_reply_info cccmri set ss2sp_statement_name = bs.name "
				+ "from bill_statement bs "
				+ "where bs.id = :statementId and cccmri.id in ( "
				+ "    select cccmri.id from customercarecase_message_reply_info cccmri "
				+ "    inner join customercarecase_message cccm on cccm.id = cccmri.msg_id "
				+ "    inner join customercarecase ccc on ccc.id = cccm.case_id "
				+ "    inner join company splr on splr.id = ccc.supplier_company_id "
				+ "    inner join bill_statement bs on bs.id = :statementId "
				+ "    where splr.id = bs.receiving_company_id "
				+ "    and cccmri.date_finish >= bs.period_start "
			    + "    and cccmri.date_finish  < bs.period_end "
			    + ")";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementId", statementId);
		 getNamedParameterJdbcTemplate().update(sql,q);
	}
	
	@Override @Transactional("transactionManager")
	public int doPurchaseOrderRelatedItem(int statementId,int beginLineSeq,String supplierKcode,int startIndex,int endIndex,TransactionLineType itemName,BillStatementType bsType) {
		String bsTable = bsType.getDbTableStatement();
		String bsliTable = bsType.getDbTableStatementLineItem();
		String sql = "insert into  " +bsliTable
				+ "( statement_id, line_seq, stlmnt_line_item_type_id, original_currency_id, original_amount, product_sku_id, quantity, transactionlinetype_id, reference, country_id ) "
				+ "select "
				+ "  statement_id, line_seq, stlmnt_line_item_type_id, original_currency_id, original_amount, product_sku_id, quantity, transactionlinetype_id, reference, country_id "
			 	+ "from("
			 	+ "    select"
			 	+ "                                                (select id from "+bsTable+" where id = :sid) as statement_id, "
			 	+ "    row_number() over (order by r.country_id, r.reference, r.transactionlinetype_id, r.product_sku_id) + :beginSeq as line_seq,"
			 	+ "    r.* "
			 	+ "    from ( "
			 	+ "        select "
			 	+ "        tlt.ss2sp_stmnt_line_type_id as stlmnt_line_item_type_id, "
			 	+ "                    dtli.currency_id as original_currency_id, "
			 	+ "                    sum(dtli.amount) as original_amount, "
			 	+ "                   dt.product_sku_id as product_sku_id, "
			 	+ "                    sum(dt.quantity) as quantity, "
			 	+ "                   dtli.item_type_id as transactionlinetype_id, "
			 	+ "                dt.shipment_ivs_name as reference, "
			 	+ "                        m.country_id as country_id "
			 	+ "        from drs_transaction dt "
				+ "        inner join drs_transaction_line_item  dtli on dt.id = dtli.drs_transaction_id "
				+ "        inner join marketplace m on m.id = dt.marketplace_id "
				+ "        inner join transactionlinetype tlt on dtli.item_type_id = tlt.id "
				+ "        inner join company rcvr on rcvr.id = dtli.rcvr_company_id "
				+ "        inner join "+bsTable+" bs on bs.id = :sid "
			    + "        where tlt.name = :iname "
		        + "        and rcvr.k_code = :supplierKcode "
				+ "        and dt.transaction_date >= bs.period_start "
				+ "        and dt.transaction_date  < bs.period_end "
				+ "        group by dt.shipment_ivs_name, dtli.item_type_id, tlt.ss2sp_stmnt_line_type_id, dt.product_sku_id, dtli.currency_id, m.country_id "
				+ "    ) as r"
				+ ") as r1 "
				+ "where r1.line_seq >= :start and r1.line_seq <= :end ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("sid", statementId);
		q.addValue("iname", itemName.getName());
		q.addValue("beginSeq", beginLineSeq);
		q.addValue("supplierKcode", supplierKcode);
		q.addValue("start", startIndex + beginLineSeq);
		q.addValue("end", endIndex + beginLineSeq);
		return getNamedParameterJdbcTemplate().update(sql,q);
	}
	
	@Override @Transactional("transactionManager")
	public int doDomesticTransaction(int statementId, int beginLineSeq, String supplierKcode, int startIndex, int endIndex,String sltName, BillStatementType bsType){
		String bsTable = bsType.getDbTableStatement();
		String bsliTable = bsType.getDbTableStatementLineItem();
		String sql = "insert into "+bsliTable
				+ "( statement_id, line_seq, stlmnt_line_item_type_id, original_currency_id, original_amount, reference, country_id ) "
				+ "select "
				+ "  statement_id, line_seq, stlmnt_line_item_type_id, original_currency_id, original_amount, reference, country_id "
			 	+ "from("
			 	+ "    select"
			 	+ "                  (select id from "+bsTable+" where id = :sid) as statement_id, "
			 	+ "          row_number() over (order by r.reference) + :beginSeq as line_seq,"
			 	+ "    (select id from statement_line_type where name = :sltName) as stlmnt_line_item_type_id, "
			 	+ "    r.* "
			 	+ "    from ( "
			 	+ "        select dt.currency_id as original_currency_id, "
			 	+ "             -dt.amount_total as original_amount, "
			 	+ "                        dt.id as reference, "
			 	+ "              ssdc.country_id as country_id "
			 	+ "        from domestic_transaction dt "
			 	+ "        inner join company ssdc on ssdc.id = dt.ssdc_company_id "
				+ "        inner join company splr on splr.id = dt.splr_company_id "
				+ "        inner join "+bsTable+" bs on bs.id = :sid "
			    + "        where splr.k_code = :supplierKcode "
				+ "        and dt.transaction_date >= bs.period_start "
				+ "        and dt.transaction_date  < bs.period_end "
				+ "        order by dt.transaction_date "
				+ "    ) as r"
				+ ") as r1 "
				+ "where r1.line_seq >= :start and r1.line_seq <= :end ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("sid", statementId);
		q.addValue("sltName", sltName);
		q.addValue("beginSeq", beginLineSeq);
		q.addValue("supplierKcode", supplierKcode);
		q.addValue("start", startIndex + beginLineSeq);
		q.addValue("end", endIndex + beginLineSeq);
		return getNamedParameterJdbcTemplate().update(sql,q);
	}
	
	@Override @Transactional("transactionManager")
	public int insertProfitShareTax(int statementId, int beginLineSeq, Currency statementCurrency,BigDecimal amount, BillStatementType bsType) {
		String bsTable = bsType.getDbTableStatement();
		String bsliTable = bsType.getDbTableStatementLineItem();
		String sql = "insert into "+bsliTable
				+ "( statement_id, line_seq,     stlmnt_line_item_type_id, statement_currency_id, statement_amount, transactionlinetype_id,      country_id ) "
				+ "select "
				+ "         bs.id, :lineSeq, tlt.ss2sp_stmnt_line_type_id,           currency.id,          :amount,                 tlt.id, isur.country_id "
				+ "from "+bsTable+" bs "
				+ "inner join transactionlinetype tlt on tlt.name = :ssiVatName "
				+ "inner join currency on currency.name = :currencyName "
				+ "inner join company isur on isur.id = bs.receiving_company_id "
				+ "where bs.id = :statementId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("lineSeq", beginLineSeq+1);
		q.addValue("amount", amount);
		q.addValue("ssiVatName", TransactionLineType.SSI_VAT.getName());
		q.addValue("currencyName", statementCurrency.name());
		q.addValue("statementId", statementId);
		int rows =  getNamedParameterJdbcTemplate().update(sql,q);
		Assert.isTrue(rows==1,"rows==1");
		return beginLineSeq+rows;
	}

	@Override @SuppressWarnings("unchecked")
	public Object[] queryCurrencyAndTotalRemittanceFromIsurToRcvr(Date startDate, Date endDate,String isurKcode, String rcvrKcode) {
		String sql = "select c.name as currency, "
				+ "     sum(amount) as amount "
				+ "from remittance rmt "
				+ "inner join currency c on c.id = rmt.currency_id "
				+ "inner join company sndr on sndr.id = rmt.sender_company_id "
				+ "inner join company rcvr on rcvr.id = rmt.receiver_company_id "
				+ "where date_sent >= :sdate "
				+ "and date_sent <  :edate "
				+ "and sndr.k_code = :isurKcode "
				+ "and rcvr.k_code = :rcvrKcode "
				+ "group by currency ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("sdate", startDate);
		q.addValue("edate", endDate);
		q.addValue("isurKcode", isurKcode);
		q.addValue("rcvrKcode", rcvrKcode);
		List<Object[]> resultList = getNamedParameterJdbcTemplate().query(sql, q, objArrayMapper);
		Assert.isTrue(resultList.size()<=1,"Multi Currency in Rmt Isur to Rcvr");
		if ((resultList == null) || (resultList.isEmpty())) return null;
		return resultList.get(0);
	}
	
	@Override @SuppressWarnings("unchecked")
	public Object[] queryCurrencyAndTotalRemittanceFromRcvrToIsur(Date startDate, Date endDate, String isurKcode, String rcvrKcode) {
		String sql = "select c.name as currency, "
				+ "     sum(amount) as amount "
				+ "from remittance rmt "
				+ "inner join currency c on c.id = rmt.currency_id "
				+ "inner join company sndr on sndr.id = rmt.sender_company_id "
				+ "inner join company rcvr on rcvr.id = rmt.receiver_company_id "
				+ "where date_received >= :sdate "
				+ "and date_received <  :edate "
				+ "and sndr.k_code = :sndr "
				+ "and rcvr.k_code = :rcvr "
				+ "group by currency ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("sdate", startDate);
		q.addValue("edate", endDate);
		q.addValue("sndr", rcvrKcode);
		q.addValue("rcvr", isurKcode);
		List<Object[]> resultList = getNamedParameterJdbcTemplate().query(sql, q, objArrayMapper);
		Assert.isTrue(resultList.size()<=1,"Multi Currency in Rmt Rcvr to Isur");
		if ((resultList == null) || (resultList.isEmpty())) return null;
		return resultList.get(0);
	}

	@Override @SuppressWarnings("unchecked")
	public List<BillStatementLineItem> getAllLineItems(int statementId, BillStatementType bsType) {
		String bsliTable = bsType.getDbTableStatementLineItem();
		String sql = "select     bsli.id as id, "
				+ "    bsli.statement_id as statement_id, "
				+ "        bsli.line_seq as line_seq, "
				+ "             slt.name as statement_line_type, "
				+ "   currency_orig.name as original_currency, "
				+ " bsli.original_amount as original_amount, "
				+ "   currency_stmt.name as statement_currency, "
				+ "bsli.statement_amount as statement_amount, "
				+ "       ps.code_by_drs as product_name, "
				+ "        bsli.quantity as quantity, "
				+ "             tlt.name as settleable_item, "
				+ "    bsli.reference as reference, "
				+ "           cntry.code as country_name "
				+ "from "+bsliTable+" bsli "
				+ "inner join statement_line_type slt on slt.id = bsli.stlmnt_line_item_type_id "
				+ "inner join currency currency_orig on currency_orig.id =bsli.original_currency_id "
				+ "left  join currency currency_stmt on currency_stmt.id =bsli.statement_currency_id "
				+ "left  join product_sku ps on ps.id = bsli.product_sku_id "
				+ "inner join transactionlinetype tlt on tlt.id = bsli.transactionlinetype_id "
				+ "inner join country cntry on cntry.id = bsli.country_id "
				+ "where statement_id = :sid ";
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
		return (List)resultList;
	}

	@Override @SuppressWarnings("unchecked")
	public List<BillStatementLineItem> getAllProfitShareItems(int statementId,BillStatementType bsType) {
		String bsliTable = bsType.getDbTableStatementLineItem();
		String sql = "select     bsli.id as id, "
				+ "    bsli.statement_id as statement_id, "
				+ "        bsli.line_seq as line_seq, "
				+ "             slt.name as statement_line_type, "
				+ "   currency_orig.name as original_currency, "
				+ " bsli.original_amount as original_amount, "
				+ "   currency_stmt.name as statement_currency, "
				+ "bsli.statement_amount as statement_amount, "
				+ "       ps.code_by_drs as product_base_code, "
				+ "       ps.code_by_drs as product_name, "
				+ "        bsli.quantity as quantity, "
				+ "             tlt.name as settleable_item, "
				+ "    bsli.reference as reference, "
				+ "           cntry.code as country_name "
				+ "from "+bsliTable+" bsli "
				+ "inner join statement_line_type slt on slt.id = bsli.stlmnt_line_item_type_id "
				+ "inner join currency currency_orig on currency_orig.id =bsli.original_currency_id "
				+ "left  join currency currency_stmt on currency_stmt.id =bsli.statement_currency_id "
				+ "left  join product_base pb on pb.id = bsli.product_base_id "
				+ "left  join product_sku ps on ps.id = bsli.product_sku_id "
				+ "inner join transactionlinetype tlt on tlt.id = bsli.transactionlinetype_id "
				+ "inner join country cntry on cntry.id = bsli.country_id "
				+ "where statement_id = :sid and slt.name = :ssProfitShareForSpName";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("sid", statementId);
		q.addValue("ssProfitShareForSpName", StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP.getValue());

		List<BillStatementLineItemImpl> resultList =  getNamedParameterJdbcTemplate().query(sql,q ,(rs , rowNum) ->
				new BillStatementLineItemImpl(
						rs.getInt("id"),rs.getInt("statement_id"),rs.getInt("line_seq"),
						rs.getString("statement_line_type"),rs.getString("original_currency"),rs.getBigDecimal("original_amount"),
						rs.getString("statement_currency"),rs.getBigDecimal("statement_amount"),rs.getString("product_base_code"),
						rs.getString("product_name"),rs.getInt("quantity"),rs.getString("settleable_item"),
						rs.getString("reference"),rs.getString("country_name")
				));

		return (List)resultList;
	}

	@Override @Transactional("transactionManager")
	public void setLineItemStatementCurrencyAndAmount(BillStatementType bsType,int statementId,int lineItemLineSeq,Currency stmntCurrency,BigDecimal amount) {
		String bsliTable = bsType.getDbTableStatementLineItem();
		String sql = "update "+bsliTable+" set "
				+ "statement_currency_id = :stmntCurrencyId, "
				+ "statement_amount = :amount "
				+ "where statement_id = :sid and line_seq = :seq";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("stmntCurrencyId", stmntCurrency.getKey());
		q.addValue("amount", amount);
		q.addValue("sid", statementId);
		q.addValue("seq", lineItemLineSeq);
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1,"q.executeUpdate()==1");
	}

	@Override @SuppressWarnings("unchecked")
	public BillStatement getStatement(int statementId, BillStatementType bsType) {
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
	public BigDecimal queryProfitShareDestinationUntaxedTotal(BillStatementType bsType, int statementId) {
		String bspsTable = bsType.getDbTableStatementProfitShareItem();
		String sql = "select sum(statement_amount_untaxed) from "+bspsTable+" where statement_id = :sid";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("sid", statementId);

		List<BigDecimal> resultList= getNamedParameterJdbcTemplate().queryForList(sql,q,BigDecimal.class);
		Assert.isTrue(resultList.size()==1,"More than one exchange rate or No exchange rate found.");
		if(resultList.get(0)==null) return BigDecimal.ZERO;
		return resultList.get(0);
	}
	
	@Override @SuppressWarnings("unchecked")
	public BigDecimal queryPreviousBalance(int statementId, BillStatementType bsType) {
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
		List<BigDecimal> resultBalanceList= getNamedParameterJdbcTemplate().queryForList(sql,q,BigDecimal.class);
		if(resultBalanceList.size()==0) return BigDecimal.ZERO;
		Assert.isTrue(resultBalanceList.size()==1,"More than one exchange rate or No exchange rate found.");
		return resultBalanceList.get(0);
	}

	@Override @SuppressWarnings("unchecked")
	public BigDecimal queryVatRate(String handlerKcode) {
		String sql = "select vat_rate from country "
				+ "inner join company hdlr on hdlr.country_id = country.id "
				+ "where hdlr.k_code = :handlerKcode ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("handlerKcode", handlerKcode);
		List<BigDecimal> resultBalanceList= getNamedParameterJdbcTemplate().queryForList(sql,q,BigDecimal.class);
		if(resultBalanceList.size()==0) return BigDecimal.ZERO;
		Assert.isTrue(resultBalanceList.size()==1,"More than one rate or no rate found.");
		return resultBalanceList.get(0);
	}

	@Override @Transactional("transactionManager")
	public void updateStatement(BillStatement statement, BillStatementType bsType) {
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

		getNamedParameterJdbcTemplate().update(sql,q);
	}

	@Override @SuppressWarnings("unchecked")
	public BillStatement getExistingStatement(int statementId, BillStatementType bsType) {
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
				+ "where bs.id = :statementId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementId", statementId);

		List<BillStatementImpl> resultList = getNamedParameterJdbcTemplate().query(sql,q ,
				(rs , rowNum) -> new BillStatementImpl(
						rs.getInt("id"), rs.getString("display_name"), rs.getDate("date_start"),
						rs.getDate("date_end") , rs.getString("isur_kcode"), rs.getString("rcvr_kcode"),
						rs.getBigDecimal("rmtnce_sent"), rs.getBigDecimal("rmtnce_received"),
						rs.getString("currency_name"), rs.getBigDecimal("total"), rs.getBigDecimal("previous_balance"),
						rs.getBigDecimal("balance")
				));

		Assert.isTrue(resultList.size()==1||resultList.size()==0,"resultList.size()==1||resultList.size()==0");
		if(resultList.size()==0) return null;
		BillStatementImpl statement = resultList.get(0);
		statement.setProfitShareItems(this.queryProfitShareItems(bsType, statementId));
		statement.setLineItems(this.getExistingStatementLineItems(statementId,bsType));
		return statement;
	}
	
	@Override @SuppressWarnings("unchecked")
	public List<BillStatementLineItem> getExistingStatementLineItems(int statementId, BillStatementType bsType) {
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
				+ "    bsli.reference as reference, "
				+ "           cntry.code as country_name "
				+ "from "+bsliTable+" bsli "
				+ "inner join statement_line_type slt on slt.id = bsli.stlmnt_line_item_type_id "
				+ "left  join currency currency_orig on currency_orig.id =bsli.original_currency_id "
				+ "left  join currency currency_stmt on currency_stmt.id =bsli.statement_currency_id "
				+ "left  join product_base pb on pb.id = bsli.product_base_id "
				+ "left  join product_sku ps on ps.id = bsli.product_sku_id "
				+ "left  join transactionlinetype tlt on tlt.id = bsli.transactionlinetype_id "
				+ "inner join country cntry on cntry.id = bsli.country_id "
				+ "where bsli.statement_id = :statementId "
				+ "order by bsli.line_seq ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementId", statementId);

		List<BillStatementLineItemImpl> resultList =  getNamedParameterJdbcTemplate().query(sql,q ,(rs , rowNum) ->
				new BillStatementLineItemImpl(
						rs.getInt("id"),rs.getInt("statement_id"),rs.getInt("line_seq"),
						rs.getString("statement_line_type"),rs.getString("original_currency"),rs.getBigDecimal("original_amount"),
						rs.getString("statement_currency"),rs.getBigDecimal("statement_amount"),rs.getString("product_base_code"),
						rs.getString("product_name"),rs.getInt("quantity"),rs.getString("settleable_item"),
						rs.getString("reference"),rs.getString("country_name")
				));

		if(resultList.size()==0) return null;
		return (List)resultList;
	}

	@Override @SuppressWarnings("unchecked")
	public List<String> queryDraftStatementNameList(){
		String sql = "select name from draft_bill_statement order by name ";

		return getJdbcTemplate().queryForList(sql,String.class);
	}

	@Override @Transactional("transactionManager")
	public void deleteDraft(String statementName) {
		this.deleteDraftCustomerCostExemptionLineItems(statementName);
		this.deleteDraftLineItems(statementName);
		this.deleteProfitShareItems(statementName);
		String sql = "delete from draft_bill_statement where name = :statementName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1,"q.executeUpdate()==1");
	}
	
	@Transactional("transactionManager")
	private void deleteDraftCustomerCostExemptionLineItems(String statementName){
		String sql = "delete from draft_bill_statementlineitem_customercase_exemption_info dbslice using draft_bill_statement dbs, draft_bill_statementlineitem dbsli "
				+ "where dbs.id = dbsli.statement_id "
				+ "and dbsli.id = dbslice.statementlineitem_id "
				+ "and dbs.name = :statementName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		getNamedParameterJdbcTemplate().update(sql,q);
	}
	
	@Transactional("transactionManager")
	private void deleteDraftLineItems(String statementName){
		String sql = "delete from draft_bill_statementlineitem dbsli using draft_bill_statement dbs "
				+ "where dbs.id = dbsli.statement_id and dbs.name = :statementName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		getNamedParameterJdbcTemplate().update(sql,q);
	}
	
	@Transactional("transactionManager")
	private void deleteProfitShareItems(String statementName){
		String sql = "delete from draft_bill_statement_profitshare_item dbspi using draft_bill_statement dbs "
				+ "where dbs.id = dbspi.statement_id and dbs.name = :statementName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		getNamedParameterJdbcTemplate().update(sql,q);
	}

	@Override @SuppressWarnings("unchecked")
	public String querySupplierKcodeOfDraft(String statementName) {
		String sql = "select com.k_code from draft_bill_statement dbs "
				+ "inner join company com on com.id = dbs.receiving_company_id "
				+ "where dbs.name = :statementName and com.is_supplier = TRUE ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		List<String> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		Assert.isTrue(resultList.size()==1,"resultList.size()==1");
		return resultList.get(0);
	}

	@Override @SuppressWarnings("unchecked")
	public Date queryDateStartOfDraft(String statementName) {
		String sql = "select dbs.period_start from draft_bill_statement dbs where dbs.name = :statementName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		List<Date> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,Date.class);
		Assert.isTrue(resultList.size()==1,"resultList.size()==1");
		return resultList.get(0);
	}

	@Override @SuppressWarnings("unchecked")
	public Date queryDateEndOfDraft(String statementName) {
		String sql = "select dbs.period_end from draft_bill_statement dbs where dbs.name = :statementName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		List<Date> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,Date.class);
		Assert.isTrue(resultList.size()==1,"resultList.size()==1");
		return resultList.get(0);
	}
	
	@Override @Transactional("transactionManager")
	public void insertProfitShareItem(BillStatementType bsType, int statementId, int lineSeq, Country country,
			Currency srcCurrency, BigDecimal sourceAmount, Currency stmntCurrency, BigDecimal subtotal,BigDecimal exchangeRate) {
		String bspsTable = bsType.getDbTableStatementProfitShareItem();
		String sql = "insert into "+bspsTable
				+ "( statement_id, line_seq, source_country_id, source_currency_id, source_amount_untaxed, statement_currency_id, statement_amount_untaxed, exchange_rate ) values "
				+ "( :statementId, :lineSeq,  :sourceCountryId,  :sourceCurrencyId,  :sourceAmountUntaxed,  :statementCurrencyId,  :statementAmountUntaxed, :exchangeRate ) ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementId", statementId);
		q.addValue("lineSeq", lineSeq);
		q.addValue("sourceCountryId", country.getKey());
		q.addValue("sourceCurrencyId", srcCurrency.getKey());
		q.addValue("sourceAmountUntaxed", sourceAmount);
		q.addValue("statementCurrencyId", stmntCurrency.getKey());
		q.addValue("statementAmountUntaxed", subtotal);
		q.addValue("exchangeRate", exchangeRate);
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1,"q.executeUpdate()==1");
	}

	@Override @SuppressWarnings("unchecked")
	public List<BillStatementLineItem> queryNonProfitShareStatementLineItems(BillStatementType bsType, int statementId) {
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
				+ "    bsli.reference as reference, "
				+ "           cntry.code as country_name "
				+ "from "+bsliTable+" bsli "
				+ "inner join statement_line_type slt on slt.id = bsli.stlmnt_line_item_type_id "
				+ "inner join currency currency_orig on currency_orig.id =bsli.original_currency_id "
				+ "left  join currency currency_stmt on currency_stmt.id =bsli.statement_currency_id "
				+ "left  join product_base pb on pb.id = bsli.product_base_id "
				+ "left  join product_sku ps on ps.id = bsli.product_sku_id "
				+ "left  join transactionlinetype tlt on tlt.id = bsli.transactionlinetype_id "
				+ "inner join country cntry on cntry.id = bsli.country_id "
				+ "where statement_id = :sid and slt.name != :ssProfitShareForSpName";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("sid", statementId);
		q.addValue("ssProfitShareForSpName", StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP.getValue());

		List<BillStatementLineItemImpl> resultList =  getNamedParameterJdbcTemplate().query(sql,q ,(rs , rowNum) ->
				new BillStatementLineItemImpl(
						rs.getInt("id"),rs.getInt("statement_id"),rs.getInt("line_seq"),
						rs.getString("statement_line_type"),rs.getString("original_currency"),rs.getBigDecimal("original_amount"),
						rs.getString("statement_currency"),rs.getBigDecimal("statement_amount"),rs.getString("product_base_code"),
						rs.getString("product_name"),rs.getInt("quantity"),rs.getString("settleable_item"),
						rs.getString("reference"),rs.getString("country_name")
				));
		return (List)resultList;
	}

	@Override @SuppressWarnings("unchecked")
	public BigDecimal queryProfitShareItemStatementAmountUntaxedTotal(BillStatementType bsType, int statementId) {
		String bspsTable = bsType.getDbTableStatementProfitShareItem();
		String sql = "select sum(bsps.statement_amount_untaxed) from "+bspsTable+" bsps where bsps.statement_id = :statementId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementId", statementId);
		List<BigDecimal> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,BigDecimal.class);
		Assert.isTrue(resultList.size()==1,"resultList.size()==1");
		return resultList.get(0);
	}

	@Override @SuppressWarnings("unchecked")
	public BigDecimal queryProfitShareTax(BillStatementType bsType, int statementId) {
		String bsliTable = bsType.getDbTableStatementLineItem();
		String sql = "select statement_amount from "+bsliTable+" bsli "
				+ "inner join transactionlinetype tlt on tlt.id = bsli.transactionlinetype_id "
				+ "where bsli.statement_id = :statementId "
				+ "and tlt.name = :ssiVatName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementId", statementId);
		q.addValue("ssiVatName", TransactionLineType.SSI_VAT.getName());
		List<BigDecimal> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,BigDecimal.class);
		Assert.isTrue(resultList.size()==0||resultList.size()==1,"resultList.size()==0||resultList.size()==1");
		if(resultList.size()==0||resultList.get(0)==null) return BigDecimal.ZERO;
		return resultList.get(0);
	}

	@Override @SuppressWarnings("unchecked")
	public BigDecimal queryNonProfitShareTotal(BillStatementType bsType,int statementId) {
		String bsliTable = bsType.getDbTableStatementLineItem();
		String sql = "select sum(bsli.statement_amount) "
				+ "from "+bsliTable+" bsli "
				+ "inner join statement_line_type slt on slt.id = bsli.stlmnt_line_item_type_id "
				+ "where statement_id = :statementId and slt.name != :ssProfitShareForSpName  " +
				" and slt.id not in (23,26,27,28,30)  ";

		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementId", statementId);
		q.addValue("ssProfitShareForSpName", StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP.getValue());
		List<BigDecimal> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,BigDecimal.class);

		Assert.isTrue(resultList.size()==1,"resultList.size()==1");
		if(resultList.get(0) == null ) return BigDecimal.ZERO;
		return resultList.get(0);
	}

	@Override @SuppressWarnings("unchecked")
	public List<BillStatementProfitShareItem> queryProfitShareItems(BillStatementType bsType, int statementId) {
		String bspsTable = bsType.getDbTableStatementProfitShareItem();
		String sql = "select               bsps.id as id, "
				+ "              bsps.statement_id as statement_id, "
				+ "                  bsps.line_seq as line_seq, "
				+ "         bsps.source_country_id as source_country_id,"
				+ "        bsps.source_currency_id as source_currency_id,"
				+ "     bsps.source_amount_untaxed as source_amount_untaxed,"
				+ "     bsps.statement_currency_id as statement_currency_id,"
				+ "  bsps.statement_amount_untaxed as statement_amount_untaxed, "
				+ "             bsps.exchange_rate as exchange_rate "
				+ "from "+bspsTable+" bsps "
				+ "where bsps.statement_id = :statementId order by bsps.line_seq ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementId", statementId);


		List<BillStatementProfitShareItemImpl> items = getNamedParameterJdbcTemplate().query(sql,q,
				(rs,rowNum) -> new BillStatementProfitShareItemImpl(
						rs.getInt("id") , rs.getInt("statement_id") , rs.getInt("line_seq"),
						rs.getInt("source_country_id") , rs.getInt("source_currency_id") , rs.getBigDecimal("source_amount_untaxed"),
						rs.getInt("statement_currency_id") , rs.getBigDecimal("statement_amount_untaxed") , rs.getBigDecimal("exchange_rate")
				));
		if(items.size()==0) return null;
		return (List)items;
	}

	@Override @Transactional("transactionManager")
	public void deleteOfficial(String statementName) {
		String sql = null;

		sql = "delete from bill_statementlineitem_customercase_exemption_info bslice using bill_statement bs, bill_statementlineitem bsli where bs.id = bsli.statement_id and bsli.id = bslice.statementlineitem_id and bs.name = :statementName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		getNamedParameterJdbcTemplate().update(sql,q);
		sql = "delete from bill_statementlineitem bsli using bill_statement bs where bs.id = bsli.statement_id and bs.name = :statementName ";
		q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		getNamedParameterJdbcTemplate().update(sql,q);
		sql = "delete from bill_statement_profitshare_item bspsi using bill_statement bs where bs.id = bspsi.statement_id and bs.name = :statementName ";
		q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		getNamedParameterJdbcTemplate().update(sql,q);
		sql = "update customercarecase_message_reply_info cmri set ss2sp_statement_name = NULL where ss2sp_statement_name = :statementName ";
		q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		getNamedParameterJdbcTemplate().update(sql,q);
		sql = "delete from bill_statement bs where bs.name = :statementName ";
		q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		getNamedParameterJdbcTemplate().update(sql,q);
	}

	@Override
	public String querySupplierKcode(BillStatementType bsType,String statementName) {
		String bsTable = bsType.getDbTableStatement();
		String sql = "select rcvr.k_code from company rcvr "
				+ "inner join "+bsTable+" bs on bs.receiving_company_id = rcvr.id "
				+ "where bs.name = :statementName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);

		return getNamedParameterJdbcTemplate().queryForObject(sql,q,String.class);
	}

	@Override
	public String queryNewestStatementName(String supplierKcode) {
		String sql = "select bs.name from bill_statement bs "
				+ "inner join company rcvr on rcvr.id = bs.receiving_company_id "
				+ "where rcvr.k_code = :supplierKcode order by bs.period_start desc offset 0 limit 1 ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("supplierKcode", supplierKcode);

		return getNamedParameterJdbcTemplate().queryForObject(sql,q,String.class);
	}

	@Override @SuppressWarnings("unchecked")
	public Map<Country,Map<String,BigDecimal>> queryCountryToProductBaseCustomerFeeMap(BillStatementType bsType, int statementId) {
		String bsTable = bsType.getDbTableStatement();
		String sql = "select ct.id, pb.code_by_drs, sum(cmri.actual_charge_by_drs) "
				+ "from customercarecase_message_reply_info cmri "
				+ "inner join customercarecase_message cm on cmri.msg_id = cm.id "
				+ "inner join customercarecase cc on cc.id = cm.case_id "
				+ "inner join marketplace m on m.id = cc.marketplace_id "
				+ "inner join country ct on ct.id = m.country_id "
				+ "inner join product_sku ps on ps.id = cmri.related_product_sku_id "
				+ "inner join product_base pb on pb.id = ps.product_base_id "
				+ "inner join "+bsTable+" bs on bs.id = :statementId "
				+ "where pb.supplier_company_id = bs.receiving_company_id "
				+ "and cmri.date_finish >= bs.period_start "
			    + "and cmri.date_finish  < bs.period_end "
			    + "and cmri.is_free_of_charge = false "
				+ "group by ct.id, pb.code_by_drs ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementId", statementId);
		List<Object[]> result = getNamedParameterJdbcTemplate().query(sql,q ,objArrayMapper);
		Map<Country,Map<String,BigDecimal>> resultMap = new TreeMap<Country,Map<String,BigDecimal>>();
		for(Object[] columns:result){
			Country country = Country.fromKey((int)columns[0]);
			String baseCode = (String)columns[1];
			BigDecimal caseFee = (BigDecimal)columns[2];
			if(!resultMap.containsKey(country)) resultMap.put(country, new TreeMap<String,BigDecimal>());
			resultMap.get(country).put(baseCode, caseFee);
		}
		return resultMap;
	}

	@Override @Transactional("transactionManager")
	public void insertCustomerCaseLineItem(
			BillStatementType bsType,
			int statementId,
			int lineSeq,
			Country country, 
			Currency currency, 
			BigDecimal resultAmount, 
			String productBase) {
		String bsliTable = bsType.getDbTableStatementLineItem();
		
		int bsliId = PostgreSQLHelper.getNextVal(getNamedParameterJdbcTemplate(),bsliTable,"id");
		String sql = "insert into "+bsliTable+" "
				+ "( id, statement_id, line_seq,     stlmnt_line_item_type_id, original_currency_id, original_amount,  quantity, transactionlinetype_id, country_id, product_base_id ) "
				+ "select "
				+ " :id, :statementId, :lineSeq, tlt.ss2sp_stmnt_line_type_id,  :originalCurrencyId, :originalAmount, :quantity,                 tlt.id, :countryId,           pb.id "
				+ "from product_base pb "
				+ "inner join transactionlinetype tlt on tlt.name = :transactionLineTypeName "
				+ "where pb.code_by_drs = :productBase ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("id", bsliId);
		q.addValue("statementId", statementId);
		q.addValue("lineSeq", lineSeq);
		q.addValue("originalCurrencyId", currency.getKey());
		q.addValue("originalAmount", resultAmount);
		q.addValue("quantity", 1);
		q.addValue("countryId", country.getKey());
		q.addValue("transactionLineTypeName", TransactionLineType.MS2SS_PURCHASE_ALWNC_CUSTOMERCARE.getName());
		q.addValue("productBase", productBase);
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1,"q.executeUpdate()==1");
	}
	
	@Override @Transactional("transactionManager")
	public void insertCustomerCaseExemptionAndInfo(
			BillStatementType bsType,
			int statementId,
			int lineSeq,
			Country country,
			Currency currency,
			BigDecimal exemptionAmount,
			String productBase,
			BigDecimal revenue,
			BigDecimal productBaseRevenueRateForCustomerCaseFreeThreshold,
			BigDecimal freeAmount,
			BigDecimal customerFee) {
		String bsliTable = bsType.getDbTableStatementLineItem();
		String bsliCeiTable = bsType.getDbTableStatementCustomerCaseExemption();
		int bsliId = PostgreSQLHelper.getNextVal(getNamedParameterJdbcTemplate(),bsliTable,"id");
		String sql = "insert into "+bsliTable+" "
				+ "( id, statement_id, line_seq,     stlmnt_line_item_type_id, original_currency_id, original_amount,  quantity, transactionlinetype_id, country_id, product_base_id ) "
				+ "select "
				+ " :id, :statementId, :lineSeq, tlt.ss2sp_stmnt_line_type_id,  :originalCurrencyId, :originalAmount, :quantity,                 tlt.id, :countryId,           pb.id "
				+ "from product_base pb "
				+ "inner join transactionlinetype tlt on tlt.name = :transactionLineTypeName "
				+ "where pb.code_by_drs = :productBase ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("id", bsliId);
		q.addValue("statementId", statementId);
		q.addValue("lineSeq", lineSeq);
		q.addValue("originalCurrencyId", currency.getKey());
		q.addValue("originalAmount", exemptionAmount);
		q.addValue("quantity", 1);
		q.addValue("countryId", country.getKey());
		q.addValue("transactionLineTypeName", TransactionLineType.MS2SS_PURCHASE_ALWNC_CUSTOMERCARE_EXEMPTION.getName());
		q.addValue("productBase", productBase);
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1,"q.executeUpdate()==1");
		
		sql = "insert into "+bsliCeiTable+" "
			+ "( statementlineitem_id, product_base_revenue_amount, free_rate_to_product_base_revenue_amount, customer_case_free_threshold, customer_case_amount, customer_case_exemption ) values "
			+ "(              :bsliId,   :productBaseRevenueAmount,      :freeRateToProductBaseRevenueAmount,   :customerCaseFreeThreshold,  :customerCaseAmount,  :customerCaseExemption )";
		q = new MapSqlParameterSource();
		q.addValue("bsliId",bsliId);
		q.addValue("productBaseRevenueAmount",revenue);
		q.addValue("freeRateToProductBaseRevenueAmount",productBaseRevenueRateForCustomerCaseFreeThreshold);
		q.addValue("customerCaseFreeThreshold",freeAmount);
		q.addValue("customerCaseAmount",customerFee);
		q.addValue("customerCaseExemption",exemptionAmount);
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1,"q.executeUpdate()==1");
	}

	@Override @SuppressWarnings("unchecked")
	public List<BillStatementLineItemCustomerCaseExemptionInfo> queryCustomerCaseInfoList(BillStatementType bsType, int statementId) {
		String bsTable = bsType.getDbTableStatement();
		String bsliTable = bsType.getDbTableStatementLineItem();
		String bsliCeiTable = bsType.getDbTableStatementCustomerCaseExemption();
		String sql = "select                             bslicei.id as id, "
				+ "                                         bs.name as statement_name, "
				+ "                                 bsli.country_id as country_id, "
				+ "                       bsli.original_currency_id as currency_id, "
				+ "                                  pb.code_by_drs as product_base_code, "
				+ "             bslicei.product_base_revenue_amount as revenue, "
				+ "bslicei.free_rate_to_product_base_revenue_amount as free_rate, "
				+ "            bslicei.customer_case_free_threshold as free_amount, "
				+ "                    bslicei.customer_case_amount as customer_case_amount, "
				+ "                 bslicei.customer_case_exemption as exemption_amount "
				+ "from "+bsliCeiTable+" bslicei "
				+ "inner join "+bsliTable+" bsli on bsli.id = bslicei.statementlineitem_id "
				+ "inner join "+bsTable+" bs on bs.id = bsli.statement_id "
				+ "inner join product_base pb on pb.id = bsli.product_base_id "
				+ "where bs.id = :statementId "
				+ "order by bsli.country_id, pb.code_by_drs ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementId", statementId);


		return (List) getNamedParameterJdbcTemplate().query(sql,q ,(rs,rowNum) -> new BsliCustomerCaseExemptionInfoImpl(
				rs.getInt("id"),rs.getString("statement_name"),rs.getInt("country_id"),rs.getInt("currency_id"),
				rs.getString("product_base_code"),rs.getBigDecimal("revenue"),rs.getBigDecimal("free_rate"),
				rs.getBigDecimal("free_amount"),	rs.getBigDecimal("customer_case_amount"),	rs.getBigDecimal("exemption_amount")
		));


	}

	@Override @SuppressWarnings("unchecked")
	public List<String> querySupplierKcodeList() {
		String sql = "select com.k_code from company com "
				+ "where com.is_supplier = TRUE and com.k_code not in (:unAvailableComKcodes) "
				+ "order by com.k_code ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		List<String> unAvailableComKcodes = new ArrayList<String>(
				Arrays.asList(
						"K101", "K151", "K408", "K448", "K486", "K490", "K491",
						"K493", "K494", "K495", "K496", "K501", "K502", "K505",
						"K507", "K509", "K512", "K515", "K518", "K525",
						"K528", "K531", "K537", "K539", "K541", "K547", "K548",
						"K550", "K552", "K561", "K563", "K565", "K569",
						"K581", "K583", "K588", "K604", "K611", "K622",
						"K640"));
		q.addValue("unAvailableComKcodes", unAvailableComKcodes);
		return getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
	}

	@Override @SuppressWarnings("unchecked")
	public List<Object []> queryCustomerCaseMsdcMsgChargeInfoList(Date start,Date end){
		StringBuilder sqlSb = new StringBuilder()
				.append("select cmri.id, com.country_id, cmri.origin_charge_currency_id, cmri.origin_charge_by_drs ")
				.append("from customercarecase_message_reply_info cmri ")
				.append("inner join customercarecase_message cm on cm.id = cmri.msg_id ")
				.append("inner join customercarecase c on c.id = cm.case_id ")
				.append("inner join company com on com.id = c.drs_company_id ")
				.append("where cmri.date_finish >= :start ")
				.append("and cmri.date_finish < :end ")
				.append("order by cmri.id ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("start", start);
		q.addValue("end", end);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query( sqlSb.toString(),q,objArrayMapper);

		/*
		* List<CustomerCaseMsdcMsgChargeInfo> customerCaseMsdcMsgChargeInfoList = new ArrayList<>();
		for(Object[] columns:columnsList){
			Integer id = (Integer)columns[0];
			Integer countryId = (Integer)columns[1];
			Integer originChargeCurrencyId = (Integer)columns[2];
			BigDecimal chargeInOriginCurrency = (BigDecimal)columns[3];
			customerCaseMsdcMsgChargeInfoList.add(new CustomerCaseMsdcMsgChargeInfoImpl(id, Country.fromKey(countryId), Currency.fromKey(originChargeCurrencyId), chargeInOriginCurrency));
		}
		return customerCaseMsdcMsgChargeInfoList;
		* */
		return columnsList;
	}

	@Override @Transactional("transactionManager")
	public void updateCustomerCaseMsdcChargeInActualCurrency(List<CustomerCaseMsdcMsgChargeInfo> customerCaseMsdcMsgChargeInfoList) {
		String sql = "update customercarecase_message_reply_info set actual_charge_currency_id = :actualChargeCurrency, actual_charge_by_drs = :chargeInActualCurrency where id = :id ";
		for(CustomerCaseMsdcMsgChargeInfo info:customerCaseMsdcMsgChargeInfoList){
			MapSqlParameterSource q = new MapSqlParameterSource();
			q.addValue("actualChargeCurrency", info.getCountry().getCurrency().getKey());
			q.addValue("chargeInActualCurrency", info.getChargeInActualCurrency());
			q.addValue("id", info.getId());
			Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1,"query.executeUpdate()==1");
		}
	}
	
	@Override
	public boolean existSs2spStatement(Date periodStart) {
		StringBuilder sqlSb = new StringBuilder()
				.append("select exists( ")
				.append("    select 1 from bill_statement bs ")
				.append("    inner join company isur on isur.id = bs.issuing_company_id ")
				.append("    inner join company rcvr on rcvr.id = bs.receiving_company_id ")
				.append("    where true ")
				.append("    and isur.is_drs_company = true ")
				.append("    and rcvr.is_supplier = true ")
				.append("    and bs.period_end > :periodStart ")
				.append(") ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("periodStart", periodStart);
		Boolean result = getNamedParameterJdbcTemplate().queryForObject(sqlSb.toString(),q,Boolean.class);
		Assert.notNull(result,"result null");
		return result;
	}

}
