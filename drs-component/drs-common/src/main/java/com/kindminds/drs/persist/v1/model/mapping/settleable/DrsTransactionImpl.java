package com.kindminds.drs.persist.v1.model.mapping.settleable;

import java.util.Date;
import java.util.List;






import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.v1.model.accounting.DrsTransaction;


public class DrsTransactionImpl implements DrsTransaction {
	
	//@Id ////@Column(name="id")
	private int id;
	//@Column(name="type")
	private String type;
	//@Column(name="transaction_date")
	private Date transactionDate;
	//@Column(name="transaction_seq")
	private int transactionSeq;
	//@Column(name="product_sku")
	private String productSku;
	//@Column(name="quantity")
	private int quantity;
	//@Column(name="marketplace_id")
	private int marketplaceId;
	//@Column(name="source_transaction_id")
	private String sourceTransactionId;
	//@Column(name="shipment_ivs_name")
	private String shipmentIvsName;
	//@Column(name="shipment_uns_name")
	private String shipmentUnsName;
	
 private DrsTransactionLineItemSource settleableItemElements=null;
 private List<DrsTransactionLineItem> settleableItemList=null;

	public DrsTransactionImpl() {
	}

	public DrsTransactionImpl(int id, String type, Date transactionDate, int transactionSeq, String productSku, int quantity,
							  int marketplaceId, String sourceTransactionId, String shipmentIvsName, String shipmentUnsName
							 ) {
		this.id = id;
		this.type = type;
		this.transactionDate = transactionDate;
		this.transactionSeq = transactionSeq;
		this.productSku = productSku;
		this.quantity = quantity;
		this.marketplaceId = marketplaceId;
		this.sourceTransactionId = sourceTransactionId;
		this.shipmentIvsName = shipmentIvsName;
		this.shipmentUnsName = shipmentUnsName;

	}

	public void setElements(DrsTransactionLineItemSource elements) {
		this.settleableItemElements = elements;
	}

	public void setSettleableItemList(List<DrsTransactionLineItem> settleableItemList) {
		this.settleableItemList = settleableItemList;
	}
	
	public int getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return "DrsTransactionImpl [getId()=" + getId() + ", getType()=" + getType() + ", getTransactionDate()="
				+ getTransactionDate() + ", getTransactionSequence()=" + getTransactionSequence() + ", getProductSku()="
				+ getProductSku() + ", getQuantity()=" + getQuantity() + ", getMarketplace()=" + getMarketplace()
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
		return this.transactionSeq;
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
		return Marketplace.fromKey(this.marketplaceId);
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
