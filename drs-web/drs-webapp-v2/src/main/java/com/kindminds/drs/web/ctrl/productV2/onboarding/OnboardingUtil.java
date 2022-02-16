package com.kindminds.drs.web.ctrl.productV2.onboarding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kindminds.drs.util.JsonHelper;

public class OnboardingUtil {
	
	public static String getMarketSideRegionListJson() {
		return JsonHelper.toJson(getMarketSideRegionList());
	}
	
	public static String getRegionToMarketplaceMapJson() {
		return JsonHelper.toJson(getRegionToMarketplaceMap());
	}
	
	
	private static List<String> getMarketSideRegionList(){
		//TODO : refactoring
		List<String> marketSideRegionList = new ArrayList<String>(); 		
		marketSideRegionList.add("US");
		marketSideRegionList.add("UK");
		marketSideRegionList.add("CA");
		marketSideRegionList.add("DE");
		marketSideRegionList.add("IT");
		marketSideRegionList.add("FR");
		marketSideRegionList.add("ES");		
		return marketSideRegionList;
	}
	
	private static Map<String,List<String>> getRegionToMarketplaceMap(){
		//TODO : refactoring
		Map<String,List<String>> regionToMarketplaceMap = new HashMap<String,List<String>>();
		List<String> marketplaceUS = new ArrayList<String>();
		marketplaceUS.add("Amazon US");
		List<String> marketplaceUK = new ArrayList<String>();
		marketplaceUK.add("Amazon UK");
		List<String> marketplaceCA = new ArrayList<String>();
		marketplaceCA.add("Amazon CA");
		List<String> marketplaceDE = new ArrayList<String>();
		marketplaceDE.add("Amazon DE");
		List<String> marketplaceIT = new ArrayList<String>();
		marketplaceIT.add("Amazon IT");
		List<String> marketplaceFR = new ArrayList<String>();
		marketplaceFR.add("Amazon FR");
		List<String> marketplaceES = new ArrayList<String>();
		marketplaceES.add("Amazon ES");		
		regionToMarketplaceMap.put("US", marketplaceUS);
		regionToMarketplaceMap.put("UK", marketplaceUK);
		regionToMarketplaceMap.put("CA", marketplaceCA);
		regionToMarketplaceMap.put("DE", marketplaceDE);
		regionToMarketplaceMap.put("IT", marketplaceIT);
		regionToMarketplaceMap.put("FR", marketplaceFR);
		regionToMarketplaceMap.put("ES", marketplaceES);					
		return regionToMarketplaceMap;
	}

}
