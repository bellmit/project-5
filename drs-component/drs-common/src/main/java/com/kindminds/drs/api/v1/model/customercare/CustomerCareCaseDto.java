package com.kindminds.drs.api.v1.model.customercare;

import com.kindminds.drs.Marketplace;

import java.util.List;

public interface CustomerCareCaseDto {
	Integer getCaseId();
	String getCaseType();
	Integer getIssueTypeCategoryId();
	List<Integer> getRelatedIssueIds();
	String getStatus();
    String getDrsCompanyKcode();
	Marketplace getMarketplace();
    String getMarketplaceOrderId();
	String getMarketplaceOrderDate();
	String getSupplierKcode();
	Integer getLatestActivityDays();
	Integer getLatestActivityHours();
	Integer getLatestActivityMinutes();
	List<String> getRelatedProductBaseCodeList();
	List<String> getRelatedProductSkuCodeList();
	String getCustomerName();
	String getDateCreated();
	List<CustomerCareCaseMessage> getMessages();
	public enum CustomerCareCaseType{
		CUSTOMER_REVIEW,CUSTOMER_EMAIL,SALES_FEEDBACK,QNA,BUSINESS_ENQUIRES;
	}
	
	public interface CustomerCareCaseMessage{
		Integer getLineSeq();
		String getMessageType();
		String getCreateBy();
		String getDateCreate();	
		String getStartDate();
		String getEndDate();
		Boolean getIsFreeOfCharge();
		String getWordCount();
		String getStandardActionCount();
		String getChargeToSKU();
		String getTimeTaken();
		String getDRSChargeByWord();
		String getCostPerHour();
		String getMs2ssStatementId();
	    String getSs2spStatementId();
	    String getResponseTemplateId();
	    String getContents();
	    Boolean getIncludesTranslationFee();
	    boolean getIsModifiable();
	    public enum MessageType{
			CUSTOMER("customer_msg"),
			MSDC("reply_msg");
			private String value;
			MessageType(String value){
				this.value = value;
			}
			public String getValue(){
				return this.value;
			}
			public static MessageType fromValue(String value){
				for(MessageType type: MessageType.values()){
					if(type.getValue().equals(value)) return type;
				}
				return null;
			}
		}
	}		
}