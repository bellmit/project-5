package com.kindminds.drs.v1.model.impl.statement;

import com.kindminds.drs.api.v1.model.report.Ms2ssStatement.Ms2ssStatementItemMsdcPaymentOnBehalf;

import java.math.BigDecimal;

public class Ms2ssStatementItemMsdcPaymentOnBehalfImpl implements Ms2ssStatementItemMsdcPaymentOnBehalf {
	
	private String name;
	private BigDecimal amount;
	
	public Ms2ssStatementItemMsdcPaymentOnBehalfImpl(
			String name,
			BigDecimal amount) {
		this.name=name;
		this.amount=amount;
	}
	
	public BigDecimal getOrigAmount(){
		return this.amount;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getAmount() {
		return this.amount.stripTrailingZeros().toPlainString();
	}

}
