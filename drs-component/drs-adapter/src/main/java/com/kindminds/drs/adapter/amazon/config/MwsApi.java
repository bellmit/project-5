package com.kindminds.drs.adapter.amazon.config;


public  enum MwsApi {

    Order(3),
    FulfillmentInventory(3),
    FulfillmentOutboundShipment(3),
    Report(3),
    Finances(3),
    Feeds(3);


    private Integer maxRequestDurationHours;
    MwsApi(int hours){

        this.maxRequestDurationHours = hours;
    }
    public int getMaxRequestDurationHours(){ return this.maxRequestDurationHours; }
}


 enum MwsAccessKey {

    NA( 0 , "AKIAIFCJMHSMRJ6UJLDA"),
     EU( 1 , "AKIAIOILJJ7E4I3PR4JA"),
     UK(2,"AKIAIOILJJ7E4I3PR4JA")
     ;


     private int key;
     private String value;

     MwsAccessKey(int key , String value){
         this.key = key;
         this.value = value;
     }

     public int getKey() {return this.key;}
     public String getValue(){
         return this.value;
     }


 }


 enum MwsSecretKey {

     NA( 0 , "4zgXhIMuglBKI5ddr2q/wmtumbqPH836ZdOgsUYQ"),
     EU( 1 , "UdLp+niCoT5M5v9vVIbwMgqzavNMRmPmeJrRzF7M"),
     UK(2,"UdLp+niCoT5M5v9vVIbwMgqzavNMRmPmeJrRzF7M")
     ;


     private int key;
     private String value;

     MwsSecretKey(int key , String value){
         this.key = key;
         this.value = value;
     }

     public int getKey() {return this.key;}
     public String getValue(){
         return this.value;
     }

 }


enum  MwsSellerId {

    NA( 0 , "A3QY80NORGXT27"),
    EU( 1 , "AMDE353JDRG7M"),
    UK(2,"AMDE353JDRG7M")


    ;

    private int key;
    private String value;

    MwsSellerId(int key , String value){
        this.key = key;
        this.value = value;
    }

    public int getKey() {return this.key;}
    public String getValue(){
        return this.value;
    }

}

enum MwsServiceUrl {

    NA_Order( 0 , "https://mws.amazonservices.com/Orders/2013-09-01"),
    EU_Order( 1 , "https://mws.amazonservices.co.uk/Orders/2013-09-01"),
    NA_FulfillmentInventory( 2 , "https://mws.amazonservices.com/FulfillmentInventory/2010-10-01"),
    UK_FulfillmentInventory(3,"https://mws.amazonservices.co.uk/FulfillmentInventory/2010-10-01"),
    NA_FulfillmentOutboundShipment( 4 , "https://mws.amazonservices.com/FulfillmentOutboundShipment/2010-10-01"),
    UK_FulfillmentOutboundShipment(5,"https://mws.amazonservices.co.uk/FulfillmentOutboundShipment/2010-10-01"),
    US_Report( 6 , "https://mws.amazonservices.com"),
    UK_Report( 7 , "https://mws-eu.amazonservices.com"),
    EU_Report(8,"https://mws-eu.amazonservices.com"),

    US_Feeds( 9 , "https://mws.amazonservices.com"),
    UK_Feeds( 10 , "https://mws-eu.amazonservices.com"),
    EU_Feeds( 11 , "https://mws.amazonservices.co.uk")

    ;

    private int key;
    private String value;

    MwsServiceUrl(int key , String value){
        this.key = key;
        this.value = value;
    }

    public int getKey() {return this.key;}
    public String getValue(){
        return this.value;
    }

}

enum MwsAppName {

    NA_Order( 0 , "www.drs.network"),
    EU_Order( 1 , "www.drs.network"),
    FulfillmentInventory( 2 , "access.drs.network"),
    FulfillmentOutboundShipment( 3 , "access.drs.network"),
    Report(4 , "access.drs.network"),
    Feeds(5 , "access.drs.network")
    ;

    private int key;
    private String value;

    MwsAppName(int key , String value){
        this.key = key;
        this.value = value;
    }

    public int getKey() {return this.key;}
    public String getValue(){
        return this.value;
    }

}

enum MwsAppVersion {

    NA_Order( 0 , "20150105"),
    EU_Order( 1 , "20160513"),
    FulfillmentInventory( 2 , "20170523"),
    FulfillmentOutboundShipment( 3 , "20170523"),
    Report( 4 , "20170523"),
    Feeds( 5 , "20170523")
    ;

    private int key;
    private String value;

    MwsAppVersion(int key , String value){
        this.key = key;
        this.value = value;
    }

    public int getKey() {return this.key;}
    public String getValue(){
        return this.value;
    }

}

enum MwsMarketplaceId {

    US(0,"ATVPDKIKX0DER"),
    CA(1,"A2EUQ1WTGCTBG2"),
    UK(2,"A1F83G8C2ARO7P"),
    DE(3,"A1PA6795UKMFR9"),
    FR(4,"A13V1IB3VIYZZH"),
    IT(5,"APJ6JRA9NG5V4"),
    ES(6,"A1RKKUPIHCS9HS")
    ;

    private int key;
    private String value;

    MwsMarketplaceId(int key , String value){
        this.key = key;
        this.value = value;
    }

    public int getKey() {return this.key;}
    public String getValue(){
        return this.value;
    }

}