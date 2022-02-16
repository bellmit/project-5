package com.kindminds.drs.web.ctrl.logistics;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.kindminds.drs.api.usecase.logistics.MaintainShipmentIvsUco;
import com.kindminds.drs.api.v1.model.logistics.ShipmentIvs;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.Ivs;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.IvsLineItem;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShipmentStatus;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShipmentType;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.kindminds.drs.api.usecase.logistics.MaintainShipmentUnsUco;



import com.kindminds.drs.api.v1.model.logistics.ShipmentUns;
import com.kindminds.drs.api.v1.model.common.DtoList;
import com.kindminds.drs.api.v1.model.common.Pager;
import com.kindminds.drs.service.util.SpringAppCtx;
import com.kindminds.drs.util.JsonHelper;
import com.kindminds.drs.web.data.dto.ShipmentIvsSearchConditionImpl;
import com.kindminds.drs.web.data.dto.UnifiedShipmentImpl;
import com.kindminds.drs.web.data.dto.UnifiedShipmentLineItemImpl;

@Controller
@RequestMapping(value = "/UnifiedShipments")
public class UnifiedShipmentController {
	
	private MaintainShipmentUnsUco getMaintainShipmentUnsUco(){		
		return (MaintainShipmentUnsUco)(SpringAppCtx.get().getBean("maintainShipmentOfUnifiedUco"));		
	}


	private MaintainShipmentIvsUco getMaintainShipmentIvsUco(){
		 return (MaintainShipmentIvsUco)(SpringAppCtx.get().getBean("maintainShipmentIvsUcoImpl"));
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('UNIFIED_SHIPMENTS_LIST'))")
	@RequestMapping(value = "")
	public String listOfUnifiedShipments(@RequestParam(value="page",defaultValue="1",required=false) Integer pageIndex,Model model) {				
		DtoList<ShipmentUns> unifiedShipmentList = this.getMaintainShipmentUnsUco().getList(pageIndex);
		Pager page = unifiedShipmentList.getPager();	
		Map<String, String> drsCompanyKcodeToNameMap = this.getMaintainShipmentUnsUco().getDrsCompanyKcodeToNameMap();		
		model.addAttribute("DrsCompanyKcodeToNameMap",drsCompanyKcodeToNameMap);		
		model.addAttribute("totalPages",page.getTotalPages());
		model.addAttribute("currentPageIndex",page.getCurrentPageIndex());
		model.addAttribute("startPage",page.getStartPage());
		model.addAttribute("endPage",page.getEndPage());		
		model.addAttribute("UnifiedShipmentList", unifiedShipmentList.getItems());								
		return "ListOfUnifiedShipments";
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('UNIFIED_SHIPMENTS_CREATE'))")
	@RequestMapping(value = "/create", method = RequestMethod.GET)	
	public String createUnifiedShipment(@ModelAttribute("UnifiedShipment") UnifiedShipmentImpl unifiedShipment,Model model){						

		unifiedShipment.lineItems.add(new UnifiedShipmentLineItemImpl());
		Map<String, String> drsCompanyKcodeToName = this.getMaintainShipmentUnsUco().getDrsCompanyKcodeToNameMap();
		ShipmentIvsSearchConditionImpl condition = new ShipmentIvsSearchConditionImpl(null,null,null,null);


		DtoList<ShipmentIvs>  shipmentOfInventoryList = this.getMaintainShipmentIvsUco().retrieveList(condition,1);

		model.addAttribute("ShipmentOfInventoryList", shipmentOfInventoryList.getItems());
		model.addAttribute("drsCompanyKcodeToName", drsCompanyKcodeToName);
		model.addAttribute("drsCompanyKcodeToNameJson", JsonHelper.toJson(drsCompanyKcodeToName));				
		model.addAttribute("InventoryShipment",unifiedShipment);
		model.addAttribute("UnifiedShipmentJson",JsonHelper.toJson(unifiedShipment));
		model.addAttribute("type", "Create");
		model.addAttribute("status", "null");
		model.addAttribute("forwarderList", this.getMaintainShipmentUnsUco().getForwarderList());				
		return "UnifiedShipment";		
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('UNIFIED_SHIPMENTS_EDIT'))")
	@RequestMapping(value = "/saveDraft", method = RequestMethod.POST)		
	public String saveDraftUnifiedShipment(
			@ModelAttribute("UnifiedShipment") UnifiedShipmentImpl unifiedShipment,
			RedirectAttributes redirectAttributes, Model model){		
		unifiedShipment.setType(ShipmentType.UNIFIED);
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm Z");
		Date date = new Date();
		String dateCreated = dateFormat.format(date);
		unifiedShipment.setDateCreated(dateCreated);
		if (isValidShipment(unifiedShipment, dateCreated)) {
			String draftName = this.getMaintainShipmentUnsUco().insertDraft(unifiedShipment);															
			return "redirect:/UnifiedShipments/"+draftName+"/view";		
		}
		redirectAttributes.addFlashAttribute("message", "Unified Shipment not saved. Incorrect input for required fields");
		return "redirect:/UnifiedShipments";
	}
	
	private Boolean isValidShipment(UnifiedShipmentImpl unifiedShipment, String dateCreated) {
		return StringUtils.hasText(unifiedShipment.getSellerCompanyKcode()) && StringUtils.hasText(unifiedShipment.getFbaId()) && 
				StringUtils.hasText(unifiedShipment.getDestinationCountry()) && StringUtils.hasText(unifiedShipment.getExpectArrivalDate()) &&
				unifiedShipment.getExpectArrivalDate().length() == dateCreated.length();
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('UNIFIED_SHIPMENTS_EDIT'))")
	@RequestMapping(value = "/updateDraft", method = RequestMethod.POST)			
	public String updateDraftUnifiedShipment(@ModelAttribute("UnifiedShipment") UnifiedShipmentImpl unifiedShipment,Model model){		
		unifiedShipment.setType(ShipmentType.UNIFIED);
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm Z");
		Date date = new Date();
		String dateCreated = dateFormat.format(date);
		unifiedShipment.setDateCreated(dateCreated);				
		String draftName = this.getMaintainShipmentUnsUco().update(unifiedShipment);															
		return "redirect:/UnifiedShipments/"+draftName+"/view";						
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('UNIFIED_SHIPMENTS_EDIT'))")
	@RequestMapping(value = "/{shipmentId}/freeze")	
	public String confirmUnifiedShipment(Model model, @PathVariable String shipmentId,final RedirectAttributes redirectAttributes,Locale locale){		
		this.getMaintainShipmentUnsUco().freeze(shipmentId);
		MessageSource source = (MessageSource)SpringAppCtx.get().getBean("messageSource");
		String message = source.getMessage("unifiedShipment.freezeUS", null, locale);
		redirectAttributes.addFlashAttribute("message",message);		
		return "redirect:/UnifiedShipments";					
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('UNIFIED_SHIPMENTS_EDIT'))")
	@RequestMapping(value = "/updateStatus", method = RequestMethod.POST)			
	public String updateUnifiedShipmentStatus(
			@ModelAttribute("UnifiedShipment") UnifiedShipmentImpl unifiedShipment,
			RedirectAttributes redirectAttributes){

		String shipmentName = unifiedShipment.getName();

		if (unifiedShipment.getStatus() == ShipmentStatus.SHPT_RECEIVED
				&& !StringUtils.hasText(unifiedShipment.getDestinationReceivedDate())) {

			redirectAttributes.addFlashAttribute("message", "Unified Shipment not saved. Actual received date required.");
		} else {
			shipmentName = this.getMaintainShipmentUnsUco().update(unifiedShipment);
		}
		return "redirect:/UnifiedShipments/"+shipmentName+"/view";		
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('UNIFIED_SHIPMENTS_VIEW'))")
	@RequestMapping(value = "/{shipmentId}/view")	
	public String showUnifiedShipment(Model model, @PathVariable String shipmentId){		
		UnifiedShipmentImpl uns = new UnifiedShipmentImpl(this.getMaintainShipmentUnsUco().get(shipmentId)); 	
		Map<String, String> drsCompanyKcodeToNameMap = this.getMaintainShipmentUnsUco().getDrsCompanyKcodeToNameMap();	    				
		model.addAttribute("DrsCompanyKcodeToNameMap",drsCompanyKcodeToNameMap);			
		model.addAttribute("UnifiedShipment",uns);
		model.addAttribute("UnifiedShipmentJson",JsonHelper.toJson(uns));
		model.addAttribute("forwarderList", this.getMaintainShipmentUnsUco().getForwarderList());				
		return "UnifiedShipmentView";		
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('UNIFIED_SHIPMENTS_VIEW'))")
	@RequestMapping(value = "/{shipmentName}/edit")	
	public String editUnifiedShipment(Model model, @PathVariable String shipmentName){							
		UnifiedShipmentImpl uns = new UnifiedShipmentImpl(this.getMaintainShipmentUnsUco().get(shipmentName)); 		
		Map<String, String> drsCompanyKcodeToName = this.getMaintainShipmentUnsUco().getDrsCompanyKcodeToNameMap();
		ShipmentIvsSearchConditionImpl condition = new ShipmentIvsSearchConditionImpl(null,null,null,null);

		DtoList<ShipmentIvs>  shipmentOfInventoryList = this.getMaintainShipmentIvsUco().retrieveList(condition,1);
		model.addAttribute("ShipmentOfInventoryList", shipmentOfInventoryList.getItems());

		model.addAttribute("drsCompanyKcodeToName", drsCompanyKcodeToName);
		model.addAttribute("drsCompanyKcodeToNameJson", JsonHelper.toJson(drsCompanyKcodeToName));		
		model.addAttribute("type", "Edit");
		model.addAttribute("status", "Draft");		
		model.addAttribute("UnifiedShipment",uns);
		model.addAttribute("UnifiedShipmentJson",JsonHelper.toJson(uns));
		model.addAttribute("forwarderList", this.getMaintainShipmentUnsUco().getForwarderList());				
		return "UnifiedShipment";		
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('UNIFIED_SHIPMENTS_DELETE'))")
	@RequestMapping(value = "/{shipmentId}/delete")	
	public String deleteUnifiedShipment(Model model, @PathVariable String shipmentId,final RedirectAttributes redirectAttributes,Locale locale){				
		this.getMaintainShipmentUnsUco().deleteDraft(shipmentId);
		MessageSource source = (MessageSource)SpringAppCtx.get().getBean("messageSource");
		String message = source.getMessage("unifiedShipment.deleteUS", null, locale);
		redirectAttributes.addFlashAttribute("message",message);		
		return "redirect:/UnifiedShipments";		
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('UNIFIED_SHIPMENTS_LIST'))")
	@RequestMapping(value = "/getAvailableInventoryShipmentNameList", method = RequestMethod.GET)			
	public @ResponseBody String getAvailableInventoryShipmentNameList(@RequestParam("sellerKcode") String sellerKcode,@RequestParam("destinationCountryCode") String destinationCountryCode){
		List<String> availableInventoryShipmentNameList = this.getMaintainShipmentUnsUco().getAvailableIvsNames(sellerKcode,destinationCountryCode);
		return JsonHelper.toJson(availableInventoryShipmentNameList);	
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('UNIFIED_SHIPMENTS_LIST'))")
	@RequestMapping(value = "/getAvailableSkusInInventoryShipment", method = RequestMethod.GET)		
	public @ResponseBody String getAvailableSkusInInventoryShipment(@RequestParam("shipmentName") String shipmentName){					
		Map<String,String> availableSkusInInventoryShipment = this.getMaintainShipmentUnsUco().getAvailableSkusInInventoryShipment(shipmentName);				
		return JsonHelper.toJson(availableSkusInInventoryShipment);				
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('UNIFIED_SHIPMENTS_LIST'))")
	@RequestMapping(value = "/getShipmentLineItem", method = RequestMethod.GET)			
	public @ResponseBody String getShipmentLineItem(@RequestParam("inventoryShipmentName") String inventoryShipmentName){		
		List<IvsLineItem> shipmentLineItem = this.getMaintainShipmentUnsUco().getIvsLineItem(inventoryShipmentName);
		return JsonHelper.toJson(shipmentLineItem);				
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('UNIFIED_SHIPMENTS_LIST'))")	
	@RequestMapping(value = "/getCompanyCurrency", method = RequestMethod.GET)				
	public @ResponseBody String getCompanyCurrency(@RequestParam("kcode") String kcode){				
		return JsonHelper.toJson(this.getMaintainShipmentUnsUco().getCompanyCurrency(kcode));		
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('UNIFIED_SHIPMENTS_LIST'))")
	@RequestMapping(value = "/getDestinationCountryCodeList", method = RequestMethod.GET)					
	public @ResponseBody String getDestinationCountryCodeList(@RequestParam("sellerKcode") String sellerKcode){			
		return JsonHelper.toJson(this.getMaintainShipmentUnsUco().getDestinationCountryCodes(sellerKcode));				
	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('UNIFIED_SHIPMENTS_LIST'))")
	@RequestMapping(value = "/getActualDestinationCountryCodeList", method = RequestMethod.GET)
	public @ResponseBody String getActualDestinationCountryCodeList(@RequestParam("destinationCountry") String destinationCountry){
		return JsonHelper.toJson(this.getMaintainShipmentUnsUco().getActualDestinationCountryCodes(destinationCountry));
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('UNIFIED_SHIPMENTS_LIST'))")	
	@RequestMapping(value = "/getDrsCompanyKcode", method = RequestMethod.GET)						
	public @ResponseBody String getDrsCompanyKcode(@RequestParam("countryCode") String countryCode){		
		return JsonHelper.toJson(this.getMaintainShipmentUnsUco().getDrsCompanyKcode(countryCode));		
	}
		
}