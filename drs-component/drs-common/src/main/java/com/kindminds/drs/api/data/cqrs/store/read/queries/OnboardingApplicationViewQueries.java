package com.kindminds.drs.api.data.cqrs.store.read.queries;



import com.kindminds.drs.api.data.transfer.productV2.onboarding.OnboardingApplication;

import java.util.List;
import java.util.Optional;

public interface OnboardingApplicationViewQueries {


    Optional<String> getSerialNumber(String productBaseCode);

    Optional<List<String>> getSerialNumbersBySupplier(String companyKcode);

    List<OnboardingApplication> getOnboardingApplications(String kcode);

    List<OnboardingApplication> getOnboardingApplications();



}
