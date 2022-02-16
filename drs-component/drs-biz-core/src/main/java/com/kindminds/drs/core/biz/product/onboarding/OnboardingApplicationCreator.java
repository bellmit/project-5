package com.kindminds.drs.core.biz.product.onboarding;


import com.kindminds.drs.api.v2.biz.domain.model.product.Product;
import com.kindminds.drs.enums.OnboardingApplicationStatusType;
import org.springframework.util.StringUtils;

import java.util.List;

public class OnboardingApplicationCreator {


    public static OnboardingApplicationImpl create(String supplierKcode,
                                                                        String serialNumber,
                                                                        List<Product> products) {

        return new OnboardingApplicationImpl(supplierKcode, serialNumber, products,
                StringUtils.hasText(serialNumber) ? OnboardingApplicationStatusType.DRAFT :
                        OnboardingApplicationStatusType.NO_SERIAL_NUM, false);
    }


    public static OnboardingApplicationImpl create(String supplierKcode,
                                                                        String serialNumber,
                                                                        Product product,
                                                                        OnboardingApplicationStatusType status) {

        return new OnboardingApplicationImpl(supplierKcode, serialNumber, product, status, false);
    }








}
