package com.kindminds.drs.v1.model.impl.amazon;

import com.kindminds.drs.api.v1.model.amazon.AmazonSettlementReportTransactionInfo;

import java.math.BigDecimal;
import java.util.Date;

public class AmazonSettlementReportTransactionInfoImpl implements AmazonSettlementReportTransactionInfo {

	private String currency;
	private String amountType;
	private String amountDescription;
	private BigDecimal amount;
	private Integer quantityPurchased;
	private Date postedDate;

	public AmazonSettlementReportTransactionInfoImpl(
			String currency, 
			String amountType, 
			String amountDescription,
			BigDecimal amount, 
			Integer quantityPurchased,
			Date postedDate) {
		super();
		this.currency = currency;
		this.amountType = amountType;
		this.amountDescription = amountDescription;
		this.amount = amount;
		this.quantityPurchased = quantityPurchased;
		this.postedDate = postedDate;
	}
	
	@Override
	public String toString() {
		return "AmazonSettlementReportTransactionInfoImpl [getCurrency()=" + getCurrency() + ", getAmountType()="
				+ getAmountType() + ", getAmountDesc()=" + getAmountDescription() + ", getAmount()=" + getAmount()
				+ ", getQuantityPurchased()=" + getQuantityPurchased() + "]";
	}
	
	@Override
	public String getCurrency() {
		return this.currency;
	}

	@Override
	public String getAmountType() {
		return this.amountType;
	}

	@Override
	public String getAmountDescription() {
		return this.amountDescription;
	}

	@Override
	public BigDecimal getAmount() {
		return this.amount;
	}

	@Override
	public Integer getQuantityPurchased() {
		return this.quantityPurchased;
	}

	@Override
	public Date getPostedDate() {
		return postedDate;
	}
}
