package com.kindminds.drs.impl;

import com.kindminds.drs.api.v1.model.logistics.ShipmentUns.ShipmentUnsLineItem;
import com.kindminds.drs.util.NumberHelper;
import com.kindminds.drs.util.TestUtil;

import java.math.BigDecimal;

public class ShipmentUnsLineItemImplForTest implements ShipmentUnsLineItem {

	private int lineSeq;
	private Integer boxNum;
	private Integer mixedBoxLineSeq;
	private Integer cartonNumberStart = 0;
	private Integer cartonNumberEnd = 0;
	private String sourceShipmentName;
	private String sku;
	private String skuNameBySupplier;
	private String perCartonGrossWeight;
	private Integer perCartonUnits;
	private Integer cartonCounts;
	private String unitAmount;
	private String unitCifAmount;
	private String cartonDimensionCm1;
	private String cartonDimensionCm2;
	private String cartonDimensionCm3;
	
	public ShipmentUnsLineItemImplForTest(int lineSeq,
			Integer boxNum,
			Integer mixedBoxLineSeq,
			String sourceShipmentName,
			String sku,
			String skuNameBySupplier,
			String perCartonGrossWeight,
			Integer perCartonUnits,
			Integer cartonCounts,
			String unitAmount,
			String unitCifAmount,
			String cartonDimension1,
			String cartonDimension2,
			String cartonDimension3) {
		this.lineSeq = lineSeq;
		this.boxNum = boxNum;
		this.mixedBoxLineSeq = mixedBoxLineSeq;
		this.sourceShipmentName = sourceShipmentName;
		this.sku = sku;
		this.skuNameBySupplier = skuNameBySupplier;
		this.perCartonGrossWeight = perCartonGrossWeight;
		this.perCartonUnits = perCartonUnits;
		this.cartonCounts = cartonCounts;
		this.unitAmount = unitAmount;
		this.unitCifAmount = unitCifAmount;
		this.cartonDimensionCm1 = cartonDimension1;
		this.cartonDimensionCm2 = cartonDimension2;
		this.cartonDimensionCm3 = cartonDimension3;
	}
	
	public void setSku(String sku) {
		this.sku = sku;
	}
	
	@Override
	public int getLineSeq() {
		return this.lineSeq;
	}
	
	@Override
	public String getSourceInventoryShipmentName() {
		return sourceShipmentName;
	}
	
	@Override
	public String getSkuCode() {
		return this.sku;
	}
	
	@Override
	public String getNameBySupplier() {
		return this.skuNameBySupplier;
	}
	
	@Override
	public String getPerCartonGrossWeightKg() {
		return NumberHelper.toGeneralCommaSeparatedString(new BigDecimal(this.perCartonGrossWeight),2);
	}
	
	@Override
	public String getPerCartonUnits() {
		return this.perCartonUnits.toString();
	}
	
	@Override
	public String getCartonCounts() {
		return this.cartonCounts.toString();
	}
	
	@Override
	public String getQuantity() {
		Integer qty =this.perCartonUnits*this.cartonCounts; 
		return NumberHelper.toGeneralCommaSeparatedString(qty,0);
	}
	
	@Override
	public String getUnitAmount() {
		return this.unitAmount;
	}
	
	@Override
	public String getUnitCifAmount() {
		return this.unitCifAmount;
	}
	
	@Override
	public String getSubtotal() {
		BigDecimal qty = new BigDecimal(this.getQuantity());
		BigDecimal unitAmount = new BigDecimal(this.unitAmount);
		return NumberHelper.toGeneralCommaSeparatedString(qty.multiply(unitAmount),2);
	}
	
	@Override
	public String getCifSubtotal() {
		BigDecimal qty = new BigDecimal(this.getQuantity());
		BigDecimal unitCifAmount = new BigDecimal(this.unitCifAmount);
		return NumberHelper.toGeneralCommaSeparatedString(qty.multiply(unitCifAmount),2);
	}
	
	@Override
	public String getCartonDimensionCm1() {
		return NumberHelper.toGeneralCommaSeparatedString(new BigDecimal(this.cartonDimensionCm1),1);
	}
	@Override
	public String getCartonDimensionCm2() {
		return NumberHelper.toGeneralCommaSeparatedString(new BigDecimal(this.cartonDimensionCm2),1);
	}
	@Override
	public String getCartonDimensionCm3() {
		return NumberHelper.toGeneralCommaSeparatedString(new BigDecimal(this.cartonDimensionCm3),1);
	}
	
	@Override
	public boolean equals(Object obj){
		if( obj instanceof ShipmentUnsLineItem){
			ShipmentUnsLineItem item = (ShipmentUnsLineItem)obj;
			boolean sourceShipmentNameEaual=false;
			if(this.getSourceInventoryShipmentName()==null&&item.getSourceInventoryShipmentName()==null) sourceShipmentNameEaual=true;
			else if(this.getSourceInventoryShipmentName().equals(item.getSourceInventoryShipmentName())) sourceShipmentNameEaual=true;
			return this.getLineSeq()==item.getLineSeq()
					&& sourceShipmentNameEaual
					&& this.getSkuCode().equals(item.getSkuCode())
					&& this.getNameBySupplier().equals(item.getNameBySupplier())
					&& this.getPerCartonGrossWeightKg().equals(item.getPerCartonGrossWeightKg())
					&& this.getPerCartonUnits().equals(item.getPerCartonUnits())
					&& this.getCartonCounts().equals(item.getCartonCounts())
					&& this.getQuantity().equals(item.getQuantity())
					&& this.getUnitAmount().equals(item.getUnitAmount())
					&& TestUtil.nullableEquals(this.getUnitCifAmount(),item.getUnitCifAmount())
					&& this.getSubtotal().equals(item.getSubtotal())
					&& this.getCartonDimensionCm1().equals(item.getCartonDimensionCm1())
					&& this.getCartonDimensionCm2().equals(item.getCartonDimensionCm2())
					&& this.getCartonDimensionCm3().equals(item.getCartonDimensionCm3());
		}
		return false;
	}

	@Override
	public String toString() {
		return "ShipmentUnsLineItemImplForTest [getLineSeq()=" + getLineSeq() + ", getSourceInventoryShipmentName()="
				+ getSourceInventoryShipmentName() + ", getSkuCode()=" + getSkuCode() + ", getNameBySupplier()="
				+ getNameBySupplier() + ", getPerCartonGrossWeightKg()=" + getPerCartonGrossWeightKg()
				+ ", getPerCartonUnits()=" + getPerCartonUnits() + ", getCartonCounts()=" + getCartonCounts()
				+ ", getQuantity()=" + getQuantity() + ", getUnitAmount()=" + getUnitAmount() + ", getUnitCifAmount()="
				+ getUnitCifAmount() + ", getSubtotal()=" + getSubtotal() + ", getCartonDimensionCm1()="
				+ getCartonDimensionCm1() + ", getCartonDimensionCm2()=" + getCartonDimensionCm2()
				+ ", getCartonDimensionCm3()=" + getCartonDimensionCm3() + "]";
	}

	@Override
	public Integer getBoxNum() {
		return boxNum;
	}

	@Override
	public Integer getMixedBoxLineSeq() {
		return mixedBoxLineSeq;
	}
	
	@Override
	public Integer getCartonNumberStart() {
		return cartonNumberStart;
	}

	@Override
	public Integer getCartonNumberEnd() {
		return cartonNumberEnd;
	}
		
}
