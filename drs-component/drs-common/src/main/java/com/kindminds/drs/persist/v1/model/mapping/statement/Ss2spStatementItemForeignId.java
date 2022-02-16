package com.kindminds.drs.persist.v1.model.mapping.statement;

import java.io.Serializable;
import java.math.BigDecimal;

public class Ss2spStatementItemForeignId implements Serializable {

	private static final long serialVersionUID = -1088226324905112711L;

	private String name;
	private BigDecimal amount;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Ss2spStatementItemForeignId other = (Ss2spStatementItemForeignId) obj;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
	
	
}
