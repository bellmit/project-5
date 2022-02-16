package com.kindminds.drs.persist.data.access.usecase.logistics;

import java.math.BigDecimal;
import java.util.*;


import com.kindminds.drs.api.data.access.usecase.logistics.MaintainShipmentUnsDao;
import com.kindminds.drs.api.v1.model.logistics.ShipmentUns;
import com.kindminds.drs.api.v1.model.product.stock.ProductSkuStock;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.IvsLineItem;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShipmentStatus;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShipmentType;
import com.kindminds.drs.persist.data.access.rdb.Dao;




import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;







import com.kindminds.drs.persist.v1.model.mapping.product.ProductSkuStockImpl;
import com.kindminds.drs.persist.v1.model.mapping.logistics.ShipmentIvsLineItemImpl;
import com.kindminds.drs.persist.v1.model.mapping.logistics.ShipmentUnsImpl;
import com.kindminds.drs.persist.v1.model.mapping.logistics.ShipmentUnsLineItemImpl;
import com.kindminds.drs.persist.data.access.rdb.util.PostgreSQLHelper;

@Repository
public class MaintainShipmentUnsDaoImpl extends Dao implements MaintainShipmentUnsDao {



	@Override
	public Integer queryNonDraftInventoryShpliCount(String shpName, String skuKcode) {
		StringBuilder sqlSb = new StringBuilder()
				.append("select count(1) from shipment_line_item shpli ")
				.append("inner join shipment shp on shp.id = shpli.shipment_id ")
				.append("inner join product_sku ps on ps.id = shpli.sku_id ")
				.append("where shp.type = :typeName ")
				.append("and shp.name = :shpName ")
				.append("and ps.code_by_drs = :skuKcode ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("typeName", ShipmentType.INVENTORY.getValue());
		q.addValue("shpName", shpName);
		q.addValue("skuKcode", skuKcode);
		Integer result = getNamedParameterJdbcTemplate().queryForObject(sqlSb.toString(),q,Integer.class);
		if (result == null) return 0;

		Assert.isTrue(result==1,":"+result);
		return result;
	}
	
	@Override
	public Integer queryAllShipmentsCounts(int userId) {
		StringBuilder sqlSb = new StringBuilder()
				.append("select count(1) from shipment shp ")
				.append("inner join user_info u on (u.company_id = shp.seller_company_id or u.company_id = shp.buyer_company_id) ")
				.append("where shp.type = :unifiedTypeName ")
				.append("and u.user_id = :userId ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("unifiedTypeName", ShipmentType.UNIFIED.getValue());
		q.addValue("userId", userId);
		Integer result = getNamedParameterJdbcTemplate().queryForObject(sqlSb.toString(),q,Integer.class);
		if (result == null) return 0;
		return result;
	}
	
	@Override @SuppressWarnings("unchecked")
	public List<ShipmentUns> queryShipmentList(int userId, int startIndex, int size) {
		StringBuilder sqlSb = new StringBuilder()
				.append("select                  shp.name as name, ")
				.append("                           shp.type as type, ")
				.append("                 shp.invoice_number as invoice_number, ")
				.append("                sui.tracking_number as tracking_number, ")
				.append("                         sui.fba_id as fba_id, ")
				.append("                      sui.forwarder as forwarder, ")
				.append("                        selr.k_code as seller_company_kcode, ")
				.append("                        buyr.k_code as buyer_company_kcode, ")
				.append("                      currency.name as currency_name, ")
				.append("                               NULL as sales_tax_rate, ")
				.append("                               NULL as amount_tax, ")
				.append("                   shp.amount_total as amount_total, ")
				.append("               shp.cif_amount_total as cif_amount_total, ")
				.append("                          dest.code as destination_country_code, ")
				.append("                          actl.code as actual_destination_code, ")
				.append("                            sm.name as shipping_method, ")
				.append("shp.date_created at time zone 'UTC' as date_created, ")
				.append("                         shp.status as status, ")
				.append("                    sui.export_date as export_date, ")
				.append("                               NULL as export_src_currency_id, ")
				.append("                               NULL as export_dst_currency_id, ")
				.append("                               NULL as export_currency_exchange_rate, ")
				.append("                               NULL as export_currency_exchange_rate_to_eur, ")
				.append("                               NULL as arrival_date, ")
				.append("            sui.expect_arrival_date as expect_arrival_date ")
				.append("from shipment shp ")
				.append("inner join shipping_method sm on sm.id = shp.shipping_method_id ")
				.append("inner join company selr on selr.id = shp.seller_company_id ")
				.append("inner join company buyr on buyr.id = shp.buyer_company_id ")
				.append("inner join currency on currency.id = shp.currency_id ")
				.append("inner join country dest on dest.id = shp.destination_country_id ")
				.append("left join country actl on actl.id = shp.actual_destination_id ")
				.append("inner join user_info u on (u.company_id = shp.seller_company_id or u.company_id = shp.buyer_company_id) ")
				.append("inner join shipment_uns_info sui on sui.shipment_id = shp.id ")
				.append("where shp.type = :unifiedTypeName ")
				.append("and u.user_id = :userId ")
				.append("order by case ")
				.append("    when shp.status = 'SHPT_DRAFT'         then 1 ")
				.append("    when shp.status = 'SHPT_EXCEPTION'     then 2 ")
				.append("    when shp.status = 'SHPT_FROZEN'        then 3 ")
				.append("    when shp.status = 'SHPT_AWAIT_PICK_UP' then 4 ")
				.append("    when shp.status = 'SHPT_IN_TRANSIT'    then 5 ")
				.append("    when shp.status = 'SHPT_RECEIVING'     then 6 ")
				.append("    when shp.status = 'SHPT_RECEIVED'      then 7 ")
				.append("    else 99 ")
				.append("end asc, ")
				.append("sui.expect_arrival_date desc, ")
				.append("sui.export_date desc ")				
				.append("limit :size offset :start ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("unifiedTypeName", ShipmentType.UNIFIED.getValue());
		q.addValue("userId", userId);
		q.addValue("size", size);
		q.addValue("start", startIndex-1);

		return (List) getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,
				(rs,rowNum) -> new ShipmentUnsImpl(
						rs.getString("name"),rs.getString("type"),rs.getString("invoice_number"),rs.getString("tracking_number"),
						rs.getString("fba_id"),rs.getString("forwarder"),rs.getString("seller_company_kcode"),rs.getString("buyer_company_kcode")
						,rs.getString("currency_name"),
						rs.getBigDecimal("amount_total"),rs.getBigDecimal("cif_amount_total"),rs.getString("destination_country_code"),
						rs.getString("actual_destination_code"),
						rs.getString("shipping_method"),rs.getDate("date_created"),rs.getString("status"),
						rs.getDate("export_date"),rs.getInt("export_src_currency_id"),
						rs.getInt("export_dst_currency_id"),rs.getBigDecimal("export_currency_exchange_rate"),
						rs.getBigDecimal("export_currency_exchange_rate_to_eur"),rs.getTimestamp("arrival_date"),
						rs.getTimestamp("expect_arrival_date")
				));
	}
	
	@Override @SuppressWarnings("unchecked")
	public List<String> queryAvailableInventoryShipmentNameList(String sellerKcode, String dstCountry) {
		StringBuilder sqlSb = new StringBuilder()
				.append("select shp.name ")
				.append("from shipment shp ")
				.append("inner join company original_buyer on original_buyer.id = shp.buyer_company_id ")
				.append("inner join country on country.id = shp.destination_country_id ")
				.append("where original_buyer.k_code = :sellerKcode ")
				.append("and country.code = :dstCountry ")
				.append("and shp.type = :inventoryShpType ")
				.append("and ( shp.status = :awaitConfirmStatusName ")
				.append("or shp.status = :bookingConfirmStatusName ) ")
				.append("order by shp.name");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("sellerKcode", sellerKcode);
		q.addValue("dstCountry", dstCountry);
		q.addValue("inventoryShpType", ShipmentType.INVENTORY.getValue());
		q.addValue("awaitConfirmStatusName", ShipmentStatus.SHPT_CONFIRMED.name());
		q.addValue("bookingConfirmStatusName", ShipmentStatus.SHPT_FC_BOOKING_CONFIRM.name());
		return getNamedParameterJdbcTemplate().queryForList(sqlSb.toString(),q,String.class);
	}
	
	@Override @SuppressWarnings("unchecked")
	public Map<String,String> queryAvailableSkuToDrsNameMapForInventoryShipment( String shipmentName) {
		StringBuilder sqlSb = new StringBuilder()
				.append("select ps.code_by_drs, p.name_by_drs ")
				.append("from shipment_line_item shpli ")
				.append("inner join product_sku ps on ps.id = shpli.sku_id ")
				.append("inner join product p on p.id = ps.product_id ")
				.append("inner join shipment shp on shp.id = shpli.shipment_id ")
				.append("where shp.name = :shipmentName ")
				.append("order by shpli.line_seq ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("shipmentName", shipmentName);
		Map<String,String> availableSkuCodeToDrsNameMap = new HashMap<String,String>();
		List<Object[]> result = getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,objArrayMapper);
		for(Object[] items:result){
			availableSkuCodeToDrsNameMap.put((String)items[0], (String)items[1]);
		}
		return availableSkuCodeToDrsNameMap;
	}

	@Override @Transactional("transactionManager")
	public String insert(Integer serialId, String name, String invoiceNumber, String status, int currencyId,
						 Date exportDate, Date arrivalDate, Date expectArrivalDate, ShipmentUns shipment,
						 Integer warehouseId, BigDecimal ddpTotal, BigDecimal cifTotal) {
		int nextId = PostgreSQLHelper.getNextVal(getNamedParameterJdbcTemplate(),"shipment","id");
		String dateStr=shipment.getDateCreated();
		StringBuilder sqlSb = new StringBuilder()
					.append("INSERT INTO shipment ")
					.append("( id, serial_id, name,  type, invoice_number, seller_company_id, buyer_company_id, currency_id,   amount_total, cif_amount_total, destination_country_id, shipping_method_id, date_created,  status,  warehouse_id, actual_destination_id ) ")
					.append("select ")
					.append(" :id, :serialId,:name, :type, :invoiceNumber,         seller.id,            ms.id, :currencyId,   :amountTotal, :cifAmountTotal,                   ct.id,             sm.id,'"+dateStr+"', :status,  :warehouseId,   actual.id  ")
					.append("from company seller ")
					.append("inner join country ct on ct.code = :countryCode ")
					.append("inner join country actual on actual.code = :actualCountry ")
					.append("inner join company ms on ms.country_id = actual.id ")
					.append("inner join shipping_method sm on sm.name = :shippingMethod ")
					.append("where seller.k_code = :sellerKcode ")
					.append("and ms.is_drs_company = TRUE ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("id",nextId);
		q.addValue("serialId",serialId);
		q.addValue("name",name);
		q.addValue("type",shipment.getType().getValue());
		q.addValue("invoiceNumber",invoiceNumber);
		q.addValue("currencyId",currencyId);
		q.addValue("amountTotal",ddpTotal);
		q.addValue("cifAmountTotal",cifTotal);
		q.addValue("shippingMethod",shipment.getShippingMethod().name());
		q.addValue("status",status);
		q.addValue("countryCode",shipment.getDestinationCountry());
		q.addValue("actualCountry",shipment.getActualDestination());
		q.addValue("sellerKcode",shipment.getSellerCompanyKcode());
		q.addValue("warehouseId", warehouseId);
		int insertedRows = getNamedParameterJdbcTemplate().update(sqlSb.toString(),q);

		Assert.isTrue(insertedRows==1,"No or more than one shipment inserted:"+insertedRows);
		this.insertUnsInfo(nextId, exportDate, arrivalDate, expectArrivalDate,shipment.getExportCurrencyExchangeRate(),shipment.getExportFxRateToEur(),shipment.getTrackingNumber(),shipment.getFbaId(),shipment.getForwarder());
		this.insertLineItems(shipment.getLineItems(),name);
		return name;
	}
	
	@Transactional("transactionManager")
	private void insertUnsInfo(int shipmentId, Date exportDate, Date arrivalDate, Date expectArrivalDate, String exchangeRate, String exchangeRateToEur, String trackingNumber, String fbaId, ShipmentUns.Forwarder forwarder){
		StringBuilder sqlSb = new StringBuilder()
				.append("insert into shipment_uns_info ")
				.append("( shipment_id,  export_date, arrival_date, expect_arrival_date,  export_currency_exchange_rate,  export_currency_exchange_rate_to_eur,  tracking_number,  fba_id,  forwarder ) values ")
				.append("( :shipmentId, :export_date, :arrivalDate,  :expectArrivalDate, :export_currency_exchange_rate, :export_currency_exchange_rate_to_eur, :tracking_number, :fba_id, :forwarder ) ");
		MapSqlParameterSource q = new MapSqlParameterSource();

		q.addValue("shipmentId",shipmentId);
		q.addValue("export_date",exportDate);
		q.addValue("arrivalDate",arrivalDate);
		q.addValue("expectArrivalDate",expectArrivalDate);
		q.addValue("export_currency_exchange_rate",StringUtils.hasText(exchangeRate)?new BigDecimal(exchangeRate):null);
		q.addValue("export_currency_exchange_rate_to_eur",StringUtils.hasText(exchangeRateToEur)?new BigDecimal(exchangeRateToEur):null);
		q.addValue("tracking_number",trackingNumber);
		q.addValue("fba_id", fbaId);
		q.addValue("forwarder",forwarder==null?null:forwarder.name());
		int insertedRows = getNamedParameterJdbcTemplate().update(sqlSb.toString(),q);
		Assert.isTrue(insertedRows==1,"No or more than one Shipment Info inserted:"+insertedRows);
	}
	
	@Override @SuppressWarnings("unchecked")
	public ShipmentUns query(String shipmentName) {
		StringBuilder sqlSb = new StringBuilder()
				.append("select                          shp.name as name, ")
				.append("                                shp.type as type, ")
				.append("                      shp.invoice_number as invoice_number, ")
				.append("                     sui.tracking_number as tracking_number, ")
				.append("                              sui.fba_id as fba_id, ")
				.append("                           sui.forwarder as forwarder, ")				
				.append("                             selr.k_code as seller_company_kcode, ")
				.append("                             buyr.k_code as buyer_company_kcode, ")
				.append("                           currency.name as currency_name, ")
				.append("                      shp.sales_tax_rate as sales_tax_rate, ")
				.append("                          shp.amount_tax as amount_tax, ")
				.append("                        shp.amount_total as amount_total, ")
				.append("                    shp.cif_amount_total as cif_amount_total, ")
				.append("                               dest.code as destination_country_code, ")
				.append("                               actl.code as actual_destination_code, ")
				.append("                                 sm.name as shipping_method, ")
				.append("                        shp.date_created as date_created, ")
				.append("                              shp.status as status, ")
				.append("                         sui.export_date as export_date, ")
				.append("                        selr.currency_id as export_src_currency_id, ")
				.append("                        buyr.currency_id as export_dst_currency_id, ")
				.append("       sui.export_currency_exchange_rate as export_currency_exchange_rate, ")
				.append("sui.export_currency_exchange_rate_to_eur as export_currency_exchange_rate_to_eur, ")
				.append("                        sui.arrival_date as arrival_date, ")
				.append("                 sui.expect_arrival_date as expect_arrival_date ")								
				.append("from shipment shp ")
				.append("inner join shipment_uns_info sui on sui.shipment_id = shp.id ")
				.append("inner join shipping_method sm on sm.id = shp.shipping_method_id ")
				.append("inner join company selr on selr.id = shp.seller_company_id ")
				.append("inner join company buyr on buyr.id = shp.buyer_company_id ")
				.append("inner join currency currency on currency.id = shp.currency_id ")
				.append("inner join country dest on dest.id = shp.destination_country_id ")
				.append("left join country actl on actl.id = shp.actual_destination_id ")
				.append("where shp.name = :name ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("name", shipmentName);
		//this.entityManager.clear();
		List<ShipmentUnsImpl> resultList = getNamedParameterJdbcTemplate()
                .query(sqlSb.toString(),q,(rs , rowNum) -> new ShipmentUnsImpl(
				rs.getString("name"),
				rs.getString("type"),
				rs.getString("invoice_number"),
				rs.getString("tracking_number"),
				rs.getString("fba_id"),
				rs.getString("forwarder"),
				rs.getString("seller_company_kcode"),
				rs.getString("buyer_company_kcode"),
				rs.getString("currency_name"),
				rs.getBigDecimal("amount_total"),
				rs.getBigDecimal("cif_amount_total"),
				rs.getString("destination_country_code"),
				rs.getString("actual_destination_code"),
				rs.getString("shipping_method"),
				rs.getDate("date_created"),
				rs.getString("status"),
				rs.getDate("export_date"),
				rs.getInt("export_src_currency_id"),
				rs.getInt("export_dst_currency_id"),
				rs.getBigDecimal("export_currency_exchange_rate"),
				rs.getBigDecimal("export_currency_exchange_rate_to_eur"),
				rs.getTimestamp("arrival_date"),
				rs.getTimestamp("expect_arrival_date")
		));
		Assert.isTrue(resultList.size()<=1,"SIZE:"+resultList.size());
		if ((resultList == null) || (resultList.size() == 0)) return null;
		ShipmentUnsImpl shp = resultList.get(0);
		shp.setLineItems(this.queryShipmentOfInventoryLineItems(shipmentName));
		System.out.println("ShipmentUNS: " + shp);
		return shp;
	}
	
	@Override @SuppressWarnings("unchecked")
	public List<IvsLineItem> queryInventoryShipmentLineItem(String inventoryShpName) {
		StringBuilder sqlSb = new StringBuilder()
				.append("select               shpli.id as id, ")
				.append("               shpli.line_seq as line_seq, ")
				.append("                shpli.box_num as box_num, ")
				.append("     shpli.mixed_box_line_seq as mixed_box_line_seq, ")
				.append("                 shp_src.name as source_shipment_name, ")
				.append("               ps.code_by_drs as code_by_drs, ")
				.append("         pps.name_by_supplier as name_by_supplier, ")
				.append("              shpli.qty_order as quantity, ")
				.append("            shpli.unit_amount as unit_amount, ")
				.append("           shpli.ctn_dim_1_cm as ctn_dim_1_cm, ")
				.append("           shpli.ctn_dim_2_cm as ctn_dim_2_cm, ")
				.append("           shpli.ctn_dim_3_cm as ctn_dim_3_cm, ")
				.append("shpli.gross_weight_per_ctn_kg as gross_weight_per_ctn_kg, ")
				.append("          shpli.units_per_ctn as units_per_ctn, ")
				.append("         shpli.numbers_of_ctn as numbers_of_ctn, ")
				.append("      shpli.require_packaging as require_packaging, ")
				.append("     shpli.gui_invoice_number as gui_invoice_number, ")
				.append("          shpli.gui_file_name as gui_file_name, ")
				.append("               shpli.gui_uuid as gui_uuid, ")
				.append("    shpli.carton_number_start as carton_number_start, ")
				.append("      shpli.carton_number_end as carton_number_end , ")
				.append("      shpli.line_status as line_status  ")
				.append("from shipment_line_item shpli ")
				.append("left join shipment shp_src on shp_src.id = shpli.source_shipment_id ")
				.append("inner join shipment shp on shp.id = shpli.shipment_id ")
				.append("inner join product_sku ps on ps.id = shpli.sku_id ")
				.append("inner join product pps on pps.id = ps.product_id ")
				.append("where shp.name = :shipmentName ")
				.append("and shp.type = :inventoryShpTypeName ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("shipmentName", inventoryShpName);
		q.addValue("inventoryShpTypeName", ShipmentType.INVENTORY.getValue());

		return  (List) getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,(rs, rowNum) -> new ShipmentIvsLineItemImpl(
				rs.getInt("id"),rs.getInt("line_seq"),rs.getInt("box_num"),rs.getInt("mixed_box_line_seq"),
				rs.getString("code_by_drs"),rs.getString("name_by_supplier"),
				rs.getBigDecimal("quantity"), rs.getBigDecimal("unit_amount"),
				rs.getBigDecimal("ctn_dim_1_cm"), rs.getBigDecimal("ctn_dim_2_cm"), rs.getBigDecimal("ctn_dim_3_cm"),
				rs.getBigDecimal("gross_weight_per_ctn_kg"), rs.getInt("units_per_ctn"),
				rs.getInt("numbers_of_ctn"), rs.getBoolean("require_packaging"),
				rs.getString("gui_invoice_number"), rs.getString("gui_file_name"),
				rs.getString("gui_uuid"), rs.getInt("carton_number_start"),
				rs.getInt("carton_number_end"), rs.getString("line_status")
		));

	}
	
	@Override@SuppressWarnings("unchecked")
	public List<ShipmentUns.ShipmentUnsLineItem> queryShipmentOfInventoryLineItems(String shipmentName) {
		StringBuilder sqlSb = new StringBuilder()
				.append("select                shpli.id as id, ")
				.append("                shpli.line_seq as line_seq, ")
				.append("                 shpli.box_num as box_num, ")
				.append("      shpli.mixed_box_line_seq as mixed_box_line_seq, ")
				.append("                  src_shp.name as source_shipment_name, ")
				.append("                ps.code_by_drs as code_by_drs, ")
				.append("          pps.name_by_supplier as name_by_supplier, ")
				.append("               shpli.qty_order as quantity, ")
				.append("             shpli.unit_amount as unit_amount, ")
				.append("       shpliiu.unit_cif_amount as unit_cif_amount, ")
				.append("            shpli.ctn_dim_1_cm as ctn_dim_1_cm, ")
				.append("            shpli.ctn_dim_2_cm as ctn_dim_2_cm, ")
				.append("            shpli.ctn_dim_3_cm as ctn_dim_3_cm, ")
				.append(" shpli.gross_weight_per_ctn_kg as gross_weight_per_ctn_kg, ")
				.append("           shpli.units_per_ctn as units_per_ctn, ")
				.append("          shpli.numbers_of_ctn as numbers_of_ctn, ")
				.append("     shpli.carton_number_start as carton_number_start, ")
				.append("       shpli.carton_number_end as carton_number_end  ")
				.append("from shipment_line_item shpli ")
				.append("inner join shipment shp on shp.id = shpli.shipment_id ")
				.append("left join shipment src_shp on src_shp.id = shpli.source_shipment_id ")
				.append("inner join product_sku ps on ps.id = shpli.sku_id ")
				.append("inner join product pps on pps.id = ps.product_id ")
				.append("left  join shipment_line_item_info_uns shpliiu on shpliiu.shipment_line_item_id = shpli.id ")
				.append("where shp.name = :shipmentName ")
				.append("order by shpli.line_seq ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("shipmentName", shipmentName);


		return (List) getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,(rs,rowNum) -> new ShipmentUnsLineItemImpl(
				rs.getInt("id"),rs.getInt("line_seq"),rs.getInt("box_num"),rs.getInt("mixed_box_line_seq"),rs.getString("source_shipment_name"),
				rs.getString("code_by_drs"),rs.getString("name_by_supplier"),
				rs.getBigDecimal("quantity"),rs.getBigDecimal("unit_amount"),
				rs.getBigDecimal("unit_cif_amount"),rs.getBigDecimal("ctn_dim_1_cm"),
				rs.getBigDecimal("ctn_dim_2_cm"),rs.getBigDecimal("ctn_dim_3_cm")
				,rs.getBigDecimal("gross_weight_per_ctn_kg"),rs.getInt("units_per_ctn"),
				rs.getInt("numbers_of_ctn"),rs.getInt("carton_number_start"),rs.getInt("carton_number_end")

		));
	}

	@Override
	public Integer queryMaxSerialIdOfDraftShipments(String companyKcode) {
		StringBuilder sqlSb = new StringBuilder()
				.append("select max(serial_id) from shipment shp ")
				.append("inner join company seller on seller.id = shp.seller_company_id ")
				.append("where seller.k_code = :companyKcode ")
				.append("and shp.status = :draftStatusName ")
				.append("and shp.type = :unifiedTypeName ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("companyKcode", companyKcode);
		q.addValue("draftStatusName", ShipmentStatus.SHPT_DRAFT.name());
		q.addValue("unifiedTypeName", ShipmentType.UNIFIED.getValue());
		Integer o = getNamedParameterJdbcTemplate().queryForObject(sqlSb.toString(),q,Integer.class);
		if (o == null)
			return 0;
		return o;
	}
	
	@Override
	public Integer queryMaxSerialIdOfNonDraftShipments(String companyKcode) {
		StringBuilder sqlSb = new StringBuilder()
				.append("select max(serial_id) from shipment shp ")
				.append("inner join company seller on seller.id = shp.seller_company_id ")
				.append("where seller.k_code = :copmanyKcode ")
				.append("and shp.status != :draftStatusName ")
				.append("and shp.type = :unifiedTypeName ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("copmanyKcode", companyKcode);
		q.addValue("draftStatusName", ShipmentStatus.SHPT_DRAFT.name());
		q.addValue("unifiedTypeName", ShipmentType.UNIFIED.getValue());
		Integer o = getNamedParameterJdbcTemplate().queryForObject(sqlSb.toString(),q,Integer.class);
		if (o == null)
			return 0;
		return o;
	}
	
	@Override
	public String queryCountryOfCompany(String kcode) {
		StringBuilder sqlSb = new StringBuilder()
				.append("select country.code ")
				.append("from company ")
				.append("inner join country on company.country_id = country.id ")
				.append("where company.k_code = :kcode");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("kcode", kcode);
		@SuppressWarnings("unchecked")
		List<String> kCodeList = getNamedParameterJdbcTemplate().queryForList(sqlSb.toString(),q,String.class);
		Assert.isTrue(kCodeList.size()==1);
		return kCodeList.get(0);
	}
	
	@Override @SuppressWarnings("unchecked")
	public String queryCompanyKcode(int countryId) {
		String sql = "select c.k_code from company c where c.country_id = :countryId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("countryId", countryId);
		List<String> kCodeList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		Assert.isTrue(kCodeList.size()==1,"No or more than one kcode found.");
		return kCodeList.get(0);
	}

	@Override @Transactional("transactionManager")
	public void updateStatus(String shipmentName, ShipmentStatus status) {
		String sql = "update shipment shp set status = :status where name = :shipmentName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("status",status.name());
		q.addValue("shipmentName",shipmentName);
		int updatedRows = getNamedParameterJdbcTemplate().update(sql,q);
		Assert.isTrue(updatedRows==1);
	}

	@Override @Transactional("transactionManager")
	public void updateInvoiceNumver(String shipmentName, String invoiceNumber) {
		String sql = "update shipment shp set invoice_number = :invoiceNumber where name = :shipmentName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("invoiceNumber",invoiceNumber);
		q.addValue("shipmentName",shipmentName);
		int updatedRows = getNamedParameterJdbcTemplate().update(sql,q);
		Assert.isTrue(updatedRows==1);
	}

	@Override @Transactional("transactionManager")
	public void updateExportDate(String shpName, Date date) {
		String sql = "update shipment_uns_info sui set export_date = :exportDate from shipment shp "
				+ "where shp.name = :shipmentName and shp.id = sui.shipment_id ";
		MapSqlParameterSource q = new MapSqlParameterSource();

		q.addValue("exportDate",date);
		q.addValue("shipmentName",shpName);
		int updatedRows = getNamedParameterJdbcTemplate().update(sql,q);
		Assert.isTrue(updatedRows==1);
	}

	@Override @Transactional("transactionManager")
	public void updateExportFxRate(String shpName, String rateStr) {
		if(rateStr==null||rateStr.replaceAll("\\s","")=="") return;
		BigDecimal rate = new BigDecimal(rateStr);
		String sql = "update shipment_uns_info sui set export_currency_exchange_rate = :exportCurrencyExchangeRate from shipment shp "
				+ "where shp.name = :shipmentName and shp.id = sui.shipment_id ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("exportCurrencyExchangeRate",rate);
		q.addValue("shipmentName",shpName);
		int updatedRows = getNamedParameterJdbcTemplate().update(sql,q);
		Assert.isTrue(updatedRows==1);
	}
	
	@Override @Transactional("transactionManager")
	public void updateExportFxRateToEur(String shpName, String rateStr) {
		if(rateStr==null||rateStr.replaceAll("\\s","")=="") return;
		BigDecimal rate = new BigDecimal(rateStr);
		String sql = "update shipment_uns_info sui set export_currency_exchange_rate_to_eur = :rate from shipment shp "
				+ "where shp.name = :shipmentName and shp.id = sui.shipment_id ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("rate",rate);
		q.addValue("shipmentName",shpName);
		int updatedRows = getNamedParameterJdbcTemplate().update(sql,q);
		Assert.isTrue(updatedRows==1);
	}
	
	@Override @Transactional("transactionManager")
	public String updateNameAndSerialId(String origName, String newName,int serialId) {
		String sql = "update shipment shp set serial_id = :serialId, name = :newName where name = :origName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("serialId",serialId);
		q.addValue("newName",newName);
		q.addValue("origName",origName);
		int updatedRows = getNamedParameterJdbcTemplate().update(sql,q);
		Assert.isTrue(updatedRows==1);
		return newName;
	}
	
	@Override @Transactional("transactionManager")
	public void updateDestinationReceivedDate(String shpName, Date date) {
		String sql = "update shipment_uns_info sui set arrival_date = :arrivalDate from shipment shp "
				+ "where shp.name = :shipmentName and shp.id = sui.shipment_id ";
		MapSqlParameterSource q = new MapSqlParameterSource();

		q.addValue("arrivalDate",date);
		q.addValue("shipmentName",shpName);
		int updatedRows = getNamedParameterJdbcTemplate().update(sql,q);
		Assert.isTrue(updatedRows==1);
	}
	
	@Override @Transactional("transactionManager")
	public void updateExpectArrivalDate(String shpName, Date date) {
		String sql = "update shipment_uns_info sui set expect_arrival_date = :expectArrivalDate from shipment shp "
				+ "where shp.name = :shipmentName and shp.id = sui.shipment_id ";
		MapSqlParameterSource q = new MapSqlParameterSource();

		q.addValue("expectArrivalDate",date);
		q.addValue("shipmentName",shpName);
		int updatedRows = getNamedParameterJdbcTemplate().update(sql,q);
		Assert.isTrue(updatedRows==1);		
	}
		
	@Override @Transactional("transactionManager")
	public void updateTrackingNumber(String shpName, String trackingNumber) {
		String sql = "update shipment_uns_info sui set tracking_number = :trackingNumber from shipment shp "
				+ "where shp.name = :shipmentName and shp.id = sui.shipment_id ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("trackingNumber",trackingNumber);
		q.addValue("shipmentName",shpName);
		int updatedRows = getNamedParameterJdbcTemplate().update(sql,q);
		Assert.isTrue(updatedRows==1);		
	}

	@Override @Transactional("transactionManager")
	public void updateFbaId(String shpName, String fbaId){
		String sql = "update shipment_uns_info sui set fba_id = :fbaId from shipment shp "
				+ "where shp.name = :shipmentName and shp.id = sui.shipment_id ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("fbaId",fbaId);
		q.addValue("shipmentName",shpName);
		int updatedRows = getNamedParameterJdbcTemplate().update(sql,q);
		Assert.isTrue(updatedRows==1);		
	}
	
	@Override @Transactional("transactionManager")
	public void updateForwarder(String shpName, ShipmentUns.Forwarder forwarder) {
		String sql = "update shipment_uns_info sui set forwarder = :forwarder from shipment shp "
				+ "where shp.name = :shipmentName and shp.id = sui.shipment_id ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("forwarder",forwarder==null?null:forwarder.name());
		q.addValue("shipmentName",shpName);
		int updatedRows = getNamedParameterJdbcTemplate().update(sql,q);
		Assert.isTrue(updatedRows==1);		
	}
	
	@Override @Transactional("transactionManager")
	public String updateData(ShipmentUns shipment,BigDecimal ddpTotal, BigDecimal cifTotal){
		StringBuilder sqlSb = new StringBuilder()
				.append("update shipment shp set ")
				.append("           amount_total = :amountTotal, ")
				.append("       cif_amount_total = :cifAmountTotal, ")
				.append(" destination_country_id = cty.id, ")
				.append("  actual_destination_id = actual.id, ")
				.append("     shipping_method_id = sm.id ")				
				.append("from country cty, country actual, shipping_method sm ")
				.append("where cty.code = :countryCode ")
				.append("and actual.code = :actualDestinationCode ")
				.append("and sm.name = :shippingMethod ")
				.append("and shp.name = :shipmentName");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("amountTotal",ddpTotal);
		q.addValue("cifAmountTotal",cifTotal);
		q.addValue("shippingMethod",shipment.getShippingMethod().name());
		q.addValue("countryCode",shipment.getDestinationCountry());
		q.addValue("actualDestinationCode",shipment.getActualDestination());
		q.addValue("shipmentName",shipment.getName());
		int insertedRows = getNamedParameterJdbcTemplate().update(sqlSb.toString(),q);
		this.deleteLineItemInfos(shipment.getName());
		this.deleteLineItems(shipment.getName());
		this.insertLineItems(shipment.getLineItems(), shipment.getName());
		Assert.isTrue(insertedRows==1,"No or more than one shipment updated.");
		return shipment.getName();
	}
	
	private void insertLineItems(List<ShipmentUns.ShipmentUnsLineItem> lineItems, String name){
		StringBuilder sqlSb = new StringBuilder()
				.append(" insert into shipment_line_item ")
				.append("( id, shipment_id, line_seq, source_shipment_id, box_num, mixed_box_line_seq, " +
						"sku_id,  qty_order,  unit_amount, ctn_dim_1_cm, ctn_dim_2_cm, ctn_dim_3_cm, " +
						"gross_weight_per_ctn_kg, units_per_ctn, numbers_of_ctn, " +
						"carton_number_start, carton_number_end ) ")
				.append(" select ")
				.append(" :id,      shp.id, :lineSeq,         src_shp.id, :boxNum, :mixedBoxLineSeq,   " +
						"ps.id, :qty_order, :unit_amount,     :ctnDim1,     :ctnDim2,     :ctnDim3, " +
						"     :perCtnWeight,   :perCtnUnit,     :ctnCounts   , " +
						":cartonNumberStart,    :cartonNumberEnd  ")
				.append(" from product_sku ps ")
				.append(" inner join shipment shp on shp.name = :shipmentName ")
				.append(" inner join shipment src_shp on src_shp.name = :srcShpName ")
				.append(" where ps.code_by_drs = :codeByDrs ");

		lineItems.sort(
				Comparator.comparing(ShipmentUns.ShipmentUnsLineItem::getSourceInventoryShipmentName)
						.thenComparing(ShipmentUns.ShipmentUnsLineItem::getBoxNum)
						.thenComparing(ShipmentUns.ShipmentUnsLineItem::getMixedBoxLineSeq));

		int lineSeq = 1;
		for(ShipmentUns.ShipmentUnsLineItem lineItem: lineItems){
			int lineItemId = PostgreSQLHelper.getNextVal(getNamedParameterJdbcTemplate(),"shipment_line_item","id");
			MapSqlParameterSource q = new MapSqlParameterSource();
			q.addValue("id", lineItemId);
			q.addValue("lineSeq", lineSeq++);
			q.addValue("boxNum",lineItem.getBoxNum());
			q.addValue("cartonNumberStart", lineItem.getCartonNumberStart());
			q.addValue("cartonNumberEnd", lineItem.getCartonNumberEnd());
			q.addValue("mixedBoxLineSeq",lineItem.getMixedBoxLineSeq());
			q.addValue("qty_order",new Integer(lineItem.getQuantity().replaceAll(",", "")));
			q.addValue("unit_amount",new BigDecimal(lineItem.getUnitAmount()));
			q.addValue("ctnDim1",new BigDecimal(lineItem.getCartonDimensionCm1()));
			q.addValue("ctnDim2",new BigDecimal(lineItem.getCartonDimensionCm2()));
			q.addValue("ctnDim3",new BigDecimal(lineItem.getCartonDimensionCm3()));
			q.addValue("perCtnWeight",new BigDecimal(lineItem.getPerCartonGrossWeightKg()));
			q.addValue("perCtnUnit",new BigDecimal(lineItem.getPerCartonUnits()));
			q.addValue("ctnCounts",new BigDecimal(lineItem.getCartonCounts()));
			q.addValue("shipmentName",name);
			q.addValue("srcShpName",lineItem.getSourceInventoryShipmentName());
			q.addValue("codeByDrs",lineItem.getSkuCode());
			int insertedRows = getNamedParameterJdbcTemplate().update(sqlSb.toString(),q);
			Assert.isTrue(insertedRows==1,"No or more than one shipmentLineItem inserted.");
			this.insertLineItemInfo(lineItemId, lineItem);
		}
	}
	
	private void insertLineItemInfo(int lineItemId, ShipmentUns.ShipmentUnsLineItem lineItem){
		if(!StringUtils.hasText(lineItem.getUnitCifAmount())) return;
		StringBuilder sqlSb = new StringBuilder()
				.append("insert into shipment_line_item_info_uns ")
				.append("(  shipment_line_item_id,  unit_cif_amount ) values ")
				.append("( :shipment_line_item_id, :unit_cif_amount ) ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("shipment_line_item_id", lineItemId);
		q.addValue("unit_cif_amount",new BigDecimal(lineItem.getUnitCifAmount()));
		int insertedRows = getNamedParameterJdbcTemplate().update(sqlSb.toString(),q);
		Assert.isTrue(insertedRows==1,"No or more than one shipmentLineItem inserted.");
	}
	
	private int deleteLineItemInfos(String name) {
		String sqlSb = "delete from shipment_line_item_info_uns shpliiu "
				+ "using shipment shp, shipment_line_item shpli "
				+ "where shp.id = shpli.shipment_id "
				+ "and shpli.id = shpliiu.shipment_line_item_id "
				+ "and shp.name = :name ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("name",name);
		return getNamedParameterJdbcTemplate().update(sqlSb.toString(),q);
	}

	private int deleteLineItems(String name){
		String sqlSb = "delete from shipment_line_item shpli using shipment shp where shp.id = shpli.shipment_id and shp.name = :shipmentName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("shipmentName",name);
		return getNamedParameterJdbcTemplate().update(sqlSb.toString(),q);
	}
	
	private void deleteInfo(String name){
		String sql = "delete from shipment_uns_info sui USING shipment shp where shp.id = sui.shipment_id and shp.name = :shipmentName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("shipmentName",name);
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql.toString(),q)==1);
	}
	
	@Override @Transactional("transactionManager")
	public String deleteDraft(String shipmentName) {
		this.deleteLineItemInfos(shipmentName);
		this.deleteLineItems(shipmentName);
		this.deleteInfo(shipmentName);
		String sql = "delete from shipment where name = :shipmentName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("shipmentName",shipmentName);
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql.toString(),q)==1,"More than one or no shpiment was deleted");
		return shipmentName;
	}
	
	@Override
	public ShipmentStatus queryStatus(String shipmentName) {
		String sql = "select status from shipment where name = :shipmentName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("shipmentName", shipmentName);
		@SuppressWarnings("unchecked")
		List<String> result = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		Assert.isTrue(result.size()==1);
		return ShipmentStatus.valueOf(result.get(0));
	}
	
	@Override @SuppressWarnings("unchecked")
	public String querySellerKcode(String name) {
		String sql = "select c.k_code from company c "
				+ "inner join shipment s on s.seller_company_id=c.id "
				+ "where s.name=:name ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("name",name);
		List<String> resultList= getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		Assert.isTrue(resultList.size()==1);
		return resultList.get(0);
	}

	@Override @Transactional("transactionManager")
	public void updateIvsStatus(Set<String> ivsNameSet, String status) {
		String sql = "update shipment shp set status = :status where name in (:ivsNameSet) ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("status",status);
		q.addValue("ivsNameSet",ivsNameSet);
		Assert.isTrue( getNamedParameterJdbcTemplate().update(sql,q)==ivsNameSet.size());
		return;
	}
	
	@Override
	public int queryQtyOrdered(String shipmentName, String sku, Integer boxNum, Integer mixedBoxLineSeq) {
		String sql = "select qty_order from shipment_line_item si "
				+ "inner join shipment s on s.id = si.shipment_id "
				+ "where s.name = :shpName "
				+ "and si.box_num = :boxNum "
				+ "and si.mixed_box_line_seq = :mixedBoxLineSeq "
				+ "and si.sku_id = ( select ps.id from product_sku ps where ps.code_by_drs = :sku ) ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("shpName", shipmentName);
		q.addValue("boxNum",boxNum);
		q.addValue("mixedBoxLineSeq",mixedBoxLineSeq);
		q.addValue("sku", sku);
		Integer o = getNamedParameterJdbcTemplate().queryForObject(sql,q,Integer.class);
		if (o == null) throw new IllegalArgumentException("method queryQtyOrdered: Query result is null");
		return o;
	}
	
	@Override @SuppressWarnings("unchecked")
	public int queryQtySold(String shipmentName,String sku, Integer boxNum, Integer mixedBoxLineSeq) {
		String sql = "select qty_sold from shipment_line_item si "
				+ "inner join shipment s on s.id = si.shipment_id "
				+ "where s.name = :shpName "
				+ "and si.box_num = :boxNum "
				+ "and si.mixed_box_line_seq = :mixedBoxLineSeq "
				+ "and si.sku_id = ( select ps.id from product_sku ps where ps.code_by_drs = :sku ) ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("shpName", shipmentName);
		q.addValue("boxNum",boxNum);
		q.addValue("mixedBoxLineSeq",mixedBoxLineSeq);
		q.addValue("sku", sku);
		Integer o = getNamedParameterJdbcTemplate().queryForObject(sql,q,Integer.class);
		if (o == null) throw new IllegalArgumentException("method queryQtySold: Query result is null");
		return o;
	}
	
	@Override @Transactional("transactionManager")
	public void updateSoldQty(String shpName, String sku, int soldQty, Integer boxNum, Integer mixedBoxLineSeq) {
		String sql = "update shipment_line_item si set qty_sold = qty_sold + :soldQty "
				+ "from shipment s "
				+ "where s.id = si.shipment_id "
				+ "and s.name = :shpName "
				+ "and si.box_num = :boxNum "
				+ "and si.mixed_box_line_seq = :mixedBoxLineSeq "
				+ "and si.sku_id = ( select ps.id from product_sku ps where ps.code_by_drs = :sku ) ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("soldQty",soldQty);
		q.addValue("shpName",shpName);
		q.addValue("boxNum",boxNum);
		q.addValue("mixedBoxLineSeq",mixedBoxLineSeq);
		q.addValue("sku",sku);
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
	}
	
	@Override @SuppressWarnings("unchecked") @Deprecated // TODO: to be remove
	public ProductSkuStock queryProductSkuStock(String shipmentName, String skuCodeByDrs) {
		String sql ="select  si.id as id, "
				+"          s.name as shipment_name, "
				+"  ps.code_by_drs as sku_code_by_drs, "
				+"     selr.k_code as seller_company_kcode, "
				+"     buyr.k_code as buyer_company_kcode, "
				+"   currency.name as currency_name, "
				+"  si.unit_amount as unit_cost, "
				+"    si.qty_order as qty_order, "
				+"     si.qty_sold as qty_sold, "
				+" si.qty_returned as qty_returned, "
				+"     source.name as parent_shipment_name "
				+ "from shipment_line_item si "
				+ "inner join product_sku ps on ps.id = si.sku_id "
				+ "inner join shipment s on s.id = si.shipment_id "
				+ "left  join shipment source on source.id = si.source_shipment_id "
				+ "inner join company selr on selr.id = s.seller_company_id "
				+ "inner join company buyr on buyr.id = s.buyer_company_id "
				+ "inner join currency on currency.id = s.currency_id "
				+ "where s.name = :shipmentName "
				+ "and ps.code_by_drs = :skuCodeByDrs ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("shipmentName",shipmentName);
		q.addValue("skuCodeByDrs",skuCodeByDrs);

		List<ProductSkuStockImpl> resultList = getNamedParameterJdbcTemplate().query(sql,q,(rs,rowNum) -> new ProductSkuStockImpl(
				rs.getInt("id"),rs.getString("shipment_name"),rs.getString("sku_code_by_drs"),
				rs.getString("seller_company_kcode"), rs.getString("buyer_company_kcode"),
				rs.getString("currency_name"),rs.getBigDecimal("unit_cost"),
				rs.getInt("qty_order"),rs.getInt("qty_sold"),rs.getInt("qty_returned"),rs.getString("parent_shipment_name")
		));

		Assert.isTrue(resultList.size()==1,"Incorrect Rows:"+resultList.size());
		return resultList.get(0);
	}

}