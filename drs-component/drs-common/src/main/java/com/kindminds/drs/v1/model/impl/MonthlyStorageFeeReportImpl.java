package com.kindminds.drs.v1.model.impl;

import com.kindminds.drs.v1.model.impl.report.MonthlyStorageFeeReportLineItemImpl;
import com.kindminds.drs.api.v1.model.report.MonthlyStorageFeeReport;

import java.math.BigDecimal;
import java.util.List;

public class MonthlyStorageFeeReportImpl implements MonthlyStorageFeeReport{

	private BigDecimal sumOfTotalEstimatedMonthlyStorageFee = BigDecimal.ZERO;
	private List<MonthlyStorageFeeReportLineItem> lineItems;
	
	public MonthlyStorageFeeReportImpl(List<MonthlyStorageFeeReportLineItem> lineItems) {
		for(MonthlyStorageFeeReportLineItem item:lineItems){
			MonthlyStorageFeeReportLineItemImpl origItem = (MonthlyStorageFeeReportLineItemImpl)item;
			this.sumOfTotalEstimatedMonthlyStorageFee = this.sumOfTotalEstimatedMonthlyStorageFee.add(origItem.getNumericTotalEstimatedMonthlyStorageFee());
		}				
		this.lineItems = lineItems;
	}
		
	@Override
	public String getSumOfTotalEstimatedMonthlyStorageFee() {
		return this.sumOfTotalEstimatedMonthlyStorageFee.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
	}

	@Override
	public List<MonthlyStorageFeeReportLineItem> getLineItems() {
		return this.lineItems;
	}

}