package com.kindminds.drs.api.v2.biz.domain.model.product.onboarding.detail;

import java.time.OffsetDateTime;

public interface OnboardingApplicationLineitemDetail {

    String getOnboardingApplicationLineitemId();

    OffsetDateTime getCreateTime();

    String getData();

    Boolean isSubmitted();
}
