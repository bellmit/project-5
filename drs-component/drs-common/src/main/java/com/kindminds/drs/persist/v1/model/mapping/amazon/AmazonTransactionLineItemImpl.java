package com.kindminds.drs.persist.v1.model.mapping.amazon;

import java.math.BigDecimal;




import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.amazon.AmazonTransaction.AmazonTransactionLineItem;


public class AmazonTransactionLineItemImpl implements AmazonTransactionLineItem {
	//@Id ////@Column(name="id")
	private int dbId;
	//@Column(name="sku")
	private String sku;
	//@Column(name="currency")
	private String currency;
	//@Column(name="amount_type")
	private String amountType;
	//@Column(name="amount_description")
	private String amountDescription;
	//@Column(name="amount")
	private BigDecimal amount;
	//@Column(name="quantity_purchased")
	private Integer qtyPurchased;

	@Override
	public String getSku() {
		return this.sku;
	}
	@Override
	public Currency getCurrency() {
		return Currency.valueOf(this.currency);
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
	public Integer getQtyPurchased() {
		return this.qtyPurchased;
	}
	@Override
	public String toString() {
		return "AmazonTransactionLineItemImpl [getSku()=" + getSku()
				+ ", getCurrency()=" + getCurrency() + ", getAmountType()="
				+ getAmountType() + ", getAmountDescription()="
				+ getAmountDescription() + ", getAmount()=" + getAmount()
				+ ", getQtyPurchased()=" + getQtyPurchased() + "]";
	}
	
}
