package com.kindminds.drs.persist.data.access.rdb.accounting;

import java.util.List;
import java.util.Objects;


import com.kindminds.drs.api.data.access.rdb.accounting.DrsTransactionDao;
import com.kindminds.drs.persist.data.access.rdb.Dao;


import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.accounting.DrsTransaction;
import com.kindminds.drs.api.v1.model.accounting.DrsTransaction.DrsTransactionLineItem;
import com.kindminds.drs.api.v1.model.accounting.DrsTransaction.DrsTransactionLineItemSource;
import com.kindminds.drs.persist.v1.model.mapping.settleable.DrsTransactionImpl;
import com.kindminds.drs.persist.v1.model.mapping.settleable.DrsTransactionLineItemSourceImpl;
import com.kindminds.drs.persist.v1.model.mapping.settleable.DrsTransactionLineItemImpl;
import com.kindminds.drs.persist.data.access.rdb.util.PostgreSQLHelper;

@Repository
public class DrsTransactionDaoImpl extends Dao implements DrsTransactionDao {


	
	@Override @SuppressWarnings("unchecked")
	public DrsTransaction query(int dtId) {
		String sql = "select          dt.id as id, "
				+ "                 dt.type as type, "
				+ "     dt.transaction_date as transaction_date,  "
				+ "      dt.transaction_seq as transaction_seq, "
				+ "          ps.code_by_drs as product_sku, "
				+ "             dt.quantity as quantity, "
				+ "       dt.marketplace_id as marketplace_id, "
				+ "dt.source_transaction_id as source_transaction_id, "
				+ "    dt.shipment_ivs_name as shipment_ivs_name, "
				+ "    dt.shipment_uns_name as shipment_uns_name "
				+ "from drs_transaction dt "
				+ "inner join product_sku ps on ps.id = dt.product_sku_id "
				+ "where dt.id = :id order by dt.transaction_seq";
			MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("id", dtId);


		List<DrsTransactionImpl> resultList = getNamedParameterJdbcTemplate().query(sql,q,
				(rs,rowNum) -> new DrsTransactionImpl(rs.getInt("id"),rs.getString("type"),rs.getTimestamp("transaction_date"),rs.getInt("transaction_seq"),
						rs.getString("product_sku"),rs.getInt("quantity"),rs.getInt("marketplace_id"),rs.getString("source_transaction_id"),
						rs.getString("shipment_ivs_name"),rs.getString("shipment_uns_name")
		));

		Assert.isTrue(resultList.size()==1,"resultList.size()==1");
		DrsTransactionImpl transaction = resultList.get(0);
		transaction.setElements(this.querySettleableItemElements(dtId));
		transaction.setSettleableItemList(this.querySettleableItemList(dtId));
		return transaction;
	}
	
	@Override @SuppressWarnings("unchecked")
	public DrsTransaction query(int seq,String sourceId,String sku) {
		String sql = "select          dt.id as id, "
				+ "                 dt.type as type, "
				+ "     dt.transaction_date as transaction_date,  "
				+ "      dt.transaction_seq as transaction_seq, "
				+ "          ps.code_by_drs as product_sku, "
				+ "             dt.quantity as quantity, "
				+ "       dt.marketplace_id as marketplace_id, "
				+ "dt.source_transaction_id as source_transaction_id, "
				+ "    dt.shipment_ivs_name as shipment_ivs_name, "
				+ "    dt.shipment_uns_name as shipment_uns_name "
				+ "from drs_transaction dt "
				+ "inner join product_sku ps on ps.id = dt.product_sku_id "
				+ "where true "
				+ "and dt.transaction_seq = :transaction_seq "
				+ "and dt.source_transaction_id = :source_transaction_id "
				+ "and dt.type = :type "
				+ "and ps.code_by_drs = :sku " +
				" ORDER BY id  ";
			MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("transaction_seq", seq);
		q.addValue("source_transaction_id", sourceId);
		q.addValue("type", "Order");
		q.addValue("sku",sku);

		List<DrsTransactionImpl> resultList = getNamedParameterJdbcTemplate().query(sql,q,
				(rs,rowNum) -> new DrsTransactionImpl(rs.getInt("id"),rs.getString("type"),rs.getTimestamp("transaction_date"),rs.getInt("transaction_seq"),
						rs.getString("product_sku"),rs.getInt("quantity"),rs.getInt("marketplace_id"),rs.getString("source_transaction_id"),
						rs.getString("shipment_ivs_name"),rs.getString("shipment_uns_name")
				));


		Assert.isTrue(resultList.size()==1,"resultList.size()!= 1, size: " + resultList.size()
				+ ", trans seq: " + seq + ", sourceId: " + sourceId + ", sku: " + sku);

		DrsTransactionImpl dt = resultList.get(0);
		dt.setElements(this.querySettleableItemElements(dt.getId()));
		dt.setSettleableItemList(this.querySettleableItemList(dt.getId()));
		return dt;
	}

	@Override @SuppressWarnings("unchecked")
	public List<DrsTransaction> query(String sourceId,String sku) {
		String sql = "select          dt.id as id, "
				+ "                 dt.type as type, "
				+ "     dt.transaction_date as transaction_date,  "
				+ "      dt.transaction_seq as transaction_seq, "
				+ "          ps.code_by_drs as product_sku, "
				+ "             dt.quantity as quantity, "
				+ "       dt.marketplace_id as marketplace_id, "
				+ "dt.source_transaction_id as source_transaction_id, "
				+ "    dt.shipment_ivs_name as shipment_ivs_name, "
				+ "    dt.shipment_uns_name as shipment_uns_name "
				+ "from drs_transaction dt "
				+ "inner join product_sku ps on ps.id = dt.product_sku_id "
				+ "where true "
				+ "and dt.source_transaction_id = :source_transaction_id "
				+ "and dt.type = :type "
				+ "and ps.code_by_drs = :sku " +
				" ORDER BY id  ";
		MapSqlParameterSource q = new MapSqlParameterSource();

		q.addValue("source_transaction_id", sourceId.trim());
		q.addValue("type", "Order");
		q.addValue("sku",sku.trim());

		List<DrsTransactionImpl> resultList = getNamedParameterJdbcTemplate().query(sql,q,
				(rs,rowNum) -> new DrsTransactionImpl(rs.getInt("id"),rs.getString("type"),rs.getTimestamp("transaction_date"),rs.getInt("transaction_seq"),
						rs.getString("product_sku"),rs.getInt("quantity"),rs.getInt("marketplace_id"),rs.getString("source_transaction_id"),
						rs.getString("shipment_ivs_name"),rs.getString("shipment_uns_name")
				));

		resultList.forEach( dt ->{
			dt.setElements(this.querySettleableItemElements(dt.getId()));
			dt.setSettleableItemList(this.querySettleableItemList(dt.getId()));
		});

		return (List<DrsTransaction>)(List<?>)resultList;
	}
	
	@SuppressWarnings("unchecked")
	private DrsTransactionLineItemSource querySettleableItemElements(int dtId){
		String sql = "select                    dtlis.id as id, "
				+ "                    dtlis.currency_id as currency_id, "
				+ "                dtlis.msdc_retainment as msdc_retainment, "
				+ "         dtlis.pretax_principal_price as pretax_principal_price, "
				+ "                dtlis.marketplace_fee as marketplace_fee, "
				+ " dtlis.marketplace_fee_non_refundable as marketplace_fee_non_refundable, "
				+ "                dtlis.fulfillment_fee as fulfillment_fee, "
				+ "                dtlis.ssdc_retainment as ssdc_retainment, "
				+ "     dtlis.fca_in_marketside_currency as fca_in_marketside_currency, "
				+ "                dtlis.sp_profit_share as sp_profit_share, "
				+ "                     dtlis.refund_fee as refund_fee "
				+ "from drs_transaction_line_item_source dtlis "
				+ "where dtlis.drs_transaction_id = :dtId ";

		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("dtId", dtId);


		List<DrsTransactionLineItemSourceImpl> resultList = getNamedParameterJdbcTemplate().query(sql,q,
				(rs,rowNum) -> new DrsTransactionLineItemSourceImpl(
						rs.getInt("id"),	rs.getInt("currency_id"),rs.getBigDecimal("pretax_principal_price") ,rs.getBigDecimal("msdc_retainment"),
						rs.getBigDecimal("marketplace_fee"),rs.getBigDecimal("marketplace_fee_non_refundable"),
						rs.getBigDecimal("fulfillment_fee") ,rs.getBigDecimal("ssdc_retainment"),
						rs.getBigDecimal("fca_in_marketside_currency") ,rs.getBigDecimal("sp_profit_share"),
						rs.getBigDecimal("refund_fee")
				));

		if(resultList.size()==0) return null;
		Assert.isTrue(resultList.size()==1,"resultList.size()==1");
		return resultList.get(0);
	}
	
	@SuppressWarnings("unchecked")
	private List<DrsTransactionLineItem> querySettleableItemList(int dtId){
		String sql = "select dtli.id as id, "
				+ "    dtli.line_seq as line_seq, "
				+ "      isur.k_code as isur_kcode, "
				+ "      rcvr.k_code as rcvr_kcode, "
				+ "         tlt.name as item_name, "
				+ " dtli.currency_id as currency_id, "
				+ "      dtli.amount as amount "
				+ "from drs_transaction_line_item dtli "
				+ "inner join company isur on isur.id = dtli.isur_company_id "
				+ "inner join company rcvr on rcvr.id = dtli.rcvr_company_id "
				+ "inner join transactionlinetype tlt on tlt.id = dtli.item_type_id "
				+ "where dtli.drs_transaction_id = :dtId order by dtli.line_seq ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("dtId", dtId);


		List<DrsTransactionLineItemImpl> resultList = getNamedParameterJdbcTemplate().query(sql,q , (rs,rowNum) -> new DrsTransactionLineItemImpl(
				rs.getInt("id"),rs.getInt("line_seq"),rs.getString("isur_kcode"),
				rs.getString("rcvr_kcode") , rs.getString("item_name"),rs.getInt("currency_id"),
				rs.getBigDecimal("amount")
		));
		if(resultList.size()==0) Assert.isTrue(false,"resultList.size()==0");
		return (List)resultList;
	}

	@Override @Transactional("transactionManager")
	public int insert(DrsTransaction dt) {
		int dtId = PostgreSQLHelper.getNextVal(getNamedParameterJdbcTemplate(), "drs_transaction", "id");
		String sql = "insert into drs_transaction "
				+ "(   id,  type, transaction_date, transaction_seq, product_sku_id,  quantity, marketplace_id, source_transaction_id, shipment_ivs_name, shipment_uns_name ) "
				+ "select "
				+ " :dtId, :type, :transactionDate, :transactionSeq,  ps.id, :quantity, :marketplaceId,  :sourceTransactionId,  :shipmentIvsName,  :shipmentUnsName "
				+ "from product_sku ps "
				+ "where ps.code_by_drs = :prodcutSku ";

		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("dtId", dtId);
		q.addValue("type", dt.getType());
		q.addValue("transactionDate", dt.getTransactionDate());
		q.addValue("transactionSeq", dt.getTransactionSequence());
		q.addValue("quantity", dt.getQuantity());
		q.addValue("marketplaceId", dt.getMarketplace().getKey());
		q.addValue("sourceTransactionId", dt.getSourceTransactionId());
		q.addValue("shipmentIvsName", dt.getShipmentIvsName());
		q.addValue("shipmentUnsName", dt.getShipmentUnsName());
		q.addValue("prodcutSku", dt.getProductSku());

		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1,"q.executeUpdate()==1");
		this.insertSettleableItemElements(dtId, dt.getSettleableItemElements());
		if (!dt.getSettleableItemList().isEmpty()) {
			this.insertSettleableItems(dtId, dt.getSettleableItemList());
		}

		return dtId;
	}
	
	private void insertSettleableItemElements(int dtId, DrsTransactionLineItemSource elements){
		if(elements==null) return;
		String sql = "insert into drs_transaction_line_item_source "
				+ "( drs_transaction_id,  currency_id,  pretax_principal_price,  msdc_retainment,  marketplace_fee,  marketplace_fee_non_refundable,  fulfillment_fee,  ssdc_retainment,  fca_in_marketside_currency,  sp_profit_share,  refund_fee ) values "
		        + "(:drs_transaction_id, :currency_id, :pretax_principal_price, :msdc_retainment, :marketplace_fee, :marketplace_fee_non_refundable, :fulfillment_fee, :ssdc_retainment, :fca_in_marketside_currency, :sp_profit_share, :refund_fee )";
		MapSqlParameterSource q = new MapSqlParameterSource();

	    q.addValue("drs_transaction_id",dtId);
	    q.addValue("currency_id",elements.getCurrency().getKey());
	    q.addValue("pretax_principal_price",elements.getPretaxPrincipalPrice());
	    q.addValue("msdc_retainment",elements.getMsdcRetainment());
	    q.addValue("marketplace_fee",elements.getMarketplaceFee());
	    q.addValue("marketplace_fee_non_refundable",elements.getMarketplaceFeeNonRefundable());
	    q.addValue("fulfillment_fee",elements.getFulfillmentFee());
	    q.addValue("ssdc_retainment",elements.getSsdcRetainment());
	    q.addValue("fca_in_marketside_currency",elements.getFcaInMarketsideCurrency());
	    q.addValue("sp_profit_share",elements.getSpProfitShare());
	    q.addValue("refund_fee",elements.getRefundFee());
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1,"q.executeUpdate()==1");
	}
	
	private void insertSettleableItems(int dtId,List<DrsTransactionLineItem> itemList){
		String sql = "insert into drs_transaction_line_item "
				+ "( drs_transaction_id, line_seq, isur_company_id, rcvr_company_id, item_type_id, currency_id,  amount )"
				+ "select "
				+ "               :dtId, :lineSeq,         isur.id,         rcvr.id,       tlt.id, :currencyId, :amount "
				+ "from company isur, company rcvr, transactionlinetype tlt "
				+ "where isur.k_code = :isurKcode "
				+ "and rcvr.k_code = :rcvrKcode "
				+ "and tlt.name = :tltName ";
		for(DrsTransactionLineItem item:itemList){
			MapSqlParameterSource q = new MapSqlParameterSource();
			q.addValue("dtId", dtId);
			q.addValue("lineSeq", item.getLineSeq());
			q.addValue("amount", item.getAmount());
			q.addValue("currencyId", Currency.valueOf(item.getCurrency()).getKey());
			q.addValue("isurKcode", item.getIsurKcode());
			q.addValue("rcvrKcode", item.getRcvrKcode());
			q.addValue("tltName", item.getItemName());
			Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1,"q.executeUpdate()==1");
		}
	}

}
