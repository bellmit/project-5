package com.kindminds.drs.persist.data.access.rdb.accounting;

import com.kindminds.drs.TransactionLineType;
import com.kindminds.drs.enums.BillStatementType;
import com.kindminds.drs.api.data.access.rdb.accounting.ProfitCostShareStatementsDao;
import com.kindminds.drs.persist.data.access.rdb.Dao;
import com.kindminds.drs.api.v1.model.accounting.StatementLineType;
import com.kindminds.drs.api.v1.model.report.ProfitCostShareListReport;
import com.kindminds.drs.api.v1.model.report.ProfitCostShareListReport.ProfitCostShareListReportItem;
import com.kindminds.drs.api.v1.model.report.ProfitShareInvoiceEmailReminder;
import com.kindminds.drs.persist.v1.model.mapping.report.ProfitShareInvoiceEmailReminderImpl;


import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ProfitCostShareStatementsDaoImpl extends Dao implements ProfitCostShareStatementsDao {


	private final String queryStatementSqlPartOfSelect = "select" + 
			"	bs.name as statement_id," + 
			"	to_char( bs.period_start at time zone :tz," + 
			"	:fm ) as period_start_utc," + 
			"	to_char(( bs.period_end-interval '1 second' ) at time zone :tz," + 
			"	:fm ) as period_end_utc," + 
			"	bs.currency_id as currency_id," + 
			"	bs.total as total," + 
			"	bs.balance as balance," + 
			"	bs.invoice_from_ssdc as invoice_from_ssdc," + 
			"	bs.invoice_from_supplier as invoice_from_supplier," + 
			"	bs.invoice_notes as invoice_notes," + 
			"	bsli.statement_amount," + 
			"	(select	sum(bspi.statement_amount_untaxed)" + 
			"		from bill_statement_profitshare_item bspi" + 
			"		where bs.id = bspi.statement_id" + 
			"	) as statement_amount_untaxed ";

	@Override
	@SuppressWarnings("unchecked")
	public List<Object []> querySettlementPeriodList() {
		StringBuilder sqlSb = new StringBuilder().append("select    sp.id as id, ").append("sp.period_start as start, ")
				.append("sp.period_end   as end ").append("from settlement_period sp ")
				.append("order by sp.period_start desc ");

		List<Object[]> columnsList = getJdbcTemplate().query(sqlSb.toString(),objArrayMapper);
		return columnsList;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Object []> queryStatementsReceivedBySupplierAndPeriod(BillStatementType type,
			String supplierKcode, int settlementPeriodId) {
		// Used to determine if a periodId is supplied, otherwise all statements for a specific supplier are returned
		boolean filterByPeriod = (settlementPeriodId > 0 ? true : false);
		StringBuilder sql = new StringBuilder(this.queryStatementSqlPartOfSelect + "from " + type.getDbTableStatement() + " as bs "
				+ "inner join currency c on" + 
				"	c.id = bs.currency_id " + 
				"inner join company rcvr on" + 
				"	rcvr.id = bs.receiving_company_id ");
				if(filterByPeriod)
					sql.append("inner join settlement_period sp on" + 
					"	:period_id = sp.id ");
				sql.append("inner join bill_statementlineitem bsli on" + 
					"	bs.id = bsli.statement_id " + 
					"inner join statement_line_type slt on" + 
					"	slt.id = bsli.stlmnt_line_item_type_id " + 
					"inner join transactionlinetype tlt on" + 
					"	tlt.id = bsli.transactionlinetype_id " + 
					"where rcvr.k_code LIKE :supplierKcode and rcvr.is_supplier = TRUE and bs.currency_id = rcvr.currency_id ");
				if(filterByPeriod)
					sql.append("and sp.period_start >= bs.period_start and sp.period_end <= bs.period_end ");
		sql.append("	and slt.name = :ss2spProfitShareName " +
				"	and tlt.name = :vatTaxName " +
				"   and bs.version_number > 2 ");
				if(filterByPeriod)
					sql.append("order by bsli.statement_amount desc ");
				else
					sql.append("order by bs.period_start desc, bs.name ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("tz", "UTC");
		q.addValue("fm", "YYYY-MM-DD HH24:MI:SS");
		q.addValue("supplierKcode", supplierKcode);
		if(filterByPeriod)
			q.addValue("period_id", settlementPeriodId);
		q.addValue("ss2spProfitShareName", StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP.getValue());
		q.addValue("vatTaxName", TransactionLineType.SSI_VAT.getName());
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql.toString(),q,objArrayMapper);
		return (columnsList);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object [] queryStatementByStatementName(BillStatementType type, String statementName) {
		String sql = this.queryStatementSqlPartOfSelect + "from " + type.getDbTableStatement() + " as bs "
				+ "inner join currency c on c.id = bs.currency_id "
				+ "inner join bill_statementlineitem bsli on"
				+ "	bs.id = bsli.statement_id "
				+ "where bs.name = :statementName "
				+ "order by bs.period_start desc, bs.name ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("tz", "UTC");
		q.addValue("fm", "YYYY-MM-DD HH24:MI:SS");
		q.addValue("statementName", statementName);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		if(columnsList == null || columnsList.size() < 1) return null;

		return columnsList.get(0);
	}

	/*
	private List<ProfitCostShareListReportItem> toStatementList(List<Object[]> columnsList) {
		List<ProfitCostShareListReportItem> resultList = new ArrayList<>();
		for (Object[] columns : columnsList) {
			resultList.add(toStatementItem(columns));
		}
		return resultList;
	}

	private ProfitCostShareListReportItem toStatementItem(Object[] columns) {
		String statementId = (String) columns[0];
		String startDateUtc = (String) columns[1];
		String endDateUtc = (String) columns[2];
		Integer currencyId = (Integer) columns[3];
		BigDecimal total = (BigDecimal) columns[4];
		BigDecimal balance = (BigDecimal) columns[5];
		String invoiceFromSsdc = (String) columns[6];
		String invoiceFromSupplier = (String) columns[7];
		String invoiceNotes = (String) columns[8];
		BigDecimal amountTax = (BigDecimal) columns[9];
		BigDecimal amountUntaxed = (BigDecimal) columns[10];
		return new ProfitCostShareListReportItemImpl(statementId, startDateUtc, endDateUtc, currencyId, total,
				balance, invoiceFromSsdc, invoiceFromSupplier, invoiceNotes, amountTax, amountUntaxed);
	}*/

	@Override @Transactional("transactionManager")
	public boolean updateProfitCostShareInvoiceNumber(BillStatementType type,
			ProfitCostShareListReportItem statementItem) {
		String sql = "UPDATE " + type.getDbTableStatement() + " "
				+ "SET invoice_from_ssdc = :invoice_from_ssdc, "
				+ "invoice_from_supplier = :invoice_from_supplier, "
				+ "invoice_notes = :invoice_notes "
				+ "WHERE name = :statement_name ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("invoice_from_ssdc", statementItem.getInvoiceFromSsdc());
		q.addValue("invoice_from_supplier", statementItem.getInvoiceFromSupplier());
		q.addValue("invoice_notes", statementItem.getInvoiceNotes());
		q.addValue("statement_name", statementItem.getStatementId());

		return (getNamedParameterJdbcTemplate().update(sql,q) > 0 ? true : false);
	}

	@Override @Transactional("transactionManager")
	public boolean bulkUpdateProfitCostShareInvoiceNumber(BillStatementType type,
			ProfitCostShareListReport statements) {
		String sql = "UPDATE " + type.getDbTableStatement() + " "
				+ "SET invoice_from_ssdc = ?, "
				+ "invoice_from_supplier = ?, "
				+ "invoice_notes = ? "
				+ "WHERE name = ? ";



		int[][] updateCounts = this.getJdbcTemplate().batchUpdate(sql, statements.getPcsStatements(), 20 ,
				new ParameterizedPreparedStatementSetter<ProfitCostShareListReportItem>() {
					@Override
					public void setValues(PreparedStatement pstmt, ProfitCostShareListReportItem item) throws SQLException {
						pstmt.setString(1, item.getInvoiceFromSsdc());
						pstmt.setString(2, item.getInvoiceFromSupplier());
						pstmt.setString(3, item.getInvoiceNotes());
						pstmt.setString(4, item.getStatementId());

					}

				});


		return true;
	}

	@Override @Transactional("transactionManager")
	public List<ProfitShareInvoiceEmailReminder> querySuppliersWithNoInvoiceCreated(BillStatementType type) {
		String sql = "select " +
				"bs.name as statement_name, " +
				"to_char( bs.period_start at time zone :tz, " +
				":fm ) as period_start_utc, " +
				"to_char(( bs.period_end-interval '1 second' ) at time zone :tz, " +
				":fm ) as period_end_utc, " +
				"ui.user_name as user_name " +
				"from " + type.getDbTableStatement() + " as bs " +
				"inner join currency c on " +
				"c.id = bs.currency_id " +
				"inner join company rcvr on " +
				"rcvr.id = bs.receiving_company_id " +
				"inner join bill_statementlineitem bsli on " +
				"bs.id = bsli.statement_id " +
				"inner join statement_line_type slt on " +
				"slt.id = bsli.stlmnt_line_item_type_id " +
				"inner join transactionlinetype tlt on " +
				"tlt.id = bsli.transactionlinetype_id " +
				"inner join user_info ui on " +
				"bs.receiving_company_id = ui.company_id " +
				"where " +
				"rcvr.is_supplier = true " +
				"and bs.currency_id = rcvr.currency_id " +
				"and slt.name = :ss2spProfitShareName " +
				"and tlt.name = :vatTaxName " +
				"and (bs.invoice_from_ssdc is null or bs.invoice_from_ssdc = '') " +
				"and (bs.invoice_from_supplier is null or bs.invoice_from_supplier = '') " +
				"and bsli.statement_amount > 0 " +
				"and bs.period_start = (select max(bs.period_start) from bill_statement bs) " +
				"order by " +
				"bsli.statement_amount desc";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("tz", "UTC");
		q.addValue("fm", "YYYY-MM-DD");
		q.addValue("ss2spProfitShareName", StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP.getValue());
		q.addValue("vatTaxName", TransactionLineType.SSI_VAT.getName());


		List<ProfitShareInvoiceEmailReminderImpl> resultList = getNamedParameterJdbcTemplate().query(sql,q,
				(rs,rowNum)-> new ProfitShareInvoiceEmailReminderImpl(
						rs.getString("statement_name"),rs.getString("period_start_utc"),rs.getString("period_end_utc"),
						rs.getString("user_name")
				));

		if(resultList.size()==0) return null;
		return (List)resultList;
	}
}
