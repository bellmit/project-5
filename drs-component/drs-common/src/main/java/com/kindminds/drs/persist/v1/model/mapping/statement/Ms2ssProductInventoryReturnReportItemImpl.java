package com.kindminds.drs.persist.v1.model.mapping.statement;


import com.kindminds.drs.api.v1.model.report.Ms2ssProductInventoryReturnReport;

public class Ms2ssProductInventoryReturnReportItemImpl implements Ms2ssProductInventoryReturnReport.Ms2ssProductInventoryReturnReportItem {
	
	//@Id ////@Column(name="id")
	private int id;
	//@Column(name="shipment_name")
	private String shipmentName;
	//@Column(name="product_sku")
	private String productSku;
	//@Column(name="quantity")
	private Integer quantity;

	public Ms2ssProductInventoryReturnReportItemImpl(String shipmentName, String productSku, Integer quantity) {
		this.shipmentName = shipmentName;
		this.productSku = productSku;
		this.quantity = quantity;
	}

	@Override
	public String getShipmentName() {
		return this.shipmentName;
	}

	@Override
	public String getProductSku() {
		return this.productSku;
	}

	@Override
	public String getQuantity() {
		return this.quantity.toString();
	}

}
