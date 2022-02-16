package com.kindminds.drs.api.data.cqrs.store.event.dao.productV2.onboarding;

import com.kindminds.drs.api.v2.biz.domain.model.product.onboarding.OnboardingApplication;

import java.util.List;

public interface OnboardingApplicationDao {

    String save(OnboardingApplication onboardingApplication);

    List<Object[]> findOnboardingApplicationById(String id);


}
