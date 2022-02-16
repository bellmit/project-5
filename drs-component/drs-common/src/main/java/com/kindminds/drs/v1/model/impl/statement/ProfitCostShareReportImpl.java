package com.kindminds.drs.v1.model.impl.statement;

import com.kindminds.drs.api.v1.model.report.ProfitCostShareListReport;

import java.util.List;

public class ProfitCostShareReportImpl implements ProfitCostShareListReport {

	private List<ProfitCostShareListReportItem> pcsStatements;


	public ProfitCostShareReportImpl() {

	}

	public void setPcsStatements(List<ProfitCostShareListReportItem> pcsStatements) {
		this.pcsStatements = pcsStatements;
	}

	@Override
	public List<ProfitCostShareListReportItem> getPcsStatements() {
		return this.pcsStatements;
	}

}
