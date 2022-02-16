package com.kindminds.drs.adapter.amazon.config.temp;

import com.amazonservices.mws.FulfillmentInventory._2010_10_01.FBAInventoryServiceMWSAsyncClient;
import com.amazonservices.mws.products.MarketplaceWebServiceProductsAsyncClient;
import com.amazonservices.mws.products.MarketplaceWebServiceProductsClient;
import com.amazonservices.mws.products.MarketplaceWebServiceProductsConfig;

import java.util.concurrent.ExecutorService;

public  class AmazonMwsProductConfig {

	private final String accessKey = "AKIAIFCJMHSMRJ6UJLDA";
	private final String secretKey = "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";
	private final String serviceURL = "https://mws.amazonservices.com";


	private static MarketplaceWebServiceProductsAsyncClient client = null;

	public AmazonMwsProductConfig() {
	}

	public static MarketplaceWebServiceProductsClient getClient() {
		return getAsyncClient();
	}

	public static synchronized MarketplaceWebServiceProductsAsyncClient getAsyncClient() {
		if (client == null) {

			MarketplaceWebServiceProductsConfig config = new MarketplaceWebServiceProductsConfig();
			config.setServiceURL("https://mws.amazonservices.com");

			client = new MarketplaceWebServiceProductsAsyncClient("AKIAIFCJMHSMRJ6UJLDA",
					"4zgXhIMuglBKI5ddr2q/wmtumbqPH836ZdOgsUYQ", "access.drs.network",
					"20170523", config, (ExecutorService)null);


		}

		System.out.println(client);

		return client;
	}

}
