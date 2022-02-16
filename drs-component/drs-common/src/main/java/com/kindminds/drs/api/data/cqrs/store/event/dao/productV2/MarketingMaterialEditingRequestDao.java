package com.kindminds.drs.api.data.cqrs.store.event.dao.productV2;

import com.kindminds.drs.api.v2.biz.domain.model.product.MarketingMaterialEditingRequest;

import java.util.List;
import java.util.Optional;

public interface MarketingMaterialEditingRequestDao {

    List<Object []> find(String productMarketingMaterialId);

    Optional<String> findId(String productMarketingMaterialId);

    void insert(MarketingMaterialEditingRequest marketingMaterialEditingRequest);

}
