package com.kindminds.drs.persist.data.access.rdb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

public abstract class Dao {

    @Value("#{dataSource}")
    //@Autowired
    protected DataSource dataSource;

    private JdbcTemplate jdbcTemplate = null;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate = null;


    protected RowMapper<Object []> objArrayMapper = (rs, rowNum) -> {
        int cols = rs.getMetaData().getColumnCount();
        Object[] arr = new Object[cols];
        for(int i=0; i<cols; i++){
            arr[i] = rs.getObject(i+1);
        }
        return arr;
    };


    public JdbcTemplate getJdbcTemplate() {
        if(this.jdbcTemplate == null){
            jdbcTemplate = new JdbcTemplate(dataSource);
        }
        return jdbcTemplate;
    }



    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
        if(this.namedParameterJdbcTemplate == null){
            namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        }
        return namedParameterJdbcTemplate;
    }


}
