package com.kindminds.drs.api.adapter;


public enum MwsFbaReportType {

    FBA_Amazon_Fulfilled_Shipments(1, "_GET_AMAZON_FULFILLED_SHIPMENTS_DATA_", true, true),
    Flat_All_Orders_Last_Update(2, "_GET_FLAT_FILE_ALL_ORDERS_DATA_BY_LAST_UPDATE_", true, true),
    Flat_All_Orders_Order_Date(3, "_GET_FLAT_FILE_ALL_ORDERS_DATA_BY_ORDER_DATE_", true, true),
    XML_All_Orders_Last_Update(4, "_GET_XML_ALL_ORDERS_DATA_BY_LAST_UPDATE_", true, true),
    XML_All_Orders_Order_Date(5, "_GET_XML_ALL_ORDERS_DATA_BY_ORDER_DATE_", true, true),
    FBA_Customer_Shipment_Sales(6, "_GET_FBA_FULFILLMENT_CUSTOMER_SHIPMENT_SALES_DATA_", true, true),
    FBA_Promotions(7, "_GET_FBA_FULFILLMENT_CUSTOMER_SHIPMENT_PROMOTION_DATA_", true, true),
    FBA_Customer_Taxes(8, "_GET_FBA_FULFILLMENT_CUSTOMER_TAXES_DATA_", true, true),
    Remote_Fulfillment_Eligibility(9, "_GET_REMOTE_FULFILLMENT_ELIGIBILITY_", true, true),

    FBA_Amazon_Fulfilled_Inventory(10, "_GET_AFN_INVENTORY_DATA_", true, true),
    FBA_Multi_Country_Inventory(11, "_GET_AFN_INVENTORY_DATA_BY_COUNTRY_", true, true),
    FBA_Daily_Inventory_History(12, "_GET_FBA_FULFILLMENT_CURRENT_INVENTORY_DATA_", true, true),
    FBA_Monthly_Inventory_History(13, "_GET_FBA_FULFILLMENT_MONTHLY_INVENTORY_DATA_", true, true),
    FBA_Received_Inventory(14, "_GET_FBA_FULFILLMENT_INVENTORY_RECEIPTS_DATA_", true, true),
    FBA_Reserved_Inventory(15, "_GET_RESERVED_INVENTORY_DATA_", true, true),
    FBA_Inventory_Event_Detail(16, "_GET_FBA_FULFILLMENT_INVENTORY_SUMMARY_DATA_", true, true),
    FBA_Inventory_Adjustments(17, "_GET_FBA_FULFILLMENT_INVENTORY_ADJUSTMENTS_DATA_", true, true),
    FBA_Inventory_Health(18, "_GET_FBA_FULFILLMENT_INVENTORY_HEALTH_DATA_", true, true),
    FBA_Manage_Inventory(19, "_GET_FBA_MYI_UNSUPPRESSED_INVENTORY_DATA_", true, true),
    FBA_Manage_Inventory_Archived(20, "_GET_FBA_MYI_ALL_INVENTORY_DATA_", true, true),
    Restock_Inventory(21, "_GET_RESTOCK_INVENTORY_RECOMMENDATIONS_REPORT_", true, true),
    FBA_Inbound_Performance(22, "_GET_FBA_FULFILLMENT_INBOUND_NONCOMPLIANCE_DATA_", true, true),
    FBA_Stranded_Inventory(23, "_GET_STRANDED_INVENTORY_UI_DATA_", true, true),
    FBA_Bulk_Fix_Stranded_Inventory(24, "_GET_STRANDED_INVENTORY_LOADER_DATA_", true, true),
    FBA_Inventory_Age(25, "_GET_FBA_INVENTORY_AGED_DATA_", true, true),
    FBA_Manage_Excess_Inventory(26, "_GET_EXCESS_INVENTORY_DATA_", true, true),
    FBA_Storage_Fees(27,"_GET_FBA_STORAGE_FEE_CHARGES_DATA_", true, true),

    FBA_Fee_Preview(28, "_GET_FBA_ESTIMATED_FBA_FEES_TXT_DATA_", true, true),
    FBA_Reimbursements(29,"_GET_FBA_REIMBURSEMENTS_DATA_", true, true),
    FBA_Long_Term_Storage_Fee_Charges(30, "_GET_FBA_FULFILLMENT_LONGTERM_STORAGE_FEE_CHARGES_DATA_", true, true),

    FBA_Returns(31,"_GET_FBA_FULFILLMENT_CUSTOMER_RETURNS_DATA_", true, true),
    FBA_Replacements(32, "_GET_FBA_FULFILLMENT_CUSTOMER_SHIPMENT_REPLACEMENT_DATA_", true, true),

    FBA_Recommended_Removal(33, "_GET_FBA_RECOMMENDED_REMOVAL_DATA_", true, true),
    FBA_Removal_Order_Detail(34, "_GET_FBA_FULFILLMENT_REMOVAL_ORDER_DETAIL_DATA_", true, true),
    FBA_Removal_Shipment_Detail(35, "_GET_FBA_FULFILLMENT_REMOVAL_SHIPMENT_DETAIL_DATA_", true, true),

    Small_Light_Inventory_Report(36, "_GET_FBA_UNO_INVENTORY_DATA_", true, true)
    ;


    private int key;
    private String value;
    private Boolean hasMarketplaceNA;
    private Boolean hasMarketplaceEU;

    MwsFbaReportType(int key , String value, Boolean hasMarketplaceNA, Boolean hasMarketplaceEU){
        this.key = key;
        this.value = value;
        this.hasMarketplaceNA = hasMarketplaceNA;
        this.hasMarketplaceEU = hasMarketplaceEU;
    }

    public int getKey() {return this.key;}
    public String getValue(){
        return this.value;
    }

    public Boolean getHasMarketplaceNA() {
        return hasMarketplaceNA;
    }

    public Boolean getHasMarketplaceEU() {
        return hasMarketplaceEU;
    }
}
