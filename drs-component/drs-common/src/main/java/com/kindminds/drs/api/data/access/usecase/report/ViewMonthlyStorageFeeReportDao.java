package com.kindminds.drs.api.data.access.usecase.report;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ViewMonthlyStorageFeeReportDao {

	public List<Object []> queryItemList(String supplierKcode, String country,int marketplaceId,String year, String month);
	public BigDecimal querySumOfTotalEstimatedMonthlyStorageFee(String supplierKcode, String country,int marketplaceId,String year, String month);
	public List<String> querySupplierKcodeList();
	public Map<String,String> queryMsdcKcodeCountryMap();
	public Date queryLastSettlementEnd();
		
}