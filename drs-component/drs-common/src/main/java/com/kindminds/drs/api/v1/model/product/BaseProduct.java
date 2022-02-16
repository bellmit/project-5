package com.kindminds.drs.api.v1.model.product;

import java.util.List;

public interface BaseProduct extends Product {
	public String getSupplierKcode();
	public String getCategory();
	public String getCodeByDrs();
	public List<SKU> getSkuList();
}
