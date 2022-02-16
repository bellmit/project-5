package com.kindminds.drs.core.biz.product.onboarding;



import com.kindminds.drs.api.v2.biz.domain.model.product.Product;
import com.kindminds.drs.api.v2.biz.domain.model.product.onboarding.OnboardingApplicationLineitemStatusType;


import java.time.OffsetDateTime;

public class OnboardingApplicationLineitemCreator {

    public static OnboardingApplicationLineitemImpl createMigratedOnboardingApplicationLineitem(
            String onboardingApplicationId , OffsetDateTime onboardingApplicationCreateTime
            , Product product , OnboardingApplicationLineitemStatusType status  ) {

        return new OnboardingApplicationLineitemImpl(onboardingApplicationId,
                onboardingApplicationCreateTime, product , status);
    }

    public static OnboardingApplicationLineitemImpl create(
            String onboardingApplicationId , OffsetDateTime onboardingApplicationCreateTime
            , Product product , OnboardingApplicationLineitemStatusType status) {

        return new OnboardingApplicationLineitemImpl(onboardingApplicationId,
                onboardingApplicationCreateTime, product ,status);
    }

    public static OnboardingApplicationLineitemImpl create(
            String onboardingApplicationId , OffsetDateTime onboardingApplicationCreateTime
            , Product product ) {

        return new OnboardingApplicationLineitemImpl(onboardingApplicationId,
                onboardingApplicationCreateTime, product);
    }

    public static OnboardingApplicationLineitemImpl create(
            String id , String onboardingApplicationId , OffsetDateTime onboardingApplicationCreateTime
            , Product product) {
        return new OnboardingApplicationLineitemImpl(id ,
                onboardingApplicationId, onboardingApplicationCreateTime,
                product);
    }



}
