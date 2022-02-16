package com.kindminds.drs.api.v2.biz.domain.model;

import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

public enum SalesChannel {
    NON_ORDER(   0,"Non-order",   "UTC",                "GMT",      "GMT" ),
    AMAZON_COM(  1,"Amazon.com",  "America/Los_Angeles","PST",      "PDT" ),
    TRUETOSOURCE(2,"TrueToSource","America/Los_Angeles","PST",      "PDT" ),
    EBAY(        3,"eBay",        "America/Los_Angeles","PST",      "PDT" ),
    AMAZON_CO_UK(4,"Amazon.co.uk","Europe/London",      "GMT",      "BST" ),
    AMAZON_CA(   5,"Amazon.ca",   "America/Vancouver",  "PST",      "PDT"),
    AMAZON_DE(   6,"Amazon.de",   "Europe/Berlin",      "GMT+01:00","GMT+02:00"),
    AMAZON_FR(   7,"Amazon.fr",   "Europe/Paris",       "GMT+01:00","GMT+02:00"),
    AMAZON_IT(   8,"Amazon.it",   "Europe/Rome",        "GMT+01:00","GMT+02:00" ),
    AMAZON_ES(   9,"Amazon.es",   "Europe/Madrid",      "GMT+01:00","GMT+02:00" ),
    ALL_CHANNEL(   99,"All-Marketplace",   "UTC",                "GMT",      "GMT");
    private int key;
    private String displayName;
    private String timeZoneAssigned;
    private String timeZonePostFixTextStandard;
    private String timeZonePostFixTextDaylightSaving;

    static private final Map<Integer,SalesChannel> idToSalesChannelMap = new HashMap<>();
    static private final Map<String,SalesChannel> displayNameToSalesChannelMap = new HashMap<>();
    static { for(SalesChannel s:SalesChannel.values()) idToSalesChannelMap.put(s.getKey(),s);}
    static { for(SalesChannel s:SalesChannel.values()) displayNameToSalesChannelMap.put(s.getDisplayName(),s);}
    SalesChannel(int id,String displayName,String timeZoneAssigned,
                 String timeZonePostFixTextStd,String timeZonePostFixTextDst ) {
        this.key = id;
        this.displayName = displayName;
        this.timeZoneAssigned = timeZoneAssigned;
        this.timeZonePostFixTextStandard = timeZonePostFixTextStd;
        this.timeZonePostFixTextDaylightSaving = timeZonePostFixTextDst;

    }
    public int getKey() {
        return this.key;
    }
    public String getDisplayName() {
        return this.displayName;
    }
    public String getTimeZoneAssigned() {
        return timeZoneAssigned;
    }
    public String getTimeZonePostFixTextStandard() {
        return timeZonePostFixTextStandard;
    }
    public String getTimeZonePostFixTextDaylightSaving() {
        return timeZonePostFixTextDaylightSaving;
    }


    public static SalesChannel fromKey(int key){
        SalesChannel salesChannel = idToSalesChannelMap.get(key);
        Assert.notNull(salesChannel);
        return salesChannel;
    }
    public static SalesChannel fromDisplayName(String displayName){
        return displayNameToSalesChannelMap.get(displayName);
    }
}