package com.kindminds.drs.persist.data.access.usecase.inventory;

import java.util.Date;
import java.util.List;





import com.kindminds.drs.persist.data.access.rdb.Dao;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import com.kindminds.drs.api.data.access.usecase.inventory.ViewInventoryHealthReportDao;

@Repository
public class ViewInventoryHealthReportDaoImpl  extends Dao implements ViewInventoryHealthReportDao {
	


	@Override @SuppressWarnings("unchecked")
	public List<Object []> queryLineItems(int marketplaceId, String supplierKcode) {
		String sql = "select           ps.code_by_drs as sku, "
				+ "                p.name_by_supplier as product_name, "
				+ "                    aihr.condition as condition, "
				+ "          aihr.unsellable_quantity as unsellable_quantity, "
				+ "            aihr.sellable_quantity as sellable_quantity, "
				+ "  aihr.qty_to_be_charged_ltsf_6_mo as qty_to_be_charged_ltsf_6_mo, "
				+ " aihr.qty_to_be_charged_ltsf_12_mo as qty_to_be_charged_ltsf_12_mo, "
				+ "          aihr.projected_ltsf_6_mo as projected_ltsf_6_mo, "
				+ "         aihr.projected_ltsf_12_mo as projected_ltsf_12_mo, "
				+ "   aihr.units_shipped_last_30_days as units_shipped_last_30_days, "
				+ "           aihr.weeks_of_cover_t30 as weeks_of_cover_t30 "
				+ "from amazon_inventory_health_report aihr "
				+ "inner join product_marketplace_info pmi on pmi.marketplace_sku = aihr.sku "
				+ "inner join product p on p.id = pmi.product_id "
				+ "inner join product_sku ps on ps.product_id = p.id "
				+ "inner join product_base pb on pb.id = ps.product_base_id "
				+ "inner join company splr on splr.id = pb.supplier_company_id "
				+ "where aihr.marketplace_id = :marketplace_id "
				+ "and pmi.marketplace_id = :marketplace_id ";
		if(supplierKcode!=null) sql += "and splr.k_code = :supplierKcode ";
		sql += "order by ps.code_by_drs ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("marketplace_id",marketplaceId);
		if(supplierKcode!=null) q.addValue("supplierKcode",supplierKcode);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);

		return columnsList;
	}

	@Override
	public Date querySnapshotDate(int marketplaceId) {
		String sql = "select distinct snapshot_date from amazon_inventory_health_report "
				+ "where marketplace_id = :marketplaceId order by snapshot_date desc limit 1 offset 0 ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("marketplaceId", marketplaceId);
		return getNamedParameterJdbcTemplate().queryForObject(sql,q,Date.class);
	}

}
