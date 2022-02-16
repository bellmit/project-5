package com.kindminds.drs.v1.model.impl;

import com.kindminds.drs.api.v1.model.product.ProductSkuInventoryHistory;

import java.util.Date;

public class ProductSkuInventoryHistoryImpl implements ProductSkuInventoryHistory {
	
	private int warehouseId;
	private int productSkuId;
	private Date snapshotDate;
	private Integer fbaQtyInStockPlusfbaQtyTransfer=0;
	private Integer qtyOrderedLastDay=null;

	public ProductSkuInventoryHistoryImpl(
			int warehouseId,
			Date snapshotDate,
			int productSkuId) {
		this.warehouseId = warehouseId;
		this.productSkuId = productSkuId;
		this.snapshotDate = snapshotDate;
	}
	
	public void setFbaQtyInStockPlusfbaQtyTransfer(Integer fbaQtyInStockPlusfbaQtyTransfer) {
		this.fbaQtyInStockPlusfbaQtyTransfer = fbaQtyInStockPlusfbaQtyTransfer;
	}
	
	public void setQtyOrderedLastDay(Integer qtyOrderedLastDay) {
		this.qtyOrderedLastDay = qtyOrderedLastDay;
	}

	@Override
	public String toString() {
		return "ProductSkuInventoryHistoryImpl [getWarehouseId()=" + getWarehouseId() + ", getProductSkuId()="
				+ getProductSkuId() + ", getSnapshotDate()=" + getSnapshotDate() + ", getQtyInStock()="
				+ getQtyInStock() + ", getQtySold()=" + getQtySold() + "]";
	}

	@Override
	public int getWarehouseId() {
		return this.warehouseId;
	}

	@Override
	public int getProductSkuId() {
		return this.productSkuId;
	}

	@Override
	public Date getSnapshotDate() {
		return this.snapshotDate;
	}

	@Override
	public Integer getQtyInStock() {
		return this.fbaQtyInStockPlusfbaQtyTransfer;
	}

	@Override
	public Integer getQtySold() {
		return this.qtyOrderedLastDay==null?0:this.qtyOrderedLastDay;
	}

}
