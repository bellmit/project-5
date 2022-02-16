package com.kindminds.drs.persist.data.access.rdb.customer;

import com.kindminds.drs.persist.data.access.rdb.Dao;
import com.kindminds.drs.api.v2.biz.domain.model.SalesChannel;
import com.kindminds.drs.api.data.transfer.customer.Return;
import com.kindminds.drs.api.data.access.rdb.customer.ReturnDao;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import java.sql.Timestamp;
import java.time.*;
import java.util.ArrayList;
import java.util.List;




@Repository
public class ReturnDaoImpl extends Dao implements ReturnDao {


    public List<Return> getReturns(String kcode , String marketPlace
                            , String bpCode , String skuCode ) {


        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append(" select Date(return_date) , order_id , sku_name_by_drs from pv.amazon_return ");
        sqlSb.append(" where return_date >= dt ");

        if( kcode != "All") sqlSb.append(" and k_code = :kcode ");
        sqlSb.append(" and bp = :bp ");
        sqlSb.append(" and sku = :sku ");
        if(marketPlace != "All") sqlSb.append(" and marketplace = :mp ");

        sqlSb.append(" order by return_date desc ");
        if( kcode == "All"){ sqlSb.append(" limit 10 "); }

        MapSqlParameterSource q = new MapSqlParameterSource();

        ZonedDateTime sd =   ZonedDateTime.now(ZoneOffset.UTC).withDayOfMonth(1).withHour(0).withMinute(0).
                withSecond(0).withNano(0);

        q.addValue("dt", Timestamp.from(sd.toInstant()));
        if( kcode != "All"){ q.addValue("kcode", kcode) ;}
        q.addValue("bp", bpCode);
        q.addValue("sku", skuCode);
        if(marketPlace != "All")q.addValue("mp",
                SalesChannel.fromKey(Integer.parseInt(marketPlace)).getDisplayName());

        List pList = new ArrayList<Return>();
        List<Object []> result = getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,objArrayMapper);
        for (Object[] r : result) {

            pList.add(new Return(
                    r[0].toString(),
                    r[1].toString(),
                    r[2].toString()));

        }
        return pList;


    }
}




