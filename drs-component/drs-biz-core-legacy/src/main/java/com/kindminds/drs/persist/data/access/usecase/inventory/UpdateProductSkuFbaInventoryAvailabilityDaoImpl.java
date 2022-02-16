package com.kindminds.drs.persist.data.access.usecase.inventory;

import java.util.Date;
import java.util.List;





import com.kindminds.drs.persist.data.access.rdb.Dao;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.kindminds.drs.api.data.access.usecase.inventory.UpdateProductSkuFbaInventoryAvailabilityDao;
import com.kindminds.drs.api.v1.model.product.ProductSkuFbaInventoryAvailabilityLine;

@Repository
public class UpdateProductSkuFbaInventoryAvailabilityDaoImpl extends Dao implements UpdateProductSkuFbaInventoryAvailabilityDao {
	


	@Override
	public Boolean selectIfNullDataExist(Date date) {
		String sql = "select exists("
				+ "        select 1 from product_sku_inventory_history h "
				+ "        where true "
				+ "        and h.snapshot_date = :date "
				+ "        and h.not_out_of_stock is null "
				+ ") ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("date", date);
		return getNamedParameterJdbcTemplate().queryForObject(sql,q,Boolean.class);
	}

	@Override @Transactional("transactionManager")
	public int updateProductSkuInventoryHistoryAvailability(Date date,List<ProductSkuFbaInventoryAvailabilityLine> lines) {
		String sql = "update product_sku_inventory_history h set not_out_of_stock = :isAvailable "
				+ "from product_sku ps, marketplace w "
				+ "where h.product_sku_id = ps.id "
				+ "and h.warehouse_id = w.id "
				+ "and ps.code_by_drs = :sku "
				+ "and w.id = :warehouseId "
				+ "and h.snapshot_date = :date ";
		int updatedRowCounts = 0;

		for(ProductSkuFbaInventoryAvailabilityLine line:lines){
//			System.out.println("isAvailable: " + line.isAvailable());
//			System.out.println("getSku: " + line.getSku());
//			System.out.println("getWarehouseId: " + line.getWarehouseId());
//			System.out.println("date: " + date);
			MapSqlParameterSource q = new MapSqlParameterSource();
			q.addValue("isAvailable", line.isAvailable());
			q.addValue("sku",line.getSku());
			q.addValue("warehouseId", line.getWarehouseId());
			q.addValue("date", date);
			int affectedRows = getNamedParameterJdbcTemplate().update(sql,q);
			Assert.isTrue(affectedRows<=1, "affectedRows: " + affectedRows);
			updatedRowCounts+=affectedRows;
		}
		return updatedRowCounts;
	}

	@Override @Transactional("transactionManager")
	public int updateProductSkuInventoryHistoryAvailabilityWithNullValue(Date date, Boolean newValue) {
		String sql = "update product_sku_inventory_history h set not_out_of_stock = :newValue "
				+ "where h.not_out_of_stock is null "
				+ "and h.snapshot_date = :date ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("newValue",newValue);
		q.addValue("date", date);
		return getNamedParameterJdbcTemplate().update(sql,q);
	}

	@Override @SuppressWarnings("unchecked")
	public List<Date> queryDatesWithAvailabilityNull(Date start, Date end) {
		String sql = "select distinct snapshot_date "
				+ "from product_sku_inventory_history h "
				+ "where h.not_out_of_stock is null "
				+ "and h.snapshot_date >= :start "
				+ "and h.snapshot_date <  :end ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("start", start);
		q.addValue("end", end);
		return getNamedParameterJdbcTemplate().queryForList(sql,q,Date.class);
	}

	@Override @Transactional("transactionManager") 
	public int updateProductSkuInventoryHistoryAvailabilityAsNull(Date date) {
		String sql = "update product_sku_inventory_history h set not_out_of_stock = null "
				+ "where snapshot_date = :date ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("date", date);
		return getNamedParameterJdbcTemplate().update(sql,q);
	}

	@Override @SuppressWarnings("unchecked")
	public List<Date> queryDatesWithAvailabilityNotNull(Date start, Date end) {
		String sql = "select distinct snapshot_date "
				+ "from product_sku_inventory_history h "
				+ "where h.not_out_of_stock is not null "
				+ "and h.snapshot_date >= :start "
				+ "and h.snapshot_date <  :end ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("start", start);
		q.addValue("end", end);
		return getNamedParameterJdbcTemplate().queryForList(sql,q,Date.class);
	}

}
