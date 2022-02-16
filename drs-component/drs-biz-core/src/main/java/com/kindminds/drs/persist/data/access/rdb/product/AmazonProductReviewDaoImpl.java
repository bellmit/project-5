package com.kindminds.drs.persist.data.access.rdb.product;

import com.kindminds.drs.api.data.transfer.product.AmazonProductReviewDto;
import com.kindminds.drs.api.v2.biz.domain.model.SalesChannel;
import com.kindminds.drs.persist.data.access.rdb.Dao;


import com.kindminds.drs.api.data.access.rdb.product.AmazonProductReviewDao;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.*;
import java.util.ArrayList;
import java.util.List;




@Repository
public class AmazonProductReviewDaoImpl extends Dao implements AmazonProductReviewDao {

    
    public List<AmazonProductReviewDto> getAmazonProductReviews(String kcode , String marketPlace ,
                                                                String bpCode  , String skuCode)  {


        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append("select date_created , star_rating, title , sku from pv.amazon_review ");
        sqlSb.append(" where  date_created >= :dt ");


        if( kcode != "All") sqlSb.append(" and k_code = :kcode ");
        sqlSb.append(" and bp = :bp ");
        sqlSb.append(" and sku = :sku ");
        if(marketPlace != "All") sqlSb.append(" and marketplace = :mp ");

        sqlSb.append(" order by date_created desc ");

        if( kcode == "All"){ sqlSb.append(" limit 10 "); }

       MapSqlParameterSource q = new MapSqlParameterSource();

        ZonedDateTime sd =   ZonedDateTime.now(ZoneOffset.UTC).withDayOfMonth(1)
                .withHour(0).withMinute(0).
                        withSecond(0).withNano(0);

        q.addValue("dt", Timestamp.from(sd.toInstant()));

        if( kcode != "All"){ q.addValue("kcode", kcode) ;}
        q.addValue("bp", bpCode);
        q.addValue("sku", skuCode);
        if(marketPlace != "All")q.addValue("mp",
                SalesChannel.fromKey(Integer.parseInt(marketPlace)).getDisplayName());

        List pList = new ArrayList<AmazonProductReviewDto>();
        List<Object []> result = getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,objArrayMapper);
        for (Object items : result) {

            Object [] r = (Object []) items ;
            pList.add( new AmazonProductReviewDto(r[0].toString() , new BigDecimal(r[1].toString()) ,
                    r[2].toString() , r[3].toString()));

        }
        return pList;


    }




}