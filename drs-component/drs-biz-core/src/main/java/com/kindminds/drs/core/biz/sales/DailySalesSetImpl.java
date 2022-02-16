package com.kindminds.drs.core.biz.sales;


import com.kindminds.drs.api.v2.biz.domain.model.sales.DailySales;
import com.kindminds.drs.api.v2.biz.domain.model.sales.DailySalesSet;

import java.math.BigDecimal;
import java.util.List;

public class DailySalesSetImpl implements DailySalesSet {

    List<DailySales> dailySales ;
    public DailySalesSetImpl( List<DailySales> dailySales){
        this.dailySales = dailySales;
    }

    @Override
    public List<DailySales> getDailySales() {
        return this.dailySales;
    }

    public BigDecimal sumTotalRevenue()   {

        BigDecimal totalRevenue    = new BigDecimal(0);
        dailySales.forEach (it -> {
            if(it != null)if(it.getRevenueUSD() != null) totalRevenue.add(it.getRevenueUSD());
        });

        return totalRevenue;
    }

    public int sumTotalQty()   {

        int totalQty  = 0;

        for (DailySales it :dailySales){
            if(it != null) totalQty+= it.getQty();
        }
        return totalQty;

    }
}