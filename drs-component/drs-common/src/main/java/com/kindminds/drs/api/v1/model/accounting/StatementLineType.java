package com.kindminds.drs.api.v1.model.accounting;

import java.util.ArrayList;
import java.util.List;

public enum StatementLineType {
	MS2SS_PRODUCT_INVENTORY_PAYMENT("MS2SS_ProductInventoryPayment"),
	MS2SS_PRODUCT_INVENTORY_REFUND("MS2SS_ProductInventoryRefund"),
	MS2SS_PURCHASE_ALLOWANCE("MS2SS_PurchaseAllowance"),
	MS2SS_PRODUCT_INVENTORY_RETURN("MS2SS_ProductInventoryReturn"),
	MS_WITHHOLDING_TAX_FOR_SS("MS_WithholdingTaxForSS"),
	SS_PRODUCT_PROFIT_SHARE_FOR_SP("SS_ProductProfitShareForSP"),
	SS_COPYWRITING_FOR_SP("SS_CopywritingWorkItemforSP"),
	SS_IMAGEPREPARATION_FOR_SP("SS_ImagePreparationWorkItemforSP"),
	SS_CUSTOMER_SERVICE_FOR_SP("SS_CustomerServiceWorkItemforSP"),
	SS_IMPORTEXPORT_FEE_FOR_SP("SS_ImportExportFeeforSP"),
	SS_PRODUCTRELATED_ADJUSTMENT_FOR_SP("SS_ProductRelatedAdjustmentforSP"),
	SS_RETURN_SHP_CHARGE_FOR_SP("SS_ReturnShipmentChargeforSP"),
	SS2SP_PRODUCT_INVENTORY_PAYMENT("SS2SP_ProductInventoryPayment"),
	SS2SP_PRODUCT_INVENTORY_REFUND("SS2SP_ProductInventoryRefund"),
	SS2SP_PRODUCT_INVENTORY_SELL_BACK("SS2SP_ProductInventorySellBack"),
	SS2SP_INVENTORY_SELL_BACK_TAIWAN("SS2SP_InventorySellBackTaiwan"),
	SS2SP_INVENTORY_SELL_BACK_OTHER("SS2SP_InventorySellBackOther"),
	SS2SP_INVENTORY_SELL_BACK_DISPOSE("SS2SP_InventorySellBackDispose"),
	SS2SP_INVENTORY_SELL_BACK_RECOVERY("SS2SP_InventorySellBackRecovery"),
	SS_INVENTORY_SHIPMENTCHARGE_FOR_SP("SS_InventoryShipmentChargeforSP"),
	SS_SUPPLIER_RELATED_ADJUSTMENT_FOR_SP("SS_SupplierRelatedAdjustmentForSP"),
	SS_SERVICE_SALE_FOR_SP("SS_ServiceSaleForSP"),
	SS_MONTHLY_MANAGEMENT_FEE_FOR_SP("SS_MonthlyManagementFeeForSP"),
	MS2SS_SSDC_PAYMENT_ON_BEHALF_OF_MSDC("MS2SS_SSDCPaymentOnBehalfOfMSDC"),
	MSDC_PAYMENT_ON_BEHALF_OF_SSDC("MSDCPaymentOnBehalfOfSSDC"),
	MS2SS_REMITTANCE_MS2SS("MS2SS_Remittance_MS2SS"),
	MS2SS_REMITTANCE_SS2MS("MS2SS_Remittance_SS2MS"),
	SS2SP_REMITTANCE_SS2SP("SS2SP_Remittance_SS2SP"),
	SS2SP_REMITTANCE_SP2SS("SS2SP_Remittance_SP2SS");
	private String name;
	StatementLineType(String value) {
		this.name = value;
	}
	public String getValue() {
		return this.name;
	}
    public static StatementLineType fromName(String value) {
    	for(StatementLineType typeEnum : StatementLineType.values()) {
    		if (typeEnum.getValue().equals(value)) {
    			return typeEnum;
    		}
    	}
        return null;
    }

    private static List<String> sellbackTypes = initializeSellbackTypes();
	private static List<String> initializeSellbackTypes() {
		List<String> types = new ArrayList<>();
		types.add(SS2SP_PRODUCT_INVENTORY_SELL_BACK.getValue());
		types.add(SS2SP_INVENTORY_SELL_BACK_TAIWAN.getValue());
		types.add(SS2SP_INVENTORY_SELL_BACK_OTHER.getValue());
		types.add(SS2SP_INVENTORY_SELL_BACK_DISPOSE.getValue());
		types.add(SS2SP_INVENTORY_SELL_BACK_RECOVERY.getValue());
		return types;
	}
	public static List<String> getSellbackTypes() {
		return sellbackTypes;
	}
}
