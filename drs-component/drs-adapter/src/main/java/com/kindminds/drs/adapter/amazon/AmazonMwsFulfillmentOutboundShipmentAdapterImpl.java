package com.kindminds.drs.adapter.amazon;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.adapter.amazon.config.MwsApi;
import com.kindminds.drs.adapter.amazon.config.MwsConfigFactory;
import org.apache.log4j.Logger;

import com.amazonservices.mws.FulfillmentOutboundShipment._2010_10_01.FBAOutboundServiceMWS;
import com.amazonservices.mws.FulfillmentOutboundShipment._2010_10_01.FBAOutboundServiceMWSException;
import com.amazonservices.mws.FulfillmentOutboundShipment._2010_10_01.model.FulfillmentOrder;
import com.amazonservices.mws.FulfillmentOutboundShipment._2010_10_01.model.FulfillmentOrderItem;
import com.amazonservices.mws.FulfillmentOutboundShipment._2010_10_01.model.GetFulfillmentOrderRequest;
import com.amazonservices.mws.FulfillmentOutboundShipment._2010_10_01.model.GetFulfillmentOrderResponse;
import com.amazonservices.mws.FulfillmentOutboundShipment._2010_10_01.model.ListAllFulfillmentOrdersByNextTokenRequest;
import com.amazonservices.mws.FulfillmentOutboundShipment._2010_10_01.model.ListAllFulfillmentOrdersByNextTokenResponse;
import com.amazonservices.mws.FulfillmentOutboundShipment._2010_10_01.model.ListAllFulfillmentOrdersRequest;
import com.amazonservices.mws.FulfillmentOutboundShipment._2010_10_01.model.ListAllFulfillmentOrdersResponse;
import com.amazonservices.mws.FulfillmentOutboundShipment._2010_10_01.model.ResponseHeaderMetadata;
import com.kindminds.drs.api.adapter.AmazonMwsFulfillmentOutboundShipmentAdapter;
import com.kindminds.drs.adapter.amazon.config.AmazonMwsFulfillmentOutboundShipmentConfig;
import com.kindminds.drs.adapter.amazon.dto.AmazonOrderImpl;
import com.kindminds.drs.adapter.amazon.dto.AmazonOrderItemImpl;
import com.kindminds.drs.api.v1.model.amazon.AmazonOrder;
import com.kindminds.drs.api.v1.model.amazon.AmazonOrder.AmazonOrderItem;
import org.springframework.stereotype.Service;

@Service
public class AmazonMwsFulfillmentOutboundShipmentAdapterImpl implements AmazonMwsFulfillmentOutboundShipmentAdapter {
	
	//@Autowired
	//private ApplicationContext appContext;
	
	private static Logger logger = Logger.getLogger(AmazonMwsFulfillmentOutboundShipmentAdapterImpl.class);

	@Override
	public List<AmazonOrder> requestOrders(String region,Date start) {

		//AmazonMwsFulfillmentOutboundShipmentConfig config = this.getConfig(region);
		MwsConfigFactory configFactory = new MwsConfigFactory();
		AmazonMwsFulfillmentOutboundShipmentConfig config  = null;
		if(region.equals("NA"))
			config = (AmazonMwsFulfillmentOutboundShipmentConfig) configFactory.getConfig(
					MwsApi.FulfillmentOutboundShipment , Marketplace.AMAZON_COM);
		if(region.equals("UK"))
			config = (AmazonMwsFulfillmentOutboundShipmentConfig)
					configFactory.getConfig(MwsApi.FulfillmentOutboundShipment , Marketplace.AMAZON_CO_UK);

		ListAllFulfillmentOrdersRequest request = this.createListAllFulfillmentOrdersRequest(config,start);

		List<FulfillmentOrder> sourceFulfillmentOrders = new ArrayList<>();

		ListAllFulfillmentOrdersResponse listAllFulfillmentOrdersResponse = this.invokeListAllFulfillmentOrders(config.getClient(),request);
		sourceFulfillmentOrders.addAll(listAllFulfillmentOrdersResponse.getListAllFulfillmentOrdersResult().getFulfillmentOrders().getMember());
		String nextToken = listAllFulfillmentOrdersResponse.getListAllFulfillmentOrdersResult().getNextToken();
		while(nextToken!=null){
			ListAllFulfillmentOrdersByNextTokenRequest nextRequest = this.createListAllFulfillmentOrdersByNextTokenRequest(config, nextToken);
			ListAllFulfillmentOrdersByNextTokenResponse nextResponse = this.invokeListAllFulfillmentOrdersByNextToken(config.getClient(), nextRequest);
			sourceFulfillmentOrders.addAll(nextResponse.getListAllFulfillmentOrdersByNextTokenResult().getFulfillmentOrders().getMember());
			nextToken = nextResponse.getListAllFulfillmentOrdersByNextTokenResult().getNextToken();
		}

		List<AmazonOrder> amazonOrders = new ArrayList<>();

		for(FulfillmentOrder sourceFulfillmentOrder:sourceFulfillmentOrders){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				logger.info("sleep interrupted", e);
				Thread.currentThread().interrupt();
			}
			GetFulfillmentOrderRequest getFulfillmentOrderRequest = this.createGetFulfillmentOrderRequest(config,sourceFulfillmentOrder.getSellerFulfillmentOrderId());
			GetFulfillmentOrderResponse getFulfillmentOrderResponse = this.invokeGetFulfillmentOrder(config.getClient(), getFulfillmentOrderRequest);
			AmazonOrderImpl order = new AmazonOrderImpl(getFulfillmentOrderResponse.getGetFulfillmentOrderResult().getFulfillmentOrder());
			order.setItems(this.toAmazonOrderItems(getFulfillmentOrderResponse.getGetFulfillmentOrderResult().getFulfillmentOrderItem().getMember()));
			amazonOrders.add(order);
		}
		return amazonOrders;
	}

	/*
	private AmazonMwsFulfillmentOutboundShipmentConfig getConfig(String region) {
		if(region.equals("NA"))
			return (AmazonMwsFulfillmentOutboundShipmentConfig)appContext.getBean("amazonMwsFulfillmentOutboundShipmentConfigNA");
		if(region.equals("UK"))
			return (AmazonMwsFulfillmentOutboundShipmentConfig)appContext.getBean("amazonMwsFulfillmentOutboundShipmentConfigUK");
		throw new IllegalArgumentException("parameter String region not valid");
	}*/
	
	private ListAllFulfillmentOrdersRequest createListAllFulfillmentOrdersRequest(AmazonMwsFulfillmentOutboundShipmentConfig config,Date start){
		ListAllFulfillmentOrdersRequest request = new ListAllFulfillmentOrdersRequest();
		request.setSellerId(config.getSellerId());
		request.setMarketplace(config.getMarketplaceId());
		request.setQueryStartDateTime(this.toXMLGregorianCalendar(start));
		return request;
	}
	
	private ListAllFulfillmentOrdersByNextTokenRequest createListAllFulfillmentOrdersByNextTokenRequest(AmazonMwsFulfillmentOutboundShipmentConfig config,String nextToken){
		ListAllFulfillmentOrdersByNextTokenRequest request = new ListAllFulfillmentOrdersByNextTokenRequest();
		request.setSellerId(config.getSellerId());
		request.setMarketplace(config.getMarketplaceId());
		request.setNextToken(nextToken);
		return request;
	}
	
	private GetFulfillmentOrderRequest createGetFulfillmentOrderRequest(AmazonMwsFulfillmentOutboundShipmentConfig config,String sellerFulfillmentOrderId){
		GetFulfillmentOrderRequest request = new GetFulfillmentOrderRequest();
		request.setSellerId(config.getSellerId());
		request.setMarketplace(config.getMarketplaceId());
		request.setSellerFulfillmentOrderId(sellerFulfillmentOrderId);
		return request;
	}
	
	private XMLGregorianCalendar toXMLGregorianCalendar(Date date){
		try {
			GregorianCalendar c = new GregorianCalendar();
			c.setTime(date);
			return DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private List<AmazonOrderItem> toAmazonOrderItems(List<FulfillmentOrderItem> fulfillmentOrderItems){
		List<AmazonOrderItem> amazonOrderItems = new ArrayList<>();
		for(FulfillmentOrderItem item:fulfillmentOrderItems){
			amazonOrderItems.add(new AmazonOrderItemImpl(item));
		}
		return amazonOrderItems;
	}
	
	public ListAllFulfillmentOrdersResponse invokeListAllFulfillmentOrders(FBAOutboundServiceMWS client, ListAllFulfillmentOrdersRequest request) {
        try {
            // Call the service.
            return client.listAllFulfillmentOrders(request);
        } catch (FBAOutboundServiceMWSException ex) {
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
	
	public ListAllFulfillmentOrdersByNextTokenResponse invokeListAllFulfillmentOrdersByNextToken(FBAOutboundServiceMWS client,ListAllFulfillmentOrdersByNextTokenRequest request) {
        try {
            // Call the service.
        	return client.listAllFulfillmentOrdersByNextToken(request);
        } catch (FBAOutboundServiceMWSException ex) {
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
	
	public GetFulfillmentOrderResponse invokeGetFulfillmentOrder(FBAOutboundServiceMWS client, GetFulfillmentOrderRequest request) {
		try {
			// Call the service.
			return client.getFulfillmentOrder(request);
		} catch (FBAOutboundServiceMWSException ex) {
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
