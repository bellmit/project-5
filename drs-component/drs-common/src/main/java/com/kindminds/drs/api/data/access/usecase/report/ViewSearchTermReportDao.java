package com.kindminds.drs.api.data.access.usecase.report;

import java.util.List;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.v1.model.report.SearchTermReport.SearchTermReportLineItem;

public interface ViewSearchTermReportDao {
	public List<SearchTermReportLineItem> queryLineItems(Marketplace marketplace,String supplierKcode,String campaignName);
	public List<String> queryCampaignNameList(Marketplace marketplace,String supplierKcode);
}
