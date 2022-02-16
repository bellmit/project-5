package com.kindminds.drs.persist.data.access.rdb;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kindminds.drs.api.data.access.rdb.EmailReminderDao;
import com.kindminds.drs.api.v1.model.accounting.SupplierLongTermStorageFee;
import com.kindminds.drs.persist.v1.model.mapping.accounting.SupplierLongTermStorageFeeImpl;

@Repository
public class EmailReminderDaoImpl extends Dao implements EmailReminderDao {
	

	
	@Override
	public Map<String, String> queryIncludedLongTermFeeReminder() {
		StringBuilder sqlSb = new StringBuilder()
				.append("SELECT co.k_code, co.short_name_en_us ")
				.append("FROM email_reminder er ")
				.append("INNER JOIN company co ON co.id = er.company_id ")
				.append("WHERE er.long_term_storage_fee = TRUE ")
				.append("AND co.is_supplier = TRUE ")
				.append("ORDER BY co.k_code ");
		return convertResultsToMap(sqlSb.toString());
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, String> convertResultsToMap(String selectSuppliers) {
		MapSqlParameterSource q = new MapSqlParameterSource();
		List<Object[]> records = getNamedParameterJdbcTemplate().query(selectSuppliers,q,objArrayMapper);
		Map<String, String> suppliers = new TreeMap<String, String>();
		for (Object[] record : records) {
			suppliers.put((String)record[0], (String)record[1]);
		}
		return suppliers;
	}
	
	@Override
	public Map<String, String> queryExcludedLongTermFeeReminder() {
		StringBuilder sqlSb = new StringBuilder()
				.append("SELECT co.k_code, co.short_name_en_us ")
				.append("FROM email_reminder er ")
				.append("INNER JOIN company co ON co.id = er.company_id ")
				.append("WHERE er.long_term_storage_fee = FALSE ")
				.append("AND co.is_supplier = TRUE ")
				.append("ORDER BY co.k_code ");
		return convertResultsToMap(sqlSb.toString());
	}
	
	@Override @Transactional("transactionManager")
	public int updateLongTermStorageReminder(List<String> kCodes) {
		if (kCodes == null || kCodes.isEmpty()) {
			return 0;
		}
		StringBuilder sqlSb = new StringBuilder()
				.append("UPDATE email_reminder er SET long_term_storage_fee = NOT long_term_storage_fee ")
				.append("FROM company co ")
				.append("WHERE co.id = er.company_id ")
				.append("AND co.k_code in (:kCodes) ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("kCodes", kCodes);
		return getNamedParameterJdbcTemplate().update(sqlSb.toString(),q);
	}
	
	@Override @SuppressWarnings("unchecked")
	public List<SupplierLongTermStorageFee> querySuppliersOverFeeLimit(List<String> kCodes, Double limit) {
		if (kCodes == null || kCodes.isEmpty()) {
			return new ArrayList<SupplierLongTermStorageFee>();
		}
		StringBuilder sqlSb = new StringBuilder()
				.append(" SELECT co.k_code, cy.code, ")
				.append(" sum(aihr.projected_ltsf_6_mo) as projected_ltsf_6_mo, ")
				.append(" sum(aihr.projected_ltsf_12_mo) as projected_ltsf_12_mo, ")
				.append(" aihr.currency FROM company co ")
				.append(" INNER JOIN product_base pb ON pb.supplier_company_id = co.id ")
				.append(" INNER JOIN product_sku ps on pb.id = ps.product_base_id ")
				.append(" INNER JOIN product_marketplace_info pmi ON pmi.product_id = ps.product_id ")
				.append(" INNER JOIN amazon_inventory_health_report aihr ON aihr.sku = pmi.marketplace_sku ")
				.append("		 AND aihr.marketplace_id = pmi.marketplace_id ")
				.append(" INNER JOIN marketplace mp ON mp.id = pmi.marketplace_id ")
				.append(" INNER JOIN country cy ON cy.id = mp.country_id ")
				.append(" WHERE co.k_code in (:kCodes) ")
				.append(" AND pmi.marketplace_id != 1 ")
				.append(" AND (aihr.projected_ltsf_6_mo != 0 ")
				.append(" OR aihr.projected_ltsf_12_mo != 0) ")
				.append(" GROUP BY co.k_code, cy.code, aihr.currency ")
				.append(" ORDER BY co.k_code ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("kCodes", kCodes);
		List<Object[]> recordList = getNamedParameterJdbcTemplate().query(sqlSb.toString(),q,objArrayMapper);
		BigDecimal cutoff = BigDecimal.valueOf(limit);
		List<SupplierLongTermStorageFee> supplierList = new ArrayList<>();
		for (Object[] record : recordList) {
			BigDecimal sixMonthFee = ((BigDecimal) record[2]).setScale(2);
			BigDecimal oneYearFee = ((BigDecimal) record[3]).setScale(2);
			if (sixMonthFee.add(oneYearFee).compareTo(cutoff) >= 0) {
				supplierList.add(new SupplierLongTermStorageFeeImpl(
					(String) record[0], (String) record[1], sixMonthFee, oneYearFee, (String) record[4]));
			}
		}
		return supplierList;
	}
	
	@Override
	public Double queryFeeToSendReminder() {
		StringBuilder sqlSb = new StringBuilder()
				.append(" SELECT long_term_storage_fee_limit ")
				.append(" FROM email_reminder_configuration ")
				.append(" WHERE id = 1 ");

		return getJdbcTemplate().queryForObject(sqlSb.toString(),BigDecimal.class).doubleValue();
	}
	
	@Override @Transactional("transactionManager")
	public int updateFeeToSendReminder(Double limit) {
		StringBuilder sqlSb = new StringBuilder()
				.append(" UPDATE email_reminder_configuration ")
				.append(" SET long_term_storage_fee_limit = :limit ")
				.append(" WHERE id = 1 ");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("limit", limit);
		return getNamedParameterJdbcTemplate().update(sqlSb.toString(),q);
	}
}
