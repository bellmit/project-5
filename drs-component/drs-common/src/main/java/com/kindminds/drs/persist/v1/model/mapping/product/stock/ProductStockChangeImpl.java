package com.kindminds.drs.persist.v1.model.mapping.product.stock;
import com.kindminds.drs.api.v1.model.product.stock.ProductSkuStockChange;

 //@IdClass(ProductStockChangeId.class)
public class ProductStockChangeImpl implements ProductSkuStockChange {
	
	//@Id //@Column(name="sku_code_by_drs")
	private String skuCodeByDrs;
	//@Id //@Column(name="quantity")
	private int quantity;
	//@Id //@Column(name="u_shipment_name")
	private String u_shipment_name;
	//@Id //@Column(name="i_shipment_name")
	private String i_shipemnt_name;
	
	@Override
	public String toString() {
		return "ProductStockChangeImpl [getSku()=" + getSku() + ", getQuantity()=" + getQuantity()
				+ ", getIshipmentName()=" + getIshipmentName() + ", getUshipmentName()=" + getUshipmentName() + "]";
	}
	
	@Override
	public String getSku() {
		return this.skuCodeByDrs;
	}
	
	@Override
	public int getQuantity() {
		return this.quantity;
	}
	
	@Override
	public String getIshipmentName() {
		return this.i_shipemnt_name;
	}
	
	@Override
	public String getUshipmentName() {
		return this.u_shipment_name;
	}

}
