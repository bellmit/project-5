package com.kindminds.drs.api.data.access.usecase.accounting;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.kindminds.drs.api.v1.model.accounting.DomesticTransaction;

public interface MaintainDomesticTransactionDao {

	Object[] query(int id);
	List<Object[]> queryLineItems(int transactionId);

	Integer insert(DomesticTransaction t,Date date,BigDecimal taxRate,int currencyId,BigDecimal subtotal,BigDecimal tax,BigDecimal total);
	Integer update(DomesticTransaction t,Date date,BigDecimal taxRate,int currencyId,BigDecimal subtotal,BigDecimal tax,BigDecimal total);
	Integer updateInvoiceNumber(DomesticTransaction t);

	int queryCurrencyId(String companyKcode);
	Map<Integer, String> queryLineItemTypeKeyName(String typeClassName);
	int queryTotalCounts();
	List<Object[]> queryList(int startRowNum, int pageSize);
	void delete(int id);
	Date queryLastSettlementEnd();
	Boolean queryBillStatementExists(Date transactionDate);
}
