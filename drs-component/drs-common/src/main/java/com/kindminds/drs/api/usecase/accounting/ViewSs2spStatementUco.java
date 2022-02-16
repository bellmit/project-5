package com.kindminds.drs.api.usecase.accounting;


import com.kindminds.drs.api.v1.model.report.*;
import com.kindminds.drs.enums.BillStatementType;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public interface ViewSs2spStatementUco {
	String getBalanceNoteMessageKey(String statementName, String dateStartStr,String balanceStr,Locale locale);
	String getMonthInfoForMonthStorageFee(String DateStart);
	Map<String,String> getSupplierKcodeToNameMap();
	Map<String,String> getAllCompanyKcodeToLocalNameMap();
	StatementListReport getSs2spStatementListReport(BillStatementType type, String supplierKcodeToFilter);
	Ss2spStatementReport getSs2spStatement(BillStatementType type,String statementName);
	Ss2spProfitShareDetailReport getSs2spProfitShareDetailReport(BillStatementType type,String statementName,String country);
	Ss2spProfitShareReport getSs2spProfitShareReport(BillStatementType type,String statementID);
	Ss2spSkuProfitShareDetailReport getSs2spSkuProfitShareDetailReport(BillStatementType type,String statementName,String countryCode,String sku);
	Ss2spSettleableItemReport getSs2spSettleableItemReportForProfitShare(BillStatementType type,String statementId,String country,String sku,String itemName);
	Ss2spPaymentAndRefundReport getSs2spPaymentAndRefundReport(BillStatementType type,String Ss2spStatementID,String sourceShipmentName);
	Ss2spSettleableItemReport getSs2spSettleableItemReportForPoRelated(BillStatementType type,String statementId,String sourcePoId, String sku, int settleableItemId);
	Ss2spSellBackReport getSellBackReport(BillStatementType type,String statementName);
	Ss2spCustomerCareCostReport getSs2spCustomerCareCostReport(BillStatementType type, String statementName, String country);
	Ss2spServiceExpenseReport getServiceExpenseReport(BillStatementType type, String statementName, String domesticTransactionInvoice);
	Ss2spServiceExpenseReport getAllServiceExpenseReport(BillStatementType type, String statementName);
	RemittanceReport getRemittanceIsurToRcvrReport(BillStatementType type, String statementName);
	RemittanceReport getRemittanceRcvrToIsurReport(BillStatementType type,String statementName);
	// methods for export
	List<Ss2spSettleableBrowserForProfitShareToExport> getSs2spSettleableBrowserForProfitShareToExport(BillStatementType type, String statementName, String countryCode);
	List<Ss2spPaymentAndRefundReportItemToExport> getSs2spPaymentAndRefundReportItemToExport(BillStatementType type, String Ss2spStatementID, String sourceShipmentName);
	ProfitShareSubtractionAdvertisingCostReport getSs2spAdvertisingCostReport(String statementName, String countryCode);
	ProfitShareSubtractionOtherRefundReport getSs2spOtherRefundReport(String statementName, String countryCode);
	ProfitShareSubtractionMarketingActivityExpenseReport getSs2spMarketingActivityExpenseReport(String statementName, String countryCode);
	ProfitSharePartialRefundReport getPartialRefundReport(String statementName, String countryCode);
}