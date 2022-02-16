package com.kindminds.drs.core.biz.product.onboarding.detail;



import com.kindminds.drs.api.v2.biz.domain.model.product.onboarding.detail.CheckInsurance;

import java.time.OffsetDateTime;

public class CheckInsuranceImpl extends  AbstractOnboardingApplicationLineitemDetail implements CheckInsurance {

    public static CheckInsuranceImpl valueOf(String onboardingApplicationLineitemId ,
                                             String data , Boolean submitted  ){

        return new CheckInsuranceImpl( onboardingApplicationLineitemId  ,
                data ,  submitted  );
    }

    private CheckInsuranceImpl(String onboardingApplicationLineitemId ,
                                                   String data , Boolean submitted  ){

        super(onboardingApplicationLineitemId  ,
                data ,  submitted );


    }

    public static CheckInsuranceImpl valueOf(String onboardingApplicationLineitemId , OffsetDateTime createTime ,
                                             String data , Boolean submitted  ){

        return new CheckInsuranceImpl( onboardingApplicationLineitemId ,  createTime ,
                data ,  submitted  );
    }

    private CheckInsuranceImpl(String onboardingApplicationLineitemId , OffsetDateTime createTime ,
                                                   String data , Boolean submitted  ){

        super(onboardingApplicationLineitemId  ,createTime , data ,  submitted );


    }


}
