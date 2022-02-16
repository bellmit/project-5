package com.kindminds.drs.v1.model.impl.factory;

import com.kindminds.drs.Country;
import com.kindminds.drs.Currency;
import com.kindminds.drs.TransactionLineType;
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
public class Ss2spStatementReportFactoryV1 extends Ss2spStatementReportFactory {

	@Override
	protected ReportVersion getReportVersion() {
		return ReportVersion.V1;
	}


	protected StatementInfoImpl getInfo(BillStatementType type , String statementName){

		Object [] columns = this.dao.queryInfo(type,statementName);

		Date start = (Date)columns[0];
		Date end = (Date)columns[1];
		String isurKcode = (String)columns[2];
		String rcvrKcode = (String)columns[3];
		return new StatementInfoImpl(start, end, isurKcode, rcvrKcode);
	}

	@Override
	public Ss2spSellBackReport createSellBackReport(BillStatementType type, String statementName, Currency currency) {
		return null;
	}

	@Override
	public Ss2spStatementReport createSs2spStatementReport(BillStatementType type, String statementName) {
		Ss2spStatementReportImplV1 report = new Ss2spStatementReportImplV1();
		report.setInfo(this.getInfo(type,statementName));
		report.setForeignStatementItems(this.dao.queryStatementItemProfitShareV2(type,statementName));
		report.setShipmentRelatedItems(this.queryShipmentRelatedItems(type,statementName));
		report.setServiceExpenseItem(this.dao.queryStatementItemServiceExpense(type,statementName));
		report.setPreviousBalance(this.dao.queryPreviousBalance(type,statementName));
		report.setRemittanceIsurToRcvr(this.dao.queryRemittanceIsurToRcvr(type,statementName));
		report.setRemittanceRcvrToIsur(this.dao.queryRemittanceRcvrToIsur(type,statementName));
		report.setBalance(this.dao.queryBalance(type,statementName));
		report.setCurrency(this.dao.queryStatementCurrency(type,statementName));
		return report;
	}

	@Override
	public Ss2spProfitShareReport createSs2spProfitShareReport(BillStatementType type, String statementName) {
		Ss2spProfitShareReportImplV1V2 report = new Ss2spProfitShareReportImplV1V2();
		report.setInfo(this.getInfo(type,statementName));
		report.setStatementCurrency(this.dao.queryStatementCurrency(type,statementName));

		List<Object []> columnsList = this.dao.queryProfitShareReportLineItemV1(type,statementName);
		List<Ss2spProfitShareReport.Ss2spProfitShareReportLineItem> resultItems = new ArrayList<>();
		for(Object[] columns:columnsList){
			Integer sourceCountryId = (Integer)columns[0];
			Integer origCurrencyId = (Integer)columns[1];
			BigDecimal sourceAmount = (BigDecimal)columns[2];
			Integer stmtCurrencyId = (Integer)columns[3];
			BigDecimal statementAmount = (BigDecimal)columns[4];
			resultItems.add(new Ss2spProfitShareReportLineItemImpl(sourceCountryId,origCurrencyId,sourceAmount,stmtCurrencyId,statementAmount));
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

		report.setCurrency(country.getCurrency());
		report.setInfo(this.getInfo(type,statementName));
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
		Ss2spSettleableItemReportImplV1 report = new Ss2spSettleableItemReportImplV1();
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

}
