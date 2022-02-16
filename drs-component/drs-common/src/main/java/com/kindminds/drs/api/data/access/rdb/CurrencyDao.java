package com.kindminds.drs.api.data.access.rdb;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;

import com.kindminds.drs.Currency;

public interface CurrencyDao {
	public Currency queryCompanyCurrency(String kcode);
	public List<Currency> getAllCurrencies();
	public void insertExchangeRate(Currency src, Currency dst, OffsetDateTime
			effectiveFrom, BigDecimal rate, BigDecimal interbankRate);
	public BigDecimal getExchangeRate(Currency srcCurrency, Currency dstCUrrency, Date date);
	public BigDecimal queryExchangeRate(Date start,Date end,Currency srcCur,Currency dstCur,BigDecimal interbankRate);
}
