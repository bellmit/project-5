package com.kindminds.drs.persist.v1.model.mapping.statement;

import java.io.Serializable;

public class Ss2spServiceExpenseReportItemId implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -402331954577420607L;
	private String sku;
	private String itemName;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((itemName == null) ? 0 : itemName.hashCode());
		result = prime * result + ((sku == null) ? 0 : sku.hashCode());
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
		Ss2spServiceExpenseReportItemId other = (Ss2spServiceExpenseReportItemId) obj;
		if (itemName == null) {
			if (other.itemName != null)
				return false;
		} else if (!itemName.equals(other.itemName))
			return false;
		if (sku == null) {
			if (other.sku != null)
				return false;
		} else if (!sku.equals(other.sku))
			return false;
		return true;
	}
	

}
