package com.kindminds.drs.v1.model.impl.close;

import com.kindminds.drs.api.v1.model.close.BillStatement;
import com.kindminds.drs.api.v1.model.close.BillStatementProfitShareItem;
import com.kindminds.drs.util.TestUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BillStatementImpl implements BillStatement {

	private Integer id;
	private String displayName;
	private Date dateStart;
	private Date dateEnd;
	private BigDecimal balance_previous;
	private BigDecimal total;
	private BigDecimal balance;
	private String rcvrName;
	private String isurName;
	private BigDecimal rmtnceSent;
	private BigDecimal rmtnceReceived;
	private String currency;
	private List<BillStatementProfitShareItem> profitShareItems=null;
	private List<BillStatementLineItem> lineItems=null;

	public BillStatementImpl(
			int createdStatementId, 
			String displayName, 
			Date dateStart, 
			Date dateEnd,
			String isurName,
			String rcvrName,
			String currency, 
			String total, 
			String balance_previous, 
			String rmtIsurToRcvr, 
			String rmtRcvrToIsur,
			String balance) {
		this.id = createdStatementId;
		this.displayName = displayName;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		this.balance_previous = new BigDecimal(balance_previous);
		this.total = new BigDecimal(total);
		this.balance = new BigDecimal(balance);
		this.rcvrName = rcvrName;
		this.isurName = isurName;
		this.rmtnceSent = new BigDecimal(rmtIsurToRcvr);
		this.rmtnceReceived = new BigDecimal(rmtRcvrToIsur);
		this.currency = currency;
	}
	
	@Override
	public boolean equals(Object obj){
		if ( obj instanceof BillStatement ){
			BillStatement stmnt = ((BillStatement) obj);
			return this.getId().equals(stmnt.getId())
				&& this.getDateStart().equals(stmnt.getDateStart())
				&& this.getDateEnd().equals(stmnt.getDateEnd())
				&& this.getIssuerKcode().equals(stmnt.getIssuerKcode())
				&& this.getReceiverKcode().equals(stmnt.getReceiverKcode())
				&& this.getPreviousBalance().equals(stmnt.getPreviousBalance())
				&& TestUtil.nullableEquals(this.getRemittanceIsurToRcvr(),stmnt.getRemittanceIsurToRcvr())
				&& TestUtil.nullableEquals(this.getRemittanceRcvrToIsur(),stmnt.getRemittanceRcvrToIsur())
				&& TestUtil.nullableEquals(this.getCurrency(),stmnt.getCurrency())
				&& this.getTotal().equals(stmnt.getTotal())
				&& this.getBalance().equals(stmnt.getBalance())
				&& TestUtil.nullableEquals(this.getProfitShareItems(),stmnt.getProfitShareItems())
				&& TestUtil.nullableEquals(this.getLineItems(),stmnt.getLineItems());
		}
	    return false;
	}

	@Override
	public String toString() {
		return "BillStatementImpl [getId()=" + getId() + ", getDisplayName()=" + getDisplayName() + ", getDateStart()="
				+ getDateStart() + ", getDateEnd()=" + getDateEnd() + ", getIssuerKcode()=" + getIssuerKcode()
				+ ", getReceiverKcode()=" + getReceiverKcode() + ", getCurrency()=" + getCurrency() + ", getTotal()="
				+ getTotal() + ", getPreviousBalance()=" + getPreviousBalance() + ", getRemittanceIsurToRcvr()="
				+ getRemittanceIsurToRcvr() + ", getRemittanceRcvrToIsur()=" + getRemittanceRcvrToIsur()
				+ ", getBalance()=" + getBalance() + ", getProfitShareItems()=" + getProfitShareItems()
				+ ", getLineItems()=" + getLineItems() + "]";
	}
	
	@Override
	public Integer getId() {
		return this.id;
	}
	
	@Override
	public String getDisplayName() {
		return this.displayName;
	}
	
	@Override
	public Date getDateStart() {
		return this.dateStart;
	}
	
	@Override
	public Date getDateEnd() {
		return this.dateEnd;
	}
	
	@Override
	public String getIssuerKcode() {
		return this.isurName;
	}
	
	@Override
	public String getReceiverKcode() {
		return this.rcvrName;
	}
	
	@Override
	public String getCurrency() {
		return this.currency;
	}

	@Override
	public BigDecimal getTotal() {
		return this.total;
	}

	@Override
	public BigDecimal getPreviousBalance() {
		return this.balance_previous;
	}

	@Override
	public BigDecimal getRemittanceIsurToRcvr() {
		return this.rmtnceSent;
	}

	@Override
	public BigDecimal getRemittanceRcvrToIsur() {
		return this.rmtnceReceived;
	}

	@Override
	public BigDecimal getBalance() {
		return this.balance;
	}
	
	@Override
	public List<BillStatementProfitShareItem> getProfitShareItems() {
		return this.profitShareItems;
	}
	
	public void addProfitShareItem(BillStatementProfitShareItem item){
		if(this.profitShareItems==null) this.profitShareItems = new ArrayList<BillStatementProfitShareItem>();
		this.profitShareItems.add(item);
	}
	
	@Override
	public List<BillStatementLineItem> getLineItems() {
		return this.lineItems;
	}
	
	public void addLineItem(BillStatementLineItem lineItem){
		if(this.lineItems==null){
			this.lineItems = new ArrayList<BillStatementLineItem>();
		}
		this.lineItems.add(lineItem);
	}

}
