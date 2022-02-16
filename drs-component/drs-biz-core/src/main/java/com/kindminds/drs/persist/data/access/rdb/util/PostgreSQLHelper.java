package com.kindminds.drs.persist.data.access.rdb.util;


import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;



public class PostgreSQLHelper {
	
	public static int getNextVal(NamedParameterJdbcTemplate jdbcTemplate, String tableName, String serialName) {
		String sql = "select nextval(pg_get_serial_sequence( :tableName, :serialName ))";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("tableName", tableName);
		q.addValue("serialName", serialName);
		return jdbcTemplate.queryForObject(sql,q ,int.class);
	}
}
