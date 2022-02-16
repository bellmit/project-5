package com.kindminds.drs.v1.model.impl.replenishment;

import com.kindminds.drs.api.v1.model.replenishment.SkuInventoryInfo;

import java.util.Date;

public class SkuInventoryImpl implements SkuInventoryInfo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 785330609939344367L;

	private String skuCode;
	private String skuName;
	private String supplierCode;
	private String supplierShortName;
	private Integer leadTime;
	private Integer sellableInventory;
	private Integer planningQuantity;
	private Date planningExpectedDate;
	private Integer inboundQuantity;
	private Date inboundExpectedDate;
	private Date expectedArribalWarehouseDate;
	private Integer salesQuantitySum;
	private Integer salesDays;
	
	public SkuInventoryImpl(String skuCode, String supplierCode,
			String supplierShortName, Integer leadTime,
			Integer sellableInventory,Integer planningQuantity, Date planningExpectedDate,
			Integer inboundQuantity, Date inboundExpectedDate,
			Date expectedArribalWarehouseDate, Integer salesQuantitySum,
			Integer salesDays) {
		super();
		this.skuCode = skuCode;
		this.supplierCode = supplierCode;
		this.supplierShortName = supplierShortName;
		this.leadTime = leadTime;
		this.sellableInventory = sellableInventory;
		this.planningQuantity = planningQuantity;
		this.planningExpectedDate = planningExpectedDate; 
		this.inboundQuantity = inboundQuantity;
		this.inboundExpectedDate = inboundExpectedDate; 
		this.expectedArribalWarehouseDate = expectedArribalWarehouseDate;
		this.salesQuantitySum = salesQuantitySum;
		this.salesDays = salesDays;
	}

	@Override
	public String getSkuCode() {
		return this.skuCode;
	}

	@Override
	public String getSkuName() {
		return this.skuName;
	}

	@Override
	public String getSupplierCode() {
		return this.supplierCode;
	}

	@Override
	public String getSupplierShortName() {
		return this.supplierShortName;
	}

	@Override
	public Integer getLeadTime() {
		return this.leadTime;
	}

	@Override
	public Integer getSellableInventory() {
		return this.sellableInventory;
	}

	@Override
	public Integer getPlanningQuantity() {
		return this.planningQuantity;
	}
	
	@Override
	public Date getPlanningExpectedDate() {
		return this.planningExpectedDate;
	}
	
	@Override
	public Integer getInboundQuantity() {
		return this.inboundQuantity;
	}

	@Override
	public Date getInboundExpectedDate() {
		return this.inboundExpectedDate;
	}
	
	@Override
	public Date getExpectedArrivableWarehouseDate() {
		return this.expectedArribalWarehouseDate;
	}

	@Override
	public Integer getSalesQuantitySum() {
		return this.salesQuantitySum;
	}

	@Override
	public Integer getSalesDays() {
		return this.salesDays;
	}

}
