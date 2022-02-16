package com.kindminds.drs.persist.data.access.usecase.accounting;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


import com.kindminds.drs.api.data.access.usecase.accounting.MaintainDomesticTransactionDao;
import com.kindminds.drs.api.v1.model.accounting.DomesticTransaction;
import com.kindminds.drs.persist.data.access.rdb.Dao;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;




import com.kindminds.drs.persist.data.access.rdb.util.PostgreSQLHelper;

@Repository
public class MaintainDomesticTransactionDaoImpl extends Dao implements MaintainDomesticTransactionDao {
	
	

	@Override @SuppressWarnings("unchecked")
	public Map<Integer, String> queryLineItemTypeKeyName(String typeClassName) {
		String sql = "select id, name from transactionlinetype tlt where tlt.class = :tltClass ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("tltClass", typeClassName);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		Map<Integer,String> keyNameMap = new TreeMap<>();
		for(Object[] columns:columnsList){
			Integer key = (Integer)columns[0];
			String name = (String)columns[1];
			keyNameMap.put(key, name);
		}
		return keyNameMap;
	}

	@Override @Transactional("transactionManager")
	public Integer insert(DomesticTransaction t, Date date, BigDecimal taxRate, int currencyId, BigDecimal subtotal, BigDecimal tax, BigDecimal total) {
		String sql = "insert into domestic_transaction "
				+ "(       id, transaction_date, ssdc_company_id, splr_company_id,  invoice_number,  tax_rate,  currency_id, amount_subtotal, amount_tax, amount_total ) "
				+ "select :id,            :date,         ssdc.id,         splr.id, :invoice_number, :tax_rate, :currency_id,       :subtotal,       :tax,       :total "
				+ "from company ssdc, company splr "
				+ "where true "
				+ "and ssdc.k_code = :ssdcKcode "
				+ "and splr.k_code = :splrKcode ";
		int id = PostgreSQLHelper.getNextVal(getNamedParameterJdbcTemplate(),"domestic_transaction","id");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("id",id);
		q.addValue("date",date);
		q.addValue("invoice_number",t.getInvoiceNumber());
		q.addValue("tax_rate",taxRate);
		q.addValue("currency_id", currencyId);
		q.addValue("subtotal",subtotal);
		q.addValue("tax",tax);
		q.addValue("total",total);
		q.addValue("ssdcKcode",t.getSsdcKcode());
		q.addValue("splrKcode",t.getSplrKcode());
		//entityManager.joinTransaction();
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
		this.insertLineItems(id, t.getLineItems());
		return id;
	}
	
	private void insertLineItems(int transactionId,List<DomesticTransaction.DomesticTransactionLineItem> lineItems){
		String sql = "insert into domestic_transaction_line_item "
				+ "(  transaction_id,  line_seq,  type_id,  note,  amount ) values "
				+ "( :transaction_id, :line_seq, :type_id, :note, :amount ) ";
		for(DomesticTransaction.DomesticTransactionLineItem item:lineItems){
			MapSqlParameterSource q = new MapSqlParameterSource();
			q.addValue("transaction_id",transactionId);
			q.addValue("line_seq",item.getLineSeq());
			q.addValue("type_id", item.getItemKey());
			q.addValue("note", item.getNote());
			q.addValue("amount", new BigDecimal(item.getAmount()));
			Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
		}
	}

	@Override @SuppressWarnings("unchecked")
	public Object[] query(int id) {
		String sql = "select       dt.id as id, "
				+ "  dt.transaction_date as date, "
				+ "          ssdc.k_code as ssdcKcode, "
				+ "ssdc.short_name_en_us as ssdcName, "
				+ "          splr.k_code as splrKcode, "
				+ "splr.short_name_en_us as splrName, "
				+ "    dt.invoice_number as invoiceNumber, "
				+ "          dt.tax_rate as taxRate, "
				+ "       dt.currency_id as currencyId, "
				+ "   dt.amount_subtotal as subtotal, "
				+ "        dt.amount_tax as tax, "
				+ "      dt.amount_total as total "
				+ "from domestic_transaction dt "
				+ "inner join company ssdc on ssdc.id = dt.ssdc_company_id "
				+ "inner join company splr on splr.id = dt.splr_company_id "
				+ "where dt.id = :id ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("id", id);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		Assert.isTrue(columnsList.size()==1);
		Object[] columns = columnsList.get(0);

		return columns;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> queryLineItems(int transactionId){
		String sql = "select dtli.line_seq as line_seq, "
				+ "           dtli.type_id as itemKey, "
				+ "               tlt.name as itemName, "
				+ "              dtli.note as note, "
				+ "            dtli.amount as amount "
				+ "from domestic_transaction_line_item dtli "
				+ "inner join domestic_transaction dt on dt.id = dtli.transaction_id "
				+ "inner join transactionlinetype tlt on tlt.id = dtli.type_id "
				+ "where dtli.transaction_id = :transactionId order by line_seq ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("transactionId",transactionId);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);

		return columnsList;
	}

	@Override
	public int queryCurrencyId(String companyKcode) {
		String sql = "select ct.currency_id from company com "
				+ "inner join country ct on ct.id = com.country_id "
				+ "where com.k_code = :companyKcode ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("companyKcode", companyKcode);
		return getNamedParameterJdbcTemplate().queryForObject(sql,q,Integer.class);
	}

	@Override
	public int queryTotalCounts() {
		String sql = "select count(1) from domestic_transaction ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		return getNamedParameterJdbcTemplate().queryForObject(sql,q,BigInteger.class).intValue();
	}

	@Override @SuppressWarnings("unchecked")
	public List<Object[]> queryList(int startRowNum, int pageSize) {
		String sql = "select       dt.id as id, "
				+ "  dt.transaction_date as date, "
				+ "          ssdc.k_code as ssdcKcode, "
				+ "ssdc.short_name_en_us as ssdcName, "
				+ "          splr.k_code as splrKcode, "
				+ "splr.short_name_en_us as splrName, "
				+ "    dt.invoice_number as invoiceNumber, "
				+ "       dt.currency_id as currencyId, "
				+ "      dt.amount_total as total "
				+ "from domestic_transaction dt "
				+ "inner join company ssdc on ssdc.id = dt.ssdc_company_id "
				+ "inner join company splr on splr.id = dt.splr_company_id "
				+ "order by dt.transaction_date desc, splr.k_code "
				+ "limit :size offset :start ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("size", pageSize);
		q.addValue("start", startRowNum-1);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);


		return columnsList;
	}

	@Override @Transactional("transactionManager")
	public void delete(int id) {
		//entityManager.joinTransaction();
		this.deleteLineItems(id);
		String sql = "delete from domestic_transaction where id = :id ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("id", id);
		int deletedRows = getNamedParameterJdbcTemplate().update(sql,q);
		Assert.isTrue(deletedRows==1);
	}
	
	private void deleteLineItems(int transactionId){
		String sql = "delete from domestic_transaction_line_item where transaction_id = :transactionId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("transactionId", transactionId);
		int deletedRows = getNamedParameterJdbcTemplate().update(sql,q);
		Assert.isTrue(deletedRows>=1);
	}

	@Override @Transactional("transactionManager")
	public Integer update(DomesticTransaction t, Date date, BigDecimal taxRate, int currencyId, BigDecimal subtotal, BigDecimal tax, BigDecimal total) {

		//entityManager.joinTransaction();
		this.deleteLineItems(t.getId());
		String sql = "update domestic_transaction dt set "
				+ " transaction_date = :date,"
				+ "  ssdc_company_id = ssdc.id, "
				+ "  splr_company_id = splr.id, "
				+ "   invoice_number = :invoice_number, "
				+ "         tax_rate = :tax_rate, "
				+ "      currency_id = :currency_id, "
				+ "  amount_subtotal = :subtotal, "
				+ "       amount_tax = :tax, "
				+ "     amount_total = :total "
				+ "from company ssdc, company splr "
				+ "where dt.id = :id "
				+ "and ssdc.k_code = :ssdcKcode "
				+ "and splr.k_code = :splrKcode ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("id",t.getId());
		q.addValue("date",date);
		q.addValue("invoice_number",t.getInvoiceNumber());
		q.addValue("tax_rate",taxRate);
		q.addValue("currency_id", currencyId);
		q.addValue("subtotal",subtotal);
		q.addValue("tax",tax);
		q.addValue("total",total);
		q.addValue("ssdcKcode",t.getSsdcKcode());
		q.addValue("splrKcode",t.getSplrKcode());
		int affectedRows = getNamedParameterJdbcTemplate().update(sql,q);
		Assert.isTrue(affectedRows==1);
		this.insertLineItems(t.getId(), t.getLineItems());
		return t.getId();
	}

	@Override @Transactional("transactionManager")
	public Integer updateInvoiceNumber(DomesticTransaction t) {
		String sql = "update domestic_transaction dt set "
				+ " invoice_number = :invoice_number "
				+ " where dt.id = :id ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("invoice_number", t.getInvoiceNumber());
		q.addValue("id", t.getId());
		int affectedRows = getNamedParameterJdbcTemplate().update(sql,q);
		Assert.isTrue(affectedRows==1);
		return t.getId();
	}

	@Override
	public Date queryLastSettlementEnd() {
		String sql = "select max(period_end) from bill_statement ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		return getNamedParameterJdbcTemplate().queryForObject(sql,q,Date.class);
	}

	@Override
	public Boolean queryBillStatementExists(Date transactionDate) {
		String sql = " SELECT EXISTS( SELECT 1 from bill_statement \n"+
				" WHERE period_end > :transactionDate )";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("transactionDate", transactionDate);
		return getNamedParameterJdbcTemplate().queryForObject(sql,q,Boolean.class);
	}



}
