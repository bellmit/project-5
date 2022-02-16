package com.kindminds.drs.api.v1.model.customercare;

import com.kindminds.drs.Locale;
import com.kindminds.drs.Marketplace;

import java.util.List;
import java.util.Map;

public interface CustomerCareCaseIssue {

	Integer getId();
	Integer getCategoryId();
	Integer getTypeId();
	Map<Locale,String> getLocaleCodeToNameMap();
	String getStatus();        
	String getSupplierKcode();
	String getName();
	String getIssueTypeName();
	String getSupplierEnName();
	List<String> getRelatedProductBaseCodeList();
	List<String> getRelatedProductSkuCodeList();
	String getTemplateOccurrences();	
	String getCreatedDate();
	Integer getDaysFromLastUpdate();
	Integer getHoursFromLastUpdate();
	Integer getMinsFromLastUpdate();
	List<CustomerCareCaseIssueTemplate> getTemplates();
   	List<CustomerCareCaseIssueComment> getComments();
	void setLocaleToNameMap(Map<Locale, String> map);
	void setRelatedProductBaseList(List<String> baseList);
	void setRelatedProductSkuList(List<String> skuList);
	void setOccurrences(Integer i);
   	
 	public interface CustomerCareCaseIssueComment{
		Integer getLineSeq();
		String getDateCreate();
		String getCreateBy();
		String getContents();
	}
 	
 	public interface CustomerCareCaseIssueTemplate{
		Integer getId();
		Integer getIssueId();
		String getIssueName();
		List<Locale> getApplicableLanguages();
		List<String> getApplicableCaseTypes();
		List<String> getApplicableMarketRegions();
		List<Marketplace> getApplicableMarketplaceList();
		String getDrsChargeByWordCount();
		String getContents();
	}
 	
 	public enum CustomerCareCaseIssueStatus{
 		PENDING_SUPPLIER_ACTION,
 		PENDING_DRS_ACTION,
 		RESPONSE_TEMPLATE_DONE,
 		ARCHIVE;
 	}
	
}
