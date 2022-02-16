package com.kindminds.drs.api.data.access.usecase.report.amazon;

import java.util.Date;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;


import com.kindminds.drs.Country;
import com.kindminds.drs.api.v1.model.amazon.AmazonOrder;


public interface ImportAmazonOrderDao {
	public void setImportingStatus(Country country, String status);
	public void insertOrUpdateOrders(List<AmazonOrder> orders);
	public List<AmazonOrder> selectOrders(Date createdAfter, Date createdBefore);
	public void updateLastUpdateDate(Country country, XMLGregorianCalendar date);
	public XMLGregorianCalendar queryInitialDateUtc(Country country);
	public XMLGregorianCalendar queryLastUpdateDateUtc(Country country);
}
