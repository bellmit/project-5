package com.kindminds.drs.persist.v1.model.mapping.report;

import com.kindminds.drs.api.v1.model.report.HsaCampaignReportDetail;




import java.math.BigDecimal;


public class HsaCampaignReportDetailImpl implements HsaCampaignReportDetail {

    //@Id
    ////@Column(name="id")
    private int id;
    //@Column(name="supplier_kcode")
    private String supplierKcode;

    //@Column(name="marketplace_name")
    private String marketplaceName;

    //@Column(name="marketplace_id")
    private Integer marketplaceId;


    //@Column(name="campaign_name")
    private String campaignName;

    //@Column(name="report_date")
    private String reportDate;
    //@Column(name="currency_name")
    private String currencyName;

    //@Column(name="impressions")
    private Integer impressions;

    //@Column(name="clicks")
    private Integer clicks;

    //@Column(name="click_thru_rate")
    private BigDecimal ctr;

    //@Column(name="cost_per_click")
    private BigDecimal cpc;

    //@Column(name="spend")
    private BigDecimal totalSpend;

    //@Column(name="total_advertising_cost_of_sales")
    private BigDecimal totalACOS;

    //@Column(name="total_return_on_advertising_spend")
    private BigDecimal totalRoAS;

    //@Column(name="total_sales_14days")
    private BigDecimal totalSales14days;

    //@Column(name="total_orders_14days")
    private Integer totalOrders14days;

    //@Column(name="total_units_14days")
    private Integer totalUnits14days;

    //@Column(name="conversion_rate_14days")
    private BigDecimal conversionRate14days;

    public HsaCampaignReportDetailImpl() {
    }


    public HsaCampaignReportDetailImpl(int id, String supplierKcode, String marketplaceName, Integer marketplaceId, String campaignName, String reportDate, String currencyName, Integer impressions, Integer clicks, BigDecimal ctr, BigDecimal cpc, BigDecimal totalSpend, BigDecimal totalACOS, BigDecimal totalRoAS, BigDecimal totalSales14days, Integer totalOrders14days, Integer totalUnits14days, BigDecimal conversionRate14days) {
        this.id = id;
        this.supplierKcode = supplierKcode;
        this.marketplaceName = marketplaceName;
        this.marketplaceId = marketplaceId;
        this.campaignName = campaignName;
        this.reportDate = reportDate;
        this.currencyName = currencyName;
        this.impressions = impressions;
        this.clicks = clicks;
        this.ctr = ctr;
        this.cpc = cpc;
        this.totalSpend = totalSpend;
        this.totalACOS = totalACOS;
        this.totalRoAS = totalRoAS;
        this.totalSales14days = totalSales14days;
        this.totalOrders14days = totalOrders14days;
        this.totalUnits14days = totalUnits14days;
        this.conversionRate14days = conversionRate14days;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getSupplierKcode() {
        return this.supplierKcode;
    }

    @Override
    public String getMarketplaceName() {
        return this.marketplaceName;
    }

    @Override
    public Integer getMarketplaceId() {
        return this.marketplaceId;
    }

    @Override
    public String getCampaignName() {
        return this.campaignName;
    }

    @Override
    public String getReportDate() {
        return this.reportDate;
    }

    @Override
    public String getCurrencyName() {
        return this.currencyName;
    }

    @Override
    public BigDecimal getTotalSpend() {
        return this.totalSpend.setScale(2);
    }


    @Override
    public Integer getImpressions() {
        return this.impressions;
    }

    @Override
    public Integer getClicks() {
        return this.clicks;
    }

    @Override
    public BigDecimal getClickThruRate() {
        return this.ctr;
    }

    @Override
    public BigDecimal getCostPerClick() {
        return this.cpc;
    }

    @Override
    public BigDecimal getTotalACOS() {
        return this.totalACOS;
    }

    @Override
    public BigDecimal getTotalRoAS() {
        return this.totalRoAS;
    }

    @Override
    public BigDecimal getTotalSales14days() {
        return this.totalSales14days;
    }

    @Override
    public Integer getTotalOrders14days() {
        return this.totalOrders14days;
    }

    @Override
    public Integer getTotalUnits14days() {
        return this.totalUnits14days;
    }

    @Override
    public BigDecimal getConversionRate14days() {
        return this.conversionRate14days;
    }


}