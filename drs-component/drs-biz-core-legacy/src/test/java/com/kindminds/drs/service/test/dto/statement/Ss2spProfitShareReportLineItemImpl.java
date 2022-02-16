package com.kindminds.drs.service.test.dto.statement;

import com.kindminds.drs.api.v1.model.report.Ss2spProfitShareReport.Ss2spProfitShareReportLineItem;
import com.kindminds.drs.util.TestUtil;

import java.math.BigDecimal;

public class Ss2spProfitShareReportLineItemImpl implements Ss2spProfitShareReportLineItem {

	private String sourceCountry;
	private String sourceCurrency;
	private String statementCurrency;
	private String sourceAmount;
	private String statementAmount;
	private String exchangeRate;

	public Ss2spProfitShareReportLineItemImpl(
			String sourceCountry,
			String sourceCurrency,
			String statementCurrency,
			String sourceAmount,
			String statementAmount,
			String exchangeRate){
		this.sourceCountry = sourceCountry;
		this.sourceCurrency = sourceCurrency;
		this.statementCurrency = statementCurrency;
		this.sourceAmount = sourceAmount;
		this.statementAmount = statementAmount;
		this.exchangeRate = exchangeRate;
	}

	public boolean equals(Object obj){
		if(obj instanceof Ss2spProfitShareReportLineItem){
			Ss2spProfitShareReportLineItem item = (Ss2spProfitShareReportLineItem)obj;
			return TestUtil.nullableEquals(this.getRevenueInUsd(),item.getRevenueInUsd())
				&& TestUtil.nullableEquals(this.getAchievedRetainmentRateGrade(),item.getAchievedRetainmentRateGrade())
				&& TestUtil.nullableEquals(this.getEffectiveRetainmentRate(),item.getEffectiveRetainmentRate())
				&& this.getSourceCountry().equals(item.getSourceCountry())
				&& this.getSourceCurrency().equals(item.getSourceCurrency())
				&& this.getStatementCurrency().equals(item.getStatementCurrency())
				&& this.getSourceAmount().equals(item.getSourceAmount())
				&& this.getStatementAmount().equals(item.getStatementAmount())
				&& TestUtil.nullableEquals(this.getExchangeRate(),item.getExchangeRate());
		}
		return false;
	}

	@Override
	public String toString() {
		return "Ss2spProfitShareReportLineItemImpl [getSourceCountry()=" + getSourceCountry() + ", getSourceCurrency()="
				+ getSourceCurrency() + ", getSourceAmount()=" + getSourceAmount() + ", getStatementCurrency()="
				+ getStatementCurrency() + ", getStatementAmount()=" + getStatementAmount() + ", getExchangeRate()="
				+ getExchangeRate() + "]";
	}



	@Override
	public String getRevenueInUsd() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAchievedRetainmentRateGrade(BigDecimal achievedRetainmentRateGrade) {
		// TODO Auto-generated method stub
	}

	@Override
	public String getAchievedRetainmentRateGrade() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEffectiveRetainmentRate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSourceCountry() {
		return this.sourceCountry;
	}

	@Override
	public String getSourceCurrency() {
		return this.sourceCurrency;
	}

	@Override
	public String getSourceAmount() {
		return this.sourceAmount;
	}

	@Override
	public String getStatementCurrency() {
		return this.statementCurrency;
	}

	@Override
	public String getStatementAmount() {
		return this.statementAmount;
	}

	@Override
	public String getExchangeRate() {
		return this.exchangeRate;
	}

}
