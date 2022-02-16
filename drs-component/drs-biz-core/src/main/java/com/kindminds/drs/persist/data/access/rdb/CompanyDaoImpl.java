package com.kindminds.drs.persist.data.access.rdb;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;





import com.kindminds.drs.api.data.access.rdb.CompanyDao;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.kindminds.drs.Currency;

@Repository
public class CompanyDaoImpl extends Dao implements CompanyDao {
	

	private final List<String> unAvailableComKcodes = Arrays.asList("K101","K408","K448","K490", "K493", "K496", "K501", "K505", "K509", "K512");
	private final List<String> unAvailableComKcodesIncludeRetail = Arrays.asList("K408","K448","K490", "K493", "K496", "K501", "K505", "K509", "K512");
	
	@Override @SuppressWarnings("unchecked")
	public String queryHandlerKcode(String supplierkcode) {
		String sql = "select handler.k_code from company c "
				+ "inner join company handler on handler.id = c.handler_company_id "
				+ "where c.k_code = :kcode "
				+ "and handler.is_drs_company = TRUE ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("kcode", supplierkcode);
		List<String> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		Assert.isTrue(resultList.size()==1);
		return resultList.get(0);
	}

	@Override
	public Integer queryCompanyIdOfUser(int userId) {
		String sql = "select c.id from company c "
				+ "inner join user_info u on u.company_id = c.id "
				+ "where u.user_id = :userId ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("userId", userId);
		Integer o = getNamedParameterJdbcTemplate().queryForObject(sql,q,Integer.class);
		if (o == null) return 0;
		return o;
	}

	@Override
	public Integer queryIdFromKcode(String kcode) {
		String sql = "select c.id from company c "
				+ " where c.k_code = :kcode ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("kcode", kcode);
		return getNamedParameterJdbcTemplate().queryForObject(sql,q,Integer.class);
	}

	@Override
	public Boolean isSupplier(String kcode) {
		String sql = "select c.is_supplier from company c where c.k_code = :kcode ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("kcode", kcode);
		Boolean o = null;
		try{
			 o = getNamedParameterJdbcTemplate().queryForObject(sql,q, Boolean.class);
		}catch (EmptyResultDataAccessException ex){

		}
		if (o == null) return false;
		return o;
	}
	
	@Override
	public Boolean isDrsCompany(String kcode) {
		String sql = "select c.is_drs_company from company c where c.k_code = :kcode ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("kcode", kcode);

		Boolean o = null;
		try{
			o = getNamedParameterJdbcTemplate().queryForObject(sql,q, Boolean.class);
		}catch (EmptyResultDataAccessException ex){

		}
		if (o == null) return false;
		return o;
	}

	@Override @SuppressWarnings("unchecked")
	public List<String> queryAllCompanyKcodeList() {
		String sql = "SELECT k_code FROM company "
				+ "where is_supplier=TRUE or is_drs_company=TRUE "
				+ "order by k_code";

		return getJdbcTemplate().queryForList(sql,String.class);
	}

	@Override @SuppressWarnings("unchecked")
	public List<String> querySupplierKcodeList() {
		String sql = "SELECT k_code FROM company "
				+ "where is_supplier=TRUE and k_code not in (:unAvailableComKcodes) "
				+ "order by k_code";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("unAvailableComKcodes", this.unAvailableComKcodes);
		return getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
	}

	@Override @SuppressWarnings("unchecked")
	public List<String> querySupplierKcodeWithRetailList() {
		String sql = "SELECT k_code FROM company "
				+ "where is_supplier=TRUE and k_code not in (:unAvailableComKcodesIncludeRetail) "
				+ "order by k_code";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("unAvailableComKcodesIncludeRetail", this.unAvailableComKcodesIncludeRetail);
		return getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
	}
	
	@Override @SuppressWarnings("unchecked")
	public List<String> queryDrsCompanyKcodeList() {
		String sql = "SELECT k_code FROM company "
				+ "where is_drs_company=TRUE "
				+ "order by k_code";

		return getJdbcTemplate().queryForList(sql,String.class);
	}

	@Override @SuppressWarnings("unchecked")
	public Map<String, String> queryAllCompanyKcodeToShortEnUsNameMap() {
		String sql = "select k_code, short_name_en_us from company "
				+ "where ( is_supplier = TRUE or is_drs_company = TRUE ) "
				+ "and k_code not in (:unAvailableComKcodes) "
				+ "order by k_code";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("unAvailableComKcodes", this.unAvailableComKcodes);
		List<Object[]> result = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		Map<String,String> allCompanyKcodeToNameMap = new TreeMap<>();
		for(Object[] items:result){
			allCompanyKcodeToNameMap.put((String)items[0], (String)items[1]);
		}
		return allCompanyKcodeToNameMap;
	}

	@Override @SuppressWarnings("unchecked")
	public Map<String, String> queryAllCompanyKcodeToShortEnUsNameWithRetailMap() {
		String sql = "select k_code, short_name_en_us from company "
				+ "where ( is_supplier = TRUE or is_drs_company = TRUE ) "
				+ "and k_code not in (:unAvailableComKcodesIncludeRetail) "
				+ "order by k_code";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("unAvailableComKcodesIncludeRetail", this.unAvailableComKcodesIncludeRetail);
		List<Object[]> result = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		Map<String,String> allCompanyKcodeToNameMap = new TreeMap<>();
		for(Object[] items:result){
			allCompanyKcodeToNameMap.put((String)items[0], (String)items[1]);
		}
		return allCompanyKcodeToNameMap;
	}
	
	@Override @SuppressWarnings("unchecked")
	public Map<String, String> queryAllCompanyKcodeToLocalNameMap() {
		String sql = "select k_code, name_local from company "
				+ "order by k_code";
	MapSqlParameterSource q = new MapSqlParameterSource();
		Map<String,String> supplierKcodeToNameMap = new HashMap<>();
		List<Object[]> result = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		for(Object[] items:result){
			supplierKcodeToNameMap.put((String)items[0], (String)items[1]);
		}
		return supplierKcodeToNameMap;
	}

	@Override @SuppressWarnings("unchecked")
	public Map<String, String> querySupplierKcodeToShortLocalNameMap() {
		String sql = "select k_code, short_name_local from company "
				+ "where is_supplier=TRUE "
				+ "order by k_code";
	MapSqlParameterSource q = new MapSqlParameterSource();
		Map<String,String> supplierKcodeToNameMap = new HashMap<>();
		List<Object[]> result = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		for(Object[] items:result){
			supplierKcodeToNameMap.put((String)items[0], (String)items[1]);
		}
		return supplierKcodeToNameMap;
	}

	@Override @SuppressWarnings("unchecked")
	public Map<String, String> querySupplierKcodeToShortEnUsNameMap() {
		String sql = "select k_code, short_name_en_us from company "
				+ "where is_supplier=TRUE and k_code not in (:unAvailableComKcodes) "
				+ "order by k_code";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("unAvailableComKcodes", this.unAvailableComKcodes);
		Map<String,String> supplierKcodeToNameMap = new TreeMap<>();
		List<Object[]> result = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		for(Object[] items:result){
			supplierKcodeToNameMap.put((String)items[0], (String)items[1]);
		}
		return supplierKcodeToNameMap;
	}

	@Override @SuppressWarnings("unchecked")
	public Map<String, String> querySupplierKcodeToShortEnUsNameWithRetailMap() {
		String sql = "select k_code, short_name_en_us from company "
				+ "where is_supplier=TRUE and k_code not in (:unAvailableComKcodesIncludeRetail) "
				+ "order by k_code";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("unAvailableComKcodesIncludeRetail", this.unAvailableComKcodesIncludeRetail);
		Map<String,String> supplierKcodeToNameMap = new TreeMap<>();
		List<Object[]> result = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		for(Object[] items:result){
			supplierKcodeToNameMap.put((String)items[0], (String)items[1]);
		}
		return supplierKcodeToNameMap;
	}
		
	@Override @SuppressWarnings("unchecked")
	public Map<String, String> querySupplierKcodeToShortEnUsNameMap(String ssdcKcode) {
		String sql = "select splr.k_code,splr.short_name_en_us from company splr "
				+ "inner join company ssdc on ssdc.id = splr.handler_company_id "
				+ "where splr.is_supplier is TRUE and ssdc.k_code = :ssdcKcode and splr.k_code not in (:unavailableSplrList)";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("ssdcKcode", ssdcKcode);
		q.addValue("unavailableSplrList",this.unAvailableComKcodes);
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
	public Map<String, String> querySupplierKcodeToShortEnUsNameWithRetailMap(String ssdcKcode) {
		String sql = "select splr.k_code,splr.short_name_en_us from company splr "
				+ "inner join company ssdc on ssdc.id = splr.handler_company_id "
				+ "where splr.is_supplier is TRUE and ssdc.k_code = :ssdcKcode and splr.k_code not in (:unavailableSplrList)";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("ssdcKcode", ssdcKcode);
		q.addValue("unavailableSplrList",this.unAvailableComKcodesIncludeRetail);
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
	public Map<String, String> queryDrsCompanyKcodeToShortEnUsNameMap() {
		String sql = "select k_code, short_name_en_us from company "
				+ "where is_drs_company=TRUE "
				+ "order by k_code";
	MapSqlParameterSource q = new MapSqlParameterSource();
		Map<String,String> drsKcodeToEnNameMap = new HashMap<>();
		List<Object[]> result = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		for(Object[] items:result){
			drsKcodeToEnNameMap.put((String)items[0], (String)items[1]);
		}
		return drsKcodeToEnNameMap;
	}
		
	@Override @SuppressWarnings("unchecked")
	public Map<String, String> queryDrsCompanyKcodeToShortLocalNameMap() {
		String sql = "select k_code, short_name_local from company "
				+ "where is_drs_company=TRUE order by k_code";
	MapSqlParameterSource q = new MapSqlParameterSource();
		Map<String,String> drsKcodeToLocalNameMap = new HashMap<>();
		List<Object[]> result = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		for(Object[] items:result){
			drsKcodeToLocalNameMap.put((String)items[0], (String)items[1]);
		}
		return drsKcodeToLocalNameMap;
	}
	
	@Override @SuppressWarnings("unchecked")
	public Map<String, String> queryDrsCompanyKcodeToCountryCodeMap() {
		String sql = "select k_code, country.code "
				+ "from company "
				+ "inner join country on country.id = company.country_id "
				+ "where is_drs_company = TRUE "
				+ "order by k_code";
	MapSqlParameterSource q = new MapSqlParameterSource();
		Map<String,String> drsCompanyKcodeToCountryCodeMap = new HashMap<>();
		List<Object[]> result = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		for(Object[] items:result){
			drsCompanyKcodeToCountryCodeMap.put((String)items[0], (String)items[1]);
		}
		return drsCompanyKcodeToCountryCodeMap;
	}

	@Override
	public List<String> queryActivatedSupplierKcodeList() {

		String sql = "SELECT k_code FROM company "
				+ "where is_supplier=TRUE and " +
				" k_code not in (:unAvailableComKcodesIncludeRetail) and  activated = true "
				+ "order by k_code";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("unAvailableComKcodesIncludeRetail", this.unAvailableComKcodesIncludeRetail);
		return getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
	}

	@Override @SuppressWarnings("unchecked")
	public String queryOfficialRegistrationNumber(String kcode) {
		String sql = "select c.official_registration_number from company c where c.k_code = :kcode ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("kcode", kcode);
		List<String> numberList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		Assert.isTrue(numberList.size()==1,"No or more than one kcode found.");
		return numberList.get(0);
	}

	@Override @SuppressWarnings("unchecked")
	public String queryAddress(String kcode) {
		String sql = "select c.address from company c where c.k_code = :kcode ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("kcode", kcode);
		List<String> numberList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		Assert.isTrue(numberList.size()==1,"No or more than one kcode found.");
		return numberList.get(0);
	}

	@Override @SuppressWarnings("unchecked")
	public List<String> querySupplierServiceEmailAddressList(String kcode) {
		String sql = "select cse.email from company_service_email cse, company splr "
				+ "where cse.company_id = splr.id and splr.k_code = :kcode and splr.is_supplier = TRUE order by cse.email ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("kcode", kcode);
		List<String> mailList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		Assert.isTrue(mailList.size()>=1,"No mail found.");
		return mailList;
	}

	@Override
	public Currency queryCompanyCurrency(String kcode) {
		String sql = "select ct.currency_id from company com "
				+ "inner join country ct on ct.id = com.country_id "
				+ "where com.k_code = :kcode ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("kcode", kcode);
		Integer currencyId = getNamedParameterJdbcTemplate().queryForObject(sql,q,Integer.class);
		return Currency.fromKey(currencyId);
	}

	@Override
	public Integer queryCompanyCountryId(String companyKcode) {
		StringBuilder sqlSb = new StringBuilder()
				.append("select ct.id ")
				.append("from company com ")
				.append("inner join country ct on com.country_id = ct.id ")
				.append("where com.k_code = :kcode");
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("kcode", companyKcode);
		return  getNamedParameterJdbcTemplate().queryForObject(sqlSb.toString(),q,Integer.class);
	}

	@Override
	public String queryAccountManager(String companyCode) {
		String sql = "SELECT account_manager_in_charge " +
				" FROM finance.supplier_payment_term spt " +
				" inner join company cpy on cpy.id = spt.splr_company_id " +
				" where cpy.k_code = :companyCode ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("companyCode", companyCode);
		String r = null;
		try{
			r = getNamedParameterJdbcTemplate().queryForObject(sql,q,String.class);
		}catch (EmptyResultDataAccessException ex){

		}

		return r;
	}
		
}
