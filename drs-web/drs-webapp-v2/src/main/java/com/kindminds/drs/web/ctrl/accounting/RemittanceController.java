package com.kindminds.drs.web.ctrl.accounting;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.usecase.MaintainRemittanceUco;
import com.kindminds.drs.api.v1.model.accounting.Remittance;
import com.kindminds.drs.api.v1.model.common.DtoList;
import com.kindminds.drs.api.v1.model.common.Pager;
import com.kindminds.drs.service.util.SpringAppCtx;
import com.kindminds.drs.util.JsonHelper;
import com.kindminds.drs.web.data.dto.RemittanceImpl;
import com.kindminds.drs.web.data.dto.RemittanceSearchConditionImpl;

@Controller
@RequestMapping(value = "/Remittance")
public class RemittanceController {
		
	private MaintainRemittanceUco getMaintainRemittanceUco(){		
		return (MaintainRemittanceUco)(SpringAppCtx.get().getBean("maintainRemittanceUco"));		
	}
	

		
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('REMITTANCE_LIST'))")
	@RequestMapping(value = "",method = RequestMethod.GET)
	public String listRemittances(@ModelAttribute("RemittanceListSearchCondition") RemittanceSearchConditionImpl condition,@RequestParam(value="page",defaultValue="1",required=false) Integer pageIndex,Model model) {				
		DtoList<Remittance> remittanceList = this.getMaintainRemittanceUco().retrieveList(condition,pageIndex);
		Pager page = remittanceList.getPager();
		Map<String, String> senderAndReceiverCompanies = this.getMaintainRemittanceUco().getKcodeToNameMap();		
		model.addAttribute("SenderAndReceiverCompanies", senderAndReceiverCompanies);			
		model.addAttribute("totalPages",page.getTotalPages());
		model.addAttribute("currentPageIndex",page.getCurrentPageIndex());
		model.addAttribute("startPage",page.getStartPage());
		model.addAttribute("endPage",page.getEndPage());						
		model.addAttribute("RemittanceList", remittanceList.getItems());
		model.addAttribute("RemittanceListSearchConditions", this.getRemittanceListSearchConditions(condition));
		return "ListOfRemittances";		
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('REMITTANCE_CREATE'))")
	@RequestMapping(value = "/import",method = RequestMethod.POST)
	public String importRemittances(
			RedirectAttributes redirectAttributes, @RequestParam("file") MultipartFile file) {
		String result;
		try {
			result = getMaintainRemittanceUco().importRemittanceData(file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
			result = "There are some problems with the upload file";
		}
		redirectAttributes.addFlashAttribute("message",result);
		return "redirect:/Remittance";		
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('REMITTANCE_CREATE'))")
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String createRemittance(@ModelAttribute("Remittance") RemittanceImpl remittance,Model model,Locale locale) {
		MessageSource source = (MessageSource)SpringAppCtx.get().getBean("messageSource");		
		Map<String, String> senderAndReceiverCompanies = this.getMaintainRemittanceUco().getKcodeToNameMap();				
		model.addAttribute("SenderAndReceiverCompanies", senderAndReceiverCompanies);		
		model.addAttribute("Remittance",remittance);
		model.addAttribute("RemittanceJson",JsonHelper.toJson(remittance));
		model.addAttribute("earliestAvailableUtcDate",this.getMaintainRemittanceUco().getEarliestAvailableUtcDate());
		model.addAttribute("title",source.getMessage("remittance.create",null, locale));		
		model.addAttribute("actionURL","/Remittance/save");
		model.addAttribute("type", "Create");				
		return "Remittance";		
	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('REMITTANCE_VIEW'))")
	@RequestMapping(value = "/{remittanceId}")
	public String showRemittance(Model model, @PathVariable int remittanceId) {
		Remittance remittance = this.getMaintainRemittanceUco().retrieve(remittanceId);
		Map<String, String> senderAndReceiverCompanies = this.getMaintainRemittanceUco().getKcodeToNameMap();										
		model.addAttribute("SenderAndReceiverCompanies",senderAndReceiverCompanies);		
		model.addAttribute("Remittance", remittance);		
		return "RemittanceView";		
	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('REMITTANCE_VIEW'))")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String saveRemittance(@ModelAttribute("Remittance") RemittanceImpl remittance,Model model) {		
		this.setAmountByCurrency(remittance);
		int remittanceId = this.getMaintainRemittanceUco().create(remittance);		
		return "redirect:/Remittance/"+remittanceId;		
	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('REMITTANCE_EDIT'))")
	@RequestMapping(value = "/{remittanceId}/edit", method = RequestMethod.GET)
	public String editRemittance(@PathVariable int remittanceId,Model model,Locale locale) {		
		MessageSource source = (MessageSource)SpringAppCtx.get().getBean("messageSource");				
		Remittance remittance = this.getMaintainRemittanceUco().retrieve(remittanceId);
		Map<String, String> senderAndReceiverCompanies = this.getMaintainRemittanceUco().getKcodeToNameMap();						
		model.addAttribute("SenderAndReceiverCompanies", senderAndReceiverCompanies);				
		model.addAttribute("Remittance", remittance);
		model.addAttribute("RemittanceJson",JsonHelper.toJson(remittance));
		model.addAttribute("earliestAvailableUtcDate",this.getMaintainRemittanceUco().getEarliestAvailableUtcDate());
		model.addAttribute("title",source.getMessage("remittance.editRemittance",null, locale));
		model.addAttribute("actionURL","/Remittance/update");
		model.addAttribute("type", "Edit");			
		return "Remittance";		
	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('REMITTANCE_VIEW'))")
	@RequestMapping(value = "/update", method = RequestMethod.POST)	
	public String updateRemittance(@ModelAttribute("Remittance") RemittanceImpl remittance,Model model){		
		this.setAmountByCurrency(remittance);		
		int remittanceId = this.getMaintainRemittanceUco().update(remittance);		
		return "redirect:/Remittance/"+remittanceId;		
	}
		
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('REMITTANCE_DELETE'))")
	@RequestMapping(value = "/delete/{remittanceId}")
	public String deleteRemittance(Model model, @PathVariable int remittanceId,final RedirectAttributes redirectAttributes) {				
		this.getMaintainRemittanceUco().delete(remittanceId);		
		redirectAttributes.addFlashAttribute("message","Remittance ID "+remittanceId+" has been deleted.");			
		return "redirect:/Remittance";		
	}

	private Remittance setAmountByCurrency(RemittanceImpl remittance){		
		double amount = Double.parseDouble(remittance.getAmount());	
	    String amountStr = "";		
		if(remittance.getCurrency() == Currency.TWD){						
			amountStr = String.valueOf((int)Math.round(amount));
			remittance.setAmount(amountStr);		
		}else{	
			DecimalFormat df = new DecimalFormat("##.00");										
			amountStr = String.valueOf(Double.parseDouble(df.format(amount)));			
		}		
		remittance.setAmount(amountStr);		
		return remittance;		
	}
	
	private Map<String,Object> getRemittanceListSearchConditions(RemittanceSearchConditionImpl conditions){
		Map<String,Object> remittanceListSearchConditions = new TreeMap<String,Object>();
		remittanceListSearchConditions.put("sndrKcode",conditions.getSndrKcode());
		remittanceListSearchConditions.put("rcvrKcode",conditions.getRcvrKcode());
		return remittanceListSearchConditions;
	}
	
}