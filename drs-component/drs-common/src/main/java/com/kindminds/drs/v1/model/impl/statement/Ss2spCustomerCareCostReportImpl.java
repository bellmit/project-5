package com.kindminds.drs.v1.model.impl.statement;

import com.kindminds.drs.api.v1.model.report.Ss2spCustomerCareCostReport;
import com.kindminds.drs.api.v1.model.report.StatementInfo;

import java.math.BigDecimal;
import java.util.List;

public class Ss2spCustomerCareCostReportImpl implements Ss2spCustomerCareCostReport {

	private StatementInfo info;
	private List<Ss2spCustomerCareCostReportItem> items;
	
	public void setInfo(StatementInfo info) {
		this.info = info;
	}
	
	public void setItems(List<Ss2spCustomerCareCostReportItem> itemList){ this.items = itemList; }
	
	@Override
	public String toString() {
		return "Ss2spCustomerCareCostReportImpl [getDateStart()="
				+ getDateStart() + ", getDateEnd()=" + getDateEnd()
				+ ", getIsurKcode()=" + getIsurKcode() + ", getRcvrKcode()="
				+ getRcvrKcode() + ", getItems()=" + getItems() + "]";
	}
	
	@Override
	public String getDateStart() {
		return this.info.getDateStart();
	}
	
	@Override
	public String getDateEnd() {
		return this.info.getDateEnd();
	}
	
	@Override
	public String getIsurKcode() {
		return this.info.getIsurKcode();
	}

	@Override
	public String getRcvrKcode() {
		return this.info.getRcvrKcode();
	}

	@Override
	public String getAmountTotal() {
		BigDecimal total = BigDecimal.ZERO;
		for(Ss2spCustomerCareCostReportItem item:this.items){
			total = total.add(item.getAmount());
		}
		return total.toPlainString();
	}
	
	@Override
	public List<Ss2spCustomerCareCostReportItem> getItems() {
		return this.items;
	}
	
	

}
