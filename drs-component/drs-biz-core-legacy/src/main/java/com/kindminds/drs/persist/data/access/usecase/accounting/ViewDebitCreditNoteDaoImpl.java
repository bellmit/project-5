package com.kindminds.drs.persist.data.access.usecase.accounting;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;





import com.kindminds.drs.persist.data.access.rdb.Dao;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.kindminds.drs.api.data.access.usecase.accounting.ViewDebitCreditNoteDao;
import com.kindminds.drs.api.v1.model.accounting.DebitCreditNote.DebitCreditNoteItem.DebitCreditNoteSkuLine;
import com.kindminds.drs.api.v1.model.accounting.StatementLineType;
import com.kindminds.drs.api.v1.model.accounting.DebitCreditNote.DebitCreditNoteItem;
import com.kindminds.drs.persist.v1.model.mapping.accounting.DebitCreditNoteItemImpl;
import com.kindminds.drs.persist.v1.model.mapping.accounting.DebitCreditNoteSkuLineImpl;

@Repository
public class ViewDebitCreditNoteDaoImpl extends Dao implements ViewDebitCreditNoteDao {
	
	@Override
	public String queryStatementDateEnd(String statementName) {
		String sql = "select date((period_end) at time zone 'UTC') from bill_statement bs "
				+ "where bs.name = :statementName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		Date date = getNamedParameterJdbcTemplate().queryForObject(sql,q,Date.class);
		return date.toString();
	}

	@Override @SuppressWarnings("unchecked")
	public BigDecimal queryNoteAmountTotal(String statementName) {
		String sql = "select sum(bsli.statement_amount) "
				+ "from bill_statementlineitem bsli "
				+ "inner join statement_line_type slt on slt.id = bsli.stlmnt_line_item_type_id "
				+ "inner join bill_statement bs on bs.id=bsli.statement_id "
				+ "where bs.name=:statementName "
				+ "and bsli.reference is not null "
				+ "and slt.name = :purchaseAllowanceName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		q.addValue("purchaseAllowanceName",StatementLineType.MS2SS_PURCHASE_ALLOWANCE.getValue());
		List<BigDecimal> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,BigDecimal.class);
		Assert.isTrue(resultList.size()==1,"No or more than one result found.");
		return resultList.get(0);
	}

	@Override @SuppressWarnings("unchecked")
	public List<DebitCreditNoteItem> queryDebitCreditNoteItems(String statementName) {
		String sql = "select shp.invoice_number as invoice_number, "
				+ "                     bs.name as statement_name, "
				+ " to_char((bs.period_start                   ) at time zone 'UTC', :dateTimeFormatText) as period_start, "
				+ " to_char((bs.period_end   - interval '1 day') at time zone 'UTC', :dateTimeFormatText) as period_end, "
				+ "  sum(bsli.statement_amount) as invoice_total "
				+ "from bill_statementlineitem bsli "
				+ "inner join statement_line_type slt on slt.id=bsli.stlmnt_line_item_type_id "
				+ "inner join shipment shp on shp.name = bsli.reference "
				+ "inner join bill_statement bs on bs.id=bsli.statement_id "
				+ "where bs.name=:statementName "
				+ "and bsli.reference is not null "
				+ "and slt.name = :purchaseAllowanceName "
				+ "group by shp.invoice_number, bs.name, bs.period_start, bs.period_end "
				+ "order by shp.invoice_number ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		q.addValue("dateTimeFormatText","YYYY-MM-DD");
		q.addValue("purchaseAllowanceName",StatementLineType.MS2SS_PURCHASE_ALLOWANCE.getValue());


		return (List) getNamedParameterJdbcTemplate().query(sql,q,(rs , rowNum) -> new DebitCreditNoteItemImpl(
			rs.getString("invoice_number"),rs.getString("statement_name"),
				rs.getString("period_start"),rs.getString("period_end"),rs.getBigDecimal("invoice_total")
		));
	}

	@Override @SuppressWarnings("unchecked")
	public List<DebitCreditNoteSkuLine> queryDebitCreditNoteSkuLines(String statementName, String invoiceNumber) {
		String sql = "select   ps.code_by_drs as sku_code, "
				+ "                 slt.en_us as description, "
				+ "sum(bsli.statement_amount) as sum_amount "
				+ "from bill_statementlineitem bsli "
				+ "inner join product_sku ps on ps.id=bsli.product_sku_id "
				+ "inner join statement_line_type slt on slt.id = bsli.stlmnt_line_item_type_id "
				+ "inner join shipment shp on shp.name = bsli.reference "
				+ "inner join bill_statement bs on bs.id=bsli.statement_id "
				+ "where bs.name=:statementName "
				+ "and bsli.reference is not null "
				+ "and slt.name = :purchaseAllowanceName "
				+ "and shp.invoice_number = :invoiceNumber "
				+ "group by ps.code_by_drs, slt.en_us "
				+ "order by ps.code_by_drs ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		q.addValue("invoiceNumber", invoiceNumber);
		q.addValue("purchaseAllowanceName",StatementLineType.MS2SS_PURCHASE_ALLOWANCE.getValue());

		return (List) getNamedParameterJdbcTemplate().query(sql,q, (rs,rowNum) -> new DebitCreditNoteSkuLineImpl(
				rs.getString("sku_code"),rs.getString("description"),rs.getBigDecimal("sum_amount")
		));
	}

	@Override @SuppressWarnings("unchecked")
	public List<Object[]> queryDebitCreditNoteSkuItems(String statementName) {
		String sql = "select   ps.code_by_drs as sku_code, "
				+ "        shp.invoice_number as invoice_number, "
				+ "sum(bsli.statement_amount) as sum_amount "
				+ "from bill_statementlineitem bsli "
				+ "inner join product_sku ps on ps.id=bsli.product_sku_id "
				+ "inner join statement_line_type slt on slt.id = bsli.stlmnt_line_item_type_id "
				+ "inner join shipment shp on shp.name = bsli.reference "
				+ "inner join bill_statement bs on bs.id=bsli.statement_id "
				+ "where bs.name=:statementName "
				+ "and bsli.reference is not null "
				+ "and slt.name = :purchaseAllowanceName "				
				+ "group by ps.code_by_drs, shp.invoice_number "
				+ "order by ps.code_by_drs ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName", statementName);
		q.addValue("purchaseAllowanceName",StatementLineType.MS2SS_PURCHASE_ALLOWANCE.getValue());
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);



		return columnsList;
	}

}
