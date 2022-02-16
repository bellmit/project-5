package com.kindminds.drs.persist.data.access.usecase.report;

import java.util.Date;
import java.util.List;



import com.kindminds.drs.enums.ProductNameDisplayOption;
import com.kindminds.drs.persist.data.access.rdb.Dao;
import com.kindminds.drs.api.v1.model.report.Satisfaction;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;


import com.kindminds.drs.api.data.access.usecase.report.ViewSatisfactionReportDao;
import com.kindminds.drs.enums.AmazonTransactionType;
import com.kindminds.drs.persist.v1.model.mapping.report.SatisfactionDto;

@Repository
public class ViewSatisfactionReportDaoImpl  extends Dao implements ViewSatisfactionReportDao {
	


	@Override
	public Date queryEnd() {
		String sql = "select max(period_end) from settlement_period ";

		return getJdbcTemplate().queryForObject(sql,Date.class);
	}

	@Override
	public Date queryStart(int periodsBefore) {
		String sql = "select period_start from settlement_period sp order by sp.period_start desc limit 1 offset :offset ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("offset",periodsBefore);
		return getNamedParameterJdbcTemplate().queryForObject(sql,q,Date.class);
	}

	@Override
	public List<Satisfaction> query(int marketPlaceId, String supplierKCode, List<String> countedReturnReasons, ProductNameDisplayOption productDisplayName) {
		String sql = buildBaseSql();
		sql = sql.replace("{whereCondMarketPlace}", buildWhereCondMarketPlace(marketPlaceId));
		sql = sql.replace("{whereCondMarketPlaceForReturn}", buildWhereCondMarketPlaceForReturn(marketPlaceId));
		Integer supplierId = querySupplierId(supplierKCode);
		sql = sql.replace("{whereCondSupplier}", buildWhereCondSupplier(supplierId));
		sql = sql.replace("{whereCondCountedReturnReason}", this.buildWhereCondCountedReturnReason());
		sql = sql.replace("{whereCondDtType}", this.buildWhereCondDtType());
		String sqlForProductDisplayName;
		if(productDisplayName== ProductNameDisplayOption.DRS) sqlForProductDisplayName = "name_by_drs";
		else sqlForProductDisplayName = "name_by_supplier";
		sql = sql.replace("{displayProductName}", sqlForProductDisplayName);
		List<SatisfactionDto> dtoList = this.queryData(marketPlaceId, supplierId, sql,countedReturnReasons);
		return (List<Satisfaction>)(List<?>) dtoList;
	}

	private String buildWhereCondMarketPlace(int marketPlaceId) {
		return "where dt.marketplace_id = :mid\n";
	}

	private String buildWhereCondMarketPlaceForReturn(int marketPlaceId) {
		return "where m.id = :mid\n";
	}

	private String buildWhereCondSupplier(Integer supplierId) {
		if (supplierId == null) return "";
		return "and pb.supplier_company_id = :sid\n";
	}

	private String buildWhereCondCountedReturnReason() {
		return "and rr.reason in (:countedReturnReason) \n";
	}

	private String buildWhereCondDtType(){
		return "and dt.type = '"+AmazonTransactionType.ORDER.getValue()+"' ";
	}

	private Integer querySupplierId(String supplierKCode) {
		if (supplierKCode == null)
			return null;
		String sql = "select id from company where k_code = :code";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("code", supplierKCode);
		return getNamedParameterJdbcTemplate().queryForObject(sql,q,Integer.class);
	}



	@SuppressWarnings("unchecked")
	private List<SatisfactionDto> queryData(int marketPlaceId, Integer supplierId, String sql,List<String> countedReturnReason) {
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("mid", marketPlaceId);
		q.addValue("countedReturnReason",countedReturnReason);
		if (supplierId != null)
			q.addValue("sid", supplierId);


		return getNamedParameterJdbcTemplate().query(sql,q,(rs,rowNum) -> new SatisfactionDto(
				rs.getInt("sku_id"),rs.getString("code_by_drs"),
				rs.getString("product_name"),rs.getInt("supplier_company_id")
				,rs.getInt("quantity_1"),rs.getInt("return_quantity_1"),
				rs.getInt("quantity_2"),rs.getInt("return_quantity_2")
				,rs.getInt("quantity_6"),rs.getInt("return_quantity_6")
		));
	}

	private String buildBaseSql() {
		String sb = "select r2.supplier_company_id, r2.code_by_drs, r2.{displayProductName} as product_name, "
				+ "r2.sku_id, sum(r2.quantity_1) as quantity_1, sum(r2.return_quantity_1) as return_quantity_1, "
				+ "sum(r2.quantity_2) as quantity_2, sum(r2.return_quantity_2) as return_quantity_2, "
				+ "sum(r2.quantity_6) as quantity_6, sum(r2.return_quantity_6) as return_quantity_6\n from ("

				//within the last 1 settlement period
			+ "select r.supplier_company_id, r.code_by_drs, r.{displayProductName}, r.id as sku_id, sum(r.quantity) as quantity_1, sum(return_quantity) as return_quantity_1, 0 as quantity_2, 0 as return_quantity_2, 0 as quantity_6, 0 as return_quantity_6\n"
			+ "from (select pb.supplier_company_id, ps.code_by_drs, p.{displayProductName}, ps.id, sum(dt.quantity) as quantity, 0 as return_quantity\n"
			+ "from product_base as pb\n"
			+ "inner join product_sku as ps on (pb.id = ps.product_base_id)\n"
			+ "inner join drs_transaction as dt on (ps.id = dt.product_sku_id)\n"
			+ "inner join product as p on ( ps.product_id = p.id )\n"
			+ "inner join (select min(period_start) as period_start, max(period_end) as period_end from settlement_period where id in (select id from settlement_period order by id desc limit 1 offset 0)) as sp on (dt.transaction_date between sp.period_start and sp.period_end)\n"
			+ "{whereCondMarketPlace}"
			+ "{whereCondSupplier}"
			+ "{whereCondDtType}"
			+ "group by pb.supplier_company_id, ps.code_by_drs, p.{displayProductName}, ps.id\n"

			+ "union\n"

			+ "select pb.supplier_company_id, ps.code_by_drs, p.{displayProductName} as product_name, ps.id, 0 as quantity, sum(rr.quantity) as return_quantity\n"
			+ "from amazon_return_report rr\n"
			+ "inner join product_sku as ps on (ps.code_by_drs = rr.sku)\n"
			+ "inner join product_base pb on pb.id = ps.product_base_id\n"
			+ "inner join product as p on ( ps.product_id = p.id )\n"
			+ "inner join pv.product_all_marketplace_info pmi on rr.sku = pmi.marketplace_sku\n"
			+ "inner join marketplace m on m.id = pmi.marketplace_id\n"
			+ "inner join (select min(period_start) as period_start, max(period_end) as period_end from settlement_period where id in (select id from settlement_period order by id desc limit 1 offset 0)) as sp on (rr.return_date between sp.period_start and sp.period_end)\n"
			+ "{whereCondMarketPlaceForReturn}"
			+ "{whereCondSupplier}"
			+ "{whereCondCountedReturnReason}"
			+ "group by pb.supplier_company_id, ps.code_by_drs, p.{displayProductName}, ps.id) as r\n"
			+ "group by r.supplier_company_id, r.code_by_drs, r.{displayProductName}, r.id\n"

			+ "union\n"

				//within the last 2 settlement periods
			+ "select r.supplier_company_id, r.code_by_drs, r.{displayProductName} as product_name, r.id as sku_id, 0 as quantity_1, 0 as return_quantity_1, sum(r.quantity) as quantity_2, sum(return_quantity) as return_quantity_2, 0 as quantity_6, 0 as return_quantity_6\n"
			+ "from (select pb.supplier_company_id, ps.code_by_drs, p.{displayProductName}, ps.id, sum(dt.quantity) as quantity, 0 as return_quantity\n"
			+ "from product_base as pb\n"
			+ "inner join product_sku as ps on (pb.id = ps.product_base_id)\n"
			+ "inner join drs_transaction as dt on (ps.id = dt.product_sku_id)\n"
			+ "inner join product as p on ( ps.product_id = p.id )\n"
			+ "inner join (select min(period_start) as period_start, max(period_end) as period_end from settlement_period where id in (select id from settlement_period order by id desc limit 2 offset 0)) as sp on (dt.transaction_date between sp.period_start and sp.period_end)\n"
			+ "{whereCondMarketPlace}"
			+ "{whereCondSupplier}"
			+ "{whereCondDtType}"
			+ "group by pb.supplier_company_id, ps.code_by_drs, p.{displayProductName}, ps.id\n"

			+ "union\n"

			+ "select pb.supplier_company_id, ps.code_by_drs, p.{displayProductName} as product_name, ps.id, 0 as quantity, sum(rr.quantity) as return_quantity\n"
			+ "from amazon_return_report rr\n"
			+ "inner join product_sku as ps on (ps.code_by_drs = rr.sku)\n"
			+ "inner join product_base pb on pb.id = ps.product_base_id\n"
			+ "inner join product as p on ( ps.product_id = p.id )\n"
			+ "inner join pv.product_all_marketplace_info pmi on rr.sku = pmi.marketplace_sku\n"
			+ "inner join marketplace m on m.id = pmi.marketplace_id\n"
			+ "inner join (select min(period_start) as period_start, max(period_end) as period_end from settlement_period where id in (select id from settlement_period order by id desc limit 2 offset 0)) as sp on (rr.return_date between sp.period_start and sp.period_end)\n"
			+ "{whereCondMarketPlaceForReturn}"
			+ "{whereCondSupplier}"
			+ "{whereCondCountedReturnReason}"
			+ "group by pb.supplier_company_id, ps.code_by_drs, p.{displayProductName}, ps.id) as r\n"
			+ "group by r.supplier_company_id, r.code_by_drs, r.{displayProductName}, r.id\n"

			+ "union\n"

				//within the last 6 settlement periods
			+ "select r.supplier_company_id, r.code_by_drs, r.{displayProductName} as product_name, r.id as sku_id, 0 as quantity_1, 0 as return_quantity_1, 0 as quantity_2, 0 as return_quantity_2, sum(r.quantity) as quantity_6, sum(return_quantity) as return_quantity_6\n"
			+ "from (select pb.supplier_company_id, ps.code_by_drs, p.{displayProductName}, ps.id, sum(dt.quantity) as quantity, 0 as return_quantity\n"
			+ "from product_base as pb\n"
			+ "inner join product_sku as ps on (pb.id = ps.product_base_id)\n"
			+ "inner join drs_transaction as dt on (ps.id = dt.product_sku_id)\n"
			+ "inner join product as p on ( ps.product_id = p.id )\n"
			+ "inner join (select min(period_start) as period_start, max(period_end) as period_end from settlement_period where id in (select id from settlement_period order by id desc limit 6 offset 0)) as sp on (dt.transaction_date between sp.period_start and sp.period_end)\n"
			+ "{whereCondMarketPlace}"
			+ "{whereCondSupplier}"
			+ "{whereCondDtType}"
			+ "group by pb.supplier_company_id, ps.code_by_drs, p.{displayProductName}, ps.id\n"

			+ "union\n"

			+ "select pb.supplier_company_id, ps.code_by_drs, p.{displayProductName} as product_name, ps.id, 0 as quantity, sum(rr.quantity) as return_quantity\n"
			+ "from amazon_return_report rr\n"
			+ "inner join product_sku as ps on (ps.code_by_drs = rr.sku)\n"
			+ "inner join product_base pb on pb.id = ps.product_base_id\n"
			+ "inner join product as p on ( ps.product_id = p.id )\n"
			+ "inner join pv.product_all_marketplace_info pmi on rr.sku = pmi.marketplace_sku\n"
			+ "inner join marketplace m on m.id = pmi.marketplace_id\n"
			+ "inner join (select min(period_start) as period_start, max(period_end) as period_end from settlement_period where id in (select id from settlement_period order by id desc limit 6 offset 0)) as sp on (rr.return_date between sp.period_start and sp.period_end)\n"
			+ "{whereCondMarketPlaceForReturn}"
			+ "{whereCondSupplier}"
			+ "{whereCondCountedReturnReason}"
			+ "group by pb.supplier_company_id, ps.code_by_drs, p.{displayProductName}, ps.id) as r\n"
			+ "group by r.supplier_company_id, r.code_by_drs, r.{displayProductName}, r.id) as r2\n"
			+ "group by r2.supplier_company_id, r2.code_by_drs, r2.{displayProductName}, r2.sku_id\n"
			+ "order by code_by_drs\n";

		return sb;

	}

}
