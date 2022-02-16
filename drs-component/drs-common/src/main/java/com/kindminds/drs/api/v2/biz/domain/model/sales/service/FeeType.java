package com.kindminds.drs.api.v2.biz.domain.model.sales.service;


import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

public enum FeeType {


    MONTHLY_SERVICE_FEE(0,"Monthly Service Fee");

    private int key;
    private String text;

    FeeType(int key , String text){
        this.key = key;
        this.text = text;
    }

    static private final Map<Integer, FeeType> keyToStatusTypeMap = new HashMap<>();
    static private final Map<String, FeeType> textToStatusTypeMap = new HashMap<>();

    static {
        for(FeeType feeType: FeeType.values()){
            keyToStatusTypeMap.put(feeType.getKey(),feeType);
            textToStatusTypeMap.put(feeType.getText(),feeType);
        }

    }

    public int getKey() {return this.key;}
    public String getText(){
        return this.text;
    }

    public FeeType getFeeType(FeeType current) {
        int previous = current.ordinal() - 1;
        return (FeeType) FeeType.values()[previous];
    }
    public FeeType getNextFeeType(FeeType current) {
        int next = current.ordinal() + 1;
        return (FeeType) FeeType.values()[next];
    }

    public static FeeType fromKey(int key){
        FeeType feeType = keyToStatusTypeMap.get(key);
        Assert.notNull(feeType);
        return feeType;
    }

    public static FeeType fromText(String text){
        FeeType s= textToStatusTypeMap.get(text);
        Assert.notNull(s);
        return s;
    }
}