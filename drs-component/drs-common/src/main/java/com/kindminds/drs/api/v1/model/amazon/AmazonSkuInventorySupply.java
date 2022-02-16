package com.kindminds.drs.api.v1.model.amazon;

public interface AmazonSkuInventorySupply {
	String getMarketplaceSku();
	Integer getTotalSupplyQuantity();
	Integer getInStockSupplyQuantity();
	Integer getDetailQuantityInStock();
	Integer getDetailQuantityInbound();
	Integer getDetailQuantityTransfer();
}
