package com.kindminds.drs.service.usecase;

import com.kindminds.drs.api.usecase.MaintainCompanyUco;

import com.kindminds.drs.api.v1.model.Company;
import com.kindminds.drs.api.v1.model.common.DtoList;
import com.kindminds.drs.api.v1.model.common.Pager;
import com.kindminds.drs.api.data.access.rdb.CompanyDao;
import com.kindminds.drs.api.data.access.usecase.MaintainCompanyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service("maintainCompanyUco")
public class MaintainCompanyUcoImpl implements MaintainCompanyUco {
	
	@Autowired private MaintainCompanyDao dao;
	@Autowired private CompanyDao companyRepo;

	@Override
	public String createSupplier(Company supplier) {
		return this.dao.insertSupplier(supplier);
	}
	
	@Override
	public String update(Company comToBeSaved) {
		return dao.updateInfo(comToBeSaved);
	}
	
	@Override
	public String updatePartial(Company company) {
		return this.dao.updatePartial(company);
	}

	@Override
	public String delete(String kcode) {
		return this.dao.delete(kcode);
	}
	
	@Override
	public Company getCompany(String kcode) {
		return dao.query(kcode);
	}
	
	@Override
	public DtoList<Company> retrieveSupplierList(int pageIndex) {
		DtoList<Company> list = new DtoList<Company>();
		list.setTotalRecords(dao.getSupplierCount());
		Pager pager = new Pager(pageIndex, list.getTotalRecords(),10);
		List<Company> items = this.dao.querySupplierList(pager.getStartRowNum(), pager.getPageSize());
		list.setPager(pager);
		list.setItems(items);
		return list;
	}
	
	@Override
	public List<String> getSupplierUserEmailList(String kcode) {
		Assert.isTrue(this.companyRepo.isSupplier(kcode));
		return this.dao.querySupplierUserEmailList(kcode);
	}

	@Override
	public Boolean isCouponUnique(String coupon,String kcode) {
		return this.dao.isCouponUnique(coupon,kcode);
	}
 
}