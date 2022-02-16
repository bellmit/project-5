package com.kindminds.drs.persist.v1.model.mapping.close;

import java.math.BigDecimal;





import org.springframework.util.Assert;

import com.kindminds.drs.Country;
import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.close.BillStatementProfitShareItem;


public class BillStatementProfitShareItemImpl implements BillStatementProfitShareItem {
	
	//@Id ////@Column(name="id")
	private int id;
	//@Column(name="statement_id")
	private Integer statementId;
	//@Column(name="line_seq")
	private Integer lineSeq;
	//@Column(name="source_country_id")
	private Integer sourceCountryId;
	//@Column(name="source_currency_id")
	private Integer sourceCurrencyId;
	//@Column(name="source_amount_untaxed")
	private BigDecimal sourceAmountUntaxed;
	//@Column(name="statement_currency_id")
	private Integer statementCurrencyId;
	//@Column(name="statement_amount_untaxed")
	private BigDecimal statementAmountUntaxed;
	//@Column(name="exchange_rate")
	private BigDecimal exchangeRate;

	public BillStatementProfitShareItemImpl() {
	}

	public BillStatementProfitShareItemImpl(int id, Integer statementId, Integer lineSeq, Integer sourceCountryId, Integer sourceCurrencyId, BigDecimal sourceAmountUntaxed, Integer statementCurrencyId, BigDecimal statementAmountUntaxed, BigDecimal exchangeRate) {
		this.id = id;
		this.statementId = statementId;
		this.lineSeq = lineSeq;
		this.sourceCountryId = sourceCountryId;
		this.sourceCurrencyId = sourceCurrencyId;
		this.sourceAmountUntaxed = sourceAmountUntaxed;
		this.statementCurrencyId = statementCurrencyId;
		this.statementAmountUntaxed = statementAmountUntaxed;
		this.exchangeRate = exchangeRate;
	}

	@Override
	public String toString() {
		return "BillStatementProfitShareItemImpl [getStatementId()=" + getStatementId() + ", getLineSeq()="
				+ getLineSeq() + ", getSourceCountry()=" + getSourceCountry() + ", getSourceCurrency()="
				+ getSourceCurrency() + ", getSourceAmountUntaxed()=" + getSourceAmountUntaxed()
				+ ", getDestinationCurrency()=" + getDestinationCurrency() + ", getDestinationAmountUntaxed()="
				+ getDestinationAmountUntaxed() + ", getExchangeRate()=" + getExchangeRate() + "]";
	}

	@Override
	public Integer getStatementId() {
		return this.statementId;
	}

	@Override
	public Integer getLineSeq() {
		return this.lineSeq;
	}

	@Override
	public String getSourceCountry() {
		Country ct = Country.fromKey(this.sourceCountryId);
		Assert.notNull(ct);
		return ct.name();
	}

	@Override
	public String getSourceCurrency() {
		Currency cur = Currency.fromKey(this.sourceCurrencyId);
		Assert.notNull(cur);
		return cur.name();
	}

	@Override
	public String getSourceAmountUntaxed() {
		return this.sourceAmountUntaxed.toPlainString();
	}

	@Override
	public String getDestinationCurrency() {
		Currency cur = Currency.fromKey(this.statementCurrencyId);
		Assert.notNull(cur);
		return cur.name();
	}

	@Override
	public String getDestinationAmountUntaxed() {
		return this.statementAmountUntaxed.toPlainString();
	}

	@Override
	public String getExchangeRate() {
		return this.exchangeRate.toPlainString();
	}

}
