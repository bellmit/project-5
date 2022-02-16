package com.kindminds.drs.v1.model.impl.statement;

import com.kindminds.drs.api.v1.model.report.Ss2spPaymentAndRefundReportItemToExport;

import java.math.BigDecimal;

public class Ss2spPaymentAndRefundReportItemToExportImpl implements Ss2spPaymentAndRefundReportItemToExport{

	private String statementName;
	private String dateStart;
	private String dateEnd;
	private String shipmentName;
	private String transactionTimeUtc;		
	private String sku;
	private String skuName;
	private String itemType;
	private String sourceName;
	private String currency;
	private BigDecimal unitAmount;
	private Integer quantity;
	private BigDecimal totalAmount;
		
	@Override
	public String toString() {
		return "Ss2spPaymentAndRefundReportItemToExportImpl [getStatementName()=" + getStatementName()
				+ ", getDateStart()=" + getDateStart() + ", getDateEnd()=" + getDateEnd() + ", getShipmentName()="
				+ getShipmentName() + ", getTransactionTimeUtc()=" + getTransactionTimeUtc() + ", getSku()=" + getSku()
				+ ", getSkuName()=" + getSkuName() + ", getItemType()=" + getItemType() + ", getSourceName()="
				+ getSourceName() + ", getCurrency()=" + getCurrency() + ", getUnitAmount()=" + getUnitAmount()
				+ ", getQuantity()=" + getQuantity() + ", getTotalAmount()=" + getTotalAmount() + "]";
	}

	public Ss2spPaymentAndRefundReportItemToExportImpl(
			String statementName,
			String dateStart,
			String dateEnd,
			String shipmentName,
			String transactionTimeUtc,		
			String sku,
			String skuName,
			String itemType,
			String sourceName,
			String currency,
			BigDecimal unitAmount,
			Integer quantity,
			BigDecimal totalAmount					
			){
		this.statementName = statementName;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		this.shipmentName = shipmentName;
		this.transactionTimeUtc = transactionTimeUtc;
		this.sku = sku;
		this.skuName = skuName;
		this.itemType = itemType;
		this.sourceName = sourceName;
		this.currency = currency;
		this.unitAmount = unitAmount;
		this.quantity = quantity;
		this.totalAmount = totalAmount;
	};
		
	@Override
	public String getStatementName() {
		return this.statementName;
	}
	
	public void setStatementName(String statementName) {
		this.statementName = statementName;
	}
	
	@Override
	public String getDateStart() {
		return this.dateStart;
	}
	
	public void setDateStart(String dateStart) {
		this.dateStart = dateStart;
	}
	
	@Override
	public String getDateEnd() {
		return this.dateEnd;
	}
	
	public void setDateEnd(String dateEnd) {
		this.dateEnd = dateEnd;
	}
	
	@Override
	public String getShipmentName() {
		return this.shipmentName;
	}
	
	public void setShipmentName(String shipmentName) {
		this.shipmentName = shipmentName;
	}
	
	@Override
	public String getTransactionTimeUtc() {
		return this.transactionTimeUtc;
	}
	
	public void setTransactionTimeUtc(String transactionTimeUtc) {
		this.transactionTimeUtc = transactionTimeUtc;
	}
	
	@Override
	public String getSku() {
		return this.sku;
	}
	
	public void setSku(String sku) {
		this.sku = sku;
	}
	
	@Override
	public String getSkuName() {
		return this.skuName;
	}
	
	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}
	
	@Override
	public String getItemType() {
		return this.itemType;
	}
	
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	
	@Override
	public String getSourceName() {
		return this.sourceName;
	}
	
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	
	@Override
	public String getCurrency() {
		return this.currency;
	}
	
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	@Override
	public BigDecimal getUnitAmount() {
		return this.unitAmount;
	}
	
	public void setUnitAmount(BigDecimal unitAmount) {
		this.unitAmount = unitAmount;
	}
	
	@Override
	public Integer getQuantity() {
		return this.quantity;
	}
	
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	@Override
	public BigDecimal getTotalAmount() {
		return this.totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	
}