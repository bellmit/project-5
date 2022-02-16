package com.kindminds.drs.api.data.cqrs.store.read.queries;


import com.kindminds.drs.api.data.transfer.productV2.LegacyProduct;

public interface MigrateDraftProductQueries {

    LegacyProduct getBaseProductOnboardingApplication(String productBaseCode);

}
