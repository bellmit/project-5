package com.kindminds.drs;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.Assert;

public enum Warehouse {
	FBA_US(101,"FBA US",Country.US),
	FBA_UK(102,"FBA UK",Country.UK),
	FBA_CA(103,"FBA CA",Country.CA),
	FBA_EU(104,"FBA EU",Country.DE),
	FBA_DE(105,"FBA DE",Country.DE),
	FBA_FR(106,"FBA FR",Country.FR);
	private int key;
	private String displayName;
	private Country country;

	static private final Map<Integer,Warehouse> keyToWarehouseMap = new HashMap<>();
	static private final Map<String,Warehouse> displayNameToWarehouseMap = new HashMap<>();
	static private final Map<Country,Warehouse> countryToWarehouseMap = new HashMap<>();
	static {
		for(Warehouse w:Warehouse.values()) keyToWarehouseMap.put(w.getKey(),w);
		for(Warehouse w:Warehouse.values()) displayNameToWarehouseMap.put(w.getDisplayName(),w);
		for(Warehouse w:Warehouse.values()) countryToWarehouseMap.put(w.getCountry(),w);
	}
	Warehouse(int keyValue,String displayName,Country country) {
		this.key = keyValue;
		this.displayName = displayName;
		this.country = country;
	}
	public int getKey() {return this.key;}
	public String getDisplayName() {return displayName;}
	public Country getCountry() {return this.country;}
	public static Warehouse fromKey(int value){
		Warehouse w = keyToWarehouseMap.get(value);
		Assert.notNull(w);
		return w;
	}
	
	public static List<Warehouse> getWarehouseList() {
		return Arrays.asList(Warehouse.values());
	}
	public static Warehouse fromDisplayName(String displayName){
		Warehouse warehouse = displayNameToWarehouseMap.get(displayName);
		Assert.notNull(warehouse);
		return warehouse;
	}	
	public static Warehouse fromCountry(Country country){
		Warehouse warehouse = countryToWarehouseMap.get(country);
		Assert.notNull(warehouse);
		return warehouse;		
	}
		
}
