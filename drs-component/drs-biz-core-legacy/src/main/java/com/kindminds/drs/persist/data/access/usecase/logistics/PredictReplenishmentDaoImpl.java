package com.kindminds.drs.persist.data.access.usecase.logistics;

import java.util.Calendar;
import java.util.Date;
import java.util.List;





import com.kindminds.drs.persist.data.access.rdb.Dao;
import com.kindminds.drs.api.v1.model.replenishment.ReplenishmentTimeSpent;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import com.kindminds.drs.api.data.access.usecase.logistics.PredictReplenishmentDao;
import com.kindminds.drs.api.v1.model.product.ProductMarketplaceInfo.ProductMarketStatus;

import com.kindminds.drs.api.v1.model.replenishment.SkuInventoryInfo;
import com.kindminds.drs.persist.v1.model.mapping.replenishment.ReplenishmentTimeSpentImpl;
import com.kindminds.drs.persist.v1.model.mapping.replenishment.SkuInventoryInfoImpl;

@Repository
public class PredictReplenishmentDaoImpl extends Dao implements PredictReplenishmentDao {



	@Override
	public List<SkuInventoryInfo> getSkuInventoryList(int warehouseId, String supplierKCode, Integer spwCaclDays, String skuNumber) {
		String sql = buildBaseSql();
		Integer supplierId = querySupplierId(supplierKCode);
		sql = sql.replace("{whereCondSupplier}", buildWhereCondSupplier(supplierId));
		sql = sql.replace("{whereCondSku}", buildWhereCondSku(skuNumber));
		return queryData(supplierId, warehouseId, spwCaclDays, skuNumber, sql);
	}

	@SuppressWarnings("unchecked")
	private List<SkuInventoryInfo> queryData(Integer supplierId, int warehouseId, Integer spwCaclDays, String skuNumber, String sql) {
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("status", 6);
		q.addValue("wid", warehouseId);
		q.addValue("nos", true);
		q.addValue("start", 0);
		q.addValue("size", spwCaclDays == null ? 28 : spwCaclDays);
		q.addValue("ldate", getUtcToday());
		q.addValue("marketplaceIds", this.queryMarketplaceIdByWareHouse(warehouseId));
		q.addValue("product_market_status", ProductMarketStatus.REGION_LIVE.getDbValue());
		if (supplierId != null)
			q.addValue("sid", supplierId);
		if (skuNumber != null)
			q.addValue("sku", skuNumber);


		return (List) getNamedParameterJdbcTemplate().query(sql,q,(rs , rowNum) -> new SkuInventoryInfoImpl(
				rs.getString("code_by_drs"),	rs.getString("sku_name"),	rs.getString("k_code"),	rs.getString("short_name_en_us"),
				rs.getInt("manufacturing_lead_time_days"),rs.getInt("qty_in_stock"),rs.getInt("pl_sum_qty"),
				rs.getDate("pl_expected_date"),rs.getInt("ib_sum_qty"),rs.getDate("ib_expected_date"),
				rs.getDate("expected_date"),rs.getInt("sum_qty_sold"),rs.getInt("sold_days")
		));
	}

	@SuppressWarnings("unchecked")
	private List<Integer> queryMarketplaceIdByWareHouse(int warehouseId){
		String sql = "select m.id from marketplace as m where m.warehouse_id = :warehouseId";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("warehouseId", warehouseId);
		List<Integer> marketplaceIdList = getNamedParameterJdbcTemplate().queryForList(sql,q, Integer.class);
		return marketplaceIdList;
	}

	private Date getUtcToday(){
		Calendar now = Calendar.getInstance();
		now.add(Calendar.MILLISECOND,-now.getTimeZone().getRawOffset());
		now.set(Calendar.HOUR_OF_DAY,0);
		now.set(Calendar.MINUTE,0);
		now.set(Calendar.SECOND,0);
		now.set(Calendar.MILLISECOND,0);
		now.add(Calendar.MILLISECOND,now.getTimeZone().getRawOffset());
		return now.getTime();
	}

	private Integer querySupplierId(String supplierKCode) {
		if (supplierKCode == null)
			return null;
		String sql = "select id from company where k_code = :code";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("code", supplierKCode);
		return getNamedParameterJdbcTemplate().queryForObject(sql,q,Integer.class);
	}

	private String buildWhereCondSupplier(Integer supplierId) {
		if (supplierId == null) return "";
		return "and pb.supplier_company_id = :sid\n";
	}

	private String buildWhereCondSku(String sku) {
		if (sku == null) return "";
		return "and ps.code_by_drs = :sku";
	}

	private String buildBaseSql() {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct c.k_code, c.short_name_en_us, ps.code_by_drs, p.name_by_supplier as sku_name, ps.manufacturing_lead_time_days, ps.product_id, psih.snapshot_date, psih.qty_in_stock, sum(psih_s.qty_sold) as sum_qty_sold,si.sold_days,CAST((select case WHEN pl.expected_date is not null AND ib.expected_date is null THEN pl.expected_date WHEN ib.expected_date is not null AND pl.expected_date is null THEN ib.expected_date WHEN pl.expected_date >= ib.expected_date THEN pl.expected_date WHEN ib.expected_date >= pl.expected_date THEN ib.expected_date ELSE NULL END as date) as date) as expected_date, ib.sum_qty as ib_sum_qty, pl.sum_qty as pl_sum_qty,pl.expected_date as pl_expected_date,CAST(ib.expected_date as date) as ib_expected_date,si.product_sku_id, si.min_date,si.max_date, psih.warehouse_id\n");
		sb.append("from company as c\n");
		sb.append("inner join product_base as pb on (c.id = pb.supplier_company_id {whereCondSupplier})\n");
		sb.append("inner join product_sku as ps on (ps.product_base_id = pb.id and ps.status_id = :status {whereCondSku})\n");
		sb.append("inner join (select distinct product_id from product_marketplace_info where marketplace_id in (:marketplaceIds) and status = :product_market_status) as pmi on (pmi.product_id = ps.product_id)\n");
		sb.append("inner join product as p on p.id = ps.product_id\n");
		sb.append("left join product_sku_inventory_history psih on (psih.product_sku_id = ps.id and psih.warehouse_id=:wid and snapshot_date = :ldate)\n");

		sb.append("left join (");

		/*
		sb.append("select a.product_sku_id, a.warehouse_id, min(a.snapshot_date) as min_date, max(a.snapshot_date) " +
				"as max_date, count(*) as sold_days\n");
		sb.append("from product_sku_inventory_history a\n");
		sb.append("where A.not_out_of_stock = true and A.snapshot_date in ( select snapshot_date\n");
		sb.append("from product_sku_inventory_history b\n");
		sb.append("where b.product_sku_id=A.product_sku_id and b.warehouse_id=A.warehouse_id " +
				"and b.not_out_of_stock = :nos\n");
		sb.append("order by product_sku_id,warehouse_id,snapshot_date desc\n");
		sb.append("offset :start limit :size) group by a.product_sku_id, a.warehouse_id,a.not_out_of_stock\n");
		*/

		sb.append("select a.product_sku_id, a.warehouse_id, min(a.snapshot_date) as min_date, max(a.snapshot_date) as max_date, count(*) as sold_days\n");
		sb.append("from (select b.* from (\n");
		sb.append(" select * , RANK () OVER ( PARTITION BY product_sku_id ,  warehouse_id\n") ;
		sb.append(" order by product_sku_id,warehouse_id,snapshot_date desc\n) ");
		sb.append("	from product_sku_inventory_history where not_out_of_stock = :nos ) b\n");
		sb.append(" where rank >= :start and rank <= :size ) a group by a.product_sku_id, a.warehouse_id,a.not_out_of_stock\n");

		sb.append(") as si ");
		sb.append("on (si.warehouse_id = :wid and si.product_sku_id = ps.id)\n");
		sb.append("left join product_sku_inventory_history as psih_s on (psih_s.product_sku_id=psih.product_sku_id and psih_s.warehouse_id= :wid and psih_s.snapshot_date\n");
		sb.append("between si.min_date and si.max_date)\n");
		sb.append("left join(select s.warehouse_id, sli.sku_id, max(expect_arrival_date) as expected_date, sum(qty_order) as sum_qty\n");
		sb.append("from shipment_uns_info as sui\n");
		sb.append("inner join shipment as s on (sui.shipment_id = s.id)\n");
		sb.append("inner join shipment_line_item as sli on (sui.shipment_id = sli.shipment_id)\n");
		sb.append("where sui.arrival_date is null and s.warehouse_id = :wid\n");
		sb.append("group by s.warehouse_id, sli.sku_id) as ib on (ib.sku_id = ps.id)\n");
		sb.append("left join (select m.warehouse_id, sli_i.sku_id, max(sii.expected_export_date + (case when s_i.shipping_method_id='1' then rtsi.days_spent_for_courier when s_i.shipping_method_id='2' then rtsi.days_spent_for_air_freight else rtsi.days_spent_for_surface_freight end)) as expected_date, sum(sli_i.qty_order) as sum_qty \n");
		sb.append("FROM shipment as s_i\n");
		sb.append("inner join shipment_info_ivs as sii on (s_i.id = sii.shipment_id)\n");
		sb.append("inner join (select distinct country_id, warehouse_id from marketplace where warehouse_id is not null) as m on (m.country_id = s_i.destination_country_id)\n");
		sb.append("inner join shipment_line_item as sli_i on (sli_i.shipment_id=sii.shipment_id)\n");
		sb.append("inner join replenishment_time_spent_info as rtsi on (rtsi.warehouse_id=m.warehouse_id)\n");
		sb.append("where not exists (select source_shipment_id from shipment_line_item where source_shipment_id=s_i.id) and m.warehouse_id = :wid\n");
		sb.append("group by m.warehouse_id,sli_i.sku_id) as pl on (pl.sku_id=ps.id)\n");
		sb.append("group by c.k_code, c.short_name_en_us, ps.code_by_drs, p.name_by_supplier, ps.manufacturing_lead_time_days, ps.product_id, psih.snapshot_date, psih.qty_in_stock, psih.warehouse_id,ib.sum_qty, pl.sum_qty,pl_expected_date,ib.expected_date,si.product_sku_id,si.sold_days, si.min_date,si.max_date\n");
		sb.append("order by c.k_code, ps.code_by_drs");
		return sb.toString();
	}

	@Override
	public ReplenishmentTimeSpent getReplenishmentTimeSpent(int warehouseId) {
		String sql = "SELECT id, days_spent_for_amazon_receiving, days_spent_for_spw_calculation,  " +
				" days_spent_for_courier, days_spent_for_air_freight, days_spent_for_surface_freight " +
				" FROM replenishment_time_spent_info\n" +
				" WHERE id = (SELECT MAX(id) from replenishment_time_spent_info WHERE warehouse_id=:wid)";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("wid", warehouseId);

		return getNamedParameterJdbcTemplate().queryForObject(sql,q,(rs,rowNum) -> new ReplenishmentTimeSpentImpl(
				rs.getInt("id"),rs.getInt("days_spent_for_amazon_receiving"),
				rs.getInt("days_spent_for_spw_calculation"),rs.getInt("days_spent_for_courier"),
				rs.getInt("days_spent_for_air_freight"),rs.getInt("days_spent_for_surface_freight")
		));
	}

	@Override
	public String getNotes() {
		StringBuilder sb = new StringBuilder();
		sb.append("select detail->>'value' as description from configuration where type = :type");
		String sql = sb.toString();
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("type", "REPLENISHMENT_PREDICTION_NOTES");
		String  result = getNamedParameterJdbcTemplate().queryForObject(sb.toString(),q,String.class);
		return result;
	}

}
