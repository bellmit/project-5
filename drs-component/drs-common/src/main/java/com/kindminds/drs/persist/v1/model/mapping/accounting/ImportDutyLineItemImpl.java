package com.kindminds.drs.persist.v1.model.mapping.accounting;

import com.kindminds.drs.api.v1.model.accounting.ImportDutyTransaction;

import java.math.BigDecimal;








public class ImportDutyLineItemImpl implements ImportDutyTransaction.ImportDutyTransactionLineItem {
	
	//@Id ////@Column(name="id")
	private int id;
	//@Column(name="source_ivs_name")
	private String sourceIvsName;
	//@Column(name="sku")
	private String sku;
	//@Column(name="quantity")
	private Integer quantity;
	//@Column(name="amount")
	private BigDecimal amount;

	public ImportDutyLineItemImpl() {
	}

	public ImportDutyLineItemImpl(int id, String sourceIvsName, String sku, Integer quantity, BigDecimal amount) {
		this.id = id;
		this.sourceIvsName = sourceIvsName;
		this.sku = sku;
		this.quantity = quantity;
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "ImportDutyLineItemImpl [getSourceIvsName()=" + getSourceIvsName() + ", getSku()=" + getSku()
				+ ", getQuantity()=" + getQuantity() + ", getAmount()=" + getAmount() + "]";
	}

	@Override
	public String getSourceIvsName() {
		return this.sourceIvsName;
	}

	@Override
	public String getSku() {
		return this.sku;
	}

	@Override
	public Integer getQuantity() {
		return this.quantity;
	}

	@Override
	public String getAmount() {
		if(this.amount==null) return null;
		return this.amount.stripTrailingZeros().toPlainString();
	}

}
