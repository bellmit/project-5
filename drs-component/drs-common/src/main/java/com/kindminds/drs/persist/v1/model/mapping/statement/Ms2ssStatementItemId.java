package com.kindminds.drs.persist.v1.model.mapping.statement;

import java.io.Serializable;

public class Ms2ssStatementItemId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5329654445935682409L;
	private String type;
	private String shipmentName;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((shipmentName == null) ? 0 : shipmentName.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		Ms2ssStatementItemId other = (Ms2ssStatementItemId) obj;
		if (shipmentName == null) {
			if (other.shipmentName != null)
				return false;
		} else if (!shipmentName.equals(other.shipmentName))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
}
