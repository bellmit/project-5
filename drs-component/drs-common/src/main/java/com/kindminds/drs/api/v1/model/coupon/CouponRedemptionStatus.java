package com.kindminds.drs.api.v1.model.coupon;



import com.kindminds.drs.api.v1.model.accounting.InternationalTransaction;

import java.util.List;

public interface CouponRedemptionStatus {
    List<InternationalTransaction> getProcessedCouponRedemptions();
    List<CouponRedemption> getUnprocessedCouponRedemptions();
}
