package com.kindminds.drs.web.view.settlement;

import java.io.Serializable;
import java.math.BigDecimal;

public class CouponRedemption implements Serializable{
			
	private static final long serialVersionUID = 4111268669222166719L;
	private int id;
	private String transactionTime;
	private Integer supplierId;
	private String supplierName;
	private String supplierKcode;
	private String periodStartUtc;
	private String periodEndUtc;
	private String coupon;
	private BigDecimal amount;
	private Integer currencyId;
	private String currencyName;
	private String marketplaceName;
	private String msdcKcode;
	private String reason;
	
	public CouponRedemption(){
		
	}
		
	public int getId(){
		return this.id;
	};
	
	public void setId(int id){
		this.id = id;
	}
		
	public String getTransactionTime(){
		return this.transactionTime;
	};
	
	public void setTransactionTime(String transactionTime){
		this.transactionTime = transactionTime;
	};
		
	public Integer getSupplierId(){
		return this.supplierId;
	};
	
	public void setSupplierId(Integer supplierId){
		this.supplierId = supplierId;
	}; 
		
	public String getSupplierName(){
		return this.supplierName;
	};
		
	public void setSupplierName(String supplierName){
		this.supplierName = supplierName;
	};
		
	public String getSupplierKcode(){
		return this.supplierKcode;
	};
		
	public void setSupplierKcode(String supplierKcode){
		this.supplierKcode = supplierKcode;
	};
		
	public String getPeriodStartUtc(){
		return this.periodStartUtc;
	};
	
	public void setPeriodStartUtc(String periodStartUtc){
		this.periodStartUtc = periodStartUtc;
	}; 
		
	public String getPeriodEndUtc(){
		return this.periodEndUtc;
	};
	
	public void setPeriodEndUtc(String periodEndUtc){
		this.periodEndUtc = periodEndUtc;
	};
		
	public String getCoupon(){
		return this.coupon;
	};
	
	public void setCoupon(String coupon){
		this.coupon = coupon;
	};
		
	public BigDecimal getAmount(){
		return this.amount;
	};
	
	public void setAmount(BigDecimal amount){
		this.amount = amount;
	};
		
	public Integer getCurrencyId(){
		return this.currencyId;
	};
	
	public void getCurrencyId(Integer currencyId){
		this.currencyId = currencyId;
	};
		
	public String getCurrencyName(){
		return this.currencyName;
	};
	
	public void setCurrencyName(String currencyName){
		this.currencyName = currencyName;
	};
		
	public String getMarketplaceName(){
		return this.marketplaceName;		
	}
	
	public void setMarketplaceName(String marketplaceName){
		this.marketplaceName = marketplaceName;		
	}
		
	public String getMsdcKcode(){
		return this.msdcKcode;
	};
	
	public void setMsdcKcode(String msdcKcode){
		this.msdcKcode = msdcKcode;
	};
		
	public String getReason(){
		return this.reason;
	};

	public void setReason(String reason){
		this.reason = reason;
	};
		
}