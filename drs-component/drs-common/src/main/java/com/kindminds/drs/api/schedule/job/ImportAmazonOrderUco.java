package com.kindminds.drs.api.schedule.job;

import com.kindminds.drs.api.v1.model.amazon.AmazonOrder;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;
import java.util.List;

public interface ImportAmazonOrderUco {

	public void importOrders();
	public void importOrderForTest(XMLGregorianCalendar createAfter, XMLGregorianCalendar createBefore);
	public List<AmazonOrder> getOrdersFromDatabase(Date createAfter, Date createdBefore);
}
