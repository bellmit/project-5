package com.kindminds.drs.api.data.transfer.productV2.onboarding;

import com.kindminds.drs.enums.OnboardingApplicationStatusType;
import com.kindminds.drs.util.Encryptor;

import java.util.List;

public final class OnboardingApplication {
    
    private final String id;
    
    private final String supplierKcode;
    
    private final OnboardingApplicationStatusType status;
    
    private final String serialNumber;
    
    private List onboardingApplicationLineitems;

    
    public final String getId() {
        return this.id;
    }

    
    public final String getSupplierKcode() {
        return this.supplierKcode;
    }

    
    public final OnboardingApplicationStatusType getStatus() {
        return this.status;
    }

    
    public final String getSerialNumber() {
        return this.serialNumber;
    }

    
    public final List getOnboardingApplicationLineitems() {
        return this.onboardingApplicationLineitems;
    }

    public final void setOnboardingApplicationLineitems( List var1) {
        this.onboardingApplicationLineitems = var1;
    }

    public OnboardingApplication() {
        this("", "", (OnboardingApplicationStatusType)null, "", (List)null);
    }

    public OnboardingApplication( String id,  String supplierKcode,  OnboardingApplicationStatusType status,  String serialNumber,  List onboardingApplicationLineitems) {
        this.id = Encryptor.encrypt(id);
        this.supplierKcode = supplierKcode;
        this.status = status;
        this.serialNumber = serialNumber;
        this.onboardingApplicationLineitems = onboardingApplicationLineitems;
    }
}