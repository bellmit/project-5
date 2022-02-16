package com.kindminds.drs.v1.model.impl.amazon.report;

import com.kindminds.drs.api.v1.model.amazon.AmazonInventoryHealthReportLineItem;

import java.math.BigDecimal;
import java.util.Date;

public class AmazonInventoryHealthReportLineItemImpl implements AmazonInventoryHealthReportLineItem {

	private Date snapshotDate;
	private String sku;
	private String fnsku;
	private String asin;
	private String productName;
	private String condition;
	private Integer salesRank;
	private String productGroup;
	private Integer totalQuantity;
	private Integer sellableQuantity;
	private Integer unsellableQuantity;
	private Integer invAge0To90Days;
	private Integer invAge91To180Days;
	private Integer invAge181To270Days;
	private Integer invAge271To365Days;
	private Integer invAge365PlusDays;
	private Integer unitsShippedLast24Hrs;
	private Integer unitsShippedLast7Days;
	private Integer unitsShippedLast30Days;
	private Integer unitsShippedLast90Days;
	private Integer unitsShippedLast180Days;
	private Integer unitsShippedLast365Days;
	private BigDecimal weeksOfCoverT7;
	private BigDecimal weeksOfCoverT30;
	private BigDecimal weeksOfCoverT90;
	private BigDecimal weeksOfCoverT180;
	private BigDecimal weeksOfCoverT365;
	private Integer numAfnNewSellers;
	private Integer numAfnUsedSellers;
	private String currency;
	private BigDecimal yourPrice;
	private BigDecimal salesPrice;
	private BigDecimal lowestAfnNewPrice;
	private BigDecimal lowestAfnUsedPrice;
	private BigDecimal lowestMfnNewPrice;
	private BigDecimal lowestMfnUsedPrice;
	private Integer qtyToBeChargedLtsf12Mo;
	private Integer qtyInLongTermStorageProgram;
	private Integer qtyWithRemovalsInProgress;
	private BigDecimal projectedLtsf12Mo;
	private BigDecimal perUnitVolume;
	private String isHazmat;
	private Integer inBoundQuantity;
	private String asinLimit;
	private Integer inboundRecommendQuantity;
	private Integer qtyToBeChargedLtsf6Mo;
	private BigDecimal projectedLtsf6Mo;

	public AmazonInventoryHealthReportLineItemImpl(
			Date snapshotDate,
			String sku,
			String fnsku,
			String asin,
			String productName,
			String condition,
			Integer salesRank,
			String productGroup,
			Integer totalQuantity,
			Integer sellableQuantity,
			Integer unsellableQuantity,
			Integer invAge0To90Days,
			Integer invAge91To180Days,
			Integer invAge181To270Days,
			Integer invAge271To365Days,
			Integer invAge365PlusDays,
			Integer unitsShippedLast24Hrs,
			Integer unitsShippedLast7Days,
			Integer unitsShippedLast30Days,
			Integer unitsShippedLast90Days,
			Integer unitsShippedLast180Days,
			Integer unitsShippedLast365Days,
			BigDecimal weeksOfCoverT7,
			BigDecimal weeksOfCoverT30,
			BigDecimal weeksOfCoverT90,
			BigDecimal weeksOfCoverT180,
			BigDecimal weeksOfCoverT365,
			Integer numAfnNewSellers,
			Integer numAfnUsedSellers,
			String currency,
			BigDecimal yourPrice,
			BigDecimal salesPrice,
			BigDecimal lowestAfnNewPrice,
			BigDecimal lowestAfnUsedPrice,
			BigDecimal lowestMfnNewPrice,
			BigDecimal lowestMfnUsedPrice,
			Integer qtyToBeChargedLtsf12Mo,
			Integer qtyInLongTermStorageProgram,
			Integer qtyWithRemovalsInProgress,
			BigDecimal projectedLtsf12Mo,
			BigDecimal perUnitVolume,
			String isHazmat,
			Integer inBoundQuantity,
			String asinLimit,
			Integer inboundRecommendQuantity,
			Integer qtyToBeChargedLtsf6Mo,
			BigDecimal projectedLtsf6Mo){
		this.snapshotDate = snapshotDate;
		this.sku = sku;
		this.fnsku = fnsku;
		this.asin = asin;
		this.productName = productName;
		this.condition = condition;
		this.salesRank = salesRank;
		this.productGroup = productGroup;
		this.totalQuantity = totalQuantity;
		this.sellableQuantity = sellableQuantity;
		this.unsellableQuantity = unsellableQuantity;
		this.invAge0To90Days = invAge0To90Days;
		this.invAge91To180Days = invAge91To180Days;
		this.invAge181To270Days = invAge181To270Days;
		this.invAge271To365Days = invAge271To365Days;
		this.invAge365PlusDays = invAge365PlusDays;
		this.unitsShippedLast24Hrs = unitsShippedLast24Hrs;
		this.unitsShippedLast7Days = unitsShippedLast7Days;
		this.unitsShippedLast30Days = unitsShippedLast30Days;
		this.unitsShippedLast90Days = unitsShippedLast90Days;
		this.unitsShippedLast180Days = unitsShippedLast180Days;
		this.unitsShippedLast365Days = unitsShippedLast365Days;
		this.weeksOfCoverT7 = weeksOfCoverT7;
		this.weeksOfCoverT30 = weeksOfCoverT30;
		this.weeksOfCoverT90 = weeksOfCoverT90;
		this.weeksOfCoverT180 = weeksOfCoverT180;
		this.weeksOfCoverT365 = weeksOfCoverT365;
		this.numAfnNewSellers = numAfnNewSellers;
		this.numAfnUsedSellers = numAfnUsedSellers;
		this.currency = currency;
		this.yourPrice = yourPrice;
		this.salesPrice = salesPrice;
		this.lowestAfnNewPrice = lowestAfnNewPrice;
		this.lowestAfnUsedPrice = lowestAfnUsedPrice;
		this.lowestMfnNewPrice = lowestMfnNewPrice;
		this.lowestMfnUsedPrice = lowestMfnUsedPrice;
		this.qtyToBeChargedLtsf12Mo = qtyToBeChargedLtsf12Mo;
		this.qtyInLongTermStorageProgram = qtyInLongTermStorageProgram;
		this.qtyWithRemovalsInProgress = qtyWithRemovalsInProgress;
		this.projectedLtsf12Mo = projectedLtsf12Mo;
		this.perUnitVolume = perUnitVolume;
		this.isHazmat = isHazmat;
		this.inBoundQuantity = inBoundQuantity;
		this.asinLimit = asinLimit;
		this.inboundRecommendQuantity = inboundRecommendQuantity;
		this.qtyToBeChargedLtsf6Mo = qtyToBeChargedLtsf6Mo;
		this.projectedLtsf6Mo = projectedLtsf6Mo;
	}

	@Override
	public String toString() {
		return "AmazonInventoryHealthReportLineItemImpl [getSnapshotDate()=" + getSnapshotDate() + ", getSku()="
				+ getSku() + ", getFnsku()=" + getFnsku() + ", getAsin()=" + getAsin() + ", getProductName()="
				+ getProductName() + ", getCondition()=" + getCondition() + ", getSalesRank()=" + getSalesRank()
				+ ", getProductGroup()=" + getProductGroup() + ", getTotalQuantity()=" + getTotalQuantity()
				+ ", getSellableQuantity()=" + getSellableQuantity() + ", getUnsellableQuantity()="
				+ getUnsellableQuantity() + ", getInvAge0To90Days()=" + getInvAge0To90Days()
				+ ", getInvAge91To180Days()=" + getInvAge91To180Days() + ", getInvAge181To270Days()="
				+ getInvAge181To270Days() + ", getInvAge271To365Days()=" + getInvAge271To365Days()
				+ ", getInvAge365PlusDays()=" + getInvAge365PlusDays() + ", getUnitsShippedLast24Hrs()="
				+ getUnitsShippedLast24Hrs() + ", getUnitsShippedLast7Days()=" + getUnitsShippedLast7Days()
				+ ", getUnitsShippedLast30Days()=" + getUnitsShippedLast30Days() + ", getUnitsShippedLast90Days()="
				+ getUnitsShippedLast90Days() + ", getUnitsShippedLast180Days()=" + getUnitsShippedLast180Days()
				+ ", getUnitsShippedLast365Days()=" + getUnitsShippedLast365Days() + ", getWeeksOfCoverT7()="
				+ getWeeksOfCoverT7() + ", getWeeksOfCoverT30()=" + getWeeksOfCoverT30() + ", getWeeksOfCoverT90()="
				+ getWeeksOfCoverT90() + ", getWeeksOfCoverT180()=" + getWeeksOfCoverT180() + ", getWeeksOfCoverT365()="
				+ getWeeksOfCoverT365() + ", getNumAfnNewSellers()=" + getNumAfnNewSellers()
				+ ", getNumAfnUsedSellers()=" + getNumAfnUsedSellers() + ", getCurrency()=" + getCurrency()
				+ ", getYourPrice()=" + getYourPrice() + ", getSalesPrice()=" + getSalesPrice()
				+ ", getLowestAfnNewPrice()=" + getLowestAfnNewPrice() + ", getLowestAfnUsedPrice()="
				+ getLowestAfnUsedPrice() + ", getLowestMfnNewPrice()=" + getLowestMfnNewPrice()
				+ ", getLowestMfnUsedPrice()=" + getLowestMfnUsedPrice() + ", getQtyToBeChargedLtsf12Mo()="
				+ getQtyToBeChargedLtsf12Mo() + ", getQtyInLongTermStorageProgram()=" + getQtyInLongTermStorageProgram()
				+ ", getQtyWithRemovalsInProgress()=" + getQtyWithRemovalsInProgress() + ", getProjectedLtsf12Mo()="
				+ getProjectedLtsf12Mo() + ", getPerUnitVolume()=" + getPerUnitVolume() + ", getIsHazmat()="
				+ getIsHazmat() + ", getInBoundQuantity()=" + getInBoundQuantity() + ", getAsinLimit()="
				+ getAsinLimit() + ", getInboundRecommendQuantity()=" + getInboundRecommendQuantity()
				+ ", getQtyToBeChargedLtsf6Mo()=" + getQtyToBeChargedLtsf6Mo() + ", getProjectedLtsf6Mo()="
				+ getProjectedLtsf6Mo() + "]";
	}

	@Override
	public Date getSnapshotDate() {
		return this.snapshotDate;
	}

	@Override
	public String getSku() {
		return this.sku;
	}

	@Override
	public String getFnsku() {
		return this.fnsku;
	}

	@Override
	public String getAsin() {
		return this.asin;
	}

	@Override
	public String getProductName() {
		return this.productName;
	}

	@Override
	public String getCondition() {
		return this.condition;
	}

	@Override
	public Integer getSalesRank() {
		return this.salesRank;
	}

	@Override
	public String getProductGroup() {
		return this.productGroup;
	}

	@Override
	public Integer getTotalQuantity() {
		return this.totalQuantity;
	}

	@Override
	public Integer getSellableQuantity() {
		return this.sellableQuantity;
	}

	@Override
	public Integer getUnsellableQuantity() {
		return this.unsellableQuantity;
	}

	@Override
	public Integer getInvAge0To90Days() {
		return this.invAge0To90Days;
	}

	@Override
	public Integer getInvAge91To180Days() {
		return this.invAge91To180Days;
	}

	@Override
	public Integer getInvAge181To270Days() {
		return this.invAge181To270Days;
	}

	@Override
	public Integer getInvAge271To365Days() {
		return this.invAge271To365Days;
	}

	@Override
	public Integer getInvAge365PlusDays() {
		return this.invAge365PlusDays;
	}

	@Override
	public Integer getUnitsShippedLast24Hrs() {
		return this.unitsShippedLast24Hrs;
	}

	@Override
	public Integer getUnitsShippedLast7Days() {
		return this.unitsShippedLast7Days;
	}

	@Override
	public Integer getUnitsShippedLast30Days() {
		return this.unitsShippedLast30Days;
	}

	@Override
	public Integer getUnitsShippedLast90Days() {
		return this.unitsShippedLast90Days;
	}

	@Override
	public Integer getUnitsShippedLast180Days() {
		return this.unitsShippedLast180Days;
	}

	@Override
	public Integer getUnitsShippedLast365Days() {
		return this.unitsShippedLast365Days;
	}

	@Override
	public BigDecimal getWeeksOfCoverT7() {
		return this.weeksOfCoverT7;
	}

	@Override
	public BigDecimal getWeeksOfCoverT30() {
		return this.weeksOfCoverT30;
	}

	@Override
	public BigDecimal getWeeksOfCoverT90() {
		return this.weeksOfCoverT90;
	}

	@Override
	public BigDecimal getWeeksOfCoverT180() {
		return this.weeksOfCoverT180;
	}

	@Override
	public BigDecimal getWeeksOfCoverT365() {
		return this.weeksOfCoverT365;
	}

	@Override
	public Integer getNumAfnNewSellers() {
		return this.numAfnNewSellers;
	}

	@Override
	public Integer getNumAfnUsedSellers() {
		return this.numAfnUsedSellers;
	}

	@Override
	public String getCurrency() {
		return this.currency;
	}

	@Override
	public BigDecimal getYourPrice() {
		return this.yourPrice;
	}

	@Override
	public BigDecimal getSalesPrice() {
		return this.salesPrice;
	}

	@Override
	public BigDecimal getLowestAfnNewPrice() {
		return this.lowestAfnNewPrice;
	}

	@Override
	public BigDecimal getLowestAfnUsedPrice() {
		return this.lowestAfnUsedPrice;
	}

	@Override
	public BigDecimal getLowestMfnNewPrice() {
		return this.lowestMfnNewPrice;
	}

	@Override
	public BigDecimal getLowestMfnUsedPrice() {
		return this.lowestMfnUsedPrice;
	}

	@Override
	public Integer getQtyToBeChargedLtsf12Mo() {
		return this.qtyToBeChargedLtsf12Mo;
	}

	@Override
	public Integer getQtyInLongTermStorageProgram() {
		return this.qtyInLongTermStorageProgram;
	}

	@Override
	public Integer getQtyWithRemovalsInProgress() {
		return this.qtyWithRemovalsInProgress;
	}

	@Override
	public BigDecimal getProjectedLtsf12Mo() {
		return this.projectedLtsf12Mo;
	}

	@Override
	public BigDecimal getPerUnitVolume() {
		return this.perUnitVolume;
	}

	@Override
	public String getIsHazmat() {
		return this.isHazmat;
	}

	@Override
	public Integer getInBoundQuantity() {
		return this.inBoundQuantity;
	}

	@Override
	public String getAsinLimit() {
		return this.asinLimit;
	}

	@Override
	public Integer getInboundRecommendQuantity() {
		return this.inboundRecommendQuantity;
	}

	@Override
	public Integer getQtyToBeChargedLtsf6Mo() {
		return this.qtyToBeChargedLtsf6Mo;
	}

	@Override
	public BigDecimal getProjectedLtsf6Mo() {
		return this.projectedLtsf6Mo;
	}

}
