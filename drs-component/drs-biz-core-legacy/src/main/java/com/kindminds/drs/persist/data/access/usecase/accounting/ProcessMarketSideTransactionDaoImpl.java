package com.kindminds.drs.persist.data.access.usecase.accounting;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


import com.kindminds.drs.api.v2.biz.domain.model.accounting.MarketSideTransaction;
import com.kindminds.drs.api.v2.biz.domain.model.settlement.MarketTransaction;
import com.kindminds.drs.persist.data.access.rdb.Dao;

import com.kindminds.drs.api.v1.model.amazon.AmazonReimbursementInfo;
import com.kindminds.drs.enums.Settlement;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import com.kindminds.drs.Country;
import com.kindminds.drs.Currency;
import com.kindminds.drs.Marketplace;

import com.kindminds.drs.api.data.access.usecase.accounting.ProcessMarketSideTransactionDao;
import com.kindminds.drs.api.v1.model.amazon.AmazonTransaction;
import com.kindminds.drs.api.v1.model.amazon.AmazonTransaction.AmazonTransactionLineItem;
import com.kindminds.drs.api.v1.model.amazon.AmazonTransaction.AmazonTransactionLineItem.AmzAmountTypeDesc;
import com.kindminds.drs.enums.AmazonTransactionType;

import com.kindminds.drs.persist.v1.model.mapping.amazon.AmazonTransactionImpl;
import com.kindminds.drs.persist.v1.model.mapping.amazon.AmazonTransactionLineItemImpl;




@Repository
public class ProcessMarketSideTransactionDaoImpl extends Dao implements ProcessMarketSideTransactionDao {
	

	@Override @SuppressWarnings("unchecked")
	public List<Object []> querySettlementPeriodList() {
		StringBuilder sqlSb = new StringBuilder()
				.append("select    sp.id as id, ")
				.append("sp.period_start as start, ")
				.append("sp.period_end   as end ")
				.append("from settlement_period sp ")
				.append("order by sp.period_start desc ");

		List<Object[]> columnsList = getJdbcTemplate().query(sqlSb.toString(),objArrayMapper);

		return columnsList;
	}

	@Override @SuppressWarnings("unchecked")
	public List<Object[]> queryFbaTransactions(Date start, Date end, String type,
											   Settlement.AmazonReturnReportDetailedDisposition disposition,
											   Settlement.AmazonReturnReportLineStatus status,
											   String companyCode) {
		String sql = "select distinct on (arr.id) "
				+ "arr.return_date as transaction_date, "
				+ "         m.name as source, "
				+ "   arr.order_id as source_id, "
				+ " ps.code_by_drs as sku "
				+ "from amazon_return_report arr  "
				+ "inner join amazon_settlement_report_v2 asr on asr.order_id = arr.order_id "
				+ "inner join marketplace m on m.id = asr.source_marketplace_id "
				+ "inner join product_marketplace_info pmi on (pmi.marketplace_id = m.id and arr.sku = pmi.marketplace_sku) "
				+ "inner join product_sku ps on ps.product_id = pmi.product_id " +
				" inner join product_base pb on ps.product_base_id  = pb.id " +
				" inner join company c on c.id = pb.supplier_company_id "
				+ "where arr.detailed_disposition = :disposition "
				+ "and arr.return_date >= :start "
				+ "and arr.return_date < :end "
				+ "and c.k_code = :kcode "
				+ "order by arr.id ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("start",start);
		q.addValue("end", end);
		q.addValue("disposition", disposition.getValue());
		q.addValue("kcode", companyCode);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);

		return columnsList;
	}



	@Override @SuppressWarnings("unchecked")
	public List<Object[]> queryAmazonOrdersRefunds(Date start, Date end,
												   List<String> ignoreMerchantOrderIdPrefixs,
												   String companyCode) {
		String sql = "select distinct "
				+ "r.posted_date_time as transaction_date, "
				+ "r.transaction_type as type, "
				+ "            m.name as source, "
				+ "          order_id as order_id, "
				+ "    ps.code_by_drs as sku "
				+ "from amazon_settlement_report_v2 r "
				+ "inner join marketplace m on m.id = r.source_marketplace_id "
				+ "inner join product_marketplace_info pmi on (pmi.marketplace_sku=r.sku and pmi.marketplace_id=m.id) "
				+ "inner join product_sku ps on ps.product_id = pmi.product_id " +
				" inner join product_base pb on ps.product_base_id  = pb.id " +
				" inner join company c on c.id = pb.supplier_company_id "
				+ "where r.sku is not null "
				+ "and r.transaction_type in (:order,:refund) "
				+ "and r.posted_date_time >= :start "
				+ "and r.posted_date_time <  :end "
				+ "and r.amount_description not in ('CS_ERROR_ITEMS' , 'Goodwill' ) "
				+ "and c.k_code = :kcode "
				+ this.generateWhereConditionForIgnoring(ignoreMerchantOrderIdPrefixs)
				+ "order by r.posted_date_time ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("order","Order");
		q.addValue("refund",AmazonTransactionType.REFUND.getValue());
		q.addValue("start",start);
		q.addValue("end", end);
		q.addValue("kcode", companyCode);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);

		return columnsList;
	}
	
	@Override @SuppressWarnings("unchecked")
	public List<Object[]> queryAmazonOtherTransactions(Date start, Date end,
													   List<String> ignoreMerchantOrderIdPrefixs,
													   String companyCode) {
		String sql = "select distinct "
				+ "  r.posted_date_time as transaction_date, "
				+ "  r.transaction_type as type, "
				+ "              m.name as source, "
				+ "            order_id as order_id, "
				+ "      ps.code_by_drs as sku, "
				+ "r.amount_description as description "
				+ "from amazon_settlement_report_v2 r "
				+ "inner join marketplace m on m.id = r.source_marketplace_id "
				+ "inner join product_marketplace_info pmi on (pmi.marketplace_sku=r.sku and pmi.marketplace_id=m.id) "
				+ "inner join product_sku ps on ps.product_id = pmi.product_id " +
				" inner join product_base pb on ps.product_base_id  = pb.id " +
				" inner join company c on c.id = pb.supplier_company_id "
				+ "where r.sku is not null "
				+ "and r.transaction_type = :other "
				+ "and r.posted_date_time >= :start "
				+ "and r.posted_date_time <  :end "
				+ "and c.k_code = :kcode "
				+ "and r.amount_description not in ('CS_ERROR_ITEMS' , 'Goodwill' )"
				+ this.generateWhereConditionForIgnoring(ignoreMerchantOrderIdPrefixs)
				+ "order by r.posted_date_time ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("other","other-transaction");
		q.addValue("start",start);
		q.addValue("end", end);
		q.addValue("kcode", companyCode);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);

		return columnsList;
	}
	
	private String generateWhereConditionForIgnoring(List<String> ignoreMerchantOrderIdPrefixs){
		StringBuilder sb = new StringBuilder();
		for(String prefix:ignoreMerchantOrderIdPrefixs){
			sb.append("and (r.merchant_order_id not like '").append(prefix).append("%' or r.merchant_order_id is NULL ) ");
		}
		return sb.toString();
	}

	@Override @SuppressWarnings("unchecked")
	public List<Object[]> queryEbayTransactionInfos(Date start, Date end, String type,
													String companyCode) {
		String sql = "select "
				+ "et.transaction_date as transaction_date, "
				+ "             m.name as source, "
				+ "        et.order_id as source_id, "
				+ "         et.drs_sku as sku "
				+ "from ebay_transaction et "
				+ "inner join marketplace m on m.id=et.marketplace_id "
				+ "where true "
				+ this.generatedrsSkuCompanyCondition(companyCode)
				+ " and transaction_date >= :start "
				+ " and transaction_date <  :end ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("start",start);
		q.addValue("end", end);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);

		return columnsList;
	}

	private String generatedrsSkuCompanyCondition(String companyCode) {
		if (companyCode != null) {
			return "AND drs_sku like '%" + companyCode + "%'";
		}
		return "";
	}

	@Override @SuppressWarnings("unchecked")
	public List<Object[]> queryShopifyOrderIdToTransactionDateMap(Marketplace marketplace,String type,
																  Date startPoint, Date endPoint,
																  String companyCode) {

		Assert.isTrue(marketplace.isShopifyMarketplace(),"marketplace.isShopifyMarketplace()");
		String sql = "select "
				+ "sptr.transaction_date as transaction_date, "
				+ "      sptr.order_name as source_id, "
				+ "       ps.code_by_drs as sku "
				+ "from shopify_payment_transaction_report sptr "
				+ "inner join shopify_order_report sor on sor.name = sptr.order_name "
				+ "inner join product_marketplace_info pmi on (pmi.marketplace_sku=sor.lineitem_sku and pmi.marketplace_id=:truetosourceId) "
				+ "inner join product_sku ps on ps.product_id = pmi.product_id " +
				" inner join product_base pb on ps.product_base_id  = pb.id " +
				" inner join company c on c.id = pb.supplier_company_id "
				+ "where type = :type "
				+ "and sptr.transaction_date >= :startPoint "
				+ "and sptr.transaction_date  < :endPoint "
				+ "and c.k_code = :kcode "
				+ "and pmi.marketplace_id = :truetosourceId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("startPoint",startPoint);
		q.addValue("endPoint",endPoint);
		q.addValue("type","charge");
		q.addValue("truetosourceId",marketplace.getKey());
		q.addValue("kcode", companyCode);

		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);

		return columnsList;
	}


	@Override @SuppressWarnings("unchecked")
	public List<Object[]> queryFbaReturnToSupplierTransactions(Date start,Date end,
															   String companyCode){
		String sql = "select "
				+ "      frt.drs_process_date as transaction_date, "
				+ "frt.source_fba_marketplace as source, "
				+ "               frt.drs_sku as sku, "
				+ "			frt.sellback_type as sellback_type "
				+ "from fba_return_to_supplier frt "
				+ "inner join marketplace m on m.name = frt.source_fba_marketplace "
				+ "where true "
				+ "and drs_process_date >= :start "
				+ "and drs_process_date <  :end "
				+ this.generatedrsSkuCompanyCondition(companyCode)
				+ " order by transaction_date, m.id, sku ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("start",start);
		q.addValue("end", end);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);

		return columnsList;
	}

	@Override @SuppressWarnings("unchecked")
	public List<Object []> queryNonProcessedTransactionList(String companyCode) {
		String sql = "select     t.id as id, "
				+ "t.transaction_date as transaction_date, "
				+ "            t.type as type, "
				+ "          t.source as source, "
				+ "       t.source_id as source_id, "
				+ "             t.sku as sku, "
				+ "     t.description as description "
				+ ", te.exception_message, "
				+ " te.exception_stacktrace "
				+ " from market_side_transaction t "
				+ " left join market_side_transaction_exception te on t.id = te.market_side_transaction_id "
				+ " where is_processed = false "
				 + generateSkuCompanyCondition(companyCode)
				+ " order by t.transaction_date ";

		MapSqlParameterSource q = new MapSqlParameterSource();
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);

		return columnsList;
	}

	//todo arthur
	private String generateSkuCompanyCondition(String companyCode) {
		if (companyCode != null) {
			return " AND sku like '" + companyCode + "%' ";
		}
		return "";
	}

	@Override
	public List<Object []> queryNonProcessedTransactionList(int nonProcessedTransactionId) {

		String sql = "select     t.id as id, "
				+ "t.transaction_date as transaction_date, "
				+ "            t.type as type, "
				+ "          t.source as source, "
				+ "       t.source_id as source_id, "
				+ "             t.sku as sku, "
				+ "     t.description as description , te.exception_message, "
				+ " te.exception_stacktrace "
				+ " from market_side_transaction t "
				+ " left join market_side_transaction_exception te on t.id = te.market_side_transaction_id "
				+ " where is_processed = false and t.id= :id "
				+ " order by t.transaction_date ";

		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("id", nonProcessedTransactionId);

		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);

		return columnsList;
	}

	@Override @Transactional("transactionManager")
	public int insertTransactions(List<MarketSideTransaction> transactions) {

		String sql = "insert into market_side_transaction "
				+ "( transaction_date,  type,  source,  source_id,  sku,  description) values "
				+ "(:transaction_date, :type, :source, :source_id, :sku, :description) ";
		int insertedRows = 0;
		for(MarketSideTransaction t:transactions){

				MapSqlParameterSource q = new MapSqlParameterSource();
				q.addValue("transaction_date",t.getTransactionDate());
				q.addValue("type",t.getType());
				q.addValue("source",t.getSource());
				q.addValue("source_id",t.getSourceId());
				q.addValue("sku",t.getSku());
				q.addValue("description",t.getDescription());
				int insertedRow = getNamedParameterJdbcTemplate().update(sql,q);
				Assert.isTrue(insertedRow==1,"insertedRow==1");
				insertedRows += insertedRow;



		}
		return insertedRows;
	}

	@Override @Transactional("transactionManager")
	public int insertMarketTransactions(List<MarketTransaction> transactions) {
		String sql = "insert into market_side_transaction "
				+ "( transaction_date,  type,  source,  source_id,  sku,  description) values "
				+ "(:transaction_date, :type, :source, :source_id, :sku, :description) ";
		int insertedRows = 0;
		for(MarketTransaction t:transactions){
			MapSqlParameterSource q = new MapSqlParameterSource();
			q.addValue("transaction_date",t.getTransactionDate());
			q.addValue("type",t.getType());
			q.addValue("source",t.getSource());
			q.addValue("source_id",t.getSourceId());
			q.addValue("sku",t.getSku());
			q.addValue("description",t.getDescription());
			int insertedRow = getNamedParameterJdbcTemplate().update(sql,q);
			Assert.isTrue(insertedRow==1,"insertedRow==1");
			insertedRows += insertedRow;
		}
		return insertedRows;
	}

	@Override @Transactional("transactionManager")
	public int deleteNonProcessedTransactions() {
		String sql = "delete from market_side_transaction mst where mst.is_processed = FALSE ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		return  getNamedParameterJdbcTemplate().update(sql,q);
	}

	@Override @SuppressWarnings("unchecked")
	public String queryShipmentIvsName(String sourceTransactionId, String productSku,String type) {
		String sql = "select distinct dt.shipment_ivs_name from drs_transaction dt "
				+ "inner join product_sku ps on ps.id = dt.product_sku_id "
				+ "where dt.source_transaction_id = :sourceTransactionId "
				+ "and dt.type = :orderType "
				+ "and ps.code_by_drs = :productSku ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("sourceTransactionId", sourceTransactionId);
		q.addValue("orderType", type);
		q.addValue("productSku", productSku);
		List<String> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		Assert.isTrue(resultList.size()==1,"resultList.size() is not one, size: " + resultList.size());
		return resultList.get(0);
	}

	@Override @SuppressWarnings("unchecked")
	public String queryShipmentUnsName(String sourceTransactionId,String productSku,String type) {
		String sql = "select distinct dt.shipment_uns_name from drs_transaction dt "
				+ "inner join product_sku ps on ps.id = dt.product_sku_id "
				+ "where dt.source_transaction_id = :sourceTransactionId "
				+ "and dt.type = :orderType "
				+ "and ps.code_by_drs = :productSku ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("sourceTransactionId", sourceTransactionId);
		q.addValue("orderType", type);
		q.addValue("productSku", productSku);

		List<String> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		Assert.isTrue(resultList.size()==1,"resultList.size()==1");
		return resultList.get(0);
	}

	@Override @SuppressWarnings("unchecked")
	public Object[] queryReturnRawColumns(Date returnDate,String sourceTransactionId,String drsSku,String disposition) {
		String sql = "select distinct arr.order_id, ps.code_by_drs, arr.quantity "
				+ "from amazon_return_report arr "
				+ "inner join amazon_settlement_report_v2 asr on asr.order_id = arr.order_id "
				+ "inner join marketplace m on m.name = asr.marketplace_name "
				+ "inner join product_marketplace_info pmi on ( pmi.marketplace_sku=arr.sku and m.id=pmi.marketplace_id ) "
				+ "inner join product p on p.id = pmi.product_id "
				+ "inner join product_sku ps on ps.product_id = p.id "
				+ "where arr.order_id = :sourceTransactionId "
				+ "and arr.return_date = :returnDate "
				+ "and arr.detailed_disposition = :disposition "
				+ "and ps.code_by_drs = :drsSku ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("sourceTransactionId", sourceTransactionId);
		q.addValue("returnDate", returnDate);
		q.addValue("disposition", disposition);
		q.addValue("drsSku", drsSku);
		List<Object[]> resultList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		Assert.isTrue(resultList.size()==1,"resultList.size()==1");
		return resultList.get(0);
	}

	@Override
	public BigInteger queryShipmentLineItemId(String unsName,String ivsName, String productSku) {
		String sql = "SELECT unsi.id from  shipment_line_item unsi \n" +
				" inner join product_sku ps on unsi.sku_id = ps.id\n" +
				" inner join shipment uns on unsi.shipment_id = uns.id\n" +
				" inner join shipment ivs on unsi.source_shipment_id = ivs.id\n" +
				" WHERE ps.code_by_drs = :productSku " +
				" and uns.name = :unsName " +
				" and ivs.name = :ivsName " +
				" order by unsi.id limit 1";

		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("productSku", productSku);
		q.addValue("unsName", unsName);
		q.addValue("ivsName", ivsName);

		return getNamedParameterJdbcTemplate().queryForObject(sql,q,BigInteger.class);
	}

	@Override @Transactional("transactionManager")
	public void incrementReturnQuantityByLineItemId(BigInteger lineItemId, Integer quantity) {
		String sql = "UPDATE shipment_line_item unsi set qty_returned = qty_returned + :quantity "
				+ " WHERE unsi.id = :lineItemId ";

		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("quantity", quantity);
		q.addValue("lineItemId", lineItemId.intValue());

		int affectedRows = getNamedParameterJdbcTemplate().update(sql,q);
		Assert.isTrue(affectedRows==1,"affectedRows==1");

	}

	@Override @Transactional("transactionManager")
	public void incrementReturnQuantity(String unsName,String ivsName, String productSku, Integer quantity) {
		String sql = "update shipment_line_item unsi set qty_returned = qty_returned + :quantity "
				+ "from product_sku ps, shipment uns, shipment ivs "
				+ "where unsi.sku_id = ps.id "
				+ "and unsi.shipment_id = uns.id "
				+ "and unsi.source_shipment_id = ivs.id "
				+ "and ps.code_by_drs = :productSku "
				+ "and uns.name = :unsName "
				+ "and ivs.name = :ivsName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("quantity", quantity);
		q.addValue("productSku", productSku);
		q.addValue("unsName", unsName);
		q.addValue("ivsName", ivsName);
		int affectedRows =  getNamedParameterJdbcTemplate().update(sql,q);
		Assert.isTrue(affectedRows==1,"affectedRows==1");

	}

	@Override @Transactional("transactionManager")
	public void setTransactionProcessed(int transactionId, boolean value) {
		String sql = "update market_side_transaction mst set is_processed = :value where mst.id = :eventId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("eventId", transactionId);
		q.addValue("value", value);
		Assert.isTrue( getNamedParameterJdbcTemplate().update(sql,q)==1,"q.executeUpdate()==1");
	}

	@Override @SuppressWarnings("unchecked")
	public Date queryLatestStatementPeriodEndDateUtc() {
		String sql = "select max(period_end) from bill_statement bs";
		List<Date> resultList = getJdbcTemplate().queryForList(sql,Date.class);
		Assert.isTrue(resultList.size()==1,"resultList.size()==1");
		return resultList.get(0);
	}

	@Override @SuppressWarnings("unchecked")
	public String querySupplierKcode(String productSku) {
		String sql = "select distinct com.k_code from product_sku ps "
				+ "inner join product_base pb on pb.id = ps.product_base_id "
				+ "inner join company com on com.id = pb.supplier_company_id "
				+ "where ps.code_by_drs = :productSku and com.is_supplier = TRUE ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("productSku", productSku);
		List<String> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		Assert.isTrue(resultList.size()==1,"resultList.size()==1");
		return resultList.get(0);
	}

	@Override @SuppressWarnings("unchecked")
	public String queryHandlerDrsKcode(String supplierKcode) {
		String sql = "select hdlr.k_code from company hdlr "
				+ "inner join company splr on splr.handler_company_id = hdlr.id "
				+ "where splr.k_code = :supplierKcode ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("supplierKcode", supplierKcode);
		List<String> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		Assert.isTrue(resultList.size()==1,"resultList.size()==1");
		return resultList.get(0);
	}

	@Override @SuppressWarnings("unchecked")
	public String queryMsdcKcode(Country country) {
		String sql = "select distinct msdc.k_code from company msdc where msdc.country_id = :countryId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("countryId",country.getKey());
		List<String> resultList =  getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		Assert.isTrue(resultList.size()==1,"resultList.size()==1");
		return resultList.get(0);
	}

	@Override @SuppressWarnings("unchecked")
	public Currency queryShipmentCurrency(String shipmentName) {
		String sql = "select shp.currency_id from shipment shp where shp.name = :shipmentName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("shipmentName",shipmentName);
		List<Integer> resultList =  getNamedParameterJdbcTemplate().queryForList(sql,q,Integer.class);
		Assert.isTrue(resultList.size()==1,"resultList.size()==1");
		return Currency.fromKey(resultList.get(0));
	}
	
	private List<String> availableTransactionTypes = Arrays.asList(
			AmazonTransactionType.ORDER.getValue(),
			AmazonTransactionType.REFUND.getValue(),
			AmazonTransactionType.OTHER.getValue());

	@Override 
	public boolean settlementIdExist(String settlementId) {
		String sql = "select exists(select 1 from amazon_settlement_report_v2 where settlement_id = :settlementId )";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("settlementId", settlementId);
		Boolean o = getNamedParameterJdbcTemplate().queryForObject(sql,q,Boolean.class);
		Assert.notNull(o,"object null");
		return o;
		
	}

	@Override
	public BigDecimal queryFeeFromAmazonSettlementReport(String orderId, String settlementId, Currency currency,AmzAmountTypeDesc typeDesc) {
		StringBuilder sqlSb = new StringBuilder()
				.append("select sum(amount) ")
				.append("from amazon_settlement_report_v2 ")
				.append("where transaction_type = :transactionType ")
				.append("and order_id = :orderId ")
				.append("and settlement_id = :settlementId ")
				.append("and currency = :currency ")
				.append("and amount_type = :amountType ")
				.append("and amount_description = :amountDesc ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("transactionType", AmazonTransactionType.ORDER.getValue());
		q.addValue("orderId", orderId);
		q.addValue("settlementId",settlementId);
		q.addValue("currency", currency.name());
		q.addValue("amountType", typeDesc.getType());
		q.addValue("amountDesc", typeDesc.getDesc());
		BigDecimal result = getNamedParameterJdbcTemplate().queryForObject(sqlSb.toString(),q,BigDecimal.class);
		if(result!=null) Assert.isTrue(result.compareTo(BigDecimal.ZERO) < 0,"result.compareTo(BigDecimal.ZERO) < 0");
		return result;
	}

	@Override @SuppressWarnings("unchecked")
	public BigDecimal queryAbsShopifyShipmentFeeTransportationFromAmazonSettlementReport(String orderId) {
		StringBuilder sqlSb = new StringBuilder()
				.append("select asr.settlement_id, sum(asr.amount) ")
				.append("from amazon_settlement_report_v2 asr ")
				.append("where transaction_type = :transactionType ")
				.append("and merchant_order_id like :orderId ")
				.append("and amount_type = :amountType ")
				.append("and amount_description = :amountDesc ")
				.append("group by asr.settlement_id ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("transactionType", AmazonTransactionType.ORDER.getValue());
		q.addValue("orderId", this.attachDbWildcardToBeginAndEnd(orderId));
		AmzAmountTypeDesc typeDesc = AmzAmountTypeDesc.SHIPMENTFEES_TRANSPORTATION;
		q.addValue("amountType", typeDesc.getType());
		q.addValue("amountDesc", typeDesc.getDesc());
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,objArrayMapper);
		Assert.isTrue(columnsList.size()<=1,"Cross settltment error.");
		if(columnsList.size()==0) return null;
		Object[] firstColumns = columnsList.get(0);
		return ((BigDecimal)firstColumns[1]).abs();
	}
	
	private String attachDbWildcardToBeginAndEnd(String str){ return str+"%"; }

	@Override @SuppressWarnings("unchecked")
	public BigDecimal queryAbsShopifyPerOrderFeeFromAmazonSettlementReport(String orderId) {
		StringBuilder sqlSb = new StringBuilder()
				.append("select asr.settlement_id, sum(asr.amount) ")
				.append("from amazon_settlement_report_v2 asr ")
				.append("where transaction_type = :transactionType ")
				.append("and merchant_order_id like :orderId ")
				.append("and amount_type = :amountType ")
				.append("and amount_description = :amountDesc ")
				.append("group by asr.settlement_id ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("transactionType", AmazonTransactionType.ORDER.getValue());
		q.addValue("orderId", this.attachDbWildcardToBeginAndEnd(orderId));
		AmzAmountTypeDesc typeDesc = AmzAmountTypeDesc.FEE_PERORDER;
		q.addValue("amountType",typeDesc.getType());
		q.addValue("amountDesc",typeDesc.getDesc());
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,objArrayMapper);
		Assert.isTrue(columnsList.size()<=1,"Cross settltment error.");
		if(columnsList.size()==0) return null;
		Object[] firstColumns = columnsList.get(0);
		return ((BigDecimal)firstColumns[1]).abs();
	}
	
	@Override @SuppressWarnings("unchecked")
	public BigDecimal queryShopifyPerUnitFeeFromAmazonSettlementReport(Integer marketplaceId,String drsSku,String orderId) {
		StringBuilder sqlSb = new StringBuilder()
				.append("select asr.settlement_id, sum(asr.amount) ")
				.append("from amazon_settlement_report_v2 asr ")
				.append("inner join product_marketplace_info pmi on pmi.marketplace_sku=asr.sku ")
				.append("inner join product_sku ps on ps.product_id=pmi.product_id ")
				.append("where transaction_type = :transactionType ")
				.append("and pmi.marketplace_id=:marketplaceId ")
				.append("and ps.code_by_drs=:drsSku ")
				.append("and merchant_order_id like :orderId ")
				.append("and amount_type = :amountType ")
				.append("and amount_description = :amountDesc ")
				.append("group by asr.settlement_id ");
		 MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("transactionType",AmazonTransactionType.ORDER.getValue());
		q.addValue("marketplaceId",marketplaceId);
		q.addValue("drsSku",drsSku);
		q.addValue("orderId", this.attachDbWildcardToBeginAndEnd(orderId));
		AmzAmountTypeDesc typeDesc = AmzAmountTypeDesc.FEE_PERUNIT;
		q.addValue("amountType",typeDesc.getType());
		q.addValue("amountDesc",typeDesc.getDesc());
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,objArrayMapper);
		//Assert.isTrue(columnsList.size()==1);
		if(columnsList.size()==0) return BigDecimal.ZERO;
		Object[] firstColumns = columnsList.get(0);
		return (BigDecimal)firstColumns[1];
	}

	@Override
	public Integer queryPerUnitFeeQuantityFromAmazonSettlementReportForShopify(String merchantOrderId, Currency currency, String sku) {
		String sql = "select sum(quantity_purchased) from amazon_settlement_report_v2 "
				+ "where transaction_type = :transactionType "
				+ "and merchant_order_id like :merchantOrderId "
				+ "and currency = :currency "
				+ "and sku = :sku "
				+ "and amount_type = :amountType "
				+ "and amount_description = :amountDesc ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("transactionType", AmazonTransactionType.ORDER.getValue());
		q.addValue("merchantOrderId", this.attachDbWildcardToBeginAndEnd(merchantOrderId));
		q.addValue("currency", currency.name());
		q.addValue("sku", sku);
		AmzAmountTypeDesc typeDesc = AmzAmountTypeDesc.FEE_PERUNIT;
		q.addValue("amountType",typeDesc.getType());
		q.addValue("amountDesc",typeDesc.getDesc());
		BigInteger o = getNamedParameterJdbcTemplate().queryForObject(sql,q,BigInteger.class);
		if (o == null) return 0;
		return o.intValue();
	}
	
	@Override
	public BigDecimal queryPerUnitFeeAmountFromAmazonSettlementReportForShopify(String merchantOrderId, Currency currency, String sku) {
		String sql = "select sum(amount) from amazon_settlement_report_v2 "
				+ "where transaction_type = :transactionType "
				+ "and merchant_order_id like :merchantOrderId "
				+ "and currency = :currency "
				+ "and sku = :sku "
				+ "and amount_type = :amountType "
				+ "and amount_description = :amountDesc ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("transactionType", AmazonTransactionType.ORDER.getValue());
		q.addValue("merchantOrderId", this.attachDbWildcardToBeginAndEnd(merchantOrderId));
		q.addValue("currency", currency.name());
		q.addValue("sku", sku);
		AmzAmountTypeDesc typeDesc = AmzAmountTypeDesc.FEE_PERUNIT;
		q.addValue("amountType",typeDesc.getType());
		q.addValue("amountDesc",typeDesc.getDesc());
		BigDecimal sum = getNamedParameterJdbcTemplate().queryForObject(sql,q,BigDecimal.class);
		if(sum==null) return null;
		return sum;
	}
	
	@Override @SuppressWarnings("unchecked")
	public BigDecimal queryAmazonPricePrincipalAmountSum(Marketplace marketplace,String orderId, String drsSku) {
		String sql = "select sum(asr.amount) "
				+ "from product_marketplace_info pmi "
				+ "inner join product_sku ps on ps.product_id=pmi.product_id "
				+ "inner join amazon_settlement_report_v2 asr on asr.sku=pmi.marketplace_sku "
				+ "where pmi.marketplace_id=:marketplace_id "
				+ "and asr.order_id=:order_id "
				+ "and asr.transaction_type='Order' "
				+ "and asr.amount_type='ItemPrice' "
				+ "and asr.amount_description='Principal' "
				+ "and ps.code_by_drs= :skuCodeByDrs ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("marketplace_id",marketplace.getKey());
		q.addValue("order_id",orderId);
		q.addValue("skuCodeByDrs", drsSku);
		List<BigDecimal> results = getNamedParameterJdbcTemplate().queryForList(sql,q,BigDecimal.class);
		Assert.isTrue(results.size()==1,"results.size()==1");
		Assert.notNull(results.get(0),"results.get(0) null");
		return results.get(0);
	}
	
	@Override @SuppressWarnings("unchecked")
	public BigDecimal queryAmazonPricePrincipalQuantitySum(Marketplace marketplace,String orderId, String drsSku) {
		String sql = "select sum(asr.quantity_purchased) "
				+ "from product_marketplace_info pmi "
				+ "inner join product_sku ps on ps.product_id=pmi.product_id "
				+ "inner join amazon_settlement_report_v2 asr on asr.sku=pmi.marketplace_sku "
				+ "where pmi.marketplace_id=:marketplace_id "
				+ "and asr.order_id=:order_id "
				+ "and asr.transaction_type='Order' "
				+ "and asr.amount_type='ItemPrice' "
				+ "and asr.amount_description='Principal' "
				+ "and ps.code_by_drs= :skuCodeByDrs ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("marketplace_id",marketplace.getKey());
		q.addValue("order_id",orderId);
		q.addValue("skuCodeByDrs", drsSku);
		List<BigInteger> results = getNamedParameterJdbcTemplate().queryForList(sql,q,BigInteger.class);
		Assert.isTrue(results.size()==1,"results.size()==1");
		Assert.notNull(results.get(0),"results.get(0) null");
		return new BigDecimal(results.get(0));
	}

	@Override @SuppressWarnings("unchecked")
	public String queryCompanyKcodeBySku(String skuCode) {
		String sql = "select company.k_code "
				+ "from product_sku ps "
				+ "inner join product_base pb on pb.id = ps.product_base_id "
				+ "inner join company on company.id = pb.supplier_company_id "
				+ "where ps.code_by_drs = :skuCode ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("skuCode", skuCode);
		List<String> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		Assert.isTrue(resultList.size()==1,"resultList.size()==1");
		return resultList.get(0);
	}
	
	@Override @SuppressWarnings("unchecked")
	public String queryDrsCompanyKcodeByCountry(String countryCode) {
		if (countryCode.equals("MX")) {
			return "K3";
		}
		String sql = "select k_code from company "
				+ "inner join country on country.id = company.country_id "
				+ "where country.code = :countryCode "
				+ "and company.is_drs_company = TRUE ";

		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("countryCode", countryCode);
		List<String> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		Assert.isTrue(resultList.size()==1,"resultList.size()==1");
		return resultList.get(0);
	}
	
	@Override @SuppressWarnings("unchecked")
	public List<Object []> queryAmazonSettlementReportTransactionInfo(
			Date postedDateTime,String transactionType,int sourceMarketplaceId,String orderId,
			String amountType,String amountDescription,String sku){
		StringBuilder sqlSb = new StringBuilder()
				.append("select    r.currency as currency, ")
				.append("       r.amount_type as amount_type, ")
				.append("r.amount_description as amount_description, ")
				.append("            r.amount as amount, ")
				.append("r.quantity_purchased as quantity_purchased," +
						"		r.posted_date as posted_date ")
				.append("from amazon_settlement_report_v2 r ")
				.append("left join product_marketplace_info pmi on (pmi.marketplace_sku=r.sku and pmi.marketplace_id=r.source_marketplace_id) ")
				.append("left join product_sku ps on ps.product_id = pmi.product_id ")
				.append("where true ")
				.append("and r.posted_date_time = :postedDateTime ")
				.append("and r.transaction_type = :transactionType ")
				.append("and r.source_marketplace_id = :sourceMarketplaceId ")
				.append(this.composeQueryAmazonSettlementReportTransactionInfoWhereCondition(orderId, amountType, amountDescription, sku));
		MapSqlParameterSource q = new MapSqlParameterSource();

		q.addValue("postedDateTime",postedDateTime);
		q.addValue("transactionType",transactionType);
		q.addValue("sourceMarketplaceId",sourceMarketplaceId);

		System.out.println(sqlSb.toString());

		this.setAmazonSettlementReportTransactionInfoWhereConditionParameter(q, orderId, amountType, amountDescription, sku);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,objArrayMapper);
		return columnsList;
	}
	
	private String composeQueryAmazonSettlementReportTransactionInfoWhereCondition(String orderId,String amountType,String amountDescription,String sku){
		StringBuilder sb = new StringBuilder("");
		if(orderId!=null) sb.append("and r.order_id = :orderId ");
		if(amountType!=null) sb.append("and r.amount_type = :amountType ");
		if(amountDescription!=null) sb.append("and r.amount_description = :amountDescription ");
		if(sku!=null) sb.append("and ps.code_by_drs = :sku ");
		return sb.toString();
	}
	
	private void setAmazonSettlementReportTransactionInfoWhereConditionParameter(MapSqlParameterSource q,String orderId,String amountType,String amountDescription,String sku){
		if(orderId!=null) {
//		    System.out.println("orderId: " + orderId);
		    q.addValue("orderId",orderId);
        }
		if(amountType!=null) {
//            System.out.println("amountType: " + amountType);
		    q.addValue("amountType",amountType);
        }
		if(amountDescription!=null) {
//            System.out.println("amountDescription: " + amountDescription);
		    q.addValue("amountDescription",amountDescription);
        }
		if(sku!=null) {
//            System.out.println("sku: " + sku);
		    q.addValue("sku",sku);
        }
	}
	
	@Override @SuppressWarnings("unchecked")
	public List<AmazonTransaction> querySpecificTrans(String settlementId, Date postedDateTime, String orderId) {
		StringBuilder sqlSb = new StringBuilder()
				.append("select distinct on (order_id, transaction_type, posted_date_time) ")
				.append("               id as id, ")
				.append("         order_id as order_id, ")	
				.append("merchant_order_id as merchant_order_id, ")
				.append(" transaction_type as transaction_type, ")
				.append(" marketplace_name as marketplace_name,")
				.append(" posted_date_time as posted_date_time ")
				.append("from amazon_settlement_report_v2 ")
				.append("where settlement_id = :settlementId ")
				.append("and transaction_type in (:availableTransactionTypes) ")
				.append("and posted_date_time = :postedDateTime ");
		if(orderId!=null) sqlSb.append("and order_id = :orderId ");
		sqlSb.append("order by posted_date_time ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("availableTransactionTypes", this.availableTransactionTypes);
		q.addValue("settlementId", settlementId);
		q.addValue("postedDateTime", postedDateTime);
		if(orderId!=null) q.addValue("orderId", orderId);
		List<AmazonTransactionImpl> trans = getNamedParameterJdbcTemplate().queryForList(sqlSb.toString(),q,AmazonTransactionImpl.class);
		Assert.isTrue(trans.size()==1,"trans.size()==1");
		return (List<AmazonTransaction>)((List<?>)trans);
	}

	@Override @SuppressWarnings("unchecked")
	public List<AmazonTransactionLineItem> queryTransactionLineItems(String settlementId, AmazonTransaction tran, List<String> excludeAmzAmountDescList) {
		StringBuilder sqlSb = new StringBuilder()
				.append("select id, sku, currency, amount_type, amount_description, amount, posted_date_time, quantity_purchased ")
				.append("from amazon_settlement_report_v2 ")
				.append("where settlement_id = :settlementId ")
				.append("and transaction_type = :type ")
				.append("and posted_date_time = :time ");
		if(tran.getOrderId()!=null) sqlSb.append("and order_id=:orderId ");
		if(excludeAmzAmountDescList!=null) sqlSb.append("and amount_description not in (:excludeDescList) ");
		sqlSb.append("order by sku ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("settlementId", settlementId);
		q.addValue("time", tran.getPostedDateTime());
		q.addValue("type", tran.getType().getValue());
		if(tran.getOrderId()!=null) q.addValue("orderId", tran.getOrderId());
		if(excludeAmzAmountDescList!=null) q.addValue("excludeDescList", excludeAmzAmountDescList);
		return (List) getNamedParameterJdbcTemplate().queryForList(sqlSb.toString(),q,AmazonTransactionLineItemImpl.class);
	}
	
	@Override
	public int querySourceMarketplaceKey(String settlementId){
		String sql = "select distinct source_marketplace_id from amazon_settlement_report_v2 where settlement_id = :settlementId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("settlementId", settlementId);
		return getNamedParameterJdbcTemplate().queryForObject(sql,q,Integer.class);
	}

	@Override @SuppressWarnings("unchecked")
	public String querySettlementId(Date postedDateTime) {
		String sql = "select distinct settlement_id from amazon_settlement_report_v2 where posted_date_time = :postedDateTime  ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("postedDateTime", postedDateTime);
		List<String> settlementIdList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		Assert.isTrue(settlementIdList.size()==1,"settlementIdList.size()==1");
		return settlementIdList.get(0);
	}
	
	@Override @SuppressWarnings("unchecked")
	public BigDecimal queryRetainmentRate(Date start, Date end, Country country, String supplierKcode) {
		String sql = "select rr.rate from retainment_rate rr "
				+ "inner join company splr on splr.id = rr.supplier_company_id "
				+ "where splr.k_code = :supplierKcode "
				+ "and rr.country_id = :countryKey "
				+ "and rr.date_start = :start "
				+ "and rr.date_end = :end ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("supplierKcode", supplierKcode);
		q.addValue("countryKey", country.getKey());
		q.addValue("start", start);
		q.addValue("end", end);
		List<BigDecimal> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,BigDecimal.class);
//		Assert.isTrue(resultList.size()==1,"resultList.size()==1");
		if (resultList.isEmpty()) {
			return BigDecimal.valueOf(0.15);
		}
		return resultList.get(0);
	}

	@Override
	public BigDecimal queryProductCategoryTrueToSourceFeeRate(String sku) {
		String sql = "select pc.truetosource_fee_rate from product_category pc "
				+ "inner join product_base pb on pb.product_category_id = pc.id "
				+ "inner join product_sku ps on ps.product_base_id = pb.id  "
				+ "where ps.code_by_drs = :sku ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("sku", sku);
		BigDecimal result = getNamedParameterJdbcTemplate().queryForObject(sql,q,BigDecimal.class);
		Assert.notNull(result,"result null");
		return (BigDecimal)result;
	}

	@Override @SuppressWarnings("unchecked")
	public String queryEbayDrsSku(String sourceId) {
		String sql = "select e.drs_sku from ebay_transaction e where e.order_id=:sourceId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("sourceId", sourceId);
		List<String> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		//Assert.isTrue(resultList.size()==1);
		Assert.notNull(resultList.get(0),"resultList.get(0) null");
		return resultList.get(0);
	}

	@Override @SuppressWarnings("unchecked")
	public Integer queryEbayMarketplaceId(String sourceId) {
		String sql = "select e.marketplace_id from ebay_transaction e where e.order_id=:sourceId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("sourceId", sourceId);
		List<Integer> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,Integer.class);
		//Assert.isTrue(resultList.size()==1);
		Assert.notNull(resultList.get(0),"resultList.get(0) null");
		return resultList.get(0);
	}

	@Override @SuppressWarnings("unchecked")
	public BigDecimal queryEbayPretaxPrice(String sourceId, String drsSku) {
		String sql = "SELECT e.pretax_price FROM ebay_transaction e " +
				" WHERE e.order_id=:sourceId " +
				" AND drs_sku = :drsSku ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("sourceId", sourceId);
		q.addValue("drsSku", drsSku);
		List<BigDecimal> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,BigDecimal.class);
		//Assert.isTrue(resultList.size()==1);
		Assert.notNull(resultList.get(0),"resultList.get(0) null");
		return resultList.get(0);
	}

	@Override @SuppressWarnings("unchecked")
	public BigDecimal queryEbayMarketplaceFee(String sourceId, String drsSku) {
		String sql = "SELECT e.marketplace_fee FROM ebay_transaction e " +
				" WHERE e.order_id=:sourceId " +
				" AND drs_sku = :drsSku ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("sourceId", sourceId);
		q.addValue("drsSku", drsSku);
		List<BigDecimal> resultList =  getNamedParameterJdbcTemplate().queryForList(sql,q,BigDecimal.class);
		//Assert.isTrue(resultList.size()==1);
		Assert.notNull(resultList.get(0),"resultList.get(0) null");
		return resultList.get(0);
	}

	@Override @SuppressWarnings("unchecked")
	public BigDecimal queryEbayFulfillmentFee(String sourceId, String drsSku) {
		String sql = "SELECT e.fulfillment_fee FROM ebay_transaction e " +
				" WHERE e.order_id=:sourceId " +
				" AND drs_sku = :drsSku ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("sourceId", sourceId);
		q.addValue("drsSku", drsSku);
		List<BigDecimal> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,BigDecimal.class);
		//Assert.isTrue(resultList.size()==1);
		Assert.notNull(resultList.get(0),"resultList.get(0) null");
		return resultList.get(0);
	}
	
	@Override @SuppressWarnings("unchecked")
	public List<Integer> queryDrsTransactionIds(String type, String sourceId, String sku) {
		StringBuilder sqlSb = new StringBuilder()
				.append("select dt.id from drs_transaction dt ")
				.append("inner join product_sku ps on ps.id = dt.product_sku_id ") 
				.append("where dt.type = :type ")
				.append("and dt.source_transaction_id = :sourceId ")
				.append("and ps.code_by_drs = :sku ")
				.append("order by dt.id ");
		 MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("type",type);
		q.addValue("sourceId",sourceId);
		q.addValue("sku",sku);
		return  getNamedParameterJdbcTemplate().queryForList(sqlSb.toString(),q,Integer.class);
	}
	
	@Override @SuppressWarnings("unchecked")
	public int queryShopifyOrderQuantity(String orderId,String drsSku) {
		StringBuilder sqlSb = new StringBuilder()
				.append("select sum(sor.lineitem_quantity) ")
				.append("from shopify_order_report sor ")
				.append("inner join product_marketplace_info pmi on pmi.marketplace_sku = sor.lineitem_sku ")
				.append("inner join product_sku ps on ps.product_id = pmi.product_id ")
				.append("where sor.name = :orderId ")
				.append("and ps.code_by_drs = :drsSku ")
				.append("and pmi.marketplace_id = :truetosourceId ");
		 MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("orderId", orderId);
		q.addValue("drsSku", drsSku);
		q.addValue("truetosourceId", Marketplace.TRUETOSOURCE.getKey());
		List<BigInteger> results = getNamedParameterJdbcTemplate().queryForList(sqlSb.toString(),q,BigInteger.class);
		Assert.isTrue(results.size()==1,"results.size()==1");
		return results.get(0).intValue();
	}

	@Override @SuppressWarnings("unchecked")
	public int queryShopifyOrderLineItemQuantity(Integer marketplaceId,String orderId, String drsSku) {
		StringBuilder sqlSb = new StringBuilder()
				.append("select sor.lineitem_quantity) ")
				.append("from shopify_order_report sor ")
				.append("inner join product_marketplace_info pmi on pmi.marketplace_sku=sor.lineitem_sku ")
				.append("inner join product_sku ps on ps.product_id=pmi.product_id ")
				.append("where pmi.marketplace_id=:marketplaceId ")
				.append("and sor.name=:orderId ")
				.append("and ps.code_by_drs=:drsSku ");
		 MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("marketplaceId", marketplaceId);
		q.addValue("orderId", orderId);
		q.addValue("drsSku", orderId);
		List<Integer> results = getNamedParameterJdbcTemplate().queryForList(sqlSb.toString(),q,Integer.class);
		Assert.isTrue(results.size()==1,"results.size()==1");
		return results.get(0);
	}

	@Override @SuppressWarnings("unchecked")
	public Object [] queryShopifyOrderReportOrderInfo(String orderId) {
		StringBuilder sqlSb = new StringBuilder()
				.append("select ")
				.append("sor.name, ")
				.append("sor.currency, ")
				.append("sor.subtotal, ")
				.append("sor.shipping, ")
				.append("sor.taxes, ")
				.append("sor.total, ")
				.append("sor.discount_amount ")
				.append("from shopify_order_report sor ")
				.append("where sor.name = :orderName ")
				.append("and sor.subtotal is not NULL ");
		 MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("orderName", orderId);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,objArrayMapper);
		Assert.isTrue(columnsList.size()==1,"columnsList.size()==1");
		Object[] columns = columnsList.get(0);
		return columns;
	}

	@Override @SuppressWarnings("unchecked")
	public Object [] queryShopifyOrderReportOrderLineInfo(Integer marketplaceId,String orderId,String sku){
		StringBuilder sqlSb = new StringBuilder()
				.append("select ")
				.append("       ps.code_by_drs as sku, ")
				.append("sor.lineitem_quantity as lineitem_quantity, ")
				.append("   sor.lineitem_price as lineitem_price, ")
				.append("  sor.discount_amount as discount_amount ")
				.append("from shopify_order_report sor ")
				.append("inner join product_marketplace_info pmi on (pmi.marketplace_sku=sor.lineitem_sku) ")
				.append("inner join product_sku ps on ps.product_id = pmi.product_id ")
				.append("where pmi.marketplace_id = :marketplaceId ")
				.append("and ps.code_by_drs = :sku ")
				.append("and sor.name = :orderId ");
		 MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("marketplaceId", marketplaceId);
		q.addValue("sku", sku);
		q.addValue("orderId", orderId);
		List<Object[]> results = getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,objArrayMapper);
		Assert.isTrue(results.size()==1,"results.size()==1");
		Object[] columns = results.get(0);
		return columns;
	}

	@Override @SuppressWarnings("unchecked")
	public BigDecimal queryShopifyUnitDiscountAmount(String orderId,String sku) {
		StringBuilder sqlSb = new StringBuilder()
				.append("select ")
				.append("       ssr.order_discount as unit_discount_amount ")
				.append("from shopify_sales_report ssr ")
				.append("inner join product_marketplace_info pmi on (pmi.marketplace_sku=ssr.product_variant_sku and pmi.marketplace_id = (select id from marketplace where name = :marketplaceName)) ")
				.append("inner join product_sku ps on ps.product_id = pmi.product_id ")
				.append("where ps.code_by_drs = :sku ")
				.append("and ssr.order_name = :orderId ")
				.append("and ssr.transaction_type = :transactionType ");
		 MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("sku", sku);
		q.addValue("orderId", orderId);
		q.addValue("transactionType", "product");
		q.addValue("marketplaceName", "TrueToSource");
		List<BigDecimal> resultList = getNamedParameterJdbcTemplate().queryForList(sqlSb.toString(),q,BigDecimal.class);
		Assert.isTrue(resultList.size()==1,"resultList.size()==1");
		Assert.notNull(resultList.get(0),"resultList.get(0) null");
		return resultList.get(0);
	}

	@Override
	public Integer queryFbaReturnToSupplierQuantity(Date transactionDate, String marketplaceName, String drsSku) {
		String sql = "select quantity from fba_return_to_supplier f "
				+ "where f.drs_process_date = :date "
				+ "and f.source_fba_marketplace = :marketplaceName "
				+ "and f.drs_sku = :drsSku ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("date", transactionDate);
		q.addValue("marketplaceName", marketplaceName);
		q.addValue("drsSku", drsSku);
		Integer quantity = getNamedParameterJdbcTemplate().queryForObject(sql.toString(),q,Integer.class);
		Assert.notNull(quantity,"quantity null");
		return quantity;
	}

	@Override
	public String queryFbaReturnToSupplierIvsName(Date transactionDate, String marketplaceName, String drsSku) {
		String sql = "select ivs_name from fba_return_to_supplier f "
				+ "where f.drs_process_date = :date "
				+ "and f.source_fba_marketplace = :marketplaceName "
				+ "and f.drs_sku = :drsSku ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("date", transactionDate);
		q.addValue("marketplaceName", marketplaceName);
		q.addValue("drsSku", drsSku);
		String name = getNamedParameterJdbcTemplate().queryForObject(sql.toString(),q,String.class);
		Assert.notNull(name,"IVS name null");
		return name;
	}

	@Override
	public String queryFbaReturnToSupplierUnsName(Date transactionDate, String marketplaceName, String drsSku) {
		String sql = "select uns_name from fba_return_to_supplier f "
				+ "where f.drs_process_date = :date "
				+ "and f.source_fba_marketplace = :marketplaceName "
				+ "and f.drs_sku = :drsSku ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("date", transactionDate);
		q.addValue("marketplaceName", marketplaceName);
		q.addValue("drsSku", drsSku);
		String name = getNamedParameterJdbcTemplate().queryForObject(sql.toString(),q,String.class);
		Assert.notNull(name,"UNS name null");
		return name;
	}

	@Override @SuppressWarnings("unchecked")
	public List<Object[]> queryFBAIvsUnsListBySku(String sku, String ivsName) {
		String sql = "SELECT distinct ivs.name ivs_name, uns.name uns_name " +
				" FROM shipment ivs  " +
				" INNER JOIN shipment_line_item unsi on unsi.source_shipment_id = ivs.id  " +
				" INNER JOIN shipment uns on uns.id = unsi.shipment_id  " +
				" INNER JOIN country cty ON cty.id = ivs.destination_country_id " +
				" INNER JOIN product_sku ps on unsi.sku_id = ps.id " +
				" WHERE ps.code_by_drs = :sku  " +
				" and ivs.name >= :ivsName  " +
				" order by ivs_name";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("sku", sku);
		q.addValue("ivsName", ivsName);
		return getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
	}

	@Override
	public BigDecimal queryTaxExcludedFcaAmount(String ivsName, String drsSku) {
		String sql = "select distinct shpli.unit_amount "
				+ "from shipment_line_item shpli "
				+ "inner join shipment shp on shp.id = shpli.shipment_id "
				+ "inner join product_sku ps on ps.id = shpli.sku_id "
				+ "where shp.name = :shipmentName "
				+ "and ps.code_by_drs = :productSku ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("shipmentName",ivsName);
		q.addValue("productSku",drsSku);
		BigDecimal amount = getNamedParameterJdbcTemplate().queryForObject(sql,q,BigDecimal.class);
		Assert.notNull(amount,"FCA amount null");
		return amount;
	}

	@Override
	public BigDecimal querySalesTaxRate(String ivsName) {
		String sql = "select s.sales_tax_rate from shipment s where s.name = :shipmentName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("shipmentName",ivsName);
		BigDecimal rate = getNamedParameterJdbcTemplate().queryForObject(sql,q,BigDecimal.class);
		Assert.notNull(rate,"sales tax rate null");
		return rate;
	}

	@Override
	public boolean existMarketSideTransaction(Date startPoint, Date endPoint) {
		StringBuilder sqlSb = new StringBuilder()
				.append("select exists( ")
				.append("    select 1 from market_side_transaction t ")
				.append("    where true ")
				.append("    and t.transaction_date >= :startDateTimePoint ")
				.append("    and t.transaction_date <  :endDateTimePoint ")
				.append(")");
		 MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("startDateTimePoint", startPoint);
		q.addValue("endDateTimePoint", endPoint);
		Boolean result = getNamedParameterJdbcTemplate().queryForObject(sqlSb.toString(),q,Boolean.class);
		Assert.notNull(result,"result null");
		return result;
	}

	@Override @Transactional("transactionManager")
	public void deleteMarketSideTransactionException(int id) {
		String sql = "DELETE FROM market_side_transaction_exception " +
				"WHERE market_side_transaction_id=:id ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("id", id);
		getNamedParameterJdbcTemplate().update(sql,q);
	}

	@Override @Transactional("transactionManager")
	public void insertMarketSideTransactionException(int id, String exceptionMsg, String exceptionStacktrace) {
		String sql = "INSERT INTO market_side_transaction_exception " +
				"(market_side_transaction_id, exception_message, exception_stacktrace) " +
				"VALUES(:id, :exception_message, :exception_stacktrace) ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("id", id);
		q.addValue("exception_message", exceptionMsg);
		q.addValue("exception_stacktrace", exceptionStacktrace);
		getNamedParameterJdbcTemplate().update(sql,q);
	}


	@SuppressWarnings("unchecked")

	public Object [] queryReimbursementInfo(
			MarketSideTransaction tx, BigDecimal amount, Date originalPostedDate) {

		String sql = "SELECT distinct approval_date, reimbursement_id, ar.amazon_order_id, reason, ar.sku, currency_unit, amount_per_unit, amount_total, \n" +
				"\tquantity_reimbursed_cash, quantity_reimbursed_inventory, original_reimbursement_id, original_reimbursement_type " +
				" , quantity_reimbursed_total \n" +
				"\tFROM finance.amazon_reimbursement ar\n" +
				" inner join amazon_settlement_report_v2 asr\n" +
				" ON ar.sku=asr.sku\n" +
				" and ar.currency_unit=asr.currency\n" +
				" and ar.amount_total= asr.amount \n" +
				" and original_posted_date <= ar.approval_date + interval '61 seconds' \n" +
				" and original_posted_date >= ar.approval_date - interval '61 seconds' \n" +
				" where asr.amount_description in ( 'COMPENSATED_CLAWBACK' , 'MISSING_FROM_INBOUND_CLAWBACK' ) " +
				" and ar.sku = :sku\n" +
				" and ar.currency_unit = :currencyUnit\n" +
				" and amount_total = :amountTotal\n" +
				" and asr.original_posted_date = :originalPostedDate";

		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("sku", tx.getSku());
		q.addValue("currencyUnit", Marketplace.fromName(tx.getSource()).getCurrency().name());
		q.addValue("amountTotal", amount);
		q.addValue("originalPostedDate", originalPostedDate);

		System.out.println("OOOOOOOOOOOOOOOOOOOOO");
		System.out.println(tx.getSku());
		System.out.println(Marketplace.fromName(tx.getSource()).getCurrency().name());
		System.out.println(amount);
		System.out.println(originalPostedDate);
		System.out.println("OOOOOOOOOOOOOOOOOOOOO");

		List<Object[]> resultList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		Assert.isTrue(resultList.size() == 1, "resultList size is not 1, size: " + resultList.size() +
				", " + tx.getSku() + ", " + amount + ", " + originalPostedDate + ", " + Marketplace.fromName(tx.getSource()).getCurrency().name());
		Object[] result = resultList.get(0);
		return result;
	}

	@SuppressWarnings("unchecked")
	public Object [] queryOriginalReimbursementInfoById (String origReimbursementId , String sku) {
		String sql = "SELECT approval_date, reimbursement_id, ar.amazon_order_id, reason, ar.sku, currency_unit, amount_per_unit, amount_total, \n" +
				"\tquantity_reimbursed_cash, quantity_reimbursed_inventory, original_reimbursement_id, original_reimbursement_type\n" +
				"\tFROM finance.amazon_reimbursement ar\n" +
				" WHERE reimbursement_id = :reimbursementId " +
				" and sku = :sku " +
				" LIMIT 1";
		MapSqlParameterSource q = new MapSqlParameterSource();
//		System.out.println("reimbursementId: " + origReimbursementId);
		q.addValue("reimbursementId", origReimbursementId);
		q.addValue("sku", sku);
		List<Object[]> resultList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		Assert.isTrue(resultList.size() == 1, "result size is not equal to 1, size: " + resultList.size());
		return resultList.get(0);
	}

	@SuppressWarnings("unchecked")
	public Object [] queryOriginalIvsUnsInfo(String orderId) {
		String sql = "SELECT shipment_ivs_name, shipment_uns_name, pretax_principal_price, sui.export_currency_exchange_rate\n" +
				"\t FROM public.drs_transaction dt\n" +
				"\t inner join drs_transaction_line_item_source dtlis on dtlis.drs_transaction_id = dt.id\n" +
				"\t inner join shipment uns on dt.shipment_uns_name = uns.name\n" +
				"\t inner join shipment_uns_info sui on sui.shipment_id = uns.id\n" +
				"\t where source_transaction_id = :orderId\n" +
				"\t and dt.type = 'other-transaction'";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("orderId", orderId);
		List<Object[]> resultList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		Assert.isTrue(!resultList.isEmpty(), "queryOriginalIvsUnsInfo: no results found");
		Object[] result = resultList.get(0);
		return result;
	}

	@Override @Transactional("transactionManager")
	public void updateSoldQuantityByLineItemId(BigInteger lineItemId, BigDecimal quantity) {
		String sql = "UPDATE shipment_line_item unsi set qty_sold = qty_sold - :quantity "
				+ " WHERE unsi.id = :lineItemId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("quantity", quantity.abs());
		q.addValue("lineItemId", lineItemId.intValue());
		int affectedRows = getNamedParameterJdbcTemplate().update(sql,q);
		Assert.isTrue(affectedRows==1,"affectedRows==1");

	}

	@SuppressWarnings("unchecked")
	public Object[] querySettlementInfoOfOriginalReimbursement(AmazonReimbursementInfo origReimbursementInfo) {
		String sql = "SELECT posted_date_time, sku \n" +
				"FROM amazon_settlement_report_v2\n" +
				"WHERE true \n" +
				"and posted_date = date_trunc('day', :approvalDate at time zone 'cst' at time zone 'utc')\n" +
				"and sku= :sku \n" +
				"and currency=  :currencyUnit \n" +
				"and amount= :amountTotal \n" +
				"and amount_description in  ( :reason ) LIMIT 1";
		MapSqlParameterSource q = new MapSqlParameterSource();
//		System.out.println("approvalDate: " +  origReimbursementInfo.getApprovalDate());
//		System.out.println("sku: " +  origReimbursementInfo.getSku());
//		System.out.println("currencyUnit: " +  origReimbursementInfo.getCurrencyUnit());
//		System.out.println("amountTotal: " +  origReimbursementInfo.getAmountTotal());
		q.addValue("approvalDate", origReimbursementInfo.getApprovalDate());
		q.addValue("sku", origReimbursementInfo.getSku());
		q.addValue("currencyUnit", origReimbursementInfo.getCurrencyUnit());
		q.addValue("amountTotal", origReimbursementInfo.getAmountTotal());

		System.out.println("CCCCCCCCCCCCCCCCCCCCC");
		System.out.println(origReimbursementInfo.getApprovalDate());
		System.out.println(origReimbursementInfo.getReason());
		System.out.println(origReimbursementInfo.getSku());
		System.out.println(origReimbursementInfo.getCurrencyUnit());
		System.out.println(origReimbursementInfo.getAmountTotal());

		System.out.println("CCCCCCCCCCCCCCCCCCCCC");

		List<String> reason = new ArrayList<>();
		if (origReimbursementInfo.getReason().equals("Damaged_Warehouse")) {
			reason.add("WAREHOUSE_DAMAGE");
		} else if (origReimbursementInfo.getReason().equals("Lost_Warehouse")) {
			reason.add("WAREHOUSE_LOST");
			reason.add("WAREHOUSE_LOST_MANUAL");
		} else if (origReimbursementInfo.getReason().equals("Lost_Inbound")) {
			reason.add("MISSING_FROM_INBOUND");
		}


		q.addValue("reason", reason);
		List<Object[]> resultList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		Assert.isTrue(resultList.size() == 1, "result size is not equal to 1, size: " + resultList.size());
		return resultList.get(0);
	}

	@SuppressWarnings("unchecked")
	public Object [] queryOriginalIvsUnsInfoByDateSku(Date postedDateTime, String sku) {
		String sql = "SELECT shipment_ivs_name, shipment_uns_name, pretax_principal_price, sui.export_currency_exchange_rate\n" +
				"\tFROM public.drs_transaction dt\n" +
				"\tINNER JOIN drs_transaction_line_item_source dtlis on dtlis.drs_transaction_id = dt.id\n" +
				"\tINNER JOIN shipment uns on dt.shipment_uns_name = uns.name\n" +
				"\tINNER JOIN shipment_uns_info sui on sui.shipment_id = uns.id\n" +
				"\tINNER JOIN product_sku ps ON ps.id = dt.product_sku_id\n" +
				"\tWHERE transaction_date = :postedDateTime\n" +
				"\tand ps.code_by_drs = :sku ";
		MapSqlParameterSource q = new MapSqlParameterSource();
//		System.out.println("postedDateTime: " + postedDateTime);
		q.addValue("postedDateTime", postedDateTime);
		q.addValue("sku", sku);
		List<Object[]> resultList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		Assert.isTrue(!resultList.isEmpty(), "queryOriginalIvsUnsInfoByDateSku: no results found");
		Object[] result = resultList.get(0);
		return result;

	}

	@SuppressWarnings("unchecked")
	public Object[] queryAmountTotalPostedDate(MarketSideTransaction transaction) {
		String sql = "SELECT amount, original_posted_date \n" +
				"\tFROM public.amazon_settlement_report_v2 \n" +
				"\tWHERE posted_date_time = :postedDateTime " +
				"\t AND sku = :sku ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("postedDateTime", transaction.getTransactionDate());
		q.addValue("sku", transaction.getSku());
		List<Object[]> resultList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		Assert.isTrue(!resultList.isEmpty(), "queryAmountTotalPostedDate: no results found");
		return resultList.get(0);
	}

}
