package com.kindminds.drs.api.v2.biz.domain.model.logistics;

public enum  ShippingMethod {
    EXPRESS(3),
    AIR_CARGO(5),
    SEA_FREIGHT(5);

    private int daysToPrepare;
    ShippingMethod(int daysToPrepare){this.daysToPrepare = daysToPrepare;}

    public int getDaysToPrepare() {return this.daysToPrepare;}
}
