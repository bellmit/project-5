package com.kindminds.drs.service.usecase.accounting;

import com.kindminds.drs.Context;
import com.kindminds.drs.Country;
import com.kindminds.drs.Currency;
import com.kindminds.drs.api.data.access.rdb.CompanyDao;
import com.kindminds.drs.api.data.access.rdb.CurrencyDao;
import com.kindminds.drs.api.data.access.usecase.accounting.ViewSs2spStatementDao;
import com.kindminds.drs.api.usecase.accounting.ViewSs2spStatementUco;
import com.kindminds.drs.v1.model.impl.report.ProfitSharePartialReturnReportImpl;
import com.kindminds.drs.v1.model.impl.statement.*;
import com.kindminds.drs.api.v1.model.report.*;
import com.kindminds.drs.api.v1.model.report.Ss2spPaymentAndRefundReport.Ss2spPaymentAndRefundReportItem;
import com.kindminds.drs.api.v1.model.report.Ss2spProfitShareDetailReport.Ss2spProfitShareDetailReportSkuProfitShareItem;
import com.kindminds.drs.api.v1.model.report.Ss2spProfitShareReport.Ss2spProfitShareReportLineItem;
import com.kindminds.drs.api.v1.model.report.Ss2spSettleableItemReport.Ss2spSettleableItemReportLineItem;
import com.kindminds.drs.api.v1.model.report.Ss2spSkuProfitShareDetailReport.Ss2spSkuProfitShareDetailReportRefundedItem;
import com.kindminds.drs.api.v1.model.report.Ss2spSkuProfitShareDetailReport.Ss2spSkuProfitShareDetailReportShippedItem;
import com.kindminds.drs.api.v1.model.report.StatementListReport.StatementListReportItem;

import com.kindminds.drs.enums.BillStatementType;
import com.kindminds.drs.v1.model.impl.factory.Ss2spStatementReportFactory;
import com.kindminds.drs.v1.model.impl.factory.Ss2spStatementReportFactory.ReportVersion;
import com.kindminds.drs.util.DateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

@Service("ViewSs2spStatementUco4Fe")
public class ViewSs2spStatementUcoImpl implements ViewSs2spStatementUco {
	
	@Autowired private ApplicationContext appContext;
	@Autowired private ViewSs2spStatementDao dao;
	@Autowired private CompanyDao companyRepo;
	@Autowired private CurrencyDao currencyRepo;

	private Ss2spStatementReportFactory selectFactory(BillStatementType type,String statementName){
		int versionNumber = this.dao.queryStatementVersionNumber(type,statementName);
		String factoryBeanId = ReportVersion.fromVersionNumber(versionNumber).getFactoryBeanId();
		return (Ss2spStatementReportFactory)this.appContext.getBean(factoryBeanId);
	}
	
	@Override
	public Map<String,String> getSupplierKcodeToNameMap() {
		if(Context.getCurrentUser().isDrsUser()) return this.companyRepo.querySupplierKcodeToShortEnUsNameMap();
		return null;
	}

	@Override
	public Map<String,String> getAllCompanyKcodeToLocalNameMap() {
		return companyRepo.queryAllCompanyKcodeToLocalNameMap();
	}
	
	@Override
	public StatementListReport getSs2spStatementListReport(BillStatementType type,String supplierKcodeToFilter) {
		String userCompanyKcode = Context.getCurrentUser().getCompanyKcode();
		boolean isDrsCompany = Context.getCurrentUser().isDrsUser();
		Currency userCompanyCurrency = this.currencyRepo.queryCompanyCurrency(userCompanyKcode);
		StatementListReportImpl ss2spStatementListReport = new StatementListReportImpl();
		ss2spStatementListReport.setCurrency(userCompanyCurrency);
		ss2spStatementListReport.setStatements(this.getSs2spStatementList(type,isDrsCompany,userCompanyKcode,supplierKcodeToFilter));
		return ss2spStatementListReport;
	}
	
	private List<StatementListReportItem> getSs2spStatementList(BillStatementType type,boolean isDrsCompany,String userCompanyKcode,String supplierKcodeToFilter){
		if(isDrsCompany){
			if(type==BillStatementType.DRAFT) return toStatementList(this.dao.queryDraftStatements(userCompanyKcode));
			if(StringUtils.hasText(supplierKcodeToFilter))
				return toStatementList(this.dao.queryStatementsReceivedBySupplier(type,supplierKcodeToFilter));
			else
				return toStatementList(this.dao.queryNewestStatementsSentByDrsCompany(type,userCompanyKcode));
		}
		else{
			return toStatementList(this.dao.queryStatementsReceivedBySupplier(type,userCompanyKcode));
		}
	}

	private List<StatementListReportItem> toStatementList(List<Object[]> columnsList) {
		List<StatementListReportItem> resultList = new ArrayList<>();
		for(Object[] columns:columnsList){
			String statementId = (String)columns[0];
			String startDateUtc = (String)columns[1];
			String endDateUtc = (String)columns[2];
			Integer currencyId = (Integer)columns[3];
			BigDecimal total = (BigDecimal)columns[4];
			BigDecimal balance = (BigDecimal)columns[5];
			String invoiceFromSsdc = (String)columns[6];
			String invoiceFromSupplier = (String)columns[7];
			resultList.add(new StatementListReportItemTwImpl(statementId,startDateUtc,endDateUtc,currencyId,total,balance,invoiceFromSsdc,invoiceFromSupplier));
		}
		return resultList;
	}
	
	@Override
	public Ss2spStatementReport getSs2spStatement(BillStatementType type,String statementName) {
		Assert.isTrue(this.checkAccessable(statementName));
		return this.selectFactory(type,statementName).createSs2spStatementReport(type,statementName);
	}
	
	@Override
	public Ss2spProfitShareReport getSs2spProfitShareReport(BillStatementType type,String statementName) {
		Assert.isTrue(this.checkAccessable(statementName));
		return this.selectFactory(type,statementName).createSs2spProfitShareReport(type,statementName);
	}
	
	@Override
	public Ss2spProfitShareDetailReport getSs2spProfitShareDetailReport(BillStatementType type,String statementName,String country) {
		Assert.isTrue(this.checkAccessable(statementName));
		return this.selectFactory(type,statementName).createSs2spProfitShareDetailReport(type,statementName, country);
	}

	private StatementInfoImpl getInfo(BillStatementType type , String statementName){

		Object [] columns = this.dao.queryInfo(type,statementName);

		Date start = (Date)columns[0];
		Date end = (Date)columns[1];
		String isurKcode = (String)columns[2];
		String rcvrKcode = (String)columns[3];
		return new StatementInfoImpl(start, end, isurKcode, rcvrKcode);
	}
	
	@Override
	public Ss2spSkuProfitShareDetailReport getSs2spSkuProfitShareDetailReport(BillStatementType type,String statementName,String countryCode, String sku) {
		Assert.isTrue(this.checkAccessable(statementName));
		Country country = Country.valueOf(countryCode);
		StatementInfo info = this.getInfo(type,statementName);
		Date start = ((StatementInfoImpl)info).getStart();
		Date end = ((StatementInfoImpl)info).getEnd();
		Ss2spSkuProfitShareDetailReportImpl report = new Ss2spSkuProfitShareDetailReportImpl();
		report.setSku(sku);
		report.setInfo(info);

		List<Object []> shippedItemList = null;
		if(statementName.equals("STM-K577-72")){
			List<String> types = new ArrayList<String>();
			types.add("Reimbursement");
			shippedItemList =this.dao.querySs2spSkuProfitShareDetailReportShippedItem(start, end, country, sku ,types);
		}else{
			shippedItemList =this.dao.querySs2spSkuProfitShareDetailReportShippedItem(start, end, country, sku);
		}

		List<Ss2spSkuProfitShareDetailReportShippedItem> results = new ArrayList<>();
		for(Object[] columns: shippedItemList){
			String utcDate=(String)columns[0];
			int marketplaceId = (Integer)columns[1];
			String orderId=(String)columns[2];
			BigDecimal pretaxPrincipalPrice=(BigDecimal)columns[3];
			BigDecimal msdcRetainment=(BigDecimal)columns[4];
			BigDecimal marketplaceFee=(BigDecimal)columns[5];
			BigDecimal marketplaceFeeNonRefundable=(BigDecimal)columns[6];
			BigDecimal fulfillmentFee=(BigDecimal)columns[7];
			BigDecimal ssdcRetainment=(BigDecimal)columns[8];
			BigDecimal fcaInMarketsideCurrency=(BigDecimal)columns[9];
			BigDecimal spProfitShare=(BigDecimal)columns[10];
			results.add(new Ss2spSkuProfitShareDetailReportShippedItemImpl(utcDate, marketplaceId, orderId, pretaxPrincipalPrice, msdcRetainment, marketplaceFee, marketplaceFeeNonRefundable, fulfillmentFee, ssdcRetainment, fcaInMarketsideCurrency, spProfitShare));
		}

		report.setShippedItems(results);

		List<Object []> refundedItemList = this.dao.querySs2spSkuProfitShareDetailReportRefundedItem(start, end, country, sku);
		List<Ss2spSkuProfitShareDetailReportRefundedItem> results2 = new ArrayList<>();
		for(Object[] columns: refundedItemList){
			String utcDate=(String)columns[0];
			int marketplaceId = (Integer)columns[1];
			String orderId=(String)columns[2];
			BigDecimal pretaxPrincipalPrice=(BigDecimal)columns[3];
			BigDecimal msdcRetainment=(BigDecimal)columns[4];
			BigDecimal marketplaceFee=(BigDecimal)columns[5];
			BigDecimal marketplaceFeeNonRefundable=(BigDecimal)columns[6];
			BigDecimal fulfillmentFee=(BigDecimal)columns[7];
			BigDecimal ssdcRetainment=(BigDecimal)columns[8];
			BigDecimal fcaInMarketsideCurrency=(BigDecimal)columns[9];
			BigDecimal spProfitShare=(BigDecimal)columns[10];
			BigDecimal refundFee=(BigDecimal)columns[11];
			results2.add(new Ss2spSkuProfitShareDetailReportRefundedItemImpl(utcDate, marketplaceId, orderId, pretaxPrincipalPrice, msdcRetainment, marketplaceFee, marketplaceFeeNonRefundable, fulfillmentFee, ssdcRetainment, fcaInMarketsideCurrency, spProfitShare,refundFee));
		}
		report.setRefundedItems(results2);
		return report;
	}
	
	private boolean checkAccessable(String name){
		if(Context.getCurrentUser().isDrsUser()) return true;
		else{
			String supplierKcode = Context.getCurrentUser().getCompanyKcode();
			String statementRcvrKcode = this.dao.queryRcvrKcode(name);
			return supplierKcode.equals(statementRcvrKcode);
		}
	}
	
	//TODO refactoring
	@Override
	public List<Ss2spSettleableBrowserForProfitShareToExport> getSs2spSettleableBrowserForProfitShareToExport(
			BillStatementType type,String statementName, String countryCode) {
		Ss2spProfitShareReport ss2spProfitShareReport = this.selectFactory(type,statementName).createSs2spProfitShareReport(type,statementName);
		Country country = Country.valueOf(countryCode);
		StatementInfo info = this.getInfo(type,statementName);
		Date start = ((StatementInfoImpl)info).getStart();
		Date end = ((StatementInfoImpl)info).getEnd();		
		List<Ss2spSettleableBrowserForProfitShareToExport> report = new ArrayList<Ss2spSettleableBrowserForProfitShareToExport>();
		for(Ss2spProfitShareReportLineItem lineItem :ss2spProfitShareReport.getLineItems()){		
			if(lineItem.getSourceCountry().equals(countryCode)){				
				Ss2spProfitShareDetailReport ss2spProfitShareDetailReport = this.selectFactory(type,statementName).createSs2spProfitShareDetailReport(type,statementName, countryCode);				
				for(Ss2spProfitShareDetailReportSkuProfitShareItem skuProfitShareItem:ss2spProfitShareDetailReport.getSkuProfitShareItems()){					
					List<Object []> columnsList  = this.dao.querySs2spSkuProfitShareDetailReportShippedItem(start, end, country, skuProfitShareItem.getSku());

					List<Ss2spSkuProfitShareDetailReportShippedItem> shippedItems = new ArrayList<>();
					for(Object[] columns:columnsList){
						String utcDate=(String)columns[0];
						int marketplaceId = (Integer)columns[1];
						String orderId=(String)columns[2];
						BigDecimal pretaxPrincipalPrice=(BigDecimal)columns[3];
						BigDecimal msdcRetainment=(BigDecimal)columns[4];
						BigDecimal marketplaceFee=(BigDecimal)columns[5];
						BigDecimal marketplaceFeeNonRefundable=(BigDecimal)columns[6];
						BigDecimal fulfillmentFee=(BigDecimal)columns[7];
						BigDecimal ssdcRetainment=(BigDecimal)columns[8];
						BigDecimal fcaInMarketsideCurrency=(BigDecimal)columns[9];
						BigDecimal spProfitShare=(BigDecimal)columns[10];
						shippedItems.add(new Ss2spSkuProfitShareDetailReportShippedItemImpl(utcDate, marketplaceId, orderId, pretaxPrincipalPrice, msdcRetainment, marketplaceFee, marketplaceFeeNonRefundable, fulfillmentFee, ssdcRetainment, fcaInMarketsideCurrency, spProfitShare));
					}


					for(Ss2spSkuProfitShareDetailReportShippedItem shippedItem :shippedItems){
						report.add(new Ss2spSettleableBrowserForProfitShareToExportImpl(
								statementName,
								ss2spProfitShareReport.getDateStart(),
								ss2spProfitShareReport.getDateEnd(),
								countryCode,
								lineItem.getSourceCurrency(),
								lineItem.getExchangeRate(),
								skuProfitShareItem.getSku(),
								skuProfitShareItem.getName(),
								"ss2spStatement.profitShare",
								shippedItem.getUtcDate(),
								shippedItem.getMarketplace(),
								shippedItem.getOrderId(),
								new Integer("1"),
								shippedItem.getPretaxPrincipalPrice(),
								shippedItem.getFcaInMarketSideCurrency(),
								shippedItem.getMarketplaceFee(),
								shippedItem.getFulfillmentFee(),
								shippedItem.getDrsRetainment(),
								shippedItem.getProfitShare()								
								));												
					}															
					List<Object []> columnsList1 = this.dao.querySs2spSkuProfitShareDetailReportRefundedItem(start, end, country, skuProfitShareItem.getSku());

					List<Ss2spSkuProfitShareDetailReportRefundedItem> refundedItems = new ArrayList<>();
					for(Object[] columns:columnsList1){
						String utcDate=(String)columns[0];
						int marketplaceId = (Integer)columns[1];
						String orderId=(String)columns[2];
						BigDecimal pretaxPrincipalPrice=(BigDecimal)columns[3];
						BigDecimal msdcRetainment=(BigDecimal)columns[4];
						BigDecimal marketplaceFee=(BigDecimal)columns[5];
						BigDecimal marketplaceFeeNonRefundable=(BigDecimal)columns[6];
						BigDecimal fulfillmentFee=(BigDecimal)columns[7];
						BigDecimal ssdcRetainment=(BigDecimal)columns[8];
						BigDecimal fcaInMarketsideCurrency=(BigDecimal)columns[9];
						BigDecimal spProfitShare=(BigDecimal)columns[10];
						BigDecimal refundFee=(BigDecimal)columns[11];
						refundedItems.add(new Ss2spSkuProfitShareDetailReportRefundedItemImpl(utcDate, marketplaceId, orderId, pretaxPrincipalPrice, msdcRetainment, marketplaceFee, marketplaceFeeNonRefundable, fulfillmentFee, ssdcRetainment, fcaInMarketsideCurrency, spProfitShare,refundFee));
					}


					for(Ss2spSkuProfitShareDetailReportRefundedItem refundedItem :refundedItems){
						report.add(new Ss2spSettleableBrowserForProfitShareToExportImpl(
								statementName,
								ss2spProfitShareReport.getDateStart(),
								ss2spProfitShareReport.getDateEnd(),
								countryCode,
								lineItem.getSourceCurrency(),
								lineItem.getExchangeRate(),
								skuProfitShareItem.getSku(),
								skuProfitShareItem.getName(),
								"ss2spStatement.profitShare",
								refundedItem.getUtcDate(),
								refundedItem.getMarketplace(),
								refundedItem.getOrderId(),
								new Integer("1"),
								refundedItem.getPretaxPrincipalPrice(),
								refundedItem.getFcaInMarketSideCurrency(),
								refundedItem.getMarketplaceFee(),
								refundedItem.getRefundFee(),
								refundedItem.getDrsRetainment(),
								refundedItem.getProfitShare()								
								));												
					}															
				}								
				if(ss2spProfitShareDetailReport.getOtherItemAmounts() != null){				
					for(Map.Entry<String, String> entry : ss2spProfitShareDetailReport.getOtherItemAmounts().entrySet()) {
						String itemName = entry.getKey();
						String profitShare = entry.getValue();
						report.add(new Ss2spSettleableBrowserForProfitShareToExportImpl(
							statementName,
							ss2spProfitShareReport.getDateStart(),
							ss2spProfitShareReport.getDateEnd(),
							countryCode,
							lineItem.getSourceCurrency(),
							lineItem.getExchangeRate(),
							null,
							null,
							itemName,
							null,
							null,
							null,
							null,
							null,
							null,
							null,
							null,
							null,
							profitShare								
						));				    					    				    
					}
				
				}
			}									
		}				
		return report;
	}
		
	@Override
	public Ss2spPaymentAndRefundReport getSs2spPaymentAndRefundReport(BillStatementType type,String statementName,String sourceShipmentName){
		Assert.isTrue(this.checkAccessable(statementName));
		return this.selectFactory(type,statementName).createSs2spPaymentAndRefundReport(type,statementName, sourceShipmentName);
	}
	
	//TODO :refactoring
	@Override
	public List<Ss2spPaymentAndRefundReportItemToExport> getSs2spPaymentAndRefundReportItemToExport(BillStatementType type,String statementName,
			String sourceShipmentName) {
		List<Object []> columnsList = this.dao.queryPaymentAndRefundReportItems(type,statementName, sourceShipmentName);

		List<Ss2spPaymentAndRefundReportItem> lineItems = new ArrayList<>();
		for(Object[] columns:columnsList){
			String sku = (String)columns[0];
			String skuName = (String)columns[1];
			String statementLineType = (String)columns[2];
			Integer quantity =BigInteger.valueOf(Long.parseLong(columns[3].toString())).intValue();
			Integer currencyId = (Integer)columns[4];
			BigDecimal amount = (BigDecimal)columns[5];
			Integer tltId = (Integer)columns[6];
			lineItems.add(new Ss2spPaymentAndRefundItemImpl(sku,skuName,statementLineType,quantity,amount,currencyId,tltId));
		}


		for(Ss2spPaymentAndRefundReportItem item:lineItems){
			((Ss2spPaymentAndRefundItemImpl)item).setUnitAmount(this.dao.queryPaymentAndRefundUnitAmount(type,statementName,sourceShipmentName, item.getSku(),item.getSettleableItemId()));;
		}
		StatementInfo statementInfo = this.getInfo(type,statementName);
		List<Ss2spPaymentAndRefundReportItemToExport> report = new ArrayList<Ss2spPaymentAndRefundReportItemToExport>();
		for(Ss2spPaymentAndRefundReportItem item:lineItems){
			List<Ss2spSettleableItemReportLineItem> ss2spSettleableItemReportLineItems = this.dao.querySettleableItemReportLineItem(type,statementName, sourceShipmentName, item.getSku(), item.getSettleableItemId());
			for(Ss2spSettleableItemReportLineItem lineItem:ss2spSettleableItemReportLineItems){
				report.add(new Ss2spPaymentAndRefundReportItemToExportImpl(
						statementName,
						statementInfo.getDateStart(),
						statementInfo.getDateEnd(),
						sourceShipmentName,
						lineItem.getTransactionTimeUtc(),
						lineItem.getSku(),
						lineItem.getSkuName(),
						lineItem.getName(),
						lineItem.getSourceName(),
						lineItem.getCurrency(),
						lineItem.getNumericAmount(),
						new Integer("1"),
						lineItem.getNumericAmount()																		
				));								
			}					
		}						
		return report;
	}
		
	@Override
	public Ss2spSellBackReport getSellBackReport(BillStatementType type,String statementName) {
		Assert.isTrue(this.checkAccessable(statementName));
		return this.selectFactory(type,statementName).createSellBackReport(type,statementName);
	}

	@Override
	public Ss2spCustomerCareCostReport getSs2spCustomerCareCostReport(BillStatementType type,String statementName, String country) {
		Assert.isTrue(this.checkAccessable(statementName));
		return this.selectFactory(type,statementName).createSs2spCustomerCareCostReport(type,statementName,country);
	}

	@Override
	public Ss2spServiceExpenseReport getServiceExpenseReport(BillStatementType type,String statementName,String domesticTransactionInvoice) {
		Assert.isTrue(this.checkAccessable(statementName));
		return this.selectFactory(type,statementName).createServiceExpenseReport(type,statementName, domesticTransactionInvoice);
	}

	@Override
	public Ss2spServiceExpenseReport getAllServiceExpenseReport(BillStatementType type,String statementName) {
		Assert.isTrue(this.checkAccessable(statementName));
		return this.selectFactory(type,statementName).createAllServiceExpenseReport(type,statementName);
	}

	@Override
	public RemittanceReport getRemittanceIsurToRcvrReport(BillStatementType type,String statementName) {
		Assert.isTrue(this.checkAccessable(statementName));
		return this.selectFactory(type,statementName).createRemittanceIsurToRcvrReport(type,statementName);
	}
	
	@Override
	public RemittanceReport getRemittanceRcvrToIsurReport(BillStatementType type,String statementName) {
		Assert.isTrue(this.checkAccessable(statementName));
		return this.selectFactory(type,statementName).createRemittanceRcvrToIsurReport(type,statementName);
	}
	
	@Override
	public Ss2spSettleableItemReport getSs2spSettleableItemReportForPoRelated(BillStatementType type,String statementName, String sourcePoId, String sku, int settleableItemId) {
		Assert.isTrue(this.checkAccessable(statementName));
		return this.selectFactory(type,statementName).createSs2spSettleableItemReportForPoRelated(type,statementName, sourcePoId, sku, settleableItemId);
	}

	@Override
	public Ss2spSettleableItemReport getSs2spSettleableItemReportForProfitShare(BillStatementType type,String statementName, String country, String sku, String itemName) {
		Assert.isTrue(this.checkAccessable(statementName));
		return this.selectFactory(type,statementName).createSs2spSettleableItemReportForProfitShare(type,statementName, country, sku, itemName);
	}

	@Override
	public String getBalanceNoteMessageKey(String statementName, String dateStartStr, String balanceStr, Locale locale) {
		BigDecimal limit = new BigDecimal("5000");
		Date oldBalanceNoteEndDate =  DateHelper.toDate("2016-08-20 UTC", "yyyy-MM-dd z");
		Date currentStatementStartDate = DateHelper.toDate(dateStartStr+" UTC", "yyyy-MM-dd z");

		String companyCode = statementName.startsWith("S") ?
				statementName.split("-")[1] : statementName.split("-")[2];
		Boolean companyBisettlement = dao.queryIsCompanyBiSettlement(companyCode);

		Boolean periodBisettlement = dao.queryIsPeriodBisettlement(currentStatementStartDate);
		if (companyBisettlement && !periodBisettlement) {
			return null;
		}

		BigDecimal balance = new BigDecimal(balanceStr);
		if(balance.signum()==0) return null;
		if(currentStatementStartDate.after(oldBalanceNoteEndDate)){
			if(balance.abs().compareTo(limit) < 0){
				return "ss2spStatement.noteForBalanceBetween";
			}
		}
		if(balance.signum()== 1) {
			if (companyBisettlement) {
				return "ss2spStatement.noteForBalanceIsPositiveBiSettlement";
			} else {
				return "ss2spStatement.noteForBalanceIsPositive";
			}
		}
		if(balance.signum()==-1) {
			if (companyBisettlement) {
				return "ss2spStatement.noteForBalanceIsNegativeBiSettlement";
			} else {
//				return "ss2spStatement.noteForBalanceIsNegative";
				if (companyCode.equals("K652")) {
					return "ss2spStatement.noteForBalanceIsNegativeK652";
				} else {
					return "ss2spStatement.noteForBalanceIsNegative";
				}
			}
		}
		Assert.isTrue(false);
		return null;
	}

	@Override
	public String getMonthInfoForMonthStorageFee(String DateStringStart) {
		Date DateStart = DateHelper.toDate(DateStringStart, "yyyy-MM-dd");
		int i = -1;
		if (DateStringStart.equals("2021-04-11")) i=-2;
		if (DateStringStart.equals("2021-08-29")) i=0;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(DateStart);
		calendar.add(Calendar.MONTH, i);
		String stringYear = Integer.toString(calendar.get(Calendar.YEAR));
		String stringMonth = Integer.toString(calendar.get(Calendar.MONTH)+1);		
		return stringYear+"-"+stringMonth;
	}

	@Override
	public ProfitShareSubtractionAdvertisingCostReport getSs2spAdvertisingCostReport(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProfitShareSubtractionMarketingActivityExpenseReport getSs2spMarketingActivityExpenseReport(String arg0,
			String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProfitSharePartialRefundReport getPartialRefundReport(String statementName, String countryCode){

		List<Object []> resultList = dao.queryPartialRefundItems(statementName,countryCode);
		BigDecimal total = BigDecimal.ZERO;
		ProfitSharePartialReturnReportImpl profitSharePartialReturnReport = new ProfitSharePartialReturnReportImpl();
		List<ProfitSharePartialRefundReport.ProfitSharePartialRefundLineItem>items =new ArrayList<>();
		for(Object[] columns : resultList) {
			String itemNote = (String) columns[0];
			String currencyName = (String) columns[1];
			BigDecimal subtotal = (BigDecimal) columns[2];
			total=total.add(subtotal);
			items.add(new ProfitSharePartialRefundLineItemImpl(itemNote, currencyName, subtotal));
		}
		StatementInfo statementInfo = getInfo(BillStatementType.OFFICIAL, statementName);
		profitSharePartialReturnReport.setDateStart(statementInfo.getDateStart());
		profitSharePartialReturnReport.setDateEnd(statementInfo.getDateEnd());
		profitSharePartialReturnReport.setIsurKcode(statementInfo.getIsurKcode());
		profitSharePartialReturnReport.setRcvrKcode(statementInfo.getRcvrKcode());
		profitSharePartialReturnReport.setTotal(total);
		profitSharePartialReturnReport.setLineItems(items);


		return profitSharePartialReturnReport;
	}

	@Override
	public ProfitShareSubtractionOtherRefundReport getSs2spOtherRefundReport(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}
	
}