package com.kindminds.drs.persist.data.access.usecase.logistics;

import java.util.List;





import com.kindminds.drs.persist.data.access.rdb.Dao;
import com.kindminds.drs.api.v1.model.replenishment.ReplenishmentTimeSpentInfo;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;


import com.kindminds.drs.api.data.access.usecase.logistics.MaintainReplenishmentTimeSpentInfoDao;

@Repository
public class MaintainReplenishmentTimeSpentInfoDaoImpl extends Dao implements MaintainReplenishmentTimeSpentInfoDao {
	


	@Override @SuppressWarnings("unchecked")
	public List<Object []> queryList() {
		String sql = "select          rtsi.warehouse_id as warehouse_id, "
				+ "                              m.name as warehouse_name, "
				+ "rtsi.days_spent_for_amazon_receiving as days_spent_for_amazon_receiving, "
				+ " rtsi.days_spent_for_spw_calculation as days_spent_for_spw_calculation, "
				+ "         rtsi.days_spent_for_courier as days_spent_for_courier, "
				+ "     rtsi.days_spent_for_air_freight as days_spent_for_air_freight, "
				+ " rtsi.days_spent_for_surface_freight as days_spent_for_surface_freight "
				+ "from replenishment_time_spent_info rtsi "
				+ "inner join marketplace m on m.id = rtsi.warehouse_id "
				+ "order by rtsi.warehouse_id ";

		List<Object[]> columnsList = getJdbcTemplate().query(sql,objArrayMapper);
		return columnsList;
	}

	@Override @Transactional("transactionManager")
	public void update(List<ReplenishmentTimeSpentInfo> infoList) {
		String sql = "update replenishment_time_spent_info rtsi set "
				+ "days_spent_for_amazon_receiving = :days_spent_for_amazon_receiving, "
				+ " days_spent_for_spw_calculation = :days_spent_for_spw_calculation, "
				+ "         days_spent_for_courier = :days_spent_for_courier, "
				+ "     days_spent_for_air_freight = :days_spent_for_air_freight, "
				+ " days_spent_for_surface_freight = :days_spent_for_surface_freight "
				+ "from marketplace w "
				+ "where w.id = rtsi.warehouse_id and w.id = :warehouse_id ";
		for(ReplenishmentTimeSpentInfo info:infoList){
			MapSqlParameterSource q = new MapSqlParameterSource();
			q.addValue("days_spent_for_amazon_receiving",info.getDaysSpentForAmazonReceiving());
			q.addValue("days_spent_for_spw_calculation",info.getDaysSpentForSpwCalculation());
			q.addValue("days_spent_for_courier",info.getDaysSpentForCourier());
			q.addValue("days_spent_for_air_freight",info.getDaysSpentForAirFreight());
			q.addValue("days_spent_for_surface_freight",info.getDaysSpentForSurfaceFreight());
			q.addValue("warehouse_id",info.getWarehouseId());
			int updatedRows = getNamedParameterJdbcTemplate().update(sql,q);
			Assert.isTrue(updatedRows==1);
		}
		return;
	}

}
