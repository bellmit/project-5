package com.kindminds.drs.v1.model.impl.factory;

import com.kindminds.drs.*;
import com.kindminds.drs.api.v1.model.report.*;
import com.kindminds.drs.v1.model.impl.statement.*;

import com.kindminds.drs.enums.BillStatementType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

@Component
public class Ss2spStatementReportFactoryV3 extends Ss2spStatementReportFactory {

	@Override
	protected ReportVersion getReportVersion() {
		return ReportVersion.V3;
	}

	@Override
	public Ss2spStatementReport createSs2spStatementReport(BillStatementType type, String statementName) {
		Ss2spStatementReportImplV2 report = new Ss2spStatementReportImplV2();
		report.setInfo(this.getInfo(type,statementName));

		report.setForeignStatementItems(this.queryStatementItemProfitShareV3(type,statementName));
		report.setShipmentRelatedItems(this.queryShipmentRelatedItems(type,statementName));

		List<Object []> resultObjectArrayList = this.dao.queryStatementItemsSellBackRelated(type,statementName);
		List<Ss2spStatementReport.Ss2spStatementItemSellBackRelated> listToReturn = new ArrayList<Ss2spStatementReport.Ss2spStatementItemSellBackRelated>();
		Ss2spSellBackReport sellBackReport = this.createSellBackReport(type,statementName);
		for(Object[] columns:resultObjectArrayList){
			Integer currencyId = (Integer)columns[0];
			BigDecimal amount = (BigDecimal)columns[1];

			if(sellBackReport.getIncludedTax()) {
				amount = new BigDecimal(sellBackReport.getTotalWithTax());
			}

			String invoiceNumber = (String)columns[2];
			listToReturn.add(new Ss2spStatementItemSellBackRelatedImpl(currencyId,amount,invoiceNumber));
		}

		report.setSellBackRelatedItems(listToReturn);

		report.setServiceExpenseItem(this.dao.queryStatementItemServiceExpense(type,statementName));
		report.setPreviousBalance(this.dao.queryPreviousBalance(type,statementName));
		report.setRemittanceIsurToRcvr(this.dao.queryRemittanceIsurToRcvr(type,statementName));
		report.setRemittanceRcvrToIsur(this.dao.queryRemittanceRcvrToIsur(type,statementName));
		report.setBalance(this.dao.queryBalance(type,statementName));

		report.setCurrency(this.dao.queryStatementCurrency(type,statementName));
		report.setProfiShareAmountTax(this.dao.queryProfitShareTaxV2(type,statementName));


		return report;
	}


	private List<Ss2spStatementReport.Ss2spStatementItemProfitShare> queryStatementItemProfitShareV3(BillStatementType type, String statementName) {
		List<Ss2spStatementReport.Ss2spStatementItemProfitShare> resultList = new ArrayList<Ss2spStatementReport.Ss2spStatementItemProfitShare>();
		if(this.dao.queryProfitShareItemCounts(type,statementName)>0){
			Ss2spStatementItemProfitShareImplV3 item = new Ss2spStatementItemProfitShareImplV3();
			item.setCurrency(this.dao.queryProfitShareStatementCurrency(type,statementName));
			item.setAmountUntaxed(this.dao.querySumOfProfitShareStatementAmountUntaxed(type,statementName));
			item.setAmountTax(this.dao.queryProfitShareTaxV3(type,statementName));
			resultList.add(item);
		}
		return resultList;
	}



	@Override
	public Ss2spProfitShareReport createSs2spProfitShareReport(BillStatementType type, String statementName) {
		Ss2spProfitShareReportImplV3 report = new Ss2spProfitShareReportImplV3();
		report.setInfo(this.getInfo(type,statementName));
		report.setStatementCurrency(this.dao.queryStatementCurrency(type,statementName));

		List<Object []> columnsList = this.dao.queryProfitShareReportLineItemV3(type,statementName);
		List<Ss2spProfitShareReport.Ss2spProfitShareReportLineItem> resultItems = new ArrayList<>();
		for(Object[] columns:columnsList){
			BigDecimal revenueInUsd = (BigDecimal)columns[0];
			BigDecimal effectiveRetainmentRate = (BigDecimal)columns[1];
			Integer sourceCountryId = (Integer)columns[2];
			Integer origCurrencyId = (Integer)columns[3];
			BigDecimal sourceAmount = (BigDecimal)columns[4];
			Integer stmtCurrencyId = (Integer)columns[5];
			BigDecimal statementAmount = (BigDecimal)columns[6];
			BigDecimal exchangeRate = (BigDecimal)columns[7];
			resultItems.add(new Ss2spProfitShareReportLineItemImpl(revenueInUsd,effectiveRetainmentRate,sourceCountryId,origCurrencyId,sourceAmount,stmtCurrencyId,statementAmount,exchangeRate));
		}

		if (report.getRcvrKcode().equals("K618")) {
			BigDecimal totalRevenue = BigDecimal.ZERO;
			for (Object[] columns:columnsList) {
				if(columns[0] != null){
					totalRevenue = totalRevenue.add((BigDecimal)columns[0]);
				}
			}
			BigDecimal achievedRetainmentRate = RevenueGrade.getGrade(totalRevenue).getRetainmentRate();
			for (Ss2spProfitShareReport.Ss2spProfitShareReportLineItem lineItem : resultItems) {
				lineItem.setAchievedRetainmentRateGrade(achievedRetainmentRate);
			}
		} else if(report.getRcvrKcode().equals("K652")) {
			BigDecimal totalRevenue = BigDecimal.ZERO;
			for (Object[] columns:columnsList) {
				if(columns[0] != null){
					totalRevenue = totalRevenue.add((BigDecimal)columns[0]);
				}
			}
			BigDecimal achievedRetainmentRate = RevenueGrade2022.getGrade(totalRevenue).getRetainmentRate();
			for (Ss2spProfitShareReport.Ss2spProfitShareReportLineItem lineItem : resultItems) {
				lineItem.setAchievedRetainmentRateGrade(achievedRetainmentRate);
			}
		}

		report.setItems(resultItems);
		report.setAmountTax(this.dao.queryProfitShareTaxV2(type,statementName));
		return report;
	}

	@Override
	public Ss2spProfitShareDetailReport createSs2spProfitShareDetailReport(BillStatementType type, String statementName, String countryStr) {
		Country country = Country.valueOf(countryStr);
		Ss2spProfitShareDetailReportImplV1 report = new Ss2spProfitShareDetailReportImplV1();

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


		report.setInfo(this.getInfo(type,statementName));
		report.setCurrency(country.getCurrency());
		this.setNonRetailItemAmounts(report,this.dao.queryProfitShareItemsAmountExcludedRetailAndInternational(type,statementName,country,this.profitShareItemExcludedRetailAndInternational));
		this.setNonRetailItemAmounts(report,this.dao.queryInternationalTransactionItems(type,statementName,country));
		return report;
	}
	
	private void setNonRetailItemAmounts(Ss2spProfitShareDetailReportImplV1 report, List<Entry<String,BigDecimal>> newItemAmountList){
		if(newItemAmountList.size()==0) return;
		if(report.getRawOtherItemAmounts()==null) report.setOtherItemAmounts(new LinkedHashMap<>());
		for(Entry<String,BigDecimal> newItemAmount:newItemAmountList){
			report.getRawOtherItemAmounts().put(newItemAmount.getKey(),newItemAmount.getValue());
		}
	}

	@Override
	public Ss2spSettleableItemReport createSs2spSettleableItemReportForProfitShare(BillStatementType type, String statementName, String countryCode, String sku, String itemName) {
		Country country = Country.valueOf(countryCode);
		Ss2spSettleableItemReportImplV3ForProfitShare report = new Ss2spSettleableItemReportImplV3ForProfitShare();
		report.setSettleableItem(TransactionLineType.fromName(itemName));
		report.setCurrency(country.getCurrency());
		report.setInfo(this.getInfo(type,statementName));
		String transactionLineTypeClass = this.dao.queryClassOfTransactionLineType(itemName);
		if(itemName.equals(TransactionLineType.MS2SS_PURCHASE_ALWNC_IMPORT_DUTY.getName())){// import duty transaction
			report.setLineItems(this.dao.querySettleableItemReportLineItemImportDutyRelated(type,statementName,country,sku));
		}else if(transactionLineTypeClass.equals("MSI")||transactionLineTypeClass.equals("SSI")){
			report.setLineItems(this.dao.querySettleableItemReportLineItemProfitShareSettleableRelated(type,statementName, country, sku, itemName));
		}
		else{
			report.setLineItems(this.dao.querySettleableItemReportLineItem(type,statementName, country, sku, itemName));
		}
		return report;
	}

	public Ss2spSellBackReport createSellBackReport(BillStatementType type, String statementName) {

		Ss2spSellBackReportImpl report = new Ss2spSellBackReportImpl();
		report.setTitle("ss2spStatement.SellBack");
		report.setInfo(this.getInfo(type,statementName));

		List<Object []> resultColumnArrayList = this.dao.querySellBackReportItems(type,statementName);
		List<Ss2spSellBackReport.Ss2spSellBackReportLineItem> listToReturn = new ArrayList<Ss2spSellBackReport.Ss2spSellBackReportLineItem>();
		Date periodEnd = null;
		for(Object[] columns:resultColumnArrayList){
			String ivsName = (String)columns[0];
			String sku = (String)columns[1];
			String skuName = (String)columns[2];
			String statementLineType = (String)columns[3];
			Integer origCurrencyId = (Integer)columns[4];
			Integer stmtCurrencyId = (Integer)columns[5];
			BigDecimal origAmnt = (BigDecimal)columns[6];
			BigDecimal stmtAmnt = (BigDecimal)columns[7];
			Integer qty = (Integer)columns[8];
			periodEnd = (Date)columns[9];

			listToReturn.add(new Ss2spSellBackReportLineItemImpl(ivsName,sku,skuName,statementLineType,origCurrencyId,stmtCurrencyId,origAmnt,stmtAmnt, qty));
		}

		report.setIncludedTax(periodEnd);
		report.setLineItems(listToReturn);

		report.setCurrency(this.dao.queryStatementCurrency(type,statementName));

		return report;
	}

	@Override
	public Ss2spSellBackReport createSellBackReport(BillStatementType type, String statementName, Currency currency) {
		Ss2spSellBackReportImpl report = new Ss2spSellBackReportImpl();
		report.setTitle("ss2spStatement.SellBack");
		report.setInfo(this.getInfo(type,statementName));

		List<Object []> resultColumnArrayList = this.dao.querySellBackReportItems(type,statementName);
		List<Ss2spSellBackReport.Ss2spSellBackReportLineItem> listToReturn = new ArrayList<Ss2spSellBackReport.Ss2spSellBackReportLineItem>();
		Date periodEnd = null;
		for(Object[] columns:resultColumnArrayList){
			String ivsName = (String)columns[0];
			String sku = (String)columns[1];
			String skuName = (String)columns[2];
			String statementLineType = (String)columns[3];
			Integer origCurrencyId = (Integer)columns[4];
			Integer stmtCurrencyId = (Integer)columns[5];
			BigDecimal origAmnt = (BigDecimal)columns[6];
			BigDecimal stmtAmnt = (BigDecimal)columns[7];

			Integer qty = (Integer)columns[8];
			periodEnd = (Date)columns[9];
			listToReturn.add(new Ss2spSellBackReportLineItemImpl(ivsName,sku,skuName,statementLineType,origCurrencyId,stmtCurrencyId,origAmnt,stmtAmnt, qty));
		}

		report.setIncludedTax(periodEnd);
		report.setLineItems(listToReturn);

		report.setCurrency(currency);

		return report;
	}

}
