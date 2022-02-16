package com.kindminds.drs.web.ctrl.accounting;


import static akka.pattern.Patterns.ask;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.util.Timeout;
import com.kindminds.drs.api.message.command.CommitSettlement;
import com.kindminds.drs.web.config.DrsActorSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kindminds.drs.Country;
import com.kindminds.drs.TransactionLineType;
import com.kindminds.drs.api.usecase.accounting.DoSs2spSettlementUco;
import com.kindminds.drs.api.usecase.accounting.ViewSs2spStatementUco;
import com.kindminds.drs.enums.BillStatementType;
import com.kindminds.drs.api.v1.model.report.RemittanceReport;
import com.kindminds.drs.api.v1.model.report.Ss2spCustomerCareCostReport;
import com.kindminds.drs.api.v1.model.report.Ss2spPaymentAndRefundReport;
import com.kindminds.drs.api.v1.model.report.Ss2spPaymentAndRefundReportItemToExport;
import com.kindminds.drs.api.v1.model.report.Ss2spProfitShareReport;
import com.kindminds.drs.api.v1.model.report.Ss2spSellBackReport;
import com.kindminds.drs.api.v1.model.report.Ss2spProfitShareDetailReport;
import com.kindminds.drs.api.v1.model.report.Ss2spServiceExpenseReport;
import com.kindminds.drs.api.v1.model.report.Ss2spSettleableBrowserForProfitShareToExport;
import com.kindminds.drs.api.v1.model.report.Ss2spSettleableItemReport;
import com.kindminds.drs.api.v1.model.report.StatementListReport;
import com.kindminds.drs.api.v1.model.report.Ss2spPaymentAndRefundReport.Ss2spPaymentAndRefundReportItem;
import com.kindminds.drs.api.v1.model.report.Ss2spProfitShareReport.Ss2spProfitShareReportLineItem;
import com.kindminds.drs.api.v1.model.report.Ss2spStatementReport;
import com.kindminds.drs.service.util.SpringAppCtx;
import com.kindminds.drs.util.JsonHelper;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;


@Controller
@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('SS2SP_DRAFT_STATEMENTS'))")
public class Ss2spDraftStatementController {
	
	@Autowired 
	@Qualifier("ViewSs2spStatementUco4Fe")
	private ViewSs2spStatementUco uco;

	private MessageSource getMessageSourceBean(){
		return (MessageSource)SpringAppCtx.get().getBean("messageSource");
	}


	private ActorRef drsCmdBus = DrsActorSystem.drsCmdBus();
	
	private BillStatementType type=BillStatementType.DRAFT;

	private static final String rootUrl = "draft-statements";
	private static final String profitShareUrl = "profitshare";
	
	private DoSs2spSettlementUco getSettlementUco() {
		return (DoSs2spSettlementUco)(SpringAppCtx.get().getBean("statementClose")); 
	}
	
	@RequestMapping(value=rootUrl)
	public String getSs2spDraftStatements(Model model) {
		Map<String, String> supplierKcodeToCompanyNameMap = this.getSettlementUco().getSupplierKcodeToCompanyNameMap();
		model.addAttribute("rootUrl", rootUrl);
		model.addAttribute("supplierKcodeToCompanyNameMap",supplierKcodeToCompanyNameMap);
		model.addAttribute("ss2spDraftStatementList",this.uco.getSs2spStatementListReport(this.type,null));
		return "draftStatements";
	}
	
	@RequestMapping(value="ss2spdraftstatements")
	public String listSs2spDraftStatements(Model model) {
		StatementListReport ss2spDraftStatementList = this.uco.getSs2spStatementListReport(this.type,null);
		Map<String, String> supplierKcodeToCompanyNameMap = this.getSettlementUco().getSupplierKcodeToCompanyNameMap();				
		model.addAttribute("rootUrl", rootUrl);
		model.addAttribute("supplierKcodeToCompanyNameMap",supplierKcodeToCompanyNameMap);
		model.addAttribute("ss2spDraftStatementList",JsonHelper.toJson(ss2spDraftStatementList));
		return "Ss2spDraftStatementList";
	}
	
	/* Statement Level 1 */
	@RequestMapping(value=rootUrl+"/{statementId}")
	public String showSs2spDraftStatements(Model model, @PathVariable String statementId, Locale locale) {
		Ss2spStatementReport report = this.uco.getSs2spStatement(this.type,statementId);
		String balanceNoteMessageKey = this.uco.getBalanceNoteMessageKey(statementId, report.getDateStart(),report.getBalance(),locale);
		model.addAttribute("companyKcodeToNameMap", this.uco.getAllCompanyKcodeToLocalNameMap());
		model.addAttribute("report", report);
		model.addAttribute("statementName", statementId);
		model.addAttribute("rootUrl", rootUrl);
		model.addAttribute("balanceNote", balanceNoteMessageKey==null?"":this.getMessageSourceBean().getMessage(balanceNoteMessageKey,new Object[] {statementId.replace("DFT-STM-","")},locale));
		model.addAttribute("profitShareUrl",profitShareUrl);
		return "Ss2spDraftStatementView";
	}
	
	/* Statement Level 1-1 */
	@RequestMapping(value=rootUrl+"/{statementName}/"+profitShareUrl)
	public String showSs2spProfitShareList(Model model,@PathVariable String statementName){
		Ss2spProfitShareReport report = this.uco.getSs2spProfitShareReport(this.type,statementName);
		Map<String, Ss2spProfitShareDetailReport> profitShareDetailReport = new HashMap<>();	
		for(Ss2spProfitShareReportLineItem lineItem :report.getLineItems()){
			profitShareDetailReport.put(lineItem.getSourceCountry(), uco.getSs2spProfitShareDetailReport(this.type,statementName,lineItem.getSourceCountry()));			
		}
		model.addAttribute("rootUrl", rootUrl);
		model.addAttribute("report", report);
		model.addAttribute("statementName",statementName);
		model.addAttribute("companyKcodeToNameMap", this.uco.getAllCompanyKcodeToLocalNameMap());
		model.addAttribute("monthInfoForMonthStorageFee",this.uco.getMonthInfoForMonthStorageFee(report.getDateStart()));
		model.addAttribute("profitShareDetailReport", profitShareDetailReport);		
		return "SS2SP_ProfitShareList_V3";
	}
	
	/* Statement Level 1-1-1-1 */
	@RequestMapping(value=rootUrl+"/{statementName}/profitshare/{country}/{sku}")
	public String showSs2spSettleableBrowserForProfitShare(Model model,
			@PathVariable String statementName,
			@PathVariable String country,
			@PathVariable String sku){
		model.addAttribute("rootUrl", rootUrl);
		model.addAttribute("report",this.uco.getSs2spSkuProfitShareDetailReport(this.type,statementName, country, sku));
		model.addAttribute("currency",Country.valueOf(country).getCurrency());
		model.addAttribute("companyKcodeToNameMap", this.uco.getAllCompanyKcodeToLocalNameMap());
		return "SS2SP_SkuCountryProfitShare";
	}

	@RequestMapping(value=rootUrl+"/{statementName}/profitshare/{country}/export")	
	public String showSs2spSettleableBrowserForProfitShareToExport(Model model,
			@PathVariable String statementName,
			@PathVariable String country){		
		List<Ss2spSettleableBrowserForProfitShareToExport> itemList = this.uco.getSs2spSettleableBrowserForProfitShareToExport(this.type,statementName,country);
		model.addAttribute("itemList",itemList);
		return "SS2SP_SkuCountryProfitShareToExport";
	}
		
	@RequestMapping(value=rootUrl+"/{statementName}/"+profitShareUrl+"/{country}/MS2SS_PURCHASE_ALWNC_CUSTOMERCARE")	
	public String showSs2spCustomerCareCaseItemsRelatedToBaseProduct(Model model,
			@PathVariable String statementName,
			@PathVariable String country){
		Ss2spCustomerCareCostReport report = uco.getSs2spCustomerCareCostReport(this.type,statementName,country);
		model.addAttribute("rootUrl", rootUrl);
		model.addAttribute("report",report);
		model.addAttribute("companyKcodeToNameMap", this.uco.getAllCompanyKcodeToLocalNameMap());
		return "SS2SP_CustomerCareCaseItems";
	}
	
	/* Statement Level 1-1-1-1 */
	@RequestMapping(value=rootUrl+"/{statementName}/"+profitShareUrl+"/{country}/{sku}/{itemName}")
	public String showSs2spSettleableBrowserForProfitShare(Model model,
			@PathVariable String statementName,
			@PathVariable String country,
			@PathVariable String sku,
			@PathVariable String itemName){
		if(TransactionLineType.fromName(itemName)==TransactionLineType.MS2SS_PURCHASE_ALWNC_CUSTOMERCARE){
			model.addAttribute("report",this.uco.getSs2spCustomerCareCostReport(this.type,statementName,country));
			model.addAttribute("productBaseCode","");
			return "SS2SP_CustomerCareCaseItems";
		} else {
			model.addAttribute("report",this.uco.getSs2spSettleableItemReportForProfitShare(this.type,statementName,country,sku,itemName));
			return "SS2SP_SettleableItemBrowser";
		}
	}
	
	/* Statement Level 1-2 */
	@RequestMapping(value=rootUrl+"/{statementName}/shipment/{ivsName}")
	public String showSs2spPaymentAndRefundForPoInPeriod(Model model,
			@PathVariable String statementName,
			@PathVariable String ivsName){
		Ss2spPaymentAndRefundReport report = uco.getSs2spPaymentAndRefundReport(this.type,statementName, ivsName);
		Map<String, Ss2spSettleableItemReport> ss2spSettleableItemReport = new HashMap<>();	
		for(Ss2spPaymentAndRefundReportItem lineItem :report.getLineItems()){
			ss2spSettleableItemReport.put(lineItem.getSku()+lineItem.getSettleableItemId(), uco.getSs2spSettleableItemReportForPoRelated(this.type,statementName,ivsName,lineItem.getSku(),lineItem.getSettleableItemId()));			
		}
		model.addAttribute("rootUrl", rootUrl);
		model.addAttribute("report",report);
		model.addAttribute("statementId",statementName);
	    model.addAttribute("lineItems",report.getLineItems());
	    model.addAttribute("total",report.getTotal());
	    model.addAttribute("sourcePoId",ivsName);
	    model.addAttribute("companyKcodeToNameMap", this.uco.getAllCompanyKcodeToLocalNameMap());
	    model.addAttribute("ss2spSettleableItemReport",ss2spSettleableItemReport);		
	    return "SS2SP_PaymentAndRefundForPoInPeriod";
	}
	
	@RequestMapping(value=rootUrl+"/{statementName}/shipment/{ivsName}/export")	
	public String showSs2spPaymentAndRefundForPoInPeriodItemToExport(Model model,
			@PathVariable String statementName,
			@PathVariable String ivsName){
		List<Ss2spPaymentAndRefundReportItemToExport> itemList = this.uco.getSs2spPaymentAndRefundReportItemToExport(this.type,statementName,ivsName);		
		model.addAttribute("itemList",itemList);	    			
		return "SS2SP_PaymentAndRefundForPoInPeriodItemToExport";		
	}
	
	/* Statement Level 1-2-1 */
	@RequestMapping(value=rootUrl+"/{statementName}/{shipmentId}/{sku}/items")
	public String showSs2spSettleableBrowserForPoRelated(Model model,
			@PathVariable String statementName,
			@PathVariable String shipmentId,
			@PathVariable String sku,
			@RequestParam("sid") int settleableItemId){
		Ss2spSettleableItemReport report = this.uco.getSs2spSettleableItemReportForPoRelated(this.type,statementName,shipmentId,sku,settleableItemId);
		model.addAttribute("report",report);
		model.addAttribute("companyKcodeToNameMap", this.uco.getAllCompanyKcodeToLocalNameMap());
		return "SS2SP_SettleableItemBrowser";
	}
	
	/* Statement Level 1-3 */
	@RequestMapping(value=rootUrl+"/{statementName}/sellback")
	public String showSs2spSellBackReport(Model model,
			@PathVariable String statementName){
		Ss2spSellBackReport report = uco.getSellBackReport(this.type,statementName);
		model.addAttribute("rootUrl", rootUrl);
		model.addAttribute("report",report);
		model.addAttribute("statementId",statementName);
	    model.addAttribute("companyKcodeToNameMap", this.uco.getAllCompanyKcodeToLocalNameMap());
		return "th/SS2SP_statement/SS2SP_SellBackReport";
	}
	
	/* Statement Level 1-4 */
	@RequestMapping(value=rootUrl+"/{statementName}/serviceexpense/{invoiceNumber}")
	public String showSs2spServiceExpenseReport(Model model,
			@PathVariable String statementName,
			@PathVariable String invoiceNumber){
		Ss2spServiceExpenseReport report = uco.getServiceExpenseReport(this.type,statementName,invoiceNumber);
		model.addAttribute("rootUrl", rootUrl);
		model.addAttribute("report",report);
		model.addAttribute("statementId",statementName);
		model.addAttribute("companyKcodeToNameMap", this.uco.getAllCompanyKcodeToLocalNameMap());
		return "SS2SP_ServiceExpense";
	}

	/* Statement Level 1-4 */
	@RequestMapping(value=rootUrl+"/{statementName}/serviceexpense")
	public String showAllSs2spServiceExpenseReport(Model model,
			@PathVariable String statementName){
		Ss2spServiceExpenseReport report = uco.getAllServiceExpenseReport(this.type,statementName);
		model.addAttribute("rootUrl", rootUrl);
		model.addAttribute("report",report);
		model.addAttribute("statementId",statementName);
		model.addAttribute("companyKcodeToNameMap", this.uco.getAllCompanyKcodeToLocalNameMap());
		return "th/SS2SP_statement/SS2SP_ServiceExpense";
	}
	
	/* Statement Level 1-5 */
	@RequestMapping(value=rootUrl+"/{statementName}/rmtsent")	
	public String showRemittanceSent(Model model, @PathVariable String statementName){
		RemittanceReport report = this.uco.getRemittanceRcvrToIsurReport(this.type,statementName);
		model.addAttribute("rootUrl", rootUrl);
		model.addAttribute("companyKcodeToNameMap", this.uco.getAllCompanyKcodeToLocalNameMap());
		model.addAttribute("displayText","ss2spRemittanceTo");
		model.addAttribute("report",report);
		return "SS2SP_Remittance";
	}
	
	/* Statement Level 1-6 */
	@RequestMapping(value=rootUrl+"/{statementName}/rmtrcvd")	
	public String showRemittanceReceived(Model model, @PathVariable String statementName){
		RemittanceReport report = this.uco.getRemittanceIsurToRcvrReport(this.type,statementName);
		model.addAttribute("rootUrl", rootUrl);
		model.addAttribute("companyKcodeToNameMap", this.uco.getAllCompanyKcodeToLocalNameMap());
		model.addAttribute("displayText","ss2spRemittanceFrom");
		model.addAttribute("report",report);
		return "SS2SP_Remittance";
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('SS2SP_DRAFT_STATEMENTS_COMMIT'))")		
	@RequestMapping(value=rootUrl+"/{statementId}/commit")
	public String commitSs2spDraftStatements(Model model, @PathVariable String statementId,final RedirectAttributes redirectAttributes) {
		int	id = this.getSettlementUco().confirmDraft(statementId);
		redirectAttributes.addFlashAttribute("message","The SS2SP draft statement "+ id +" is successfully stored as an official statement");	
		return "redirect:/"+rootUrl;
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('SS2SP_DRAFT_STATEMENTS_COMMIT'))")		
	@RequestMapping(value="ss2spdraftstatements/commitAll")
	public String commitAllSs2spDraftStatements(Model model,final RedirectAttributes redirectAttributes) {
		this.getSettlementUco().commitAllDraft();
		redirectAttributes.addFlashAttribute("message","All draft statements has been confirmed to be official statements");

		//todo move above to backend
		drsCmdBus.tell(new CommitSettlement(), ActorRef.noSender());

		return "redirect:/"+rootUrl;
	}
	
	@RequestMapping(value = "ss2spdraftstatements/discard/{statementId}")
	public String discardSs2spDraftStatements(Model model, @PathVariable String statementId,final RedirectAttributes redirectAttributes) {
		this.getSettlementUco().deleteDraft(statementId);
		redirectAttributes.addFlashAttribute("message","The SS2SP draft statement "
				+ statementId + " is successfully deleted");
		return "redirect:/ss2spdraftstatements";
	}
	
	@RequestMapping(value="/DraftSettlement/createSs2spDraftStatements", method = RequestMethod.GET)
	public @ResponseBody String createSs2spDraftStatements(
			@RequestParam("supplierKcode") String supplierKcode){
		String message = this.getSettlementUco().createDraft(supplierKcode);
		return JsonHelper.toJson(message);

	}
	
	@RequestMapping(value="/DraftSettlement/createSs2spDraftStatementForAll", method = RequestMethod.GET)	
	public @ResponseBody String createSs2spDraftStatementForAll(){
		String message = this.getSettlementUco().createDraftForAllSupplier();
		return JsonHelper.toJson(message);	
	}
	
	@RequestMapping(value="/DraftSettlement/deleteAllDraft", method = RequestMethod.GET)	
	public String deleteAllDraft(){
		this.getSettlementUco().deleteAllDraft();
		return "redirect:/"+rootUrl;	
	}
	
	@RequestMapping(value="/DraftSettlement/getSs2spDraftStatementList", method = RequestMethod.GET)
	public @ResponseBody String getSs2spDraftStatementList(){
		StatementListReport ss2spDraftStatementList = this.uco.getSs2spStatementListReport(this.type,null);
		return JsonHelper.toJson(ss2spDraftStatementList);
	}
			
}