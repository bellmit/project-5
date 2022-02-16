package com.kindminds.drs.api.usecase.accounting;

import java.util.Map;


import com.kindminds.drs.api.v1.model.report.Ms2ssPaymentAndRefundReport;
import com.kindminds.drs.api.v1.model.report.Ms2ssProductInventoryReturnReport;
import com.kindminds.drs.api.v1.model.report.Ms2ssPurchaseAllowanceReport;
import com.kindminds.drs.api.v1.model.report.Ms2ssSettleableItemReport;
import com.kindminds.drs.api.v1.model.report.Ms2ssStatement;
import com.kindminds.drs.api.v1.model.report.RemittanceReport;
import com.kindminds.drs.api.v1.model.report.StatementListReport;
import com.kindminds.drs.enums.BillStatementType;

public interface ViewMs2ssStatementUco {
	Map<String,String> getDrsCompanyKcodeToEnUsNameMap();
	StatementListReport queryStatementsRcvd(BillStatementType type);
	StatementListReport queryStatementsIsud(BillStatementType type);
	Ms2ssStatement queryStatement(BillStatementType type,String statementDispName);
	Ms2ssPurchaseAllowanceReport getPurchaseAllowanceReport(BillStatementType type,String statementDispName,String shipmentName);
	Ms2ssPaymentAndRefundReport getPaymentAndRefundReport(BillStatementType type,String statementDispName,String sourceId);
	Ms2ssProductInventoryReturnReport getProductInventoryReturnReport(BillStatementType type,String statementName);
	RemittanceReport getRemittanceIsurToRcvrReport(BillStatementType type,String statementName);
	RemittanceReport getRemittanceRcvrToIsurReport(BillStatementType type,String statementName);
	Ms2ssSettleableItemReport querySettleableItemReportForPaymentRefund(BillStatementType type,String statementDispName, String sourcePoId, String sku, int settleableItemId);
	Ms2ssSettleableItemReport querySettleableItemReportForPurchaseAllowance(BillStatementType type,String statementDispName, String sourcePoId, String sku, int settleableItemId);
	Ms2ssSettleableItemReport querySettleableItemReportForPaymentOnBehalf(BillStatementType type,String statementName);
	Ms2ssSettleableItemReport getMsdcPaymentOnBehalfDetail(BillStatementType type,String statementName);
}