package com.kindminds.drs.web.data.dto;

import java.util.ArrayList;
import java.util.List;

import com.kindminds.drs.Marketplace;

import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseDto;

public class CustomerCareCaseDtoImpl implements CustomerCareCaseDto {
	
	private int caseId;
	private String caseType;
	private List<Integer> relatedIssueIds;
	private Integer issueTypeCategoryId;
	private String status;
	private String drsCompanyKcode;
	private Marketplace marketplace;
	private String marketplaceOrderId;
	private String supplierKcode;
	private List<String> relatedProductBaseCodeList;
	private List<String> relatedProductSkuCodeList; 
	private String customerName;
	private String dateCreated;
	public List<CustomerCareCaseBaseProductMessageImpl> messages = new ArrayList<CustomerCareCaseBaseProductMessageImpl>();
	private String marketplaceOrderDate;
	private Integer latestActivityDays;
	private Integer latestActivityHours;
	private Integer latestActivityMinutes;
	
	public CustomerCareCaseDtoImpl(){};
	
	public CustomerCareCaseDtoImpl(CustomerCareCaseDto original){
		this.caseId = original.getCaseId();
		this.caseType = original.getCaseType();
		this.relatedIssueIds = original.getRelatedIssueIds();		
		if(original.getIssueTypeCategoryId() == 0){			
			this.issueTypeCategoryId = null;
		}else{
			this.issueTypeCategoryId = original.getIssueTypeCategoryId();			
		}						
		this.status = original.getStatus();
		this.drsCompanyKcode = original.getDrsCompanyKcode();
		this.marketplace = original.getMarketplace();
		this.marketplaceOrderId = original.getMarketplaceOrderId();
		this.supplierKcode = original.getSupplierKcode();
		this.relatedProductBaseCodeList = original.getRelatedProductBaseCodeList(); 
		this.relatedProductSkuCodeList = original.getRelatedProductSkuCodeList();
		this.customerName = original.getCustomerName();
		this.dateCreated = original.getDateCreated();
		this.marketplaceOrderDate = original.getMarketplaceOrderDate(); 
		this.latestActivityDays = original.getLatestActivityDays();
		this.latestActivityHours = original.getLatestActivityHours();
		this.latestActivityMinutes = original.getLatestActivityMinutes();

		for(CustomerCareCaseMessage origMessage:original.getMessages()){
			this.messages.add(new CustomerCareCaseBaseProductMessageImpl(origMessage));			
		}		
	};
	
	@Override
	public Integer getCaseId() {
		return this.caseId;
	}

	public void setCaseId(Integer caseId){
		this.caseId = caseId;		
	}
	
	@Override
	public String getCaseType() {		
		return this.caseType;
	}
	
	public void setCaseType(String caseType) {
		this.caseType = caseType;		
	}
	
	@Override
	public Integer getIssueTypeCategoryId() {		
		return this.issueTypeCategoryId;
	}

	public void setIssueTypeCategoryId(Integer issueTypeCategoryId) {
		this.issueTypeCategoryId = issueTypeCategoryId; 		
	}	
	
	@Override
	public List<Integer> getRelatedIssueIds() {		
		return this.relatedIssueIds;
	}

	public void setRelatedIssueIds(List<Integer> relatedIssueIds) {
		this.relatedIssueIds = relatedIssueIds;		
	}
	
	@Override
	public String getStatus() {		
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;				
	}
			
	@Override
	public String getDrsCompanyKcode() {		
		return this.drsCompanyKcode;
	}

	public void setDrsCompanyKcode(String drsCompanyKcode) {
		this.drsCompanyKcode = drsCompanyKcode;		
	}
		
	@Override
	public Marketplace getMarketplace() {		
		return this.marketplace;
	}
	
	public void setMarketplace(Marketplace marketplace) {
	 	this.marketplace = marketplace;				
	}
	
	@Override
	public String getMarketplaceOrderId() {		
		return this.marketplaceOrderId;
	}

	public void setMarketplaceOrderId(String marketplaceOrderId) {
		this.marketplaceOrderId = marketplaceOrderId;		
	}
		
	@Override
	public String getSupplierKcode() {		
		return this.supplierKcode;
	}

	public void setSupplierKcode(String supplierKcode) {
		this.supplierKcode = supplierKcode;				
	}
		
	@Override
	public List<String> getRelatedProductBaseCodeList() {		
		return this.relatedProductBaseCodeList;
	}

	public void setRelatedProductBaseCodeList(List<String> relatedProductBaseCodeList){
		this.relatedProductBaseCodeList = relatedProductBaseCodeList;		
	}
		
	@Override
	public List<String> getRelatedProductSkuCodeList() {
		return this.relatedProductSkuCodeList;
	}
		
	public void setRelatedProductSkuCodeList(List<String> relatedProductSkuCodeList){
		this.relatedProductSkuCodeList = relatedProductSkuCodeList; 			
	}
	
	@Override
	public String getCustomerName() {		
		return this.customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;				
	}
				
	@Override
	public String getDateCreated() {		
		return this.dateCreated;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated; 		
	}
		
	@Override
	public List<CustomerCareCaseMessage> getMessages() {
		List<CustomerCareCaseMessage> messages = new ArrayList<CustomerCareCaseMessage>();
		for(CustomerCareCaseMessage message:this.messages){
			messages.add(message);
		}				
		return messages;
	}
	
	public List<CustomerCareCaseBaseProductMessageImpl> getMessage(){		
		return this.messages;	
	}
	
	public void setMessage(List<CustomerCareCaseBaseProductMessageImpl> message){
		this.messages = message;		
	}

	@Override
	public String getMarketplaceOrderDate() {		
		return this.marketplaceOrderDate;
	}
	
	public void setMarketplaceOrderDate(String marketplaceOrderDate) {
		this.marketplaceOrderDate = marketplaceOrderDate;				
	}

	@Override
	public Integer getLatestActivityDays() {		
		return this.latestActivityDays;
	}

	public void setLatestActivityDays(Integer latestActivityDays) {
		this.latestActivityDays = latestActivityDays; 		
	}	
	
	@Override
	public Integer getLatestActivityHours() {		
		return this.latestActivityHours;
	}
	
	public void  setLatestActivityHours(Integer latestActivityHours) {
		this.latestActivityHours = latestActivityHours;		
	}

	@Override
	public Integer getLatestActivityMinutes() {		
		return this.latestActivityMinutes;
	}
		
	public void setLatestActivityMinutes(Integer latestActivityMinutes) {		
		this.latestActivityMinutes = latestActivityMinutes;
	}

	@Override
	public String toString() {
		return "CustomerCareCaseDtoImpl [getCaseId()=" + getCaseId() + ", getCaseType()=" + getCaseType()
				+ ", getIssueTypeCategoryId()=" + getIssueTypeCategoryId() + ", getRelatedIssueIds()="
				+ getRelatedIssueIds() + ", getStatus()=" + getStatus() + ", getDrsCompanyKcode()="
				+ getDrsCompanyKcode() + ", getMarketplace()="
				+ getMarketplace() + ", getMarketplaceOrderId()=" + getMarketplaceOrderId() + ", getSupplierKcode()="
				+ getSupplierKcode() + ", getRelatedProductBaseCodeList()=" + getRelatedProductBaseCodeList()
				+ ", getRelatedProductSkuCodeList()=" + getRelatedProductSkuCodeList() + ", getCustomerName()="
				+ getCustomerName() + ", getDateCreated()=" + getDateCreated() + ", getMessages()=" + getMessages()
				+ ", getMessage()=" + getMessage() + ", getMarketplaceOrderDate()=" + getMarketplaceOrderDate()
				+ ", getLatestActivityDays()=" + getLatestActivityDays() + ", getLatestActivityHours()="
				+ getLatestActivityHours() + ", getLatestActivityMinutes()=" + getLatestActivityMinutes() + "]";
	}
	
	
						
}