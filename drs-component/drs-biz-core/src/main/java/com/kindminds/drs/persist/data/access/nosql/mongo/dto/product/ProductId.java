package com.kindminds.drs.persist.data.access.nosql.mongo.dto.product;



public class ProductId {

//    @Field(name="name")
   // @Transient
    private String name = "Product ID";

//    @Field(name="value")
    private String value;

    public ProductId() {
    }

    public ProductId(String value) {
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

