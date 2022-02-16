package com.kindminds.drs.service.usecase.report;

import com.kindminds.drs.Context;
import com.kindminds.drs.Country;
import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.usecase.ViewMonthlyStorageFeeReportUco;
import com.kindminds.drs.v1.model.impl.report.MonthlyStorageFeeReportLineItemImpl;
import com.kindminds.drs.api.v1.model.report.MonthlyStorageFeeReport;
import com.kindminds.drs.api.v1.model.report.MonthlyStorageFeeReport.MonthlyStorageFeeReportLineItem;
import com.kindminds.drs.api.data.access.rdb.CompanyDao;
import com.kindminds.drs.api.data.access.usecase.report.ViewMonthlyStorageFeeReportDao;
import com.kindminds.drs.v1.model.impl.MonthlyStorageFeeReportImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

@Service
public class ViewMonthlyStorageFeeReportUcoImpl implements ViewMonthlyStorageFeeReportUco{

	@Autowired private ViewMonthlyStorageFeeReportDao dao;
	@Autowired private CompanyDao companyRepo;
	
	private final Map<String,Integer> countryToMarketplaceId = Marketplace.getCountryToMarketplaceIdMap();
	
	private int yearStart = 2017;
	
	@Override
	public Map<String, String> getSupplierKcodeToShortEnNameMap() {
		if(Context.getCurrentUser().isDrsUser()){
			return this.companyRepo.querySupplierKcodeToShortEnUsNameWithRetailMap();
		}
		return null;
	}
	
	@Override
	public List<Country> getCountries() {
		return Country.getAmazonCountryList();
	}
	
	@Override
	public List<String> getYears() {
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		int length = currentYear - yearStart;
		List<String> years = new ArrayList<>();
		for(int i=0;i<length;i++) years.add(String.valueOf(currentYear-i));
		return years;		
	}

	@Override
	public List<String> getMonths() {
		return Arrays.asList("01","02","03","04","05","06","07","08","09","10","11","12");
	}

	@Override
	public boolean isDrsUser() {
		return Context.getCurrentUser().isDrsUser();
	}

	@Override
	public String getActualSupplierKcode(String requestSupplierKcode) {
		String userCompanyKcode = Context.getCurrentUser().getCompanyKcode();
		if(Context.getCurrentUser().isDrsUser()){
			if(!StringUtils.hasText(requestSupplierKcode)) return null;
			return requestSupplierKcode;
		}else return userCompanyKcode;
	}
	
	@Override
	public String getDefaultMarketplaceId() {
		return String.valueOf(Marketplace.AMAZON_COM.getKey());
	}

	@Override
	public String getDefaultCountry() {
		return Country.US.toString();
	}
	
	@Override
	public String getDefaultYear() {
		return String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
	}

	@Override
	public String getDefaultMonth() {
		int zeroBasedMonth = Calendar.getInstance().get(Calendar.MONTH);
		return String.format("%02d",zeroBasedMonth);
	}

	@Override
	public MonthlyStorageFeeReport getReport(String supplierKcode, String country, String year,
			String month) {
		List<Object []> columnsList = this.dao.queryItemList(supplierKcode, country, this.countryToMarketplaceId.get(country), year, month);

		List<MonthlyStorageFeeReportLineItem> resultList = new ArrayList<>();
		for(Object[] columns:columnsList){
			String sku = (String)columns[0];
			String skuName = (String)columns[1];
			BigDecimal itemVolume = (BigDecimal)columns[3];
			String volumeUnits = (String)columns[4];
			BigDecimal totalAverageQuantityOnHand = (BigDecimal)columns[5];
			BigDecimal totalAverageQuantityPendingRemoval = (BigDecimal)columns[6];
			BigDecimal totalEstimatedTotalItemVolume = (BigDecimal)columns[7];
			String monthOfCharge = (String)columns[8];
			BigDecimal storageRate = (BigDecimal)columns[9];
			String currency = (String)columns[10];
			BigDecimal totalEstimatedMonthlyStorageFee = (BigDecimal)columns[11];
			resultList.add(new MonthlyStorageFeeReportLineItemImpl(sku, skuName, itemVolume, volumeUnits, totalAverageQuantityOnHand, totalAverageQuantityPendingRemoval, totalEstimatedTotalItemVolume, monthOfCharge, storageRate, currency, totalEstimatedMonthlyStorageFee));
		}


		MonthlyStorageFeeReportImpl report = new MonthlyStorageFeeReportImpl(resultList);
		return report;
	}

}