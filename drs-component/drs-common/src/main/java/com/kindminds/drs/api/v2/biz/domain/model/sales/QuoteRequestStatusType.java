package com.kindminds.drs.api.v2.biz.domain.model.sales;

import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

public enum QuoteRequestStatusType {


    QUOTE_REQUEST_STARTED(0,"Quotation Request Started"),
    QUOTE_CREATED(1,"Quotation Created"),
    QUOTE_MODIFIED(2,"Quotation Modified"),
    QUOTE_REJECTED(3,"Quotation Rejected"),
    QUOTE_ACCEPTED(4,"Quotation Accepted"),
    QUOTE_CONFIRMED(5,"Quotation Confirmed");

    private int key;
    private String text;

    QuoteRequestStatusType(int key , String text){
        this.key = key;
        this.text = text;
    }

    static private final Map<Integer, QuoteRequestStatusType> keyToStatusTypeMap = new HashMap<>();
    static private final Map<String, QuoteRequestStatusType> textToStatusTypeMap = new HashMap<>();

    static {
        for(QuoteRequestStatusType statusType: QuoteRequestStatusType.values()){
            keyToStatusTypeMap.put(statusType.getKey(),statusType);
            textToStatusTypeMap.put(statusType.getText(),statusType);
        }

    }

    public int getKey() {return this.key;}
    public String getText(){
        return this.text;
    }

    public QuoteRequestStatusType getPreviousStatusType(QuoteRequestStatusType current) {
        int previous = current.ordinal() - 1;
        return (QuoteRequestStatusType) QuoteRequestStatusType.values()[previous];
    }
    public QuoteRequestStatusType getNextStatusType(QuoteRequestStatusType current) {
        int next = current.ordinal() + 1;
        return (QuoteRequestStatusType) QuoteRequestStatusType.values()[next];
    }

    public static QuoteRequestStatusType fromKey(int key){
        QuoteRequestStatusType statusType = keyToStatusTypeMap.get(key);
        Assert.notNull(statusType);
        return statusType;
    }

    public static QuoteRequestStatusType fromText(String text){
        QuoteRequestStatusType s= textToStatusTypeMap.get(text);
        Assert.notNull(s);
        return s;
    }
}