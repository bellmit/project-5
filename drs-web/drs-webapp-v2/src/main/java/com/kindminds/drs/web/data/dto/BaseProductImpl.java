package com.kindminds.drs.web.data.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import com.kindminds.drs.api.v1.model.product.BaseProduct;
import com.kindminds.drs.api.v1.model.product.SKU;

public class BaseProductImpl implements BaseProduct {
	
	private String supplierKcode;
	private String category;
	private String codeBySupplier;
	private String codeByDrs;
	private String nameBySupplier;
	private String nameByDrs;
	private String internalNote;
	private List<SKU> skuList = new ArrayList<SKU>();
			
	@Override
	public String getSupplierKcode() {
		return this.supplierKcode;
	}

	public void setSupplierKcode(String supplierKcode) {
		this.supplierKcode = supplierKcode;
	}

	@Override
	public String getCategory() {
		return StringUtils.hasText(this.category)?this.category:null;
	}
	
	public void setCategory(String category) {
		this.category = category;		
	}
		
	@Override
	public String getCodeBySupplier() {
		return this.codeBySupplier;
	}

	public void setCodeBySupplier(String codeBySupplier) {
		this.codeBySupplier = codeBySupplier;
	}

	@Override
	public String getCodeByDrs() {
		return this.codeByDrs;
	}

	public void setCodeByDrs(String codeByDrs) {
		this.codeByDrs = codeByDrs;
	}

	@Override
	public String getNameBySupplier() {
		return this.nameBySupplier;
	}

	public void setNameBySupplier(String nameBySupplier) {
		this.nameBySupplier = nameBySupplier;
	}

	@Override
	public String getNameByDrs() {
		return this.nameByDrs;
	}
	
	public void setNameByDrs(String nameByDrs) {
		this.nameByDrs = nameByDrs;		
	}
	
	@Override
	public String getInternalNote() {
		return this.internalNote;
	}

	public void setInternalNote(String internalNote) {
		this.internalNote = internalNote;
	}

	@Override
	public List<SKU> getSkuList() {		
		return this.skuList;
	}
	
	public void setSkuList(List<SKU> skuList){
		this.skuList = skuList;		
	}
		
}