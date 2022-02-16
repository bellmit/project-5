package com.kindminds.drs.v1.model.impl;

import com.kindminds.drs.Currency;
import com.kindminds.drs.Marketplace;
import com.kindminds.drs.v1.model.impl.report.SkuAdvertisingEvaluationReportLineItemImpl;
import com.kindminds.drs.api.v1.model.report.SkuAdvertisingEvaluationReport;
import com.kindminds.drs.util.BigDecimalHelper;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class SkuAdvertisingEvaluationReportImpl implements SkuAdvertisingEvaluationReport {
	
	private Marketplace marketplace=null;
	private Currency currency=null;
	private Integer grandTotalSessions=0;
	private Integer grandTotalPageViews=0;
	private BigDecimal grandAverageBuyBox=BigDecimal.ZERO;
	private Integer grandTotalUnitOrdered=0;
	private BigDecimal grandTotalOrderedProductSales=BigDecimal.ZERO;
	
	private Integer grandTotalClicks=0;
	private BigDecimal grandTotalSpend=BigDecimal.ZERO;
	private BigDecimal grandTotalOneWeekOrderedProductSales=BigDecimal.ZERO;
	
	private List<SkuAdvertisingEvaluationReportLineItem> lineItems;
	
	public void setCurrency(Currency currency){this.currency=currency;}
	public void setMarketplace(Marketplace marketplace){this.marketplace=marketplace;}
	
	public SkuAdvertisingEvaluationReportImpl(List<SkuAdvertisingEvaluationReportLineItem> lineItems) {
		BigDecimal buyBoxTimesSessionsSum = BigDecimal.ZERO;
		for(SkuAdvertisingEvaluationReportLineItem item:lineItems){
			SkuAdvertisingEvaluationReportLineItemImpl origItem = (SkuAdvertisingEvaluationReportLineItemImpl)item;
			this.grandTotalSessions += origItem.getNumericTotalSessions();
			this.grandTotalPageViews += origItem.getNumericTotalPageViews();
			buyBoxTimesSessionsSum = buyBoxTimesSessionsSum.add(
					origItem.getNumericBuyBoxRate().multiply(new BigDecimal(origItem.getNumericTotalSessions())));
			this.grandTotalUnitOrdered += origItem.getNumericTotalUnitOrdered();
			this.grandTotalOrderedProductSales = this.grandTotalOrderedProductSales.add(origItem.getNumericTotalOrderedProductSales());
			this.grandTotalClicks += origItem.getDataFromCampaignPerformanceReport()==null?0:origItem.getDataFromCampaignPerformanceReport().getTotalClicks();
			this.grandTotalSpend = this.grandTotalSpend.add(origItem.getDataFromCampaignPerformanceReport()==null?BigDecimal.ZERO:origItem.getDataFromCampaignPerformanceReport().getTotalSpend());
			this.grandTotalOneWeekOrderedProductSales = this.grandTotalOneWeekOrderedProductSales.add(origItem.getDataFromCampaignPerformanceReport()==null?BigDecimal.ZERO:origItem.getDataFromCampaignPerformanceReport().getTotalOneWeekOrderedProductSales());
		}
		this.grandAverageBuyBox = this.calculateGrandAverageBuyBox(buyBoxTimesSessionsSum, this.grandTotalSessions);
		this.lineItems = lineItems;
	}
	
	private BigDecimal calculateGrandAverageBuyBox(BigDecimal buyBoxTimesSessionsSum,int grandTotalSessions){
		Assert.isTrue(grandTotalSessions>=0);
		if(grandTotalSessions==0) return BigDecimal.ZERO;
		return buyBoxTimesSessionsSum.divide(new BigDecimal(grandTotalSessions),6,RoundingMode.HALF_UP);
	}

	@Override
	public Marketplace getMarketplace() {
		return this.marketplace;
	}

	@Override
	public String getCurrency() {
		return this.currency.name();
	}
	
	@Override
	public String getGrandTotalSessions() {
		return this.grandTotalSessions==null?"N/A":this.grandTotalSessions.toString();
	}

	@Override
	public String getGrandTotalPageViews() {
		return this.grandTotalPageViews==null?"N/A":this.grandTotalPageViews.toString();
	}

	@Override
	public String getGrandBuyBoxPercentage() {
		return BigDecimalHelper.toPercentageString(this.grandAverageBuyBox, 2);
	}
	
	@Override
	public String getGrandTotalUnitOrdered() {
		return this.grandTotalUnitOrdered.toString();
	}
	
	public String getGrandTotalAdClicks() {
		return this.grandTotalClicks.toString();
	}
	
	public String getGrandTotalAdSpend() {
		return this.grandTotalSpend.setScale(this.currency.getScale(),RoundingMode.HALF_UP).toPlainString();
	}
	
	@Override
	public String getGrandTotalOrderedProductSales() {
		return this.grandTotalOrderedProductSales.setScale(this.currency.getScale(),RoundingMode.HALF_UP).toPlainString();
	}
	
	@Override
	public String getGrandConversionRate() {
		if(this.grandTotalSessions.compareTo(0)==0) return "N/A";
		BigDecimal grandTotalUnitOrdered = new BigDecimal(this.grandTotalUnitOrdered);
		BigDecimal grandTotalSessions = new BigDecimal(this.grandTotalSessions);
		BigDecimal grandConversionRate = grandTotalUnitOrdered.divide(grandTotalSessions,6,RoundingMode.HALF_UP);
		return BigDecimalHelper.toPercentageString(grandConversionRate,2);
	}
	
	@Override
	public String getGrandAcos() {
		if(this.grandTotalOrderedProductSales.compareTo(BigDecimal.ZERO)==0) return "N/A";
		return BigDecimalHelper.toPercentageString(this.grandTotalSpend.divide(this.grandTotalOrderedProductSales,4,RoundingMode.HALF_UP),2);
	}
	
	@Override
	public String getGrandTotalClicksOverTotalPageViews() {
		if(this.grandTotalPageViews.compareTo(0)==0) return "N/A";
		BigDecimal grandTotalClicks = new BigDecimal(this.grandTotalClicks);
		BigDecimal grandTotalPageViews = new BigDecimal(this.grandTotalPageViews);
		return BigDecimalHelper.toPercentageString(grandTotalClicks.divide(grandTotalPageViews,4,RoundingMode.HALF_UP),2);
	}
	
	@Override
	public List<SkuAdvertisingEvaluationReportLineItem> getLineItems() {
		return this.lineItems;
	}

}
