package com.kindminds.drs.persist.v1.model.mapping.close;

import java.math.BigDecimal;



import com.kindminds.drs.Country;
import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.close.BillStatementLineItemCustomerCaseExemptionInfo;


public class BsliCustomerCaseExemptionInfoImpl implements BillStatementLineItemCustomerCaseExemptionInfo {
	
	//@Id ////@Column(name="id")
	private int id;
	//@Column(name="statement_name")
	private String statementName;
	//@Column(name="country_id")
	private Integer countryId;
	//@Column(name="currency_id")
	private Integer currencyId;
	//@Column(name="product_base_code")
	private String productBaseCode;
	//@Column(name="revenue")
	private BigDecimal revenue;
	//@Column(name="free_rate")
	private BigDecimal freeRate;
	//@Column(name="free_amount")
	private BigDecimal freeAmount;
	//@Column(name="customer_case_amount")
	private BigDecimal customerCaseAmount;
	//@Column(name="exemption_amount")
	private BigDecimal exemptionAmount;

	public BsliCustomerCaseExemptionInfoImpl() {
	}

	public BsliCustomerCaseExemptionInfoImpl(int id, String statementName, Integer countryId, Integer currencyId, String productBaseCode, BigDecimal revenue, BigDecimal freeRate, BigDecimal freeAmount, BigDecimal customerCaseAmount, BigDecimal exemptionAmount) {
		this.id = id;
		this.statementName = statementName;
		this.countryId = countryId;
		this.currencyId = currencyId;
		this.productBaseCode = productBaseCode;
		this.revenue = revenue;
		this.freeRate = freeRate;
		this.freeAmount = freeAmount;
		this.customerCaseAmount = customerCaseAmount;
		this.exemptionAmount = exemptionAmount;
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
		return Country.fromKey(this.countryId).name();
	}

	@Override
	public String getCurrency() {
		return Currency.fromKey(this.currencyId).name();
	}

	@Override
	public String getProductBaseCode() {
		return this.productBaseCode;
	}

	@Override
	public String getRevenue() {
		int scale = Currency.fromKey(this.currencyId).getScale();
		return this.revenue.setScale(scale).toPlainString();
	}

	@Override
	public String getFreeRate() {
		return this.freeRate.stripTrailingZeros().toPlainString();
	}

	@Override
	public String getFreeAmount() {
		return this.freeAmount.stripTrailingZeros().toPlainString();
	}

	@Override
	public String getCustomerCaseFeeAmount() {
		return this.customerCaseAmount.toPlainString();
	}

	@Override
	public String getExemptionAmount() {
		return this.exemptionAmount.toPlainString();
	}

}
