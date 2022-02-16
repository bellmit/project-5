package com.kindminds.drs.web.ctrl.util;

import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.kindminds.drs.api.usecase.MaintainSupplementalLinkUco;
import com.kindminds.drs.api.v1.model.SupplementalLink;
import com.kindminds.drs.service.util.SpringAppCtx;
import com.kindminds.drs.util.JsonHelper;
import com.kindminds.drs.web.data.dto.SupplementalLinkImpl;

@Controller
@RequestMapping(value = "/SupplementalLinks")
public class SupplementalLinkController {
	
	private MaintainSupplementalLinkUco getMaintainSupplementalLinkUco(){				
		return (MaintainSupplementalLinkUco)(SpringAppCtx.get().getBean("maintainSupplementalLinkUco"));		
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('SUPPLEMENTAL_LINKS_LIST'))")	
	@RequestMapping(value = "")	
	public String listSupplementalLinks(Model model){		
		model.addAttribute("supplierKcodeToNameMap",this.getMaintainSupplementalLinkUco().getSupplierKcodeToNameMap());		
		model.addAttribute("SupplementalLinkList",JsonHelper.toJson(this.getMaintainSupplementalLinkUco().getList()));		
		return "ListOfSupplementalLinks";		
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('SUPPLEMENTAL_LINKS_CREATE'))")		
	@RequestMapping(value = "/create")
	public String createSupplementalLink(@ModelAttribute("SupplementalLink") SupplementalLinkImpl supplementalLink, Model model){		
		model.addAttribute("SupplierKcodeList",this.getMaintainSupplementalLinkUco().getSupplierKcodeList());
		model.addAttribute("title","Create supplemental link");
		model.addAttribute("actionURL","/SupplementalLinks/save");		
		return "SupplementalLink";				
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('SUPPLEMENTAL_LINKS_EDIT'))")	
	@RequestMapping(value = "/edit/{id}")
	public String editSupplementalLink(@PathVariable int id, Model model){	
		model.addAttribute("SupplierKcodeList",this.getMaintainSupplementalLinkUco().getSupplierKcodeList());		
		model.addAttribute("SupplementalLink",this.getMaintainSupplementalLinkUco().get(id));
		model.addAttribute("title","Edit supplemental link");
		model.addAttribute("actionURL","/SupplementalLinks/update");		
		return "SupplementalLink";		
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('SUPPLEMENTAL_LINKS_CREATE'))")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String saveSupplementalLink(@ModelAttribute("SupplementalLink") SupplementalLinkImpl supplementalLink){		
		if(supplementalLink.getSupplierKcode()=="")supplementalLink.setSupplierKcode(null);
		this.getMaintainSupplementalLinkUco().save(supplementalLink);		
		return "redirect:/SupplementalLinks";		
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('SUPPLEMENTAL_LINKS_EDIT'))")	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String updateSupplementalLink(@ModelAttribute("SupplementalLink") SupplementalLinkImpl supplementalLink){		
		if(supplementalLink.getSupplierKcode()=="")supplementalLink.setSupplierKcode(null);
		this.getMaintainSupplementalLinkUco().update(supplementalLink);		
		return "redirect:/SupplementalLinks";		
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('SUPPLEMENTAL_LINKS_DELETE'))")		
	@RequestMapping(value = "/delete", method = RequestMethod.POST)	
	public String deleteSupplementalLink(@RequestParam("idList") List<Integer> idList,final RedirectAttributes redirectAttributes, Locale locale){								
		this.getMaintainSupplementalLinkUco().delete(idList);				
		redirectAttributes.addFlashAttribute("message", "The selected supplemental link(s) have been deleted");		
		return "redirect:/SupplementalLinks";		
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('SUPPLEMENTAL_LINKS_PREVIEW'))")		
	@RequestMapping(value="/getListOfSupplementalLinksBySupplierKcode", method = RequestMethod.GET)
	public @ResponseBody String getListOfSupplementalLinksBySupplierKcode(HttpServletRequest request, HttpServletResponse response,@RequestParam("supplierKcode") String supplierKcode){		 
        try
        {      	    	
        	List<SupplementalLink> supplementalLink = this.getMaintainSupplementalLinkUco().getList(supplierKcode);                             
    		request.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=utf-8");
            response.setHeader("Cache-Control", "no-cache");
            PrintWriter out = response.getWriter();
            out.print(JsonHelper.toJson(supplementalLink).toString());
            out.flush();
            out.close();    		            
        } 
        catch (Exception e) {           
        }        		
        return null;				
	} 
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('SUPPLEMENTAL_LINKS_PREVIEW'))")			
	@RequestMapping(value="/getAllSupplementalLinks", method = RequestMethod.GET)	
	public @ResponseBody String getAllSupplementalLinks(HttpServletRequest request, HttpServletResponse response){		
		try
        {      	        	
        	List<SupplementalLink> supplementalLink = this.getMaintainSupplementalLinkUco().getList();                             
    		request.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=utf-8");
            response.setHeader("Cache-Control", "no-cache");
            PrintWriter out = response.getWriter();
            out.print(JsonHelper.toJson(supplementalLink).toString());
            out.flush();
            out.close();   		            
        } 
        catch (Exception e) {           
        }        		
        return null;				
	}		
}