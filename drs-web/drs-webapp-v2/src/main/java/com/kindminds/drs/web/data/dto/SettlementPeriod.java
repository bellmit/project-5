package com.kindminds.drs.web.data.dto;

import com.kindminds.drs.util.DateHelper;

import java.util.Calendar;
import java.util.Date;

public class SettlementPeriod implements com.kindminds.drs.api.v1.model.accounting.SettlementPeriod{
	private String endDate;
	private Date endPoint;
	private int id;
	private String startDate;
	private Date startPoint;
	private String periodAsString;
	private Date dayBeforeEndPoint;

	@Override
	public String getEndDate() {
		return this.endDate;
	}

	@Override
	public Date getEndPoint() {
		return this.endPoint;
	}

	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public String getStartDate() {
		return this.startDate;
	}

	@Override
	public Date getStartPoint() {
		return this.startPoint;
	}

	@Override
	public String getPeriodAsString() {
		return this.periodAsString;
	}

	@Override
	public Date getDayBeforeEndPoint() {
		return this.dayBeforeEndPoint;
	}

	@Override
	public void setFormat(String arg0) {
		// TODO Auto-generated method stub
		
	}

}
