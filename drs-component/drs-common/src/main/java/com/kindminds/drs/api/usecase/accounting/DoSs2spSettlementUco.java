package com.kindminds.drs.api.usecase.accounting;

import java.util.Date;
import java.util.List;
import java.util.Map;


import com.kindminds.drs.api.v1.model.close.BillStatement;
import com.kindminds.drs.api.v1.model.close.BillStatementLineItemCustomerCaseExemptionInfo;
import com.kindminds.drs.api.v1.model.close.BillStatementProfitShareItem;
import com.kindminds.drs.enums.BillStatementType;

public interface DoSs2spSettlementUco {
	Map<String,String> getSupplierKcodeToCompanyNameMap();
	void deleteOfficial(String statementName);
	void deleteAllDraft();
	void deleteDraft(String statementName);
	void commitAllDraft();
	int confirmDraft(String statementName);
	String createDraft(String supplierKcode);
	String createDraft(String supplierKcode,String utcDateStart,String utcDateEnd);
	String createDraftForAllSupplier();
	String createDraftForAllSupplier(String utcDateStart,String utcDateEnd);
	int doSettlementForOfficial(String supplierKcode , Date dateStart,Date dateEnd);
	int doSettlementForDraft(String supplierKcode,String utcDateStart,String utcDateEnd);
	int doSettlement(String supplierKcode, Date start, Date end, BillStatementType bsType);
	BillStatement getStatement(BillStatementType type,int id);
	List<BillStatementProfitShareItem> getProfitShareItems(BillStatementType bsType, int statementId);
	List<BillStatementLineItemCustomerCaseExemptionInfo> getCustomerCaseInfoList(BillStatementType bsType, int statementId);
}
