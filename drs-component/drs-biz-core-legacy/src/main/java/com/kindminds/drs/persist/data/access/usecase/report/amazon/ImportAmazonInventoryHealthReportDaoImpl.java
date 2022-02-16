package com.kindminds.drs.persist.data.access.usecase.report.amazon;

import java.util.List;





import com.kindminds.drs.persist.data.access.rdb.Dao;


import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.kindminds.drs.api.data.access.usecase.report.amazon.ImportAmazonInventoryHealthReportDao;
import com.kindminds.drs.api.v1.model.amazon.AmazonInventoryHealthReportLineItem;

@Repository
public class ImportAmazonInventoryHealthReportDaoImpl extends Dao implements ImportAmazonInventoryHealthReportDao {



	@Override @Transactional("transactionManager")
	public int deleteLineItems(int marketplaceId) {
		String sql = "delete from amazon_inventory_health_report where marketplace_id = :marketplace_id ";
		MapSqlParameterSource query = new MapSqlParameterSource();
		query.addValue("marketplace_id", marketplaceId);
		return getNamedParameterJdbcTemplate().update(sql,query);
	}

	@Override @Transactional("transactionManager")
	public int insertLineItems(int marketplaceId,List<AmazonInventoryHealthReportLineItem> lineItems) {
		String sql = "insert into amazon_inventory_health_report "
				+ "( marketplace_id,  snapshot_date,  sku,  fnsku,  asin,  product_name,  condition,  sales_rank,  product_group,  total_quantity,  sellable_quantity,  unsellable_quantity,  inv_age_0_to_90_days,  inv_age_91_to_180_days,  inv_age_181_to_270_days,  inv_age_271_to_365_days,  inv_age_365_plus_days,  units_shipped_last_24_hrs,  units_shipped_last_7_days,  units_shipped_last_30_days,  units_shipped_last_90_days,  units_shipped_last_180_days,  units_shipped_last_365_days,  weeks_of_cover_t7,  weeks_of_cover_t30,  weeks_of_cover_t90,  weeks_of_cover_t180,  weeks_of_cover_t365,  num_afn_new_sellers,  num_afn_used_sellers,  currency,  your_price,  sales_price,  lowest_afn_new_price,  lowest_afn_used_price,  lowest_mfn_new_price,  lowest_mfn_used_price,  qty_to_be_charged_ltsf_12_mo,  qty_in_long_term_storage_program,  qty_with_removals_in_progress,  projected_ltsf_12_mo,  per_unit_volume,  is_hazmat,  in_bound_quantity,  asin_limit,  inbound_recommend_quantity,  qty_to_be_charged_ltsf_6_mo,  projected_ltsf_6_mo) values "
				+ "(:marketplace_id, :snapshot_date, :sku, :fnsku, :asin, :product_name, :condition, :sales_rank, :product_group, :total_quantity, :sellable_quantity, :unsellable_quantity, :inv_age_0_to_90_days, :inv_age_91_to_180_days, :inv_age_181_to_270_days, :inv_age_271_to_365_days, :inv_age_365_plus_days, :units_shipped_last_24_hrs, :units_shipped_last_7_days, :units_shipped_last_30_days, :units_shipped_last_90_days, :units_shipped_last_180_days, :units_shipped_last_365_days, :weeks_of_cover_t7, :weeks_of_cover_t30, :weeks_of_cover_t90, :weeks_of_cover_t180, :weeks_of_cover_t365, :num_afn_new_sellers, :num_afn_used_sellers, :currency, :your_price, :sales_price, :lowest_afn_new_price, :lowest_afn_used_price, :lowest_mfn_new_price, :lowest_mfn_used_price, :qty_to_be_charged_ltsf_12_mo, :qty_in_long_term_storage_program, :qty_with_removals_in_progress, :projected_ltsf_12_mo, :per_unit_volume, :is_hazmat, :in_bound_quantity, :asin_limit, :inbound_recommend_quantity, :qty_to_be_charged_ltsf_6_mo, :projected_ltsf_6_mo) ";

		int insertedRows = 0;
		for(AmazonInventoryHealthReportLineItem lineItem:lineItems){
			MapSqlParameterSource q = new MapSqlParameterSource();

			q.addValue("marketplace_id",marketplaceId);
			q.addValue("snapshot_date",lineItem.getSnapshotDate());
			q.addValue("sku",lineItem.getSku());
			q.addValue("fnsku",lineItem.getFnsku());
			q.addValue("asin",lineItem.getAsin());
			q.addValue("product_name",lineItem.getProductName());
			q.addValue("condition",lineItem.getCondition());
			q.addValue("sales_rank",lineItem.getSalesRank());
			q.addValue("product_group",lineItem.getProductGroup());
			q.addValue("total_quantity",lineItem.getTotalQuantity());
			q.addValue("sellable_quantity",lineItem.getSellableQuantity());
			q.addValue("unsellable_quantity",lineItem.getUnsellableQuantity());
			q.addValue("inv_age_0_to_90_days",lineItem.getInvAge0To90Days());
			q.addValue("inv_age_91_to_180_days",lineItem.getInvAge91To180Days());
			q.addValue("inv_age_181_to_270_days",lineItem.getInvAge181To270Days());
			q.addValue("inv_age_271_to_365_days",lineItem.getInvAge271To365Days());
			q.addValue("inv_age_365_plus_days",lineItem.getInvAge365PlusDays());
			q.addValue("units_shipped_last_24_hrs",lineItem.getUnitsShippedLast24Hrs());
			q.addValue("units_shipped_last_7_days",lineItem.getUnitsShippedLast7Days());
			q.addValue("units_shipped_last_30_days",lineItem.getUnitsShippedLast30Days());
			q.addValue("units_shipped_last_90_days",lineItem.getUnitsShippedLast90Days());
			q.addValue("units_shipped_last_180_days",lineItem.getUnitsShippedLast180Days());
			q.addValue("units_shipped_last_365_days",lineItem.getUnitsShippedLast365Days());
			q.addValue("weeks_of_cover_t7",lineItem.getWeeksOfCoverT7());
			q.addValue("weeks_of_cover_t30",lineItem.getWeeksOfCoverT30());
			q.addValue("weeks_of_cover_t90",lineItem.getWeeksOfCoverT90());
			q.addValue("weeks_of_cover_t180",lineItem.getWeeksOfCoverT180());
			q.addValue("weeks_of_cover_t365",lineItem.getWeeksOfCoverT365());
			q.addValue("num_afn_new_sellers",lineItem.getNumAfnNewSellers());
			q.addValue("num_afn_used_sellers",lineItem.getNumAfnUsedSellers());
			q.addValue("currency",lineItem.getCurrency());
			q.addValue("your_price",lineItem.getYourPrice());
			q.addValue("sales_price",lineItem.getSalesPrice());
			q.addValue("lowest_afn_new_price",lineItem.getLowestAfnNewPrice());
			q.addValue("lowest_afn_used_price",lineItem.getLowestAfnUsedPrice());
			q.addValue("lowest_mfn_new_price",lineItem.getLowestMfnNewPrice());
			q.addValue("lowest_mfn_used_price",lineItem.getLowestMfnUsedPrice());
			q.addValue("qty_to_be_charged_ltsf_12_mo",lineItem.getQtyToBeChargedLtsf12Mo());
			q.addValue("qty_in_long_term_storage_program",lineItem.getQtyInLongTermStorageProgram());
			q.addValue("qty_with_removals_in_progress",lineItem.getQtyWithRemovalsInProgress());
			q.addValue("projected_ltsf_12_mo",lineItem.getProjectedLtsf12Mo());
			q.addValue("per_unit_volume",lineItem.getPerUnitVolume());
			q.addValue("is_hazmat",lineItem.getIsHazmat());
			q.addValue("in_bound_quantity",lineItem.getInBoundQuantity());
			q.addValue("asin_limit",lineItem.getAsinLimit());
			q.addValue("inbound_recommend_quantity",lineItem.getInboundRecommendQuantity());
			q.addValue("qty_to_be_charged_ltsf_6_mo",lineItem.getQtyToBeChargedLtsf6Mo());
			q.addValue("projected_ltsf_6_mo",lineItem.getProjectedLtsf6Mo());
			Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
			insertedRows++;
		}
		return insertedRows;
	}

	@Override @SuppressWarnings("unchecked")
	public List<Object []> queryImportInfoList() {
		String sql = "select distinct marketplace_id, snapshot_date from amazon_inventory_health_report order by marketplace_id ";

		List<Object[]> columnsList = getJdbcTemplate().query(sql,objArrayMapper);
		return columnsList;

	}

}