package com.kindminds.drs.service.helper;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.kindminds.drs.api.v2.biz.domain.model.MarketingReportHelper;
import com.kindminds.drs.v1.model.impl.report.SkuAdvertisingPerformanceReportLineItemImpl;
import com.kindminds.drs.api.v1.model.report.SkuAdvertisingPerformanceReportLineItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kindminds.drs.Marketplace;

import com.kindminds.drs.api.data.access.rdb.report.MarketingReportHelperDao;


@Service
public class MarketingReportHelperImpl implements MarketingReportHelper {
	
	@Autowired private MarketingReportHelperDao dao;

	@Override
	public List<SkuAdvertisingPerformanceReportLineItem> getSkuAdvertisingPerformanceReportLineItems(String supplierKcode, Marketplace marketplace, Date start, Date end) {


	 List<Object []> columnsList =	this.dao.querySkuAdvertisingPerformanceReportLineItems(supplierKcode,marketplace,start,end,null);

		List<SkuAdvertisingPerformanceReportLineItem> resultList = new ArrayList<>();
		for(Object[] columns:columnsList){
			String currency = (String)columns[0];
			String sku = (String)columns[1];
			Integer totalImpressions = BigInteger.valueOf(Long.parseLong(columns[2].toString())).intValue();
			Integer totalClicks =BigInteger.valueOf(Long.parseLong(columns[3].toString())).intValue();
			BigDecimal totalSpend = (BigDecimal)columns[4];
			Integer totalOneWeekOrdersPlaced = BigInteger.valueOf(Long.parseLong(columns[5].toString())).intValue();
			BigDecimal totalOneWeekOrderedProductSales = (BigDecimal)columns[6];
			resultList.add(new SkuAdvertisingPerformanceReportLineItemImpl(currency,sku,totalImpressions,totalClicks,totalSpend,totalOneWeekOrdersPlaced,totalOneWeekOrderedProductSales));
		}

		return resultList;
	}

	@Override
	public List<SkuAdvertisingPerformanceReportLineItem> getSkuAdvertisingPerformanceReportLineItems(String supplierKcode, Marketplace marketplace, Date start, Date end, String campaignName) {


		List<Object []> columnsList = this.dao.querySkuAdvertisingPerformanceReportLineItems(supplierKcode,marketplace,start,end,campaignName);

		List<SkuAdvertisingPerformanceReportLineItem> resultList = new ArrayList<>();
		for(Object[] columns:columnsList){
			String currency = (String)columns[0];
			String sku = (String)columns[1];
			Integer totalImpressions = BigInteger.valueOf(Long.parseLong(columns[2].toString())).intValue();
			Integer totalClicks = BigInteger.valueOf(Long.parseLong(columns[3].toString())).intValue();
			BigDecimal totalSpend = (BigDecimal)columns[4];
			Integer totalOneWeekOrdersPlaced = BigInteger.valueOf(Long.parseLong(columns[5].toString())).intValue();
			BigDecimal totalOneWeekOrderedProductSales = (BigDecimal)columns[6];
			resultList.add(new SkuAdvertisingPerformanceReportLineItemImpl(currency,sku,totalImpressions,totalClicks,totalSpend,totalOneWeekOrdersPlaced,totalOneWeekOrderedProductSales));
		}

		return resultList;


	}

}
