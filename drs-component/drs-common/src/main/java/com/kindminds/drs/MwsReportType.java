package com.kindminds.drs;


public enum  MwsReportType {

    FBA_Manage_Inventory( 0 , "_GET_FBA_MYI_UNSUPPRESSED_INVENTORY_DATA_", "", true, true),
    FBA_Reimbursements(1,"_GET_FBA_REIMBURSEMENTS_DATA_", "", true, true),
    Settlement_V2(2,"_GET_V2_SETTLEMENT_REPORT_DATA_FLAT_FILE_V2_", "", true, true),
    FBA_Storage_Fees(3,"_GET_FBA_STORAGE_FEE_CHARGES_DATA_", "", true, true),
    FBA_Customer_Return(4,"_GET_FBA_FULFILLMENT_CUSTOMER_RETURNS_DATA_", "", true, true),


    FBA_Amazon_Fulfilled_Shipments(10, "_GET_AMAZON_FULFILLED_SHIPMENTS_DATA_", "FBA", true, true),
    Flat_All_Orders_Last_Update(11, "_GET_FLAT_FILE_ALL_ORDERS_DATA_BY_LAST_UPDATE_", "FBA", true, true),
    Flat_All_Orders_Order_Date(12, "_GET_FLAT_FILE_ALL_ORDERS_DATA_BY_ORDER_DATE_", "FBA", true, true),
    XML_All_Orders_Last_Update(13, "_GET_XML_ALL_ORDERS_DATA_BY_LAST_UPDATE_", "FBA", true, true),
    XML_All_Orders_Order_Date(14, "_GET_XML_ALL_ORDERS_DATA_BY_ORDER_DATE_", "FBA", true, true),
    FBA_Customer_Shipment_Sales(15, "_GET_FBA_FULFILLMENT_CUSTOMER_SHIPMENT_SALES_DATA_", "FBA", true, true),
    FBA_Promotions(16, "_GET_FBA_FULFILLMENT_CUSTOMER_SHIPMENT_PROMOTION_DATA_", "FBA", true, false),
    FBA_Customer_Taxes(17, "_GET_FBA_FULFILLMENT_CUSTOMER_TAXES_DATA_", "FBA", true, false),
    Remote_Fulfillment_Eligibility(18, "_GET_REMOTE_FULFILLMENT_ELIGIBILITY_", "FBA", true, false),

    FBA_Amazon_Fulfilled_Inventory(19, "_GET_AFN_INVENTORY_DATA_", "FBA", true, true),
    FBA_Multi_Country_Inventory(20, "_GET_AFN_INVENTORY_DATA_BY_COUNTRY_", "FBA", false, true),
    FBA_Daily_Inventory_History(21, "_GET_FBA_FULFILLMENT_CURRENT_INVENTORY_DATA_", "FBA", true, true),
    FBA_Monthly_Inventory_History(22, "_GET_FBA_FULFILLMENT_MONTHLY_INVENTORY_DATA_", "FBA", true, true),
    FBA_Received_Inventory(23, "_GET_FBA_FULFILLMENT_INVENTORY_RECEIPTS_DATA_", "FBA", true, true),
    FBA_Reserved_Inventory(24, "_GET_RESERVED_INVENTORY_DATA_", "FBA", true, true),
    FBA_Inventory_Event_Detail(25, "_GET_FBA_FULFILLMENT_INVENTORY_SUMMARY_DATA_", "FBA", true, true),
    FBA_Inventory_Adjustments(26, "_GET_FBA_FULFILLMENT_INVENTORY_ADJUSTMENTS_DATA_", "FBA", true, true),
    FBA_Inventory_Health(27, "_GET_FBA_FULFILLMENT_INVENTORY_HEALTH_DATA_", "FBA", true, true),
    FBA_Manage_Inventory_Report(28, "_GET_FBA_MYI_UNSUPPRESSED_INVENTORY_DATA_", "FBA", true, true),
    FBA_Manage_Inventory_Archived(29, "_GET_FBA_MYI_ALL_INVENTORY_DATA_", "FBA", true, true),
    Restock_Inventory(30, "_GET_RESTOCK_INVENTORY_RECOMMENDATIONS_REPORT_", "FBA", true, false),
    FBA_Inbound_Performance(31, "_GET_FBA_FULFILLMENT_INBOUND_NONCOMPLIANCE_DATA_", "FBA", true, true),
    FBA_Stranded_Inventory(32, "_GET_STRANDED_INVENTORY_UI_DATA_", "FBA", true, false),
    FBA_Bulk_Fix_Stranded_Inventory(33, "_GET_STRANDED_INVENTORY_LOADER_DATA_", "FBA", true, false),
    FBA_Inventory_Age(34, "_GET_FBA_INVENTORY_AGED_DATA_", "FBA", true, false),
    FBA_Manage_Excess_Inventory(35, "_GET_EXCESS_INVENTORY_DATA_", "FBA", true, true),
    FBA_Storage_Fee_Charges(36,"_GET_FBA_STORAGE_FEE_CHARGES_DATA_", "FBA", true, true),
//    Report_Exchange_Data(37, "_GET_PRODUCT_EXCHANGE_DATA_", "FBA", false, false),

    // FBA_Fee_Preview: To successfully generate a report,
    // specify the StartDate parameter of a minimum 72 hours prior to NOW and EndDate to NOW
    FBA_Fee_Preview(38, "_GET_FBA_ESTIMATED_FBA_FEES_TXT_DATA_", "FBA", true, true),
    FBA_Reimbursements_Report(39,"_GET_FBA_REIMBURSEMENTS_DATA_", "FBA", true, true),
    FBA_Long_Term_Storage_Fee_Charges(40, "_GET_FBA_FULFILLMENT_LONGTERM_STORAGE_FEE_CHARGES_DATA_", "FBA", true, true),

    FBA_Returns(41,"_GET_FBA_FULFILLMENT_CUSTOMER_RETURNS_DATA_", "FBA", true, true),
    FBA_Replacements(42, "_GET_FBA_FULFILLMENT_CUSTOMER_SHIPMENT_REPLACEMENT_DATA_", "FBA", true, false),

    // FBA_Recommended_Removal: Identifies sellable items that will be 365 days or older
    // during the next Long-Term Storage cleanup event,
    // if the report is generated within six weeks of the cleanup event date.
    FBA_Recommended_Removal(43, "_GET_FBA_RECOMMENDED_REMOVAL_DATA_", "FBA", true, true),
    FBA_Removal_Order_Detail(44, "_GET_FBA_FULFILLMENT_REMOVAL_ORDER_DETAIL_DATA_", "FBA", true, true),
    FBA_Removal_Shipment_Detail(45, "_GET_FBA_FULFILLMENT_REMOVAL_SHIPMENT_DETAIL_DATA_", "FBA", true, true),

    Small_Light_Inventory_Report(46, "_GET_FBA_UNO_INVENTORY_DATA_", "FBA", true, true),

    Inventory(50, "_GET_FLAT_FILE_OPEN_LISTINGS_DATA_", "Inventory", true, true),
    All_Listings(51, "_GET_MERCHANT_LISTINGS_ALL_DATA_", "Inventory", true, true),
    Active_Listings(52, "_GET_MERCHANT_LISTINGS_DATA_", "Inventory", true, true),
    Inactive_Listings(53, "_GET_MERCHANT_LISTINGS_INACTIVE_DATA_", "Inventory", true, true),
    Open_Listings(54, "_GET_MERCHANT_LISTINGS_DATA_BACK_COMPAT_", "Inventory", true, true),
    Open_Listings_Lite(56, "_GET_MERCHANT_LISTINGS_DATA_LITE_", "Inventory", true, true),
    Open_Listings_Liter(57, "_GET_MERCHANT_LISTINGS_DATA_LITER_", "Inventory", true, true),
    Canceled_Listings(58, "_GET_MERCHANT_CANCELLED_LISTINGS_DATA_", "Inventory", true, true),
    Sold_Listings(59, "_GET_CONVERGED_FLAT_FILE_SOLD_LISTINGS_DATA_", "Inventory", true, true),
    Listing_Quality_and_Suppressed_Listing(60, "_GET_MERCHANT_LISTINGS_DEFECT_DATA_", "Inventory", true, true),
    Pan_European_Eligibility_FBA_ASINs(61, "_GET_PAN_EU_OFFER_STATUS_", "Inventory", false, true),
    Pan_European_Eligibility_Self_Fulfilled_ASINs(62, "_GET_MFN_PAN_EU_OFFER_STATUS_", "Inventory", false, true),
    Global_Expansion_Opportunities(63, "_GET_FLAT_FILE_GEO_OPPORTUNITIES_", "Inventory", true, true),
    //do not request a report more than once per 24 hour period
    Referral_Fee_Preview(64, "_GET_REFERRAL_FEE_PREVIEW_REPORT_", "Inventory", true, true),
    FBA_Inventory_Ledger(65, "_GET_RESERVED_INVENTORY_DATA_", "FBA", true, true),
    ;


    private int key;
    private String value;
    private String type;
    private Boolean availableNA;
    private Boolean availableEU;

    MwsReportType(int key , String value, String type, Boolean availableNA, Boolean availableEU){
        this.key = key;
        this.value = value;
        this.type = type;
        this.availableNA = availableNA;
        this.availableEU = availableEU;
    }

    public int getKey() {return this.key;}
    public String getValue(){
        return this.value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean isAvailableNA() {
        return availableNA;
    }

    public Boolean isAvailableEU() {
        return availableEU;
    }


}
