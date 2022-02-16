package com.kindminds.drs.api.data.access.nosql.mongo.productmongo;

import java.util.List;

public interface Sku {

    Integer getPageIndex();

    void setPageIndex(Integer pageIndex);

    String getRetailPrice();

    void setRetailPrice(String retailPrice);

    Integer getsettlementsPeriodOrder();

    void setsettlementsPeriodOrder(Integer settlementsPeriodOrder);

    Integer getlastSevenDaysOrder();

    void setlastSevenDaysOrder(Integer lastSevenDaysOrder);

    Integer getFbaQuantity();

    void setFbaQuantity(Integer fbaQuantity);

    String getStatus();

    void setStatus(String status);

    List<String> getActions();

    void setActions(List<String> actions);

    String getSellerSku();

    void setSellerSku(String sellerSku);

    ProductId getProductId();

    void setProductId(ProductId productId);

    ProductIdType getProductIdType();

    void setProductIdType(ProductIdType productIdType);

    Variable getVariable();

    void setVariable(Variable variable);

}
