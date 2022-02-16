package com.kindminds.drs.core.biz.product.onboarding.detail;



import com.kindminds.drs.api.v2.biz.domain.model.product.onboarding.detail.ApproveSample;

import java.time.OffsetDateTime;

public class ApproveSampleImpl extends  AbstractOnboardingApplicationLineitemDetail
        implements ApproveSample {

    public static ApproveSampleImpl valueOf(String onboardingApplicationLineitemId ,
                                            String data , Boolean submitted  ){

        return new ApproveSampleImpl( onboardingApplicationLineitemId  ,
                data ,  submitted  );
    }

    private ApproveSampleImpl(String onboardingApplicationLineitemId ,
                           String data , Boolean submitted  ){

        super(onboardingApplicationLineitemId  ,
                data ,  submitted );


    }

    public static ApproveSampleImpl valueOf(String onboardingApplicationLineitemId , OffsetDateTime createTime ,
                                            String data , Boolean submitted  ){

        return new ApproveSampleImpl( onboardingApplicationLineitemId ,  createTime ,
                data ,  submitted  );
    }

    private ApproveSampleImpl(String onboardingApplicationLineitemId , OffsetDateTime createTime ,
                           String data , Boolean submitted  ){

        super(onboardingApplicationLineitemId  ,createTime , data ,  submitted );


    }
}
