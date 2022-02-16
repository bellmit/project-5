package com.kindminds.drs.persist.v1.model.mapping.statement;

import java.math.BigDecimal;





import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.report.Ms2ssStatement.Ms2ssStatementItemProductInventoryReturn;


public class Ms2ssStatementItemProductInventoryReturnImpl implements Ms2ssStatementItemProductInventoryReturn {
	
	//@Id //@Column(name="display_name")
	private String displayName;
	//@Column(name="amount")
	private BigDecimal amount;
	//@Column(name="currency_id")
	private Integer currencyId;

	public Ms2ssStatementItemProductInventoryReturnImpl() {
	}

	public Ms2ssStatementItemProductInventoryReturnImpl(String displayName, BigDecimal amount, Integer currencyId) {
		this.displayName = displayName;
		this.amount = amount;
		this.currencyId = currencyId;
	}

	@Override
	public String getDisplayName() {
		return this.displayName;
	}

	@Override
	public String getAmountStr() {
		Currency currency = Currency.fromKey(this.currencyId); 
		return this.amount.setScale(currency.getScale()).toPlainString();
	}

	@Override
	public BigDecimal getAmount() {
		return this.amount;
	}

}
