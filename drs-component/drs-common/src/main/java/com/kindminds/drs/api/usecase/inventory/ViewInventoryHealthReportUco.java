package com.kindminds.drs.api.usecase.inventory;

import java.util.List;
import java.util.Map;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.v1.model.report.InventoryHealthReport;
import com.kindminds.drs.Currency;

public interface ViewInventoryHealthReportUco {
	Map<String,String> getSupplierKcodeNames();
	List<Marketplace> getMarketplaces();
	InventoryHealthReport getReport(String marketplaceId,String supplierKcode);
	Currency getCurrency(String marketplaceId);

}
