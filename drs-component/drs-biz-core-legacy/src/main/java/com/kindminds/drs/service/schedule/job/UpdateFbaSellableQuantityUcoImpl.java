package com.kindminds.drs.service.schedule.job;


import com.amazonservices.mws.client.MwsUtl;
import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.adapter.AmazonMwsFulfillmentInventoryAdapter;
import com.kindminds.drs.api.usecase.inventory.UpdateFbaSellableQuantityUco;
import com.kindminds.drs.api.v1.model.amazon.AmazonSkuInventorySupply;
import com.kindminds.drs.api.data.access.usecase.inventory.UpdateFbaSellableQuantityDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Service
public class UpdateFbaSellableQuantityUcoImpl implements UpdateFbaSellableQuantityUco {
	
	@Autowired private AmazonMwsFulfillmentInventoryAdapter adapter;
	@Autowired private UpdateFbaSellableQuantityDao dao;
	
	private final String serviceName = "Fulfillment Inventory";
	
	private Marketplace marketplace = null;
	private boolean isProcessingFlag = false;
	
	private void setIsProcessing(boolean value) {this.isProcessingFlag=value;}
	private boolean isProcessing() {return this.isProcessingFlag;}
	
	public void setMarketplace(String marketplaceName) {this.marketplace = Marketplace.fromName(marketplaceName);}

	@Override
	public void update() {
		Assert.notNull(this.marketplace);
		Assert.notNull(this.serviceName);
		try{
			if(this.isProcessing()){
				this.dao.setImportingStatus(this.serviceName,this.marketplace.getName(),"Busy");
			}
			else {
				this.setIsProcessing(true);
				XMLGregorianCalendar queryStartDateTime = this.getQueryStartDateTime(this.serviceName,this.marketplace);
				List<AmazonSkuInventorySupply> amazonSkuInventorySupplies = this.adapter.requestListInventorySupplies(this.marketplace, queryStartDateTime);
				for(AmazonSkuInventorySupply supply:amazonSkuInventorySupplies){
					this.updateOrCreateAmazonSkuInventorySupply(supply);
				}
				this.dao.updateLastUpdateDate(this.serviceName,this.marketplace.getName(),this.getNowWithoutSecondsAndMilliSeconds());
				this.dao.setImportingStatus(serviceName, this.marketplace.getName(), "LAST REQUEST OK");
				this.setIsProcessing(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.setIsProcessing(false);
			this.dao.setImportingStatus(this.serviceName, this.marketplace.getName(), "Exception");
		}
	}
	
	private void updateOrCreateAmazonSkuInventorySupply(AmazonSkuInventorySupply supply){
		if(this.dao.selectExist(this.marketplace.getKey(), supply.getMarketplaceSku())){
			this.dao.updateAmazonInventorySupply(this.marketplace.getKey(),supply,this.getNowWithoutSecondsAndMilliSeconds());
		} else{
			this.dao.insertAmazonInventorySupply(this.marketplace.getKey(),supply,this.getNowWithoutSecondsAndMilliSeconds());
		}
	}
	
	private XMLGregorianCalendar getQueryStartDateTime(String serviceName,Marketplace marketplace){
		Date lastUpdateDate = this.dao.queryLastUpdateDate(serviceName,marketplace.getName());
		if(lastUpdateDate==null) lastUpdateDate = this.dao.queryInitialDate(serviceName,marketplace.getName());
		return this.toXMLGregorianCalendar(lastUpdateDate);
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
	
	private XMLGregorianCalendar getNowWithoutSecondsAndMilliSeconds(){
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		XMLGregorianCalendar now = MwsUtl.getDTF().newXMLGregorianCalendar(gregorianCalendar);
		now.setSecond(0);
		now.setMillisecond(0);
		return now;
	}

}
