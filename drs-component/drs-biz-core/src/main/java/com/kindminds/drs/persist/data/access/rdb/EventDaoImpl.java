package com.kindminds.drs.persist.data.access.rdb;

import com.kindminds.drs.api.data.access.rdb.EventDao;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;

@Repository
public class EventDaoImpl  extends Dao implements EventDao {

    @Override
    public void insert(String name , String result) {

        String sql = "INSERT INTO event " +
                " (name, event_data, date_created) " +
                "VALUES(:name, :evtData, :dateCreated)";

        MapSqlParameterSource q = new MapSqlParameterSource();
        q.addValue("name", name);
        q.addValue("evtData", result);
        q.addValue("dateCreated", Timestamp.valueOf(OffsetDateTime.now()
                .atZoneSameInstant(ZoneId.of("Asia/Taipei")).toLocalDateTime()));

        int insertedRows = getNamedParameterJdbcTemplate().update(sql,q);
        Assert.isTrue(insertedRows==1);

    }
}
