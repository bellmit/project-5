package com.kindminds.drs.persist.v1.model.mapping.replenishment;

import java.util.Date;





import com.kindminds.drs.api.v1.model.replenishment.SkuInventoryInfo;


public class SkuInventoryInfoImpl implements SkuInventoryInfo {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8366417513327340680L;
	
	//@Id//@Column(name="code_by_drs")
	private String skuCode;
	//@Column(name="sku_name")
	private String skuName;
	//@Column(name="k_code")
	private String supplierCode;
	//@Column(name="short_name_en_us")
	private String supplierShortName;
	//@Column(name="manufacturing_lead_time_days")
	private int leadTime;
	//@Column(name="qty_in_stock")
	private Integer sellableInventory;		
	//@Column(name="pl_sum_qty")
	private Integer planningQuantity;			
	//@Column(name="pl_expected_date")
	private Date planningExpectedDate;
	//@Column(name="ib_sum_qty")
	private Integer inboundQuantity;
	//@Column(name="ib_expected_date")
	private Date inboundExpectedDate;			
	//@Column(name="expected_date")
	private Date expectedArribalWarehouseDate;
	//@Column(name="sum_qty_sold")
	private Integer salesQuantitySum;
	//@Column(name="sold_days")
	private Integer salesDays;

	public SkuInventoryInfoImpl() {
	}

	public SkuInventoryInfoImpl(String skuCode, String skuName, String supplierCode, String supplierShortName, int leadTime, Integer sellableInventory, Integer planningQuantity, Date planningExpectedDate, Integer inboundQuantity, Date inboundExpectedDate, Date expectedArribalWarehouseDate, Integer salesQuantitySum, Integer salesDays) {
		this.skuCode = skuCode;
		this.skuName = skuName;
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
