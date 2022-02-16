package com.kindminds.drs.v1.model.impl.statement;

import java.math.BigDecimal;

import com.kindminds.drs.api.v1.model.report.Ss2spProfitShareDetailReport.Ss2spProfitShareDetailReportSkuProfitShareItem;

public class Ss2spProfitShareDetailReportSkuProfitShareItemImpl implements Ss2spProfitShareDetailReportSkuProfitShareItem {
	
	private String sku;
	private String name;
	private Integer shippedQty;
	private Integer refundedQty;
	private BigDecimal subtotal;
	
	public Ss2spProfitShareDetailReportSkuProfitShareItemImpl(String sku,String name,Integer shippedQty,Integer refundedQty,BigDecimal subtotal) {
		this.sku = sku;
		this.name = name;
		this.shippedQty = shippedQty;
		this.refundedQty = refundedQty;
		this.subtotal = subtotal;
	}
	
	public BigDecimal getRawSubtotal(){ return this.subtotal; }

	@Override
	public String getSku() {
		return this.sku;
	}
	
	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public String getShippedQty() {
		return this.shippedQty.toString();
	}

	@Override
	public String getRefundedQty() {
		return this.refundedQty.toString();
	}

	@Override
	public String getSubtotal() {
		return this.subtotal.toPlainString();
	}

}
