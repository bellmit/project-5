package com.kindminds.drs.api.adapter;

import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;


import com.kindminds.drs.Country;
import com.kindminds.drs.api.v1.model.amazon.AmazonOrder;


public interface AmazonMwsOrderAdapter {
	public List<AmazonOrder> requestOrders(Country country,
										   XMLGregorianCalendar createAfter, XMLGregorianCalendar createBefore);
}
