package com.kindminds.drs.api.data.transfer.productV2;



public final class SimpleBaseProductDto {
    
    private final String productBaseCode;
    
    private final String name;

    
    public String getProductBaseCode() {
        return this.productBaseCode;
    }

    
    public String getName() {
        return this.name;
    }

    public SimpleBaseProductDto() {
        this("", "");
    }

    public SimpleBaseProductDto( String productBaseCode,  String name) {

        this.productBaseCode = productBaseCode;
        this.name = name;
    }
}


