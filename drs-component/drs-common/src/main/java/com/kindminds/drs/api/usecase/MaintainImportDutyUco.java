package com.kindminds.drs.api.usecase;

import java.util.List;

import com.kindminds.drs.api.v1.model.accounting.ImportDutyTransaction;
import com.kindminds.drs.api.v1.model.accounting.ImportDutyTransaction.ImportDutyTransactionLineItem;
import com.kindminds.drs.api.v1.model.common.DtoList;

public interface MaintainImportDutyUco {
	// CRUD
	public DtoList<ImportDutyTransaction> getList(int pageIndex);
	public ImportDutyTransaction get(String unsName);
	public String create(ImportDutyTransaction duty);
	public String update(ImportDutyTransaction duty);
	public void delete(String unsName);
	// OPTION SOURCES FOR CREATE
	public List<String> getUnsNameList();
	public String getCountry(String unsName);
	public String getCurrency(String countryName);
	public List<ImportDutyTransactionLineItem> getLineItemInfoForCreate(String unsName);
}
