package com.kindminds.drs.api.v1.model.amazon;

import java.util.Date;

public interface AmazonReturnReportRawLine {
	Date getReturnDate();
	String getOrderId();
	String getSku();
	String getAsin();
	String getFnsku();
	String getProductName();
	Integer getQuantity();
	String getFulfillmentCenterId();
	String getDetailedDisposition();
	String getReason();
	String getStatus();
	String getLicensePlateNumber();
	String getCustomerComments();
}
