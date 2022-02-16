package com.kindminds.drs.web.ctrl.amazon;

import java.io.IOException;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.usecase.report.amazon.ImportAmazonDetailPageSalesTrafficByChildItemReportByDateUco;
import com.kindminds.drs.service.util.SpringAppCtx;
import com.kindminds.drs.util.JsonHelper;


@Controller
@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('AMAZON_DAILY_PAGE_SALES_TRAFFIC_REPROT'))")
@RequestMapping("/AmazonDailyPageSalesTrafficReport")
public class AmazonDailyPageSalesTrafficReportController {
	
	private ImportAmazonDetailPageSalesTrafficByChildItemReportByDateUco getUco(){
		return (ImportAmazonDetailPageSalesTrafficByChildItemReportByDateUco)(SpringAppCtx.get().getBean("importAmazonDetailPageSalesTrafficByChildItemReportByDateUcoImpl"));		
	}
		
	@RequestMapping(value="")	
	public String AmazonDailyPageSalesTrafficReportImported(@RequestParam(value="year",defaultValue="") String year,@RequestParam(value="month",defaultValue="") String month,Model model){
	
		if(year.isEmpty() == true) year = this.getUco().getDefaultYear();				
		if(month.isEmpty() == true) month = this.getUco().getDefaultMonth();						
		Map<String,Map<Marketplace,Boolean>> importStatuses = this.getUco().getImportStatus(year, month);		
		model.addAttribute("marketplaces",this.getUco().getMarketplaces());		
		model.addAttribute("yearList", this.getUco().getYears());
		model.addAttribute("monthList", this.getUco().getMonths());
//		model.addAttribute("year", JsonHelper.toJson(year));
//		model.addAttribute("month", JsonHelper.toJson(month));
		model.addAttribute("year", year);
		model.addAttribute("month", month);
		model.addAttribute("importStatuses",importStatuses);
		return "th/amazonReport/amazonDailyPageSalesTrafficReport";
		
	}

	@RequestMapping(value="/uploadAndImport",method=RequestMethod.POST)    		
	public String uploadAndImport(
			@RequestParam(value="marketplaceKey",defaultValue="")String marketplaceKey,
			@RequestParam("file") MultipartFile file,
			@RequestParam("date") String date,
			@RequestParam("importType") String importType,
			RedirectAttributes redirectAttributes){
		try {
			byte[] fileBytes = file.getBytes();			
			String result = "";

			if(importType.equals("0")){
				result = this.getUco().importFile(marketplaceKey, date, fileBytes);
			}else{
				result = this.getUco().importECMFile(marketplaceKey, date, fileBytes);
			}

			redirectAttributes.addFlashAttribute("message",result);
		} catch (IOException e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("message","There are some problems with the upload file.");
		}	
		return "redirect:/AmazonDailyPageSalesTrafficReport";
		
	}
	
	@RequestMapping(value="/{marketplaceKey}/{date}/delete")	
	public String delete(@PathVariable String marketplaceKey,@PathVariable String date,final RedirectAttributes redirectAttributes){	
	
		String result = this.getUco().delete(date, marketplaceKey);
		redirectAttributes.addFlashAttribute("message",result);
		return "redirect:/AmazonDailyPageSalesTrafficReport";
		
	}
		
}