package com.kindminds.drs.core.biz.product.onboarding.detail;



import com.kindminds.drs.api.v2.biz.domain.model.product.onboarding.detail.EvalSample;

import java.time.OffsetDateTime;

public class EvalSampleImpl extends  AbstractOnboardingApplicationLineitemDetail implements EvalSample {

    public static EvalSampleImpl valueOf(String onboardingApplicationLineitemId ,
                                         String data , Boolean submitted  ){

        return new EvalSampleImpl( onboardingApplicationLineitemId  ,
                data ,  submitted  );
    }

    private EvalSampleImpl(String onboardingApplicationLineitemId ,
                             String data , Boolean submitted  ){

        super(onboardingApplicationLineitemId  ,
                data ,  submitted );


    }

    public static EvalSampleImpl valueOf(String onboardingApplicationLineitemId , OffsetDateTime createTime ,
                                         String data , Boolean submitted  ){

        return new EvalSampleImpl( onboardingApplicationLineitemId ,  createTime ,
                data ,  submitted  );
    }

    private EvalSampleImpl(String onboardingApplicationLineitemId , OffsetDateTime createTime ,
                             String data , Boolean submitted  ){

        super(onboardingApplicationLineitemId  ,createTime , data ,  submitted );


    }

}
