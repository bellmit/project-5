package com.kindminds.drs.adapter.amazon.temp;


import com.amazonservices.mws.products.MarketplaceWebServiceProducts;
import com.amazonservices.mws.products.MarketplaceWebServiceProductsClient;
import com.amazonservices.mws.products.MarketplaceWebServiceProductsException;
import com.amazonservices.mws.products.model.ASINListType;
import com.amazonservices.mws.products.model.GetMatchingProductRequest;
import com.amazonservices.mws.products.model.GetMatchingProductResponse;
import com.amazonservices.mws.products.model.ResponseHeaderMetadata;
import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.adapter.AmazonMwsProductAdapter;
import com.kindminds.drs.adapter.amazon.config.temp.AmazonMwsProductConfig;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AmazonMwsProductAdapterImpl implements AmazonMwsProductAdapter {



	enum InventorySupplyDetailType{
		INSTOCK("InStock"),
		INBOUND("Inbound"),
		TRANSFER("Transfer");
		private String value;
		static private final Map<String, InventorySupplyDetailType> valueToTypeMap = new HashMap<>();
		static { for(InventorySupplyDetailType t: InventorySupplyDetailType.values()) valueToTypeMap.put(t.value,t);};
		InventorySupplyDetailType(String value) { this.value=value; }
		public static InventorySupplyDetailType fromValue(String value){
			return valueToTypeMap.get(value);
		}
	}
	
	enum ProductMarketplace {
		US(Marketplace.AMAZON_COM,  "amazonMwsProductConfigNaUs"),
		UK(Marketplace.AMAZON_CO_UK,"amazonMwsProductConfigUk"),
		CA(Marketplace.AMAZON_CA,   "amazonMwsProductConfigNaCa");

		private Marketplace marketplace;
		private String configBeanId;

		static private final Map<Marketplace,ProductMarketplace>
				marketplaceToProductMap = new HashMap<>();

		static { for(ProductMarketplace m: ProductMarketplace.values())
			marketplaceToProductMap.put(m.getMarketplace(),m); }
		ProductMarketplace(Marketplace marketplace,String configBeanId) {

			this.marketplace = marketplace;
			this.configBeanId = configBeanId;
		}

		public Marketplace getMarketplace() {return marketplace;}
		public String getConfigBeanId() {return configBeanId;}
		public static ProductMarketplace fromMarketplace(Marketplace marketplace){
			ProductMarketplace prodMarketplace = marketplaceToProductMap.get(marketplace);
			Assert.notNull(prodMarketplace);
			return prodMarketplace;
		}

	}

	/*
	@Autowired private ApplicationContext appContext;
	
	private AmazonMwsProductConfig getConfig(Marketplace marketplace){
		String configBeanId = ProductMarketplace.fromMarketplace(marketplace).getConfigBeanId();
		return (AmazonMwsProductConfig)this.appContext.getBean(configBeanId);
	}*/


	@Override
	public void requestOrders() {
		//AmazonMwsProductConfig config = this.getConfig(Marketplace.AMAZON_COM);


		// Get a client connection.
		// Make sure you've set the variables in MarketplaceWebServiceProductsSampleConfig.
		MarketplaceWebServiceProductsClient client = AmazonMwsProductConfig.getClient();


		// Create a request.
		GetMatchingProductRequest request = new GetMatchingProductRequest();
		String sellerId = "A3QY80NORGXT27";
		request.setSellerId(sellerId);


		//String mwsAuthToken = "example";
		//request.setMWSAuthToken(mwsAuthToken);

		String marketplaceId = "ATVPDKIKX0DER";
		request.setMarketplaceId(marketplaceId);
		ASINListType asinList = new ASINListType();
		List<String> aList = new ArrayList<String>();
		aList.add("B07PCMQQ7L");
		aList.add("B00E9IO3HA");
		asinList.setASIN(aList);


		request.setASINList(asinList);

		// Make the call.
		this.invokeGetMatchingProduct(client, request);

	}


	public void invokeGetMatchingProduct(
			MarketplaceWebServiceProducts client,
			GetMatchingProductRequest request) {
		try {
			// Call the service.

			System.out.println("AAAAAAAAA");

			GetMatchingProductResponse response = client.getMatchingProduct(request);
			System.out.println("AAAAAAAAA");

			ResponseHeaderMetadata rhmd = response.getResponseHeaderMetadata();
			// We recommend logging every the request id and timestamp of every call.
			System.out.println("Response:");
			System.out.println("RequestId: "+rhmd.getRequestId());
			System.out.println("Timestamp: "+rhmd.getTimestamp());
			String responseXml = response.toXML();
			System.out.println(responseXml);

		} catch (MarketplaceWebServiceProductsException ex) {
			// Exception properties are important for diagnostics.
			System.out.println("Service Exception:");
			ResponseHeaderMetadata rhmd = ex.getResponseHeaderMetadata();
			if(rhmd != null) {
				System.out.println("RequestId: "+rhmd.getRequestId());
				System.out.println("Timestamp: "+rhmd.getTimestamp());
			}
			System.out.println("Message: "+ex.getMessage());
			System.out.println("StatusCode: "+ex.getStatusCode());
			System.out.println("ErrorCode: "+ex.getErrorCode());
			System.out.println("ErrorType: "+ex.getErrorType());
			throw ex;
		}
	}


}
