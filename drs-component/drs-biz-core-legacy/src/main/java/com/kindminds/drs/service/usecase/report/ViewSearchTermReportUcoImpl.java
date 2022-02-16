package com.kindminds.drs.service.usecase.report;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.usecase.ViewSearchTermReportUco;
import com.kindminds.drs.api.v1.model.report.SearchTermReport;
import com.kindminds.drs.api.data.access.usecase.report.ViewSearchTermReportDao;
import com.kindminds.drs.v1.model.impl.SearchTermReportImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ViewSearchTermReportUcoImpl implements ViewSearchTermReportUco {
	
	@Autowired private ViewSearchTermReportDao dao;

	@Override
	public List<Marketplace> getMarketplaces() {
		return Marketplace.getAmazonMarketplaces();
	}
	
	@Override
	public SearchTermReport getReport(Marketplace marketplace,String supplierKcode,String campaignName) {
		if(!StringUtils.hasText(supplierKcode) || !StringUtils.hasText(campaignName)) return null;
		SearchTermReportImpl report = new SearchTermReportImpl();
		report.setMarketplace(marketplace);
		report.setLineItems(this.dao.queryLineItems(marketplace,supplierKcode,campaignName));
		return report;
	}
	
	@Override
	public List<String> getCampaignNameList(Marketplace marketplace,String supplierKcode){
		return this.dao.queryCampaignNameList(marketplace,supplierKcode);
	}
	
}
