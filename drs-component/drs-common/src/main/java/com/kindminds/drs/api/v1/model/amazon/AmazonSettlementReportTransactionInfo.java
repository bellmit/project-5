package com.kindminds.drs.api.v1.model.amazon;

import java.math.BigDecimal;
import java.util.Date;

public interface AmazonSettlementReportTransactionInfo {
	String getCurrency();
	String getAmountType();
	String getAmountDescription();
	BigDecimal getAmount();
	Integer getQuantityPurchased();
	Date getPostedDate();
}
