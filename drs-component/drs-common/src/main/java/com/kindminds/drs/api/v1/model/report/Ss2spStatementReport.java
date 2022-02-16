package com.kindminds.drs.api.v1.model.report;

import com.kindminds.drs.Currency;

import java.math.BigDecimal;
import java.util.List;

public interface Ss2spStatementReport {
	
	public int getVersionNumber();
	public String getDateStart();
	public String getDateEnd();
	public String getTotal();
	public String getCurrency();
	public String getIsurKcode();
	public String getRcvrKcode();
	public String getPreviousBalance();
	public String getRemittanceIsurToRcvr();
	public String getRemittanceRcvrToIsur();
	public String getBalance();
	public String getProfitShareAmountUntaxed();
	public String getProfitShareAmountTax();
	public List<Ss2spStatementItemProfitShare> getProfitShareItems();
	public List<Ss2spStatementItemShipmentRelated> getShipmentRelatedItems();
	public List<Ss2spStatementItemSellBackRelated> getSellBackRelatedItems();
	public List<Ss2spStatementItemServiceExpense> getStatementItemsServiceExpense();
	
	public interface Ss2spStatementItemProfitShare{
		public String getDisplayName();
		public String getAmountUntaxStr();
		public String getAmountTaxStr();
		public String getAmountStr();
		public Currency getCurrency();
		public BigDecimal getAmount();
	}
	
	public interface Ss2spStatementItemShipmentRelated{
		public String getItemName();
		public String getCurrency();
		public String getAmountStr();
		public String getNoteText();
		public String getSourceShipmentName();
		public String getSourceShipmentInvoiceNumber();
		public BigDecimal getAmount();
	}
	
	public interface Ss2spStatementItemSellBackRelated{
		public String getItemName();
		public String getCurrency();
		public String getAmountStr();
		public String getNoteText();
		public String getInvoiceNumber();
		public BigDecimal getAmount();
	}
	
	public interface Ss2spStatementItemServiceExpense{
		public String getDisplayName();
		public String getCurrency();
		public String getAmountStr();
		public BigDecimal getAmount();
		public String getInvoiceNumber();
	}
}
