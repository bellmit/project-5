package com.kindminds.drs.service.usecase.report;


import com.kindminds.drs.Currency;
import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.usecase.ViewSkuAdvertisingEvaluationReportUco;
import com.kindminds.drs.api.v1.model.accounting.SettlementPeriod;
import com.kindminds.drs.v1.model.impl.accounting.SettlementPeriodImpl;
import com.kindminds.drs.v1.model.impl.report.SkuAdvertisingEvaluationReportLineItemDataFromCampaignPerformanceReportImpl;
import com.kindminds.drs.v1.model.impl.report.SkuAdvertisingEvaluationReportLineItemImpl;
import com.kindminds.drs.api.v1.model.report.SkuAdvertisingEvaluationReport;
import com.kindminds.drs.api.v1.model.report.SkuAdvertisingEvaluationReport.SkuAdvertisingEvaluationReportLineItem;
import com.kindminds.drs.api.v1.model.report.SkuAdvertisingEvaluationReport.SkuAdvertisingEvaluationReportLineItemDataFromCampaignPerformanceReport;
import com.kindminds.drs.api.data.access.rdb.CompanyDao;
import com.kindminds.drs.api.data.access.usecase.report.ViewSkuAdvertisingEvaluationReportDao;
import com.kindminds.drs.v1.model.impl.SkuAdvertisingEvaluationReportImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

@Service
public class ViewSkuAdvertisingEvaluationReportUcoImpl implements ViewSkuAdvertisingEvaluationReportUco {
	
	@Autowired private ViewSkuAdvertisingEvaluationReportDao dao;
	@Autowired private CompanyDao companyRepo;

	@Override
	public List<Marketplace> getMarketplaces() {
		return Marketplace.getAmazonMarketplaces();
	}

	@Override
	public SkuAdvertisingEvaluationReport getReport(String supplierKcode,String marketplaceId,String settlementPeriodId) {
		if(StringUtils.hasText(supplierKcode)&&StringUtils.hasText(settlementPeriodId)&&StringUtils.hasText(marketplaceId)){
			int periodId = Integer.valueOf(settlementPeriodId);
			Marketplace marketplace = Marketplace.fromKey(Integer.parseInt(marketplaceId));
			List<Object []> columnsList = this.dao.queryLineItems(supplierKcode, marketplace.getKey(),periodId);

            List<SkuAdvertisingEvaluationReportLineItem> lineItems = new ArrayList<>();
            for(Object[] columns:columnsList){
                String sku = (String)columns[0];
                String skuName = (String)columns[1];
                Integer totalSessions = BigInteger.valueOf(Long.parseLong(columns[2].toString())).intValue();
                Integer totalPageViews = BigInteger.valueOf(Long.parseLong(columns[3].toString())).intValue();
                BigDecimal totalBuyBoxTimesSessions = (BigDecimal)columns[4];
                Integer totalUnitOrdered = BigInteger.valueOf(Long.parseLong(columns[5].toString())).intValue();
                BigDecimal totalOrderedProductSales = (BigDecimal)columns[6];
                lineItems.add(new SkuAdvertisingEvaluationReportLineItemImpl(
                        sku, skuName, totalSessions, totalPageViews, totalBuyBoxTimesSessions,
                        totalUnitOrdered, totalOrderedProductSales));
            }

			this.setLineItemsCurrency(marketplace.getCountry().getCurrency(), lineItems);
			this.setLineItemDateFromCampaignPerformanceReport(lineItems, marketplace, periodId);
			SkuAdvertisingEvaluationReportImpl report = new SkuAdvertisingEvaluationReportImpl(lineItems);
			report.setMarketplace(marketplace);
			report.setCurrency(marketplace.getCountry().getCurrency());
			return report;
		}
		return null;
	}
	
	private void setLineItemsCurrency(Currency currency,List<SkuAdvertisingEvaluationReportLineItem> lineItems){
		for(SkuAdvertisingEvaluationReportLineItem item:lineItems ){
			((SkuAdvertisingEvaluationReportLineItemImpl)item).setCurrency(currency);
		}
	}
	
	private void setLineItemDateFromCampaignPerformanceReport( List<SkuAdvertisingEvaluationReportLineItem> lineItems,Marketplace marketplace,int periodId) {
		List<String> skuList = this.getSkuListFromReportLineItems(lineItems);
		Date start = this.dao.queryPeriodStart(periodId);
		Date end = this.dao.queryPeriodEnd(periodId);

        Map<String, SkuAdvertisingEvaluationReportLineItemDataFromCampaignPerformanceReport> result = new HashMap<>();
        List<Object []> columnsList = this.dao.querySkuInfoFromCampaignPerformanceReport(skuList, marketplace.getKey(), start, end);

        for(Object[] columns:columnsList){
            String sku = (String)columns[0];
            int totalClicks =BigInteger.valueOf(Long.parseLong(columns[1].toString())).intValue();
            BigDecimal totalSpend = (BigDecimal)columns[2];
            BigDecimal totalOneWeekOrderedProductSales = (BigDecimal)columns[3];
            Assert.isTrue(!result.containsKey(sku));
            result.put(sku, new SkuAdvertisingEvaluationReportLineItemDataFromCampaignPerformanceReportImpl(totalClicks, totalSpend, totalOneWeekOrderedProductSales));
        }


		for(SkuAdvertisingEvaluationReportLineItem item:lineItems ){
			SkuAdvertisingEvaluationReportLineItemDataFromCampaignPerformanceReport data = result.get(item.getSku());
			((SkuAdvertisingEvaluationReportLineItemImpl)item).setDataFromCampaignPerformanceReport(data);
		}
	}
	
	private List<String> getSkuListFromReportLineItems(List<SkuAdvertisingEvaluationReportLineItem> lineItems){
		List<String> skuList = new ArrayList<String>();
		for(SkuAdvertisingEvaluationReportLineItem lineItem:lineItems){
			skuList.add(lineItem.getSku());
		}
		return skuList;
	}

	@Override
	public List<SettlementPeriod> getSettlementPeriodList() {
		List<Object []> columnsList = this.dao.querySettlementPeriodList();

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
	public Map<String,String> getSupplierKcodeNameMap() {
		return this.companyRepo.querySupplierKcodeToShortEnUsNameMap();
	}

	@Override
	public String getDefaultMarketplaceId(){
		return String.valueOf(Marketplace.AMAZON_COM.getKey());
	}
	
}
