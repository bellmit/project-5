package com.kindminds.drs.web.ctrl.accounting;

import static akka.pattern.Patterns.ask;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.kindminds.drs.api.v1.model.report.*;
import com.kindminds.drs.web.config.DrsActorSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kindminds.drs.Country;
import com.kindminds.drs.api.usecase.accounting.DoSs2spSettlementUco;
import com.kindminds.drs.api.usecase.accounting.ViewSs2spStatementUco;
import com.kindminds.drs.enums.BillStatementType;
import com.kindminds.drs.api.v1.model.report.Ss2spPaymentAndRefundReport.Ss2spPaymentAndRefundReportItem;
import com.kindminds.drs.api.v1.model.report.Ss2spProfitShareReport.Ss2spProfitShareReportLineItem;
import com.kindminds.drs.service.util.SpringAppCtx;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindminds.drs.util.JsonHelper;
import com.kindminds.drs.web.data.dto.AdvertisingCost;
import com.kindminds.drs.web.data.dto.OtherRefundAndAllowance;
import com.kindminds.drs.web.data.dto.MarketingActivityExpense;

import akka.actor.ActorRef;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

@Controller
@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('LIST_SS2SP_STATEMENTS'))")
@RequestMapping(value = "/statements")
public class Ss2spStatementController {
	
	@Autowired @Qualifier("ViewSs2spStatementUco4Fe") private ViewSs2spStatementUco uco;

	ActorRef drsQueryBus  = DrsActorSystem.drsQueryBus();
	
	private BillStatementType type=BillStatementType.OFFICIAL;
	
	public static final String rootUrl = "statements";
	
	private DoSs2spSettlementUco getDoSs2spSettlementUco(){		
		return (DoSs2spSettlementUco)SpringAppCtx.get().getBean("statementClose");
	}
	
	private MessageSource getMessageSourceBean(){
		return (MessageSource)SpringAppCtx.get().getBean("messageSource");
	}
	
	public enum Version {
		V1(1,"SS2SP_StatementView_V1","SS2SP_ProfitShareList_V1V2"), 
		V2(2,"th/SS2SP_statement/SS2SP_StatementView_V2","SS2SP_ProfitShareList_V1V2"),
		V3(3,"th/SS2SP_statement/SS2SP_StatementView_V2","th/SS2SP_statement/SS2SP_ProfitShareList_V3"),
		V4(4,"th/SS2SP_statement/SS2SP_StatementView_V2","th/SS2SP_statement/SS2SP_ProfitShareList_V3");
		private int versionNumber;
		private String jspTileNameOfStatementReport;
		private String jspTileNameOfProfitShareListReport;
		Version(int versionNumber, String jspTileNameOfStatementReport, String jspTileNameOfProfitShareListReport){
			this.versionNumber = versionNumber;
			this.jspTileNameOfStatementReport=jspTileNameOfStatementReport;
			this.jspTileNameOfProfitShareListReport=jspTileNameOfProfitShareListReport;
		}
		public String getJspTileNameOfStatementReport(){ return this.jspTileNameOfStatementReport; }
		public String getJspTileNameOfProfitShareListReport(){ return this.jspTileNameOfProfitShareListReport; }
		public static Version fromVersionNumber(int versionNumber) {
	    	for (Version v : Version.values())
	    		if (v.versionNumber==versionNumber) return v;
	    	Assert.isTrue(false);
	        return null;
	    }
	}
	
	@RequestMapping(value="")
	public String listSs2spStatements(Model model, @RequestParam(value="supplierKcode",defaultValue="") String supplierKcode) {

		System.out.println(supplierKcode);

		model.addAttribute("rootUrl", rootUrl);
		model.addAttribute("supplierKcodeNameMap", this.uco.getSupplierKcodeToNameMap());
		model.addAttribute("report", this.uco.getSs2spStatementListReport(this.type,supplierKcode));
		model.addAttribute("supplierKcode", JsonHelper.toJson(supplierKcode));

		return "th/SS2SP_statement/SS2SP_ListStatements";

	}

	@RequestMapping(value="/v4")
	public String listSs2spStatementsV4(Model model, @RequestParam(value="supplierKcode",defaultValue="") String supplierKcode) {

		System.out.println(supplierKcode);

		model.addAttribute("rootUrl", rootUrl);
		model.addAttribute("supplierKcodeNameMap", this.uco.getSupplierKcodeToNameMap());
		model.addAttribute("report", this.uco.getSs2spStatementListReport(this.type,supplierKcode));
		model.addAttribute("supplierKcode", JsonHelper.toJson(supplierKcode));

		return "SS2SP_ListStatementsV4";

	}


	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('SS2SP_DRAFT_STATEMENTS'))")
	@RequestMapping(value="/{statementName}/delete")
	public String deleteStatement(Model model,@PathVariable String statementName){
		this.getDoSs2spSettlementUco().deleteOfficial(statementName);
		return "redirect:/statements";
	}

	
	/* Statement Level 1 */
	@RequestMapping(value="/{statementName}")
	public String showStatement(Model model, @PathVariable String statementName,Locale locale){
		Ss2spStatementReport report = uco.getSs2spStatement(this.type,statementName);
		String balanceNoteMessageKey = this.uco.getBalanceNoteMessageKey(statementName, report.getDateStart(),report.getBalance(),locale);
		model.addAttribute("rootUrl", rootUrl);
		model.addAttribute("companyKcodeToNameMap", this.uco.getAllCompanyKcodeToLocalNameMap());
		model.addAttribute("report", report);
		model.addAttribute("statementName", statementName);
		model.addAttribute("balanceNote", balanceNoteMessageKey==null?"":this.getMessageSourceBean().getMessage(balanceNoteMessageKey,new Object[] {statementName.replace("STM-","")},locale));
		Version version = Version.fromVersionNumber(report.getVersionNumber());
		return version.getJspTileNameOfStatementReport();
	}


	/* Statement Level 1 */
	@RequestMapping(value="/{statementName}/v4")
	public String showStatementV4(Model model, @PathVariable String statementName,Locale locale){
		Ss2spStatementReport report = uco.getSs2spStatement(this.type,statementName);
		String balanceNoteMessageKey =
				this.uco.getBalanceNoteMessageKey(statementName, report.getDateStart(),report.getBalance(),locale);
		model.addAttribute("rootUrl", rootUrl);
		model.addAttribute("companyKcodeToNameMap", this.uco.getAllCompanyKcodeToLocalNameMap());
		model.addAttribute("report", report);
		model.addAttribute("statementName", statementName);
		model.addAttribute("balanceNote", balanceNoteMessageKey==null?"":this.getMessageSourceBean().getMessage(balanceNoteMessageKey,new Object[] {statementName.replace("STM-","")},locale));
		Version version = Version.fromVersionNumber(report.getVersionNumber());
		//return version.getJspTileNameOfStatementReport();
		return "SS2SP_StatementView_V4";
	}



	/* Statement Level 1-1 */
	@RequestMapping(value="/{statementName}/profitshare")
	public String showSs2spProfitShareList(Model model,@PathVariable String statementName){
		Ss2spProfitShareReport report = uco.getSs2spProfitShareReport(this.type,statementName);
		Map<String, Ss2spProfitShareDetailReport> profitShareDetailReport = new HashMap<>();	
		for(Ss2spProfitShareReportLineItem lineItem :report.getLineItems()){
			profitShareDetailReport.put(lineItem.getSourceCountry(), uco.getSs2spProfitShareDetailReport(this.type,statementName,lineItem.getSourceCountry()));			
		}
		
		String storageFeeMonth = this.uco.getMonthInfoForMonthStorageFee(report.getDateStart()).split("-")[1];
		if (storageFeeMonth.length()< 2) {
			storageFeeMonth = "0" + storageFeeMonth;
		}
		
		model.addAttribute("rootUrl", rootUrl);
		model.addAttribute("report", report);
		model.addAttribute("statementName",statementName);
		model.addAttribute("companyKcodeToNameMap", this.uco.getAllCompanyKcodeToLocalNameMap());
		model.addAttribute("monthInfoForMonthStorageFee",this.uco.getMonthInfoForMonthStorageFee(report.getDateStart()));
		model.addAttribute("storageFeeMonth", storageFeeMonth);
		model.addAttribute("profitShareDetailReport", profitShareDetailReport);
		Version version = Version.fromVersionNumber(report.getVersionNumber());
		return version.getJspTileNameOfProfitShareListReport();		
	}


	/* Statement Level 1-1 */
	@RequestMapping(value="/{statementName}/profitshare/v4")
	public String showSs2spProfitShareListV4(Model model,@PathVariable String statementName){
		Ss2spProfitShareReport report = uco.getSs2spProfitShareReport(this.type,statementName);
		Map<String, Ss2spProfitShareDetailReport> profitShareDetailReport = new HashMap<>();
		for(Ss2spProfitShareReportLineItem lineItem :report.getLineItems()){
			profitShareDetailReport.put(lineItem.getSourceCountry(), uco.getSs2spProfitShareDetailReport(this.type,statementName,lineItem.getSourceCountry()));
		}

		String storageFeeMonth = this.uco.getMonthInfoForMonthStorageFee(report.getDateStart()).split("-")[1];
		if (storageFeeMonth.length()< 2) {
			storageFeeMonth = "0" + storageFeeMonth;
		}

		model.addAttribute("rootUrl", rootUrl);
		model.addAttribute("report", report);
		model.addAttribute("statementName",statementName);
		model.addAttribute("companyKcodeToNameMap", this.uco.getAllCompanyKcodeToLocalNameMap());
		model.addAttribute("monthInfoForMonthStorageFee",this.uco.getMonthInfoForMonthStorageFee(report.getDateStart()));
		model.addAttribute("storageFeeMonth", storageFeeMonth);
		model.addAttribute("profitShareDetailReport", profitShareDetailReport);
		Version version = Version.fromVersionNumber(report.getVersionNumber());

		return "SS2SP_ProfitShareList_V4";
	}

	@RequestMapping(value="/{statementName}/profitshare/{country}/MS2SS_PURCHASE_ALWNC_CUSTOMERCARE")	
	public String showSs2spCustomerCareCaseItemsRelatedToBaseProduct(Model model,
			@PathVariable String statementName,
			@PathVariable String country){
		Ss2spCustomerCareCostReport report = uco.getSs2spCustomerCareCostReport(this.type,statementName,country);
		model.addAttribute("rootUrl", rootUrl);
		model.addAttribute("report",report);
		model.addAttribute("companyKcodeToNameMap", this.uco.getAllCompanyKcodeToLocalNameMap());
		return "th/SS2SP_statement/SS2SP_CustomerCareCaseItems";
	}

	@RequestMapping(value="/{statementName}/profitshare/{country}/MS2SS_PURCHASE_ALWNC_CUSTOMERCARE/v4")
	public String showSs2spCustomerCareCaseItemsRelatedToBaseProductV4(Model model,
																	 @PathVariable String statementName,
																	 @PathVariable String country){
		Ss2spCustomerCareCostReport report = uco.getSs2spCustomerCareCostReport(this.type,statementName,country);
		model.addAttribute("rootUrl", rootUrl);
		model.addAttribute("report",report);
		model.addAttribute("companyKcodeToNameMap", this.uco.getAllCompanyKcodeToLocalNameMap());
		return "SS2SP_CustomerCareCaseItemsV4";
	}
		
	/* Statement Level 1-1-1-1 */
	@RequestMapping(value="/{statementName}/profitshare/{country}/{sku}")
	public String showSs2spSettleableBrowserForProfitShare(Model model,
			@PathVariable String statementName,
			@PathVariable String country,
			@PathVariable String sku){
		model.addAttribute("rootUrl", rootUrl);
		model.addAttribute("report",this.uco.getSs2spSkuProfitShareDetailReport(this.type,statementName, country, sku));
		model.addAttribute("currency",Country.valueOf(country).getCurrency());
		model.addAttribute("companyKcodeToNameMap", this.uco.getAllCompanyKcodeToLocalNameMap());
		return "th/SS2SP_statement/SS2SP_SkuCountryProfitShare";
	}

	/* Statement Level 1-1-1-1 */
	@RequestMapping(value="/{statementName}/profitshare/{country}/{sku}/v4")
	public String showSs2spSettleableBrowserForProfitShareV4(Model model,
														   @PathVariable String statementName,
														   @PathVariable String country,
														   @PathVariable String sku){
		model.addAttribute("rootUrl", rootUrl);
		model.addAttribute("report",this.uco.getSs2spSkuProfitShareDetailReport(this.type,statementName, country, sku));
		model.addAttribute("currency",Country.valueOf(country).getCurrency());
		model.addAttribute("companyKcodeToNameMap", this.uco.getAllCompanyKcodeToLocalNameMap());
		return "SS2SP_SkuCountryProfitShareV4";
	}
	
	@RequestMapping(value="/{statementName}/profitshare/{country}/export")
	public String showSs2spSettleableBrowserForProfitShareToExport(Model model,
																   @PathVariable String statementName,
																   @PathVariable String country){
		List<Ss2spSettleableBrowserForProfitShareToExport> itemList = this.uco.getSs2spSettleableBrowserForProfitShareToExport(this.type,statementName,country);
		model.addAttribute("itemList",itemList);
		return "th/SS2SP_statement/SS2SP_SkuCountryProfitShareToExport";
	}

	@RequestMapping(value="/{statementName}/profitshare/{country}/export/v4")
	public String showSs2spSettleableBrowserForProfitShareToExportV4(Model model,
																   @PathVariable String statementName,
																   @PathVariable String country){
		List<Ss2spSettleableBrowserForProfitShareToExport> itemList = this.uco.getSs2spSettleableBrowserForProfitShareToExport(this.type,statementName,country);
		model.addAttribute("itemList",itemList);
		return "SS2SP_SkuCountryProfitShareToExportV4";
	}
		
	/* Statement Level 1-2 */
	@RequestMapping(value="/{statementName}/shipment/{ivsName}")
	public String showSs2spPaymentAndRefundForPoInPeriod(Model model,
			@PathVariable String statementName,
			@PathVariable String ivsName){
		Ss2spPaymentAndRefundReport report = uco.getSs2spPaymentAndRefundReport(this.type,statementName, ivsName);
		Map<String, Ss2spSettleableItemReport> ss2spSettleableItemReport = new HashMap<>();	
		for(Ss2spPaymentAndRefundReportItem lineItem :report.getLineItems()){
			ss2spSettleableItemReport.put(lineItem.getSku()+lineItem.getSettleableItemId(),
					uco.getSs2spSettleableItemReportForPoRelated(
							this.type,statementName,ivsName,lineItem.getSku(),lineItem.getSettleableItemId()));
		}
		model.addAttribute("rootUrl", rootUrl);
		model.addAttribute("report",report);
		model.addAttribute("statementId",statementName);
	    model.addAttribute("lineItems",report.getLineItems());
	    model.addAttribute("total",report.getTotal());
	    model.addAttribute("sourcePoId",ivsName);
	    model.addAttribute("companyKcodeToNameMap", this.uco.getAllCompanyKcodeToLocalNameMap());
	    model.addAttribute("ss2spSettleableItemReport",ss2spSettleableItemReport);		
	    return "th/SS2SP_statement/SS2SP_PaymentAndRefundForPoInPeriod";
	}

	/* Statement Level 1-2 */
	@RequestMapping(value="/{statementName}/shipment/{ivsName}/v4")
	public String showSs2spPaymentAndRefundForPoInPeriodV4(Model model,
														 @PathVariable String statementName,
														 @PathVariable String ivsName){
		Ss2spPaymentAndRefundReport report = uco.getSs2spPaymentAndRefundReport(this.type,statementName, ivsName);
		Map<String, Ss2spSettleableItemReport> ss2spSettleableItemReport = new HashMap<>();
		for(Ss2spPaymentAndRefundReportItem lineItem :report.getLineItems()){
			ss2spSettleableItemReport.put(lineItem.getSku()+lineItem.getSettleableItemId(),
					uco.getSs2spSettleableItemReportForPoRelated(
							this.type,statementName,ivsName,lineItem.getSku(),lineItem.getSettleableItemId()));
		}
		model.addAttribute("rootUrl", rootUrl);
		model.addAttribute("report",report);
		model.addAttribute("statementId",statementName);
		model.addAttribute("lineItems",report.getLineItems());
		model.addAttribute("total",report.getTotal());
		model.addAttribute("sourcePoId",ivsName);
		model.addAttribute("companyKcodeToNameMap", this.uco.getAllCompanyKcodeToLocalNameMap());
		model.addAttribute("ss2spSettleableItemReport",ss2spSettleableItemReport);

		return "SS2SP_PaymentAndRefundForPoInPeriodV4";
	}
	
	@RequestMapping(value="/{statementName}/shipment/{ivsName}/export")	
	public String showSs2spPaymentAndRefundForPoInPeriodItemToExport(Model model,
			@PathVariable String statementName,
			@PathVariable String ivsName){
		List<Ss2spPaymentAndRefundReportItemToExport> itemList = this.uco.getSs2spPaymentAndRefundReportItemToExport(this.type,statementName,ivsName);		
		model.addAttribute("itemList",itemList);	    			
		return "th/SS2SP_statement/SS2SP_PaymentAndRefundForPoInPeriodItemToExport";
	}

	@RequestMapping(value="/{statementName}/shipment/{ivsName}/export/v4")
	public String showSs2spPaymentAndRefundForPoInPeriodItemToExportV4(Model model,
																	 @PathVariable String statementName,
																	 @PathVariable String ivsName){
		List<Ss2spPaymentAndRefundReportItemToExport> itemList = this.uco.getSs2spPaymentAndRefundReportItemToExport(this.type,statementName,ivsName);
		model.addAttribute("itemList",itemList);
		return "SS2SP_PaymentAndRefundForPoInPeriodItemToExportV4";
	}
		
	/* Statement Level 1-2-1 */
	@RequestMapping(value="/{statementName}/shipment/{ivsName}/{sku}/items")
	public String showSs2spSettleableBrowserForPoRelated(Model model,
			@PathVariable String statementName,
			@PathVariable String ivsName,
			@PathVariable String sku,
			@RequestParam("sid") int settleableItemId){
		Ss2spSettleableItemReport report = uco.getSs2spSettleableItemReportForPoRelated(this.type,statementName,ivsName,sku,settleableItemId);
		model.addAttribute("report",report);
		model.addAttribute("companyKcodeToNameMap", this.uco.getAllCompanyKcodeToLocalNameMap());
		return "SS2SP_SettleableItemBrowser";
	}
	/* Statement Level 1-3 */
	@RequestMapping(value="/{statementName}/sellback")
	public String showSs2spSellBackReport(Model model,@PathVariable String statementName){
		Ss2spSellBackReport report = uco.getSellBackReport(this.type,statementName);
		model.addAttribute("rootUrl", rootUrl);
		model.addAttribute("report",report);
		model.addAttribute("statementId",statementName);
	    model.addAttribute("companyKcodeToNameMap", this.uco.getAllCompanyKcodeToLocalNameMap());
		return "th/SS2SP_statement/SS2SP_SellBackReport";
	}

	/* Statement Level 1-2-1 */
	@RequestMapping(value="/{statementName}/shipment/{ivsName}/{sku}/items/v4")
	public String showSs2spSettleableBrowserForPoRelatedV4(Model model,
														 @PathVariable String statementName,
														 @PathVariable String ivsName,
														 @PathVariable String sku,
														 @RequestParam("sid") int settleableItemId){
		Ss2spSettleableItemReport report = uco.getSs2spSettleableItemReportForPoRelated(this.type,statementName,ivsName,sku,settleableItemId);
		model.addAttribute("report",report);
		model.addAttribute("companyKcodeToNameMap", this.uco.getAllCompanyKcodeToLocalNameMap());
		return "SS2SP_SettleableItemBrowserV4";
	}

	/* Statement Level 1-3 */
	@RequestMapping(value="/{statementName}/sellback/v4")
	public String showSs2spSellBackReportV4(Model model,@PathVariable String statementName){
		Ss2spSellBackReport report = uco.getSellBackReport(this.type,statementName);
		model.addAttribute("rootUrl", rootUrl);
		model.addAttribute("report",report);
		model.addAttribute("statementId",statementName);
		model.addAttribute("companyKcodeToNameMap", this.uco.getAllCompanyKcodeToLocalNameMap());
		return "SS2SP_SellBackReportV4";
	}


	/* Statement Level 1-4 */
	@RequestMapping(value="/{statementName}/ServiceExpense/{invoiceNumber}")
	public String showSs2spServiceExpenseReport(Model model,
			@PathVariable String statementName,
			@PathVariable String invoiceNumber){
		Ss2spServiceExpenseReport report = uco.getServiceExpenseReport(this.type,statementName,invoiceNumber);
		model.addAttribute("rootUrl", rootUrl);
		model.addAttribute("report",report);
		model.addAttribute("statementId",statementName);
		model.addAttribute("companyKcodeToNameMap", this.uco.getAllCompanyKcodeToLocalNameMap());
		return "th/SS2SP_statement/SS2SP_ServiceExpense";
	}

	/* Statement Level 1-4 */
	@RequestMapping(value="/{statementName}/ServiceExpense")
	public String showAllSs2spServiceExpenseReport(Model model,
			@PathVariable String statementName){
		Ss2spServiceExpenseReport report = uco.getAllServiceExpenseReport(this.type,statementName);
		model.addAttribute("rootUrl", rootUrl);
		model.addAttribute("report",report);
		model.addAttribute("statementId",statementName);
		model.addAttribute("companyKcodeToNameMap", this.uco.getAllCompanyKcodeToLocalNameMap());
		return "th/SS2SP_statement/SS2SP_ServiceExpense";
	}

	/* Statement Level 1-4 */
	@RequestMapping(value="/{statementName}/ServiceExpense/{invoiceNumber}/v4")
	public String showSs2spServiceExpenseReportV4(Model model,
												@PathVariable String statementName,
												@PathVariable String invoiceNumber){
		Ss2spServiceExpenseReport report = uco.getServiceExpenseReport(this.type,statementName,invoiceNumber);
		model.addAttribute("rootUrl", rootUrl);
		model.addAttribute("report",report);
		model.addAttribute("statementId",statementName);
		model.addAttribute("companyKcodeToNameMap", this.uco.getAllCompanyKcodeToLocalNameMap());

		return "SS2SP_ServiceExpenseV4";
	}

	/* Statement Level 1-5 */
	@RequestMapping(value="/{statementName}/RemittanceSent")	
	public String showRemittanceSent(Model model, @PathVariable String statementName){
		RemittanceReport report = this.uco.getRemittanceRcvrToIsurReport(this.type,statementName);
		model.addAttribute("rootUrl", rootUrl);
		model.addAttribute("companyKcodeToNameMap", this.uco.getAllCompanyKcodeToLocalNameMap());
		model.addAttribute("displayText","ss2spRemittanceTo");
		model.addAttribute("report",report);
		return "th/SS2SP_statement/SS2SP_Remittance";
	}

	/* Statement Level 1-5 */
	@RequestMapping(value="/{statementName}/RemittanceSent/v4")
	public String showRemittanceSentV4(Model model, @PathVariable String statementName){
		RemittanceReport report = this.uco.getRemittanceRcvrToIsurReport(this.type,statementName);
		model.addAttribute("rootUrl", rootUrl);
		model.addAttribute("companyKcodeToNameMap", this.uco.getAllCompanyKcodeToLocalNameMap());
		model.addAttribute("displayText","ss2spRemittanceTo");
		model.addAttribute("report",report);
		return "SS2SP_RemittanceV4";
	}

	/* Statement Level 1-6 */
	@RequestMapping(value="/{statementName}/RemittanceReceived")	
	public String showRemittanceReceived(Model model, @PathVariable String statementName){
		RemittanceReport report = this.uco.getRemittanceIsurToRcvrReport(this.type,statementName);
		model.addAttribute("rootUrl", rootUrl);
		model.addAttribute("companyKcodeToNameMap", this.uco.getAllCompanyKcodeToLocalNameMap());
		model.addAttribute("displayText","ss2spRemittanceFrom");
		model.addAttribute("report",report);
		return "th/SS2SP_statement/SS2SP_Remittance";
	}

	@RequestMapping(value="/{statementName}/RemittanceReceived/v4")
	public String showRemittanceReceivedV4(Model model, @PathVariable String statementName){
		RemittanceReport report = this.uco.getRemittanceIsurToRcvrReport(this.type,statementName);
		model.addAttribute("rootUrl", rootUrl);
		model.addAttribute("companyKcodeToNameMap", this.uco.getAllCompanyKcodeToLocalNameMap());
		model.addAttribute("displayText","ss2spRemittanceFrom");
		model.addAttribute("report",report);
		return "SS2SP_RemittanceV4";
	}
	
	@RequestMapping(value = "/{statementName}/profitshare/advertising-cost/{country}")
	public String showAdvertisingCost(Model model,
			@PathVariable String statementName,
			@PathVariable String country){		
		
		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
		
		final Future<Object> advertisingCostReportQuery = ask(drsQueryBus,
				new com.kindminds.drs.api.message.GetSs2spAdvertisingCostReport(statementName, country), timeout);
		
		try {			

			ObjectMapper mapper = new ObjectMapper();
			String advertisingCostReportJson = (String) Await.result(advertisingCostReportQuery, timeout.duration());
			
			AdvertisingCost advertisingCost = mapper.readValue(advertisingCostReportJson,
					com.kindminds.drs.web.data.dto.AdvertisingCost.class);

			model.addAttribute("companyKcodeToNameMap", this.uco.getAllCompanyKcodeToLocalNameMap());
			model.addAttribute("advertisingCost", advertisingCost);
			model.addAttribute("rootUrl", rootUrl);
			model.addAttribute("country", country);
			System.out.println(advertisingCost.toString());

		} catch (Exception e) {		
			e.printStackTrace();
		}
		return "th/SS2SP_statement/SS2SP_AdvertisingCost";
	}


	@RequestMapping(value = "/{statementName}/profitshare/advertising-cost/{country}/v4")
	public String showAdvertisingCostV4(Model model,
									  @PathVariable String statementName,
									  @PathVariable String country){

		Timeout timeout = new Timeout(Duration.create(10, "seconds"));

		final Future<Object> advertisingCostReportQuery = ask(drsQueryBus,
				new com.kindminds.drs.api.message.GetSs2spAdvertisingCostReport(statementName, country), timeout);

		try {

			ObjectMapper mapper = new ObjectMapper();
			String advertisingCostReportJson = (String) Await.result(advertisingCostReportQuery, timeout.duration());

			AdvertisingCost advertisingCost = mapper.readValue(advertisingCostReportJson,
					com.kindminds.drs.web.data.dto.AdvertisingCost.class);

			model.addAttribute("companyKcodeToNameMap", this.uco.getAllCompanyKcodeToLocalNameMap());
			model.addAttribute("advertisingCost", advertisingCost);
			model.addAttribute("rootUrl", rootUrl);
			model.addAttribute("country", country);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "SS2SP_AdvertisingCostV4";
	}
	
	@RequestMapping(value = "/{statementName}/profitshare/other-refund-and-allowance/{country}")
	public String OtherRefundAndAllowance(Model model,
			@PathVariable String statementName,
			@PathVariable String country){		
		
		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
		
		// query bus
		final Future<Object> otherRefundReportQuery = ask(drsQueryBus,
				new com.kindminds.drs.api.message.GetSs2spOtherRefundReport(statementName, country), timeout);
		
		try {			
			ObjectMapper mapper = new ObjectMapper();
			
			// get json string data
			String otherRefundReportJson = (String) Await.result(otherRefundReportQuery, timeout.duration());
			
			// map json string to object 
			OtherRefundAndAllowance otherRefundAndAllowance = mapper.readValue(otherRefundReportJson,
					com.kindminds.drs.web.data.dto.OtherRefundAndAllowance.class);

			model.addAttribute("companyKcodeToNameMap", this.uco.getAllCompanyKcodeToLocalNameMap());
			model.addAttribute("otherRefundAndAllowance", otherRefundAndAllowance);
			model.addAttribute("rootUrl", rootUrl);
			model.addAttribute("country", country);
			
		} catch (Exception e) {		
			e.printStackTrace();
		}
		
		return "th/SS2SP_statement/SS2SP_OtherRefundAndAllowance";
	}


	@RequestMapping(value = "/{statementName}/profitshare/other-refund-and-allowance/{country}/v4")
	public String OtherRefundAndAllowanceV4(Model model,
										  @PathVariable String statementName,
										  @PathVariable String country){

		Timeout timeout = new Timeout(Duration.create(10, "seconds"));

		// query bus
		final Future<Object> otherRefundReportQuery = ask(drsQueryBus,
				new com.kindminds.drs.api.message.GetSs2spOtherRefundReport(statementName, country), timeout);

		try {
			ObjectMapper mapper = new ObjectMapper();

			// get json string data
			String otherRefundReportJson = (String) Await.result(otherRefundReportQuery, timeout.duration());

			// map json string to object
			OtherRefundAndAllowance otherRefundAndAllowance = mapper.readValue(otherRefundReportJson,
					com.kindminds.drs.web.data.dto.OtherRefundAndAllowance.class);

			model.addAttribute("companyKcodeToNameMap", this.uco.getAllCompanyKcodeToLocalNameMap());
			model.addAttribute("otherRefundAndAllowance", otherRefundAndAllowance);
			model.addAttribute("rootUrl", rootUrl);
			model.addAttribute("country", country);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "SS2SP_OtherRefundAndAllowanceV4";
	}
	
	//	marketing activity expense
	@RequestMapping(value = "/{statementName}/profitshare/marketing-activity-expense/{country}")
	public String showMarketingActivityExpense(Model model,
			@PathVariable String statementName,
			@PathVariable String country){		
		
		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
		
		// query bus
		final Future<Object> marketingExpenseQuery = ask(drsQueryBus,
				new com.kindminds.drs.api.message.GetSs2spMarketingActivityExpenseReport(statementName, country), timeout);
		
		try {			
			ObjectMapper mapper = new ObjectMapper();
			
			// get json string data
			String marketingExpenseJson = (String) Await.result(marketingExpenseQuery, timeout.duration());
			
			// map json string to object 
			MarketingActivityExpense marketingActivityExpense = mapper.readValue(marketingExpenseJson,
					com.kindminds.drs.web.data.dto.MarketingActivityExpense.class);

			model.addAttribute("companyKcodeToNameMap", this.uco.getAllCompanyKcodeToLocalNameMap());
			model.addAttribute("marketingActivityExpense", marketingActivityExpense);
			model.addAttribute("rootUrl", rootUrl);
			model.addAttribute("country", country);
			
		} catch (Exception e) {		
			e.printStackTrace();
		}
		
		return "th/SS2SP_statement/SS2SP_MarketingActivityExpense";
	}


	//	marketing activity expense
	@RequestMapping(value = "/{statementName}/profitshare/marketing-activity-expense/{country}/v4")
	public String showMarketingActivityExpenseV4(Model model,
											   @PathVariable String statementName,
											   @PathVariable String country){

		Timeout timeout = new Timeout(Duration.create(10, "seconds"));

		// query bus
		final Future<Object> marketingExpenseQuery = ask(drsQueryBus,
				new com.kindminds.drs.api.message.GetSs2spMarketingActivityExpenseReport(statementName, country), timeout);

		try {
			ObjectMapper mapper = new ObjectMapper();

			// get json string data
			String marketingExpenseJson = (String) Await.result(marketingExpenseQuery, timeout.duration());

			// map json string to object
			MarketingActivityExpense marketingActivityExpense = mapper.readValue(marketingExpenseJson,
					com.kindminds.drs.web.data.dto.MarketingActivityExpense.class);

			model.addAttribute("companyKcodeToNameMap", this.uco.getAllCompanyKcodeToLocalNameMap());
			model.addAttribute("marketingActivityExpense", marketingActivityExpense);
			model.addAttribute("rootUrl", rootUrl);
			model.addAttribute("country", country);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "SS2SP_MarketingActivityExpenseV4";
	}

	@RequestMapping(value = "/{statementName}/profitshare/partial-refund/{country}")
	public String showPartialRefund(Model model,
											   @PathVariable String statementName,
											   @PathVariable String country){

		Timeout timeout = new Timeout(Duration.create(10, "seconds"));


		try {
			ObjectMapper mapper = new ObjectMapper();

			ProfitSharePartialRefundReport pspr  = this.uco.getPartialRefundReport(statementName,country);

			model.addAttribute("companyKcodeToNameMap", this.uco.getAllCompanyKcodeToLocalNameMap());
			model.addAttribute("partialRefund", pspr);
			model.addAttribute("rootUrl", rootUrl);
			model.addAttribute("country", country);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "th/SS2SP_statement/SS2SP_PartialRefund";
	}

		
}

