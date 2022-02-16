package com.kindminds.drs.service.usecase.product;

import com.kindminds.drs.Context;
import com.kindminds.drs.api.usecase.product.MaintainProductSkuUco;
import com.kindminds.drs.api.v1.model.product.BaseProduct;
import com.kindminds.drs.api.v1.model.product.SKU;
import com.kindminds.drs.api.v1.model.product.SKU.Status;
import com.kindminds.drs.api.data.access.rdb.CompanyDao;
import com.kindminds.drs.api.data.access.usecase.product.MaintainProductSkuDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MaintainProductSkuUcoImpl implements MaintainProductSkuUco {

	@Autowired private MaintainProductSkuDao dao;
	@Autowired private CompanyDao CompanyDao;

	private boolean checkProductSkuAccessable(String skuCodeByDrs){
		String userCompanyKcode = Context.getCurrentUser().getCompanyKcode();
		if(Context.getCurrentUser().isDrsUser()) return true;
		String supplierKcodeOfBase = this.dao.querySupplierKcode(skuCodeByDrs);
		if(userCompanyKcode.equals(supplierKcodeOfBase)) return true;
		return false;
	}

	@Override
	public SKU get(String skuCodeByDrs) {
		Assert.isTrue(this.checkProductSkuAccessable(skuCodeByDrs));
		return this.dao.query(skuCodeByDrs);
	}

	@Override
	public SKU getProductSku(String skuCodeByDrs) {
		return this.dao.query(skuCodeByDrs);
	}

	@Override
	public String insert(SKU sku) {
		String drsSkuCode = this.generateDrsSkuCode(sku.getSupplierKcode(), sku.getCodeBySupplier());
		return this.dao.insert(sku,drsSkuCode);
	}

	@Override
	public String insert(SKU sku, String drsSkuCode) {
		return this.dao.insert(sku,drsSkuCode);
	}



	private String generateDrsSkuCode(String companyKcode,String codeBySupplier){
		if(companyKcode.equals("K101")) return codeBySupplier;
		return companyKcode.trim()+"-"+codeBySupplier.trim();
	}

	@Override
	public String update(SKU sku) {
		Assert.isTrue(this.checkProductSkuAccessable(sku.getCodeByDrs()));
		String origDrsCode = sku.getCodeByDrs();
		String newDrsCode = this.generateDrsSkuCode(sku.getSupplierKcode(), sku.getCodeBySupplier());
		return this.dao.update(origDrsCode,sku,newDrsCode);
	}

	@Override
	public String updateSku(SKU sku) {
		String drsSkuCode = sku.getCodeByDrs();
		return this.dao.updateSku(drsSkuCode,sku);
	}

	@Override
	public String updateSkuMltAndContainLithium(SKU sku) {
		Assert.isTrue(this.checkProductSkuAccessable(sku.getCodeByDrs()));
		return this.dao.updateSkuMltAndContainLithium(sku);
	}

	@Override
	public String delete(String skuCodeByDrs) {
		if(this.dao.query(skuCodeByDrs)==null) return null;
		Assert.isTrue(this.checkProductSkuAccessable(skuCodeByDrs));
		return this.dao.delete(skuCodeByDrs);
	}

	@Override
	public List<String> getBaseCodeList(String supplierKcode) {
		return this.dao.queryBaseCodeList(supplierKcode);
	}

	@Override
	public List<String> getSkuStatusList() {
		ArrayList<String> list = new ArrayList<String>();
		for(Status rs:Status.values()){
			list.add(rs.name());
		}
		return list;
	}

	@Override
	public Map<String,String> getSupplierKcodeToNameMap() {
		Map<String,String> result=this.CompanyDao.querySupplierKcodeToShortEnUsNameWithRetailMap();
		return result;
	}

	@Override
	public boolean isSkuExist(String supplierKcode, String skuCodeBySupplier) {
		return this.dao.isProductSkuSupplierCodeExist(supplierKcode, skuCodeBySupplier);
	}

}