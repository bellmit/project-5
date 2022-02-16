package com.kindminds.drs.v1.model.impl;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.report.InventoryPaymentReport;


import java.math.BigDecimal;
import java.util.List;

public class PaymentRecordReportImpl implements InventoryPaymentReport {
	
	private String shipmentName;
	private Currency currency;
	private List<InventoryPaymentReportQuantitySummaryLineItem> quantitySummarylineItems=null;
	private List<InventoryPaymentReportAmountDetailLineItem> amountDetailLineItems=null;
	
	public void setShipmentName(String name){ this.shipmentName = name; }
	public void setCurrency(Currency cur){ this.currency = cur; }
	public void setQuantitySummaryLineItems(List<InventoryPaymentReportQuantitySummaryLineItem> items){
		this.quantitySummarylineItems = items; }

	public void setAmountDetailLineItems(List<InventoryPaymentReportAmountDetailLineItem> items){ this.amountDetailLineItems = items; }

	@Override
	public String toString() {
		return "PaymentRecordReportImpl [getShipmentName()=" + getShipmentName() + ", getCurrency()=" + getCurrency()
				+ ", getLineItems()=" + getAmountDetailLineItems() + ", getSubtotal()=" + getAmountSubtotal() + "]";
	}
	
	@Override
	public String getShipmentName() {
		return this.shipmentName;
	}

	@Override
	public String getCurrency() {
		return this.currency.name();
	}

	@Override @SuppressWarnings("unchecked")
	public String getAmountSubtotal() {
		BigDecimal subtotal = BigDecimal.ZERO;
		List<PaymentRecordReportAmountDetailLineItemImpl> listInSubType = (List<PaymentRecordReportAmountDetailLineItemImpl>)((List<?>)this.amountDetailLineItems);
		for(PaymentRecordReportAmountDetailLineItemImpl item:listInSubType){
			subtotal = subtotal.add(item.getNumericAmount());
		}
		return subtotal.setScale(this.currency.getScale()).toPlainString();
	}
	
	@Override
	public List<InventoryPaymentReportQuantitySummaryLineItem> getQuantitySummaryLineItems() {
		return this.quantitySummarylineItems;
	}
	
	@Override
	public List<InventoryPaymentReportAmountDetailLineItem> getAmountDetailLineItems() {
		return this.amountDetailLineItems;
	}

}
