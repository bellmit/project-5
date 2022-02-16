package com.kindminds.drs.v1.model.impl.statement;

import java.math.BigDecimal;
import org.springframework.util.Assert;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.accounting.StatementLineType;
import com.kindminds.drs.api.v1.model.report.Ss2spStatementReport.Ss2spStatementItemProfitShare;


public class Ss2spStatementItemProfitShareImplV3 implements Ss2spStatementItemProfitShare {

	private Currency currency=null;
	private BigDecimal amountUntaxed=null;
	private BigDecimal amountTax=null;
	
	public void setCurrency(Currency value) {this.currency=value;}
	public void setAmountUntaxed(BigDecimal value) {this.amountUntaxed=value;}
	public void setAmountTax(BigDecimal value) {this.amountTax=value;}
	
	@Override
	public String toString() {
		return "Ss2spStatementItemProfitShareImplV3 [getDisplayName()=" + getDisplayName() + ", getAmountUntaxStr()="
				+ getAmountUntaxStr() + ", getAmountTaxStr()=" + getAmountTaxStr() + ", getAmountStr()="
				+ getAmountStr() + ", getCurrency()=" + getCurrency() + ", getAmount()=" + getAmount() + "]";
	}
	@Override
	public String getDisplayName() {
		return StatementLineType.SS_PRODUCT_PROFIT_SHARE_FOR_SP.getValue();
	}
	
	@Override
	public String getAmountUntaxStr() {
		return this.amountUntaxed.setScale(this.currency.getScale()).toPlainString();
	}
	@Override
	public String getAmountTaxStr() {
		return this.amountTax.setScale(this.currency.getScale()).toPlainString(); 
	}
	
	@Override
	public String getAmountStr() {
		return this.amountUntaxed.add(this.amountTax).setScale(this.currency.getScale()).toPlainString();
	}
	
	@Override
	public Currency getCurrency() {
		Assert.notNull(this.currency);
		return this.currency;
	}
	@Override
	public BigDecimal getAmount() {
		return this.amountUntaxed.add(this.amountTax);
	}

}
