package com.kindminds.drs.v1.model.impl.statement;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.v1.model.report.Ss2spSettleableBrowserForProfitShareToExport;

public class Ss2spSettleableBrowserForProfitShareToExportImpl implements Ss2spSettleableBrowserForProfitShareToExport{

	private String statementName;
	private String dateStart;
	private String dateEnd;
	private String country;	
	private String sourceCurrency;
	private String exchangeRate;
	private String sku;
	private String skuName;
	private String name;
	private String utcDate;
	private Marketplace marketplace;
	private String orderId;
	private Integer quantity;
	private String pretaxPrincipalPrice;
	private String fcaInMarketSideCurrency;
	private String marketplaceFee;
	private String fulfillmentFee;
	private String drsRetainment;
	private String profitShare;
		
	@Override
	public String toString() {
		return "Ss2spSettleableBrowserForProfitShareToExportImpl [getStatementName()=" + getStatementName()
				+ ", getDateStart()=" + getDateStart() + ", getDateEnd()=" + getDateEnd() + ", getCountry()="
				+ getCountry() + ", getSourceCurrency()=" + getSourceCurrency() + ", getExchangeRate()="
				+ getExchangeRate() + ", getSku()=" + getSku() + ", getSkuName()=" + getSkuName() + ", getName()="
				+ getName() + ", getUtcDate()=" + getUtcDate() + ", getMarketplace()=" + getMarketplace()
				+ ", getOrderId()=" + getOrderId() + ", getQuantity()=" + getQuantity() + ", getPretaxPrincipalPrice()="
				+ getPretaxPrincipalPrice() + ", getFcaInMarketSideCurrency()=" + getFcaInMarketSideCurrency()
				+ ", getMarketplaceFee()=" + getMarketplaceFee() + ", getFulfillmentFee()=" + getFulfillmentFee()
				+ ", getDrsRetainment()=" + getDrsRetainment() + ", getProfitShare()=" + getProfitShare() + "]";
	}

	public Ss2spSettleableBrowserForProfitShareToExportImpl(
			String statementName,
			String dateStart,
			String dateEnd,
			String country,	
			String sourceCurrency,
			String exchangeRate,
			String sku,
			String skuName,
			String name,
			String utcDate,
			Marketplace marketplace,
			String orderId,
			Integer quantity,
			String pretaxPrincipalPrice,
			String fcaInMarketSideCurrency,
			String marketplaceFee,
			String fulfillmentFee,
			String drsRetainment,
			String profitShare					
			){
		this.statementName = statementName;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		this.country = country;
		this.sourceCurrency = sourceCurrency;
		this.exchangeRate = exchangeRate;
		this.sku = sku;
		this.skuName = skuName;
		this.name = name;
		this.utcDate = utcDate;
		this.marketplace = marketplace;
		this.orderId = orderId;
		this.quantity = quantity;
		this.pretaxPrincipalPrice = pretaxPrincipalPrice;
		this.fcaInMarketSideCurrency = fcaInMarketSideCurrency;
		this.marketplaceFee = marketplaceFee;
		this.fulfillmentFee = fulfillmentFee;
		this.drsRetainment = drsRetainment;
		this.profitShare = profitShare;
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
	public String getCountry() {
		return this.country;
	}
	
	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public String getSourceCurrency() {
		return this.sourceCurrency;
	}

	public void setSourceCurrency(String sourceCurrency) {
		this.sourceCurrency = sourceCurrency;
	}
	
	@Override
	public String getExchangeRate() {
		return this.exchangeRate;
	}

	public void setExchangeRate(String exchangeRate) {
		this.exchangeRate = exchangeRate;
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
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String getUtcDate() {
		return this.utcDate;
	}

	public void setUtcDate(String utcDate) {
		this.utcDate = utcDate;
	}
	
	@Override
	public Marketplace getMarketplace() {
		return this.marketplace;
	}
	
	public void setMarketplace(Marketplace marketplace) {
		this.marketplace = marketplace;
	}

	@Override
	public String getOrderId() {
		return this.orderId;
	}
	
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	@Override
	public Integer getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
		
	@Override
	public String getPretaxPrincipalPrice() {
		return this.pretaxPrincipalPrice;
	}

	public void setPretaxPrincipalPrice(String pretaxPrincipalPrice) {
		this.pretaxPrincipalPrice = pretaxPrincipalPrice;
	}
		
	@Override
	public String getFcaInMarketSideCurrency() {
		return this.fcaInMarketSideCurrency;
	}

	public void setFcaInMarketSideCurrency(String fcaInMarketSideCurrency) {
		this.fcaInMarketSideCurrency = fcaInMarketSideCurrency;
	}
	
	@Override
	public String getMarketplaceFee() {
		return this.marketplaceFee;
	}

	public void setMarketplaceFee(String marketplaceFee) {
		this.marketplaceFee = marketplaceFee;
	}
	
	@Override
	public String getFulfillmentFee() {
		return this.fulfillmentFee;
	}

	public void setFulfillmentFee(String fulfillmentFee) {
		this.fulfillmentFee = fulfillmentFee;
	}
	
	@Override
	public String getDrsRetainment() {
		return this.drsRetainment;
	}

	public void setDrsRetainment(String drsRetainment) {
		this.drsRetainment = drsRetainment;
	}
	
	@Override
	public String getProfitShare() {
		return this.profitShare;
	}
	
	public void setProfitShare(String profitShare) {
		this.profitShare = profitShare;
	}

}