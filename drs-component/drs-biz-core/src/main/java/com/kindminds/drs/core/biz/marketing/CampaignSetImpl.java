package com.kindminds.drs.core.biz.marketing;



import com.kindminds.drs.api.v2.biz.domain.model.marketing.Campaign;
import com.kindminds.drs.api.v2.biz.domain.model.marketing.CampaignSet;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class CampaignSetImpl implements CampaignSet {

    BigDecimal totalSpend    = new BigDecimal(0);
    int totalOrderedProductSales    =  0 ;
    List<Campaign> campaigns;
    
    public CampaignSetImpl( List<Campaign> campaigns){
        this.campaigns = campaigns;
    }

    public int sumTotalOrderedProductSales()   {

        this.totalOrderedProductSales  = 0;
        campaigns.forEach (it->{
            this.totalOrderedProductSales += it.getOrderedProductSales();
        });

        return this.totalOrderedProductSales;
    }

    @Override
    public List<Campaign> getCampaigns() {
        return this.campaigns;
    }

    public BigDecimal sumTotalSpend()   {

        this.totalSpend  = new BigDecimal(0);

        campaigns.forEach (it -> {
           this.totalSpend.add(it.getTotalSepnd());
        });

        return this.totalSpend;
    }


    public  BigDecimal  calcTotalACos()   {

        if(this.totalOrderedProductSales == 0)
            return new BigDecimal(0);
        else
            return ((this.totalSpend.divide(new BigDecimal(this.totalOrderedProductSales))).multiply(new BigDecimal(100)))
                .setScale(2,RoundingMode.HALF_UP);

    }



}