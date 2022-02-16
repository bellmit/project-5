package com.kindminds.drs.persist.v1.model.mapping.accounting;

import java.math.BigDecimal;
import java.util.Date;






import org.springframework.util.Assert;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.accounting.Remittance;
import com.kindminds.drs.util.DateHelper;


public class RemittanceImpl implements Remittance {

	//@Id //@Column(name="id",updatable=false)
	private int id;
	//@Column(name="date_sent")
	private Date dateSent;
	//@Column(name="date_rcvd")
	private Date dateRcvd;
	//@Column(name="sndr_company_kcode")
	private String sndrCompanyKcode;
	//@Column(name="rcvr_company_kcode")
	private String rcvrCompanyKcode;
	//@Column(name="amount")
	private BigDecimal amount;
	//@Column(name="currency_id")
	private Integer currencyId;
	//@Column(name="reference")
	private String reference;
	//@Column(name="fee_amount")
	private BigDecimal feeAmount;
	//@Column(name="fee_included")
	private Boolean feeIncluded;
	//@Column(name="statement_name")
	private String statementName;
	//@Column(name="bank_payment")
	private BigDecimal bankPayment;

	public RemittanceImpl() {
	}

	public RemittanceImpl(int id, Date dateSent, Date dateRcvd, String sndrCompanyKcode, String rcvrCompanyKcode, BigDecimal amount, Integer currencyId, String reference, BigDecimal feeAmount, Boolean feeIncluded, String statementName, BigDecimal bankPayment) {
		this.id = id;
		this.dateSent = dateSent;
		this.dateRcvd = dateRcvd;
		this.sndrCompanyKcode = sndrCompanyKcode;
		this.rcvrCompanyKcode = rcvrCompanyKcode;
		this.amount = amount;
		this.currencyId = currencyId;
		this.reference = reference;
		this.feeAmount = feeAmount;
		this.feeIncluded = feeIncluded;
		this.statementName = statementName;
		this.bankPayment = bankPayment;
	}

	@Override
	public String toString() {
		return "RemittanceImpl [getRemittanceId()=" + getRemittanceId() + ", getUtcDateSent()=" + getUtcDateSent()
				+ ", getUtcDateReceived()=" + getUtcDateReceived() + ", getSender()=" + getSender() + ", getReceiver()="
				+ getReceiver() + ", getCurrency()=" + getCurrency() + ", getAmount()=" + getAmount()
				+ ", getReference()=" + getReference() + "]";
	}

	@Override
	public int getRemittanceId() {
		return this.id;
	}

	@Override
	public String getUtcDateSent() {
		return this.dateSent==null?null:DateHelper.toString(this.dateSent, "yyyy-MM-dd", "UTC");
	}

	@Override
	public String getUtcDateReceived() {
		return this.dateRcvd==null?null:DateHelper.toString(this.dateRcvd, "yyyy-MM-dd", "UTC");
	}

	@Override
	public String getSender() {
		return this.sndrCompanyKcode;
	}

	@Override
	public String getReceiver() {
		return this.rcvrCompanyKcode;
	}

	@Override
	public Currency getCurrency() {
		return Currency.fromKey(this.currencyId);
	}

	@Override
	public String getAmount() {
		Currency currency = this.getCurrency();
		Assert.isTrue(this.amount.stripTrailingZeros().scale()<=currency.getScale());
		return this.amount.setScale(currency.getScale()).toString();
	}

	public String getFeeAmount() {
		Currency currency = this.getCurrency();
		Assert.isTrue(this.feeAmount.stripTrailingZeros().scale()<=currency.getScale());
		return this.feeAmount.setScale(currency.getScale()).toString();
	}

	public String getFeeIncluded() {
		if (feeIncluded) {
			return "Yes";
		}
		return "No";
	}

	public String getStatementName() {
		return this.statementName;
	}

	public String getBankPayment() {
		Currency currency = this.getCurrency();
		Assert.isTrue(this.bankPayment.stripTrailingZeros().scale()<=currency.getScale());
		return this.bankPayment.setScale(currency.getScale()).toString();
	}

	@Override
	public String getReference() {
		return this.reference;
	}

}
