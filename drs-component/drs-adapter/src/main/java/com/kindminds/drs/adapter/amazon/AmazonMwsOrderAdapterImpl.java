package com.kindminds.drs.adapter.amazon;

import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;


import com.amazonaws.mws.model.IdList;
import com.kindminds.drs.Country;
import com.kindminds.drs.Marketplace;
import com.kindminds.drs.adapter.amazon.config.MwsApi;
import com.kindminds.drs.adapter.amazon.config.MwsConfig;
import com.kindminds.drs.adapter.amazon.config.MwsConfigFactory;
import org.apache.log4j.Logger;

import com.amazonservices.mws.orders._2013_09_01.*;
import com.amazonservices.mws.orders._2013_09_01.model.*;
import com.kindminds.drs.api.adapter.AmazonMwsOrderAdapter;
import com.kindminds.drs.adapter.amazon.dto.AmazonOrderImpl;
import com.kindminds.drs.adapter.amazon.dto.AmazonOrderItemImpl;

import com.kindminds.drs.api.v1.model.amazon.AmazonOrder;
import com.kindminds.drs.api.v1.model.amazon.AmazonOrder.AmazonOrderItem;
import org.springframework.stereotype.Service;


@Service
public class AmazonMwsOrderAdapterImpl implements AmazonMwsOrderAdapter {
	
	//@Autowired
	//private ApplicationContext appContext;
	
	private static Logger logger = Logger.getLogger(AmazonMwsOrderAdapterImpl.class);
	
	private void logErrorAndRestoreInterruptStatus(InterruptedException e) {
		logger.info("sleep interrupted: ", e);
		Thread.currentThread().interrupt(); // Restore the interrupted status
	}
	
	@Override
	public List<AmazonOrder> requestOrders(Country country,
                                           XMLGregorianCalendar lastUpdatedAfter,
                                           XMLGregorianCalendar lastUpdatedBefore) {

	    //String configBeanId = region.getConfigBeanId();
		//MwsConfig config  = (MwsConfig)appContext.getBean(configBeanId);
        MwsConfigFactory configFactory = new MwsConfigFactory();
        MwsConfig config  = null;
            if(country == Country.NA)
                config =  (MwsConfig)configFactory.getConfig(MwsApi.Order , Marketplace.AMAZON_COM);
            else if(country == Country.EU)
                config =  (MwsConfig)configFactory.getConfig(MwsApi.Order, Marketplace.AMAZON_CO_UK);

		String sellerId = config.getSellerId();
		IdList marketplaceIdList = config.getMarketplaceIdList();
		MarketplaceWebServiceOrdersClient client = (MarketplaceWebServiceOrdersClient) config.getClient();

		ListOrdersResponse response = this.getListOrderResponse(lastUpdatedAfter,lastUpdatedBefore,sellerId,
                config.getMarketplaceIds(),client);

		List<Order> orders = response.getListOrdersResult().getOrders();
        String nextToken = response.getListOrdersResult().getNextToken();

        while(nextToken!=null){
        	try {
        		Thread.sleep(60000);
        	} catch (InterruptedException e) {
        		logErrorAndRestoreInterruptStatus(e);
        	}
        	ListOrdersByNextTokenResponse nextResponse = this.getNextOrders(nextToken,sellerId,client);
        	orders.addAll(nextResponse.getListOrdersByNextTokenResult().getOrders());
        	nextToken = nextResponse.getListOrdersByNextTokenResult().getNextToken();
        }


        List<AmazonOrder> orderListToReturn = this.toAmazonOrderDto(orders);
        this.appendOrderItems(sellerId,client,orderListToReturn);
        return orderListToReturn;
	}
	
	private List<AmazonOrder> toAmazonOrderDto(List<Order> origOrders){
		List<AmazonOrder> newOrderList = new ArrayList<AmazonOrder>();
		for(Order o:origOrders){
			newOrderList.add(new AmazonOrderImpl(o));
		}
		return newOrderList;
	}

	
	private void appendOrderItems(String sellerId, MarketplaceWebServiceOrdersClient client,List<AmazonOrder> orderList){
		for(AmazonOrder order:orderList){
			List<AmazonOrderItem> orderItems = new ArrayList<AmazonOrderItem>();
			ListOrderItemsResponse response = this.requestOrderItems(sellerId,client,order.getAmazonOrderId());
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
        		logErrorAndRestoreInterruptStatus(e);
			}
        	orderItems.addAll(this.toAmazonOrderItemDto(response.getListOrderItemsResult().getOrderItems()));
        	String nextToken = response.getListOrderItemsResult().getNextToken();
        	while(nextToken!=null){
        		ListOrderItemsByNextTokenResponse nextResponse = this.requestNextOrderItems(nextToken,sellerId,client);
        		try {
        			Thread.sleep(2000);
        		} catch (InterruptedException e) {
            		logErrorAndRestoreInterruptStatus(e);
        		}
        		orderItems.addAll(this.toAmazonOrderItemDto(nextResponse.getListOrderItemsByNextTokenResult().getOrderItems()));
        		nextToken = nextResponse.getListOrderItemsByNextTokenResult().getNextToken();
        	}
        	((AmazonOrderImpl)order).setItems(orderItems);
        }
	}
	
	private List<AmazonOrderItem> toAmazonOrderItemDto(List<OrderItem> origList){
		List<AmazonOrderItem> newItemList = new ArrayList<AmazonOrderItem>();
		for(OrderItem item:origList){
			newItemList.add(new AmazonOrderItemImpl(item));
		}
		return newItemList;
	}
	
	private ListOrdersResponse getListOrderResponse(
			XMLGregorianCalendar lastUpdatedAfter, XMLGregorianCalendar lastUpdatedBefore,
			String sellerId, List<String> marketplaceIdList, MarketplaceWebServiceOrdersClient client){

		ListOrdersRequest request = new ListOrdersRequest();
		request.setSellerId(sellerId);
		request.setLastUpdatedAfter(lastUpdatedAfter);
		request.setLastUpdatedBefore(lastUpdatedBefore);
        request.setMarketplaceId(marketplaceIdList);
        return this.invokeListOrders(client, request);
	}
	
	private ListOrdersByNextTokenResponse getNextOrders(String nextToken,String sellerId,MarketplaceWebServiceOrdersClient client){
		ListOrdersByNextTokenRequest nextRequest = new ListOrdersByNextTokenRequest(sellerId,nextToken);
		return this.invokeListOrdersByNextToken(client, nextRequest);
	}
	
	private ListOrderItemsResponse requestOrderItems(String sellerId, MarketplaceWebServiceOrdersClient client, String amazonOrderId){
    	ListOrderItemsRequest itemsRequest = new ListOrderItemsRequest(sellerId,amazonOrderId);
    	return this.invokeListOrderItems(client, itemsRequest);
	}
	private ListOrderItemsByNextTokenResponse requestNextOrderItems(String nextToken,String sellerId,MarketplaceWebServiceOrdersClient client){
		ListOrderItemsByNextTokenRequest nextItemsRequest = new ListOrderItemsByNextTokenRequest(sellerId,nextToken);
		return this.invokeListOrderItemsByNextToken(client, nextItemsRequest);
	}
	
	private ListOrdersResponse invokeListOrders(MarketplaceWebServiceOrders client,ListOrdersRequest request) {
        try {
            // Call the service.
            ListOrdersResponse response = client.listOrders(request);
            ResponseHeaderMetadata rhmd = response.getResponseHeaderMetadata();
            // We recommend logging every the request id and timestamp of every call.
            System.out.println("Response:");
            System.out.println("RequestId: "+rhmd.getRequestId());
            System.out.println("Timestamp: "+rhmd.getTimestamp());
            String responseXml = response.toXML();
            System.out.println(responseXml);
            return response;
        } catch (MarketplaceWebServiceOrdersException ex) {
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
	
	private ListOrdersByNextTokenResponse invokeListOrdersByNextToken(MarketplaceWebServiceOrders client, ListOrdersByNextTokenRequest request) {
        try {
            // Call the service.
            ListOrdersByNextTokenResponse response = client.listOrdersByNextToken(request);
            ResponseHeaderMetadata rhmd = response.getResponseHeaderMetadata();
            // We recommend logging every the request id and timestamp of every call.
            System.out.println("Response:");
            System.out.println("RequestId: "+rhmd.getRequestId());
            System.out.println("Timestamp: "+rhmd.getTimestamp());
            String responseXml = response.toXML();
            System.out.println(responseXml);
            return response;
        } catch (MarketplaceWebServiceOrdersException ex) {
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
	
	private ListOrderItemsResponse invokeListOrderItems(MarketplaceWebServiceOrders client, ListOrderItemsRequest request) {
        try {
            // Call the service.
            ListOrderItemsResponse response = client.listOrderItems(request);
            ResponseHeaderMetadata rhmd = response.getResponseHeaderMetadata();
            // We recommend logging every the request id and timestamp of every call.
            System.out.println("Response:");
            System.out.println("RequestId: "+rhmd.getRequestId());
            System.out.println("Timestamp: "+rhmd.getTimestamp());
            String responseXml = response.toXML();
            System.out.println(responseXml);
            return response;
        } catch (MarketplaceWebServiceOrdersException ex) {
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
	
	private ListOrderItemsByNextTokenResponse invokeListOrderItemsByNextToken(
            MarketplaceWebServiceOrders client, 
            ListOrderItemsByNextTokenRequest request) {
        try {
            // Call the service.
            ListOrderItemsByNextTokenResponse response = client.listOrderItemsByNextToken(request);
            ResponseHeaderMetadata rhmd = response.getResponseHeaderMetadata();
            // We recommend logging every the request id and timestamp of every call.
            System.out.println("Response:");
            System.out.println("RequestId: "+rhmd.getRequestId());
            System.out.println("Timestamp: "+rhmd.getTimestamp());
            String responseXml = response.toXML();
            System.out.println(responseXml);
            return response;
        } catch (MarketplaceWebServiceOrdersException ex) {
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
