package com.kindminds.drs.v1.model.impl.accounting;

import com.kindminds.drs.util.DateHelper;
import com.kindminds.drs.api.v1.model.accounting.SettlementPeriod;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class SettlementPeriodImpl implements SettlementPeriod, Serializable {

	private int id;
	private Date startPoint;
	private Date endPoint;
	private String format = "yyyy-MM-dd";
	private String startDate;
	private String endDate;
	private Date dayBeforeEndPoint;
	private String periodAsString;

	public SettlementPeriodImpl() {}
	
	public SettlementPeriodImpl(
			Date start,
			Date end){
		this.id = 0;
		this.startPoint = start;
		this.endPoint = end;
		this.startDate = DateHelper.toString(this.startPoint, this.format, "UTC");
		this.dayBeforeEndPoint = getDaysBefore(1, this.endPoint);
		this.endDate = DateHelper.toString(dayBeforeEndPoint, this.format, "UTC");
		this.periodAsString = DateHelper.toString(startPoint, "yyyyMMdd", "UTC") + "-" +
				DateHelper.toString(dayBeforeEndPoint, "yyyyMMdd", "UTC");
	}
	
	public SettlementPeriodImpl(
			int id,
			Date start,
			Date end){
		this.id = id;
		this.startPoint = start;
		this.endPoint = end;
		this.startDate = DateHelper.toString(this.startPoint, this.format, "UTC");
		this.dayBeforeEndPoint = getDaysBefore(1, this.endPoint);
		this.endDate = DateHelper.toString(dayBeforeEndPoint, this.format, "UTC");
		this.periodAsString = DateHelper.toString(startPoint, "yyyyMMdd", "UTC") + "-" +
				DateHelper.toString(dayBeforeEndPoint, "yyyyMMdd", "UTC");
	}

	@Override
	public String toString() {
		return "SettlementPeriodImpl [getId()=" + getId() + ", getStartPoint()=" + getStartPoint() + ", getEndPoint()="
				+ getEndPoint() + ", getStartDate()=" + getStartDate() + ", getEndDate()=" + getEndDate() + "]";
	}

	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public Date getStartPoint() {
		return this.startPoint;
	}

	@Override
	public Date getEndPoint() {
		return this.endPoint;
	}

	@Override
	public String getStartDate() {
		return this.startDate;
	}

	@Override
	public String getEndDate() {
		return this.endDate;
	}

	@Override
	public Date getDayBeforeEndPoint() {
		return this.dayBeforeEndPoint;
	}

	@Override
	public String getPeriodAsString() {
		return this.periodAsString;
	}

	@Override
	public void setFormat(String format) {
		this.format = format;
	}

	private Date getDaysBefore(int days, Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE,-days);
		return c.getTime();
	}

}