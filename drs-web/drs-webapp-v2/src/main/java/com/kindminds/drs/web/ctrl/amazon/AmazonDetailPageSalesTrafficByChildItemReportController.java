package com.kindminds.drs.web.ctrl.amazon;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kindminds.drs.api.usecase.report.amazon.ImportAmazonDetailPageSalesTrafficByChildItemReportUco;
import com.kindminds.drs.util.JsonHelper;


@Controller
@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('CAMPAIGN_PERFORMANCE_REPORT_LIST'))")
public class AmazonDetailPageSalesTrafficByChildItemReportController {
		
	@Autowired private ImportAmazonDetailPageSalesTrafficByChildItemReportUco uco;
			
	@RequestMapping(value="/AmazonDetailPageSalesTrafficByChildItemReport")
	public String getAmazonDetailPageSalesTrafficByChildItemReportMainPage(Model model){
		model.addAttribute("marketplaces",this.uco.getMarketplaces());
		return "AmazonDetailPageSalesTrafficByChildItemReport";
	}
	
	@RequestMapping(value="/AmazonDetailPageSalesTrafficByChildItemReport/upload",method=RequestMethod.POST)    
	public String uploadFile(RedirectAttributes redirectAttributes,
			@RequestParam("file") MultipartFile file,
			@RequestParam(value="marketplaceId") String marketplaceId,
			@RequestParam(value="periodId",defaultValue="") String periodId){
		
		if(!StringUtils.hasText(periodId)){
			redirectAttributes.addFlashAttribute("message","Please select settlement period" );
			return "redirect:/AmazonDetailPageSalesTrafficByChildItemReport";
		}
		try {
			byte[] fileBytes;
			fileBytes = file.getBytes();
			String result = this.uco.importFile(marketplaceId,Integer.valueOf(periodId),fileBytes);
			redirectAttributes.addFlashAttribute("message",result);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "redirect:/AmazonDetailPageSalesTrafficByChildItemReport";
	}
	
	@RequestMapping(value="/AmazonDetailPageSalesTrafficByChildItemReport/delete",method=RequestMethod.GET)    
	public String delete(RedirectAttributes redirectAttributes,
			@RequestParam(value="marketplaceId") String marketplaceId,
			@RequestParam(value="periodId") String periodId){
		this.uco.deleteReportDataByPeriod(marketplaceId,periodId);
		redirectAttributes.addFlashAttribute("filterMarketplaceId", marketplaceId);
		return "redirect:/AmazonDetailPageSalesTrafficByChildItemReport";
	}
	
	// AJAX ------------------------------------------------------------------------------------------------------------------------------
	
	@RequestMapping(value="AmazonSearchTermReport/getUnOccupiedSettlementPeriods", method = RequestMethod.GET)
	public @ResponseBody String getUnOccupiedSettlementPeriods(@RequestParam("marketplaceId") String marketplaceId){
		return JsonHelper.toJson(this.uco.getUnOccupiedSettlementPeriods(marketplaceId));
	}
	
	@RequestMapping(value="AmazonDetailPageSalesTrafficByChildItemReport/getSettlementPeriodImportingStatuses", method = RequestMethod.GET)
	public @ResponseBody String getSettlementPeriodImportingStatuses(@RequestParam("marketplaceId") String marketplaceId){
		return JsonHelper.toJson(this.uco.getSettlementPeriodImportingStatuses(marketplaceId));
	}
		
}