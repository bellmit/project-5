package com.kindminds.drs.api.adapter;

import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.v1.model.amazon.AmazonSkuInventorySupply;

public interface AmazonMwsFulfillmentInventoryAdapter {
	List<AmazonSkuInventorySupply> requestListInventorySupplies(Marketplace marketplace,XMLGregorianCalendar queryStartDateTime);
}
