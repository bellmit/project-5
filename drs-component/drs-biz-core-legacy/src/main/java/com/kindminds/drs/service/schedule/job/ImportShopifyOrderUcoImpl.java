package com.kindminds.drs.service.schedule.job;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.adapter.ShopifyAdapter;
import com.kindminds.drs.api.adapter.ShopifyAdapter.ShopifyOrderStatus;

import com.kindminds.drs.api.adapter.ShopifyConstants;
import com.kindminds.drs.api.usecase.report.shopify.ImportShopifyOrderUco;
import com.kindminds.drs.api.v1.model.shopify.ShopifyOrder;
import com.kindminds.drs.api.data.access.usecase.report.shopify.ImportShopifyOrderDao;
import com.kindminds.drs.util.DateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ImportShopifyOrderUcoImpl implements ImportShopifyOrderUco {
	
	@Autowired private ShopifyAdapter shopifyAdapter;
	@Autowired private ImportShopifyOrderDao dao;
	
	private final int bufferMins = 5;
	private final int maxRequestDurationHours = 168;
	private boolean isProcessingFlag = false;
	
	private void setIsProcessing(boolean value) {this.isProcessingFlag=value;}
	private boolean isProcessing() {return this.isProcessingFlag;}
	
	@Override
	public void saveTestOrder() {
		ShopifyOrder order = this.shopifyAdapter.getTestOrder();
		this.dao.insert(order);
	}
	
	@Override
	public void updateTestOrder(){
		ShopifyOrder order = this.shopifyAdapter.getTestOrder();
		this.dao.update(order);
	}

	@Override
	public void importOrders() {
		Marketplace marketplace = Marketplace.TRUETOSOURCE;
		String serviceName = "order";
		Date updatedAtMin = this.getUpdatedAtMin(marketplace,serviceName);
		Date updatedAtMax = this.getUpdatedAtMax((Date)updatedAtMin.clone());
		this.importOrders(updatedAtMin,updatedAtMax,marketplace,serviceName);
	}
	
	private void importOrders(Date updatedAtMin,Date updatedAtMax,Marketplace marketplace,String serviceName){
		if(this.isProcessing()) this.dao.setImportingStatus(marketplace, serviceName,"BUSY");
		else{
			this.setIsProcessing(true);
			String updatedAtMinFormattedStr = DateHelper.toString(updatedAtMin, ShopifyConstants.dateFormat);
			String updatedAtMaxFormattedStr = DateHelper.toString(updatedAtMax,ShopifyConstants.dateFormat);
			try {
				List<ShopifyOrder> orders = this.shopifyAdapter.requestOrders(updatedAtMinFormattedStr, updatedAtMaxFormattedStr, ShopifyOrderStatus.ANY);
				this.saveOrUpdateOrders(orders);
				this.dao.updateLastUpdateDate(marketplace, serviceName,updatedAtMax);
				this.dao.setImportingStatus(marketplace, serviceName, "LAST REQUEST OK");
			}
			catch (Exception e) {
				e.printStackTrace();
				this.dao.setImportingStatus(marketplace, serviceName, "EXCEPTION");
			}
			this.setIsProcessing(false);
		}
	}
	
	private void saveOrUpdateOrders(List<ShopifyOrder> orders){
		for(ShopifyOrder order:orders){
			if(this.dao.isExist(order.getOrderId())){
				this.dao.update(order);
			}else{
				this.dao.insert(order);
			}
		}
	}
	
	private Date getUpdatedAtMin(Marketplace marketplace,String serviceName){
		Date updatedAtMin = this.dao.queryLastUpdateDate(marketplace,serviceName);
		if(updatedAtMin==null) updatedAtMin = this.dao.queryInitialDate(marketplace,serviceName);
		return updatedAtMin;
	}
	
	private Date getUpdatedAtMax(Date updatedAtMin){
		Date defaultUpdatedAtMax = this.getMinutesBeforeNow(this.bufferMins);
		long durationHours = this.getDurationHours(updatedAtMin, defaultUpdatedAtMax);
		//todo Ralph temporary disable
//		if(durationHours>=this.maxRequestDurationHours){
//			return this.getHoursAfter(updatedAtMin,this.maxRequestDurationHours);
//		}
		return defaultUpdatedAtMax;
	}
	
	private Date getMinutesBeforeNow(int minutes){
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MINUTE, -minutes);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		Date result = new Date();
		result.setTime(c.getTime().getTime());
		return result;
	}
	
	private long getDurationHours(Date a,Date b){
		long totalMilliSecond = b.getTime()-a.getTime();
		return TimeUnit.MILLISECONDS.toHours(totalMilliSecond);
	}
	
	private Date getHoursAfter(Date base,int hours){
		Calendar c = Calendar.getInstance();
		c.setTime(base);
		c.add(Calendar.HOUR,hours);
		base.setTime(c.getTime().getTime());
		return base;
	}

}
