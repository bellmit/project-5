package com.kindminds.drs.v1.model.impl.statement;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.report.ProfitCostShareListReport.ProfitCostShareListReportItem;

import java.math.BigDecimal;

public class ProfitCostShareListReportItemImpl implements ProfitCostShareListReportItem{
	
	private String statementId;
	private String startDateUtc;
	private String endDateUtc;
	private Currency currency;
	private BigDecimal total;
	private BigDecimal balance;
	private String invoiceFromSsdc;
	private String invoiceFromSupplier;
	private String invoiceNotes;
	private String amountTotal;
	private BigDecimal amountTax;
	private BigDecimal amountUntaxed;
	
	public ProfitCostShareListReportItemImpl() {
		
	}
	
	public ProfitCostShareListReportItemImpl(
			String statementId,
			String startDateUtc,
			String endDateUtc,
			Integer currencyId,
			BigDecimal total,
			BigDecimal balance,
			String invoiceFromSsdc,
			String invoiceFromSupplier,
			String invoiceNotes,
			BigDecimal amountTax,
			BigDecimal amountUntaxed) {
		this.statementId = statementId;
		this.startDateUtc = startDateUtc;
		this.endDateUtc = endDateUtc;
		this.currency = Currency.fromKey(currencyId);
		this.total = total;
		this.balance = balance;
		this.invoiceFromSsdc = invoiceFromSsdc;
		this.invoiceFromSupplier = invoiceFromSupplier;
		this.invoiceNotes = invoiceNotes;
		this.amountTax = amountTax;
		this.amountUntaxed = amountUntaxed;
	}
	
	public ProfitCostShareListReportItemImpl(ProfitCostShareListReportItem profitCostShare) {
		this.statementId = profitCostShare.getStatementId();
		this.startDateUtc = profitCostShare.getPeriodStartUtc();
		this.endDateUtc = profitCostShare.getPeriodEndUtc();
		this.invoiceFromSsdc = profitCostShare.getInvoiceFromSsdc();
		this.invoiceFromSupplier = profitCostShare.getInvoiceFromSupplier();
		this.invoiceNotes = profitCostShare.getInvoiceNotes();
	}

	@Override
	public String getStatementId() {
		return this.statementId;
	}
	
	public void setStatementId(String statementId) {
		this.statementId = statementId;
	}
	
	@Override
	public String getPeriodStartUtc() {
		return this.startDateUtc;
	}
	
	public void setPeriodStartUtc(String startDateUtc) {
		this.startDateUtc = startDateUtc;
	}
	
	@Override
	public String getPeriodEndUtc() {
		return this.endDateUtc;
	}
	
	public void setPeriodEndUtc(String endDateUtc) {
		this.endDateUtc = endDateUtc;
	}
	
	@Override
	public String getCurrency() {
		return this.currency.name();
	}
	
	@Override
	public String getTotal() {
		return this.total.setScale(this.currency.getScale()).toPlainString();
	}

	@Override
	public String getBalance() {
		return this.balance.setScale(this.currency.getScale()).toPlainString();
	}

	@Override
	public String getInvoiceFromSsdc() {
		return this.invoiceFromSsdc;
	}

	@Override
	public String getInvoiceFromSupplier() {
		return this.invoiceFromSupplier;
	}

	@Override
	public String getInvoiceNotes() {
		return this.invoiceNotes;
	}

	public void setInvoiceFromSsdc(String invoiceFromSsdc) {
		this.invoiceFromSsdc = invoiceFromSsdc;
		
	}

	public void setInvoiceFromSupplier(String invoiceFromSupplier) {
		this.invoiceFromSupplier = invoiceFromSupplier;
	}

	public void setInvoiceNotes(String invoiceNotes) {
		this.invoiceNotes = invoiceNotes;
	}

	@Override
	public String getAmountTotal() {
		return this.amountTotal;
	}

	@Override
	public void setAmountTotal(String amountTotal) {
		this.amountTotal = amountTotal;
		
	}

	@Override
	public String getAmountStr() {
		return this.amountUntaxed.add(this.amountTax).setScale(this.currency.getScale()).toPlainString();
	}

}