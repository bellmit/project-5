package com.kindminds.drs.v1.model.impl.report;

import com.kindminds.drs.api.v1.model.report.DateRangeImportStatus;

import java.io.Serializable;
import java.util.Date;

public class DateRangeImportStatusImpl implements DateRangeImportStatus, Serializable {
	private String marketplaceId;
	private Date importDate;
	private Date dateTime;
	
	public DateRangeImportStatusImpl(String marketplaceId, Date importDate, Date dateTime) {
		this.marketplaceId = marketplaceId;
		this.importDate = importDate;
		this.dateTime = dateTime;
	}
	
	@Override
	public String getMarketplaceId() {
		return marketplaceId;
	}
	
	@Override
	public Date getImportDate() {
		return importDate;
	}
	
	@Override
	public Date getDateTime() {
		return dateTime;
	}
}
