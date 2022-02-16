package com.kindminds.drs.api.data.cqrs.store.event.dao.productV2.onboarding;

import com.kindminds.drs.api.v2.biz.domain.model.product.onboarding.OnboardingApplicationLineitem;

import java.util.List;

public interface OnboardingApplicationLineitemDao {


    void save(OnboardingApplicationLineitem onboardingApplicationLineitem);

    List<Object[]> findOnboardingApplicationLineitem(String onboardingApplicationLineitemId);


}
