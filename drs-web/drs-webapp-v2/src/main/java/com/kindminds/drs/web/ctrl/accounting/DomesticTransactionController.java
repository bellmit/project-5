package com.kindminds.drs.web.ctrl.accounting;

import java.util.Locale;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kindminds.drs.api.usecase.accounting.MaintainDomesticTransactionUco;
import com.kindminds.drs.api.v1.model.accounting.DomesticTransaction;
import com.kindminds.drs.api.v1.model.common.DtoList;
import com.kindminds.drs.api.v1.model.common.Pager;
import com.kindminds.drs.service.util.SpringAppCtx;
import com.kindminds.drs.util.JsonHelper;
import com.kindminds.drs.web.data.dto.DomesticTransactionImpl;

@Controller
@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DOMESTIC_TRANSACTION'))")
@RequestMapping(value = "/DomesticTransactions")
public class DomesticTransactionController {

	public MaintainDomesticTransactionUco getUco(){
		return (MaintainDomesticTransactionUco)(SpringAppCtx.get().getBean("maintainDomesticTransactionUcoImpl"));
	}
		
	@RequestMapping(value = "", method = RequestMethod.GET)		
	public String listDomesticTransactions(@RequestParam(value="page",defaultValue="1",required=false) Integer pageIndex,Model model){		
		DtoList<DomesticTransaction> transactionList = this.getUco().getList(pageIndex);
		Pager page = transactionList.getPager();
		model.addAttribute("totalPages",page.getTotalPages());
		model.addAttribute("currentPageIndex",page.getCurrentPageIndex());
		model.addAttribute("startPage",page.getStartPage());
		model.addAttribute("endPage",page.getEndPage());						
		model.addAttribute("TransactionList", transactionList.getItems());		
		return "listOfDomesticTransactions";	
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)			
	public String createDomesticTransactionPage(@ModelAttribute("DomesticTransaction") DomesticTransactionImpl domesticTransaction,Model model){				
		model.addAttribute("DomesticTransaction",domesticTransaction);		
		model.addAttribute("DomesticTransactionJson",JsonHelper.toJson(null));
		model.addAttribute("defaultSalesTaxPercentage", JsonHelper.toJson(this.getUco().getDefaultSalesTaxPercentage()));
		model.addAttribute("earliestAvailableUtcDate",this.getUco().getEarliestAvailableUtcDate());
		model.addAttribute("ssdcKcodeNameMap",this.getUco().getSsdcKcodeNames());
		model.addAttribute("lineItemTypeKeyNameMap",this.getUco().getLineItemTypeKeyName());						
		model.addAttribute("type", "Create");
		model.addAttribute("isAllEditable", true);
		return "domesticTransaction";				
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)	
	public String saveDomesticTransaction(@ModelAttribute("DomesticTransaction") DomesticTransactionImpl domesticTransaction,Model model){		
		Integer id = this.getUco().create(domesticTransaction);
		return "redirect:/DomesticTransactions/"+id;				
	}
	
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)				
	public String editDomesticTransactionPage(@PathVariable Integer id,Model model){		
		DomesticTransaction transaction = this.getUco().get(id);
		if(!transaction.isEditable()) return "accessDeny";
		DomesticTransactionImpl t = new DomesticTransactionImpl(transaction);
		model.addAttribute("DomesticTransaction",t);
		model.addAttribute("DomesticTransactionJson",JsonHelper.toJson(t));					
		model.addAttribute("defaultSalesTaxPercentage", JsonHelper.toJson(transaction.getTaxPercentage()));		
		model.addAttribute("earliestAvailableUtcDate", getUco().getEarliestAvailableUtcDate());
		model.addAttribute("ssdcKcodeNameMap", getUco().getSsdcKcodeNames());
		model.addAttribute("lineItemTypeKeyNameMap", getUco().getLineItemTypeKeyName());
		model.addAttribute("type", "Edit");
		model.addAttribute("isAllEditable", getUco().isAllEditable(transaction.getTransactionDate()));
		return "domesticTransaction";				
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)		
	public String updateDomesticTransaction(@ModelAttribute("DomesticTransaction") DomesticTransactionImpl domesticTransaction,Model model){				
		Integer id = this.getUco().update(domesticTransaction);

		return "redirect:/DomesticTransactions/"+id;				
	}
	
	@RequestMapping(value = "/{id}")		
	public String showDomesticTransaction(@PathVariable int id,Model model){
		DomesticTransaction transaction = this.getUco().get(id);
		model.addAttribute("t",transaction);			
		return "domesticTransactionView";		
	}
	
	@RequestMapping(value = "/{id}/delete")			
	public String deleteDomesticTransaction(@PathVariable Integer id,final RedirectAttributes redirectAttributes, Locale locale){
		this.getUco().delete(id);
		redirectAttributes.addFlashAttribute("message", "This transaction has been deleted.");						
		return "redirect:/DomesticTransactions";						
	}
	
	@RequestMapping(value = "/getCurrency", method = RequestMethod.GET)		
	public @ResponseBody String getCurrency(@RequestParam("ssdcKcode") String ssdcKcode){		
		return JsonHelper.toJson(this.getUco().getCurrency(ssdcKcode));		
	}
		
	@RequestMapping(value = "/getSplrKcodeNameMap", method = RequestMethod.GET)	
	public @ResponseBody String getSplrKcodeNameMap(@RequestParam("ssdcKcode") String ssdcKcode){
		return JsonHelper.toJson(this.getUco().getSplrKcodeNames(ssdcKcode));				
	}
	
}