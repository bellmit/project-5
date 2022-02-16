package com.kindminds.drs.web.data.dto;

import com.kindminds.drs.api.v1.model.accounting.ImportDutyTransaction.ImportDutyTransactionLineItem;

public class ImportDutyTransactionLineItemImpl implements ImportDutyTransactionLineItem{

	private String sourceIvsName;
	private String sku;
	private Integer quantity;
	private String amount;
		
	public ImportDutyTransactionLineItemImpl(){}
	
	public ImportDutyTransactionLineItemImpl(String sourceIvsName,String sku,Integer quantity,String amount){
		this.sourceIvsName  = sourceIvsName;
		this.sku = sku;
		this.quantity = quantity;
		this.amount = amount;
	}
		
	@Override
	public String getSourceIvsName() {
		return this.sourceIvsName;
	}

	public void setSourceIvsName(String sourceIvsName) {
		this.sourceIvsName = sourceIvsName;		
	}
	
	@Override
	public String getSku() {
		return this.sku;
	}

	public void setSku(String sku) {
		this.sku = sku;		
	}
	
	@Override
	public Integer getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity; 		
	}
	
	@Override
	public String getAmount() {
		return this.amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;		
	}
	
}