package com.kindminds.drs.api.data.cqrs.store.event.queries;

import com.kindminds.drs.Country;

import java.util.List;
import java.util.Optional;

public interface OnboardingApplicationLineitemQueries {


    Optional<String> getId(String onboardingApplicationId, Country marketSide);

    Optional<List<String>> getIds(String onboardingApplicationId);
}
