package com.kindminds.drs.api.v2.biz.domain.model.product.onboarding.detail;

import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

public enum EvalProductInfoStatusType {


    INSURANCE_PROVIDER_CONFIRMED( 1,"INSURANCE_PROVIDER_CONFIRMED"),
    INSURANCE_CHECKED( 2, "INSURANCE_CHECKED"),
    COMPLIANCE_CHECKED( 3,"COMPLIANCE_CHECKED"),

    PROFITABILITY_CHECKED( 4,"PROFITABILITY_CHECKED"),
    REVERSE_LOGISTICS_CHECKED( 5,"REVERSE_LOGISTICS_CHECKED"),
    MARKETABILITY_CHECKED( 6,"MARKETABILITY_CHECKED"),
    PACKAGE_CONTENT_EVALUATED( 7,"PACKAGE_CONTENT_EVALUATED");

    private int key;
    private String text;

    EvalProductInfoStatusType(int key , String text){
        this.key = key;
        this.text = text;
    }

    static private final Map<Integer, EvalProductInfoStatusType> keyToStatusTypeMap = new HashMap<>();
    static private final Map<String, EvalProductInfoStatusType> textToStatusTypeMap = new HashMap<>();

    static {
        for(EvalProductInfoStatusType statusType: EvalProductInfoStatusType.values()){
            keyToStatusTypeMap.put(statusType.getKey(),statusType);
            textToStatusTypeMap.put(statusType.getText(),statusType);
        }

    }

    public int getKey() {return this.key;}
    public String getText(){
        return this.text;
    }

    public EvalProductInfoStatusType getPreviousStatusType(EvalProductInfoStatusType current) {
        int previous = current.ordinal() - 1;
        return (EvalProductInfoStatusType) EvalProductInfoStatusType.values()[previous];
    }
    public EvalProductInfoStatusType getNextStatusType(EvalProductInfoStatusType current) {
        int next = current.ordinal() + 1;
        return (EvalProductInfoStatusType) EvalProductInfoStatusType.values()[next];
    }

    public static EvalProductInfoStatusType fromKey(int key){
        EvalProductInfoStatusType statusType = keyToStatusTypeMap.get(key);
        Assert.notNull(statusType);
        return statusType;
    }

    public static EvalProductInfoStatusType fromText(String text){
        EvalProductInfoStatusType s= textToStatusTypeMap.get(text);
        Assert.notNull(s);
        return s;
    }

}