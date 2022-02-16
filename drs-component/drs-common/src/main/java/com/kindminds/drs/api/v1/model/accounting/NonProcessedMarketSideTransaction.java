package com.kindminds.drs.api.v1.model.accounting;

import java.util.Date;

public interface NonProcessedMarketSideTransaction  {

	Integer getId();
	Date getTransactionDate();
	String getType();
	String getSource();
	String getSourceId();
	String getSku();
	String getDescription();
	Integer getAssignedSourceOrderSeq();

	String getExceptionMessage();
	String getStackTracke();

}
