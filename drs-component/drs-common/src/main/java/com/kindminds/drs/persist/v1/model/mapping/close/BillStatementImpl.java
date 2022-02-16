package com.kindminds.drs.persist.v1.model.mapping.close;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;






import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.close.BillStatement;
import com.kindminds.drs.api.v1.model.close.BillStatementProfitShareItem;


//
public class BillStatementImpl implements BillStatement {
	
	//@Id ////@Column(name="id")
	private Integer id;
	//@Column(name="display_name")
	private String displayName;
	//@Column(name="date_start")
	private Date dateStart;
	//@Column(name="date_end")
	private Date dateEnd;
	//@Column(name="isur_kcode")
	private String isurName;
	//@Column(name="rcvr_kcode")
	private String rcvrName;
	//@Column(name="rmtnce_sent")
	private BigDecimal rmtnceSent;
	//@Column(name="rmtnce_received")
	private BigDecimal rmtnceReceived;
	//@Column(name="currency_name")
	private String currency;
	//@Column(name="total")
	private BigDecimal total;
	//@Column(name="previous_balance")
	private BigDecimal balance_previous;
	//@Column(name="balance")
	private BigDecimal balance;

	private List<BillStatementLineItem> lineItems=null;

	private List<BillStatementProfitShareItem> profitShareItem=null;
	
	public void setLineItems(List<BillStatementLineItem> items){
		this.lineItems=items;
	}
	
	public void setProfitShareItems(List<BillStatementProfitShareItem> items){
		this.profitShareItem=items;
	}

	public void setPreviousBalance(BigDecimal previousbalance) {
		this.balance_previous = previousbalance;	
	}

	public void setCurrency(Currency currency) {
		this.currency = currency.name();
	}
	
	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public void setBalance(BigDecimal balance) {
		this.balance=balance;
	}

	public void setRemittanceIsurToRcvr(BigDecimal remittanceSent) {
		this.rmtnceSent=remittanceSent;
	}

	public void setRemittanceRcvrToIsur(BigDecimal remittanceReceived) {
		this.rmtnceReceived=remittanceReceived;
	}

	public BillStatementImpl( Integer id ,String displayName ,Date dateStart ,Date dateEnd ,
							  String isurName, String rcvrName, BigDecimal rmtnceSent,
			 BigDecimal rmtnceReceived, String currency, BigDecimal total, BigDecimal balance_previous, BigDecimal balance){

		this.id = id;
		this.displayName = displayName;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		this.isurName = isurName;
		this.rcvrName = rcvrName;
		this.rmtnceSent = rmtnceSent;
		this.rmtnceReceived = rmtnceReceived;
		this.currency = currency;
		this.total = total;
		this.balance_previous = balance_previous;
		this.balance = balance;


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
		return this.currency==null?null:Currency.valueOf(this.currency).name();
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
		return this.profitShareItem;
	}
	
	@Override
	public List<BillStatementLineItem> getLineItems() {
		return this.lineItems;
	}
}
