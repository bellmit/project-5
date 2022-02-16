package com.kindminds.drs.web.view.product;

import java.math.BigDecimal;
import java.math.RoundingMode;





public class KeyProductStatsReportLineItem {


	private String skuCodeByDrs;

	private String baseCode;

	private String baseCodeByDrs;
	
	private String supplierKCode;
	
	private String supplierName;
	
	private String skuCode;
	
	private String marketplaceSku;
	
	private String skuName;
	
	private String numericCurrentBaseRetailPrice;
	 private String fbaQtyInBound=null;
	 private String fbaQtyInStock=null;
	 private String fbaQtyTransfer=null;
	 private String qtyOrderedInCurrentSettlementPeriod;
	 private String qtyOrderedInLastSevenDays;
	 private String qtyToReceive;
	 private String qtyInStock;
	 private String currentBaseRetailPrice = "";
	 private String numericQtyOrderedInLastSevenDays;
	 
	 

	
	public void setQtyOrderedInCurrentSettlementPeriod(String value){
		this.qtyOrderedInCurrentSettlementPeriod = value;
	}
	
	public void setQtyOrderedInLastSevenDays(String value){
		this.qtyOrderedInLastSevenDays = value;
	}
	
	public void setQtyToReceive(String value){
		this.qtyToReceive = value;
	}
	
	public void setQtyInStock(String value){
		this.qtyInStock = value;
	}
	
	public void setFbaQtyInBound(String fbaQtyInBound) {
		this.fbaQtyInBound = fbaQtyInBound;
	}
	
	public void setFbaQtyInStock(String fbaQtyInStock) {
		this.fbaQtyInStock = fbaQtyInStock;
	}
	
	public void setFbaQtyTransfer(String fbaQtyTransfer) {
		this.fbaQtyTransfer = fbaQtyTransfer;
	}
	
	public String getMarketplaceSku() {
		return marketplaceSku;
	}
	

	
	
	public String getBaseCode() {
		return this.baseCode;
	}
	
	
	public String getBaseCodeByDrs() {
		return this.baseCodeByDrs;
	}

	
	public String getSkuCode() {
		return this.skuCode;
	}
	
	
	public String getSkuCodeByDrs() {
		return this.skuCodeByDrs;
	}

	
	public String getSkuName() {
		return this.skuName;
	}

	public String getSupplierKCode() {
		return this.supplierKCode;
	}

	
	public String getSupplierName() {
		return this.supplierName;
	}

	
	
	public String getCurrentBaseRetailPrice() {
		return  this.currentBaseRetailPrice;
	
	}

	
	public String getQtyOrderedInCurrentSettlementPeriod() {
		return this.qtyOrderedInCurrentSettlementPeriod.toString();
	}

	
	public String getQtyOrderedInLastSevenDays() {
		return this.qtyOrderedInLastSevenDays.toString();
	}
	
	
	public String getQtyToReceive() {
		return this.qtyToReceive.toString();
	}

	
	public String getQtyInStock() {	
		return this.qtyInStock;
	}

	
	public String getFbaQtyInBound() {
		return this.fbaQtyInBound;
	}

	
	public String getFbaQtyInStock() {
		return this.fbaQtyInStock.toString();
	}

	
	public String getFbaQtyTransfer() {
		return this.fbaQtyTransfer.toString();
	}

	
	public String getNumericCurrentBaseRetailPrice() {
		return this.numericCurrentBaseRetailPrice;
	}

	
	public String getNumericQtyOrderedInLastSevenDays() {
	  
	    return this.numericQtyOrderedInLastSevenDays;
	}
}