package com.kindminds.drs.v1.model.impl.statement;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.report.Ms2ssStatement;
import com.kindminds.drs.api.v1.model.report.StatementInfo;

import java.math.BigDecimal;
import java.util.List;

public class Ms2ssStatementReportImpl implements Ms2ssStatement {
	
	private StatementInfo info;
	private BigDecimal previousBalance;
	private BigDecimal remittanceIsurToRcvr;
	private BigDecimal remittanceRcvrToIsur;
	private BigDecimal balance;
	
	private Currency currency;
	private List<Ms2ssStatementItem> items=null;
	private List<Ms2ssStatementItemPaymentOnBehalf> itemsPob=null;
	private Ms2ssStatementItemMsdcPaymentOnBehalf ms2ssStatementItemMsdcPaymentOnBehalf=null;
	private Ms2ssStatementItemProductInventoryReturn itemProductInventoryReturn=null;
	
	public void setInfo(StatementInfo info) {
		this.info = info;
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
	
	public void setItems( List<Ms2ssStatementItem> items) {
		this.items = items;
	}
	
	public void setItemsPaymentOnBehalf( List<Ms2ssStatementItemPaymentOnBehalf> items) {
		this.itemsPob = items;
	}
	
	public void setMs2ssStatementItemMsdcPaymentOnBehalf(Ms2ssStatementItemMsdcPaymentOnBehalf ms2ssStatementItemMsdcPaymentOnBehalf) {
		this.ms2ssStatementItemMsdcPaymentOnBehalf = ms2ssStatementItemMsdcPaymentOnBehalf;
	}
	
	public void setProductInventoryReturnItem(Ms2ssStatementItemProductInventoryReturn item){
		this.itemProductInventoryReturn = item;
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
	public String getTotal() {
		BigDecimal total = BigDecimal.ZERO;
		for(Ms2ssStatementItem item:this.items){
			total = total.add(item.getAmount());
		}
		for(Ms2ssStatementItemPaymentOnBehalf item:this.itemsPob){
			total = total.add(item.getAmount());
		}
		if(this.ms2ssStatementItemMsdcPaymentOnBehalf!=null){
			Ms2ssStatementItemMsdcPaymentOnBehalfImpl origItem = (Ms2ssStatementItemMsdcPaymentOnBehalfImpl)this.ms2ssStatementItemMsdcPaymentOnBehalf;
			total = total.add(origItem.getOrigAmount());
		}
		return total.setScale(Currency.USD.getScale(), BigDecimal.ROUND_HALF_UP).toString();
	}
	
	@Override
	public String getCurrency() {
		return this.currency.name();
	}
	
	@Override
	public List<Ms2ssStatementItem> getStatementItems() {
		return this.items;
	}
	
	@Override
	public List<Ms2ssStatementItemPaymentOnBehalf> getStatementItemsPaymentOnBehalf() {
		return this.itemsPob;
	}
	
	@Override
	public Ms2ssStatementItemMsdcPaymentOnBehalf getMs2ssStatementItemMsdcPaymentOnBehalf() {
		return this.ms2ssStatementItemMsdcPaymentOnBehalf;
	}

	@Override
	public Ms2ssStatementItemProductInventoryReturn getProductInventoryReturnItem() {
		return this.itemProductInventoryReturn;
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
	public String getPreviousBalance() {
		return this.previousBalance.toString();
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
		return this.balance.toString();
	}
}
