package com.kindminds.drs.web.view.statement;

import java.io.Serializable;
import java.util.Date;

public class SettlementPeriod implements Serializable {
	
	private static final long serialVersionUID = 5882826023194018546L;
	private int id;
	private Date startPoint;
	private Date endPoint;	
	private String startDate;
	private String endDate;
	private String periodAsString;
	private Date dayBeforeEndPoint;
	
	public SettlementPeriod(){
		
	}
		
	public int getId() {
		return this.id;
	}
	
	public Date getStartPoint(){
		return this.startPoint;
	};
		
	public Date getEndPoint(){
		return this.endPoint;
	};
	
	public String getStartDate() {
		return this.startDate;
	}

	public String getEndDate() {
		return this.endDate;
	}

	public String getPeriodAsString() {
		return this.periodAsString;
	}

	public Date getDayBeforeEndPoint() {
		return this.dayBeforeEndPoint;
	}
	
}