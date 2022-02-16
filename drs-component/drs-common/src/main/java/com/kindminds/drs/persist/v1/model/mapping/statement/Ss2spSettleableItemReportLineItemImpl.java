package com.kindminds.drs.persist.v1.model.mapping.statement;

import java.math.BigDecimal;




import com.kindminds.drs.api.v1.model.report.Ss2spSettleableItemReport.Ss2spSettleableItemReportLineItem;



public class Ss2spSettleableItemReportLineItemImpl implements Ss2spSettleableItemReportLineItem{
	
	//@Id ////@Column(name="id")
	private int id;
	//@Column(name="time_utc")
	private String transactionTimeUtc;
	//@Column(name="sku")
	private String sku;
	//@Column(name="sku_name")
	private String skuName;
	//@Column(name="name")
	private String name;
	//@Column(name="description")
	private String description;
	//@Column(name="sourceName")
	private String sourceName;
	//@Column(name="original_currency_name")
	private String currency;
	//@Column(name="original_amount")
	private BigDecimal amount;

	public Ss2spSettleableItemReportLineItemImpl() {
	}

	public Ss2spSettleableItemReportLineItemImpl(int id, String transactionTimeUtc, String sku, String skuName, String name, String description, String sourceName, String currency, BigDecimal amount) {
		this.id = id;
		this.transactionTimeUtc = transactionTimeUtc;
		this.sku = sku;
		this.skuName = skuName;
		this.name = name;
		this.description = description;
		this.sourceName = sourceName;
		this.currency = currency;
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "Ss2spSettleableItemReportLineItemImpl [getTransactionTimeUtc()=" + getTransactionTimeUtc()
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
		return this.amount.toString();
	}

	@Override
	public BigDecimal getNumericAmount() {
		return this.amount;
	}
	
}
