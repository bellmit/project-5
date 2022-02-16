package com.kindminds.drs.service.usecase.accounting;

import com.kindminds.drs.*;
import com.kindminds.drs.Currency;
import com.kindminds.drs.api.usecase.CalculateRetainmentRateUco;
import com.kindminds.drs.api.v1.model.accounting.RetainmentRate;
import com.kindminds.drs.api.v1.model.accounting.SettlementPeriod;
import com.kindminds.drs.api.v1.model.amazon.AmazonTransaction.AmazonTransactionLineItem.AmzAmountTypeDesc;
import com.kindminds.drs.api.data.access.rdb.CurrencyDao;
import com.kindminds.drs.api.data.access.usecase.accounting.CalculateRetainmentRateDao;
import com.kindminds.drs.api.data.access.rdb.accounting.SettlementPeriodDao;
import com.kindminds.drs.v1.model.impl.accounting.SettlementPeriodImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CalculateRetainmentRateUcoImpl implements CalculateRetainmentRateUco {
	
	@Autowired private CalculateRetainmentRateDao dao;
	@Autowired private SettlementPeriodDao settlementPeriodRepo;
	@Autowired private CurrencyDao currencyRepo;

	@Override
	public List<SettlementPeriod> getAvailableSettlementPeriodList() {
		List<Object []> columnsList =  this.dao.querySettlementPeriodList();

		List<SettlementPeriod> listToReturn = new ArrayList<SettlementPeriod>();
		for(Object[] columns:columnsList){
			int id =(int)columns[0];
			Date start =(Date)columns[1];
			Date end =(Date)columns[2];
			listToReturn.add(new SettlementPeriodImpl(id, start, end));
		}
		return listToReturn;
	}

	@Override
	public List<RetainmentRate> getList() {
		return this.dao.queryList();
	}

	private List<SettlementPeriod> processRecentPeriods(int counts) {
		List<Object []> columnsList = this.settlementPeriodRepo.queryRecentPeriods(counts);
		List<SettlementPeriod> periods = new ArrayList<SettlementPeriod>();
		for(Object[] columns:columnsList){
			int id =(int)columns[0];
			Date start =(Date)columns[1];
			Date end =(Date)columns[2];
			periods.add(new SettlementPeriodImpl(id, start, end));
		}

		return periods;
	}

	@Override
	public String calculate() {
		SettlementPeriod period = this.processRecentPeriods(1).get(0);
		if(!this.retainmentRateReady(period)){
			this.calculate(String.valueOf(period.getId()));
			return "Retainment rates for "+period.getStartDate()+" to "+period.getEndDate()+" has been successfully calculated."; 
		}
		return "Retainment rates for "+period.getStartDate()+" to "+period.getEndDate()+" already exist.";
	}
	
	private boolean retainmentRateReady(SettlementPeriod period){
		return this.dao.existRetainmentRate(period.getStartPoint(), period.getEndPoint());
	}

	@Override @Transactional("transactionManager")
	public void calculate(String settlementPeriodId) {
		if(!StringUtils.hasText(settlementPeriodId)) return;
		Integer periodId = Integer.parseInt(settlementPeriodId);
		Date start = this.dao.queryPeriodStart(periodId);
		Date end = this.dao.queryPeriodEnd(periodId);
		Date latestRetainmentRateEnd = this.dao.queryLatestRetainmentDateEnd();
		Assert.isTrue(start.compareTo(latestRetainmentRateEnd)==0);
		Assert.isTrue(start.before(end));
		this.calculate(start,end);
	}
	
	private void calculate(Date start,Date end){
		BigDecimal totalK618Revenue = BigDecimal.ZERO;
		Map<Country, Map<String, BigDecimal>> countryToKcodeToRevenueMap = new HashMap<>();
		Map<Country, Map<String, BigDecimal>> countryToKcodeToRevenueMap2022 = new HashMap<>();

		List<Country> countryList = Country.getAmazonCountryList();

		//create country-company to revenue map and calculate K618 total revenue in USD
		for(Country country:countryList){
			Map<String,BigDecimal> supplierIdToRevenueInOriginalCurrencyMap =
					this.getSupplierIdToRevenueMap(country,start, end, 1);

			Map<String,BigDecimal> supplierIdToRevenueInOriginalCurrencyMap2022 =
					this.getSupplierIdToRevenueMap(country,start, end, 2);

			countryToKcodeToRevenueMap.put(country, supplierIdToRevenueInOriginalCurrencyMap);

			countryToKcodeToRevenueMap2022.put(country, supplierIdToRevenueInOriginalCurrencyMap2022);

			BigDecimal currencyExchangeRateToUsd = this.getCurrencyExchangeRateToUsd(start,end,country.getCurrency());

			BigDecimal revenueInOriginalCurrency = supplierIdToRevenueInOriginalCurrencyMap.get("K618");

			if (revenueInOriginalCurrency != null) {
				BigDecimal revenueInUsd = revenueInOriginalCurrency.multiply(currencyExchangeRateToUsd);
				totalK618Revenue = totalK618Revenue.add(revenueInUsd);
			}
		}
		System.out.println("K618 total amount: " + totalK618Revenue.toPlainString());


		for(Country country:countryList){
			Map<String,BigDecimal> supplierIdToRevenueInOriginalCurrencyMap = countryToKcodeToRevenueMap.get(country);
			Map<String,BigDecimal> supplierIdToRevenueInOriginalCurrencyMap2022 = countryToKcodeToRevenueMap2022.get(country);

			Currency originalCurrency = country.getCurrency();
			BigDecimal currencyExchangeRateToUsd = this.getCurrencyExchangeRateToUsd(start,end,originalCurrency);

			for(String supplierKcode:supplierIdToRevenueInOriginalCurrencyMap.keySet()){
				BigDecimal revenueInOriginalCurrency = supplierIdToRevenueInOriginalCurrencyMap.get(supplierKcode);
				BigDecimal revenueInUsd = revenueInOriginalCurrency.multiply(currencyExchangeRateToUsd);

				BigDecimal retainmentRate = calculateRetainmentRate(
						supplierKcode, revenueInUsd, totalK618Revenue);
				this.dao.insert(start,end,country,supplierKcode,originalCurrency,revenueInOriginalCurrency,currencyExchangeRateToUsd,revenueInUsd,retainmentRate);
			}

			for(String supplierKcode:supplierIdToRevenueInOriginalCurrencyMap2022.keySet()){
				BigDecimal revenueInOriginalCurrency = supplierIdToRevenueInOriginalCurrencyMap2022.get(supplierKcode);
				BigDecimal revenueInUsd = revenueInOriginalCurrency.multiply(currencyExchangeRateToUsd);

				BigDecimal retainmentRate = calculateRetainmentRate2022(revenueInUsd);
				this.dao.insert(start,end,country,supplierKcode,originalCurrency,revenueInOriginalCurrency,currencyExchangeRateToUsd,revenueInUsd,retainmentRate);
			}
		}
	}

	private BigDecimal calculateRetainmentRate(String supplierCode, BigDecimal revenueInUsd,
											   BigDecimal totalK618Revenue) {
		if (supplierCode.equals("K612")) {
			return BigDecimal.valueOf(.1);
		}

		if (supplierCode.equals("K618") && totalK618Revenue.compareTo(BigDecimal.valueOf(15000)) >= 0) {
			return RevenueGrade.calculateEffectiveRetainmentRate(totalK618Revenue);
		}

		return RevenueGrade.calculateEffectiveRetainmentRate(revenueInUsd);
	}

	private BigDecimal calculateRetainmentRate2022(BigDecimal revenueInUsd) {

		return RevenueGrade2022.calculateEffectiveRetainmentRate(revenueInUsd);
	}
	
	private BigDecimal getCurrencyExchangeRateToUsd(Date start, Date end, Currency originalCurrency) {
		return originalCurrency==Currency.USD?BigDecimal.ONE:this.currencyRepo.queryExchangeRate(start, end, originalCurrency, Currency.USD, DrsConstants.interbankRateForCalculatingDrsRetainmentRate);
	}

	private final List<String> revenueRelatedDescriptionList = Arrays.asList(
			AmzAmountTypeDesc.FBA_DAMAGE.getDesc(),
			AmzAmountTypeDesc.FBA_LOST.getDesc(),
			AmzAmountTypeDesc.FBA_RETURN.getDesc(),
			AmzAmountTypeDesc.FBA_REVERSAL_REIMBURSEMENT.getDesc(),
			AmzAmountTypeDesc.FBA_CS_ERROR_NON_ITEMIZED.getDesc(),
			AmzAmountTypeDesc.FBA_RE_EVALUATION.getDesc(),
			AmzAmountTypeDesc.FBA_MISSING_FROM_INBOUND.getDesc());
	
	private Map<String,BigDecimal> getSupplierIdToRevenueMap(Country country,Date start,Date end,int revenueGradeVer){
		Map<String,BigDecimal> supplierRevenueMap = new HashMap<String,BigDecimal>();
		Marketplace marketplace = this.getAmazonMarketplace(country);
		this.addToTotalRevenue(supplierRevenueMap, this.dao.querySupplierAmazonOrderRevenue(marketplace,start,end,revenueGradeVer));
		if(country==Country.US)this.addToTotalRevenue(supplierRevenueMap,this.dao.querySupplierShopifyRevenue(Marketplace.TRUETOSOURCE,start,end,revenueGradeVer));
		if(country==Country.US)this.addToTotalRevenue(supplierRevenueMap,this.dao.querySupplierEbayRevenue(Marketplace.EBAY_COM,start,end,revenueGradeVer));
		if(country==Country.DE)this.addToTotalRevenue(supplierRevenueMap,this.dao.querySupplierEbayRevenue(Marketplace.EBAY_DE,start,end,revenueGradeVer));
		if(country==Country.IT)this.addToTotalRevenue(supplierRevenueMap,this.dao.querySupplierEbayRevenue(Marketplace.EBAY_IT,start,end,revenueGradeVer));
		if(country== Country.US||country==Country.UK||country==Country.CA||country==Country.FR){
			this.addToTotalRevenue(supplierRevenueMap, this.dao.querySupplierIdToFbaReimbursementRevenueMap(country,start,end,this.revenueRelatedDescriptionList,revenueGradeVer));
		}
		return supplierRevenueMap;
	}
	
	private Marketplace getAmazonMarketplace(Country country){
		if(country==Country.US) return Marketplace.AMAZON_COM;
		if(country==Country.UK) return Marketplace.AMAZON_CO_UK;
		if(country==Country.CA) return Marketplace.AMAZON_CA;
		if(country==Country.DE) return Marketplace.AMAZON_DE;
		if(country==Country.IT) return Marketplace.AMAZON_IT;
		if(country==Country.FR) return Marketplace.AMAZON_FR;
		if(country==Country.ES) return Marketplace.AMAZON_ES;
		if(country==Country.MX) return Marketplace.AMAZON_COM_MX;
		Assert.isTrue(false);
		return null;
	}
	
	private void addToTotalRevenue(Map<String,BigDecimal> revenueMap,Map<String,BigDecimal> mapToAdd){
		for(String kcode:mapToAdd.keySet()){
			BigDecimal newRevenue = mapToAdd.get(kcode);
			if(revenueMap.containsKey(kcode)) newRevenue = newRevenue.add(revenueMap.get(kcode));
			revenueMap.put(kcode,newRevenue);
		}
	}
	
	@Override
	public int delete(int rateId) {
		return this.dao.delete(rateId);
	}

	@Override
	public String getRate(String utcStartDate,String utcEndDate,Country country,String supplierKcode) {
		Date start = this.toUtcDate(utcStartDate);
		Date end = this.getEndPoint(utcEndDate);
		BigDecimal rate = this.dao.queryRate(start, end, country.getKey(), supplierKcode);
		return rate.toPlainString();
	}
	


	@Override
	public String getRevenueInOriginalCurrency(String startDate, String endDate, Country c, String supplierKcode) {
		Date start = this.toUtcDate(startDate);
		Date end = this.getEndPoint(endDate);
		return this.dao.queryRevenueInOriginalCurrency(start, end, c.getKey(), supplierKcode).toPlainString();
	}

	private Date getEndPoint(String utcEndDate) {
		Date end = this.toUtcDate(utcEndDate);
		return this.addOneDay(end);
	}

	private Date addOneDay(Date d){
		if (d == null) return null;
	    Calendar c = Calendar.getInstance();
	    c.setTime(d);
	    c.add(Calendar.DATE, 1);
	    d.setTime( c.getTime().getTime() );
	    return d;
	}
	
	private Date toUtcDate(String dateStr){
		if (!StringUtils.hasText(dateStr)) return null;
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd z");
			date = sdf.parse(dateStr + " UTC");
		} catch (ParseException e) { e.printStackTrace();}
		return date;
	}

	@Override
	public void calculateForTest(String utcStartDate, String utcEndDate) {
		Date start = this.toUtcDate(utcStartDate);
		Date end = this.getEndPoint(utcEndDate);
		if (start != null && end != null) {
			Assert.isTrue(start.before(end));
			this.calculate(start,end);
		}
	}

}