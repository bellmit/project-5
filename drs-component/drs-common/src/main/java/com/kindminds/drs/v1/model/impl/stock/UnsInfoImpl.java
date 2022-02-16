package com.kindminds.drs.v1.model.impl.stock;

import java.math.BigDecimal;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v2.biz.domain.model.product.SkuShipmentAllocationInfo;


public class UnsInfoImpl implements SkuShipmentAllocationInfo.UnsInfo {
	
	private String name;
	private int ddpCurrencyKey;
	private BigDecimal ddpAmount;
	private BigDecimal fxRateFromFcaCurrencyToDestinationCountryCurrency;
	private BigDecimal fxRateFromFcaCurrencyToEur;

	public UnsInfoImpl(
			String name,
			int ddpCurrencyKey,
			BigDecimal ddpAmount,
			BigDecimal fxRateFromFcaCurrencyToDestinationCountryCurrency,
			BigDecimal fxRateFromFcaCurrencyToEur) {
		super();
		this.name = name;
		this.ddpCurrencyKey = ddpCurrencyKey;
		this.ddpAmount = ddpAmount;
		this.fxRateFromFcaCurrencyToDestinationCountryCurrency = fxRateFromFcaCurrencyToDestinationCountryCurrency;
		this.fxRateFromFcaCurrencyToEur = fxRateFromFcaCurrencyToEur;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Currency getDdpCurrency() {
		return Currency.fromKey(this.ddpCurrencyKey);
	}

	@Override
	public BigDecimal getDdpAmount() {
		return this.ddpAmount;
	}

	@Override
	public BigDecimal getFxRateFromFcaCurrencyToDestinationCountryCurrency() {
		return this.fxRateFromFcaCurrencyToDestinationCountryCurrency;
	}

	@Override
	public BigDecimal getFxRateFromFcaCurrencyToEur() {
		return this.fxRateFromFcaCurrencyToEur;
	}

}
