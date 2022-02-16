package com.kindminds.drs.service.test.dto.statement;

import com.kindminds.drs.api.v1.model.report.Ss2spStatementReport;
import com.kindminds.drs.util.TestUtil;

import java.util.List;

public class Ss2spStatementReportImpl implements Ss2spStatementReport {

	private int versionNumber;
	private String dateStart;
	private String dateEnd;
	private String total;
	private String currency;
	private String isurKcode;
	private String rcvrKcode;
	private String previousBalance;
	private String remittanceIsurToRcvr;
	private String remittanceRcvrToIsur;
	private String balance;
	private String profitShareAmountUntaxed;
	private String profitShareAmountTax;
	private List<Ss2spStatementItemProfitShare> profitShareItems;
	private List<Ss2spStatementItemShipmentRelated> shipmentRelatedItems;
	private List<Ss2spStatementItemSellBackRelated> sellBackRelatedItems;
	private List<Ss2spStatementItemServiceExpense> statementItemsServiceExpense;

	public Ss2spStatementReportImpl(
			int versionNumber,
			String dateStart,
			String dateEnd,
			String total,
			String currency,
			String isurKcode,
			String rcvrKcode,
			String previousBalance,
			String remittanceIsurToRcvr,
			String remittanceRcvrToIsur,
			String balance,
			String profitShareAmountUntaxed,
			String profitShareAmountTax,
			List<Ss2spStatementItemProfitShare> profitShareItems,
			List<Ss2spStatementItemShipmentRelated> shipmentRelatedItems,
			List<Ss2spStatementItemSellBackRelated> sellBackRelatedItems,
			List<Ss2spStatementItemServiceExpense> statementItemsServiceExpense) {
		this.versionNumber = versionNumber;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		this.total = total;
		this.currency = currency;
		this.isurKcode = isurKcode;
		this.rcvrKcode = rcvrKcode;
		this.previousBalance = previousBalance;
		this.remittanceIsurToRcvr = remittanceIsurToRcvr;
		this.remittanceRcvrToIsur = remittanceRcvrToIsur;
		this.balance = balance;
		this.profitShareAmountUntaxed = profitShareAmountUntaxed;
		this.profitShareAmountTax = profitShareAmountTax;
		this.profitShareItems = profitShareItems;
		this.shipmentRelatedItems = shipmentRelatedItems;
		this.sellBackRelatedItems = sellBackRelatedItems;
		this.statementItemsServiceExpense = statementItemsServiceExpense;
	}

	@Override
	public boolean equals(Object obj){
		if(obj instanceof Ss2spStatementReport){
			Ss2spStatementReport rp = (Ss2spStatementReport)obj;
			return this.getVersionNumber()==rp.getVersionNumber()
				&& this.getDateStart().equals(rp.getDateStart())
				&& this.getDateEnd().equals(rp.getDateEnd())
				&& this.getTotal().equals(rp.getTotal())
				&& this.getCurrency().equals(rp.getCurrency())
				&& this.getIsurKcode().equals(rp.getIsurKcode())
				&& this.getRcvrKcode().equals(rp.getRcvrKcode())
				&& this.getPreviousBalance().equals(rp.getPreviousBalance())
				&& this.getRemittanceIsurToRcvr().equals(rp.getRemittanceIsurToRcvr())
				&& this.getRemittanceRcvrToIsur().equals(rp.getRemittanceRcvrToIsur())
				&& this.getBalance().equals(rp.getBalance())
				&& TestUtil.nullableEquals(this.getProfitShareAmountUntaxed(),rp.getProfitShareAmountUntaxed())
				&& TestUtil.nullableEquals(this.getProfitShareAmountTax(),rp.getProfitShareAmountTax())
				&& this.getProfitShareItems().equals(rp.getProfitShareItems())
				&& this.getShipmentRelatedItems().equals(rp.getShipmentRelatedItems())
				&& TestUtil.nullableEquals(this.getSellBackRelatedItems(),rp.getSellBackRelatedItems())
				&& this.getStatementItemsServiceExpense().equals(rp.getStatementItemsServiceExpense());
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "Ss2spStatementReportImpl [getVersionNumber()=" + getVersionNumber() + ", getDateStart()="
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
		return this.versionNumber;
	}

	@Override
	public String getDateStart() {
		return this.dateStart;
	}

	@Override
	public String getDateEnd() {
		return this.dateEnd;
	}

	@Override
	public String getTotal() {
		return this.total;
	}

	@Override
	public String getCurrency() {
		return this.currency;
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
	public String getPreviousBalance() {
		return this.previousBalance;
	}

	@Override
	public String getRemittanceIsurToRcvr() {
		return this.remittanceIsurToRcvr;
	}

	@Override
	public String getRemittanceRcvrToIsur() {
		return this.remittanceRcvrToIsur;
	}

	@Override
	public String getBalance() {
		return this.balance;
	}

	@Override
	public String getProfitShareAmountUntaxed() {
		return this.profitShareAmountUntaxed;
	}

	@Override
	public String getProfitShareAmountTax() {
		return this.profitShareAmountTax;
	}

	@Override
	public List<Ss2spStatementItemProfitShare> getProfitShareItems() {
		return this.profitShareItems;
	}

	@Override
	public List<Ss2spStatementItemShipmentRelated> getShipmentRelatedItems() {
		return this.shipmentRelatedItems;
	}

	@Override
	public List<Ss2spStatementItemSellBackRelated> getSellBackRelatedItems() {
		return this.sellBackRelatedItems;
	}

	@Override
	public List<Ss2spStatementItemServiceExpense> getStatementItemsServiceExpense() {
		return this.statementItemsServiceExpense;
	}

}
