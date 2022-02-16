package com.kindminds.drs.v1.model.impl.factory;

import com.kindminds.drs.Currency;
import com.kindminds.drs.TransactionLineType;

import com.kindminds.drs.api.data.access.usecase.accounting.ViewSs2spStatementDao;
import com.kindminds.drs.api.v1.model.report.*;
import com.kindminds.drs.enums.BillStatementType;
import com.kindminds.drs.v1.model.impl.statement.Ss2spPaymentAndRefundItemImpl;
import com.kindminds.drs.v1.model.impl.RemittanceReportImpl;

import com.kindminds.drs.v1.model.impl.statement.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public abstract class Ss2spStatementReportFactory {
	
	public enum ReportVersion {
		V1(1,"ss2spStatementReportFactoryV1"),
		V2(2,"ss2spStatementReportFactoryV2"),
		V3(3,"ss2spStatementReportFactoryV3"),
		V4(4,"ss2spStatementReportFactoryV4");
		private int versionNumber;
		private String factoryBeanId;
		ReportVersion(int versionNum,String factoryBeanId){
			this.versionNumber = versionNum;
			this.factoryBeanId = factoryBeanId;
		}
		public String getFactoryBeanId(){ return this.factoryBeanId; }
		public static ReportVersion fromVersionNumber(int n){
			for(ReportVersion reportVersion: ReportVersion.values()){
				if(reportVersion.versionNumber==n) return reportVersion;
			}
			Assert.isTrue(false);
			return null;
		}
	}
	
	@Autowired protected ViewSs2spStatementDao dao;
	
	protected final List<TransactionLineType> countryProfitShareSkuRelatedTypes = Arrays.asList(
			TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_ADDITION,
			TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_SUBTRACTION,
			TransactionLineType.SS2SP_UNIT_PROFIT_SHARE_SUBTRACTION_REFUND_FEE
			);
	
	protected final List<String> profitShareItemExcludedRetailAndInternational = Arrays.asList(
			"MS2SS_PURCHASE_ALWNC_CUSTOMERCARE",
			"MS2SS_PURCHASE_ALWNC_CUSTOMERCARE_EXEMPTION"
			);
	
	protected abstract ReportVersion getReportVersion();
	
	public abstract Ss2spStatementReport createSs2spStatementReport(BillStatementType type, String statementName);
	
	protected List<Ss2spStatementReport.Ss2spStatementItemShipmentRelated> queryShipmentRelatedItems(BillStatementType type, String statementName){
		List<Ss2spStatementReport.Ss2spStatementItemShipmentRelated> domesticItems = new ArrayList<Ss2spStatementReport.Ss2spStatementItemShipmentRelated>();
		domesticItems.addAll(this.dao.queryStatementItemsShipmentRelated(type,statementName));
		return domesticItems;
	}
	
	public abstract Ss2spProfitShareReport createSs2spProfitShareReport(BillStatementType type, String statementName);
	
	public abstract Ss2spProfitShareDetailReport createSs2spProfitShareDetailReport(BillStatementType type, String statementName, String country);
	
	public abstract Ss2spSettleableItemReport createSs2spSettleableItemReportForProfitShare(BillStatementType type, String statementName, String country, String sku, String itemName);
	
	public Ss2spPaymentAndRefundReport createSs2spPaymentAndRefundReport(BillStatementType type, String statementName, String sourceShipmentName) {
		List<Object []> columnsList = this.dao.queryPaymentAndRefundReportItems(type,statementName, sourceShipmentName);
		List<Ss2spPaymentAndRefundReport.Ss2spPaymentAndRefundReportItem> lineItems = new ArrayList<>();
		for(Object[] columns:columnsList){
			String sku = (String)columns[0];
			String skuName = (String)columns[1];
			String statementLineType = (String)columns[2];
			Integer quantity = Integer.parseInt(columns[3].toString());
			Integer currencyId = (Integer)columns[4];
			BigDecimal amount = (BigDecimal)columns[5];
			Integer tltId = (Integer)columns[6];
			lineItems.add(new Ss2spPaymentAndRefundItemImpl(sku,skuName,statementLineType,quantity,amount,currencyId,tltId));
		}


		for(Ss2spPaymentAndRefundReport.Ss2spPaymentAndRefundReportItem item:lineItems){
			((Ss2spPaymentAndRefundItemImpl)item).setUnitAmount(this.dao.queryPaymentAndRefundUnitAmount(type,statementName, sourceShipmentName, item.getSku(),item.getSettleableItemId()));;
		}
		Ss2spPaymentAndRefundReportImpl report = new Ss2spPaymentAndRefundReportImpl();
		report.setTitle("ss2spStatement.PaymentAndRefund");
		report.setInfo(this.getInfo(type,statementName));
		report.setLineItems(lineItems);
		report.setCurrency(this.dao.queryStatementCurrency(type,statementName));
		return report;
	}

	protected StatementInfoImpl getInfo(BillStatementType type , String statementName){

		Object [] columns = this.dao.queryInfo(type,statementName);

		Date start = (Date)columns[0];
		Date end = (Date)columns[1];
		String isurKcode = (String)columns[2];
		String rcvrKcode = (String)columns[3];
		return new StatementInfoImpl(start, end, isurKcode, rcvrKcode);
	}


	
	public Ss2spSettleableItemReport createSs2spSettleableItemReportForPoRelated(BillStatementType type,String statementName, String sourcePoId,String sku, int settleableItemId) {
		Ss2spSettleableItemReportImplV1 report = new Ss2spSettleableItemReportImplV1();
		report.setCurrency(this.dao.queryStatementCurrency(type,statementName));
		report.setSettleableItem(this.dao.querySettleableItem(settleableItemId));
		report.setInfo(this.getInfo(type,statementName));
		report.setLineItems(this.dao.querySettleableItemReportLineItem(type,statementName, sourcePoId, sku, settleableItemId));
		return report;
	}
	
	public Ss2spSettleableItemReport createSettleableItemReportForSellBack(BillStatementType type,String statementName, String sourcePoId,String sku, int settleableItemId) {
		Ss2spSettleableItemReportImplV1 report = new Ss2spSettleableItemReportImplV1();
		report.setSettleableItem(this.dao.querySettleableItem(settleableItemId));
		report.setInfo(this.getInfo(type,statementName));
		report.setLineItems(this.dao.querySettleableItemReportLineItem(type,statementName, sourcePoId, sku, settleableItemId));
		return report;
	}
	
	public Ss2spCustomerCareCostReport createSs2spCustomerCareCostReport(BillStatementType type,String statementName,String country){
		Ss2spCustomerCareCostReportImpl report = new Ss2spCustomerCareCostReportImpl();
		report.setInfo(this.getInfo(type,statementName));
		report.setItems(this.dao.querySs2spCustomerCareCostReportItemByProdcutBase(type,statementName, country));
		return report;
	}
	
	public Ss2spSellBackReport createSellBackReport(BillStatementType type,String statementName) {
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
			listToReturn.add(new Ss2spSellBackReportLineItemImpl(ivsName,sku,skuName,statementLineType,
					origCurrencyId,stmtCurrencyId,origAmnt,stmtAmnt, qty));
		}

		report.setIncludedTax(periodEnd);
		report.setLineItems(listToReturn);
		report.setCurrency(this.dao.queryStatementCurrency(type,statementName));
		return report;
	}
	
	public Ss2spServiceExpenseReport createServiceExpenseReport(BillStatementType type,String statementName, String domesticTransactionInvoice) {
		Ss2spServiceExpenseReportImpl report = new Ss2spServiceExpenseReportImpl();
		report.setInfo(this.getInfo(type,statementName));
		report.setCurrency(this.dao.queryStatementCurrency(type,statementName));
		report.setTaxRate(this.dao.queryServiceExpenseTaxRate(domesticTransactionInvoice));
		report.setItems(this.dao.queryServiceExpenseReportItems(domesticTransactionInvoice));
		return report;
	}

	public Ss2spServiceExpenseReport createAllServiceExpenseReport(BillStatementType type,String statementName) {
		Ss2spServiceExpenseReportImpl report = new Ss2spServiceExpenseReportImpl();
		report.setInfo(this.getInfo(type,statementName));
		report.setCurrency(this.dao.queryStatementCurrency(type,statementName));
		report.setTaxRate(this.dao.queryAllServiceExpenseTaxRate(statementName));
		report.setItems(this.dao.queryAllServiceExpenseReportItems(statementName));
		return report;
	}

	public RemittanceReport createRemittanceIsurToRcvrReport(BillStatementType type,String statementName) {
		RemittanceReportImpl report = new RemittanceReportImpl();
		report.setInfo(this.getInfo(type,statementName));
		report.setItems(this.dao.queryRemittanceReportItemsIsurToRcvr(type,statementName));
		
		return report;
	}

	public RemittanceReport createRemittanceRcvrToIsurReport(BillStatementType type,String statementName) {
		RemittanceReportImpl report = new RemittanceReportImpl();
		report.setInfo(this.getInfo(type,statementName));
		report.setItems(this.dao.queryRemittanceReportItemsRcvrToIsur(type,statementName));
		return report;
	}

	public abstract  Ss2spSellBackReport createSellBackReport(BillStatementType type, String statementName , Currency currency) ;
}
