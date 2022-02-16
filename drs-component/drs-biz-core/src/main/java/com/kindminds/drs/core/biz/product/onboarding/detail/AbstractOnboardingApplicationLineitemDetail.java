package com.kindminds.drs.core.biz.product.onboarding.detail;



import com.kindminds.drs.api.v2.biz.domain.model.product.onboarding.detail.OnboardingApplicationLineitemDetail;

import java.time.OffsetDateTime;

public abstract  class AbstractOnboardingApplicationLineitemDetail implements OnboardingApplicationLineitemDetail {

    private String onboardingApplicationLineitemId;
    private OffsetDateTime createTime;
    private String data;
    private Boolean submitted;


    protected AbstractOnboardingApplicationLineitemDetail(String onboardingApplicationLineitemId ,
                             String data , Boolean submitted  ){

        this.onboardingApplicationLineitemId = onboardingApplicationLineitemId;
        this.createTime = OffsetDateTime.now();
        this.data = data;
        this.submitted = submitted;


    }


    protected AbstractOnboardingApplicationLineitemDetail(String onboardingApplicationLineitemId , OffsetDateTime createTime ,
                             String data , Boolean submitted  ){

        this.onboardingApplicationLineitemId = onboardingApplicationLineitemId;
        this.createTime = createTime;
        this.data = data;
        this.submitted = submitted;


    }


    @Override
    public String getOnboardingApplicationLineitemId() {
        return this.onboardingApplicationLineitemId;
    }

    @Override
    public OffsetDateTime getCreateTime() {
        return this.createTime;
    }

    @Override
    public String getData() {
        return this.data;
    }

    @Override
    public Boolean isSubmitted() {
        return this.submitted;
    }



}
