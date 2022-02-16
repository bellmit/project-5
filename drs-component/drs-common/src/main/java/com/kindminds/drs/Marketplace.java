package com.kindminds.drs;

import java.util.*;

import org.springframework.util.Assert;

public enum Marketplace {
	AMAZON_COM(  1,  "Amazon.com",Country.US,Country.US,true,false),
	TRUETOSOURCE(2,"TrueToSource",Country.US,Country.US,false,true),
	EBAY_COM(    3,    "eBay.com",Country.US,Country.US,false,false),
	AMAZON_CO_UK(4,"Amazon.co.uk",Country.UK,Country.UK,true,false),
	AMAZON_CA(   5,   "Amazon.ca",Country.CA,Country.CA,true,false),
	AMAZON_DE(   6,   "Amazon.de",Country.DE,Country.EU,true,false),
	AMAZON_FR(   7,   "Amazon.fr",Country.FR,Country.EU,true,false),
	AMAZON_IT(   8,   "Amazon.it",Country.IT,Country.EU,true,false),
	AMAZON_ES(   9,   "Amazon.es",Country.ES,Country.EU,true,false),
	EBAY_DE(    10,     "eBay.de",Country.DE,Country.EU,false,false),
	EBAY_IT(    11,     "eBay.it",Country.IT,Country.EU,false,false),
	EBAY_UK(    12,  "eBay.co.uk",Country.UK,Country.UK,false,false),
	EBAY_CA(    13,  	"eBay.ca",Country.CA,Country.CA,false,false),
	AMAZON_COM_MX(16, "Amazon.com.mx", Country.MX, Country.US, true, false);
	private int key;
	private String name;
	private Country country;
	private Country unsDestinationCountry;
	private boolean isAmazonMarketplace;
	private boolean isShopifyMarketplace;
	static private final Map<Integer,Marketplace> keyToMarketplaceMap = new HashMap<>();
	static private final Map<String,Marketplace> nameToMarketplaceMap = new HashMap<>();
	static private final Map<Integer,String> keyToCountryMap = new HashMap<>();
	static { for(Marketplace m:Marketplace.values()) keyToMarketplaceMap.put(m.getKey(),m);}
	static { for(Marketplace m:Marketplace.values()) nameToMarketplaceMap.put(m.getName(),m);}
	static { for(Marketplace m:Marketplace.values()) keyToCountryMap.put(m.getKey(),m.country.toString());}

	static private final Map<String,Integer> countryToKeyMap = initializeCountryToKeyMap();
	private static Map<String,Integer> initializeCountryToKeyMap() {
		Map<String,Integer> countryToMarketplaceMap = new HashMap<>();
		countryToMarketplaceMap.put(Country.US.toString(),Marketplace.AMAZON_COM.getKey());
		countryToMarketplaceMap.put(Country.UK.toString(),Marketplace.AMAZON_CO_UK.getKey());
		countryToMarketplaceMap.put(Country.CA.toString(),Marketplace.AMAZON_CA.getKey());
		countryToMarketplaceMap.put(Country.DE.toString(),Marketplace.AMAZON_DE.getKey());
		countryToMarketplaceMap.put(Country.FR.toString(),Marketplace.AMAZON_FR.getKey());
		countryToMarketplaceMap.put(Country.IT.toString(),Marketplace.AMAZON_IT.getKey());
		countryToMarketplaceMap.put(Country.ES.toString(),Marketplace.AMAZON_ES.getKey());
		countryToMarketplaceMap.put(Country.MX.toString(),Marketplace.AMAZON_COM_MX.getKey());
		return countryToMarketplaceMap;
	}

	Marketplace(
			int keyValue,
			String name,
			Country country,
			Country unsDestinationCountry,
			boolean isAmazonMarketplace,
			boolean isShopifyMarketplace) {
		this.key = keyValue;
		this.name = name;
		this.country = country;
		this.unsDestinationCountry = unsDestinationCountry;
		this.isAmazonMarketplace = isAmazonMarketplace;
		this.isShopifyMarketplace = isShopifyMarketplace;
	}
	public int getKey() {return this.key;}
	public String getName() {return this.name;}
	public Country getCountry() {return this.country;}
	public Country getUnsDestinationCountry() {return unsDestinationCountry;}
	public Currency getCurrency() {return this.country.getCurrency();}
	public java.util.Locale getLocale(){ return this.country.getLocale(); }
	public boolean isAmazonMarketplace() { return this.isAmazonMarketplace; }
	public boolean isShopifyMarketplace() { return this.isShopifyMarketplace; }
	public static Marketplace fromName(String value){
		Marketplace m = nameToMarketplaceMap.get(value);
		Assert.notNull(m);
		return m;
	}
	public static Marketplace fromKey(int value){
		Marketplace m = keyToMarketplaceMap.get(value);
		Assert.notNull(m);
		return m;
	}
	public static Map<String, Integer> getCountryToMarketplaceIdMap() {
		return countryToKeyMap;
	}
	public static Integer getIdFromCountry(String countryAbbr) {
		Integer id = countryToKeyMap.get(countryAbbr);
		Assert.notNull(id);
		return id;
	}
	public static String getMarketplaceNameFromCountry(String countryAbbr) {
		return fromKey(getIdFromCountry(countryAbbr)).name;
	}
	public static String getCountryFromId(Integer marketplaceId) {
		String countryAbbr = keyToCountryMap.get(marketplaceId);
		Assert.notNull(countryAbbr);
		return countryAbbr;
	}
	public static List<Marketplace> getAmazonMarketplaces(){
		List<Marketplace> marketplaces = new ArrayList<>();
		for(Marketplace marketplace:Marketplace.values()){
			if(marketplace.isAmazonMarketplace()) marketplaces.add(marketplace);
		}
		return marketplaces;
	}

	public static List<Marketplace> getMarketplaceList() {
		return Arrays.asList(Marketplace.values());
	}


}