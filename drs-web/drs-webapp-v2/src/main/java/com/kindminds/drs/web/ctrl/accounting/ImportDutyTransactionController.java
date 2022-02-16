package com.kindminds.drs.web.ctrl.accounting;

import java.util.List;

import com.kindminds.drs.service.usecase.accounting.MaintainImportDutyUcoImpl;
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

import com.kindminds.drs.api.usecase.MaintainImportDutyUco;
import com.kindminds.drs.api.v1.model.accounting.ImportDutyTransaction;
import com.kindminds.drs.api.v1.model.accounting.ImportDutyTransaction.ImportDutyTransactionLineItem;
import com.kindminds.drs.api.v1.model.common.DtoList;
import com.kindminds.drs.api.v1.model.common.Pager;

import com.kindminds.drs.service.util.SpringAppCtx;
import com.kindminds.drs.util.JsonHelper;
import com.kindminds.drs.web.data.dto.ImportDutyTransactionImpl;

@Controller
@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('IMPORT_DUTY_TRANSACTION'))")	
@RequestMapping(value = "/ImportDutyTransaction")
public class ImportDutyTransactionController {

	private MaintainImportDutyUco getMaintainImportDutyUco(){
		return (MaintainImportDutyUco)(SpringAppCtx.get().getBean("maintainImportDutyUcoImpl"));
	}
	
	@RequestMapping(value = "")
	public String listImportDutyTransactions(@RequestParam(value="page",defaultValue="1",required=false) Integer pageIndex,Model model){		
		DtoList<ImportDutyTransaction> ImportDutyTransactionList = this.getMaintainImportDutyUco().getList(pageIndex);
		Pager page = ImportDutyTransactionList.getPager();
		model.addAttribute("totalPages",page.getTotalPages());
		model.addAttribute("currentPageIndex",page.getCurrentPageIndex());
		model.addAttribute("startPage",page.getStartPage());
		model.addAttribute("endPage",page.getEndPage());
		model.addAttribute("ImportDutyTransactionList", ImportDutyTransactionList.getItems());		
		return "ListOfImportDutyTransactions";		
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String createImportDutyTransaction(@ModelAttribute("ImportDutyTransaction") ImportDutyTransactionImpl importDuty,Model model){				
		model.addAttribute("unsNameList",this.getMaintainImportDutyUco().getUnsNameList());
		model.addAttribute("ImportDutyTransactionItem",JsonHelper.toJson(null));
		model.addAttribute("type","Create");	
		return "ImportDutyTransaction";		
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)	
	public String saveImportDutyTransaction(@ModelAttribute("ImportDutyTransaction") ImportDutyTransactionImpl importDuty){		
		String unsName = this.getMaintainImportDutyUco().create(importDuty);		
		return "redirect:/ImportDutyTransaction/"+unsName;		
	}
	
	@RequestMapping(value = "/edit/{unsName}")		
	public String editImportDutyTransaction(@PathVariable String unsName,Model model){		
		ImportDutyTransaction importDutyTransaction  = this.getMaintainImportDutyUco().get(unsName);
		model.addAttribute("ImportDutyTransaction",new ImportDutyTransactionImpl());	
		model.addAttribute("unsNameList",this.getMaintainImportDutyUco().getUnsNameList());	
		model.addAttribute("ImportDutyTransactionItem",JsonHelper.toJson(importDutyTransaction));
		model.addAttribute("type","Edit");		
		return "ImportDutyTransaction";		
	}
		
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String updateImportDutyTransaction(@ModelAttribute("ImportDuty") ImportDutyTransactionImpl importDuty){		
		String unsName = this.getMaintainImportDutyUco().update(importDuty);		
		return "redirect:/ImportDutyTransaction/"+unsName;		
	}
	
	@RequestMapping(value = "/{unsName}")	
	public String showImportDutyTransaction(@PathVariable String unsName,Model model){		
		ImportDutyTransaction importDutyTransaction = this.getMaintainImportDutyUco().get(unsName);
		model.addAttribute("ImportDutyTransaction",importDutyTransaction);				
		return "ImportDutyTransactionView";		
	}
		
	@RequestMapping(value = "/delete/{unsName}")	
	public String deleteImportDutyTransaction(@PathVariable String unsName,final RedirectAttributes redirectAttributes){		
		this.getMaintainImportDutyUco().delete(unsName);		
		redirectAttributes.addFlashAttribute("message","Import duty transaction has been deleted.");		
		return "redirect:/ImportDutyTransaction";		
	}
		
	@RequestMapping(value = "/getLineItemInfoForCreate", method = RequestMethod.GET) 		
	public @ResponseBody String getLineItemInfoForCreate(@RequestParam("unsName") String unsName){	
		 List<ImportDutyTransactionLineItem> LineItemInfoForCreateList = this.getMaintainImportDutyUco().getLineItemInfoForCreate(unsName); 		
		 return JsonHelper.toJson(LineItemInfoForCreateList);		 
	}
	
	@RequestMapping(value = "/getCountry", method = RequestMethod.GET) 			
	public @ResponseBody String getCountry(@RequestParam("unsName") String unsName) {	
		return JsonHelper.toJson(this.getMaintainImportDutyUco().getCountry(unsName));		
	}
	
	@RequestMapping(value = "/getCurrency", method = RequestMethod.GET)	
	public @ResponseBody String getCurrency(@RequestParam("countryName") String countryName){				
		return JsonHelper.toJson(this.getMaintainImportDutyUco().getCurrency(countryName));		
	}
		
}