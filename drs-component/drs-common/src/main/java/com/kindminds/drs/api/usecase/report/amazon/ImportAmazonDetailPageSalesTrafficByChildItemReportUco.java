package com.kindminds.drs.api.usecase.report.amazon;

import java.util.List;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.v1.model.accounting.SettlementPeriod;
import com.kindminds.drs.api.v1.model.accounting.SettlementPeriodImportingStatus;

public interface ImportAmazonDetailPageSalesTrafficByChildItemReportUco {
	List<Marketplace> getMarketplaces();
	String importFile(String marketplaceId,int periodId,byte[] fileBytes);
	String deleteReportDataByPeriod(String marketplaceId,String periodId);
	List<SettlementPeriod> getUnOccupiedSettlementPeriods(String marketplaceId);
	List<SettlementPeriodImportingStatus> getSettlementPeriodImportingStatuses(String marketplaceId);
}
