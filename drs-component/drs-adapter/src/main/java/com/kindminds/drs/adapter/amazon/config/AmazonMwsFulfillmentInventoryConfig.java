package com.kindminds.drs.adapter.amazon.config;

import com.amazonaws.mws.model.IdList;
import com.amazonservices.mws.FulfillmentInventory._2010_10_01.FBAInventoryServiceMWSAsyncClient;
import com.amazonservices.mws.FulfillmentInventory._2010_10_01.FBAInventoryServiceMWSConfig;
import com.kindminds.drs.Marketplace;

import java.util.Arrays;
import java.util.List;

public class AmazonMwsFulfillmentInventoryConfig
		implements  MwsConfig<FBAInventoryServiceMWSAsyncClient>{


	public String getResponseGroup(){ return "Detailed"; };

	private  String sellerId = "";

	private  String accessKey = "";
	private  String secretKey = "";


	private  String appName = "";
	private  String appVersion = "";
	private  String serviceURL = "";

	private  String marketplaceId = "";
	private static IdList marketplaceIdList = null;


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
	public IdList  getMarketplaceIdList() {
		return this.marketplaceIdList;
	}


	public AmazonMwsFulfillmentInventoryConfig(Marketplace marketplace) {

		this.appName = MwsAppName.FulfillmentInventory.getValue();
		this.appVersion = MwsAppVersion.FulfillmentInventory.getValue();

		if (marketplace == Marketplace.AMAZON_COM || marketplace == Marketplace.AMAZON_CA) {

			this.sellerId = MwsSellerId.NA.getValue();
			this.accessKey = MwsAccessKey.NA.getValue();
			this.secretKey = MwsSecretKey.NA.getValue();

			this.serviceURL = MwsServiceUrl.NA_FulfillmentInventory.getValue();

			this.marketplaceId = marketplace == Marketplace.AMAZON_COM ?
					MwsMarketplaceId.US.getValue() : MwsMarketplaceId.CA.getValue();

		} else if (marketplace == Marketplace.AMAZON_CO_UK) {

			this.sellerId = MwsSellerId.UK.getValue();

			this.marketplaceId = MwsMarketplaceId.UK.getValue();

			this.accessKey = MwsAccessKey.UK.getValue();
			this.secretKey = MwsSecretKey.UK.getValue();

			this.serviceURL = MwsServiceUrl.UK_FulfillmentInventory.getValue();

		} else if (marketplace == Marketplace.AMAZON_DE || marketplace == Marketplace.AMAZON_FR || marketplace == Marketplace.AMAZON_IT || marketplace == Marketplace.AMAZON_ES) {

			this.sellerId = MwsSellerId.EU.getValue();

			if (marketplace == Marketplace.AMAZON_DE) {
				this.marketplaceId = MwsMarketplaceId.DE.getValue();
			} else if (marketplace == Marketplace.AMAZON_FR) {
				this.marketplaceId = MwsMarketplaceId.FR.getValue();
			} else if (marketplace == Marketplace.AMAZON_IT) {
				this.marketplaceId = MwsMarketplaceId.IT.getValue();
			} else if (marketplace == Marketplace.AMAZON_ES) {
				this.marketplaceId = MwsMarketplaceId.ES.getValue();
			}

			this.accessKey = MwsAccessKey.EU.getValue();
			this.secretKey = MwsSecretKey.EU.getValue();

			this.serviceURL = MwsServiceUrl.UK_FulfillmentInventory.getValue();


		}
	}


	private  FBAInventoryServiceMWSAsyncClient client = null;

	@Override
	public synchronized FBAInventoryServiceMWSAsyncClient getClient() {
		if (client==null) {
			FBAInventoryServiceMWSConfig config = new FBAInventoryServiceMWSConfig();
			config.setServiceURL(serviceURL);
			// Set other client connection configurations here.
			client = new FBAInventoryServiceMWSAsyncClient(accessKey,secretKey,appName,appVersion,config,null);
		}
		return client;
	}









}
