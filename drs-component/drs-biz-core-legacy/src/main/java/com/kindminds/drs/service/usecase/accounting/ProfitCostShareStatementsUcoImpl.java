package com.kindminds.drs.service.usecase.accounting;

import com.kindminds.drs.Context;
import com.kindminds.drs.api.usecase.ProfitCostShareStatementsUco;
import com.kindminds.drs.api.v1.model.accounting.SettlementPeriod;
import com.kindminds.drs.v1.model.impl.accounting.SettlementPeriodImpl;
import com.kindminds.drs.v1.model.impl.statement.ProfitCostShareListReportItemImpl;
import com.kindminds.drs.api.v1.model.report.ProfitCostShareListReport;
import com.kindminds.drs.api.v1.model.report.ProfitCostShareListReport.ProfitCostShareListReportItem;
import com.kindminds.drs.api.data.access.rdb.CompanyDao;
import com.kindminds.drs.api.data.access.rdb.accounting.ProfitCostShareStatementsDao;
import com.kindminds.drs.enums.BillStatementType;

import com.kindminds.drs.v1.model.impl.statement.ProfitCostShareReportImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ProfitCostShareStatementsUcoImpl implements ProfitCostShareStatementsUco {
	
	@Autowired private ProfitCostShareStatementsDao dao;
	
	@Autowired private CompanyDao companyRepo;

	@Override
	public Map<String, String> getSupplierKcodeToNameMap() {
		if(Context.getCurrentUser().isDrsUser()) return this.companyRepo.querySupplierKcodeToShortEnUsNameMap();
		return null;
	}

	@Override
	public Map<String, String> getAllCompanyKcodeToLocalNameMap() {
		return companyRepo.queryAllCompanyKcodeToLocalNameMap();
	}

	@Override
	public List<SettlementPeriod> getSettlementPeriodList() {
		List<Object []> columnsList =  this.dao.querySettlementPeriodList();
		List<SettlementPeriod> listToReturn = new ArrayList<SettlementPeriod>();
		for (Object[] columns : columnsList) {
			int id = (int) columns[0];
			Date start = (Date) columns[1];
			Date end = (Date) columns[2];
			listToReturn.add(new SettlementPeriodImpl(id, start, end));
		}
		return listToReturn;
	}

	@Override
	public ProfitCostShareListReport getProfitCostShareStatementListReport(BillStatementType type,
			String supplierKcodeToFilter, String settlementPeriodId) {
		int periodId = -1;
		if(StringUtils.hasText(settlementPeriodId))
			periodId = Integer.parseInt(settlementPeriodId);
		if(!StringUtils.hasText(supplierKcodeToFilter))
			supplierKcodeToFilter = "%";
		ProfitCostShareReportImpl profitCostShareReportImpl = new ProfitCostShareReportImpl();
		List<ProfitCostShareListReportItem> profitCostShareReportItems = toStatementList(
				this.dao.queryStatementsReceivedBySupplierAndPeriod(type, supplierKcodeToFilter, Integer.valueOf(periodId)));
		profitCostShareReportImpl.setPcsStatements(profitCostShareReportItems);

		return profitCostShareReportImpl;
	}

	private List<ProfitCostShareListReportItem> toStatementList(List<Object[]> columnsList) {
		List<ProfitCostShareListReportItem> resultList = new ArrayList<>();
		for (Object[] columns : columnsList) {
			resultList.add(toStatementItem(columns));
		}
		return resultList;
	}

	private ProfitCostShareListReportItem toStatementItem(Object[] columns) {
		String statementId = (String) columns[0];
		String startDateUtc = (String) columns[1];
		String endDateUtc = (String) columns[2];
		Integer currencyId = (Integer) columns[3];
		BigDecimal total = (BigDecimal) columns[4];
		BigDecimal balance = (BigDecimal) columns[5];
		String invoiceFromSsdc = (String) columns[6];
		String invoiceFromSupplier = (String) columns[7];
		String invoiceNotes = (String) columns[8];
		BigDecimal amountTax = (BigDecimal) columns[9];
		BigDecimal amountUntaxed = (BigDecimal) columns[10];
		return new ProfitCostShareListReportItemImpl(statementId, startDateUtc, endDateUtc, currencyId, total,
				balance, invoiceFromSsdc, invoiceFromSupplier, invoiceNotes, amountTax, amountUntaxed);
	}

	@Override
	public ProfitCostShareListReportItem getProfitCostShareStatement(BillStatementType type, String statementName) {
		if(!StringUtils.hasText(statementName)) return null;
		return toStatementItem(this.dao.queryStatementByStatementName(type, statementName));
	}

	@Override
	public boolean updateProfitCostShareStatement(BillStatementType type,
			ProfitCostShareListReportItem profitCostShareListReportItem) {
		return this.dao.updateProfitCostShareInvoiceNumber(type, profitCostShareListReportItem);
	}

	@Override
	public boolean bulkUpdateProfitCostShareStatement(BillStatementType type,
			ProfitCostShareListReport profitCostShareListReport) {
		return this.dao.bulkUpdateProfitCostShareInvoiceNumber(type, profitCostShareListReport);
	}
}
