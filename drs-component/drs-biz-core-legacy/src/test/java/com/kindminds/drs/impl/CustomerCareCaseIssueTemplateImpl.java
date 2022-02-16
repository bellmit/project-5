package com.kindminds.drs.impl;

import com.kindminds.drs.Locale;
import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseIssue.CustomerCareCaseIssueTemplate;

import java.util.ArrayList;
import java.util.List;

public class CustomerCareCaseIssueTemplateImpl implements CustomerCareCaseIssueTemplate {

	private int id;
	private Integer issueId;
	private String issueName;
	private List<Locale> applicableLanguagess;
	private List<String> applicableCaseTypes;
	private List<String> applicableMarketRegions;
	private List<Marketplace> applicableMarketplaceList;
	private String content;
	
	public CustomerCareCaseIssueTemplateImpl(
			int id,
			int issueId,
			List<String> applicableLanguagess,
			List<String> applicableCaseTypes,
			List<String> applicableMarketRegions,
			List<Marketplace> applicableMarketplaceList,
			String content) {
		this.id = id;
		this.issueId = issueId;
		this.applicableLanguagess = new ArrayList<Locale>();
		for(String localeCode:applicableLanguagess){
			this.applicableLanguagess.add(Locale.fromCode(localeCode));
		}
		this.applicableCaseTypes = applicableCaseTypes;
		this.applicableMarketRegions = applicableMarketRegions;
		this.applicableMarketplaceList = applicableMarketplaceList;
		this.content = content;
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj instanceof CustomerCareCaseIssueTemplate){
			CustomerCareCaseIssueTemplate t = (CustomerCareCaseIssueTemplate)obj;
			return this.getId().equals(t.getId())
				&& this.getIssueId().equals(t.getIssueId())
				&& this.getApplicableLanguages().equals(t.getApplicableLanguages())
				&& this.getApplicableCaseTypes().equals(t.getApplicableCaseTypes())
				&& this.getApplicableMarketRegions().equals(t.getApplicableMarketRegions())
				&& this.getApplicableMarketplaceList().equals(t.getApplicableMarketplaceList())
				&& this.getContents().equals(t.getContents());
		}
		return false;
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
		return this.applicableLanguagess;
	}

	@Override
	public List<String> getApplicableCaseTypes() {
		return this.applicableCaseTypes;
	}

	@Override
	public List<String> getApplicableMarketRegions() {
		return this.applicableMarketRegions;
	}

	@Override
	public List<Marketplace> getApplicableMarketplaceList() {
		return this.applicableMarketplaceList;
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
