package com.kindminds.drs.api.v1.model.close;

import com.kindminds.drs.Country;
import com.kindminds.drs.Currency;

import java.math.BigDecimal;

public interface CustomerCaseMsdcMsgChargeInfo {
	int getId();
	Country getCountry();
	Currency getOriginCurrency();
	BigDecimal getChargeInOriginCurrency();
	Currency getActualCurrency();
	BigDecimal getChargeInActualCurrency();
}
