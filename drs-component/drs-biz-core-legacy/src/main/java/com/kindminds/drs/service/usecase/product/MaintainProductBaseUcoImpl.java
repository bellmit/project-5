package com.kindminds.drs.service.usecase.product;

import com.kindminds.drs.Context;
import com.kindminds.drs.api.usecase.product.MaintainProductBaseUco;
import com.kindminds.drs.api.v1.model.common.DtoList;
import com.kindminds.drs.api.v1.model.common.Pager;
import com.kindminds.drs.api.v1.model.product.BaseProduct;
import com.kindminds.drs.api.data.access.rdb.CompanyDao;
import com.kindminds.drs.api.data.access.usecase.product.MaintainProductBaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;


@Service
public class MaintainProductBaseUcoImpl implements MaintainProductBaseUco {

	@Autowired private MaintainProductBaseDao dao;
	@Autowired private CompanyDao CompanyDao;

	@Override
	public DtoList<BaseProduct> getList(int pageIndex) {
		String userCompanyKcode = Context.getCurrentUser().getCompanyKcode();
		Boolean isDrsCompany = Context.getCurrentUser().isDrsUser();
		String supplierKcode = isDrsCompany?null:userCompanyKcode;
		DtoList<BaseProduct> list = new DtoList<BaseProduct>();
		list.setTotalRecords(this.dao.queryCount(supplierKcode));
		list.setPager(new Pager(pageIndex,list.getTotalRecords()));
		list.setItems(this.dao.queryListWithSku(list.getPager().getStartRowNum(),list.getPager().getPageSize(),supplierKcode));
		return list;
	}

	@Override
	public BaseProduct get(String baseCodeByDrs) {
		Assert.isTrue(this.checkProductBaseAccessable(baseCodeByDrs));
		return this.dao.query(baseCodeByDrs);
	}

	@Override
	public BaseProduct getBaseProduct(String baseCodeByDrs) {
		return this.dao.query(baseCodeByDrs);
	}

	private boolean checkProductBaseAccessable(String baseCodeByDrs){
		String userCompanyKcode = Context.getCurrentUser().getCompanyKcode();
		if(Context.getCurrentUser().isDrsUser()) return true;
		String supplierKcodeOfBase = this.dao.querySupplierKcodeOfProductBase(baseCodeByDrs);
		if(userCompanyKcode.equals(supplierKcodeOfBase)) return true;
		return false;
	}

	@Override
	public String insert(BaseProduct base) {
		Assert.isTrue(!this.dao.isProductBaseSupplierCodeExist(base.getSupplierKcode(), base.getCodeBySupplier()));
		String drsBaseCode = this.generateDrsBaseCode(base.getSupplierKcode(), base.getCodeBySupplier());
		return this.dao.insert(base,drsBaseCode);
	}

	@Override
	public String insert(BaseProduct base, String drsBaseCode) {
		Assert.isTrue(!this.dao.isProductBaseSupplierCodeExist(base.getSupplierKcode(), base.getCodeBySupplier()));
		return this.dao.insert(base,drsBaseCode);
	}

	private String generateDrsBaseCode(String companyKcode,String supplierCode){
		StringBuilder codeSb=new StringBuilder()
				.append("BP-")
				.append(companyKcode)
				.append("-")
				.append(supplierCode);
		return codeSb.toString();
	}

	@Override
	public String update(BaseProduct base) {
		Assert.isTrue(this.checkProductBaseAccessable(base.getCodeByDrs()));
		String origDrsCode = base.getCodeByDrs();
		String newDrsCode = this.generateDrsBaseCode(base.getSupplierKcode(), base.getCodeBySupplier());
		return this.dao.update(origDrsCode,base,newDrsCode);
	}

	@Override
	public String updateBaseProduct(BaseProduct base) {
		String drsBaseCode = base.getCodeByDrs();
		return this.dao.updateBaseProduct(drsBaseCode,base);
	}

	@Override
	public String delete(String baseCodeByDrs) {
		int skuCount = this.dao.queryProductSkuCountInBase(baseCodeByDrs);
		Assert.isTrue(skuCount==0,"There are still sku(s) belong to this product base");
		Assert.isTrue(this.checkProductBaseAccessable(baseCodeByDrs));
		return this.dao.delete(baseCodeByDrs);
	}

	@Override
	public Map<String, String> getSupplierKcodeToNameMap() {
		Map<String,String> result = this.CompanyDao.querySupplierKcodeToShortEnUsNameWithRetailMap();
		return result;
	}

	@Override
	public boolean isBaseExist(String supplierKcode, String baseCodeBySupplier) {
		return this.dao.isProductBaseSupplierCodeExist(supplierKcode, baseCodeBySupplier);
	}

	@Override
	public List<String> getCategoryList() {
		return this.dao.queryCategoryList();
	}

}