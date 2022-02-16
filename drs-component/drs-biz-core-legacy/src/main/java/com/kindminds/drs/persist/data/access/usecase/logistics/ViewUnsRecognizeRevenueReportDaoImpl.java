package com.kindminds.drs.persist.data.access.usecase.logistics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;





import com.kindminds.drs.persist.data.access.rdb.Dao;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import com.kindminds.drs.api.data.access.usecase.logistics.ViewUnsRecognizeRevenueReportDao;

@Repository
public class ViewUnsRecognizeRevenueReportDaoImpl extends Dao implements ViewUnsRecognizeRevenueReportDao {
	

	
	@Override @SuppressWarnings("unchecked")
	public List<Object[]> queryUnsSkuPaymentDetails(String statementName,String incoterm) {
		String sql = "select "
				+ "bsi.reference, "
				+ "ps.code_by_drs, "
				+ "sum(case when tlt.name = 'MS2SS_UNIT_DDP_PAYMENT' then bsi.quantity else 0 end) as quantity_payment, "
				+ "sum(case when tlt.name = 'MS2SS_UNIT_DDP_REFUND'  then bsi.quantity else 0 end) as quantity_refund, "
				+ "sum(bsi.statement_amount) "
				+ "from bill_statement bs "
				+ "inner join bill_statementlineitem bsi on bsi.statement_id = bs.id "
				+ "inner join product_sku ps on ps.id = bsi.product_sku_id "
				+ "inner join shipment uns on uns.name = bsi.reference "
				+ "inner join shipment_uns_info unsi on unsi.shipment_id = uns.id "
				+ "inner join transactionlinetype tlt on tlt.id = bsi.transactionlinetype_id "
				+ "where bs.name = :statementName "
				+ "and unsi.incoterm = :incoterm "
				+ "and tlt.name in ('MS2SS_UNIT_DDP_PAYMENT','MS2SS_UNIT_DDP_REFUND') "
				+ "group by uns.serial_id,bsi.reference,ps.code_by_drs "
				+ "order by uns.serial_id,code_by_drs";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName",statementName);
		q.addValue("incoterm",incoterm);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		return columnsList;
	}

	@Override @SuppressWarnings("unchecked")
	public List<Object []> queryUnsSkuPriceInfo(String statementName,String incoterm) {
		String sql = "select distinct "
				+ "bsi.reference, "
				+ "ps.code_by_drs, "
				+ "unsli.unit_amount, "
				+ "unslii.unit_cif_amount "
				+ "from bill_statement bs "
				+ "inner join bill_statementlineitem bsi on bsi.statement_id = bs.id "
				+ "inner join product_sku ps on ps.id = bsi.product_sku_id "
				+ "inner join shipment uns on uns.name = bsi.reference "
				+ "inner join shipment_uns_info unsi on unsi.shipment_id = uns.id "
				+ "inner join shipment_line_item unsli on (unsli.shipment_id=uns.id and unsli.sku_id=bsi.product_sku_id) "
				+ "inner join transactionlinetype tlt on tlt.id = bsi.transactionlinetype_id "
				+ "left join shipment_line_item_info_uns unslii on unslii.shipment_line_item_id = unsli.id "
				+ "where bs.name = :statementName "
				+ "and unsi.incoterm = :incoterm "
				+ "and tlt.name in ('MS2SS_UNIT_DDP_PAYMENT','MS2SS_UNIT_DDP_REFUND') ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statementName",statementName);
		q.addValue("incoterm",incoterm);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		return columnsList;
	}

	@Override @SuppressWarnings("unchecked")
	public Map<String,String> queryUnsInvoiceNumber(Set<String> unsNames) {
		if(unsNames.isEmpty()) return null;
		String sql = "select uns.name, uns.invoice_number "
				+ "from shipment uns "
				+ "where uns.name in (:unsNames) ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("unsNames", unsNames);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		Map<String,String> unsInvoiceNumber = new HashMap<>();
		for(Object[] columns:columnsList){
			String unsName = (String)columns[0];
			String invoiceNumber = (String)columns[1];
			unsInvoiceNumber.put(unsName, invoiceNumber);
		}
		return unsInvoiceNumber;
	}
	
}
