package com.kindminds.drs.persist.v1.model.mapping.statement;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.report.Ms2ssPaymentAndRefundReport.Ms2ssPaymentAndRefundReportItem;

 //@IdClass(Ss2spPaymentAndRefundItemId.class)
public class Ms2ssPaymentAndRefundReportItemImpl implements Ms2ssPaymentAndRefundReportItem {

	//@Id //@Column(name="sku")
	private String sku;
	//@Column(name="sku_name")
	private String skuName;
	//@Id //@Column(name="type")
	private String itemType;
	//@Column(name="original_amount_per_unit")
	private BigDecimal unitAmount;
	//@Column(name="quantity")
	private Integer quantity;
	//@Column(name="original_amount")
	private BigDecimal totalAmount;
	//@Column(name="original_currency_name")
	private String currencyName;
	//@Column(name="transactionlinetype_id")
	private Integer settleableItemId;

	 public Ms2ssPaymentAndRefundReportItemImpl() {
	 }

	 public Ms2ssPaymentAndRefundReportItemImpl(String sku, String skuName, String itemType, BigDecimal unitAmount, Integer quantity, BigDecimal totalAmount, String currencyName, Integer settleableItemId) {
		 this.sku = sku;
		 this.skuName = skuName;
		 this.itemType = itemType;
		 this.unitAmount = unitAmount;
		 this.quantity = quantity;
		 this.totalAmount = totalAmount;
		 this.currencyName = currencyName;
		 this.settleableItemId = settleableItemId;
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
		return Currency.valueOf(this.currencyName);
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
