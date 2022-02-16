package com.kindminds.drs.api.usecase;

import com.kindminds.drs.api.v1.model.coupon.CouponRedemption;
import com.kindminds.drs.api.v1.model.coupon.CouponRedemptionStatus;
import com.kindminds.drs.api.v1.model.message.MessageCode;

import java.util.List;

public interface ProcessCouponRedemptionUco {
    CouponRedemptionStatus processCouponRedemptionFees();
    CouponRedemptionStatus processCouponRedemptionFees(Integer periodId);
    MessageCode getCouponProcessStatus();
    List<CouponRedemption> getFailedCouponRedemptionFees();
}
