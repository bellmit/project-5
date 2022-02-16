package com.kindminds.drs.api.v1.model.accounting;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ServiceItem {
	public enum ServiceItemName {
		SS2SP_UnitProfitShareAddition(12, "SS2SP_UnitProfitShareAddition"), 
		SS2SP_UnitProfitShareSubtraction(13, "SS2SP_UnitProfitShareSubtraction"),
		SS2SP_RevenueShareSubtractionForMSCountryIncomeTaxWithholding(14, "SS2SP_RevenueShareSubtractionForMSCountryIncomeTaxWithholding"),
		SSI_Unit_Inventory_Payment(36, "SSI_Unit_Inventory_Payment"),
		SSI_Unit_Inventory_Refund(37, "SSI_Unit_Inventory_Refund");
		
		private static final Map<Integer, ServiceItemName> typesByValue = new HashMap<Integer, ServiceItemName>();
		
		static {
	        for (ServiceItemName type : ServiceItemName.values()) {
	            typesByValue.put(type.key, type);
	        }
	    }
		
		public static ServiceItemName fromKey(int key) {
	        return typesByValue.get(key);
	    }
	    
	    public static ServiceItemName fromValue(String value) {
	    	for (ServiceItemName r : typesByValue.values()) {
	    		if (r.getValue().equals(value)) {
	    			return r;
	    		}
	    	}
	        return null;
	    }
		
		private int key;
		private String value;
		ServiceItemName(int key, String value) {
			this.key = key;
			this.value = value;
		}
		
		public int getKey() {
			return this.key;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	public enum ServiceItemType {
//		MSI_Service_Items(0, "MS2SS_RevenueShareSubtractionForCopywriting:MS2SS_RevenueShareSubtractionForImagePreparation:MS2SS_RevenueShareSubtractionForCustomerCare:MS2SS_RevenueShareSubtractionForInventoryPlacementProgramCosts:MS2SS_RevenueShareSubtractionForImportExportCosts:MS2SS_RevenueShareSubtractionForStorageCosts:MS2SS_RevenueShareSubtractionForAdvertising:MS2SS_RevenueShareSubtractionForReplacementProcessing:MS2SS_RevenueShareSubtractionForOtherConcessionToCustomer:MS2SS_RevenueShareSubtractionForProductRelatedAdjustment:MS2SS_RevenueShareSubtractionForReturnShipments"),
//		SSI_Service_Items(1, "SSI_Copywriting_Work_Item:SSI_Image_Preparation_Work_Item:SSI_Customer_Service_Work_Item:SSI_Inventory_Placement_Program_Fee:SSI_Export_Fee:SSI_Storage_Fee:SSI_Advertising_Fee:SSI_Replacement_Processing_Fee:SSI_Other_Concession_To_Customer:SSI_Product_Related_Adjustment:SSI_Return_Shipment_Charge");
		MSI_Service_Items(0, "MS2SS_PurchaseAllowanceForCustomerCare:MS2SS_PurchaseAllowanceForInventoryPlacementProgramCosts:MS2SS_PurchaseAllowanceForStorageCosts:MS2SS_PurchaseAllowanceForAdvertising:MS2SS_PurchaseAllowanceForReplacementProcessing:MS2SS_PurchaseAllowanceForOtherConcessionToCustomer"),
		SSI_Service_Items(1, "SSI_Copywriting_Work_Item:SSI_Image_Preparation_Work_Item:SSI_Customer_Service_Work_Item:SSI_Export_Fee:SSI_Product_Related_Adjustment:SSI_Return_Shipment_Charge");
		private int key;
		private String value;
		ServiceItemType(int key, String value) {
			this.key = key;
			this.value = value;
		}
		
		public int getKey() {
			return this.key;
		}
		
		public List<String> getValue() {
			String v2 = new String(value);
			String[] values = v2.split(":");
			List<String> valueList = Arrays.asList(values);
			return valueList;
		}
	}
	public int getId();
	public String getServiceItemName();
	public BigDecimal getAmount();
	
}