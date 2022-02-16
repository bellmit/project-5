package com.kindminds.drs.api.data.access.usecase.accounting;

import com.kindminds.drs.api.v1.model.coupon.CouponRedemption;

import java.util.List;

public interface ProcessCouponRedemptionDao {
    List<CouponRedemption> getUnprocessedCouponRedemptionFees(Integer periodId);
    List<CouponRedemption> getUnprocessedCouponRedemptionFees();
    boolean updateProcessedCouponRedemptionFee(Integer amazonSettlementId, boolean isProcessed, String reason);
    List<CouponRedemption> getFailedCouponRedemptionFees();
}
