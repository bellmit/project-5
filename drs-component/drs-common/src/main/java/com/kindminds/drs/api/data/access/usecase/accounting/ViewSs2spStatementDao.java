package com.kindminds.drs.api.data.access.usecase.accounting;

import com.kindminds.drs.Country;
import com.kindminds.drs.Currency;
import com.kindminds.drs.TransactionLineType;
import com.kindminds.drs.api.v1.model.report.ProfitShareSubtractionMarketingActivityExpenseReport.ProfitShareSubtractionMarketingActivityExpenseReportLineItem;
import com.kindminds.drs.api.v1.model.report.ProfitShareSubtractionOtherRefundReport.ProfitShareSubtractionOtherRefundReportLineItem;
import com.kindminds.drs.api.v1.model.report.RemittanceReport.RemittanceReportItem;
import com.kindminds.drs.api.v1.model.report.Ss2spCustomerCareCostReport.Ss2spCustomerCareCostReportItem;
import com.kindminds.drs.api.v1.model.report.Ss2spPaymentAndRefundReport.Ss2spPaymentAndRefundReportItem;
import com.kindminds.drs.api.v1.model.report.Ss2spProfitShareDetailReport.Ss2spProfitShareDetail;
import com.kindminds.drs.api.v1.model.report.Ss2spProfitShareDetailReport.Ss2spProfitShareDetailOfProductSku;
import com.kindminds.drs.api.v1.model.report.Ss2spProfitShareDetailReport.Ss2spProfitShareDetailReportSkuProfitShareItem;
import com.kindminds.drs.api.v1.model.report.Ss2spProfitShareReport.Ss2spProfitShareReportLineItem;
import com.kindminds.drs.api.v1.model.report.Ss2spSellBackReport.Ss2spSellBackReportLineItem;
import com.kindminds.drs.api.v1.model.report.Ss2spServiceExpenseReport.Ss2spServiceExpenseReportItem;
import com.kindminds.drs.api.v1.model.report.Ss2spSettleableItemReport.Ss2spSettleableItemReportLineItem;
import com.kindminds.drs.api.v1.model.report.Ss2spSkuProfitShareDetailReport.Ss2spSkuProfitShareDetailReportRefundedItem;
import com.kindminds.drs.api.v1.model.report.Ss2spSkuProfitShareDetailReport.Ss2spSkuProfitShareDetailReportShippedItem;
import com.kindminds.drs.api.v1.model.report.Ss2spStatementReport.Ss2spStatementItemProfitShare;
import com.kindminds.drs.api.v1.model.report.Ss2spStatementReport.Ss2spStatementItemSellBackRelated;
import com.kindminds.drs.api.v1.model.report.Ss2spStatementReport.Ss2spStatementItemServiceExpense;
import com.kindminds.drs.api.v1.model.report.Ss2spStatementReport.Ss2spStatementItemShipmentRelated;
import com.kindminds.drs.api.v1.model.report.StatementInfo;
import com.kindminds.drs.api.v1.model.report.StatementListReport.StatementListReportItem;
import com.kindminds.drs.api.v1.model.report.ProfitShareSubtractionAdvertisingCostReport.ProfitShareSubtractionAdvertisingCostReportLineItem;
import com.kindminds.drs.enums.BillStatementType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

public interface ViewSs2spStatementDao {

	String queryRcvrKcode(String name);
	Boolean queryIsCompanyBiSettlement(String companyCode);
	Boolean queryIsPeriodBisettlement(Date periodStart);
	Object [] queryInfo(BillStatementType type, String name);

	List<Object []> queryStatementsSentByDrsCompany(BillStatementType type, String drsCompanyKcode);

	List<Object []> queryStatementsReceivedBySupplier(BillStatementType type, String supplierKcode);

	List<Object []> queryNewestStatementsSentByDrsCompany(BillStatementType type, String drsCompanyKcode);
	List<Object []> queryDraftStatements(String drsCompanyKcode);

	int queryStatementVersionNumber(BillStatementType type, String statementName);
	String queryClassOfTransactionLineType(String name);
	TransactionLineType querySettleableItem(int sid);
	BigDecimal queryPreviousBalance(BillStatementType type, String statementName);
	BigDecimal queryRemittanceIsurToRcvr(BillStatementType type, String statementName);
	BigDecimal queryRemittanceRcvrToIsur(BillStatementType type, String statementName);
	BigDecimal queryBalance(BillStatementType type, String statementName);
	BigDecimal queryServiceExpenseTaxRate(String domesticTransactionInvoice);
	BigDecimal queryAllServiceExpenseTaxRate(String statementName);
	BigDecimal queryProfitShareTaxV2(BillStatementType type, String statementName);
	Currency queryStatementCurrency(BillStatementType type, String statementName);
	/* Level 1 items */
	List<Ss2spStatementItemProfitShare> queryStatementItemProfitShareV2(BillStatementType type, String statementName);


	//List<Ss2spStatementItemProfitShare> queryStatementItemProfitShareV3(BillStatementType type, String statementName);
	int queryProfitShareItemCounts(BillStatementType type,String statementName);
	Currency queryProfitShareStatementCurrency(BillStatementType type,String statementName);
	BigDecimal querySumOfProfitShareStatementAmountUntaxed(BillStatementType type,String statementName);
	BigDecimal queryProfitShareTaxV3(BillStatementType type,String statementName);

	List<Ss2spStatementItemShipmentRelated> queryStatementItemsShipmentRelated(BillStatementType type, String statementId);

	List<Object []> queryStatementItemsSellBackRelated(BillStatementType type, String statementName);
	List<Ss2spStatementItemServiceExpense> queryStatementItemServiceExpense(BillStatementType type, String statementId);
	/* Level 1-1 items */
	List<Object []> queryProfitShareReportLineItemV1(BillStatementType type, String statementId);
	List<Object []> queryProfitShareReportLineItemV3(BillStatementType type, String statementName);
	/* Level 1-1-1 raw items */
	List<Object []> querySkuProfitShareItems(BillStatementType type, String statementName, Country country, List<TransactionLineType> relatedItems);

	List<Object []> queryProfitShareDetailItemsV1(BillStatementType type, String statementName, Country country, List<TransactionLineType> excludeTlt);

	List<Object[]> queryProfitShareDetailItemsV3(BillStatementType type, String statementName, Country country, List<TransactionLineType> excludeTlt);

	List<Object []>  queryProfitShareDetailV4ofProductSku(BillStatementType type, String statementName, Country country, List<TransactionLineType> excludeTlt);

	List<Entry<String,BigDecimal>> queryProfitShareItemsAmountExcludedRetailAndInternational(BillStatementType type, String statementName, Country country, List<String> itemNames);
	List<Entry<String,BigDecimal>> queryInternationalTransactionItems(BillStatementType type, String statementName, Country country);

	/* Level 1-1-1-1 items */
	List<Object []> querySs2spSkuProfitShareDetailReportShippedItem(Date start,Date end,Country country,String sku);
	List<Object []> querySs2spSkuProfitShareDetailReportShippedItem(Date start,Date end,Country country,String sku , List<String> types);

	List<Object []> querySs2spSkuProfitShareDetailReportRefundedItem(Date start,Date end,Country country,String sku);

	List<Ss2spCustomerCareCostReportItem> querySs2spCustomerCareCostReportItemByProdcutBase(BillStatementType type, String statementName, String country);
	List<Ss2spCustomerCareCostReportItem> querySs2spCustomerCareCostReportItemBySku(BillStatementType type, String statementName, String country, String sku);
	List<Ss2spSettleableItemReportLineItem> querySettleableItemReportLineItem(BillStatementType type, String statementId, Country country, String sku, String itemName);
	List<Ss2spSettleableItemReportLineItem> querySettleableItemReportLineItemProfitShareSettleableRelated(BillStatementType type, String statementId, Country country, String sku, String itemName);
	List<Ss2spSettleableItemReportLineItem> querySettleableItemReportLineItemImportDutyRelated(BillStatementType type, String statementName, Country country, String sku);

	/* Level 1-2 items */
	List<Object []> queryPaymentAndRefundReportItems(BillStatementType type, String statementId, String sourcePoId);
	BigDecimal queryPaymentAndRefundUnitAmount(BillStatementType type, String statementName, String ivsName, String sku, int transactionLineTypeId);
	/* Level 1-2-1 items */
	List<Ss2spSettleableItemReportLineItem> querySettleableItemReportLineItem(BillStatementType type, String statementId, String sourcePoId, String sku, int settleableItemId);
	/* Level 1-3 items */
	List<Object []> querySellBackReportItems(BillStatementType type, String statementName);
	List<Object []> queryAdvertisingCostReportItems(String statementName, String countryCode);

	List<Object []> queryOtherRefundReportItems(String statementName, String countryCode);
	List<Object []> queryMarketingActivityExpenseReportItems(String statementName, String countryCode);
	List<Object []> queryPartialRefundItems(String statementName, String countryCode);
	/* Level 1-4-1 items */
	List<Ss2spServiceExpenseReportItem> queryServiceExpenseReportItems(String domesticTransactionInvoice);
	List<Ss2spServiceExpenseReportItem> queryAllServiceExpenseReportItems(String statementName);
	/* Level 1-5 items */
	List<RemittanceReportItem> queryRemittanceReportItemsIsurToRcvr(BillStatementType type, String statementName);
	/* Level 1-6 items */
	List<RemittanceReportItem> queryRemittanceReportItemsRcvrToIsur(BillStatementType type, String statementName);

	public List<Entry<String,BigDecimal>> queryIvsForImportDutyTransaction(BillStatementType type, String statementName, Country country);
}
