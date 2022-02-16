package com.kindminds.drs.v1.model.impl.statement;

import java.io.Serializable;
import java.math.BigDecimal;

import com.kindminds.drs.util.NumberHelper;

public class StatementListReportItemTwImpl extends
		 StatementListReportItemImpl implements  Serializable{

	private static final long serialVersionUID = -1233724319016276555L;

	public StatementListReportItemTwImpl(
			String statementId,
			String startDateUtc,
			String endDateUtc,
			Integer currencyId,
			BigDecimal total,
			BigDecimal balance,
			String invoiceFromSsdc,
			String invoiceFromSupplier) {
		super(statementId, startDateUtc, endDateUtc, currencyId, total, balance, invoiceFromSsdc, invoiceFromSupplier);
	}
	
	@Override
	public String getTotal() {
		return NumberHelper.toGeneralCommaSeparatedString(this.total,this.currency.getScale());
	}

	@Override
	public String getBalance() {
		return NumberHelper.toGeneralCommaSeparatedString(this.balance,this.currency.getScale());
	}
}
