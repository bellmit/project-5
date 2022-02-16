package com.kindminds.drs.core.biz.product.onboarding.detail;



import com.kindminds.drs.api.v2.biz.domain.model.product.onboarding.detail.ConfirmInsurance;

import java.time.OffsetDateTime;

public class ConfirmInsuranceImpl extends  AbstractOnboardingApplicationLineitemDetail implements ConfirmInsurance {

    public static ConfirmInsuranceImpl valueOf(String onboardingApplicationLineitemId ,
                                               String data , Boolean submitted  ){

        return new ConfirmInsuranceImpl( onboardingApplicationLineitemId  ,
                data ,  submitted  );
    }

    private ConfirmInsuranceImpl(String onboardingApplicationLineitemId ,
                                   String data , Boolean submitted  ){

        super(onboardingApplicationLineitemId  ,
                data ,  submitted );


    }

    public static ConfirmInsuranceImpl valueOf(String onboardingApplicationLineitemId , OffsetDateTime createTime ,
                                               String data , Boolean submitted  ){

        return new ConfirmInsuranceImpl( onboardingApplicationLineitemId ,  createTime ,
                data ,  submitted  );
    }

    private ConfirmInsuranceImpl(String onboardingApplicationLineitemId , OffsetDateTime createTime ,
                                   String data , Boolean submitted  ){

        super(onboardingApplicationLineitemId  ,createTime , data ,  submitted );


    }

}
