package com.kindminds.drs.persist.v1.model.mapping.accounting;

import java.math.BigDecimal;





import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.report.RemittanceReport.RemittanceReportItem;


public class RemittanceReportItemImpl implements RemittanceReportItem {
	
	//@Id ////@Column(name="id")
	private int id;
	//@Column(name="date_sent")
	private String dateSent;
	//@Column(name="date_received")
	private String dateRcvd;
	//@Column(name="currency_name")
	private String currencyName;
	//@Column(name="amount")
	private BigDecimal amount;
	//@Column(name="reference")
	private String reference;

	public RemittanceReportItemImpl() {
	}

	public RemittanceReportItemImpl(int id, String dateSent, String dateRcvd, String currencyName, BigDecimal amount, String reference) {
		this.id = id;
		this.dateSent = dateSent;
		this.dateRcvd = dateRcvd;
		this.currencyName = currencyName;
		this.amount = amount;
		this.reference = reference;
	}

	@Override
	public String getDateSent() {
		return this.dateSent;
	}
	
	@Override
	public String getDateRcvd() {
		return this.dateRcvd;
	}
	
	@Override
	public String getAmountStr() {
		Currency c = Currency.valueOf(this.currencyName);
		return this.amount.setScale(c.getScale(), BigDecimal.ROUND_HALF_UP).toString();
	}
	
	@Override
	public Currency getCurrency() {
		return Currency.valueOf(this.currencyName);
	}
	
	@Override
	public BigDecimal getAmount() {
		return this.amount;
	}
	
	@Override
	public String getReference() {
		return this.reference;
	}

}
