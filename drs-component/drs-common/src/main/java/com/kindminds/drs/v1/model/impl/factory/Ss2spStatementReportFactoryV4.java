package com.kindminds.drs.v1.model.impl.factory;

import com.kindminds.drs.Country;
import com.kindminds.drs.v1.model.impl.statement.Ss2spProfitShareDetailReportSkuProfitShareItemImpl;
import com.kindminds.drs.api.v1.model.report.Ss2spProfitShareDetailReport;
import com.kindminds.drs.enums.BillStatementType;
import com.kindminds.drs.v1.model.impl.statement.Ss2spProfitShareDetailReportImplV4;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

@Component
public class Ss2spStatementReportFactoryV4 extends Ss2spStatementReportFactoryV3 {
	
	@Override
	protected ReportVersion getReportVersion() {
		return ReportVersion.V4;
	}

	@Override
	public Ss2spProfitShareDetailReport createSs2spProfitShareDetailReport(BillStatementType type,String statementName, String countryCode) {
		Country country = Country.valueOf(countryCode);
		Ss2spProfitShareDetailReportImplV4 report = new Ss2spProfitShareDetailReportImplV4();
		report.setInfo(this.getInfo(type,statementName));
		report.setCurrency(country.getCurrency());

		List<Object []> columnsList = this.dao.querySkuProfitShareItems(type,statementName, country, this.countryProfitShareSkuRelatedTypes);
		List<Ss2spProfitShareDetailReport.Ss2spProfitShareDetailReportSkuProfitShareItem> resultItems = new ArrayList<>();
		for(Object[] columns:columnsList){
			String sku = (String)columns[0];
			String name = (String)columns[1];
			Integer shippedQty = BigInteger.valueOf(Long.parseLong(columns[2].toString())).intValue();
			Integer refundedQty = BigInteger.valueOf(Long.parseLong(columns[3].toString())).intValue();
			BigDecimal subtotal = (BigDecimal)columns[4];
			resultItems.add(new Ss2spProfitShareDetailReportSkuProfitShareItemImpl(sku,name,shippedQty,refundedQty,subtotal));
		}

		report.setSkuProfitShareItems(resultItems);

		this.setNonRetailItemAmounts(report,this.dao.queryProfitShareItemsAmountExcludedRetailAndInternational(type,statementName,country,this.profitShareItemExcludedRetailAndInternational));
		this.setNonRetailItemAmounts(report,this.dao.queryIvsForImportDutyTransaction(type,statementName,country));
		this.setNonRetailItemAmounts(report,this.dao.queryInternationalTransactionItems(type,statementName,country));
		return report;
	}
	
	private void setNonRetailItemAmounts(Ss2spProfitShareDetailReportImplV4 report, List<Entry<String,BigDecimal>> newItemAmountList){
		if(newItemAmountList.size()==0) return;
		if(report.getRawOtherItemAmounts()==null) report.setOtherItemAmounts(new LinkedHashMap<>());
		for(Entry<String,BigDecimal> newItemAmount:newItemAmountList){
			report.getRawOtherItemAmounts().put(newItemAmount.getKey(),newItemAmount.getValue());
		}
	}

}
