package com.kindminds.drs.web.data.dto;

import java.util.Collection;

import com.kindminds.drs.api.v1.model.product.BaseProductOnboardingWithSKU.BaseProductOnboardingSKUItem;

public class BaseProductOnboardingSKUItemImpl implements BaseProductOnboardingSKUItem {

	private int index;
	private String originalSKU;
	private String updatedSKU;
	private String type1;
	private String type1Value;
	private String type2;
	private String type2Value;
	private String GTINValue;
	private Collection<String> applicableRegionList;
		
	public BaseProductOnboardingSKUItemImpl(){}
	
	public BaseProductOnboardingSKUItemImpl(BaseProductOnboardingSKUItem origLineItem){
		this.index = origLineItem.getIndex();
		this.originalSKU = origLineItem.getOriginalSKU();
		this.updatedSKU = origLineItem.getUpdatedSKU();
		this.type1 = origLineItem.getType1();
		this.type1Value  = origLineItem.getType1Value();
		this.type2 = origLineItem.getType2();
		this.type2Value = origLineItem.getType2Value();		
		this.GTINValue = origLineItem.getGTINValue();
		this.applicableRegionList = origLineItem.getApplicableRegionList();
	}
			
	@Override
	public String toString() {
		return "BaseProductOnboardingSKUItemImpl [getIndex()=" + getIndex() + ", getOriginalSKU()=" + getOriginalSKU()
				+ ", getUpdatedSKU()=" + getUpdatedSKU() + ", getType1()=" + getType1() + ", getType1Value()="
				+ getType1Value() + ", getType2()=" + getType2() + ", getType2Value()=" + getType2Value()
				+ ", getGTINValue()=" + getGTINValue() + ", getApplicableRegionList()=" + getApplicableRegionList()
				+ "]";
	}

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