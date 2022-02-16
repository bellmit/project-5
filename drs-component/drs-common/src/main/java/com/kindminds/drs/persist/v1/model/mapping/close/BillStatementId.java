package com.kindminds.drs.persist.v1.model.mapping.close;

import java.io.Serializable;

public class BillStatementId implements Serializable {

	private static final long serialVersionUID = -2479369624375923520L;
	private String id;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		BillStatementId other = (BillStatementId) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
