package com.kindminds.drs.service.schedule.job;

import com.amazonservices.mws.client.MwsUtl;
import com.kindminds.drs.Country;
import com.kindminds.drs.api.adapter.AmazonMwsOrderAdapter;
import com.kindminds.drs.api.schedule.job.ImportAmazonOrderUco;
import com.kindminds.drs.api.v1.model.amazon.AmazonOrder;
import com.kindminds.drs.api.data.access.usecase.report.amazon.ImportAmazonOrderDao;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ImportAmazonOrderUcoImpl implements ImportAmazonOrderUco {
	
	@Autowired private AmazonMwsOrderAdapter amzMwsAdapter;
	@Autowired private ImportAmazonOrderDao dao;

	private final int bufferMins = 5;
	private Country country = null;
	private boolean isProcessingFlag = false;
	
	private void setIsProcessing(boolean value) {this.isProcessingFlag=value;}
	private boolean isProcessing() {return this.isProcessingFlag;}

	public void setCountry(Country country) {this.country= country;}
	private final  Integer maxRequestDurationHours = 3;
	
	@Override
	public void importOrders() {
		XMLGregorianCalendar lastUpdatedAfter = this.getLastUpdatedAfter(this.country);
		XMLGregorianCalendar lastUpdatedBefore = this.getLastUpdatedBefore(lastUpdatedAfter);

		System.out.println(lastUpdatedAfter);
		System.out.println(lastUpdatedBefore);

		Assert.isTrue(lastUpdatedBefore.compare(lastUpdatedAfter)>0);
		this.importOrder(this.country, lastUpdatedAfter, lastUpdatedBefore);
	}
	
	public void importOrderForTest(XMLGregorianCalendar lastUpdatedAfter,XMLGregorianCalendar lastUpdatedBefore) {
		this.importOrder(Country.NA, lastUpdatedAfter, lastUpdatedBefore);
	}
	
	@Transactional("transactionManager")
	private void importOrder(Country country, XMLGregorianCalendar lastUpdatedAfter,XMLGregorianCalendar lastUpdatedBefore) {
		try{
			if(this.isProcessing()) {
				this.dao.setImportingStatus(country,"busy");
			} else {
				this.setIsProcessing(true);
				List<AmazonOrder> orders = this.amzMwsAdapter.requestOrders(country,lastUpdatedAfter, lastUpdatedBefore);
				this.dao.insertOrUpdateOrders(orders);
				this.dao.updateLastUpdateDate(country,lastUpdatedBefore);
				this.setIsProcessing(false);
				this.dao.setImportingStatus(country,"Last Request OK");
			}
		} catch (Exception e){
			System.out.println(e.getMessage());
			this.setIsProcessing(false);
			this.dao.setImportingStatus(country,"exception");
		}
	}

	@Override
	public List<AmazonOrder> getOrdersFromDatabase(Date lastUpdatedAfter,Date lastUpdatedBefore) {
		return this.dao.selectOrders(lastUpdatedAfter, lastUpdatedBefore);
	}
	
	private XMLGregorianCalendar getLastUpdatedAfter(Country country){
		XMLGregorianCalendar lastUpdatedAfter = this.dao.queryLastUpdateDateUtc(country);
		if(lastUpdatedAfter==null) lastUpdatedAfter = this.dao.queryInitialDateUtc(country);
		return lastUpdatedAfter;
	}
	
	private XMLGregorianCalendar getLastUpdatedBefore(XMLGregorianCalendar lastUpdatedAfter){
		XMLGregorianCalendar defaultDrsLastUpdatedBefore = this.getStandardLastUpdatedBefore();
		long durationHours = this.getDurationHours(lastUpdatedAfter, defaultDrsLastUpdatedBefore);
		if(durationHours>=this.maxRequestDurationHours){
			return this.getTimeAfterDuration(lastUpdatedAfter,0,this.maxRequestDurationHours);
		}
		return defaultDrsLastUpdatedBefore;
	}
	
	private long getDurationHours(XMLGregorianCalendar a,XMLGregorianCalendar b){
		long totalMilliSecond = b.toGregorianCalendar().getTimeInMillis()-a.toGregorianCalendar().getTimeInMillis();
		return TimeUnit.MILLISECONDS.toHours(totalMilliSecond);
	}
	
	private XMLGregorianCalendar getTimeAfterDuration(XMLGregorianCalendar base,int days,int hours){
		XMLGregorianCalendar valueToReturn = (XMLGregorianCalendar)base.clone();
		valueToReturn.add(MwsUtl.getDTF().newDuration(true,0,0,days,hours,0,0));
		return valueToReturn;
	}
	
	private XMLGregorianCalendar getStandardLastUpdatedBefore(){
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		XMLGregorianCalendar now = MwsUtl.getDTF().newXMLGregorianCalendar(gregorianCalendar);
		now.setSecond(0);
		now.setMillisecond(0);
		Duration d = MwsUtl.getDTF().newDuration(false, 0, 0, 0, 0,this.bufferMins,0);
		now.add(d);
		return now;
	}
	
}
