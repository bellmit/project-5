package com.kindminds.drs.api.v1.model.report;

import java.util.List;

public interface CustomerSatisfactionReport {
	public String getEnd();
	public String getLast1PeriodStart();
	public String getLast2PeriodStart();
	public String getLast6PeriodStart();
	public List<SatisfactionReportLineItem> getLineItems();
}
