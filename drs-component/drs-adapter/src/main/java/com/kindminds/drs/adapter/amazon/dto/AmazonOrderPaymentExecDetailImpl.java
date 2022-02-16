package com.kindminds.drs.adapter.amazon.dto;

import java.math.BigDecimal;

import com.amazonservices.mws.orders._2013_09_01.model.PaymentExecutionDetailItem;
import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.amazon.AmazonOrder.AmazonOrderPaymentExecDetail;

public class AmazonOrderPaymentExecDetailImpl implements AmazonOrderPaymentExecDetail {

	private String paymentMethod;
	private Currency currency;
	private BigDecimal amount;
	
	public AmazonOrderPaymentExecDetailImpl(PaymentExecutionDetailItem orig){
		this.paymentMethod = orig.getPaymentMethod();
		this.currency = Currency.valueOf(orig.getPayment().getCurrencyCode());
		this.amount = new BigDecimal(orig.getPayment().getAmount());
	}
	
	@Override
	public String getPaymentMethod() {
		return this.paymentMethod;
	}

	@Override
	public Currency getCurrency() {
		return this.currency;
	}

	@Override
	public BigDecimal getAmount() {
		return this.amount;
	}

}
