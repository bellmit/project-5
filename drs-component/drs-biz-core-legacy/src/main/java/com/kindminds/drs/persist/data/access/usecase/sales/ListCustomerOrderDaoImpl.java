package com.kindminds.drs.persist.data.access.usecase.sales;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;


import com.kindminds.drs.api.data.access.usecase.sales.ListCustomerOrderDao;
import com.kindminds.drs.persist.data.access.rdb.Dao;
import com.kindminds.drs.api.v1.model.sales.CustomerOrder;
import com.kindminds.drs.api.v1.model.sales.CustomerOrderExporting;
import com.kindminds.drs.api.v1.model.sales.ListCustomerOrderCondition;
import com.kindminds.drs.enums.SkuDisplayOption;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.v2.biz.domain.model.SalesChannel;


import com.kindminds.drs.api.v1.model.accounting.AmazonOrderDetail;
import com.kindminds.drs.persist.v1.model.mapping.amazon.AmazonOrderDetailImpl;
import com.kindminds.drs.persist.v1.model.mapping.sales.CustomerOrderExportingImpl;
import com.kindminds.drs.persist.v1.model.mapping.sales.CustomerOrderImpl;
import com.kindminds.drs.util.DateHelper;
import org.springframework.util.StringUtils;

@Repository
public class ListCustomerOrderDaoImpl extends Dao implements ListCustomerOrderDao {
	


		
	@Override @SuppressWarnings("unchecked")
	public Map<String,String> queryProductBaseCodeToNameMap(String supplierKcode) {
		StringBuilder sqlSb = new StringBuilder();
		sqlSb.append("select pb.code_by_drs, p.name_by_supplier ");
		sqlSb.append("from product_base pb ");
		sqlSb.append("inner join product p on p.id = pb.product_id ");
		sqlSb.append("inner join company splr on splr.id = pb.supplier_company_id ");
		sqlSb.append("where splr.k_code = :supplierKcode ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("supplierKcode",supplierKcode);
		Map<String,String> productBaseCodeToNameMap = new TreeMap<String,String>();
		List<Object[]> result = getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,objArrayMapper);
		for(Object[] items:result){
			productBaseCodeToNameMap.put((String)items[0], (String)items[1]);
		}
		return productBaseCodeToNameMap;
	}

	@Override @SuppressWarnings("unchecked")
	public Map<String,String> queryProductSkuCodeToNameMap(String productBaseCode) {
		StringBuilder sqlSb = new StringBuilder();
		sqlSb.append("select ps.code_by_drs, p.name_by_supplier ");
		sqlSb.append("from product_sku ps ");
		sqlSb.append("inner join product p on p.id = ps.product_id ");
		sqlSb.append("inner join product_base pb on pb.id = ps.product_base_id ");
		sqlSb.append("where pb.code_by_drs = :productBaseCode ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("productBaseCode",productBaseCode);
		Map<String,String> productSkuCodeToNameMap = new TreeMap<String,String>();
		List<Object[]> result = getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,objArrayMapper);
		for(Object[] items:result){
			productSkuCodeToNameMap.put((String)items[0], (String)items[1]);
		}
		return productSkuCodeToNameMap;
	}
	
	@Override
	public int queryListCount(int lastDays) {

		StringBuilder sqlSb = new StringBuilder();
		/*
		sqlSb.append("select count(*) from ( ");		
		sqlSb.append("SELECT * FROM( ");
			sqlSb.append("    select distinct CAST(aoi.id AS char(30)) as id,ao.purchase_date as order_time, p.name_by_drs ");
			sqlSb.append(this.generateBasicQuerySqlPartOfFromForAmazon());
			sqlSb.append(" UNION ");
			sqlSb.append("    select distinct CAST(aoi.id AS char(30)) as id,ao.purchase_date as order_time, p.name_by_drs ");
			sqlSb.append(this.generateBasicQuerySqlPartOfFromForAmazonK101());				
		sqlSb.append(" ) AS item_amazon ");
		sqlSb.append("where item_amazon.order_time > :assignedDate ");
		sqlSb.append(" UNION ");
		sqlSb.append("SELECT * FROM( ");
			sqlSb.append(" 	  select distinct so.order_id as id ,so.created_at as order_time, p.name_by_drs ");
			sqlSb.append(this.generateBasicQuerySqlPartOfFromForShopify());
			sqlSb.append(" UNION ");
			sqlSb.append(" 	  select distinct so.order_id as id ,so.created_at as order_time, p.name_by_drs ");
			sqlSb.append(this.generateBasicQuerySqlPartOfFromForShopifyK101());				
		sqlSb.append(" ) AS item_shopify ");
		sqlSb.append("where item_shopify.order_time > :assignedDate ");				
		sqlSb.append(") as temp ");
		*/


		sqlSb.append("SELECT count(*) ");
		sqlSb.append("	FROM sales.all_orders o" );
		sqlSb.append("	 where order_time > :assignedDate  " );


		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("assignedDate", this.getDaysBefore(lastDays));
		Integer o = getNamedParameterJdbcTemplate().queryForObject(sqlSb.toString(),q,Integer.class);
		return o;
	}
			
	@Override @SuppressWarnings("unchecked")
	public List<CustomerOrder> queryList(int lastDays,SkuDisplayOption option,
										 Integer startIndex, Integer size) {
		Assert.isTrue(!(startIndex==null^size==null)); // should be both null or both not null
		StringBuilder sqlSb = new StringBuilder();

		/*
		sqlSb.append("SELECT * FROM( ");
			sqlSb.append("SELECT * FROM( ");
				sqlSb.append(this.generateBasicQuerySqlPartOfSelectForAmazonExporting(option));
				sqlSb.append(this.generateBasicQuerySqlPartOfFromForAmazon());
				sqlSb.append(" UNION ");
				sqlSb.append(this.generateBasicQuerySqlPartOfSelectForAmazonExportingK101(option));
				sqlSb.append(this.generateBasicQuerySqlPartOfFromForAmazonK101());			
			sqlSb.append(" ) AS item_amazon ");
			sqlSb.append(" UNION ");
			sqlSb.append("SELECT * FROM( ");
				sqlSb.append(this.generateBasicQuerySqlPartOfSelectForShopifyExporting(option));
				sqlSb.append(this.generateBasicQuerySqlPartOfFromForShopify());
				sqlSb.append(" UNION ");
				sqlSb.append(this.generateBasicQuerySqlPartOfSelectForShopifyExportingK101(option));
				sqlSb.append(this.generateBasicQuerySqlPartOfFromForShopifyK101());						
			sqlSb.append(" ) AS item_shopify ");
		sqlSb.append(" ) AS item ");
		*/

		sqlSb.append("SELECT o.id, source_id, last_update_date, order_time, local_order_time, " +
				"transaction_time, marketplace_order_id, shopify_order_id, promotion_id, " +
				"order_status, asin, com_code, base_code, sku_code  as seller_sku , product_name, item_price," +
				" actual_retail_price, actual_shipping_price, actual_total_shipping_price, qty," +
				" buyer, buyer_email, sales_channel, fulfillment_center, " +
				"refund_dt_id, city, state, country  ");

		if(option== SkuDisplayOption.DRS) sqlSb.append(  " , sku_code_by_drs as sku_code  " );
		if(option==SkuDisplayOption.SUPPLIER) sqlSb.append( ", sku_code_by_supplier  as sku_code" );

		sqlSb.append("	FROM sales.all_orders o " );

		sqlSb.append("where order_time > :assignedDate ");
		sqlSb.append("order by order_time desc ");



		if(this.pagingRequired(startIndex, size)) sqlSb.append("limit :size offset :start ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("assignedDate", this.getDaysBefore(lastDays));
		if(this.pagingRequired(startIndex, size)){
			q.addValue("size", size);
			q.addValue("start", startIndex-1);
		}


		List<CustomerOrderImpl> resultList = getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,
				(rs,rowNum) -> new CustomerOrderImpl(
						rs.getInt("id"),
						rs.getTimestamp("order_time").getTime()
						,rs.getString("marketplace_order_id")
						,rs.getString("shopify_order_id"),
						rs.getString("order_status"),
						rs.getString("sku_code"),
						rs.getString("product_name"),
						rs.getBigDecimal("item_price"),
						rs.getBigDecimal("actual_retail_price"),rs.getString("qty"),
						rs.getString("buyer"),
						rs.getString("sales_channel"),
						rs.getString("fulfillment_center"),
						rs.getInt("refund_dt_id"),
						rs.getDate("transaction_time") == null ? null :
								rs.getDate("transaction_time").getTime(),
						rs.getString("promotion_Id")
				));


		//this.setTransactionDate(resultList);

		//this.setPromotionIds(resultList);

		return (List<CustomerOrder>)((List<?>)resultList);
	}
							
	@Override @SuppressWarnings("unchecked")
	public List<CustomerOrderExporting> queryExportingList(int lastDays, SkuDisplayOption option) {

		StringBuilder sqlSb = new StringBuilder();

		/*
		sqlSb.append("SELECT * FROM( ");
			sqlSb.append("SELECT * FROM( ");
				sqlSb.append(this.generateBasicQuerySqlPartOfSelectForAmazonExporting(option));
				sqlSb.append(this.generateBasicQuerySqlPartOfFromForAmazon());
				sqlSb.append(" UNION ");			
				sqlSb.append(this.generateBasicQuerySqlPartOfSelectForAmazonExportingK101(option));
				sqlSb.append(this.generateBasicQuerySqlPartOfFromForAmazonK101());			
			sqlSb.append(" ) AS item_amazon ");
			sqlSb.append(" UNION ");
			sqlSb.append("SELECT * FROM( ");
				sqlSb.append(this.generateBasicQuerySqlPartOfSelectForShopifyExporting(option));
				sqlSb.append(this.generateBasicQuerySqlPartOfFromForShopify());
				sqlSb.append(" UNION ");
				sqlSb.append(this.generateBasicQuerySqlPartOfSelectForShopifyExportingK101(option));
				sqlSb.append(this.generateBasicQuerySqlPartOfFromForShopifyK101());						
			sqlSb.append(" ) AS item_shopify ");
		sqlSb.append(" ) AS item ");
		*/


		sqlSb.append("SELECT o.id, source_id, last_update_date, order_time, local_order_time, " +
				"transaction_time, marketplace_order_id, shopify_order_id, promotion_id, " +
				"order_status, asin, com_code, base_code, sku_code  as seller_sku , product_name, item_price," +
				" actual_retail_price, actual_shipping_price, actual_total_shipping_price, qty," +
				" buyer, buyer_email, sales_channel, fulfillment_center, " +
				"refund_dt_id, city, state, country  ");

		if(option==SkuDisplayOption.DRS) sqlSb.append(  " , sku_code_by_drs as sku_code  " );
		if(option==SkuDisplayOption.SUPPLIER) sqlSb.append( ", sku_code_by_supplier  as sku_code" );

		sqlSb.append("	FROM sales.all_orders o " );

		sqlSb.append("where order_time > :assignedDate ");
		sqlSb.append("order by order_time desc ");
		sqlSb.append("limit 1000  ");

		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("assignedDate", this.getDaysBefore(lastDays));


		List<CustomerOrderExportingImpl> resultList = getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,
				(rs,rowNum) -> new CustomerOrderExportingImpl(
						rs.getInt("id"),rs.getTimestamp("order_time").getTime(),rs.getString("marketplace_order_id")
						,rs.getString("shopify_order_id"),
						rs.getString("order_status"),rs.getString("sku_code"),rs.getString("product_name"),
						rs.getBigDecimal("item_price"),rs.getBigDecimal("actual_retail_price"),rs.getString("qty"),
						rs.getString("buyer"),rs.getString("sales_channel"),rs.getString("fulfillment_center"),
						rs.getInt("refund_dt_id"),rs.getString("city"),rs.getString("state"),
						rs.getString("country"),rs.getString("transaction_time"),
						rs.getString("transaction_time"),
						rs.getString("promotion_Id")
				));


		//this.setTransactionDateForExporting(resultList);
		//this.setPromotionIdsForExporting(resultList);
		return (List<CustomerOrderExporting>)((List<?>)resultList);
	}
					
	@Override
	public int queryListCount(int lastDays, String userCompanyKcode) {

		StringBuilder sqlSb = new StringBuilder();
		/*
		sqlSb.append("select count(*) from ( ");		
		sqlSb.append("SELECT * FROM( ");
			sqlSb.append("    select distinct CAST(aoi.id AS char(30)) as id,ao.purchase_date as order_time, p.name_by_drs ");
			sqlSb.append(this.generateBasicQuerySqlPartOfFromForAmazon());
			sqlSb.append("where com.k_code = :supplierKcode ");
			sqlSb.append(" UNION ");
			sqlSb.append("    select distinct CAST(aoi.id AS char(30)) as id,ao.purchase_date as order_time, p.name_by_drs ");
			sqlSb.append(this.generateBasicQuerySqlPartOfFromForAmazonK101());				
			if(userCompanyKcode.equals("K101"))sqlSb.append(this.generateJoinSqlPartOfK101());
			if(!userCompanyKcode.equals("K101"))sqlSb.append("where com.k_code = :supplierKcode ");
		sqlSb.append(" ) AS item_amazon ");
		sqlSb.append("where item_amazon.order_time > :assignedDate ");
		sqlSb.append(" UNION ");
		sqlSb.append("SELECT * FROM( ");
			sqlSb.append(" 	  select distinct so.order_id as id, so.created_at as order_time, p.name_by_drs ");
			sqlSb.append(this.generateBasicQuerySqlPartOfFromForShopify());
			sqlSb.append("where com.k_code = :supplierKcode ");
			sqlSb.append(" UNION ");
			sqlSb.append(" 	  select distinct so.order_id as id ,so.created_at as order_time, p.name_by_drs ");
			sqlSb.append(this.generateBasicQuerySqlPartOfFromForShopifyK101());
			if(userCompanyKcode.equals("K101"))sqlSb.append(this.generateJoinSqlPartOfK101());
			if(!userCompanyKcode.equals("K101"))sqlSb.append("where com.k_code = :supplierKcode ");
		sqlSb.append(" ) AS item_shopify ");
		sqlSb.append("where item_shopify.order_time > :assignedDate ");				
		sqlSb.append(") as temp ");
		*/


		sqlSb.append("SELECT count(*) ");
		sqlSb.append("	FROM sales.all_orders o " );
		sqlSb.append("	 where com_code = :supplierKcode and order_time > :assignedDate  " );


		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("assignedDate", this.getDaysBefore(lastDays));
		q.addValue("supplierKcode", userCompanyKcode);


		Integer o = getNamedParameterJdbcTemplate().queryForObject(sqlSb.toString(),q,Integer.class);
		return o;
	}
	
	@Override @SuppressWarnings("unchecked")
	public List<CustomerOrder> queryList(int lastDays, String supplierKcode,
										 SkuDisplayOption option, Integer startIndex, Integer size) {

		Assert.isTrue(!(startIndex==null^size==null)); // should be both null or both not null


		StringBuilder sqlSb = new StringBuilder();

		/*
		sqlSb.append("SELECT * FROM( ");
		sqlSb.append("SELECT * FROM( ");
			sqlSb.append(this.generateBasicQuerySqlPartOfSelectForAmazon(option));
			sqlSb.append(this.generateBasicQuerySqlPartOfFromForAmazon());		
			sqlSb.append("where com.k_code = :supplierKcode ");
			sqlSb.append("UNION ");		
			sqlSb.append(this.generateBasicQuerySqlPartOfSelectForAmazonK101(option));
			sqlSb.append(this.generateBasicQuerySqlPartOfFromForAmazonK101());
			if(supplierKcode.equals("K101"))sqlSb.append(this.generateJoinSqlPartOfK101());		
			if(!supplierKcode.equals("K101"))sqlSb.append("where com.k_code = :supplierKcode ");
		sqlSb.append(") as item_amazon ");
		sqlSb.append("UNION ");
		sqlSb.append("SELECT * FROM( ");
			sqlSb.append(this.generateBasicQuerySqlPartOfSelectForShopify(option));
			sqlSb.append(this.generateBasicQuerySqlPartOfFromForShopify());	
			sqlSb.append("where com.k_code = :supplierKcode ");
			sqlSb.append("UNION ");
			sqlSb.append(this.generateBasicQuerySqlPartOfSelectForShopifyK101(option));
			sqlSb.append(this.generateBasicQuerySqlPartOfFromForShopifyK101());
			if(supplierKcode.equals("K101"))sqlSb.append(this.generateJoinSqlPartOfK101());			
			if(!supplierKcode.equals("K101"))sqlSb.append("where com.k_code = :supplierKcode ");
		sqlSb.append(") as item_shopify ");
		sqlSb.append(" ) AS item ");
		*/


		sqlSb.append("SELECT o.id, source_id, last_update_date, order_time, local_order_time, " +
				"transaction_time, marketplace_order_id, shopify_order_id, promotion_id, " +
				"order_status, asin, com_code, base_code, sku_code  as seller_sku , product_name, item_price," +
				" actual_retail_price, actual_shipping_price, actual_total_shipping_price, qty," +
				" buyer, buyer_email, sales_channel, fulfillment_center, " +
				"refund_dt_id, city, state, country  ");

		if(option==SkuDisplayOption.DRS) sqlSb.append(  " , sku_code_by_drs as sku_code  " );
		if(option==SkuDisplayOption.SUPPLIER) sqlSb.append( ", sku_code_by_supplier  as sku_code" );

		sqlSb.append("	FROM sales.all_orders o " );
		sqlSb.append("	where com_code = :supplierKcode and order_time > :assignedDate  " );
		sqlSb.append("order by order_time desc ");



		if(this.pagingRequired(startIndex, size)) sqlSb.append("limit :size offset :start ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("assignedDate", this.getDaysBefore(lastDays));
		q.addValue("supplierKcode", supplierKcode);		
		if(this.pagingRequired(startIndex, size)){
			q.addValue("size", size);
			q.addValue("start", startIndex-1);
		}


		List<CustomerOrderImpl> resultList = getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,
				(rs,rowNum) -> new CustomerOrderImpl(
						rs.getInt("id"),
						rs.getTimestamp("order_time").getTime(),rs.getString("marketplace_order_id")
						,rs.getString("shopify_order_id"),
						rs.getString("order_status"),rs.getString("sku_code"),rs.getString("product_name"),
						rs.getBigDecimal("item_price"),rs.getBigDecimal("actual_retail_price"),rs.getString("qty"),
						rs.getString("buyer"),rs.getString("sales_channel"),rs.getString("fulfillment_center"),
						rs.getInt("refund_dt_id"),
						rs.getDate("transaction_time") == null ? null : rs.getDate("transaction_time").getTime(),
						rs.getString("promotion_Id")
				));

		//this.setTransactionDate(resultList);
		//this.setPromotionIds(resultList);
		return (List<CustomerOrder>)((List<?>)resultList);
	}
			
	@Override @SuppressWarnings("unchecked")
	public List<CustomerOrderExporting> queryExportingList(int lastDays, String supplierKcode, SkuDisplayOption option) {


		StringBuilder sqlSb = new StringBuilder();

		/*
		sqlSb.append("SELECT * FROM( ");
		sqlSb.append("SELECT * FROM( ");
			sqlSb.append(this.generateBasicQuerySqlPartOfSelectForAmazonExporting(option));
			sqlSb.append(this.generateBasicQuerySqlPartOfFromForAmazon());			
			sqlSb.append("where com.k_code = :supplierKcode ");
			sqlSb.append("UNION ");		
			sqlSb.append(this.generateBasicQuerySqlPartOfSelectForAmazonExportingK101(option));
			sqlSb.append(this.generateBasicQuerySqlPartOfFromForAmazonK101());
			if(supplierKcode.equals("K101"))sqlSb.append(this.generateJoinSqlPartOfK101());			
			if(!supplierKcode.equals("K101"))sqlSb.append("where com.k_code = :supplierKcode ");
		sqlSb.append(") as item_amazon ");
		sqlSb.append("UNION ");
		sqlSb.append("SELECT * FROM( ");
			sqlSb.append(this.generateBasicQuerySqlPartOfSelectForShopifyExporting(option));
			sqlSb.append(this.generateBasicQuerySqlPartOfFromForShopify());			
			sqlSb.append("where com.k_code = :supplierKcode ");
			sqlSb.append("UNION ");			
			sqlSb.append(this.generateBasicQuerySqlPartOfSelectForShopifyExportingK101(option));
			sqlSb.append(this.generateBasicQuerySqlPartOfFromForShopifyK101());
			if(supplierKcode.equals("K101"))sqlSb.append(this.generateJoinSqlPartOfK101());				
			if(!supplierKcode.equals("K101"))sqlSb.append("where com.k_code = :supplierKcode ");
		sqlSb.append(") as item_shopify ");	
		sqlSb.append(" ) AS item ");
		*/


		sqlSb.append("SELECT o.id, source_id, last_update_date, order_time, local_order_time, " +
				"transaction_time, marketplace_order_id, shopify_order_id, promotion_id, " +
				"order_status, asin, com_code, base_code, sku_code  as seller_sku , product_name, item_price," +
				" actual_retail_price, actual_shipping_price, actual_total_shipping_price, qty," +
				" buyer, buyer_email, sales_channel, fulfillment_center, " +
				"refund_dt_id, city, state, country  ");

		if(option==SkuDisplayOption.DRS) sqlSb.append(  " , sku_code_by_drs as sku_code  " );
		if(option==SkuDisplayOption.SUPPLIER) sqlSb.append( ", sku_code_by_supplier  as sku_code" );

		sqlSb.append("	FROM sales.all_orders o " );

		sqlSb.append("	where com_code = :supplierKcode and order_time > :assignedDate  " );
		sqlSb.append("order by order_time desc ");
		sqlSb.append("limit 1000  ");

		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("assignedDate", this.getDaysBefore(lastDays));
		q.addValue("supplierKcode", supplierKcode);

		List<CustomerOrderExportingImpl> resultList = getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,
				(rs,rowNum) -> new CustomerOrderExportingImpl(
						rs.getInt("id"),rs.getTimestamp("order_time").getTime(),rs.getString("marketplace_order_id")
						,rs.getString("shopify_order_id"),
						rs.getString("order_status"),rs.getString("sku_code"),rs.getString("product_name"),
						rs.getBigDecimal("item_price"),rs.getBigDecimal("actual_retail_price"),rs.getString("qty"),
						rs.getString("buyer"),rs.getString("sales_channel"),rs.getString("fulfillment_center"),
						rs.getInt("refund_dt_id"),rs.getString("city"),rs.getString("state"),
						rs.getString("country"),rs.getString("transaction_time"),
						rs.getString("transaction_time"),
						rs.getString("promotion_Id")
				));

		//this.setTransactionDateForExporting(resultList);
		//this.setPromotionIdsForExporting(resultList);
		return (List<CustomerOrderExporting>)((List<?>)resultList);
	}
		
	@Override @SuppressWarnings("unchecked")
	public AmazonOrderDetail queryAmazonOrderDetail(Marketplace marketplace,String orderId,String sku) {
		String sql = "select     	 aoi.order_item_id as id, "
				+ "              ao.ship_service_level as ship_service_level, "
				+ "			    ao.address_countrycode as address_country, "
				+ "           ao.address_stateorregion as address_district, "
				+ "                    ao.address_city as address_city, "
				+ "				 ao.address_postalcode as address_postalcode, "
				+ "				   aoi.item_tax_amount as item_tax_amount, "
				+ "		     aoi.shipping_price_amount as shipping_price_amount, "
				+ " 		   aoi.shipping_tax_amount as shipping_tax_amount, "
				+ "         aoi.gift_wrap_price_amount as gift_wrap_price_amount, "
				+ "			   aoi.giftwrap_tax_amount as gift_wrap_tax_amount, "
				+ "		 aoi.promotion_discount_amount as promotion_discount_amount, "
				+ "       aoi.shipping_discount_amount as shipping_discount_amount, "
				+ "                         arr.reason as refund_reason, "
				+ "           arr.detailed_disposition as refund_detail_disposition "
				+ " from amazon_order_item aoi "
				+ " inner join amazon_order ao on aoi.amazon_order_id = ao.amazon_order_id "	
				+ " left join amazon_return_report arr on aoi.amazon_order_id = arr.order_id"
				+ " where aoi.amazon_order_id = :orderId "
				+ " and ao.sales_channel = :marketplace "
				+ " and aoi.seller_sku ilike :sku ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("marketplace", marketplace.getName());		
		q.addValue("orderId", orderId);
		q.addValue("sku", this.attachDbWildcardToBeginAndEnd(sku));


		List<AmazonOrderDetailImpl> list = getNamedParameterJdbcTemplate().query(sql,q,
				(rs,rowNum) -> new AmazonOrderDetailImpl(
						rs.getString("id"),rs.getString("ship_service_level"),rs.getString("address_country"),
						rs.getString("address_district"),rs.getString("address_city"),rs.getString("address_postalcode"),
						rs.getBigDecimal("item_tax_amount"),rs.getBigDecimal("shipping_price_amount"),
						rs.getBigDecimal("shipping_tax_amount"),
						rs.getBigDecimal("gift_wrap_price_amount"),rs.getBigDecimal("gift_wrap_tax_amount"),
						rs.getBigDecimal("promotion_discount_amount"),
						rs.getBigDecimal("shipping_discount_amount"),rs.getString("refund_reason"),
						rs.getString("refund_detail_disposition")
				));

		if ((list == null) || (list.size() == 0)) return null;
		return list.get(0);		
	}
	
	@Override @SuppressWarnings("unchecked")
	public Map<String, String> queryShopifyOrderDetail(String orderId) {		
		String sql = "select     				sosi.code as shipping_method, "
				+ "			soi.json_destination_location as address, "
				+ "					   soi.json_tax_lines as tax, "
				+ "			           soi.total_discount as total_discount "
				+ " from shopify_order_item soi "
				+ " inner join shopify_order_shipping_line sosi on soi.shopify_order_id = sosi.shopify_order_id "
				+ " where soi.shopify_order_id = :orderId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("orderId", orderId);
		List<Object[]> objectArrayList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		Map<String,String> resultMap = new TreeMap<String,String>();
		for(Object[] columns:objectArrayList){
			resultMap.put("shippingMethod", (String)columns[0]);
			resultMap.put("jsonDestinationLocation", (String)columns[1]);			
			resultMap.put("jsonTaxLines", (String)columns[2]);			
			resultMap.put("totalDiscount", (String)columns[3]);						
		}
		return resultMap;
	}
	
	@Override
	public String queryShopifyFieldData(String field, String orderId, String sku) {
		String sql = "select "+field + " "
				+ " from shopify_order_line_item soi "
				+ " inner join shopify_order_shipping_line sosi on soi.shopify_order_id = sosi.shopify_order_id "
				+ " inner join shopify_order so on soi.shopify_order_id = so.id "
				+ " where so.name = :orderId "
				+ " and soi.sku ilike :sku ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("orderId", orderId);
		q.addValue("sku", this.attachDbWildcardToBeginAndEnd(sku));		
		return getNamedParameterJdbcTemplate().queryForObject(sql,q,String.class);
	}
	
	@Override
	public String queryShopifyJsonData(ShopifyJsonDetailType type, String orderId, String sku) {
		String sql = "select soi."+type.getDbTableColumnName()+" "
				+ " from shopify_order_line_item soi "
				+ " inner join shopify_order so on soi.shopify_order_id = so.id "
				+ " where so.name = :orderId "
				+ " and soi.sku ilike :sku ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("orderId", orderId);		
		q.addValue("sku", this.attachDbWildcardToBeginAndEnd(sku));
		return getNamedParameterJdbcTemplate().queryForObject(sql,q,String.class);
	}
			
	private Date getDaysBefore(int days){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE,-days);
		return cal.getTime();
	}
		
	@Override
	public int queryListCount(ListCustomerOrderCondition condition) {
		StringBuilder sqlSb = new StringBuilder();

		/*
		sqlSb.append("select count(*) from ( ");
		sqlSb.append("SELECT * FROM( ");
			sqlSb.append("    select distinct CAST(aoi.id AS char(30)) as id,ao.purchase_date as order_time, p.name_by_drs ");
			sqlSb.append(this.generateBasicQuerySqlPartOfFromForAmazon());
			sqlSb.append("    where TRUE ");
			sqlSb.append(this.composeSqlConditionStringForAmazon(condition));			
			sqlSb.append(" UNION ");
			sqlSb.append("    select distinct CAST(aoi.id AS char(30)) as id,ao.purchase_date as order_time, p.name_by_drs ");
			sqlSb.append(this.generateBasicQuerySqlPartOfFromForAmazonK101());
			if(condition.getSupplierKcode()!= null && condition.getSupplierKcode().equals("K101"))sqlSb.append(this.generateJoinSqlPartOfK101());
			sqlSb.append("    where TRUE ");
			sqlSb.append(this.composeSqlConditionStringForAmazonK101(condition));			
		sqlSb.append(" ) AS item_amazon ");
		sqlSb.append(" UNION ");
		sqlSb.append("SELECT * FROM( ");
			sqlSb.append(" 	  select distinct so.order_id as id ,so.created_at as order_time, p.name_by_drs ");
			sqlSb.append(this.generateBasicQuerySqlPartOfFromForShopify());
			sqlSb.append("    where TRUE ");
			sqlSb.append(this.composeSqlConditionStringForShopify(condition));
			sqlSb.append(" UNION ");
			sqlSb.append(" 	  select distinct so.order_id as id ,so.created_at as order_time, p.name_by_drs ");
			sqlSb.append(this.generateBasicQuerySqlPartOfFromForShopifyK101());
			if(condition.getSupplierKcode()!= null && condition.getSupplierKcode().equals("K101"))sqlSb.append(this.generateJoinSqlPartOfK101());
			sqlSb.append("    where TRUE ");
			sqlSb.append(this.composeSqlConditionStringForShopifyK101(condition));
		sqlSb.append(" ) AS item_shopify ");		
		sqlSb.append(" ) AS temp ");
		*/


		sqlSb.append("SELECT count(*) ");
		sqlSb.append("	FROM sales.all_orders o " );
		sqlSb.append("	 where TRUE  " );

		sqlSb.append(this.composeSqlConditionString(condition));

		MapSqlParameterSource q = new MapSqlParameterSource();
		this.setQueryParameters(q,condition);

		Integer o = getNamedParameterJdbcTemplate().queryForObject(sqlSb.toString(),q,Integer.class);
		return o;
	}	;
				
	@Override @SuppressWarnings("unchecked")
	public List<CustomerOrder> queryList(ListCustomerOrderCondition condition, SkuDisplayOption option,
										 Integer startIndex, Integer size) {

		Assert.isTrue(!(startIndex==null^size==null)); // should be both null or both not null
		StringBuilder sqlSb = new StringBuilder();
		/*
		sqlSb.append("SELECT * FROM( ");
				sqlSb.append("SELECT * FROM( ");
				sqlSb.append(this.generateBasicQuerySqlPartOfSelectForAmazon(option));
				sqlSb.append(this.generateBasicQuerySqlPartOfFromForAmazon());	
				sqlSb.append("where TRUE ");		
				sqlSb.append(this.composeSqlConditionStringForAmazon(condition));
				sqlSb.append("UNION ");			
				sqlSb.append(this.generateBasicQuerySqlPartOfSelectForAmazonK101(option));
				sqlSb.append(this.generateBasicQuerySqlPartOfFromForAmazonK101());
				if(condition.getSupplierKcode()!= null && condition.getSupplierKcode().equals("K101"))sqlSb.append(this.generateJoinSqlPartOfK101());
				sqlSb.append("where TRUE ");		
				sqlSb.append(this.composeSqlConditionStringForAmazonK101(condition));
			sqlSb.append(") AS item_amazon ");
			sqlSb.append("UNION ");
			sqlSb.append("SELECT * FROM( ");
				sqlSb.append(this.generateBasicQuerySqlPartOfSelectForShopify(option));
				sqlSb.append(this.generateBasicQuerySqlPartOfFromForShopify());
				sqlSb.append("where TRUE ");		
				sqlSb.append(this.composeSqlConditionStringForShopify(condition));
				sqlSb.append("UNION ");	
				sqlSb.append(this.generateBasicQuerySqlPartOfSelectForShopifyK101(option));
				sqlSb.append(this.generateBasicQuerySqlPartOfFromForShopifyK101());
				if(condition.getSupplierKcode()!= null && condition.getSupplierKcode().equals("K101"))sqlSb.append(this.generateJoinSqlPartOfK101());
				sqlSb.append("where TRUE ");		
			sqlSb.append(this.composeSqlConditionStringForShopifyK101(condition));
			sqlSb.append(") AS item_shopify ");		
		sqlSb.append(" ) AS item ");
		*/


		sqlSb.append("SELECT o.id, source_id, last_update_date, order_time, local_order_time, " +
				"transaction_time, marketplace_order_id, shopify_order_id, promotion_id, " +
				"order_status, asin, com_code, base_code, sku_code as seller_sku  , product_name, item_price," +
				" actual_retail_price, actual_shipping_price, actual_total_shipping_price, qty," +
				" buyer, buyer_email, sales_channel, fulfillment_center, " +
				"refund_dt_id, city, state, country  ");

		if(option==SkuDisplayOption.DRS) sqlSb.append(  " , sku_code_by_drs as sku_code  " );
		if(option==SkuDisplayOption.SUPPLIER) sqlSb.append( ", sku_code_by_supplier  as sku_code" );

		sqlSb.append("	FROM sales.all_orders o " );
		sqlSb.append("	 where TRUE  " );


		sqlSb.append(this.composeSqlConditionString(condition));
		sqlSb.append("order by order_time desc ");

		if(this.pagingRequired(startIndex, size)) sqlSb.append("limit :size offset :start ");
	MapSqlParameterSource q = new MapSqlParameterSource();
		this.setQueryParameters(q, condition);
		if(this.pagingRequired(startIndex, size)){
			q.addValue("size", size);
			q.addValue("start", startIndex-1);
		}
		List<CustomerOrderImpl> resultList = getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,
				(rs,rowNum) -> new CustomerOrderImpl(
						rs.getInt("id"),
						rs.getTimestamp("order_time").getTime(),rs.getString("marketplace_order_id")
						,rs.getString("shopify_order_id"),
						rs.getString("order_status"),rs.getString("sku_code"),rs.getString("product_name"),
						rs.getBigDecimal("item_price"),rs.getBigDecimal("actual_retail_price"),rs.getString("qty"),
						rs.getString("buyer"),rs.getString("sales_channel"),rs.getString("fulfillment_center"),
						rs.getInt("refund_dt_id"),
						rs.getDate("transaction_time") == null ? null : rs.getDate("transaction_time").getTime(),
						rs.getString("promotion_Id")
				));

		//this.setTransactionDate(resultList);
		//this.setPromotionIds(resultList);
		return (List<CustomerOrder>)((List<?>)resultList);		
	}
	
	@Override @SuppressWarnings("unchecked")
	public List<CustomerOrderExporting> queryExportingList(ListCustomerOrderCondition condition, SkuDisplayOption option) {
		StringBuilder sqlSb = new StringBuilder();


		/*
		sqlSb.append("SELECT * FROM( ");
			sqlSb.append("SELECT * FROM( ");
				sqlSb.append(this.generateBasicQuerySqlPartOfSelectForAmazonExporting(option));
				sqlSb.append(this.generateBasicQuerySqlPartOfFromForAmazon());	
				sqlSb.append("where TRUE ");		
				sqlSb.append(this.composeSqlConditionStringForAmazon(condition));
				sqlSb.append("UNION ");
				sqlSb.append(this.generateBasicQuerySqlPartOfSelectForAmazonExportingK101(option));
				sqlSb.append(this.generateBasicQuerySqlPartOfFromForAmazonK101());
				if(condition.getSupplierKcode()!= null && condition.getSupplierKcode().equals("K101"))sqlSb.append(this.generateJoinSqlPartOfK101());
				sqlSb.append("where TRUE ");		
				sqlSb.append(this.composeSqlConditionStringForAmazonK101(condition));
			sqlSb.append(") AS item_amazon ");
			sqlSb.append("UNION ");
			sqlSb.append("SELECT * FROM( ");
				sqlSb.append(this.generateBasicQuerySqlPartOfSelectForShopifyExporting(option));
				sqlSb.append(this.generateBasicQuerySqlPartOfFromForShopify());
				sqlSb.append("where TRUE ");		
				sqlSb.append(this.composeSqlConditionStringForShopify(condition));
				sqlSb.append("UNION ");
				sqlSb.append(this.generateBasicQuerySqlPartOfSelectForShopifyExportingK101(option));
				sqlSb.append(this.generateBasicQuerySqlPartOfFromForShopifyK101());
				if(condition.getSupplierKcode()!= null && condition.getSupplierKcode().equals("K101"))sqlSb.append(this.generateJoinSqlPartOfK101());
				sqlSb.append("where TRUE ");		
				sqlSb.append(this.composeSqlConditionStringForShopifyK101(condition));
			sqlSb.append(") AS item_shopify ");
		sqlSb.append(" ) AS item ");
		*/




		sqlSb.append("SELECT o.id, source_id, last_update_date, order_time, local_order_time, " +
						"transaction_time, marketplace_order_id, shopify_order_id, promotion_id, " +
						"order_status, asin, com_code, base_code, sku_code  as seller_sku , product_name, item_price," +
						" actual_retail_price, actual_shipping_price, actual_total_shipping_price, qty," +
						" buyer, buyer_email, sales_channel, fulfillment_center, " +
						"refund_dt_id, city, state, country  ");

		if(option==SkuDisplayOption.DRS) sqlSb.append(  " , sku_code_by_drs as sku_code  " );
		if(option==SkuDisplayOption.SUPPLIER) sqlSb.append( ", sku_code_by_supplier  as sku_code" );

		sqlSb.append("	FROM sales.all_orders o " );
		sqlSb.append("	 where TRUE  " );

		sqlSb.append(this.composeSqlConditionString(condition));
		sqlSb.append("order by order_time desc ");
		sqlSb.append("limit 1000  ");

		MapSqlParameterSource q = new MapSqlParameterSource();
		this.setQueryParameters(q, condition);

		List<CustomerOrderExportingImpl> resultList = getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,
				(rs,rowNum) -> new CustomerOrderExportingImpl(
						rs.getInt("id"),rs.getTimestamp("order_time").getTime(),rs.getString("marketplace_order_id")
						,rs.getString("shopify_order_id"),
						rs.getString("order_status"),rs.getString("sku_code"),rs.getString("product_name"),
						rs.getBigDecimal("item_price"),rs.getBigDecimal("actual_retail_price"),rs.getString("qty"),
						rs.getString("buyer"),rs.getString("sales_channel"),rs.getString("fulfillment_center"),
						rs.getInt("refund_dt_id"),rs.getString("city"),rs.getString("state"),
						rs.getString("country"),rs.getString("transaction_time"),
						rs.getString("transaction_time"),
						rs.getString("promotion_Id")
				));

		//this.setTransactionDateForExporting(resultList);
		//this.setPromotionIdsForExporting(resultList);
		return (List<CustomerOrderExporting>)((List<?>)resultList);
	}
			
	private boolean pagingRequired(Integer startIndex, Integer size){
		return startIndex!=null&&size!=null;
	}

	/*
	private void setTransactionDate(List<CustomerOrderImpl> orderList){
		// TODO: REFACTOR, Move assembling here to UCO.
		if(orderList.isEmpty()) return;
		List<String> marketplaceOrderIdList = this.getMarketplaceOrderIdList(orderList);
		String sql = "select distinct on (dt.source_transaction_id) dt.source_transaction_id, to_char( dt.transaction_date at time zone 'UTC','YYYY-MM-DD HH24:MI:SS' ) "
				+ "from drs_transaction dt "
				+ "where dt.source_transaction_id in :marketplaceOrderIdList and dt.type = '"+AmazonTransactionType.ORDER.getValue()+"' ";
		Query q = this.entityManager.createNativeQuery(sql);
		q.addValue("marketplaceOrderIdList", marketplaceOrderIdList);
		List<Object[]> objectArrayList = q.getResultList();
		Map<String,String> orderIdToTransactionDateMap = new HashMap<String,String>();
		for(Object[] columns:objectArrayList)
			orderIdToTransactionDateMap.put((String)columns[0], (String)columns[1]);
		for(CustomerOrderImpl order:orderList){
			if(orderIdToTransactionDateMap.containsKey(order.getMarketplaceOrderId()))
				order.setTransactionDate(orderIdToTransactionDateMap.get(order.getMarketplaceOrderId()));
		}
	}*/

	/*
	private void setTransactionDateForExporting(List<CustomerOrderExportingImpl> orderList){
		// TODO: REFACTOR, Move assembling here to UCO.
		if(orderList.isEmpty()) return;
		List<String> marketplaceOrderIdList = this.getMarketplaceOrderIdListForExporting(orderList);
		String sql = "select distinct on (dt.source_transaction_id) dt.source_transaction_id, to_char( dt.transaction_date at time zone 'UTC','YYYY-MM-DD HH24:MI:SS' ) "
				+ "from drs_transaction dt "
				+ "where dt.source_transaction_id in :marketplaceOrderIdList and dt.type = '"+AmazonTransactionType.ORDER.getValue()+"' ";
		Query q = this.entityManager.createNativeQuery(sql);
		q.addValue("marketplaceOrderIdList", marketplaceOrderIdList);
		List<Object[]> objectArrayList = q.getResultList();
		Map<String,String> orderIdToTransactionDateMap = new HashMap<String,String>();
		for(Object[] columns:objectArrayList)
			orderIdToTransactionDateMap.put((String)columns[0], (String)columns[1]);
		for(CustomerOrderExportingImpl order:orderList){
			if(orderIdToTransactionDateMap.containsKey(order.getMarketplaceOrderId()))
				order.setTransactionDate(orderIdToTransactionDateMap.get(order.getMarketplaceOrderId()));
		}		
	}
	*/
			
	private List<String> getMarketplaceOrderIdList(List<CustomerOrderImpl> orderList){
		List<String> marketplaceOrderIdList = new ArrayList<String>();
		for(CustomerOrder order:orderList)
			marketplaceOrderIdList.add(order.getMarketplaceOrderId());
		return marketplaceOrderIdList;
	}
	
	private List<String> getMarketplaceOrderIdListForExporting(List<CustomerOrderExportingImpl> orderList){
		List<String> marketplaceOrderIdList = new ArrayList<String>();
		for(CustomerOrderExporting order:orderList)
			marketplaceOrderIdList.add(order.getMarketplaceOrderId());
		return marketplaceOrderIdList;		
	}
	
	@SuppressWarnings("unchecked")
	/*
	private void setPromotionIds(List<CustomerOrderImpl> orderList){
		// TODO: REFACTOR, Move assembling here to UCO.
		if(orderList.isEmpty()) return;
		List<Integer> orderItemDbIdList = this.getOrderItemDbIdList(orderList);
		String sql = "select aoi.id, aoipi.promotion_id from amazon_order_item aoi inner join amazon_order_item_promotion_id aoipi on aoipi.order_item_db_id = aoi.id "
				+ "where aoi.id in :orderItemDbIdList order by aoipi.promotion_id ";
		Query q = this.entityManager.createNativeQuery(sql);
		q.addValue("orderItemDbIdList", orderItemDbIdList);
		List<Object[]> objectArrayList = q.getResultList();
		Map<Integer,String> orderItemDbIdToPromotionIdMap = new HashMap<Integer,String>();
		for(Object[] columns:objectArrayList){
			Integer key = ((BigInteger)columns[0]).intValue();
			String nextPromotionId = (String)columns[1];
			if(orderItemDbIdToPromotionIdMap.containsKey(key)){
				String originalValue = orderItemDbIdToPromotionIdMap.get(key);
				orderItemDbIdToPromotionIdMap.put(key, originalValue+", "+nextPromotionId);
			}
			else{
				orderItemDbIdToPromotionIdMap.put(key, nextPromotionId);
			}
		}
		for(CustomerOrderImpl order:orderList){
			if(orderItemDbIdToPromotionIdMap.containsKey(order.getId()) && !order.getSalesChannel().equals(SalesChannel.TRUETOSOURCE.getDisplayName())){
				order.setPromotionIds(orderItemDbIdToPromotionIdMap.get(order.getId()));
			}
		}
	}
	*/


	/*
	private void setPromotionIdsForExporting(List<CustomerOrderExportingImpl> orderList){
		// TODO: REFACTOR, Move assembling here to UCO.
		if(orderList.isEmpty()) return;
		List<Integer> orderItemDbIdList = this.getOrderItemDbIdListForExporting(orderList);
		String sql = "select aoi.id, aoipi.promotion_id from amazon_order_item aoi inner join amazon_order_item_promotion_id aoipi on aoipi.order_item_db_id = aoi.id "
				+ "where aoi.id in :orderItemDbIdList order by aoipi.promotion_id ";
		Query q = this.entityManager.createNativeQuery(sql);
		q.addValue("orderItemDbIdList", orderItemDbIdList);
		List<Object[]> objectArrayList = q.getResultList();
		Map<Integer,String> orderItemDbIdToPromotionIdMap = new HashMap<Integer,String>();
		for(Object[] columns:objectArrayList){
				Integer key = ((BigInteger)columns[0]).intValue();
				String nextPromotionId = (String)columns[1];
				if(orderItemDbIdToPromotionIdMap.containsKey(key)){
					String originalValue = orderItemDbIdToPromotionIdMap.get(key);
					orderItemDbIdToPromotionIdMap.put(key, originalValue+", "+nextPromotionId);
				}
				else{
					orderItemDbIdToPromotionIdMap.put(key, nextPromotionId);
				}
		}
		for(CustomerOrderExportingImpl order:orderList){
			if(orderItemDbIdToPromotionIdMap.containsKey(order.getId()) && !order.getSalesChannel().equals(SalesChannel.TRUETOSOURCE.getDisplayName())){
				order.setPromotionIds(orderItemDbIdToPromotionIdMap.get(order.getId()));
			}
		}
	}
	*/
		
	private List<Integer> getOrderItemDbIdList(List<CustomerOrderImpl> orderList){
		List<Integer> orderItemDbIdList = new ArrayList<Integer>();
		for(CustomerOrderImpl order:orderList)			
			orderItemDbIdList.add(order.getId());								
		return orderItemDbIdList;
	}
	
	private List<Integer> getOrderItemDbIdListForExporting(List<CustomerOrderExportingImpl> orderList){
		List<Integer> orderItemDbIdList = new ArrayList<Integer>();
		for(CustomerOrderExportingImpl order:orderList)			
			orderItemDbIdList.add(order.getId());								
		return orderItemDbIdList;				
	}
		
	private void setQueryParameters(MapSqlParameterSource q, ListCustomerOrderCondition condition){

		if(condition.getSupplierKcode()!=null){
			q.addValue("supplierKcode",condition.getSupplierKcode());
		}
		if(condition.getOrderDateStart()!=null){
			String timeZoneText = this.getSalesChannelTimeZoneText(condition.getSalesChannelId());
			q.addValue("orderDateStart",this.dateStrToDate(condition.getOrderDateStart(),timeZoneText));
		}
		if(condition.getOrderDateEnd()!=null){
			String timeZoneText = this.getSalesChannelTimeZoneText(condition.getSalesChannelId());
			q.addValue("orderDateEnd", this.addOneDays(this.dateStrToDate(condition.getOrderDateEnd(),timeZoneText)));
		}
		if(condition.getTransactionDateStart()!=null){
			q.addValue("transactionDateStart",this.dateStrToUtcDate(condition.getTransactionDateStart()));
		}
		if(condition.getTransactionDateEnd()!=null){
			q.addValue("transactionDateEnd", this.addOneDays(this.dateStrToUtcDate(condition.getTransactionDateEnd())));
		}
		if(condition.getRelatedBaseProductCode()!=null && condition.getRelatedSkuCode()==null ){
			q.addValue("productBaseCode",condition.getRelatedBaseProductCode());
		}

		if(condition.getRelatedSkuCode()!=null){ 
			q.addValue("productSkuCode",condition.getRelatedSkuCode());
		}
		if(condition.getAmazonMerchantSKU()!=null){ 
			q.addValue("amazonMerchantSku",condition.getAmazonMerchantSKU());
		}


		if(condition.getSalesChannelId()!=null){
			SalesChannel salesChannel = SalesChannel.fromKey(Integer.parseInt(condition.getSalesChannelId()));
			if(salesChannel.getKey() != 99){
				q.addValue("salesChannel",salesChannel.getDisplayName());
			}

		}


		if (StringUtils.hasText(condition.getOrderStatus())) {
			if(!condition.getOrderStatus().equalsIgnoreCase("Canceled")){
				q.addValue("orderStatus", condition.getOrderStatus());
			}
		}

		if(condition.getMarketplaceOrderId()!=null){ 
			q.addValue("marketplaceOrderId",condition.getMarketplaceOrderId());
		}
		if(condition.getCustomerName()!=null){ 
			q.addValue("customerName", this.attachDbWildcardToBeginAndEnd(condition.getCustomerName()));
		}
		if(condition.getBuyerEmail()!=null){
			q.addValue("buyerEmail",condition.getBuyerEmail());
		}
		if(condition.getPromotionId()!=null){
			q.addValue("promotionId", this.attachDbWildcardToBeginAndEnd(condition.getPromotionId()));
		}
		if(condition.getAsin()!=null){
			q.addValue("asin",condition.getAsin());
		}
		
	}
	
	private String getSalesChannelTimeZoneText(String salesChannelId){
		if(salesChannelId==null) return "UTC";
		return SalesChannel.fromKey(Integer.parseInt(salesChannelId)).getTimeZoneAssigned();
	}
	
	private Date dateStrToDate(String dateText,String timeZoneText){
		String timeZoneShortDisplayName = this.getTimeZoneShortDisplayName(dateText, TimeZone.getTimeZone(timeZoneText)); 
		return DateHelper.toDate(dateText+" "+timeZoneShortDisplayName,"yyyy-MM-dd z");
	}
	
	private String getTimeZoneShortDisplayName(String dateText,TimeZone timeZone){
		if(timeZone.inDaylightTime(DateHelper.toDate(dateText+" "+timeZone.getDisplayName(false, TimeZone.SHORT), "yyyy-MM-dd z"))){
			return timeZone.getDisplayName(true, TimeZone.SHORT);
		}else{
			return timeZone.getDisplayName(false, TimeZone.SHORT);
		}
	}
	
	private Date dateStrToUtcDate(String dateStr){
		return DateHelper.toDate(dateStr+" UTC","yyyy-MM-dd z");
	}

	private String composeSqlConditionString(ListCustomerOrderCondition condition){
		StringBuilder sqlSb = new StringBuilder();

		if(condition.getSupplierKcode()!=null){
			sqlSb.append("and com_code = :supplierKcode ");
		}
		if(condition.getOrderDateStart()!=null){
			sqlSb.append("and order_time >= :orderDateStart ");
		}
		if(condition.getOrderDateEnd()!=null){
			sqlSb.append("and order_time < :orderDateEnd ");
		}
		if(condition.getTransactionDateStart()!=null){
			sqlSb.append("and transaction_time >= :transactionDateStart ");
		}
		if(condition.getTransactionDateEnd()!=null){
			sqlSb.append("and transaction_time < :transactionDateEnd ");
		}
		if(condition.getRelatedBaseProductCode()!=null && condition.getRelatedSkuCode()==null ){
			sqlSb.append("and base_code = :productBaseCode ");
		}


		if(condition.getRelatedSkuCode()!=null){
			sqlSb.append("and sku_code_by_drs = :productSkuCode ");
		}

		if(condition.getAmazonMerchantSKU()!=null){
			sqlSb.append("and sku_code = :amazonMerchantSku ");
		}

		if(condition.getSalesChannelId()!=null){
			if(condition.getSalesChannelId().equals("99")){
				sqlSb.append("and sales_channel <> 'Non-order' and  sales_channel <> 'Webstore' ");
			}else{
				sqlSb.append("and sales_channel = :salesChannel ");
			}

		}

		if (StringUtils.hasText(condition.getOrderStatus())) {
			if (condition.getOrderStatus().equalsIgnoreCase("Canceled")) {
				sqlSb.append( "and (order_status = 'Canceled' OR order_status = 'CANCELLED') ");
			} else if (condition.getOrderStatus().equalsIgnoreCase("Refunded")){
				sqlSb.append( "and (order_status = :orderStatus " +
						" OR (refund_dt_id is not null and refund_dt_id > 0 ) ) ");
			} else if (condition.getOrderStatus().equals("Unpaid")) {
				sqlSb.append(" and (order_status = 'Pending' OR (order_status != 'Canceled' AND transaction_time is null))");
			} else {
				sqlSb.append( "and order_status = :orderStatus " );
			}
		}

		if(condition.getMarketplaceOrderId()!=null){
			sqlSb.append("and marketplace_order_id = :marketplaceOrderId ");
		}

		if(condition.getCustomerName()!=null){
			sqlSb.append("and buyer like :customerName ");
			//todo
			//or ao.address_name ilike :customerName
		}


		if(condition.getBuyerEmail()!=null){
			sqlSb.append("and buyer_email = :buyerEmail ");
		}
		if(condition.getPromotionId()!=null){
			sqlSb.append("and promotion_id like :promotionId ");
		}

		if(condition.getAsin()!=null){
			sqlSb.append("and asin = :asin ");
		}


		return sqlSb.toString();
	}

			
	private String attachDbWildcardToBeginAndEnd(String str){
		return "%"+str+"%";
	}
	
	private String attachDbWildcardToEnd(String str){
		return str+"%";
	}
		
	private Date addOneDays(Date d){
	    Calendar c = Calendar.getInstance();
	    c.setTime(d);
	    c.add(Calendar.DATE, 1);
	    d.setTime( c.getTime().getTime() );
	    return d;
	}


	/*
	private String generateBasicQuerySqlPartOfSelectForAmazon(SkuDisplayOption option){
		String sqlForSkuColumnSource = null;
		if(option==SkuDisplayOption.DRS) sqlForSkuColumnSource = " ps.code_by_drs ";
		if(option==SkuDisplayOption.SUPPLIER) sqlForSkuColumnSource = " p.code_by_supplier ";
		Assert.notNull(sqlForSkuColumnSource);
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct on (aoi.id,ao.purchase_date)        aoi.id as id, ");
		sb.append("                                                         ao.purchase_date as order_time, ");
		sb.append("                                                       ao.amazon_order_id as marketplace_order_id, ");
		sb.append("                                                                     NULL as shopify_order_id, ");
		sb.append("                                                          ao.order_status as order_status, ");
		sb.append(                                                 sqlForSkuColumnSource + " as sku_code, ");
		sb.append("                                                            p.name_by_drs as product_name, ");
		sb.append("                                                    aoi.item_price_amount as item_price, ");
		sb.append("                      aoi.item_price_amount-aoi.promotion_discount_amount as actual_retail_price, ");
		sb.append("                                                     aoi.quantity_ordered as qty, ");
		sb.append("                                                            ao.buyer_name as buyer, ");
		sb.append("                                                         ao.sales_channel as sales_channel, ");
		sb.append("                                                                   f.name as fulfillment_center, ");
		sb.append("                                                             dt_refund.id as refund_dt_id ");
		return sb.toString();
	}

	private String generateBasicQuerySqlPartOfSelectForAmazonK101(SkuDisplayOption option){
		String sqlForSkuColumnSource = null;
		if(option==SkuDisplayOption.DRS) sqlForSkuColumnSource = " ps.code_by_drs ";
		if(option==SkuDisplayOption.SUPPLIER) sqlForSkuColumnSource = " p.code_by_supplier ";
		Assert.notNull(sqlForSkuColumnSource);
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct on (aoi.id,ao.purchase_date)        aoi.id as id, ");
		sb.append("                                                         ao.purchase_date as order_time, ");
		sb.append("                                                       ao.amazon_order_id as marketplace_order_id, ");
		sb.append("                                                                     NULL as shopify_order_id, ");
		sb.append("                                                          ao.order_status as order_status, ");
		sb.append(                                                 sqlForSkuColumnSource + " as sku_code, ");
		sb.append("                                                            p.name_by_drs as product_name, ");
		sb.append("                                                    aoi.item_price_amount as item_price, ");
		sb.append("                      aoi.item_price_amount-aoi.promotion_discount_amount as actual_retail_price, ");
		sb.append("                                                     aoi.quantity_ordered as qty, ");
		sb.append("                                                            ao.buyer_name as buyer, ");
		sb.append("                                                         ao.sales_channel as sales_channel, ");
		sb.append("                                                                   f.name as fulfillment_center, ");
		sb.append("                                                             		NULL as refund_dt_id ");
		return sb.toString();
	}

	private String generateBasicQuerySqlPartOfSelectForAmazonExporting(SkuDisplayOption option){
		String sqlForSkuColumnSource = null;
		if(option==SkuDisplayOption.DRS) sqlForSkuColumnSource = " ps.code_by_drs ";
		if(option==SkuDisplayOption.SUPPLIER) sqlForSkuColumnSource = " p.code_by_supplier ";
		Assert.notNull(sqlForSkuColumnSource);
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct on (aoi.id,ao.purchase_date)        aoi.id as id, ");
		sb.append("                                                         ao.purchase_date as order_time, ");
		sb.append("                                                       ao.amazon_order_id as marketplace_order_id, ");
		sb.append("                                                                     NULL as shopify_order_id, ");
		sb.append("                                                          ao.order_status as order_status, ");
		sb.append(                                                 sqlForSkuColumnSource + " as sku_code, ");
		sb.append("                                                            p.name_by_drs as product_name, ");
		sb.append("                                                    aoi.item_price_amount as item_price, ");
		sb.append("                      aoi.item_price_amount-aoi.promotion_discount_amount as actual_retail_price, ");
		sb.append("                                                     aoi.quantity_ordered as qty, ");
		sb.append("                                                            ao.buyer_name as buyer, ");
		sb.append("                                                         ao.sales_channel as sales_channel, ");
		sb.append("                                                                   f.name as fulfillment_center, ");
		sb.append("                                                             dt_refund.id as refund_dt_id, ");
		sb.append("                                                          ao.address_city as city, ");
		sb.append("                                                 ao.address_stateorregion as state, ");
		sb.append("                                                   ao.address_countrycode as country ");
		return sb.toString();
	}

	private String generateBasicQuerySqlPartOfSelectForAmazonExportingK101(SkuDisplayOption option){
		String sqlForSkuColumnSource = null;
		if(option==SkuDisplayOption.DRS) sqlForSkuColumnSource = " ps.code_by_drs ";
		if(option==SkuDisplayOption.SUPPLIER) sqlForSkuColumnSource = " p.code_by_supplier ";
		Assert.notNull(sqlForSkuColumnSource);
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct on (aoi.id,ao.purchase_date)        aoi.id as id, ");
		sb.append("                                                         ao.purchase_date as order_time, ");
		sb.append("                                                       ao.amazon_order_id as marketplace_order_id, ");
		sb.append("                                                                     NULL as shopify_order_id, ");
		sb.append("                                                          ao.order_status as order_status, ");
		sb.append(                                                 sqlForSkuColumnSource + " as sku_code, ");
		sb.append("                                                            p.name_by_drs as product_name, ");
		sb.append("                                                    aoi.item_price_amount as item_price, ");
		sb.append("                      aoi.item_price_amount-aoi.promotion_discount_amount as actual_retail_price, ");
		sb.append("                                                     aoi.quantity_ordered as qty, ");
		sb.append("                                                            ao.buyer_name as buyer, ");
		sb.append("                                                         ao.sales_channel as sales_channel, ");
		sb.append("                                                                   f.name as fulfillment_center, ");
		sb.append("                                                             		NULL as refund_dt_id, ");
		sb.append("                                                          ao.address_city as city, ");
		sb.append("                                                 ao.address_stateorregion as state, ");
		sb.append("                                                   ao.address_countrycode as country ");
		return sb.toString();
	}

	private String generateBasicQuerySqlPartOfFromForAmazon(){
		StringBuilder sb = new StringBuilder()
				.append("from amazon_order ao ")
				.append("inner join amazon_order_item aoi on ao.id = aoi.order_db_id ")
				.append("inner join marketplace m on m.mws_marketplace_id = ao.marketplace_id ")
				.append("inner join marketplace f on f.id = m.warehouse_id ")
				.append("inner join product_marketplace_info pmi on ( pmi.marketplace_id=m.id and aoi.seller_sku=pmi.marketplace_sku ) ")
				.append("inner join product p on p.id = pmi.product_id ")
				.append("inner join product_sku ps on ps.product_id = p.id ")
				.append("inner join product_base pb on pb.id = ps.product_base_id ")
				.append("inner join company com on com.id = pb.supplier_company_id ")
				.append("left join amazon_order_item_promotion_id aoipi on aoi.id = aoipi.order_item_db_id ")
				.append("left join drs_transaction dt on (")
				.append("    dt.source_transaction_id = ao.amazon_order_Id and ")
				.append("    dt.product_sku_id = ps.id and ")
				.append("    dt.type = '"+AmazonTransactionType.ORDER.getValue()+"' ")
				.append(")")
				.append("left join drs_transaction dt_refund on (")
				.append("    dt_refund.source_transaction_id = ao.amazon_order_Id and ")
				.append("    dt_refund.product_sku_id = ps.id and ")
				.append("    dt_refund.type = '"+AmazonTransactionType.REFUND.getValue()+"' ")
				.append(") ");
		return sb.toString();
	}

	private String generateBasicQuerySqlPartOfFromForAmazonK101(){
		StringBuilder sb = new StringBuilder()
				.append("from amazon_order ao ")
				.append("inner join amazon_order_item aoi on ao.id = aoi.order_db_id ")
				.append("inner join marketplace m on m.mws_marketplace_id = ao.marketplace_id ")
				.append("inner join marketplace f on f.id = m.warehouse_id ")
				.append("inner join product_k101_marketplace_info pmi on ( pmi.marketplace_id=m.id and aoi.seller_sku=pmi.marketplace_sku ) ")
				.append("inner join product p on p.id = pmi.product_id ")
				.append("inner join product_sku ps on ps.product_id = p.id ")
				.append("inner join product_base pb on pb.id = ps.product_base_id ")
				.append("inner join company com on com.id = pb.supplier_company_id ")
				.append("left join amazon_order_item_promotion_id aoipi on aoi.id = aoipi.order_item_db_id ");
		return sb.toString();
	}

	private String generateBasicQuerySqlPartOfSelectForShopify(SkuDisplayOption option){
		String sqlForSkuColumnSource = null;
		if(option==SkuDisplayOption.DRS) sqlForSkuColumnSource = " ps.code_by_drs ";
		if(option==SkuDisplayOption.SUPPLIER) sqlForSkuColumnSource = " p.code_by_supplier ";
		Assert.notNull(sqlForSkuColumnSource);
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct on (soi.id)                            soi.id as id, ");
		sb.append("                                                so.created_at as order_time, ");
		sb.append("                                    		             so.name as marketplace_order_id, ");
		sb.append("                                    		         so.order_id as shopify_order_id, ");
		sb.append("                                          so.financial_status as order_status, ");
		sb.append( 									   sqlForSkuColumnSource + " as sku_code, ");
		sb.append("                                                p.name_by_drs as product_name, ");
		sb.append("                                 				   soi.price as item_price, ");
		sb.append(                    this.calculateActualRetailPriceShopify()+" as actual_retail_price, ");
		sb.append("                                  	            soi.quantity as qty, ");
		sb.append(                 this.generateSqlqueryBuyerNameFromShopify()+" as buyer, ");
		sb.append("                                                       m.name as sales_channel, ");
		sb.append("                                                       f.name as fulfillment_center, ");
		sb.append("                                          	    dt_refund.id as refund_dt_id ");
		return sb.toString();
	}

	private String generateBasicQuerySqlPartOfSelectForShopifyK101(SkuDisplayOption option){
		String sqlForSkuColumnSource = null;
		if(option==SkuDisplayOption.DRS) sqlForSkuColumnSource = " ps.code_by_drs ";
		if(option==SkuDisplayOption.SUPPLIER) sqlForSkuColumnSource = " p.code_by_supplier ";
		Assert.notNull(sqlForSkuColumnSource);
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct on (soi.id)                            soi.id as id, ");
		sb.append("                                                so.created_at as order_time, ");
		sb.append("                                    		             so.name as marketplace_order_id, ");
		sb.append("                                    		         so.order_id as shopify_order_id, ");
		sb.append("                                          so.financial_status as order_status, ");
		sb.append( 									   sqlForSkuColumnSource + " as sku_code, ");
		sb.append("                                                p.name_by_drs as product_name, ");
		sb.append("                                 				   soi.price as item_price, ");
		sb.append(                    this.calculateActualRetailPriceShopify()+" as actual_retail_price, ");
		sb.append("                                  	            soi.quantity as qty, ");
		sb.append(                 this.generateSqlqueryBuyerNameFromShopify()+" as buyer, ");
		sb.append("                                                       m.name as sales_channel, ");
		sb.append("                                                       f.name as fulfillment_center, ");
		sb.append("                                          	    		NULL as refund_dt_id ");
		return sb.toString();
	}

	private String generateBasicQuerySqlPartOfSelectForShopifyExporting(SkuDisplayOption option){
		String sqlForSkuColumnSource = null;
		if(option==SkuDisplayOption.DRS) sqlForSkuColumnSource = " ps.code_by_drs ";
		if(option==SkuDisplayOption.SUPPLIER) sqlForSkuColumnSource = " p.code_by_supplier ";
		Assert.notNull(sqlForSkuColumnSource);
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct on (soi.id)                      	  soi.id as id, ");
		sb.append("                                                so.created_at as order_time, ");
		sb.append("                                    		             so.name as marketplace_order_id, ");
		sb.append("                                    		         so.order_id as shopify_order_id, ");
		sb.append("                                          so.financial_status as order_status, ");
		sb.append( 									   sqlForSkuColumnSource + " as sku_code, ");
		sb.append("                                                p.name_by_drs as product_name, ");
		sb.append("                                 				   soi.price as item_price, ");
		sb.append(                    this.calculateActualRetailPriceShopify()+" as actual_retail_price, ");
		sb.append("                                  	            soi.quantity as qty, ");
		sb.append(                 this.generateSqlqueryBuyerNameFromShopify()+" as buyer, ");
		sb.append("                                                       m.name as sales_channel, ");
		sb.append("                                                       f.name as fulfillment_center, ");
		sb.append("                                          	    dt_refund.id as refund_dt_id, ");
		sb.append(						this.generateSqlqueryCityFromShopify()+" as city, ");
		sb.append(					   this.generateSqlqueryStateFromShopify()+" as state, ");
		sb.append(					 this.generateSqlqueryCountryFromShopify()+" as country ");
		return sb.toString();
	}

	private String generateBasicQuerySqlPartOfSelectForShopifyExportingK101(SkuDisplayOption option){
		String sqlForSkuColumnSource = null;
		if(option==SkuDisplayOption.DRS) sqlForSkuColumnSource = " ps.code_by_drs ";
		if(option==SkuDisplayOption.SUPPLIER) sqlForSkuColumnSource = " p.code_by_supplier ";
		Assert.notNull(sqlForSkuColumnSource);
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct on (soi.id)							  soi.id as id, ");
		sb.append("                                                so.created_at as order_time, ");
		sb.append("                                    		             so.name as marketplace_order_id, ");
		sb.append("                                    		         so.order_id as shopify_order_id, ");
		sb.append("                                          so.financial_status as order_status, ");
		sb.append( 									   sqlForSkuColumnSource + " as sku_code, ");
		sb.append("                                                p.name_by_drs as product_name, ");
		sb.append("                                 				   soi.price as item_price, ");
		sb.append(                    this.calculateActualRetailPriceShopify()+" as actual_retail_price, ");
		sb.append("                                  	            soi.quantity as qty, ");
		sb.append(                 this.generateSqlqueryBuyerNameFromShopify()+" as buyer, ");
		sb.append("                                                       m.name as sales_channel, ");
		sb.append("                                                       f.name as fulfillment_center, ");
		sb.append("                                          	    		NULL as refund_dt_id, ");
		sb.append(						this.generateSqlqueryCityFromShopify()+" as city, ");
		sb.append(					   this.generateSqlqueryStateFromShopify()+" as state, ");
		sb.append(					 this.generateSqlqueryCountryFromShopify()+" as country ");
		return sb.toString();
	}
	*/

	/*
	private String calculateActualRetailPriceShopify() {
		return "CASE WHEN soi.json_discount_allocations != '[]' \n" +
				"\t\tTHEN (soi.quantity * soi.price) - (json_array_elements(cast(soi.json_discount_allocations as json))->>'amount')\\:\\:numeric \n" +
				"\tELSE soi.quantity * soi.price END";
	}

	private String generateSqlqueryBuyerNameFromShopify(){
		StringBuilder sb = new StringBuilder();
		sb.append("(select value from json_each_text( CAST(soi.json_destination_location as json ) )  where key = 'name')");
		return sb.toString();
	}

	private String generateSqlqueryCityFromShopify(){
		StringBuilder sb = new StringBuilder();
		sb.append("(select value from json_each_text( CAST(soi.json_destination_location as json ) )  where key = 'city')");
		return sb.toString();
	}

	private String generateSqlqueryStateFromShopify(){
		StringBuilder sb = new StringBuilder();
		sb.append("(select value from json_each_text( CAST(soi.json_destination_location as json ) )  where key = 'province_code')");
		return sb.toString();
	}

	private String generateSqlqueryCountryFromShopify(){
		StringBuilder sb = new StringBuilder();
		sb.append("(select value from json_each_text( CAST(soi.json_destination_location as json ) )  where key = 'country_code')");
		return sb.toString();
	}

	private String generateBasicQuerySqlPartOfFromForShopify(){
		StringBuilder sb = new StringBuilder();
		sb.append("from shopify_order so ");
		sb.append("inner join shopify_order_line_item soi on so.id = soi.shopify_order_id ");
		sb.append("inner join shopify_order_shipping_line sosi on so.id = sosi.shopify_order_id ");
		sb.append("inner join product_marketplace_info pmi on soi.sku = pmi.marketplace_sku ");
		sb.append("inner join marketplace m on m.name = '"+Marketplace.TRUETOSOURCE.getName()+"' ");
		sb.append("inner join marketplace f on f.id = m.warehouse_id ");
		sb.append("inner join product p on p.id = pmi.product_id ");
		sb.append("inner join product_sku ps on ps.product_id = p.id ");
		sb.append("inner join product_base pb on pb.id = ps.product_base_id ");
		sb.append("inner join company com on com.id = pb.supplier_company_id ");
		sb.append("left join drs_transaction dt on (");
		sb.append("    dt.source_transaction_id = so.order_id and ");
		sb.append("    dt.product_sku_id = ps.id and ");
		sb.append("    dt.type = '"+AmazonTransactionType.ORDER.getValue()+"' ");
		sb.append(")");
		sb.append("left join drs_transaction dt_refund on (");
		sb.append("    dt_refund.source_transaction_id = so.order_id and ");
		sb.append("    dt_refund.product_sku_id = ps.id and ");
		sb.append("    dt_refund.type = '"+AmazonTransactionType.REFUND.getValue()+"' ");
		sb.append(") ");
		return sb.toString();
	}

	private String generateBasicQuerySqlPartOfFromForShopifyK101(){
		StringBuilder sb = new StringBuilder();
		sb.append("from shopify_order so ");
		sb.append("inner join shopify_order_line_item soi on so.id = soi.shopify_order_id ");
		sb.append("inner join shopify_order_shipping_line sosi on so.id = sosi.shopify_order_id ");
		sb.append("inner join product_k101_marketplace_info pmi on soi.sku = pmi.marketplace_sku ");
		sb.append("inner join marketplace m on m.name = '"+Marketplace.TRUETOSOURCE.getName()+"' ");
		sb.append("inner join marketplace f on f.id = m.warehouse_id ");
		sb.append("inner join product p on p.id = pmi.product_id ");
		sb.append("inner join product_sku ps on ps.product_id = p.id ");
		sb.append("inner join product_base pb on pb.id = ps.product_base_id ");
		sb.append("inner join company com on com.id = pb.supplier_company_id ");
		return sb.toString();
	}

	private String generateJoinSqlPartOfK101(){
		return "join (" +
				"select distinct product_id from product_k101_marketplace_info" +
				") pmi101 on p.id = pmi101.product_id ";
	}*/



	/*
	private String composeSqlConditionStringForAmazon(ListCustomerOrderCondition condition){
		StringBuilder sqlSb = new StringBuilder();
		if(condition.getSupplierKcode()!=null){
			sqlSb.append("and com.k_code = :supplierKcode ");
		}
		if(condition.getOrderDateStart()!=null){
			sqlSb.append("and ao.purchase_date >= :orderDateStart ");
		}
		if(condition.getOrderDateEnd()!=null){
			sqlSb.append("and ao.purchase_date < :orderDateEnd ");
		}
		if(condition.getTransactionDateStart()!=null){
			sqlSb.append("and dt.transaction_date >= :transactionDateStart ");
		}
		if(condition.getTransactionDateEnd()!=null){
			sqlSb.append("and dt.transaction_date < :transactionDateEnd ");
		}
		if(condition.getRelatedBaseProductCode()!=null && condition.getRelatedSkuCode()==null ){
			sqlSb.append("and pb.code_by_drs = :productBaseCode ");
		}
		if(condition.getRelatedSkuCode()!=null){
			sqlSb.append("and ps.code_by_drs = :productSkuCode ");
		}
		if(condition.getAmazonMerchantSKU()!=null){
			sqlSb.append("and aoi.seller_sku = :amazonMerchantSku ");
		}
		if(condition.getSalesChannelId()!=null){
			SalesChannel salesChannel = SalesChannel.fromKey(Integer.parseInt(condition.getSalesChannelId()));
			if(salesChannel==SalesChannel.NON_ORDER){
				sqlSb.append("and ao.sales_channel is null ");
				sqlSb.append("and (ao.amazon_order_id not ilike :sbOrderId) and (ao.amazon_order_id not ilike :eBayOrderId) ");
			}
			else if(salesChannel==SalesChannel.EBAY){
				sqlSb.append("and (ao.amazon_order_id ilike :sbOrderId or ao.amazon_order_id ilike :eBayOrderId) ");
			}
			else{
				sqlSb.append("and ao.sales_channel = :salesChannel ");
			}
		}

		sqlSb.append(conditionForAmazonOrderStatus(condition.getOrderStatus()));

		if(condition.getMarketplaceOrderId()!=null){
			sqlSb.append("and ao.amazon_order_id = :marketplaceOrderId ");
		}
		if(condition.getCustomerName()!=null){
			sqlSb.append("and ( ao.buyer_name ilike :customerName or ao.address_name ilike :customerName )");
		}
		if(condition.getBuyerEmail()!=null){
			sqlSb.append("and ao.buyer_email = :buyerEmail ");
		}
		if(condition.getPromotionId()!=null){
			sqlSb.append("and aoipi.promotion_id ilike :promotionId ");
		}
		if(condition.getAsin()!=null){
			sqlSb.append("and aoi.asin = :asin ");
		}
		sqlSb.append("and ao.amazon_order_id not like 'USTTS%' ");
		return sqlSb.toString();
	}

	private String composeSqlConditionStringForAmazonK101(ListCustomerOrderCondition condition){
		StringBuilder sqlSb = new StringBuilder();
		if(condition.getSupplierKcode()!=null && !condition.getSupplierKcode().equals("K101")){
			sqlSb.append("and com.k_code = :supplierKcode ");
		}
		if(condition.getOrderDateStart()!=null){
			sqlSb.append("and ao.purchase_date >= :orderDateStart ");
		}
		if(condition.getOrderDateEnd()!=null){
			sqlSb.append("and ao.purchase_date < :orderDateEnd ");
		}
		if(condition.getRelatedBaseProductCode()!=null && condition.getRelatedSkuCode()==null ){
			sqlSb.append("and pb.code_by_drs = :productBaseCode ");
		}
		if(condition.getRelatedSkuCode()!=null){
			sqlSb.append("and ps.code_by_drs = :productSkuCode ");
		}
		if(condition.getAmazonMerchantSKU()!=null){
			sqlSb.append("and aoi.seller_sku = :amazonMerchantSku ");
		}
		if(condition.getSalesChannelId()!=null){
			SalesChannel salesChannel = SalesChannel.fromKey(Integer.parseInt(condition.getSalesChannelId()));
			if(salesChannel==SalesChannel.NON_ORDER){
				sqlSb.append("and ao.sales_channel is null ");
				sqlSb.append("and (ao.amazon_order_id not ilike :sbOrderId) and (ao.amazon_order_id not ilike :eBayOrderId) ");
			}
			else if(salesChannel==SalesChannel.EBAY){
				sqlSb.append("and (ao.amazon_order_id ilike :sbOrderId or ao.amazon_order_id ilike :eBayOrderId) ");
			}
			else{
				sqlSb.append("and ao.sales_channel = :salesChannel ");
			}
		}

		sqlSb.append(conditionForAmazonK101OrderStatus(condition.getOrderStatus()));

		if(condition.getMarketplaceOrderId()!=null){
			sqlSb.append("and ao.amazon_order_id = :marketplaceOrderId ");
		}
		if(condition.getCustomerName()!=null){
			sqlSb.append("and ( ao.buyer_name ilike :customerName or ao.address_name ilike :customerName )");
		}
		if(condition.getBuyerEmail()!=null){
			sqlSb.append("and ao.buyer_email = :buyerEmail ");
		}
		if(condition.getPromotionId()!=null){
			sqlSb.append("and aoipi.promotion_id ilike :promotionId ");
		}
		if(condition.getAsin()!=null){
			sqlSb.append("and aoi.asin = :asin ");
		}
		sqlSb.append("and ao.amazon_order_id not like 'USTTS%' ");
		return sqlSb.toString();
	}

	private String composeSqlConditionStringForShopify(ListCustomerOrderCondition condition){
		StringBuilder sqlSb = new StringBuilder();
		if(condition.getSupplierKcode()!=null){
			sqlSb.append("and com.k_code = :supplierKcode ");
		}
		if(condition.getOrderDateStart()!=null){
			sqlSb.append("and so.created_at >= :orderDateStart ");
		}
		if(condition.getOrderDateEnd()!=null){
			sqlSb.append("and so.created_at < :orderDateEnd ");
		}
		if(condition.getTransactionDateStart()!=null){
			sqlSb.append("and dt.transaction_date >= :transactionDateStart ");
		}
		if(condition.getTransactionDateEnd()!=null){
			sqlSb.append("and dt.transaction_date < :transactionDateEnd ");
		}
		if(condition.getRelatedBaseProductCode()!=null && condition.getRelatedSkuCode()==null ){
			sqlSb.append("and pb.code_by_drs = :productBaseCode ");
		}
		if(condition.getRelatedSkuCode()!=null){
			sqlSb.append("and ps.code_by_drs = :productSkuCode ");
		}
		if(condition.getCustomerName()!=null){
			sqlSb.append("and EXISTS ( select value from json_each_text( CAST(soi.json_destination_location as json ) )  where key = 'name' and value ilike :customerName )");
		}
		if(condition.getSalesChannelId()!=null){
			SalesChannel salesChannel = SalesChannel.fromKey(Integer.parseInt(condition.getSalesChannelId()));
			if(salesChannel!=SalesChannel.TRUETOSOURCE) sqlSb.append("and false ");
		}

		sqlSb.append(conditionForShopifyOrderStatus(condition.getOrderStatus()));

		if(condition.getMarketplaceOrderId()!=null){
			sqlSb.append("and so.name = :marketplaceOrderId ");
		}
		if(condition.getBuyerEmail()!=null){
			sqlSb.append("and so.email = :buyerEmail ");
		}
		if(condition.getAmazonMerchantSKU()!=null||condition.getPromotionId()!=null||condition.getAsin()!=null){
			sqlSb.append("and FALSE ");
		}
		return sqlSb.toString();
	}

	private String composeSqlConditionStringForShopifyK101(ListCustomerOrderCondition condition){
		StringBuilder sqlSb = new StringBuilder();
		if(condition.getSupplierKcode()!=null && !condition.getSupplierKcode().equals("K101")){
			sqlSb.append("and com.k_code = :supplierKcode ");
		}
		if(condition.getOrderDateStart()!=null){
			sqlSb.append("and so.created_at >= :orderDateStart ");
		}
		if(condition.getOrderDateEnd()!=null){
			sqlSb.append("and so.created_at < :orderDateEnd ");
		}
		if(condition.getRelatedBaseProductCode()!=null && condition.getRelatedSkuCode()==null ){
			sqlSb.append("and pb.code_by_drs = :productBaseCode ");
		}
		if(condition.getRelatedSkuCode()!=null){
			sqlSb.append("and ps.code_by_drs = :productSkuCode ");
		}
		if(condition.getCustomerName()!=null){
			sqlSb.append("and EXISTS ( select value from json_each_text( CAST(soi.json_destination_location as json ) )  where key = 'name' and value ilike :customerName )");
		}
		if(condition.getSalesChannelId()!=null){
			SalesChannel salesChannel = SalesChannel.fromKey(Integer.parseInt(condition.getSalesChannelId()));
			if(salesChannel!=SalesChannel.TRUETOSOURCE) sqlSb.append("and false ");
		}

		sqlSb.append(conditionForShopifyK101OrderStatus(condition.getOrderStatus()));

		if(condition.getMarketplaceOrderId()!=null){
			sqlSb.append("and so.name = :marketplaceOrderId ");
		}
		if(condition.getBuyerEmail()!=null){
			sqlSb.append("and so.email = :buyerEmail ");
		}
		if(condition.getAmazonMerchantSKU()!=null||condition.getPromotionId()!=null||condition.getAsin()!=null){
			sqlSb.append("and FALSE ");
		}
		return sqlSb.toString();
	}

	private String conditionForAmazonOrderStatus(String orderStatus) {
		if (StringUtils.hasText(orderStatus)) {
			if (orderStatus.equalsIgnoreCase("Canceled")) {
				return "and (ao.order_status = 'Canceled' OR ao.order_status = 'CANCELLED') ";
			} else if (orderStatus.equalsIgnoreCase("Refunded")){
				return "and (ao.order_status = :orderStatus OR dt_refund.id is not null ) ";
			} else {
				return "and ao.order_status = :orderStatus ";
			}
		}
		return "";
	}

	private String conditionForAmazonK101OrderStatus(String orderStatus) {
		if (StringUtils.hasText(orderStatus)) {
			if (orderStatus.equalsIgnoreCase("Canceled")) {
				return "and (ao.order_status = 'Canceled' OR ao.order_status = 'CANCELLED') ";
			} else {
				return "and ao.order_status = :orderStatus ";
			}
		}
		return "";
	}

	private String conditionForShopifyOrderStatus(String orderStatus) {
		if (StringUtils.hasText(orderStatus)) {
			if (orderStatus.equalsIgnoreCase("Refunded")) {
				return "and (so.financial_status = :orderStatus OR dt_refund.id is not null ) ";
			}
			return "and so.financial_status = :orderStatus ";
		}
		return "";
	}

	private String conditionForShopifyK101OrderStatus(String orderStatus) {
		if (StringUtils.hasText(orderStatus)) {
			return "and so.financial_status = :orderStatus ";
		}
		return "";
	}
	*/

}