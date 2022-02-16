package com.kindminds.drs.api.v2.biz.domain.model.logistics;

public  interface IvsSearchCondition {

    String getSellerCompanyKcode();
    String getDestinationCountry();
    ShippingMethod getShippingMethod();
    ShipmentStatus getStatus();

}
