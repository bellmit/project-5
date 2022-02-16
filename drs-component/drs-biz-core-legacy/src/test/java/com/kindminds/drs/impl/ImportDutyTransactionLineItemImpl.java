package com.kindminds.drs.impl;

import com.kindminds.drs.api.v1.model.accounting.ImportDutyTransaction.ImportDutyTransactionLineItem;
import com.kindminds.drs.util.TestUtil;

public class ImportDutyTransactionLineItemImpl implements ImportDutyTransactionLineItem {
	
	private String sourceIvsName;
	private String sku;
	private Integer quantity;
	private String amount;

	public ImportDutyTransactionLineItemImpl(
			String sourceIvsName,
			String sku,
			Integer quantity,
			String amount){
		this.sourceIvsName = sourceIvsName;
		this.sku = sku;
		this.quantity = quantity;
		this.amount = amount;
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj instanceof ImportDutyTransactionLineItem){
			ImportDutyTransactionLineItem lineItem = (ImportDutyTransactionLineItem)obj;
			return this.getSourceIvsName().equals(lineItem.getSourceIvsName())
				&& this.getSku().equals(lineItem.getSku())
				&& this.getQuantity().equals(lineItem.getQuantity())
				&& TestUtil.nullableEquals(this.getAmount(),lineItem.getAmount());
		}
		return false;
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
		return this.amount;
	}

}
