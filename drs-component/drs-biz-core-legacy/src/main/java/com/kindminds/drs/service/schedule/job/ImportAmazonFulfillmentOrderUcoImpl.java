package com.kindminds.drs.service.schedule.job;

import com.kindminds.drs.api.adapter.AmazonMwsFulfillmentOutboundShipmentAdapter;
import com.kindminds.drs.api.usecase.report.amazon.ImportAmazonFulfillmentOrderUco;
import com.kindminds.drs.api.v1.model.amazon.AmazonOrder;
import com.kindminds.drs.api.data.access.usecase.report.amazon.ImportAmazonFulfillmentOrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ImportAmazonFulfillmentOrderUcoImpl implements ImportAmazonFulfillmentOrderUco {

	@Autowired private AmazonMwsFulfillmentOutboundShipmentAdapter adapter;
	@Autowired private ImportAmazonFulfillmentOrderDao dao;
	
	private boolean isProcessingFlag = false;
	private String region = null;
	
	private boolean isProcessing() {return this.isProcessingFlag;}
	private void setIsProcessing(boolean value) {this.isProcessingFlag=value;}
	public void setRegion(String region) {this.region = region;}
	
	@Override
	public void importOrders() {
		if(this.isProcessing()) {
			this.dao.setImportingStatus(this.region,"BUSY");
		}
		else{
			try {
				this.setIsProcessing(true);
				Date queryStartDate = this.getQueryStartDate();
				List<AmazonOrder> orders = this.adapter.requestOrders(this.region,queryStartDate);
				this.dao.updateLastUpdateDate(this.region,this.getLastUpdateDate());
				this.saveOrUpdateOrders(orders);
				this.dao.setImportingStatus(this.region,"LAST REQUEST OK");
			} catch (Exception e) {
				this.dao.setImportingStatus(this.region,"EXCEPTION");
				e.printStackTrace();
			}
			this.setIsProcessing(false);
		}
	}
	
	private Date getQueryStartDate() {
		Date queryStartDate = this.dao.queryLastUpdateDate(this.region);
		if(queryStartDate==null) queryStartDate = this.dao.queryInitialDate(this.region);
		return queryStartDate;
	}
	
	private void saveOrUpdateOrders(List<AmazonOrder> orders) {
		for(AmazonOrder order:orders){
			if(this.dao.isOrderExist(order.getAmazonOrderId())){
				if(this.updatable(order.getAmazonOrderId())){
					this.dao.update(order);
				}
			}
			else{
				this.dao.insert(order);
			}
		}
	}
	
	private boolean updatable(String amazonOrderId) {
		return this.dao.isSalesChannelNull(amazonOrderId);
	}
	
	private Date getLastUpdateDate(){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE,-10);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND,0);
		return calendar.getTime();
	}

}
