package com.kindminds.drs.v1.model.impl;

import com.kindminds.drs.api.usecase.logistics.ViewUnsRecognizeRevenueReportUco.UnsGroup;
import com.kindminds.drs.api.usecase.logistics.ViewUnsRecognizeRevenueReportUco.UnsGroupDetail;

import java.math.BigDecimal;
import java.util.List;

public class UnsGroupImpl implements UnsGroup {
	
	private String unsName;
	private String invoiceNumber;
	private List<UnsGroupDetail> unsGroupDetails;
	
	public UnsGroupImpl(String unsName,List<UnsGroupDetail> unsGroupDetails) {
		this.unsName = unsName;
		this.unsGroupDetails = unsGroupDetails;
	}
	
	@Override
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	@Override
	public String getUnsName() {
		return unsName;
	}
	
	@Override
	public String getInvoiceNumber() {
		return this.invoiceNumber;
	}

	@Override
	public String getTotal() {
		BigDecimal total = BigDecimal.ZERO;
		for(UnsGroupDetail unsGroupDetail:this.unsGroupDetails){
			total = total.add(unsGroupDetail.getSubtotal());
		}
		return total.toPlainString();
	}
	
	@Override
	public List<UnsGroupDetail> getUnsGroupDetails(){
		return this.unsGroupDetails;
	}

}
