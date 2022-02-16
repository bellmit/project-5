package com.kindminds.drs.api.data.access.usecase.accounting;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.kindminds.drs.Country;
import com.kindminds.drs.Currency;
import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.v1.model.accounting.RetainmentRate;

public interface CalculateRetainmentRateDao {
	List<Object []> querySettlementPeriodList();
	Date queryPeriodStart(int settlementPeriodId);
	Date queryPeriodEnd(int settlementPeriodId);
	Date queryLatestRetainmentDateEnd();
	List<RetainmentRate> queryList();
	void insert(Date start,Date end,Country country,String supplierKcode,Currency originalCurrency, BigDecimal revenueInOriginalCurrency, BigDecimal currencyExchangeRateToUsd, BigDecimal revenueInUsd,BigDecimal retainmentRate);
	int delete(int rateId);
	BigDecimal queryRate(Date start,Date end,int countryId,String supplierKcode);
	BigDecimal queryRevenueInOriginalCurrency(Date start,Date end,int countryId,String supplierKcode);
	Map<String,BigDecimal> querySupplierAmazonOrderRevenue(Marketplace marketplace,Date start,Date end, int revenueGradeVer);
	Map<String,BigDecimal> querySupplierShopifyRevenue(Marketplace marketplace,Date start,Date end, int revenueGradeVer);
	Map<String,BigDecimal> querySupplierEbayRevenue(Marketplace marketplace,Date start,Date end, int revenueGradeVer);
	Map<String,BigDecimal> querySupplierIdToFbaReimbursementRevenueMap(Country country, Date start, Date end,List<String> relatedDescriptionList, int revenueGradeVer);
	boolean existRetainmentRate(Date startDateTimePoint,Date endDateTimePoint);
}
