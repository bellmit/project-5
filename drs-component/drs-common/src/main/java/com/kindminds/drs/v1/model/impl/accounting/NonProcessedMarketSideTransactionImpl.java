package com.kindminds.drs.v1.model.impl.accounting;

import com.kindminds.drs.api.v1.model.accounting.NonProcessedMarketSideTransaction;

import java.util.Date;

public class NonProcessedMarketSideTransactionImpl implements NonProcessedMarketSideTransaction {

	private Integer id;
	private Date transactionDate;
	private String type;
	private String source;
	private String sourceId;
	private String sku;
	private String description;
	//private List<SkuShipmentAllocationInfo> allocationInfos=null;
	private String exceptionMsg;
	private String stackTrace;


	public NonProcessedMarketSideTransactionImpl(
			Integer id,
			Date transactionDate,
			String type,
			String source,
			String sourceId,
			String sku ,
			String exceptionMsg,
			String stackTrace) {
		super();
		this.id = id;
		this.transactionDate = transactionDate;
		this.type = type;
		this.source = source;
		this.sourceId = sourceId;
		this.sku = sku;
		this.exceptionMsg = exceptionMsg;
		this.stackTrace = stackTrace;
	}

	public NonProcessedMarketSideTransactionImpl(int id, Date transactionDate,
												 String type, String source,
												 String sourceId, String sku,
												 String description , String exceptionMsg,
												 String stackTrace) {
		this.id=id;
		this.transactionDate=transactionDate;
		this.type=type;
		this.source=source;
		this.sourceId=sourceId;
		this.sku=sku;
		this.description=description;
		this.exceptionMsg = exceptionMsg;
		this.stackTrace = stackTrace;
	}


//	@Override
//	public List<Integer> process(Date periodStart, Date periodEnd) {
//		return null;
//	}
//
//	@Override
//	public boolean needAllocationInfos() {
//		return false;
//	}
//
//	@Override
//	public Integer getTotalQuantityPurchased() {
//		return null;
//	}
//
//	@Override
//	public BigDecimal getExportExchangeRate(Marketplace marketplace, SkuShipmentAllocationInfo allocationInfo) {
//		return null;
//	}


//	public void setAllocationInfos(List<SkuShipmentAllocationInfo> allocationInfos) {
//		this.allocationInfos = allocationInfos;
//	}


	@Override
	public String toString() {
		return "MarketSideTransactionImpl [getId()=" + getId() + ", getTransactionDate()=" + getTransactionDate()
				+ ", getType()=" + getType() + ", getSource()=" + getSource() + ", getSourceId()=" + getSourceId()
				+ ", getSku()=" + getSku() + ", getDescription()=" + getDescription() + "]";
	}

	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public Date getTransactionDate() {
		return this.transactionDate;
	}

	@Override
	public String getType() {
		return this.type;
	}

	@Override
	public String getSource() {
		return this.source;
	}

	@Override
	public String getSourceId() {
		return this.sourceId;
	}
	
	@Override
	public String getSku() {
		return this.sku;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

//	@Override
//	public List<SkuShipmentAllocationInfo> getAllocationInfos() {
//		return this.allocationInfos;
//	}

	@Override
	public Integer getAssignedSourceOrderSeq() {
		return null;
	}

	@Override
	public String getExceptionMessage() {
		return this.exceptionMsg;
	}

	@Override
	public String getStackTracke() {
		return this.stackTrace;
	}


}
