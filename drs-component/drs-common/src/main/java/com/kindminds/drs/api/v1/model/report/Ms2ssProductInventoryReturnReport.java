package com.kindminds.drs.api.v1.model.report;

import java.util.List;

public interface Ms2ssProductInventoryReturnReport {
	public String getTitle();
	public String getDateStart();
	public String getDateEnd();
	public String getIsurKcode();
	public String getRcvrKcode();
	public List<Ms2ssProductInventoryReturnReportItem> getLineItems();
	public interface Ms2ssProductInventoryReturnReportItem{
		public String getShipmentName();
		public String getProductSku();
		public String getQuantity();
	}
}
