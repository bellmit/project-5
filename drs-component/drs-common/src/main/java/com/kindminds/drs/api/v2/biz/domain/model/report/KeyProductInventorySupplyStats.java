package com.kindminds.drs.api.v2.biz.domain.model.report;

public interface KeyProductInventorySupplyStats {


    String getMarketplace();

    String getBpCode();

    String getSku();

    int getInBound();

    int getInStock();

    int getTransfer();
}
