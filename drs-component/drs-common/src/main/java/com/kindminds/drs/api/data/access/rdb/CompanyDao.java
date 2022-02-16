package com.kindminds.drs.api.data.access.rdb;

import java.util.List;
import java.util.Map;

import com.kindminds.drs.Currency;

public interface CompanyDao {
	Boolean isSupplier(String kcode);
	Boolean isDrsCompany(String kcode);
	String queryHandlerKcode(String supplierkcode);
	String queryOfficialRegistrationNumber(String kcode);
	String queryAddress(String kcode);
	String queryAccountManager(String companyCode);
	Integer queryIdFromKcode(String kcode);
	Integer queryCompanyIdOfUser(int userId);
	Integer queryCompanyCountryId(String companyKcode);
	Currency queryCompanyCurrency(String kcode);
	List<String> querySupplierServiceEmailAddressList(String kcode);
	List<String> queryAllCompanyKcodeList();
	List<String> querySupplierKcodeList();
	List<String> querySupplierKcodeWithRetailList();
	List<String> queryDrsCompanyKcodeList();
	Map<String,String> querySupplierKcodeToShortEnUsNameMap();
	Map<String,String> querySupplierKcodeToShortEnUsNameWithRetailMap();
	Map<String,String> querySupplierKcodeToShortEnUsNameMap(String ssdcKcode);
	Map<String,String> querySupplierKcodeToShortEnUsNameWithRetailMap(String ssdcKcode);
	Map<String,String> querySupplierKcodeToShortLocalNameMap();
	Map<String,String> queryAllCompanyKcodeToShortEnUsNameMap();
	Map<String,String> queryAllCompanyKcodeToShortEnUsNameWithRetailMap();
	Map<String,String> queryAllCompanyKcodeToLocalNameMap();
	Map<String,String> queryDrsCompanyKcodeToShortEnUsNameMap();
	Map<String,String> queryDrsCompanyKcodeToShortLocalNameMap();
	Map<String,String> queryDrsCompanyKcodeToCountryCodeMap();
	List<String> queryActivatedSupplierKcodeList();
}
