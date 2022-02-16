package com.kindminds.drs.persist.data.access.usecase.accounting;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;





import com.kindminds.drs.persist.data.access.rdb.Dao;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.kindminds.drs.Country;
import com.kindminds.drs.Currency;
import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.data.access.usecase.accounting.CalculateRetainmentRateDao;
import com.kindminds.drs.api.v1.model.accounting.RetainmentRate;
import com.kindminds.drs.api.v1.model.amazon.AmazonTransaction.AmazonTransactionLineItem.AmzAmountTypeDesc;
import com.kindminds.drs.persist.v1.model.mapping.accounting.RetainmentRateImpl;

@Repository
public class CalculateRetainmentRateDaoImpl extends Dao implements CalculateRetainmentRateDao {
	

	
	@Override @SuppressWarnings("unchecked")
	public List<Object []> querySettlementPeriodList() {
		StringBuilder sqlSb = new StringBuilder()
				.append("select    sp.id as id, ")
				.append("sp.period_start as start, ")
				.append("sp.period_end   as end ")
				.append("from settlement_period sp ")
				.append("order by sp.period_start desc ");


		List<Object[]> columnsList = getJdbcTemplate().query(sqlSb.toString(), super.objArrayMapper);
		return columnsList;
	}

	@Override @SuppressWarnings("unchecked")
	public List<RetainmentRate> queryList() {
		String sql = "select                   rr.id as id, "
				+ "                    rr.date_start as utc_date_start, "
				+ "                    rr.date_end   as utc_date_end, "
				+ "                    rr.country_id as country_id, "
				+ "                      splr.k_code as supplier_kcode, "
				+ "          rr.original_currency_id as original_currency_id, "
				+ "  rr.revenue_in_original_currency as revenue_in_original_currency, "
				+ " rr.currency_exchange_rate_to_usd as currency_exchange_rate_to_usd, "
				+ "                rr.revenue_in_usd as revenue_in_usd, "
				+ "                          rr.rate as rate "
				+ "from retainment_rate rr "
				+ "inner join company splr on splr.id = rr.supplier_company_id "
				+ "order by utc_date_start desc, country_id, supplier_kcode ";




		return (List) this.getJdbcTemplate().query(sql, (rs, rowNum) -> new RetainmentRateImpl(
				rs.getInt("id"),rs.getTimestamp("utc_date_start"),
				rs.getTimestamp("utc_date_end"),rs.getInt("country_id"),
				rs.getString("supplier_kcode"),rs.getInt("original_currency_id"),
				rs.getBigDecimal("revenue_in_original_currency"),rs.getBigDecimal("currency_exchange_rate_to_usd"),
				rs.getBigDecimal("revenue_in_usd"),rs.getBigDecimal("rate")
		));
	}

	@Override @SuppressWarnings("unchecked")
	public Map<String,BigDecimal> querySupplierAmazonOrderRevenue(Marketplace marketplace, Date start, Date end, int revenueGradeVer) {
		Assert.isTrue(marketplace.isAmazonMarketplace());
		StringBuilder sqlSb = new StringBuilder() // using distinct to prevent from counting amount for multiple times 
				.append("select k_code, sum(amount) from ")
				.append("( ")
				.append("    select distinct on (asr.id) splr.k_code, asr.id, asr.amount ")
				.append("    from amazon_settlement_report_v2 asr ")
				.append("    inner join product_marketplace_info pmi on pmi.marketplace_sku = asr.sku ")
				.append("    inner join product p on p.id = pmi.product_id ")
				.append("    inner join product_sku ps on ps.product_id = p.id ")
				.append("    inner join product_base pb on pb.id = ps.product_base_id ")
				.append("    inner join company splr on splr.id = pb.supplier_company_id ")
				.append("    where asr.source_marketplace_id = :marketplaceKey ")
				.append("    and splr.revenue_grade_ver = :revenueGradeVer ")
				.append("    and pmi.marketplace_id = :marketplaceKey ")
				.append("    and asr.amount_description = :principalDescription ")
				.append("    and asr.posted_date_time >= :start ")
				.append("    and asr.posted_date_time < :end ")
				.append("    and (asr.merchant_order_id not ilike 'ebay%' or asr.merchant_order_id is NULL ) ")
				.append(") r ")
				.append("group by k_code ");


		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("revenueGradeVer", revenueGradeVer);
		q.addValue("marketplaceKey", marketplace.getKey());
		q.addValue("principalDescription", AmzAmountTypeDesc.PRICE_PRINCIPAL.getDesc());
		q.addValue("start", start);
		q.addValue("end", end);
		List<Object[]> objectArrayList = getNamedParameterJdbcTemplate().query(sqlSb.toString(), q , objArrayMapper);
		Map<String,BigDecimal> resultMap = new TreeMap<String,BigDecimal>();
		for(Object[] columns:objectArrayList){
			String supplierKcode = (String)columns[0];
			BigDecimal revenue = (BigDecimal)columns[1];
			Assert.isTrue(!resultMap.containsKey(supplierKcode));
			resultMap.put(supplierKcode, revenue);
		}
		return resultMap;
	}

	@Override @SuppressWarnings("unchecked")
	public Map<String,BigDecimal> querySupplierShopifyRevenue(Marketplace marketplace, Date start, Date end, int revenueGradeVer) {
		Assert.isTrue(marketplace.isShopifyMarketplace());
		String sql = "select splr.k_code,sum(sor.subtotal) "
				+ "from shopify_order_report sor "
				+ "inner join shopify_payment_transaction_report sptr on sptr.order_name = sor.name "
				+ "inner join product_marketplace_info pmi on pmi.marketplace_sku = sor.lineitem_sku "
				+ "inner join product p on p.id = pmi.product_id "
				+ "inner join product_sku ps on ps.product_id = p.id "
				+ "inner join product_base pb on pb.id = ps.product_base_id "
				+ "inner join company splr on splr.id = pb.supplier_company_id "
				+ "where pmi.marketplace_id = :marketplaceKey "
				+ "and splr.revenue_grade_ver = :revenueGradeVer "
				+ "and sptr.transaction_date >= :start "
				+ "and sptr.transaction_date <  :end "
				+ "group by splr.k_code ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("revenueGradeVer", revenueGradeVer);
		q.addValue("marketplaceKey", marketplace.getKey());
		q.addValue("start", start);
		q.addValue("end", end);
		List<Object[]> objectArrayList = getNamedParameterJdbcTemplate().query(sql,q,super.objArrayMapper);
		Map<String,BigDecimal> resultMap = new TreeMap<String,BigDecimal>();
		for(Object[] columns:objectArrayList){
			String supplierKcode = (String)columns[0];
			BigDecimal revenue = (BigDecimal)columns[1];
			Assert.isTrue(!resultMap.containsKey(supplierKcode));
			resultMap.put(supplierKcode, revenue);
		}
		return resultMap;
	}

	@Override @SuppressWarnings("unchecked")
	public Map<String, BigDecimal> querySupplierEbayRevenue(Marketplace marketplace, Date start, Date end, int revenueGradeVer) {
		Assert.isTrue(!marketplace.isAmazonMarketplace());
		Assert.isTrue(!marketplace.isShopifyMarketplace());
		String sql = "select splr.k_code,sum(et.pretax_price) "
				+ "from ebay_transaction et "
				+ "inner join product_sku ps on ps.code_by_drs=et.drs_sku "
				+ "inner join product_base pb on pb.id = ps.product_base_id "
				+ "inner join company splr on splr.id = pb.supplier_company_id "
				+ "where et.marketplace_id = :marketplaceKey "
				+ "and splr.revenue_grade_ver = :revenueGradeVer "
				+ "and et.transaction_date >= :start "
				+ "and et.transaction_date <  :end "
				+ "group by splr.k_code ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("revenueGradeVer", revenueGradeVer);
		q.addValue("marketplaceKey", marketplace.getKey());
		q.addValue("start", start);
		q.addValue("end", end);
		List<Object[]> objectArrayList = getNamedParameterJdbcTemplate().query(sql,q,super.objArrayMapper);
		Map<String,BigDecimal> resultMap = new TreeMap<String,BigDecimal>();
		for(Object[] columns:objectArrayList){
			String supplierKcode = (String)columns[0];
			BigDecimal revenue = (BigDecimal)columns[1];
			Assert.isTrue(!resultMap.containsKey(supplierKcode));
			resultMap.put(supplierKcode, revenue);
		}
		return resultMap;
	}

	@Override @SuppressWarnings("unchecked")
	public Map<String,BigDecimal> querySupplierIdToFbaReimbursementRevenueMap(Country country, Date start, Date end,List<String> relatedDescriptionList, int revenueGradeVer) {
		StringBuilder sqlSb = new StringBuilder() // using distinct to prevent from counting amount for multiple times
				.append("select k_code,sum(amount) from ")
				.append("( ")
				.append("    select distinct on (asr.id) splr.k_code, asr.id, asr.amount ")
				.append("    from amazon_settlement_report_v2 asr ")
				.append("    inner join product_marketplace_info pmi on (pmi.marketplace_sku = asr.sku and asr.source_marketplace_id = pmi.marketplace_id) ")
				.append("    inner join marketplace m on asr.source_marketplace_id = m.id ")
				.append("    inner join product_sku ps on ps.product_id = pmi.product_id ")
				.append("    inner join product_base pb on pb.id = ps.product_base_id ")
				.append("    inner join company splr on splr.id = pb.supplier_company_id ")
				.append("    where m.country_id = (:countryKey) ")
				.append("    and splr.revenue_grade_ver = :revenueGradeVer ")
				.append("    and asr.amount_type = :amountType ")
				.append("    and asr.amount_description in (:amountDescs) ")
				.append("    and asr.posted_date_time >= :start ")
				.append("    and asr.posted_date_time < :end ")
				.append(") r ")
				.append("group by k_code ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("revenueGradeVer", revenueGradeVer);
		q.addValue("amountType", "FBA Inventory Reimbursement");
		q.addValue("countryKey", country.getKey());
		q.addValue("amountDescs", relatedDescriptionList);
		q.addValue("start", start);
		q.addValue("end", end);
		List<Object[]> objectArrayList = getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,super.objArrayMapper);
		Map<String,BigDecimal> resultMap = new TreeMap<String,BigDecimal>();
		for(Object[] columns:objectArrayList){
			String supplierKcode = (String)columns[0];
			BigDecimal revenue = (BigDecimal)columns[1];
			Assert.isTrue(!resultMap.containsKey(supplierKcode));
			resultMap.put(supplierKcode, revenue);
		}
		return resultMap;
	}

	@Override @Transactional("transactionManager")
	public void insert(Date start,Date end,Country country,String supplierKcode,Currency originalCurrency, BigDecimal revenueInOriginalCurrency, BigDecimal currencyExchangeRateToUsd, BigDecimal revenueInUsd,BigDecimal retainmentRate){
		String sql = "insert into retainment_rate "
				+ "( date_start, date_end, country_id, supplier_company_id, original_currency_id, revenue_in_original_currency, currency_exchange_rate_to_usd,  revenue_in_usd,  rate ) select "
				+ "      :start,     :end, :countryId,              com.id,  :originalCurrencyId,   :revenueInOriginalCurrency,    :currencyExchangeRateToUsd,   :revenueInUsd, :rate "
				+ "from company com where com.k_code = :supplierKcode ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("start", start);
		q.addValue("end", end);
		q.addValue("countryId", country.getKey());
		q.addValue("originalCurrencyId", originalCurrency.getKey());
		q.addValue("revenueInOriginalCurrency", revenueInOriginalCurrency);
		q.addValue("currencyExchangeRateToUsd", currencyExchangeRateToUsd);
		q.addValue("currencyExchangeRateToUsd", currencyExchangeRateToUsd);
		q.addValue("revenueInUsd", revenueInUsd);
		q.addValue("rate", retainmentRate);
		q.addValue("supplierKcode", supplierKcode);
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
	}

	@Override
	public BigDecimal queryRate(Date start, Date end, int countryId, String supplierKcode) {
		String sql = "select rr.rate "
				+ "from retainment_rate rr "
				+ "inner join company com on com.id = rr.supplier_company_id "
				+ "where rr.date_start = :start "
				+ "and rr.date_end = :end "
				+ "and rr.country_id = :countryId "
				+ "and com.k_code = :supplierKcode  ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("start", start);
		q.addValue("end", end);
		q.addValue("countryId", countryId);
		q.addValue("supplierKcode", supplierKcode);
		BigDecimal result = getNamedParameterJdbcTemplate().queryForObject(sql,q,BigDecimal.class);
		Assert.notNull(result);;
		return result;
	}

	@Override
	public BigDecimal queryRevenueInOriginalCurrency(Date start, Date end, int countryId, String supplierKcode) {
		String sql = "select rr.revenue_in_original_currency "
				+ "from retainment_rate rr "
				+ "inner join company com on com.id = rr.supplier_company_id "
				+ "where rr.date_start = :start "
				+ "and rr.date_end = :end "
				+ "and rr.country_id = :countryId "
				+ "and com.k_code = :supplierKcode  ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("start", start);
		q.addValue("end", end);
		q.addValue("countryId", countryId);
		q.addValue("supplierKcode", supplierKcode);
		BigDecimal result = getNamedParameterJdbcTemplate().queryForObject(sql,q,BigDecimal.class);
		Assert.notNull(result);;
		return result;
	}

	@Override @Transactional("transactionManager")
	public int delete(int rateId) {
		String sql = "delete from retainment_rate where id = :rateId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("rateId", rateId);
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
		return rateId;
	}
	
	@Override
	public Date queryPeriodStart(int settlementPeriodId) {
		String sql = "select period_start from settlement_period where id = :settlementPeriodId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("settlementPeriodId", settlementPeriodId);
		return getNamedParameterJdbcTemplate().queryForObject(sql,q,Date.class);
	}

	@Override
	public Date queryPeriodEnd(int settlementPeriodId) {
		String sql = "select period_end from settlement_period where id = :settlementPeriodId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("settlementPeriodId", settlementPeriodId);
		return getNamedParameterJdbcTemplate().queryForObject(sql,q,Date.class);
	}

	@Override
	public Date queryLatestRetainmentDateEnd() {
		String sql = "select max(rr.date_end) from retainment_rate rr ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		return getNamedParameterJdbcTemplate().queryForObject(sql,q,Date.class);
	}

	@Override
	public boolean existRetainmentRate(Date startDateTimePoint, Date endDateTimePoint) {
		StringBuilder sqlSb = new StringBuilder()
				.append("select exists( ")
				.append("    select 1 from retainment_rate r ")
				.append("    where true ")
				.append("    and r.date_start >= :startDateTimePoint ")
				.append("    and r.date_end   <= :endDateTimePoint ")
				.append(")");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("startDateTimePoint", startDateTimePoint);
		q.addValue("endDateTimePoint", endDateTimePoint);
		boolean result = getNamedParameterJdbcTemplate().queryForObject(sqlSb.toString(),q,boolean.class );
		Assert.notNull(result);
		return (boolean)result;
	}


}
