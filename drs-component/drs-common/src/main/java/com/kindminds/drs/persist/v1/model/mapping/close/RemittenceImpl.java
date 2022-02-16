package com.kindminds.drs.persist.v1.model.mapping.close;

import java.math.BigDecimal;









import org.springframework.util.StringUtils;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.close.Remittance;

 //@IdClass(RemittenceId.class)
/*
@SqlResultSetMappings({
	 @SqlResultSetMapping(name="remittence",
	   entities={
	    Result(entityClass=RemittenceImpl.class)} ,
	    columns={//@ColumnResult(name="type") , //@ColumnResult(name="currency") ,
			 //@ColumnResult(name="amount")
	 })
})

 */
public class RemittenceImpl implements Remittance {
	
	//@Id
	//@Column(name="type")
	private String type;
	
	//@Id
	//@Column(name="currency")
	private String currency;
	
	//@Column(name="amount")
	private BigDecimal amount;

	private BigDecimal statementCurrencyAmount;

	public RemittenceImpl(){}

	 public RemittenceImpl(String type, String currency, BigDecimal amount) {
		 this.type = type;
		 this.currency = currency;
		 this.amount = amount;

	 }

	 public RemittenceImpl(String type, String currency, BigDecimal amount, BigDecimal statementCurrencyAmount) {
		 this.type = type;
		 this.currency = currency;
		 this.amount = amount;
		 this.statementCurrencyAmount = statementCurrencyAmount;
	 }


	 @Override
	public RemittenceType getType() {
		if (!StringUtils.hasText(type))
			return null;
		return RemittenceType.valueOf(this.type);
	}

	@Override
	public Currency getCurrency() {
		return Currency.valueOf(this.currency);
	}

	@Override
	public BigDecimal getAmount() {
		if (this.amount == null)
			return BigDecimal.ZERO;
		return this.amount;
	}

	@Override
	public BigDecimal getStatementCurrencyAmount() {
		if (this.statementCurrencyAmount == null)
			return BigDecimal.ZERO;
		return statementCurrencyAmount;
	}

	@Override
	public void setStatementCurrencyAmount(BigDecimal statementCurrencyAmount) {
		this.statementCurrencyAmount = statementCurrencyAmount;
	}

}
