package com.kindminds.drs.api.v1.model.accounting;

import com.kindminds.drs.Marketplace;

public interface AmazonSettlementReportInfo {
	String getSettlementId();
	String getDateStart();
	String getDateEnd();
	String getDateDeposit();
	String getTotalAmount();
	String getCurrency();
	Marketplace getSourceMarketplace(); 
}
