package com.kindminds.drs.v1.model.impl.statement;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.report.Ss2spStatementReport;
import com.kindminds.drs.api.v1.model.report.StatementInfo;

import java.math.BigDecimal;
import java.util.List;

public class Ss2spStatementReportImplV1 implements Ss2spStatementReport{
	
	private static final int versionNumber = 1;
	
	private StatementInfo info;
	private BigDecimal previousBalance;
	private BigDecimal remittanceIsurToRcvr;
	private BigDecimal remittanceRcvrToIsur;
	private BigDecimal balance;
	private Currency currency;
	private List<Ss2spStatementItemProfitShare> foreignItems=null;
	private List<Ss2spStatementItemShipmentRelated> domesticItems=null;
	private List<Ss2spStatementItemServiceExpense> serviceExpenseItem=null;
	
	public void setInfo(StatementInfo info) {
		this.info = info;
	}
	
	public void setCurrency(Currency c){
		this.currency = c;
	}

	public void setForeignStatementItems(List<Ss2spStatementItemProfitShare> foreignItems) {
		this.foreignItems=foreignItems;
	}

	public void setShipmentRelatedItems(List<Ss2spStatementItemShipmentRelated> domesticItems) {
		this.domesticItems=domesticItems;
	}

	public void setServiceExpenseItem(List<Ss2spStatementItemServiceExpense> otherItems) {
		this.serviceExpenseItem=otherItems;
	}

	public void setPreviousBalance(BigDecimal amount){
		this.previousBalance = amount;
	}

	public void setRemittanceIsurToRcvr(BigDecimal rmt){
		this.remittanceIsurToRcvr = rmt;
	}

	public void setRemittanceRcvrToIsur(BigDecimal rmt){
		this.remittanceRcvrToIsur = rmt;
	}

	public void setBalance(BigDecimal amount){
		this.balance = amount;
	}

	@Override
	public String toString() {
		return "Ss2spStatementReportImplV1 [getVersionNumber()=" + getVersionNumber() + ", getDateStart()="
				+ getDateStart() + ", getDateEnd()=" + getDateEnd() + ", getTotal()=" + getTotal() + ", getCurrency()="
				+ getCurrency() + ", getIsurKcode()=" + getIsurKcode() + ", getRcvrKcode()=" + getRcvrKcode()
				+ ", getPreviousBalance()=" + getPreviousBalance() + ", getRemittanceIsurToRcvr()="
				+ getRemittanceIsurToRcvr() + ", getRemittanceRcvrToIsur()=" + getRemittanceRcvrToIsur()
				+ ", getBalance()=" + getBalance() + ", getProfitShareAmountUntaxed()=" + getProfitShareAmountUntaxed()
				+ ", getProfitShareAmountTax()=" + getProfitShareAmountTax() + ", getProfitShareItems()="
				+ getProfitShareItems() + ", getShipmentRelatedItems()=" + getShipmentRelatedItems()
				+ ", getSellBackRelatedItems()=" + getSellBackRelatedItems() + ", getStatementItemsServiceExpense()="
				+ getStatementItemsServiceExpense() + "]";
	}

	@Override
	public int getVersionNumber() {
		return versionNumber;
	}

	@Override
	public String getDateStart() {
		return this.info.getDateStart();
	}
	
	@Override
	public String getDateEnd() {
		return this.info.getDateEnd();
	}
	
	@Override
	public String getIsurKcode() {
		return this.info.getIsurKcode();
	}

	@Override
	public String getRcvrKcode() {
		return this.info.getRcvrKcode();
	}
	
	@Override
	public String getTotal() {
		BigDecimal total=new BigDecimal(0);
		for(Ss2spStatementItemProfitShare item: foreignItems){
			total=total.add(item.getAmount());
		}
		for(Ss2spStatementItemShipmentRelated item: domesticItems){
			total=total.add(item.getAmount());
		}
		for(Ss2spStatementItemServiceExpense item: this.serviceExpenseItem){
			total=total.add(item.getAmount());
		}
		return total.setScale(this.currency.getScale()).toString();
	}
	@Override
	public String getCurrency() {
		return this.currency.name();
	}
	
	@Override
	public String getPreviousBalance() {
		return this.previousBalance.setScale(this.currency.getScale()).toString();
	}
	@Override
	public String getRemittanceIsurToRcvr() {
		return this.remittanceIsurToRcvr.toString();
	}
	@Override
	public String getRemittanceRcvrToIsur() {
		return this.remittanceRcvrToIsur.toString();
	}
	@Override
	public String getBalance() {
		return this.balance.setScale(this.currency.getScale()).toString();
	}
	
	@Override
	public String getProfitShareAmountUntaxed() {
		return null;
	}

	@Override
	public String getProfitShareAmountTax() {
		return null;
	}
	@Override
	public List<Ss2spStatementItemProfitShare> getProfitShareItems() {
		return this.foreignItems;
	}

	@Override
	public List<Ss2spStatementItemShipmentRelated> getShipmentRelatedItems() {
		return this.domesticItems;
	}

	@Override
	public List<Ss2spStatementItemSellBackRelated> getSellBackRelatedItems() {
		return null;
	}

	@Override
		public List<Ss2spStatementItemServiceExpense> getStatementItemsServiceExpense() {
	//		Assert.isTrue(this.serviceExpenseItem.size()<=1);
			return this.serviceExpenseItem;
		}
	
	

}
