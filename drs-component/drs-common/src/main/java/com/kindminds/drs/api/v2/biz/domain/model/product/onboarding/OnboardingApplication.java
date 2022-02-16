package com.kindminds.drs.api.v2.biz.domain.model.product.onboarding;



import com.kindminds.drs.enums.OnboardingApplicationStatusType;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;


public interface OnboardingApplication {

    String getId();

    String getSerialNumber();

    OffsetDateTime getCreateTime();

    Integer getSupplierCompanyId();

    String getSupplierKcode();

    OnboardingApplicationStatusType getStatus();

    List<OnboardingApplicationLineitem> getLineitems();

    BigDecimal getServiceFee();

    String getQuotationNumber();

    void submit();

    void accept();

    void startQuoteRequest();

    void completeQuoteRequest();

    void switchToReviewLineitems();

    void reviewFinalFeasibility();

    void approveProductFeasibility();

    void rejectProductFeasibility();

}
