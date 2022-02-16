package com.kindminds.drs.api.usecase.report.amazon;

import java.util.List;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.v1.model.accounting.AmazonSettlementReportInfo;
import com.kindminds.drs.api.v1.model.accounting.AmazonSettlementReportRawLine;

public interface ImportAmazonSettlementReportUco {
	public List<Marketplace> getMarketplaces();
	public List<AmazonSettlementReportInfo> getSettlementReportInfoList();
	public String uploadFileAndImport(String marketplaceId,byte[] bytes);
	public String importFile(String marketplaceId,String fullPath);
	public void insertReportInfoForTest(String settlementId,String settlementStartDate,String settlementEndDate,String depositDate,String totalAmount,String currency,Marketplace sourceMarketplace);
	// For Test
	public void saveReportRawLine(Marketplace sourceMarketplace,List<AmazonSettlementReportRawLine> lines);
}
