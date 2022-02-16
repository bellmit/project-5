package com.kindminds.drs.persist.v1.model.mapping.product;

import java.math.BigDecimal;
import java.math.RoundingMode;






import com.kindminds.drs.api.v1.model.report.KeyProductStatsReport.KeyProductStatsReportLineItem;


public class KeyProductStatsReportLineItemImpl implements
		 KeyProductStatsReportLineItem {

	public  KeyProductStatsReportLineItemImpl(){

	}

	public KeyProductStatsReportLineItemImpl(String skuCodeByDrs, String baseCode, String supplierKCode,
											 String supplierName, String baseCodeByDrs, String skuCode,
											 String marketplaceSku, String skuName, BigDecimal numericCurrentBaseRetailPrice) {
		this.skuCodeByDrs = skuCodeByDrs;
		this.baseCode = baseCode;
		this.supplierKCode = supplierKCode;
		this.supplierName = supplierName;
		this.baseCodeByDrs = baseCodeByDrs;
		this.skuCode = skuCode;
		this.marketplaceSku = marketplaceSku;
		this.skuName = skuName;
		this.numericCurrentBaseRetailPrice = numericCurrentBaseRetailPrice;
	}
// Unrecognized field "currentBaseRetailPrice" (
     //       class com.kindminds.drs.web.dto.product.KeyProductStatsReportLineItemImpl), not marked as ignorable (14 known properties: "fbaQtyInStock", "skuCodeByDrs", "qtyOrderedInCurrentSettlementPeriod", "skuName", "numericCurrentBaseRetailPrice", "skuCode", "baseCodeByDrs", "marketplaceSku", "qtyInStock", "fbaQtyTransfer", "baseCode", "fbaQtyInBound", "qtyOrderedInLastSevenDays", "qtyToReceive"])
    //at [Source: (StringReader); line: 1, column: 602] (through reference chai

	//@Id //@Column(name="sku_code_by_drs")
	private String skuCodeByDrs;
	//@Column(name="base_code_to_show")
	private String baseCode;

	//@Column(name="k_code")
	private String supplierKCode;
	//@Column(name="short_name_local")
	private String supplierName;

	//@Column(name="base_code_by_drs")
	private String baseCodeByDrs;
	//@Column(name="sku_code_by_supplier")
	private String skuCode;
	//@Column(name="marketplace_sku")
	private String marketplaceSku;
	//@Column(name="sku_name_by_supplier")
	private String skuName;
	//@Column(name="current_base_retail_price")
	private BigDecimal numericCurrentBaseRetailPrice;

 	private Integer fbaQtyInBound=null;
 	private Integer fbaQtyInStock=null;
 	private Integer fbaQtyTransfer=null;
 	private Integer qtyOrderedInCurrentSettlementPeriod=0;
 	private Integer qtyOrderedInLastSevenDays=0;
 	private Integer qtyToReceive=0;
 	private Integer qtyInStockAtDrsSettlementEndPlusQtyReceivedInCurrentSettlement=0;
    private String currentBaseRetailPrice ="";
    private BigDecimal numericQtyOrderedInLastSevenDays;

	@Override
	public String toString() {
		return "KeyProductStatsReportLineItemImpl [getBaseCode()=" + getBaseCode() + ", getBaseCodeByDrs()="
				+ getBaseCodeByDrs() + ", getSkuCode()=" + getSkuCode() + ", getSkuCodeByDrs()=" + getSkuCodeByDrs()
				+ ", getSkuName()=" + getSkuName() + ", getCurrentBaseRetailPrice()=" + getCurrentBaseRetailPrice()
				+ ", getQtyOrderedInCurrentSettlementPeriod()=" + getQtyOrderedInCurrentSettlementPeriod()
				+ ", getQtyOrderedInLastSevenDays()=" + getQtyOrderedInLastSevenDays() + ", getQtyToReceive()="
				+ getQtyToReceive() + ", getQtyInStock()=" + getQtyInStock() + ", getFbaQtyInBound()="
				+ getFbaQtyInBound() + ", getFbaQtyInStock()=" + getFbaQtyInStock() + ", getFbaQtyTransfer()="
				+ getFbaQtyTransfer() + ", getNumericCurrentBaseRetailPrice()=" + getNumericCurrentBaseRetailPrice()
				+ ", getNumericQtyOrderedInLastSevenDays()=" + getNumericQtyOrderedInLastSevenDays() + "]";
	}
	
	public void setQtyOrderedInCurrentSettlementPeriod(Integer value){
		this.qtyOrderedInCurrentSettlementPeriod = value;
	}
	
	public void setQtyOrderedInLastSevenDays(Integer value){
		this.qtyOrderedInLastSevenDays = value;
	}
	
	public void setQtyToReceive(Integer value){
		this.qtyToReceive = value;
	}
	
	public void setQtyInStock(Integer value){
		this.qtyInStockAtDrsSettlementEndPlusQtyReceivedInCurrentSettlement = value;
	}
	
	public void setFbaQtyInBound(Integer fbaQtyInBound) {
		this.fbaQtyInBound = fbaQtyInBound;
	}
	
	public void setFbaQtyInStock(Integer fbaQtyInStock) {
		this.fbaQtyInStock = fbaQtyInStock;
	}
	
	public void setFbaQtyTransfer(Integer fbaQtyTransfer) {
		this.fbaQtyTransfer = fbaQtyTransfer;
	}

	@Override
	public String getMarketplaceSku() {
		return marketplaceSku;
	}

	/*
	@Override
	public String getSupplierKCode() {
		return this.supplierKCode;
	}

	@Override
	public String getSupplierName() {
		return this.supplierName;
	}
	*/
	
	@Override
	public String getBaseCode() {
		return this.baseCode;
	}
	
	@Override
	public String getBaseCodeByDrs() {
		return this.baseCodeByDrs;
	}

	@Override
	public String getSupplierKCode() {
		return this.supplierKCode;
	}
	@Override
	public String getSupplierName() {
		return this.supplierName;
	}

	@Override
	public String getSkuCode() {
		return this.skuCode;
	}
	
	@Override
	public String getSkuCodeByDrs() {
		return this.skuCodeByDrs;
	}

	@Override
	public String getSkuName() {
		return this.skuName;
	}

	@Override
	public String getCurrentBaseRetailPrice() {
        this.currentBaseRetailPrice =this.numericCurrentBaseRetailPrice.setScale(2, RoundingMode.HALF_UP).toPlainString();
		return  currentBaseRetailPrice;
	}

	@Override
	public String getQtyOrderedInCurrentSettlementPeriod() {
		return this.qtyOrderedInCurrentSettlementPeriod.toString();
	}

	@Override
	public String getQtyOrderedInLastSevenDays() {
		return this.qtyOrderedInLastSevenDays.toString();
	}
	
	@Override
	public String getQtyToReceive() {
		return this.qtyToReceive.toString();
	}

	@Override
	public String getQtyInStock() {	
		Integer qtyInStock = this.qtyInStockAtDrsSettlementEndPlusQtyReceivedInCurrentSettlement-this.qtyOrderedInCurrentSettlementPeriod;
		return qtyInStock<0?"0":qtyInStock.toString(); 
	}

	@Override
	public String getFbaQtyInBound() {
		if(this.fbaQtyInBound==null) return "0";
		return this.fbaQtyInBound.toString();
	}

	@Override
	public String getFbaQtyInStock() {
		if(this.fbaQtyInStock==null) return "0";
		return this.fbaQtyInStock.toString();
	}

	@Override
	public String getFbaQtyTransfer() {
		if(this.fbaQtyTransfer==null) return "0";
		return this.fbaQtyTransfer.toString();
	}

	@Override
	public BigDecimal getNumericCurrentBaseRetailPrice() {
		return this.numericCurrentBaseRetailPrice;
	}

	@Override
	public BigDecimal getNumericQtyOrderedInLastSevenDays() {
	    this.numericQtyOrderedInLastSevenDays = new BigDecimal(this.qtyOrderedInLastSevenDays);
	    return this.numericQtyOrderedInLastSevenDays;
	}
	
}
