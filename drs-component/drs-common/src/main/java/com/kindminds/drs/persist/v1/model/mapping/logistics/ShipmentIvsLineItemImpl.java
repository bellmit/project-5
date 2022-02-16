package com.kindminds.drs.persist.v1.model.mapping.logistics;

import com.kindminds.drs.api.v1.model.logistics.ShipmentIvs;
import org.springframework.util.StringUtils;


import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;






public class ShipmentIvsLineItemImpl implements ShipmentIvs.ShipmentIvsLineItem , Serializable {

	//@Id ////@Column(name="id")
	private int id;
	//@Column(name="line_seq")
	private int lineSeq;
	//@Column(name="box_num")
	private Integer boxNum;
	//@Column(name="mixed_box_line_seq")
	private Integer mixedBoxLineSeq;
	//@Column(name="code_by_drs")
	private String codeByDrs;	
	//@Column(name="name_by_supplier")
	private String nameBySupplier;	
	//@Column(name="quantity")
	private BigDecimal quantity;
	//@Column(name="unit_amount")
	private BigDecimal unitAmount;
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
	//@Column(name="require_packaging")
	private Boolean requireRepackaging;
	//@Column(name="gui_invoice_number")
	private String GUIInvoiceNumber;
	//@Column(name="gui_file_name")
	private String GUIFileName;

	private String GUIuuid;
	//@Column(name="carton_number_start")
	private Integer cartonNumberStart;
	//@Column(name="carton_number_end")
	private Integer cartonNumberEnd;
	//@Column(name="line_status")
	private String productVerificationStatus;

	public ShipmentIvsLineItemImpl() {

	}

	public ShipmentIvsLineItemImpl(
			Integer boxNum, Integer mixedBoxLineSeq, String codeByDrs,
			Integer quantity, Integer unitAmount,
			Double ctnDim1cm, Double ctnDim2cm, Double ctnDim3cm,
			Double grossWeightPerCtnKg, Integer unitsPerCtn, Integer ctnCounts) {
		this.boxNum = boxNum;
		this.mixedBoxLineSeq = mixedBoxLineSeq;
		this.codeByDrs = codeByDrs;
		this.quantity = new BigDecimal(quantity);
		this.unitAmount = new BigDecimal(unitAmount);
		this.ctnDim1cm = BigDecimal.valueOf(ctnDim1cm);
		this.ctnDim2cm = BigDecimal.valueOf(ctnDim2cm);
		this.ctnDim3cm = BigDecimal.valueOf(ctnDim3cm);
		this.grossWeightPerCtnKg = BigDecimal.valueOf(grossWeightPerCtnKg);
		this.unitsPerCtn = unitsPerCtn;
		this.ctnCounts = ctnCounts;
	}

	public ShipmentIvsLineItemImpl(int id, int lineSeq, Integer boxNum, Integer mixedBoxLineSeq, String codeByDrs,
								   String nameBySupplier, BigDecimal quantity, BigDecimal unitAmount,
								   BigDecimal ctnDim1cm, BigDecimal ctnDim2cm, BigDecimal ctnDim3cm, BigDecimal grossWeightPerCtnKg,
								   Integer unitsPerCtn, Integer ctnCounts, Boolean requireRepackaging, String GUIInvoiceNumber,
								   String GUIFileName, String GUIuuid, Integer cartonNumberStart, Integer cartonNumberEnd, String productVerificationStatus) {
		this.id = id;
		this.lineSeq = lineSeq;
		this.boxNum = boxNum;
		this.mixedBoxLineSeq = mixedBoxLineSeq;
		this.codeByDrs = codeByDrs;
		this.nameBySupplier = nameBySupplier;
		this.quantity = quantity;
		this.unitAmount = unitAmount;
		this.ctnDim1cm = ctnDim1cm;
		this.ctnDim2cm = ctnDim2cm;
		this.ctnDim3cm = ctnDim3cm;
		this.grossWeightPerCtnKg = grossWeightPerCtnKg;
		this.unitsPerCtn = unitsPerCtn;
		this.ctnCounts = ctnCounts;
		this.requireRepackaging = requireRepackaging;
		this.GUIInvoiceNumber = GUIInvoiceNumber;
		this.GUIFileName = GUIFileName;
		this.GUIuuid = GUIuuid;
		this.cartonNumberStart = cartonNumberStart;
		this.cartonNumberEnd = cartonNumberEnd;
		this.productVerificationStatus = productVerificationStatus;
	}

	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public int getLineSeq() {
		return this.lineSeq;
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
		return this.grossWeightPerCtnKg.setScale(3).toString();
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
	public String getQuantity() {
		return this.quantity.setScale(0).toString();
	}
	
	@Override
	public String getUnitAmount() {
		return this.unitAmount.stripTrailingZeros().toPlainString();
	}

	@Override
	public String getAmountUntaxed() {
		BigDecimal amountUntaxed = this.quantity.multiply(this.unitAmount);
		return amountUntaxed.setScale(2, RoundingMode.HALF_UP).toString();
	}
	
	@Override
	public String getCartonDimensionCm1() {
		return this.ctnDim1cm.stripTrailingZeros().toPlainString();
	}
	
	@Override
	public String getCartonDimensionCm2() {
		return this.ctnDim2cm.stripTrailingZeros().toPlainString();
	}
	
	@Override
	public String getCartonDimensionCm3() {
		return this.ctnDim3cm.stripTrailingZeros().toPlainString();
	}

	@Override
	public Boolean getRequireRepackaging() {
		return requireRepackaging;
	}


	@Override
	public String getGUIInvoiceNumber() {
		return GUIInvoiceNumber;
	}

	@Override
	public void setGUIInvoiceNumber(String guiInvoiceNumber) {
		GUIInvoiceNumber = guiInvoiceNumber;
	}

	@Override
	public String getGUIFileName() {
		return StringUtils.isEmpty(GUIFileName) ? null : GUIFileName ;
	}

	@Override
	public String getGUIuuid() {return StringUtils.isEmpty(GUIuuid) ? null : GUIuuid ;}

	@Override
	public void setGUIuuid(String GUIuuid) {this.GUIuuid = GUIuuid;}

	@Override
	public byte[] getGUIInvoiceFileBytes() {
		return new byte[0];
	}

	@Override
	public String getProductVerificationStatus() {
		return StringUtils.isEmpty(this.productVerificationStatus) ? "New" : this.productVerificationStatus  ;
	}


	@Override
	public Integer getCartonNumberStart() {
		return cartonNumberStart==null? 0 : cartonNumberStart;
	}

	@Override
	public Integer getCartonNumberEnd() {
		return cartonNumberEnd==null? 0 : cartonNumberEnd;
	}

	@Override
	public void setCartonNumberStart(Integer cartonNumberStart) {
		this.cartonNumberStart = cartonNumberStart;
	}

	@Override
	public void setCartonNumberEnd(Integer cartonNumberEnd) {
		this.cartonNumberEnd = cartonNumberEnd;
	}

	@Override
	public String toString() {
		return "ShipmentIvsLineItemImpl [getLineSeq()=" + getLineSeq()
				+ ", getBoxNum()=" + getBoxNum()
				+ ", getMixedBoxLineSeq()=" + getMixedBoxLineSeq()
				+ ", getSkuCode()=" + getSkuCode()
				+ ", getNameBySupplier()=" + getNameBySupplier()
				+ ", getPerCartonGrossWeightKg()=" + getPerCartonGrossWeightKg()
				+ ", getPerCartonUnits()=" + getPerCartonUnits()
				+ ", getCartonCounts()=" + getCartonCounts()
				+ ", getQuantity()=" + getQuantity()
				+ ", getUnitAmount()=" + getUnitAmount()
				+ ", getAmountUntaxed()=" + getAmountUntaxed()
				+ ", getCartonDimensionCm1()=" + getCartonDimensionCm1()
				+ ", getCartonDimensionCm2()=" + getCartonDimensionCm2()
				+ ", getCartonDimensionCm3()=" + getCartonDimensionCm3()
				+ ", getRequireRepackaging()=" + getRequireRepackaging()
				+ ", getGUIInvoiceNumber()=" + getGUIInvoiceNumber()
				+ ", getCartonNumberStart()=" + getCartonNumberStart()
				+ ", getCartonNumberEnd()=" + getCartonNumberEnd()
				+ ", getGUIFileName()=" + getGUIFileName()
				+ ", getGUIuuid()=" + getGUIuuid()
				+ "]";
	}


}
