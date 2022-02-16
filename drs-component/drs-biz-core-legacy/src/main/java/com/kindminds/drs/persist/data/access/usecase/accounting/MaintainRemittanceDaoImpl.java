package com.kindminds.drs.persist.data.access.usecase.accounting;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;





import com.kindminds.drs.persist.data.access.rdb.Dao;
import com.kindminds.drs.api.v1.model.report.RemittanceImportItem;


import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.kindminds.drs.api.data.access.usecase.accounting.MaintainRemittanceDao;
import com.kindminds.drs.api.v1.model.accounting.Remittance;
import com.kindminds.drs.api.v1.model.accounting.RemittanceSearchCondition;
import com.kindminds.drs.persist.v1.model.mapping.accounting.RemittanceImpl;
import com.kindminds.drs.persist.data.access.rdb.util.PostgreSQLHelper;

@Repository("maintainRemittanceDao")
public class MaintainRemittanceDaoImpl extends Dao implements MaintainRemittanceDao {

	//28800000ms = 8hours
	private static final long EIGHT_HOURS = 28800000;



	@Override
	public int getCount(RemittanceSearchCondition condition) {
		StringBuilder sqlSb = new StringBuilder();
		sqlSb.append("select count(1) from remittance rmt ");
		sqlSb.append("inner join company sndr on sndr.id = rmt.sender_company_id ");
		sqlSb.append("inner join company rcvr on rcvr.id = rmt.receiver_company_id ");
		sqlSb.append("where true ");
		sqlSb.append(this.composeSqlWherePart(condition));
		MapSqlParameterSource q = new MapSqlParameterSource();
		this.setQueryParameters(q,condition);
		Integer o = getNamedParameterJdbcTemplate().queryForObject(sqlSb.toString(),q,Integer.class);
		Assert.notNull(o);
		return o;
	}

	private Object composeSqlWherePart(RemittanceSearchCondition condition) {
		StringBuilder sqlSb = new StringBuilder();
		if(StringUtils.hasText(condition.getSndrKcode())) sqlSb.append("and sndr.k_code = :sndrKcode ");
		if(StringUtils.hasText(condition.getRcvrKcode())) sqlSb.append("and rcvr.k_code = :rcvrKcode ");
		return sqlSb.toString();
	}

	private void setQueryParameters(MapSqlParameterSource q  , RemittanceSearchCondition condition) {
		if(StringUtils.hasText(condition.getSndrKcode())) q.addValue("sndrKcode", condition.getSndrKcode());
		if(StringUtils.hasText(condition.getRcvrKcode())) q.addValue("rcvrKcode", condition.getRcvrKcode());
	}

	@Override @SuppressWarnings("unchecked")
	public List<Remittance> queryList(RemittanceSearchCondition condition, int startIndex, int size) {
		String sql = "select    rmt.id as id, "
				+ "      rmt.date_sent as date_sent, "
				+ "  rmt.date_received as date_rcvd, "
				+ "        sndr.k_code as sndr_company_kcode, "
				+ "        rcvr.k_code as rcvr_company_kcode, "
				+ "         rmt.amount as amount, "
				+ "    rmt.currency_id as currency_id, "
				+ "      rmt.reference as reference, "
				+ "     rmt.fee_amount as fee_amount,  "
				+ "   rmt.fee_included as fee_included,  "
				+ " rmt.statement_name as statement_name,  "
				+ "   rmt.bank_payment as bank_payment  "
				+ "from remittance rmt "
				+ "inner join company sndr on sndr.id=rmt.sender_company_id "
				+ "inner join company rcvr on rcvr.id=rmt.receiver_company_id "
				+ this.composeSqlWherePart(condition)
				+ "order by rmt.id desc "
				+ "limit :size offset :start ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		this.setQueryParameters(q, condition);
		q.addValue("size", size);
		q.addValue("start", startIndex-1);

		return (List) getNamedParameterJdbcTemplate().query(sql,q,(rs,rowNum) -> new RemittanceImpl(
				rs.getInt("id"),rs.getTimestamp("date_sent"),rs.getTimestamp("date_rcvd"),rs.getString("sndr_company_kcode"),
				rs.getString("rcvr_company_kcode"),
				rs.getBigDecimal("amount") , rs.getInt("currency_id") ,rs.getString("reference"),
				rs.getBigDecimal("fee_amount"),rs.getBoolean("fee_included"),
				rs.getString("statement_name"),rs.getBigDecimal("bank_payment")
		));
	}

	@Override @SuppressWarnings("unchecked")
	public Remittance query(int id) {
		String sql = "select    rmt.id as id, "
				+ "      rmt.date_sent as date_sent, "
				+ "  rmt.date_received as date_rcvd, "
				+ "        sndr.k_code as sndr_company_kcode, "
				+ "        rcvr.k_code as rcvr_company_kcode, "
				+ "         rmt.amount as amount, "
				+ "    rmt.currency_id as currency_id, "
				+ "      rmt.reference as reference, "
				+ "     rmt.fee_amount as fee_amount,  "
				+ "   rmt.fee_included as fee_included,  "
				+ " rmt.statement_name as statement_name,  "
				+ "   rmt.bank_payment as bank_payment  "
				+ "from remittance rmt "
				+ "inner join company sndr on sndr.id=rmt.sender_company_id "
				+ "inner join company rcvr on rcvr.id=rmt.receiver_company_id "
				+ "where rmt.id = :id ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("id", id);

		List<RemittanceImpl> list = getNamedParameterJdbcTemplate().query(sql,q,(rs,rowNum) -> new RemittanceImpl(
				rs.getInt("id"),rs.getTimestamp("date_sent"),rs.getTimestamp("date_rcvd"),rs.getString("sndr_company_kcode"),
				rs.getString("rcvr_company_kcode"),
				rs.getBigDecimal("amount") , rs.getInt("currency_id") ,rs.getString("reference"),
				rs.getBigDecimal("fee_amount"),rs.getBoolean("fee_included"),
				rs.getString("statement_name"),rs.getBigDecimal("bank_payment")
		));
		if ((list == null) || (list.size() == 0)) return null;
		return list.get(0);
	}
	
	@Override @Transactional("transactionManager")
	public int insert(Remittance remittance,Date dateRcvd,Date dateSend) {
		Assert.isTrue(remittance.getRemittanceId()<=0);
		Assert.isTrue(dateSend!=null||dateRcvd!=null);
		int id = PostgreSQLHelper.getNextVal(getNamedParameterJdbcTemplate(),"remittance","id");
		String sql="insert into remittance "
				+ "( id,  date_sent,  date_received,  reference, sender_company_id, receiver_company_id, amount, currency_id, fee_amount, fee_included, statement_name, bank_payment ) select "
				+ " :id,      :sdte,          :rdte, :reference,           sndr.id,             rcvr.id,   :amt, :currencyId, :feeAmount, :feeIncluded, :statementName, :bankPayment "
				+ "from company sndr, company rcvr "
				+ "where sndr.k_code = :sndrName and  rcvr.k_code = :rcvrName ";
		//Session session = this.entityManager.unwrap(Session.class);
		MapSqlParameterSource q = new MapSqlParameterSource();
		//SQLQuery q = session.createSQLQuery(sql);
		q.addValue("id", id);
		q.addValue("amt", new BigDecimal(remittance.getAmount()));
		q.addValue("currencyId", remittance.getCurrency().getKey());
		q.addValue("sdte", dateSend);
		q.addValue("rdte", dateRcvd);
		q.addValue("reference", remittance.getReference());
		q.addValue("sndrName", remittance.getSender());
		q.addValue("rcvrName", remittance.getReceiver());
		q.addValue("feeAmount", new BigDecimal(remittance.getFeeAmount()));
		q.addValue("feeIncluded", Boolean.valueOf(remittance.getFeeIncluded()));
		String statementName = remittance.getStatementName();
		if (!statementName.startsWith("STM-")) {
			statementName = "STM-" + statementName;
		}
		q.addValue("statementName", statementName);
		q.addValue("bankPayment", new BigDecimal(remittance.getBankPayment()));
		getNamedParameterJdbcTemplate().update(sql,q);
		return id;
	}

	@Override @Transactional("transactionManager")
	public Integer autoInsertFromStatement(Date date) {
		if (autoInsertDataExists(date)) return 0;
		String sql = "INSERT INTO remittance ("
				+ " date_sent, date_received, sender_company_id, "
				+ " receiver_company_id, amount, currency_id, reference)"
				+ " 	(select (bs.period_end + interval '10' day), "
				+ " 	(bs.period_end + interval '10' day), "
				+ " 	bs.issuing_company_id, "
				+ " 	bs.receiving_company_id, "
				+ " 	bs.balance as amount, "
				+ " 	bs.currency_id, "
				+ " 	concat(bs.name,' 內扣匯費15元') "
				+ " 	FROM bill_statement bs "
				+ " 	INNER JOIN company com ON com.id = bs.receiving_company_id "
				+ " 	WHERE :date BETWEEN bs.period_start AND bs.period_end "
				+ " 	AND (bs.balance >= 5000) "
				+ " 	AND com.is_supplier "
				+ " 	ORDER BY bs.receiving_company_id )";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("date", date);
		return getNamedParameterJdbcTemplate().update(sql,q);
	}

	private Boolean autoInsertDataExists(Date date) {
		String sql = "SELECT exists(select 1 from remittance "
				+ " WHERE sender_company_id = 2 "
				+ " AND date_sent = (SELECT DISTINCT period_end FROM bill_statement "
				+ " 	WHERE :date BETWEEN period_start AND period_end) "
				+ " + interval '10' day )";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("date", date);
		return getNamedParameterJdbcTemplate().queryForObject(sql,q,Boolean.class);
	}

	@Override @Transactional("transactionManager")
	public Integer deleteByDate(Date date) {
		String sql = "DELETE FROM remittance "
				+ " WHERE sender_company_id = 2 "
				+ " AND date_sent = (SELECT DISTINCT period_end FROM bill_statement "
				+ " 	WHERE :date BETWEEN period_start AND period_end) "
				+ " + interval '10' day ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("date", date);
		return getNamedParameterJdbcTemplate().update(sql,q);
	}

	@Override @Transactional("transactionManager")
	public Integer insertFromCSV(List<RemittanceImportItem> records) {

		String insertSql = "INSERT INTO remittance ( "
				+ " date_sent, date_received, sender_company_id, receiver_company_id, "
				+ " amount, currency_id, reference, fee_amount, "
				+ " fee_included, statement_name, bank_payment) "
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?); ";


		 int[][] updateCounts = this.getJdbcTemplate().batchUpdate(insertSql, records, 100 ,
				 new ParameterizedPreparedStatementSetter<RemittanceImportItem>() {
			@Override
			public void setValues(PreparedStatement pstmt, RemittanceImportItem r) throws SQLException {
				Timestamp sent = null;
				Timestamp received = null;
				try {
					sent = toTimestamp(r.getDateSent());
					received = toTimestamp(r.getDateReceived());
				} catch (ParseException e) {
					e.printStackTrace();
				}

				if (!remittanceRecordExists(r, sent, received)) {
					pstmt.setTimestamp(1, sent);
					pstmt.setTimestamp(2, received);
					pstmt.setInt(3, r.getSender());
					pstmt.setInt(4, r.getReceiver());
					pstmt.setBigDecimal(5, r.getAmount());
					pstmt.setInt(6, r.getCurrency());
					pstmt.setString(7, r.getReference());
					pstmt.setBigDecimal(8, r.getFeeAmount());
					pstmt.setBoolean(9, r.getFeeIncluded());
					pstmt.setString(10, r.getStatementName());
					pstmt.setBigDecimal(11, r.getBankPayment());
				}

			}

		});

	 return  records.size();
	}

	private Timestamp toTimestamp(String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return new Timestamp(sdf.parse(date).getTime() + EIGHT_HOURS);
	}

	private Boolean remittanceRecordExists(RemittanceImportItem record, Timestamp dateSent, Timestamp dateReceived) {
		String existSql = "SELECT exists(select 1 from remittance "
				+ " WHERE date_sent = :date_sent "
				+ " AND date_received = :date_received "
				+ " AND sender_company_id = :sender_company_id "
				+ " AND receiver_company_id = :receiver_company_id "
				+ " AND reference = :reference)";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("date_sent", dateSent);
		q.addValue("date_received", dateReceived);
		q.addValue("sender_company_id", record.getSender());
		q.addValue("receiver_company_id", record.getReceiver());
		q.addValue("reference", record.getReference());
		return getNamedParameterJdbcTemplate().queryForObject(existSql,q,Boolean.class);
	}
	
	@Override @Transactional("transactionManager")
	public int update(Remittance remittance,Date dateRcvd,Date dateSent) {
		Assert.isTrue(remittance.getRemittanceId()>0);
		String sql = "update remittance rmt set "
				+ "          date_sent = :sdte, "
			    + "      date_received = :rdte, "
		        + "  sender_company_id = sndr.id, "
				+ "receiver_company_id = rcvr.id, "
				+ "             amount = :amt, "
				+ "        currency_id = :currencyId, "
			    + "          reference = :reference, "
				+ "         fee_amount = :feeAmount,  "
				+ "       fee_included = :feeIncluded,  "
				+ "     statement_name = :statementName,  "
				+ "       bank_payment = :bankPayment  "
		        + "from company sndr, company rcvr "
				+ "where rmt.id = :id "
				+ "and sndr.k_code = :sndrName "
				+ "and rcvr.k_code = :rcvrName ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		//SQLQuery q = session.createSQLQuery(sql);
		q.addValue("id", remittance.getRemittanceId());
		q.addValue("amt", new BigDecimal(remittance.getAmount()));
		q.addValue("currencyId", remittance.getCurrency().getKey());
		q.addValue("rdte", dateRcvd);
		q.addValue("sdte", dateSent);
		q.addValue("sndrName", remittance.getSender());
		q.addValue("rcvrName", remittance.getReceiver());
		q.addValue("reference", remittance.getReference());
		q.addValue("feeAmount", new BigDecimal(remittance.getFeeAmount()));
		q.addValue("feeIncluded", Boolean.valueOf(remittance.getFeeIncluded()));
		String statementName = remittance.getStatementName();
		if (!statementName.startsWith("STM-")) {
			statementName = "STM-" + statementName;
		}
		q.addValue("statementName", statementName);
		q.addValue("bankPayment", new BigDecimal(remittance.getBankPayment()));
		getNamedParameterJdbcTemplate().update(sql,q);
		return remittance.getRemittanceId();
	}
	
	@Override @Transactional("transactionManager")
	public boolean delete(int id) {
		Assert.isTrue(id>0);
		String sql = "delete from remittance rmt where rmt.id = :id";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("id",id);
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
		return true;
	}
	
	@Override @SuppressWarnings("unchecked")
	public Date queryLatestSettlementEnd(String isurKcode, String rcvrKcode) {
		String sql = "select bs.period_end from bill_statement bs "
				+ "inner join company isur on isur.id = bs.issuing_company_id "
				+ "inner join company rcvr on rcvr.id = receiving_company_id "
				+ "where isur.k_code = :isurKcode and rcvr.k_code = :rcvrKcode "
				+ "order by period_end desc offset 0 limit 1 ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("isurKcode",isurKcode);
		q.addValue("rcvrKcode",rcvrKcode);
		List<Date> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,Date.class);
		if(resultList.size()==0) return null;
		return resultList.get(0);
	}

	@Override
	public Date queryLastSettlementEnd() {
		String sql = "select max(period_end) from bill_statement ";

		return getJdbcTemplate().queryForObject(sql,Date.class);
	}

}
