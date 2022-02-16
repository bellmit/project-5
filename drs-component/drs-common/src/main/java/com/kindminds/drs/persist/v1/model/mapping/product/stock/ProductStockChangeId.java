package com.kindminds.drs.persist.v1.model.mapping.product.stock;

import java.io.Serializable;

public class ProductStockChangeId implements Serializable {

	private static final long serialVersionUID = 3584611702830967317L;
	private String skuCodeByDrs;
	private int quantity;
	private String u_shipment_name;
	private String i_shipemnt_name;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((i_shipemnt_name == null) ? 0 : i_shipemnt_name.hashCode());
		result = prime * result + quantity;
		result = prime * result + ((skuCodeByDrs == null) ? 0 : skuCodeByDrs.hashCode());
		result = prime * result + ((u_shipment_name == null) ? 0 : u_shipment_name.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductStockChangeId other = (ProductStockChangeId) obj;
		if (i_shipemnt_name == null) {
			if (other.i_shipemnt_name != null)
				return false;
		} else if (!i_shipemnt_name.equals(other.i_shipemnt_name))
			return false;
		if (quantity != other.quantity)
			return false;
		if (skuCodeByDrs == null) {
			if (other.skuCodeByDrs != null)
				return false;
		} else if (!skuCodeByDrs.equals(other.skuCodeByDrs))
			return false;
		if (u_shipment_name == null) {
			if (other.u_shipment_name != null)
				return false;
		} else if (!u_shipment_name.equals(other.u_shipment_name))
			return false;
		return true;
	}
}
