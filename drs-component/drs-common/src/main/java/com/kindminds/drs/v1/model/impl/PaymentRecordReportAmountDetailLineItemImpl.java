package com.kindminds.drs.v1.model.impl;

import java.math.BigDecimal;





import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.report.InventoryPaymentReport.InventoryPaymentReportAmountDetailLineItem;


public class PaymentRecordReportAmountDetailLineItemImpl implements InventoryPaymentReportAmountDetailLineItem {
	
	//@Id ////@Column(name="id")
	private int id;
	//@Column(name="sku")
	private String sku;
	//@Column(name="statement_name")
	private String statementName;
	//@Column(name="item_name")
	private String itemName;
	//@Column(name="quantity")
	private Integer quantity;
	//@Column(name="currency_id")
	private Integer currencyId;
	//@Column(name="amount")
	private BigDecimal amount;

	public PaymentRecordReportAmountDetailLineItemImpl() {
	}

	public PaymentRecordReportAmountDetailLineItemImpl(int id, String sku, String statementName, String itemName, Integer quantity, Integer currencyId, BigDecimal amount) {
		this.id = id;
		this.sku = sku;
		this.statementName = statementName;
		this.itemName = itemName;
		this.quantity = quantity;
		this.currencyId = currencyId;
		this.amount = amount;
	}

	public BigDecimal getNumericAmount(){ return this.amount; }

	@Override
	public String toString() {
		return "PaymentRecordReportAmountDetailLineItemImpl [getNumericAmount()=" + getNumericAmount() + ", getSku()="
				+ getSku() + ", getStatementName()=" + getStatementName() + ", getItemName()=" + getItemName()
				+ ", getQuantity()=" + getQuantity() + ", getAmount()=" + getAmount() + "]";
	}

	@Override
	public String getSku() {
		return this.sku;
	}

	@Override
	public String getStatementName() {
		return this.statementName;
	}

	@Override
	public String getItemName() {
		return this.itemName;
	}

	@Override
	public String getQuantity() {
		return this.quantity.toString();
	}

	@Override
	public String getAmount() {
		Currency currency = Currency.fromKey(this.currencyId);
		return this.amount.setScale(currency.getScale()).toPlainString();
	}

}
