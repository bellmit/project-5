package com.kindminds.drs.api.data.access.usecase.accounting;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.kindminds.drs.Country;
import com.kindminds.drs.Currency;
import com.kindminds.drs.Marketplace;


import com.kindminds.drs.api.v2.biz.domain.model.accounting.MarketSideTransaction;


import com.kindminds.drs.api.v1.model.amazon.AmazonReimbursementInfo;
import com.kindminds.drs.api.v1.model.amazon.AmazonTransaction;
import com.kindminds.drs.api.v1.model.amazon.AmazonTransaction.AmazonTransactionLineItem;
import com.kindminds.drs.api.v1.model.amazon.AmazonTransaction.AmazonTransactionLineItem.AmzAmountTypeDesc;
import com.kindminds.drs.enums.Settlement;
import com.kindminds.drs.api.v2.biz.domain.model.settlement.MarketTransaction;

public interface ProcessMarketSideTransactionDao {

	List<Object []> querySettlementPeriodList();
	Object[] queryReturnRawColumns(Date returnDate,String sourceTransactionId,String drsSku,String disposition);
	String queryShipmentIvsName(String sourceTransactionId,String productSku,String type);
	String queryShipmentUnsName(String sourceTransactionId,String productSku,String type);
	BigInteger queryShipmentLineItemId(String unsName,String ivsName, String productSku);
	void incrementReturnQuantityByLineItemId(BigInteger lineItemId, Integer quantity);
	void incrementReturnQuantity(String unsName,String ivsName, String productSku, Integer quantity);
	List<Object[]> queryFbaTransactions(Date start, Date end, String type,
										Settlement.AmazonReturnReportDetailedDisposition disposition,
										Settlement.AmazonReturnReportLineStatus status, String companyCode);
	List<Object[]> queryAmazonOrdersRefunds(Date start,Date end,List<String> ignoreMerchantOrderIdPrefixs, String companyCode);
	List<Object[]> queryEbayTransactionInfos(Date start,Date end,String type, String companyCode);
	List<Object[]> queryAmazonOtherTransactions(Date start,Date end, List<String> ignoreMerchantOrderIdPrefixs,
												String companyCode);
	List<Object[]> queryShopifyOrderIdToTransactionDateMap(Marketplace marketplace,String type,
														   Date startPoint,Date endPoint, String companyCode);
	List<Object[]> queryFbaReturnToSupplierTransactions(Date startPoint,Date endPoint, String companyCode);

	List<Object []> queryNonProcessedTransactionList(String companyCode);
	List<Object []> queryNonProcessedTransactionList(int nonProcessedTransactionId);

	int insertMarketTransactions(List<MarketTransaction> transactions);
	int insertTransactions(List<MarketSideTransaction> transactions);

	int deleteNonProcessedTransactions();
	void setTransactionProcessed(int transactionId,boolean value);
	Date queryLatestStatementPeriodEndDateUtc();
	String querySupplierKcode(String productSku);
	String queryHandlerDrsKcode(String supplierKcode);
	String queryMsdcKcode(Country country);
	Currency queryShipmentCurrency(String ivsNameOrdr);
	boolean settlementIdExist(String settlementId);
	String queryDrsCompanyKcodeByCountry(String countryCode);
	String queryCompanyKcodeBySku(String skuCode);
	String querySettlementId(Date postedDateTime);

	BigDecimal queryAmazonPricePrincipalAmountSum(Marketplace marketplace,String orderId,String drsSku);
	BigDecimal queryAmazonPricePrincipalQuantitySum(Marketplace marketplace,String orderId,String drsSku);
	BigDecimal queryFeeFromAmazonSettlementReport(String orderId,String settlementId,Currency currency,AmzAmountTypeDesc typeDesc);
	Integer queryPerUnitFeeQuantityFromAmazonSettlementReportForShopify(String merchantOrderId,Currency currency,String sku);
	BigDecimal queryPerUnitFeeAmountFromAmazonSettlementReportForShopify(String merchantOrderId,Currency currency,String sku);
	List<Object []> queryAmazonSettlementReportTransactionInfo(Date postedDateTime,String transactionType,int sourceMarketplaceId,String orderId,String amountType,String amountDescription,String sku);
	List<AmazonTransaction> querySpecificTrans(String settlementId,Date postedDateTime,String ordderId);
	List<AmazonTransactionLineItem> queryTransactionLineItems(String settlementId, AmazonTransaction tran, List<String> excludeAmzAmountDescList);
	int querySourceMarketplaceKey(String settlementId);

	BigDecimal queryRetainmentRate(Date start,Date end,Country country,String supplierKcode);
	BigDecimal queryProductCategoryTrueToSourceFeeRate(String sku);
	String queryEbayDrsSku(String sourceId);
	Integer queryEbayMarketplaceId(String sourceId);
	BigDecimal queryEbayPretaxPrice(String sourceTransactionId, String drsSku);
	BigDecimal queryEbayMarketplaceFee(String sourceTransactionId, String drsSku);
	BigDecimal queryEbayFulfillmentFee(String sourceTransactionId, String drsSku);
	List<Integer> queryDrsTransactionIds(String type, String sourceId, String sku);

	Object [] queryShopifyOrderReportOrderInfo(String orderId);

	Object [] queryShopifyOrderReportOrderLineInfo(Integer marketplaceId,String orderId,String sku);

	BigDecimal queryShopifyUnitDiscountAmount(String orderId,String sku);
	int queryShopifyOrderQuantity(String orderId,String drsSku);
	int queryShopifyOrderLineItemQuantity(Integer marketplaceId,String orderId,String drsSku);

	BigDecimal queryAbsShopifyShipmentFeeTransportationFromAmazonSettlementReport(String orderId);
	BigDecimal queryAbsShopifyPerOrderFeeFromAmazonSettlementReport(String orderId);
	BigDecimal queryShopifyPerUnitFeeFromAmazonSettlementReport(Integer marketplaceId,String drsSku,String orderId);
	Integer queryFbaReturnToSupplierQuantity(Date transactionDate, String marketplaceName, String drsSku);
	String queryFbaReturnToSupplierIvsName(Date transactionDate, String marketplaceName, String drsSku);
	String queryFbaReturnToSupplierUnsName(Date transactionDate, String marketplaceName, String drsSku);
	List<Object[]> queryFBAIvsUnsListBySku(String sku, String ivsName);
	BigDecimal queryTaxExcludedFcaAmount(String ivsName, String drsSku);
	BigDecimal querySalesTaxRate(String ivsName);
	boolean existMarketSideTransaction(Date startPoint, Date endPoint);
	void deleteMarketSideTransactionException(int id);
	void insertMarketSideTransactionException(int id, String exceptionMsg, String exceptionStacktrace);

	Object [] queryReimbursementInfo(MarketSideTransaction tx,
												   BigDecimal amount, Date postedDate);
	Object [] queryOriginalReimbursementInfoById (String reimbursementId,String sku);
	Object [] queryOriginalIvsUnsInfo(String orderId);
	void updateSoldQuantityByLineItemId(BigInteger lineItemId, BigDecimal quantity);
	Object[] querySettlementInfoOfOriginalReimbursement(AmazonReimbursementInfo origReimbursementInfo);
	Object [] queryOriginalIvsUnsInfoByDateSku(Date postedDateTime, String sku);
	Object[] queryAmountTotalPostedDate(MarketSideTransaction transaction);





}
