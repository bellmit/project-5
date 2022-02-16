package com.kindminds.drs.v1.model.impl;

import com.kindminds.drs.api.v1.model.accounting.SettlementPeriod;
import com.kindminds.drs.api.v1.model.accounting.SettlementPeriodImportingStatus;

public class SettlementPeriodImportingStatusImpl implements SettlementPeriodImportingStatus {
	
	private SettlementPeriod period;
	private Boolean isImported;
	
	public SettlementPeriodImportingStatusImpl(SettlementPeriod period, Boolean isImported) {
		this.period = period;
		this.isImported = isImported;
	}

	@Override
	public String toString() {
		return "SettlementPeriodImportingStatusImpl [getPeriod()=" + getPeriod() + ", getIsImported()="
				+ getIsImported() + "]";
	}

	@Override
	public SettlementPeriod getPeriod() {
		return this.period;
	}

	@Override
	public Boolean getIsImported() {
		return this.isImported;
	}

}
