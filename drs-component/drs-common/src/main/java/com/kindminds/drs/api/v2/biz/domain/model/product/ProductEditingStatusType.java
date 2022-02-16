package com.kindminds.drs.api.v2.biz.domain.model.product;


import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

public enum ProductEditingStatusType {
    PENDING_SUPPLIER_ACTION( 0,"Pending supplier action"),
    PENDING_DRS_REVIEW(1,"Pending DRS review"),
    FINALIZED( 99,"Finalized");

    private int key;
    private String text;

    ProductEditingStatusType(int key , String text){
        this.key = key;
        this.text = text;
    }

    static private final Map<Integer, ProductEditingStatusType> keyToStatusTypeMap = new HashMap<>();
    static private final Map<String, ProductEditingStatusType> textToStatusTypeMap = new HashMap<>();

    static {
        for(ProductEditingStatusType statusType: ProductEditingStatusType.values()){
            keyToStatusTypeMap.put(statusType.getKey(),statusType);
            textToStatusTypeMap.put(statusType.getText(),statusType);
        }

    }

    public int getKey() {return this.key;}
    public String getText(){
        return this.text;
    }

    public ProductEditingStatusType getPreviousStatusType(ProductEditingStatusType current) {
        int previous = current.ordinal() - 1;
        return (ProductEditingStatusType) ProductEditingStatusType.values()[previous];
    }
    public ProductEditingStatusType getNextStatusType(ProductEditingStatusType current) {
        int next = current.ordinal() + 1;
        return (ProductEditingStatusType) ProductEditingStatusType.values()[next];
    }

    public static ProductEditingStatusType fromKey(int key){
        ProductEditingStatusType statusType = keyToStatusTypeMap.get(key);
        Assert.notNull(statusType);
        return statusType;
    }

    public static ProductEditingStatusType fromText(String text){
        ProductEditingStatusType s= textToStatusTypeMap.get(text);
        Assert.notNull(s);
        return s;
    }

}