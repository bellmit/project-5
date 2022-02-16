package com.kindminds.drs.web.data.dto;

import java.io.IOException;

import com.kindminds.drs.api.v2.biz.domain.model.logistics.IvsLineItem;
import com.kindminds.drs.api.v1.model.logistics.ShipmentIvs;
import org.springframework.web.multipart.MultipartFile;



public class InventoryShipmentLineItemImpl  {
	
	private int lineSeq;
	private Integer boxNum;
	private Integer mixedBoxLineSeq;
	private String skuCode;
	private Boolean requireRepackaging;
	private Integer cartonNumberStart;
	private Integer cartonNumberEnd;
	private String skuNameBySupplier;
	private String cartonDimensionCm1;
	private String cartonDimensionCm2;
	private String cartonDimensionCm3;
	private String perCartonGrossWeightKg;
	private String perCartonUnits;
	private String cartonCounts;
	private String quantity;
	private String unitAmount;
	private String amountUntaxed;
	private String GUIInvoiceNumber;
	private String GUIFileName;
	private String GUIuuid;
	private Boolean isGUIInvoiceIsRequired;
	private byte[] GUIInvoiceFileBytes;
	private MultipartFile GUIInvoiceFile;
	
	public InventoryShipmentLineItemImpl(){}
	
	public InventoryShipmentLineItemImpl(ShipmentIvs.ShipmentIvsLineItem origLineItem) throws IOException{
		this.boxNum = origLineItem.getBoxNum();
		this.mixedBoxLineSeq = origLineItem.getMixedBoxLineSeq();
		this.skuCode = origLineItem.getSkuCode();
		this.requireRepackaging = origLineItem.getRequireRepackaging(); 
		this.cartonNumberStart = origLineItem.getCartonNumberStart();
		this.cartonNumberEnd = origLineItem.getCartonNumberEnd();		
		this.skuNameBySupplier = origLineItem.getNameBySupplier();
		this.cartonDimensionCm1 = origLineItem.getCartonDimensionCm1();
		this.cartonDimensionCm2 = origLineItem.getCartonDimensionCm2();
		this.cartonDimensionCm3 = origLineItem.getCartonDimensionCm3();
		this.perCartonGrossWeightKg = origLineItem.getPerCartonGrossWeightKg();
		this.perCartonUnits = origLineItem.getPerCartonUnits();
		this.cartonCounts = origLineItem.getCartonCounts();
		this.quantity = origLineItem.getQuantity();
		this.unitAmount = origLineItem.getUnitAmount();
		this.amountUntaxed = origLineItem.getAmountUntaxed();
		this.GUIInvoiceNumber = origLineItem.getGUIInvoiceNumber();
		this.GUIFileName = origLineItem.getGUIFileName();
		this.GUIuuid = origLineItem.getGUIuuid();
		this.isGUIInvoiceIsRequired = origLineItem.getGUIFileName() == null?false:true;
		this.GUIInvoiceFileBytes = origLineItem.getGUIInvoiceFileBytes();
	}
		
	@Override
	public String toString() {
		return "InventoryShipmentLineItemImpl [getLineSeq()=" + getLineSeq() + ", getBoxNum()=" + getBoxNum()
				+ ", getMixedBoxLineSeq()=" + getMixedBoxLineSeq() + ", getSkuCode()=" + getSkuCode()
				+ ", getRequireRepackaging()=" + getRequireRepackaging() + ", getCartonNumberStart()="
				+ getCartonNumberStart() + ", getCartonNumberEnd()=" + getCartonNumberEnd() + ", getNameBySupplier()="
				+ getNameBySupplier() + ", getCartonDimensionCm1()=" + getCartonDimensionCm1()
				+ ", getCartonDimensionCm2()=" + getCartonDimensionCm2() + ", getCartonDimensionCm3()="
				+ getCartonDimensionCm3() + ", getPerCartonGrossWeightKg()=" + getPerCartonGrossWeightKg()
				+ ", getPerCartonUnits()=" + getPerCartonUnits() + ", getCartonCounts()=" + getCartonCounts()
				+ ", getQuantity()=" + getQuantity() + ", getUnitAmount()=" + getUnitAmount() + ", getAmountUntaxed()="
				+ getAmountUntaxed() + ", getGUIInvoiceNumber()=" + getGuiInvoiceNumber() + ", getGUIFileName()="
				+ getGuiFileName() +" , getGUIuuid()=" + getGuiuuid() + ", getIsGUIInvoiceIsRequired()=" + getIsGUIInvoiceIsRequired()
				+ ", getGUIInvoiceFileBytes()=" + null + "]";
	}


	public int getLineSeq() {
		return 0;
	}
	

	public Integer getBoxNum() {
		return this.boxNum;
	}
		

	public void setBoxNum(Integer boxNum) {
		this.boxNum = boxNum; 		
	}
	

	public Integer getMixedBoxLineSeq() {
		return this.mixedBoxLineSeq;
	}
		

	public void setMixedBoxLineSeq(Integer mixedBoxLineSeq) {
		this.mixedBoxLineSeq = mixedBoxLineSeq;
	}
	

	public String getSkuCode(){
		return this.skuCode;
	}
	
	public void setSkuCode(String skuCode){
		this.skuCode = skuCode;		
	}
		

	public Boolean getRequireRepackaging() {
		return this.requireRepackaging;
	}
	
	public void setRequireRepackaging(Boolean requireRepackaging) {
		this.requireRepackaging = requireRepackaging;
	}
		

	public Integer getCartonNumberStart() {
		return this.cartonNumberStart;
	}
	
	public void setCartonNumberStart(Integer cartonNumberStart) {
		this.cartonNumberStart = cartonNumberStart;
	}
		

	public Integer getCartonNumberEnd() {
		return this.cartonNumberEnd;
	}
	
	public void setCartonNumberEnd(Integer cartonNumberEnd) {
		this.cartonNumberEnd = cartonNumberEnd;
	}
		

	public String getNameBySupplier() {
		return this.skuNameBySupplier;
	}
	
	public void setSkuNameBySupplier(String skuNameBySupplier) {
		this.skuNameBySupplier = skuNameBySupplier;
	}
	

	public String getCartonDimensionCm1(){
		return this.cartonDimensionCm1;	   			
	}
	
	public void setCartonDimensionCm1(String cartonDimensionCm1){
		this.cartonDimensionCm1 = cartonDimensionCm1;	
	}
	

	public String getCartonDimensionCm2(){
		return this.cartonDimensionCm2;
	}
	
	public void setCartonDimensionCm2(String cartonDimensionCm2){
		this.cartonDimensionCm2 = cartonDimensionCm2;	
	}
	

	public String getCartonDimensionCm3(){
		return this.cartonDimensionCm3;
	}
	
	public void setCartonDimensionCm3(String cartonDimensionCm3){
		this.cartonDimensionCm3 = cartonDimensionCm3;	
	}
	

	public String getPerCartonGrossWeightKg(){
		return this.perCartonGrossWeightKg;		
	}
	
	public void setPerCartonGrossWeightKg(String perCartonGrossWeightKg){
		this.perCartonGrossWeightKg = perCartonGrossWeightKg;		
	}
	

	public String getPerCartonUnits(){
		return this.perCartonUnits;		
	}
	
	public void setPerCartonUnits(String perCartonUnits){
		this.perCartonUnits = perCartonUnits;		
	}
		

	public String getCartonCounts(){
		return this.cartonCounts;		
	}
	
	public void setCartonCounts(String cartonCounts){
		this.cartonCounts = cartonCounts;		
	}
	

	public String getQuantity(){
		return this.quantity;		
	}
	
	public void setQuantity(String quantity){
		this.quantity = quantity;		
	}
	

	public String getUnitAmount(){
		return this.unitAmount;		
	}
	
	public void setUnitAmount(String unitAmount){
		this.unitAmount = unitAmount;
	}
			

	public String getAmountUntaxed() {		
		return this.amountUntaxed;
	}
	
	public void setAmountUntaxed(String amountUntaxed) {
		this.amountUntaxed = amountUntaxed;		
	}
		

	public String getGuiInvoiceNumber() {
		return this.GUIInvoiceNumber;
	}
	
	public void setGUIInvoiceNumber(String GUIInvoiceNumber) {
		if(GUIInvoiceNumber.isEmpty())this.GUIInvoiceNumber = null; else this.GUIInvoiceNumber = GUIInvoiceNumber;
	}
		

	public String getGuiFileName() {
		return this.GUIFileName;
	}
	
	public void setGUIFileName(String GUIFileName){
		if(GUIFileName.isEmpty())this.GUIFileName = null; else this.GUIFileName = GUIFileName;
	}

	public String getGuiuuid() { return this.GUIuuid; }

	public void setGUIuuid(String GUIuuid) {
		if(GUIuuid.isEmpty())this.GUIuuid = null; else this.GUIuuid = GUIuuid;
	}

	public Boolean getIsGUIInvoiceIsRequired(){
		return this.isGUIInvoiceIsRequired;
	}
		

	public byte[] getGuiInvoiceFileBytes() {
		return this.GUIInvoiceFileBytes;
	}



	public void setGuiInvoiceNumber( String s) {
		this.GUIInvoiceNumber = s;
	}
}