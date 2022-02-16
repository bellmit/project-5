package com.kindminds.drs.api.usecase.accounting;

import java.util.Date;
import java.util.Map;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.accounting.DomesticTransaction;
import com.kindminds.drs.api.v1.model.common.DtoList;

public interface MaintainDomesticTransactionUco {
	DtoList<DomesticTransaction> getList(int pageIndex);
	Map<String,String> getSsdcKcodeNames();
	Map<String,String> getSplrKcodeNames(String ssdcKcode);
	Currency getCurrency(String ssdcKcode);
	Map<Integer,String> getLineItemTypeKeyName();
	DomesticTransaction get(Integer id);
	Integer create(DomesticTransaction transaction);
	Integer update(DomesticTransaction transaction);
	void delete(int id);
	String getEarliestAvailableUtcDate();
	Integer createForSettlementTest(DomesticTransaction transaction);
	String getDefaultSalesTaxPercentage();
	Boolean isAllEditable(Date transactionDate);
}
