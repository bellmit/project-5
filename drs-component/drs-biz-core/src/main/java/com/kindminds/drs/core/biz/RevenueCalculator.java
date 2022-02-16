package com.kindminds.drs.core.biz;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.kindminds.drs.service.util.SpringAppCtx;
import org.springframework.stereotype.Service;

import com.kindminds.drs.Country;
import com.kindminds.drs.persist.data.access.rdb.RevenueCalculatorDao;

@Service
public class RevenueCalculator {
	
	public Map<Country,Map<String,BigDecimal>> getRevenueGroupByCountryAndProductBase(Date start,Date end, String supplierKcode){
		RevenueCalculatorDao dao = (RevenueCalculatorDao) SpringAppCtx.get().getBean("revenueCalculatorDao");
		Map<Country,Map<String,BigDecimal>> totalCountryProductBaseRevenue = new HashMap<>();
		this.addToTotalRevenue(totalCountryProductBaseRevenue,dao.queryRevenueGroupByCountryAndProductBaseFromAmazon( start,end,supplierKcode));
		this.addToTotalRevenue(totalCountryProductBaseRevenue,dao.queryRevenueGroupByCountryAndProductBaseFromEbay(   start,end,supplierKcode));
		this.addToTotalRevenue(totalCountryProductBaseRevenue,dao.queryRevenueGroupByCountryAndProductBaseFromShopify(start,end,supplierKcode));
		return totalCountryProductBaseRevenue;
	}
	
	private void addToTotalRevenue(Map<Country,Map<String,BigDecimal>> revenueTotal,Map<Country,Map<String,BigDecimal>> revenueToAdd){
		for(Country country:revenueToAdd.keySet()){
			for(String productBase:revenueToAdd.get(country).keySet()){
				if(!revenueTotal.containsKey(country)) revenueTotal.put(country, new HashMap<>());
				BigDecimal resultAmount = revenueToAdd.get(country).get(productBase);
				if(revenueTotal.get(country).containsKey(productBase)){
					resultAmount = resultAmount.add(revenueTotal.get(country).get(productBase));
				}
				revenueTotal.get(country).put(productBase,resultAmount);
			}
		}
	}
	
}
