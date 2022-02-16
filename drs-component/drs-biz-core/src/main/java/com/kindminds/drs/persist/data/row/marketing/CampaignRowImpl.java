package com.kindminds.drs.persist.data.row.marketing;


import com.kindminds.drs.api.data.row.marketing.CampaignRow;

import java.math.BigDecimal;


public class CampaignRowImpl implements CampaignRow {

    //@Id
    ////@Column(name="id")
       int id ;

    //@Column(name="k_code")
    String kCode;   

    //@Column(name="campaign_name")
    String name ;

    //@Column(name="start_date")
    String startDate ;

    //@Column(name="total_spend")
    BigDecimal totalSpend  ; 

    //@Column(name="one_week_ordered_product_sales")
       int orderedProductSales   ;


    public CampaignRowImpl(){}

    public  CampaignRowImpl(
           int id  ,
           String kCode   ,
           String name   ,
           String startDate     ,
           BigDecimal totalSpend   ,
           int orderedProductSales
    ) {

        this.id = id;
        this.kCode = kCode;
        this.name = name;
        this.startDate = startDate;
        this.totalSpend = totalSpend;
        this.orderedProductSales = orderedProductSales;

    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getkCode() {
        return kCode;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getStartDate() {
        return startDate;
    }

    @Override
    public BigDecimal getTotalSpend() {
        return totalSpend;
    }

    @Override
    public int getOrderedProductSales() {
        return orderedProductSales;
    }
}