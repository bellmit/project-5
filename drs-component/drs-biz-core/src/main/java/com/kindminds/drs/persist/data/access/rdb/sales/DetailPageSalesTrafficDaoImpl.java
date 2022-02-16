package com.kindminds.drs.persist.data.access.rdb.sales;

import com.kindminds.drs.Criteria;
import com.kindminds.drs.Filter;
import com.kindminds.drs.KeyProductStatsQueryField;
import com.kindminds.drs.api.data.access.rdb.sales.DetailPageSalesTrafficDao;
import com.kindminds.drs.persist.data.access.rdb.Dao;
import com.kindminds.drs.persist.data.row.sales.DetailPageSalesTrafficImpl;
import com.kindminds.drs.api.v2.biz.domain.model.sales.DetailPageSalesTraffic;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class DetailPageSalesTrafficDaoImpl extends Dao implements DetailPageSalesTrafficDao {

    @Override
    public DetailPageSalesTraffic queryPageSalesTraffic(Filter filter, Date from, Date to){

        String sql ="select sum(dpst.sessions) as totalSessions, " +
                "sum(dpst.page_views) as totalPageViews, " +
                "sum(dpst.sessions * dpst.buy_box_percentage) as buyBoxPercentage " +
                "FROM amazon_detail_page_sales_traffic_report_by_childitem_daily dpst " +
                "inner join product_marketplace_info_amazon pmia on pmia.asin = dpst.child_asin " +
                "inner join product_marketplace_info pmi on pmi.id = pmia.product_marketplace_info_id " +
                "inner join product_sku ps on pmi.product_id =ps.product_id " +
                "inner join product_base pb on pb.id = ps.product_base_id " +
                "inner join company c on c.id = pb.supplier_company_id " +
                "inner join marketplace m on m.id =pmi.marketplace_id " +
                "where pmi.status='Live' " +
                "and dpst.report_date >= :startDate " +
                "and dpst.report_date < :endDate ";

        MapSqlParameterSource q = new MapSqlParameterSource();

        for (Criteria it: filter.getCriteriaList()) {
            if(it.field == KeyProductStatsQueryField.KCode){
                if(it.value != "All") q.addValue("kcode", it.value);
            }

            if(it.field == KeyProductStatsQueryField.BpCode){
                q.addValue("bp", it.value);
            }

            if(it.field == KeyProductStatsQueryField.SkuCode){
                q.addValue("sku", it.value);
            }

            if(it.field == KeyProductStatsQueryField.Marketplace){
                if(it.value != "All")  q.addValue("sc", it.value);
            }
        }
        q.addValue("startDate",from);
        q.addValue("endDate",to);

        DetailPageSalesTrafficImpl result = getNamedParameterJdbcTemplate().queryForObject(doFilter(sql,filter),q,
                (rs,rowNum) -> new DetailPageSalesTrafficImpl(
                        rs.getInt("totalSessions"),rs.getInt("totalPageViews"),
                        rs.getBigDecimal("buyBoxPercentage")
                ));

        return result;
    }

    private String doFilter(String sql , Filter filter){
        for (Criteria it: filter.getCriteriaList()) {
            if(it.field == KeyProductStatsQueryField.KCode){
                if(it.value != "All") sql += "and c.k_code =  :kcode ";
            }


            if(it.field == KeyProductStatsQueryField.BpCode){
                sql +=    "  and pb.code_by_drs = :bp ";
            }

            if(it.field == KeyProductStatsQueryField.SkuCode){
                sql += " and ps.code_by_drs  = :sku ";
            }

            if(it.field == KeyProductStatsQueryField.Marketplace){
                if(it.value != "All")  sql += " and m.name  = :sc ";

            }
        }
        return sql;
    }
}
