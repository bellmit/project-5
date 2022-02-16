package com.kindminds.drs.v1.model.impl.statement;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.report.Ms2ssSettleableItemReport;
import com.kindminds.drs.api.v1.model.report.StatementInfo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class Ms2ssSettleableItemReportImpl implements Ms2ssSettleableItemReport {

	private String settleableItemDrsName;
	private StatementInfo info;
	private Currency currency;
	private List<Ms2ssSettleableItemReportLineItem> lineItems=null;
	
	public void setInfo(StatementInfo info) {
		this.info = info;
	}
	
	public void setTitle(String siDrsName){
		this.settleableItemDrsName = siDrsName;
	}
	
	public void setCurrency(Currency currency){
		this.currency = currency;
	}

	public void setLineItems(List<Ms2ssSettleableItemReportLineItem> lineItems){
		this.lineItems=lineItems;
	}

	@Override
	public String toString() {
		return "Ms2ssSettleableItemReportImpl [getTitle()=" + getTitle() + ", getDateStart()=" + getDateStart()
				+ ", getDateEnd()=" + getDateEnd() + ", getIsurKcode()=" + getIsurKcode() + ", getRcvrKcode()="
				+ getRcvrKcode() + ", getCurrency()=" + getCurrency() + ", getTotal()=" + getTotal()
				+ ", getItemList()=" + getItemList() + "]";
	}

	@Override
	public String getTitle(){
		return this.settleableItemDrsName;
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
	public String getCurrency() {
		return this.currency.name();
	}

	@Override
	public String getTotal() {
		BigDecimal total = BigDecimal.ZERO;
		for(Ms2ssSettleableItemReportLineItem item:this.lineItems){
			total=total.add(item.getNumericAmount());
		}
		return total.setScale(this.currency.getScale(),RoundingMode.HALF_UP).toString();
	}
	
	@Override
	public List<Ms2ssSettleableItemReportLineItem> getItemList() {
		return this.lineItems;
	}
	

}
