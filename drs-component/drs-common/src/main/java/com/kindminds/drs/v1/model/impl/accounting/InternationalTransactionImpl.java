package com.kindminds.drs.v1.model.impl.accounting;

import com.kindminds.drs.Currency;
import com.kindminds.drs.util.DateHelper;
import com.kindminds.drs.api.v1.model.accounting.InternationalTransaction;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class InternationalTransactionImpl implements InternationalTransaction {
	
	private int id;
	private Date transactionDate;
	private Integer cashFlowDirectionKey;
	private String msdcKcode;
	private String msdcName;
	private String ssdcKcode;
	private String ssdcName;
	private String splrKcode;
	private String splrName;
	private Integer currencyId;
	private BigDecimal total;
	private String note;
	private Boolean isEditable;
	private List<InternationalTransactionLineItem> lineItems;


	public void setId(int id) {
		this.id = id;
	}
	
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}
	
	public void setCashFlowDirectionKey(Integer cashFlowDirectionKey) {
		this.cashFlowDirectionKey = cashFlowDirectionKey;
	}

	public void setMsdcKcode(String msdcKcode) {
		this.msdcKcode = msdcKcode;
	}

	public void setMsdcName(String msdcName) {
		this.msdcName = msdcName;
	}

	public void setSsdcKcode(String ssdcKcode) {
		this.ssdcKcode = ssdcKcode;
	}

	public void setSsdcName(String ssdcName) {
		this.ssdcName = ssdcName;
	}

	public void setSplrKcode(String splrKcode) {
		this.splrKcode = splrKcode;
	}

	public void setSplrName(String splrName) {
		this.splrName = splrName;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	public void setIsEditable(Boolean isEditable) {
		this.isEditable = isEditable;
	}

	public void setLineItems(List<InternationalTransactionLineItem> lineItems) {
		this.lineItems = lineItems;
	}

	@Override
	public String toString() {
		return "InternationalTransactionImpl [getId()=" + getId() + ", getUtcDate()=" + getUtcDate()
				+ ", getCashFlowDirectionKey()=" + getCashFlowDirectionKey() + ", getMsdcKcode()=" + getMsdcKcode()
				+ ", getMsdcName()=" + getMsdcName() + ", getSsdcKcode()=" + getSsdcKcode() + ", getSsdcName()="
				+ getSsdcName() + ", getSplrKcode()=" + getSplrKcode() + ", getSplrName()=" + getSplrName()
				+ ", getCurrency()=" + getCurrency() + ", getTotal()=" + getTotal() + ", getNote()=" + getNote()
				+ ", isEditable()=" + isEditable() + ", getLineItems()=" + getLineItems() + "]";
	}

	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public String getUtcDate() {
		return DateHelper.toString(this.transactionDate,"yyyy-MM-dd","UTC");
	}
	
	@Override
	public Integer getCashFlowDirectionKey(){
		return this.cashFlowDirectionKey;
	}

	@Override
	public String getMsdcKcode() {
		return this.msdcKcode;
	}

	@Override
	public String getMsdcName() {
		return this.msdcName;
	}

	@Override
	public String getSsdcKcode() {
		return this.ssdcKcode;
	}

	@Override
	public String getSsdcName() {
		return this.ssdcName;
	}

	@Override
	public String getSplrKcode() {
		return this.splrKcode;
	}

	@Override
	public String getSplrName() {
		return this.splrName;
	}

	@Override
	public Currency getCurrency() {
		return Currency.fromKey(this.currencyId);
	}

	@Override
	public String getTotal() {
		return
				this.total.setScale(this.getCurrency().getScale() , BigDecimal.ROUND_HALF_UP).toPlainString();
	}

	@Override
	public String getNote() {
		return this.note;
	}

	@Override
	public Boolean isEditable() {
		return this.isEditable;
	}

	@Override
	public List<InternationalTransactionLineItem> getLineItems() {
		return this.lineItems;
	}

}
