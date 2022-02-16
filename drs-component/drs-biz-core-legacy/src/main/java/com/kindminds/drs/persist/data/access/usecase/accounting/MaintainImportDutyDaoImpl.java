package com.kindminds.drs.persist.data.access.usecase.accounting;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;





import com.kindminds.drs.persist.data.access.rdb.Dao;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.kindminds.drs.api.data.access.usecase.accounting.MaintainImportDutyDao;
import com.kindminds.drs.api.v1.model.accounting.ImportDutyTransaction;
import com.kindminds.drs.api.v1.model.accounting.ImportDutyTransaction.ImportDutyTransactionLineItem;
import com.kindminds.drs.persist.v1.model.mapping.accounting.ImportDutyImpl;
import com.kindminds.drs.persist.v1.model.mapping.accounting.ImportDutyLineItemImpl;
import com.kindminds.drs.persist.data.access.rdb.util.PostgreSQLHelper;

@Repository
public class MaintainImportDutyDaoImpl extends Dao implements MaintainImportDutyDao {
	


	@Override @Transactional("transactionManager")
	public String insert(ImportDutyTransaction duty,Date transactionDate,BigDecimal totalAmount) {		
		int idtId = PostgreSQLHelper.getNextVal(getNamedParameterJdbcTemplate(),"import_duty_transaction", "id");
		String sql = "insert into import_duty_transaction "
				+ "(    id, shipment_uns_id, transaction_date,    currency_id, amount_total ) "
				+ "select "
				+ " :idtId,          uns.id, :transactionDate, ct.currency_id, :totalAmount "
				+ "from shipment uns "
				+ "inner join country ct on ct.id = uns.destination_country_id "
				+ "where uns.name = :unsName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("idtId", idtId);
		q.addValue("transactionDate", transactionDate);
		q.addValue("totalAmount", totalAmount);
		q.addValue("unsName", duty.getUnsName());
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
		this.insertLineItems(duty.getUnsName(), duty.getLineItems());
		return duty.getUnsName();
	}
	
	@Transactional("transactionManager")
	private void insertLineItems(String unsName,List<ImportDutyTransactionLineItem> lineItems) {
		String sql = "insert into import_duty_transaction_line_item "
				+ "( transaction_id, shipment_uns_line_item_id,  amount ) "
				+ "select "
				+ "          idt.id,                  unsli.id, :amount "
				+ "from import_duty_transaction idt "
				+ "inner join shipment uns on uns.id = idt.shipment_uns_id "
				+ "inner join shipment_line_item unsli on unsli.shipment_id = uns.id "
				+ "inner join shipment ivs on ivs.id = unsli.source_shipment_id "
				+ "inner join product_sku ps on ps.id = unsli.sku_id "
				+ "where uns.name = :unsName "
				+ "and ivs.name = :ivsName "
				+ "and ps.code_by_drs = :sku ";
		for(ImportDutyTransactionLineItem item:lineItems){
			MapSqlParameterSource q = new MapSqlParameterSource();
			q.addValue("amount", new BigDecimal(item.getAmount()));
			q.addValue("unsName", unsName);
			q.addValue("ivsName", item.getSourceIvsName());
			q.addValue("sku", item.getSku());
			Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
		}
		return;
	}

	@Override @Transactional("transactionManager")
	public String update(ImportDutyTransaction t, Date transactionDate, BigDecimal totalAmount) {
		String sql = "update import_duty_transaction idt set "
				+ " transaction_date = :transactionDate, "
				+ "     amount_total = :totalAmount "
				+ "from shipment uns "
				+ "where uns.id = idt.shipment_uns_id "
				+ "and uns.name = :unsName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("transactionDate", transactionDate);
		q.addValue("totalAmount", totalAmount);
		q.addValue("unsName", t.getUnsName());
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
		this.deleteLineItems(t.getUnsName());
		this.insertLineItems(t.getUnsName(), t.getLineItems());
		return t.getUnsName();
	}
	
	private void deleteLineItems(String unsName){
		String sql = "delete from import_duty_transaction_line_item idtli "
				+ "using import_duty_transaction idt, shipment uns "
				+ "where idt.id = idtli.transaction_id "
				+ "and uns.id = idt.shipment_uns_id "
				+ "and uns.name = :unsName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("unsName", unsName);
		getNamedParameterJdbcTemplate().update(sql,q);
		return;
	}

	@Override @SuppressWarnings("unchecked")
	public ImportDutyTransaction query(String unsName) {
		String sql = "select                                   idt.id as id, "
				+ "                                          uns.name as shipment_uns_name, "
				+ "to_char(idt.transaction_date at time zone :tz,:fm) as transaction_date_utc, "
				+ "                                           ct.code as country, "
				+ "                                   idt.currency_id as currency_id, "
				+ "                                  idt.amount_total as amount_total "
				+ "from import_duty_transaction idt "
				+ "inner join shipment uns on uns.id = idt.shipment_uns_id "
				+ "inner join country ct on ct.id = uns.destination_country_id "
				+ "where uns.name = :unsName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("tz","UTC");
		q.addValue("fm","YYYY-MM-DD");
		q.addValue("unsName", unsName);

		List<ImportDutyImpl> resultList = getNamedParameterJdbcTemplate().query(sql,q,(rs,rowNum) -> new ImportDutyImpl(
				rs.getInt("id"),rs.getString("shipment_uns_name"),rs.getString("transaction_date_utc") ,
				rs.getString("country"),rs.getInt("currency_id"),rs.getBigDecimal("amount_total"))
		);
		Assert.isTrue(resultList.size()==1);
		ImportDutyImpl result = resultList.get(0);
		result.setLineItems(this.queryLineItems(unsName));
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private List<ImportDutyTransactionLineItem> queryLineItems(String unsName){
		String sql = "select idtli.id as id,"
				+ "          ivs.name as source_ivs_name, "
				+ "    ps.code_by_drs as sku, "
				+ "   unsli.qty_order as quantity, "
				+ "      idtli.amount as amount "
				+ "from import_duty_transaction_line_item idtli "
				+ "inner join import_duty_transaction idt on idt.id = idtli.transaction_id "
				+ "inner join shipment uns on uns.id = idt.shipment_uns_id "
				+ "inner join shipment_line_item unsli on unsli.id = idtli.shipment_uns_line_item_id "
				+ "inner join shipment ivs on ivs.id = unsli.source_shipment_id "
				+ "inner join product_sku ps on ps.id = unsli.sku_id "
				+ "where uns.name = :unsName order by ivs.name, ps.code_by_drs ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("unsName", unsName);

		return	(List) getNamedParameterJdbcTemplate().query(sql,q , (rs,rowNum) -> new ImportDutyLineItemImpl(
				rs.getInt("id") ,rs.getString("source_ivs_name"), rs.getString("sku") , rs.getInt("quantity"),
				rs.getBigDecimal("amount")
		));
	}

	@Override @SuppressWarnings("unchecked")
	public List<ImportDutyTransactionLineItem> queryLineItemInfoForCreate(String unsName) {
		String sql = "select unsli.id as id,"
				+ "          ivs.name as source_ivs_name, "
				+ "    ps.code_by_drs as sku, "
				+ "   unsli.qty_order as quantity, "
				+ "              NULL as amount "
				+ "from shipment uns "
				+ "inner join shipment_line_item unsli on unsli.shipment_id = uns.id "
				+ "inner join shipment ivs on ivs.id = unsli.source_shipment_id "
				+ "inner join product_sku ps on ps.id = unsli.sku_id "
				+ "where uns.name = :unsName order by ivs.name, ps.code_by_drs ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("unsName", unsName);

		return	(List) getNamedParameterJdbcTemplate().query(sql,q , (rs,rowNum) -> new ImportDutyLineItemImpl(
				rs.getInt("id") ,rs.getString("source_ivs_name"), rs.getString("sku") , rs.getInt("quantity"),
				rs.getBigDecimal("amount")
		));
	}

	@Override @SuppressWarnings("unchecked")
	public List<String> queryUnsNameList() {
		String sql = "select uns.name "
				+ "from shipment uns "
				+ "inner join shipment_uns_info ui on ui.shipment_id = uns.id "
				+ "left join import_duty_transaction idt on idt.shipment_uns_id = uns.id "
				+ "where idt.id is NULL "
				+ "order by uns.serial_id ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		List<String> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		return resultList;
	}

	@Override
	public String queryCountry(String unsName) {
		String sql = "select ct.code from shipment uns "
				+ "inner join country ct on ct.id = uns.destination_country_id "
				+ "where uns.name = :unsName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("unsName", unsName);
		String result = getNamedParameterJdbcTemplate().queryForObject(sql,q,String.class);
		Assert.notNull(result);
		return result;
	}

	@Override @Transactional("transactionManager")
	public void delete(String unsName) {
		this.deleteLineItems(unsName);
		String sql = "delete from import_duty_transaction idt using shipment uns "
				+ "where uns.id = idt.shipment_uns_id "
				+ "and uns.name = :unsName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("unsName", unsName);
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
	}

	@Override
	public int queryTotalCount() {
		String sql = "select count(*) from import_duty_transaction ";

		BigInteger count = getJdbcTemplate().queryForObject(sql,BigInteger.class);
		return count.intValue();
	}

	@Override @SuppressWarnings("unchecked")
	public List<ImportDutyTransaction> queryList(int startIndex, int size) {
		String sql = "select                                   idt.id as id, "
				+ "                                          uns.name as shipment_uns_name, "
				+ "to_char(idt.transaction_date at time zone :tz,:fm) as transaction_date_utc, "
				+ "                                           ct.code as country, "
				+ "                                   idt.currency_id as currency_id, "
				+ "                                  idt.amount_total as amount_total "
				+ "from import_duty_transaction idt "
				+ "inner join shipment uns on uns.id = idt.shipment_uns_id "
				+ "inner join country ct on ct.id = uns.destination_country_id "
				+ "order by uns.serial_id desc "
				+ "limit :size offset :start ";
		MapSqlParameterSource q = new MapSqlParameterSource();


		q.addValue("tz","UTC");
		q.addValue("fm","YYYY-MM-DD");
		q.addValue("size", size);
		q.addValue("start", startIndex-1);

		List<ImportDutyImpl> resultList = getNamedParameterJdbcTemplate().query(sql,q,(rs,rowNum) -> new ImportDutyImpl(
				rs.getInt("id"),rs.getString("shipment_uns_name"),rs.getString("transaction_date_utc") ,
				rs.getString("country"),rs.getInt("currency_id"),rs.getBigDecimal("amount_total"))
		);

		return (List)resultList;
	}

}
