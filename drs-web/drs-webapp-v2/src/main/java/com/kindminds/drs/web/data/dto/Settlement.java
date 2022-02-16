package com.kindminds.drs.web.data.dto;

import com.kindminds.drs.Currency;

public class Settlement {

	public String issuer;
	public String receiver;
	public String periodStartUTC;
	public String periodEndUTC;
	public Currency currency;
	
	public String getIssuer(){
		return this.issuer;		
	}
	
	public void setIssuer(String issuer){
		this.issuer = issuer;		
	}
		
	public String getReceiver(){
		return this.receiver;		
	}
	
	public void setReceiver(String receiver){
		this.receiver = receiver;				
	}
		
	public String getPeriodStartUTC(){
		return this.periodStartUTC;		
	} 
	
	public void setPeriodStartUTC(String periodStartUTC){
		this.periodStartUTC = periodStartUTC;		
	}
		
	public String getPeriodEndUTC(){
		return this.periodEndUTC;				
	}
	
	public void setPeriodEndUTC(String periodEndUTC){
		this.periodEndUTC = periodEndUTC;		
	}
		
	public Currency getCurrency(){
		return this.currency;
	}

	public void setCurrency(Currency currency){
		this.currency = currency;
	}
		
}
