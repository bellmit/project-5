package com.kindminds.drs.api.data.cqrs.store.read.queries;






import com.kindminds.drs.api.data.transfer.productV2.ProductDetail;
import com.kindminds.drs.api.data.transfer.productV2.ProductDto;

import java.util.List;
import java.util.Map;


public interface ProductViewQueries {

    ProductDto getActivateBaseProductOnboarding(String productBaseCode) ;

    ProductDto getInActivateBaseProductOnboarding(String productBaseCode) ;

    ProductDetail getProductInfoMarketSide(String supplierKcode, String productBaseCode, String country);

    List<ProductDetail> getActivateProductInfoMarketSide(String productBaseCode) ;

    List<ProductDetail> getInactivateProductInfoMarketSide(String productBaseCode) ;

    List<ProductDto> getBaseProductOnboardingList(int startIndex, int size);

    List<ProductDto> getBaseProductOnboardingList(int startIndex, int size, String companyKcode);

    int getBaseProductOnboardingTotalSize();

    int getBaseProductOnboardingTotalSize(String companyKcode);

    String getSupplierKcodeOfBaseProductOnboarding(String productBaseCode);

    Map<String, List<String>> getBaseCodesToMarketplacesMap(String companyCode);

}
