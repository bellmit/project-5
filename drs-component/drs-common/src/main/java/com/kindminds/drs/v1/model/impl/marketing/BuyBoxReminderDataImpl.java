package com.kindminds.drs.v1.model.impl.marketing;

import com.kindminds.drs.api.v1.model.marketing.BuyBoxReminderData;

import java.math.BigDecimal;
import java.util.Date;

public class BuyBoxReminderDataImpl implements BuyBoxReminderData {
    private Date reportDate;
    private String marketplaceName;
    private String companyCode;
    private String shortNameEnUs;
    private String skuCode;
    private String baseCode;
    private BigDecimal buyBoxRate;

    public BuyBoxReminderDataImpl(Date reportDate, String marketplaceName,
                                  String companyCode, String shortNameEnUs,
                                  String skuCode, String baseCode,
                                  BigDecimal buyBoxRate) {
        this.reportDate = reportDate;
        this.marketplaceName = marketplaceName;
        this.companyCode = companyCode;
        this.shortNameEnUs = shortNameEnUs;
        this.skuCode = skuCode;
        this.baseCode = baseCode;
        this.buyBoxRate = buyBoxRate;
    }

    public Date getReportDate() {
        return reportDate;
    }

    public String getMarketplaceName() {
        return marketplaceName;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public String getShortNameEnUs() {
        return shortNameEnUs;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public String getBaseCode() {
        return baseCode;
    }

    public BigDecimal getBuyBoxRate() {
        return buyBoxRate;
    }
}
