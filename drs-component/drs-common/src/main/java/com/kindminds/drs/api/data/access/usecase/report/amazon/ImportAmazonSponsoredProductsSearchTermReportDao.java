package com.kindminds.drs.api.data.access.usecase.report.amazon;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.v1.model.amazon.AmazonSponsoredProductsSearchTermReportRawItem;

public interface ImportAmazonSponsoredProductsSearchTermReportDao {
	int importReportRawLines(Marketplace marketplace,List<AmazonSponsoredProductsSearchTermReportRawItem> rawLines);
	void deleteDCPExistingReportLine(Marketplace marketplace);
	void deleteDCPExistingReportLineByDate(Marketplace marketplace , Date startDate , Date endDate );
	void deleteExistingReportLine(Marketplace marketplace , String kcode);
	void deleteExistingCampaignNameSupplierMap(Marketplace marketplace);
	List<String> queryDistinctCampaignNameList(Marketplace marketplace);
	void insertCampaignNameList(Marketplace marketplace, Map<String,Integer> campaignNameToSupplierId);
	Map<String,String> queryCampaignNameToSupplierKcodeMap(Marketplace marketplace);
	void updateSupplierOfCampaignName(Marketplace marketplace,String campaignName, String supplierKcode);
	void clearBelongedSupplierOfCampaignName(Marketplace marketplace);
	Integer queryCompanyId(String probablyKcode);
}
