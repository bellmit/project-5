package com.kindminds.drs.api.data.access.rdb.accounting;

import java.util.List;

import com.kindminds.drs.api.v1.model.report.ProfitCostShareListReport;
import com.kindminds.drs.api.v1.model.report.ProfitCostShareListReport.ProfitCostShareListReportItem;
import com.kindminds.drs.api.v1.model.report.ProfitShareInvoiceEmailReminder;
import com.kindminds.drs.enums.BillStatementType;

public interface ProfitCostShareStatementsDao {
	List<Object []> querySettlementPeriodList();
	List<Object []> queryStatementsReceivedBySupplierAndPeriod(BillStatementType type, String supplierKcode, int settlementPeriodId);
	Object [] queryStatementByStatementName(BillStatementType type, String statementName);
	boolean updateProfitCostShareInvoiceNumber(BillStatementType type, ProfitCostShareListReportItem statementItem);
	boolean bulkUpdateProfitCostShareInvoiceNumber(BillStatementType type, ProfitCostShareListReport statements);
	List<ProfitShareInvoiceEmailReminder> querySuppliersWithNoInvoiceCreated(BillStatementType type);
}
