package com.kindminds.drs.api.v1.model.report;

public interface Satisfaction {
    int getSkuId();

    void setSkuId(int skuId);

    String getSkuCodeByDrs();

    void setSkuCodeByDrs(String skuCodeByDrs);

    String getProductName();

    void setProductName(String productName);

    int getSupplierId();

    void setSupplierId(int supplierId);

    int getQuantityByOne();

    void setQuantityByOne(int quantityByOne);

    int getReturnQuantityByOne();

    void setReturnQuantityByOne(int returnQuantityByOne);

    int getQuantityByTwo();

    void setQuantityByTwo(int quantityByTwo);

    int getReturnQuantityByTwo();

    void setReturnQuantityByTwo(int returnQuantityByTwo);

    int getQuantityBySix();

    void setQuantityBySix(int quantityBySix);

    int getReturnQuantityBySix();

    void setReturnQuantityBySix(int returnQuantityBySix);
}
