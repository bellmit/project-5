package com.kindminds.drs.persist.v1.model.mapping.customercare;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;





import org.springframework.util.Assert;

import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseDto.CustomerCareCaseMessage;
import com.kindminds.drs.util.DateHelper;


public class CustomerCareCaseMessageImpl implements CustomerCareCaseMessage {

	//@Id ////@Column(name="id")
	private int id;
	//@Column(name="line_seq")
	private int lineSeq;
	//@Column(name="type")
	private String type;
	//@Column(name="creator_name")
	private String creatorName;
	//@Column(name="date_create")
	private String dateCreate;
	//@Column(name="date_start")
	private String dateStart;
	//@Column(name="date_finish")
	private String dateFinish;	
	//@Column(name="time_taken")
	private String timeTaken;	
	//@Column(name="related_sku")
	private String relatedSku;
	//@Column(name="is_free_of_charge")
	private Boolean isFreeOfCharge;
//	//@Column(name="num_of_invoice")
//	private String numOfInvoice;
	//@Column(name="num_of_character")
	private String numOfCharacter;
	//@Column(name="num_of_action")
	private String numOfAction;
	//@Column(name="charge_by_drs")
	private BigDecimal chargeByDrs;
	//@Column(name="message")
	private String message;
	//@Column(name="ms2ss_statement_name")
	private String ms2ssStatementName;
	//@Column(name="ss2sp_statement_name")
	private String ss2spStatementName;
	//@Column(name="includes_translation_fee")
	private Boolean includesTranslationFee;



	public CustomerCareCaseMessageImpl(int id, int lineSeq, String type, String creatorName,
									   String dateCreate, String dateStart, String dateFinish, String timeTaken, String relatedSku,
									   Boolean isFreeOfCharge, String numOfCharacter, String numOfAction, BigDecimal chargeByDrs, String message, String ms2ssStatementName, String ss2spStatementName, Boolean includesTranslationFee) {

		this.id = id;
		this.lineSeq = lineSeq;
		this.type = type;
		this.creatorName = creatorName;
		this.dateCreate = dateCreate;
		this.dateStart = dateStart;
		this.dateFinish = dateFinish;
		this.timeTaken = timeTaken;
		this.relatedSku = relatedSku;
		this.isFreeOfCharge = isFreeOfCharge;
		this.numOfCharacter = numOfCharacter;
		this.numOfAction = numOfAction;
		this.chargeByDrs = chargeByDrs;
		this.message = message;
		this.ms2ssStatementName = ms2ssStatementName;
		this.ss2spStatementName = ss2spStatementName;
		this.includesTranslationFee = includesTranslationFee;
	}

	@Override
	public String toString() {
		return "CustomerCareCaseMessageImpl [getLineSeq()=" + getLineSeq()
				+ ", getMessageType()=" + getMessageType() + ", getCreateBy()="
				+ getCreateBy() + ", getDateCreate()=" + getDateCreate()
				+ ", getStartDate()=" + getStartDate() + ", getEndDate()="
				+ getEndDate() + ", getIsFreeOfCharge()=" + getIsFreeOfCharge()
				+ ", getWordCount()=" + getWordCount()
				+ ", getStandardActionCount()=" + getStandardActionCount()
				+ ", getChargeToSKU()=" + getChargeToSKU()
				+ ", getTimeTaken()=" + getTimeTaken()
				+ ", getDRSChargeByWord()=" + getDRSChargeByWord()
				+ ", getCostPerHour()=" + getCostPerHour()
				+ ", getMs2ssStatementId()=" + getMs2ssStatementId()
				+ ", getSs2spStatementId()=" + getSs2spStatementId()
				+ ", getContents()=" + getContents() + ", getIsModifiable()="
				+ getIsModifiable() + "]";
	}

	@Override
	public Integer getLineSeq() {
		return this.lineSeq;
	}

	@Override
	public String getMessageType() {
		Assert.notNull(MessageType.fromValue(this.type));
		return this.type;
	}

	@Override
	public String getCreateBy() {
		return this.creatorName;
	}
	
	@Override
	public String getDateCreate() {
		return this.dateCreate;
	}

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

	@Override
	public String getWordCount() {
		return this.numOfCharacter;
	}

	@Override
	public String getStandardActionCount() {
		return this.numOfAction;
	}

	@Override
	public String getChargeToSKU() {
		return this.relatedSku;
	}

	@Override
	public String getTimeTaken() {		
		Date start  = DateHelper.toDate(this.dateStart,"yyyy-MM-dd HH:mm:ss Z");
		Date finish = DateHelper.toDate(this.dateFinish,"yyyy-MM-dd HH:mm:ss Z");
		if(start==null) return this.timeTaken;
		long milliSecondsDiff = finish.getTime()-start.getTime();
		double diffSeconds = milliSecondsDiff/1000;
		BigDecimal minute = BigDecimal.valueOf(diffSeconds/60.0);
		String time = minute.setScale(0, RoundingMode.UP).stripTrailingZeros().toPlainString();
		return time;				
		/*
		Date start  = DateHelper.toDate(this.dateStart,"yyyy-MM-dd HH:mm:ss Z");
		Date finish = DateHelper.toDate(this.dateFinish,"yyyy-MM-dd HH:mm:ss Z");
		if(start==null||finish==null) return null;
		long milliSecondsDiff = finish.getTime()-start.getTime();
		long diffSeconds = milliSecondsDiff / 1000 % 60;
		long diffMinutes = milliSecondsDiff / (60 * 1000) % 60;
		long diffHours = milliSecondsDiff / (60 * 60 * 1000) % 24;
		String time = String.format("%02d:%02d:%02d", diffHours, diffMinutes, diffSeconds);
		return time;
		*/
	}

	@Override
	public String getDRSChargeByWord() {
		if(this.chargeByDrs!=null){
			return this.chargeByDrs.stripTrailingZeros().toPlainString();
		}
		return null;
	}

	@Override
	public String getCostPerHour() {
		Date start  = DateHelper.toDate(this.dateStart,"yyyy-MM-dd HH:mm:ss Z");
		Date finish = DateHelper.toDate(this.dateFinish,"yyyy-MM-dd HH:mm:ss Z");				
		if(this.type.equals("customer_msg")) return null;	
		BigDecimal drsChargeByWord = new BigDecimal(this.getDRSChargeByWord());									
		if(start==null){
			BigDecimal seconds = new BigDecimal(this.getTimeTaken()).multiply(new BigDecimal(60));
			BigDecimal hour = BigDecimal.valueOf(seconds.doubleValue()/3600.0);			 
			return drsChargeByWord.divide(hour, 2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString();									
		}
		double milliSecondsDiff = finish.getTime()-start.getTime();
		double diffSeconds = milliSecondsDiff/1000;
		BigDecimal hour = BigDecimal.valueOf(diffSeconds/3600.0);
		return drsChargeByWord.divide(hour, 2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString();
		/*
		Date start  = DateHelper.toDate(this.dateStart,"yyyy-MM-dd HH:mm:ss Z");
		Date finish = DateHelper.toDate(this.dateFinish,"yyyy-MM-dd HH:mm:ss Z");
		if(start==null||finish==null) return null;
		BigDecimal drsChargeByWord = new BigDecimal(this.getDRSChargeByWord());
		double milliSecondsDiff = finish.getTime()-start.getTime();
		double diffSeconds = milliSecondsDiff/1000;
		BigDecimal hour = new BigDecimal(diffSeconds/3600.0);
		return drsChargeByWord.divide(hour, 2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString();
		*/
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
		return null;
	}

	@Override
	public String getContents() {
		return this.message;
	}

	@Override
	public Boolean getIncludesTranslationFee() {
		return this.includesTranslationFee;
	}

	@Override
	public boolean getIsModifiable() {
		return this.ms2ssStatementName==null&&this.ss2spStatementName==null;
	}
}
