package com.kindminds.drs;

import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

public enum AccountManager {
	ANNIE("Annie","annie.hsieh@tw.drs.network"),
	ARIEL("Ariel","ariel.hsieh@drs.network"),
	CLEO("Cleo","cleo.chen@tw.drs.network"),
	JESSIE("Jessie","jean-charles.cartier@drs.network"),
	VINCENT("Vincent","vincent.wang@tw.drs.network"),
	WEI("Wei","wei.wu@tw.drs.network");
	private String name;
	private String email;
	static private final Map<String, String> nameToEmailMap = new HashMap<>();
	static { for(AccountManager c: AccountManager.values()) nameToEmailMap.put(c.getName(),c.getEmail());}

	AccountManager(String name, String email){
		this.name = name;
		this.email = email;
	}

	public String getName() {
		return this.name;
	}

	public String getEmail() {
		return this.email;
	}

	public static String getEmail(String name){
		String email = nameToEmailMap.get(name);
		Assert.notNull(email);
		return email;
	}
}