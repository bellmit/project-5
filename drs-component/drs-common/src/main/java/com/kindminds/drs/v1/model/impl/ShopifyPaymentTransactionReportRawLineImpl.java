package com.kindminds.drs.v1.model.impl;

import com.kindminds.drs.api.v1.model.shopify.ShopifyPaymentTransactionReportRawLine;
import com.kindminds.drs.util.DateHelper;

import java.math.BigDecimal;
import java.util.Date;

public class ShopifyPaymentTransactionReportRawLineImpl implements ShopifyPaymentTransactionReportRawLine {

	private Date transactionDate;
	private String type;
	private String orderName;
	private String cardBrand;
	private String cardSource;
	private String payoutStatus;
	private Date payoutDate;
	private BigDecimal amount;
	private BigDecimal fee;
	private BigDecimal net;
	private String checkout;
	private String paymentMethodName;
	private BigDecimal presentmentAmount;
	private String presentmentCurrency;
	private String currency;

	public ShopifyPaymentTransactionReportRawLineImpl(
			String transactionDate,
			String type,
			String orderName,
			String cardBrand,
			String cardSource,
			String payoutStatus,
			String payoutDate,
			String amount,
			String fee,
			String net,
			String checkout,
			String paymentMethodName,
			String presentmentAmount,
			String presentmentCurrency,
			String currency){
		this.transactionDate = DateHelper.toDate(transactionDate,"yyyy-MM-dd HH:mm:ss z");
		this.type = type;
		this.orderName = orderName;
		this.cardBrand = cardBrand;
		this.cardSource = cardSource;
		this.payoutStatus = payoutStatus;
		this.payoutDate = DateHelper.toDate(payoutDate,"yyyy-MM-dd z");
		this.amount = new BigDecimal(amount);
		this.fee = new BigDecimal(fee);
		this.net = new BigDecimal(net);
		this.checkout = checkout;
		this.paymentMethodName = paymentMethodName;
		this.presentmentAmount = new BigDecimal(presentmentAmount);
		this.presentmentCurrency = presentmentCurrency;
		this.currency = currency;
	}

	@Override
	public Date getTransactionDate() {
		return this.transactionDate;
	}

	@Override
	public String getType() {
		return this.type;
	}

	@Override
	public String getOrderName() {
		return this.orderName;
	}

	@Override
	public String getCardBrand() {
		return this.cardBrand;
	}

	@Override
	public String getCardSource() {
		return this.cardSource;
	}

	@Override
	public String getPayoutStatus() {
		return this.payoutStatus;
	}

	@Override
	public Date getPayoutDate() {
		return this.payoutDate;
	}

	@Override
	public BigDecimal getAmount() {
		return this.amount;
	}

	@Override
	public BigDecimal getFee() {
		return this.fee;
	}

	@Override
	public BigDecimal getNet() {
		return this.net;
	}

	@Override
	public String getCheckout() {
		return checkout;
	}

	@Override
	public String getPaymentMethodName() {
		return paymentMethodName;
	}

	@Override
	public BigDecimal getPresentmentAmount() {
		return presentmentAmount;
	}

	@Override
	public String getPresentmentCurrency() {
		return presentmentCurrency;
	}

	@Override
	public String getCurrency() {
		return currency;
	}
}
