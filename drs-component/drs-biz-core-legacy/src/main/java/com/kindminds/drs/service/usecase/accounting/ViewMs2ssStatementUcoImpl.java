package com.kindminds.drs.service.usecase.accounting;

import com.kindminds.drs.Context;
import com.kindminds.drs.Currency;
import com.kindminds.drs.TransactionLineType;
import com.kindminds.drs.api.v1.model.report.*;
import com.kindminds.drs.v1.model.impl.statement.*;
import com.kindminds.drs.api.usecase.accounting.ViewMs2ssStatementUco;
import com.kindminds.drs.api.v1.model.report.Ms2ssPurchaseAllowanceReport.Ms2ssPurchaseAllowanceReportRawItem;
import com.kindminds.drs.api.data.access.rdb.CompanyDao;
import com.kindminds.drs.api.data.access.usecase.accounting.ViewMs2ssStatementDao;
import com.kindminds.drs.enums.BillStatementType;
import com.kindminds.drs.v1.model.impl.RemittanceReportImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ViewMs2ssStatementUcoImpl implements ViewMs2ssStatementUco {
	
	@Autowired private ViewMs2ssStatementDao dao;
	@Autowired private CompanyDao companyRepo;
	
	private final String paymentOnbehalfTitle="SSDC payment on behalf of MSDC";
	
	@Override
	public Map<String,String> getDrsCompanyKcodeToEnUsNameMap() {
		return this.companyRepo.queryDrsCompanyKcodeToShortEnUsNameMap();
	}
	
	@Override
	public StatementListReport queryStatementsRcvd(BillStatementType type) {
		String userCompanyKcode = Context.getCurrentUser().getCompanyKcode();
		Assert.isTrue(Context.getCurrentUser().isDrsUser());
		Currency currency = Currency.USD;
		StatementListReportImpl report = new StatementListReportImpl();
		report.setCurrency(currency);
		report.setStatements(toStatementList(this.dao.queryStatementsRcvd(type,userCompanyKcode,currency)));
		return report;
	}

	private List<StatementListReport.StatementListReportItem> toStatementList(List<Object[]> columnsList) {
		List<StatementListReport.StatementListReportItem> resultList = new ArrayList<>();
		for(Object[] columns:columnsList){
			String statementId = (String)columns[0];
			String startDateUtc = (String)columns[1];
			String endDateUtc = (String)columns[2];
			Integer currencyId = (Integer)columns[3];
			BigDecimal total = (BigDecimal)columns[4];
			BigDecimal balance = (BigDecimal)columns[5];
			String invoiceFromSsdc = (String)columns[6];
			String invoiceFromSupplier = (String)columns[7];
			resultList.add(new StatementListReportItemUsImpl(statementId,startDateUtc,endDateUtc,currencyId,total,balance,invoiceFromSsdc,invoiceFromSupplier));
		}
		return resultList;
	}
	
	@Override
	public StatementListReport queryStatementsIsud(BillStatementType type) {
		int userId = Context.getCurrentUser().getUserId();
		int userCompanyId = this.companyRepo.queryCompanyIdOfUser(userId);
		StatementListReportImpl report = new StatementListReportImpl();
		report.setStatements(toStatementList(this.dao.queryStatementsIsud(type,userCompanyId)));
		return report;
	}

	private StatementInfo getInfo(BillStatementType type, String statementName){

		Object [] columns = this.dao.queryInfo(type,statementName);
		Date start = (Date)columns[0];
		Date end = (Date)columns[1];
		String isurKcode = (String)columns[2];
		String rcvrKcode = (String)columns[3];
		return new StatementInfoImpl(start, end, isurKcode, rcvrKcode);
	}

	@Override
	public Ms2ssStatement queryStatement(BillStatementType type, String statementName) {
		Ms2ssStatementReportImpl report = new Ms2ssStatementReportImpl();
		report.setInfo(this.getInfo(type,statementName));
		report.setItems(this.dao.queryStatementItems(type,statementName));
		report.setItemsPaymentOnBehalf(this.dao.queryStatementItemsPaymentOnBehalf(type,statementName));

		Object [] columns = this.dao.queryStatementItemMsdcPaymentOnBehalf(type, statementName);
		if (columns != null) {
			String name = (String) columns[0];
			BigDecimal amount = (BigDecimal) columns[1];

			report.setMs2ssStatementItemMsdcPaymentOnBehalf(new Ms2ssStatementItemMsdcPaymentOnBehalfImpl(name, amount));
		}
		report.setProductInventoryReturnItem(this.dao.queryProductInventoryReturnItem(type,statementName));
		report.setPreviousBalance(this.dao.queryPreviousBalance(type,statementName));
		report.setRemittanceIsurToRcvr(this.dao.queryRemittanceIsurToRcvr(type,statementName));
		report.setRemittanceRcvrToIsur(this.dao.queryRemittanceRcvrToIsur(type,statementName));
		report.setBalance(this.dao.queryBalance(type,statementName));
		return report;
	}
	
	@Override
	public Ms2ssPaymentAndRefundReport getPaymentAndRefundReport(BillStatementType type, String statementName, String sourcePoId) {
		Ms2ssPaymentAndRefundReportImpl report = new Ms2ssPaymentAndRefundReportImpl();
		report.setInfo(this.getInfo(type,statementName));
		report.setLineItems(this.dao.queryPaymentAndRefundReportItems(type,statementName, sourcePoId));
		return report;
	}
	
	@Override
	public Ms2ssPurchaseAllowanceReport getPurchaseAllowanceReport(BillStatementType type, String statementName, String shipmentName) {
		List<Ms2ssPurchaseAllowanceReportRawItem> items = this.dao.queryPurchaseAllowanceReportRawItems(type,statementName, shipmentName);
		Ms2ssPurchaseAllowanceReportImpl report = new Ms2ssPurchaseAllowanceReportImpl(items);
		report.setInfo(this.getInfo(type,statementName));
		report.setOtherItemAmounts(this.dao.queryPurchaseAllowanceUnRelatedToSku(type,statementName,shipmentName));
		return report;
	}
	
	@Override
	public RemittanceReport getRemittanceIsurToRcvrReport(BillStatementType type,String statementName) {
		RemittanceReportImpl report = new RemittanceReportImpl();
		report.setInfo(this.getInfo(type,statementName));
		report.setItems(this.dao.queryRemittanceReportItemsIsurToRcvr(type,statementName));
		return report;
	}
	
	@Override
	public RemittanceReport getRemittanceRcvrToIsurReport(BillStatementType type,String statementName) {
		RemittanceReportImpl report = new RemittanceReportImpl();
		report.setInfo(this.getInfo(type,statementName));
		report.setItems(this.dao.queryRemittanceReportItemsRcvrToIsur(type,statementName));
		return report;
	}
	
	@Override
	public Ms2ssSettleableItemReport querySettleableItemReportForPaymentRefund(BillStatementType type,String statementName, String sourcePoId, String sku, int settleableItemId) {
		Ms2ssSettleableItemReportImpl report = new Ms2ssSettleableItemReportImpl();
		report.setInfo(this.getInfo(type,statementName));
		report.setLineItems(this.dao.querySettleableItemReportLineItem(type,statementName, sourcePoId, sku, settleableItemId));
		report.setTitle(this.dao.querySettleableItemDrsName(settleableItemId));
		report.setCurrency(this.dao.queryShipmentCurrency(sourcePoId));
		return report;
	}

	@Override
	public Ms2ssSettleableItemReport querySettleableItemReportForPurchaseAllowance(BillStatementType type,String statementName, String shipmentName, String sku,int settleableItemId) {
		Ms2ssSettleableItemReportImpl report = new Ms2ssSettleableItemReportImpl();
		report.setInfo(this.getInfo(type,statementName));
		report.setTitle(this.dao.querySettleableItemDrsName(settleableItemId));
		report.setCurrency(Currency.USD);
		TransactionLineType si = this.dao.queryItemById(settleableItemId);
		if( si==TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_SALES||
			si==TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_REFUND||
			si==TransactionLineType.MS2SS_UNIT_PURCHASE_ALWNC_WITH_RETAIL_REFUND_FEE){
			report.setLineItems(this.dao.querySettleableItemReportLineItem(type,statementName, shipmentName, sku, settleableItemId));
		}
		else if(si==TransactionLineType.MS2SS_PURCHASE_ALWNC_IMPORT_DUTY){
			report.setLineItems(this.dao.querySettleableItemReportLineItemForImpoortDuty(type,statementName, shipmentName, sku, settleableItemId));
		}
		else if(si==TransactionLineType.MS2SS_PURCHASE_ALWNC_CUSTOMERCARE){
			report.setLineItems(this.dao.querySettleableItemReportLineItemForCustomerCareCase(type,statementName, sku, settleableItemId));
		}
		else{
			report.setLineItems(this.dao.querySettleableItemReportLineItemSpecial(type,statementName, sku, settleableItemId));
		}
		return report;
	}
	
	@Override
	public Ms2ssSettleableItemReport querySettleableItemReportForPaymentOnBehalf(BillStatementType type,String statementName) {
		Ms2ssSettleableItemReportImpl report = new Ms2ssSettleableItemReportImpl();
		report.setInfo(this.getInfo(type,statementName));
		report.setLineItems(this.dao.querySettleableItemReportLineItemPaymentOnBehalfOfImportDuty(type,statementName));
		report.setTitle(this.paymentOnbehalfTitle);
		report.setCurrency(this.dao.queryIssuerCurrency(type,statementName));
		return report;
	}
	
	@Override
	public Ms2ssSettleableItemReport getMsdcPaymentOnBehalfDetail(BillStatementType type, String statementName) {
		Ms2ssSettleableItemReportImpl report = new Ms2ssSettleableItemReportImpl();
		report.setInfo(this.getInfo(type,statementName));
		report.setLineItems(this.dao.querySettleableItemReportLineItemMsdcPaymentOnBehalf(type,statementName));
		report.setTitle("MSDC payment on behalf of SSDC");
		report.setCurrency(this.dao.queryIssuerCurrency(type,statementName));
		return report;
	}

	@Override
	public Ms2ssProductInventoryReturnReport getProductInventoryReturnReport(BillStatementType type,String statementName) {
		Ms2ssProductInventoryReturnReportImpl report = new Ms2ssProductInventoryReturnReportImpl();
		report.setTitle("Product Inventory Return");
		report.setInfo(this.getInfo(type,statementName));
		report.setLineItems(this.dao.queryMs2ssProductInventoryReturnReportItems(type,statementName));
		return report;
	}
	
}