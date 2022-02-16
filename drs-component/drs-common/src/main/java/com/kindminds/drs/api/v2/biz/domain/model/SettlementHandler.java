package com.kindminds.drs.api.v2.biz.domain.model;

import java.util.List;
import java.util.Map;

import com.kindminds.drs.Marketplace;

public interface SettlementHandler {
	//void doAllSettlement();
	void doMs2ssSettlement();
	void doSs2spSettlementInDraft();
	List<Marketplace> getAmazonMarketplaces();
	Map<String,List<String>> getAmazonSettlementReportPeriodReadyMarketplaces();
}
