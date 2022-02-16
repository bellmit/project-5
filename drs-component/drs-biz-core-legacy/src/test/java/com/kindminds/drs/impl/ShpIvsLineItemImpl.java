package com.kindminds.drs.impl;


import com.kindminds.drs.api.v1.model.logistics.ShipmentIvs;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.IvsLineItem;
import com.kindminds.drs.util.TestUtil;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;

public class ShpIvsLineItemImpl implements ShipmentIvs.ShipmentIvsLineItem {

	private int id;
	private int lineSeq;
	private Integer boxNum = 0;
	private Integer mixedBoxLineSeq = 0;
	private String sku;
	private String skuNameBySupplier;
	private BigDecimal perCartonGrossWeight;
	private Integer perCartonUnits;
	private Integer cartonCounts;
	private BigDecimal unitAmount;
	private String cartonDimensionCm1;
	private String cartonDimensionCm2;
	private String cartonDimensionCm3;
	private Boolean requireRepackaging = false;
	private String GUIInvoiceNumber;
	private String GUIFileName;
	private String GUIuuid;
	private Integer cartonNumberStart = 0;
	private Integer cartonNumberEnd = 0;
	private String productVerificationStatus;
	
	public ShpIvsLineItemImpl(IvsLineItem origLineItem) {
		this.id = origLineItem.getId();
		this.boxNum = origLineItem.getBoxNum();
		this.mixedBoxLineSeq = origLineItem.getMixedBoxLineSeq();
		this.sku = origLineItem.getSkuCode();
		this.requireRepackaging = origLineItem.getRequireRepackaging(); 
		this.cartonNumberStart = Integer.valueOf(origLineItem.getCartonNumberStart());
		this.cartonNumberEnd = Integer.valueOf(origLineItem.getCartonNumberEnd());		
		this.skuNameBySupplier = origLineItem.getNameBySupplier();
		this.cartonDimensionCm1 = origLineItem.getCartonDimensionCm1();
		this.cartonDimensionCm2 = origLineItem.getCartonDimensionCm2();
		this.cartonDimensionCm3 = origLineItem.getCartonDimensionCm3();
		this.perCartonGrossWeight = new BigDecimal(origLineItem.getPerCartonGrossWeightKg());
		this.perCartonUnits = Integer.valueOf(origLineItem.getPerCartonUnits());
		this.cartonCounts = Integer.valueOf(origLineItem.getCartonCounts());
		this.unitAmount = new BigDecimal(origLineItem.getUnitAmount());
		this.GUIInvoiceNumber = origLineItem.getGuiInvoiceNumber();
		this.GUIFileName = origLineItem.getGuiFileName();
		this.GUIuuid = origLineItem.getGuiuuid();
		this.productVerificationStatus = origLineItem.getProductVerificationStatus().getValue();
	}

	public ShpIvsLineItemImpl(int id , int lineSeq, Integer boxNum,
								   Integer mixedBoxLineSeq, String codeByDrs,
								   String nameBySupplier, String quantity, String unitAmount,
								   String ctnDim1cm, String ctnDim2cm, String ctnDim3cm,
								   String grossWeightPerCtnKg, Integer unitsPerCtn,
								   Integer ctnCounts, Boolean requireRepackaging,
								   String GUIInvoiceNumber, String GUIFileName,String GUIuuid,
								   Integer cartonNumberStart, Integer cartonNumberEnd) {
		this.id = id;
		this.lineSeq = lineSeq;
		this.boxNum = boxNum;
		this.mixedBoxLineSeq = mixedBoxLineSeq;
		this.sku = codeByDrs;
		this.skuNameBySupplier = nameBySupplier;
		this.unitAmount = new BigDecimal(unitAmount);
		this.cartonDimensionCm1 = ctnDim1cm;
		this.cartonDimensionCm2 = ctnDim2cm;
		this.cartonDimensionCm3 = ctnDim3cm;
		this.perCartonGrossWeight = new BigDecimal(grossWeightPerCtnKg);
		this.perCartonUnits = unitsPerCtn;
		this.cartonCounts = ctnCounts;
		this.requireRepackaging = requireRepackaging;
		this.GUIInvoiceNumber = GUIInvoiceNumber;
		this.GUIFileName = GUIFileName;
		this.GUIuuid = GUIuuid;
		this.cartonNumberStart = cartonNumberStart;
		this.cartonNumberEnd = cartonNumberEnd;
	}
	
	public ShpIvsLineItemImpl(
			int id,
			int lineSeq,
			Integer boxNum, 
			Integer mixedBoxLineSeq, 
			String sku,
			String skuNameBySupplier,
			Boolean requireRepackaging,
			String cartonDimension1,
			String cartonDimension2,
			String cartonDimension3,
			String perCartonGrossWeight,
			Integer perCartonUnits,
			Integer cartonCounts,
			String unitAmount) {
		this.id = id;
		this.lineSeq = lineSeq;
		this.boxNum = boxNum;
		this.mixedBoxLineSeq = mixedBoxLineSeq;
		this.sku = sku;
		this.skuNameBySupplier = skuNameBySupplier;
		this.requireRepackaging = requireRepackaging;
		this.cartonDimensionCm1 = cartonDimension1;
		this.cartonDimensionCm2 = cartonDimension2;
		this.cartonDimensionCm3 = cartonDimension3;
		this.perCartonGrossWeight = perCartonGrossWeight==null?BigDecimal.ZERO:new BigDecimal(perCartonGrossWeight);
		this.cartonCounts = cartonCounts;
		this.perCartonUnits = perCartonUnits;
		this.unitAmount = unitAmount==null?BigDecimal.ZERO:new BigDecimal(unitAmount);
	}

	public ShpIvsLineItemImpl(
			int id,
			int lineSeq,
			String sku,
			String skuNameBySupplier,
			String perCartonGrossWeight,
			Integer perCartonUnits,
			Integer cartonCounts,
			String unitAmount,
			String cartonDimension1,
			String cartonDimension2,
			String cartonDimension3) {
		this.id = id;
		this.lineSeq = lineSeq;
		this.sku = sku;
		this.skuNameBySupplier = skuNameBySupplier;
		this.perCartonGrossWeight = new BigDecimal(perCartonGrossWeight);
		this.perCartonUnits = perCartonUnits;
		this.cartonCounts = cartonCounts;
		this.unitAmount = new BigDecimal(unitAmount);
		this.cartonDimensionCm1 = cartonDimension1;
		this.cartonDimensionCm2 = cartonDimension2;
		this.cartonDimensionCm3 = cartonDimension3;
	}

	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public int getLineSeq() {
		return this.lineSeq;
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
		return this.perCartonGrossWeight.setScale(3).toString();
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
		if (cartonCounts==null || cartonCounts == 0) return Integer.toString(perCartonUnits * 1);
		return Integer.toString(perCartonUnits * cartonCounts);
	}
	
	@Override
	public String getUnitAmount() {
		return this.unitAmount.stripTrailingZeros().toPlainString();
	}
	
	@Override
	public String getAmountUntaxed() {
		BigDecimal qty = new BigDecimal(this.getQuantity());
		return qty.multiply(this.unitAmount).setScale(2).toString();
	}
	
	@Override
	public String getCartonDimensionCm1() {
		return this.cartonDimensionCm1;
	}
	
	@Override
	public String getCartonDimensionCm2() {
		return this.cartonDimensionCm2;
	}
	
	@Override
	public String getCartonDimensionCm3() {
		return this.cartonDimensionCm3;
	}
	
	@Override
	public boolean equals(Object obj){
		if( obj instanceof IvsLineItem){
			IvsLineItem item = (IvsLineItem)obj;
			return this.getLineSeq()==item.getLineSeq()
				&& this.getSkuCode().equals(item.getSkuCode())
				&& this.getNameBySupplier().equals(item.getNameBySupplier())
				&& this.getPerCartonGrossWeightKg().equals(item.getPerCartonGrossWeightKg())
				&& this.getPerCartonUnits().equals(item.getPerCartonUnits())
				&& this.getCartonCounts().equals(item.getCartonCounts())
				&& this.getQuantity().equals(item.getQuantity())
				&& this.getUnitAmount().equals(item.getUnitAmount())
				&& this.getAmountUntaxed().equals(item.getAmountUntaxed())
				&& this.getCartonDimensionCm1().equals(item.getCartonDimensionCm1())
				&& this.getCartonDimensionCm2().equals(item.getCartonDimensionCm2())
				&& this.getCartonDimensionCm3().equals(item.getCartonDimensionCm3())
				&& this.getRequireRepackaging().equals(item.getRequireRepackaging())
				&& TestUtil.nullableEquals(this.getGUIInvoiceNumber(),item.getGuiInvoiceNumber())
				&& TestUtil.nullableEquals(this.getGUIFileName(),item.getGuiFileName())
				&& TestUtil.nullableEquals(this.getGUIuuid(),item.getGuiuuid());
		}
		return false;
	}

	public Boolean getRequireRepackaging() {
		return requireRepackaging;
	}

	public String getGUIInvoiceNumber() {
		return GUIInvoiceNumber;
	}
	
	public void setGUIInvoiceNumber(String guiInvoiceNumber) {
		GUIInvoiceNumber = guiInvoiceNumber;
	}

	@Override
	public String getGUIFileName() {
		return null;
	}

	@Override
	public String getGUIuuid() { return GUIuuid;}

	@Override
	public void setGUIuuid(String guiuuid) {GUIuuid=guiuuid;}

	public Integer getCartonNumberStart() {
		return cartonNumberStart;
	}

	public Integer getCartonNumberEnd() {
		return cartonNumberEnd;
	}

	@Override
	public void setCartonNumberStart(Integer start) {
		this.cartonNumberStart = start;
	}

	@Override
	public void setCartonNumberEnd(Integer end) {
		this.cartonNumberEnd = end;
	}



	@Override
	public Integer getBoxNum() {
		return boxNum;
	}

	public void setBoxNum(Integer boxNum) {
		this.boxNum = boxNum;
	}

	@Override
	public Integer getMixedBoxLineSeq() {
		return mixedBoxLineSeq;
	}

	public void setMixedBoxLineSeq(Integer mixedBoxLineSeq) {
		this.mixedBoxLineSeq = mixedBoxLineSeq;
	}

//	@Override
	public byte[] getGUIInvoiceFileBytes() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(GUIFileName).getFile());
        return Files.readAllBytes(file.toPath());
	}

	@Override
	public String getProductVerificationStatus() {
		return this.productVerificationStatus;
	}


	@Override
	public String toString() {
		return "ShpIvsLineItemImpl [getLineSeq()=" + getLineSeq()
		 		+ ", getBoxNum()=" + getBoxNum() + ", getMixedBoxLineSeq()=" + getMixedBoxLineSeq()
		 		+ ", getSkuCode()=" + getSkuCode()
				+ ", getNameBySupplier()=" + getNameBySupplier() + ", getPerCartonGrossWeightKg()="
				+ getPerCartonGrossWeightKg() + ", getPerCartonUnits()=" + getPerCartonUnits() + ", getCartonCounts()="
				+ getCartonCounts() + ", getQuantity()=" + getQuantity() + ", getUnitAmount()=" + getUnitAmount()
				+ ", getAmountUntaxed()=" + getAmountUntaxed() + ", getCartonDimensionCm1()=" + getCartonDimensionCm1()
				+ ", getCartonDimensionCm2()=" + getCartonDimensionCm2() + ", getCartonDimensionCm3()="
				+ getCartonDimensionCm3() + ", getRequireRepackaging()=" + getRequireRepackaging()
				+ ", getGUIInvoiceNumber()=" + getGUIInvoiceNumber() + ", getCartonNumberStart()="
				+ getCartonNumberStart() + ", getCartonNumberEnd()=" + getCartonNumberEnd() + ", getGUIFileName()="
				+ getGUIFileName()+" , getGUIuuid()=" + getGUIuuid()
				+ "]";
	}



}
