package com.kindminds.drs.service.test.dto.statement;

import com.kindminds.drs.api.v1.model.report.Ss2spSettleableItemReport;

import java.util.List;

public class Ss2spSettleableItemReportImpl implements Ss2spSettleableItemReport {

	private String itemName;
	private String dateStart;
	private String dateEnd;
	private String isurKcode;
	private String rcvrKcode;
	private String currency;
	private String total;
	private List<Ss2spSettleableItemReportLineItem> itemList;
	
	public Ss2spSettleableItemReportImpl(
			String itemName,
			String dateStart,
			String dateEnd,
			String isurKcode,
			String rcvrKcode,
			String currency,
			String total,
			List<Ss2spSettleableItemReportLineItem> itemList){
		this.itemName=itemName;
		this.dateStart=dateStart;
		this.dateEnd=dateEnd;
		this.isurKcode=isurKcode;
		this.rcvrKcode=rcvrKcode;
		this.currency=currency;
		this.total=total;
		this.itemList=itemList;
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj instanceof Ss2spSettleableItemReport){
			Ss2spSettleableItemReport r = (Ss2spSettleableItemReport)obj;
			return this.getSettleableItemName().equals(r.getSettleableItemName())
				&& this.getDateStart().equals(r.getDateStart())
				&& this.getDateEnd().equals(r.getDateEnd())
				&& this.getIsurKcode().equals(r.getIsurKcode())
				&& this.getRcvrKcode().equals(r.getRcvrKcode())
				&& this.getCurrency().equals(r.getCurrency())
				&& this.getTotal().equals(r.getTotal())
				&& this.getItemList().equals(r.getItemList());
		}
		return false;
	}

	@Override
	public String toString() {
		return "Ss2spSettleableItemReportImpl [getSettleableItemName()=" + getSettleableItemName() + ", getDateStart()="
				+ getDateStart() + ", getDateEnd()=" + getDateEnd() + ", getIsurKcode()=" + getIsurKcode()
				+ ", getRcvrKcode()=" + getRcvrKcode() + ", getCurrency()=" + getCurrency() + ", getTotal()="
				+ getTotal() + ", getItemList()=" + getItemList() + "]";
	}

	@Override
	public String getSettleableItemName() {
		return this.itemName;
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
	public List<Ss2spSettleableItemReportLineItem> getItemList() {
		return this.itemList;
	}

}
