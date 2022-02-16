package com.kindminds.drs.api.v2.biz.domain.model.marketing;

import com.kindminds.drs.api.v2.biz.domain.model.marketing.Campaign;

import java.math.BigDecimal;
import java.util.List;

public  interface CampaignSet{

    List<Campaign> getCampaigns() ;

    BigDecimal sumTotalSpend() ;

    int sumTotalOrderedProductSales() ;

    BigDecimal calcTotalACos() ;

}
