package com.kindminds.drs.persist.v1.model.mapping.accounting;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;





import com.kindminds.drs.Country;
import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.accounting.RetainmentRate;
import com.kindminds.drs.util.DateHelper;


public class RetainmentRateImpl implements RetainmentRate {
	
	//@Id ////@Column(name="id")
	private int id;
	//@Column(name="utc_date_start")
	private Date utcDateStart;
	//@Column(name="utc_date_end")
	private Date utcDateEnd;
	//@Column(name="country_id")
	private Integer countryId;
	//@Column(name="supplier_kcode")
	private String supplierKcode;
	//@Column(name="original_currency_id")
	private Integer originalCurrencyId;
	//@Column(name="revenue_in_original_currency")
	private BigDecimal revenueInOriginalCurrency;
	//@Column(name="currency_exchange_rate_to_usd")
	private BigDecimal currencyExchangeRateToUsd;
	//@Column(name="revenue_in_usd")
	private BigDecimal revenueInUsd;
	//@Column(name="rate")
	private BigDecimal rate;

	public RetainmentRateImpl(){

	}

	public RetainmentRateImpl(int id, Date utcDateStart, Date utcDateEnd, Integer countryId, String supplierKcode, Integer originalCurrencyId, BigDecimal revenueInOriginalCurrency, BigDecimal currencyExchangeRateToUsd, BigDecimal revenueInUsd, BigDecimal rate) {
		this.id = id;
		this.utcDateStart = utcDateStart;
		this.utcDateEnd = utcDateEnd;
		this.countryId = countryId;
		this.supplierKcode = supplierKcode;
		this.originalCurrencyId = originalCurrencyId;
		this.revenueInOriginalCurrency = revenueInOriginalCurrency;
		this.currencyExchangeRateToUsd = currencyExchangeRateToUsd;
		this.revenueInUsd = revenueInUsd;
		this.rate = rate;
	}


	@Override
	public String toString() {
		return "RetainmentRateImpl [getId()=" + getId() + ", getUtcDateStart()=" + getUtcDateStart()
				+ ", getUtcDateEnd()=" + getUtcDateEnd() + ", getCountry()=" + getCountry() + ", getSupplierKcode()="
				+ getSupplierKcode() + ", getOriginalCurrency()=" + getOriginalCurrency()
				+ ", getRevenueInOriginalCurrency()=" + getRevenueInOriginalCurrency()
				+ ", getCurrencyExchangeRateToUsd()=" + getCurrencyExchangeRateToUsd() + ", getRevenueInUsd()="
				+ getRevenueInUsd() + ", getRate()=" + getRate() + ", isDeletable()=" + isDeletable() + "]";
	}

	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public String getUtcDateStart() {
		return DateHelper.toString(this.utcDateStart, "yyyy-MM-dd", "UTC");
	}

	@Override
	public String getUtcDateEnd() {
		Date dateEnd = this.getDaysBefore(1, this.utcDateEnd);
		return DateHelper.toString(dateEnd, "yyyy-MM-dd", "UTC");
	}

	@Override
	public String getCountry() {
		return Country.fromKey(this.countryId).name();
	}

	@Override
	public String getSupplierKcode() {
		return this.supplierKcode;
	}
	
	@Override
	public String getOriginalCurrency() {
		return Currency.fromKey(this.originalCurrencyId).name();
	}

	@Override
	public String getRevenueInOriginalCurrency() {
		return this.revenueInOriginalCurrency.toPlainString();
	}

	@Override
	public String getCurrencyExchangeRateToUsd() {
		return this.currencyExchangeRateToUsd.toPlainString();
	}

	@Override
	public String getRevenueInUsd() {
		return this.revenueInUsd.toPlainString();
	}

	@Override
	public String getRate() {
		return this.rate.toPlainString();
	}

	@Override
	public Boolean isDeletable() {
		return null;
	}
	
	private Date getDaysBefore(int days, Date date){
		Calendar c = Calendar.getInstance(); 
		c.setTime(date); 
		c.add(Calendar.DATE,-days);
		return c.getTime();
	}

}
