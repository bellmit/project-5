package com.kindminds.drs.api.data.access.usecase.customercare;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseDto;
import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseDto.CustomerCareCaseMessage;
import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseOrderInfo;
import com.kindminds.drs.api.v1.model.customercare.CustomerCaseSearchCondition;

public interface MaintainCustomerCareCaseDao {
	CustomerCareCaseOrderInfo queryOrderInfoById(String orderId);
	int queryCounts(CustomerCaseSearchCondition condition);
	List<CustomerCareCaseDto> queryList(int startIndex, int size, CustomerCaseSearchCondition condition);
	// CRUD CUSTOMER CARE CASE
	int insert(int userId, CustomerCareCaseDto customerCareCaseDto);
	CustomerCareCaseDto query(int id);
	int update(CustomerCareCaseDto customerCareCaseDto);
	int updateStatus(CustomerCareCaseDto customerCareCaseDto);
	int delete(int id);
	// CRUD CUSTOMER CARE CASE MESSAGE
	List<CustomerCareCaseMessage> queryMessages(int caseId);
	int insertMsdcMessage(int userId, int caseId,CustomerCareCaseMessage msg, BigDecimal chargeByDrs);
	int insertCustomerMessage(int userId, int caseId,CustomerCareCaseMessage msg);
	CustomerCareCaseMessage queryMessage(int caseId, int lineSeq);
	int updateMsdcMessage(int userId, int caseId,CustomerCareCaseMessage msg, BigDecimal chargeByDrs);
	int updateCustomerMessage(int userId, int caseId,CustomerCareCaseMessage msg);
	boolean deleteMessage(int caseId, int lineSeq);
	// OPTION SOURCE
	List<String> queryTypeList();
	Map<String,String> queryProductBaseCodeToSupplierNameMap(String supplierKcode);
	Map<String,String> queryProductSkuCodeToSupplierNameMap(String supplierKcode);
	Map<String,String> queryProductSkuCodeToSupplierNameMapUnderBases(List<String> baseCodes);
	Map<String,String> queryContactChannelNameToDrsCompanyShortEnUsNameMap();
	Map<String,String> queryProductBaseCodeToSupplierNameMapByMarketplace(String supplierKcode,int marketplaceId);
	Map<String,String> queryProductSkuCodeToSupplierNameMapByMarketplace(String supplierKcode,int marketplaceId);
	// UTIL
	String queryMsdcMsgMs2ssStatementName(int caseId, int msgLineSeq);
	String queryMsdcMsgSs2spStatementName(int caseId, int msgLineSeq);
	Map<Integer, String> queryApplicableTemplate(int caseId);
	String queryTemplateContent(int templateId);
	Map<Integer,String> queryAllIssueIdToEnUsNameMap(Integer typeCategoryId, Integer typeId);
	Map<Integer,String> queryTemplateIdToDisplayName(int issueId);
	String querySupplierKcodeOfCase(int caseId);
	void updateLastUpdateTime(int caseId,Date date);
	String queryCompanyEnglishShortName(String kCode);
	Integer queryMaxCaseId();
}
