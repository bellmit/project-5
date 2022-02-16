package com.kindminds.drs.service.test.dto.statement;

import com.kindminds.drs.api.v1.model.report.Ms2ssSettleableItemReport.Ms2ssSettleableItemReportLineItem;
import com.kindminds.drs.util.TestUtil;

import java.math.BigDecimal;

public class Ms2ssSettleableItemReportLineItemImpl implements Ms2ssSettleableItemReportLineItem {

	private String transactionTimeUtc;
	private String sku;
	private String skuName;
	private String name;
	private String description;
	private String sourceName;
	private String currency;
	private String amount;
	private BigDecimal numericAmount;

	public Ms2ssSettleableItemReportLineItemImpl(
			String transactionTimeUtc,
			String sku,
			String skuName,
			String name,
			String description,
			String sourceName,
			String currency,
			String amount,
			String numericAmount){
		this.transactionTimeUtc = transactionTimeUtc;
		this.sku = sku;
		this.skuName = skuName;
		this.name = name;
		this.description = description;
		this.sourceName = sourceName;
		this.currency = currency;
		this.amount = amount;
		this.numericAmount = new BigDecimal(numericAmount);
	}

	@Override
	public boolean equals(Object obj){
		if(obj instanceof Ms2ssSettleableItemReportLineItem){
			Ms2ssSettleableItemReportLineItem item = (Ms2ssSettleableItemReportLineItem)obj;
			return this.getTransactionTimeUtc().equals(item.getTransactionTimeUtc())
				&& TestUtil.nullableEquals(this.getSku(),item.getSku())
				&& TestUtil.nullableEquals(this.getSkuName(),item.getSkuName())
				&& this.getName().equals(item.getName())
				&& TestUtil.nullableEquals(this.getDescription(),item.getDescription())
				&& TestUtil.nullableEquals(this.getSourceName(),item.getSourceName())
				&& this.getCurrency().equals(item.getCurrency())
				&& this.getAmount().equals(item.getAmount())
				&& this.getNumericAmount().equals(item.getNumericAmount());
		}
		return false;
	}

	@Override
	public String toString() {
		return "Ms2ssSettleableItemReportLineItemImpl [getTransactionTimeUtc()=" + getTransactionTimeUtc()
				+ ", getSku()=" + getSku() + ", getSkuName()=" + getSkuName() + ", getName()=" + getName()
				+ ", getDescription()=" + getDescription() + ", getSourceName()=" + getSourceName() + ", getCurrency()="
				+ getCurrency() + ", getAmount()=" + getAmount() + ", getNumericAmount()=" + getNumericAmount() + "]";
	}

	@Override
	public String getTransactionTimeUtc() {
		return this.transactionTimeUtc;
	}

	@Override
	public String getSku() {
		return this.sku;
	}

	@Override
	public String getSkuName() {
		return this.skuName;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public String getSourceName() {
		return this.sourceName;
	}

	@Override
	public String getCurrency() {
		return this.currency;
	}

	@Override
	public String getAmount() {
		return this.amount;
	}

	@Override
	public BigDecimal getNumericAmount() {
		return this.numericAmount;
	}

}
