package com.kindminds.drs.impl;

import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseDto.CustomerCareCaseMessage;
import com.kindminds.drs.util.TestUtil;

public class CustomerCareCaseMessageImplForTest implements CustomerCareCaseMessage {

	private Integer lineSeq;
	private MessageType type;
	private String creatorName;
	private String dateCreate;
	private String dateStart;
	private String dateFinish;
	private Boolean isFreeOfCharge=false;
	private String numOfCharacters;
	private String numOfAction;
	private String relatedSku;
	private String timeTaken;
	private String chargeByDrs;
	private String costPerHour;
	private String ms2ssStatementName=null;
	private String ss2spStatementName=null;
	private String responseTemplateId;
	private String content;
	private Boolean includesTranslationFee;
	
	public CustomerCareCaseMessageImplForTest(
			Integer lineSeq,
			MessageType type,
			String creatorName,
			String dateCreate,
			String dateStart,
			String dateFinish,
			Boolean freeOfCharge,
			String timeTaken,
			String numOfCharacters,
			String numOfAction,
			String chargeByDrs,
			String relatedSku,
			String costPerHour,
			String responseTemplateId,
			String content,
			Boolean includesTranslationFee){
		this.lineSeq = lineSeq;
		this.type = type;
		this.creatorName = creatorName;
		this.dateCreate = dateCreate;
		this.dateStart = dateStart;
		this.dateFinish = dateFinish;
		this.isFreeOfCharge = freeOfCharge;
		this.numOfCharacters = numOfCharacters;
		this.numOfAction = numOfAction;
		this.relatedSku = relatedSku;
		this.timeTaken = timeTaken;
		this.chargeByDrs = chargeByDrs;
		this.costPerHour = costPerHour;
		this.responseTemplateId = responseTemplateId;
		this.content = content;
		this.includesTranslationFee = includesTranslationFee;
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj instanceof CustomerCareCaseMessage){
			CustomerCareCaseMessage cccm = (CustomerCareCaseMessage)obj;
			return this.getLineSeq().equals(cccm.getLineSeq())
				&& TestUtil.nullableEquals(this.getMessageType(),cccm.getMessageType())
				&& this.getCreateBy().equals(cccm.getCreateBy())
				//&& this.getDateCreate().equals(cccm.getDateCreate())
				//&& TestUtil.nullableEquals(this.getStartDate(),cccm.getStartDate())
				&& TestUtil.nullableEquals(this.getEndDate(),cccm.getEndDate())
				&& TestUtil.nullableEquals(this.getWordCount(),cccm.getWordCount())
				&& TestUtil.nullableEquals(this.getStandardActionCount(),cccm.getStandardActionCount())
				&& TestUtil.nullableEquals(this.getChargeToSKU(),cccm.getChargeToSKU())
				&& this.getIsFreeOfCharge()==cccm.getIsFreeOfCharge()
				&& TestUtil.nullableEquals(this.getTimeTaken(),cccm.getTimeTaken())
				&& TestUtil.nullableEquals(this.getDRSChargeByWord(),cccm.getDRSChargeByWord())
				&& TestUtil.nullableEquals(this.getCostPerHour(),cccm.getCostPerHour())
				&& TestUtil.nullableEquals(this.getMs2ssStatementId(),cccm.getMs2ssStatementId())
				&& TestUtil.nullableEquals(this.getSs2spStatementId(),cccm.getSs2spStatementId())
				&& this.getContents().equals(cccm.getContents());
		}
		return false;
	}

	@Override
	public String toString() {
		return "CustomerCareCaseMessageImplForTest [getLineSeq()=" + getLineSeq() + ", getMessageType()="
				+ getMessageType() + ", getCreateBy()=" + getCreateBy() + ", getDateCreate()=" + getDateCreate()
				+ ", getStartDate()=" + getStartDate() + ", getEndDate()=" + getEndDate() + ", getIsFreeOfCharge()="
				+ getIsFreeOfCharge() + ", getWordCount()=" + getWordCount() + ", getStandardActionCount()="
				+ getStandardActionCount() + ", getChargeToSKU()=" + getChargeToSKU() + ", getTimeTaken()="
				+ getTimeTaken() + ", getDRSChargeByWord()=" + getDRSChargeByWord() + ", getCostPerHour()="
				+ getCostPerHour() + ", getMs2ssStatementId()=" + getMs2ssStatementId() + ", getSs2spStatementId()="
				+ getSs2spStatementId() + ", getContents()=" + getContents() + ", getIsModifiable()="
				+ getIsModifiable() + "]";
	}

	@Override
	public Integer getLineSeq() {
		return this.lineSeq;
	}

	@Override
	public String getMessageType() {
		if(this.type==null){
			return null;
		}
		return this.type.getValue();
	}
	
	@Override
	public String getCreateBy() {
		return this.creatorName;
	}

	@Override
	public String getDateCreate() {
		return this.dateCreate;
	}

//	@Override
//	public String getDateReply() {
//		return null;
//	}

	@Override
	public String getStartDate() {
		return this.dateStart;
	}

	@Override
	public String getEndDate() {
		return this.dateFinish;
	}

	@Override
	public Boolean getIsFreeOfCharge() {
		return this.isFreeOfCharge;
	}

//	@Override
//	public String getReplacementRefundInvoices() {
//		return this.numOfInvoice;
//	}

	@Override
	public String getWordCount() {
		return this.numOfCharacters;
	}

	@Override
	public String getStandardActionCount() {
		return this.numOfAction;
	}

//	@Override
//	public String getExtraActions() {
//		return this.numOfAction;
//	}

	@Override
	public String getChargeToSKU() {
		return this.relatedSku;
	}

	@Override
	public String getTimeTaken() {
		return this.timeTaken;
	}

	@Override
	public String getDRSChargeByWord() {
		return this.chargeByDrs;
	}

	@Override
	public String getCostPerHour() {
		return this.costPerHour;
	}

	@Override
	public String getMs2ssStatementId() {
		return this.ms2ssStatementName;
	}

	@Override
	public String getSs2spStatementId() {
		return this.ss2spStatementName;
	}

	@Override
	public String getResponseTemplateId() {
		return this.responseTemplateId;
	}

	@Override
	public String getContents() {
		return this.content;
	}

	@Override
	public boolean getIsModifiable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Boolean getIncludesTranslationFee() {
		return this.includesTranslationFee;
	}

	

}
