package com.kindminds.drs.api.data.access.usecase.accounting;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.kindminds.drs.api.v1.model.accounting.InternationalTransaction;

public interface MaintainInternationalTransactionDao {
	Map<String,String> queryMsdcKcodeNameMap(String drsUserCompanyKcode);
	Map<String,String> querySsdcKcodeNameMap(String drsUserCompanyKcode);
	Map<Integer,String> queryLineItemKeyNameMap(int cashFlowDirectionKey);
	int queryTotalCounts();
	List<Object[]> queryList(int startIndex,int size);
	Object[] query(int id);
	List<Object []> queryLineItems(int transactionId);
	Integer insert(InternationalTransaction t,Date date,BigDecimal total);
	Integer update(InternationalTransaction t,Date date,BigDecimal total);
	void delete(int id);
	Date queryLastSettlementEnd();
	int queryTotalCountsMonthlyStorageFee(String year,String month);
	int countCouponTransactionsProcessed();
}
