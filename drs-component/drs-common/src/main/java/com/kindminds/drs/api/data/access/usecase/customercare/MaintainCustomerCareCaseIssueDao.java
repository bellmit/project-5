package com.kindminds.drs.api.data.access.usecase.customercare;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseIssue;
import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseIssue.CustomerCareCaseIssueComment;

public interface MaintainCustomerCareCaseIssueDao {
	// FOR SCHEDULER
	Map<Integer,Date> queryNeedRenotifyIssueIdToLastCommentDateTimeMap(String status);
	// LIST
	int queryIssueCount(String kcodeToFilter);
	int queryIssueCount(String kcodeToFilter,Integer categoryId);
	int queryIssueCount(String kcodeToFilter,Integer categoryId,Integer typeId);
	List<CustomerCareCaseIssue> queryIssueList(String supplierKcode,int startIndex,int size);
	List<CustomerCareCaseIssue> queryIssueList(String supplierKcode,Integer categoryId,int startIndex,int size);
	List<CustomerCareCaseIssue> queryIssueList(String supplierKcode,Integer categoryId,Integer typeId,int startIndex,int size);
	void setExtraIssueInfo(List<CustomerCareCaseIssue> issueList);
	// CRUD CUSTOMER CARE CASE ISSUE TYPE CATEGORY
	String insertIssueTypeCategory(String name);
	Integer insertIssueType(Integer categoryId,String typeName);
	String deleteIssueTypeCategory(String name);
	String deleteIssueType(String categoryName,String name);
	// CRUD CUSTOMER CARE CASE ISSUE
	Integer insertIssue(CustomerCareCaseIssue issue);
	CustomerCareCaseIssue queryIssue(int id);
	CustomerCareCaseIssue update(CustomerCareCaseIssue issue);
	boolean delete(int id);
	Integer insertComment(int userId, int issueId,CustomerCareCaseIssueComment comment);
	void updateIssueStatus(int issueId,String status);
	void setNeedRenotifyForCommentToSupplier(int issueId, boolean value);
	Map<Integer,String> queryCategoryIdToNameMap();
	List<String> queryIssueTypeList(Integer categoryId);
	Map<Integer,Map<Integer,String>> queryCategoryIdToIssueTypeAndIdMap();
	Map<String,Map<Integer,String>> queryIssueTypeToIssuesMap(Integer categoryId);
	Map<String,Map<Integer,String>> queryGeneralIssueTypeToIssuesMap(Integer categoryId);
	Map<String,Map<Integer,String>> queryIssueTypeToIssuesMapByProductBase(Integer categoryId, List<String> baseList);
	Map<String,Map<Integer,String>> queryIssueTypeToIssuesMapByProductSku(Integer categoryId, List<String> skuList);
	String queryEnUsIssueName(int issueId);
	// UTIL
	String querySupplierKcodeOfIssue(int issueId);
	String queryKcodeOfSupplierInIssue(int issueId);
	String queryEmailOfNewestDrsCommenter(int issueId);
	void updateType(Integer id, String name);
	Map<Integer,String> queryTypeIdToNameMap();
	Map<Integer,String> queryTypeIdToNameMap(Integer categoryId);
	Integer queryMaxIssueId();
}
