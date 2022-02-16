package com.kindminds.drs.v1.model.impl.report;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.v1.model.report.InventoryHealthReport.InventoryHealthReportLineItem;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class InventoryHealthReportLineItemImpl implements InventoryHealthReportLineItem {

	private Integer marketplaceId;
	private String sku;
	private String productName;
	private String condition;
	private Integer unsellableQuantity;
	private Integer sellableQuantity;
	private Integer qtyToBeChargedLtsf6Mo;
	private Integer qtyToBeChargedLtsf12Mo;
	private BigDecimal projectedLtsf6Mo;
	private BigDecimal projectedLtsf12Mo;
	private Integer unitsShippedLast30Days;
	private BigDecimal weeksOfCoverT30;
	
	public InventoryHealthReportLineItemImpl(
			Integer marketplaceId,
			String sku,
			String productName,
			String condition,
			Integer unsellableQuantity,
			Integer sellableQuantity,
			Integer qtyToBeChargedLtsf6Mo,
			Integer qtyToBeChargedLtsf12Mo,
			BigDecimal projectedLtsf6Mo,
			BigDecimal projectedLtsf12Mo,
			Integer unitsShippedLast30Days,
			BigDecimal weeksOfCoverT30){
		this.marketplaceId = marketplaceId;
		this.sku = sku;
		this.productName = productName;
		this.condition = condition;
		this.unsellableQuantity = unsellableQuantity;
		this.sellableQuantity = sellableQuantity;
		this.qtyToBeChargedLtsf6Mo = qtyToBeChargedLtsf6Mo;
		this.qtyToBeChargedLtsf12Mo = qtyToBeChargedLtsf12Mo;
		this.projectedLtsf6Mo = projectedLtsf6Mo;
		this.projectedLtsf12Mo = projectedLtsf12Mo;
		this.unitsShippedLast30Days = unitsShippedLast30Days;
		this.weeksOfCoverT30 = weeksOfCoverT30;
	}

	@Override
	public String getSku() {
		return this.sku;
	}

	@Override
	public String getProductName() {
		return this.productName;
	}

	@Override
	public String getCondition() {
		return this.condition;
	}

	@Override
	public Integer getUnsellableQuantity() {
		return this.unsellableQuantity;
	}

	@Override
	public Integer getSellableQuantity() {
		return this.sellableQuantity;
	}

	@Override
	public Integer getQtyToBeChargedLtsf6Mo() {
		return this.qtyToBeChargedLtsf6Mo;
	}

	@Override
	public Integer getQtyToBeChargedLtsf12Mo() {
		return this.qtyToBeChargedLtsf12Mo;
	}

	@Override
	public String getProjectedLtsf6Mo() {
		if(this.projectedLtsf6Mo==null) return null;
		return this.projectedLtsf6Mo.setScale(this.getScale()).toPlainString();
	}

	@Override
	public String getProjectedLtsf12Mo() {
		return this.projectedLtsf12Mo.setScale(this.getScale()).toPlainString();
	}

	@Override
	public Integer getUnitsShippedLast30Days() {
		return this.unitsShippedLast30Days;
	}

	@Override
	public String getWeeksOfCoverT30() {
		if(this.weeksOfCoverT30==null) return "Infinite";
		return this.weeksOfCoverT30.setScale(2,RoundingMode.HALF_UP).toPlainString();
	}
	
	private int getScale(){
		Marketplace marketplace = Marketplace.fromKey(this.marketplaceId);
		return marketplace.getCurrency().getScale();
	}

}
