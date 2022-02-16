package com.kindminds.drs.v1.model.impl.statement;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.util.Assert;

import com.kindminds.drs.Country;
import com.kindminds.drs.Currency;
import com.kindminds.drs.RevenueGrade;
import com.kindminds.drs.api.v1.model.report.Ss2spProfitShareReport.Ss2spProfitShareReportLineItem;
import com.kindminds.drs.util.BigDecimalHelper;

public class Ss2spProfitShareReportLineItemImpl implements Ss2spProfitShareReportLineItem {
	
	private BigDecimal revenueInUsd;
	private BigDecimal achievedRetainmentRateGrade;
	private BigDecimal effectiveRetainmentRate;
	private Integer sourceCountryId;
	private Integer origCurrencyId;
	private BigDecimal sourceAmount;
	private Integer stmtCurrencyId;
	private BigDecimal statementAmount;
	private BigDecimal exchangeRate;
	
	public BigDecimal getNumericStatementAmount() {
		return this.statementAmount;
	}
	
	public Ss2spProfitShareReportLineItemImpl(
			Integer sourceCountryId,
			Integer origCurrencyId,
			BigDecimal sourceAmount,
			Integer stmtCurrencyId,
			BigDecimal statementAmount) {
		this.revenueInUsd = null;
		this.effectiveRetainmentRate = null;
		this.sourceCountryId = sourceCountryId;
		this.origCurrencyId = origCurrencyId;
		this.sourceAmount = sourceAmount;
		this.stmtCurrencyId = stmtCurrencyId;
		this.statementAmount = statementAmount;
		this.exchangeRate = null;
	}
	
	public Ss2spProfitShareReportLineItemImpl(
			BigDecimal revenueInUsd,
			BigDecimal effectiveRetainmentRate,
			Integer sourceCountryId,
			Integer origCurrencyId,
			BigDecimal sourceAmount,
			Integer stmtCurrencyId,
			BigDecimal statementAmount,
			BigDecimal exchangeRate) {
		this.revenueInUsd = revenueInUsd;
		this.effectiveRetainmentRate = effectiveRetainmentRate;
		this.sourceCountryId = sourceCountryId;
		this.origCurrencyId = origCurrencyId;
		this.sourceAmount = sourceAmount;
		this.stmtCurrencyId = stmtCurrencyId;
		this.statementAmount = statementAmount;
		this.exchangeRate = exchangeRate;
	}

	public Ss2spProfitShareReportLineItemImpl(
			BigDecimal revenueInUsd,
			BigDecimal achievedRetainmentRateGrade,
			BigDecimal effectiveRetainmentRate,
			Integer sourceCountryId,
			Integer origCurrencyId,
			BigDecimal sourceAmount,
			Integer stmtCurrencyId,
			BigDecimal statementAmount,
			BigDecimal exchangeRate) {
		this.revenueInUsd = revenueInUsd;
		this.achievedRetainmentRateGrade = achievedRetainmentRateGrade;
		this.effectiveRetainmentRate = effectiveRetainmentRate;
		this.sourceCountryId = sourceCountryId;
		this.origCurrencyId = origCurrencyId;
		this.sourceAmount = sourceAmount;
		this.stmtCurrencyId = stmtCurrencyId;
		this.statementAmount = statementAmount;
		this.exchangeRate = exchangeRate;
	}

	@Override
	public void setAchievedRetainmentRateGrade(BigDecimal achievedRetainmentRateGrade) {
		this.achievedRetainmentRateGrade = achievedRetainmentRateGrade;
	}

	@Override
	public String getRevenueInUsd() {
		if(this.revenueInUsd==null) return null;
		return this.revenueInUsd.toPlainString();
	}

	@Override
	public String getAchievedRetainmentRateGrade() {
		if (this.revenueInUsd == null) return null;
		if (this.achievedRetainmentRateGrade == null) {
			achievedRetainmentRateGrade = RevenueGrade.getGrade(this.revenueInUsd).getRetainmentRate();
		}
		return BigDecimalHelper.toPercentageString(achievedRetainmentRateGrade,2);
	}

	@Override
	public String getEffectiveRetainmentRate() {
		if(this.effectiveRetainmentRate==null) return null;
		return BigDecimalHelper.toPercentageString(this.effectiveRetainmentRate, 4);
	}

	@Override
	public String getSourceCountry() {
		Country country = Country.fromKey(this.sourceCountryId);
		Assert.notNull(country);
		return country.name();
	}
	
	@Override
	public String getSourceCurrency() {
		Currency currency = Currency.fromKey(this.origCurrencyId);
		Assert.notNull(currency);
		return currency.name();
	}

	@Override
	public String getSourceAmount() {
		Currency currency = Currency.valueOf(this.getSourceCurrency());
		return this.sourceAmount.setScale(currency.getScale(),RoundingMode.HALF_UP).toString();
	}
	
	@Override
	public String getStatementCurrency() {
		Currency currency = Currency.fromKey(this.stmtCurrencyId);
		Assert.notNull(currency);
		return currency.name();
	}
	
	@Override
	public String getStatementAmount() {
		Currency currency = Currency.valueOf(this.getStatementCurrency());
		return this.statementAmount.setScale(currency.getScale()).toString();
	}

	@Override
	public String getExchangeRate() {
		if(this.exchangeRate==null) return null;
		return this.exchangeRate.toPlainString();
	}

	@Override
	public String toString() {
		return "Ss2spProfitShareReportLineItemImpl [getSourceCountry()=" + getSourceCountry() + ", getSourceCurrency()="
				+ getSourceCurrency() + ", getSourceAmount()=" + getSourceAmount() + ", getStatementCurrency()="
				+ getStatementCurrency() + ", getStatementAmount()=" + getStatementAmount() + ", getExchangeRate()="
				+ getExchangeRate() + "]";
	}

}
