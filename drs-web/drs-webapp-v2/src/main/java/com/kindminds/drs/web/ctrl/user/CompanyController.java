package com.kindminds.drs.web.ctrl.user;

import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;
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

import com.kindminds.drs.Context;
import com.kindminds.drs.api.usecase.MaintainCompanyUco;
import com.kindminds.drs.api.v1.model.Company;
import com.kindminds.drs.api.v1.model.common.DtoList;
import com.kindminds.drs.api.v1.model.common.Pager;
import com.kindminds.drs.service.util.SpringAppCtx;
import com.kindminds.drs.util.JsonHelper;
import com.kindminds.drs.web.data.dto.CompanyImpl;

@Controller
public class CompanyController {
		
	private MaintainCompanyUco getMaintainCompanyUco(){		
		return (MaintainCompanyUco)(SpringAppCtx.get().getBean("maintainCompanyUco"));				
	}
	
	//TODO
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('COMPANIES_CREATE'))")
	@RequestMapping(value = "/Companies/create", method = RequestMethod.GET)	
	public String createCompany(@ModelAttribute("Company") CompanyImpl company,Model model,Locale locale) {
		MessageSource source = (MessageSource) SpringAppCtx.get().getBean("messageSource");				
		model.addAttribute("Company",company);
		model.addAttribute("CompanyJson",JsonHelper.toJson(company));
		model.addAttribute("title",source.getMessage("company.create",null, locale));						
		return "Company";		
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('COMPANIES_LIST'))")
	@RequestMapping(value = "/Companies")	
	public String listCompanies(@RequestParam(value="page",defaultValue="1",required=false) Integer pageIndex,Model model){				
		DtoList<Company> companyList = this.getMaintainCompanyUco().retrieveSupplierList(pageIndex);
		Pager page = companyList.getPager();				
		model.addAttribute("totalPages",page.getTotalPages());
		model.addAttribute("currentPageIndex",page.getCurrentPageIndex());
		model.addAttribute("startPage",page.getStartPage());
		model.addAttribute("endPage",page.getEndPage());						
		model.addAttribute("CompanyList", companyList.getItems());									
		return "ListOfCompanies";		
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('COMPANIES_VIEW'))")
	@RequestMapping(value = "/Companies/{companyKCode}")	
	public String showCompany(@PathVariable String companyKCode,Model model) {		
		Company company = this.getMaintainCompanyUco().getCompany(companyKCode);
		model.addAttribute("Company",company);
		return "CompanyView";		
    }
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('COMPANIES_EDIT_BY_ONE'))")	
	@RequestMapping(value = "/Company/edit")
	public String editCompanyByOne(Model model){
		Company company = this.getMaintainCompanyUco().getCompany(Context.getCurrentUser().getCompanyKcode());
		List<String> supplierUserEmailList = this.getMaintainCompanyUco().getSupplierUserEmailList(company.getKcode());		
		model.addAttribute("supplierUserEmailList",supplierUserEmailList);
		model.addAttribute("CompanyJson",JsonHelper.toJson(company));
		model.addAttribute("Company",company);		
		return "CompanyEditByOne";		
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('COMPANIES_VIEW_BY_ONE'))")	
	@RequestMapping(value = "/Company/view")
	public String showCompanyByOne(Model model){
		Company company = this.getMaintainCompanyUco().getCompany(Context.getCurrentUser().getCompanyKcode());
		List<String> supplierUserEmailList = this.getMaintainCompanyUco().getSupplierUserEmailList(company.getKcode());			
		model.addAttribute("supplierUserEmailList",supplierUserEmailList);
		model.addAttribute("Company",company);		
		return "CompanyViewByOne";		
	}
		
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('COMPANIES_VIEW'))")
	@RequestMapping(value = "/Companies/save", method = RequestMethod.POST)
	public String saveCompany(@ModelAttribute("Company") CompanyImpl company,Model model){				
		String kCode = this.getMaintainCompanyUco().createSupplier(company);						
		return "redirect:/Companies/"+kCode;		
	}	
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('COMPANIES_VIEW'))")
	@RequestMapping(value = "/Companies/update", method = RequestMethod.POST)
	public String updateCompany(@ModelAttribute("Company") CompanyImpl company,Model model){				
		String kCode = this.getMaintainCompanyUco().update(company);					
		return "redirect:/Companies/"+kCode;		
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('COMPANIES_EDIT_BY_ONE'))")	
	@RequestMapping(value = "/Company/update", method = RequestMethod.POST)	
	public String updateCompanyBySupplier(@ModelAttribute("Company") CompanyImpl company,Model model,final RedirectAttributes redirectAttributes, Locale locale){			
		this.getMaintainCompanyUco().updatePartial(company);		
		MessageSource source = (MessageSource)SpringAppCtx.get().getBean("messageSource");
		String message = source.getMessage("company.dataSaved", null, locale);
		redirectAttributes.addFlashAttribute("message",message);		
		return "redirect:/Company/edit";		
	}
			
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('COMPANIES_EDIT'))")
	@RequestMapping(value = "/Companies/{companyKCode}/edit", method = RequestMethod.GET)	
	public String editCompany(@PathVariable String companyKCode,Model model,Locale locale) {				
		MessageSource source = (MessageSource) SpringAppCtx.get().getBean("messageSource");
		Company company = this.getMaintainCompanyUco().getCompany(companyKCode);
		List<String> supplierUserEmailList = this.getMaintainCompanyUco().getSupplierUserEmailList(companyKCode);			
		model.addAttribute("supplierUserEmailList",supplierUserEmailList);		
		model.addAttribute("Company",company);
		model.addAttribute("CompanyJson",JsonHelper.toJson(company));		
		model.addAttribute("title",source.getMessage("company.editCompany",new Object[]{company.getShortNameLocal()}, locale));				
		return "Company";		
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('COMPANIES_DELETE'))")
	@RequestMapping(value = "/Companies/{companyKCode}/delete")	
	public String deleteCompany(@PathVariable String companyKCode,final RedirectAttributes redirectAttributes, Locale locale) {				
		this.getMaintainCompanyUco().delete(companyKCode);				
		MessageSource source = (MessageSource)SpringAppCtx.get().getBean("messageSource");
		String message = source.getMessage("company.deleteMsg", new Object[] {companyKCode}, locale);
		redirectAttributes.addFlashAttribute("message", message);						
		return "redirect:/Companies";		
	}
	
	@RequestMapping(value = "/Companies/isCouponUnique", method = RequestMethod.GET)
	public @ResponseBody String isCouponUnique(@RequestParam("couponKeyword") String couponKeyword,@RequestParam("companyKCode") String companyKCode){
		boolean couponUnique = this.getMaintainCompanyUco().isCouponUnique(couponKeyword,companyKCode);
		return JsonHelper.toJson(couponUnique);
	} 

}