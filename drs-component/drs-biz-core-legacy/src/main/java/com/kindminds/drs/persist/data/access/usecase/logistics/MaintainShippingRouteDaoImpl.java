package com.kindminds.drs.persist.data.access.usecase.logistics;

import java.util.Date;
import java.util.List;


import com.kindminds.drs.persist.data.access.rdb.Dao;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShippingMethod;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import com.kindminds.drs.Country;
import com.kindminds.drs.api.data.access.usecase.logistics.MaintainShippingRouteDao;

@Repository
public class MaintainShippingRouteDaoImpl extends Dao implements MaintainShippingRouteDao{


	
	@Override @SuppressWarnings("unchecked")
	public Date queryShippingDate(Date startDate, Country exportCountry, Country marketCountry, ShippingMethod shippingMethod) {
		String sql = "select shipping_date "
				+ "from shipping_route_schedule_info srsi "
				+ "where srsi.shipping_route_id = "
				+ "( select id from shipping_route sr "
				+ "where sr.export_country_id = :exportCountryId and "
				+ "sr.market_country_id = :marketCountryId and "
				+ "sr.shipping_method_id = (select id from shipping_method sm where sm.name = :shippingMethod ) ) and "
				+ "srsi.ivs_deadline > :ivsDeadline " 
				+ "limit 1 ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("exportCountryId", exportCountry.getKey());
		q.addValue("marketCountryId", marketCountry.getKey());
		q.addValue("shippingMethod", shippingMethod.name());
		q.addValue("ivsDeadline", startDate);									
		List<Date> columnsList = getNamedParameterJdbcTemplate().queryForList(sql,q,Date.class);
		if (columnsList.size() ==0) return null;
		return columnsList.get(0);						
	}

	@Override @SuppressWarnings("unchecked")
	public Date queryShippingDate(Date startDate, Date endDate,Country exportCountry,Country marketCountry,ShippingMethod shippingMethod) {
		String sql = "select shipping_date "
				+ "from shipping_route_schedule_info srsi "
				+ "where srsi.shipping_route_id = "
				+ "( select id from shipping_route sr "
				+ "where sr.export_country_id = :exportCountryId and "
				+ "sr.market_country_id = :marketCountryId and "
				+ "sr.shipping_method_id = (select id from shipping_method sm where sm.name = :shippingMethod ) ) and "
				+ "srsi.ivs_deadline between :startDate and :endDate "
				+ "order by srsi.shipping_date desc "
				+ "limit 1 ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("exportCountryId", exportCountry.getKey());
		q.addValue("marketCountryId", marketCountry.getKey());
		q.addValue("shippingMethod", shippingMethod.name());		
		q.addValue("startDate", startDate);
		q.addValue("endDate", endDate);
		List<Date> columnsList = getNamedParameterJdbcTemplate().queryForList(sql,q,Date.class);
		if (columnsList.size() ==0) return null;
		return columnsList.get(0);
	}

}
