package com.kindminds.drs.v1.model.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;


import com.kindminds.drs.api.v1.model.report.HistoryLine;
import com.kindminds.drs.util.BigDecimalHelper;
import com.kindminds.drs.util.DateHelper;

public class HistoryLineImpl implements HistoryLine {
	
	private Date date;
	private BigDecimal sessions = BigDecimal.ZERO;
	private BigDecimal pageViews = BigDecimal.ZERO;
	private BigDecimal sessionsMultiplyBuyboxPercentage = BigDecimal.ZERO;
	private BigDecimal unitsOrdered = BigDecimal.ZERO;
	private BigDecimal orderedProductSales = BigDecimal.ZERO;
	private BigDecimal totalOrderItems = BigDecimal.ZERO;

	public HistoryLineImpl() {
	}
	
	public HistoryLineImpl(Date date, BigDecimal sessions, BigDecimal pageViews,
			BigDecimal sessionsMultiplyBuyboxPercentage, BigDecimal unitsOrdered,
			BigDecimal orderedProductSales, BigDecimal totalOrderItems) {
		super();
		this.date = date;
		this.sessions = sessions;
		this.pageViews = pageViews;
		this.sessionsMultiplyBuyboxPercentage = sessionsMultiplyBuyboxPercentage;
		this.unitsOrdered = unitsOrdered;
		this.orderedProductSales = orderedProductSales;
		this.totalOrderItems = totalOrderItems;
	}
	
	@Override
	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public void setSessions(BigDecimal sessions) {
		this.sessions = sessions;
	}

	@Override
	public void setPageViews(BigDecimal pageViews) {
		this.pageViews = pageViews;
	}

	@Override
	public void setSessionsMultiplyBuyboxPercentage(BigDecimal sessionsMultiplyBuyboxPercentage) {
		this.sessionsMultiplyBuyboxPercentage = sessionsMultiplyBuyboxPercentage;
	}

	@Override
	public void setUnitsOrdered(BigDecimal unitsOrdered) {
		this.unitsOrdered = unitsOrdered;
	}

	@Override
	public void setOrderedProductSales(BigDecimal orderedProductSales) {
		this.orderedProductSales = orderedProductSales;
	}

	@Override
	public void setTotalOrderItems(BigDecimal totalOrderItems) {
		this.totalOrderItems = totalOrderItems;
	}
	
	
	
	@Override
	public Date getDateObj() {
		return this.date;
	}

	@Override
	public BigDecimal getSessionsDec() {
		return this.sessions;
	}

	@Override
	public BigDecimal getPageViewsDec() {
		return this.pageViews;
	}

	@Override
	public BigDecimal getSessionsXBuyBoxPercentDec() {
		return this.sessionsMultiplyBuyboxPercentage;
	}

	@Override
	public BigDecimal getUnitsOrderedDec() {
		return this.unitsOrdered;
	}

	@Override
	public BigDecimal getOrderedProductSalesDec() {
		return this.orderedProductSales;
	}

	@Override
	public BigDecimal getTotalOrderItemsDec() {
		return this.totalOrderItems;
	}
	
	
	
	@Override
	public String getDate() {
		return DateHelper.toString(this.date,"yyyy-MM-dd","UTC");
	}

	@Override
	public String getSessions() {
		return this.sessions==null?"-":this.sessions.toPlainString();
	}

	@Override
	public String getPageViews() {
		return this.pageViews==null?"-":this.pageViews.toPlainString();
	}

	@Override
	public String getBuyBoxPercentage() {
		if(sessions == null || sessions.compareTo(BigDecimal.ZERO) == 0) return "-";
		BigDecimal averagedBuyboxPercentage = this.sessionsMultiplyBuyboxPercentage.divide(sessions,6,RoundingMode.HALF_UP);
		if (averagedBuyboxPercentage.compareTo(new BigDecimal(1)) > 0) {
			averagedBuyboxPercentage = new BigDecimal(1);
		}
		return BigDecimalHelper.toPercentageString(averagedBuyboxPercentage, 0);
	}

	@Override
	public String getUnitSessionPercentage() {
		if(sessions == null || sessions.compareTo(BigDecimal.ZERO) == 0) return "-";
		BigDecimal unitSessionPercentage = unitsOrdered.divide(sessions,6,RoundingMode.HALF_UP);
		return BigDecimalHelper.toPercentageString(unitSessionPercentage,2);
	}

	@Override
	public String getOrderedProductSales() {
		return this.orderedProductSales==null?"-":this.orderedProductSales.setScale(2).toPlainString();
	}

	@Override
	public String getOrderItems() {
		return this.totalOrderItems==null?"-":this.totalOrderItems.toPlainString();
	}

	@Override
	public String getUnitsOrdered() {
		return this.unitsOrdered==null?"-":this.unitsOrdered.toPlainString();
	}

	@Override
	public String toString() {
		return "HistoryLineImpl [getDate()=" + getDate() + ", getSessions()=" + getSessions() + ", getPageViews()="
				+ getPageViews() + ", getBuyBoxPercentage()=" + getBuyBoxPercentage() + ", getUnitSessionPercentage()="
				+ getUnitSessionPercentage() + ", getOrderedProductSales()=" + getOrderedProductSales()
				+ ", getOrderItems()=" + getOrderItems() + "]";
	}

}
