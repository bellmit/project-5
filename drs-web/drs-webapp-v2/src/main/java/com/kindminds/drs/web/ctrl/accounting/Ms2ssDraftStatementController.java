package com.kindminds.drs.web.ctrl.accounting;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kindminds.drs.api.usecase.accounting.DoMs2ssSettlementUco;
import com.kindminds.drs.api.usecase.accounting.ViewMs2ssStatementUco;

import com.kindminds.drs.api.v1.model.report.Ms2ssPaymentAndRefundReport;
import com.kindminds.drs.api.v1.model.report.Ms2ssPurchaseAllowanceReport;
import com.kindminds.drs.api.v1.model.report.Ms2ssSettleableItemReport;
import com.kindminds.drs.api.v1.model.report.Ms2ssStatement;
import com.kindminds.drs.api.v1.model.report.StatementListReport.StatementListReportItem;
import com.kindminds.drs.enums.BillStatementType;
import com.kindminds.drs.util.JsonHelper;

@Controller
@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('MS2SS_DRAFT_STATEMENTS'))")
@RequestMapping(value = "/ms2ssdraftstatements")
public class Ms2ssDraftStatementController {
	
	@Autowired private DoMs2ssSettlementUco doMs2ssUco;
	@Autowired private ViewMs2ssStatementUco uco;
	private final BillStatementType type = BillStatementType.DRAFT;
	private static final String rootUrl = "ms2ssdraftstatements";
	
	@RequestMapping(value="")
	public String listMs2ssDraftStatements(Model model) {
		model.addAttribute("rootUrl", rootUrl);
		model.addAttribute("type","received");
		List<StatementListReportItem> temp = uco.queryStatementsRcvd(this.type).getItems();
		model.addAttribute("statements",JsonHelper.toJson(temp));
		model.addAttribute("drsCompanyKcodeToEnUsNameMap",this.uco.getDrsCompanyKcodeToEnUsNameMap());		
		return "MS2SS_ListDraftStatement";
	}
	
	@RequestMapping(value="/{statementName}")
	public String showDraftStatement(Model model, @PathVariable String statementName) {
		model.addAttribute("statementName", statementName);
		Ms2ssStatement report = this.uco.queryStatement(this.type,statementName);
		model.addAttribute("rootUrl", rootUrl);
		model.addAttribute("report", report);
		model.addAttribute("kcodeToNameMap", this.uco.getDrsCompanyKcodeToEnUsNameMap());
		return "Ms2ssDraftStatement";		
	}
	
	@RequestMapping(value="/{statementName}/PaymentRefund/{shipmentName}")		
	public String showPaymentAndRefundReport(Model model,
			@PathVariable String statementName,
			@PathVariable String shipmentName){
		Ms2ssPaymentAndRefundReport report = this.uco.getPaymentAndRefundReport(this.type,statementName, shipmentName);
		model.addAttribute("rootUrl", rootUrl);
		model.addAttribute("report",report);
		model.addAttribute("statementId",statementName);
	    model.addAttribute("currency","USD");
	    model.addAttribute("sourcePoId",shipmentName);
	    model.addAttribute("kcodeToNameMap", this.uco.getDrsCompanyKcodeToEnUsNameMap());
		return "Ms2ssDraftPaymentAndRefund";
	}
	
	@RequestMapping(value="/{statementName}/PurchaseAllowance/{shipmentName}")		
	public String showPurchaseAllowanceReport(Model model,
			@PathVariable String statementName,
			@PathVariable String shipmentName){
		Ms2ssPurchaseAllowanceReport report = this.uco.getPurchaseAllowanceReport(this.type,statementName,shipmentName);
		model.addAttribute("rootUrl", rootUrl);
		model.addAttribute("report",report);
		model.addAttribute("statementId",statementName);
	    model.addAttribute("currency","USD");
	    model.addAttribute("shipmentName",shipmentName);
	    model.addAttribute("kcodeToNameMap", this.uco.getDrsCompanyKcodeToEnUsNameMap());
	    return "Ms2ssDraftPurchaseAlwnc";
	}
	
	@RequestMapping(value="/{statementName}/PaymentRefund/{shipmentName}/{sku}/items")		
	public String showSettleableItemReportForPaymentRefund(Model model,
			@PathVariable String statementName,
			@PathVariable String shipmentName,
			@PathVariable String sku,
			@RequestParam("sid") int settleableItemId){
		Ms2ssSettleableItemReport report = this.uco.querySettleableItemReportForPaymentRefund(this.type,statementName, shipmentName, sku, settleableItemId);
		model.addAttribute("report",report);
		model.addAttribute("currency","USD");
		model.addAttribute("kcodeToNameMap", this.uco.getDrsCompanyKcodeToEnUsNameMap());
	    return "Ms2ssSettleableItemBrowser";
	}
	
	@RequestMapping(value="/{statementName}/PurchaseAllowance/{shipmentName}/{sku}/items")		
	public String showSettleableItemReportForPurchaseAllowance(Model model,
			@PathVariable String statementName,
			@PathVariable String shipmentName,
			@PathVariable String sku,
			@RequestParam("sid") int settleableItemId){
		Ms2ssSettleableItemReport report = this.uco.querySettleableItemReportForPurchaseAllowance(this.type,statementName, shipmentName, sku, settleableItemId);
		model.addAttribute("report",report);
		model.addAttribute("currency","USD");
		model.addAttribute("kcodeToNameMap", this.uco.getDrsCompanyKcodeToEnUsNameMap());
	    return "Ms2ssSettleableItemBrowser";
	}
	
	@RequestMapping(value="/{statementName}/PaymentOnBehalf")		
	public String showSettleableItemReportForPaymentOnBehalf(Model model,
			@PathVariable String statementName){
		Ms2ssSettleableItemReport report = this.uco.querySettleableItemReportForPaymentOnBehalf(this.type,statementName);
		model.addAttribute("report",report);
		model.addAttribute("currency","USD");
		model.addAttribute("kcodeToNameMap", this.uco.getDrsCompanyKcodeToEnUsNameMap());
	    return "Ms2ssSettleableItemBrowser";
	}
	
	@RequestMapping(value="/{statementName}/msdc-payment-on-behalf")		
	public String requestSettleableItemReportForMsdcPaymentOnBehalf(Model model,
			@PathVariable String statementName){
		Ms2ssSettleableItemReport report = this.uco.getMsdcPaymentOnBehalfDetail(this.type,statementName);
		model.addAttribute("report",report);
		model.addAttribute("kcodeToNameMap", this.uco.getDrsCompanyKcodeToEnUsNameMap());
	    return "Ms2ssSettleableItemBrowser";
	}
	
	@RequestMapping(value="/{statementName}/commit")
	public String commitMs2ssDraftStatements(Model model, @PathVariable String statementName,final RedirectAttributes redirectAttributes) {
		this.doMs2ssUco.commitDraft(statementName);
		redirectAttributes.addFlashAttribute("message","The MS2SS draft statement "+ statementName +" is successfully stored as an official statement");		
		return "redirect:/"+rootUrl;		
	}
	
	@RequestMapping(value="/commitAll")
	public String commitAllMs2ssDraftStatements(Model model,final RedirectAttributes redirectAttributes){
		this.doMs2ssUco.confirmAllDraft();
		redirectAttributes.addFlashAttribute("message","All draft statements has been confirmed to be official statements");		
		return "redirect:/"+rootUrl;
	}
		
	@RequestMapping(value="/{statementName}/discard")
	public String discardMs2ssDraftStatements(Model model, @PathVariable String statementName,final RedirectAttributes redirectAttributes) {
		this.doMs2ssUco.deleteDraft(statementName);
		redirectAttributes.addFlashAttribute("message","The MS2SS draft statement is successfully deleted");				
		return "redirect:/"+rootUrl;		
	}
		
	@RequestMapping(value="/createMs2ssDraftStatements", method = RequestMethod.GET)	
	public @ResponseBody String createMs2ssDraftStatements(@RequestParam("Issuer") String Issuer,@RequestParam("Receiver") String Receiver,@RequestParam("utcDateStart") String utcDateStart,@RequestParam("utcDateEnd") String utcDateEnd){							
		return JsonHelper.toJson(this.doMs2ssUco.createDraft(Issuer, Receiver, utcDateStart, utcDateEnd));		
	}
	
	@RequestMapping(value="/deleteAllDraft", method = RequestMethod.GET)	
	public @ResponseBody String deleteAllDraft(){
		//TODO UCO method about deleteAllDraft
		return JsonHelper.toJson("");	
	}
	
	@RequestMapping(value="/getMs2ssDraftStatementList", method = RequestMethod.GET)
	public @ResponseBody String getMs2ssDraftStatementList(){						
		return JsonHelper.toJson(this.uco.queryStatementsRcvd(this.type).getItems());			
	}
		
}