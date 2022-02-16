package com.kindminds.drs.v1.model.impl.factory;

import com.kindminds.drs.api.v1.model.report.Ss2spStatementReport;
import com.kindminds.drs.enums.BillStatementType;
import com.kindminds.drs.v1.model.impl.statement.Ss2spStatementReportImplV2;
import org.springframework.stereotype.Component;

@Component
public class Ss2spStatementReportFactoryV2 extends Ss2spStatementReportFactoryV1 {

	@Override
	protected ReportVersion getReportVersion() {
		return ReportVersion.V2;
	}

	@Override
	public Ss2spStatementReport createSs2spStatementReport(BillStatementType type,String statementName) {
		Ss2spStatementReportImplV2 report = new Ss2spStatementReportImplV2();
		report.setInfo(this.getInfo(type,statementName));
		report.setForeignStatementItems(this.dao.queryStatementItemProfitShareV2(type,statementName));
		report.setShipmentRelatedItems(this.queryShipmentRelatedItems(type,statementName));
		report.setServiceExpenseItem(this.dao.queryStatementItemServiceExpense(type,statementName));
		report.setPreviousBalance(this.dao.queryPreviousBalance(type,statementName));
		report.setRemittanceIsurToRcvr(this.dao.queryRemittanceIsurToRcvr(type,statementName));
		report.setRemittanceRcvrToIsur(this.dao.queryRemittanceRcvrToIsur(type,statementName));
		report.setBalance(this.dao.queryBalance(type,statementName));
		report.setCurrency(this.dao.queryStatementCurrency(type,statementName));
		report.setProfiShareAmountTax(this.dao.queryProfitShareTaxV2(type,statementName));
		return report;
	}

}
