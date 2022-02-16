package com.kindminds.drs.v1.model.impl.amazon;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.v1.model.accounting.AmazonSettlementReportInfo;

public class AmazonSettlementReportInfoImplForFile implements AmazonSettlementReportInfo {

	private String settlementId=null;
	private String dateTimeStart=null;
	private String dateTimeEnd=null;
	private String dateTimeDeposit=null;
	private String totalAmount=null;
	private String currency=null;
	private Marketplace sourceMarketplace;
	
	public AmazonSettlementReportInfoImplForFile(
			String settlementId,
			String dateTimeStart,
			String dateTimeEnd,
			String dateTimeDeposit,
			String totalAmount,
			String currency,
			Marketplace sourceMarketplace){
		this.settlementId = settlementId;
		this.dateTimeStart = dateTimeStart;
		this.dateTimeEnd = dateTimeEnd;
		this.dateTimeDeposit = dateTimeDeposit;
		this.totalAmount = totalAmount;
		this.currency = currency;
		this.sourceMarketplace=sourceMarketplace;
	}
	
	@Override
	public String getSettlementId() {
		return this.settlementId;
	}

	@Override
	public String getDateStart() {
		return this.dateTimeStart;
	}

	@Override
	public String getDateEnd() {
		return this.dateTimeEnd;
	}

	@Override
	public String getDateDeposit() {
		return this.dateTimeDeposit;
	}

	@Override
	public String getTotalAmount() {
		return this.totalAmount;
	}

	@Override
	public String getCurrency() {
		return this.currency;
	}

	@Override
	public Marketplace getSourceMarketplace() {
		return this.sourceMarketplace;
	}

}
