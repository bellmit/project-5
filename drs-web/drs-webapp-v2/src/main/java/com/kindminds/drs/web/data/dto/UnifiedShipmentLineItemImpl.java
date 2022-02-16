package com.kindminds.drs.web.data.dto;

import com.kindminds.drs.api.v1.model.logistics.ShipmentUns.ShipmentUnsLineItem;

public class UnifiedShipmentLineItemImpl implements ShipmentUnsLineItem{
	
	private int lineSeq;
	private String sourceInventoryShipmentName;
	private String skuCode;
	private String skuNameBySupplier;
	private Integer boxNum;
	private Integer mixedBoxLineSeq;
	private Integer cartonNumberStart;
	private Integer cartonNumberEnd;
	private String cartonDimensionCm1;
	private String cartonDimensionCm2;
	private String cartonDimensionCm3;	
	private String perCartonGrossWeightKg;
	private String perCartonUnits;
	private String cartonCounts;
	private String quantity;
	private String unitAmount;
	private String unitCifAmount;
	private String subtotal;
	private String cifSubtotal;
	
	public UnifiedShipmentLineItemImpl(){}
	
	public UnifiedShipmentLineItemImpl(ShipmentUnsLineItem origLineItem){		
		this.lineSeq = origLineItem.getLineSeq();
		this.sourceInventoryShipmentName = origLineItem.getSourceInventoryShipmentName();
		this.skuCode = origLineItem.getSkuCode();
		this.skuNameBySupplier = origLineItem.getNameBySupplier();
		this.boxNum = origLineItem.getBoxNum();
		this.mixedBoxLineSeq = origLineItem.getMixedBoxLineSeq();
		this.cartonDimensionCm1 = origLineItem.getCartonDimensionCm1();
		this.cartonDimensionCm2 = origLineItem.getCartonDimensionCm2();
		this.cartonDimensionCm3 = origLineItem.getCartonDimensionCm3();
		this.perCartonGrossWeightKg = origLineItem.getPerCartonGrossWeightKg();
		this.perCartonUnits = origLineItem.getPerCartonUnits();
		this.cartonCounts = origLineItem.getCartonCounts();
		this.quantity = origLineItem.getQuantity();
		this.unitAmount = origLineItem.getUnitAmount();
		this.unitCifAmount = origLineItem.getUnitCifAmount();
		this.subtotal = origLineItem.getSubtotal();
		this.cifSubtotal = origLineItem.getCifSubtotal();
		this.cartonNumberStart = origLineItem.getCartonNumberStart();
		this.cartonNumberEnd = origLineItem.getCartonNumberEnd();
	}
	
	@Override
	public int getLineSeq() {
		return this.lineSeq;
	}
	
	public void setLineSeq(int lineSeq) {
		this.lineSeq = lineSeq;		
	}
		
	@Override
	public String getSourceInventoryShipmentName() {		
		return this.sourceInventoryShipmentName;
	}
	
	public void setSourceInventoryShipmentName(String sourceInventoryShipmentName){
		this.sourceInventoryShipmentName = sourceInventoryShipmentName;		
	}
	
	@Override
	public String getSkuCode(){
		return this.skuCode;				
	}
		
	public void setSkuCode(String skuCode){
		this.skuCode = skuCode;		
	}
	
	@Override
	public String getNameBySupplier() {
		return this.skuNameBySupplier;
	}
	
	public void setNameBySupplier(String skuNameBySupplier) {
		this.skuNameBySupplier = skuNameBySupplier;
	}
	
	@Override
	public Integer getBoxNum() {
		return this.boxNum;
	}
	
	public void setBoxNum(Integer boxNum) {
		this.boxNum = boxNum;
	}
		
	@Override
	public Integer getMixedBoxLineSeq() {
		return this.mixedBoxLineSeq;
	}
	
	public void setMixedBoxLineSeq(Integer mixedBoxLineSeq) {
		this.mixedBoxLineSeq = mixedBoxLineSeq;
	}
	
	@Override
	public Integer getCartonNumberStart() {
		return cartonNumberStart;
	}

	public void setCartonNumberStart(Integer cartonNumberStart) {
		this.cartonNumberStart = cartonNumberStart;
	}

	@Override
	public Integer getCartonNumberEnd() {
		return cartonNumberEnd;
	}

	public void setCartonNumberEnd(Integer cartonNumberEnd) {
		this.cartonNumberEnd = cartonNumberEnd;
	}

	@Override
	public String getCartonDimensionCm1(){
		return this.cartonDimensionCm1;	   			
	}
	
	public void setCartonDimensionCm1(String cartonDimensionCm1){
		this.cartonDimensionCm1 = cartonDimensionCm1;	
	}
	
	@Override
	public String getCartonDimensionCm2(){
		return this.cartonDimensionCm2;
	}
	
	public void setCartonDimensionCm2(String cartonDimensionCm2){
		this.cartonDimensionCm2 = cartonDimensionCm2;	
	}
	
	@Override
	public String getCartonDimensionCm3(){
		return this.cartonDimensionCm3;
	}
	
	public void setCartonDimensionCm3(String cartonDimensionCm3){
		this.cartonDimensionCm3 = cartonDimensionCm3;	
	}
		
	@Override
	public String getPerCartonGrossWeightKg(){
		return this.perCartonGrossWeightKg;		
	}
	
	public void setPerCartonGrossWeightKg(String perCartonGrossWeightKg){
		this.perCartonGrossWeightKg = perCartonGrossWeightKg;		
	}
	
	@Override
	public String getPerCartonUnits(){
		return this.perCartonUnits;		
	}
	
	public void setPerCartonUnits(String perCartonUnits){
		this.perCartonUnits = perCartonUnits;		
	}
		
	@Override
	public String getCartonCounts(){
		return this.cartonCounts;		
	}
	
	public void setCartonCounts(String cartonCounts){
		this.cartonCounts = cartonCounts;		
	}
	
	@Override
	public String getQuantity(){
		return this.quantity;		
	}
	
	public void setQuantity(String quantity){
		this.quantity = quantity;		
	}
	
	@Override
	public String getUnitAmount(){
		return this.unitAmount;		
	}
	
	public void setUnitAmount(String unitAmount){
		this.unitAmount = unitAmount; 		
	}
	
	@Override
	public String getUnitCifAmount() {
		return this.unitCifAmount;
	}
	
	public void setUnitCifAmount(String unitCifAmount) {
		this.unitCifAmount = unitCifAmount;		
	}
		
	@Override
	public String getSubtotal(){
		return this.subtotal;		
	}
	
	public void setSubtotal(String subtotal){
		this.subtotal = subtotal;		
	}

	@Override
	public String getCifSubtotal() {
		return cifSubtotal;
	}

	public void setCifSubtotal(String cifSubtotal) {
		this.cifSubtotal = cifSubtotal;
	}
	
	
	
}