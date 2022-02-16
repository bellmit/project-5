package com.kindminds.drs.enums;

import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

public enum OnboardingApplicationStatusType {
    NO_SERIAL_NUM( -1,"No Serial Number"),
    DRAFT( 0,"Draft"),
    SUBMITTED(1,"Submitted"),
    ACCEPTED(2,"Accepted"),

    QUOTE_REQUEST_STARTED(3,"Quote Request Started"),
    QUOTE_REQUEST_COMPLETED(4,"Quote Request Completed"),

    LINEITEM_REVIEWING(5,"Lineitem Reviewing"),
    PRODUCT_INFO_EVALUATED(6,"Product Info Evaluated"),

    FEASIBILITY_REVIEWED(7,"Feasibility Reviewed"),
    //FEASIBILITY_APPROVED(10,"Feasibility Approved"),
    FEASIBILITY_REJECTED(8,"Feasibility Rejected"),

    APPROVED(97,"Approved"),
    ABORTED(98,"Aborted"),
    MIGRATED(99,"MIGRATED");


    private int key;
    private String text;

    OnboardingApplicationStatusType(int key , String text){
        this.key = key;
        this.text = text;
    }

    static private final Map<Integer, OnboardingApplicationStatusType> keyToStatusTypeMap = new HashMap<>();
    static private final Map<String, OnboardingApplicationStatusType> textToStatusTypeMap = new HashMap<>();

    static {
        for(OnboardingApplicationStatusType statusType: OnboardingApplicationStatusType.values()){
            keyToStatusTypeMap.put(statusType.getKey(),statusType);
            textToStatusTypeMap.put(statusType.getText(),statusType);
        }

    }

    public int getKey() {return this.key;}
    public String getText(){
        return this.text;
    }

    public OnboardingApplicationStatusType getPreviousStatusType(OnboardingApplicationStatusType current) {
        int previous = current.ordinal() - 1;
        return (OnboardingApplicationStatusType) OnboardingApplicationStatusType.values()[previous];
    }
    public OnboardingApplicationStatusType getNextStatusType(OnboardingApplicationStatusType current) {
        int next = current.ordinal() + 1;
        return (OnboardingApplicationStatusType) OnboardingApplicationStatusType.values()[next];
    }

    public static OnboardingApplicationStatusType fromKey(int key){
        OnboardingApplicationStatusType statusType = keyToStatusTypeMap.get(key);
        Assert.notNull(statusType);
        return statusType;
    }

    public static OnboardingApplicationStatusType fromText(String text){
        OnboardingApplicationStatusType s= textToStatusTypeMap.get(text);
        Assert.notNull(s);
        return s;
    }
}
