package com.kindminds.drs.v1.model.impl.close;

import com.kindminds.drs.Country;
import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.close.CustomerCaseMsdcMsgChargeInfo;

import java.math.BigDecimal;

public class CustomerCaseMsdcMsgChargeInfoImpl implements CustomerCaseMsdcMsgChargeInfo {
	
	private int id;
	private Country country;
	private Currency originCurrency;
	private BigDecimal chargeInOriginCurrency;
	private Currency actualCurrency;
	private BigDecimal chargeInActualCurrency;
	
	public void setActualCurrency(Currency actualCurrency) {
		this.actualCurrency = actualCurrency;
	}
	
	public void setChargeInActualCurrency(BigDecimal chargeInActualCurrency) {
		this.chargeInActualCurrency = chargeInActualCurrency;
	}
	
	public CustomerCaseMsdcMsgChargeInfoImpl(
			int id,
			Country country,
			Currency originCurrency,
			BigDecimal chargeInOriginCurrency){
		this.id = id;
		this.country = country;
		this.originCurrency = originCurrency;
		this.chargeInOriginCurrency = chargeInOriginCurrency;
	}

	@Override
	public String toString() {
		return "CustomerCaseMsdcMsgChargeInfoImpl [getId()=" + getId() + ", getCountry()=" + getCountry()
				+ ", getOriginCurrency()=" + getOriginCurrency() + ", getChargeInOriginCurrency()="
				+ getChargeInOriginCurrency() + ", getActualCurrency()=" + getActualCurrency()
				+ ", getChargeInActualCurrency()=" + getChargeInActualCurrency() + "]";
	}

	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public Country getCountry() {
		return this.country;
	}

	@Override
	public Currency getOriginCurrency() {
		return this.originCurrency;
	}

	@Override
	public BigDecimal getChargeInOriginCurrency() {
		return this.chargeInOriginCurrency;
	}

	@Override
	public Currency getActualCurrency() {
		return this.actualCurrency;
	}

	@Override
	public BigDecimal getChargeInActualCurrency() {
		return this.chargeInActualCurrency;
	}

}
