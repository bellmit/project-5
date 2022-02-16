package com.kindminds.drs.core.biz.accounting.strategy;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.kindminds.drs.api.data.access.rdb.CurrencyDao;
import com.kindminds.drs.api.data.access.usecase.accounting.ProcessMarketSideTransactionDao;
import com.kindminds.drs.api.v2.biz.domain.model.accounting.MarketSideTransaction;
import com.kindminds.drs.api.v2.biz.domain.model.product.SkuShipmentAllocationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.kindminds.drs.Country;
import com.kindminds.drs.Marketplace;


public abstract class MarketSideTransactionProcessor {
	
	@Autowired protected ProcessMarketSideTransactionDao dao;
	@Autowired protected CurrencyDao currencyRepo;
	
	protected final BigDecimal defaultDrsRetainmentRate = new BigDecimal("0.15");
	
	public abstract List<Integer> process(Date periodStart, Date periodEnd, MarketSideTransaction transaction);
	public boolean needAllocationInfos(){ return false; }
	public Integer getTotalQuantityPurchased(MarketSideTransaction transaction){ return null; }
	final protected BigDecimal getExportExchangeRate(Marketplace marketplace, SkuShipmentAllocationInfo allocationInfo){
		BigDecimal fxRate = null;
		System.out.println("unsName: " + allocationInfo.getUnsName());
		System.out.println("ivsName: " + allocationInfo.getIvsName());
		if(marketplace.getUnsDestinationCountry()==Country.UK && marketplace.getCountry()!=Country.UK){
			fxRate = allocationInfo.getFxRateFromFcaCurrencyToEur();
		}
		else {
			fxRate = allocationInfo.getFxRateFromFcaCurrencyToDestinationCountryCurrency();
		}
		Assert.notNull(fxRate,"fxRate is null");
		Assert.isTrue(fxRate.compareTo(BigDecimal.ONE)==-1,"fxRate "+ fxRate +" is not less than 1, UNS: " + allocationInfo.getUnsName());
		return fxRate;
	}
}
