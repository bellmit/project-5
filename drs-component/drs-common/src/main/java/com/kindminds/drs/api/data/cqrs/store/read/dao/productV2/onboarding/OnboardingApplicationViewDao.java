package com.kindminds.drs.api.data.cqrs.store.read.dao.productV2.onboarding;

import com.kindminds.drs.api.v2.biz.domain.model.product.onboarding.OnboardingApplication;

public interface OnboardingApplicationViewDao {

    String saveView(OnboardingApplication onboardingApplication);

    String deleteView(String onboardingApplicationId);
}
