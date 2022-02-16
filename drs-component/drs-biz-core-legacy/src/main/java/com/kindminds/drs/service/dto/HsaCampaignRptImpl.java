package com.kindminds.drs.service.dto;

import com.kindminds.drs.api.data.report.HsaCampaignReport;
import com.kindminds.drs.api.v1.model.amazon.AmazonHeadlineSearchAdReportItem;

import com.kindminds.drs.api.v1.model.report.HsaCampaignReportDetail;


import java.math.BigDecimal;
import java.util.List;

public class HsaCampaignRptImpl implements HsaCampaignReport {


    private String currency;
    private Integer totalImpressions;
    private Integer totalClicks;
    private BigDecimal totalClickThruRate;
    private BigDecimal totalCostPerClick;
    private BigDecimal totalSpend;
    private BigDecimal totalSales14days;
    private Integer totalOrders14days;
    private Integer totalUnits14days;
    private BigDecimal totalConversionRate14days;
    private List<HsaCampaignReportDetail> items;

    @Override
    public Integer getTotalImpressions() {
        return totalImpressions;
    }

    @Override
    public Integer getTotalClicks() {
        return totalClicks;
    }

    @Override
    public BigDecimal getTotalClickThruRate() {
        return totalClickThruRate;
    }

    @Override
    public BigDecimal getTotalCostPerClick() {
        return totalCostPerClick;
    }

    @Override
    public BigDecimal getTotalSpend() {
        return totalSpend;
    }

    @Override
    public BigDecimal getTotalSales14days() {
        return totalSales14days;
    }

    @Override
    public Integer getTotalOrders14days() {
        return totalOrders14days;
    }

    @Override
    public Integer getTotalUnits14days() {
        return totalUnits14days;
    }

    @Override
    public BigDecimal getTotalConversionRate14days() {
        return totalConversionRate14days;
    }

    @Override
    public String getCurrency() {
        return this.currency;
    }

    @Override
    public List<HsaCampaignReportDetail> getHsaCampaignReportItems() {
        return this.items;
    }


    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setTotalImpressions(Integer totalImpressions) {
        this.totalImpressions = totalImpressions;
    }

    public void setTotalClicks(Integer totalClicks) {
        this.totalClicks = totalClicks;
    }

    public void setTotalClickThruRate(BigDecimal totalClickThruRate) {
        this.totalClickThruRate = totalClickThruRate;
    }

    public void setTotalCostPerClick(BigDecimal totalCostPerClick) {
        this.totalCostPerClick = totalCostPerClick;
    }

    public void setTotalSpend(BigDecimal totalspend) {
        this.totalSpend = totalspend;
    }

    public void setTotalSales14days(BigDecimal totalSales14days) {
        this.totalSales14days = totalSales14days;
    }

    public void setTotalOrders14days(Integer totalOrders14days) {
        this.totalOrders14days = totalOrders14days;
    }

    public void setTotalUnits14days(Integer totalUnits14days) {
        this.totalUnits14days = totalUnits14days;
    }

    public void setTotalConversionRate14days(BigDecimal totalConversionRate14days) {
        this.totalConversionRate14days = totalConversionRate14days;
    }

    public void setHsaCampaignReportItems(List<HsaCampaignReportDetail> items) {
        this.items = items;
    }

}
