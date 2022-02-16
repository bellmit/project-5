package com.kindminds.drs.persist.data.row.sales;

import com.kindminds.drs.api.v2.biz.domain.model.sales.DetailPageSalesTraffic;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DetailPageSalesTrafficImpl implements DetailPageSalesTraffic {


    private int session;
    private int pageView;
    private BigDecimal buyBoxPercentage;

    public DetailPageSalesTrafficImpl(int session, int pageView, BigDecimal buyBoxPercentage) {
        this.session = session;
        this.pageView = pageView;
        this.buyBoxPercentage = calculateAverageBuyBoxPercentage(session,buyBoxPercentage);
    }



    @Override
    public int getSession() {
        return session;
    }

    @Override
    public int getPageView() {
        return pageView;
    }

    @Override
    public BigDecimal getBuyBoxPercentage() {
        return buyBoxPercentage;
    }

    private BigDecimal calculateAverageBuyBoxPercentage(int session , BigDecimal buyBoxPercentage){
        BigDecimal averageBuyBoxPercentage = buyBoxPercentage.divide(BigDecimal.valueOf(session),6, RoundingMode.HALF_UP);
        if (averageBuyBoxPercentage.compareTo(new BigDecimal(1)) > 0) {
            averageBuyBoxPercentage = new BigDecimal(1);
        }
        return averageBuyBoxPercentage;
    }

    @Override
    public String toString() {
        return "DetailPageSalesTrafficImpl{" +
                "session=" + session +
                ", pageView=" + pageView +
                ", buyBoxPercentage=" + buyBoxPercentage +
                '}';
    }
}
