package com.kindminds.drs.core.biz.inventory;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v2.biz.domain.model.product.SkuShipmentAllocationInfo;


import java.math.BigDecimal;


public class SkuShipmentAllocationInfoImplSvc implements SkuShipmentAllocationInfo {

	private int ivsSkuIdentificationId;
	private String ivsSkuIdentificationSerialNo;
	private String ivsSkuIdentificationStatus;
	private String ivsSkuIdentificationRemark;
	private int ivsSkuIdentificationDrsTranId;

	private String sku;
	private IvsInfo ivsInfo;
	private UnsInfo unsInfo;

	public SkuShipmentAllocationInfoImplSvc(
			int ivsSkuIdentificationId ,
			String ivsSkuIdentificationSerialNo ,
			String ivsSkuIdentificationRemark ,
			int ivsSkuIdentificationDrsTranId,
			String ivsSkuIdentificationStatus,
			String sku,
			IvsInfo ivsInfo,
			UnsInfo unsInfo){
		super();
		this.ivsSkuIdentificationId = ivsSkuIdentificationId;
		this.ivsSkuIdentificationSerialNo = ivsSkuIdentificationSerialNo;
		this.sku = sku;
		this.ivsInfo = ivsInfo;
		this.unsInfo = unsInfo;
		this.ivsSkuIdentificationRemark = ivsSkuIdentificationRemark;
		this.ivsSkuIdentificationDrsTranId = ivsSkuIdentificationDrsTranId;
		this.ivsSkuIdentificationStatus = ivsSkuIdentificationStatus;
	}

	@Override
	public String toString() {
		return "SkuShipmentAllocationInfoImplSvc [getSku()=" + getSku() + ", getIvsName()=" + getIvsName()
				+ ", getFcaCurrency()=" + getFcaCurrency() + ", getFcaPrice()=" + getFcaPrice()
				+ ", getIvsSalesTaxRate()=" + getIvsSalesTaxRate() + ", getUnsName()=" + getUnsName()
				+ ", getDdpCurrency()=" + getDdpCurrency() + ", getDdpAmount()=" + getDdpAmount()
				+ ", getFxRateFromFcaCurrencyToDestinationCountryCurrency()="
				+ getFxRateFromFcaCurrencyToDestinationCountryCurrency() + ", getFxRateFromFcaCurrencyToEur()="
				+ getFxRateFromFcaCurrencyToEur() + "]";
	}

	@Override
	public int getIvsSkuIdentificationId() {
		return this.ivsSkuIdentificationId;
	}

	@Override
	public String getIvsSkuIdentificationSerialNo() {
		return this.ivsSkuIdentificationSerialNo;
	}

	@Override
	public String getIvsSkuIdentificationStatus(){
		return this.ivsSkuIdentificationStatus;
	}

	@Override
	public void setIvsSkuIdentificationStatus(String status){
		this.ivsSkuIdentificationStatus = status;
	}

	@Override
	public String getIvsSkuIdentificationRemark() {
		return this.ivsSkuIdentificationRemark;
	}

	@Override
	public void setIvsSkuIdentificationRemark(String remark) {
		this.ivsSkuIdentificationRemark = remark;
	}

	@Override
	public int getIvsSkuIdentificationDrsTranId() {
		return this.ivsSkuIdentificationDrsTranId;
	}


	@Override
	public String getSku() {
		return this.sku;
	}

	@Override
	public String getIvsName() {
		return this.ivsInfo.getName();
	}

	@Override
	public Currency getFcaCurrency() {
		return this.ivsInfo.getFcaCurrency();
	}

	@Override
	public BigDecimal getFcaPrice() {
		return this.ivsInfo.getFcaPrice();
	}

	@Override
	public BigDecimal getIvsSalesTaxRate() {
		return this.ivsInfo.getSalesTaxRate();
	}

	@Override
	public String getUnsName() {
		return this.unsInfo.getName();
	}

	@Override
	public Currency getDdpCurrency() {
		return this.unsInfo.getDdpCurrency();
	}

	@Override
	public BigDecimal getDdpAmount() {
		return this.unsInfo.getDdpAmount();
	}

	@Override
	public BigDecimal getFxRateFromFcaCurrencyToDestinationCountryCurrency() {
		return this.unsInfo.getFxRateFromFcaCurrencyToDestinationCountryCurrency();
	}

	@Override
	public BigDecimal getFxRateFromFcaCurrencyToEur() {
		return this.unsInfo.getFxRateFromFcaCurrencyToEur();
	}

}