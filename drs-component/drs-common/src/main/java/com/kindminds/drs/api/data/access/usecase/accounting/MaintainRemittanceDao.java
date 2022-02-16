package com.kindminds.drs.api.data.access.usecase.accounting;

import java.util.Date;
import java.util.List;

import com.kindminds.drs.api.v1.model.accounting.Remittance;
import com.kindminds.drs.api.v1.model.accounting.RemittanceSearchCondition;
import com.kindminds.drs.api.v1.model.report.RemittanceImportItem;

public interface MaintainRemittanceDao {
	int getCount(RemittanceSearchCondition condition);
	List<Remittance> queryList(RemittanceSearchCondition condition, int startIndex, int size);
	Remittance query(int id);
	int insert(Remittance remittance,Date dateRcvd,Date dateSend);
	Integer insertFromCSV(List<RemittanceImportItem> records);
	Integer autoInsertFromStatement(Date date);
	int update(Remittance remittance,Date dateRcvd,Date dateSent);
	boolean delete(int id);
	Integer deleteByDate(Date date);
	Date queryLatestSettlementEnd(String isurKcode,String rcvrKcode);
	Date queryLastSettlementEnd();
}
