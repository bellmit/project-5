package com.kindminds.drs.persist.data.access.nosql.mongo.dto.product;



public class VariationThemeSku {

    //@Transient
    private String name = "Variation Theme";

    private String value;

    public VariationThemeSku() {
    }

    public VariationThemeSku(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

