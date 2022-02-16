package com.kindminds.drs.v1.model.impl.stock;


import com.kindminds.drs.api.v2.biz.domain.model.inventory.ShipmentStockAvailableInfo;

public class ShipmentStockAvailableInfoImpl implements ShipmentStockAvailableInfo {
	
	private String unsName;
	private String ivsName;
	private Integer quantity;
	private Integer ivsSkuIdentificationId;
	private String ivsSkuIdentificationSerialNo;
	private String ivsSkuIdentificationRemark;
	private Integer ivsSkuIdentificationDrsTranId;
	private String ivsSkuIdentificationStatus;
	
	public ShipmentStockAvailableInfoImpl(String unsName, String ivsName, Integer quantity ,
										  Integer ivsSkuIdentificationId,String ivsSkuIdentificationSerialNo ,
										  String ivsSkuIdentificationRemark ,
										  Integer ivsSkuIdentificationDrsTranId ,
										  String ivsSkuIdentificationStatus) {
		this.unsName = unsName;
		this.ivsName = ivsName;
		this.quantity = quantity;
		this.ivsSkuIdentificationId = ivsSkuIdentificationId;
		this.ivsSkuIdentificationSerialNo = ivsSkuIdentificationSerialNo;
		this.ivsSkuIdentificationRemark = ivsSkuIdentificationRemark;
		this.ivsSkuIdentificationDrsTranId = ivsSkuIdentificationDrsTranId;
		this.ivsSkuIdentificationStatus = ivsSkuIdentificationStatus;
	}
	
	@Override
	public String getUnsName() {
		return this.unsName;
	}

	@Override
	public String getIvsName() {
		return this.ivsName;
	}
	
	@Override
	public Integer getQuantity() {
		return this.quantity;
	}

	@Override
	public void setQuantity(Integer value) {
		this.quantity=value;
	}

	@Override
	public Integer getIvsSkuIdentificationId() {
		return this.ivsSkuIdentificationId;
	}

	@Override
	public String getIvsSkuIdentificationSerialNo() {
		return this.ivsSkuIdentificationSerialNo;
	}

	@Override
	public String getIvsSkuIdentificationRemark() {
		return this.ivsSkuIdentificationRemark;
	}

	@Override
	public Integer getIvsSkuIdentificationDrsTranId() {
		return this.ivsSkuIdentificationDrsTranId;
	}

	@Override
	public String getIvsSkuIdentificationStatus() {
		return this.ivsSkuIdentificationStatus;
	}

}
