package com.kindminds.drs.persist.data.access.usecase.report;

import com.kindminds.drs.api.data.access.usecase.report.ViewAmazonSponsoredBrandsCampaignReportDao;
import com.kindminds.drs.api.v1.model.amazon.AmazonHeadlineSearchAdReportItem;
import com.kindminds.drs.persist.data.access.rdb.Dao;
import com.kindminds.drs.persist.v1.model.mapping.amazon.AmazonHeadlineSearchAdReportItemImpl;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class ViewAmazonSponsoredBrandsCampaignReportDaoImpl  extends Dao implements ViewAmazonSponsoredBrandsCampaignReportDao {



    @Override @SuppressWarnings("unchecked")
    public List<String> queryCampaignNames(String supplierKcode, Integer marketplaceId, Date start, Date end) {
        String sql = "select distinct ahkr.campaign_name "
                + " from amazon_hsa_keyword_report ahkr "
                + " where ( ahkr.campaign_name like :nameType1  "
                + " OR ahkr.campaign_name like :nameType2 )"
                + " and ahkr.marketplace_id = :marketplaceId  ";
        if(start!=null) sql += "and ahkr.report_date >= :startDate ";
        if(end!=null) sql += "and ahkr.report_date <= :endDate ";

        MapSqlParameterSource q = new MapSqlParameterSource();

        q.addValue("marketplaceId",marketplaceId);
        q.addValue("nameType1", "[" + supplierKcode + "%");
        q.addValue("nameType2", supplierKcode + "%");
        if(start!=null) q.addValue("startDate",start);
        if(end!=null) q.addValue("endDate",end);
        return getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
    }

    @Override @SuppressWarnings("unchecked")
    public List<AmazonHeadlineSearchAdReportItem> queryReportDaily(String supplierKcode, Integer marketplaceId, Date start, Date end, String campaignName) {
        String sql = "select " +
                " id as id, " +
                " marketplace_id as marketplace_id, " +
                " report_date as report_date, " +
                " currency as currency, " +
                " campaign_name as campaign_name, " +
                " targeting as targeting, " +
                " match_type as match_type, " +
                " impressions as impressions, " +
                " clicks as clicks, " +
                " click_thru_rate as click_thru_rate, " +
                " cost_per_click as cost_per_click, " +
                " spend as spend, " +
                " total_advertising_cost_of_sales as total_advertising_cost_of_sales, " +
                " total_return_on_advertising_spend as total_return_on_advertising_spend, " +
                " total_sales_14days as total_sales_14days, " +
                " total_orders_14days as total_orders_14days, " +
                " total_units_14days as total_units_14days, " +
                " conversion_rate_14days as conversion_rate_14days " +
                " from amazon_hsa_keyword_report ahkr " +
                " where  " +
                " ahkr.marketplace_id = :marketplaceId " +
                " and ( ahkr.campaign_name like :nameType1 " +
                " OR ahkr.campaign_name like :nameType2 ) ";
        if(campaignName != null && !campaignName.isEmpty())
            sql += "  and ahkr.campaign_name = :campaignName";
        if(start!=null)
            sql += " and ahkr.report_date >= :startDate ";
        if(end!=null)
            sql += " and ahkr.report_date <= :endDate ";

        sql += " order by targeting, match_type ";

        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("marketplaceId",marketplaceId);
        q.addValue("nameType1", "[" + supplierKcode + "%");
        q.addValue("nameType2", supplierKcode + "%");
        if(campaignName != null && !campaignName.isEmpty())
            q.addValue("campaignName",campaignName);
        if(start!=null)
            q.addValue("startDate",start);
        if(end!=null)
            q.addValue("endDate",end);

        return (List) getNamedParameterJdbcTemplate().query(sql,q,(rs,rowNum) -> new AmazonHeadlineSearchAdReportItemImpl(
                rs.getInt(""),rs.getInt("marketplace_id"),
                rs.getDate("report_date"),
                rs.getString("currency"),
                rs.getString("campaign_name")
                ,rs.getString("targeting"),rs.getString("match_type"),
                rs.getInt("impressions"),rs.getInt("clicks"),rs.getBigDecimal("click_thru_rate")
                ,rs.getBigDecimal("cost_per_click"),rs.getBigDecimal("spend"),
                rs.getBigDecimal("total_advertising_cost_of_sales"),
                rs.getBigDecimal("total_return_on_advertising_spend")
                ,rs.getBigDecimal("total_sales_14days"),
                rs.getInt("total_orders_14days"),rs.getInt("total_units_14days"),rs.getBigDecimal("conversion_rate_14days")
        ));
    }


    @Override @SuppressWarnings("unchecked")
    public List<AmazonHeadlineSearchAdReportItem> queryReportByPeriod(String supplierKcode, Integer marketplaceId, Date start, Date end, String campaignName) {
        String sql = "select " +
                " marketplace_id as marketplace_id, " +
                " currency as currency, " +
                " campaign_name as campaign_name, " +
                " targeting as targeting, " +
                " match_type as match_type, " +
                " sum(impressions) as impressions, " +
                " sum(clicks) as clicks, " +
                " sum(spend) as spend, " +
                " sum(total_sales_14days) as total_sales_14days, " +
                " sum(total_orders_14days) as total_orders_14days, " +
                " sum(total_units_14days) as total_units_14days " +
                " from amazon_hsa_keyword_report ahkr  " +
                " where  " +
                " ahkr.marketplace_id = :marketplaceId " +
                " and ( ahkr.campaign_name like :nameType1 " +
                " OR ahkr.campaign_name like :nameType2 ) ";
        if(campaignName != null && !campaignName.isEmpty())
            sql += "  and ahkr.campaign_name = :campaignName";
        if(start!=null)
            sql += " and ahkr.report_date >= :startDate ";
        if(end!=null)
            sql += " and ahkr.report_date <= :endDate ";

        sql += " group by marketplace_id, currency, campaign_name, targeting, match_type " +
                " order by targeting, match_type ";

        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("marketplaceId",marketplaceId);
        q.addValue("nameType1", "[" + supplierKcode + "%");
        q.addValue("nameType2", supplierKcode + "%");
        if(campaignName != null && !campaignName.isEmpty())
            q.addValue("campaignName",campaignName);
        if(start!=null)
            q.addValue("startDate",start);
        if(end!=null)
            q.addValue("endDate",end);

        List<Object[]> resultList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
        List<AmazonHeadlineSearchAdReportItem> amazonHeadlineSearchAdReportItems = new ArrayList<>();
        for (Object[] result : resultList) {
            amazonHeadlineSearchAdReportItems.add(
                    new AmazonHeadlineSearchAdReportItemImpl(
                            (Integer) result[0],
                            (String) result[1],
                            (String) result[2],
                            (String) result[3],
                            (String) result[4],
                            BigInteger.valueOf( (Long) result[5]), BigInteger.valueOf( (Long) result[6]),
                            (BigDecimal) result[7],
                            (BigDecimal) result[8], BigInteger.valueOf( (Long) result[9]), BigInteger.valueOf( (Long) result[10])
                    )
            );
        }
        return amazonHeadlineSearchAdReportItems;
    }


}