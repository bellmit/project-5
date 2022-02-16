package com.kindminds.drs.v1.model.impl.statement;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.kindminds.drs.api.v1.model.report.Ss2spProfitShareDetailReport.Ss2spProfitShareDetailOfProductSku;

public class Ss2spProfitShareDetailOfProductSkuImplV4 implements Ss2spProfitShareDetailOfProductSku {
	
	private String productSkuCode;
	private String productSkuName;
	private Map<String,BigDecimal> itemNameToAmountMap;
	
	public Ss2spProfitShareDetailOfProductSkuImplV4(
			String productSkuCode,
			String productSkuName,
			Map<String,BigDecimal> itemNameToAmountMap){
		this.productSkuCode = productSkuCode;
		this.productSkuName = productSkuName;
		this.itemNameToAmountMap = itemNameToAmountMap;
	}
	
	public Map<String,BigDecimal> getRawItemNameToAmountMap(){
		return this.itemNameToAmountMap;
	}

	@Override
	public String toString() {
		return "Ss2spProfitShareDetailOfProductSkuImplV4 [getRawItemNameToAmountMap()=" + getRawItemNameToAmountMap()
				+ ", getProductSkuCode()=" + getProductSkuCode() + ", getProductSkuName()=" + getProductSkuName()
				+ ", getSubtotal()=" + getSubtotal() + ", getItemNameToAmountMap()=" + getItemNameToAmountMap() + "]";
	}

	@Override
	public String getProductSkuCode() {
		return this.productSkuCode;
	}

	@Override
	public String getProductSkuName() {
		return this.productSkuName;
	}
	
	@Override
	public String getSubtotal() {
		BigDecimal subtotal = BigDecimal.ZERO;
		List<BigDecimal> amountList = new ArrayList<BigDecimal>(this.itemNameToAmountMap.values());
		for(BigDecimal amount:amountList){
			subtotal = subtotal.add(amount);
		}
		return subtotal.toPlainString();
	}
	
	@Override
	public Map<String,String> getItemNameToAmountMap() {
		Map<String,String> mapToReturn = new TreeMap<String,String>();
		for(String itemName:this.itemNameToAmountMap.keySet()){
			mapToReturn.put(itemName, this.itemNameToAmountMap.get(itemName).toPlainString());
		}
		return mapToReturn;
	}

}
