package com.kindminds.drs.v1.model.impl.statement;

import java.io.Serializable;
import java.math.BigDecimal;
import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.report.StatementListReport.StatementListReportItem;

public class StatementListReportItemImpl implements
		Serializable , StatementListReportItem{

	private static final long serialVersionUID = 5546735735318988902L;
	private String statementId;
	private String startDateUtc;
	private String endDateUtc;
	protected Currency currency;
	protected BigDecimal total;
	protected BigDecimal balance;
	private String invoiceFromSsdc;
	private String invoiceFromSupplier;
	
	public StatementListReportItemImpl(
			String statementId,
			String startDateUtc,
			String endDateUtc,
			Integer currencyId,
			BigDecimal total,
			BigDecimal balance,
			String invoiceFromSsdc,
			String invoiceFromSupplier) {
		this.statementId = statementId;
		this.startDateUtc = startDateUtc;
		this.endDateUtc = endDateUtc;
		this.currency = Currency.fromKey(currencyId);
		this.total = total;
		this.balance = balance;
		this.invoiceFromSsdc = invoiceFromSsdc;
		this.invoiceFromSupplier = invoiceFromSupplier;
	}
	
	@Override
	public String getStatementId() {
		return this.statementId;
	}
	
	@Override
	public String getPeriodStartUtc() {
		return this.startDateUtc;
	}
	
	@Override
	public String getPeriodEndUtc() {
		return this.endDateUtc;
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
}
