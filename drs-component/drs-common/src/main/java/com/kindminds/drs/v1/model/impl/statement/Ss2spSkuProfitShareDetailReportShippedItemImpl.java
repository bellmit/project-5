package com.kindminds.drs.v1.model.impl.statement;

import java.math.BigDecimal;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.v1.model.report.Ss2spSkuProfitShareDetailReport.Ss2spSkuProfitShareDetailReportShippedItem;

public class Ss2spSkuProfitShareDetailReportShippedItemImpl implements Ss2spSkuProfitShareDetailReportShippedItem {
	
	private String utcDate;
	private int marketplaceId;
	private String orderId;
	private BigDecimal pretaxPrincipalPrice;
	private BigDecimal msdcRetainment;
	private BigDecimal marketplaceFee;
	private BigDecimal marketplaceFeeNonRefundable;
	private BigDecimal fcaInMarketSideCurrency;
	private BigDecimal fulfillmentFee;
	private BigDecimal ssdcRetainment;
	private BigDecimal supplierProfitShare;
	
	public Ss2spSkuProfitShareDetailReportShippedItemImpl(
			String utcDate,
			int marketplaceId,
			String orderId,
			BigDecimal pretaxPrincipalPrice,
			BigDecimal msdcRetainment,
			BigDecimal marketplaceFee,
			BigDecimal marketplaceFeeNonRefundable,
			BigDecimal fulfillmentFee,
			BigDecimal ssdcRetainment,
			BigDecimal fcaInMarketSideCurrency,
			BigDecimal supplierProfitShare){
		this.utcDate = utcDate;
		this.marketplaceId = marketplaceId;
		this.orderId = orderId;
		this.pretaxPrincipalPrice = pretaxPrincipalPrice;
		this.msdcRetainment = msdcRetainment;
		this.marketplaceFee = marketplaceFee;
		this.marketplaceFeeNonRefundable = marketplaceFeeNonRefundable;
		this.fulfillmentFee = fulfillmentFee;
		this.ssdcRetainment = ssdcRetainment;
		this.fcaInMarketSideCurrency = fcaInMarketSideCurrency;
		this.supplierProfitShare = supplierProfitShare;
	}
	
	public BigDecimal getRawSubtotal(){
		return this.supplierProfitShare;
	}

	@Override
	public String getUtcDate(){
		return this.utcDate;
	}

	@Override
	public Marketplace getMarketplace() {
		return Marketplace.fromKey(this.marketplaceId);
	}

	@Override
	public String getOrderId() {
		return this.orderId;
	}

	@Override
	public String getPretaxPrincipalPrice() {
		return this.pretaxPrincipalPrice.toPlainString();
	}

	@Override
	public String getFcaInMarketSideCurrency() {
		return "-"+this.fcaInMarketSideCurrency.toPlainString();
	}

	@Override
	public String getMarketplaceFee() {
		BigDecimal tempNonRefundable = this.marketplaceFeeNonRefundable==null?BigDecimal.ZERO:this.marketplaceFeeNonRefundable;
		return "-"+this.marketplaceFee.add(tempNonRefundable).toPlainString();
	}

	@Override
	public String getFulfillmentFee() {
		return "-"+this.fulfillmentFee.toPlainString();
	}

	@Override
	public String getDrsRetainment() {
		return "-"+this.msdcRetainment.add(this.ssdcRetainment).toPlainString();
	}

	@Override
	public String getProfitShare() {
		return this.supplierProfitShare.toPlainString();
	}

}
