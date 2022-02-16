package com.kindminds.drs.api.v2.biz.domain.model.accounting;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.v2.biz.domain.model.product.SkuShipmentAllocationInfo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface MarketSideTransaction {
	Integer getId();
	Date getTransactionDate();
	String getType();
	String getSource();
	String getSourceId();
	String getSku();
	String getDescription();
	Integer getAssignedSourceOrderSeq();
	List<SkuShipmentAllocationInfo> getAllocationInfos();

	List<Integer> process(Date periodStart, Date periodEnd);
	boolean needAllocationInfos();
	Integer getTotalQuantityPurchased();
	BigDecimal getExportExchangeRate(Marketplace marketplace, SkuShipmentAllocationInfo allocationInfo);

}
