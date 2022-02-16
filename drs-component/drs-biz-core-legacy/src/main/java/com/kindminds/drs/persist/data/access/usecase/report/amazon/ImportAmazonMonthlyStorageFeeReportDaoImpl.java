package com.kindminds.drs.persist.data.access.usecase.report.amazon;

import java.util.List;


import com.kindminds.drs.api.v1.model.accounting.AmazonMonthlyStorageFeeReportRawLine;
import com.kindminds.drs.persist.data.access.rdb.Dao;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.kindminds.drs.Warehouse;
import com.kindminds.drs.api.data.access.usecase.report.amazon.ImportAmazonMonthlyStorageFeeReportDao;


@Repository
public class ImportAmazonMonthlyStorageFeeReportDaoImpl extends Dao implements ImportAmazonMonthlyStorageFeeReportDao{


	
	@Override @Transactional("transactionManager")
	public int insertRawLines(Warehouse sourceWarehouse, List<AmazonMonthlyStorageFeeReportRawLine> lineList) {
		String insertSql = "insert into amazon_monthly_storage_fee "
				//TODO: remove soon
				//+ "( asin,  fnsku,  product_name,  fulfillment_center,  country_code,  longest_side,  median_side,  shortest_side,  measurement_units,  weight,  weight_units,  item_volume,  volume_units,  average_quantity_on_hand,  average_quantity_pending_removal,  estimated_total_item_volume,  month_of_charge,  storage_rate,  currency,  estimated_monthly_storage_fee,  source_warehouse_id ) values "
				//+ "(:asin, :fnsku, :product_name, :fulfillment_center, :country_code, :longest_side, :median_side, :shortest_side, :measurement_units, :weight, :weight_units, :item_volume, :volume_units, :average_quantity_on_hand, :average_quantity_pending_removal, :estimated_total_item_volume, :month_of_charge, :storage_rate, :currency, :estimated_monthly_storage_fee, :source_warehouse_id) ";
				+ "( asin,  fnsku,  product_name,  fulfillment_center,  country_code,  longest_side,  median_side,  shortest_side,  measurement_units,  weight,  weight_units,  item_volume,  volume_units,  product_size_tier,  average_quantity_on_hand,  average_quantity_pending_removal,  estimated_total_item_volume,  month_of_charge,  storage_rate,  currency,  estimated_monthly_storage_fee,  dangerous_goods_storage_type,  category,  source_warehouse_id ) values "
				+ "(:asin, :fnsku, :product_name, :fulfillment_center, :country_code, :longest_side, :median_side, :shortest_side, :measurement_units, :weight, :weight_units, :item_volume, :volume_units, :product_size_tier, :average_quantity_on_hand, :average_quantity_pending_removal, :estimated_total_item_volume, :month_of_charge, :storage_rate, :currency, :estimated_monthly_storage_fee, :dangerous_goods_storage_type, :category, :source_warehouse_id) ";
		int insertedRows = 0;
		for(AmazonMonthlyStorageFeeReportRawLine line:lineList){

			MapSqlParameterSource q = new MapSqlParameterSource();
			q.addValue("asin",line.getAsin());
			q.addValue("fnsku",line.getFnsku());
			q.addValue("product_name",line.getProductName());
			q.addValue("fulfillment_center",line.getFulfillmentCenter());
			q.addValue("country_code",line.getCountryCode());
			q.addValue("longest_side",line.getLongestSide());
			q.addValue("median_side",line.getMedianSide());
			q.addValue("shortest_side",line.getShortestSide());
			q.addValue("measurement_units",line.getMeasurementUnits());
			q.addValue("weight",line.getWeight());
			q.addValue("weight_units",line.getWeightUnits());
			q.addValue("item_volume",line.getItemVolume());
			q.addValue("volume_units",line.getVolumeUnits());
			q.addValue("product_size_tier", line.getProductSizeTier());
			q.addValue("average_quantity_on_hand",line.getAverageQuantityOnHand());			
			q.addValue("average_quantity_pending_removal",line.getAverageQuantityPendingRemoval());
			q.addValue("estimated_total_item_volume",line.getEstimatedTotalItemVolume());
			q.addValue("month_of_charge",line.getMonthOfCharge());
			q.addValue("storage_rate",line.getStorageRate());
			q.addValue("currency",line.getCurrency());
			q.addValue("estimated_monthly_storage_fee",line.getEstimatedMonthlyStorageFee());
			q.addValue("dangerous_goods_storage_type", line.getDangerousGoodsStorageType());
			q.addValue("category", line.getCategory());
			q.addValue("source_warehouse_id",sourceWarehouse.getKey());
			System.out.println(insertSql);
			Assert.isTrue(getNamedParameterJdbcTemplate().update(insertSql,q)==1);
			System.out.println(insertedRows);
			insertedRows++;
		}
		return insertedRows;
	}

	@Override
	public boolean queryExist(Warehouse sourceWarehouse, String monthOfCharge) {
		String sql = "select exists("
				+ "    select 1 from amazon_monthly_storage_fee amsf "
				+ "    where amsf.source_warehouse_id = :source_warehouse_id "
				+ "    and amsf.month_of_charge = :month_of_charge "
				+ ")";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("source_warehouse_id",sourceWarehouse.getKey());
		q.addValue("month_of_charge",monthOfCharge);		
		return getNamedParameterJdbcTemplate().queryForObject(sql,q,Boolean.class);
	}

}