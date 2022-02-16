package com.kindminds.drs.core.biz.product.onboarding.detail;



import com.kindminds.drs.api.v2.biz.domain.model.product.onboarding.detail.CheckComplianceAndCertAvailability;

import java.time.OffsetDateTime;

public class CheckComplianceAndCertAvailabilityImpl extends AbstractOnboardingApplicationLineitemDetail
        implements CheckComplianceAndCertAvailability {

    public static CheckComplianceAndCertAvailabilityImpl valueOf(String onboardingApplicationLineitemId ,
                                                                 String data , Boolean submitted  ){

        return new CheckComplianceAndCertAvailabilityImpl( onboardingApplicationLineitemId  ,
                data ,  submitted  );
    }

    private CheckComplianceAndCertAvailabilityImpl(String onboardingApplicationLineitemId ,
                           String data , Boolean submitted  ){

        super(onboardingApplicationLineitemId  ,
                data ,  submitted );


    }

    public static CheckComplianceAndCertAvailabilityImpl valueOf(String onboardingApplicationLineitemId , OffsetDateTime createTime ,
                                                                 String data , Boolean submitted  ){

        return new CheckComplianceAndCertAvailabilityImpl( onboardingApplicationLineitemId ,  createTime ,
                data ,  submitted  );
    }

    private CheckComplianceAndCertAvailabilityImpl(String onboardingApplicationLineitemId , OffsetDateTime createTime ,
                           String data , Boolean submitted  ){

        super(onboardingApplicationLineitemId  ,createTime , data ,  submitted );


    }

}
