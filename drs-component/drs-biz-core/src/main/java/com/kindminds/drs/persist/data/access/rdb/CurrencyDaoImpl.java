package com.kindminds.drs.persist.data.access.rdb;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import com.kindminds.drs.api.data.access.rdb.CurrencyDao;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.kindminds.drs.Currency;

@Repository("CurrencyDao")
public class CurrencyDaoImpl extends Dao implements CurrencyDao {
	
	
	@Override @SuppressWarnings("unchecked")
	public Currency queryCompanyCurrency(String kcode) {
		String sql = "select com.currency_id from company com where com.k_code = :kcode ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("kcode", kcode);
		List<Integer> resultList= getNamedParameterJdbcTemplate().queryForList(sql,q,Integer.class);
		Assert.isTrue(resultList.size()==1);
		return Currency.fromKey(resultList.get(0));
	}
	
	@Override @Transactional("transactionManager")
	public void insertExchangeRate(Currency src, Currency dst, OffsetDateTime date, BigDecimal rate, BigDecimal interbankRate) {

		String sql = "insert into currency_exchange "
				+ "( src_currency_id, dst_currency_id,  date,  rate, interbank_rate ) values "
				+ "( :srcCurrencyKey, :dstCurrencyKey, :date, :rate, :interbankRate )";

		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("srcCurrencyKey", src.getKey());
		q.addValue("dstCurrencyKey", dst.getKey());

		//String dateSrt =  date.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		q.addValue("date", date);
		q.addValue("rate", rate);
		q.addValue("interbankRate", interbankRate);
		int insertedRows = getNamedParameterJdbcTemplate().update(sql,q);
		Assert.isTrue(insertedRows==1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Currency> getAllCurrencies() {
		String sql = "SELECT name FROM currency order by id ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		List<String> resultCurrencyListStr= getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		List<Currency> currencyList = new ArrayList<Currency>();
		for(String result:resultCurrencyListStr){
			currencyList.add(Currency.valueOf(result.toString()));
		}
		return currencyList;
	}
	
	@Override @SuppressWarnings("unchecked")
	public BigDecimal getExchangeRate(Currency srcCurrency, Currency dstCUrrency, Date date) {
		if(srcCurrency==dstCUrrency) return new BigDecimal("1");
		String sql = "select rate "
			+ "from currency_exchange ce "
			+ "inner join currency src on src.id = ce.src_currency_id "
			+ "inner join currency dst on dst.id = ce.dst_currency_id "
			+ "where  src.name=:srcCurrency "
			+ "and dst.name=:dstCurrency "
			+ "and ce.effective_from <= :date "
			+ "order by ce.effective_from desc limit 1";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("srcCurrency", srcCurrency.name());
		q.addValue("dstCurrency", dstCUrrency.name());
		q.addValue("date", date);
		List<BigDecimal> resultRateList= getNamedParameterJdbcTemplate().queryForList(sql,q,BigDecimal.class);
		Assert.isTrue(resultRateList.size()<=1,"More than one exchange rate were found.");
		if(resultRateList.size()==0){
			return null;
		}
		return resultRateList.get(0);
	}

	@Override @SuppressWarnings("unchecked")
	public BigDecimal queryExchangeRate(Date start, Date end, Currency srcCur,
										Currency dstCur,BigDecimal interbankRate) {
		String sql = "select rate " +
				" from currency_exchange ce " +
				" where ce.interbank_rate = :interbankRate " +
				" and ce.date = (SELECT MAX(date) FROM currency_exchange " +
				" \t\twhere date > :start " +
				" \t\tand date < :end) " +
				" and ce.src_currency_id = :srcCurrencyKey " +
				" and ce.dst_currency_id = :dstCurrencyKey ";

		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("interbankRate", interbankRate);
		q.addValue("start", start);
		q.addValue("end", Timestamp.valueOf(end.toInstant().atOffset(ZoneOffset.UTC).minusHours(30).toLocalDateTime()));
		q.addValue("srcCurrencyKey", srcCur.getKey());
		q.addValue("dstCurrencyKey", dstCur.getKey());

		List<BigDecimal> resultRateList= getNamedParameterJdbcTemplate().queryForList(sql,q,BigDecimal.class);

		Assert.isTrue(resultRateList.size()<=1,"More than one found:"+resultRateList.size());

		if(resultRateList.size()==0) return null;
		return resultRateList.get(0);
	}

}

