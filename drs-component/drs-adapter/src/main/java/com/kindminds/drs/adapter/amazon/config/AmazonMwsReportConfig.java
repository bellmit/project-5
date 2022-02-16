package com.kindminds.drs.adapter.amazon.config;

import com.amazonaws.mws.MarketplaceWebServiceClient;
import com.amazonaws.mws.MarketplaceWebServiceConfig;
import com.amazonaws.mws.model.IdList;
import com.amazonservices.mws.FulfillmentInventory._2010_10_01.FBAInventoryServiceMWSAsyncClient;
import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersAsyncClient;
import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersConfig;
import com.kindminds.drs.Country;
import com.kindminds.drs.Marketplace;

import java.util.Arrays;
import java.util.List;

public class AmazonMwsReportConfig implements  MwsConfig<MarketplaceWebServiceClient>{

    private  String sellerId = "";

    private  IdList marketplaceIdList = null;

    private  String accessKey = "";
    private  String secretKey = "";


    private  String appName = "";
    private  String appVersion = "";
    private  String serviceURL = "";

    private  MarketplaceWebServiceClient client = null;

    @Override
    public String getSellerId() {
        return this.sellerId;
    }

    @Override
    public String getMarketplaceId() {
        return null;
    }

    @Override
    public List<String> getMarketplaceIds() {
        return null;
    }

    @Override
    public IdList getMarketplaceIdList() {
        return this.marketplaceIdList;
    }



    public AmazonMwsReportConfig(Marketplace marketplace){


        if(marketplace == Marketplace.AMAZON_COM || marketplace == Marketplace.AMAZON_CA){

            this.sellerId = MwsSellerId.NA.getValue();

            //todo arhtur , different report have to setup different way ?
            if(marketplace == Marketplace.AMAZON_COM)
                this.marketplaceIdList = new IdList().withId(MwsMarketplaceId.US.getValue());
            else
                this.marketplaceIdList = new IdList().withId(MwsMarketplaceId.CA.getValue());


            this.accessKey = MwsAccessKey.NA.getValue();
            this.secretKey = MwsSecretKey.NA.getValue();


            this.appName = MwsAppName.Report.getValue();
            this.appVersion = MwsAppVersion.Report.getValue();


            this.serviceURL = MwsServiceUrl.US_Report.getValue();

        } else if(marketplace == Marketplace.AMAZON_CO_UK ){

            this.sellerId = MwsSellerId.UK.getValue();

            //todo arhtur , different report have to setup different way ?
            this.marketplaceIdList =
                    new IdList().withId(MwsMarketplaceId.UK.getValue());


            this.accessKey = MwsAccessKey.UK.getValue();
            this.secretKey = MwsSecretKey.UK.getValue();


            this.appName = MwsAppName.Report.getValue();
            this.appVersion = MwsAppVersion.Report.getValue();

            this.serviceURL = MwsServiceUrl.UK_Report.getValue();

        }else if( marketplace == Marketplace.AMAZON_DE){

            this.sellerId = MwsSellerId.EU.getValue();

            //todo arhtur , different report have to setup different way ?
            this.marketplaceIdList =
                    new IdList().withId(MwsMarketplaceId.DE.getValue());


            this.accessKey = MwsAccessKey.EU.getValue();
            this.secretKey = MwsSecretKey.EU.getValue();


            this.appName = MwsAppName.Report.getValue();
            this.appVersion = MwsAppVersion.Report.getValue();

            this.serviceURL = MwsServiceUrl.EU_Report.getValue();
        }else if(marketplace == Marketplace.AMAZON_FR){

            this.sellerId = MwsSellerId.EU.getValue();

            //todo arhtur , different report have to setup different way ?
            this.marketplaceIdList =
                    new IdList().withId(MwsMarketplaceId.FR.getValue());


            this.accessKey = MwsAccessKey.EU.getValue();
            this.secretKey = MwsSecretKey.EU.getValue();


            this.appName = MwsAppName.Report.getValue();
            this.appVersion = MwsAppVersion.Report.getValue();

            this.serviceURL = MwsServiceUrl.EU_Report.getValue();
        }else if( marketplace == Marketplace.AMAZON_IT){

            this.sellerId = MwsSellerId.EU.getValue();

            //todo arhtur , different report have to setup different way ?
            this.marketplaceIdList =
                    new IdList().withId(MwsMarketplaceId.IT.getValue());


            this.accessKey = MwsAccessKey.EU.getValue();
            this.secretKey = MwsSecretKey.EU.getValue();


            this.appName = MwsAppName.Report.getValue();
            this.appVersion = MwsAppVersion.Report.getValue();

            this.serviceURL = MwsServiceUrl.EU_Report.getValue();
        }else if(marketplace == Marketplace.AMAZON_ES ){

            this.sellerId = MwsSellerId.EU.getValue();

            //todo arhtur , different report have to setup different way ?
            this.marketplaceIdList =
                    new IdList().withId(MwsMarketplaceId.ES.getValue());


            this.accessKey = MwsAccessKey.EU.getValue();
            this.secretKey = MwsSecretKey.EU.getValue();


            this.appName = MwsAppName.Report.getValue();
            this.appVersion = MwsAppVersion.Report.getValue();

            this.serviceURL = MwsServiceUrl.EU_Report.getValue();
        }

    }



    @Override
    public synchronized MarketplaceWebServiceClient getClient() {

        MarketplaceWebServiceConfig config = new MarketplaceWebServiceConfig();

        System.out.println(this.serviceURL);
        config.setServiceURL(this.serviceURL);

        MarketplaceWebServiceClient client = new MarketplaceWebServiceClient(
                accessKey, secretKey, appName,
                appVersion, config);


        return client;
    }

}
