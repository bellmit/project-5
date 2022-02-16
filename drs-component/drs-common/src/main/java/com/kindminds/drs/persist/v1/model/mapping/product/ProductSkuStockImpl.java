package com.kindminds.drs.persist.v1.model.mapping.product;

import java.math.BigDecimal;




import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.product.stock.ProductSkuStock;


public class ProductSkuStockImpl implements ProductSkuStock {

	//@Id ////@Column(name="id")
	private int id;
	//@Column(name="shipment_name")
	private String shipmentName;
	//@Column(name="sku_code_by_drs")
	private String drsSkuCode;
	//@Column(name="seller_company_kcode")
	private String sellerCompanyKcode;
	//@Column(name="buyer_company_kcode")
	private String buyerCompanyKcode;
	//@Column(name="currency_name")
	private String currencyName;
	//@Column(name="unit_cost")
	private BigDecimal unitCost;
	//@Column(name="qty_order")
	private Integer qtyOrder;
	//@Column(name="qty_sold")
	private Integer qtySold;
	//@Column(name="qty_returned")
	private Integer qtyReturned;
	//@Column(name="parent_shipment_name")
	private String parentShipmentName;

	public ProductSkuStockImpl() {
	}

	public ProductSkuStockImpl(int id, String shipmentName, String drsSkuCode, String sellerCompanyKcode, String buyerCompanyKcode, String currencyName, BigDecimal unitCost, Integer qtyOrder, Integer qtySold, Integer qtyReturned, String parentShipmentName) {
		this.id = id;
		this.shipmentName = shipmentName;
		this.drsSkuCode = drsSkuCode;
		this.sellerCompanyKcode = sellerCompanyKcode;
		this.buyerCompanyKcode = buyerCompanyKcode;
		this.currencyName = currencyName;
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
		return Currency.valueOf(this.currencyName);
	}
	
	@Override
	public String getUnitCost() {
		return this.unitCost.stripTrailingZeros().toPlainString();
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
	public String toString() {
		return "ProductSkuStockImpl [getShipmentName()=" + getShipmentName() + ", getDrsSkuCode()=" + getDrsSkuCode()
				+ ", getSellerCompanyKcode()=" + getSellerCompanyKcode() + ", getBuyerCompanyKcode()="
				+ getBuyerCompanyKcode() + ", getCurrency()=" + getCurrency() + ", getUnitCost()=" + getUnitCost()
				+ ", getQtyOrder()=" + getQtyOrder() + ", getQtySold()=" + getQtySold() + ", getQtyReturned()="
				+ getQtyReturned() + ", getParentShipmentName()=" + getParentShipmentName() + "]";
	}

}
