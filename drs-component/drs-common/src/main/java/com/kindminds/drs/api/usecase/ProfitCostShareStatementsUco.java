package com.kindminds.drs.api.usecase;

import java.util.List;
import java.util.Map;


import com.kindminds.drs.api.v1.model.accounting.SettlementPeriod;
import com.kindminds.drs.api.v1.model.report.ProfitCostShareListReport;
import com.kindminds.drs.api.v1.model.report.ProfitCostShareListReport.ProfitCostShareListReportItem;
import com.kindminds.drs.enums.BillStatementType;

public interface ProfitCostShareStatementsUco {
	Map<String,String> getSupplierKcodeToNameMap();
	Map<String,String> getAllCompanyKcodeToLocalNameMap();
	List<SettlementPeriod> getSettlementPeriodList();
	ProfitCostShareListReport getProfitCostShareStatementListReport(BillStatementType type, String supplierKcodeToFilter, String settlementPeriodId);
	ProfitCostShareListReportItem getProfitCostShareStatement(BillStatementType type, String statementName);
	boolean updateProfitCostShareStatement(BillStatementType type, ProfitCostShareListReportItem profitCostShareListReportItem);
	boolean bulkUpdateProfitCostShareStatement(BillStatementType type, ProfitCostShareListReport profitCostShareListReport);
}
