package com.kindminds.drs.v1.model.impl;

import com.kindminds.drs.api.v1.model.product.BaseProductOnboardingWithSKU.BaseProductOnboardingSKUItem;

import java.util.Collection;

public class BaseProductOnboardingSKUItemImpl implements BaseProductOnboardingSKUItem{

	private int index;
	private String originalSKU;
	private String updatedSKU;
	private String type1;
	private String type1Value;
	private String type2;
	private String type2Value;
	private String GTINValue;
	private Collection<String> applicableRegionList;
	
	@Override
	public int getIndex() {
		return this.index;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	@Override
	public String getOriginalSKU() {
		return this.originalSKU;
	}

	public void setOriginalSKU(String originalSKU) {
		this.originalSKU = originalSKU;		
	}
		
	@Override
	public String getUpdatedSKU() {
		return this.updatedSKU;
	}

	public void setUpdatedSKU(String updatedSKU) {
		this.updatedSKU = updatedSKU; 		
	}

	@Override
	public String getType1() {
		return this.type1;
	}
	
	public void setType1(String type1) {
		this.type1 = type1;
	}
	
	@Override
	public String getType1Value() {
		return this.type1Value;
	}

	public void setType1Value(String type1Value) {
		this.type1Value = type1Value; 		
	}

	@Override
	public String getType2() {
		return this.type2;
	}
	
	public void setType2(String type2) {
		this.type2 = type2;
	}
	
	@Override
	public String getType2Value() {
		return this.type2Value;
	}

	public void setType2Value(String type2Value) {
		this.type2Value = type2Value; 		
	}
			
	@Override
	public String getGTINValue() {
		return this.GTINValue;
	}

	public void setGTINValue(String GTINValue) {
		this.GTINValue = GTINValue;		
	}
		
	@Override
	public Collection<String> getApplicableRegionList() {
		return this.applicableRegionList;
	}
		
	public void setApplicableRegionList(Collection<String> applicableRegionList) {
		this.applicableRegionList = applicableRegionList;		
	}
		
}