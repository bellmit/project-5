package com.kindminds.drs.persist.data.access.usecase.accounting;

import com.kindminds.drs.api.data.access.usecase.accounting.MaintainInternationalTransactionDao;
import com.kindminds.drs.persist.data.access.rdb.Dao;
import com.kindminds.drs.api.v1.model.accounting.InternationalTransaction;
import com.kindminds.drs.api.v1.model.accounting.InternationalTransaction.InternationalTransactionLineItem;
import com.kindminds.drs.persist.data.access.rdb.util.PostgreSQLHelper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;




import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

@Repository
public class MaintainInternationalTransactionDaoImpl extends Dao implements MaintainInternationalTransactionDao {
	
	
	
	@Override @SuppressWarnings("unchecked")
	public Map<String,String> queryMsdcKcodeNameMap(String drsUserCompanyKcode){
		String sql = "select msdc.k_code,msdc.short_name_en_us from company msdc where msdc.k_code != :drsUserCompanyKcode and msdc.is_drs_company is TRUE ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("drsUserCompanyKcode", drsUserCompanyKcode);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		Map<String,String> keyNameMap = new TreeMap<>();
		for(Object[] columns:columnsList){
			String kcode = (String)columns[0];
			String name = (String)columns[1];
			keyNameMap.put(kcode, name);
		}
		return keyNameMap;
	}
	
	@Override @SuppressWarnings("unchecked")
	public Map<String,String> querySsdcKcodeNameMap(String drsUserCompanyKcode){
		String sql = "select ssdc.k_code,ssdc.short_name_en_us from company ssdc where ssdc.k_code = :drsUserCompanyKcode ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("drsUserCompanyKcode", drsUserCompanyKcode);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		Map<String,String> keyNameMap = new TreeMap<>();
		for(Object[] columns:columnsList){
			String kcode = (String)columns[0];
			String name = (String)columns[1];
			keyNameMap.put(kcode, name);
		}
		return keyNameMap;
	}
	
	@Override @SuppressWarnings("unchecked")
	public Map<Integer,String> queryLineItemKeyNameMap(int cashFlowDirectionKey){
		String sql = "select id, name from transactionlinetype tlt where tlt.class = :tltClass and tlt.cash_flow_direction_key = :cash_flow_direction_key ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("tltClass", "International");
		q.addValue("cash_flow_direction_key",cashFlowDirectionKey);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		Map<Integer,String> keyNameMap = new TreeMap<>();
		for(Object[] columns:columnsList){
			Integer key = (Integer)columns[0];
			String name = (String)columns[1];
			keyNameMap.put(key, name);
		}
		return keyNameMap;
	}

	@Override
	public int queryTotalCounts() {
		String sql = "select count(1) from international_transaction ";

		return getJdbcTemplate().queryForObject(sql,BigInteger.class).intValue();
	}

	@Override @SuppressWarnings("unchecked")
	public List<Object[]> queryList(int startIndex, int size) {
		String sql = "select            it.id as id, "
				+ "       it.transaction_date as transaction_date, "
				+ "it.cash_flow_direction_key as cash_flow_direction_key, "
				+ "               msdc.k_code as msdcKcode, "
				+ "     msdc.short_name_en_us as msdcName, "
				+ "               ssdc.k_code as ssdcKcode, "
				+ "     ssdc.short_name_en_us as ssdcName, "
				+ "               splr.k_code as splrKcode, "
				+ "     splr.short_name_en_us as splrName, "
				+ "            it.currency_id as currency_id, "
				+ "                  it.total as total "
				+ "from international_transaction it  "
				+ "inner join company msdc on msdc.id = it.msdc_company_id "
				+ "inner join company ssdc on ssdc.id = it.ssdc_company_id "
				+ "inner join company splr on splr.id = it.splr_company_id "
				+ "order by it.transaction_date desc, msdc.k_code, ssdc.k_code, splr.k_code "
				+ "limit :size offset :start ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("size", size);
		q.addValue("start", startIndex-1);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		return columnsList;
	}
	
	@Override @SuppressWarnings("unchecked")
	public Object [] query(int id) {
		String sql = "select            it.id as id, "
				+ "       it.transaction_date as transaction_date, "
				+ "it.cash_flow_direction_key as cash_flow_direction_key, "
				+ "               msdc.k_code as msdcKcode, "
				+ "     msdc.short_name_en_us as msdcName, "
				+ "               ssdc.k_code as ssdcKcode, "
				+ "     ssdc.short_name_en_us as ssdcName, "
				+ "               splr.k_code as splrKcode, "
				+ "     splr.short_name_en_us as splrName, "
				+ "            it.currency_id as currency_id, "
				+ "                  it.total as total, "
				+ "                   it.note as note "
				+ "from international_transaction it  "
				+ "inner join company msdc on msdc.id = it.msdc_company_id "
				+ "inner join company ssdc on ssdc.id = it.ssdc_company_id "
				+ "inner join company splr on splr.id = it.splr_company_id "
				+ "where it.id = :id ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("id", id);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		Assert.isTrue(columnsList.size()==1);
		Object[] columns = columnsList.get(0);
		return columns;

	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Object []> queryLineItems(int transactionId){
		String sql = "select itli.line_seq as line_seq, "
				+ "           itli.type_id as itemKey, "
				+ "               tlt.name as itemName, "
				+ "               itli.note as itemNote, "
				+ "          itli.subtotal as subtotal, "
				+ "         it.currency_id as currency_id, "
				+ "         itli.vat_rate as vat_rate, "
				+ "         itli.vat_amount as vat_amount "
				+ "from international_transaction_line_item itli "
				+ "inner join international_transaction it on it.id = itli.transaction_id "
				+ "inner join transactionlinetype tlt on tlt.id = itli.type_id "
				+ "where itli.transaction_id = :transactionId order by line_seq ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("transactionId",transactionId);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		return columnsList;
	}

	@Override @Transactional("transactionManager")
	public Integer insert(InternationalTransaction t,Date date,BigDecimal total) {
		String sql = "insert into international_transaction "
				+ "(  id,  transaction_date,  cash_flow_direction_key, msdc_company_id, ssdc_company_id, splr_company_id, currency_id,  total,  note ) select  "
				+ "  :id, :transaction_date, :cash_flow_direction_key,         msdc.id,         ssdc.id,         splr.id,      cur.id, :total, :note "
				+ "from company msdc "
				+ "inner join country ct on ct.id = msdc.country_id "
				+ "inner join currency cur on cur.id = ct.currency_id "
				+ "inner join company ssdc on ssdc.k_code = :ssdcKcode "
				+ "inner join company splr on splr.k_code = :splrKcode "
				+ "where msdc.k_code = :msdcKcode ";
		System.out.println(sql);
		int id = PostgreSQLHelper.getNextVal(getNamedParameterJdbcTemplate(), "international_transaction", "id");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("id",id);
		q.addValue("transaction_date",date );
		q.addValue("cash_flow_direction_key",t.getCashFlowDirectionKey());
		q.addValue("msdcKcode",t.getMsdcKcode());
		q.addValue("ssdcKcode",t.getSsdcKcode());
		q.addValue("splrKcode",t.getSplrKcode());
		q.addValue("total",total);
		q.addValue("note",t.getNote());
		//entityManager.joinTransaction();
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
		this.insertLineItems(id, t.getLineItems());
		return id;
	}
	
	private void insertLineItems(int transactionId,List<InternationalTransactionLineItem> lineItems){
		String sql = "insert into international_transaction_line_item "
				+ "(  line_seq,  transaction_id,  type_id,  note,  subtotal,  vat_rate,  vat_amount ) values "
				+ "( :line_seq, :transaction_id, :type_id, :note, :subtotal, :vat_rate, :vat_amount ) ";
		for(InternationalTransactionLineItem item:lineItems){

			MapSqlParameterSource q = new MapSqlParameterSource();
			q.addValue("line_seq",item.getLineSeq());
			q.addValue("transaction_id",transactionId);
			q.addValue("type_id", item.getItemKey());
			q.addValue("note", item.getItemNote());
			q.addValue("subtotal", new BigDecimal(item.getSubtotal()));
			q.addValue("vat_rate", item.getVatRate() == null?
					BigDecimal.ZERO : new BigDecimal(item.getVatRate()));
			q.addValue("vat_amount", item.getVatAmount() == null?
					BigDecimal.ZERO : new BigDecimal(item.getVatAmount()));
			Assert.isTrue(this.getNamedParameterJdbcTemplate().update(sql,q)==1);

		}
	}

	@Override @Transactional("transactionManager")
	public Integer update(InternationalTransaction t,Date date,BigDecimal total) {
		//entityManager.joinTransaction();
		this.deleteLineItems(t.getId());
		String sql = "update international_transaction it set "
				+ "       transaction_date = :transaction_date, "
				+ "cash_flow_direction_key = :cash_flow_direction_key, "
				+ "        msdc_company_id = msdc.id, "
				+ "        ssdc_company_id = ssdc.id, "
				+ "        splr_company_id = splr.id, "
				+ "            currency_id = ct.currency_id, "
				+ "                  total = :total,"
				+ "                   note = :note "
				+ "from company msdc, company ssdc, company splr, country ct "
				+ "where it.id = :id "
				+ "and msdc.k_code = :msdcKcode "
				+ "and ssdc.k_code = :ssdcKcode "
				+ "and splr.k_code = :splrKcode "
				+ "and ct.id = msdc.country_id ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("id",t.getId());
		q.addValue("transaction_date",date );
		q.addValue("cash_flow_direction_key",t.getCashFlowDirectionKey());
		q.addValue("msdcKcode",t.getMsdcKcode());
		q.addValue("ssdcKcode",t.getSsdcKcode());
		q.addValue("splrKcode",t.getSplrKcode());
		
		q.addValue("total",total);
		q.addValue("note",t.getNote());
		int affectedRows = getNamedParameterJdbcTemplate().update(sql,q);
		Assert.isTrue(affectedRows==1);
		this.insertLineItems(t.getId(),t.getLineItems());
		return t.getId();
	}

	@Override @Transactional("transactionManager")
	public void delete(int id) {
		//entityManager.joinTransaction();
		this.deleteLineItems(id);
		String sql = "delete from international_transaction where id = :id ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("id", id);
		int deletedRows = getNamedParameterJdbcTemplate().update(sql,q);
		Assert.isTrue(deletedRows==1);
	}
	
	private void deleteLineItems(int transactionId){
		String sql = "delete from international_transaction_line_item where transaction_id = :transactionId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("transactionId", transactionId);
		int deletedRows =  getNamedParameterJdbcTemplate().update(sql,q);
		Assert.isTrue(deletedRows>=1);
	}
	
	@Override
	public Date queryLastSettlementEnd() {
		String sql = "select max(period_end) from bill_statement ";

		return getJdbcTemplate().queryForObject(sql,Date.class);
	}

	@Override
	public int queryTotalCountsMonthlyStorageFee(String year,String month) {
		String sql = "select count(1) from international_transaction where note = :note";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("note",year+"-"+month+" FBA_MONTHLY_STORAGE");
		return getNamedParameterJdbcTemplate().queryForObject(sql,q,BigInteger.class).intValue();
	}

	@Override
	public int countCouponTransactionsProcessed() {
		String sql = "select count(*) " +
				"from international_transaction it " +
				"where " +
				"it.transaction_date >= (select max(period_end) from bill_statement) and " +
				"it.note like '%Coupon Redemption Fee'";

		return getJdbcTemplate().queryForObject(sql,BigInteger.class).intValue();
	}

}
