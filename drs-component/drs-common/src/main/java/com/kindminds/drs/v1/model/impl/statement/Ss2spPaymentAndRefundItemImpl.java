package com.kindminds.drs.v1.model.impl.statement;

import java.math.BigDecimal;
import java.math.RoundingMode;
import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.report.Ss2spPaymentAndRefundReport.Ss2spPaymentAndRefundReportItem;

public class Ss2spPaymentAndRefundItemImpl implements Ss2spPaymentAndRefundReportItem{
	
	private String sku;
	private String skuName;
	private String itemType;
	private BigDecimal unitAmount;
	private Integer quantity;
	private BigDecimal totalAmount;
	private Integer currencyId;
	private Integer settleableItemId;
	
	public Ss2spPaymentAndRefundItemImpl(
			String sku,
			String skuName,
			String itemType,
			Integer quantity,
			BigDecimal totalAmount,
			Integer currencyId,
			Integer settleableItemId) {
		super();
		this.sku = sku;
		this.skuName = skuName;
		this.itemType = itemType;
		this.quantity = quantity;
		this.totalAmount = totalAmount;
		this.currencyId = currencyId;
		this.settleableItemId = settleableItemId;
	}

	public void setUnitAmount(BigDecimal unitAmount) {
		this.unitAmount = unitAmount;
	}
	
	@Override
	public String getSku() {
		return this.sku;
	}
	
	@Override
	public String getSkuName() {
		return this.skuName;
	}
	
	@Override
	public String getItemType() {
		return this.itemType;
	}
	
	@Override
	public BigDecimal getUnitAmount() {
		return this.unitAmount;
	}
	
	@Override
	public Integer getQuantity() {
		return this.quantity;
	}
	
	@Override
	public String getTotalAmount() {
		return this.totalAmount.setScale(this.getCurrency().getScale(), RoundingMode.HALF_UP).toString();
	}
	
	@Override
	public BigDecimal getNumericTotalAmount() {
		return this.totalAmount;
	}
	
	@Override
	public Currency getCurrency() {
		return Currency.fromKey(this.currencyId);
	}
	
	@Override
	public Integer getSettleableItemId() {
		return this.settleableItemId;
	}
	
	@Override
	public String toString() {
		return "Ss2spPaymentAndRefundReportItemImplForTest [getSku()="
				+ getSku() + ", getItemType()=" + getItemType()
				+ ", getUnitAmount()=" + getUnitAmount() + ", getQuantity()="
				+ getQuantity() + ", getTotalAmount()=" + getTotalAmount()
				+ ", getNumericTotalAmount()=" + getNumericTotalAmount()
				+ ", getCurrency()=" + getCurrency() + "]";
	}
	

}