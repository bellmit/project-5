package com.kindminds.drs.api.data.transfer.productV2;



public final class ProductPackageDimWeightDto {
    
    private final String dimLength;
    
    private final String dimWidth;
    
    private final String dimHeight;
    
    private final String dimUnit;
    
    private final String weight;
    
    private final String weightUnit;

    
    public String getDimLength() {
        return this.dimLength;
    }

    
    public String getDimWidth() {
        return this.dimWidth;
    }

    
    public String getDimHeight() {
        return this.dimHeight;
    }

    
    public String getDimUnit() {
        return this.dimUnit;
    }

    
    public String getWeight() {
        return this.weight;
    }

    
    public String getWeightUnit() {
        return this.weightUnit;
    }

    public ProductPackageDimWeightDto() {
        this("", "", "", "", "", "");
    }

    public ProductPackageDimWeightDto( String dimLength,  String dimWidth,  String dimHeight,  String dimUnit,  String weight,  String weightUnit) {

        this.dimLength = dimLength;
        this.dimWidth = dimWidth;
        this.dimHeight = dimHeight;
        this.dimUnit = dimUnit;
        this.weight = weight;
        this.weightUnit = weightUnit;
    }
}
