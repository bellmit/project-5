package com.kindminds.drs.api.adapter;

import java.util.Date;
import java.util.List;

import com.kindminds.drs.api.v1.model.amazon.AmazonOrder;

public interface AmazonMwsFulfillmentOutboundShipmentAdapter {
	List<AmazonOrder> requestOrders(String region,Date start);
}
