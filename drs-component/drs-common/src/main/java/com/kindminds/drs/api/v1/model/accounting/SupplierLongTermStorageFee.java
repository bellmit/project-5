package com.kindminds.drs.api.v1.model.accounting;

import java.math.BigDecimal;

public interface SupplierLongTermStorageFee {
	String getKcode();
	String getMarketplace();
	BigDecimal getSixMonthStorageFee();
	BigDecimal getOneYearStorageFee();
	String getCurrency();
}
