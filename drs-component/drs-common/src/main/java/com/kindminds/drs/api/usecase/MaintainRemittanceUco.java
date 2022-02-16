package com.kindminds.drs.api.usecase;

import java.util.List;
import java.util.Map;

import com.kindminds.drs.api.v1.model.accounting.Remittance;
import com.kindminds.drs.api.v1.model.accounting.RemittanceSearchCondition;
import com.kindminds.drs.api.v1.model.common.DtoList;

public interface MaintainRemittanceUco {
	public List<String> getKcodeList();
	public Map<String,String> getKcodeToNameMap();
	public DtoList<Remittance> retrieveList(RemittanceSearchCondition condition, int pageIndex);
	public Remittance retrieve(int id);	
	public int create(Remittance remmitance);
	public int update(Remittance remmitance);
	public boolean delete(int id);
	String getEarliestAvailableUtcDate();
	String importRemittanceData(byte[] fileBytes);
}
