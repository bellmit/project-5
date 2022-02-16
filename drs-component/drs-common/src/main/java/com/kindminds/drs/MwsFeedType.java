package com.kindminds.drs;


public enum MwsFeedType {

    Product_Feed(0, "_POST_PRODUCT_DATA_"),
    Inventory_Feed(1, "_POST_INVENTORY_AVAILABILITY_DATA_"),
    Overrides_Feed(2, "_POST_PRODUCT_OVERRIDES_DATA_"),
    Pricing_Feed(3, "_POST_PRODUCT_PRICING_DATA_"),
    Product_Images_Feed(4, "_POST_PRODUCT_IMAGE_DATA_"),
    Relationships_Feed(5, "_POST_PRODUCT_RELATIONSHIP_DATA_"),
    Flat_File_Inventory_Loader_Feed(6, "_POST_FLAT_FILE_INVLOADER_DATA_"),
    Flat_File_Listings_Feed(7, "_POST_FLAT_FILE_LISTINGS_DATA_")
    ;


    private int key;
    private String value;

    MwsFeedType(int key , String value){
        this.key = key;
        this.value = value;
    }

    public int getKey() {return this.key;}
    public String getValue(){
        return this.value;
    }


}
