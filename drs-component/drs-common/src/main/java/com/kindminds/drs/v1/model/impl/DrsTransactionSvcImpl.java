package com.kindminds.drs.v1.model.impl;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.v1.model.accounting.DrsTransaction;

import java.util.Date;
import java.util.List;

public class DrsTransactionSvcImpl implements DrsTransaction {

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
	private List<DrsTransactionLineItem> settleableItemList=null;
	
	public DrsTransactionSvcImpl(
			String type,
			Date transactionDate,
			Integer transactionSequence,
			String productSku,
			Integer quantity,
			Marketplace marketplace,
			String sourceTransactionId,
			String shipmentIvsName,
			String shipmentUnsName){
		this.type = type;
		this.transactionDate = transactionDate;
		this.transactionSequence = transactionSequence;
		this.productSku = productSku;
		this.quantity = quantity;
		this.marketplace = marketplace;
		this.sourceTransactionId = sourceTransactionId;
		this.shipmentIvsName = shipmentIvsName;
		this.shipmentUnsName = shipmentUnsName;
	}
	
	public void setSettleableItemElements(DrsTransactionLineItemSource elements) { this.settleableItemElements = elements; }
	public void setSettleableItemList(List<DrsTransactionLineItem> list) { this.settleableItemList=list; }

	@Override
	public String toString() {
		return "DrsTransactionSvcImpl [getType()=" + getType() + ", getTransactionDate()=" + getTransactionDate()
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
