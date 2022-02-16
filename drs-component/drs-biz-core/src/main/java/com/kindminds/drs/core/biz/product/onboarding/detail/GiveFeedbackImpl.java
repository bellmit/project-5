package com.kindminds.drs.core.biz.product.onboarding.detail;



import com.kindminds.drs.api.v2.biz.domain.model.product.onboarding.detail.GiveFeedback;

import java.time.OffsetDateTime;

public class GiveFeedbackImpl extends AbstractOnboardingApplicationLineitemDetail
        implements GiveFeedback {

    public static GiveFeedbackImpl valueOf(String onboardingApplicationLineitemId ,
                                           String data , Boolean submitted  ){

        return new GiveFeedbackImpl( onboardingApplicationLineitemId  ,
                data ,  submitted  );
    }

    private GiveFeedbackImpl(String onboardingApplicationLineitemId ,
                              String data , Boolean submitted  ){

        super(onboardingApplicationLineitemId  ,
                data ,  submitted );


    }

    public static GiveFeedbackImpl valueOf(String onboardingApplicationLineitemId , OffsetDateTime createTime ,
                                           String data , Boolean submitted  ){

        return new GiveFeedbackImpl( onboardingApplicationLineitemId ,  createTime ,
                data ,  submitted  );
    }

    private GiveFeedbackImpl(String onboardingApplicationLineitemId , OffsetDateTime createTime ,
                              String data , Boolean submitted  ){

        super(onboardingApplicationLineitemId  ,createTime , data ,  submitted );


    }
}
