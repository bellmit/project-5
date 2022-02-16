package com.kindminds.drs.core.biz.marketing;


import com.kindminds.drs.api.data.row.marketing.CampaignRow;
import com.kindminds.drs.api.v2.biz.domain.model.marketing.Campaign;

import java.math.BigDecimal;

public class CampaignImpl implements Campaign {

    private CampaignRow campaignRow  ;

    public  CampaignImpl(CampaignRow campaignRow){
        this.campaignRow = campaignRow;
    }

    @Override
    public int getId() {
        return this.campaignRow.getId();
    }

    @Override
    public java.lang.String getKCode() {
        return this.campaignRow.getkCode();
    }

    @Override
    public java.lang.String getName() {
        return this.campaignRow.getName();
    }

    @Override
    public java.lang.String getStartDate() {
        return this.campaignRow.getStartDate();
    }

    @Override
    public java.math.BigDecimal getTotalSepnd() {
        return this.campaignRow.getTotalSpend();
    }

    @Override
    public int getOrderedProductSales() {
        return this.campaignRow.getOrderedProductSales();
    }

    @Override
    public BigDecimal calcAcos() {
        return null;
    }


}