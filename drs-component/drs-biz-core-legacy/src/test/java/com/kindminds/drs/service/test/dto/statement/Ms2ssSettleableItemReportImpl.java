package com.kindminds.drs.service.test.dto.statement;

import com.kindminds.drs.api.v1.model.report.Ms2ssSettleableItemReport;

import java.util.List;

public class Ms2ssSettleableItemReportImpl implements Ms2ssSettleableItemReport {

	private String title;
	private String dateStart;
	private String dateEnd;
	private String isurKcode;
	private String rcvrKcode;
	private String currency;
	private String total;
	private List<Ms2ssSettleableItemReportLineItem> itemList;

	public Ms2ssSettleableItemReportImpl(
			String title,
			String dateStart,
			String dateEnd,
			String isurKcode,
			String rcvrKcode,
			String currency,
			String total,
			List<Ms2ssSettleableItemReportLineItem> itemList){
		this.title = title;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		this.isurKcode = isurKcode;
		this.rcvrKcode = rcvrKcode;
		this.currency = currency;
		this.total = total;
		this.itemList = itemList;
	}

	@Override
	public boolean equals(Object obj){
		if(obj instanceof Ms2ssSettleableItemReport){
			Ms2ssSettleableItemReport report = (Ms2ssSettleableItemReport)obj;
			return this.getTitle().equals(report.getTitle())
				&& this.getDateStart().equals(report.getDateStart())
				&& this.getDateEnd().equals(report.getDateEnd())
				&& this.getIsurKcode().equals(report.getIsurKcode())
				&& this.getRcvrKcode().equals(report.getRcvrKcode())
				&& this.getCurrency().equals(report.getCurrency())
				&& this.getTotal().equals(report.getTotal())
				&& this.getItemList().equals(report.getItemList());
		}
		return false;
	}

	@Override
	public String toString() {
		return "Ms2ssSettleableItemReportImpl [getTitle()=" + getTitle() + ", getDateStart()=" + getDateStart()
				+ ", getDateEnd()=" + getDateEnd() + ", getIsurKcode()=" + getIsurKcode() + ", getRcvrKcode()="
				+ getRcvrKcode() + ", getCurrency()=" + getCurrency() + ", getTotal()=" + getTotal()
				+ ", getItemList()=" + getItemList() + "]";
	}

	@Override
	public String getTitle() {
		return this.title;
	}

	@Override
	public String getDateStart() {
		return this.dateStart;
	}

	@Override
	public String getDateEnd() {
		return this.dateEnd;
	}

	@Override
	public String getIsurKcode() {
		return this.isurKcode;
	}

	@Override
	public String getRcvrKcode() {
		return this.rcvrKcode;
	}

	@Override
	public String getCurrency() {
		return this.currency;
	}

	@Override
	public String getTotal() {
		return this.total;
	}

	@Override
	public List<Ms2ssSettleableItemReportLineItem> getItemList() {
		return this.itemList;
	}

}
