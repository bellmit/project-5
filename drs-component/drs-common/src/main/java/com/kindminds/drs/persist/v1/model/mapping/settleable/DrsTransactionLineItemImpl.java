package com.kindminds.drs.persist.v1.model.mapping.settleable;

import java.math.BigDecimal;



import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.accounting.DrsTransaction;


public class DrsTransactionLineItemImpl implements DrsTransaction.DrsTransactionLineItem {
	
	//@Id ////@Column(name="id")
	private int id;
	//@Column(name="line_seq")
	private Integer lineSeq;
	//@Column(name="isur_kcode")
	private String isurKcode;
	//@Column(name="rcvr_kcode")
	private String rcvrKcode;
	//@Column(name="item_name")
	private String itemName;
	//@Column(name="currency_id")
	private Integer currencyId;
	//@Column(name="amount")
	private BigDecimal amount;

	public DrsTransactionLineItemImpl() {
	}

	public DrsTransactionLineItemImpl(int id, Integer lineSeq, String isurKcode, String rcvrKcode, String itemName, Integer currencyId, BigDecimal amount) {
		this.id = id;
		this.lineSeq = lineSeq;
		this.isurKcode = isurKcode;
		this.rcvrKcode = rcvrKcode;
		this.itemName = itemName;
		this.currencyId = currencyId;
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "DrsTransactionLineItemImpl [getLineSeq()=" + getLineSeq() + ", getIsurKcode()=" + getIsurKcode()
				+ ", getRcvrKcode()=" + getRcvrKcode() + ", getItemName()=" + getItemName() + ", getCurrency()="
				+ getCurrency() + ", getAmount()=" + getAmount() + "]";
	}

	@Override
	public Integer getLineSeq() {
		return this.lineSeq;
	}

	@Override
	public String getIsurKcode() {
		return this.isurKcode;
	}

	@Override
	public String getRcvrKcode() {
		return this.rcvrKcode;
	}

	@Override
	public String getItemName() {
		return this.itemName;
	}

	@Override
	public String getCurrency() {
		return Currency.fromKey(this.currencyId).name();
	}

	@Override
	public BigDecimal getAmount() {
		return this.amount;
	}

}
