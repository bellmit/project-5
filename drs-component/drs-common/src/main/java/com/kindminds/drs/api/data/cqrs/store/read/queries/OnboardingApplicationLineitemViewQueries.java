package com.kindminds.drs.api.data.cqrs.store.read.queries;




import com.kindminds.drs.data.transfer.productV2.onboarding.OnboardingApplicationLineitem;

import java.util.Optional;

public interface OnboardingApplicationLineitemViewQueries {

    Optional<OnboardingApplicationLineitem> getOnboardingApplicationLineitem(String onboardingApplicationLineitemId);
}
