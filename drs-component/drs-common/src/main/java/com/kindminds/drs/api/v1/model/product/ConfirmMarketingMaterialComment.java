package com.kindminds.drs.api.v1.model.product;

public interface ConfirmMarketingMaterialComment {
    Integer getMaterialMarketsideId();
    Integer getMarketplaceId();
    String getProductBaseCode();
    Integer getLineSeq();
    String getDateCreated();
    String getCreatorId();
    String getCreatorName();
    String getContents();
    String toString();

}
