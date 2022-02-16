package com.kindminds.drs.api.message.coupon

import com.kindminds.drs.api.message.{Command}

/**
  * Uses the latest settlement period
  *
  * Returns - CouponRedemptionStatus
  */
case class ProcessCouponRedemption() extends Command

/**
  * Returns - CouponRedemptionStatus
  */
case class ProcessCouponRedemptionBySettlementPeriod(periodId : Integer) extends Command