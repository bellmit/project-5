package com.kindminds.drs.api.data.access.usecase.product;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.kindminds.drs.Country;
import com.kindminds.drs.Marketplace;


import com.kindminds.drs.api.v1.model.report.KeyProductStatsReport;
import com.kindminds.drs.api.v1.model.report.KeyProductStatsReport.KeyProductStatsReportLineItem;
import com.kindminds.drs.enums.DisplayOption;


public interface ViewKeyProductStatsDao {
	public interface FbaQuantities{
		Integer getInBound();
		Integer getInStock();
		Integer getTransfer();
	}
	Date queryLatestStatementPeriodEnd(String supplierKcode);
	String queryLatestStatementPeriodEndDateStr(String supplierKcode);
	String queryLatestStatementPeriodEndDateUtc(String supplierKcode);
	String queryNextStatementPeriodStartDateUtc(String supplierKcode);
	String queryAmazonOrderLastUpdateDate(Country country);
	List<String> queryProductBaseIdList(String supplierKcode);
	List<KeyProductStatsReportLineItem> queryLineItems(List<Integer> marketplaceIds, String supplierKcode, DisplayOption option);
    Map<String,Integer> queryAmazonQtyOrdered(Marketplace marketplace, String supplierKcode, Date purchaseDateFrom, Date purchaseDateTo);
	Map<String,Integer> queryShopifyQtyOrdered(Marketplace marketplace,String supplierKcode,Date createDateFrom,Date createDateTo);
	Map<String,Integer> queryQtyToReceive(Country country,String supplierKcode);
	Map<String,Integer> queryQtyAtDrsSettlementEndPlusQtyReceivedInCurrentSettlement(Country country,String supplierKcode);
	List<Object []> querySkuFbaQuantities(Marketplace marketplace);


	void save(KeyProductStatsReport rpt);

}
