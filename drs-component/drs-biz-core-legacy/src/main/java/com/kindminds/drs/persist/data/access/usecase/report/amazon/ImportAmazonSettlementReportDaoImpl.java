package com.kindminds.drs.persist.data.access.usecase.report.amazon;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;




import com.kindminds.drs.persist.data.access.rdb.Dao;


import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.kindminds.drs.api.data.access.usecase.report.amazon.ImportAmazonSettlementReportDao;
import com.kindminds.drs.api.v1.model.accounting.AmazonSettlementReportInfo;
import com.kindminds.drs.api.v1.model.accounting.AmazonSettlementReportRawLine;
import com.kindminds.drs.util.DateHelper;

@Repository
public class ImportAmazonSettlementReportDaoImpl extends Dao implements ImportAmazonSettlementReportDao {
	


	@Override @SuppressWarnings("unchecked")
	public List<Object []> querySettlementReportInfoList() {
		String sql = "select "
				+ "settlement_start_date as date_start, "
				+ "  settlement_end_date as date_end, "
				+ "        settlement_id as settlement_id, "
				+ "             currency as currency_name, "
				+ "source_marketplace_id as source_marketplace_id "
				+ "from amazon_settlement_report_v2_info order by settlement_end_date desc ";
			
		List<Object[]> columnsList = getJdbcTemplate().query(sql,objArrayMapper);
		return columnsList;
	}
	
	@Override @Transactional("transactionManager")
	public void insertReportInfo(AmazonSettlementReportInfo info,String format) {
		String sql = "insert into amazon_settlement_report_v2_info "
				+ "( settlement_id, settlement_start_date, settlement_end_date, deposit_date, total_amount,  currency, is_imported,  source_marketplace_id ) values "
				+ "( :settlementId,  :settlementStartDate,  :settlementEndDate, :depositDate, :totalAmount, :currency,       FALSE, :source_marketplace_id ) ";
			MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("settlementId", info.getSettlementId());
		q.addValue("settlementStartDate",  DateHelper.toDate(info.getDateStart(),format));
		q.addValue("settlementEndDate", DateHelper.toDate(info.getDateEnd(),format));
		q.addValue("depositDate", DateHelper.toDate(info.getDateDeposit(),format));
		q.addValue("totalAmount", new BigDecimal(info.getTotalAmount()));
		q.addValue("currency", info.getCurrency());
		q.addValue("source_marketplace_id", info.getSourceMarketplace().getKey());
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
	}

	@Override @Transactional("transactionManager")
	public void insertReportInfoAsImported(String settlementId, String settlementStartDate, String settlementEndDate,String depositDate, String totalAmount, String currency,int sourceMarketplaceId) {
		String sql = "insert into amazon_settlement_report_v2_info "
				+ "( settlement_id, settlement_start_date, settlement_end_date, deposit_date, total_amount,  currency, is_imported,  source_marketplace_id ) values "
				+ "( :settlementId,  :settlementStartDate,  :settlementEndDate, :depositDate, :totalAmount, :currency,        TRUE, :source_marketplace_id ) ";
		String dateTimeFormatUS = "yyyy-MM-dd HH:mm:ss z";
			MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("settlementId", settlementId);
		q.addValue("settlementStartDate", DateHelper.toDate(settlementStartDate, dateTimeFormatUS));
		q.addValue("settlementEndDate", DateHelper.toDate(settlementEndDate, dateTimeFormatUS));
		q.addValue("depositDate", DateHelper.toDate(depositDate, dateTimeFormatUS));
		q.addValue("totalAmount", new BigDecimal(totalAmount));
		q.addValue("currency", currency);
		q.addValue("source_marketplace_id", sourceMarketplaceId);
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
	}

	@Override @Transactional("transactionManager")
	public void insertReportRawLines(List<AmazonSettlementReportRawLine> lines,String dateFormat,String dateTimeFormat,int sourceMarketplaceId) {
		String sql = "insert into amazon_settlement_report_v2 "
				+ "( settlement_id,  currency, transaction_type, order_id, merchant_order_id, adjustment_id, shipment_id, marketplace_name, amount_type, amount_description,  amount, fulfillment_id, posted_date, posted_date_time, order_item_code, merchant_order_item_id, merchant_adjustment_item_id,  sku, quantity_purchased, promotion_id, source_marketplace_id, original_posted_date ) values "
				+ "( :settlementId, :currency, :transactionType, :orderId,  :merchantOrderId, :adjustmentId, :shipmentId, :marketplaceName, :amountType, :amountDescription, :amount, :fulfillmentId, :postedDate,  :postedDateTime,  :orderItemCode,   :merchantOrderItemId,   :merchantAdjustmentItemId, :sku, :quantityPurchased, :promotionId,:source_marketplace_id, :originalPostedDate ) ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		for(AmazonSettlementReportRawLine line:lines){

			Date postedDateTime = DateHelper.toDate(line.getPostedDateTime(), dateTimeFormat);
			q.addValue("settlementId", line.getSettlementId());
			q.addValue("currency", line.getCurrency());
			q.addValue("transactionType", line.getTransactionType());
			q.addValue("orderId", line.getOrderId());
			q.addValue("merchantOrderId", line.getMerchantOrderId());
			q.addValue("adjustmentId", line.getAdjustmentId());
			q.addValue("shipmentId", line.getShipmentId());
			q.addValue("marketplaceName", line.getMarketplaceName());
			q.addValue("amountType", line.getAmountType());
			q.addValue("amountDescription", line.getAmountDescription());
			q.addValue("amount", new BigDecimal(line.getAmount()));
			q.addValue("fulfillmentId", line.getFulfillmentId());
			q.addValue("postedDate", DateHelper.toDate(line.getPostedDate(), dateFormat));
			q.addValue("postedDateTime", postedDateTime);
			q.addValue("orderItemCode", line.getOrderItemCode());
			q.addValue("merchantOrderItemId", line.getMerchantOrderItemId());
			q.addValue("merchantAdjustmentItemId", line.getMerchantAdjustmentItemId());
			q.addValue("sku", line.getSku());
			q.addValue("quantityPurchased", line.getQuantityPurchased()==null?null:new Integer(line.getQuantityPurchased()));
			q.addValue("promotionId", line.getPromotionId());
			q.addValue("source_marketplace_id", sourceMarketplaceId);
			q.addValue("originalPostedDate", postedDateTime);
			Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
		}
	}

	@Override  @Transactional("transactionManager")
	public void updateReportAsImported(String settlementId) {
		String sql = "update amazon_settlement_report_v2_info set is_imported = TRUE "
				+ "where settlement_id = :settlementId ";
			MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("settlementId", settlementId);
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
	}

	@Override @SuppressWarnings("unchecked")
	public Boolean isReportImported(String settlementId) {
		String sql = "select is_imported from amazon_settlement_report_v2_info asrvi "
				+ "where asrvi.settlement_id = :settlementId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("settlementId", settlementId);
		List<Boolean> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,Boolean.class);
		Assert.isTrue(resultList.size()==0||resultList.size()==1);
		if(resultList.size()==0) return false;
		return resultList.get(0);
	}

	@Override @Transactional("transactionManager")
	public void clearUncompletedInfoAndLines(String settlementId) {
		String sql = "delete from amazon_settlement_report_v2 asrv using amazon_settlement_report_v2_info asrvi "
				+ "where asrv.settlement_id = asrvi.settlement_id "
				+ "and asrvi.settlement_id = :settlementId "
				+ "and asrvi.is_imported = FALSE ";
			MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("settlementId", settlementId);
		getNamedParameterJdbcTemplate().update(sql,q);
		sql = "delete from amazon_settlement_report_v2_info where settlement_id = :settlementId ";
		 q = new MapSqlParameterSource();
		q.addValue("settlementId", settlementId);
		getNamedParameterJdbcTemplate().update(sql,q);
	}

	@Override 
	public boolean settlementIdExist(String settlementId) {
		String sql = "select exists(select 1 from amazon_settlement_report_v2 where settlement_id = :settlementId )";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("settlementId", settlementId);
		Boolean o = getNamedParameterJdbcTemplate().queryForObject(sql,q,Boolean.class);
		Assert.notNull(o);
		return o;
	}

	@Override @Transactional("transactionManager")
	public void insertReportRawLine(List<AmazonSettlementReportRawLine> lines,String dateFormat,String dateTimeFormat,int sourceMarketplaceId) {
		String sql = "insert into amazon_settlement_report_v2 "
				+ "( settlement_id,  currency, transaction_type, order_id, merchant_order_id, adjustment_id, shipment_id, marketplace_name, amount_type, amount_description,  amount, fulfillment_id, posted_date, posted_date_time, order_item_code, merchant_order_item_id, merchant_adjustment_item_id,  sku, quantity_purchased, promotion_id, source_marketplace_id , original_posted_date ) values "
				+ "( :settlementId, :currency, :transactionType, :orderId,  :merchantOrderId, :adjustmentId, :shipmentId, :marketplaceName, :amountType, :amountDescription, :amount, :fulfillmentId, :postedDate,  :postedDateTime,  :orderItemCode,   :merchantOrderItemId,   :merchantAdjustmentItemId, :sku, :quantityPurchased, :promotionId,:source_marketplace_id , :originalPostedDate ) ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		for(AmazonSettlementReportRawLine line:lines){

			Date postedDateTime = DateHelper.toDate(line.getPostedDateTime(), dateTimeFormat);
			q.addValue("settlementId", line.getSettlementId());
			q.addValue("currency", line.getCurrency());
			q.addValue("transactionType", line.getTransactionType());
			q.addValue("orderId", line.getOrderId());
			q.addValue("merchantOrderId", line.getMerchantOrderId());
			q.addValue("adjustmentId", line.getAdjustmentId());
			q.addValue("shipmentId", line.getShipmentId());
			q.addValue("marketplaceName", line.getMarketplaceName());
			q.addValue("amountType", line.getAmountType());
			q.addValue("amountDescription", line.getAmountDescription());
			q.addValue("amount", new BigDecimal(line.getAmount()));
			q.addValue("fulfillmentId", line.getFulfillmentId());
			q.addValue("postedDate", DateHelper.toDate(line.getPostedDate(), dateFormat));
			q.addValue("postedDateTime", postedDateTime);
			q.addValue("orderItemCode", line.getOrderItemCode());
			q.addValue("merchantOrderItemId", line.getMerchantOrderItemId());
			q.addValue("merchantAdjustmentItemId", line.getMerchantAdjustmentItemId());
			q.addValue("sku", line.getSku());
			q.addValue("quantityPurchased", line.getQuantityPurchased()==null?null:new Integer(line.getQuantityPurchased()));
			q.addValue("promotionId", line.getPromotionId());
			q.addValue("source_marketplace_id",sourceMarketplaceId);
			q.addValue("originalPostedDate", postedDateTime);
			Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
		}
	}
}
