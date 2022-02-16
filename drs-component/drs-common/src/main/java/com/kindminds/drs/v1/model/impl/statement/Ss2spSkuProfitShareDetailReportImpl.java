package com.kindminds.drs.v1.model.impl.statement;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.report.Ss2spSkuProfitShareDetailReport;
import com.kindminds.drs.api.v1.model.report.StatementInfo;

import java.math.BigDecimal;
import java.util.List;

public class Ss2spSkuProfitShareDetailReportImpl implements Ss2spSkuProfitShareDetailReport {

	private StatementInfo info;
	private String sku;
	private Currency currency;
	private List<Ss2spSkuProfitShareDetailReportShippedItem> shippedItems;
	private List<Ss2spSkuProfitShareDetailReportRefundedItem> refundedItems;
	
	public void setSku(String sku) {
		this.sku = sku;
	}
	
	public void setInfo(StatementInfo info) {
		this.info = info;
	}
	
	public void setShippedItems(List<Ss2spSkuProfitShareDetailReportShippedItem> shippedItems) {
		this.shippedItems = shippedItems;
	}
	
	public void setRefundedItems(List<Ss2spSkuProfitShareDetailReportRefundedItem> refundedItems) {
		this.refundedItems = refundedItems;
	}

	@Override
	public String toString() {
		return "Ss2spSkuProfitShareDetailReportImpl [getDateStart()=" + getDateStart() + ", getDateEnd()="
				+ getDateEnd() + ", getIsurKcode()=" + getIsurKcode() + ", getRcvrKcode()=" + getRcvrKcode()
				+ ", getSku()=" + getSku() + ", getSubTotal()=" + getTotal() + ", getCurrency()=" + getCurrency()
				+ ", getShippedItems()=" + getShippedItems() + "]";
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
	public String getSku() {
		return this.sku;
	}

	@Override
	public String getTotal() {
		if(this.shippedItems.isEmpty()&&this.refundedItems.isEmpty()) return null;
		BigDecimal total = BigDecimal.ZERO;
		for(Ss2spSkuProfitShareDetailReportShippedItem item:this.shippedItems){
			total=total.add(((Ss2spSkuProfitShareDetailReportShippedItemImpl)item).getRawSubtotal());
		}
		for(Ss2spSkuProfitShareDetailReportRefundedItem item:this.refundedItems){
			total=total.add(((Ss2spSkuProfitShareDetailReportRefundedItemImpl)item).getRawSubtotal());
		}
		return total.toPlainString();
	}

	@Override
	public Currency getCurrency() {
		return this.currency;
	}

	@Override
	public List<Ss2spSkuProfitShareDetailReportShippedItem> getShippedItems() {
		return this.shippedItems;
	}
	
	@Override
	public List<Ss2spSkuProfitShareDetailReportRefundedItem> getRefundedItems() {
		return this.refundedItems;
	}

}
