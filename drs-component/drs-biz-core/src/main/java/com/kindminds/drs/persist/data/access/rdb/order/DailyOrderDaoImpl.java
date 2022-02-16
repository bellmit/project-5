package com.kindminds.drs.persist.data.access.rdb.order;

import com.kindminds.drs.Criteria;
import com.kindminds.drs.DailyOrderQueryField;
import com.kindminds.drs.Filter;
import com.kindminds.drs.persist.data.access.rdb.Dao;
import com.kindminds.drs.api.data.access.rdb.order.DailyOrderDao;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Repository
public class DailyOrderDaoImpl extends Dao implements DailyOrderDao {


    public int queryDailyOrderByFilter(Filter filter){
        MapSqlParameterSource q = new MapSqlParameterSource();

        for (Criteria it: filter.getCriteriaList()) {
            if(it.field == DailyOrderQueryField.KCode){
                if(it.value != "All") q.addValue("kcode", it.value);
            }

            if(it.field == DailyOrderQueryField.StartDate){

                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                        .withZone(ZoneOffset.UTC);
                Timestamp t = Timestamp.from(ZonedDateTime.parse(it.value, df).toInstant());

                q.addValue("startDate", t);
            }

            if(it.field == DailyOrderQueryField.EndDate){

                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                        .withZone(ZoneOffset.UTC);
                Timestamp t = Timestamp.from(ZonedDateTime.parse(it.value, df).toInstant());

                q.addValue("endDate", t);
            }

            if(it.field == DailyOrderQueryField.BpCode){
                q.addValue("bp", it.value);
            }

            if(it.field == DailyOrderQueryField.SkuCode){
                q.addValue("sku", it.value);
            }

            if(it.field == DailyOrderQueryField.Marketplace){
                if(it.value != "All")  q.addValue("sc", it.value);
            }
        }

        int ordersCount = getNamedParameterJdbcTemplate().queryForObject(doFilter(filter),q,Integer.class);

        return ordersCount;
    }


    private String doFilter(Filter filter){

        String sql = "Select count(*) "+
                " FROM sales.all_orders "+
                " where 1=1 ";

        for (Criteria it: filter.getCriteriaList()) {
            if(it.field == DailyOrderQueryField.KCode){
                if(it.value != "All") sql += "and com_code =  :kcode";
            }

            if(it.field == DailyOrderQueryField.StartDate){
                sql += " and order_time >=  :startDate";
            }
            if(it.field == DailyOrderQueryField.EndDate){
                sql += " and order_time <  :endDate";
            }

            if(it.field == DailyOrderQueryField.BpCode){
                sql +=    "  and base_code = :bp ";
            }

            if(it.field == DailyOrderQueryField.SkuCode){
                sql += " and sku_code  = :sku ";
            }

            if(it.field == DailyOrderQueryField.Marketplace){
                if(it.value != "All")  sql += " and sales_channel  = :sc ";

            }
        }

        return sql;
    }
}
