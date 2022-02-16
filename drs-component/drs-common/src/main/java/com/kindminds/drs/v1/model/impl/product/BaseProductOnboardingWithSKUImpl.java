package com.kindminds.drs.v1.model.impl.product;


import com.kindminds.drs.api.v1.model.product.BaseProductOnboardingWithSKU;

import java.util.ArrayList;
import java.util.List;

public class BaseProductOnboardingWithSKUImpl implements BaseProductOnboardingWithSKU {

    private String productBaseCode;
    private String supplierKcode;
    private String status;
    private String variationType1;
    private String variationType2;
    private String productWithVariation;
    private List<BaseProductOnboardingSKUItemImpl> SKULineItems = new ArrayList<BaseProductOnboardingSKUItemImpl>();;

    @Override
    public String getProductBaseCode() {
        return this.productBaseCode;
    }

    public void setProductBaseCode(String productBaseCode) {
        this.productBaseCode = productBaseCode;
    }

    @Override
    public String getSupplierKcode() {
        return this.supplierKcode;
    }

    public void setSupplierKcode(String supplierKcode) {
        this.supplierKcode = supplierKcode;
    }

    @Override
    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String getVariationType1() {
        return this.variationType1;
    }

    public void setVariationType1(String variationType1) {
        this.variationType1 = variationType1;
    }

    @Override
    public String getVariationType2() {
        return this.variationType2;
    }

    public void setVariationType2(String variationType2) {
        this.variationType2 = variationType2;
    }

    @Override
    public String getProductWithVariation() {
        return this.productWithVariation;
    }

    public void setProductWithVariation(String productWithVariation) {
        this.productWithVariation = productWithVariation;
    }

    @Override
    public List<BaseProductOnboardingSKUItem> getSKULineItems() {
        List<BaseProductOnboardingSKUItem> items = new ArrayList<BaseProductOnboardingSKUItem>();
        for (BaseProductOnboardingSKUItem item : this.SKULineItems) {
            items.add(item);
        }
        return items;
    }

    public List<BaseProductOnboardingSKUItemImpl> getSKULineItem(){
        return this.SKULineItems;
    }

    public void setSKULineItem(List<BaseProductOnboardingSKUItemImpl> lineItem){
        this.SKULineItems = lineItem;
    }

}
