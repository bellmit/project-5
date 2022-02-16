package com.kindminds.drs.v1.model.impl.statement;

import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;

import com.kindminds.drs.api.v1.model.report.Ss2spProfitShareDetailReport.Ss2spProfitShareDetail;

public class Ss2spProfitShareDetailImplV1 implements Ss2spProfitShareDetail {

	private String sku = null;
	private String skuName = null;
	private Map<String,BigDecimal> itemNameToAmountMap;

	public Ss2spProfitShareDetailImplV1(String sku,String skuName,Map<String,BigDecimal> itemNameToAmountMap) {
		this.sku = sku;
		this.skuName = skuName;
		this.itemNameToAmountMap = itemNameToAmountMap;
	}
	
	public Map<String,BigDecimal> getRawItemNameToAmountMap(){
		return this.itemNameToAmountMap;
	}
	
	public BigDecimal getNumericSubtotal() {
		BigDecimal subtotal = BigDecimal.ZERO;
		for(String key:this.itemNameToAmountMap.keySet()){
			subtotal = subtotal.add(this.itemNameToAmountMap.get(key));
		}
		return subtotal;
	}
	
	@Override
	public String getSku() {
		return this.sku;
	}
	
	@Override
	public String getSkuName() {
		return this.skuName;
	}
	
	@Override
	public String getSubtotal() {
		return this.getNumericSubtotal().toPlainString();
	}

	@Override
	public Map<String,String> getItemNameToAmountMap() {
		Map<String,String> mapToReturn = new TreeMap<String,String>();
		for(String key:this.itemNameToAmountMap.keySet()){
			String amountStr = this.itemNameToAmountMap.get(key).toPlainString();
			mapToReturn.put(key,amountStr);
		}
		return mapToReturn;
	}

}
