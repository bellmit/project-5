package com.kindminds.drs.api.data.access.usecase;

import java.util.List;

import com.kindminds.drs.api.v1.model.SupplementalLink;

public interface MaintainSupplementalLinkDao {
	public List<SupplementalLink> queryList();
	public List<SupplementalLink> queryList(String companyKcode);
	public int insert(SupplementalLink link);
	public SupplementalLink query(int id);
	public SupplementalLink update(SupplementalLink link);
	public void delete(List<Integer> ids);
}
