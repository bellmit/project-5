package com.kindminds.drs.impl;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.v1.model.accounting.DrsTransaction;
import com.kindminds.drs.util.DateHelper;
import com.kindminds.drs.util.TestUtil;

import java.util.Date;
import java.util.List;

public class DrsTransactionImpl implements DrsTransaction {

	private String type;
	private Date transactionDate;
	private Integer transactionSequence;
	private String productSku;
	private Integer quantity;
	private Marketplace marketplace;
	private String sourceTransactionId;
	private String shipmentIvsName;
	private String shipmentUnsName;
	private DrsTransactionLineItemSource settleableItemElements;
	private List<DrsTransactionLineItem> settleableItemList;
	
	public DrsTransactionImpl(
			String type,
			String transactionDate,
			Integer transactionSequence,
			String productSku,
			Integer quantity,
			String marketplace,
			String sourceTransactionId,
			String shipmentIvsName,
			String shipmentUnsName,
			List<DrsTransactionLineItem> settleableItemList){
		this.type = type;
		this.transactionDate = DateHelper.toDate(transactionDate,"yyyy-MM-dd HH:mm:ss z");
		this.transactionSequence = transactionSequence;
		this.productSku = productSku;
		this.quantity = quantity;
		this.marketplace = Marketplace.fromName(marketplace);
		this.sourceTransactionId = sourceTransactionId;
		this.shipmentIvsName = shipmentIvsName;
		this.shipmentUnsName = shipmentUnsName;
		this.settleableItemList = settleableItemList;

	}
	
	public void setTransactionSequence(Integer seq){
		this.transactionSequence = seq;
	}
	
	public void setProductSku(String productSku){
		this.productSku = productSku;
	}
	
	public void setSettleableItemElements(DrsTransactionLineItemSource settleableItemElements) {
		this.settleableItemElements = settleableItemElements;
	}
	
	@Override
	public boolean equals(Object object){
		if(object instanceof DrsTransaction){
			DrsTransaction dt = (DrsTransaction)object;
			return this.getType().equals(dt.getType())
				&& this.getTransactionDate().equals(dt.getTransactionDate())
				&& this.getTransactionSequence().equals(dt.getTransactionSequence())
				&& this.getProductSku().equals(dt.getProductSku())
				&& this.getQuantity().equals(dt.getQuantity())
				&& this.getMarketplace().equals(dt.getMarketplace())
				&& TestUtil.nullableEquals(this.getSourceTransactionId(),dt.getSourceTransactionId())
				&& this.getShipmentIvsName().equals(dt.getShipmentIvsName())
				&& this.getShipmentUnsName().equals(dt.getShipmentUnsName())
				&& TestUtil.nullableEquals(this.getSettleableItemElements(),dt.getSettleableItemElements())
				&& TestUtil.nullableEquals(this.getSettleableItemList(),dt.getSettleableItemList());
		}
		return false;
	}

	@Override
	public String toString() {
		return "DrsTransactionImpl [getType()=" + getType() + ", getTransactionDate()=" + getTransactionDate()
				+ ", getTransactionSequence()=" + getTransactionSequence() + ", getProductSku()=" + getProductSku()
				+ ", getQuantity()=" + getQuantity() + ", getMarketplace()=" + getMarketplace()
				+ ", getSourceTransactionId()=" + getSourceTransactionId() + ", getShipmentIvsName()="
				+ getShipmentIvsName() + ", getShipmentUnsName()=" + getShipmentUnsName()
				+ ", getSettleableItemElements()=" + getSettleableItemElements() + ", getSettleableItemList()="
				+ getSettleableItemList() + "]";
	}

	@Override
	public String getType() {
		return this.type;
	}

	@Override
	public Date getTransactionDate() {
		return this.transactionDate;
	}

	@Override
	public Integer getTransactionSequence() {
		return this.transactionSequence;
	}

	@Override
	public String getProductSku() {
		return this.productSku;
	}

	@Override
	public Integer getQuantity() {
		return this.quantity;
	}

	@Override
	public Marketplace getMarketplace() {
		return this.marketplace;
	}

	@Override
	public String getSourceTransactionId() {
		return this.sourceTransactionId;
	}

	@Override
	public String getShipmentIvsName() {
		return this.shipmentIvsName;
	}

	@Override
	public String getShipmentUnsName() {
		return this.shipmentUnsName;
	}

	@Override
	public DrsTransactionLineItemSource getSettleableItemElements() {
		return this.settleableItemElements;
	}

	@Override
	public List<DrsTransactionLineItem> getSettleableItemList() {
		return this.settleableItemList;
	}

}
