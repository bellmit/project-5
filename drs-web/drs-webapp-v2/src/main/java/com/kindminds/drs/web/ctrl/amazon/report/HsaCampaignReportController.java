package com.kindminds.drs.web.ctrl.amazon.report;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.service.util.SpringAppCtx;
import com.kindminds.drs.api.usecase.report.amazon.ImportAmazonHeadlineSearchAdReportUco;
import com.kindminds.drs.api.usecase.report.amazon.ImportAmazonSponsoredProductsAdvertisedProductReportUco;
import com.kindminds.drs.util.JsonHelper;
import com.kindminds.drs.api.v1.model.common.DtoList;
import com.kindminds.drs.api.v1.model.common.Pager;
import com.kindminds.drs.api.v1.model.report.AmazonSponsoredProductsAdvertisedProductReportBriefLineItem;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class HsaCampaignReportController {


	private static final String CAMPAIGN = "campaign";
	private static final String CAMPAIGN_VIDEO = "campaign_video";
	private static final String DISPLAY = "display";
			
	private ImportAmazonHeadlineSearchAdReportUco getUco(){
		return (ImportAmazonHeadlineSearchAdReportUco)(SpringAppCtx.get().getBean("importAmazonHeadlineSearchAdReportUcoImpl"));
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('CAMPAIGN_PERFORMANCE_REPORT_LIST'))")
	@RequestMapping(value = "/HsaCampaignReport")
	public String ListCampaignPerformanceReport(Model model){
		model.addAttribute("marketplaces", Marketplace.getAmazonMarketplaces());
		return "HsaCampaignReport";
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('CAMPAIGN_PERFORMANCE_REPORT_LIST'))")		
	@RequestMapping(value="/HsaCampaignReport/uploadFile",method=RequestMethod.POST)
	public String uploadFile(final RedirectAttributes redirectAttributes,
			@RequestParam String marketplaceId,@RequestParam String importType,
			@RequestParam("file") MultipartFile file) throws IOException{

		byte[] fileBytes = file.getBytes();
		int marketplaceIdInt;
		String reportType = null;
		String resultMessage;

		if (importType.equals("0")) {
			reportType = CAMPAIGN;
		} else if (importType.equals("1")) {
			reportType = CAMPAIGN_VIDEO;
		} else if (importType.equals("2")) {
			reportType = DISPLAY;
		}

		if (fileBytes == null || fileBytes.length == 0) {
			resultMessage = "File not found";

		} else if (reportType == null) {
			resultMessage = "Report type not recognized";

		}  else if (!StringUtils.hasText(marketplaceId)) {
			resultMessage = "Invalid marketplace";

		} else {

			marketplaceIdInt = Integer.valueOf(marketplaceId);
			resultMessage = this.getUco().importReport(marketplaceIdInt, fileBytes, reportType);
		}


		redirectAttributes.addFlashAttribute("message",resultMessage);

		return "redirect:/HsaCampaignReport";
	}
			
}