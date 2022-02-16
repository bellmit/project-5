package com.kindminds.drs.persist.v1.model.mapping.statement;

import java.io.Serializable;

public class Ss2spPaymentAndRefundItemId implements Serializable {

	private static final long serialVersionUID = 5688807108711997667L;
	private String sku;
	private String itemType;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((itemType == null) ? 0 : itemType.hashCode());
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
		Ss2spPaymentAndRefundItemId other = (Ss2spPaymentAndRefundItemId) obj;
		if (itemType == null) {
			if (other.itemType != null)
				return false;
		} else if (!itemType.equals(other.itemType))
			return false;
		if (sku == null) {
			if (other.sku != null)
				return false;
		} else if (!sku.equals(other.sku))
			return false;
		return true;
	}
}
