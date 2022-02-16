package com.kindminds.drs.v1.model.impl.statement;

import com.kindminds.drs.api.v1.model.report.Ms2ssPurchaseAllowanceReport.Ms2ssPurchaseAllowanceReportItemGroupBySku;
import com.kindminds.drs.api.v1.model.report.Ms2ssPurchaseAllowanceReport.Ms2ssPurchaseAllowanceReportRawItem;

import java.math.BigDecimal;
import java.util.List;

public class Ms2ssPurchaseAllowanceReportItemGroupBySkuImpl implements Ms2ssPurchaseAllowanceReportItemGroupBySku {
	
	private String sku = null;
	private List<Ms2ssPurchaseAllowanceReportRawItem> rawItems;
	
	public Ms2ssPurchaseAllowanceReportItemGroupBySkuImpl(String sku, List<Ms2ssPurchaseAllowanceReportRawItem> rawItems){
		this.sku = sku;
		this.rawItems = rawItems;
	}
	
	@Override
	public String getSku() {
		return this.sku;
	}

	@Override
	public String getSubtotalStr() {
		return this.getSubtotal().toString();
	}

	@Override
	public BigDecimal getSubtotal() {
		BigDecimal subtotal= BigDecimal.ZERO;
		for(Ms2ssPurchaseAllowanceReportRawItem item:rawItems ){
			subtotal=subtotal.add(item.getStatementAmount().setScale(item.getStatementCurrency().getScale()));
		}
		return subtotal;
	}

	@Override
	public List<Ms2ssPurchaseAllowanceReportRawItem> getRawItems() {
		return this.rawItems;
	}

	@Override
	public String toString() {
		return "Ms2ssPurchaseAllowanceReportItemGroupBySkuImpl [getSku()="
				+ getSku() + ", getSubtotalStr()=" + getSubtotalStr()
				+ ", getSubtotal()=" + getSubtotal() + ", getRawItems()="
				+ getRawItems() + "]";
	}
	
	

}
