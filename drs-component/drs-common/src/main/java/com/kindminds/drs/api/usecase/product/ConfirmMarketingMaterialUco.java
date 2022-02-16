package com.kindminds.drs.api.usecase.product;

import com.kindminds.drs.api.v1.model.product.ConfirmMarketingMaterialComment;

import java.util.List;

public interface ConfirmMarketingMaterialUco {

    void autoRenotifySuppliers();

    void sendEmail(int emailType, String marketplace, String productBaseCode,
                   String productCodeName, String supplierKCode);

    void addComment(int userId, String marketplace, String productBaseCode,
                    String supplierKcode, String contents,
                    String baseCodeAndName);

    List<ConfirmMarketingMaterialComment> getComments(
            int marketplaceId, String productBaseCode);

}
