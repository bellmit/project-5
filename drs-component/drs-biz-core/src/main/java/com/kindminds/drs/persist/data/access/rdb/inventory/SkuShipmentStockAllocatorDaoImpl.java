package com.kindminds.drs.persist.data.access.rdb.inventory;

import java.util.List;

import com.kindminds.drs.api.data.access.rdb.inventory.SkuShipmentStockAllocatorDao;
import com.kindminds.drs.persist.data.access.rdb.Dao;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.kindminds.drs.Country;




@Repository
public class SkuShipmentStockAllocatorDaoImpl extends Dao implements SkuShipmentStockAllocatorDao {
	


	@Override @SuppressWarnings("unchecked")
	public List<Object []> queryShipmentStockAvailableInfo(Country unsDestinationCountry, String drsSku) {
		/*
		StringBuilder sqlSb = new StringBuilder()
				.append("select  uns.name as uns_name, ")
				.append(" ivs.name as ivs_name, ")
				.append("unsi.qty_order - unsi.qty_sold + unsi.qty_returned as available_qty ")
				.append("from shipment_line_item unsi ")
				.append("inner join product_sku ps on ps.id = unsi.sku_id ")
				.append("inner join shipment uns on uns.id = unsi.shipment_id ")
				.append("inner join company ms on ms.id = uns.buyer_company_id ")
				.append("inner join shipment_uns_info sui on sui.shipment_id = uns.id ")
				.append("inner join shipment ivs on ivs.id = unsi.source_shipment_id ")
				.append("where ps.code_by_drs = :drsSku ")
				.append("and uns.destination_country_id = :countryKey ")
				.append("and unsi.qty_order-unsi.qty_sold+unsi.qty_returned >=1 ")
				.append("order by sui.arrival_date, ivs.serial_id ");
		 */

		StringBuilder sqlSb = new StringBuilder()
				.append("select  uns.name as uns_name , ivs.name as ivs_name  ," +
						" ssi.id, ssi.ivs_sku_serial_no , " +
						" ssi.remark , ssi.drs_transaction_id , ssi.status " +
						" from shipment_sku_identification ssi " +
						" inner join shipment_line_item sli on ssi.ivs_shipment_line_item_id  = sli.id " +
						" inner join product_sku ps on ps.id = ssi.sku_id  " +
						" inner join shipment ivs on ivs.id = ssi.ivs_shipment_id  " +
						" inner join company c on c.id = ivs.seller_company_id " +
						" inner join shipment uns on uns.id = ssi.uns_shipment_id " +
						" inner join shipment_uns_info sui on sui.shipment_id = uns.id " +
						" where " +
						" (ssi.status = '' or ssi.status = 'Refund' or ssi.status is null) and ps.code_by_drs = :drsSku  " +
						" and uns.destination_country_id = :countryKey  " +
						" and (uns.status = 'SHPT_RECEIVING' or uns.status = 'SHPT_RECEIVED' ) " +
						" order by sui.arrival_date , ssi.id ");


		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("drsSku",drsSku);
		q.addValue("countryKey",unsDestinationCountry.getKey());
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,objArrayMapper);
		return columnsList;
	}

	@Override @SuppressWarnings("unchecked")
	public Object [] queryUnsInfo(String name, String drsSku) {
		StringBuilder sqlSb = new StringBuilder()
				.append("select distinct                uns.name as uns_name,  ")
				.append("                        uns.currency_id as ddp_currency_id, ")
				.append("                      unsli.unit_amount as ddp_price, ")
				.append("       ui.export_currency_exchange_rate as fx_rate_fca_currency_to_dst_country_currency, ")
				.append("ui.export_currency_exchange_rate_to_eur as fx_rate_fca_currency_to_eur ")
				.append("from shipment uns ")
				.append("inner join shipment_uns_info ui on ui.shipment_id = uns.id  ")
				.append("inner join shipment_line_item unsli on unsli.shipment_id = uns.id ")
				.append("inner join product_sku ps on ps.id = unsli.sku_id ")
				.append("where uns.name = :unsName ")
				.append("and ps.code_by_drs = :sku ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("unsName", name);
		q.addValue("sku", drsSku);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,objArrayMapper);
		Assert.isTrue(columnsList.size() == 1, drsSku + " is not in " + name + " or multiple DDP amount found.");
		Object[] columns = columnsList.get(0);
		return columns;
	}

	@Override @SuppressWarnings("unchecked")
	public Object [] queryIvsInfo(String name, String drsSku) {
		StringBuilder sqlSb = new StringBuilder()
				.append("select ")
				.append("ivs.name, ")
				.append("ivs.currency_id, ")
				.append("ivsli.unit_amount, ")
				.append("ivs.sales_tax_rate ")
				.append("from shipment ivs ")
				.append("inner join shipment_line_item ivsli on ivsli.shipment_id=ivs.id ")
				.append("inner join product_sku ps on ps.id = ivsli.sku_id ")
				.append("where ps.code_by_drs = :sku ")
				.append("and ivs.name = :name ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("name", name);
		q.addValue("sku", drsSku);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,objArrayMapper);
		Assert.isTrue(columnsList.size()>=1,drsSku+" is not in "+name+" or multiple DDP amount found.");
		Object[] columns=columnsList.get(0);
		return columns;
	}

	@Override @Transactional("transactionManager")
	public void increaseStockQuantity(String unsName, String ivsName, String sku, int quantity, Integer lineSeq) {
		String sql = "update shipment_line_item unsi set qty_sold = qty_sold + :quantity "
				+ "from product_sku ps, shipment uns, shipment ivs "
				+ "where true "
				+ "and unsi.shipment_id = uns.id "
				+ "and unsi.source_shipment_id = ivs.id "
				+ "and unsi.sku_id = ps.id "
				+ "and ps.code_by_drs = :sku "
				+ "and uns.name = :shipmentName "
				+ "and ivs.name = :sourceShipmentName "
				+ " and unsi.line_seq = :lineSeq ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("quantity",quantity);
		q.addValue("sku",sku);
		q.addValue("shipmentName",unsName);
		q.addValue("sourceShipmentName",ivsName);
		q.addValue("lineSeq",lineSeq);
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1,"q.executeUpdate()==1");
	}

	@Override @SuppressWarnings("unchecked")
	public List<Object[]> queryIvsStockQuantity(String unsName, String ivsName, String sku) {
		String sql = "select line_seq, qty_order, qty_sold, qty_returned  " +
				" from shipment_line_item unsi " +
				" inner join product_sku ps on unsi.sku_id = ps.id  " +
				" inner join shipment uns on unsi.shipment_id = uns.id  " +
				" inner join shipment ivs on unsi.source_shipment_id = ivs.id  " +
				" where true  " +
				" and ps.code_by_drs = :sku  " +
				" and uns.name = :shipmentName  " +
				" and ivs.name = :sourceShipmentName " +
				" order by line_seq";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("sku",sku);
		q.addValue("shipmentName",unsName);
		q.addValue("sourceShipmentName",ivsName);
		return getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
	}

	public Integer queryRefundedQuantity(String ivsName, String sku) {
		String sql = "SELECT sum(summary.quantity_refund) as quantity_refund " +
				" FROM shipment ivs " +
				" INNER JOIN product_sku ps on ps.id in (select distinct sku_id from shipment_line_item where shipment_id = ivs.id )  " +
				" LEFT JOIN (select ps.id as ps_id,  " +
				"     sum(case when dt.type = 'Refund' then dt.quantity when dt.type = 'other-transaction' and dtlis.pretax_principal_price <= 0 then dt.quantity else 0 end) as quantity_refund " +
				" from drs_transaction dt " +
				" inner join product_sku ps on ps.id = dt.product_sku_id " +
				" inner join drs_transaction_line_item_source dtlis on dt.id = dtlis.drs_transaction_id " +
				" where dt.shipment_ivs_name = 'IVS-K598-24'  and dt.inventory_excluded = false  " +
				" group by ps.id, ps.code_by_drs " +
				") summary on summary.ps_id = ps.id  " +
				"where ivs.name = :ivsName  " +
				"and ps.code_by_drs = :sku " +
				"group by ps.id, ps.code_by_drs";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("ivsName",ivsName);
		q.addValue("sku",sku);

		Integer result = null;
		try{
			result = getNamedParameterJdbcTemplate().queryForObject(sql,q,Integer.class);
		}catch (EmptyResultDataAccessException ex){

		}

		if (result == null) {
			return 0;
		}
		return result;
		
	}

	@Override
	public List<Object[]> queryShipmentInfo(Country unsDestinationCountry, String drsSku) {

		StringBuilder sqlSb = new StringBuilder()
				.append("select  uns.name as uns_name, ")
				.append(" ivs.name as ivs_name ")
				.append("from shipment_line_item unsi ")
				.append("inner join product_sku ps on ps.id = unsi.sku_id ")
				.append("inner join shipment uns on uns.id = unsi.shipment_id ")
				.append("inner join company ms on ms.id = uns.buyer_company_id ")
				.append("inner join shipment_uns_info sui on sui.shipment_id = uns.id ")
				.append("inner join shipment ivs on ivs.id = unsi.source_shipment_id ")
				.append("where ps.code_by_drs = :drsSku ")
				.append("and uns.destination_country_id = :countryKey ")
				.append("order by sui.arrival_date, ivs.serial_id ");


		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("drsSku",drsSku);
		q.addValue("countryKey",unsDestinationCountry.getKey());
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,objArrayMapper);
		return columnsList;
	}

}
