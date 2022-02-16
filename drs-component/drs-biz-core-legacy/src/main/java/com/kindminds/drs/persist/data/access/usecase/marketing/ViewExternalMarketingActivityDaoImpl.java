package com.kindminds.drs.persist.data.access.usecase.marketing;

import com.kindminds.drs.api.data.access.usecase.marketing.ViewExternalMarketingActivityDao;
import com.kindminds.drs.persist.data.access.rdb.Dao;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;


import java.util.*;

@Repository
public class ViewExternalMarketingActivityDaoImpl extends Dao implements ViewExternalMarketingActivityDao {



    @Override @SuppressWarnings("unchecked")
    public List<Object []> querySkuList() {
        String sql = "SELECT cty.code as marketplace, com.k_code, pmi.marketplace_sku as sku_code " +
                " FROM product_marketplace_info pmi " +
                " inner join product pd on pd.id = pmi.product_id " +
                " inner join product_sku ps on ps.product_id = pd.id " +
                " inner join product_base pb on pb.id = ps.product_base_id " +
                " inner join company com on com.id = pb.supplier_company_id " +
                " inner join marketplace mp on mp.id = pmi.marketplace_id " +
                " inner join country cty on cty.id = mp.country_id " +
                " WHERE pmi.status = 'Live' " +
                " AND mp.id != 2 " +
                " ORDER BY marketplace_sku, marketplace_id ";

        return getJdbcTemplate().query(sql,objArrayMapper);
    }

    @Override @SuppressWarnings("unchecked")
    public List<Object []> querySalesData(String marketplace, String skuCode) {
        String asin = queryAsin(marketplace, skuCode);
        if (asin == null) return new ArrayList<>();
        String sql = "SELECT report_date, ordered_product_sales " +
                " FROM amazon_detail_page_sales_traffic_report_by_childitem_daily adpstrc  " +
                " INNER JOIN marketplace mp on mp.id = adpstrc.marketplace_id " +
                " INNER JOIN country cty on cty.id = mp.country_id " +
                " WHERE cty.code = :marketplace  " +
                " AND child_asin = :asin " +
                " order by report_date ";
       MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("marketplace", marketplace);
        q.addValue("asin", asin);

        return getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
    }

    @SuppressWarnings("unchecked")
    private String queryAsin(String marketplace, String skuCode) {
        String sql = "SELECT pmia.asin " +
                " FROM product_marketplace_info_amazon pmia " +
                " INNER JOIN product_marketplace_info pmi on pmi.id = pmia.product_marketplace_info_id " +
                " INNER JOIN product_sku ps on ps.product_id = pmi.product_id " +
                " INNER JOIN marketplace mp on mp.id = pmi.marketplace_id " +
                " INNER JOIN country cty on cty.id = mp.country_id " +
                " WHERE cty.code = :marketplace " +
                " AND ps.code_by_drs = :skuCode ";
       MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("marketplace", marketplace);
        q.addValue("skuCode", skuCode);
        List<String> result = getNamedParameterJdbcTemplate().queryForList(sql,q, String.class);
        if (result.isEmpty()) return null;
        return result.get(0);
    }

    @Override @SuppressWarnings("unchecked")
    public List<Object []> queryActivityData(String marketplace, String skuCode) {
        String sql = "SELECT activity_type, start_date, activity_name " +
                " FROM external_marketing_activity ema " +
                " INNER JOIN marketplace mp on mp.id = ema.marketplace_id " +
                " INNER JOIN country cty on cty.id = mp.country_id " +
                " WHERE cty.code = :marketplace " +
                " AND drs_sku = :skuCode ";
       MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("marketplace", marketplace);
        q.addValue("skuCode", skuCode);

        return getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
    }

    @Override @SuppressWarnings("unchecked")
    public List<Object []> queryAmazonAdData(String marketplace, String skuCode) {
        String sql = " SELECT campaign_name, MIN(start_date) " +
                " FROM amazon_campaign_performance_report acpr " +
                " INNER JOIN marketplace mp on mp.id = acpr.marketplace_id " +
                " INNER JOIN country cty on cty.id = mp.country_id " +
                " WHERE cty.code = :marketplace " +
                " AND advertised_sku = :skuCode " +
                " GROUP BY campaign_name";
       MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("marketplace", marketplace);
        q.addValue("skuCode", skuCode);

        return getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
    }

}
