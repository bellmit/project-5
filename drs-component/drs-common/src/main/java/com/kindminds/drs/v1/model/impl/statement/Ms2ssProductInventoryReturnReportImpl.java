package com.kindminds.drs.v1.model.impl.statement;

import com.kindminds.drs.api.v1.model.report.Ms2ssProductInventoryReturnReport;
import com.kindminds.drs.api.v1.model.report.StatementInfo;

import java.util.List;

public class Ms2ssProductInventoryReturnReportImpl implements Ms2ssProductInventoryReturnReport {
	
	private String title;
	private StatementInfo info;
	private List<Ms2ssProductInventoryReturnReportItem> lineItems=null;
	
	public void setTitle(String value){ this.title=value; }
	
	public void setInfo(StatementInfo info) {
		this.info = info;
	}
	
	public void setLineItems(List<Ms2ssProductInventoryReturnReportItem> items){ this.lineItems=items; }

	@Override
	public String toString() {
		return "Ms2ssProductInventoryReturnReportImpl [getTitle()=" + getTitle() + ", getDateStart()=" + getDateStart()
				+ ", getDateEnd()=" + getDateEnd() + ", getIsurKcode()=" + getIsurKcode() + ", getRcvrKcode()="
				+ getRcvrKcode() + ", getLineItems()=" + getLineItems() + "]";
	}

	@Override
	public String getTitle() {
		return this.title;
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
	public List<Ms2ssProductInventoryReturnReportItem> getLineItems() {
		return this.lineItems;
	}

}
