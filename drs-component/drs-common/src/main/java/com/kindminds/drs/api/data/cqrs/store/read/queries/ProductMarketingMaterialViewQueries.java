package com.kindminds.drs.api.data.cqrs.store.read.queries;

import com.kindminds.drs.Country;
import com.kindminds.drs.api.data.transfer.productV2.ProductDetail;


public interface ProductMarketingMaterialViewQueries {


    ProductDetail findProductMarketingMaterial(String productBaseCode,
                                               String productVariationCode, Country country);

}
