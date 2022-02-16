package com.kindminds.drs.api.usecase.accounting;

import java.util.List;
import java.util.Map;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.accounting.InternationalTransaction;
import com.kindminds.drs.api.v1.model.accounting.InternationalTransaction.CashFlowDirection;
import com.kindminds.drs.api.v1.model.common.DtoList;

public interface MaintainInternationalTransactionUco {
	List<CashFlowDirection> getCashFlowDirections();
	Map<String,String> getMsdcKcodeNameMap();
	Map<String,String> getSsdcKcodeNameMap();
	Map<String,String> getSplrKcodeNameMap(String ssdcKcode);
	Currency getCurrency(String msdcKcode);
	Map<Integer,String> getLineItemKeyNameMap(int cashFlowDirectionKey);
	DtoList<InternationalTransaction> getList(int pageIndex);
	InternationalTransaction get(Integer id);
	List<InternationalTransaction> get(List<Integer> id);
	Integer create(InternationalTransaction transaction);
	Integer update(InternationalTransaction transaction);
	void delete(Integer id);
	String getEarliestAvailableUtcDate();
	Integer createForSettlementTest(InternationalTransaction t);
	int countCouponTransactionsProcessed();
}
