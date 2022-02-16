package com.kindminds.drs.v1.model.impl.accounting;

import com.kindminds.drs.api.data.access.usecase.logistics.ViewUnsRecognizeRevenueReportDao.PaymentDetail;

import java.math.BigDecimal;

public class PaymentDetailImpl implements PaymentDetail {
	
	private Integer quantityPayment;
	private Integer quantityRefund;
	private BigDecimal ddpSubtotal;
	
	public PaymentDetailImpl(
			Integer quantityPayment,
			Integer quantityRefund,
			BigDecimal ddpSubtotal) {
		this.quantityPayment = quantityPayment;
		this.quantityRefund = quantityRefund;
		this.ddpSubtotal = ddpSubtotal;
	}
	
	@Override
	public Integer getQuantityPayment() {
		return quantityPayment;
	}
	
	@Override
	public Integer getQuantityRefund() {
		return quantityRefund;
	}
	
	@Override
	public BigDecimal getDdpSubtotal() {
		return ddpSubtotal;
	}
	

}
