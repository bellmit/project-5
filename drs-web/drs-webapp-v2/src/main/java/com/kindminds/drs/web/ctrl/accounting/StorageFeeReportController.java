package com.kindminds.drs.web.ctrl.accounting;

import java.io.IOException;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kindminds.drs.api.usecase.report.amazon.ImportAmazonMonthlyStorageFeeReportUco;
import com.kindminds.drs.service.util.SpringAppCtx;

@Controller
@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('IMPORT_MONTHLY_STOREAGE_FEE_REPORT'))")
public class StorageFeeReportController {

	private ImportAmazonMonthlyStorageFeeReportUco getUco(){
		return (ImportAmazonMonthlyStorageFeeReportUco)(SpringAppCtx.get().getBean("importAmazonMonthlyStorageFeeReportUcoImpl"));		
	}
	
	@RequestMapping(value = "/AmazonStorageFeeReport")
	public String showAmazonStorageFeeReport(Model model){
		model.addAttribute("warehouses",this.getUco().getWarehouses());
		return "storageFeeReport";
	}
	
	@RequestMapping(value="/AmazonStorageFeeReport/uploadFile",method=RequestMethod.POST)    
	public String uploadFile(final RedirectAttributes redirectAttributes,
			@RequestParam String warehouseId,
			@RequestParam("file") MultipartFile file){
		try {
			byte[] fileBytes = file.getBytes();
			String resultMessage = this.getUco().importFile(warehouseId,fileBytes);
			redirectAttributes.addFlashAttribute("message",resultMessage);				
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "redirect:/AmazonStorageFeeReport";		
	}
		
}