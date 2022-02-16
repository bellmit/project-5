package com.kindminds.drs.web.ctrl.amazon.report;

import java.io.IOException;
import java.util.HashMap;import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kindminds.drs.Context;
import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.usecase.report.amazon.ImportAmazonSponsoredProductsSearchTermReportUco;
import com.kindminds.drs.api.usecase.ViewSatisfactionReportUco;
import com.kindminds.drs.api.usecase.ViewSearchTermReportUco;
import com.kindminds.drs.api.v1.model.report.SatisfactionReportLineItem.Statistics;
import com.kindminds.drs.service.util.SpringAppCtx;
import com.kindminds.drs.util.JsonHelper;

@Controller
@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('CAMPAIGN_PERFORMANCE_REPORT_LIST'))")
public class SearchTermReportController {
	
	private ImportAmazonSponsoredProductsSearchTermReportUco getUcoForImport(){
		return (ImportAmazonSponsoredProductsSearchTermReportUco)(SpringAppCtx.get().getBean("importAmazonSponsoredProductsSearchTermReportUcoImpl"));
	}
	
	private ViewSatisfactionReportUco  getSatisfactionReportUco(){
		return (ViewSatisfactionReportUco)(SpringAppCtx.get().getBean("viewSatisfactionReportUcoImpl"));
	}

	private ViewSearchTermReportUco getUcoForView(){
		return (ViewSearchTermReportUco)(SpringAppCtx.get().getBean("viewSearchTermReportUcoImpl"));
	}
	
	@RequestMapping(value="AmazonSearchTermReport")
	public String AmazonSearchTermReportImport(Model model,@RequestParam(value="marketplace",defaultValue="1",required=false) String marketplace){
		model.addAttribute("marketplaces",this.getUcoForImport().getMarketplaceList());
		model.addAttribute("marketplace", marketplace);
		return "AmazonSearchTermReport";
	}
	
	@RequestMapping(value="AmazonSearchTermReport/updateCampaignNameSupplierMap")
	public String updateCampaignNameSupplierMap(Model model,@RequestParam Map<String,String> params){
		String marketplaceKeyStr = params.get("marketplace");
		Marketplace marketplace = Marketplace.fromKey(Integer.parseInt(marketplaceKeyStr));
		params.remove("marketplace");
		model.addAttribute("marketplace", marketplaceKeyStr);
		this.getUcoForImport().updateCampaignNameSupplierMap(marketplace,params);
		return "redirect:/AmazonSearchTermReport";
	}
	
	@RequestMapping(value="AmazonSearchTermReport/uploadAndImport",method=RequestMethod.POST)    	
	public String uploadAndImport(
			@RequestParam(value="marketplaceKey",defaultValue="")String marketplaceKey,
			@RequestParam String importType,
			@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes){
		try {
			byte[] fileBytes = file.getBytes();
			String result = "";

			if(importType == "0"){
				result = this.getUcoForImport().importFile(marketplaceKey,fileBytes);
			}else{
				result = this.getUcoForImport().importECMFile(marketplaceKey,fileBytes);
			}

			redirectAttributes.addFlashAttribute("message",result);
		} catch (IOException e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("message","There are some problems with the upload file.");
		}
		return "redirect:/AmazonSearchTermReport";		
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('SKU_ADVERTISING_PERFORMANCE_REPORT'))")	
	@RequestMapping(value = "/SearchTermReport")	
	public String showSearchTermReport(Model model,
			@RequestParam(value="supplierKcode",defaultValue="") String supplierKcode,
			@RequestParam(value="assignedMarketplace",defaultValue="AMAZON_COM") String assignedMarketplace){
		Marketplace marketplace = Marketplace.valueOf(assignedMarketplace);
		String assignedCompanyKcode = null;
		if(Context.getCurrentUser().isSupplier()){
			assignedCompanyKcode = Context.getCurrentUser().getCompanyKcode();
		}else{
			assignedCompanyKcode = supplierKcode;
			model.addAttribute("supplierKcode",supplierKcode);
			model.addAttribute("supplierKcodeNameMap",this.getUcoForImport().getSupplierKcodeNameMap());
		}
		List<String> campaignNameList = this.getUcoForView().getCampaignNameList(marketplace, assignedCompanyKcode);
		String campaignName = campaignNameList.size()==0?null:campaignNameList.get(0);
		model.addAttribute("report",this.getUcoForView().getReport(marketplace,assignedCompanyKcode,campaignName));
		model.addAttribute("campaignNameList",JsonHelper.toJson(campaignNameList));
		model.addAttribute("assignedMarketplace", assignedMarketplace );
		model.addAttribute("marketplaces", this.getUcoForView().getMarketplaces());
		//return "SearchTermReport";
		return "th/marketingReport/SearchTermReport";
	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('SKU_ADVERTISING_PERFORMANCE_REPORT'))")
	@RequestMapping(value = "/SearchTermReportV4")
	public String showSearchTermReportV4(Model model,
									   @RequestParam(value="supplierKcode",defaultValue="") String supplierKcode,
									   @RequestParam(value="assignedMarketplace",defaultValue="AMAZON_COM") String assignedMarketplace){
		Marketplace marketplace = Marketplace.valueOf(assignedMarketplace);
		String assignedCompanyKcode = null;
		if(Context.getCurrentUser().isSupplier()){
			assignedCompanyKcode = Context.getCurrentUser().getCompanyKcode();
		}else{
			assignedCompanyKcode = supplierKcode;
			model.addAttribute("supplierKcode",supplierKcode);
			model.addAttribute("supplierKcodeNameMap",this.getUcoForImport().getSupplierKcodeNameMap());
		}
		List<String> campaignNameList = this.getUcoForView().getCampaignNameList(marketplace, assignedCompanyKcode);
		String campaignName = campaignNameList.size()==0?null:campaignNameList.get(0);
		model.addAttribute("report",this.getUcoForView().getReport(marketplace,assignedCompanyKcode,campaignName));
		model.addAttribute("campaignNameList",JsonHelper.toJson(campaignNameList));
		model.addAttribute("assignedMarketplace", assignedMarketplace );
		model.addAttribute("marketplaces", this.getUcoForView().getMarketplaces());
		return "SearchTermReportV4";
	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('SKU_ADVERTISING_PERFORMANCE_REPORT'))")
	@RequestMapping(value = "/SearchTermReport/getReportByCampaignName")
	public @ResponseBody String getSearchTermReportByCampaignName(
			@RequestParam("supplierKcode") String supplierKcode, 
			@RequestParam("assignedMarketplace") String assignedMarketplace, 
			@RequestParam("campaignName") String campaignName
			){

		boolean isDrsUser = Context.getCurrentUser().isDrsUser();
		if(!isDrsUser) supplierKcode = Context.getCurrentUser().getCompanyKcode();

		Marketplace marketplace = Marketplace.valueOf(assignedMarketplace);
		return JsonHelper.toJson(this.getUcoForView().getReport(marketplace,supplierKcode,campaignName));		
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('SKU_ADVERTISING_PERFORMANCE_REPORT'))")		
	@RequestMapping(value="AmazonSearchTermReport/getCampaignsToSupplierMap", method = RequestMethod.GET)
	public @ResponseBody String getCampaignsToSupplierMap(@RequestParam("marketplace") String marketplace){
		Marketplace m = Marketplace.fromKey(Integer.parseInt(marketplace));
		Map<String,Map<String,String>> campaignsToSupplierMap = new TreeMap<String,Map<String,String>>();				
		Map<String,String> campaignNameToSupplierKcodeMap = this.getUcoForImport().getCampaignNameToSupplierKcodeMap(m);
		Map<String,String> supplierKcodeNameMap = this.getUcoForImport().getSupplierKcodeNameMap();
		campaignsToSupplierMap.put("campaignNameToSupplierKcodeMap", campaignNameToSupplierKcodeMap);
		campaignsToSupplierMap.put("supplierKcodeNameMap", supplierKcodeNameMap);
		return JsonHelper.toJson(campaignsToSupplierMap);				
	}
			
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('SKU_ADVERTISING_PERFORMANCE_REPORT'))")	
	@RequestMapping(value = "/customer-satisfaction")	
	public String showCustomerSatisfactionReport(Model model,
			@RequestParam(value="supplierKcode",defaultValue="") String supplierKcode,
			@RequestParam(value="assignedMarketplaceId",defaultValue="1") String assignedMarketplaceId){

		String assignedCompanyKcode = null;
		if(Context.getCurrentUser().isSupplier()){
			assignedCompanyKcode = Context.getCurrentUser().getCompanyKcode();
		}else{
			assignedCompanyKcode = supplierKcode;
			model.addAttribute("supplierKcode",supplierKcode);
			model.addAttribute("supplierKcodeNameMap",this.getUcoForImport().getSupplierKcodeToShortEnUsNameWithRetailMap());
		}
		model.addAttribute("report",this.getSatisfactionReportUco().getReport(assignedMarketplaceId,assignedCompanyKcode.equals("") ? null : assignedCompanyKcode));
		model.addAttribute("marketplaces",this.getUcoForView().getMarketplaces());
		model.addAttribute("assignedMarketplaceId", assignedMarketplaceId );
		model.addAttribute("statusColorMap",this.createStatusColorMap());
		return "th/marketingReport/customerSatisfaction";
	}


	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('SKU_ADVERTISING_PERFORMANCE_REPORT'))")
	@RequestMapping(value = "/customer-satisfactionV4")
	public String showCustomerSatisfactionReportV4(Model model,
												 @RequestParam(value="supplierKcode",defaultValue="") String supplierKcode,
												 @RequestParam(value="assignedMarketplaceId",defaultValue="1") String assignedMarketplaceId){
		String assignedCompanyKcode = null;
		if(Context.getCurrentUser().isSupplier()){
			assignedCompanyKcode = Context.getCurrentUser().getCompanyKcode();
		}else{
			assignedCompanyKcode = supplierKcode;
			model.addAttribute("supplierKcode",supplierKcode);
			model.addAttribute("supplierKcodeNameMap",this.getUcoForImport().getSupplierKcodeToShortEnUsNameWithRetailMap());
		}
		model.addAttribute("report",this.getSatisfactionReportUco().getReport(assignedMarketplaceId,assignedCompanyKcode.equals("") ? null : assignedCompanyKcode));
		model.addAttribute("marketplaces",this.getUcoForView().getMarketplaces());
		model.addAttribute("assignedMarketplaceId", assignedMarketplaceId );
		model.addAttribute("statusColorMap",this.createStatusColorMap());
		return "CustomerSatisfactionV4";
	}
	
	private Map<String,String> createStatusColorMap(){
		Map<String,String> result = new HashMap<>();
		result.put(Statistics.Good.name(),"satisfactionGood");
		result.put(Statistics.Okay.name(),"satisfactionOkay");
		result.put(Statistics.Bad.name(),"satisfactionBad");
		result.put(Statistics.Unacceptable.name(),"satisfactionUnacceptable");
		result.put("N/A","satisfactionNA");		
		return result;
	}
	
}