package com.kindminds.drs.web.ctrl.amazon;

import java.io.IOException;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kindminds.drs.api.usecase.report.amazon.ImportAmazonInventoryHealthReportUco;
import com.kindminds.drs.api.usecase.inventory.ViewInventoryHealthReportUco;
import com.kindminds.drs.service.util.SpringAppCtx;

@Controller
public class AmazonInventoryHealthReportController {
		
	private ImportAmazonInventoryHealthReportUco getImportAmazonInventoryHealthReportUco(){
		return (ImportAmazonInventoryHealthReportUco)(SpringAppCtx.get().getBean("importAmazonInventoryHealthReportUcoImpl"));
	}
	
	private ViewInventoryHealthReportUco getViewInventoryHealthReportUco(){ 
		return (ViewInventoryHealthReportUco)SpringAppCtx.get().getBean("viewInventoryHealthReportUcoImpl"); 
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('AMAZON_INVENTORY_HEALTH_REPROT_IMPORT'))")
	@RequestMapping(value="amazon-inventory-health-report")	
	public String AmazonInventoryHealthReportImported(Model model){
		
		model.addAttribute("marketplaces", getImportAmazonInventoryHealthReportUco().getMarketplaceList());
		model.addAttribute("importStatuses", getImportAmazonInventoryHealthReportUco().getImportStatuses());				
		return "amazonInventoryHealthReport";
				
	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('AMAZON_INVENTORY_HEALTH_REPROT_IMPORT'))")	
	@RequestMapping(value="amazon-inventory-health-report/uploadAndImport",method=RequestMethod.POST)    	
	public String uploadAndImport(
			@RequestParam(value="marketplaceKey",defaultValue="")String marketplaceKey,
			@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes){
		try {
			byte[] fileBytes = file.getBytes();			
			String result = getImportAmazonInventoryHealthReportUco().importReport(marketplaceKey,fileBytes);			
			redirectAttributes.addFlashAttribute("message",result);
		} catch (IOException e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("message","There are some problems with the upload file.");
		}		
		return "redirect:/amazon-inventory-health-report";
		
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('AMAZON_INVENTORY_HEALTH_REPROT_VIEW'))")		
	@RequestMapping(value="/inventory-health-report")
	public String showLongTermStorageFeeReport(Model model,
			@RequestParam(value="marketplaceId",defaultValue="1") String marketplaceId,
			@RequestParam(value="supplierKcode",defaultValue="") String supplierKcode){
		model.addAttribute("supplierKcodeNames", getViewInventoryHealthReportUco().getSupplierKcodeNames());
		model.addAttribute("marketplaces", getViewInventoryHealthReportUco().getMarketplaces());
		model.addAttribute("report", getViewInventoryHealthReportUco().getReport(marketplaceId,supplierKcode));
		model.addAttribute("currency", getViewInventoryHealthReportUco().getCurrency(marketplaceId));
		return "th/amazonReport/inventoryHealthReport";
	}



	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('AMAZON_INVENTORY_HEALTH_REPROT_VIEW'))")
	@RequestMapping(value="/inventory-health-report/v4")
	public String showLongTermStorageFeeReportv4(Model model,
											   @RequestParam(value="marketplaceId",defaultValue="1") String marketplaceId,
											   @RequestParam(value="supplierKcode",defaultValue="") String supplierKcode){
		model.addAttribute("supplierKcodeNames", getViewInventoryHealthReportUco().getSupplierKcodeNames());
		model.addAttribute("marketplaces", getViewInventoryHealthReportUco().getMarketplaces());
		model.addAttribute("report", getViewInventoryHealthReportUco().getReport(marketplaceId,supplierKcode));
		model.addAttribute("currency", getViewInventoryHealthReportUco().getCurrency(marketplaceId));
		return "inventoryHealthReportV4";
	}


}