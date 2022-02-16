package com.kindminds.drs.adapter.amazon.config;

import com.amazon.mws.finances._2015_05_01.MWSFinancesServiceAsync;
import com.amazon.mws.finances._2015_05_01.MWSFinancesServiceAsyncClient;
import com.amazon.mws.finances._2015_05_01.MWSFinancesServiceConfig;
import com.amazonaws.mws.MarketplaceWebServiceClient;
import com.amazonaws.mws.MarketplaceWebServiceConfig;
import com.amazonaws.mws.model.IdList;
import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersAsyncClient;
import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersConfig;
import com.kindminds.drs.Country;
import com.kindminds.drs.Marketplace;

import java.util.Arrays;
import java.util.List;


public class AmazonMwsFinancesConfig
        implements MwsConfig<MWSFinancesServiceAsyncClient>
{

    private  String sellerId = "";

    private  IdList marketplaceIdList = null;

    private  String accessKey = "";
    private  String secretKey = "";


    private  String appName = "";
    private  String appVersion = "";
    private  String serviceURL = "";

    private  MWSFinancesServiceAsyncClient client = null;

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



    public AmazonMwsFinancesConfig(Marketplace marketplace){


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

        } else if(marketplace == Marketplace.AMAZON_CO_UK || marketplace == Marketplace.AMAZON_DE ||
                marketplace == Marketplace.AMAZON_FR || marketplace == Marketplace.AMAZON_IT ||
                marketplace == Marketplace.AMAZON_ES  ){

            this.sellerId = MwsSellerId.EU.getValue();

            //todo arhtur , different report have to setup different way ?
            this.marketplaceIdList =
                    new IdList().withId(MwsMarketplaceId.UK.getValue(),MwsMarketplaceId.DE.getValue(),
                            MwsMarketplaceId.FR.getValue(),
                            MwsMarketplaceId.IT.getValue(),MwsMarketplaceId.ES.getValue());


            this.accessKey = MwsAccessKey.EU.getValue();
            this.secretKey = MwsSecretKey.EU.getValue();


            this.appName = MwsAppName.Report.getValue();
            this.appVersion = MwsAppVersion.Report.getValue();

            this.serviceURL = MwsServiceUrl.EU_Report.getValue();
        }

    }



    @Override
    public synchronized MWSFinancesServiceAsyncClient getClient() {

        MWSFinancesServiceConfig config = new MWSFinancesServiceConfig();


        System.out.println(this.serviceURL);
        config.setServiceURL(this.serviceURL);

        if (client==null) {

            config.setServiceURL(serviceURL);
            // Set other client connection configurations here.
            client = new MWSFinancesServiceAsyncClient(accessKey, secretKey,
                    appName, appVersion, config, null);


            config.setServiceURL(this.serviceURL);


        }



        return client;
    }

}
