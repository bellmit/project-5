package com.kindminds.drs.v1.model.impl.close;

import com.kindminds.drs.api.v1.model.close.BillStatementLineItemCustomerCaseExemptionInfo;

public class BsliCustomerCaseExemptionInfoImpl implements BillStatementLineItemCustomerCaseExemptionInfo {
	
	private String statementName;
	private String country;
	private String currency;
	private String productBaseCode;
	private String revenue;
	private String freeRate;
	private String freeAmount;
	private String customerCaseAmount;
	private String exemptionAmount;
	
	public BsliCustomerCaseExemptionInfoImpl(
			String statementName,
			String country,
			String currency,
			String productBaseCode,
			String revenue,
			String freeRate,
			String freeAmount,
			String customerCaseAmount,
			String exemptionAmount){
		this.statementName = statementName;
		this.country = country;
		this.currency = currency;
		this.productBaseCode = productBaseCode;
		this.revenue = revenue;
		this.freeRate = freeRate;
		this.freeAmount = freeAmount;
		this.customerCaseAmount = customerCaseAmount;
		this.exemptionAmount = exemptionAmount;
	}
	
	@Override
	public boolean equals(Object object){
		if(object instanceof BillStatementLineItemCustomerCaseExemptionInfo){
			BillStatementLineItemCustomerCaseExemptionInfo info = (BillStatementLineItemCustomerCaseExemptionInfo)object;
			return this.getStatementName().equals(info.getStatementName())
				&& this.getCountry().equals(info.getCountry())
				&& this.getCurrency().equals(info.getCurrency())
				&& this.getProductBaseCode().equals(info.getProductBaseCode())
				&& this.getRevenue().equals(info.getRevenue())
				&& this.getFreeRate().equals(info.getFreeRate())
				&& this.getFreeAmount().equals(info.getFreeAmount())
				&& this.getCustomerCaseFeeAmount().equals(info.getCustomerCaseFeeAmount())
				&& this.getExemptionAmount().equals(info.getExemptionAmount());
		}
		return false;
	}

	@Override
	public String toString() {
		return "BsliCustomerCaseExemptionInfoImpl [getStatementName()=" + getStatementName() + ", getCountry()="
				+ getCountry() + ", getCurrency()=" + getCurrency() + ", getProductBaseCode()=" + getProductBaseCode()
				+ ", getRevenue()=" + getRevenue() + ", getFreeRate()=" + getFreeRate() + ", getFreeAmount()="
				+ getFreeAmount() + ", getCustomerCaseFeeAmount()=" + getCustomerCaseFeeAmount()
				+ ", getExemptionAmount()=" + getExemptionAmount() + "]";
	}

	@Override
	public String getStatementName() {
		return this.statementName;
	}

	@Override
	public String getCountry() {
		return this.country;
	}

	@Override
	public String getCurrency() {
		return this.currency;
	}

	@Override
	public String getProductBaseCode() {
		return this.productBaseCode;
	}

	@Override
	public String getRevenue() {
		return this.revenue;
	}

	@Override
	public String getFreeRate() {
		return this.freeRate;
	}

	@Override
	public String getFreeAmount() {
		return this.freeAmount;
	}

	@Override
	public String getCustomerCaseFeeAmount() {
		return this.customerCaseAmount;
	}

	@Override
	public String getExemptionAmount() {
		return this.exemptionAmount;
	}

}
