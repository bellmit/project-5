package com.kindminds.drs.api.v1.model.product.stock;

public interface ProductSkuStockChange {
	public String getSku();
	public int getQuantity();
	public String getIshipmentName();
	public String getUshipmentName();
}
