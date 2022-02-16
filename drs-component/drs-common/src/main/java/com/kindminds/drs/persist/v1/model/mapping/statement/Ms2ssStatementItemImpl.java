package com.kindminds.drs.persist.v1.model.mapping.statement;

import java.math.BigDecimal;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.report.Ms2ssStatement.Ms2ssStatementItem;

 //@IdClass(Ms2ssStatementItemId.class)
public class Ms2ssStatementItemImpl implements Ms2ssStatementItem {
	
	//@Id //@Column(name="item_type")
	private String type;
	//@Id //@Column(name="shipment_name")
	private String shipmentName;
	//@Column(name="amount")
	private BigDecimal amount;
	//@Column(name="invoice_number")
	private String invoiceNumber;

	 public Ms2ssStatementItemImpl() {
	 }

	 public Ms2ssStatementItemImpl(String type, String shipmentName, BigDecimal amount, String invoiceNumber) {
		 this.type = type;
		 this.shipmentName = shipmentName;
		 this.amount = amount;
		 this.invoiceNumber = invoiceNumber;
	 }

	 private Ms2ssStatementItemType getType() {
		return Ms2ssStatementItemType.valueOf(this.type);
	}
	
	@Override
	public String getDisplayName() {
		return this.getType().getDisplayStr()+" for "+this.shipmentName;
	}
	@Override
	public String getAmountStr() {
		return amount.setScale(Currency.USD.getScale(), BigDecimal.ROUND_HALF_UP).toString();
	}
	@Override
	public String getNote() {
		return "Invoice number: "+this.invoiceNumber;
	}
	@Override
	public String getShipmentName() {
		return this.shipmentName;
	}
	@Override
	public String getUrlPath() {
		return this.getType().getUrlText()+"/"+this.shipmentName;
	}
	@Override
	public BigDecimal getAmount() {
		return this.amount;
	}
	@Override
	public String toString() {
		return "Ms2ssStatementItemImpl [getType()=" + getType()
				+ ", getDisplayName()=" + getDisplayName()
				+ ", getAmountStr()=" + getAmountStr() + ", getNote()="
				+ getNote() + ", getShipmentName()=" + getShipmentName()
				+ ", getAmount()=" + getAmount() + "]";
	}
	
	

}
