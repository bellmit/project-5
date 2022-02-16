package com.kindminds.drs.api.adapter;


public enum MwsInventoryReportType {

    Inventory(1, "_GET_FLAT_FILE_OPEN_LISTINGS_DATA_", true, true),
    All_Listings(2, "_GET_MERCHANT_LISTINGS_ALL_DATA_", true, true),
    Active_Listings(3, "_GET_MERCHANT_LISTINGS_DATA_", true, true),
    Inactive_Listings(4, "_GET_MERCHANT_LISTINGS_INACTIVE_DATA_", true, true),
    Open_Listings(5, "_GET_MERCHANT_LISTINGS_DATA_BACK_COMPAT_", true, true),
    Open_Listings_Lite(6, "_GET_MERCHANT_LISTINGS_DATA_LITE_", true, true),
    Open_Listings_Liter(7, "_GET_MERCHANT_LISTINGS_DATA_LITER_", true, true),
    Canceled_Listings(8, "_GET_MERCHANT_CANCELLED_LISTINGS_DATA_", true, true),
    Sold_Listings(9, "_GET_CONVERGED_FLAT_FILE_SOLD_LISTINGS_DATA_", true, true),
    Listing_Quality_and_Suppressed_Listing(10, "_GET_MERCHANT_LISTINGS_DEFECT_DATA_", true, true),
    Pan_European_Eligibility_FBA_ASINs(11, "_GET_PAN_EU_OFFER_STATUS_", true, true),
    Pan_European_Eligibility_Self_Fulfilled_ASINs(12, "_GET_MFN_PAN_EU_OFFER_STATUS_", true, true),
    Global_Expansion_Opportunities(13, "_GET_FLAT_FILE_GEO_OPPORTUNITIES_", true, true),
    Referral_Fee_Preview(14, "_GET_REFERRAL_FEE_PREVIEW_REPORT_", true, true)
    ;


    private int key;
    private String value;
    private Boolean hasMarketplaceNA;
    private Boolean hasMarketplaceEU;

    MwsInventoryReportType(int key , String value, Boolean hasMarketplaceNA, Boolean hasMarketplaceEU){
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
