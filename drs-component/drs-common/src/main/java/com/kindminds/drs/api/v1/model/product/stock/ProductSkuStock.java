package com.kindminds.drs.api.v1.model.product.stock;

import com.kindminds.drs.Currency;

public interface ProductSkuStock {
	public String getShipmentName();
	public String getDrsSkuCode();
	public String getSellerCompanyKcode();
	public String getBuyerCompanyKcode();
	public Currency getCurrency();
	public String getUnitCost();
	public Integer getQtyOrder();
	public Integer getQtySold();
	public Integer getQtyReturned();
	public String getParentShipmentName();
}
