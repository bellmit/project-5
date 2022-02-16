package com.kindminds.drs.persist.data.access.usecase;

import java.util.ArrayList;
import java.util.List;


import com.kindminds.drs.api.v1.model.Company;
import com.kindminds.drs.persist.data.access.rdb.Dao;
import com.kindminds.drs.persist.v1.model.mapping.CompanyImpl;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.data.access.usecase.MaintainCompanyDao;
import com.kindminds.drs.api.v1.model.product.SKU;
import com.kindminds.drs.persist.v1.model.mapping.product.ProductSkuImpl;

@Repository("maintainCompanyDao")
public class MaintainCompanyDaoImpl extends Dao implements MaintainCompanyDao {


	@Override
	public int getSupplierCount() {
		String sql = "select count(*) from company where is_supplier is TRUE ";

		Integer o = getJdbcTemplate().queryForObject(sql,Integer.class);
		if (o == null) return 0;
		return o;
	}

	@Override @SuppressWarnings("unchecked")
	public Company query(String kcode) {
		String sql = "select                 com.id as id, "
				+ "                      com.k_code as k_code, "
				+ "                  com.name_en_us as name_en_us, "
				+ "                  com.name_local as name_local, "
				+ "            com.short_name_en_us as short_name_en_us, "
				+ "            com.short_name_local as short_name_local, "
				+ "                     com.address as address, "
				+ "                com.phone_number as phone_number, "
				+ "                   currency.name as currency_name, "
				+ "                    country.code as country_code, "
				+ "com.official_registration_number as official_registration_number, "
				+ "                   com.bank_name as bank_name, "
				+ "            com.bank_branch_name as bank_branch_name, "
				+ "           com.bank_account_code as bank_account_code, "
				+ "           com.bank_account_name as bank_account_name,"
				+ "                       com.notes as notes, "
				+ "                  handler.k_code as handler_kcode, "
				+ "              com.is_drs_company as is_drs_company, "
				+ "                 com.is_supplier as is_supplier, "
				+ "                   com.activated as activated "
				+ " from company com "
				+ " inner join country  on  country.id = com.country_id "
				+ " inner join currency on currency.id = com.currency_id "
				+ " left  join company handler on handler.id = com.handler_company_id "
				+ " where com.k_code = :kcode ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("kcode", kcode);


		List<CompanyImpl> list = getNamedParameterJdbcTemplate().query(sql,q,(rs,rowNum)-> new CompanyImpl(
				rs.getInt("id"),rs.getString("k_code"),rs.getString("name_en_us"),rs.getString("name_local")
				,rs.getString("short_name_en_us"),rs.getString("short_name_local"),
				rs.getString("address"),rs.getString("phone_number")
				,rs.getString("currency_name"),rs.getString("country_code"),
				rs.getString("official_registration_number"),rs.getString("bank_name")
				,rs.getString("bank_branch_name"),rs.getString("bank_account_code"),
				rs.getString("bank_account_name"),rs.getString("notes")
				,rs.getString("handler_kcode"),rs.getBoolean("is_drs_company")
				,rs.getBoolean("is_supplier"),rs.getBoolean("activated")
		));

		Assert.isTrue(list.size()<=1);
		if(list.size()==0) return null;
		CompanyImpl com = list.get(0);
		com.setServiceEmailList(this.queryServiceEmailList(kcode));
		com.setCouponList(this.queryCouponList(kcode));
		com.setProductEmailList(this.queryProductEmailList(kcode));
		return com;
	}

	@SuppressWarnings("unchecked")
	private List<String> queryServiceEmailList(String kcode){
		String sql = "select cse.email from company_service_email cse, company c "
				+ "where c.id = cse.company_id and c.k_code = :kcode ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("kcode", kcode);
		List<String> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		if(resultList.size()==0) return null;
		return resultList;
	}

	@SuppressWarnings("unchecked")
	private List<String> queryCouponList(String kcode){
		String sql = "select cc.coupon from company_coupon cc, company c "
				+ "where c.id = cc.company_id and c.k_code = :kcode ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("kcode", kcode);
		List<String> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		if(resultList.size()==0) return null;
		return resultList;
	}

	@SuppressWarnings("unchecked")
	private List<String> queryProductEmailList(String kcode){
		String sql = "select pce.email from product_contact_email pce, company c "
				+ "where c.id = pce.company_id and c.k_code = :kcode ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("kcode", kcode);
		List<String> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		if(resultList.size()==0) return null;
		return resultList;
	}

	@Override @Transactional("transactionManager")
	public String insertSupplier(Company c) {
		String sql = "insert into company "
				+ "( k_code, name_en_us, name_local, short_name_en_us, short_name_local,  address, phone_number, currency_id, country_id, official_registration_number, bank_name, bank_branch_name, bank_account_code, bank_account_name,  notes, handler_company_id, is_drs_company, is_supplier )"
				+ "select  "
				+ "  :kCode,  :nameEnUs, :nameLocal,   :shortNameEnUs,  :shortNameLocal, :address, :phoneNumber, currency.id, country.id,  :officialRegistrationNumber, :bankName,  :bankBranchName,  :bankAccountCode,  :bankAccountName, :notes,         handler.id,          FALSE,        TRUE "
				+ "from company handler "
				+ "inner join currency on currency.name = :currencyName "
				+ "inner join country on country.id = handler.country_id "
				+ "where country.code = :countryCode "
				+ "and handler.is_drs_company is TRUE ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("kCode", c.getKcode());
		q.addValue("nameEnUs", c.getNameEnUs());
		q.addValue("nameLocal", c.getNameLocal());
		q.addValue("shortNameEnUs", c.getShortNameEnUs());
		q.addValue("shortNameLocal", c.getShortNameLocal());
		q.addValue("address", c.getAddress());
		q.addValue("phoneNumber", c.getPhoneNumber());
		q.addValue("currencyName", Currency.valueOf(c.getCurrency()).name());
		q.addValue("countryCode", c.getCountryCode());
		q.addValue("officialRegistrationNumber",c.getOfficialRegistrationNo());
		q.addValue("bankName",c.getBankName());
		q.addValue("bankBranchName",c.getBankBranchName());
		q.addValue("bankAccountCode",c.getAccountNumber());
		q.addValue("bankAccountName",c.getAccountName());
		q.addValue("notes", c.getNotes());
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
		this.insertServiceEmailList(c.getKcode(), c.getServiceEmailList());
		this.insertProductEmailList(c.getKcode(), c.getProductEmailList());
		this.insertCouponList(c.getKcode(), c.getCouponList());
		return c.getKcode();
	}

	@Override @Transactional("transactionManager")
	public String updateInfo(Company company) {
		String sql = "update company set "
				+ "                   name_en_us = :nameEnUs, "
				+ "                   name_local = :nameLocal, "
				+ "             short_name_en_us = :shortNameEnUs, "
				+ "             short_name_local = :shortNameLocal, "
				+ "                      address = :address, "
				+ "                 phone_number = :phoneNumber, "
				+ "                  currency_id = currency.id, "
				+ " official_registration_number = :officialRegistrationNumber, "
				+ "                    bank_name = :bankName, "
				+ "             bank_branch_name = :bankBranchName, "
				+ "            bank_account_code = :bankAccountCode, "
				+ "            bank_account_name = :bankAccountName, "
				+ "                        notes = :notes, "
				+ "                    activated = :activated "
				+ "from currency "
				+ "where company.k_code = :kcode "
				+ "and currency.name = :currencyName ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("kcode", company.getKcode());
		q.addValue("nameEnUs", company.getNameEnUs());
		q.addValue("nameLocal", company.getNameLocal());
		q.addValue("shortNameEnUs", company.getShortNameEnUs());
		q.addValue("shortNameLocal", company.getShortNameLocal());
		q.addValue("address", company.getAddress());
		q.addValue("phoneNumber", company.getPhoneNumber());
		q.addValue("currencyName", Currency.valueOf(company.getCurrency()).name());
		q.addValue("officialRegistrationNumber",company.getOfficialRegistrationNo());
		q.addValue("bankName",company.getBankName());
		q.addValue("bankBranchName",company.getBankBranchName());
		q.addValue("bankAccountCode",company.getAccountNumber());
		q.addValue("bankAccountName",company.getAccountName());
		q.addValue("notes", company.getNotes());
		q.addValue("activated",company.getActivated());
		this.updateServiceEmailList(company.getKcode(),company.getServiceEmailList());
		this.updateCouponList(company.getKcode(),company.getCouponList());
		this.updateProductEmailList(company.getKcode(),company.getProductEmailList());
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
		return company.getKcode();
	}

	@Transactional("transactionManager")
	private void  updateServiceEmailList(String kcode, List<String> serviceEmailList){
		this.deleteServiceEmailList(kcode);
		this.insertServiceEmailList(kcode, serviceEmailList);
	}

	@Transactional("transactionManager")
	private void deleteServiceEmailList(String kcode){
		String sql = "delete from company_service_email cse using company c where c.id = cse.company_id and c.k_code = :kcode ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("kcode",kcode);
		getNamedParameterJdbcTemplate().update(sql,q);
	}

	@Transactional("transactionManager")
	private void insertServiceEmailList(String kcode, List<String> serviceEmailList){
		if(serviceEmailList!=null&&serviceEmailList.size()>=1){
			String insertSql = "insert into company_service_email ( company_id, email ) values ";
			Integer companyId = this.queryCompanyId(kcode);
			for(String email:serviceEmailList){
				insertSql += "( :companyId, '"+email+"' ),";
			}
			insertSql = insertSql.substring(0, insertSql.length()-1);
			MapSqlParameterSource q = new MapSqlParameterSource();
			q.addValue("companyId",companyId);
			getNamedParameterJdbcTemplate().update(insertSql,q);
		}
	}

	@Transactional("transactionManager")
	private void  updateProductEmailList(String kcode, List<String> productEmailList){
		this.deleteProductEmailList(kcode);
		this.insertProductEmailList(kcode, productEmailList);
	}

	@Transactional("transactionManager")
	private void deleteProductEmailList(String kcode){
		String sql = "delete from product_contact_email pce using company c where c.id = pce.company_id and c.k_code = :kcode ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("kcode",kcode);
		getNamedParameterJdbcTemplate().update(sql,q);
	}

	@Transactional("transactionManager")
	private void insertProductEmailList(String kcode, List<String> productEmailList){
		if(productEmailList!=null&&productEmailList.size()>=1){
			String insertSql = "insert into product_contact_email ( company_id, email ) values ";
			Integer companyId = this.queryCompanyId(kcode);
			for(String email:productEmailList){
				insertSql += "( :companyId, '"+email+"' ),";
			}
			insertSql = insertSql.substring(0, insertSql.length()-1);
			MapSqlParameterSource q = new MapSqlParameterSource();
			q.addValue("companyId",companyId);
			getNamedParameterJdbcTemplate().update(insertSql,q);
		}
	}

	@Transactional("transactionManager")
	private void  updateCouponList(String kcode, List<String> couponList){
		this.deleteCouponList(kcode);
		this.insertCouponList(kcode, couponList);
	}

	@Transactional("transactionManager")
	private void deleteCouponList(String kcode){
		String sql = "delete from company_coupon cc using company c where c.id = cc.company_id and c.k_code = :kcode ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("kcode",kcode);
		getNamedParameterJdbcTemplate().update(sql,q);
	}

	@Transactional("transactionManager")
	private void insertCouponList(String kcode, List<String> couponList){
		if(couponList!=null&&couponList.size()>=1){
			String insertSql = "insert into company_coupon ( company_id, coupon ) values ";
			Integer companyId = this.queryCompanyId(kcode);
			for(String coupon : couponList){
				insertSql += "( :companyId, '"+coupon+"' ),";
			}
			insertSql = insertSql.substring(0, insertSql.length()-1);
			MapSqlParameterSource q = new MapSqlParameterSource();
			q.addValue("companyId",companyId);
			getNamedParameterJdbcTemplate().update(insertSql,q);
		}
	}

	private Integer queryCompanyId(String kcode){
		String sql = "select id from company c where c.k_code = :kcode ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("kcode", kcode);
		Integer o = getNamedParameterJdbcTemplate().queryForObject(sql,q,Integer.class);
		Assert.notNull(o);
		return o;
	}

	@Override @Transactional("transactionManager")
	public String delete(String kcode) {
		this.deleteServiceEmailList(kcode);
		this.deleteProductEmailList(kcode);
		String sql = "Delete from company where k_code = :kcode";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("kcode", kcode);
	 	getNamedParameterJdbcTemplate().update(sql,q);
		return kcode;
	}

	@Override @SuppressWarnings("unchecked")
	public List<Company> querySupplierList(int startIndex, int size) {
		String sql = "select      com.id as id, "
				+ "           com.k_code as k_code, "
				+ "                 NULL as name_en_us, "
				+ "                 NULL as name_local, "
				+ "                 NULL as short_name_en_us, "
				+ " com.short_name_local as short_name_local, "
				+ "                 NULL as address, "
				+ "                 NULL as phone_number, "
				+ "                 NULL as mail_address_service, "
				+ "                 NULL as currency_name, "
				+ "                 NULL as country_code, "
				+ "                 NULL as official_registration_number, "
				+ "                 NULL as bank_name, "
				+ "                 NULL as bank_branch_name, "
				+ "                 NULL as bank_code, "
				+ "                 NULL as bank_branch_code, "
				+ "                 NULL as bank_account_code, "
				+ "                 NULL as bank_account_name,"
				+ "                 NULL as notes, "
				+ "                 NULL as handler_kcode, "
				+ "                 NULL as is_drs_company, "
				+ "                 NULL as is_supplier, "
				+ "                 NULL as activated "
				+ " from company com "
				+ " inner join country  on  country.id = com.country_id "
				+ " inner join currency on currency.id = com.currency_id "
				+ " left  join company handler on handler.id = com.handler_company_id "
				+ " where com.is_supplier is TRUE"
				+ " order by com.k_code "
				+ " limit :size offset :start ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("size", size);
		q.addValue("start", startIndex-1);


		List<CompanyImpl> companyList = getNamedParameterJdbcTemplate().query(sql,q,(rs,rowNum)-> new CompanyImpl(
				rs.getInt("id"),rs.getString("k_code"),rs.getString("name_en_us"),rs.getString("name_local")
				,rs.getString("short_name_en_us"),rs.getString("short_name_local"),
				rs.getString("address"),rs.getString("phone_number")
				,rs.getString("currency_name"),rs.getString("country_code"),
				rs.getString("official_registration_number"),rs.getString("bank_name")
				,rs.getString("bank_branch_name"),rs.getString("bank_account_code"),
				rs.getString("bank_account_name"),rs.getString("notes")
				,rs.getString("handler_kcode"),rs.getBoolean("is_drs_company")
				,rs.getBoolean("is_Supplier"),rs.getBoolean("activated")
		));

		for(CompanyImpl c : companyList){
			c.setSkuList(this.querySkuList(c.getKcode()));
		}
		List<Company> listToReturn = new ArrayList<Company>();
		listToReturn.addAll(companyList);
		return listToReturn;
	}

	@Override
	public Boolean isCouponUnique(String coupon,String kcode) {
		String sql = "select case\twhen exists (\n" +
				" select " +
				"  cc.coupon " +
				" from " +
				"  company_coupon cc " +
				" where " +
				"  upper(cc.coupon) like upper(:coupon) and cc.company_id != (select id from company c where c.k_code = :kcode) ) " +
				" then cast(1 as bit) " +
				" else cast(0 as bit) " +
				"end";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("coupon", coupon);
		q.addValue("kcode", kcode);
		Boolean exists = getNamedParameterJdbcTemplate().queryForObject(sql,q,Boolean.class);
		return !exists;
	}

	@SuppressWarnings("unchecked")
	private List<SKU> querySkuList(String kcode){
		String sql = "select                ps.id as id, "
				+ "                          NULL as supplier_company_kcode, "
				+ "                          NULL as product_base_code, "
				+ "                          NULL as code_by_supplier, "
				+ "                ps.code_by_drs as code_by_drs, "
				+ "                          NULL as name_by_supplier, "
				+ "               pps.name_by_drs as name_by_drs, "
				+ "                          NULL as ean, "
				+ "                          NULL as ean_provider, "
				+ "                          NULL as status, "
				+ "                          NULL as manufacturing_lead_time_days, "
				+ "ps.contain_lithium_ion_battery as contain_lithium_ion_battery, "
				+ "                          NULL as internal_notes "
				+ "from product_sku ps "
				+ "inner join product pps on pps.id = ps.product_id "
				+ "inner join product_base pb on pb.id = ps.product_base_id "
				+ "inner join company splr on splr.id = pb.supplier_company_id "
				+ "where splr.k_code = :kcode "
				+ "order by ps.code_by_drs";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("kcode", kcode);

		return (List) getNamedParameterJdbcTemplate().query(sql,q,(rs,rowNum) -> new ProductSkuImpl(
				rs.getInt("id"),rs.getString("supplier_company_kcode"),
				rs.getString("product_base_code"),rs.getString("code_by_supplier"),rs.getString("code_by_drs"),
				rs.getString("name_by_supplier"),rs.getString("name_by_drs"),rs.getString("ean"),
				rs.getString("ean_provider"),rs.getString("status"),
				rs.getString("manufacturing_lead_time_days"),rs.getBoolean("contain_lithium_ion_battery"),rs.getString("internal_Notes")
		));
	}

	@Override @SuppressWarnings("unchecked")
	public List<String> querySupplierUserEmailList(String kcode) {
		String sql = "select ui.user_email from user_info ui "
				+ "inner join company com on com.id = ui.company_id "
				+ "where com.k_code = :kcode and is_supplier = TRUE "
				+ "and ui.user_email not ilike '%DRS%' "
				+ "order by ui.user_email ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("kcode", kcode);
		return getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
	}

	@Override @Transactional("transactionManager")
	public String updatePartial(Company company) {
		String sql = "update company set address = :address, "
				+ "                 phone_number = :phoneNumber "
				+ "where company.k_code = :kcode ";
	MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("kcode", company.getKcode());
		q.addValue("address", company.getAddress());
		q.addValue("phoneNumber", company.getPhoneNumber());
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
		this.updateServiceEmailList(company.getKcode(), company.getServiceEmailList());
		this.updateProductEmailList(company.getKcode(), company.getProductEmailList());
		return company.getKcode();
	}

}