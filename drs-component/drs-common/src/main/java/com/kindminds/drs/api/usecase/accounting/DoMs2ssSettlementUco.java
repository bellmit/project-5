package com.kindminds.drs.api.usecase.accounting;

import java.util.Date;
import java.util.Map;


import com.kindminds.drs.api.v1.model.close.BillStatement;
import com.kindminds.drs.enums.BillStatementType;


public interface DoMs2ssSettlementUco {
	public Map<String,String> getSupplierKcodeToCompanyNameMap();
	public int createDraft(String isurKcode,String rcvrKcode, String expectDateStart, String expectDateEnd);
	public int commitDraft(String statementName);
	public void deleteDraft(String statementName);
	public void deleteOfficial(String statementName);
	public int doStatementOfficial(String isurName,String rcvrName, Date expectDateStart, Date expectDateEnd);
	public int doStatementForTest(String isurKcode,String rcvrKcode, Date expectDateStart, Date expectDateEnd);
	public String doSettlement();
	public int doSettlement(BillStatementType type, String isurKcode, String rcvrKcode, Date start, Date end);
	public BillStatement getExistingBillStatement(int id);
	public BillStatement getExistingDraftBillStatement(int id);
	public void confirmAllDraft();
}
