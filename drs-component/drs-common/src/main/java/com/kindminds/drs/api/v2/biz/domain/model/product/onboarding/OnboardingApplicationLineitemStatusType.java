package com.kindminds.drs.api.v2.biz.domain.model.product.onboarding;


import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

public enum OnboardingApplicationLineitemStatusType {

    NO_SERIAL_NUM( -1,"No Serial Number"),
    DRAFT( 0,"Application Draft"),
    INITIAL_REVIEWING(1,"Initial Reviewing"),
    TRIAL_LISTED( 2,"TRIAL LISTED"),
    SAMPLE_EVALUATED( 3,"Sample Evaluated"),

    SAMPLE_PRESENTED( 4,"Sample Presented"),
    COMMENT_BY_MS( 5,"Comment Provided by MS"),
    SAMPLE_FEEDBACK_BY_SC( 6,"Sample Feedback by SC"),
    SAMPLE_COMMENT_BY_MS( 7,"Sample Comment by MS"),
    SAMPLE_APPROVED( 8,"Sample Approved"),

    APPLICATION_INFO_RETURNED( 9,"Application Info Returned"),
    APPLICATION_INFO_RESUBMITTED( 10,"Application Info Resubmitted"),
    APPLICATION_INFO_COMPLETED( 11,"Application Info Completed"),

    PRODUCT_INFO_EVALUATING( 12,"Product Info Evaluating"),
    PRODUCT_INFO_EVALUATED( 13,"Product Info Evaluated"),

    FEASIBILITY_REVIEWED( 14,"Feasibility Reviewed"),
    FEASIBILITY_REJECTED( 15,"Feasibility Rejected"),
    FEASIBILITY_APPROVED( 16,"Feasibility Approved"),


    APPROVED( 97,"Approved"),
    REJECTED( 98,"Rejected"),
    ABORTED( 99 , "Aborted");

    private int key;
    private String text;

    OnboardingApplicationLineitemStatusType(int key , String text){
        this.key = key;
        this.text = text;
    }

    static private final Map<Integer, OnboardingApplicationLineitemStatusType> keyToStatusTypeMap = new HashMap<>();
    static private final Map<String, OnboardingApplicationLineitemStatusType> textToStatusTypeMap = new HashMap<>();

    static {
        for(OnboardingApplicationLineitemStatusType statusType: OnboardingApplicationLineitemStatusType.values()){
            keyToStatusTypeMap.put(statusType.getKey(),statusType);
            textToStatusTypeMap.put(statusType.getText(),statusType);
        }

    }

    public int getKey() {return this.key;}
    public String getText(){
        return this.text;
    }

    public OnboardingApplicationLineitemStatusType getPreviousStatusType(OnboardingApplicationLineitemStatusType current) {
        int previous = current.ordinal() - 1;
        return (OnboardingApplicationLineitemStatusType) OnboardingApplicationLineitemStatusType.values()[previous];
    }
    public OnboardingApplicationLineitemStatusType getNextStatusType(OnboardingApplicationLineitemStatusType current) {
        int next = current.ordinal() + 1;
        return (OnboardingApplicationLineitemStatusType) OnboardingApplicationLineitemStatusType.values()[next];
    }

    public static OnboardingApplicationLineitemStatusType fromKey(int key){
        OnboardingApplicationLineitemStatusType statusType = keyToStatusTypeMap.get(key);
        Assert.notNull(statusType);
        return statusType;
    }

    public static OnboardingApplicationLineitemStatusType fromText(String text){
        OnboardingApplicationLineitemStatusType s= textToStatusTypeMap.get(text);
        Assert.notNull(s);
        return s;
    }

}