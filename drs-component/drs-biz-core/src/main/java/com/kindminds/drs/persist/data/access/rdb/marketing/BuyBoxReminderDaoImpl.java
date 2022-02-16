package com.kindminds.drs.persist.data.access.rdb.marketing;


import com.kindminds.drs.api.data.access.rdb.marketing.BuyBoxReminderDao;
import com.kindminds.drs.persist.data.access.rdb.Dao;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;


import java.time.Instant;
import java.util.Date;
import java.util.List;

@Repository
public class BuyBoxReminderDaoImpl extends Dao implements BuyBoxReminderDao {

    //@PersistenceContext(unitName = "default")  private EntityManager entityManager;

    @Override
    public Instant queryLatestReportDate() {
        String sql = "SELECT max(report_date) " +
                " from amazon_detail_page_sales_traffic_report_by_childitem_daily ";

        return getJdbcTemplate().queryForObject(sql,Date.class).toInstant();
    }

    @Override @SuppressWarnings("unchecked")
    public List<Object []> queryLowBuyBoxSkus(Instant reportDate) {
        String sql = "SELECT * FROM ( \n" +
                "    SELECT adp.report_date, \n" +
                "        mp.name as marketplace_name, \n" +
                "        cpy.k_code as company_code, \n" +
                "        cpy.short_name_en_us, \n" +
                "        ps.code_by_drs as sku_code,\n" +
                "        pb.code_by_drs as base_code, \n" +
                "        CASE WHEN sum(sessions) = 0 THEN 0\n" +
                "            ELSE sum(sessions * buy_box_percentage) / sum(sessions)\n" +
                "        END as buy_box_rate\n" +
                "    FROM public.amazon_detail_page_sales_traffic_report_by_childitem_daily adp\n" +
                "    INNER JOIN product_marketplace_info_amazon pmia ON pmia.asin = adp.child_asin\n" +
                "    INNER JOIN product_marketplace_info pmi ON pmi.id = pmia.product_marketplace_info_id\n" +
                "    INNER JOIN product_sku ps ON ps.product_id = pmi.product_id \n" +
                "    INNER JOIN product_base pb ON ps.product_base_id = pb.id\n" +
                "    INNER JOIN company cpy ON cpy.id = pb.supplier_company_id\n" +
                "    INNER JOIN marketplace mp ON mp.id = pmi.marketplace_id\n" +
                "    WHERE report_date = :reportDate \n" +
                "    AND adp.marketplace_id = pmi.marketplace_id\n" +
                "    GROUP BY report_date, mp.name, cpy.k_code, cpy.short_name_en_us, pb.code_by_drs, ps.code_by_drs\n" +
                "    UNION\n" +
                "    SELECT adp.report_date, \n" +
                "        mp.name as marketplace_name, \n" +
                "        cpy.k_code as company_code, \n" +
                "        cpy.short_name_en_us, \n" +
                "        ps.code_by_drs as sku_code,\n" +
                "        pb.code_by_drs as base_code, \n" +
                "        CASE WHEN sum(sessions) = 0 THEN 0\n" +
                "            ELSE sum(sessions * buy_box_percentage) / sum(sessions)\n" +
                "        END as buy_box_rate\n" +
                "    FROM public.amazon_detail_page_sales_traffic_report_by_childitem_daily adp\n" +
                "    INNER JOIN product_K101_marketplace_info_amazon pmia ON pmia.asin = adp.child_asin\n" +
                "    INNER JOIN product_K101_marketplace_info pmi ON pmi.id = pmia.product_marketplace_info_id\n" +
                "    INNER JOIN product_sku ps ON ps.product_id = pmi.product_id \n" +
                "    INNER JOIN product_base pb ON ps.product_base_id = pb.id\n" +
                "    INNER JOIN company cpy ON cpy.id = pb.supplier_company_id\n" +
                "    INNER JOIN marketplace mp ON mp.id = pmi.marketplace_id\n" +
                "    WHERE report_date = :reportDate \n" +
                "    AND adp.marketplace_id = pmi.marketplace_id\n" +
                "    GROUP BY report_date, mp.name, cpy.k_code, cpy.short_name_en_us, pb.code_by_drs, ps.code_by_drs\n" +
                "    ) buy_box_info\n" +
                "WHERE buy_box_rate < .95\n" +
                "order by company_code, sku_code, marketplace_name";
        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("reportDate", reportDate);
        List<Object[]> resultList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);

        return resultList;
    }

}
