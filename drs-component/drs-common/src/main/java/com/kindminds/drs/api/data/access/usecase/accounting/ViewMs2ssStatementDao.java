package com.kindminds.drs.api.data.access.usecase.accounting;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.kindminds.drs.Currency;
import com.kindminds.drs.TransactionLineType;
import com.kindminds.drs.api.v1.model.report.Ms2ssPaymentAndRefundReport.Ms2ssPaymentAndRefundReportItem;
import com.kindminds.drs.api.v1.model.report.Ms2ssProductInventoryReturnReport.Ms2ssProductInventoryReturnReportItem;
import com.kindminds.drs.api.v1.model.report.Ms2ssPurchaseAllowanceReport.Ms2ssPurchaseAllowanceReportRawItem;
import com.kindminds.drs.api.v1.model.report.Ms2ssSettleableItemReport.Ms2ssSettleableItemReportLineItem;
import com.kindminds.drs.api.v1.model.report.Ms2ssStatement.Ms2ssStatementItem;
import com.kindminds.drs.api.v1.model.report.Ms2ssStatement.Ms2ssStatementItemPaymentOnBehalf;
import com.kindminds.drs.api.v1.model.report.Ms2ssStatement.Ms2ssStatementItemProductInventoryReturn;
import com.kindminds.drs.api.v1.model.report.RemittanceReport.RemittanceReportItem;
import com.kindminds.drs.enums.BillStatementType;

public interface ViewMs2ssStatementDao {

	Object [] queryInfo(BillStatementType type, String name);
	String querySettleableItemDrsName(int sid);
	Currency queryIssuerCurrency(BillStatementType type, String statementName);
	Currency queryShipmentCurrency(String sourcePoId);
	BigDecimal queryPreviousBalance(BillStatementType type, String statementName);
	BigDecimal queryRemittanceIsurToRcvr(BillStatementType type, String statementName);
	BigDecimal queryRemittanceRcvrToIsur(BillStatementType type, String statementName);
	BigDecimal queryBalance(BillStatementType type, String statementName);
	TransactionLineType queryItemById(int id);
	Ms2ssStatementItemProductInventoryReturn queryProductInventoryReturnItem(BillStatementType type, String statementName);

	List<Object []> queryStatementsRcvd(BillStatementType type, String drsCompanyKcode, Currency currency);

	List<Object []> queryStatementsIsud(BillStatementType type, int drsCompanyId);
	List<Ms2ssStatementItem> queryStatementItems(BillStatementType type, String statementDispName);
	List<Ms2ssStatementItemPaymentOnBehalf> queryStatementItemsPaymentOnBehalf(BillStatementType type, String statementName);

	Object [] queryStatementItemMsdcPaymentOnBehalf(BillStatementType type, String statementName);
	List<Ms2ssPurchaseAllowanceReportRawItem> queryPurchaseAllowanceReportRawItems(BillStatementType type, String statementDispName, String shipmentName);
	Map<String,BigDecimal> queryPurchaseAllowanceUnRelatedToSku(BillStatementType type, String statementName, String shipmentName);
	List<Ms2ssPaymentAndRefundReportItem> queryPaymentAndRefundReportItems(BillStatementType type, String statementId, String sourcePoId);
	List<RemittanceReportItem> queryRemittanceReportItemsIsurToRcvr(BillStatementType type, String statementName);
	List<RemittanceReportItem> queryRemittanceReportItemsRcvrToIsur(BillStatementType type, String statementName);
	List<Ms2ssSettleableItemReportLineItem> querySettleableItemReportLineItem(BillStatementType type, String statementDispName, String sourcePoId, String sku, int settleableItemId);
	List<Ms2ssSettleableItemReportLineItem> querySettleableItemReportLineItemSpecial(BillStatementType type, String statementDispName, String sku, int settleableItemId);
	List<Ms2ssSettleableItemReportLineItem> querySettleableItemReportLineItemForImpoortDuty(BillStatementType type, String statementName, String shipmentName, String sku, int settleableItemId);
	List<Ms2ssSettleableItemReportLineItem> querySettleableItemReportLineItemPaymentOnBehalfOfImportDuty(BillStatementType type, String statementName);
	List<Ms2ssSettleableItemReportLineItem> querySettleableItemReportLineItemMsdcPaymentOnBehalf(BillStatementType type, String statementName);
	List<Ms2ssSettleableItemReportLineItem> querySettleableItemReportLineItemForCustomerCareCase(BillStatementType type, String statementName, String sku, int settleableItemId);
	List<Ms2ssProductInventoryReturnReportItem> queryMs2ssProductInventoryReturnReportItems(BillStatementType type, String statementName);
}
