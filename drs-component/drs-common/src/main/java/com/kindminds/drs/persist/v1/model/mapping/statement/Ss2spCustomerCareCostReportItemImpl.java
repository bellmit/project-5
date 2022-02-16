package com.kindminds.drs.persist.v1.model.mapping.statement;

import java.math.BigDecimal;


import com.kindminds.drs.api.v1.model.report.Ss2spCustomerCareCostReport;
import org.springframework.util.Assert;

import com.kindminds.drs.Currency;


public class Ss2spCustomerCareCostReportItemImpl implements Ss2spCustomerCareCostReport.Ss2spCustomerCareCostReportItem {

	//@Id ////@Column(name="id")
	private int id;
	//@Column(name="date_time_utc")
	private String dateTimeUtc;
	//@Column(name="product_sku")
	private String productSku;
	//@Column(name="case_id")
	private Integer caseId;
	//@Column(name="message_id")
	private Integer messageLineSeq;
	//@Column(name="currency")
	private String currency;
	//@Column(name="amount")
	private BigDecimal amount;

	public Ss2spCustomerCareCostReportItemImpl() {
	}

	public Ss2spCustomerCareCostReportItemImpl(int id, String dateTimeUtc, String productSku, Integer caseId, Integer messageLineSeq, String currency, BigDecimal amount) {
		this.id = id;
		this.dateTimeUtc = dateTimeUtc;
		this.productSku = productSku;
		this.caseId = caseId;
		this.messageLineSeq = messageLineSeq;
		this.currency = currency;
		this.amount = amount;
	}

	private Currency getEnumCurrency(){
		Currency c = Currency.valueOf(this.currency);
		Assert.notNull(c);
		return c;
	}
	
	@Override
	public String toString() {
		return "Ss2spCustomerCareCostReportItemImpl [getEnumCurrency()=" + getEnumCurrency() + ", getDateTimeUtc()="
				+ getDateTimeUtc() + ", getProductSku()=" + getProductSku() + ", getCaseId()=" + getCaseId()
				+ ", getMessageLineSeq()=" + getMessageLineSeq() + ", getCurrency()=" + getCurrency()
				+ ", getAmountStr()=" + getAmountStr() + ", getAmount()=" + getAmount() + "]";
	}
	
	@Override
	public String getDateTimeUtc() {
		return this.dateTimeUtc;
	}
	
	@Override
	public String getProductSku(){
		return this.productSku;
	}
	
	@Override
	public String getCaseId() {
		return this.caseId.toString();
	}
	
	@Override
	public String getMessageLineSeq() {
		return this.messageLineSeq.toString();
	}
	
	@Override
	public String getCurrency() {
		return this.getEnumCurrency().name();
	}
	
	@Override
	public String getAmountStr() {
		return this.amount.toPlainString();
	}

	@Override
	public BigDecimal getAmount() {
		return this.amount;
	}

}
