package com.kindminds.drs.web.ctrl.amazon.report;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.service.util.SpringAppCtx;
import com.kindminds.drs.api.usecase.report.amazon.ImportAmazonHeadlineSearchAdReportUco;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class HsaKeywordReportController {


	private static final String KEYWORD = "keyword";
	private static final String KEYWORD_VIDEO = "keyword_video";
			
	private ImportAmazonHeadlineSearchAdReportUco getUco(){
		return (ImportAmazonHeadlineSearchAdReportUco)(SpringAppCtx.get().getBean("importAmazonHeadlineSearchAdReportUcoImpl"));
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('CAMPAIGN_PERFORMANCE_REPORT_LIST'))")
	@RequestMapping(value = "/HsaKeywordReport")
	public String ListCampaignPerformanceReport(Model model){
		model.addAttribute("marketplaces", Marketplace.getAmazonMarketplaces());
		return "HsaKeywordReport";
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('CAMPAIGN_PERFORMANCE_REPORT_LIST'))")		
	@RequestMapping(value="/HsaKeywordReport/uploadFile",method=RequestMethod.POST)
	public String uploadFile(final RedirectAttributes redirectAttributes,
			@RequestParam String marketplaceId,@RequestParam String importType,
			@RequestParam("file") MultipartFile file) throws IOException{

		byte[] fileBytes = file.getBytes();
		int marketplaceIdInt;
		String reportType = null;
		String resultMessage;

		if (importType.equals("0")) {
			reportType = KEYWORD;
		} else if (importType.equals("1")) {
			reportType = KEYWORD_VIDEO;
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

		return "redirect:/HsaKeywordReport";
	}
			
}