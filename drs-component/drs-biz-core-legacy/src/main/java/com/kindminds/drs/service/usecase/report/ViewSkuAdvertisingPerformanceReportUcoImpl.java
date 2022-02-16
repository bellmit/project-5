package com.kindminds.drs.service.usecase.report;

import com.kindminds.drs.Context;
import com.kindminds.drs.Marketplace;

import com.kindminds.drs.api.v1.model.report.SkuAdvertisingPerformanceReportLineItem;
import com.kindminds.drs.api.usecase.ViewSkuAdvertisingPerformanceReportUco;
import com.kindminds.drs.api.v1.model.report.SkuAdvertisingPerformanceReport;
import com.kindminds.drs.api.data.access.rdb.CompanyDao;
import com.kindminds.drs.api.data.access.usecase.report.ViewSkuAdvertisingPerformanceReportDao;
import com.kindminds.drs.api.v2.biz.domain.model.MarketingReportHelper;
import com.kindminds.drs.v1.model.impl.SkuAdvertisingPerformanceReportImpl;
import com.kindminds.drs.util.DateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class ViewSkuAdvertisingPerformanceReportUcoImpl implements ViewSkuAdvertisingPerformanceReportUco {
	
	@Autowired private ViewSkuAdvertisingPerformanceReportDao dao;
	@Autowired private MarketingReportHelper marketingReportHelper;
	@Autowired private CompanyDao companyRepo;

	@Override
	public List<Marketplace> getMarketplaces(){
		return Marketplace.getAmazonMarketplaces();
	}
	
	@Override
	public boolean isDrsUser() {
		return Context.getCurrentUser().isDrsUser();
	}

	@Override
	public String getActualSupplierKcode(String requestSupplierKcode) {
		String userCompanyKcode = Context.getCurrentUser().getCompanyKcode();
		if(Context.getCurrentUser().isDrsUser()){
			if(!StringUtils.hasText(requestSupplierKcode)) return null;
			return requestSupplierKcode;
		}else return userCompanyKcode;
	}
	
	@Override
	public String getDefaultMarketplaceId(){
		return String.valueOf(Marketplace.AMAZON_COM.getKey());
	}

	@Override
	public String getDefaultUtcDateStart() {
		return this.dao.queryLatestSettlementStartUtcDate();
	}

	@Override
	public String getDefaultUtcDateEnd() {
		return this.dao.queryLatestSettlementEndUtcDate();
	}

	@Override
	public Map<String,String> getSupplierKcodeToShortEnNameMap() {
		if(Context.getCurrentUser().isDrsUser()){
			return this.companyRepo.querySupplierKcodeToShortEnUsNameMap(); 
		}
		return null;
	}

	@Override
	public SkuAdvertisingPerformanceReport getReport(String supplierKcode,String marketplaceId,
													 String utcStartDate,String utcEndDate,String campaignName) {

		if(supplierKcode==null) return null;
		Assert.isTrue(this.companyRepo.isSupplier(supplierKcode),"isSupplier(supplierKcode)");
		Marketplace marketplace = Marketplace.fromKey(Integer.parseInt(marketplaceId));
	;
		Date start = DateHelper.toDate(utcStartDate+" UTC", "yyyy-MM-dd z");
		Date end = this.getEndDate(utcEndDate);
		Map<String,List<SkuAdvertisingPerformanceReportLineItem>> campaignItems =
				this.generateItemsByCampaignName(campaignName, supplierKcode, marketplace, start, end);

		if(campaignItems==null) return null;
		SkuAdvertisingPerformanceReportImpl report = new SkuAdvertisingPerformanceReportImpl(campaignItems);
		report.setMarketplace(marketplace);
		report.setCurrency(marketplace.getCurrency());
		return report;
	}
	
	@Override
	public List<String> getCampaignNameList(String supplierKcode,String marketplaceId,String utcStartDate,String utcEndDate){
		if(supplierKcode==null) return new ArrayList<String>();

		Assert.isTrue(this.companyRepo.isSupplier(supplierKcode));
		Marketplace marketplace = Marketplace.fromKey(Integer.parseInt(marketplaceId));
		Date start = DateHelper.toDate(utcStartDate+" UTC", "yyyy-MM-dd z");
		Date end = this.getEndDate(utcEndDate);		
		return this.dao.queryCampaignNames(supplierKcode,marketplace,start,end);
	}
	
	private Map<String,List<SkuAdvertisingPerformanceReportLineItem>> generateItemsByCampaignName(String campaignName,String supplierKcode,Marketplace marketplace,Date start,Date end){
		if(campaignName==null) return null;
		Map<String,List<SkuAdvertisingPerformanceReportLineItem>> campaignItems = new TreeMap<>();
		List<SkuAdvertisingPerformanceReportLineItem> lineItem = this.marketingReportHelper.getSkuAdvertisingPerformanceReportLineItems(supplierKcode,marketplace,start,end,campaignName);
		campaignItems.put(campaignName, lineItem);
		return campaignItems;		
	}
	
	
	private Date getEndDate(String utcEndDate){
		Date endDate = DateHelper.toDate(utcEndDate+" UTC", "yyyy-MM-dd z");
		if(endDate!=null) return this.addOneDays(endDate);
		return null;
	}
	
	private Date addOneDays(Date d){
	    Calendar c = Calendar.getInstance();
	    c.setTime(d);
	    c.add(Calendar.DATE, 1);
	    d.setTime( c.getTime().getTime() );
	    return d;
	}

}
