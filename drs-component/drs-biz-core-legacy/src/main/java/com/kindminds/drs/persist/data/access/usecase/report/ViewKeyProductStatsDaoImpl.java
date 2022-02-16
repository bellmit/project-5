package com.kindminds.drs.persist.data.access.usecase.report;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;






import com.fasterxml.uuid.Generators;
import com.kindminds.drs.Filter;
import com.kindminds.drs.persist.data.access.rdb.Dao;
import com.kindminds.drs.api.v1.model.report.KeyProductStatsReport;
//import org.hibernate.ejb.EntityManagerImpl;
//import org.hibernate.service.jdbc.connections.spi.ConnectionProvider;

import com.kindminds.drs.enums.DisplayOption;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.kindminds.drs.Country;
import com.kindminds.drs.Marketplace;


import com.kindminds.drs.api.data.access.usecase.product.ViewKeyProductStatsDao;
import com.kindminds.drs.api.v1.model.product.SKU;
import com.kindminds.drs.api.v1.model.amazon.AmazonOrder.AmazonOrderStatus;
import com.kindminds.drs.api.v1.model.report.KeyProductStatsReport.KeyProductStatsReportLineItem;

import com.kindminds.drs.persist.v1.model.mapping.product.KeyProductStatsReportLineItemImpl;

@Repository
public class ViewKeyProductStatsDaoImpl extends Dao implements ViewKeyProductStatsDao {
	



	@Transactional("transactionManager")
	private void delete() {

		String sql = "delete from key_product_status ";

		getJdbcTemplate().update(sql);

	}

	@Override @Transactional("transactionManager")
	public void save(KeyProductStatsReport rpt) {

		this.delete();

		String period = rpt.getNextSettlementPeriod();

		rpt.getKeyProductStatsByCountryList().forEach(x -> {

			String region = x.getCountry();

			x.getBaseToKeySkuStatsListMap().entrySet().forEach(y -> {

				y.getValue().forEach(z -> {

					String sql = "insert into key_product_status "
							+ "(id , marketplace_region, supplier_kcode ,  supplier_name , base_code , base_code_drs ,  sku_code ,  sku_code_drs , " +
							" sku_name ,current_base_retail_price, settlement_period, qty_ordered_in_current_settlement_period ," +
							" qty_ordered_in_last_seven_days , qty_to_receive , qty_in_stock ," +
							" fba_qty_inbound ,fba_qty_instock ,  fba_qty_transfer )  values (" +
							"  :id , :marketplace_region, :supplier_kcode , :supplier_name , :base_code , :base_code_drs ,  :sku_code ,  :sku_code_drs , " +
							" :sku_name ,:current_base_retail_price, :settlement_period, :qty_ordered_in_current_settlement_period ," +
							" :qty_ordered_in_last_seven_days , :qty_to_receive , :qty_in_stock ," +
							" :fba_qty_inbound ,:fba_qty_instock ,  :fba_qty_transfer)";


				MapSqlParameterSource q = new MapSqlParameterSource();

					q.addValue("id", Generators.randomBasedGenerator().generate());
					q.addValue("marketplace_region", region);

					q.addValue("supplier_kcode", z.getSupplierKCode());
					q.addValue("supplier_name", z.getSupplierName());


					q.addValue("base_code", z.getBaseCode());
					q.addValue("base_code_drs", z.getBaseCodeByDrs());
					q.addValue("sku_code", z.getSkuCode());
					q.addValue("sku_code_drs", z.getSkuCodeByDrs());

					q.addValue("sku_name", z.getSkuName());
					q.addValue("current_base_retail_price", z.getNumericCurrentBaseRetailPrice());
					q.addValue("settlement_period", period);
					q.addValue("qty_ordered_in_current_settlement_period", z.getQtyOrderedInCurrentSettlementPeriod());
					q.addValue("qty_ordered_in_last_seven_days", z.getNumericQtyOrderedInLastSevenDays());

					q.addValue("qty_to_receive", z.getQtyToReceive());
					q.addValue("qty_in_stock", z.getQtyInStock());
					q.addValue("fba_qty_inbound", z.getFbaQtyInBound());
					q.addValue("fba_qty_instock", z.getFbaQtyInStock());
					q.addValue("fba_qty_transfer", z.getFbaQtyTransfer());


					getNamedParameterJdbcTemplate().update(sql,q);


				});

			});

		});
	}


		@Override
	public Date queryLatestStatementPeriodEnd(String supplierKcode){
		String sql = "select max(period_end) "
				+ "from bill_statement bs "
				+ "inner join company splr on splr.id = bs.receiving_company_id "
				+ "where splr.k_code = :supplierKcode ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("supplierKcode", supplierKcode);


		Date result = getNamedParameterJdbcTemplate().queryForObject(sql,q,Date.class);
		Assert.notNull(result);
		return result;
	}
	
	@Override @SuppressWarnings("unchecked")
	public String queryLatestStatementPeriodEndDateStr(String supplierKcode){
		String sql = "select to_char( max(period_end) at time zone 'UTC', :dateTimeFormatText ) "
				+ "from bill_statement bs "
				+ "inner join company splr on splr.id = bs.receiving_company_id "
				+ "where splr.k_code = :supplierKcode ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("dateTimeFormatText","YYYY-MM-DD HH24:MI:SS OF00");
		q.addValue("supplierKcode", supplierKcode);
		List<String> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		Assert.isTrue(resultList.size()==1);
		return resultList.get(0);
	}

	@Override @SuppressWarnings("unchecked")
	public String queryLatestStatementPeriodEndDateUtc(String supplierKcode) {
		String sql = "select to_char( max(period_end) at time zone 'UTC', :dateTimeFormatText ) "
				+ "from bill_statement bs "
				+ "inner join company splr on splr.id = bs.receiving_company_id "
				+ "where splr.k_code = :supplierKcode ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("dateTimeFormatText","YYYY-MM-DD");
		q.addValue("supplierKcode", supplierKcode);
		List<String> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		Assert.isTrue(resultList.size()==1);
		return resultList.get(0);
	}

	@Override @SuppressWarnings("unchecked")
	public String queryNextStatementPeriodStartDateUtc(String supplierKcode) {
		String sql = "select to_char( (max(period_end) + interval '13 days') at time zone 'UTC', :dateTimeFormatText ) "
				+ "from bill_statement bs "
				+ "inner join company splr on splr.id = bs.receiving_company_id "
				+ "where splr.k_code = :supplierKcode ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("dateTimeFormatText","YYYY-MM-DD");
		q.addValue("supplierKcode", supplierKcode);
		List<String> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		Assert.isTrue(resultList.size()==1);
		return resultList.get(0);
	}

	@Override @SuppressWarnings("unchecked")
	public String queryAmazonOrderLastUpdateDate(Country country) {
		String sql = "select to_char( date_last_update at time zone :tz, :fm ) "
				+ "from amazon_import_info where service_name = :serviceName and region = :region ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("tz", "UTC");
		q.addValue("fm","YYYY-MM-DD HH24:MI:SS OF00");
		q.addValue("serviceName", "Orders");
		q.addValue("region", country.name());
		List<String> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		Assert.isTrue(resultList.size()==1);
		return resultList.get(0);
	}

	@Override @SuppressWarnings("unchecked")
	public List<String> queryProductBaseIdList(String supplierKcode) {
		String sql = "select pb.code_by_supplier from product_base pb "
				+ "inner join company c on c.id = pb.supplier_company_id "
				+ "where c.k_code = :supplierKcode ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("supplierKcode", supplierKcode);
		List<String> countryCodeList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		return countryCodeList;
	}
	
	private String getBaseCodeSource(DisplayOption option){
		String result=null;
		if(option==DisplayOption.DRS) result = "pb.code_by_drs";
		else if(option==DisplayOption.SUPPLIER) result = "ppb.code_by_supplier";
		Assert.notNull(result);
		return result;
	}

	@Override @SuppressWarnings("unchecked")
	public List<KeyProductStatsReportLineItem> queryLineItems(List<Integer> marketplaceIds, String supplierKcode, DisplayOption option) {
		String baseCodeSource = this.getBaseCodeSource(option);
		StringBuilder sqlSb = new StringBuilder()
				.append("select ")
				.append("		distinct on (item.sku_code_by_drs) ")
				.append("		item.base_code_to_show as base_code_to_show, ")
				.append("		item.base_code_by_drs as base_code_by_drs, ")
				.append("		item.sku_code_by_supplier as sku_code_by_supplier, ")
				.append("		item.sku_code_by_drs as sku_code_by_drs, ")
				.append("		item.marketplace_sku as marketplace_sku, ")
				.append("		item.sku_name_by_supplier as sku_name_by_supplier, ")
				.append("		item.current_base_retail_price as current_base_retail_price , " +
						" item.k_code, item.short_name_local ")
				.append("from ( ")
				.append("select distinct on ( ps.code_by_drs ) ")
				.append(baseCodeSource).append("         as base_code_to_show, ")
				.append("                 pb.code_by_drs as base_code_by_drs, ")
				.append("           pps.code_by_supplier as sku_code_by_supplier, ")
				.append("                 ps.code_by_drs as sku_code_by_drs, ")
				.append("            pmi.marketplace_sku as marketplace_sku, ")
				.append("           pps.name_by_supplier as sku_name_by_supplier, ")
				.append("  pmi.current_base_retail_price as current_base_retail_price , " +
						"splr.k_code , splr.short_name_local  ")
				.append("from product_marketplace_info pmi ")
				.append("inner join product pps on pps.id = pmi.product_id ")
				.append("inner join product_sku ps on ps.product_id = pps.id ")
				.append("inner join product_sku_status pss on pss.id = ps.status_id ")
				.append("inner join product_base pb on pb.id = ps.product_base_id ")
				.append("inner join product ppb on ppb.id = pb.product_id ")
				.append("inner join company splr on splr.id = pb.supplier_company_id ")
				.append("where pmi.marketplace_id in (:marketplaceIds) ");
		if(supplierKcode!=null) sqlSb.append("and splr.k_code = :supplierKcode ");
		sqlSb.append("   and pss.name = :activeStatusName ");
		sqlSb.append("UNION ")
				.append("select distinct on ( ps.code_by_drs ) ")
				.append(baseCodeSource).append("         as base_code_to_show, ")
				.append("                 pb.code_by_drs as base_code_by_drs, ")
				.append("           pps.code_by_supplier as sku_code_by_supplier, ")
				.append("                 ps.code_by_drs as sku_code_by_drs, ")
				.append("            pmi.marketplace_sku as marketplace_sku, ")
				.append("           pps.name_by_supplier as sku_name_by_supplier, ")
				.append("  pmi.current_base_retail_price as current_base_retail_price ," +
						" splr.k_code , splr.short_name_local  ")
				.append("from product_k101_marketplace_info pmi ")
				.append("inner join product pps on pps.id = pmi.product_id ")
				.append("inner join product_sku ps on ps.product_id = pps.id ")
				.append("inner join product_sku_status pss on pss.id = ps.status_id ")
				.append("inner join product_base pb on pb.id = ps.product_base_id ")
				.append("inner join product ppb on ppb.id = pb.product_id ")
				.append("inner join company splr on splr.id = pb.supplier_company_id ")
				.append("where pmi.marketplace_id in (:marketplaceIds) ");
		if(supplierKcode!=null) sqlSb.append("and splr.k_code = :supplierKcode ");
		sqlSb.append("   and pss.name = :activeStatusName ");
		sqlSb.append("   ) AS item order by item.sku_code_by_drs; ");

		MapSqlParameterSource q = new MapSqlParameterSource();
		if(supplierKcode!=null) q.addValue("supplierKcode", supplierKcode);
		q.addValue("marketplaceIds", marketplaceIds);
		q.addValue("activeStatusName",SKU.Status.SKU_ACTIVE.name());

		/*
		this.skuCodeByDrs = skuCodeByDrs;
		this.baseCode = baseCode;
		this.supplierKCode = supplierKCode;
		this.supplierName = supplierName;
		this.baseCodeByDrs = baseCodeByDrs;
		this.skuCode = skuCode;
		this.marketplaceSku = marketplaceSku;
		this.skuName = skuName;
		this.numericCurrentBaseRetailPrice = numericCurrentBaseRetailPrice;
		 */

	  List<KeyProductStatsReportLineItemImpl> result  = getNamedParameterJdbcTemplate().query(sqlSb.toString(),q ,
				(rs, rowNum) -> new KeyProductStatsReportLineItemImpl(
						rs.getString("sku_code_by_drs"),rs.getString("base_code_to_show"),
						rs.getString("k_code"),rs.getString("short_name_local"),
						rs.getString("base_code_by_drs"),rs.getString("sku_code_by_supplier")
						,rs.getString("marketplace_sku"),rs.getString("sku_name_by_supplier")
						,rs.getBigDecimal("current_base_retail_price")
				)
		) ;


		return  (List) result;
	}
	
	@Override @SuppressWarnings("unchecked")
	public Map<String,Integer> queryAmazonQtyOrdered(Marketplace marketplace,String supplierKcode,Date purchaseDateFrom,Date purchaseDateTo) {
		Assert.isTrue(marketplace.isAmazonMarketplace());
		StringBuilder sqlSb = new StringBuilder()
				.append("select * from ( ")
				.append("select ")
				.append("pps.code_by_supplier, ")
				.append("coalesce(sum(aoi.quantity_ordered),0) ")
				.append("from product_marketplace_info pmi ")
				.append("inner join product pps on pps.id = pmi.product_id ")
				.append("inner join product_sku ps on ps.product_id = pps.id ")
				.append("inner join product_sku_status pss on pss.id = ps.status_id ")
				.append("inner join product_base pb on pb.id = ps.product_base_id ")
				.append("inner join company splr on splr.id = pb.supplier_company_id ")
				.append("inner join amazon_order_item aoi on aoi.seller_sku = pmi.marketplace_sku ")
				.append("inner join amazon_order ao on ao.id = aoi.order_db_id ")
				.append("where true ")
				.append("and pmi.marketplace_id = :marketplaceId ")
				.append("and pss.name = :activeStatusName ")
				.append("and ao.sales_channel = :marketplaceName ")
				.append("and ao.order_status != :canceledStatus ");
		if(purchaseDateFrom!=null) sqlSb.append("and ao.purchase_date >= :purchaseDateFrom ");
		if(purchaseDateTo!=null) sqlSb.append("and ao.purchase_date < :purchaseDateTo ");
		if(supplierKcode!=null) sqlSb.append("and splr.k_code = :supplierKcode ");
		sqlSb.append("group by pps.code_by_supplier ");
		sqlSb.append("UNION ")
			 .append("select ")
			 .append("pps.code_by_supplier, ")
			 .append("coalesce(sum(aoi.quantity_ordered),0) ")
			 .append("from product_k101_marketplace_info pmi ")
			 .append("inner join product pps on pps.id = pmi.product_id ")
			 .append("inner join product_sku ps on ps.product_id = pps.id ")
			 .append("inner join product_sku_status pss on pss.id = ps.status_id ")
			 .append("inner join product_base pb on pb.id = ps.product_base_id ")
			 .append("inner join company splr on splr.id = pb.supplier_company_id ")
			 .append("inner join amazon_order_item aoi on aoi.seller_sku = pmi.marketplace_sku ")
			 .append("inner join amazon_order ao on ao.id = aoi.order_db_id ")
			 .append("where true ")
			 .append("and pmi.marketplace_id = :marketplaceId ")
			 .append("and pss.name = :activeStatusName ")
			 .append("and ao.sales_channel = :marketplaceName ")
			 .append("and ao.order_status != :canceledStatus ");
		if(purchaseDateFrom!=null) sqlSb.append("and ao.purchase_date >= :purchaseDateFrom ");
		if(purchaseDateTo!=null) sqlSb.append("and ao.purchase_date < :purchaseDateTo ");
		if(supplierKcode!=null) sqlSb.append("and splr.k_code = :supplierKcode ");
		sqlSb.append("group by pps.code_by_supplier ");		
		sqlSb.append(" ) AS item; ");

	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("marketplaceId", marketplace.getKey());
		q.addValue("canceledStatus", AmazonOrderStatus.CANCELED.getStrValue());
		q.addValue("activeStatusName",SKU.Status.SKU_ACTIVE.name() );
		q.addValue("marketplaceName",marketplace.getName() );
		if(purchaseDateFrom!=null) q.addValue("purchaseDateFrom", purchaseDateFrom);
		if(purchaseDateTo!=null) q.addValue("purchaseDateTo", purchaseDateTo);
		if(supplierKcode!=null) q.addValue("supplierKcode", supplierKcode);
		Map<String,Integer> serviceItemKeyToDrsNameMap = new HashMap<>();
		List<Object[]> result = getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,objArrayMapper);
		for(Object[] items:result){
			serviceItemKeyToDrsNameMap.put((String)items[0], ((Long)items[1]).intValue());
		}
		return serviceItemKeyToDrsNameMap;
	}
	
	@Override @SuppressWarnings("unchecked")
	public Map<String,Integer> queryShopifyQtyOrdered(Marketplace marketplace,String supplierKcode,Date createDateFrom,Date createDateTo){
		Assert.isTrue(marketplace.isShopifyMarketplace());
		StringBuilder sqlSb = new StringBuilder()
				.append("select * from ( ")
				.append("select ")
				.append("pps.code_by_supplier, ")
				.append("coalesce(sum(soli.quantity),0) ")
				.append("from product_marketplace_info pmi ")
				.append("inner join product pps on pps.id = pmi.product_id ")
				.append("inner join product_sku ps on ps.product_id = pps.id ")
				.append("inner join product_sku_status pss on pss.id = ps.status_id ")
				.append("inner join product_base pb on pb.id = ps.product_base_id ")
				.append("inner join company splr on splr.id = pb.supplier_company_id ")
				.append("inner join shopify_order_line_item soli on soli.sku = pmi.marketplace_sku ")
				.append("inner join shopify_order so on so.id = soli.shopify_order_id ")
				.append("where true ")
				.append("and pmi.marketplace_id = :marketplaceId ")
				.append("and pss.name = :activeStatusName ")
				.append("and so.financial_status = :paidStatus ");
		if(createDateFrom!=null) sqlSb.append("and so.created_at >= :createDateFrom ");
		if(createDateTo!=null) sqlSb.append("and so.created_at < :createDateTo ");
		if(supplierKcode!=null) sqlSb.append("and splr.k_code = :supplierKcode ");
		sqlSb.append("group by pps.code_by_supplier ");
		sqlSb.append("UNION ")
			 .append("select ")
			 .append("pps.code_by_supplier, ")
			 .append("coalesce(sum(soli.quantity),0) ")
			 .append("from product_k101_marketplace_info pmi ")
			 .append("inner join product pps on pps.id = pmi.product_id ")
			 .append("inner join product_sku ps on ps.product_id = pps.id ")
			 .append("inner join product_sku_status pss on pss.id = ps.status_id ")
			 .append("inner join product_base pb on pb.id = ps.product_base_id ")
			 .append("inner join company splr on splr.id = pb.supplier_company_id ")
			 .append("inner join shopify_order_line_item soli on soli.sku = pmi.marketplace_sku ")
			 .append("inner join shopify_order so on so.id = soli.shopify_order_id ")
			 .append("where true ")
			 .append("and pmi.marketplace_id = :marketplaceId ")
			 .append("and pss.name = :activeStatusName ")
			 .append("and so.financial_status = :paidStatus ");
		if(createDateFrom!=null) sqlSb.append("and so.created_at >= :createDateFrom ");
		if(createDateTo!=null) sqlSb.append("and so.created_at < :createDateTo ");
		if(supplierKcode!=null) sqlSb.append("and splr.k_code = :supplierKcode ");
		sqlSb.append("group by pps.code_by_supplier ");
		sqlSb.append(" ) AS item; ");
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("marketplaceId", marketplace.getKey());
		q.addValue("paidStatus", "paid");
		q.addValue("activeStatusName",SKU.Status.SKU_ACTIVE.name() );
		if(createDateFrom!=null) q.addValue("createDateFrom", createDateFrom);
		if(createDateTo!=null) q.addValue("createDateTo", createDateTo);
		if(supplierKcode!=null) q.addValue("supplierKcode", supplierKcode);
		Map<String,Integer> serviceItemKeyToDrsNameMap = new HashMap<>();
		List<Object[]> result = getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,objArrayMapper);
		for(Object[] items:result){
			serviceItemKeyToDrsNameMap.put((String)items[0], ((Long)items[1]).intValue());
		}
		return serviceItemKeyToDrsNameMap;
	}

	@Override @SuppressWarnings("unchecked")
	public Map<String, Integer> queryQtyToReceive(Country country,String supplierKcode) {
		StringBuilder sqlSb = new StringBuilder()
				.append("select ")
				.append("ps.code_by_drs, ")
				.append("coalesce(sum(unsi.qty_order),0) ")
				.append("from shipment_line_item unsi ")
				.append("inner join shipment uns on uns.id = unsi.shipment_id ")
				.append("inner join shipment_uns_info shpui on shpui.shipment_id = uns.id ")
				.append("inner join product_sku ps on ps.id = unsi.sku_id ")
				.append("inner join product_sku_status pssts on pssts.id = ps.status_id ")
				.append("inner join product_base pb on pb.id = ps.product_base_id ")
				.append("inner join company splr on splr.id = pb.supplier_company_id ")
				.append("inner join company ms on ms.id = uns.buyer_company_id ")
				.append("where ms.country_id = :countryKey ")
				.append("and pssts.name = :activeStatusName ")
				.append("and shpui.arrival_date is null ");
		if(supplierKcode!=null) sqlSb.append("and splr.k_code = :supplierKcode ");
		sqlSb.append("group by ps.code_by_drs ");
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("countryKey", country.getKey());
		if(supplierKcode!=null) q.addValue("supplierKcode", supplierKcode);
		q.addValue("activeStatusName",SKU.Status.SKU_ACTIVE.name() );
		Map<String,Integer> serviceItemKeyToDrsNameMap = new HashMap<>();
		List<Object[]> result = getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,objArrayMapper);
		for(Object[] items:result){
			serviceItemKeyToDrsNameMap.put((String)items[0], ((Long)items[1]).intValue());
		}
		return serviceItemKeyToDrsNameMap;
	}

	//todo arthur system qty in stock
	@Override @SuppressWarnings("unchecked")
	public Map<String,Integer> queryQtyAtDrsSettlementEndPlusQtyReceivedInCurrentSettlement(Country country,String supplierKcode) {
		StringBuilder sqlSb = new StringBuilder()
				.append("select ")
				.append("ps.code_by_drs, ")
				.append("coalesce(sum(unsi.qty_order)-sum(unsi.qty_sold)+sum(unsi.qty_returned),0) ")
				.append("from shipment_line_item unsi ")
				.append("inner join shipment uns on uns.id = unsi.shipment_id ")
				.append("inner join shipment_uns_info shpui on shpui.shipment_id = uns.id ")
				.append("inner join product_sku ps on ps.id = unsi.sku_id ")
				.append("inner join product_sku_status pssts on pssts.id = ps.status_id ")
				.append("inner join product_base pb on pb.id = ps.product_base_id ")
				.append("inner join company splr on splr.id = pb.supplier_company_id ")
				.append("inner join company ms on ms.id = uns.buyer_company_id ")
				.append("where ms.country_id = :countryKey ")
				.append("and pssts.name = :activeStatusName ")
				.append("and shpui.arrival_date is not null ")
				.append("and shpui.arrival_date < now() ");
				if(supplierKcode!=null) sqlSb.append("and splr.k_code = :supplierKcode ");
				sqlSb.append("group by ps.code_by_drs ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("countryKey", country.getKey());
		if(supplierKcode!=null) q.addValue("supplierKcode", supplierKcode);
		q.addValue("activeStatusName",SKU.Status.SKU_ACTIVE.name() );
		Map<String,Integer> serviceItemKeyToDrsNameMap = new HashMap<>();
		List<Object[]> result = getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,objArrayMapper);
		for(Object[] items:result){
			serviceItemKeyToDrsNameMap.put((String)items[0], ((Long)items[1]).intValue());
		}
		return serviceItemKeyToDrsNameMap;
	}

	@Override @SuppressWarnings("unchecked")
	public List<Object []> querySkuFbaQuantities(Marketplace marketplace) {
		StringBuilder sqlSb = new StringBuilder()
				.append("select ")
				.append("ais.marketplace_sku, ")
				.append("ais.detail_quantity_inbound, ")
				.append("ais.detail_quantity_instock, ")
				.append("ais.detail_quantity_transfer ")
				.append("from amazon_inventory_supply ais ")
				.append("where ais.marketplace_id = :marketplace_id ")
				.append("and detail_quantity_inbound+detail_quantity_instock+detail_quantity_transfer!=0 order by ais.id ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("marketplace_id", marketplace.getKey());


		List<Object[]> result = getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,objArrayMapper);
		return result;
	}

}
