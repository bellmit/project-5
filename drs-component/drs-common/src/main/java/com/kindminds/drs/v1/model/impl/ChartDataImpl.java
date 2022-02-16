package com.kindminds.drs.v1.model.impl;

import com.kindminds.drs.api.usecase.ViewSalesAndPageTrafficReportUco.ChartData;
import com.kindminds.drs.util.DateHelper;

import java.math.BigDecimal;
import java.util.Date;

public class ChartDataImpl implements ChartData {
	
	private Date date;
	private BigDecimal total=BigDecimal.ZERO;
	
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	
	@Override
	public Date getDateObj(){
		return this.date;
	}
	
	@Override
	public BigDecimal getTotalBigDec(){
		return this.total;
	}

	public ChartDataImpl(Date date) {
		super();
		this.date = date;
	}
	
	public ChartDataImpl(Date date, BigDecimal total) {
		this.date = date;
		this.total = total;
	}

	@Override
	public String toString() {
		return "ChartDataImpl [getDate()=" + getDate() + ", getTotal()=" + getTotal() + "]";
	}

	@Override
	public String getDate() {
		return DateHelper.toString(this.date, "yyyy-MM-dd", "UTC");
	}

	@Override
	public String getTotal() {
		return this.total.stripTrailingZeros().toPlainString();
	}

}
