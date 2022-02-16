package com.kindminds.drs.adapter.amazon.config;

import com.amazonaws.mws.model.IdList;
import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersAsyncClient;
import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersConfig;
import com.kindminds.drs.Country;
import com.kindminds.drs.Marketplace;

import java.util.Arrays;
import java.util.List;


public class AmazonMwsOrderConfig
        implements MwsConfig<MarketplaceWebServiceOrdersAsyncClient>
{

    private  String sellerId = "";

    private  IdList marketplaceIdList = null;
    private  List<String> marketplaceIds = null;

    private  String accessKey = "";
    private  String secretKey = "";


    private  String appName = "";
    private  String appVersion = "";
    private  String serviceURL = "";

    private  MarketplaceWebServiceOrdersAsyncClient client = null;

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
        return  marketplaceIds;
    }

    @Override
    public IdList getMarketplaceIdList() {
        return this.marketplaceIdList;
    }

    public AmazonMwsOrderConfig(Country country){

        if(country == Country.NA){

            this.sellerId = MwsSellerId.NA.getValue();
            this.marketplaceIdList =
                    new IdList().withId(MwsMarketplaceId.US.getValue(),MwsMarketplaceId.CA.getValue());

            this.marketplaceIds = Arrays.asList(MwsMarketplaceId.US.getValue(),MwsMarketplaceId.CA.getValue());

            this.accessKey = MwsAccessKey.NA.getValue();
            this.secretKey = MwsSecretKey.NA.getValue();
            this.appName = MwsAppName.NA_Order.getValue();
            this.appVersion = MwsAppVersion.NA_Order.getValue();
            this.serviceURL = MwsServiceUrl.NA_Order.getValue();

        } else if(country == Country.EU){

            this.sellerId = MwsSellerId.EU.getValue();

            this.marketplaceIdList =
                    new IdList().withId(MwsMarketplaceId.UK.getValue(),MwsMarketplaceId.DE.getValue(),
                            MwsMarketplaceId.FR.getValue(),
                            MwsMarketplaceId.IT.getValue(),MwsMarketplaceId.ES.getValue());

            this.marketplaceIds = Arrays.asList(MwsMarketplaceId.UK.getValue(),MwsMarketplaceId.DE.getValue(),
                    MwsMarketplaceId.FR.getValue(),
                    MwsMarketplaceId.IT.getValue(),MwsMarketplaceId.ES.getValue());

            this.accessKey = MwsAccessKey.EU.getValue();
            this.secretKey = MwsSecretKey.EU.getValue();


            this.appName = MwsAppName.EU_Order.getValue();
            this.appVersion = MwsAppVersion.EU_Order.getValue();
            this.serviceURL = MwsServiceUrl.EU_Order.getValue();
        }

    }



    @Override
    public MarketplaceWebServiceOrdersAsyncClient getClient() {
        return getAsyncClient();
    }

    public  synchronized MarketplaceWebServiceOrdersAsyncClient getAsyncClient() {
        if (client==null) {
            MarketplaceWebServiceOrdersConfig config = new MarketplaceWebServiceOrdersConfig();
            config.setServiceURL(serviceURL);
            // Set other client connection configurations here.
            client = new MarketplaceWebServiceOrdersAsyncClient(accessKey, secretKey,
                    appName, appVersion, config, null);
        }
        return client;
    }

}
