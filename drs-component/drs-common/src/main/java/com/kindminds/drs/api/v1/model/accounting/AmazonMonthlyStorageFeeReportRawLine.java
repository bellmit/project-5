package com.kindminds.drs.api.v1.model.accounting;

import java.math.BigDecimal;

public interface AmazonMonthlyStorageFeeReportRawLine {

	String getAsin();
	String getFnsku();
	String getProductName();
	String getFulfillmentCenter();
	String getCountryCode();
	BigDecimal getLongestSide();
	BigDecimal getMedianSide();
	BigDecimal getShortestSide();
	String getMeasurementUnits();
	BigDecimal getWeight();
	String getWeightUnits();
	BigDecimal getItemVolume();
	String getVolumeUnits();
	String getProductSizeTier();
	BigDecimal getAverageQuantityOnHand();
	BigDecimal getAverageQuantityPendingRemoval();
	BigDecimal getEstimatedTotalItemVolume();
	String getMonthOfCharge();
	BigDecimal getStorageRate();
	String getCurrency();
	BigDecimal getEstimatedMonthlyStorageFee();
	String getDangerousGoodsStorageType();
	String getCategory();
		
}
