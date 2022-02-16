package com.kindminds.drs.persist.data.access.rdb.ec;






import com.kindminds.drs.api.data.access.rdb.ec.AutoProcessECReportDao;
import com.kindminds.drs.api.data.transfer.ec.*;
import com.kindminds.drs.persist.data.access.rdb.Dao;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;






import java.sql.Timestamp;
import java.time.LocalDateTime;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.fasterxml.uuid.Generators;
import org.springframework.transaction.annotation.Transactional;


@Repository
public class AutoProcessECReportDaoImpl extends Dao implements AutoProcessECReportDao {

    
    private final String sql = "insert into auto_process_ec_report " +
            "(id, create_time, marketplace_id , scheduled_date , report_type , " +
            " started , downloaded ,correct_file ,  save_to_hdfs , " +
            " import_to_db , import_to_hbase  ) values " +
            " (:id, :ct, :mid , :sdate , :rt , :s , :d , :cf , :sh , :itd , :ith) ";

    @Override
    @Transactional("transactionManager")
    public void save(AutoProcessECReport autoProcessECReport) {

        Optional<String> id = this.getAutoProcessECReportId(autoProcessECReport);


        if(autoProcessECReport instanceof StartProcessECReport){
            doStartProcessECReport(id , autoProcessECReport);
        }else if(autoProcessECReport instanceof DownloadECReport){
            doDownloadECReport(id , autoProcessECReport);
        }else if(autoProcessECReport instanceof ValidateECReport){
            doValidateECReport(id , autoProcessECReport);
        }else if(autoProcessECReport instanceof SaveECReportToHDFS){
            doSaveECReportToHDFS(id , autoProcessECReport);
        }else if(autoProcessECReport instanceof ImportECReportToHbase){
            doImportECReportToHbase(id , autoProcessECReport);
        }else if(autoProcessECReport instanceof ImportECReportToDRSDB){
            doImportECReportToDRSDB(id , autoProcessECReport);
        }

    }

    @Override
    public List<ProcessResult> getProcessResultList(int getReportType, ZonedDateTime startTime ,
                                                    ZonedDateTime endTime) {

        String sql = "select * from ( "
                +" SELECT id, create_time ,  rank() OVER (PARTITION BY id ORDER BY create_time DESC) as r , "
                +" marketplace_id , scheduled_date , report_type , started , downloaded, "
                +" correct_file,save_to_hdfs,import_to_db,import_to_hbase "
                +" FROM auto_process_ec_report "
                +" where report_type = :rt and create_time > :qd and create_time < :qde  ) "
                +" as sb where r = 1 ";

        MapSqlParameterSource q = new MapSqlParameterSource();

        q.addValue("rt", getReportType);
        q.addValue("qd", Timestamp.from(startTime.toInstant()));
        q.addValue("qde",  Timestamp.from(endTime.toInstant()));

        List<Object[]> resultList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);

        List<ProcessResult> prList = new ArrayList<ProcessResult>();
        for (Object[] objs:resultList) {

            java.sql.Timestamp ct = (java.sql.Timestamp)objs[1];
            java.sql.Date sd = (java.sql.Date)objs[4];


            prList.add(new com.kindminds.drs.api.data.transfer.ec.ProcessResult(objs[0].toString(),
                    ct.toLocalDateTime(),
                    Integer.parseInt(objs[3].toString()),
                    sd.toLocalDate(),
                    Integer.parseInt(objs[5].toString()),
                    BooleanUtils.toBoolean(objs[6].toString()) ,
                    BooleanUtils.toBoolean(objs[7].toString()),
                    BooleanUtils.toBoolean(objs[8].toString()) ,
                    BooleanUtils.toBoolean(objs[9].toString()) ,
                    (objs[10] != null ? objs[10].toString().equals("1") ? true : false : false) ,
                    (objs[11] != null ? objs[11].toString().equals("1") ? true : false  : false )));

        }


        return prList;
    }


    private Optional<String> getAutoProcessECReportId(AutoProcessECReport ap){

        String sql = "select id "
                + "from auto_process_ec_report "
                + " where  marketplace_id = :mid and scheduled_date = :sdate " +
                " and report_type = :rt order by create_time desc LIMIT 1";

        MapSqlParameterSource q = new MapSqlParameterSource();

        q.addValue("mid", ap.getMarketPlaceId());
        q.addValue("sdate", java.sql.Date.valueOf(ap.getScheduledDate()));
        q.addValue("rt", ap.getReportType());

        List<String> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);


        return resultList.size() > 0 ? Optional.of(resultList.get(0)) : Optional.empty();

    }


    private void doStartProcessECReport(Optional<String> id , AutoProcessECReport ap ){


        String procId = id.isPresent() ? id.get() : Generators.randomBasedGenerator().generate().toString();

       MapSqlParameterSource q = new MapSqlParameterSource();

        q.addValue("id", procId);
        q.addValue("ct",  Timestamp.valueOf(LocalDateTime.now()));
        q.addValue("mid", ap.getMarketPlaceId());
        q.addValue("sdate", java.sql.Date.valueOf(ap.getScheduledDate()));
        q.addValue("rt", ap.getReportType());

        q.addValue("s", BooleanUtils.toInteger(ap.getSuccess()));
        q.addValue("d", "0");
        q.addValue("cf", "0");
        q.addValue("sh", "0");
        q.addValue("itd", "0");
        q.addValue("ith", "0");


       getNamedParameterJdbcTemplate().update(sql,q);


    }


    private void doValidateECReport(Optional<String> id , AutoProcessECReport ap){


        String procId = id.isPresent() ? id.get() : Generators.randomBasedGenerator().generate().toString();

       MapSqlParameterSource q = new MapSqlParameterSource();

        q.addValue("id", procId);
        q.addValue("ct",  Timestamp.valueOf(LocalDateTime.now()));
        q.addValue("mid", ap.getMarketPlaceId());
        q.addValue("sdate", java.sql.Date.valueOf(ap.getScheduledDate()));
        q.addValue("rt", ap.getReportType());
        q.addValue("s", "1");
        q.addValue("d", "1");
        q.addValue("cf", BooleanUtils.toInteger(ap.getSuccess()));
        q.addValue("sh", "0");
        q.addValue("itd", "0");
        q.addValue("ith", "0");


        getNamedParameterJdbcTemplate().update(sql,q);


    }

    private void doSaveECReportToHDFS(Optional<String> id , AutoProcessECReport ap){


        String procId = id.isPresent() ? id.get() : Generators.randomBasedGenerator().generate().toString();

       MapSqlParameterSource q = new MapSqlParameterSource();

        q.addValue("id", procId);
        q.addValue("ct",  Timestamp.valueOf(LocalDateTime.now()));
        q.addValue("mid", ap.getMarketPlaceId());
        q.addValue("sdate", java.sql.Date.valueOf(ap.getScheduledDate()));
        q.addValue("rt", ap.getReportType());
        q.addValue("s", "1");
        q.addValue("d","1");
        q.addValue("cf", "1");
        q.addValue("sh", BooleanUtils.toInteger(ap.getSuccess()));
        q.addValue("itd", "0");
        q.addValue("ith", "0");


        getNamedParameterJdbcTemplate().update(sql,q);


    }

    private void doImportECReportToHbase(Optional<String> id , AutoProcessECReport ap){


        String procId = id.isPresent() ? id.get() : Generators.randomBasedGenerator().generate().toString();

       MapSqlParameterSource q = new MapSqlParameterSource();

        q.addValue("id", procId);
        q.addValue("ct",  Timestamp.valueOf(LocalDateTime.now()));
        q.addValue("mid", ap.getMarketPlaceId());
        q.addValue("sdate", java.sql.Date.valueOf(ap.getScheduledDate()));
        q.addValue("rt", ap.getReportType());
        q.addValue("s", "1");
        q.addValue("d", "1");
        q.addValue("cf", "1");
        q.addValue("sh", "1");
        q.addValue("itd", null);
        q.addValue("ith", BooleanUtils.toInteger(ap.getSuccess()));


        getNamedParameterJdbcTemplate().update(sql,q);


    }

    private void doDownloadECReport(Optional<String> id , AutoProcessECReport ap){


        String procId = id.isPresent() ? id.get() : Generators.randomBasedGenerator().generate().toString();

       MapSqlParameterSource q = new MapSqlParameterSource();

        q.addValue("id", procId);
        q.addValue("ct",  Timestamp.valueOf(LocalDateTime.now()));
        q.addValue("mid", ap.getMarketPlaceId());
        q.addValue("sdate", java.sql.Date.valueOf(ap.getScheduledDate()));
        q.addValue("rt", ap.getReportType());
        q.addValue("s", "1");
        q.addValue("d", BooleanUtils.toInteger(ap.getSuccess()));
        q.addValue("cf", "0");
        q.addValue("sh", "0");
        q.addValue("itd", "0");
        q.addValue("ith", "0");


        getNamedParameterJdbcTemplate().update(sql,q);


    }

    private void doImportECReportToDRSDB(Optional<String> id , AutoProcessECReport ap){


        String procId = id.isPresent() ? id.get() : Generators.randomBasedGenerator().generate().toString();

       MapSqlParameterSource q = new MapSqlParameterSource();

        q.addValue("id", procId);
        q.addValue("ct",  Timestamp.valueOf(LocalDateTime.now()));
        q.addValue("mid", ap.getMarketPlaceId());
        q.addValue("sdate", java.sql.Date.valueOf(ap.getScheduledDate()));
        q.addValue("rt", ap.getReportType());

        q.addValue("s", "1");
        q.addValue("d","1");
        q.addValue("cf", "1");
        q.addValue("sh", "1");
        q.addValue("itd", BooleanUtils.toInteger(ap.getSuccess()));

        q.addValue("ith", null);


        getNamedParameterJdbcTemplate().update(sql,q);


    }

}
