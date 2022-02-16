package com.kindminds.drs.adapter.amazon.config;

import com.amazonaws.mws.model.IdList;
import com.amazonservices.mws.FulfillmentOutboundShipment._2010_10_01.FBAOutboundServiceMWSAsyncClient;
import com.amazonservices.mws.FulfillmentOutboundShipment._2010_10_01.FBAOutboundServiceMWSClient;
import com.amazonservices.mws.FulfillmentOutboundShipment._2010_10_01.FBAOutboundServiceMWSConfig;
import com.kindminds.drs.Marketplace;

import java.util.List;

public class AmazonMwsFulfillmentOutboundShipmentConfig
		implements  MwsConfig<FBAOutboundServiceMWSClient>{



	private  String sellerId = "";

	private  String marketplaceId = "";
	private  IdList marketplaceIdList = null;

	private  String accessKey = "";
	private  String secretKey = "";


	private  String appName = "";
	private  String appVersion = "";
	private  String serviceURL = "";


	@Override
	public String getSellerId() {
		return this.sellerId;
	}

	@Override
	public String getMarketplaceId() {
		return this.marketplaceId;
	}

	@Override
	public List<String> getMarketplaceIds() {
		return null;
	}

	@Override
	public IdList getMarketplaceIdList() {
		return this.marketplaceIdList;
	}

	public  AmazonMwsFulfillmentOutboundShipmentConfig(Marketplace marketplace){

		this.appName = MwsAppName.FulfillmentOutboundShipment.getValue();
		this.appVersion = MwsAppVersion.FulfillmentOutboundShipment.getValue();

		if(marketplace == Marketplace.AMAZON_COM || marketplace == Marketplace.AMAZON_CA){

			this.sellerId = MwsSellerId.NA.getValue();
			this.accessKey = MwsAccessKey.NA.getValue();
			this.secretKey = MwsSecretKey.NA.getValue();

			this.serviceURL = MwsServiceUrl.NA_FulfillmentOutboundShipment.getValue();

			this.marketplaceId = marketplace == Marketplace.AMAZON_COM ?
					MwsMarketplaceId.US.getValue() : MwsMarketplaceId.CA.getValue();

		} else if(marketplace == Marketplace.AMAZON_CO_UK){

			this.sellerId = MwsSellerId.UK.getValue();

			this.marketplaceId = MwsMarketplaceId.UK.getValue();

			this.accessKey = MwsAccessKey.UK.getValue();
			this.secretKey = MwsSecretKey.EU.getValue();

			this.serviceURL = MwsServiceUrl.UK_FulfillmentOutboundShipment.getValue();

		}


	}


	private  FBAOutboundServiceMWSAsyncClient client = null;

	@Override
	public synchronized FBAOutboundServiceMWSClient getClient(){
		if(client==null) {
			FBAOutboundServiceMWSConfig config = new FBAOutboundServiceMWSConfig();
			config.setServiceURL(this.serviceURL);
			client = new FBAOutboundServiceMWSAsyncClient(accessKey,secretKey,appName, appVersion, config, null);
		}
		return client;
	}
}
