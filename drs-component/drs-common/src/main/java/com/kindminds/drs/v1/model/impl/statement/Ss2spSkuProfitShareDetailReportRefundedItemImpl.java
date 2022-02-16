package com.kindminds.drs.v1.model.impl.statement;

import java.math.BigDecimal;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.v1.model.report.Ss2spSkuProfitShareDetailReport.Ss2spSkuProfitShareDetailReportRefundedItem;

public class Ss2spSkuProfitShareDetailReportRefundedItemImpl implements Ss2spSkuProfitShareDetailReportRefundedItem {
	
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
	private BigDecimal refundFee;
	
	public Ss2spSkuProfitShareDetailReportRefundedItemImpl(
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
			BigDecimal supplierProfitShare,
			BigDecimal refundFee){
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
		this.refundFee = refundFee;
	}
	
	public BigDecimal getRawSubtotal(){
		BigDecimal tempMarketplaceFeeNonRefundable = this.marketplaceFeeNonRefundable==null?BigDecimal.ZERO:this.marketplaceFeeNonRefundable;
		return this.supplierProfitShare.add(this.fulfillmentFee).add(this.refundFee).add(tempMarketplaceFeeNonRefundable);
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
		return this.fcaInMarketSideCurrency.negate().toPlainString();
	}

	@Override
	public String getMarketplaceFee() {
		return this.marketplaceFee.negate().toPlainString();
	}

	@Override
	public String getDrsRetainment() {
		return this.msdcRetainment.add(this.ssdcRetainment).negate().toPlainString();
	}

	@Override
	public String getRefundFee() {
		return this.refundFee.toPlainString();
	}

	@Override
	public String getProfitShare() {
		return this.getRawSubtotal().toPlainString();
	}

}
