package com.kindminds.drs.core.biz.product.onboarding;


import com.fasterxml.uuid.Generators;


import com.kindminds.drs.api.v2.biz.domain.model.product.Product;
import com.kindminds.drs.api.v2.biz.domain.model.product.onboarding.OnboardingApplicationLineitem;
import com.kindminds.drs.api.v2.biz.domain.model.product.onboarding.OnboardingApplicationLineitemStatusType;
import com.kindminds.drs.api.v2.biz.domain.model.product.onboarding.detail.*;
import com.kindminds.drs.core.biz.product.onboarding.detail.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class OnboardingApplicationLineitemImpl implements OnboardingApplicationLineitem {


    private String id;
    private OffsetDateTime createTime;

    private String onboardingApplicationId;
    private OffsetDateTime onboardingApplicationCreateTime;

    private Product product;
    private OnboardingApplicationLineitemStatusType status;
    private List<EvalProductInfoStatusType> evalProductInfoStatus;

    private Optional<TrialListing> trialList = Optional.empty();
    private Optional<EvalSample> evalSample = Optional.empty();
    private Optional<PresentSample> presentSample = Optional.empty();
    private Optional<ApproveSample> approveSample = Optional.empty();
    private Optional<GiveFeedback> giveFeedback = Optional.empty();
    private Optional<ProvideComment> provideComment = Optional.empty();
    private Optional<CheckInsurance> checkInsurance = Optional.empty();
    private Optional<ConfirmInsurance> confirmInsurance = Optional.empty();
    private Optional<CheckComplianceAndCertAvailability> checkComplianceAndCertAvailability = Optional.empty();
    private Optional<CheckProfitability> checkProfitability = Optional.empty();



    public static OnboardingApplicationLineitemImpl valueOf(String id , OffsetDateTime createTime ,
                                                            String onboardingApplicationId , OffsetDateTime onboardingApplicationCreateTime ,
                                                            Product product , OnboardingApplicationLineitemStatusType status ,
                                                            List<EvalProductInfoStatusType> evalProductInfoStatus){

        return new OnboardingApplicationLineitemImpl(id ,createTime,onboardingApplicationId,
                onboardingApplicationCreateTime , product ,status, evalProductInfoStatus);
    }

    private OnboardingApplicationLineitemImpl(String id , OffsetDateTime createTime ,
                                              String onboardingApplicationId , OffsetDateTime onboardingApplicationCreateTime ,
                                              Product product , OnboardingApplicationLineitemStatusType status ,
                                              List<EvalProductInfoStatusType> evalProductInfoStatus ){

        this.id = id ;
        this.createTime = createTime;
        this.onboardingApplicationId = onboardingApplicationId;
        this.onboardingApplicationCreateTime = onboardingApplicationCreateTime;
        this.product = product;
        this.status = status;
        this.evalProductInfoStatus = evalProductInfoStatus;



    }


    //migrated
    /*
    public OnboardingApplicationLineitemImpl(String onboardingApplicationId ,
                                              OffsetDateTime onboardingApplicationCreateTime,
                                              ProductDto product ,
                                             OnboardingApplicationLineitemStatusType status ) {


        this.id = Generators.randomBasedGenerator().generate().toString();
        this.status = status;
        this.onboardingApplicationId = onboardingApplicationId;
        this.onboardingApplicationCreateTime = onboardingApplicationCreateTime;
        this.product = product;

        this.createTime = OffsetDateTime.now();


    }*/


    public OnboardingApplicationLineitemImpl(String onboardingApplicationId ,
                                             OffsetDateTime onboardingApplicationCreateTime,
                                             Product product , OnboardingApplicationLineitemStatusType status ) {


        this.id = Generators.randomBasedGenerator().generate().toString();
        this.status = status;

        this.init(onboardingApplicationId, onboardingApplicationCreateTime,
                product);


    }

    public OnboardingApplicationLineitemImpl(String onboardingApplicationId ,
                                             OffsetDateTime onboardingApplicationCreateTime,
                                             Product product ) {


        this.id = Generators.randomBasedGenerator().generate().toString();
        this.init(onboardingApplicationId, onboardingApplicationCreateTime,
                product);


    }

    public OnboardingApplicationLineitemImpl(String id , String onboardingApplicationId ,
                                              OffsetDateTime onboardingApplicationCreateTime,
                                              Product product ) {


        this.id = id;

        this.init(onboardingApplicationId, onboardingApplicationCreateTime,
                product);

        //StatusType.PENDING_SUPPLIER_ACTION

    }



    private void init( String onboardingApplicationId , OffsetDateTime onboardingApplicationCreateTime,
                       Product product){


        this.createTime = OffsetDateTime.now();

        this.onboardingApplicationId = onboardingApplicationId;
        this.onboardingApplicationCreateTime = onboardingApplicationCreateTime;
        this.product = product;
        this.status = OnboardingApplicationLineitemStatusType.DRAFT;
    }

    @Override
    public OffsetDateTime getCreateTime() {
        return this.createTime;
    }

    @Override
    public String getOnboardingApplicationId() {
        return this.onboardingApplicationId;
    }


    @Override
    public String getId() {
        return this.id;
    }


    @Override
    public Product getProduct() {
        return this.product;
    }


    @Override
    public OnboardingApplicationLineitemStatusType getStatus() {
        return this.status;
    }

    @Override
    public Optional<TrialListing> getTrialList() {
        return this.trialList;
    }

    @Override
    public Optional<EvalSample> getEvalSample() {

        return this.evalSample;
    }

    @Override
    public Optional<GiveFeedback> getGiveFeedback() {
        return this.giveFeedback;
    }

    @Override
    public Optional<ProvideComment> getProvideComment() {
        return this.provideComment;
    }

    @Override
    public Optional<PresentSample> getPresentSample() {
        return this.presentSample;
    }

    @Override
    public Optional<ApproveSample> getApproveSample() {
        return this.approveSample;
    }

    @Override
    public Optional<CheckInsurance> getCheckInsurance() {
        return this.checkInsurance;
    }

    @Override
    public Optional<ConfirmInsurance> getConfirmInsurance() {
        return this.confirmInsurance;
    }

    @Override
    public Optional<CheckComplianceAndCertAvailability> getCheckComplianceAndCertAvailability() {
        return this.checkComplianceAndCertAvailability;
    }

    @Override
    public Optional<CheckProfitability> getCheckProfitability() {
        return this.checkProfitability;
    }


    @Override
    public List<EvalProductInfoStatusType> getEvalProductInfoStatus() {
        return this.evalProductInfoStatus;
    }

    @Override
    public void initialReviewing() {
        this.status = OnboardingApplicationLineitemStatusType.INITIAL_REVIEWING;

    }

    @Override
    public void approve() {
        this.status = OnboardingApplicationLineitemStatusType.APPROVED;
    }

    @Override
    public void reject() {
        this.status = OnboardingApplicationLineitemStatusType.REJECTED;
    }

    @Override
    public void doTrialList(String trialListJson , Boolean submit) {

        this.status = OnboardingApplicationLineitemStatusType.TRIAL_LISTED;
        this.createTime = OffsetDateTime.now();

        TrialListing tl = TrialListingImpl.valueOf(this.id , trialListJson,submit);
        this.trialList = Optional.of(tl);

    }

    @Override
    public void evaluateSample(String evalSampleJson , Boolean submit) {

        this.status = OnboardingApplicationLineitemStatusType.SAMPLE_EVALUATED;
        this.createTime = OffsetDateTime.now();

        this.evalSample = Optional.of(EvalSampleImpl.valueOf(this.id , evalSampleJson, submit));

    }

    @Override
    public void presentSample(String presentSampleJson , Boolean submit) {
        this.status = OnboardingApplicationLineitemStatusType.SAMPLE_PRESENTED;
        this.createTime = OffsetDateTime.now();

        this.presentSample = Optional.of(PresentSampleImpl.valueOf(this.id , presentSampleJson, submit));
    }

    @Override
    public void giveFeedback(String giveFeedbackJson , Boolean submit) {
        this.status = OnboardingApplicationLineitemStatusType.SAMPLE_FEEDBACK_BY_SC;
        this.createTime = OffsetDateTime.now();

        this.giveFeedback = Optional.of(GiveFeedbackImpl.valueOf(this.id , giveFeedbackJson, submit));
    }

    @Override
    public void provideComment(String provdieCommentJson , Boolean submit) {
        this.status = OnboardingApplicationLineitemStatusType.SAMPLE_COMMENT_BY_MS;
        this.createTime = OffsetDateTime.now();

        this.provideComment = Optional.of(ProvideCommentImpl.valueOf(this.id , provdieCommentJson, submit));
    }

    @Override
    public void approveSample(String approveSampleJson , Boolean submit) {
        this.status = OnboardingApplicationLineitemStatusType.SAMPLE_APPROVED;
        this.createTime = OffsetDateTime.now();

        this.approveSample = Optional.of(ApproveSampleImpl.valueOf(this.id , approveSampleJson, submit));
    }

    @Override
    public void acceptCompleteness() {
        this.status = OnboardingApplicationLineitemStatusType.APPLICATION_INFO_COMPLETED;
        this.createTime = OffsetDateTime.now();
    }

    @Override
    public void rejectCompleteness() {
        this.status = OnboardingApplicationLineitemStatusType.APPLICATION_INFO_RETURNED;
        this.createTime = OffsetDateTime.now();
    }

    @Override
    public void completeProductDetail() {
        this.status = OnboardingApplicationLineitemStatusType.APPLICATION_INFO_RESUBMITTED;
        this.createTime = OffsetDateTime.now();
    }

    @Override
    public void confirmInsurance(String confirmInsuranceJson , Boolean submit) {

        if(this.evalProductInfoStatus == null)
            this.evalProductInfoStatus = new ArrayList<EvalProductInfoStatusType>();

        this.evalProductInfoStatus.add(EvalProductInfoStatusType.INSURANCE_PROVIDER_CONFIRMED);
        this.createTime = OffsetDateTime.now();

        this.confirmInsurance = Optional.of(ConfirmInsuranceImpl.valueOf(this.id , confirmInsuranceJson , submit));

        this.checkEvalProductInfoStatus();

    }

    @Override
    public void checkInsurance(String checkInsuranceJson , Boolean submit) {
        if(this.evalProductInfoStatus == null)
            this.evalProductInfoStatus = new ArrayList<EvalProductInfoStatusType>();

        this.evalProductInfoStatus.add(EvalProductInfoStatusType.INSURANCE_CHECKED);
        this.createTime = OffsetDateTime.now();

        this.checkInsurance = Optional.of(CheckInsuranceImpl.valueOf(this.id , checkInsuranceJson , submit));

        this.checkEvalProductInfoStatus();

    }

    @Override
    public void checkComplianceAndCertAvailability(String checkCpCaJson , Boolean submit) {
        if(this.evalProductInfoStatus == null)
            this.evalProductInfoStatus = new ArrayList<EvalProductInfoStatusType>();

        this.evalProductInfoStatus.add(EvalProductInfoStatusType.COMPLIANCE_CHECKED);
        this.createTime = OffsetDateTime.now();

        this.checkComplianceAndCertAvailability = Optional.of(CheckComplianceAndCertAvailabilityImpl.valueOf(this.id,
                checkCpCaJson,submit));

        this.checkEvalProductInfoStatus();
    }

    @Override
    public void checkReverseLogistics() {
        if(this.evalProductInfoStatus == null)
            this.evalProductInfoStatus = new ArrayList<EvalProductInfoStatusType>();

        this.evalProductInfoStatus.add(EvalProductInfoStatusType.REVERSE_LOGISTICS_CHECKED);
        this.createTime = OffsetDateTime.now();

        this.checkEvalProductInfoStatus();
    }

    @Override
    public void checkProfitability(String checkProfitabilityJson , Boolean submit) {
        if(this.evalProductInfoStatus == null)
            this.evalProductInfoStatus = new ArrayList<EvalProductInfoStatusType>();

        this.evalProductInfoStatus.add(EvalProductInfoStatusType.PROFITABILITY_CHECKED);
        this.createTime = OffsetDateTime.now();

        this.checkProfitability = Optional.of(CheckProfitabilityImpl.valueOf(this.id,checkProfitabilityJson,submit));

        this.checkEvalProductInfoStatus();

    }

    @Override
    public void checkMarketability() {
        if(this.evalProductInfoStatus == null)
            this.evalProductInfoStatus = new ArrayList<EvalProductInfoStatusType>();

        this.evalProductInfoStatus.add(EvalProductInfoStatusType.MARKETABILITY_CHECKED);
        this.createTime = OffsetDateTime.now();

        this.checkEvalProductInfoStatus();
    }

    @Override
    public void evaluatePackageContentAndManual() {
        if(this.evalProductInfoStatus == null)
            this.evalProductInfoStatus = new ArrayList<EvalProductInfoStatusType>();

        this.evalProductInfoStatus.add(EvalProductInfoStatusType.PACKAGE_CONTENT_EVALUATED);
        this.createTime = OffsetDateTime.now();

        this.checkEvalProductInfoStatus();
    }

    @Override
    public void evaluateProductInfo() {

        this.status = OnboardingApplicationLineitemStatusType.PRODUCT_INFO_EVALUATING;
        this.createTime = OffsetDateTime.now();

    }

    private void checkEvalProductInfoStatus() {

        Optional<Integer> total = this.evalProductInfoStatus.stream().
                map(x -> x.getKey()).
                collect(toList()).stream().reduce((x,y) -> x+y);

        if(total.isPresent()){
            if(total.get() == 28 ){
                this.finishEvaluatingProductInfo();
            }
        }

    }

    private void finishEvaluatingProductInfo() {
        this.status = OnboardingApplicationLineitemStatusType.PRODUCT_INFO_EVALUATED;
        this.createTime = OffsetDateTime.now();
    }



    @Override
    public OffsetDateTime getOnboardingApplicationCreateTime() {
        return this.onboardingApplicationCreateTime;
    }

}
