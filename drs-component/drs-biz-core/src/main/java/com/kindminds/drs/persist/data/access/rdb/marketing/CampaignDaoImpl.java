package com.kindminds.drs.persist.data.access.rdb.marketing;

import com.kindminds.drs.CampaignQueryField;
import com.kindminds.drs.Criteria;
import com.kindminds.drs.Filter;
import com.kindminds.drs.api.data.row.marketing.CampaignRow;
import com.kindminds.drs.api.data.access.rdb.marketing.CampaignDao;
import com.kindminds.drs.persist.data.access.rdb.Dao;
import com.kindminds.drs.persist.data.row.marketing.CampaignRowImpl;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Repository
public class  CampaignDaoImpl extends Dao implements CampaignDao  {


    public List<CampaignRow> getCampaignByFilter(Filter filter )  {


        MapSqlParameterSource q = new MapSqlParameterSource();

        for (Criteria it: filter.getCriteriaList()) {
            if(it.field == CampaignQueryField.KCode){
                if(it.value != "All") q.addValue("kcode", it.value);
            }

            if(it.field == CampaignQueryField.Marketplace){
                if(it.value != "All")  q.addValue("mpId", Integer.parseInt(it.value));
            }

            if(it.field == CampaignQueryField.StartDate){

                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmmss")
                        .withZone(ZoneOffset.UTC);
                Timestamp t =
                        Timestamp.from(ZonedDateTime.parse(it.value, df).toInstant());

                q.addValue("sd", t);


            }

            if(it.field == CampaignQueryField.EndDate){

                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmmss")
                        .withZone(ZoneOffset.UTC);
                Timestamp t = Timestamp.from(ZonedDateTime.parse(it.value, df).toInstant());

                q.addValue("ed", t);

            }

            if(it.field == CampaignQueryField.BpCode){
                q.addValue("bp", it.value);
            }

            if(it.field == CampaignQueryField.SkuCode){
                q.addValue("sku", it.value);
            }

        }

        List<CampaignRow> columnsList = getNamedParameterJdbcTemplate().query(this.doFilterSql(filter),q ,
                (rs,rowNum) -> new CampaignRowImpl(
                        rs.getInt("id"),rs.getString("k_code"),rs.getString("campaign_name"),
                        rs.getString("start_date"),
                        rs.getBigDecimal("total_spend"),rs.getInt("one_week_ordered_product_sales")
                ));

        return columnsList;


    }

    private String doFilterSql(Filter filter  )  {

        String sql = "select acp.id , acp.k_code , acp.campaign_name , acp.start_date , acp.total_spend , " +
                " acp.one_week_ordered_product_sales " +
                " from pv.amazon_campaign_performance  acp " +
                " inner join pv.product_all_marketplace_info as pmi " +
                " on acp.advertised_sku = pmi.marketplace_sku  and pmi.marketplace_id = acp.marketplace_id " +
                " where 1=1   ";


        for (Criteria it: filter.getCriteriaList()) {

            if(it.field == CampaignQueryField.KCode){
                if(it.value != "All") sql += " and acp.k_code =  kcode";
            }

            if(it.field == CampaignQueryField.Marketplace){
                if(it.value != "All")   sql += " and acp.marketplace_id =  mpId";
            }

            if(it.field == CampaignQueryField.StartDate){
                sql +=    "  and acp.start_date >= sd ";
            }

            if(it.field == CampaignQueryField.EndDate){
                sql += " and acp.start_date <= ed ";
            }

            if(it.field == CampaignQueryField.BpCode){
                sql += "  and pmi.product_id in ( select p.id from product_base as pb " +
                        " inner join product_sku as ps on pb.id = ps.product_base_id " +
                        " inner join product as p on p.id = ps.product_id " +
                        " where pb.code_by_drs = bp ) ";
            }


            if(it.field == CampaignQueryField.SkuCode){
                sql += " and pmi.product_id in ( " +
                        "select p.id from  product_sku as ps " +
                        "inner join product as p on p.id = ps.product_id " +
                        " where ps.code_by_drs = sku) ";

            }

        }


        return sql;

    }
}