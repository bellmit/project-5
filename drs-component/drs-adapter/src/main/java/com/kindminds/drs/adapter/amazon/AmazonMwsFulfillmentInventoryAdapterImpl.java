package com.kindminds.drs.adapter.amazon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.XMLGregorianCalendar;

import com.kindminds.drs.adapter.amazon.config.MwsApi;
import com.kindminds.drs.adapter.amazon.config.MwsConfigFactory;
import org.springframework.stereotype.Service;

import com.amazonservices.mws.FulfillmentInventory._2010_10_01.*;
import com.amazonservices.mws.FulfillmentInventory._2010_10_01.model.*;
import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.adapter.AmazonMwsFulfillmentInventoryAdapter;
import com.kindminds.drs.adapter.amazon.config.AmazonMwsFulfillmentInventoryConfig;
import com.kindminds.drs.adapter.amazon.dto.AmazonSkuInventorySupplyImpl;
import com.kindminds.drs.api.v1.model.amazon.AmazonSkuInventorySupply;

@Service
public class AmazonMwsFulfillmentInventoryAdapterImpl implements AmazonMwsFulfillmentInventoryAdapter {
	
	enum InventorySupplyDetailType{
		INSTOCK("InStock"),
		INBOUND("Inbound"),
		TRANSFER("Transfer");
		private String value;
		static private final Map<String, InventorySupplyDetailType> valueToTypeMap = new HashMap<>();
		static { for(InventorySupplyDetailType t:InventorySupplyDetailType.values()) valueToTypeMap.put(t.value,t);};
		InventorySupplyDetailType(String value) { this.value=value; }
		public static InventorySupplyDetailType fromValue(String value){
			return valueToTypeMap.get(value);
		}
	}

	/*
	enum FulfillmentInventoryMarketplace {
		US(Marketplace.AMAZON_COM,  "amazonMwsFulfillmentInventoryConfigNaUs"),
		UK(Marketplace.AMAZON_CO_UK,"amazonMwsFulfillmentInventoryConfigUk"),
		CA(Marketplace.AMAZON_CA,   "amazonMwsFulfillmentInventoryConfigNaCa");
		private Marketplace marketplace;
		private String configBeanId;
		static private final Map<Marketplace,FulfillmentInventoryMarketplace> marketplaceToFulfillmentInventoryMap = new HashMap<>();
		static { for(FulfillmentInventoryMarketplace m:FulfillmentInventoryMarketplace.values()) marketplaceToFulfillmentInventoryMap.put(m.getMarketplace(),m); }
		FulfillmentInventoryMarketplace(Marketplace marketplace,String configBeanId) {
			this.marketplace = marketplace;
			this.configBeanId = configBeanId;
		}
		public Marketplace getMarketplace() {return marketplace;}
		public String getConfigBeanId() {return configBeanId;}
		public static FulfillmentInventoryMarketplace fromMarketplace(Marketplace marketplace){
			FulfillmentInventoryMarketplace fulfillmentInventoryMarketplace = marketplaceToFulfillmentInventoryMap.get(marketplace);
			Assert.notNull(fulfillmentInventoryMarketplace);
			return fulfillmentInventoryMarketplace;
		}
	}*/
	
	//@Autowired private ApplicationContext appContext;

	/*
	private AmazonMwsFulfillmentInventoryConfig getConfig(Marketplace marketplace){

		String configBeanId = FulfillmentInventoryMarketplace.fromMarketplace(marketplace).getConfigBeanId();
		return (AmazonMwsFulfillmentInventoryConfig)this.appContext.getBean(configBeanId);

	}*/

	@Override
	public List<AmazonSkuInventorySupply> requestListInventorySupplies(Marketplace marketplace,XMLGregorianCalendar queryStartDateTime) {

		//AmazonMwsFulfillmentInventoryConfig config = this.getConfig(marketplace);
		MwsConfigFactory configFactory = new MwsConfigFactory();
		AmazonMwsFulfillmentInventoryConfig config =
				(AmazonMwsFulfillmentInventoryConfig)configFactory.getConfig(MwsApi.FulfillmentInventory , marketplace);

		ListInventorySupplyRequest request = new ListInventorySupplyRequest();
		request.setSellerId(config.getSellerId());
		request.setMarketplaceId(config.getMarketplaceId());
		request.setQueryStartDateTime(queryStartDateTime);
		request.setResponseGroup(config.getResponseGroup());
        // Make the call.
		ListInventorySupplyResponse response = this.invokeListInventorySupply(config.getClient(), request);
		List<InventorySupply> inventorySupplies = response.getListInventorySupplyResult().getInventorySupplyList().getMember();
		String nextToken = response.getListInventorySupplyResult().getNextToken();
		while(nextToken!=null){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
        		Thread.currentThread().interrupt(); // Restore the interrupted status
			}
			ListInventorySupplyByNextTokenResponse nextResponse = this.getNextResponse(nextToken,config);
			inventorySupplies.addAll(nextResponse.getListInventorySupplyByNextTokenResult().getInventorySupplyList().getMember());
			nextToken = nextResponse.getListInventorySupplyByNextTokenResult().getNextToken();
		}
		List<AmazonSkuInventorySupply> amazonSkuInventorySupplies = this.toAmazonSkuInventorySupplies(inventorySupplies);
        return amazonSkuInventorySupplies;
	}
	
	private List<AmazonSkuInventorySupply> toAmazonSkuInventorySupplies(List<InventorySupply> inventorySupplies) {
		List<AmazonSkuInventorySupply> amazonSkuInventorySupplies = new ArrayList<>();
		for(InventorySupply inventorySupply:inventorySupplies){
			String marketplaceSku = inventorySupply.getSellerSKU();
			Integer totalSupplyQuantity = inventorySupply.getTotalSupplyQuantity();
			Integer inStockSupplyQuantity = inventorySupply.getInStockSupplyQuantity();
			Integer detailQuantityInbound=0;
			Integer detailQuantityInStock=0;
			Integer detailQuantityTransfer=0;
			for(InventorySupplyDetail detail:inventorySupply.getSupplyDetail().getMember()){
				InventorySupplyDetailType type=InventorySupplyDetailType.fromValue(detail.getSupplyType());
				if(type==null) continue;
				if(type==InventorySupplyDetailType.INBOUND){  detailQuantityInbound +=detail.getQuantity(); continue;}
				if(type==InventorySupplyDetailType.INSTOCK){  detailQuantityInStock +=detail.getQuantity(); continue;}
				if(type==InventorySupplyDetailType.TRANSFER){ detailQuantityTransfer+=detail.getQuantity(); continue;}
			}
			amazonSkuInventorySupplies.add(new AmazonSkuInventorySupplyImpl(marketplaceSku,totalSupplyQuantity,inStockSupplyQuantity,detailQuantityInStock,detailQuantityInbound,detailQuantityTransfer));
		}
		return amazonSkuInventorySupplies;
	}

	private ListInventorySupplyByNextTokenResponse getNextResponse(String nextToken, AmazonMwsFulfillmentInventoryConfig config){
		ListInventorySupplyByNextTokenRequest nextRequest = new ListInventorySupplyByNextTokenRequest();
		nextRequest.setSellerId(config.getSellerId());
		nextRequest.setMarketplace(config.getMarketplaceId());
		nextRequest.setNextToken(nextToken);
		return this.invokeListInventorySupplyByNextToken(config.getClient(),nextRequest);
	}
    public ListInventorySupplyResponse invokeListInventorySupply(FBAInventoryServiceMWS client,ListInventorySupplyRequest request) {
        try {
            // Call the service.
            return client.listInventorySupply(request);
        } catch (FBAInventoryServiceMWSException ex) {
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
    
    private ListInventorySupplyByNextTokenResponse invokeListInventorySupplyByNextToken(FBAInventoryServiceMWS client, ListInventorySupplyByNextTokenRequest request) {
        try {
            // Call the service.
            return client.listInventorySupplyByNextToken(request);
        } catch (FBAInventoryServiceMWSException ex) {
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
