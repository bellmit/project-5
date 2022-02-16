package com.kindminds.drs.api.data.cqrs.store.read.dao.productV2.onboarding;

import com.kindminds.drs.api.v2.biz.domain.model.product.onboarding.OnboardingApplicationLineitem;

public interface OnboardingApplicationLineitemViewDao {

    void saveView(OnboardingApplicationLineitem onboardingApplicationLineitem);

    void deleteView(String onboardingApplicationLineitemId);

}
