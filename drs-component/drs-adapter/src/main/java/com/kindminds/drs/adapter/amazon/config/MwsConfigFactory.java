package com.kindminds.drs.adapter.amazon.config;


import com.kindminds.drs.Country;
import com.kindminds.drs.Marketplace;

public class MwsConfigFactory {

    public MwsConfig getConfig(MwsApi api , Marketplace marketplace){

        if(api == MwsApi.Order ){
            if(marketplace == Marketplace.AMAZON_COM || marketplace == Marketplace.AMAZON_CA)
                return new AmazonMwsOrderConfig(Country.NA);
            else
                return new AmazonMwsOrderConfig(Country.EU);
        }

        if(api == MwsApi.FulfillmentInventory){
            return new AmazonMwsFulfillmentInventoryConfig(marketplace);
        }

        if(api == MwsApi.FulfillmentOutboundShipment)
            return new AmazonMwsFulfillmentOutboundShipmentConfig(marketplace);

        if(api == MwsApi.Report){
            return new AmazonMwsReportConfig(marketplace);
        }


        if(api == MwsApi.Finances) {
            return new AmazonMwsFinancesConfig(marketplace);
        }

        if (api == MwsApi.Feeds) {
            return new AmazonMwsFeedConfig(marketplace);
        }

        return null;
    }

}
