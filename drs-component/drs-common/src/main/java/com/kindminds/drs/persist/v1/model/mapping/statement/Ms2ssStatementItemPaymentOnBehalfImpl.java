package com.kindminds.drs.persist.v1.model.mapping.statement;

import java.math.BigDecimal;





import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.report.Ms2ssStatement.Ms2ssStatementItemPaymentOnBehalf;


public class Ms2ssStatementItemPaymentOnBehalfImpl implements Ms2ssStatementItemPaymentOnBehalf {
	
	//@Id ////@Column(name="id")
	private int id;
	//@Column(name="display_name")
	private String displayName;
	//@Column(name="amount")
	private BigDecimal amount;

	public Ms2ssStatementItemPaymentOnBehalfImpl() {
	}

	public Ms2ssStatementItemPaymentOnBehalfImpl(int id, String displayName, BigDecimal amount) {
		this.id = id;
		this.displayName = displayName;
		this.amount = amount;
	}

	@Override
	public String getDisplayName() {
		return this.displayName;
	}

	@Override
	public String getAmountStr() {
		return this.amount.setScale(Currency.USD.getScale()).toString();
	}

	@Override
	public String getUrlPath() {
		return "PaymentOnBehalf";
	}

	@Override
	public BigDecimal getAmount() {
		return this.amount;
	}

}
