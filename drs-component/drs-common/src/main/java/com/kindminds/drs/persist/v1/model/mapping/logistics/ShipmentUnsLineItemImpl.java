package com.kindminds.drs.persist.v1.model.mapping.logistics;

import java.math.BigDecimal;





import com.kindminds.drs.api.v1.model.logistics.ShipmentUns.ShipmentUnsLineItem;
import com.kindminds.drs.util.NumberHelper;


public class ShipmentUnsLineItemImpl implements ShipmentUnsLineItem {

	//@Id ////@Column(name="id")
	private int id;
	//@Column(name="line_seq")
	private int lineSeq;
	//@Column(name="box_num")
	private Integer boxNum;
	//@Column(name="mixed_box_line_seq")
	private Integer mixedBoxLineSeq;
	//@Column(name="source_shipment_name")
	private String sourceShipmentName;
	//@Column(name="code_by_drs")
	private String codeByDrs;	
	//@Column(name="name_by_supplier")
	private String nameBySupplier;		
	//@Column(name="quantity")
	private BigDecimal quantity;
	//@Column(name="unit_amount")
	private BigDecimal unitAmount;
	//@Column(name="unit_cif_amount")
	private BigDecimal unitCifAmount;
	//@Column(name="ctn_dim_1_cm")
	private BigDecimal ctnDim1cm;
	//@Column(name="ctn_dim_2_cm")
	private BigDecimal ctnDim2cm;
	//@Column(name="ctn_dim_3_cm")
	private BigDecimal ctnDim3cm;
	//@Column(name="gross_weight_per_ctn_kg")
	private BigDecimal grossWeightPerCtnKg;
	//@Column(name="units_per_ctn")
	private Integer unitsPerCtn;
	//@Column(name="numbers_of_ctn")
	private Integer ctnCounts;
	//@Column(name="carton_number_start")
	private Integer cartonNumberStart;
	//@Column(name="carton_number_end")
	private Integer cartonNumberEnd;

	public ShipmentUnsLineItemImpl(int id, int lineSeq, Integer boxNum, Integer mixedBoxLineSeq, String sourceShipmentName, String codeByDrs, String nameBySupplier, BigDecimal quantity, BigDecimal unitAmount, BigDecimal unitCifAmount, BigDecimal ctnDim1cm, BigDecimal ctnDim2cm, BigDecimal ctnDim3cm, BigDecimal grossWeightPerCtnKg, Integer unitsPerCtn, Integer ctnCounts, Integer cartonNumberStart, Integer cartonNumberEnd) {
		this.id = id;
		this.lineSeq = lineSeq;
		this.boxNum = boxNum;
		this.mixedBoxLineSeq = mixedBoxLineSeq;
		this.sourceShipmentName = sourceShipmentName;
		this.codeByDrs = codeByDrs;
		this.nameBySupplier = nameBySupplier;
		this.quantity = quantity;
		this.unitAmount = unitAmount;
		this.unitCifAmount = unitCifAmount;
		this.ctnDim1cm = ctnDim1cm;
		this.ctnDim2cm = ctnDim2cm;
		this.ctnDim3cm = ctnDim3cm;
		this.grossWeightPerCtnKg = grossWeightPerCtnKg;
		this.unitsPerCtn = unitsPerCtn;
		this.ctnCounts = ctnCounts;
		this.cartonNumberStart = cartonNumberStart;
		this.cartonNumberEnd = cartonNumberEnd;
	}

	public ShipmentUnsLineItemImpl() {
	}

	@Override
	public int getLineSeq() {
		return this.lineSeq;
	}

	@Override
	public Integer getBoxNum() {
		return this.boxNum;
	}

	@Override
	public Integer getMixedBoxLineSeq() {
		return this.mixedBoxLineSeq;
	}
	
	@Override
	public String getSourceInventoryShipmentName() {
		return this.sourceShipmentName;
	}

	@Override
	public String getSkuCode() {
		return this.codeByDrs;
	}
	
	@Override
	public String getNameBySupplier() {
		return this.nameBySupplier;
	}
	
	@Override
	public String getPerCartonGrossWeightKg() {
		return NumberHelper.toGeneralCommaSeparatedString((BigDecimal)this.grossWeightPerCtnKg,2);
	}
	
	@Override
	public String getPerCartonUnits() {
		return this.unitsPerCtn.toString();
	}
	
	@Override
	public String getCartonCounts() {
		return this.ctnCounts.toString();
	}

	@Override
	public Integer getCartonNumberStart() {
		return cartonNumberStart;
	}

	@Override
	public Integer getCartonNumberEnd() {
		return cartonNumberEnd;
	}

	@Override
	public String getQuantity() {
		return NumberHelper.toGeneralCommaSeparatedString(this.quantity,0);
	}
	
	@Override
	public String getUnitAmount() {
		return this.unitAmount.stripTrailingZeros().toPlainString();
	}
	
	
	
	@Override
	public String getUnitCifAmount(){
		if(this.unitCifAmount==null) return null;
		return this.unitCifAmount.stripTrailingZeros().toPlainString();
	}
	
	@Override
	public String getSubtotal() {
		BigDecimal subtotal = this.quantity.multiply(this.unitAmount);
		return NumberHelper.toGeneralCommaSeparatedString(subtotal,2);
	}

	@Override
	public String getCifSubtotal() {
		if(this.unitCifAmount==null) return null;
		BigDecimal subtotal = this.quantity.multiply(this.unitCifAmount);
		return NumberHelper.toGeneralCommaSeparatedString(subtotal,2);
	}
	
	@Override
	public String getCartonDimensionCm1() {
		return NumberHelper.toGeneralCommaSeparatedString((BigDecimal)this.ctnDim1cm,1);
	}
	
	@Override
	public String getCartonDimensionCm2() {
		return NumberHelper.toGeneralCommaSeparatedString((BigDecimal)this.ctnDim2cm,1);		
	}
	
	@Override
	public String getCartonDimensionCm3() {
		return NumberHelper.toGeneralCommaSeparatedString((BigDecimal)this.ctnDim3cm,1);		
	}

	@Override
	public String toString() {
		return "ShipmentUnsLineItemImpl [getLineSeq()=" + getLineSeq() + ", getSourceInventoryShipmentName()="
				+ getSourceInventoryShipmentName() + ", getSkuCode()=" + getSkuCode() + ", getNameBySupplier()="
				+ getNameBySupplier() + ", getPerCartonGrossWeightKg()=" + getPerCartonGrossWeightKg()
				+ ", getPerCartonUnits()=" + getPerCartonUnits() + ", getCartonCounts()=" + getCartonCounts()
				+ ", getQuantity()=" + getQuantity() + ", getUnitAmount()=" + getUnitAmount() + ", getUnitCifAmount()="
				+ getUnitCifAmount() + ", getSubtotal()=" + getSubtotal() + ", getCartonDimensionCm1()="
				+ getCartonDimensionCm1() + ", getCartonDimensionCm2()=" + getCartonDimensionCm2()
				+ ", getCartonDimensionCm3()=" + getCartonDimensionCm3() + "]";
	}
		
}
