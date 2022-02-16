package com.kindminds.drs.core.biz.settlement;

import com.kindminds.drs.Country;
import com.kindminds.drs.Marketplace;

import com.kindminds.drs.api.data.access.rdb.CurrencyDao;
import com.kindminds.drs.api.data.access.usecase.accounting.ProcessMarketSideTransactionDao;
import com.kindminds.drs.api.v2.biz.domain.model.accounting.MarketSideTransaction;
import com.kindminds.drs.api.v2.biz.domain.model.product.SkuShipmentAllocationInfo;
import com.kindminds.drs.service.util.SpringAppCtx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public abstract class AbstractMarketSideTransaction implements MarketSideTransaction {

	private Integer id;
	private Date transactionDate;
	private String type;
	private String source;
	private String sourceId;
	private String sku;
	private String description;
	private String exceptionMsg;
	private String stackTrace;

	protected List<SkuShipmentAllocationInfo> allocationInfos;
	
	protected final BigDecimal defaultDrsRetainmentRate = new BigDecimal("0.15");

	protected ProcessMarketSideTransactionDao dao =  (ProcessMarketSideTransactionDao)SpringAppCtx
			.get().getBean(ProcessMarketSideTransactionDao.class);
	protected CurrencyDao currencyRepo =  (CurrencyDao) SpringAppCtx
			.get().getBean(CurrencyDao.class);

	public AbstractMarketSideTransaction() { }

	public AbstractMarketSideTransaction(Integer id, Date transactionDate, String type,
										 String source, String sourceId, String sku) {
		this.id = id;
		this.transactionDate = transactionDate;
		this.type = type;
		this.source = source;
		this.sourceId = sourceId;
		this.sku = sku;

	}

	public AbstractMarketSideTransaction(Integer id, Date transactionDate, String type,
										 String source, String sourceId, String sku, String description) {
		this.id = id;
		this.transactionDate = transactionDate;
		this.type = type;
		this.source = source;
		this.sourceId = sourceId;
		this.sku = sku;
		this.description = description;
	}

	public AbstractMarketSideTransaction(Integer id, Date transactionDate, String type,
										 String source, String sourceId, String sku,
										 String description, String exceptionMsg, String stackTrace) {
		this.id = id;
		this.transactionDate = transactionDate;
		this.type = type;
		this.source = source;
		this.sourceId = sourceId;
		this.sku = sku;
		this.description = description;
		this.exceptionMsg = exceptionMsg;
		this.stackTrace = stackTrace;
	}

	@Override
	public Integer getTotalQuantityPurchased(){ return null; }

	@Override
	public BigDecimal getExportExchangeRate(Marketplace marketplace, SkuShipmentAllocationInfo allocationInfo){
		BigDecimal fxRate = null;
//		System.out.println("unsName: " + allocationInfo.getUnsName());
//		System.out.println("ivsName: " + allocationInfo.getIvsName());
		if(marketplace.getUnsDestinationCountry()==Country.UK && marketplace.getCountry()!=Country.UK){
			fxRate = allocationInfo.getFxRateFromFcaCurrencyToEur();
		}
		else {
			fxRate = allocationInfo.getFxRateFromFcaCurrencyToDestinationCountryCurrency();
		}
		Assert.notNull(fxRate,"fxRate is null");
		Assert.isTrue(fxRate.compareTo(BigDecimal.ONE)==-1,"fxRate " + fxRate +" is not less than 1, UNS: " + allocationInfo.getUnsName());
		return fxRate;
	}

	@Override
	public boolean needAllocationInfos(){ return false; }

	@Override
	public Integer getAssignedSourceOrderSeq() {
		return null;
	}

	@Override
	public List<SkuShipmentAllocationInfo> getAllocationInfos() {
		return allocationInfos;
	}

	public Integer getId() {
		return id;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public String getType() {
		return type;
	}

	public String getSource() {
		return source;
	}

	public String getSourceId() {
		return sourceId;
	}

	public String getSku() {
		return sku;
	}

	public String getDescription() {
		return description;
	}

	public String getExceptionMsg() {
		return exceptionMsg;
	}

	public String getStackTrace() {
		return stackTrace;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setAllocationInfos(List<SkuShipmentAllocationInfo> allocationInfos) {
		this.allocationInfos = allocationInfos;
	}

	public void setExceptionMsg(String exceptionMsg) {
		this.exceptionMsg = exceptionMsg;
	}

	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}


}
