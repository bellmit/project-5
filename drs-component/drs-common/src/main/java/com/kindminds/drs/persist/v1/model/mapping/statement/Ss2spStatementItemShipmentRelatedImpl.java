package com.kindminds.drs.persist.v1.model.mapping.statement;

import java.math.BigDecimal;
import org.springframework.util.Assert;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.report.Ss2spStatementReport.Ss2spStatementItemShipmentRelated;

 //@IdClass(Ss2spStatementItemPaymentAndRefundId.class)
public class Ss2spStatementItemShipmentRelatedImpl implements Ss2spStatementItemShipmentRelated {
	
	//@Id //@Column(name="shipment_name")
	private String shipmentName;
	//@Column(name="shipment_invoice")
	private String relatedShipmentInvoice;
	//@Column(name="currency_id")
	private Integer currencyId;
	//@Id //@Column(name="amount")
	private BigDecimal amount;

	 public Ss2spStatementItemShipmentRelatedImpl() {
	 }

	 public Ss2spStatementItemShipmentRelatedImpl(String shipmentName, String relatedShipmentInvoice, Integer currencyId, BigDecimal amount) {
		 this.shipmentName = shipmentName;
		 this.relatedShipmentInvoice = relatedShipmentInvoice;
		 this.currencyId = currencyId;
		 this.amount = amount;
	 }

	 @Override
	public String toString() {
		return "Ss2spStatementItemShipmentRelatedImpl [getItemName()=" + getItemName() + ", getCurrency()="
				+ getCurrency() + ", getAmountStr()=" + getAmountStr() + ", getNoteText()=" + getNoteText()
				+ ", getSourceShipmentName()=" + getSourceShipmentName() + ", getSourceShipmentInvoiceNumber()="
				+ getSourceShipmentInvoiceNumber() + ", getAmount()=" + getAmount() + "]";
	}

	@Override
	public String getItemName() {
		return "ss2spStatement.PaymentAndRefund";
	}
	
	@Override
	public String getCurrency() {
		Currency currency = Currency.fromKey(this.currencyId);
		Assert.notNull(currency);
		return currency.name();
	}
	
	@Override
	public String getAmountStr() {
		Currency currency = Currency.fromKey(this.currencyId);
		return this.amount.setScale(currency.getScale()).toString();
	}
	
	@Override
	public String getNoteText() {
		return "ss2spStatement.noteForPaymentRefund";
	}
	
	@Override
	public String getSourceShipmentName() {
		return this.shipmentName;
	}
	
	@Override
	public String getSourceShipmentInvoiceNumber() {
		return this.relatedShipmentInvoice;
	}
	
	@Override
	public BigDecimal getAmount() {
		return this.amount;
	}
	
}
