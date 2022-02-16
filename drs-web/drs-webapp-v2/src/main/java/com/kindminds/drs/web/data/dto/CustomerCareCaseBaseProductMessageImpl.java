package com.kindminds.drs.web.data.dto;

import org.springframework.util.StringUtils;

import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseDto.CustomerCareCaseMessage;

public class CustomerCareCaseBaseProductMessageImpl implements CustomerCareCaseMessage{

	private Integer lineSeq;
	private String responseTemplateId;
	private String ms2ssStatementId;
	private String ss2spStatementId;
	private String messageType;
	private String dateCreate;	
	private String startDate;
	private String endDate;
	private Boolean isFreeOfCharge;	
	private String wordCount;
	private String standardActionCount;
	private String chargeToSKU;
	private String timeTaken;
	private String DRSChargeByWord;
	private String costPerHour;
	private String contents;
	private String createBy;
	private boolean isModifiable;
	private Boolean includesTranslationFee;
    
	public CustomerCareCaseBaseProductMessageImpl(){};
	
	public CustomerCareCaseBaseProductMessageImpl(CustomerCareCaseMessage message){
		this.lineSeq = message.getLineSeq();
		this.responseTemplateId = message.getResponseTemplateId();
		this.ms2ssStatementId = message.getMs2ssStatementId();
		this.ss2spStatementId = message.getSs2spStatementId();		
		this.messageType = message.getMessageType();
		this.dateCreate = message.getDateCreate();
		this.startDate = message.getStartDate();
		this.endDate = message.getEndDate();
		this.isFreeOfCharge = message.getIsFreeOfCharge();
		this.wordCount = message.getWordCount();
		this.standardActionCount = message.getStandardActionCount(); 
		this.chargeToSKU = message.getChargeToSKU();
		this.timeTaken = message.getTimeTaken();
		this.DRSChargeByWord = message.getDRSChargeByWord();
		this.costPerHour = message.getCostPerHour();
		this.contents = message.getContents();
		this.createBy = message.getCreateBy();
		this.isModifiable = message.getIsModifiable();
		this.includesTranslationFee = message.getIncludesTranslationFee();
	};
	
	@Override
	public Integer getLineSeq() {		
		return this.lineSeq;
	}
	
	public void setLineSeq(Integer lineseq) {		
		this.lineSeq = lineseq;
	}
	
	@Override
	public String getResponseTemplateId() {
		return StringUtils.hasText(this.responseTemplateId)?this.responseTemplateId:null;
	}
	
	public void setResponseTemplateId(String responseTemplateId) {
		this.responseTemplateId = responseTemplateId;		
	}
	
	@Override
	public String getMessageType() {		
		return this.messageType;
	}

    public void setMessageType(String messageType) {
    	this.messageType = messageType;    	
    }
		
	@Override
	public String getDateCreate() {		
		return this.dateCreate;
	}

	public void setDateCreate(String dateCreate) {
		this.dateCreate = dateCreate;		
	}
		
	@Override
	public String getStartDate() {		
		return this.startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;		
	}
	
	@Override
	public String getEndDate() {		
		return this.endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;				
	}
		
	@Override
	public Boolean getIsFreeOfCharge() {		
		return this.isFreeOfCharge;
	}

	public void setIsFreeOfCharge(Boolean isFreeOfCharge) {
		this.isFreeOfCharge = isFreeOfCharge; 		
	}
		
		
	@Override
	public String getWordCount() {		
		return this.wordCount;
	}

	public void setWordCount(String wordCount) {
		this.wordCount = wordCount;		
	}
		
	@Override
	public String getStandardActionCount() {		
		return this.standardActionCount;
	}
	
	public void setStandardActionCount(String standardActionCount) {		
		this.standardActionCount = standardActionCount; 
	}

	@Override
	public String getChargeToSKU() {		
		return this.chargeToSKU;
	}

	public void setChargeToSKU(String chargeToSKU) {
		this.chargeToSKU = chargeToSKU; 		
	}
	
	@Override
	public String getTimeTaken() {		
		return this.timeTaken;
	}

	public void setTimeTaken(String timeTaken) {
		this.timeTaken = timeTaken;		
	}
		
	@Override
	public String getDRSChargeByWord() {		
		return this.DRSChargeByWord;
	}
	
	public void setDRSChargeByWord(String DRSChargeByWord) {
		this.DRSChargeByWord = DRSChargeByWord; 		
	}

	@Override
	public String getCostPerHour() {		
		return this.costPerHour;
	}
	
	public void setCostPerHour(String costPerHour) {
		this.costPerHour = costPerHour;		
	}
	
	@Override
	public String getContents() {		
		return this.contents;
	}

	public void setContents(String contents) {
		this.contents = contents;		
	}

	@Override
	public String getCreateBy() {		
		return this.createBy;
	}
	
	public void setCreateBy(String createBy) {
		this.createBy = createBy; 		
	}

	@Override
	public String getMs2ssStatementId() {		
		return this.ms2ssStatementId;
	}

	public void setMs2ssStatementId(String ms2ssStatementId){
		this.ms2ssStatementId = ms2ssStatementId;		
	}

	@Override
	public String getSs2spStatementId() {		
		return this.ss2spStatementId;
	}
	
	public void setSs2spStatementId(String ss2spStatementId){
		this.ss2spStatementId = ss2spStatementId; 		
	}

	@Override
	public boolean getIsModifiable() {		
		return this.isModifiable;
	}
	
	public void setIsModifiable(boolean isModifiable) {
		this.isModifiable = isModifiable;		
	}

	@Override
	public Boolean getIncludesTranslationFee() {
		return this.includesTranslationFee;
	}
	
	public void setIncludesTranslationFee(boolean includesTranslationFee) {
		this.includesTranslationFee = includesTranslationFee;
	}
		
}