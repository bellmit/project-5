package com.kindminds.drs.v1.model.impl.report;

import com.kindminds.drs.api.v1.model.report.MonthlyStorageFeeReport.MonthlyStorageFeeReportLineItem;

import java.math.BigDecimal;

public class MonthlyStorageFeeReportLineItemImpl implements MonthlyStorageFeeReportLineItem{

	private String sku;
	private String productName;
	private BigDecimal itemVolume;
	private String volumeUnit;
	private BigDecimal totalAverageQuantityOnHand;
	private BigDecimal totalAverageQuantityPendingRemoval;
	private BigDecimal totalEstimatedTotalItemVolume;
	private String monthOfCharge;
	private BigDecimal storageRate;
	private String currency;
	private BigDecimal totalEstimatedMonthlyStorageFee;
		
	public BigDecimal getNumericTotalEstimatedMonthlyStorageFee() {return this.totalEstimatedMonthlyStorageFee.setScale(2, BigDecimal.ROUND_HALF_UP);}
	
	public MonthlyStorageFeeReportLineItemImpl(
			String sku,
			String productName,
			BigDecimal itemVolume,
			String volumeUnit,
			BigDecimal totalAverageQuantityOnHand,
			BigDecimal totalAverageQuantityPendingRemoval,
			BigDecimal totalEstimatedTotalItemVolume,
			String monthOfCharge,
			BigDecimal storageRate,
			String currency,
			BigDecimal totalEstimatedMonthlyStorageFee){
		this.sku = sku;
		this.productName = productName;
		this.itemVolume = itemVolume;
		this.volumeUnit = volumeUnit;
		this.totalAverageQuantityOnHand = totalAverageQuantityOnHand;
		this.totalAverageQuantityPendingRemoval = totalAverageQuantityPendingRemoval;
		this.totalEstimatedTotalItemVolume = totalEstimatedTotalItemVolume;
		this.monthOfCharge = monthOfCharge;
		this.storageRate = storageRate;
		this.currency = currency;
		this.totalEstimatedMonthlyStorageFee = totalEstimatedMonthlyStorageFee;		
	}
	
	@Override
	public String getSku() {
		return this.sku;
	}

	@Override
	public String getProductName() {
		return this.productName;
	}

	@Override
	public String getItemVolume() {
		return this.itemVolume.setScale(5, BigDecimal.ROUND_HALF_UP).toPlainString();
	}

	@Override
	public String getVolumeUnit() {
		return this.volumeUnit;
	}

	@Override
	public String getTotalAverageQuantityOnHand() {
		return this.totalAverageQuantityOnHand.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
	}

	@Override
	public String getTotalAverageQuantityPendingRemoval() {
		return this.totalAverageQuantityPendingRemoval.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
	}

	@Override
	public String getTotalEstimatedTotalItemVolume() {
		return this.totalEstimatedTotalItemVolume.setScale(4, BigDecimal.ROUND_HALF_UP).toPlainString();
	}

	@Override
	public String getMonthOfCharge() {
		return this.monthOfCharge;
	}

	@Override
	public String getStorageRate() {
		return this.storageRate.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
	}

	@Override
	public String getCurrency() {
		return this.currency;
	}

	@Override
	public String getTotalEstimatedMonthlyStorageFee() {
		return this.totalEstimatedMonthlyStorageFee.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
	}

}