package com.kindminds.drs.v1.model.impl.marketing;

import com.kindminds.drs.api.v1.model.marketing.ExternalMarketingSalesItem;

import java.math.BigDecimal;

public class ExternalMarketingSalesItemImpl implements ExternalMarketingSalesItem {

    private String date;
    private BigDecimal sales;

    public ExternalMarketingSalesItemImpl(){}

    public ExternalMarketingSalesItemImpl(String date, BigDecimal sales) {
        this.date = date;
        this.sales = sales;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public BigDecimal getSales() {
        return sales;
    }

    public void setSales(BigDecimal sales) {
        this.sales = sales;
    }
}
