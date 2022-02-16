package com.kindminds.drs.web.ctrl.accounting;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kindminds.drs.api.usecase.accounting.DoMs2ssSettlementUco;
import com.kindminds.drs.api.usecase.ViewDebitCreditNoteUco;
import com.kindminds.drs.api.usecase.accounting.ViewMs2ssStatementUco;
import com.kindminds.drs.api.usecase.logistics.ViewUnsRecognizeRevenueReportUco;
import com.kindminds.drs.enums.BillStatementType;
import com.kindminds.drs.api.v1.model.accounting.DebitCreditNote;
import com.kindminds.drs.api.v1.model.report.Ms2ssPaymentAndRefundReport;
import com.kindminds.drs.api.v1.model.report.Ms2ssProductInventoryReturnReport;
import com.kindminds.drs.api.v1.model.report.Ms2ssPurchaseAllowanceReport;
import com.kindminds.drs.api.v1.model.report.Ms2ssSettleableItemReport;
import com.kindminds.drs.api.v1.model.report.Ms2ssStatement;
import com.kindminds.drs.api.v1.model.report.RemittanceReport;
import com.kindminds.drs.service.util.SpringAppCtx;

@Controller
@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('MS2SS_STATEMENTS'))")
@RequestMapping(value = "/MS2SS-Statements")
public class Ms2ssStatementController {
	
	@Autowired private ViewMs2ssStatementUco uco;
	@Autowired private ViewDebitCreditNoteUco viewDebitCreditNoteUco;
	@Autowired private ViewUnsRecognizeRevenueReportUco viewUnsRecognizeRevenueReportUco;
	
	private BillStatementType type=BillStatementType.OFFICIAL;
	
	@RequestMapping(value="/received")
	public String listStatementsReceived(Model model) {
		model.addAttribute("type","received");
		model.addAttribute("report",this.uco.queryStatementsRcvd(this.type));	
		return "MS2SS_ListStatements";
	}
	
	@RequestMapping(value="/issued")
	public String listStatementsIssued(Model model) {
		model.addAttribute("type","issued");
		model.addAttribute("statements",uco.queryStatementsIsud(this.type).getItems());	
		return "MS2SS_ListStatements";
	}
	
	@RequestMapping(value="/{statementName}")
	public String showStatement(Model model, 
			@PathVariable String statementName){
		model.addAttribute("statementName", statementName);
		Ms2ssStatement report = this.uco.queryStatement(this.type,statementName);
		model.addAttribute("report", report);
		model.addAttribute("kcodeToNameMap", this.uco.getDrsCompanyKcodeToEnUsNameMap());
		return "MS2SS_StatementView";
	}
	
	@RequestMapping(value="/{statementName}/delete")
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('MS2SS_STATEMENTS_DELETE'))")
	public String deleteStatement(Model model,@PathVariable String statementName){
		DoMs2ssSettlementUco ms2ssUco = (DoMs2ssSettlementUco)(SpringAppCtx.get().getBean("DoMs2ssSettlementUco"));	
		ms2ssUco.deleteOfficial(statementName);
		return "redirect:/MS2SS-Statements/received";
	}
	
	@RequestMapping(value="/{statementName}/productinventoryreturn")
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('MS2SS_STATEMENTS_DELETE'))")
	public String showStatementProductInventoryReturnDetail(Model model,@PathVariable String statementName){
		Ms2ssProductInventoryReturnReport report = this.uco.getProductInventoryReturnReport(this.type,statementName);
		model.addAttribute("report",report);
		model.addAttribute("statementId",statementName);
		model.addAttribute("kcodeToNameMap", this.uco.getDrsCompanyKcodeToEnUsNameMap());
		return "MS2SS_ProductInventoryReturn";
	}
	
	@RequestMapping(value="/{statementName}/PaymentRefund/{shipmentName}")		
	public String showPaymentAndRefundReport(Model model,
			@PathVariable String statementName,
			@PathVariable String shipmentName){
		Ms2ssPaymentAndRefundReport report = this.uco.getPaymentAndRefundReport(this.type,statementName, shipmentName);
		model.addAttribute("report",report);
		model.addAttribute("statementId",statementName);
	    model.addAttribute("currency","USD");
	    model.addAttribute("sourcePoId",shipmentName);
	    model.addAttribute("kcodeToNameMap", this.uco.getDrsCompanyKcodeToEnUsNameMap());
		return "MS2SS_PaymentAndRefundForPoInPeriod";
	}
	
	@RequestMapping(value="/{statementName}/PurchaseAllowance/{shipmentName}")		
	public String showPurchaseAllowanceReport(Model model,
			@PathVariable String statementName,
			@PathVariable String shipmentName){
		Ms2ssPurchaseAllowanceReport report = this.uco.getPurchaseAllowanceReport(this.type,statementName,shipmentName);
		model.addAttribute("report",report);
		model.addAttribute("statementId",statementName);
	    model.addAttribute("currency","USD");
	    model.addAttribute("shipmentName",shipmentName);
	    model.addAttribute("kcodeToNameMap", this.uco.getDrsCompanyKcodeToEnUsNameMap());
	    return "Ms2ssPurchaseAllowanceReport";
	}
	
	@RequestMapping(value="/{statementName}/MS2SS-RemittanceReceived")	
	public String showRemittanceSent(Model model, @PathVariable String statementName){
		RemittanceReport report = this.uco.getRemittanceIsurToRcvrReport(this.type,statementName);
		model.addAttribute("displayText","Remittance Received From");
		model.addAttribute("report",report);
		model.addAttribute("kcodeToNameMap", this.uco.getDrsCompanyKcodeToEnUsNameMap());
		return "MS2SS_Remittance";
	}
	
	@RequestMapping(value="/{statementName}/MS2SS-RemittanceSent")	
	public String showRemittanceReceived(Model model, @PathVariable String statementName){
		RemittanceReport report = this.uco.getRemittanceRcvrToIsurReport(this.type,statementName);
		model.addAttribute("displayText","Remittance Made To");
		model.addAttribute("report",report);
		model.addAttribute("kcodeToNameMap", this.uco.getDrsCompanyKcodeToEnUsNameMap());
		return "MS2SS_Remittance";
	}
	
	@RequestMapping(value="/{statementName}/PaymentRefund/{shipmentName}/{sku}/items")		
	public String showSettleableItemReportForPaymentRefund(Model model,
			@PathVariable String statementName,
			@PathVariable String shipmentName,
			@PathVariable String sku,
			@RequestParam("sid") int settleableItemId){
		Ms2ssSettleableItemReport report = uco.querySettleableItemReportForPaymentRefund(this.type,statementName, shipmentName, sku, settleableItemId);
		model.addAttribute("report",report);
		model.addAttribute("kcodeToNameMap", this.uco.getDrsCompanyKcodeToEnUsNameMap());
	    return "Ms2ssSettleableItemBrowser";
	}
	
	@RequestMapping(value="/{statementName}/PurchaseAllowance/{shipmentName}/{sku}/items")		
	public String showSettleableItemReportForPurchaseAllowance(Model model,
			@PathVariable String statementName,
			@PathVariable String shipmentName,
			@PathVariable String sku,
			@RequestParam("sid") int settleableItemId){
		Ms2ssSettleableItemReport report = uco.querySettleableItemReportForPurchaseAllowance(this.type,statementName, shipmentName, sku, settleableItemId);
		model.addAttribute("report",report);
		model.addAttribute("kcodeToNameMap", this.uco.getDrsCompanyKcodeToEnUsNameMap());
	    return "Ms2ssSettleableItemBrowser";
	}
	
	@RequestMapping(value="/{statementName}/PaymentOnBehalf")		
	public String showSettleableItemReportForPaymentOnBehalf(Model model,
			@PathVariable String statementName){
		Ms2ssSettleableItemReport report = this.uco.querySettleableItemReportForPaymentOnBehalf(this.type,statementName);
		model.addAttribute("report",report);
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
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DEBIT_NOTE_VIEW'))")	
	@RequestMapping(value="/DebitCreditNote/{statementName}")
	public String showDebitCreditNote(Model model,@PathVariable String statementName){		
		DebitCreditNote DebitCreditNote = viewDebitCreditNoteUco.getDebitCreditNote(statementName);
		model.addAttribute("statementName",statementName);
		model.addAttribute("debitCreditNote",DebitCreditNote);
		return "debitCreditNote";	
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DEBIT_NOTE_DOWNLOAD'))")
	@RequestMapping(value="/DebitCreditNoteReport/{statementName}")
	public void getDebitCreditNoteReport(HttpServletResponse response,@PathVariable String statementName) throws IOException{
		response.setContentType("text/csv");
		String csvFileName = "debit-credit-note-"+statementName+".tsv";
		String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",csvFileName);
        response.setHeader(headerKey, headerValue);
        response.getOutputStream().print(this.viewDebitCreditNoteUco.getTsvReport(statementName));
        response.getOutputStream().flush();
	} 
		
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('UNS_RECOGNIZE_REVENUE_REPORT'))")	
	@RequestMapping(value="/revenue-recognize-report/{statementName}")
	public void getUnsRecognizeRevenueReport(HttpServletResponse response,@PathVariable String statementName) throws IOException{
		response.setContentType("text/csv");
		String csvFileName = "uns-recognize-revenue-"+statementName+".tsv";
		String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",csvFileName);
        response.setHeader(headerKey, headerValue);
        response.getOutputStream().print(this.viewUnsRecognizeRevenueReportUco.getTsvReport(statementName));
        response.getOutputStream().flush();
	}
		
}
