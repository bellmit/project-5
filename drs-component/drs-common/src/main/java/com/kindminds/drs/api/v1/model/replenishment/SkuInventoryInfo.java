package com.kindminds.drs.api.v1.model.replenishment;

import java.io.Serializable;
import java.util.Date;

public interface SkuInventoryInfo extends Serializable {
	public String getSkuCode();
	public String getSkuName();
	public String getSupplierCode();
	public String getSupplierShortName();
	public Integer getLeadTime();
	public Integer getSellableInventory();
	public Integer getPlanningQuantity();
	public Date getPlanningExpectedDate();
	public Integer getInboundQuantity();
	public Date getInboundExpectedDate();
	public Date getExpectedArrivableWarehouseDate();
	public Integer getSalesQuantitySum();
	public Integer getSalesDays();
}
