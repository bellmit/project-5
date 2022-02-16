package com.kindminds.drs.persist.data.access.usecase.accounting;

import com.kindminds.drs.api.data.access.usecase.accounting.ProcessCouponRedemptionDao;
import com.kindminds.drs.persist.data.access.rdb.Dao;
import com.kindminds.drs.api.v1.model.coupon.CouponRedemption;
import com.kindminds.drs.persist.v1.model.mapping.accounting.CouponRedemptionImpl;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;




import java.util.List;

@Repository
public class ProcessCouponRedemptionDaoImpl extends Dao implements ProcessCouponRedemptionDao {

    @Override
    public List<CouponRedemption> getUnprocessedCouponRedemptionFees(Integer settlementPeriodId) {
        String sql = "select distinct" +
                " asr.id as id," +
                " to_char( asr.posted_date_time at time zone :tz," +
                " :pdfm ) as transaction_time," +
                " cp.company_id as supplier_id," +
                " c.name_en_us as supplier_name," +
                " c.k_code as supplier_kcode," +
                " to_char( sp.period_start at time zone :tz," +
                " :fm ) as period_start_utc," +
                " to_char( sp.period_end at time zone :tz," +
                " :fm ) as period_end_utc," +
                " asr.amount_description as coupon," +
                " asr.amount as amount," +
                " cur.id as currency_id," +
                " asr.currency as currency_name, " +
                " mp.name as marketplace_name, " +
                " null as reason, " +
                " (select msdc.k_code" +
                " from company msdc" +
                " inner join marketplace mp on asr.source_marketplace_id = mp.id" +
                " where msdc.is_drs_company = true and msdc.country_id = mp.country_id " +
                " ) as msdc_kcode " +
                "from" +
                " amazon_settlement_report_v2 asr " +
                "inner join settlement_period sp on" +
                " :period_id = sp.id " +
                "left join company_coupon cp on" +
                " upper(asr.amount_description) like concat('%', upper(cp.coupon), '%') " +
                "left join company c on" +
                " cp.company_id = c.id " +
                "inner join currency cur on asr.currency = cur.\"name\" " +
                "inner join marketplace mp on asr.source_marketplace_id = mp.id " +
                "where" +
                " asr.posted_date_time >= sp.period_start" +
                " and asr.posted_date_time < sp.period_end" +
                " and asr.transaction_type = 'CouponRedemptionFee'" +
                " and (c.k_code <> 'K101' or c.k_code is null)" +
                " and (not exists ( " +
                " select " +
                "  null " +
                " from " +
                "  amazon_settlement_report_v2_processed asrp " +
                " where " +
                "  asr.id = asrp.amazon_settlement_id) " +
                " or exists ( " +
                " select " +
                "  null " +
                " from " +
                "  amazon_settlement_report_v2_processed asrp " +
                " where " +
                "  asr.id = asrp.amazon_settlement_id and asrp.processed = false))" +
                " order by cp.company_id, msdc_kcode";
        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("pdfm", "YYYY-MM-DD HH24:MI:SS");
        q.addValue("tz", "UTC");
        q.addValue("fm", "YYYYMMDD");
        q.addValue("period_id", settlementPeriodId);



        List<CouponRedemptionImpl> list = getNamedParameterJdbcTemplate().query(sql,q,(rs , rowNum) -> new CouponRedemptionImpl(
                rs.getInt("id"),rs.getString("transaction_time"),rs.getInt("supplier_id"),rs.getString("supplier_name")
                ,rs.getString("supplier_kcode"),
                rs.getString("period_start_utc"),rs.getString("period_end_utc"),
                rs.getString("coupon"),rs.getBigDecimal("amount"),rs.getInt("currency_id"),
                rs.getString("currency_name"),rs.getString("marketplace_name"),
                rs.getString("msdc_kcode" ), rs.getString("reason"))
        );

        if(list == null || list.isEmpty())
            return null;
        return (List)list;
    }

    @Override
    public List<CouponRedemption> getUnprocessedCouponRedemptionFees() {
        String sql = "select distinct" +
                " asr.id as id," +
                " to_char( asr.posted_date_time at time zone :tz," +
                " :pdfm ) as transaction_time," +
                " cp.company_id as supplier_id," +
                " c.name_en_us as supplier_name," +
                " c.k_code as supplier_kcode," +
                " to_char( sp.period_start at time zone :tz," +
                " :fm ) as period_start_utc," +
                " to_char( sp.period_end at time zone :tz," +
                " :fm ) as period_end_utc," +
                " asr.amount_description as coupon," +
                " asr.amount as amount," +
                " cur.id as currency_id," +
                " asr.currency as currency_name, " +
                " mp.name as marketplace_name, " +
                " null as reason, " +
                " (select msdc.k_code" +
                " from company msdc" +
                " inner join marketplace mp on asr.source_marketplace_id = mp.id" +
                " where msdc.is_drs_company = true and msdc.country_id = mp.country_id " +
                " ) as msdc_kcode " +
                "from" +
                " amazon_settlement_report_v2 asr " +
                "inner join settlement_period sp on " +
                "(select max(period_end) from bill_statement) = sp.period_end " +
                "left join company_coupon cp on" +
                " upper(asr.amount_description) like concat('%', upper(cp.coupon), '%') " +
                "left join company c on" +
                " cp.company_id = c.id " +
                "inner join currency cur on asr.currency = cur.\"name\" " +
                "inner join marketplace mp on asr.source_marketplace_id = mp.id " +
                "where" +
                " asr.posted_date_time >= (select max(period_start) from settlement_period) " +
                " and asr.posted_date_time < (select max(period_end) from settlement_period) " +
                " and asr.transaction_type = 'CouponRedemptionFee'" +
                " and (c.k_code <> 'K101' or c.k_code is null)" +
                " and (not exists ( " +
                " select " +
                "  null " +
                " from " +
                "  amazon_settlement_report_v2_processed asrp " +
                " where " +
                "  asr.id = asrp.amazon_settlement_id) " +
                " or exists ( " +
                " select " +
                "  null " +
                " from " +
                "  amazon_settlement_report_v2_processed asrp " +
                " where " +
                "  asr.id = asrp.amazon_settlement_id and asrp.processed = false))" +
                " order by cp.company_id, msdc_kcode";
        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("pdfm", "YYYY-MM-DD HH24:MI:SS");
        q.addValue("tz", "UTC");
        q.addValue("fm", "YYYYMMDD");

        List<CouponRedemptionImpl> list = getNamedParameterJdbcTemplate().query(sql,q,(rs , rowNum) -> new CouponRedemptionImpl(
                rs.getInt("id"),rs.getString("transaction_time"),rs.getInt("supplier_id"),rs.getString("supplier_name")
                ,rs.getString("supplier_kcode"),
                rs.getString("period_start_utc"),rs.getString("period_end_utc"),
                rs.getString("coupon"),rs.getBigDecimal("amount"),rs.getInt("currency_id"),
                rs.getString("currency_name"),rs.getString("marketplace_name"),
                rs.getString("msdc_kcode" ), rs.getString("reason"))
        );

        if(list == null || list.isEmpty())
            return null;
        return (List) list;
    }

    @Override
    @Transactional("transactionManager")
    public boolean updateProcessedCouponRedemptionFee(Integer amazonSettlementId, boolean isProcessed, String reason) {
        deleteProcessedCouponRedemptionFee(amazonSettlementId);
        return addProcessedCouponRedemptionFee(amazonSettlementId, isProcessed, reason);
    }

    @Transactional("transactionManager")
    public void deleteProcessedCouponRedemptionFee(Integer amazonSettlementId) {
        String sql = "delete from amazon_settlement_report_v2_processed asrp " +
                "where asrp.amazon_settlement_id = :amazon_settlement_id";
        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("amazon_settlement_id", amazonSettlementId);
        getNamedParameterJdbcTemplate().update(sql,q);
    }

    @Transactional("transactionManager")
    public boolean addProcessedCouponRedemptionFee(Integer amazonSettlementId, boolean isProcessed, String reason) {
        String sql = "insert into amazon_settlement_report_v2_processed " +
                "(amazon_settlement_id, processed, reason) " +
                "values (:amazon_settlement_id, :isProcessed, :reason)";
        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("amazon_settlement_id", amazonSettlementId);
        q.addValue("isProcessed", isProcessed);
        q.addValue("reason", reason);
        int successful = getNamedParameterJdbcTemplate().update(sql,q);
        return (successful > 0 ? true : false);
    }

    @Override
    public List<CouponRedemption> getFailedCouponRedemptionFees() {
        String sql = "select " +
                " asr.id as id, " +
                " to_char( asr.posted_date_time at time zone :tz, " +
                " :pdfm ) as transaction_time, " +
                " to_char( sp.period_start at time zone :tz, " +
                " :fm ) as period_start_utc, " +
                " to_char( sp.period_end at time zone :tz, " +
                " :fm ) as period_end_utc, " +
                " 0 as supplier_id, " +
                " null as supplier_name, " +
                " null as supplier_kcode, " +
                " asr.amount_description as coupon, " +
                " asr.amount as amount, " +
                " cur.id as currency_id, " +
                " asr.currency as currency_name, " +
                " mp.name as marketplace_name, " +
                " asrp.reason as reason, " +
                " ( " +
                " select " +
                "  msdc.k_code " +
                " from " +
                "  company msdc " +
                " inner join marketplace mp on " +
                "  asr.source_marketplace_id = mp.id " +
                " where " +
                "  msdc.is_drs_company = true " +
                "  and msdc.country_id = mp.country_id ) as msdc_kcode " +
                "from " +
                " amazon_settlement_report_v2 asr " +
                "inner join settlement_period sp on " +
                " (select max(period_end) from bill_statement) = sp.period_end  " +
                "inner join currency cur on " +
                " asr.currency = cur.\"name\" " +
                "inner join marketplace mp on " +
                " asr.source_marketplace_id = mp.id " +
                "inner join amazon_settlement_report_v2_processed asrp on " +
                " asr.id = asrp.amazon_settlement_id " +
                "where " +
                " asr.posted_date_time >= (select max(period_start) from settlement_period) " +
                " and asr.posted_date_time < (select max(period_end) from settlement_period) " +
                " and asr.transaction_type = 'CouponRedemptionFee' " +
                " and asrp.processed = false " +
                "order by " +
                " msdc_kcode";
        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("pdfm", "YYYY-MM-DD HH24:MI:SS");
        q.addValue("tz", "UTC");
        q.addValue("fm", "YYYYMMDD");

        List<CouponRedemptionImpl> list = getNamedParameterJdbcTemplate().query(sql,q,(rs , rowNum) -> new CouponRedemptionImpl(
                rs.getInt("id"),rs.getString("transaction_time"),rs.getInt("supplier_id"),rs.getString("supplier_name")
                ,rs.getString("supplier_kcode"),
                rs.getString("period_start_utc"),rs.getString("period_end_utc"),
                rs.getString("coupon"),rs.getBigDecimal("amount"),rs.getInt("currency_id"),
                rs.getString("currency_name"),rs.getString("marketplace_name"),
                rs.getString("msdc_kcode" ), rs.getString("reason"))
        );

        if(list == null || list.isEmpty())
            return null;
        return (List) list;
    }
}
