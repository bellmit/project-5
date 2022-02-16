package com.kindminds.drs.api.data.transfer.customer;


public final class Return {

    public final String returnDate;
    public final String orderId;
    public final String skuName;

    public Return(String returnDate, String orderId, String skuName) {
        this.returnDate = returnDate;
        this.orderId = orderId;
        this.skuName = skuName;
    }
}


