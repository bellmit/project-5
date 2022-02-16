package com.kindminds.drs.impl;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseDto;
import com.kindminds.drs.util.TestUtil;

import java.util.List;

public class CustomerCareCaseDtoImplForTest implements CustomerCareCaseDto {
	
	private Integer id;
	private String type;
	private Integer issueTypeCategoryId;
	private String status;
	private String drsCompanyKcode;
	private Marketplace marketPlace;
	private String marketPlaceOrderId;
	private String marketPlaceOrderDate;
	private String supplierKcode;
	
	private String customerName;
	private String dateCreated;
	
	private List<Integer> relatedIssueIds;
	private List<String> relatedProductBaseList;
	private List<String> relatedProductSkuList;
	private List<CustomerCareCaseMessage> messageList;
	
	public CustomerCareCaseDtoImplForTest(
			Integer id, 
			String type, 
			Integer issueTypeCategoryId, 
			String drsCompanyKcode,
			Marketplace marketPlace, 
			String marketPlaceOrderId, 
			String marketPlaceOrderDate,
			String supplierKcode, 
			String customerName,
			String dateCreateStr, 
			String status,
			List<Integer> issueIdList, 
			List<String> baseList, 
			List<String> skuList,
			List<CustomerCareCaseMessage> msgList){
		this.id = id;
		this.type = type;
		this.issueTypeCategoryId = issueTypeCategoryId;
		this.drsCompanyKcode = drsCompanyKcode;
		this.marketPlace = marketPlace;
		this.marketPlaceOrderId = marketPlaceOrderId;
		this.marketPlaceOrderDate = marketPlaceOrderDate;
		this.supplierKcode = supplierKcode;
		this.customerName = customerName;
		this.dateCreated = dateCreateStr;
		this.status = status;
		this.relatedIssueIds = issueIdList;
		this.relatedProductBaseList = baseList;
		this.relatedProductSkuList = skuList;
		this.messageList = msgList;
	}
	
	public void addMessage(CustomerCareCaseMessage msg){
		this.messageList.add(msg);
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj instanceof CustomerCareCaseDto){
			CustomerCareCaseDto ccc = ((CustomerCareCaseDto)obj);
			boolean messagesEquals = false;
			if(this.getMessages()==null&&ccc.getMessages()==null) messagesEquals = true;
			else if(this.getMessages().isEmpty()&&ccc.getMessages()==null) messagesEquals = true;
			else if(this.getMessages().equals(ccc.getMessages())) messagesEquals = true;
			return this.getCaseId().equals(ccc.getCaseId())
				&& this.getCaseType().equals(ccc.getCaseType())
				&& TestUtil.nullableEquals(this.getIssueTypeCategoryId(),ccc.getIssueTypeCategoryId())
				&& this.getDrsCompanyKcode().equals(ccc.getDrsCompanyKcode())
				&& this.getMarketplace().equals(ccc.getMarketplace())
				&& TestUtil.nullableEquals(this.getMarketplaceOrderId(),ccc.getMarketplaceOrderId())
				&& TestUtil.nullableEquals(this.getMarketplaceOrderDate(),ccc.getMarketplaceOrderDate())
				&& this.getSupplierKcode().equals(ccc.getSupplierKcode())
				&& this.getCustomerName().equals(ccc.getCustomerName())
				&& this.getDateCreated().equals(ccc.getDateCreated())
				&& this.getStatus().equals(ccc.getStatus())
				&& TestUtil.nullableEquals(this.getRelatedIssueIds(),ccc.getRelatedIssueIds())
				&& TestUtil.nullableEquals(this.getRelatedProductBaseCodeList(),ccc.getRelatedProductBaseCodeList())
				&& TestUtil.nullableEquals(this.getRelatedProductSkuCodeList(),ccc.getRelatedProductSkuCodeList())
				&& messagesEquals;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "CustomerCareCaseDtoImplForTest [getCaseId()=" + getCaseId() + ", getCaseType()=" + getCaseType()
				+ ", getIssueTypeCategoryId()=" + getIssueTypeCategoryId() + ", getRelatedIssueIds()="
				+ getRelatedIssueIds() + ", getStatus()=" + getStatus() + ", getDrsCompanyKcode()="
				+ getDrsCompanyKcode() + ", getMarketplace()=" + getMarketplace() + ", getMarketplaceOrderId()="
				+ getMarketplaceOrderId() + ", getMarketplaceOrderDate()=" + getMarketplaceOrderDate()
				+ ", getSupplierKcode()=" + getSupplierKcode() + ", getLatestActivityDays()=" + getLatestActivityDays()
				+ ", getLatestActivityHours()=" + getLatestActivityHours() + ", getLatestActivityMinutes()="
				+ getLatestActivityMinutes() + ", getRelatedProductBaseCodeList()=" + getRelatedProductBaseCodeList()
				+ ", getRelatedProductSkuCodeList()=" + getRelatedProductSkuCodeList() + ", getCustomerName()="
				+ getCustomerName() + ", getDateCreated()=" + getDateCreated() + ", getMessages()=" + getMessages()
				+ "]";
	}

	@Override
	public Integer getCaseId() {
		return this.id;
	}

	@Override
	public String getCaseType() {
		return this.type;
	}

	@Override
	public Integer getIssueTypeCategoryId() {
		return this.issueTypeCategoryId;
	}

	@Override
	public List<Integer> getRelatedIssueIds() {
		return this.relatedIssueIds;
	}

	@Override
	public String getStatus() {
		return this.status;
	}
	
	@Override
	public String getDrsCompanyKcode() {
		return this.drsCompanyKcode;
	}

	@Override
	public Marketplace getMarketplace() {
		return this.marketPlace;
	}
	
	@Override
	public String getMarketplaceOrderId() {
		return this.marketPlaceOrderId;
	}

	@Override
	public String getMarketplaceOrderDate() {
		return this.marketPlaceOrderDate;
	}

	@Override
	public String getSupplierKcode() {
		return this.supplierKcode;
	}
	
	@Override
	public Integer getLatestActivityDays() {
		return null;
	}

	@Override
	public Integer getLatestActivityHours() {
		return null;
	}
	
	@Override
	public Integer getLatestActivityMinutes() {
		return null;
	}

	@Override
	public List<String> getRelatedProductBaseCodeList() {
		return this.relatedProductBaseList;
	}

	@Override
	public List<String> getRelatedProductSkuCodeList() {
		return this.relatedProductSkuList;
	}

	@Override
	public String getCustomerName() {
		return this.customerName;
	}

	@Override
	public String getDateCreated() {
		return this.dateCreated;
	}

	@Override
	public List<CustomerCareCaseMessage> getMessages() {
		return this.messageList;
	}

}
