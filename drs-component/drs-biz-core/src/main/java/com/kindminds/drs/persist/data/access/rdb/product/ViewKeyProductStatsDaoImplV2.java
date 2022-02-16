package com.kindminds.drs.persist.data.access.rdb.product;


import java.util.Date;
import java.util.List;


import com.kindminds.drs.*;
import com.kindminds.drs.api.data.access.rdb.product.ViewKeyProductStatsDaoV2;
import com.kindminds.drs.persist.data.access.rdb.Dao;
import com.kindminds.drs.persist.v1.model.mapping.report.KeyProductBaseRetailPriceImpl;
import com.kindminds.drs.persist.v1.model.mapping.report.KeyProductInventorySupplyStatsImpl;
import com.kindminds.drs.persist.v1.model.mapping.report.KeyProductTotalOrderImpl;
//import org.hibernate.ejb.EntityManagerImpl;
//import org.hibernate.service.jdbc.connections.spi.ConnectionProvider;

import com.kindminds.drs.api.v2.biz.domain.model.report.KeyProductBaseRetailPrice;
import com.kindminds.drs.api.v2.biz.domain.model.report.KeyProductInventorySupplyStats;
import com.kindminds.drs.api.v2.biz.domain.model.report.KeyProductTotalOrder;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

@Repository("ViewKeyProductStatsDaoImplV2")
public class ViewKeyProductStatsDaoImplV2 extends Dao implements ViewKeyProductStatsDaoV2 {

	@Override
	public List<KeyProductBaseRetailPrice> queryBaseRetailPrice(Filter filter){
		String sql ="SELECT "
				+"                        m.name as marketplace, "
				+"                pb.code_by_drs as bpCode, "
				+"           pmi.marketplace_sku as sku, "
				+" pmi.current_base_retail_price as baseRetailPrice "
				+"       FROM product_marketplace_info pmi "
				+"     inner join product_sku ps on pmi.marketplace_sku =ps.code_by_drs "
				+"    inner join product_base pb on pb.id = ps.product_base_id "
				+"          inner join company c on c.id = pb.supplier_company_id "
				+"      inner join marketplace m on m.id =pmi.marketplace_id "
				+"      where ps.status_id =6";

		MapSqlParameterSource q = new MapSqlParameterSource();

		for (Criteria it: filter.getCriteriaList()) {
			if(it.field == KeyProductStatsQueryField.KCode){
				if(it.value != "All") q.addValue("kcode", it.value);
			}

			if(it.field == KeyProductStatsQueryField.BpCode){
				q.addValue("bp", it.value);
			}

			if(it.field == KeyProductStatsQueryField.SkuCode){
				q.addValue("sku", it.value);
			}

			if(it.field == KeyProductStatsQueryField.Marketplace){
				if(it.value != "All")  q.addValue("sc", it.value);
			}
		}

		List<KeyProductBaseRetailPriceImpl> result = getNamedParameterJdbcTemplate().query(doFilter(sql,filter),q,
				(rs,rowNum) ->new KeyProductBaseRetailPriceImpl(
						rs.getString("marketplace"),rs.getString("bpCode"),
						rs.getString("sku"),rs.getInt("BaseRetailPrice")
				));

		return (List)result;
	}

	@Override
	public List<KeyProductTotalOrder> queryTotalOrder(Filter filter, Date from, Date to){

		Boolean hasShopify = false;

		String sql ="SELECT "
				+"          ao.sales_channel as marketplace, "
				+"            pb.code_by_drs as bpCode, "
				+"            aoi.seller_sku as sku, "
				+" sum(aoi.quantity_ordered) as totalOrder "
				+        "FROM amazon_order_item aoi "
				+" inner join product_sku ps on aoi.seller_sku = ps.code_by_drs "
				+"inner join product_base pb on pb.id = ps.product_base_id "
				+"      inner join company c on c.id = pb.supplier_company_id "
				+"inner join amazon_order ao on ao.amazon_order_id = aoi.amazon_order_id "
				+"  inner join marketplace m on m.name =ao.sales_channel "
				+"  where ps.status_id =6 "
				+"  and ao.purchase_date >= :startDate "
				+"  and ao.purchase_date < :endDate ";
		sql = doFilter(sql,filter);

		MapSqlParameterSource q = new MapSqlParameterSource();

		for (Criteria it: filter.getCriteriaList()) {
			if(it.field == KeyProductStatsQueryField.KCode){
				if(it.value != "All") q.addValue("kcode", it.value);
			}

			if(it.field == KeyProductStatsQueryField.BpCode){
				q.addValue("bp", it.value);
			}

			if(it.field == KeyProductStatsQueryField.SkuCode){
				q.addValue("sku", it.value);
			}

			if(it.field == KeyProductStatsQueryField.Marketplace){
				if(it.value != "All")  q.addValue("sc", it.value);
				if(it.value == "All" || it.value == "TrueToSource"){
					hasShopify=true;
				}
			}
		}

		if(from != null) q.addValue("startDate" ,from);

		if(to != null) q.addValue("endDate" ,to);

		sql += " group by ao.sales_channel,pb.code_by_drs,  aoi.seller_sku";

		List<KeyProductTotalOrderImpl> result = getNamedParameterJdbcTemplate().query(sql,q,
				(rs,rowNum) ->new KeyProductTotalOrderImpl(
						rs.getString("marketplace"),rs.getString("bpCode"),
						rs.getString("sku"),rs.getInt("totalOrder")
				));


		if (hasShopify == true) {
			String sqlForShopify = "SELECT "
					+ "      pb.code_by_drs as bpCode , "
					+ "            soli.sku as sku, "
					+ "  sum(soli.quantity) as totalOrder "
					+ "   FROM shopify_order_line_item soli "
					+ "    inner join product_sku ps on soli.sku =ps.code_by_drs "
					+ "   inner join product_base pb on pb.id = ps.product_base_id "
					+ "         inner join company c on c.id = pb.supplier_company_id "
			        + "  inner join shopify_order so on so.id = soli.shopify_order_id "
					+ "    where ps.status_id =6 "
					+ "  and so.created_at >= :startDate "
					+ "  and ao.created_at < :endDate ";

			MapSqlParameterSource qs = new MapSqlParameterSource();

			for (Criteria it: filter.getCriteriaList()) {
				if(it.field == KeyProductStatsQueryField.KCode){
					if(it.value != "All") {
						sqlForShopify += "and c.k_code = :kcode ";
						qs.addValue("kcode", it.value);
					}
				}

				if(it.field == KeyProductStatsQueryField.BpCode){
					sqlForShopify += "and pb.code_by_drs = :bp ";
					qs.addValue("bp", it.value);
				}

				if(it.field == KeyProductStatsQueryField.SkuCode){
					sqlForShopify += "and ps.code_by_drs = :sku ";
					qs.addValue("sku", it.value);
				}

			}
			if(from != null) qs.addValue("startDate" ,from);

			if(to != null) qs.addValue("endDate" ,to);
			sqlForShopify +=" group by pb.code_by_drs , soli.sku ";

			List<KeyProductTotalOrderImpl> resultForShopify = getNamedParameterJdbcTemplate().query(sqlForShopify,qs,
					(rs,rowNum) ->new KeyProductTotalOrderImpl(
							"TrueToSource",rs.getString("bpCode"),
							rs.getString("sku"),rs.getInt("totalOrder")
					));
			resultForShopify.forEach( x->{
				result.add(x);
			});

		}

		return (List)result;
	}

	@Override
	public List<KeyProductInventorySupplyStats> queryInventorySupplyStatsByFilter(Filter filter){

		String sql ="SELECT "
				+"                    m.name as marketplace, "
				+"            pb.code_by_drs as bpCode, "
				+"      ais.marketplace_sku  as sku, "
				+"   detail_quantity_inbound as inbound, "
				+"   detail_quantity_instock as instock, "
				+" detail_quantity_transfer  as transfer "
				+"       FROM amazon_inventory_supply ais "
				+"     inner join product_sku ps on ais.marketplace_sku =ps.code_by_drs "
				+"    inner join product_base pb on pb.id = ps.product_base_id "
				+"          inner join company c on c.id = pb.supplier_company_id "
				+"      inner join marketplace m on m.id =ais.marketplace_id "
				+"             where ps.status_id =6 ";

		MapSqlParameterSource q = new MapSqlParameterSource();

		for (Criteria it: filter.getCriteriaList()) {
			if(it.field == KeyProductStatsQueryField.KCode){
				if(it.value != "All") q.addValue("kcode", it.value);
			}

			if(it.field == KeyProductStatsQueryField.BpCode){
				q.addValue("bp", it.value);
			}

			if(it.field == KeyProductStatsQueryField.SkuCode){
				q.addValue("sku", it.value);
			}

			if(it.field == KeyProductStatsQueryField.Marketplace){
				if(it.value != "All")  q.addValue("sc", it.value);
			}
		}
		List<KeyProductInventorySupplyStatsImpl> result =getNamedParameterJdbcTemplate().query(doFilter(sql,filter),q,
				(rs,rowNum) -> new KeyProductInventorySupplyStatsImpl(
						rs.getString("marketplace"), rs.getString("bpCode"),
						rs.getString("sku"),rs.getInt("inbound"),
						rs.getInt("instock"),rs.getInt("transfer")
				));



		return (List)result;
	}

	@Override
	public Date queryLastPeriodEnd(){
		String sql = "select max(period_end) "
				+ "from bill_statement bs ";
		MapSqlParameterSource q = new MapSqlParameterSource();


		Date result = getNamedParameterJdbcTemplate().queryForObject(sql,q,Date.class);
		Assert.notNull(result);
		return result;
	}

	private String doFilter(String sql ,Filter filter){
		for (Criteria it: filter.getCriteriaList()) {
			if(it.field ==KeyProductStatsQueryField.KCode){
				if(it.value != "All") sql += "and c.k_code =  :kcode ";
			}


			if(it.field == KeyProductStatsQueryField.BpCode){
				sql +=    "  and pb.code_by_drs = :bp ";
			}

			if(it.field == KeyProductStatsQueryField.SkuCode){
				sql += " and ps.code_by_drs  = :sku ";
			}

			if(it.field == KeyProductStatsQueryField.Marketplace){
				if(it.value != "All")  sql += " and m.name  = :sc ";

			}
		}
		return sql;
	}

}
