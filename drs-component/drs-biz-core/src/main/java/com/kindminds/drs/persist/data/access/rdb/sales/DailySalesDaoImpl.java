package com.kindminds.drs.persist.data.access.rdb.sales;


import com.kindminds.drs.Criteria;
import com.kindminds.drs.DailySalesQueryField;
import com.kindminds.drs.Filter;
import com.kindminds.drs.api.data.access.rdb.sales.DailySalesDao;
import com.kindminds.drs.api.data.row.sales.DailySalesRow;
import com.kindminds.drs.persist.data.access.rdb.Dao;
import com.kindminds.drs.persist.data.row.sales.DailySalesRowImpl;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;





@Repository
public class DailySalesDaoImpl extends Dao implements DailySalesDao{

    @Override
    public List<DailySalesRow> queryDailySalesByFilter(Filter filter )  {


        MapSqlParameterSource q = new MapSqlParameterSource();
        
        for (Criteria it: filter.getCriteriaList()) {
            if(it.field == DailySalesQueryField.KCode){
                if(it.value != "All") q.addValue("kcode", it.value);
            }

            if(it.field == DailySalesQueryField.SalesDate){

                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                        .withZone(ZoneOffset.UTC);
                Timestamp t = Timestamp.from(ZonedDateTime.parse(it.value, df).toInstant());

                q.addValue("salesDate", t);
            }

            if(it.field == DailySalesQueryField.BpCode){
                q.addValue("bp", it.value);
            }

            if(it.field == DailySalesQueryField.SkuCode){
                q.addValue("sku", it.value);
            }

            if(it.field == DailySalesQueryField.Marketplace){
                if(it.value != "All")  q.addValue("sc", it.value);
            }
        }

        List columnsList = getNamedParameterJdbcTemplate().query(doFilterSql(filter),q,(rs ,rowNum) -> new DailySalesRowImpl(
                rs.getInt("id"),rs.getString("k_code"),rs.getString("sales_date"),
                rs.getString("sales_channel"),rs.getString("product_base_code"),
                rs.getString("product_sku_code"),rs.getString("product_name"),
                rs.getString("asin"),rs.getBigDecimal("revenue"),
                rs.getBigDecimal("revenue_usd"),rs.getBigDecimal("gross_profit") ,
                rs.getBigDecimal("gross_profit_usd"),rs.getInt("qty")
        ));

        return columnsList;
    }

    private String doFilterSql(Filter filter  )  {

        String sql = "select id , k_code , " +
                " sales_date , sales_channel ,  product_base_code, product_sku_code, product_name, asin,  " +
                "  revenue, revenue_usd, gross_profit, gross_profit_usd, qty " +
                " from sales.daily_sales where 1=1 ";

        for (Criteria it: filter.getCriteriaList()) {
            if(it.field == DailySalesQueryField.KCode){
                if(it.value != "All") sql += "and k_code =  :kcode";
            }

            if(it.field == DailySalesQueryField.SalesDate){
                sql += " and sales_date =  :salesDate";
            }

            if(it.field == DailySalesQueryField.BpCode){
                sql +=    "  and product_base_code = :bp ";
            }

            if(it.field == DailySalesQueryField.SkuCode){
                sql += " and product_sku_code  = :sku ";
            }

            if(it.field == DailySalesQueryField.Marketplace){
                if(it.value != "All")  sql += " and sales_channel  = :sc ";

            }
        }




        return sql;

    }





}