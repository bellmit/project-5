package com.kindminds.drs.api.usecase;

import java.util.List;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.v1.model.report.SearchTermReport;

public interface ViewSearchTermReportUco {
	List<Marketplace> getMarketplaces();
	SearchTermReport getReport(Marketplace marketplace,String supplierKcode,String campaignName);
	List<String> getCampaignNameList(Marketplace marketplace,String supplierKcode);
}
