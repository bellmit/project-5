package com.kindminds.drs.persist.data.access.nosql.mongo.dto.product;


public class VariableSku {

    //@Transient
    private String name = "Variable";

    private String value;

    public VariableSku() {
    }

    public VariableSku(String value) {
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

