package com.kindminds.drs.web.data.dto;

import java.util.ArrayList;
import java.util.List;

import com.kindminds.drs.v1.model.impl.statement.ProfitCostShareListReportItemImpl;
import com.kindminds.drs.api.v1.model.report.ProfitCostShareListReport;


public class ProfitCostShareListReportImpl implements ProfitCostShareListReport{

	private List<ProfitCostShareListReportItemImpl> profitCostShareList;

	public ProfitCostShareListReportImpl() {
		this.profitCostShareList = new ArrayList<ProfitCostShareListReportItemImpl>();
	}

	public ProfitCostShareListReportImpl(List<ProfitCostShareListReportItem> profitCostShareList) {
		this.profitCostShareList = new ArrayList<ProfitCostShareListReportItemImpl>();
		for(ProfitCostShareListReportItem profitCostShareItem : profitCostShareList) {
			this.profitCostShareList.add((ProfitCostShareListReportItemImpl)profitCostShareItem);
		}
	}

	public List<ProfitCostShareListReportItemImpl> getProfitCostShareList() {
		return profitCostShareList;
	}

	public void setProfitCostShareList(List<ProfitCostShareListReportItemImpl> profitCostShareList) {
		this.profitCostShareList = profitCostShareList;
	}

	@Override
	public List<ProfitCostShareListReportItem> getPcsStatements() {
		List<ProfitCostShareListReportItem> profitCostShareStatements = new ArrayList<ProfitCostShareListReportItem>();
		for(ProfitCostShareListReportItemImpl profitCostShareItem : profitCostShareList) {
			profitCostShareStatements.add(new ProfitCostShareListReportItemImpl(profitCostShareItem));
		}
		return profitCostShareStatements;
	}
}
