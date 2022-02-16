package com.kindminds.drs.api.usecase;

import java.util.List;
import java.util.Map;

import com.kindminds.drs.api.v1.model.SupplementalLink;

public interface MaintainSupplementalLinkUco {
	public List<SupplementalLink> getList();
	public List<SupplementalLink> getList(String supplierKcode);
	public int save(SupplementalLink link);
	public SupplementalLink get(int id);
	public SupplementalLink update(SupplementalLink link);
	public void delete(List<Integer> ids);
	public List<String> getSupplierKcodeList();
	public Map<String,String> getSupplierKcodeToNameMap();
}
