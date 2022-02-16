package com.kindminds.drs.v1.model.impl;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.accounting.DrsTransaction.DrsTransactionLineItemSource;

import java.math.BigDecimal;

public class DrsTransactionLineItemSourceSvcImpl implements DrsTransactionLineItemSource {
	
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
	
	public DrsTransactionLineItemSourceSvcImpl(
			Currency currency,
			BigDecimal pretaxPrincipalPrice,
			BigDecimal msdcRetainment,
			BigDecimal marketplaceFee,
			BigDecimal marketplaceFeeNonRefundable,
			BigDecimal fulfillmentFee,
			BigDecimal ssdcRetainment,
			BigDecimal fcaInMarketsideCurrency,
			BigDecimal spProfitShare,
			BigDecimal refundFee){
		this.currency = currency;
		this.pretaxPrincipalPrice = pretaxPrincipalPrice;
		this.msdcRetainment = msdcRetainment;
		this.marketplaceFee = marketplaceFee;
		this.marketplaceFeeNonRefundable = marketplaceFeeNonRefundable;
		this.fulfillmentFee = fulfillmentFee;
		this.ssdcRetainment = ssdcRetainment;
		this.fcaInMarketsideCurrency = fcaInMarketsideCurrency;
		this.spProfitShare = spProfitShare;
		this.refundFee = refundFee;
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
