package com.kindminds.drs.persist.data.access.nosql.mongo.dto.product;

//import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

public class Sku {

//    @Field(name="sellerSku")
    private String sellerSku;

    //    @Field(name="productId")
    private ProductId productId;

    //    @Field(name="productIdType")
    private ProductIdType productIdType;

    //    @Field(name="variable")
    private VariableSku variable;

    private VariationThemeSku variationTheme;

//    @Field(name="page_index")
    private Integer pageIndex;

//    @Field(name="retail_price")
    private String retailPrice;

//    @Field(name="sales_volume")
    private Integer settlementsPeriodOrder;

//    @Field(name="open_position")
    private Integer lastSevenDaysOrder;

//    @Field(name="fba_quantity")
    private Integer fbaQuantity;

    private String status;

    private List<String> actions;

    private Boolean editable;

    private Boolean isSelected;

//    @Field(name="hscode")
//    private Hscode hscode;



    public Sku() {}

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public String getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(String retailPrice) {
        this.retailPrice = retailPrice;
    }

    public Integer getsettlementsPeriodOrder() {
        return settlementsPeriodOrder;
    }

    public void setsettlementsPeriodOrder(Integer settlementsPeriodOrder) {
        this.settlementsPeriodOrder = settlementsPeriodOrder;
    }

    public Integer getlastSevenDaysOrder() {
        return lastSevenDaysOrder;
    }

    public void setlastSevenDaysOrder(Integer lastSevenDaysOrder) {
        this.lastSevenDaysOrder = lastSevenDaysOrder;
    }

    public Integer getFbaQuantity() {
        return fbaQuantity;
    }

    public void setFbaQuantity(Integer fbaQuantity) {
        this.fbaQuantity = fbaQuantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getActions() {
        return actions;
    }

    public void setActions(List<String> actions) {
        this.actions = actions;
    }

    public String getSellerSku() {
        return sellerSku;
    }

    public void setSellerSku(String sellerSku) {
        this.sellerSku = sellerSku;
    }

    public ProductId getProductId() {
        return productId;
    }

    public void setProductId(ProductId productId) {
        this.productId = productId;
    }

    public ProductIdType getProductIdType() {
        return productIdType;
    }

    public void setProductIdType(ProductIdType productIdType) {
        this.productIdType = productIdType;
    }

    public VariableSku getVariable() {
        return variable;
    }

    public void setVariable(VariableSku variable) {
        this.variable = variable;
    }

    public VariationThemeSku getVariationTheme() {
        return variationTheme;
    }

    public void setVariationTheme(VariationThemeSku variationTheme) {
        this.variationTheme = variationTheme;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public Boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }


    @Override
    public String toString() {
        return "Sku{" +
                "sellerSku='" + sellerSku + '\'' +
                ", productId=" + productId +
                ", productIdType=" + productIdType +
                ", variable=" + variable +
                ", variationTheme=" + variationTheme +
                ", pageIndex=" + pageIndex +
                ", retailPrice='" + retailPrice + '\'' +
                ", settlementsPeriodOrder=" + settlementsPeriodOrder +
                ", lastSevenDaysOrder=" + lastSevenDaysOrder +
                ", fbaQuantity=" + fbaQuantity +
                ", status='" + status + '\'' +
                ", actions=" + actions +
                ", editable=" + editable +
                ", isSelected=" + isSelected +
                '}';
    }
}

