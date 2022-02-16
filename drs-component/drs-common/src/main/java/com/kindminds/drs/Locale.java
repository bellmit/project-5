package com.kindminds.drs;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.Assert;

public enum Locale {
	ZH_TW(1,"zh_TW","繁體中文 (台灣)"),
	EN_US(2,"en_US","English (US)"),
	DE_DE(3,"de_DE","Deutsch (Deutschland)"),
	FR_FR(4,"fr_FR","Français (France)"),
	IT_IT(5,"it_IT","Italiano (Italia)"),
	ES_ES(6,"es_ES","Español (España)");
	private int key;
	private String code;
	private String fullName;
	private static final Map<Integer,Locale> keyToLocaleMap = new HashMap<Integer,Locale>();
	private static final Map<String,Locale> codeToLocaleMap = new HashMap<String,Locale>();
	static{
		for(Locale l:Locale.values()){
			keyToLocaleMap.put(l.getKey(), l);
			codeToLocaleMap.put(l.getCode(), l);
		}
	}
	Locale(int key,String code,String fullName){
		this.key = key;
		this.code = code;
		this.fullName = fullName;
	}
	public int getKey(){ return this.key; }
	public String getCode(){ return this.code; }
	public String getFullName(){ return this.fullName; }
	public static Locale fromKey(int key){
		Locale l = keyToLocaleMap.get(key);
		Assert.notNull(l);
		return l;
	}
	public static Locale fromCode(String code){
		Locale l = codeToLocaleMap.get(code);
		Assert.notNull(l);
		return l;
	}
}
