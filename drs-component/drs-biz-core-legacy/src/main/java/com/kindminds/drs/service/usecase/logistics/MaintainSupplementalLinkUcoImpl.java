package com.kindminds.drs.service.usecase.logistics;

import com.kindminds.drs.Context;
import com.kindminds.drs.api.usecase.MaintainSupplementalLinkUco;

import com.kindminds.drs.api.data.access.rdb.CompanyDao;
import com.kindminds.drs.api.data.access.usecase.MaintainSupplementalLinkDao;
import com.kindminds.drs.api.v1.model.SupplementalLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("maintainSupplementalLinkUco")
public class MaintainSupplementalLinkUcoImpl implements MaintainSupplementalLinkUco {
	
	@Autowired private MaintainSupplementalLinkDao dao;
	@Autowired private CompanyDao companyRepor;

	@Override
	public List<SupplementalLink> getList() {
		String userCompanyKcode = Context.getCurrentUser().getCompanyKcode();
		if(Context.getCurrentUser().isDrsUser()) return this.dao.queryList();
		return this.dao.queryList(userCompanyKcode);
	}
	
	@Override
	public List<SupplementalLink> getList(String supplierKcode) {
		return this.dao.queryList(supplierKcode);
	}

	@Override
	public int save(SupplementalLink link) {
		return this.dao.insert(link);
	}

	@Override
	public SupplementalLink get(int id) {
		return this.dao.query(id);
	}

	@Override
	public SupplementalLink update(SupplementalLink link) {
		return this.dao.update(link);
	}

	@Override
	public void delete(List<Integer> ids) {
		if(ids==null||ids.size()==0) return;
		this.dao.delete(ids);
	}

	@Override
	public List<String> getSupplierKcodeList() {
		return this.companyRepor.querySupplierKcodeList();
	}

	@Override
	public Map<String, String> getSupplierKcodeToNameMap() {
		return this.companyRepor.querySupplierKcodeToShortEnUsNameMap();
	}

}
