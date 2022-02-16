package com.kindminds.drs.v1.model.impl.replenishment;

import com.kindminds.drs.api.v1.model.replenishment.ReplenishmentPredition.Supplier;

import java.io.Serializable;

public class SupplierImpl implements Supplier, Serializable {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((supplierName == null) ? 0 : supplierName.hashCode());
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
		SupplierImpl other = (SupplierImpl) obj;
		if (supplierName == null) {
			if (other.supplierName != null)
				return false;
		} else if (!supplierName.equals(other.supplierName))
			return false;
		return true;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 6585070491400106437L;
	
	private String supplierKcode;
	private String supplierName;
		
	public SupplierImpl(String supplierKcode ,String supplierName) {
		this.supplierKcode = supplierKcode;
		this.supplierName = supplierName;
	}

	@Override
	public String getSupplierKcode() {
		return this.supplierKcode;
	}
	
	@Override
	public String getSupplierName() {
		return this.supplierName;
	}
	
	@Override
	public String toString() {
		//return "SupplierImpl [getSupplierName()=" + getSupplierName() + "]";
		return getSupplierKcode();
	}
	
}
