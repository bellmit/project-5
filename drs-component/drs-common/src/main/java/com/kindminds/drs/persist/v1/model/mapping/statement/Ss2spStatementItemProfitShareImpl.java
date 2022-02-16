package com.kindminds.drs.persist.v1.model.mapping.statement;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.util.Assert;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.report.Ss2spStatementReport.Ss2spStatementItemProfitShare;

 //@IdClass(Ss2spStatementItemForeignId.class)
public class Ss2spStatementItemProfitShareImpl implements Ss2spStatementItemProfitShare {

	//@Id //@Column(name="name")
	private String name;
	//@Id //@Column(name="amount")
	private BigDecimal amount;
	//@Column(name="currency_name")
	private String currencyName;

	 public Ss2spStatementItemProfitShareImpl() {
	 }

	 public Ss2spStatementItemProfitShareImpl(String name, BigDecimal amount, String currencyName) {
		 this.name = name;
		 this.amount = amount;
		 this.currencyName = currencyName;
	 }

	 @Override
	public String toString() {
		return "Ss2spStatementItemProfitShareImpl [getDisplayName()=" + getDisplayName() + ", getAmountUntaxStr()="
				+ getAmountUntaxStr() + ", getAmountTaxStr()=" + getAmountTaxStr() + ", getAmountStr()="
				+ getAmountStr() + ", getCurrency()=" + getCurrency() + ", getAmount()=" + getAmount() + "]";
	}
	@Override
	public String getDisplayName() {
		return this.name;
	}
	@Override
	public String getAmountUntaxStr() {
		BigDecimal amountTax = new BigDecimal(this.getAmountTaxStr());
		return this.getAmount().subtract(amountTax).setScale(this.getCurrency().getScale()).toPlainString();
	}
	@Override
	public String getAmountTaxStr() {
		BigDecimal taxRate = new BigDecimal("0.05");
		BigDecimal origAmountUntax = this.getAmount().divide(BigDecimal.ONE.add(taxRate), 10, RoundingMode.HALF_UP);
		return origAmountUntax.multiply(taxRate).setScale(this.getCurrency().getScale(),RoundingMode.HALF_UP).toPlainString(); 
	}
	
	@Override
	public String getAmountStr() {
		return this.amount.setScale(this.getCurrency().getScale()).toString();	
	}
	@Override
	public Currency getCurrency() {
		Currency currency = Currency.valueOf(this.currencyName);
		Assert.notNull(currency);
		return currency;
	}
	@Override
	public BigDecimal getAmount() {
		return this.amount;
	}

}
