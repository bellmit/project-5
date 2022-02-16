package com.kindminds.drs.api.data.access.usecase.customercare;

import java.util.List;

import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseIssue.CustomerCareCaseIssueStatus;
import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseIssue.CustomerCareCaseIssueTemplate;

public interface MaintainCustomerCareCaseIssueTemplateDao {
	public int insert(CustomerCareCaseIssueTemplate template);
	public CustomerCareCaseIssueTemplate update(CustomerCareCaseIssueTemplate template);
	public CustomerCareCaseIssueTemplate query(int id);
	public boolean delete(int id);
	public List<String> queryCaseTypeList();
	public List<Integer> queryMarketplaceIdList();
	public void updateIssueStatus(int issueId, CustomerCareCaseIssueStatus status);
	public String queryKcodeOfSupplierInIssue(int issueId);
	public String queryEnUsIssueName(int issueId);
}
