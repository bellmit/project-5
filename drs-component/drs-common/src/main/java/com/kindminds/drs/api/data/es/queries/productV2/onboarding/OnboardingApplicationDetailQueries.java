package com.kindminds.drs.api.data.es.queries.productV2.onboarding;

import java.util.Optional;

public interface OnboardingApplicationDetailQueries {

    Optional<String> getTrialList(String onboardingApplicationLineitemId);

    Optional<String> getEvalSample(String onboardingApplicationLineitemId);

    Optional<String> getPresentSample(String onboardingApplicationLineitemId);

    Optional<String> getApproveSample(String onboardingApplicationLineitemId);

    Optional<String> getProvideComment(String onboardingApplicationLineitemId);

    Optional<String> getGiveFeedback(String onboardingApplicationLineitemId);

    Optional<String> getCheckInsurance(String onboardingApplicationLineitemId);

    Optional<String> getCheckProfitability(String onboardingApplicationLineitemId);

    Optional<String> getCheckCompliance(String onboardingApplicationLineitemId);


}
