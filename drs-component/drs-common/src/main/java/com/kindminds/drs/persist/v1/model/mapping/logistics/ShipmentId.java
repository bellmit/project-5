package com.kindminds.drs.persist.v1.model.mapping.logistics;

import java.io.Serializable;

public class ShipmentId implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3993942383767471225L;
	private String name;
	private String sellerCompanyKcode;
	private String buyerCompanyKcode;
	private String status;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((buyerCompanyKcode == null) ? 0 : buyerCompanyKcode
						.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime
				* result
				+ ((sellerCompanyKcode == null) ? 0 : sellerCompanyKcode
						.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
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
		ShipmentId other = (ShipmentId) obj;
		if (buyerCompanyKcode == null) {
			if (other.buyerCompanyKcode != null)
				return false;
		} else if (!buyerCompanyKcode.equals(other.buyerCompanyKcode))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (sellerCompanyKcode == null) {
			if (other.sellerCompanyKcode != null)
				return false;
		} else if (!sellerCompanyKcode.equals(other.sellerCompanyKcode))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		return true;
	}
}
