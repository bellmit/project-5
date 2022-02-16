package com.kindminds.drs.v1.model.impl.statement;

import com.kindminds.drs.Currency;

import com.kindminds.drs.api.v1.model.report.Ss2spStatementReport;
import com.kindminds.drs.api.v1.model.report.StatementInfo;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class Ss2spStatementReportImplV2 implements Ss2spStatementReport {

	private static final int versionNumber = 2;
	
	private StatementInfo info;
	private BigDecimal profitShareAmountTax;
	private BigDecimal previousBalance;
	private BigDecimal remittanceIsurToRcvr;
	private BigDecimal remittanceRcvrToIsur;
	private BigDecimal balance;
	private Currency currency;
	private List<Ss2spStatementItemProfitShare> itemsProfitShare=null;
	private List<Ss2spStatementItemShipmentRelated> domesticItems=null;
	private List<Ss2spStatementItemSellBackRelated> sellBackItems=null;
	private List<Ss2spStatementItemServiceExpense> serviceExpenseItem=null;
	
	public void setInfo(StatementInfo info) {
		this.info = info;
	}
	
	public void setProfiShareAmountTax(BigDecimal amountTax) {
		this.profitShareAmountTax = amountTax;
	}
	
	@Override
	public String toString() {
		return "Ss2spStatementReportImplV2 [getVersionNumber()=" + getVersionNumber() + ", getDateStart()="
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
		for(Ss2spStatementItemProfitShare item: itemsProfitShare){
			total=total.add(item.getAmount());
		}
		for(Ss2spStatementItemShipmentRelated item: domesticItems){
			total=total.add(item.getAmount());
		}
		if(this.sellBackItems!=null){
			for(Ss2spStatementItemSellBackRelated item: this.sellBackItems){
				total=total.add(item.getAmount());
			}	
		}
		for(Ss2spStatementItemServiceExpense item: this.serviceExpenseItem){
			total=total.add(item.getAmount());
		}

		//return total.setScale(this.currency.getScale()).toString();
		return total.setScale(this.currency.getScale() , RoundingMode.HALF_UP).toString();
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
		return this.remittanceIsurToRcvr.setScale(this.currency.getScale()).toPlainString();
	}
	
	@Override
	public String getRemittanceRcvrToIsur() {
		return this.remittanceRcvrToIsur.setScale(this.currency.getScale()).toPlainString();
	}
	
	@Override
	public String getBalance() {
		return this.balance.setScale(this.currency.getScale()).toString();
	}
	
	@Override
	public String getProfitShareAmountUntaxed() {
		Assert.isTrue(this.itemsProfitShare.size()==1);
		BigDecimal amountTotal   = itemsProfitShare.get(0).getAmount();
		BigDecimal amountTax     = this.profitShareAmountTax;
		BigDecimal amountUntaxed = amountTotal.subtract(amountTax); 
		return amountUntaxed.setScale(this.currency.getScale()).toPlainString();
	}
	
	@Override
	public String getProfitShareAmountTax() {
		return this.profitShareAmountTax.setScale(this.currency.getScale()).toPlainString();
	}
	
	@Override
	public List<Ss2spStatementItemProfitShare> getProfitShareItems() {
		return this.itemsProfitShare;
	}
	@Override
	public List<Ss2spStatementItemShipmentRelated> getShipmentRelatedItems() {
		return this.domesticItems;
	}
	
	@Override
	public List<Ss2spStatementItemSellBackRelated> getSellBackRelatedItems() {
		return this.sellBackItems;
	}
	
	@Override
	public List<Ss2spStatementItemServiceExpense> getStatementItemsServiceExpense() {
//		Assert.isTrue(this.serviceExpenseItem.size()<=1);
		return this.serviceExpenseItem;
	}

	public void setCurrency(Currency c){
		this.currency = c;
	}
	public void setForeignStatementItems(List<Ss2spStatementItemProfitShare> foreignItems) {
		Assert.isTrue(foreignItems.size()==1||foreignItems.size()==0);
		this.itemsProfitShare=foreignItems;
	}
	public void setShipmentRelatedItems(List<Ss2spStatementItemShipmentRelated> domesticItems) {
		this.domesticItems=domesticItems;
	}
	public void setSellBackRelatedItems(List<Ss2spStatementItemSellBackRelated> sellBackItems) {
		this.sellBackItems = sellBackItems;
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



}
