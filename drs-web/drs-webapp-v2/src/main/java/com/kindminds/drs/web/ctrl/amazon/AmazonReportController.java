package com.kindminds.drs.web.ctrl.amazon;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kindminds.drs.api.usecase.report.amazon.ImportAmazonSettlementReportUco;
import com.kindminds.drs.api.v1.model.accounting.AmazonSettlementReportInfo;
import com.kindminds.drs.util.JsonHelper;

@Controller
@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('AMAZON_REPORT'))")
@RequestMapping(value = "/amazon-settlement-reports")
public class AmazonReportController {
		
	@Autowired private ImportAmazonSettlementReportUco importAmazonSettlementReportUco;
			
	@RequestMapping(value = "")
	public String ListOfAmazonReport(Model model){
		List<AmazonSettlementReportInfo> settlementReportInfoList = importAmazonSettlementReportUco.getSettlementReportInfoList();
		model.addAttribute("settlementReportInfoList",JsonHelper.toJson(settlementReportInfoList));
		model.addAttribute("marketplaces",importAmazonSettlementReportUco.getMarketplaces());
		return "amazonSettlementReports";		
	}
	
	@RequestMapping(value = "/uploadFileAndImport",method=RequestMethod.POST)    
	public String uploadFileAndImport(RedirectAttributes redirectAttributes,
			@RequestParam("file") MultipartFile file,
			@RequestParam("marketplaceId") String marketplaceId){
		try {
			byte[] fileBytes = file.getBytes();
			String resultMessage = this.importAmazonSettlementReportUco.uploadFileAndImport(marketplaceId,fileBytes);
			redirectAttributes.addFlashAttribute("message",resultMessage);			
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message",e.getMessage());
			e.printStackTrace();
		}
		return "redirect:/amazon-settlement-reports";
		
	}
									
}