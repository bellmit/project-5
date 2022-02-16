package com.kindminds.drs.core.biz.sales;


import com.kindminds.drs.api.data.row.sales.DailySalesRow;
import com.kindminds.drs.api.v2.biz.domain.model.sales.DailySales;

import java.math.BigDecimal;

public  class DailySalesImpl implements DailySales {

    private DailySalesRow dailySalesRow;

    public  DailySalesImpl(DailySalesRow dailySalesRow){
        this.dailySalesRow = dailySalesRow;
    }


    @Override
    public String getKcode() {
        return this.dailySalesRow.getKcode();
    }

    @Override
    public int getQty() {
        return this.dailySalesRow.getQty();
    }

    @Override
    public BigDecimal getRevenueUSD() {
        return this.dailySalesRow.getRevenueUsd();
    }

    @Override
    public String getSalesDate() {
        return this.dailySalesRow.getSalesDate();
    }
}