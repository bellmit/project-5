package com.kindminds.drs.api.data.cqrs.store.event.queries;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface OnboardingApplicationQueries {

    Optional<String> getId(String productBaseCode);


    public Integer getNextSeq(String supplierKcode);


}
