package com.kindminds.drs.persist.data.access.usecase.logistics;

import java.math.BigDecimal;
import java.util.*;


import com.kindminds.drs.persist.data.access.rdb.Dao;
import com.kindminds.drs.persist.v1.model.mapping.sales.PurchaseOrderSkuInfoImpl;
import com.kindminds.drs.api.v1.model.accounting.StatementLineType;
import com.kindminds.drs.api.v1.model.logistics.ShipmentIvs;
import com.kindminds.drs.api.v1.model.sales.PurchaseOrderInfo;
import com.kindminds.drs.api.v1.model.sales.PurchaseOrderSkuInfo;
import com.kindminds.drs.persist.v1.model.mapping.logistics.ShipmentIvsImpl;
import com.kindminds.drs.persist.v1.model.mapping.logistics.ShipmentIvsLineItemImpl;
import com.kindminds.drs.persist.v1.model.mapping.product.ProductSkuStockImpl;
import com.kindminds.drs.persist.v1.model.mapping.sales.PurchaseOrderInfoImpl;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.Ivs;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.IvsLineItem;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShipmentStatus;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShippingMethod;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import com.kindminds.drs.Country;
import com.kindminds.drs.Currency;

import com.kindminds.drs.api.data.access.usecase.logistics.MaintainShipmentIvsDao;


import com.kindminds.drs.api.v1.model.product.SKU.Status;
import com.kindminds.drs.api.v1.model.product.stock.ProductSkuStock;
import com.kindminds.drs.util.DateHelper;

@Repository
public class MaintainShipmentIvsDaoImpl extends Dao implements MaintainShipmentIvsDao {


	
	private final String dateFormat = "yyyy-MM-dd";

	@Override
	public ShipmentStatus queryStatus(String shipmentName) {
		String sql = "select status from shipment shp where shp.name = :shipmentName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("shipmentName", shipmentName);
		String o = getNamedParameterJdbcTemplate().queryForObject(sql,q ,String.class);
		ShipmentStatus status = ShipmentStatus.valueOf(o);
		Assert.notNull(status);
		return status;
	}
	@Override @SuppressWarnings("unchecked")
	public ShipmentIvs query(String name) {
		StringBuilder sqlSb = new StringBuilder()
				.append("select                         shp.id as id, ")
				.append("                             shp.name as name, ")
				.append("                             shp.type as type, ")
				.append("to_char(sii.expected_export_date,:fm) as expected_export_date, ")
				.append("         sii.shipment_fca_location_id as shipment_fca_location_id, ")
				.append("   to_char(sii.fca_delivery_date,:fm) as fca_delivery_date, ")
				.append("                   shp.invoice_number as invoice_number, ")
				.append("                          sndr.k_code as seller_company_kcode, ")
				.append("                          rcvr.k_code as buyer_company_kcode, ")
				.append("                             cur.name as currency_name, ")
				.append("                   shp.sales_tax_rate as sales_tax_rate, ")
				.append("                         shp.subtotal as subtotal, ")
				.append("                       shp.amount_tax as amount_tax, ")
				.append("                     shp.amount_total as amount_total, ")
				.append("                         country.code as destination_country_code, ")
				.append("                              sm.name as shipping_method, ")
				.append("    shp.date_created at time zone :tz as date_created, ")
				.append("    				sii.date_purchased as date_purchased, ")
				.append("    				 sii.internal_note as internal_note, ")
				.append("                  sii.special_request as special_request, ")
				.append("               sii.num_of_repackaging as num_of_repackaging, ")
				.append("                  sii.repackaging_fee as repackaging_fee, ")
				.append("                      sii.required_po as required_po, ")
				.append("                        sii.po_number as po_number, ")
				.append("                           shp.status as status ")
				.append("from shipment shp ")
				.append("left join shipment_info_ivs sii on sii.shipment_id = shp.id ")
				.append("inner join company sndr on sndr.id = shp.seller_company_id ")
				.append("inner join company rcvr on rcvr.id = shp.buyer_company_id ")
				.append("inner join currency cur on cur.id = shp.currency_id ")
				.append("inner join country on country.id = shp.destination_country_id ")
				.append("inner join shipping_method sm on sm.id = shp.shipping_method_id ");
		sqlSb.append("where shp.name = :name ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("name", name);
		q.addValue("fm", "YYYY-MM-DD");
		q.addValue("tz", "UTC");
		//this.entityManager.clear();


		List<ShipmentIvsImpl> resultList =  getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,(rs , rowNum) -> new ShipmentIvsImpl(
				rs.getInt("id") , rs.getString("name"),rs.getString("type"),rs.getString("expected_export_date"),
				rs.getString("shipment_fca_location_id"),rs.getString("fca_delivery_date"),
				rs.getString("invoice_number"),rs.getString("seller_company_kcode"),
				rs.getString("buyer_company_kcode")
				,rs.getString("currency_name") ,rs.getBigDecimal("sales_tax_rate") ,
				rs.getBigDecimal("subtotal"),rs.getBigDecimal("amount_tax"), rs.getBigDecimal("amount_total") ,
				rs.getString("destination_country_code"),
				rs.getString("shipping_method"),
				rs.getString("date_created"),rs.getString("status"),
				rs.getString("date_purchased"),
				rs.getString("internal_note"),
				rs.getString("special_request"),rs.getInt("num_of_repackaging"),rs.getBigDecimal("repackaging_fee"),
				rs.getBoolean("required_po"),rs.getString("po_number")
		));

		Assert.isTrue(resultList.size()<=1);
		if ((resultList == null) || (resultList.size() == 0)) return null;
		ShipmentIvsImpl shp = resultList.get(0);
		shp.setLineItems(this.queryShipmentOfInventoryLineItems(name));
		shp.setPaymentTotal(this.queryShipmentPaymentTotal(name));
		return shp;
	}
	@SuppressWarnings("unchecked")
	private List<ShipmentIvs.ShipmentIvsLineItem> queryShipmentOfInventoryLineItems(String shipmentName) {
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
				.append("            shpli.ctn_dim_1_cm as ctn_dim_1_cm, ")
				.append("            shpli.ctn_dim_2_cm as ctn_dim_2_cm, ")
				.append("            shpli.ctn_dim_3_cm as ctn_dim_3_cm, ")
				.append(" shpli.gross_weight_per_ctn_kg as gross_weight_per_ctn_kg, ")
				.append("           shpli.units_per_ctn as units_per_ctn, ")
				.append("          shpli.numbers_of_ctn as numbers_of_ctn, ")
				.append("       shpli.require_packaging as require_packaging, ")
				.append("      shpli.gui_invoice_number as gui_invoice_number, ")
				.append("           shpli.gui_file_name as gui_file_name, ")
				.append("                shpli.gui_uuid as gui_uuid, ")
				.append("     shpli.carton_number_start as carton_number_start, ")
				.append("       shpli.carton_number_end as carton_number_end ")
				.append("from shipment_line_item shpli ")
				.append("inner join shipment shp on shp.id = shpli.shipment_id ")
				.append("left join shipment src_shp on src_shp.id = shpli.source_shipment_id ")
				.append("inner join product_sku ps on ps.id = shpli.sku_id ")
				.append("inner join product pps on pps.id = ps.product_id ")
				.append("where shp.name = :shipmentName ")
				.append("order by box_num, mixed_box_line_seq ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("shipmentName", shipmentName);

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

	@Override
	public Integer queryMaxSerialIdOfDraftShipments(String supplierKcode) {
		StringBuilder sqlSb = new StringBuilder()
				.append("select max(serial_id) from shipment shp ")
				.append("inner join company selr on selr.id = shp.seller_company_id ")
				.append("where selr.k_code = :supplierKcode ")
				.append("and status = :draftStatusName ");
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("supplierKcode", supplierKcode);
		q.addValue("draftStatusName", ShipmentStatus.SHPT_DRAFT.name());
		Integer o = getNamedParameterJdbcTemplate().queryForObject(sqlSb.toString(),q,Integer.class);
		if (o == null)
			return 0;
		return o;
	}
	
	@Override
	public Integer queryMaxSerialIdOfNonDraftShipments(String supplierKcode) {
		StringBuilder sqlSb = new StringBuilder()
				.append("select max(serial_id) from shipment shp ")
				.append("inner join company supplier on supplier.id = shp.seller_company_id ")
				.append("where supplier.k_code = :supplierKcode ")
				.append("and status != :draftStatusName ");
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("supplierKcode", supplierKcode);
		q.addValue("draftStatusName", ShipmentStatus.SHPT_DRAFT.name());
		Integer o = getNamedParameterJdbcTemplate().queryForObject(sqlSb.toString(),q,Integer.class);
		if (o == null)
			return 0;
		return o;
	}
	
	@Override @Transactional("transactionManager")
	public String insert(String supplierKcode, Ivs shipment, Integer serialId,String draftName,BigDecimal salesTaxRate,BigDecimal subtotal,BigDecimal tax,BigDecimal total,Currency handlerCurrency,ShipmentStatus status) {

	    /*
	    int nextId = PostgreSQLHelper.getNextVal(this.entityManager,"shipment","id");
		String dateStr=shipment.getDateCreated();
		StringBuilder sqlSb = new StringBuilder()
				.append("INSERT INTO shipment ")
				.append("( id, serial_id,  name,  type, invoice_number, seller_company_id,        buyer_company_id,      currency_id, sales_tax_rate,  subtotal, amount_tax, amount_total, destination_country_id, shipping_method_id, date_created,  status ) ")
				.append("select ")
				.append(" :id, :serialId, :name, :type, :invoiceNumber,           splr.id, splr.handler_company_id, splr.currency_id,  :salesTaxRate, :subtotal, :amountTax, :amountTotal,                  ct.id,              sm.id,'"+dateStr+"', :status ")
				.append("from company splr, country ct, shipping_method sm ")
				.append("where splr.k_code = :supplierKcode ")
				.append("and ct.code = :countryCode ")
				.append("and sm.name = :shippingMethod ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("id",nextId);
		q.addValue("serialId",serialId);
		q.addValue("name",draftName);
		q.addValue("type",shipment.getType().getValue());
		q.addValue("invoiceNumber",shipment.getInvoiceNumber());
		q.addValue("salesTaxRate",salesTaxRate);
		q.addValue("subtotal",subtotal);
		q.addValue("amountTax",tax);
		q.addValue("amountTotal",total);
		q.addValue("shippingMethod",shipment.getShippingMethod().name());
		q.addValue("status",status.name());
		q.addValue("countryCode",shipment.getDestinationCountry());
		q.addValue("supplierKcode",supplierKcode);
		int insertedRows = q.executeUpdate();
		Assert.isTrue(insertedRows==1,"No or more than one shipment inserted.");
		this.insertInfo(draftName,shipment);
		this.insertLineItems(draftName, shipment);
		return draftName;
		*/
	    return "";
	}

	
	private void insertLineItems(String shipmentName, Ivs shipment){

		/*
		StringBuilder sqlSb = new StringBuilder()
				.append("insert into shipment_line_item ")
				.append("( shipment_id, line_seq, box_num, mixed_box_line_seq, sku_id,  qty_order,  unit_amount, ctn_dim_1_cm, ctn_dim_2_cm, ctn_dim_3_cm, gross_weight_per_ctn_kg, units_per_ctn, numbers_of_ctn, require_packaging, gui_invoice_number, gui_file_name, carton_number_start, carton_number_end ) ")
				.append("select ")
				.append("       shp.id, :lineSeq, :box_num, :mixed_box_line_seq, ps.id, :qty_order, :unit_amount,     :ctnDim1,     :ctnDim2,     :ctnDim3,           :perCtnWeight,   :perCtnUnit,  :ctnCounts, :require_packaging, :gui_invoice_number, :gui_file_name, :carton_number_start, :carton_number_end ")
				.append("from product_sku ps ")
				.append("inner join shipment shp on shp.name = :shipmentName ")
				.append("where ps.code_by_drs = :codeByDrs ");
		for(IvsLineItem lineItem: shipment.getLineItems()){
			MapSqlParameterSource q = new MapSqlParameterSource();
			q.addValue("lineSeq",lineItem.getLineSeq());
			q.addValue("box_num",lineItem.getBoxNum());
			q.addValue("mixed_box_line_seq",lineItem.getMixedBoxLineSeq());
			q.addValue("qty_order",new Integer(lineItem.getQuantity()));
			q.addValue("unit_amount",new BigDecimal(lineItem.getUnitAmount()));
			q.addValue("ctnDim1", lineItem.getCartonDimensionCm1()==null?
					BigDecimal.ZERO : new BigDecimal(lineItem.getCartonDimensionCm1()));
			q.addValue("ctnDim2", lineItem.getCartonDimensionCm2()==null?
					BigDecimal.ZERO : new BigDecimal(lineItem.getCartonDimensionCm2()));
			q.addValue("ctnDim3", lineItem.getCartonDimensionCm3()==null?
					BigDecimal.ZERO : new BigDecimal(lineItem.getCartonDimensionCm3()));
			q.addValue("perCtnWeight", lineItem.getPerCartonGrossWeightKg()==null?
					BigDecimal.ZERO : new BigDecimal(lineItem.getPerCartonGrossWeightKg()));
			q.addValue("perCtnUnit",new BigDecimal(lineItem.getPerCartonUnits()));
			q.addValue("ctnCounts", lineItem.getCartonCounts()==null?
					BigDecimal.ZERO : new BigDecimal(lineItem.getCartonCounts()));
			q.addValue("shipmentName",shipmentName);
			q.addValue("codeByDrs",lineItem.getSkuCode());
			q.addValue("require_packaging",lineItem.getRequireRepackaging()==null?
					false : lineItem.getRequireRepackaging());
			q.addValue("gui_invoice_number",lineItem.getGuiInvoiceNumber());
			q.addValue("gui_file_name",lineItem.getGuiFileName());
			q.addValue("carton_number_start",lineItem.getCartonNumberStart());
			q.addValue("carton_number_end",lineItem.getCartonNumberEnd());
			int insertedRows = q.executeUpdate();
			Assert.isTrue(insertedRows==1,"No or more than one shipmentLineItem inserted:"+insertedRows);
		}
		*/
	}

	@Override @Transactional("transactionManager")
	public void updateGuiInvoiceData(Ivs shipment) {
		String sql = "update shipment_line_item sli "
				+ " SET gui_file_name = :gui_file_name, "
				+ " gui_uuid = :gui_uuid"
				+ " gui_invoice_number = :gui_invoice_number "
				+ " FROM shipment sh "
				+ " WHERE sh.id = sli.shipment_id "
				+ " AND sh.name = :name "
				+ " AND sli.box_num = :box_num "
				+ " AND sli.mixed_box_line_seq = :mixed_box_line_seq ";
		for(IvsLineItem lineItem: shipment.getLineItems()) {
			MapSqlParameterSource q = new MapSqlParameterSource();
			q.addValue("gui_file_name", lineItem.getGuiFileName());
			q.addValue("gui_uuid" , lineItem.getGuiuuid());
			q.addValue("gui_invoice_number", lineItem.getGuiInvoiceNumber());
			q.addValue("name", shipment.getName());
			q.addValue("box_num", lineItem.getBoxNum());
			q.addValue("mixed_box_line_seq", lineItem.getMixedBoxLineSeq());
			int updated = getNamedParameterJdbcTemplate().update(sql,q);
			Assert.isTrue(updated == 1, "No or more than one shipmentLineItem inserted:"+updated);
		}
	}

	@Override @Transactional("transactionManager")
	public String updateData(Ivs shipment, BigDecimal salesTaxRate, BigDecimal subtotal, BigDecimal salesTax, BigDecimal total) {
		StringBuilder sqlSb = new StringBuilder()
				.append("update shipment shp set ")
				.append("        sales_tax_rate = :salesTaxRate, ")
				.append("              subtotal = :subtotal, ")
				.append("            amount_tax = :salesTax, ")
				.append("          amount_total = :total, ")
				.append("destination_country_id = :countryId, ")
				.append("    shipping_method_id = sm.id ")
				.append("from shipping_method sm ")
				.append("where sm.name = :shippingMethod ")
				.append("and shp.name = :shipmentName");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("salesTaxRate",salesTaxRate);
		q.addValue("subtotal",subtotal);
		q.addValue("salesTax",salesTax);
		q.addValue("total",total);
		q.addValue("shippingMethod",shipment.getShippingMethod().name());
		q.addValue("countryId",Country.valueOf(shipment.getDestinationCountry()).getKey());
		q.addValue("shipmentName",shipment.getName());
		int insertedRows = getNamedParameterJdbcTemplate().update(sqlSb.toString(),q);
		Assert.isTrue(insertedRows==1,"No or more than one shipment updated.");
		this.deleteInfo(shipment.getName());
		this.insertInfo(shipment.getName(),shipment);
		this.deleteLineItems(shipment.getName());
		this.insertLineItems(shipment.getName(),shipment);
		return shipment.getName();
	}
	
	private void deleteInfo(String shipmentName) {
		StringBuilder sqlSb = new StringBuilder();
		sqlSb.append("delete from shipment_info_ivs sii using shipment shp where shp.id = sii.shipment_id and shp.name = :shipmentName ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("shipmentName",shipmentName);
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sqlSb.toString(),q)==1);
	}
	
	private void insertInfo(String shipmentName,Ivs shipment) {
		StringBuilder sqlSb = new StringBuilder()
				.append("insert into shipment_info_ivs ")
				.append("( shipment_id, fca_delivery_date, shipment_fca_location_id, expected_export_date, special_request, num_of_repackaging, repackaging_fee, required_po, po_number ) select ")
				.append("       shp.id,  :fcaDeliveryDate,   :shipmentFcaLocationId,  :expectedExportDate, :special_request, :num_of_repackaging, :repackaging_fee, :required_po, :po_number from shipment shp where shp.name = :shipmentName");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("shipmentName",shipmentName);
		q.addValue("expectedExportDate",DateHelper.toDate(shipment.getExpectedExportDate(),this.dateFormat));
		q.addValue("shipmentFcaLocationId",Short.parseShort(shipment.getFcaDeliveryLocationId())); 
		q.addValue("fcaDeliveryDate",DateHelper.toDate(shipment.getFcaDeliveryDate(),this.dateFormat));
		q.addValue("special_request",shipment.getSpecialRequest());
		String numBoxes = shipment.getBoxesNeedRepackaging();
		q.addValue("num_of_repackaging",numBoxes == null?
				0 : Integer.valueOf(numBoxes));
		q.addValue("repackaging_fee",shipment.getRepackagingFee() == null?
				BigDecimal.ZERO : new BigDecimal(shipment.getRepackagingFee()));
		q.addValue("required_po",shipment.getRequiredPO() == null?
				false : shipment.getRequiredPO());
		q.addValue("po_number",shipment.getPoNumber());
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sqlSb.toString(),q)==1,"No or more than one shipment inserted.");
	}
	
	private void deleteLineItems(String shipmentName) {
		StringBuilder sqlSb = new StringBuilder();
		sqlSb.append("delete from shipment_line_item shpli using shipment shp where shp.id = shpli.shipment_id and shp.name = :shipmentName ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("shipmentName",shipmentName);
		getNamedParameterJdbcTemplate().update(sqlSb.toString(),q);
	}
	
	@Override @Transactional("transactionManager")
	public String updateStatus(String shipmentName, ShipmentStatus status) {
		String sql = "update shipment shp set status = :status where name = :shipmentName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("status",status.name());
		q.addValue("shipmentName",shipmentName);
		int updatedRows = getNamedParameterJdbcTemplate().update(sql.toString(),q);
		Assert.isTrue(updatedRows==1);
		return shipmentName;
	}
	
	@Override @Transactional("transactionManager")
	public String updatePurchasedDate(String shipmentName, Date purchasedDate) {
		String sql = "update shipment_info_ivs sii set date_purchased = :purchasedDate "
				+"from shipment "
				+"inner join shipment shp on shp.name = :shipmentName "
				+"where sii.shipment_id = shp.id ";	
		MapSqlParameterSource q = new MapSqlParameterSource();	
		q.addValue("purchasedDate",purchasedDate);
		q.addValue("shipmentName",shipmentName);
		int updatedRows = getNamedParameterJdbcTemplate().update(sql.toString(),q);
		Assert.isTrue(updatedRows==1);
		return shipmentName;
	}
	
	@Override @Transactional("transactionManager")
	public void updateDateConfirmed(String shipmentName,Date date){
		String sql = "update shipment s set date_confirmed = :date where s.name = :shipmentName ";	
		MapSqlParameterSource q = new MapSqlParameterSource();	
		q.addValue("date",date);
		q.addValue("shipmentName",shipmentName);
		int updatedRows = getNamedParameterJdbcTemplate().update(sql.toString(),q);
		Assert.isTrue(updatedRows==1);
	}
	
	@Override @Transactional("transactionManager")
	public String updatedInternalNote(String shipmentName, String internalNote) {
		String sql = "update shipment_info_ivs sii set internal_note = :internalNote "
				+"from shipment "
				+"inner join shipment shp on shp.name = :shipmentName "
				+"where sii.shipment_id = shp.id ";
		MapSqlParameterSource q = new MapSqlParameterSource();	
		q.addValue("internalNote",internalNote);
		q.addValue("shipmentName",shipmentName);
		int updatedRows = getNamedParameterJdbcTemplate().update(sql.toString(),q);
		Assert.isTrue(updatedRows==1);
		return shipmentName;
	}
		
	@Override @Transactional("transactionManager")
	public void updateInvoiceNumber(String shipmentName, String invoiceNumber) {
		String sql = "update shipment shp set invoice_number = :invoiceNumber where name = :shipmentName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("invoiceNumber",invoiceNumber);
		q.addValue("shipmentName",shipmentName);
		int updatedRows = getNamedParameterJdbcTemplate().update(sql.toString(),q);
		Assert.isTrue(updatedRows==1);
	}
	@Override @Transactional("transactionManager")
	public String updateNameAndSerialId(String origName, String newName,int serialId) {
		String sql = "update shipment shp set serial_id = :serialId, name = :newName where name = :origName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("serialId",serialId);
		q.addValue("newName",newName);
		q.addValue("origName",origName);
		int updatedRows = getNamedParameterJdbcTemplate().update(sql.toString(),q);
		Assert.isTrue(updatedRows==1);
		return newName;
	}
	
	@Override @Transactional("transactionManager")
	public String delete(String shipmentName) {
		this.deleteInfo(shipmentName);
		this.deleteLineItems(shipmentName);
		String sql = "delete from shipment where name = :shipmentName and status = :draftStatusName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("shipmentName",shipmentName);
		q.addValue("draftStatusName",ShipmentStatus.SHPT_DRAFT.name());
		getNamedParameterJdbcTemplate().update(sql.toString(),q);
		return shipmentName;
	}
	
	@Override @SuppressWarnings("unchecked")
	public String queryBuyerKcode(String shipmentName) {
		String sql = "select c.k_code from shipment shp  "
				+ "inner join company c on c.id = shp.buyer_company_id "
				+ "where shp.name = :shipmentName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("shipmentName", shipmentName);
		List<String> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		Assert.isTrue(resultList.size()==1);
		return resultList.get(0);
	}
	
	@Override @SuppressWarnings("unchecked")
	public String querySellerKcode(String shipmentName) {
		StringBuilder sqlSb = new StringBuilder()
				.append("select c.k_code from shipment shp  ")
				.append("inner join company c on c.id = shp.seller_company_id ")
				.append("where shp.name = :shipmentName ");
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("shipmentName", shipmentName);
		List<String> resultList = getNamedParameterJdbcTemplate().queryForList(sqlSb.toString(),q,String.class);
		Assert.isTrue(resultList.size()==1);
		return resultList.get(0);
	}
	
	@Override @SuppressWarnings("unchecked")
	public String queryBuyerNameLocal(String shipmentName) {
		StringBuilder sqlSb = new StringBuilder()
				.append("select c.name_local from shipment shp  ")
				.append("inner join company c on c.id = shp.buyer_company_id ")
				.append("where shp.name = :shipmentName ");
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("shipmentName", shipmentName);
		List<String> resultList = getNamedParameterJdbcTemplate().queryForList(sqlSb.toString(),q,String.class);
		Assert.isTrue(resultList.size()==1);
		return resultList.get(0);
	}
	
	@Override @SuppressWarnings("unchecked")
	public String queryShipmentPickupRequesterEmail(String shipmentName) {
		StringBuilder sqlSb = new StringBuilder()
				.append("select ui.user_email from user_info ui ")
				.append("inner join shipment_info_ivs shpii on shpii.pick_up_requester_id = ui.user_id ")
				.append("inner join shipment shp on shp.id = shpii.shipment_id ")
				.append("where shp.name = :shipmentName ");
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("shipmentName", shipmentName);
		List<String> resultList = getNamedParameterJdbcTemplate().queryForList(sqlSb.toString(),q,String.class);
		Assert.isTrue(resultList.size()<=1);
		if(resultList.size()==0) return null;
		return resultList.get(0);
	}
	
	@Override @SuppressWarnings("unchecked")
	public Map<String, String> queryAllActiveSkuCodeToSupplierNameMap() {
		StringBuilder sqlSb = new StringBuilder()
				.append("select ps.code_by_drs, p.name_by_supplier ")
				.append("from product_sku ps ")
				.append("inner join product_base pb on pb.id = ps.product_base_id ")
				.append("inner join product_sku_status pss on pss.id = ps.status_id ")
				.append("inner join product p on p.id = ps.product_id ")
				.append("where pss.name in ( :activeStatusName, :preparingLaunchStatusName ) ");
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("activeStatusName", Status.SKU_ACTIVE.name());
		q.addValue("preparingLaunchStatusName", Status.SKU_PREPARING_LAUNCH.name());
		Map<String,String> allActiveSkuCodeToNameMap = new TreeMap<>();
		List<Object[]> result = getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,objArrayMapper);
		for(Object[] items:result){
			allActiveSkuCodeToNameMap.put((String)items[0], (String)items[1]);
		}
		return allActiveSkuCodeToNameMap;
	}

	@Override @SuppressWarnings("unchecked")
	public Map<String, String> queryActiveAndOnboardingSkuCodeToSupplierNameMap(String kcode) {
		StringBuilder sqlSb = new StringBuilder()
				.append("select ps.code_by_drs, p.name_by_supplier ")
				.append("from product_sku ps ")
				.append("inner join product_base pb on pb.id = ps.product_base_id ")
				.append("inner join product_sku_status pss on pss.id = ps.status_id ")
				.append("inner join product p on p.id = ps.product_id ")
				.append("inner join company splr on splr.id = pb.supplier_company_id ")
				.append("where splr.k_code = :kcode and splr.is_supplier is TRUE ")
				.append("and pss.name in( :activeStatusName, :onboardingStatusName )");
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("kcode", kcode);
		q.addValue("activeStatusName", Status.SKU_ACTIVE.name());
		q.addValue("onboardingStatusName", Status.SKU_PREPARING_LAUNCH.name());
		Map<String,String> activeSkuCodeToNameMap = new TreeMap<>();
		List<Object[]> result = getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,objArrayMapper);
		for(Object[] items:result){
			activeSkuCodeToNameMap.put((String)items[0], (String)items[1]);
		}
		return activeSkuCodeToNameMap;
	}
	
	@Override @SuppressWarnings("unchecked")
	public Map<Integer,String> queryAvailableFcaLocationIdToNameMap() {
		String sql = "SELECT sfl.id, sfl.name \n" +
				" FROM shipment_fca_location sfl  ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		Map<Integer,String> fcaLocationIdToNameMap = new HashMap<Integer,String>();
		List<Object[]> result = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		for(Object[] items:result){
			fcaLocationIdToNameMap.put((Integer)items[0], (String)items[1]);
		}
		return fcaLocationIdToNameMap;
	}
	
	@Override @SuppressWarnings("unchecked")
	public int queryDaysBeforeExportForFcaDelivery(Country targetCountry, ShippingMethod shippingMethod, int fcaLocationId) {
		StringBuilder sqlSb = new StringBuilder()
				.append("select days_before_export_for_fca_delivery ")
				.append("from shipment_fca_location_available_info sflai ")
				.append("inner join shipping_method sm on sm.id = sflai.shipping_method_id ")
				.append("where sflai.target_country_id = :targetCountryKey ")
				.append("and sm.name = :shippingMethodName ")
				.append("and sflai.shipment_fca_location_id = :fcaLocationId ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("targetCountryKey",targetCountry.getKey());
		q.addValue("shippingMethodName",shippingMethod.name());
		q.addValue("fcaLocationId",fcaLocationId);
		List<Short> resultList = getNamedParameterJdbcTemplate().queryForList(sqlSb.toString(),q,Short.class);
		Assert.isTrue(resultList.size()==1);
		Assert.notNull(resultList.get(0));
		return resultList.get(0).intValue();
	}

	@Override @SuppressWarnings("unchecked")
	public Currency queryCurrency(String shipmentName) {
		String sql = "select shp.currency_id from shipment shp where shp.name = :shipmentName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("shipmentName",shipmentName);
		List<Integer> resultList = getNamedParameterJdbcTemplate().queryForList(sql.toString(),q,Integer.class);
		Assert.isTrue(resultList.size()==1);
		return Currency.fromKey(resultList.get(0));
	}
	
	@Override @Transactional("transactionManager")
	public void setPickupRequsterForShipment(String shipmentName, int userId) {
		StringBuilder sqlSb = new StringBuilder()
				.append("update shipment_info_ivs shpii set pick_up_requester_id = :userId ")
				.append("from shipment shp ")
				.append("where shp.id = shpii.shipment_id ")
				.append("and shp.name = :shipmentName ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("userId",userId);
		q.addValue("shipmentName",shipmentName);
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sqlSb.toString(),q)==1,"No or more than one shipment inserted.");
	}
	
	@Override
	public void setConfirmor(String shipmentName, int userId) {
		StringBuilder sqlSb = new StringBuilder()
				.append("update shipment_info_ivs shpii set confirmor_id = :userId ")
				.append("from shipment shp ")
				.append("where shp.id = shpii.shipment_id ")
				.append("and shp.name = :shipmentName ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("userId",userId);
		q.addValue("shipmentName",shipmentName);
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sqlSb.toString(),q)==1,"No or more than one shipment inserted.");
	}

	@Override @SuppressWarnings("unchecked")
	public Map<String,BigDecimal> queryShipmentNameToPaymentTotalMap(List<String> shipmentNameList) {
		String sql = "select bsli.reference, sum(bsli.statement_amount) "
				+ "from bill_statementlineitem bsli "
				+ "inner join statement_line_type slt on slt.id = bsli.stlmnt_line_item_type_id "
				+ "where bsli.reference in (:shipmentNameList) "
				+ "and slt.name not in (:sellBackType) "
				+ "group by bsli.reference ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("shipmentNameList",shipmentNameList);
		q.addValue("sellBackType", StatementLineType.getSellbackTypes());
		Map<String,BigDecimal> resultMap = new HashMap<String,BigDecimal>();
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		for(Object[] columns:columnsList){
			String ivsName = (String)columns[0];
			BigDecimal paymentAmount = (BigDecimal)columns[1];
			resultMap.put(ivsName, paymentAmount);
		}
		return resultMap;
	}

	@Override @SuppressWarnings("unchecked")
	public BigDecimal queryShipmentPaymentTotal(String shipmentName) {
		String sql = "select sum(bsli.statement_amount) "
				+ "from bill_statementlineitem bsli "
				+ "inner join statement_line_type slt on slt.id = bsli.stlmnt_line_item_type_id "
				+ "where bsli.reference = :shipmentName "
				+ "and slt.name not in (:sellBackType) ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("shipmentName",shipmentName);
		q.addValue("sellBackType",StatementLineType.getSellbackTypes());
		List<BigDecimal> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,BigDecimal.class);
		Assert.isTrue(resultList.size()==1,"No or more than one result found.");		
		return resultList.get(0)==null?BigDecimal.ZERO:resultList.get(0);
	}
		
	@Override @SuppressWarnings("unchecked")
	public Map<String,String> queryShipmentNameToUnsNameMap(List<String> shipmentNameList) {
		StringBuilder sqlSb = new StringBuilder()
				.append("select distinct ivs.name ivs_name, uns.name uns_name ") 
				.append("from shipment ivs ")
				.append("inner join shipment_line_item unsi on unsi.source_shipment_id = ivs.id ")
				.append("inner join shipment uns on uns.id = unsi.shipment_id ")
				.append("where ivs.name in (:ivsNames) ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("ivsNames",shipmentNameList);
		Map<String,String> resultMap = new HashMap<>();
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,objArrayMapper);
		for(Object[] columns:columnsList){
			String ivsName = (String)columns[0];
			String unsName = (String)columns[1];
			resultMap.put(ivsName,unsName);
		}
		return resultMap;
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

	@Override
	public String queryCountryOfOrigin(String prodcutBaseCode) {
		String sql = " SELECT data->>'originalPlace' as country_of_origin " +
				" FROM   product.product_view " +
				" where product_base_code = :bp and market_side = 0  ";
		MapSqlParameterSource q = new MapSqlParameterSource();

		q.addValue("bp",prodcutBaseCode);
		return getNamedParameterJdbcTemplate().queryForObject(sql,q,String.class);
	}

	@Override
	public Boolean guiFileNameExists(String guiFileName, String shipmentName,
									 int boxNum, int mixedBoxLineSeq) {
		String sql = "select exists ("
				+ " select 1 from shipment_line_item sli "
				+ " INNER JOIN shipment sh on sh.id = sli.shipment_id "
				+ " WHERE gui_file_name = :gui_file_name)"
				+ " AND sh.name = :name "
				+ " AND sli.box_num = :box_num "
				+ " AND sli.mixed_box_line_seq = :mixed_box_line_seq ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("gui_file_name", guiFileName);
		return getNamedParameterJdbcTemplate().queryForObject(sql,q,Boolean.class);
	}

	@Override
	public PurchaseOrderInfo queryPurchaseOrderInfo(String shipmentName) {
		String sql = "select sh.id, cpy.name_local, cpy.phone_number, " +
				" cpy.address, sii.po_number, sii.fca_delivery_date, " +
				" sfl.name as fca_shipment_location, sh.subtotal, " +
				" sh.amount_tax, sh.amount_total, cur.name as currency, sh.sales_tax_rate" +
				" from company cpy " +
				" inner join shipment sh on sh.seller_company_id = cpy.id " +
				" inner join shipment_info_ivs sii on sii.shipment_id = sh.id " +
				" inner join shipment_fca_location sfl on sfl.id = sii.shipment_fca_location_id " +
				" inner join currency cur on cur.id = sh.currency_id " +
				" where sh.name = :shipmentName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("shipmentName", shipmentName);


		return getNamedParameterJdbcTemplate().queryForObject(sql,q , (rs, rowNum) -> new PurchaseOrderInfoImpl(
			rs.getInt("id"),rs.getString("name_local"),rs.getString("phone_number"),
				rs.getString("address"),rs.getString("po_number"),
			rs.getDate("fca_delivery_date"),rs.getString("fca_shipment_location"),
				rs.getBigDecimal("subtotal"),rs.getBigDecimal("amount_tax"),
				rs.getBigDecimal("amount_total"),rs.getString("currency"),rs.getBigDecimal("sales_tax_rate")
		));
	}

	@Override @SuppressWarnings("unchecked")
	public List<PurchaseOrderSkuInfo> queryPurchaseOrderInfoList(String shipmentName) {
		String sql = "select sh.id, ps.code_by_drs, " +
				" pd.name_by_supplier, sli.unit_amount, " +
				" sum(sli.qty_order) as qty_order, " +
				" (sli.unit_amount * sum(sli.qty_order)) as subtotal " +
				" from shipment sh " +
				" inner join shipment_line_item sli on sli.shipment_id = sh.id " +
				" inner join product_sku ps on ps.id = sli.sku_id " +
				" inner join product pd on pd.id = ps.product_id " +
				" where sh.name = :shipmentName " +
				" group by sh.id, ps.code_by_drs, " +
				" pd.name_by_supplier, sli.unit_amount " +
				" order by ps.code_by_drs";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("shipmentName", shipmentName);

		return (List) getNamedParameterJdbcTemplate().query(sql,q, (rs,rowNum) -> new PurchaseOrderSkuInfoImpl(
				rs.getInt("id"),rs.getString("code_by_drs"),rs.getString("name_by_supplier"),
				rs.getBigDecimal("unit_amount"),rs.getInt("qty_order"),	rs.getBigDecimal("subtotal")
		));
	}



	@Override @SuppressWarnings("unchecked")
	public Object[] queryShippingCostData(String productBaseCode, String countryCode) {
		String sql = "SELECT DISTINCT " +
				" data->>'hsCode' as hs_code,  " +
				" json_array_elements(CAST(data->>'products' as json)) ->> 'packageWeight' as product_weight, " +
				" json_array_elements(CAST(data->>'products' as json)) ->> 'packageWeightUnit' as product_weight_unit, " +
				" json_array_elements(CAST(data->>'products' as json)) ->> 'packageDimension1' as product_dim_1, " +
				" json_array_elements(CAST(data->>'products' as json)) ->> 'packageDimension2' as product_dim_2, " +
				" json_array_elements(CAST(data->>'products' as json)) ->> 'packageDimension3' as product_dim_3, " +
				" json_array_elements(CAST(data->>'products' as json)) ->> 'packageDimensionUnit' as product_dim_unit " +
				" FROM product.product_view p " +
				" INNER JOIN country cty on cty.id = p.market_side " +
				" WHERE cty.code = :countryCode " +
				" AND p.product_base_code = :bp ";

		//System.out.println("bp: " + productBaseCode);
		//System.out.println("countryCode: " + countryCode);

		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("bp", productBaseCode);
		q.addValue("countryCode", countryCode);
		List<Object[]> resultList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		for (Object[] result : resultList) {
			if (!Arrays.asList(result).contains("")) {
				return result;
			}
		}
		return null;
	}


	@Override @SuppressWarnings("unchecked")
	public String queryBatteryType(String productBaseCode) {
		String sql = "SELECT DISTINCT " +
				" json_array_elements(CAST(data->>'batteries' as json)) ->> 'batteryType' as battery_type " +
				" FROM  product.product_view " +
				" WHERE product_base_code = :bp";
		MapSqlParameterSource q = new MapSqlParameterSource();

		//productBaseCode
		q.addValue("bp", productBaseCode );
		List<String> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		if (resultList.isEmpty()) return "";
		return resultList.get(0);
	}

	@Override @SuppressWarnings("unchecked")
	public BigDecimal queryFreightFeeRate(
			String countryCode, ShippingMethod shippingMethod, Boolean msFlag, BigDecimal chargeableWeight) {
		String sql = " SELECT  " + shippingMethod.name() +
				" FROM freight_rate " +
				" WHERE ms_flag = :msFlag " +
				" AND country_code = :countryCode " +
				" AND min_eff_weight  <= :chargeableWeight  " +
				" AND effective_date <= now() " +
				" ORDER BY effective_date DESC, min_eff_weight DESC";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("msFlag", msFlag);
		q.addValue("countryCode", countryCode);
		q.addValue("chargeableWeight", chargeableWeight);
		List<BigDecimal> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,BigDecimal.class);
		if (resultList.isEmpty()) return BigDecimal.ZERO;
		return resultList.get(0);
	}



}
