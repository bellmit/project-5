package com.kindminds.drs.v1.model.impl;

import com.kindminds.drs.api.usecase.logistics.ViewUnsRecognizeRevenueReportUco.UnsGroupDetail;

import java.math.BigDecimal;

public class UnsGroupDetailImpl implements UnsGroupDetail {
	
	private String sku;
	private BigDecimal ddpPrice;
	private BigDecimal cifPrice;
	private Integer quantityPayment;
	private Integer quantityRefund;

	public UnsGroupDetailImpl(
			String sku,
			BigDecimal ddpPrice,
			BigDecimal cifPrice,
			Integer quantityPayment,
			Integer quantityRefund) {
		this.sku = sku;
		this.ddpPrice = ddpPrice;
		this.cifPrice = cifPrice;
		this.quantityPayment = quantityPayment;
		this.quantityRefund = quantityRefund;
	}
	
	private Integer calculateQuantityEffective(){
		return this.quantityPayment-this.quantityRefund;
	}
	
	private BigDecimal getUnitRevenue(){
		if(this.cifPrice==null) return this.ddpPrice;
		BigDecimal unitRevenue = ddpPrice.subtract(cifPrice);
		if(unitRevenue.signum()<=0) return BigDecimal.ZERO;
		return unitRevenue;
	}

	@Override
	public String getSku() {
		return this.sku;
	}

	@Override
	public String getDdpPrice() {
		return this.ddpPrice.setScale(2).toPlainString();
	}

	@Override
	public String getCifPrice() {
		if(this.cifPrice==null) return null;
		return this.cifPrice.setScale(2).toPlainString();
	}

	@Override
	public String getQuantityPayment() {
		return this.quantityPayment.toString();
	}

	@Override
	public String getQuantityRefund() {
		return this.quantityRefund.toString();
	}

	@Override
	public String getQuantityEffective() {
		return this.calculateQuantityEffective().toString();
	}

	@Override
	public String getSubtotalText() {
		return this.getSubtotal().toPlainString();
	}

	@Override
	public BigDecimal getSubtotal() {
		BigDecimal unitRevenue = this.getUnitRevenue();
		BigDecimal quantityEffective = new BigDecimal(this.calculateQuantityEffective());
		return unitRevenue.multiply(quantityEffective);
	}
	
	

}
