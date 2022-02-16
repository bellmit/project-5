package com.kindminds.drs.core.biz.product.onboarding.detail;



import com.kindminds.drs.api.v2.biz.domain.model.product.onboarding.detail.TrialListing;

import java.time.OffsetDateTime;

public class TrialListingImpl extends  AbstractOnboardingApplicationLineitemDetail implements TrialListing {



    public static TrialListingImpl valueOf(String onboardingApplicationLineitemId ,
                                           String data , Boolean submitted  ){

        return new TrialListingImpl( onboardingApplicationLineitemId  ,
                data ,  submitted  );
    }

    private TrialListingImpl(String onboardingApplicationLineitemId ,
                             String data , Boolean submitted  ){

        super(onboardingApplicationLineitemId  ,
                data ,  submitted );


    }

    public static TrialListingImpl valueOf(String onboardingApplicationLineitemId , OffsetDateTime createTime ,
                                           String data , Boolean submitted  ){

        return new TrialListingImpl( onboardingApplicationLineitemId ,  createTime ,
                 data ,  submitted  );
    }

    private TrialListingImpl(String onboardingApplicationLineitemId , OffsetDateTime createTime ,
                             String data , Boolean submitted  ){

        super(onboardingApplicationLineitemId  ,createTime , data ,  submitted );


    }




}
