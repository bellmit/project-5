package com.kindminds.drs.service.usecase.product;

import com.kindminds.drs.Country;
import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.usecase.ViewKeyProductStatsUco;
import com.kindminds.drs.v1.model.impl.FbaQuantitiesImpl;
import com.kindminds.drs.api.v1.model.report.KeyProductStatsReport;
import com.kindminds.drs.api.v1.model.report.KeyProductStatsReport.KeyProductStatsByCountry;
import com.kindminds.drs.api.v1.model.report.KeyProductStatsReport.KeyProductStatsReportLineItem;
import com.kindminds.drs.api.data.access.usecase.product.ViewKeyProductStatsDao;
import com.kindminds.drs.api.data.access.usecase.product.ViewKeyProductStatsDao.FbaQuantities;

import com.kindminds.drs.enums.DisplayOption;
import com.kindminds.drs.persist.v1.model.mapping.product.KeyProductStatsReportLineItemImpl;
import com.kindminds.drs.v1.model.impl.KeyProductStatsByCountryImpl;
import com.kindminds.drs.v1.model.impl.KeyProductStatsReportImpl;
import com.kindminds.drs.util.DateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.*;

//import com.kindminds.drs.Context;

@Service 
public class ViewKeyProductStatsUcoImpl implements ViewKeyProductStatsUco {
	
	@Autowired private ViewKeyProductStatsDao dao;
	
	private final List<Country> availableCountryList = Arrays.asList(Country.US, Country.UK, Country.CA, Country.DE, Country.FR, Country.IT, Country.ES);

	@Override
	public KeyProductStatsReport getKeyProductStatsReport(Boolean isSupplier ,
                                                          String companyKcode) {
		String supplierKcode = this.getAssignedSupplierKcode(isSupplier , companyKcode);
		DisplayOption displayOption = this.getDisplayOption(isSupplier);
		List<KeyProductStatsByCountry> keyProductStatsByCountryList = new ArrayList<KeyProductStatsByCountry>();
		for(Country country:this.availableCountryList){
			if (country.getKey() == 11) {
				continue;
			}

			Country unsDestinationCountry = this.getUnsDestinationCountry(country);
			List<Marketplace> marketplaces = this.getMarketplaces(country);

			List<KeyProductStatsReportLineItem> lineItemsByCountry = this.dao.queryLineItems(
					this.toMarketplaceIds(marketplaces),supplierKcode, displayOption);

			if(lineItemsByCountry!=null&&!lineItemsByCountry.isEmpty()){
				this.appendQtyOrderedInCurrentPeriod(supplierKcode,marketplaces,lineItemsByCountry);
				this.appendQtyOrderedInLastSevenDays(supplierKcode,marketplaces,lineItemsByCountry);
				this.appendQtyToReceive(supplierKcode,unsDestinationCountry,lineItemsByCountry);
				this.appendQtyAtSettlementReportEndPlusQtyReceivedInCurrentSettlement(supplierKcode,country,lineItemsByCountry);
				this.appendFbaInStockQuantity(country, lineItemsByCountry);

				Map<String,List<KeyProductStatsReportLineItem>> basesToLineItemsMap = this.mapLineItemByBaseCode(lineItemsByCountry);
				KeyProductStatsByCountryImpl keyProductStatsByCountry = new KeyProductStatsByCountryImpl();
				keyProductStatsByCountry.setCountry(country);
				keyProductStatsByCountry.setCurrency(country.getCurrency());
				keyProductStatsByCountry.setLastUpdateDateUtc(this.getLastUpdateDateUtc(isSupplier&&!supplierKcode.equals("K101")?supplierKcode:"K488",country));
				keyProductStatsByCountry.setBaseToKeySkuStatsListMap(basesToLineItemsMap);
				if(!isSupplier) keyProductStatsByCountry.setEstimatedSevenDayRevenue(this.calculateEstimatedSevenDayRevenue(lineItemsByCountry));
				keyProductStatsByCountryList.add(keyProductStatsByCountry);
			}

		}
		KeyProductStatsReportImpl report = new KeyProductStatsReportImpl();
		report.setNextStatementPeriod(this.composeNextSettlementPeriodStr(isSupplier&&!supplierKcode.equals("K101")?supplierKcode:"K488"));
		report.setKeyProductStatsByCountryList(keyProductStatsByCountryList);
		return report;
	}

	
	private String getAssignedSupplierKcode(Boolean isSupplier , String companyKCode){
		return isSupplier? companyKCode:null;

	}
	
	private DisplayOption getDisplayOption(Boolean isSupplier){
		return isSupplier? DisplayOption.SUPPLIER: DisplayOption.DRS;
	}
	
	private Country getUnsDestinationCountry(Country country){
		List<Country> euroCountries=Arrays.asList(Country.DE, Country.FR, Country.IT, Country.ES);
		if(euroCountries.contains(country)) return Country.UK;
		return country;
	}
	
	private List<Marketplace> getMarketplaces(Country country){
		List<Marketplace> marketplaces = null;
		if(country== Country.US) marketplaces = Arrays.asList(Marketplace.AMAZON_COM, Marketplace.TRUETOSOURCE);
		if(country== Country.UK) marketplaces = Arrays.asList(Marketplace.AMAZON_CO_UK);
		if(country== Country.CA) marketplaces = Arrays.asList(Marketplace.AMAZON_CA);
		if(country== Country.DE) marketplaces = Arrays.asList(Marketplace.AMAZON_DE);
		if(country== Country.FR) marketplaces = Arrays.asList(Marketplace.AMAZON_FR);
		if(country== Country.IT) marketplaces = Arrays.asList(Marketplace.AMAZON_IT);
		if(country== Country.ES) marketplaces = Arrays.asList(Marketplace.AMAZON_ES);
		Assert.notNull(marketplaces);
		return marketplaces;
	}
	
	private List<Integer> toMarketplaceIds(List<Marketplace> marketplaces){
		List<Integer> ids = new ArrayList<>();
		for(Marketplace marketplace:marketplaces){
			ids.add(marketplace.getKey());
		}
		return ids;
	}
	
	private void appendQtyOrderedInCurrentPeriod(String supplierKcode, List<Marketplace> marketplaces, List<KeyProductStatsReportLineItem> lineItemsByCountry) {
		Date latestPeriodEnd = this.dao.queryLatestStatementPeriodEnd(supplierKcode==null||supplierKcode.equals("K101")?"K488":supplierKcode);
		Date nextPeriodStart = DateHelper.addDays((Date)latestPeriodEnd.clone(), 14);
		Map<String,Integer> totalQtyOrdered = this.getTotalQtyOrdered(supplierKcode, marketplaces, latestPeriodEnd, nextPeriodStart);
		for(KeyProductStatsReportLineItem item:lineItemsByCountry){
			if(totalQtyOrdered.containsKey(item.getSkuCode())){
				((KeyProductStatsReportLineItemImpl)item).setQtyOrderedInCurrentSettlementPeriod(totalQtyOrdered.get(item.getSkuCode()));
			}
		}
	}
	
	private void appendQtyOrderedInLastSevenDays(String supplierKcode, List<Marketplace> marketplaces, List<KeyProductStatsReportLineItem> lineItemsByCountry) {
		Date sevenDaysAgo = DateHelper.addDays(new Date(),-7);
		Map<String,Integer> totalQtyOrdered = this.getTotalQtyOrdered(supplierKcode,marketplaces,sevenDaysAgo,null);
		for(KeyProductStatsReportLineItem item:lineItemsByCountry){
			if(totalQtyOrdered.containsKey(item.getSkuCode())){
				((KeyProductStatsReportLineItemImpl)item).setQtyOrderedInLastSevenDays(totalQtyOrdered.get(item.getSkuCode()));
			}
		}
	}
	
	private Map<String,Integer> getTotalQtyOrdered(String supplierKcode, List<Marketplace> marketplaces, Date from, Date to){
		Map<String,Integer> totalQtyOrdered = new HashMap<>();
		for(Marketplace marketplace:marketplaces){
			if(marketplace.isAmazonMarketplace()){
				Map<String,Integer> amazonQtyOrdered = this.dao.queryAmazonQtyOrdered(marketplace, supplierKcode, from, to);

				this.accumulate(totalQtyOrdered, amazonQtyOrdered);
			}
			if(marketplace.isShopifyMarketplace()){
				Map<String,Integer> shopifyQtyOrdered = this.dao.queryShopifyQtyOrdered(marketplace, supplierKcode, from, to);
				this.accumulate(totalQtyOrdered, shopifyQtyOrdered);
			}
		}
		return totalQtyOrdered;
	}
	
	private void accumulate(Map<String,Integer> total,Map<String,Integer> subTotal) {
		for(String subTotalSku :subTotal.keySet()){
			if(!total.containsKey(subTotalSku))
				total.put(subTotalSku, 0);
			total.put(subTotalSku,total.get(subTotalSku)+subTotal.get(subTotalSku));
		}
	}
	
	private void appendQtyToReceive(String supplierKcode, Country country, List<KeyProductStatsReportLineItem> lineItemsByCountry) {
		Map<String,Integer> skuQtyMap = this.dao.queryQtyToReceive(country, supplierKcode);
		for(KeyProductStatsReportLineItem item:lineItemsByCountry){
			if(skuQtyMap.containsKey(item.getSkuCodeByDrs())){
				((KeyProductStatsReportLineItemImpl)item).setQtyToReceive(skuQtyMap.get(item.getSkuCodeByDrs()));
			}
		}
	}
	
	private void appendQtyAtSettlementReportEndPlusQtyReceivedInCurrentSettlement(String supplierKcode, Country country, List<KeyProductStatsReportLineItem> lineItemsByCountry) {
		Map<String,Integer> skuQtyMap = this.dao.queryQtyAtDrsSettlementEndPlusQtyReceivedInCurrentSettlement(country,supplierKcode);
		for(KeyProductStatsReportLineItem item:lineItemsByCountry){
			if(skuQtyMap.containsKey(item.getSkuCodeByDrs())){
				((KeyProductStatsReportLineItemImpl)item).setQtyInStock(skuQtyMap.get(item.getSkuCodeByDrs()));
			}
		}
	}
	
	private void appendFbaInStockQuantity(Country country, List<KeyProductStatsReportLineItem> lineItemsByCountry) {
		Marketplace marketplace = null;
		if(country== Country.US) marketplace= Marketplace.AMAZON_COM;
		else if(country== Country.UK) marketplace= Marketplace.AMAZON_CO_UK;
		else if(country== Country.CA) marketplace= Marketplace.AMAZON_CA;
		else if(country== Country.DE) marketplace= Marketplace.AMAZON_DE;
		else if(country== Country.FR) marketplace= Marketplace.AMAZON_FR;
		else if(country== Country.IT) marketplace= Marketplace.AMAZON_IT;
		else if(country== Country.ES) marketplace= Marketplace.AMAZON_ES;
		Assert.notNull(marketplace);
		List<Object []>  result= this.dao.querySkuFbaQuantities(marketplace);

		Map<String,FbaQuantities> marketplaceSkuQtyMap = new HashMap<>();

		for(Object[] items:result){
			String marketplaceSku = (String)items[0];
			Integer inBound = (Integer)items[1];
			Integer inStock = (Integer)items[2];
			Integer transfer = (Integer)items[3];
			marketplaceSkuQtyMap.put(marketplaceSku, new FbaQuantitiesImpl(inBound, inStock, transfer));
		}


		for(KeyProductStatsReportLineItem item:lineItemsByCountry){
			KeyProductStatsReportLineItemImpl origItem = (KeyProductStatsReportLineItemImpl)item;
			if(marketplaceSkuQtyMap.containsKey(origItem.getMarketplaceSku())){
				origItem.setFbaQtyInBound((marketplaceSkuQtyMap.get(origItem.getMarketplaceSku()).getInBound()));
				origItem.setFbaQtyInStock((marketplaceSkuQtyMap.get(origItem.getMarketplaceSku()).getInStock()));
				origItem.setFbaQtyTransfer((marketplaceSkuQtyMap.get(origItem.getMarketplaceSku()).getTransfer()));
			}
		}
	}

	
	private BigDecimal calculateEstimatedSevenDayRevenue(List<KeyProductStatsReportLineItem> lineItemsByCountry) {
		BigDecimal estimatedSevenDayRevenue = BigDecimal.ZERO;
		for(KeyProductStatsReportLineItem item:lineItemsByCountry){
			BigDecimal skuEstimatedSevenDayRevenue = item.getNumericCurrentBaseRetailPrice().multiply(item.getNumericQtyOrderedInLastSevenDays());
			estimatedSevenDayRevenue = estimatedSevenDayRevenue.add(skuEstimatedSevenDayRevenue);
		}
		return estimatedSevenDayRevenue;
	}
	
	private Map<String,List<KeyProductStatsReportLineItem>> mapLineItemByBaseCode(List<KeyProductStatsReportLineItem> rawList){
		Map<String,List<KeyProductStatsReportLineItem>> resultMap = new TreeMap<String,List<KeyProductStatsReportLineItem>>();
		for(KeyProductStatsReportLineItem item: rawList){
			if(resultMap.containsKey(item.getBaseCode())){
				resultMap.get(item.getBaseCode()).add(item);
			}
			else{
				List<KeyProductStatsReportLineItem> newList = new ArrayList<KeyProductStatsReportLineItem>();
				newList.add(item);
				resultMap.put(item.getBaseCode(),newList);
			}
		}
		return resultMap;
	}
	
	private String getLastUpdateDateUtc(String supplierKcode, Country country){
		String lastStatementPeriodEnd = this.dao.queryLatestStatementPeriodEndDateStr(supplierKcode);
		String amazonOrderLastUpdateDate = null;
		if(country== Country.US||country== Country.CA)
			amazonOrderLastUpdateDate = this.dao.queryAmazonOrderLastUpdateDate(Country.NA);
		if(country== Country.UK||country== Country.DE||country== Country.FR||country== Country.IT||country== Country.ES)
			amazonOrderLastUpdateDate = this.dao.queryAmazonOrderLastUpdateDate(Country.EU);
		return this.getLaterDateStr(lastStatementPeriodEnd, amazonOrderLastUpdateDate);
	}
	
	private String getLaterDateStr(String dateStr1,String dateStr2){
		Assert.isTrue(dateStr1.length()==dateStr2.length());
		return dateStr1.compareTo(dateStr2)>0?dateStr1:dateStr2;
	}
	
	private String composeNextSettlementPeriodStr(String supplierKcode){
		String start = this.dao.queryLatestStatementPeriodEndDateUtc(supplierKcode);
		String end   = this.dao.queryNextStatementPeriodStartDateUtc(supplierKcode);
		return start + " ~ " +end;
	}



}
