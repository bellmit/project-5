package com.kindminds.drs.persist.data.access.usecase.accounting;

import com.kindminds.drs.api.data.access.usecase.accounting.ProcessAdSpendTransactionDao;
import com.kindminds.drs.persist.data.access.rdb.Dao;
import com.kindminds.drs.api.v1.model.marketing.AdSpendTransaction;
import com.kindminds.drs.persist.v1.model.mapping.accounting.AdSpendTransactionImpl;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;




import java.math.BigInteger;
import java.util.List;

@Repository
public class ProcessAdSpendTransactionDaoImpl extends Dao implements ProcessAdSpendTransactionDao {


    @Override
    public boolean isAdSpendProcessed() {
        String sql = "select count(*) " +
                "from international_transaction_line_item itli " +
                "inner join international_transaction it on itli.transaction_id = it.id " +
                "where it.transaction_date >= (select max(period_end) from bill_statement) and itli.type_id = 5 " +
                "and ( itli.note like '%Sponsored Ad%' " +
                "or itli.note like '%Headline Ad Spend%' ) ";

        BigInteger count = getJdbcTemplate().queryForObject(sql,BigInteger.class);

        return count.intValue() > 0;
    }

    @Override
    public List<AdSpendTransaction> getSponsoredAdTransactions() {
        String sql = "SELECT  " +
                "  company.k_code AS supplier_kcode,  " +
                "  \"MSDC\".k_code AS msdc_kcode, " +
                "  currency.id as currency_id, " +
                "  sum(amazon_campaign_performance_report.total_spend) as sum, " +
                " ( select to_char(max(period_start) at time zone :tz, " +
                " :fm) from settlement_period) as period_start_utc, " +
                "  ( select to_char(max(period_end) at time zone :tz, " +
                " :fm) from settlement_period) as period_end_utc " +
                "FROM  " +
                "  public.amazon_campaign_performance_report,  " +
                "  public.product_marketplace_info,  " +
                "  public.product_sku,  " +
                "  public.product_base,  " +
                "  public.company,  " +
                "  public.marketplace, " +
                "  public.currency,  " +
                "  public.company \"MSDC\" " +
                "WHERE  " +
                "  amazon_campaign_performance_report.marketplace_id = marketplace.id AND " +
                "  product_marketplace_info.marketplace_id = amazon_campaign_performance_report.marketplace_id AND " +
                "  product_marketplace_info.marketplace_sku = amazon_campaign_performance_report.advertised_sku AND " +
                "  product_marketplace_info.product_id = product_sku.product_id AND " +
                "  product_sku.product_base_id = product_base.id AND " +
                "  product_base.supplier_company_id = company.id AND " +
                "  marketplace.country_id = \"MSDC\".country_id AND " +
                "  amazon_campaign_performance_report.currency=currency.name AND " +
                "  amazon_campaign_performance_report.start_date >= (select max(period_start) from settlement_period) AND  " +
                "  amazon_campaign_performance_report.start_date < (select max(period_end) from settlement_period) AND  " +
                "  \"MSDC\".is_drs_company = TRUE " +
                "GROUP BY " +
                "  company.k_code,  " +
                "  \"MSDC\".k_code, " +
                "  currency.id " +
                "ORDER BY  " +
                "  company.k_code,  " +
                "  \"MSDC\".k_code, " +
                "  currency.id ";
        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("fm", "YYYYMMDD");
        q.addValue("tz", "UTC");



        List<AdSpendTransactionImpl> list = getNamedParameterJdbcTemplate().query(sql,q, (rs , rowNum ) -> new AdSpendTransactionImpl(
            rs.getString("supplier_kcode"),rs.getString("msdc_kcode"),
                rs.getInt("currency_id"),rs.getBigDecimal("sum"),rs.getString("period_start_utc"),rs.getString("period_end_utc")
        ));
        if(list == null || list.isEmpty())
            return null;
        return (List)list;
    }

    @Override
    public List<AdSpendTransaction> getHeadlineAdTransactions() {
        String sql = "SELECT  " +
                "  com.k_code as supplier_kcode,  " +
                "   (select msdc.k_code " +
                "    from company msdc " +
                "    where msdc.country_id = mp.country_id and " +
                "  msdc.is_drs_company = true) as msdc_kcode, " +
                "  cur.id as currency_id, " +
                "  sum(ahcr.spend) as sum, " +
                " ( select to_char(max(period_start) at time zone :tz, " +
                " :fm) from settlement_period) as period_start_utc, " +
                "  ( select to_char(max(period_end) at time zone :tz, " +
                " :fm) from settlement_period) as period_end_utc " +
                "FROM  " +
                "  amazon_hsa_campaign_report ahcr " +
                "inner join currency cur on ahcr.currency = cur.\"name\" " +
                "inner join company com on com.k_code = substring(ahcr.campaign_name, 2, 4) " +
                "inner join marketplace mp on ahcr.marketplace_id = mp.id " +
                "WHERE  " +
                "  ahcr.report_date >= (select max(period_start) from settlement_period) AND  " +
                "  ahcr.report_date < (select max(period_end) from settlement_period)  " +
                "GROUP BY " +
                "  com.k_code, " +
                "  msdc_kcode, " +
                "  cur.id " +
                "ORDER BY  " +
                "  com.k_code, " +
                "  msdc_kcode, " +
                "  cur.id ";
        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("fm", "YYYYMMDD");
        q.addValue("tz", "UTC");

        List<AdSpendTransactionImpl> list = getNamedParameterJdbcTemplate().query(sql,q, (rs , rowNum ) -> new AdSpendTransactionImpl(
                rs.getString("supplier_kcode"),rs.getString("msdc_kcode"),
                rs.getInt("currency_id"),rs.getBigDecimal("sum"),rs.getString("period_start_utc"),rs.getString("period_end_utc")
        ));

        if(list == null || list.isEmpty())
            return null;
        return (List)list;
    }
}