package com.kindminds.drs.persist.v1.model.mapping.customercare;

import java.util.List;






import com.kindminds.drs.Locale;
import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseIssue.CustomerCareCaseIssueTemplate;


public class CustomerCareCaseIssueTemplateImpl implements CustomerCareCaseIssueTemplate {
	
	//@Id ////@Column(name="id")
	private int id;
	//@Column(name="issue_id")
	private int issueId;
	//@Column(name="issue_name")
	private String issueName;

	private List<Locale> locales;

	private List<String> caseTypes;

	private List<String> marketRegions;

	private List<Marketplace> marketplaceList;
	//@Column(name="content")
	private String content;

	public CustomerCareCaseIssueTemplateImpl() {
	}

	public CustomerCareCaseIssueTemplateImpl(int id, int issueId, String issueName, String content) {
		this.id = id;
		this.issueId = issueId;
		this.issueName = issueName;
		this.content = content;
	}

	public void setLocales(List<Locale> locales){
		this.locales = locales;
	}
	
	public void setCaseTypes(List<String> caseTypes) {
		this.caseTypes = caseTypes;
	}

	public void setMarketRegions(List<String> marketRegions) {
		this.marketRegions = marketRegions;
	}

	public void setMarketplaces(List<Marketplace> marketplaceList) {
		this.marketplaceList = marketplaceList;
	}

	@Override
	public String toString() {
		return "CustomerCareCaseIssueTemplateImpl [getId()=" + getId()
				+ ", getIssueId()=" + getIssueId() + ", getIssueName()="
				+ getIssueName() + ", getApplicableLanguages()="
				+ getApplicableLanguages() + ", getApplicableCaseTypes()="
				+ getApplicableCaseTypes() + ", getApplicableMarketRegions()="
				+ getApplicableMarketRegions()
				+ ", getApplicableMarketplaces()="
				+ getApplicableMarketplaceList() + ", getContents()="
				+ getContents() + "]";
	}

	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public Integer getIssueId() {
		return this.issueId;
	}

	@Override
	public String getIssueName() {
		return this.issueName;
	}

	@Override
	public List<Locale> getApplicableLanguages() {
		return this.locales;
	}

	@Override
	public List<String> getApplicableCaseTypes() {
		return this.caseTypes;
	}

	@Override
	public List<String> getApplicableMarketRegions() {
		return this.marketRegions;
	}

	@Override
	public List<Marketplace> getApplicableMarketplaceList() {
		return this.marketplaceList;
	}

	@Override
	public String getContents() {
		return this.content;
	}

	@Override
	public String getDrsChargeByWordCount() {
		// TODO Auto-generated method stub
		return null;
	}

}
