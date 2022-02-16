package com.kindminds.drs.impl;

import com.kindminds.drs.api.v1.model.product.BaseProduct;
import com.kindminds.drs.api.v1.model.product.SKU;
import com.kindminds.drs.util.TestUtil;

import java.util.ArrayList;
import java.util.List;

public class ProductBaseImplForTest implements BaseProduct {
	private String supplierCompanyKcode;
	private String category;
	private String codeBySupplier;
	private String codeByDrs;
	private String nameBySupplier;
	private String internalNote;
	private List<SKU> skuList=null;
	
	public ProductBaseImplForTest(
			String supplierCompanyKcode,
			String category,
			String codeBySupplier, 
			String codeByDrs, 
			String nameBySupplier, 
			String internalNote, 
			List<SKU> skuList) {
		this.supplierCompanyKcode = supplierCompanyKcode;
		this.category = category;
		this.codeBySupplier = codeBySupplier;
		this.codeByDrs = codeByDrs;
		this.nameBySupplier = nameBySupplier;
		this.internalNote = internalNote;
		this.skuList = skuList;
	}
	
	public void addSku(SKU sku){
		if(this.skuList==null){
			this.skuList=new ArrayList<SKU>();
		}
		this.skuList.add(sku);
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public void setCodeBySupplier(String codeBySupplier) {
		this.codeBySupplier = codeBySupplier;
	}
	
	public void setCodeByDrs(String codeByDrs) {
		this.codeByDrs = codeByDrs;
	}
	
	public void setNameBySupplier(String nameBySupplier) {
		this.nameBySupplier = nameBySupplier;
	}
	
	@Override
	public boolean equals(Object obj){
		if ( obj instanceof BaseProduct ){
			BaseProduct bp = ((BaseProduct) obj);
			return TestUtil.nullableEquals(this.getSupplierKcode(),bp.getSupplierKcode())
				&& TestUtil.nullableEquals(this.getCodeBySupplier(),bp.getCodeBySupplier())
				&& TestUtil.nullableEquals(this.getCategory(), bp.getCategory())
				&& this.getCodeByDrs().equals(bp.getCodeByDrs())
				&& TestUtil.nullableEquals(this.getNameBySupplier(),bp.getNameBySupplier())
				&& TestUtil.nullableEquals(this.getInternalNote(),bp.getInternalNote())
				&& this.getSkuList().equals(bp.getSkuList());
		}
		else {
	      return false;
	    }
	}
	
	@Override
	public String toString() {
		return "ProductBaseImplForTest [getSupplierKcode()=" + getSupplierKcode() + ", getCategory()=" + getCategory()
				+ ", getCodeByDrs()=" + getCodeByDrs() + ", getNameByDrs()=" + getNameByDrs() + ", getCodeBySupplier()="
				+ getCodeBySupplier() + ", getNameBySupplier()=" + getNameBySupplier() + ", getInternalNote()="
				+ getInternalNote() + ", getSkuList()=" + getSkuList() + "]";
	}
	
	@Override
	public String getSupplierKcode() {
		return this.supplierCompanyKcode;
	}

	@Override
	public String getCategory() {
		return this.category;
	}

	@Override
	public String getCodeByDrs() {
		return this.codeByDrs;
	}

	@Override
	public String getNameByDrs() {
		return null;
	}

	@Override
	public String getCodeBySupplier() {
		return this.codeBySupplier;
	}

	@Override
	public String getNameBySupplier() {
		return this.nameBySupplier;
	}

	@Override
	public String getInternalNote() {
		return this.internalNote;
	}
	
	@Override
	public List<SKU> getSkuList() {
		if(this.skuList==null){
			this.skuList=new ArrayList<SKU>();
		}
		return this.skuList;
	}

}
