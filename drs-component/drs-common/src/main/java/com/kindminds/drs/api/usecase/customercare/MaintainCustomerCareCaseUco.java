package com.kindminds.drs.api.usecase.customercare;

import java.util.List;
import java.util.Map;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseDto;
import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseDto.CustomerCareCaseMessage;
import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseOrderInfo;
import com.kindminds.drs.api.v1.model.common.DtoList;

public interface MaintainCustomerCareCaseUco {
	// CRUD CUSTOMER CARE CASE
	int save(CustomerCareCaseDto customerCareCaseDto);
	Integer getElasticHitCount(String searchWords);
	DtoList<CustomerCareCaseDto> getList(int pageIndex, String customerName);
	DtoList<CustomerCareCaseDto> getListElastic(int pageIndex, String searchWords);
	CustomerCareCaseDto get(int Id);
	int update(CustomerCareCaseDto customerCareCaseDto);
	int updateStatus(CustomerCareCaseDto customerCareCaseDto);
	int delete(int id);
	// CRUD CUSTOMER CARE CASE MESSAGE 
	CustomerCareCaseMessage getMessage(int caseId,int lineSeq);
	int addMessage(int caseId,CustomerCareCaseMessage msg);
	CustomerCareCaseMessage updateMessage(int caseId,CustomerCareCaseMessage msg);
	boolean deleteMessage(int caseId,int lineSeq);
	// OPTION SOURCES CUSTOMER CARE CASE
	List<String> getTypeList();
	List<Marketplace> getMarketplaceList();
	Map<Integer, String> getMarketplaceIdToDrsCompanyMap();
	Map<String,String> getMarketplaceToDrsCompanyMap();
	Map<String,String> getProductBaseCodeToSupplierNameMap(String supplierKcode);
	Map<String,String> getProductSkuCodeToSupplierNameMap(String supplierKcode);
	Map<String,String> getProductSkuCodeToSupplierNameMapUnderBases(List<String> baseCodes);
	Map<String,String> getSupplierKcodeToShortEnUsNameMap();
	Map<String,String> getDrsCompanyKcodeToShortEnUsNameMap();
	Map<String,String> getProductBaseCodeToSupplierNameMapByMarketplace(String supplierKcode,Marketplace marketplace);
	Map<String,String> getProductSkuCodeToSupplierNameMapByMarketplace(String supplierKcode,Marketplace marketplace);
	// UTILITY
	CustomerCareCaseOrderInfo getOrderDateById(String orderId);
	Map<Integer,String> getApplicableTemplate(int caseId);
	String getTemplateContent(int templateId);
	Map<Integer,String> getIssueCategoryIdToNameMap();
	Map<Integer,String> getTypeIdToNameMap(Integer categoryId);
	Map<Integer,String> getIssueIdToEnUsNameMap(Integer typeCategoryId,Integer typeId);
	Map<Integer,String> getTemplates(int issueId);
	
}
