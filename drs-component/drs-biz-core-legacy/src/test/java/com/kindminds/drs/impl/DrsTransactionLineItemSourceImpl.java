package com.kindminds.drs.impl;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.accounting.DrsTransaction.DrsTransactionLineItemSource;
import com.kindminds.drs.util.TestUtil;

import java.math.BigDecimal;

public class DrsTransactionLineItemSourceImpl implements DrsTransactionLineItemSource {
	
	private Currency currency;
	private BigDecimal pretaxPrincipalPrice;
	private BigDecimal msdcRetainment;
	private BigDecimal marketplaceFee;
	private BigDecimal marketplaceFeeNonRefundable;
	private BigDecimal fulfillmentFee;
	private BigDecimal ssdcRetainment;
	private BigDecimal fcaInMarketsideCurrency;
	private BigDecimal spProfitShare;
	private BigDecimal refundFee;
	
	public DrsTransactionLineItemSourceImpl(
			String currency,
			String pretaxPrincipalPrice,
			String msdcRetainment,
			String marketplaceFee,
			String marketplaceFeeNonRefundable,
			String fulfillmentFee,
			String ssdcRetainment,
			String fcaInMarketsideCurrency,
			String spProfitShare,
			String refundFee){
		this.currency = Currency.valueOf(currency);
		this.pretaxPrincipalPrice = new BigDecimal(pretaxPrincipalPrice);
		this.msdcRetainment = new BigDecimal(msdcRetainment);
		this.marketplaceFee = new BigDecimal(marketplaceFee);
		this.marketplaceFeeNonRefundable = marketplaceFeeNonRefundable==null?null:new BigDecimal(marketplaceFeeNonRefundable);
		this.fulfillmentFee = new BigDecimal(fulfillmentFee);
		this.ssdcRetainment = new BigDecimal(ssdcRetainment);
		this.fcaInMarketsideCurrency = new BigDecimal(fcaInMarketsideCurrency);
		this.spProfitShare = new BigDecimal(spProfitShare);
		this.refundFee = refundFee==null?null:new BigDecimal(refundFee);
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj instanceof DrsTransactionLineItemSource){
			DrsTransactionLineItemSource elements = (DrsTransactionLineItemSource)obj;
			return this.getCurrency().equals(elements.getCurrency())
				&& this.getPretaxPrincipalPrice().equals(elements.getPretaxPrincipalPrice())
				&& this.getMsdcRetainment().equals(elements.getMsdcRetainment())
				&& this.getMarketplaceFee().equals(elements.getMarketplaceFee())
				&& TestUtil.nullableEquals(this.getMarketplaceFeeNonRefundable(),elements.getMarketplaceFeeNonRefundable())
				&& this.getFulfillmentFee().equals(elements.getFulfillmentFee())
				&& this.getSsdcRetainment().equals(elements.getSsdcRetainment())
				&& this.getFcaInMarketsideCurrency().equals(elements.getFcaInMarketsideCurrency())
				&& this.getSpProfitShare().equals(elements.getSpProfitShare())
				&& TestUtil.nullableEquals(this.getRefundFee(),elements.getRefundFee());
		}
		return false;
	}

	@Override
	public String toString() {
		return "DrsTransactionSettleableItemElementsImpl [getCurrency()=" + getCurrency()
				+ ", getPretaxPrincipalPrice()=" + getPretaxPrincipalPrice() + ", getMsdcRetainment()="
				+ getMsdcRetainment() + ", getMarketplaceFee()=" + getMarketplaceFee()
				+ ", getMarketplaceFeeNonRefundable()=" + getMarketplaceFeeNonRefundable() + ", getFulfillmentFee()="
				+ getFulfillmentFee() + ", getSsdcRetainment()=" + getSsdcRetainment()
				+ ", getFcaInMarketsideCurrency()=" + getFcaInMarketsideCurrency() + ", getSpProfitShare()="
				+ getSpProfitShare() + ", getRefundFee()=" + getRefundFee() + "]";
	}

	@Override
	public Currency getCurrency() {
		return this.currency;
	}
	
	@Override
	public BigDecimal getPretaxPrincipalPrice() {
		return this.pretaxPrincipalPrice;
	}

	@Override
	public BigDecimal getMsdcRetainment() {
		return this.msdcRetainment;
	}

	@Override
	public BigDecimal getMarketplaceFee() {
		return this.marketplaceFee;
	}

	@Override
	public BigDecimal getMarketplaceFeeNonRefundable() {
		return this.marketplaceFeeNonRefundable;
	}

	@Override
	public BigDecimal getFulfillmentFee() {
		return this.fulfillmentFee;
	}

	@Override
	public BigDecimal getSsdcRetainment() {
		return this.ssdcRetainment;
	}

	@Override
	public BigDecimal getFcaInMarketsideCurrency() {
		return this.fcaInMarketsideCurrency;
	}

	@Override
	public BigDecimal getSpProfitShare() {
		return this.spProfitShare;
	}

	@Override
	public BigDecimal getRefundFee() {
		return this.refundFee;
	}

}
