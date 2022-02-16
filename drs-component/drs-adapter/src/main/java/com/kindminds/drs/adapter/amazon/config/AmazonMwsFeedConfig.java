package com.kindminds.drs.adapter.amazon.config;

import com.amazonaws.mws.MarketplaceWebServiceClient;
import com.amazonaws.mws.MarketplaceWebServiceConfig;
import com.amazonaws.mws.model.IdList;
import com.kindminds.drs.Marketplace;

import java.util.List;

public class AmazonMwsFeedConfig implements  MwsConfig<MarketplaceWebServiceClient>{

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



    public AmazonMwsFeedConfig(Marketplace marketplace){

        this.appName = MwsAppName.Feeds.getValue();
        this.appVersion = MwsAppVersion.Feeds.getValue();

        if(marketplace == Marketplace.AMAZON_COM || marketplace == Marketplace.AMAZON_CA){

            this.sellerId = MwsSellerId.NA.getValue();
            if(marketplace == Marketplace.AMAZON_COM)
                this.marketplaceIdList = new IdList().withId(MwsMarketplaceId.US.getValue());
            else
                this.marketplaceIdList = new IdList().withId(MwsMarketplaceId.CA.getValue());


            this.accessKey = MwsAccessKey.NA.getValue();
            this.secretKey = MwsSecretKey.NA.getValue();

            this.serviceURL = MwsServiceUrl.US_Feeds.getValue();

        } else if(marketplace == Marketplace.AMAZON_CO_UK || marketplace == Marketplace.AMAZON_DE ||
                marketplace == Marketplace.AMAZON_FR || marketplace == Marketplace.AMAZON_IT ||
                marketplace == Marketplace.AMAZON_ES  ){

            this.sellerId = MwsSellerId.EU.getValue();

            this.marketplaceIdList =
                    new IdList().withId(MwsMarketplaceId.UK.getValue(),MwsMarketplaceId.DE.getValue(),
                            MwsMarketplaceId.FR.getValue(),
                            MwsMarketplaceId.IT.getValue(),MwsMarketplaceId.ES.getValue());


            this.accessKey = MwsAccessKey.EU.getValue();
            this.secretKey = MwsSecretKey.EU.getValue();

            this.serviceURL = MwsServiceUrl.EU_Feeds.getValue();
        }

    }



    @Override
    public synchronized MarketplaceWebServiceClient getClient() {

        MarketplaceWebServiceConfig config = new MarketplaceWebServiceConfig();

        System.out.println(this.serviceURL);
        System.out.println(this.getSellerId());
        config.setServiceURL(this.serviceURL);

//        MarketplaceWebServiceMock to test?
        MarketplaceWebServiceClient client = new MarketplaceWebServiceClient(
                accessKey, secretKey, appName,
                appVersion, config);

        return client;
    }

}
