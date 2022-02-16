package com.kindminds.drs.api.usecase.customercare;

import java.util.List;
import java.util.Map;

import com.kindminds.drs.Locale;
import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseIssue;
import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseIssue.CustomerCareCaseIssueComment;
import com.kindminds.drs.api.v1.model.common.DtoList;

public interface MaintainCustomerCareCaseIssueUco {
	// FOR SCHEDULER
	public void doRenotify();
	// LIST
	public String getDateTimeNowUtc();
	public DtoList<CustomerCareCaseIssue> getList(Integer categoryId,Integer typeId,int pageIndex);
	public DtoList<CustomerCareCaseIssue> getListElastic(String searchWords,int pageIndex);
	// CRUD
	public Integer createIssue(CustomerCareCaseIssue issue);
	public CustomerCareCaseIssue getIssue(int id);
	public CustomerCareCaseIssue update(CustomerCareCaseIssue issue);
	public boolean deleteIssue(int id);
	public Integer addComment(int issueId,Boolean pendingSupplierAction, CustomerCareCaseIssueComment comment);
	// CRUD TYPE & CATEGORY
	public String createIssueTypeCategory(String name);
	public String deleteIssueTypeCategory(String name);
	public Map<Integer,Map<Integer,String>> getCategoryIdToIssueTypeAndIdMap();
	public void updateType(Integer id,String name);
	public Integer createIssueType(Integer categoryId, String typeName);
	public String deleteIssueType(String categoryName, String name);
	// OPTION SOURCES
	public Map<Integer,String> getCategoryIdToNameMap();
	public Map<Integer,String> getTypeIdToNameMap();// FOR ISSUE LIST
	public Map<Integer,String> getTypeIdToNameMap(Integer categoryId);
	public Map<String,Map<Integer,String>> getIssueTypeToIssuesMap(List<String> baseList,List<String> skuList, Integer categoryId);
	public String getEnUsIssueName(int issueId);
	public List<Locale> getLocaleCodeList();
	public List<String> getIssueStatusList();
}
