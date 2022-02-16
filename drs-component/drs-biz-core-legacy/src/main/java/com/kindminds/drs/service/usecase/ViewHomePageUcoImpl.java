package com.kindminds.drs.service.usecase;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.data.access.usecase.accounting.ViewSs2spStatementDao;
import com.kindminds.drs.api.usecase.ViewHomePageUco;
import com.kindminds.drs.v1.model.impl.statement.StatementListReportItemTwImpl;
import com.kindminds.drs.api.v1.model.report.StatementListReport;
import com.kindminds.drs.api.v1.model.report.StatementListReport.StatementListReportItem;
import com.kindminds.drs.enums.BillStatementType;
import com.kindminds.drs.v1.model.impl.statement.StatementListReportImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

//import com.kindminds.drs.Context;

@Service("viewHomePageUco")
public class ViewHomePageUcoImpl implements ViewHomePageUco {

	@Autowired private com.kindminds.drs.api.data.access.rdb.CompanyDao CompanyDao;
	@Autowired private com.kindminds.drs.api.data.access.rdb.CurrencyDao CurrencyDao;
	@Autowired private ViewSs2spStatementDao ss2spDao;

	@Override
	public StatementListReport getSs2spStatementListReport(String userCompanyKcode) {
		StatementListReportImpl ss2spStatementListReport = new StatementListReportImpl();
		//String userCompanyKcode = Context.getCurrentUser().getCompanyKcode();
		Currency userCompanyCurrency = this.CurrencyDao.queryCompanyCurrency(userCompanyKcode);
		ss2spStatementListReport.setCurrency(userCompanyCurrency);
		ss2spStatementListReport.setStatements(this.getSs2spStatementList(userCompanyKcode));
		return ss2spStatementListReport;
	}
	
	private List<StatementListReportItem> getSs2spStatementList(String userCompanyKcode){
		if(this.CompanyDao.isDrsCompany(userCompanyKcode))
			return toStatementList(
					this.ss2spDao.queryNewestStatementsSentByDrsCompany(BillStatementType.OFFICIAL,userCompanyKcode));
		else
			return toStatementList(this.ss2spDao.queryStatementsReceivedBySupplier(BillStatementType.OFFICIAL,userCompanyKcode));
	}


	private List<StatementListReportItem> toStatementList(List<Object[]> columnsList) {
		List<StatementListReportItem> resultList = new ArrayList<>();
		for(Object[] columns:columnsList){
			String statementId = (String)columns[0];
			String startDateUtc = (String)columns[1];
			String endDateUtc = (String)columns[2];
			Integer currencyId = (Integer)columns[3];
			BigDecimal total = (BigDecimal)columns[4];
			BigDecimal balance = (BigDecimal)columns[5];
			String invoiceFromSsdc = (String)columns[6];
			String invoiceFromSupplier = (String)columns[7];
			resultList.add(new StatementListReportItemTwImpl(statementId,startDateUtc,endDateUtc,currencyId,total,balance,invoiceFromSsdc,invoiceFromSupplier));
		}
		return resultList;
	}
}
