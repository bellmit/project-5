package com.kindminds.drs.api.data.access.usecase;

import java.util.List;

import com.kindminds.drs.api.v1.model.Company;

public interface MaintainCompanyDao {

	public int getSupplierCount();
	public String insertSupplier(Company comToBeSaved);
	public String updateInfo(Company comToBeSaved);
	public String updatePartial(Company company);
	public String delete(String kcode);
	public Company query(String kcode);
	public List<Company> querySupplierList(int startRowNum, int pageSize);
	public Boolean isCouponUnique(String coupon,String kcode);
	// UTILITY
	public List<String> querySupplierUserEmailList(String kcode);
	

}
