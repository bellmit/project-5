package com.kindminds.drs.persist.data.row.sales;



import com.kindminds.drs.api.data.row.sales.DailySalesRow;

import java.math.BigDecimal;






public class DailySalesRowImpl implements DailySalesRow {

    //@Id
    ////@Column(name="id")
       int id ;
    //@Column(name="k_code")
    String kcode;

    //@Column(name="sales_date")
    String salesDate;
    //@Column(name="sales_channel")
    String salesChannel;
    //@Column(name="product_base_code")
    String productBaseCode;
    //@Column(name="product_sku_code")
    String productSkuCode;
    //@Column(name="product_name")
    String productName ;
    //@Column(name="asin")
    String asin ;
    //@Column(name="revenue")
    BigDecimal revenue  ;
    //@Column(name="revenue_usd")
    BigDecimal revenueUsd;
    //@Column(name="gross_profit")
    BigDecimal grossProfit;
    //@Column(name="gross_profit_usd")
    BigDecimal grossProfitUsd;
    //@Column(name="qty")
       int qty;



    public DailySalesRowImpl(){}


    public DailySalesRowImpl(
           int id  ,
           String  kcode   ,
           String  salesDate    ,
           String salesChannel    ,
           String productBaseCode   ,
           String productSkuCode   ,
           String productName   ,
           String asin   ,
           BigDecimal revenue   ,
           BigDecimal revenueUsd   ,
           BigDecimal grossProfit   ,
           BigDecimal grossProfitUsd   ,
           int qty
         ) {

        this.id = id;
        this.kcode = kcode;

        this.salesDate = salesDate;
        this.salesChannel = salesChannel;
        this.productBaseCode = productBaseCode;
        this.productSkuCode = productSkuCode;
        this.productName = productName;
        this.asin = asin;
        this.revenue = revenue;
        this.revenueUsd = revenueUsd;
        this.grossProfit = grossProfit;
        this.grossProfitUsd = grossProfitUsd;
        this.qty = qty;


    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getKcode() {
        return kcode;
    }

    @Override
    public String getSalesDate() {
        return salesDate;
    }

    @Override
    public String getSalesChannel() {
        return salesChannel;
    }

    @Override
    public String getProductBaseCode() {
        return productBaseCode;
    }

    @Override
    public String getProductSkuCode() {
        return productSkuCode;
    }

    @Override
    public String getProductName() {
        return productName;
    }

    @Override
    public String getAsin() {
        return asin;
    }

    @Override
    public BigDecimal getRevenue() {
        return revenue;
    }

    @Override
    public BigDecimal getRevenueUsd() {
        return revenueUsd;
    }

    @Override
    public BigDecimal getGrossProfit() {
        return grossProfit;
    }

    @Override
    public BigDecimal getGrossProfitUsd() {
        return grossProfitUsd;
    }

    @Override
    public int getQty() {
        return qty;
    }
}