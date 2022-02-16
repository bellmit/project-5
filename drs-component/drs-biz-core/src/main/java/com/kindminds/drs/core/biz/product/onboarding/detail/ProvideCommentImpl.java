package com.kindminds.drs.core.biz.product.onboarding.detail;



import com.kindminds.drs.api.v2.biz.domain.model.product.onboarding.detail.ProvideComment;

import java.time.OffsetDateTime;

public class ProvideCommentImpl extends AbstractOnboardingApplicationLineitemDetail
        implements ProvideComment {

    public static ProvideCommentImpl valueOf(String onboardingApplicationLineitemId ,
                                             String data , Boolean submitted  ){

        return new ProvideCommentImpl( onboardingApplicationLineitemId  ,
                data ,  submitted  );
    }

    private ProvideCommentImpl(String onboardingApplicationLineitemId ,
                               String data , Boolean submitted  ){

        super(onboardingApplicationLineitemId  ,
                data ,  submitted );


    }

    public static ProvideCommentImpl valueOf(String onboardingApplicationLineitemId , OffsetDateTime createTime ,
                                             String data , Boolean submitted  ){

        return new ProvideCommentImpl( onboardingApplicationLineitemId ,  createTime ,
                data ,  submitted  );
    }

    private ProvideCommentImpl(String onboardingApplicationLineitemId , OffsetDateTime createTime ,
                               String data , Boolean submitted  ){

        super(onboardingApplicationLineitemId  ,createTime , data ,  submitted );


    }
}
