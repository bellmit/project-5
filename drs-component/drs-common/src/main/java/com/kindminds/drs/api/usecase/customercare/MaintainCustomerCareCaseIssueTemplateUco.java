package com.kindminds.drs.api.usecase.customercare;

import java.util.List;

import com.kindminds.drs.Locale;
import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseIssue.CustomerCareCaseIssueTemplate;

public interface MaintainCustomerCareCaseIssueTemplateUco {
	public int create(CustomerCareCaseIssueTemplate template);
	public CustomerCareCaseIssueTemplate get(int id);
	public CustomerCareCaseIssueTemplate update(CustomerCareCaseIssueTemplate template);
	public boolean delete(int id);
	
	public List<Locale> getLocaleCodeList();
	public List<String> getCaseTypeList();
	public List<String> getMarketRegionList();
	public List<Marketplace> getMarketplaceList();
	
	
}
