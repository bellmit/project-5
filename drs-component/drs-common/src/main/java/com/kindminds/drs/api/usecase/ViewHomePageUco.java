package com.kindminds.drs.api.usecase;

import com.kindminds.drs.api.v1.model.report.StatementListReport;


public interface ViewHomePageUco {
	public StatementListReport getSs2spStatementListReport(String userCompanyKcode);
}