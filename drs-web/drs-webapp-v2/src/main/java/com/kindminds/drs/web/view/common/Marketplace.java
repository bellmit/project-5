package com.kindminds.drs.web.view.common;

import java.io.Serializable;

import com.kindminds.drs.Country;

public class Marketplace implements Serializable{

	private static final long serialVersionUID = -8714770432161303966L;
	private int key;
	private String name;
	private Country country;
	private Country unsDestinationCountry;
	private boolean isAmazonMarketplace;
	private boolean isShopifyMarketplace;
	
	public Marketplace(){
		
	}
				
	@Override
	public String toString() {
		return "Marketplace [getKey()=" + getKey() + ", getName()=" + getName() + ", getCountry()=" + getCountry()
				+ ", getUnsDestinationCountry()=" + getUnsDestinationCountry() + ", isAmazonMarketplace()="
				+ isAmazonMarketplace() + ", isShopifyMarketplace()=" + isShopifyMarketplace() + "]";
	}

	public int getKey() {
		return this.key;
	}
		
	public String getName() {
		return this.name;
	}
	
	public Country getCountry() {
		return this.country;
	}
	
	public Country getUnsDestinationCountry() {
		return unsDestinationCountry;
	}
	
	public boolean isAmazonMarketplace() { 
		return this.isAmazonMarketplace; 
	}
	
	public boolean isShopifyMarketplace() { 
		return this.isShopifyMarketplace; 
	}
		
}