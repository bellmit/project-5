package com.kindminds.drs;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.Assert;

public enum Currency{
	TWD(0,1),
	USD(2,2),
	GBP(2,3),
	CAD(2,4),
	EUR(2,5),
	MXN(2,6);
	private int scale;
	private int key;
	static private final Map<Integer,Currency> keyToCurrencyMap = new HashMap<Integer,Currency>();
	static { for(Currency c:Currency.values()) keyToCurrencyMap.put(c.getKey(),c);}

	Currency(int scale, int key){
		this.scale=scale;
		this.key = key;
	}
	public int getScale() {return this.scale;}
	public int getKey() {return this.key;}
	public static Currency fromKey(int key){
		Currency targetCurrency = keyToCurrencyMap.get(key);
		Assert.notNull(targetCurrency);
		return targetCurrency;
	}

	public static Integer getId(String currency) {
		if ("NTD".equals(currency) || "TWD".equals(currency)) {
			return 1;
		} else if ("USD".equals(currency)){
			return 2;
		} else if ("GBP".equals(currency)){
			return 3;
		} else if ("CAD".equals(currency)){
			return 4;
		} else if ("EUR".equals(currency)){
			return 5;
		} else if ("MXN".equals(currency)) {
			return 6;
		}
		return 0;
	}
}