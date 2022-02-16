package com.kindminds.drs.persist.v1.model.mapping.statement;

import java.io.Serializable;
import java.math.BigDecimal;

public class Ss2spStatementItemPaymentAndRefundId implements Serializable {

	private static final long serialVersionUID = 3878382216228435260L;
	private String shipmentName;
	private BigDecimal amount;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result
				+ ((shipmentName == null) ? 0 : shipmentName.hashCode());
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
		Ss2spStatementItemPaymentAndRefundId other = (Ss2spStatementItemPaymentAndRefundId) obj;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (shipmentName == null) {
			if (other.shipmentName != null)
				return false;
		} else if (!shipmentName.equals(other.shipmentName))
			return false;
		return true;
	}
	
}
