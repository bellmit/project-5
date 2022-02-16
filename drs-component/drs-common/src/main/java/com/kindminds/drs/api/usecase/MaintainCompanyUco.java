package com.kindminds.drs.api.usecase;

import java.util.List;

import com.kindminds.drs.api.v1.model.Company;
import com.kindminds.drs.api.v1.model.common.DtoList;

public interface MaintainCompanyUco {
	public String createSupplier(Company supplier);
	public String update(Company company);
	public String updatePartial(Company company);
	public String delete(String kcode);
	public Company getCompany(String kcode);
	public DtoList<Company> retrieveSupplierList(int pageIndex);
	public Boolean isCouponUnique(String coupon,String kcode);
	// UTILITY
	public List<String> getSupplierUserEmailList(String kcode);
}
