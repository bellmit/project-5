package com.kindminds.drs.web.ctrl.amazon;

import static akka.pattern.Patterns.ask;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.usecase.report.amazon.ImportAmazonDateRangeReportUco;
import com.kindminds.drs.api.v1.model.report.DateRangeImportStatus;

import akka.actor.ActorRef;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;


@Controller
public class AmazonDateRangeReportController {
	
//	@Autowired @Qualifier("drsCmdBus")ActorRef drsCmdBus;
	
	@Autowired 
	private ImportAmazonDateRangeReportUco uco;
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('IMPORT_AMAZON_DATE_RANGE_REPORT'))")
	@RequestMapping(value="/amazon-date-range-report")	
	public String AmazonInventoryHealthReportImported(Model model){
		
//		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
//	    final Future<Object> f1 =
//	                ask(drsCmdBus, new com.kindminds.drs.api.message.importAmazonDateRangeReportUco.GetMarketplaces(),
//	                		timeout);
//	    
//	    final Future<Object> f2 =
//                ask(drsCmdBus, new com.kindminds.drs.api.message.importAmazonDateRangeReportUco.GetImportStatus(),
//                		timeout);
	    try {
//			List<Marketplace> marketplaces = (List<Marketplace>) Await.result(f1, timeout.duration());
//			List<DateRangeImportStatus> importStatuses = (List<DateRangeImportStatus>) Await.result(f2, timeout.duration());
			
			List<Marketplace> marketplaces = uco.getMarketplaces();
			List<DateRangeImportStatus> importStatuses = uco.getImportStatus();
			
			model.addAttribute("marketplaces", marketplaces);
			model.addAttribute("importStatuses", importStatuses);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "importAmazonDateRangeReport";
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('IMPORT_AMAZON_DATE_RANGE_REPORT'))")	
	@RequestMapping(value="amazon-date-range-report/uploadAndImport",method=RequestMethod.POST)    	
	public String uploadAndImport(
			@RequestParam(value="marketplaceReport",defaultValue="") int marketplaceId,
			@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes){
		try {
			byte[] fileBytes = file.getBytes();
			String result = "Please select a file.";
			if (!"".equals(file.getOriginalFilename())) {
//				Timeout timeout = new Timeout(Duration.create(10, "seconds"));
//			    final Future<Object> f1 =
//			                ask(drsCmdBus, new com.kindminds.drs.api.message.importAmazonDateRangeReportUco.ImportReport(marketplaceId, fileBytes),
//			                		timeout);
//				result = (String) Await.result(f1, timeout.duration());
				
				result = uco.importReport(marketplaceId, fileBytes);
			}
			redirectAttributes.addFlashAttribute("message",result);
		} catch (IOException e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("message","There are some problems with the upload file.");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message","Import timed out.");
			e.printStackTrace();
		}
			
		return "redirect:/amazon-date-range-report";
	}
	
}