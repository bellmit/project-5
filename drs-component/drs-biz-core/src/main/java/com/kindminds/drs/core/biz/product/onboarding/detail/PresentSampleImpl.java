package com.kindminds.drs.core.biz.product.onboarding.detail;



import com.kindminds.drs.api.v2.biz.domain.model.product.onboarding.detail.PresentSample;

import java.time.OffsetDateTime;

public class PresentSampleImpl  extends  AbstractOnboardingApplicationLineitemDetail
        implements PresentSample {

    public static PresentSampleImpl valueOf(String onboardingApplicationLineitemId ,
                                            String data , Boolean submitted  ){

        return new PresentSampleImpl( onboardingApplicationLineitemId  ,
                data ,  submitted  );
    }

    private PresentSampleImpl(String onboardingApplicationLineitemId ,
                             String data , Boolean submitted  ){

        super(onboardingApplicationLineitemId  ,
                data ,  submitted );


    }

    public static PresentSampleImpl valueOf(String onboardingApplicationLineitemId , OffsetDateTime createTime ,
                                            String data , Boolean submitted  ){

        return new PresentSampleImpl( onboardingApplicationLineitemId ,  createTime ,
                data ,  submitted  );
    }

    private PresentSampleImpl(String onboardingApplicationLineitemId , OffsetDateTime createTime ,
                             String data , Boolean submitted  ){

        super(onboardingApplicationLineitemId  ,createTime , data ,  submitted );


    }


}
