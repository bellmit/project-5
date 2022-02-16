package com.kindminds.drs.v1.model.impl;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.product.stock.ProductSkuStock;

public class ProductSkuStockImplSvc implements ProductSkuStock {
	
	private String shipmentName;
	private String drsSkuCode;
	private String sellerCompanyKcode;
	private String buyerCompanyKcode;
	private Currency currency;
	private String unitCost;
	private Integer qtyOrder;
	private Integer qtySold;
	private Integer qtyReturned;
	private String parentShipmentName;
	
	public ProductSkuStockImplSvc(
			String shipmentName,
			String drsSkuCode,
			String sellerCompanyKcode,
			String buyerCompanyKcode,
			Currency currency,
			String unitCost,
			Integer qtyOrder,
			Integer qtySold,
			Integer qtyReturned,
			String parentShipmentName) {
		super();
		this.shipmentName = shipmentName;
		this.drsSkuCode = drsSkuCode;
		this.sellerCompanyKcode = sellerCompanyKcode;
		this.buyerCompanyKcode = buyerCompanyKcode;
		this.currency = currency;
		this.unitCost = unitCost;
		this.qtyOrder = qtyOrder;
		this.qtySold = qtySold;
		this.qtyReturned = qtyReturned;
		this.parentShipmentName = parentShipmentName;
	}
	
	@Override
	public String getShipmentName() {
		return this.shipmentName;
	}
	
	@Override
	public String getDrsSkuCode() {
		return this.drsSkuCode;
	}
	
	@Override
	public String getSellerCompanyKcode() {
		return this.sellerCompanyKcode;
	}
	
	@Override
	public String getBuyerCompanyKcode() {
		return this.buyerCompanyKcode;
	}
	
	@Override
	public Currency getCurrency() {
		return this.currency;
	}
	
	@Override
	public String getUnitCost() {
		return this.unitCost;
	}
	
	@Override
	public Integer getQtyOrder() {
		return this.qtyOrder;
	}
	
	@Override
	public Integer getQtySold() {
		return this.qtySold;
	}
	
	@Override
	public Integer getQtyReturned() {
		return this.qtyReturned;
	}

	@Override
	public String getParentShipmentName() {
		return this.parentShipmentName;
	}
	
	@Override 
	public boolean equals(Object obj){
		if(obj instanceof ProductSkuStock){
			ProductSkuStock pss = (ProductSkuStock)obj;
			boolean parentShipmentNameEqual = false;
			if(this.getParentShipmentName()==null && pss.getParentShipmentName()==null) parentShipmentNameEqual = true;
			else if( this.getParentShipmentName().equals(pss.getParentShipmentName())) parentShipmentNameEqual = true;
			return this.getShipmentName().equals(pss.getShipmentName())
				&& this.getDrsSkuCode().equals(pss.getDrsSkuCode())
				&& this.getSellerCompanyKcode().equals(pss.getSellerCompanyKcode())
				&& this.getBuyerCompanyKcode().equals(pss.getBuyerCompanyKcode())
				&& this.getCurrency().equals(pss.getCurrency())
				&& this.getUnitCost().equals(pss.getUnitCost())
				&& this.getQtyOrder().equals(pss.getQtyOrder())
				&& this.getQtySold().equals(pss.getQtySold())
				&& parentShipmentNameEqual;
		}
		return false;
	}
	@Override
	public String toString() {
		return "ProductSkuStockImplSvc [getShipmentName()=" + getShipmentName() + ", getDrsSkuCode()=" + getDrsSkuCode()
				+ ", getSellerCompanyKcode()=" + getSellerCompanyKcode() + ", getBuyerCompanyKcode()="
				+ getBuyerCompanyKcode() + ", getCurrency()=" + getCurrency() + ", getUnitCost()=" + getUnitCost()
				+ ", getQtyOrder()=" + getQtyOrder() + ", getQtySold()=" + getQtySold() + ", getQtyReturned()="
				+ getQtyReturned() + ", getParentShipmentName()=" + getParentShipmentName() + "]";
	}

}




















