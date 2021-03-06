package com.kindminds.drs;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.Assert;

public enum TransactionLineType{
	MS2SS_PURCHASE_ALWNC_IMPORT_DUTY("MS2SS_PURCHASE_ALWNC_IMPORT_DUTY"),
	MS2SS_PURCHASE_ALWNC_CUSTOMERCARE("MS2SS_PURCHASE_ALWNC_CUSTOMERCARE"),
	MS2SS_PURCHASE_ALWNC_CUSTOMERCARE_EXEMPTION("MS2SS_PURCHASE_ALWNC_CUSTOMERCARE_EXEMPTION"),
	INVENTORY_PLACEMENT_PROGRAM_COST("INVENTORY_PLACEMENT_PROGRAM_COST"),
	MS2SS_PURCHASE_ALWNC_STORAGECOSTS("MS2SS_PurchaseAllowanceForStorageCosts"),
	MARKET_SIDE_ADVERTISING_COST("MARKET_SIDE_ADVERTISING_COST"),
	PARTIAL_REFUND("PARTIAL_REFUND"),
	MARKET_SIDE_MARKETING_ACTIVITY("MARKET_SIDE_MARKETING_ACTIVITY"),
	MARKETING_ACTIVITY_EXPENSE_REFUND("MARKETING_ACTIVITY_EXPENSE_REFUND"),
	MS2SS_PURCHASE_ALWNC_REPLACEMENTPROCESSING("MS2SS_PurchaseAllowanceForReplacementProcessing"),
	MS2SS_PURCHASE_ALWNC_OTHERCONCESSIONTOCUSTOMER_SUBTRACTION("MS2SS_PurchaseAllowanceForOtherConcessionToCustomerSubtraction"),
	MS2SS_PURCHASE_ALWNC_OTHERCONCESSIONTOCUSTOMER_ADDITION("MS2SS_PurchaseAllowanceForOtherConcessionToCustomerAddition"),
	MS2SS_PURCHASE_ALWNC_PRODUCT_RELATED_SUBTRACTION("MS2SS_PurchaseAllowanceForProductRelatedSubtraction"),
	PRODUCT_RELATED_ADDITION("PRODUCT_RELATED_ADDITION"),
	SS2SP_UNIT_PROFIT_SHARE_ADDITION("SS2SP_UnitProfitShareAddition"),
	SS2SP_UNIT_PROFIT_SHARE_SUBTRACTION("SS2SP_UnitProfitShareSubtraction"),
	SS2SP_UNIT_PROFIT_SHARE_SUBTRACTION_REFUND_FEE("SS2SP_UnitProfitShareSubtractionRefundFee"),
	SSDC_UNIT_RETAINMENT("SSDC_UnitRetainment"),
	SSDC_UNIT_RETAINMENT_REVERSAL("SSDC_UnitRetainmentReversal"),
	MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_SALES("MS2SS_UnitPurchaseAllowanceWithRetailSales"),
	MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_REFUND("MS2SS_UnitPurchaseAllowanceWithRetailRefund"),
	MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_REFUND_FEE("MS2SS_UnitPurchaseAllowanceWithRetailRefundFee"),
	MS2SS_UNIT_DDP_PAYMENT("MS2SS_UNIT_DDP_PAYMENT"),
	MS2SS_UNIT_DDP_REFUND("MS2SS_UNIT_DDP_REFUND"),
	MS_COUNTRY_INCOMETAX_WITHHOLDING_FORSS("MS_CountryIncomeTaxWithholdingForSS"),
	MS_STATE_INCOMETAX_WITHHOLDING_FORSS("MS_StateIncomeTaxWithholdingForSS"),
	SS2SP_UNIT_INVENTORY_PAYMENT("SS2SP_Unit_Inventory_Payment"),
	SS2SP_UNIT_INVENTORY_REFUND("SS2SP_Unit_Inventory_Refund"),
	SS2SP_UNIT_INVENTORY_SELL_BACK("SS2SP_Unit_Inventory_Sell_Back"),
	SS2SP_INVENTORY_SELL_BACK_TAIWAN("SS2SP_Inventory_Sell_Back_Taiwan"),
	SS2SP_INVENTORY_SELL_BACK_OTHER("SS2SP_Inventory_Sell_Back_Other"),
	SS2SP_INVENTORY_SELL_BACK_DISPOSE("SS2SP_Inventory_Sell_Back_Dispose"),
	SS2SP_INVENTORY_SELL_BACK_RECOVERY("SS2SP_Inventory_Sell_Back_Recovery"),
	SSI_VAT("SSI_VAT"),
	MS2SS_SSDC_PAYMENT_ONBEHALF_OF_MSDC_FOR_IMPORT_DUTY("MS2SS_SSDCPaymentOnBehalfOfMSDCForImportDuty"),
	MS2SS_PRODUCT_INVENTORY_RETURN("MS2SS_Product_Inventory_Return"),
	FBA_REMOVAL_FEE("FBA_REMOVAL_FEE"),
	FBA_LONG_TERM_STORAGE("FBA_LONG_TERM_STORAGE"),
	FBA_MONTHLY_STORAGE("FBA_MONTHLY_STORAGE"),
	OTHER_REFUND_AND_ALLOWANCE_FROM_SALE_SIDE_CUSTOMER("OTHER_REFUND_AND_ALLOWANCE_FROM_SALE_SIDE_CUSTOMER");
	private String name;
	private static final Map<String,TransactionLineType> nameToTypeMap = new HashMap<>();
	static {
        for (TransactionLineType type : TransactionLineType.values())
        	nameToTypeMap.put(type.getName(),type);
    }
	TransactionLineType(String name) {
		this.name=name;
	}
	public String getName() {
		return this.name;
	}
	public static TransactionLineType fromName(String name){
		TransactionLineType type = nameToTypeMap.get(name);
		Assert.notNull(type,"TransactionLineType nul");
		return type;
	}
}