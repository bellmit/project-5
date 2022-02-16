package com.kindminds.drs.api.data.access.usecase.report.amazon;

import java.util.List;

import com.kindminds.drs.api.v1.model.accounting.AmazonSettlementReportInfo;
import com.kindminds.drs.api.v1.model.accounting.AmazonSettlementReportRawLine;

public interface ImportAmazonSettlementReportDao {

	public Boolean isReportImported(String settlementId);
	public void clearUncompletedInfoAndLines(String settlementId);
	public void insertReportInfo(AmazonSettlementReportInfo info,String dateTimeFormat);
	public void insertReportInfoAsImported(String settlementId,String settlementStartDate,String settlementEndDate,String depositDate,String totalAmount,String currency,int sourceMarketplaceId);
	public void updateReportAsImported(String settlementId);
	public void insertReportRawLines(List<AmazonSettlementReportRawLine> lines,String dateFormat,String dateTimeFormat,int sourceMatketplaceId);
	public List<Object []> querySettlementReportInfoList();
	
	public enum SettlementReportColumn{
		SETTLEMENT_ID("settlement-id"),
		SETTLEMENT_START_DATE("settlement-start-date"),
		SETTLEMENT_END_DATE("settlement-end-date"),
		DEPOSIT_DATE("deposit-date"),
		TOTAL_AMOUNT("total-amount"),
		CURRENCY("currency"),
		TRANSACTION_TYPE("transaction-type"),
		ORDER_ID("order-id"),
		MERCHANT_ORDER_ID("merchant-order-id"),
		ADJUSTMENT_ID("adjustment-id"),
		SHIPMENT_ID("shipment-id"),
		MARKETPLACE_NAME("marketplace-name"),
		AMOUNT_TYPE("amount-type"),
		AMOUNT_DESCRIPTION("amount-description"),
		AMOUNT("amount"),
		FULFILLMENT_ID("fulfillment-id"),
		POSTED_DATE("posted-date"),
		POSTED_DATE_TIME("posted-date-time"),
		ORDER_ITEM_CODE("order-item-code"),
		MERCHANT_ORDER_ITEM_ID("merchant-order-item-id"),
		MERCHANT_ADJUSTMENT_ITEM_ID("merchant-adjustment-item-id"),
		SKU("sku"),
		QUANTITY_PURCHASED("quantity-purchased"),
		PROMOTION_ID("promotion-id");
		private String name = null;
		SettlementReportColumn(String name){this.name= name;}
		public String getName(){return this.name;}
	}
	
	public boolean settlementIdExist(String settlementId);
	public void insertReportRawLine(List<AmazonSettlementReportRawLine> line,String dateFormat,String dateTimeFormat,int sourceMatketplaceId);
}
