package com.kindminds.drs.persist.v1.model.mapping.report;

import com.kindminds.drs.api.v1.model.marketing.CampaignReportDetail;




import java.math.BigDecimal;


public class CampaignReportDetailImpl implements CampaignReportDetail {

    //@Id
    ////@Column(name="id")
    private int id;
    //@Column(name="supplier_kcode")
    private String supplierKcode;
    //@Column(name="marketplace_name")
    private String marketplaceName;
    //@Column(name="campaign_name")
    private String campaignName;
    //@Column(name="advertised_sku")
    private String advertisedSku;
    //@Column(name="sku")
    private String sku;
    //@Column(name="bp")
    private String bp;
    //@Column(name="period_start_utc")
    private String periodStartUtc;
    //@Column(name="period_end_utc")
    private String periodEndUtc;
    //@Column(name="currency_name")
    private String currencyName;
    //@Column(name="total_spend")
    private BigDecimal totalSpend;

    public CampaignReportDetailImpl() {
    }

    public CampaignReportDetailImpl(int id, String supplierKcode, String marketplaceName, String campaignName, String advertisedSku, String sku, String bp, String periodStartUtc, String periodEndUtc, String currencyName, BigDecimal totalSpend) {
        this.id = id;
        this.supplierKcode = supplierKcode;
        this.marketplaceName = marketplaceName;
        this.campaignName = campaignName;
        this.advertisedSku = advertisedSku;
        this.sku = sku;
        this.bp = bp;
        this.periodStartUtc = periodStartUtc;
        this.periodEndUtc = periodEndUtc;
        this.currencyName = currencyName;
        this.totalSpend = totalSpend;
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
    public String getCampaignName() {
        return this.campaignName;
    }

    @Override
    public String getAdvertisedSku() {
        return this.advertisedSku;
    }

    @Override
    public String getSku() {
        return this.sku;
    }

    @Override
    public String getBp() {
        return this.bp;
    }

    @Override
    public String getPeriodStartUtc() {
        return this.periodStartUtc;
    }

    @Override
    public String getPeriodEndUtc() {
        return this.periodEndUtc;
    }

    @Override
    public String getCurrencyName() {
        return this.currencyName;
    }

    @Override
    public BigDecimal getTotalSpend() {
        return this.totalSpend.setScale(2);
    }
}