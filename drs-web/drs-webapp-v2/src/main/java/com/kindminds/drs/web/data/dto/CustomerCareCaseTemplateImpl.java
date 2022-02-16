package com.kindminds.drs.web.data.dto;

import java.util.List;

import com.kindminds.drs.Locale;
import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseIssue.CustomerCareCaseIssueTemplate;

public class CustomerCareCaseTemplateImpl implements CustomerCareCaseIssueTemplate {

	private Integer id;
	private Integer issueId;
	private String issueName;
	private List<Locale> applicableLanguages;
	private List<String> applicableCaseTypes;
	private List<String> applicableMarketRegions;
	private List<Marketplace> applicableMarketplaceList;
	private String drsChargeByWordCount;
	private String contents;
	
	@Override
	public Integer getId() {		
		return this.id;
	}

	public void setId(Integer id){
		this.id = id;		
	}
	
	@Override
	public Integer getIssueId() {		
		return this.issueId;
	}

	public void setIssueId(Integer issueId) {
		this.issueId = issueId;		
	}
		
	@Override
	public String getIssueName() {		
		return this.issueName;
	}
	
	public void setIssueName(String issueName) {
		this.issueName = issueName;		
	}
	
	@Override
	public List<Locale> getApplicableLanguages() {		
		return this.applicableLanguages;
	}

	public void setApplicableLanguages(List<Locale> applicableLanguages) {
		this.applicableLanguages = applicableLanguages; 				
	}
	
	@Override
	public List<String> getApplicableCaseTypes() {		
		return this.applicableCaseTypes;
	}

	public void setApplicableCaseTypes(List<String> applicableCaseTypes) {
		this.applicableCaseTypes = applicableCaseTypes;
	}
	
	@Override
	public List<String> getApplicableMarketRegions() {		
		return this.applicableMarketRegions;
	}

	public void setApplicableMarketRegions(List<String> applicableMarketRegions) {
		this.applicableMarketRegions = applicableMarketRegions; 
	}
	
	@Override
	public List<Marketplace> getApplicableMarketplaceList() {		
		return this.applicableMarketplaceList;
	}

	public void setApplicableMarketplaceList(List<Marketplace> applicableMarketplaceList) {
		this.applicableMarketplaceList = applicableMarketplaceList; 
	}
	
	@Override
	public String getDrsChargeByWordCount() {		
		return this.drsChargeByWordCount;
	}
	
	public void setDrsChargeByWordCount(String drsChargeByWordCount) {			
		this.drsChargeByWordCount = drsChargeByWordCount;		
	}
		
	@Override
	public String getContents() {		
		return this.contents;
	}

	public void setContents(String contents) {
		this.contents = contents; 		
	}
	
}