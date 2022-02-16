package com.kindminds.drs.persist.v1.model.mapping.report;

import com.kindminds.drs.api.v1.model.report.Satisfaction;


public class SatisfactionDto implements Satisfaction {
	
	//@Id //@Column(name="sku_id")
	private int skuId;
	//@Column(name="code_by_drs")
	private String skuCodeByDrs;
	//@Column(name="product_name")
	private String productName;
	//@Column(name="supplier_company_id")
	private int supplierId;
	//@Column(name="quantity_1")
	private int quantityByOne;
	//@Column(name="return_quantity_1")
	private int returnQuantityByOne;
	//@Column(name="quantity_2")
	private int quantityByTwo;
	//@Column(name="return_quantity_2")
	private int returnQuantityByTwo;
	//@Column(name="quantity_6")
	private int quantityBySix;
	//@Column(name="return_quantity_6")
	private int returnQuantityBySix;

	public SatisfactionDto() {
	}

	public SatisfactionDto(int skuId, String skuCodeByDrs, String productName, int supplierId, int quantityByOne, int returnQuantityByOne, int quantityByTwo, int returnQuantityByTwo, int quantityBySix, int returnQuantityBySix) {
		this.skuId = skuId;
		this.skuCodeByDrs = skuCodeByDrs;
		this.productName = productName;
		this.supplierId = supplierId;
		this.quantityByOne = quantityByOne;
		this.returnQuantityByOne = returnQuantityByOne;
		this.quantityByTwo = quantityByTwo;
		this.returnQuantityByTwo = returnQuantityByTwo;
		this.quantityBySix = quantityBySix;
		this.returnQuantityBySix = returnQuantityBySix;
	}

	@Override
	public int getSkuId() {
		return skuId;
	}
	@Override
	public void setSkuId(int skuId) {
		this.skuId = skuId;
	}
	@Override
	public String getSkuCodeByDrs() {
		return skuCodeByDrs;
	}
	@Override
	public void setSkuCodeByDrs(String skuCodeByDrs) {
		this.skuCodeByDrs = skuCodeByDrs;
	}
	@Override
	public String getProductName() {
		return productName;
	}
	@Override
	public void setProductName(String productName) {
		this.productName = productName;
	}
	@Override
	public int getSupplierId() {
		return supplierId;
	}
	@Override
	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}
	@Override
	public int getQuantityByOne() {
		return quantityByOne;
	}
	@Override
	public void setQuantityByOne(int quantityByOne) {
		this.quantityByOne = quantityByOne;
	}
	@Override
	public int getReturnQuantityByOne() {
		return returnQuantityByOne;
	}
	@Override
	public void setReturnQuantityByOne(int returnQuantityByOne) {
		this.returnQuantityByOne = returnQuantityByOne;
	}
	@Override
	public int getQuantityByTwo() {
		return quantityByTwo;
	}
	@Override
	public void setQuantityByTwo(int quantityByTwo) {
		this.quantityByTwo = quantityByTwo;
	}
	@Override
	public int getReturnQuantityByTwo() {
		return returnQuantityByTwo;
	}
	@Override
	public void setReturnQuantityByTwo(int returnQuantityByTwo) {
		this.returnQuantityByTwo = returnQuantityByTwo;
	}
	@Override
	public int getQuantityBySix() {
		return quantityBySix;
	}
	@Override
	public void setQuantityBySix(int quantityBySix) {
		this.quantityBySix = quantityBySix;
	}
	@Override
	public int getReturnQuantityBySix() {
		return returnQuantityBySix;
	}
	@Override
	public void setReturnQuantityBySix(int returnQuantityBySix) {
		this.returnQuantityBySix = returnQuantityBySix;
	}
}
