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

import com.kindminds.drs.api.usecase.inventory.UpdateProductSkuFbaInventoryAvailabilityUco;
import com.kindminds.drs.service.util.SpringAppCtx;
import com.kindminds.drs.util.JsonHelper;

@Controller
@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('AMAZON_INVENTORY_AVAILABILITY'))")
@RequestMapping("/updateProductSkuFBAInventoryAvailability")
public class AmazonInventoryAvailabilityController {

	private UpdateProductSkuFbaInventoryAvailabilityUco getUco(){
		return(UpdateProductSkuFbaInventoryAvailabilityUco)(SpringAppCtx.get().getBean("updateProductSkuFbaInventoryAvailabilityUcoImpl"));		
	}
		
	@RequestMapping(value="")	
	public String AmazonInventoryAvailabilityUpdated(@RequestParam(value="year",defaultValue="") String year,@RequestParam(value="month",defaultValue="") String month,Model model){		
		
		if(year.isEmpty() == true) year = this.getUco().getDefaultYear();				
		if(month.isEmpty() == true) month = this.getUco().getDefaultMonth();		
		Map<String,Boolean> updateStatuses = this.getUco().getUpdateStatus(year, month);
		model.addAttribute("yearList", this.getUco().getYears());
		model.addAttribute("monthList", this.getUco().getMonths());
		model.addAttribute("year", JsonHelper.toJson(year));
		model.addAttribute("month", JsonHelper.toJson(month));
		model.addAttribute("updateStatuses",updateStatuses);		
		return "amazonInventoryAvailability";				
	}
	
	@RequestMapping(value="/update",method=RequestMethod.POST)    		
	public String update(
			@RequestParam("file") MultipartFile file,
			@RequestParam("date") String date,
			RedirectAttributes redirectAttributes){
		try {
			byte[] fileBytes = file.getBytes();			
			String result = this.getUco().uploadFileAndUpdate(date, fileBytes);
			redirectAttributes.addFlashAttribute("message",result);
		} catch (IOException e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("message","There are some problems with the upload file.");
		}	
		return "redirect:/updateProductSkuFBAInventoryAvailability";		
	}
	
	@RequestMapping(value="/{date}/clear")	
	public String clear(@PathVariable String date,final RedirectAttributes redirectAttributes){		
		String result = this.getUco().clearAvailabilityData(date);
		redirectAttributes.addFlashAttribute("message",result);
		return "redirect:/updateProductSkuFBAInventoryAvailability";		
	}
	
}