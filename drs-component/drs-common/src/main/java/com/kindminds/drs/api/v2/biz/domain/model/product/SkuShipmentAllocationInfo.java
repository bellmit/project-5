package com.kindminds.drs.api.v2.biz.domain.model.product;

import com.kindminds.drs.Currency;

import java.math.BigDecimal;

public interface SkuShipmentAllocationInfo {

	int getIvsSkuIdentificationId();
	String getIvsSkuIdentificationSerialNo();

	String getIvsSkuIdentificationStatus();
	void setIvsSkuIdentificationStatus(String status);

	String getIvsSkuIdentificationRemark();
	void setIvsSkuIdentificationRemark(String remark);

	int getIvsSkuIdentificationDrsTranId();

	String getSku();
	String getIvsName();
	Currency getFcaCurrency();
	BigDecimal getFcaPrice();
	BigDecimal getIvsSalesTaxRate();
	String getUnsName();
	Currency getDdpCurrency();
	BigDecimal getDdpAmount();
	BigDecimal getFxRateFromFcaCurrencyToDestinationCountryCurrency();
	BigDecimal getFxRateFromFcaCurrencyToEur();

	public interface IvsInfo {
		String getName();
		Currency getFcaCurrency();
		BigDecimal getFcaPrice();
		BigDecimal getSalesTaxRate();
	}

	public interface UnsInfo {
		String getName();
		Currency getDdpCurrency();
		BigDecimal getDdpAmount();
		BigDecimal getFxRateFromFcaCurrencyToDestinationCountryCurrency();
		BigDecimal getFxRateFromFcaCurrencyToEur();
	}
}