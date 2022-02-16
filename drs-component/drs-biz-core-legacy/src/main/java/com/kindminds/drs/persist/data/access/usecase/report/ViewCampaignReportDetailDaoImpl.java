package com.kindminds.drs.persist.data.access.usecase.report;

import com.kindminds.drs.api.data.access.usecase.report.ViewCampaignReportDetailDao;
import com.kindminds.drs.persist.data.access.rdb.Dao;
import com.kindminds.drs.api.v1.model.marketing.CampaignReportDetail;
import com.kindminds.drs.persist.v1.model.mapping.report.CampaignReportDetailImpl;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;




import java.util.List;

@Repository
public class ViewCampaignReportDetailDaoImpl extends Dao implements ViewCampaignReportDetailDao {



    @Override
    public List<CampaignReportDetail> generateCampaignReport(Integer settlementPeriodId) {
        String sql = "select " +
                " acpr.id as id, " +
                " splr.k_code as supplier_kcode, " +
                " m.\"name\" as marketplace_name, " +
                " acpr.campaign_name, " +
                " acpr.advertised_sku, " +
                " ps.code_by_drs AS \"sku\", " +
                " pb.code_by_drs AS \"bp\", " +
                " acpr.start_date as period_start_utc, " +
                " acpr.end_date as period_end_utc, " +
                " acpr.total_spend as total_spend, " +
                " acpr.currency as currency_name " +
                "from " +
                " amazon_campaign_performance_report acpr " +
                "inner join product_marketplace_info pmi on " +
                " pmi.marketplace_id = acpr.marketplace_id " +
                " and pmi.marketplace_sku = acpr.advertised_sku " +
                "inner join product_sku ps on " +
                " ps.product_id = pmi.product_id " +
                "inner join product_base pb on " +
                " pb.id = ps.product_base_id " +
                "inner join company splr on " +
                " splr.id = pb.supplier_company_id " +
                "inner join marketplace m on " +
                " m.id = acpr.marketplace_id " +
                "inner join settlement_period sp on " +
                " :period_id = sp.id " +
                "where " +
                " acpr.start_date >= sp.period_start " +
                " and acpr.start_date < sp.period_end " +
                "order by " +
                " splr.k_code, " +
                " m.\"name\", " +
                " acpr.campaign_name";
        
       MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("period_id", settlementPeriodId);


        List<CampaignReportDetail> list = (List) getNamedParameterJdbcTemplate().query(sql,q,(rs,rowNum) ->
                new CampaignReportDetailImpl(
                        rs.getInt("id"),rs.getString("supplier_kcode"),rs.getString("marketplace_name"),
                        rs.getString("campaign_name")
                        ,rs.getString("advertised_sku"),rs.getString("sku"),rs.getString("bp")
                        ,rs.getString("period_start_utc"),rs.getString("period_end_utc"),rs.getString("currency_name"),
                        rs.getBigDecimal("total_spend")

                ));
        if(list == null || list.isEmpty())
            return null;
        return list;
    }
}