package com.kindminds.drs.data.pipelines.core.dto

case class EarlyReviewIntTran(
             id:Int,
             marketplace_id:Int,
             inserted_on: java.sql.Timestamp,
             date_time: java.sql.Timestamp,
             description: String,
             marketplace: String,
             other_transaction_fees: BigDecimal,
             total: BigDecimal
                             )
case class SettlementDates(
             id:Int,
             period_start: java.sql.Timestamp,
             period_end: java.sql.Timestamp
                           )