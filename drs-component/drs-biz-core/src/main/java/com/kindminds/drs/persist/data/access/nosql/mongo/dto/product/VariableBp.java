package com.kindminds.drs.persist.data.access.nosql.mongo.dto.product;

import java.util.List;

public class VariableBp {

    private String value;

    private List<Integer> index;


    public VariableBp() {
    }

    public VariableBp(String value, List<Integer> index) {
        this.value = value;
        this.index = index;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<Integer> getIndex() {
        return index;
    }

    public void setIndex(List<Integer> index) {
        this.index = index;
    }
}

