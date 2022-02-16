package com.kindminds.drs.v1.model.impl.amazon;

import com.kindminds.drs.Currency;
import com.kindminds.drs.Marketplace;
import com.kindminds.drs.util.DateHelper;
import com.kindminds.drs.api.v1.model.accounting.AmazonSettlementReportInfo;

import java.util.Date;

public class AmazonSettlementReportInfoImpl implements AmazonSettlementReportInfo {

	private Date dateStart;
	private Date dateEnd;
	private String settlementId;
	private String currencyName;
	private Integer sourceMarketplaceId;
	
	public AmazonSettlementReportInfoImpl(
			Date dateStart,
			Date dateEnd,
			String settlementId,
			String currencyName,
			Integer sourceMarketplaceId) {
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		this.settlementId = settlementId;
		this.currencyName = currencyName;
		this.sourceMarketplaceId = sourceMarketplaceId;
	}
	
	@Override
	public String toString() {
		return "AmazonSettlementReportInfoImpl [getDateStart()=" + getDateStart() + ", getDateEnd()=" + getDateEnd()
				+ ", getSettlementId()=" + getSettlementId() + ", getDateDeposit()=" + getDateDeposit()
				+ ", getCurrency()=" + getCurrency() + ", getTotalAmount()=" + getTotalAmount() + ", getMarketplace()="
				+ getSourceMarketplace() + "]";
	}

	@Override
	public String getDateStart() {
		return DateHelper.toString(this.dateStart,"yyyy-MM-dd","UTC");
	}

	@Override
	public String getDateEnd() {
		return DateHelper.toString(this.dateEnd,"yyyy-MM-dd","UTC");
	}

	@Override
	public String getSettlementId() {
		return this.settlementId;
	}

	@Override
	public String getDateDeposit() {
		return null;
	}

	@Override
	public String getCurrency() {
		return Currency.valueOf(this.currencyName).name();
	}

	@Override
	public String getTotalAmount() {
		return null;
	}

	@Override
	public Marketplace getSourceMarketplace() {
		return Marketplace.fromKey(this.sourceMarketplaceId);
	}

}
