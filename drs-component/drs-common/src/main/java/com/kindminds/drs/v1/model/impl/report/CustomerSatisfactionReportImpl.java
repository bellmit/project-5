package com.kindminds.drs.v1.model.impl.report;

import com.kindminds.drs.api.v1.model.report.CustomerSatisfactionReport;
import com.kindminds.drs.api.v1.model.report.SatisfactionReportLineItem;
import com.kindminds.drs.util.DateHelper;

import java.util.Date;
import java.util.List;

public class CustomerSatisfactionReportImpl implements CustomerSatisfactionReport {
	
	private String utcEndDate;
	private Date last1PeriodStart;
	private Date last2PeriodStart;
	private Date last6PeriodStart;
	private List<SatisfactionReportLineItem> lineItems;
	
	public void setEnd(Date endPoint) {
		Date endPonintCopy = (Date)endPoint.clone();
		DateHelper.addDays(endPonintCopy, -1);
		this.utcEndDate = this.toUtcDateString(endPonintCopy); 
	}
	public void setLast1PeriodStart(Date last1PeriodStart) { this.last1PeriodStart = last1PeriodStart; }
	public void setLast2PeriodStart(Date last2PeriodStart) { this.last2PeriodStart = last2PeriodStart; }
	public void setLast6PeriodStart(Date last6PeriodStart) { this.last6PeriodStart = last6PeriodStart; }
	public void setLineItems(List<SatisfactionReportLineItem> lineItems) { this.lineItems = lineItems; }

	private String toUtcDateString(Date date){
		return DateHelper.toString(date, "yyyy-MM-dd", "UTC");
	}

	@Override
	public String getEnd() {
		return this.utcEndDate;
	}
	
	@Override
	public String getLast1PeriodStart() {
		return this.toUtcDateString(this.last1PeriodStart);
	}

	@Override
	public String getLast2PeriodStart() {
		return this.toUtcDateString(this.last2PeriodStart);
	}

	@Override
	public String getLast6PeriodStart() {
		return this.toUtcDateString(this.last6PeriodStart);
	}

	@Override
	public List<SatisfactionReportLineItem> getLineItems() {
		return this.lineItems;
	}

}
