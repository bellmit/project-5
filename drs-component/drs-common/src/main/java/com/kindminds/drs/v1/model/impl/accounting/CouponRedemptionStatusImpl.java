package com.kindminds.drs.v1.model.impl.accounting;

import com.kindminds.drs.api.v1.model.accounting.InternationalTransaction;
import com.kindminds.drs.api.v1.model.coupon.CouponRedemption;
import com.kindminds.drs.api.v1.model.coupon.CouponRedemptionStatus;

import java.util.List;

public class CouponRedemptionStatusImpl implements CouponRedemptionStatus {
    private List<InternationalTransaction> processedCouponRedemptions;
    private List<CouponRedemption> unprocessedCouponRedemptions;

    @Override
    public List<InternationalTransaction> getProcessedCouponRedemptions() {
        return this.processedCouponRedemptions;
    }

    @Override
    public List<CouponRedemption> getUnprocessedCouponRedemptions() {
        return this.unprocessedCouponRedemptions;
    }

    public void setProcessedCouponRedemptions(List<InternationalTransaction> processedCouponRedemptions) {
        this.processedCouponRedemptions = processedCouponRedemptions;
    }

    public void setUnprocessedCouponRedemptions(List<CouponRedemption> unprocessedCouponRedemptions) {
        this.unprocessedCouponRedemptions = unprocessedCouponRedemptions;
    }
}