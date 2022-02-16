package com.kindminds.drs.api.v2.biz.domain.model.product.onboarding;


import com.kindminds.drs.api.v2.biz.domain.model.product.Product;
import com.kindminds.drs.api.v2.biz.domain.model.product.onboarding.detail.*;


import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface OnboardingApplicationLineitem {

    String getId();

    OffsetDateTime getCreateTime();

    String getOnboardingApplicationId();

    OffsetDateTime getOnboardingApplicationCreateTime();

    Product getProduct();

    OnboardingApplicationLineitemStatusType getStatus();

    Optional<TrialListing> getTrialList();

    Optional<EvalSample> getEvalSample();

    Optional<GiveFeedback> getGiveFeedback();

    Optional<ProvideComment> getProvideComment();

    Optional<PresentSample> getPresentSample();

    Optional<ApproveSample> getApproveSample();

    Optional<CheckInsurance> getCheckInsurance();

    Optional<ConfirmInsurance> getConfirmInsurance();

    Optional<CheckComplianceAndCertAvailability> getCheckComplianceAndCertAvailability();

    Optional<CheckProfitability> getCheckProfitability();

    List<EvalProductInfoStatusType> getEvalProductInfoStatus();

    void initialReviewing();

    void approve();

    void reject();

    void doTrialList(String trialListJson , Boolean submit);

    void evaluateSample(String evalSampleJson , Boolean submit);

    void presentSample(String presentSampleJson , Boolean submit);

    void giveFeedback(String giveFeedbackJson , Boolean submit);

    void provideComment(String provideCommentJson , Boolean submit);

    void approveSample(String approveSampleJson , Boolean submit);

    void acceptCompleteness();

    void rejectCompleteness();

    void completeProductDetail();

    void confirmInsurance(String confirmInsuranceJson , Boolean submit);

    void checkInsurance(String checkInsuranceJson , Boolean submit);

    void checkComplianceAndCertAvailability(String checkCpCaJson , Boolean submit);

    void checkReverseLogistics();

    void checkProfitability(String checkProfitability , Boolean submit);

    void checkMarketability();

    void evaluatePackageContentAndManual();

    void evaluateProductInfo();




}
