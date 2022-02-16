package com.kindminds.drs.v1.model.impl;

import com.kindminds.drs.api.v1.model.accounting.AmazonMonthlyStorageFeeReportRawLine;
import org.springframework.util.Assert;

import java.math.BigDecimal;

public class AmazonMonthlyStorageFeeReportRawLineImpl implements AmazonMonthlyStorageFeeReportRawLine {

	private String asin;
	private String fnsku;
	private String productName;
	private String fulfillmentCenter;	
	private String countryCode; 	
	private BigDecimal longestSide;	
	private BigDecimal medianSide;	
	private BigDecimal shortestSide;
	private String measurementUnits;	
	private BigDecimal weight; 	
	private String weightUnits;
	private BigDecimal itemVolume;
	private String volumeUnits;	
	private String productSizeTier;
	private BigDecimal averageQuantityOnHand;	
	private BigDecimal averageQuantityPendingRemoval; 	
	private BigDecimal estimatedTotalItemVolume;	
	private String monthOfCharge;	
	private BigDecimal storageRate;	
	private String currency;	
	private BigDecimal estimatedMonthlyStorageFee;
	private String dangerousGoodsStorageType;
	private String category;
		
	public AmazonMonthlyStorageFeeReportRawLineImpl(
			String asin,
			String fnsku,
			String productName,
			String fulfillmentCenter,	
			String countryCode, 	
			BigDecimal longestSide,	
			BigDecimal medianSide,	
			BigDecimal shortestSide,
			String measurementUnits,	
			BigDecimal weight, 	
			String weightUnits,
			BigDecimal itemVolume,
			String volumeUnits,
			String productSizeTier,
			BigDecimal averageQuantityOnHand,	
			BigDecimal averageQuantityPendingRemoval, 	
			BigDecimal estimatedTotalItemVolume,
			String monthOfCharge,	
			BigDecimal storageRate,	
			String currency,	
			BigDecimal estimatedMonthlyStorageFee,
			String dangerousGoodsStorageType,
			String category){
		this.asin = asin;
		this.fnsku = fnsku;
		this.productName = productName;
		this.fulfillmentCenter = fulfillmentCenter;
		this.countryCode = countryCode.equals("GB")?"UK":countryCode;
		this.longestSide = longestSide;
		this.medianSide = medianSide;
		this.shortestSide = shortestSide;
		this.measurementUnits = measurementUnits;
		this.weight = weight;
		this.weightUnits = weightUnits;
		this.itemVolume = itemVolume;
		this.volumeUnits = volumeUnits;
		this.productSizeTier = productSizeTier;
		this.averageQuantityOnHand = averageQuantityOnHand;
		this.averageQuantityPendingRemoval = averageQuantityPendingRemoval;
		this.estimatedTotalItemVolume = estimatedTotalItemVolume;
		this.monthOfCharge = monthOfCharge;
		this.storageRate = storageRate;
		this.currency = currency;
		this.estimatedMonthlyStorageFee = estimatedMonthlyStorageFee;
		this.dangerousGoodsStorageType = dangerousGoodsStorageType;
		this.category = category;
		this.assertFieldWithValue();
	}
	
	private void assertFieldWithValue(){
		Assert.notNull(this.asin);
		Assert.notNull(this.fulfillmentCenter);
		Assert.notNull(this.countryCode);
		Assert.notNull(this.longestSide);
		Assert.notNull(this.medianSide);
		Assert.notNull(this.shortestSide);
		Assert.notNull(this.measurementUnits);
		Assert.notNull(this.weight);
		Assert.notNull(this.weightUnits);		
		Assert.notNull(this.itemVolume);
		Assert.notNull(this.volumeUnits);
		Assert.notNull(this.averageQuantityOnHand);
		Assert.notNull(this.averageQuantityPendingRemoval);
		Assert.notNull(this.estimatedTotalItemVolume);		
		Assert.notNull(this.monthOfCharge);
		Assert.notNull(this.storageRate);
		Assert.notNull(this.currency);
		Assert.notNull(this.estimatedMonthlyStorageFee);				
	}
		
	@Override
	public String toString() {
		return "AmazonMonthlyStorageFeeReportRawLineImpl [getAsin()=" + getAsin() + ", getFnsku()=" + getFnsku()
				+ ", getProductName()=" + getProductName() + ", getFulfillmentCenter()=" + getFulfillmentCenter()
				+ ", getCountryCode()=" + getCountryCode() + ", getLongestSide()=" + getLongestSide()
				+ ", getMedianSide()=" + getMedianSide() + ", getShortestSide()=" + getShortestSide()
				+ ", getMeasurementUnits()=" + getMeasurementUnits() + ", getWeight()=" + getWeight()
				+ ", getWeightUnits()=" + getWeightUnits() + ", getItemVolume()=" + getItemVolume()
				+ ", getVolumeUnits()=" + getVolumeUnits() + ", getProductSizeTier()=" + getProductSizeTier()
				+ ", getAverageQuantityOnHand()=" + getAverageQuantityOnHand() + ", getAverageQuantityPendingRemoval()="
				+ getAverageQuantityPendingRemoval() + ", getEstimatedTotalItemVolume()="
				+ getEstimatedTotalItemVolume() + ", getMonthOfCharge()=" + getMonthOfCharge() + ", getStorageRate()="
				+ getStorageRate() + ", getCurrency()=" + getCurrency() + ", getEstimatedMonthlyStorageFee()="
				+ getEstimatedMonthlyStorageFee() + "]";
	}

	@Override
	public String getAsin() {
		return this.asin;
	}

	@Override
	public String getFnsku() {
		return this.fnsku;
	}

	@Override
	public String getProductName() {
		return this.productName;
	}

	@Override
	public String getFulfillmentCenter() {
		return this.fulfillmentCenter;
	}

	@Override
	public String getCountryCode() {
		return this.countryCode;
	}

	@Override
	public BigDecimal getLongestSide() {
		return this.longestSide;
	}

	@Override
	public BigDecimal getMedianSide() {
		return this.medianSide;
	}

	@Override
	public BigDecimal getShortestSide() {
		return this.shortestSide;
	}

	@Override
	public String getMeasurementUnits() {
		return this.measurementUnits;
	}

	@Override
	public BigDecimal getWeight() {
		return this.weight;
	}

	@Override
	public String getWeightUnits() {
		return this.weightUnits;
	}

	@Override
	public BigDecimal getItemVolume() {
		return this.itemVolume;
	}

	@Override
	public String getVolumeUnits() {
		return this.volumeUnits;
	}

	@Override
	public String getProductSizeTier() {
		return this.productSizeTier;
	}
	
	@Override
	public BigDecimal getAverageQuantityOnHand() {
		return this.averageQuantityOnHand;
	}

	@Override
	public BigDecimal getAverageQuantityPendingRemoval() {
		return this.averageQuantityPendingRemoval;
	}

	@Override
	public BigDecimal getEstimatedTotalItemVolume() {
		return this.estimatedTotalItemVolume;
	}

	@Override
	public String getMonthOfCharge() {
		return this.monthOfCharge;
	}

	@Override
	public BigDecimal getStorageRate() {
		return this.storageRate;
	}

	@Override
	public String getCurrency() {
		return this.currency;
	}

	@Override
	public BigDecimal getEstimatedMonthlyStorageFee() {
		return this.estimatedMonthlyStorageFee;
	}

	@Override
	public String getDangerousGoodsStorageType() {
		return dangerousGoodsStorageType;
	}

	@Override
	public String getCategory() {
		return category;
	}
}
