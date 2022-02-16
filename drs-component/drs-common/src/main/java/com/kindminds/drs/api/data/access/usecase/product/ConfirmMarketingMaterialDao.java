package com.kindminds.drs.api.data.access.usecase.product;

import com.kindminds.drs.api.v1.model.product.ConfirmMarketingMaterialComment;

import java.util.List;

public interface ConfirmMarketingMaterialDao {
    public List<String> querySupplierEmailsByBaseCode(
            String productBaseCode);

    Boolean marketingMaterialMarketsideExists(
            int marketplaceId, String productBaseCode);

    void insertMarketingMaterialMarketside(
            int marketplaceId, String productBaseCode,
            String productCodeName, String supplierKCode);

    Integer queryMarketingMaterialMarketsideId(
            int marketplaceId, String productBaseCode);

    Integer queryDrsStaffId(int marketplaceId, String productBaseCode);

    List<Object[]> queryProductBaseListToRenotify();

    void updateDrsStaffId(int drsStaffId,
                          int marketplaceId, String productBaseCode);

    void updateMarketingMaterialStatus(
            String status, Boolean renotify,
            int marketplaceId, String productBaseCode);


    List<ConfirmMarketingMaterialComment> queryCommentByProductBaseCode(
            int marketplaceId, String productBaseCode);



    String insertComment(
            int materialMarketsideId, int marketplaceId,
            String productBaseCode, int creatorId,
            String contents);

}
