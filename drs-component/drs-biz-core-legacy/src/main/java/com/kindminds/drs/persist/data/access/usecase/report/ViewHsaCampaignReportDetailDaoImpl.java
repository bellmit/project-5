package com.kindminds.drs.persist.data.access.usecase.report;

import com.kindminds.drs.api.data.access.usecase.report.ViewHsaCampaignReportDetailDao;
import com.kindminds.drs.persist.data.access.rdb.Dao;
import com.kindminds.drs.api.v1.model.report.HsaCampaignReportDetail;
import com.kindminds.drs.persist.v1.model.mapping.report.HsaCampaignReportDetailImpl;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;




import java.util.Date;
import java.util.List;

@Repository
public class ViewHsaCampaignReportDetailDaoImpl extends Dao implements ViewHsaCampaignReportDetailDao {



    @Override
    public List<HsaCampaignReportDetail> generateHsaCampaignReport(Integer settlementPeriodId) {
        String sql = "SELECT  " +
                " ahcr.id as id, " +
                "  com.k_code as supplier_kcode, " +
                " mp.\"name\" as marketplace_name, " +
                " ahcr.marketplace_id as marketplace_id, " +
                " ahcr.report_date as report_date, " +
                "  ahcr.currency as currency_name, " +
                " ahcr.campaign_name as campaign_name, " +
                " ahcr.impressions as impressions, " +
                " ahcr.clicks as clicks, " +
                " ahcr.click_thru_rate as click_thru_rate, " +
                " ahcr.cost_per_click as cost_per_click, " +
                " ahcr.spend as spend, " +
                " ahcr.total_advertising_cost_of_sales as total_advertising_cost_of_sales, " +
                " ahcr.total_return_on_advertising_spend as total_return_on_advertising_spend, " +
                " ahcr.total_sales_14days as total_sales_14days, " +
                " ahcr.total_orders_14days as total_orders_14days, " +
                " ahcr.total_units_14days as total_units_14days, " +
                " ahcr.conversion_rate_14days as conversion_rate_14days " +
                "FROM  " +
                "  amazon_hsa_campaign_report ahcr " +
                "inner join company com on com.k_code = substring(ahcr.campaign_name, 2, 4) " +
                "inner join marketplace mp on ahcr.marketplace_id = mp.id " +
                "inner join settlement_period sp on :period_id = sp.id " +
                "WHERE  " +
                "  ahcr.report_date >= sp.period_start AND " +
                "  ahcr.report_date < sp.period_end " +
                "ORDER BY  " +
                "  com.k_code, " +
                "  mp.\"name\", " +
                "  ahcr.campaign_name, " +
                "  ahcr.report_date ";

       MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("period_id", settlementPeriodId);


        List<HsaCampaignReportDetail> list = (List) getNamedParameterJdbcTemplate().query(sql,q,(rs,rowNum)->
                new HsaCampaignReportDetailImpl(
                        rs.getInt("id"),rs.getString("supplier_kcode"),rs.getString("marketplace_name"),
                        rs.getInt("marketplace_id"),rs.getString("campaign_name"),
                        rs.getString("report_date"),rs.getString("currency_name"),
                        rs.getInt("impressions"),rs.getInt("clicks"),rs.getBigDecimal("click_thru_rate"),
                        rs.getBigDecimal("cost_per_click"),rs.getBigDecimal("spend"),
                        rs.getBigDecimal("total_advertising_cost_of_sales"),
                        rs.getBigDecimal("total_return_on_advertising_spend"),
                        rs.getBigDecimal("total_sales_14days"),
                        rs.getInt("total_orders_14days"),rs.getInt("total_units_14days"),
                        rs.getBigDecimal("conversion_rate_14days")
                ));
        if(list == null || list.isEmpty())
            return null;
        return list;
    }


    @Override @SuppressWarnings("unchecked")
    public List<String> queryCampaignNames(String supplierKcode, Integer marketplaceId, Date start, Date end) {
        String sql = "select distinct ahcr.campaign_name "
                + " from amazon_hsa_campaign_report ahcr "
                + " where ( ahcr.campaign_name like :nameType1  "
                + " OR ahcr.campaign_name like :nameType2 )"
                + " and ahcr.marketplace_id = :marketplaceId  ";

        if(start!=null) sql += "and ahcr.report_date >= :startDate ";
        if(end!=null) sql += "and ahcr.report_date <= :endDate ";

      MapSqlParameterSource q = new MapSqlParameterSource();

        q.addValue("marketplaceId",marketplaceId);
        q.addValue("nameType1", "[" + supplierKcode + "%");
        q.addValue("nameType2", supplierKcode + "%");
        if(start!=null) q.addValue("startDate",start);
        if(end!=null) q.addValue("endDate",end);
        return getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
    }

    @Override
    public List<HsaCampaignReportDetail> queryReport(String supplierKcode, Integer marketplaceId,
                                                     Date start, Date end, String campaignName) {

        String sql = "select " +
                " ahcr.id as id, " +
                "  com.k_code as supplier_kcode, " +
                " mp.\"name\" as marketplace_name, " +
                " ahcr.marketplace_id as marketplace_id, " +
                " ahcr.report_date as report_date, " +
                "  ahcr.currency as currency_name, " +
                " ahcr.campaign_name as campaign_name, " +
                " ahcr.impressions as impressions, " +
                " ahcr.clicks as clicks, " +
                " ahcr.click_thru_rate as click_thru_rate, " +
                " ahcr.cost_per_click as cost_per_click, " +
                " ahcr.spend as spend, " +
                " ahcr.total_advertising_cost_of_sales as total_advertising_cost_of_sales, " +
                " ahcr.total_return_on_advertising_spend as total_return_on_advertising_spend, " +
                " ahcr.total_sales_14days as total_sales_14days, " +
                " ahcr.total_orders_14days as total_orders_14days, " +
                " ahcr.total_units_14days as total_units_14days, " +
                " ahcr.conversion_rate_14days as conversion_rate_14days " +
                " from amazon_hsa_campaign_report ahcr " +
                " inner join company com on com.k_code = substring(ahcr.campaign_name, 2, 4) " +
                " inner join marketplace mp on ahcr.marketplace_id = mp.id " +
                " where  " +
                " ahcr.marketplace_id = :marketplaceId " +
                " and ( ahcr.campaign_name like :nameType1 " +
                " OR ahcr.campaign_name like :nameType2 ) ";

        if(campaignName != null && !campaignName.isEmpty())
            sql += "  and ahcr.campaign_name = :campaignName";
        if(start!=null)
            sql += " and ahcr.report_date >= :startDate ";
        if(end!=null)
            sql += " and ahcr.report_date <= :endDate ";

        // sql += " limit 100";

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



        List<HsaCampaignReportDetail> amazonHeadlineSearchAdReportItems = (List) getNamedParameterJdbcTemplate().query(sql,q,(rs,rowNum)->
                new HsaCampaignReportDetailImpl(
                        rs.getInt("id"),rs.getString("supplier_kcode"),rs.getString("marketplace_name"),
                        rs.getInt("marketplace_id"),rs.getString("campaign_name"),
                        rs.getString("report_date"),rs.getString("currency_name"),
                        rs.getInt("impressions"),rs.getInt("clicks"),rs.getBigDecimal("click_thru_rate"),
                        rs.getBigDecimal("cost_per_click"),rs.getBigDecimal("spend"),
                        rs.getBigDecimal("total_advertising_cost_of_sales"),
                        rs.getBigDecimal("total_return_on_advertising_spend"),
                        rs.getBigDecimal("total_sales_14days"),
                        rs.getInt("total_orders_14days"),rs.getInt("total_units_14days"),
                        rs.getBigDecimal("conversion_rate_14days")
                ));

        return amazonHeadlineSearchAdReportItems;


    }


}