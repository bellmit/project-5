package com.kindminds.drs.persist.v1.model.mapping.product;

import java.util.List;






import com.kindminds.drs.api.v1.model.product.BaseProduct;
import com.kindminds.drs.api.v1.model.product.SKU;


public class ProductBaseImpl implements BaseProduct {

	////@Column(name="id")
	private int id;
	//@Column(name="supplier_company_kcode")
	private String supplierCompanyName;
	//@Column(name="code_by_supplier")
	private String codeBySupplier;
	//@Column(name="category")
	private String category;
	//@Id //@Column(name="code_by_drs")
	private String codeByDrs;
	//@Column(name="name_by_supplier")
	private String nameBySupplier;
	//@Column(name="internal_notes")
	private String internalNotes;

	private List<SKU> skuList=null;

	public ProductBaseImpl() {
	}
	public ProductBaseImpl(
			String supplierCompanyName,
			String category,
			String codeBySupplier,
			String codeByDrs,
			String nameBySupplier,
			String internalNotes,
			List<SKU> skuList) {
		this.supplierCompanyName = supplierCompanyName;
		this.category = category;
		this.codeBySupplier = codeBySupplier;
		this.codeByDrs = codeByDrs;
		this.nameBySupplier = nameBySupplier;
		this.internalNotes = internalNotes;
		this.skuList = skuList;
	}
	public ProductBaseImpl(int id, String supplierCompanyName, String codeBySupplier, String category, String codeByDrs, String nameBySupplier, String internalNotes) {
		this.id = id;
		this.supplierCompanyName = supplierCompanyName;
		this.codeBySupplier = codeBySupplier;
		this.category = category;
		this.codeByDrs = codeByDrs;
		this.nameBySupplier = nameBySupplier;
		this.internalNotes = internalNotes;
	}

	public void setSkuList(List<SKU> skuList){
		this.skuList = skuList;
	}
	
	@Override
	public String toString() {
		return "ProductBaseImpl [getSupplierKcode()=" + getSupplierKcode() + ", getCategory()=" + getCategory()
				+ ", getCodeByDrs()=" + getCodeByDrs() + ", getNameByDrs()=" + getNameByDrs() + ", getCodeBySupplier()="
				+ getCodeBySupplier() + ", getNameBySupplier()=" + getNameBySupplier() + ", getInternalNote()="
				+ getInternalNote() + ", getSkuList()=" + getSkuList() + "]";
	}

	@Override
	public String getSupplierKcode() {
		return this.supplierCompanyName;
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
		return this.internalNotes;
	}
	
	@Override
	public List<SKU> getSkuList() {
		return this.skuList;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setSupplierCompanyKcode(String supplierCompanyName) {
		this.supplierCompanyName = supplierCompanyName;
	}

	public void setCodeBySupplier(String codeBySupplier) {
		this.codeBySupplier = codeBySupplier;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setCodeByDrs(String codeByDrs) {
		this.codeByDrs = codeByDrs;
	}

	public void setNameBySupplier(String nameBySupplier) {
		this.nameBySupplier = nameBySupplier;
	}

	public void setInternalNotes(String internalNotes) {
		this.internalNotes = internalNotes;
	}
}
