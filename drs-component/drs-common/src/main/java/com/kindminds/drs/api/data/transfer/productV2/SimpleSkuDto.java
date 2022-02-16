package com.kindminds.drs.api.data.transfer.productV2;



public final class SimpleSkuDto {
    
    private final String skuCode;
    
    private final String name;

    
    public String getSkuCode() {
        return this.skuCode;
    }

    
    public String getName() {
        return this.name;
    }

    public SimpleSkuDto() {
        this("", "");
    }

    public SimpleSkuDto( String skuCode,  String name) {

        this.skuCode = skuCode;
        this.name = name;
    }
}
