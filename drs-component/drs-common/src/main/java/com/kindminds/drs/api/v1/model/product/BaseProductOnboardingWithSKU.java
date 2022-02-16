package com.kindminds.drs.api.v1.model.product;

import java.util.Collection;
import java.util.List;

public interface BaseProductOnboardingWithSKU {
	
	public String getProductBaseCode();
	public String getSupplierKcode();
	public String getStatus();
	public String getVariationType1();
	public String getVariationType2();
	public String getProductWithVariation();
	public List<BaseProductOnboardingSKUItem> getSKULineItems();
	
	public interface BaseProductOnboardingSKUItem{
		public int getIndex();
		public String getOriginalSKU();
		public String getUpdatedSKU();
		public String getType1();
		public String getType1Value();
		public String getType2();
		public String getType2Value();				
		public String getGTINValue();
		public Collection<String> getApplicableRegionList();
	}
		
}
