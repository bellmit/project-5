package com.kindminds.drs.core.biz.product.onboarding.detail;



import com.kindminds.drs.api.v2.biz.domain.model.product.onboarding.detail.CheckProfitability;

import java.time.OffsetDateTime;

public class CheckProfitabilityImpl extends  AbstractOnboardingApplicationLineitemDetail implements CheckProfitability {

    public static CheckProfitabilityImpl valueOf(String onboardingApplicationLineitemId ,
                                                 String data , Boolean submitted  ){

        return new CheckProfitabilityImpl( onboardingApplicationLineitemId  ,
                data ,  submitted  );
    }

    private CheckProfitabilityImpl(String onboardingApplicationLineitemId ,
                               String data , Boolean submitted  ){

        super(onboardingApplicationLineitemId  ,
                data ,  submitted );


    }

    public static CheckProfitabilityImpl valueOf(String onboardingApplicationLineitemId , OffsetDateTime createTime ,
                                                 String data , Boolean submitted  ){

        return new CheckProfitabilityImpl( onboardingApplicationLineitemId ,  createTime ,
                data ,  submitted  );
    }

    private CheckProfitabilityImpl(String onboardingApplicationLineitemId , OffsetDateTime createTime ,
                               String data , Boolean submitted  ){

        super(onboardingApplicationLineitemId  ,createTime , data ,  submitted );


    }

}
