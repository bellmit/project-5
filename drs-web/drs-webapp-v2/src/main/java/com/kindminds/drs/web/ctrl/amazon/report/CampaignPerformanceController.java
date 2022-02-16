package com.kindminds.drs.web.ctrl.amazon.report;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.kindminds.drs.api.v1.model.report.AmazonSponsoredProductsAdvertisedProductReportBriefLineItem;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kindminds.drs.api.usecase.report.amazon.ImportAmazonSponsoredProductsAdvertisedProductReportUco;

import com.kindminds.drs.api.v1.model.common.DtoList;
import com.kindminds.drs.api.v1.model.common.Pager;
import com.kindminds.drs.service.util.SpringAppCtx;
import com.kindminds.drs.util.JsonHelper;

@Controller
public class CampaignPerformanceController {
			
	private ImportAmazonSponsoredProductsAdvertisedProductReportUco getUco(){
		return (ImportAmazonSponsoredProductsAdvertisedProductReportUco)(SpringAppCtx.get().getBean("importAmazonSponsoredProductsAdvertisedProductReportUcoImpl"));
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('CAMPAIGN_PERFORMANCE_REPORT_LIST'))")
	@RequestMapping(value = "/AmazonCampaignPerformanceReport")		
	public String ListCampaignPerformanceReport(Model model){
		model.addAttribute("marketplaces",this.getUco().getMarketplaces());
		return "AmazonCampaignPerformanceReport";
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('CAMPAIGN_PERFORMANCE_REPORT_LIST'))")		
	@RequestMapping(value="/CampaignPerformanceReportList/uploadFile",method=RequestMethod.POST)    
	public String uploadFile(final RedirectAttributes redirectAttributes,
			@RequestParam String marketplaceId,@RequestParam String importType,
			@RequestParam("file") MultipartFile file) throws IOException{

		byte[] fileBytes = file.getBytes();
		String resultMessage =  "";

		if(importType == "0"){
			resultMessage = this.getUco().importFile(marketplaceId,fileBytes);
		}else{
			resultMessage = this.getUco().importECMFile(marketplaceId,fileBytes);
		}

		redirectAttributes.addFlashAttribute("message",resultMessage);

		return "redirect:/AmazonCampaignPerformanceReport";		
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('CAMPAIGN_PERFORMANCE_REPORT_LIST'))")	
	@RequestMapping(value="/CampaignPerformanceReportList/findBriefCampaignInformation", method = RequestMethod.GET)	
	public @ResponseBody String findBriefCampaignInformation(@RequestParam(value="page") Integer pageIndex,@RequestParam("marketplaceId") String marketplaceId,@RequestParam("utcDateStart") String utcDateStart,@RequestParam("utcDateEnd") String utcDateEnd,@RequestParam("sku") String sku){		
		DtoList<AmazonSponsoredProductsAdvertisedProductReportBriefLineItem> lineItems = this.getUco().getBriefLineItem(pageIndex,marketplaceId,utcDateStart, utcDateEnd, sku);
		Pager page = lineItems.getPager();	
		Map<String, Object> briefCampaignInformationMap = new HashMap<String, Object>();
		briefCampaignInformationMap.put("totalPages", Integer.toString(page.getTotalPages()));
		briefCampaignInformationMap.put("currentPageIndex", Integer.toString(page.getCurrentPageIndex()));
		briefCampaignInformationMap.put("startPage", Integer.toString(page.getStartPage()));
		briefCampaignInformationMap.put("endPage", Integer.toString(page.getEndPage()));		
		briefCampaignInformationMap.put("data",lineItems.getItems());										
		return JsonHelper.toJson(briefCampaignInformationMap);		
	}
			
}