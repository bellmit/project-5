package com.kindminds.drs.v1.model.impl.statement;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.report.StatementListReport;

import java.util.List;

public class StatementListReportImpl implements StatementListReport {
	
	private Currency currency;
	private List<StatementListReportItem> ss2spStatements = null;
	
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
	
	public void setStatements(List<StatementListReportItem> ss2spStatements) {
		this.ss2spStatements = ss2spStatements;
	}

	@Override
	public String getCurrency() {
		return this.currency.name();
	}
	
	@Override
	public List<StatementListReportItem> getItems() {
		return this.ss2spStatements;
	}
	




}