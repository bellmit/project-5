package com.kindminds.drs;

import java.math.BigDecimal;
import java.util.*;
import java.util.Locale;

import org.springframework.util.Assert;

public enum Country {
	CORE(0,Currency.TWD,Locale.TAIWAN,                        null,new BigDecimal("0.05")),
	TW(1,Currency.TWD,Locale.TAIWAN,                          null,new BigDecimal("0.05")),
	US(2,Currency.USD,Locale.US,                              null,                  null),
	UK(3,Currency.GBP,Locale.UK,            new BigDecimal("0.20"),                  null),
	CA(4,Currency.CAD,Locale.CANADA,                          null,                  null),
	DE(5,Currency.EUR,Locale.GERMANY,       new BigDecimal("0.19"),                  null),
	FR(6,Currency.EUR,Locale.FRANCE,        new BigDecimal("0.20"),                  null),
	IT(7,Currency.EUR,Locale.ITALY,         new BigDecimal("0.22"),                  null),
	ES(8,Currency.EUR,new Locale("es","ES"),new BigDecimal("0.21"),                  null),
	EU(9,Currency.EUR,null, null, null),
	NA(10,Currency.USD,null, null, null),
	MX(11,Currency.MXN,Locale.US, null, null);
	private int key;
	private Currency currency;
	private Locale locale;
	private BigDecimal vatRate;
	private BigDecimal salesTaxRate;
	private String region;

	static private final Map<Integer,Country> keyToCountryMap = new HashMap<>();
	static { for(Country country:Country.values()) keyToCountryMap.put(country.getKey(),country);}
	Country(int keyValue,Currency cur,Locale locale,BigDecimal vatRate,BigDecimal salesTaxRate) {
		this.key=keyValue;
		this.currency=cur;
		this.locale=locale;
		this.vatRate=vatRate;
		this.salesTaxRate=salesTaxRate;
	}
	public int getKey() {return this.key;}
	public Currency getCurrency() {return this.currency;}
	public Locale getLocale(){ return this.locale; }
	public BigDecimal getVatRate() {return this.vatRate;}
	public BigDecimal getSalesTaxRate() {return salesTaxRate;}
	public static Country fromKey(int key){
		Country country = keyToCountryMap.get(key);
		Assert.notNull(country);
		return country;
	}

	public static List<Country> getAmazonCountryList() {
		List<Country> countries = new ArrayList<>();
		countries.add(Country.US);
		countries.add(Country.UK);
		countries.add(Country.CA);
		countries.add(Country.DE);
		countries.add(Country.FR);
		countries.add(Country.IT);
		countries.add(Country.ES);
		countries.add(Country.MX);
		return countries;
	}

	public static List<String> getIvsDestinationCountries() {
		List<String> countries = new ArrayList<>();
		countries.add(Country.US.name());
		countries.add(Country.CA.name());
		countries.add(Country.EU.name());
		countries.add(Country.UK.name());
		return countries;
	}
}
