package com.kindminds.drs.v1.model.impl;

import com.kindminds.drs.api.v1.model.amazon.AmazonReturnReportRawLine;

import java.util.Date;

public class AmazonReturnReportRawLineImpl implements AmazonReturnReportRawLine {
	
	private Date returnDate;
	private String orderId;
	private String sku;
	private String asin;
	private String fnsku;
	private String productName;
	private Integer quantity;
	private String fulfillmentCenterId;
	private String detailedDisposition;
	private String reason;
	private String status;
	private String licensePlateNumber;
	private String customerComments;

	public AmazonReturnReportRawLineImpl(
			Date returnDate,
			String orderId,
			String sku,
			String asin,
			String fnsku,
			String productName,
			Integer quantity,
			String fulfillmentCenterId,
			String detailedDisposition,
			String reason,
			String status,
			String licensePlateNumber,
			String customerComments){
		this.returnDate = returnDate;
		this.orderId = orderId;
		this.sku=sku;
		this.asin=asin;
		this.fnsku=fnsku;
		this.productName=productName;
		this.quantity=quantity;
		this.fulfillmentCenterId=fulfillmentCenterId;
		this.detailedDisposition=detailedDisposition;
		this.reason=reason;
		this.status=status;
		this.licensePlateNumber=licensePlateNumber;
		this.customerComments=customerComments;
	}
	
	@Override
	public String toString() {
		return "AmazonReturnReportRawLineImpl [getReturnDate()=" + getReturnDate() + ", getOrderId()=" + getOrderId()
				+ ", getSku()=" + getSku() + ", getAsin()=" + getAsin() + ", getFnsku()=" + getFnsku()
				+ ", getProductName()=" + getProductName() + ", getQuantity()=" + getQuantity()
				+ ", getFulfillmentCenterId()=" + getFulfillmentCenterId() + ", getDetailedDisposition()="
				+ getDetailedDisposition() + ", getReason()=" + getReason() + ", getStatus()=" + getStatus()
				+ ", getLicensePlateNumber()=" + getLicensePlateNumber() + ", getCustomerComments()="
				+ getCustomerComments() + "]";
	}

	@Override
	public Date getReturnDate() {
		return this.returnDate;
	}

	@Override
	public String getOrderId() {
		return this.orderId;
	}

	@Override
	public String getSku() {
		return this.sku;
	}

	@Override
	public String getAsin() {
		return this.asin;
	}

	@Override
	public String getFnsku() {
		return this.fnsku;
	}

	@Override
	public String getProductName() {
		return this.productName;
	}

	@Override
	public Integer getQuantity() {
		return this.quantity;
	}

	@Override
	public String getFulfillmentCenterId() {
		return this.fulfillmentCenterId;
	}

	@Override
	public String getDetailedDisposition() {
		return this.detailedDisposition;
	}

	@Override
	public String getReason() {
		return this.reason;
	}

	@Override
	public String getStatus() {
		return this.status;
	}

	@Override
	public String getLicensePlateNumber() {
		return this.licensePlateNumber;
	}

	@Override
	public String getCustomerComments() {
		return this.customerComments;
	}

}
