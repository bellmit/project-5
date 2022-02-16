package com.kindminds.drs.api.v1.model.accounting;

import java.util.Date;

public interface SettlementPeriod {
	int getId();
	Date getStartPoint();
	Date getEndPoint();
	String getStartDate();
	String getEndDate();
	Date getDayBeforeEndPoint();
	String getPeriodAsString();
	void setFormat(String format);
}
