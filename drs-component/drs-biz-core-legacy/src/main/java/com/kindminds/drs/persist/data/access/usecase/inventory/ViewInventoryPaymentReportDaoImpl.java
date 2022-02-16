package com.kindminds.drs.persist.data.access.usecase.inventory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kindminds.drs.api.data.access.usecase.inventory.ViewInventoryPaymentReportDao;
import com.kindminds.drs.persist.data.access.rdb.Dao;
import com.kindminds.drs.v1.model.impl.PaymentRecordReportAmountDetailLineItemImpl;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import com.kindminds.drs.api.v1.model.accounting.StatementLineType;
import com.kindminds.drs.api.v1.model.accounting.DrsTransaction.DrsTransactionType;
import com.kindminds.drs.enums.AmazonTransactionType;
import com.kindminds.drs.api.v1.model.report.InventoryPaymentReport.InventoryPaymentReportAmountDetailLineItem;
import com.kindminds.drs.api.v1.model.report.InventoryPaymentReport.InventoryPaymentReportQuantitySummaryLineItem;

import com.kindminds.drs.persist.v1.model.mapping.accounting.PaymentRecordReportQuantitySummaryLineItemImpl;

import static java.lang.Math.round;

@Repository
public class ViewInventoryPaymentReportDaoImpl extends Dao implements ViewInventoryPaymentReportDao {
	


	@Override @SuppressWarnings("unchecked")
	public Map<String,Integer> queryShipmentSkuToOriginalInvoiceQuantityMap(String shipmentName) {
		String sql = "select ps.code_by_drs, sum(ivsli.qty_order ) "
				+ "from shipment ivs "
				+ "inner join shipment_line_item ivsli on ivsli.shipment_id = ivs.id "
				+ "inner join product_sku ps on ps.id = ivsli.sku_id "
				+ "where ivs.name = :shipmentName  "
				+ "group by ps.code_by_drs ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("shipmentName",shipmentName);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		Map<String,Integer> shipmentSkuToTotalMap = new HashMap<>();
		for(Object[] columns:columnsList){
			String productSku = (String)columns[0];
			Integer total = BigInteger.valueOf(Long.parseLong(columns[1].toString())).intValue();
			shipmentSkuToTotalMap.put(productSku, total);
		}
		return shipmentSkuToTotalMap;
	}

	@Override @SuppressWarnings("unchecked")
	public Map<String,Integer> queryShipmentSkuToOriginalInvoiceAmountMap(String shipmentName) {
		String sql = "select ps.code_by_drs, sum(ivsli.qty_order * ivsli.unit_amount) "
				+ "from shipment ivs "
				+ "inner join shipment_line_item ivsli on ivsli.shipment_id = ivs.id "
				+ "inner join product_sku ps on ps.id = ivsli.sku_id "
				+ "where ivs.name = :shipmentName  "
				+ "group by ps.code_by_drs ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("shipmentName",shipmentName);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		Map<String,Integer> shipmentSkuToTotalMap = new HashMap<>();
		for(Object[] columns:columnsList){
			String productSku = (String)columns[0];
			BigDecimal roundTotal = new BigDecimal(columns[1].toString());
			Integer total = roundTotal.intValue();
			shipmentSkuToTotalMap.put(productSku, total);
		}
		return shipmentSkuToTotalMap;
	}

	@Override @SuppressWarnings("unchecked")
	public List<InventoryPaymentReportQuantitySummaryLineItem> queryInventoryPaymentReportQuantitySummaryLineItems(String shipmentName) {

		//todo need seperate calc amount and inventory quantity
		String sql = "select ps.id as id, "
				+ " ps.code_by_drs as sku, "
				+ " sum(summary.quantity_order) as quantity_order, "
				+ " sum(summary_return.quantity_fba_return_to_supplier) as quantity_fba_return_to_supplier, "
				+ " sum(summary_sellback_recovery.quantity_soldback_recovery) as quantity_soldback_recovery, "
				+ " sum(summary.quantity_other_transaction) as quantity_other_transaction, "
				+ " sum(summary.quantity_refund) as quantity_refund "
				+ "from shipment ivs "
				//+ "inner join shipment_line_item ivsi on ivsi.shipment_id = ivs.id "
				//+ "inner join product_sku ps on ps.id = ivsi.sku_id "
				+ " inner join product_sku ps on ps.id in (select distinct sku_id  from shipment_line_item where shipment_id = ivs.id ) "
				+ "left join ("
				+ "    select    ps.id as ps_id, "
				+ "    sum(case when dt.type = :order then dt.quantity else 0 end) as quantity_order, "
				+ "    sum(case when dt.type = :other and dtlis.pretax_principal_price > 0 then dt.quantity else 0 end) as quantity_other_transaction, "
				+ "    sum(case when dt.type = :refnd then dt.quantity when dt.type = :other and dtlis.pretax_principal_price <= 0 then dt.quantity else 0 end) as quantity_refund "
				+ "    from drs_transaction dt "
				+ "    inner join product_sku ps on ps.id = dt.product_sku_id "
				+ "    inner join drs_transaction_line_item_source dtlis on dt.id = dtlis.drs_transaction_id "
				+ "    where dt.shipment_ivs_name = :shipmentName and dt.inventory_excluded = false "
				+ "    group by ps.id, ps.code_by_drs "
				+ ") summary on summary.ps_id = ps.id "
				+ "left join ("
				+ "		select							ps.id as ps_id, "
				+ " 	sum(case when dt.type in (:fbaRt) then dt.quantity else 0 end) as quantity_fba_return_to_supplier "
				+ " 	from drs_transaction dt "
				+ " 	inner join product_sku ps on ps.id = dt.product_sku_id "
				+ " 	where dt.shipment_ivs_name = :shipmentName and dt.inventory_excluded = false "
				+ " 	group by ps.id, ps.code_by_drs "
				+ ") summary_return on summary_return.ps_id = ps.id "
				+ "left join ("
				+ "		select							ps.id as ps_id, "
				+ " 	sum(case when dt.type in (:fbaRtRecovery) then dt.quantity else 0 end) as quantity_soldback_recovery "
				+ " 	from drs_transaction dt "
				+ " 	inner join product_sku ps on ps.id = dt.product_sku_id "
				+ " 	where dt.shipment_ivs_name = :shipmentName and dt.inventory_excluded = false "
				+ " 	group by ps.id, ps.code_by_drs "
				+ ") summary_sellback_recovery on summary_sellback_recovery.ps_id = ps.id "
				+ " where ivs.name = :shipmentName "
				+ " group by ps.id, ps.code_by_drs "
				+ " order by ps.code_by_drs ";


		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("order",AmazonTransactionType.ORDER.getValue());
		q.addValue("fbaRt",DrsTransactionType.getFbaReturnTypesNoRecovery());
		q.addValue("fbaRtRecovery","FBA Returns Recovery");
		q.addValue("other",AmazonTransactionType.OTHER.getValue());
		q.addValue("refnd",AmazonTransactionType.REFUND.getValue());
		q.addValue("shipmentName",shipmentName);


		return (List) getNamedParameterJdbcTemplate().query(sql,q,
				(rs,rowNum) -> new PaymentRecordReportQuantitySummaryLineItemImpl(
						rs.getInt("id"),rs.getString("sku"),
						rs.getInt("quantity_order"),rs.getInt("quantity_fba_return_to_supplier"),
						rs.getInt("quantity_soldback_recovery"),
						rs.getInt("quantity_other_transaction"),rs.getInt("quantity_refund")
				));
	}

	@Override @SuppressWarnings("unchecked")
	public List<InventoryPaymentReportQuantitySummaryLineItem> queryInventoryPaymentReportAmountSummaryLineItems(String shipmentName) {

		//todo need seperate calc amount and inventory quantity
		String sql = " select "
				+ " distinct(ps.code_by_drs) as sku, "
				+ " qty.quantity_order * sli.unit_amount as amount_order,  "
				+ " qty.quantity_fba_return_to_supplier * sli.unit_amount as amount_fba_return_to_supplier, "
				+ " qty.quantity_soldback_recovery * sli.unit_amount as amount_soldback_recovery, "
				+ " qty.quantity_other_transaction * sli.unit_amount as amount_other_transaction, "
				+ " qty.quantity_refund * sli.unit_amount as amount_refund "
				+ " from shipment_line_item sli "
				+ " inner join product_sku ps on sli.sku_id = ps.id "
				+ " inner join shipment ivs on sli.shipment_id = ivs.id "
				+ " inner join ( "
				+ " select ps.id as id, "
				+ " ps.code_by_drs as sku, "
				+ " sum(summary.quantity_order) as quantity_order, "
				+ " sum(summary_return.quantity_fba_return_to_supplier) as quantity_fba_return_to_supplier, "
				+ " sum(summary_sellback_recovery.quantity_soldback_recovery) as quantity_soldback_recovery, "
				+ " sum(summary.quantity_other_transaction) as quantity_other_transaction, "
				+ " sum(summary.quantity_refund) as quantity_refund "
				+ "from shipment ivs "
				+ " inner join product_sku ps on ps.id in (select distinct sku_id  from shipment_line_item where shipment_id = ivs.id ) "
				+ " left join ("
				+ "    select    ps.id as ps_id, "
				+ "    sum(case when dt.type = :order then dt.quantity else 0 end) as quantity_order, "
				+ "    sum(case when dt.type = :other and dtlis.pretax_principal_price > 0 then dt.quantity else 0 end) as quantity_other_transaction, "
				+ "    sum(case when dt.type = :refund then dt.quantity when dt.type = :other and dtlis.pretax_principal_price <= 0 then dt.quantity else 0 end) as quantity_refund "
				+ "    from drs_transaction dt "
				+ "    inner join product_sku ps on ps.id = dt.product_sku_id "
				+ "    inner join drs_transaction_line_item_source dtlis on dt.id = dtlis.drs_transaction_id "
				+ "    where dt.shipment_ivs_name = :shipmentName and dt.inventory_excluded = false "
				+ "    group by ps.id, ps.code_by_drs "
				+ ") summary on summary.ps_id = ps.id "
				+ "left join ("
				+ "		select							ps.id as ps_id, "
				+ " 	sum(case when dt.type in (:fbaRt) then dt.quantity else 0 end) as quantity_fba_return_to_supplier "
				+ " 	from drs_transaction dt "
				+ " 	inner join product_sku ps on ps.id = dt.product_sku_id "
				+ " 	where dt.shipment_ivs_name = :shipmentName and dt.inventory_excluded = false "
				+ " 	group by ps.id, ps.code_by_drs "
				+ ") summary_return on summary_return.ps_id = ps.id "
				+ "left join ("
				+ "		select							ps.id as ps_id, "
				+ " 	sum(case when dt.type in (:fbaRtRecovery) then dt.quantity else 0 end) as quantity_soldback_recovery "
				+ " 	from drs_transaction dt "
				+ " 	inner join product_sku ps on ps.id = dt.product_sku_id "
				+ " 	where dt.shipment_ivs_name = :shipmentName and dt.inventory_excluded = false "
				+ " 	group by ps.id, ps.code_by_drs "
				+ ") summary_sellback_recovery on summary_sellback_recovery.ps_id = ps.id "
				+ " where ivs.name = :shipmentName "
				+ " group by ps.id, ps.code_by_drs "
				+ " order by ps.code_by_drs "
				+ ") qty on qty.sku = ps.code_by_drs "
				+ " where ivs.name = :shipmentName ";



		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("order",AmazonTransactionType.ORDER.getValue());
		q.addValue("fbaRt",DrsTransactionType.getFbaReturnTypesNoRecovery());
		q.addValue("fbaRtRecovery","FBA Returns Recovery");
		q.addValue("other",AmazonTransactionType.OTHER.getValue());
		q.addValue("refund",AmazonTransactionType.REFUND.getValue());
		q.addValue("shipmentName",shipmentName);

		return (List) getNamedParameterJdbcTemplate().query(sql,q,
				(rs,rowNum) -> new PaymentRecordReportQuantitySummaryLineItemImpl(
						rs.getString("sku"),
						rs.getInt("amount_order"),rs.getInt("amount_fba_return_to_supplier"),
						rs.getInt("amount_soldback_recovery"),
						rs.getInt("amount_other_transaction"),rs.getInt("amount_refund")
				));
	}



	@Override @SuppressWarnings("unchecked")
	public List<InventoryPaymentReportAmountDetailLineItem> queryInventoryPaymentReportAmountDetailLineItems(String shipmentName) {
		String sql = "select          bsli.id as id," +
				"            ps.code_by_drs as sku, " +
				"                   bs.name as statement_name, " +
				"                  slt.name as item_name, " +
				"             bsli.quantity as quantity, " +
				"bsli.statement_currency_id as currency_id, " +
				"     bsli.statement_amount as amount " +
				"from bill_statementlineitem bsli " +
				"inner join bill_statement bs on bs.id = bsli.statement_id " +
				"inner join product_sku ps on ps.id = bsli.product_sku_id " +
				"inner join statement_line_type slt on slt.id = bsli.stlmnt_line_item_type_id " +
				"where bsli.reference = :shipmentName " +
				"and slt.name not in (:sellBackType) " +
				"order by ps.code_by_drs, bs.sequence ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("shipmentName",shipmentName);
		q.addValue("sellBackType",StatementLineType.getSellbackTypes());

		return (List) getNamedParameterJdbcTemplate().query(sql,q,(rs,rowNum) -> new PaymentRecordReportAmountDetailLineItemImpl(
				rs.getInt("id"),rs.getString("sku"),
				rs.getString("statement_name"),rs.getString("item_name"),
				rs.getInt("quantity"),
				rs.getInt("currency_id"),rs.getBigDecimal("amount")
		));
	}

}
