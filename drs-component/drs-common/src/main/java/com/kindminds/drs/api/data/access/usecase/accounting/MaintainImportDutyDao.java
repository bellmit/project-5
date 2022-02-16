package com.kindminds.drs.api.data.access.usecase.accounting;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.kindminds.drs.api.v1.model.accounting.ImportDutyTransaction;
import com.kindminds.drs.api.v1.model.accounting.ImportDutyTransaction.ImportDutyTransactionLineItem;

public interface MaintainImportDutyDao {
	public int queryTotalCount();
	public String insert(ImportDutyTransaction t,Date transactionDate,BigDecimal totalAmount);
	public String update(ImportDutyTransaction t,Date transactionDate,BigDecimal totalAmount);
	public void delete(String unsName);
	public ImportDutyTransaction query(String unsName);
	public List<String> queryUnsNameList();
	public String queryCountry(String unsName);
	public List<ImportDutyTransaction> queryList(int startIndex,int size);
	public List<ImportDutyTransactionLineItem> queryLineItemInfoForCreate(String unsName);
}
