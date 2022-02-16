package com.kindminds.drs.web.ctrl.logistics;

import static akka.pattern.Patterns.ask;
import static org.springframework.web.util.HtmlUtils.htmlEscape;

import java.io.*;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindminds.drs.UserInfo;
import com.kindminds.drs.api.message.query.ivs.GetIvsShippingCosts;
import com.kindminds.drs.api.message.query.marketing.amazonSponsoredBrandsCampaign.GetSupplierKcodeToShortEnNameMap;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.Ivs;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShipmentStatus;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShipmentType;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShippingMethod;
import com.kindminds.drs.api.v2.biz.domain.model.request.SaveIvsLineitemRequest;
import com.kindminds.drs.api.v2.biz.domain.model.request.SaveIvsRequest;
import com.kindminds.drs.api.v1.model.logistics.ShipmentIvs;

import com.kindminds.drs.api.usecase.logistics.MaintainShipmentIvsUco;
import com.kindminds.drs.web.config.DrsActorSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kindminds.drs.Context;

import com.kindminds.drs.api.usecase.inventory.ViewInventoryPaymentReportUco;
import com.kindminds.drs.api.v1.model.sales.PurchaseOrderInfo;
import com.kindminds.drs.api.v1.model.sales.PurchaseOrderSkuInfo;




import com.kindminds.drs.api.v1.model.common.DtoList;
import com.kindminds.drs.api.v1.model.common.Pager;
import com.kindminds.drs.service.util.SpringAppCtx;
import com.kindminds.drs.util.JsonHelper;
import com.kindminds.drs.web.ctrl.accounting.Ss2spStatementController;
import com.kindminds.drs.web.data.dto.InventoryShipmentImpl;
import com.kindminds.drs.web.data.dto.InventoryShipmentLineItemImpl;
import com.kindminds.drs.web.data.dto.ShipmentIvsSearchConditionImpl;

import akka.actor.ActorRef;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

@Controller
public class InventoryShipmentController {


	ActorRef drsCmdBus = DrsActorSystem.drsCmdBus();

	ActorRef drsQueryBus = DrsActorSystem.drsQueryBus();

	private MaintainShipmentIvsUco getUco(){
		return (MaintainShipmentIvsUco)(SpringAppCtx.get().getBean("maintainShipmentIvsUcoImpl"));
	}
	
	private ViewInventoryPaymentReportUco getInventoryPaymentReportUco(){
		return (ViewInventoryPaymentReportUco)(SpringAppCtx.get().getBean("viewInventoryPaymentReportUcoImpl"));
	}
		
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('INVENTORY_SHIPMENTS_LIST'))")
	@RequestMapping(value = "/InventoryShipments",method = RequestMethod.GET)
	public String listOfInventoryShipments(
			@ModelAttribute("ShipmentIvsSearchCondition") ShipmentIvsSearchConditionImpl conditions ,
			@RequestParam(value="page",defaultValue="1",required=false) Integer pageIndex,Model model) {


		MessageSource source = (MessageSource) SpringAppCtx.get().getBean("messageSource");
		UserInfo user = Context.getCurrentUser();
		String companyCode = user.getCompanyKcode();
		String importTemplate = "";

		if (companyCode.equals("K101")) {
			importTemplate = source.getMessage("inventoryShipment.importTemplateLink",
					new Object[] {"https://access.drs.network/resources/files/k101_ivs_template.csv"},
					user.getLocale());
		} else if (user.isSupplier()) {
			importTemplate = source.getMessage("inventoryShipment.importTemplateLink",
					new Object[] {"https://access.drs.network/resources/files/ivs_template.csv"},
					user.getLocale());
		}

		DtoList<ShipmentIvs> shipmentOfInventoryList = this.getUco().retrieveList(conditions,pageIndex);


		Pager page = shipmentOfInventoryList.getPager();

		Map<String, String> allCompanyKcodeToNameMap = this.getUco().getAllCompanyKcodeToNameMap();
		Map<String, String> sellerKcodeToNameMap = this.getUco().getSellerKcodeToNameMap();
		String kcode = Context.getCurrentUser().getCompanyKcode();
		List<String> destinationCountryList = this.getUco().getDestinationCountries();
		List<ShippingMethod> shippingMethodList = this.getUco().getShippingMethods();
		List<ShipmentStatus> shipmentStatusList = this.getUco().getShipmentStatusList();


		model.addAttribute("FCADeliveryLocationIdToLocationMap",this.getDeliveryLocationIdToLocationMap());
		model.addAttribute("AllCompanyKcodeToNameMap",allCompanyKcodeToNameMap);
		model.addAttribute("sellerKcodeToNameMap",sellerKcodeToNameMap);	
		model.addAttribute("destinationCountryList",destinationCountryList);	
		model.addAttribute("shippingMethodList",shippingMethodList);
		model.addAttribute("shipmentStatusList",shipmentStatusList);		
		model.addAttribute("totalPages",page.getTotalPages());
		model.addAttribute("currentPageIndex",page.getCurrentPageIndex());
		model.addAttribute("startPage",page.getStartPage());
		model.addAttribute("endPage",page.getEndPage());
		model.addAttribute("ShipmentOfInventoryList", shipmentOfInventoryList.getItems());
		model.addAttribute("IvsSearchConditions", this.getIvsSearchConditions(conditions));
		model.addAttribute("importTemplate", importTemplate);
		model.addAttribute("isSupplier",Context.getCurrentUser().isSupplier());

		return "th/inventoryShipment/ListOfInventoryShipments";
	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('INVENTORY_SHIPMENTS_CREATE'))")
	@RequestMapping(value = "/InventoryShipments/create", method = RequestMethod.GET)
	public String createInventoryShipment(@ModelAttribute("InventoryShipment") SaveIvsRequest inventoryShipment, Model model){

		String kcode = Context.getCurrentUser().getCompanyKcode();
		Map<String, String> activeSkuCodeToNameMapJson = this.getUco().getActiveAndOnboardingSkuCodeToSupplierNameMap();
		inventoryShipment.getLineItem().add(new SaveIvsLineitemRequest());
		List<String> destinationCountryList = this.getUco().getApprovedDestinationCountries(kcode);
//		List<String> activeSkuCodeToNameMap = this.getUco().getActiveAndOnboardingSkuCode(kcode,"US");
		String defaultSalesTaxPercentage = this.getUco().getDefaultSalesTaxPercentage();


//		model.addAttribute("ActiveSkuCodeToNameMap",activeSkuCodeToNameMap);
//		model.addAttribute("ActiveSkuCodeToNameMapJson",activeSkuCodeToNameMapJson);
		//model.addAttribute("ActiveSkuCodeToNameMapJson",JsonHelper.toJson(activeSkuCodeToNameMap));
		model.addAttribute("destinationCountryList",destinationCountryList);
//		model.addAttribute("defaultSalesTaxPercentage", JsonHelper.toJson(defaultSalesTaxPercentage));
		model.addAttribute("defaultSalesTaxPercentage", defaultSalesTaxPercentage);
		model.addAttribute("InventoryShipment",inventoryShipment);
		model.addAttribute("type", "Create");
		model.addAttribute("status","null");
//		model.addAttribute("isDrsUser",JsonHelper.toJson(Context.getCurrentUser().isDrsUser()));
//		model.addAttribute("isSupplier",JsonHelper.toJson(Context.getCurrentUser().isSupplier()));
		model.addAttribute("isDrsUser",Context.getCurrentUser().isDrsUser());
		model.addAttribute("isSupplier",Context.getCurrentUser().isSupplier());
		model.addAttribute("InventoryShipmentJson",JsonHelper.toJson(null));
		model.addAttribute("SupplierKcode",Context.getCurrentUser().getCompanyKcode());
		return "th/inventoryShipment/InventoryShipment";
	}

	@RequestMapping(value = "/InventoryShipments/getFcaDeliveryLocationIdToLocationMap", method = RequestMethod.GET)
	public @ResponseBody String getFcaDeliveryLocationIdToLocationMap(HttpServletRequest request, HttpServletResponse response){
		try
		{
			Map<Integer, String> FCADeliveryLocationIdToLocationMap = this.getUco().getFcaDeliveryLocationIdToLocationMap();
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=utf-8");
			response.setHeader("Cache-Control", "no-cache");
			PrintWriter out = response.getWriter();
			out.print(JsonHelper.toJson(FCADeliveryLocationIdToLocationMap).toString());
			out.flush();
			out.close();
		}
		catch (Exception e) {
		}
		return null;
	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('INVENTORY_SHIPMENTS_VIEW'))")
	@RequestMapping(value = "/InventoryShipments/saveDraft", method = RequestMethod.POST)
	public String saveDraftInventoryShipment(
			@ModelAttribute("InventoryShipment") SaveIvsRequest saveIvsDraftRequest
			,Model model){
		saveIvsDraftRequest.setType(ShipmentType.INVENTORY);

		String draftName = this.getUco().createDraft(saveIvsDraftRequest);

		return "redirect:/InventoryShipments/"+draftName;
	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('INVENTORY_SHIPMENTS_VIEW'))")
	@RequestMapping(value = "/InventoryShipments/saveAndSubmitDraft", method = RequestMethod.POST)
	public String saveAndSubmitDraftInventoryShipment(
			@ModelAttribute("InventoryShipment") SaveIvsRequest saveIvsDraftRequest
			,Model model){

		saveIvsDraftRequest.setType(ShipmentType.INVENTORY);

		String draftName = this.getUco().createDraft(saveIvsDraftRequest);

		return "redirect:/InventoryShipments/"+draftName+"/submit";
	}

	@Autowired
	@Qualifier("envProperties")
	private Properties env;

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('INVENTORY_SHIPMENTS_VIEW'))")
	@RequestMapping(value = "/InventoryShipments/{shipmentId}/editDraft")
	public String editDraftInventoryShipment(Model model,
											 @PathVariable String shipmentId) throws IOException{

		String kcode = Context.getCurrentUser().getCompanyKcode();
		//InventoryShipmentImpl ivs = new InventoryShipmentImpl(this.getUco().get(shipmentId));

		ShipmentIvs shipmentIvs = this.getUco().get(shipmentId);
		String skuList = getSkuList(shipmentIvs.getDestinationCountry(),kcode);

		Map<String, String> activeSkuCodeToNameMap =
				this.getUco().getActiveAndOnboardingSkuCodeToSupplierNameMap(shipmentIvs.getSellerCompanyKcode());
		List<String> destinationCountryList = this.getUco().getApprovedDestinationCountries(kcode);


		model.addAttribute("type", "Edit");
		model.addAttribute("status", "DRAFT");
//		model.addAttribute("isDrsUser",JsonHelper.toJson(Context.getCurrentUser().isDrsUser()));
//		model.addAttribute("isSupplier",JsonHelper.toJson(Context.getCurrentUser().isSupplier()));
		model.addAttribute("isDrsUser",Context.getCurrentUser().isDrsUser());
		model.addAttribute("isSupplier",Context.getCurrentUser().isSupplier());
		model.addAttribute("SupplierKcode",Context.getCurrentUser().getCompanyKcode());
		model.addAttribute("ActiveSkuCodeToNameMap",activeSkuCodeToNameMap);
		model.addAttribute("ActiveSkuCodeToNameMapJson",JsonHelper.toJson(activeSkuCodeToNameMap));
		model.addAttribute("destinationCountryList",destinationCountryList);
		model.addAttribute("defaultSalesTaxPercentage", JsonHelper.toJson(null));
		model.addAttribute("skuList",skuList);

		model.addAttribute("InventoryShipment",shipmentIvs);

		ObjectMapper om  = new ObjectMapper();
		try {
			String j= om.writeValueAsString(shipmentIvs);

			model.addAttribute("InventoryShipmentJson", j);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		//model.addAttribute("InventoryShipmentJson",JsonHelper.toJson(shipmentIvs));



		return "th/inventoryShipment/InventoryShipment";
	}


	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('INVENTORY_SHIPMENTS_VIEW'))")
	@RequestMapping(value = "/InventoryShipments/updateDraft", method = RequestMethod.POST)
	public String updateDraftInventoryShipment(HttpServletRequest request,
											   @ModelAttribute("InventoryShipment") SaveIvsRequest saveIvsRequest,
											   Model model){

		saveIvsRequest.setStatus(ShipmentStatus.SHPT_DRAFT);

		String draftName = this.getUco().update(saveIvsRequest);


		return "redirect:/InventoryShipments/"+draftName;
	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('INVENTORY_SHIPMENTS_VIEW'))")
	@RequestMapping(value = "/InventoryShipments/updateAndSubmitDraft", method = RequestMethod.POST)
	public String updateAndSubmitDraftInventoryShipment(HttpServletRequest request,
											   @ModelAttribute("InventoryShipment") SaveIvsRequest saveIvsRequest,
											   Model model){

		saveIvsRequest.setStatus(ShipmentStatus.SHPT_DRAFT);

		String draftName = this.getUco().update(saveIvsRequest);


		return "redirect:/InventoryShipments/"+draftName+"/submit";
	}


	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('INVENTORY_SHIPMENTS_SUBMIT'))")
	@RequestMapping(value = "/InventoryShipments/{shipmentId}/submit")
	public String submitInventoryShipment(Model model, @PathVariable String shipmentId,
										  final RedirectAttributes redirectAttributes,Locale locale){

		String ivsName = this.getUco().submit(shipmentId);
		MessageSource source = (MessageSource)SpringAppCtx.get().getBean("messageSource");

		String message  = source.getMessage("inventoryShipment.submitIS", null,locale);
		redirectAttributes.addFlashAttribute("message",message);



		return "redirect:/InventoryShipments/"+ivsName;
	}


	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('INVENTORY_SHIPMENTS_EDIT'))")
	@RequestMapping(value = "/InventoryShipments/{shipmentId}/edit")
	public String editInventoryShipment(Model model, @PathVariable String shipmentId) throws IOException{


//		String kcode = Context.getCurrentUser().getCompanyKcode();

		ShipmentIvs shipmentIvs = this.getUco().get(shipmentId);
		String kcode =shipmentIvs.getSellerCompanyKcode();

		Map<String, String> activeSkuCodeToNameMap = this.getUco().
				getActiveAndOnboardingSkuCodeToSupplierNameMap(shipmentIvs.getSellerCompanyKcode());

		Map<String, String> allCompanyKcodeToNameMap = this.getUco().getAllCompanyKcodeToNameMap();
		List<String> destinationCountryList = this.getUco().getApprovedDestinationCountries(kcode);

		String shippingCost = getShippingCostMessage(shipmentIvs, shipmentId);
		String skuList = getSkuList(shipmentIvs.getDestinationCountry(),kcode);

		model.addAttribute("type", "Edit");
		model.addAttribute("status", "noDRAFT");
//		model.addAttribute("isDrsUser",JsonHelper.toJson(Context.getCurrentUser().isDrsUser()));
//		model.addAttribute("isSupplier",JsonHelper.toJson(Context.getCurrentUser().isSupplier()));
		model.addAttribute("isDrsUser",Context.getCurrentUser().isDrsUser());
		model.addAttribute("isSupplier",Context.getCurrentUser().isSupplier());
		model.addAttribute("SupplierKcode",Context.getCurrentUser().getCompanyKcode());
		model.addAttribute("AllCompanyKcodeToNameMap",allCompanyKcodeToNameMap);
		model.addAttribute("ActiveSkuCodeToNameMap",activeSkuCodeToNameMap);
		model.addAttribute("ActiveSkuCodeToNameMapJson",JsonHelper.toJson(activeSkuCodeToNameMap));
		model.addAttribute("destinationCountryList",destinationCountryList);
		model.addAttribute("defaultSalesTaxPercentage", JsonHelper.toJson(null));
		model.addAttribute("InventoryShipment",shipmentIvs);
		//model.addAttribute("InventoryShipmentJson",JsonHelper.toJson(shipmentIvs));
		model.addAttribute("FCADeliveryLocationIdToLocationMap",this.getDeliveryLocationIdToLocationMap());
		model.addAttribute("shippingCost", shippingCost);
		model.addAttribute("skuList",skuList);
		ObjectMapper om  = new ObjectMapper();
		try {
			String j= om.writeValueAsString(shipmentIvs);

			model.addAttribute("InventoryShipmentJson", j);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}



		return "th/inventoryShipment/InventoryShipment";
	}

	private String getShippingCostMessage(ShipmentIvs shipmentIvs, String shipmentId) {

		if (Context.getCurrentUser().isDrsUser() &&
				shipmentIvs.getStatus() != ShipmentStatus.SHPT_DRAFT &&
				shipmentIvs.getStatus() != ShipmentStatus.SHPT_AWAIT_PLAN &&
				shipmentIvs.getStatus() != ShipmentStatus.SHPT_FC_BOOKING_CONFIRM &&
				shipmentIvs.getStatus() != ShipmentStatus.SHPT_PLANNING &&
				shipmentIvs.getStatus() != ShipmentStatus.SHPT_INITIAL_VERIFIED) {

			Timeout timeout = new Timeout(Duration.create(10, "seconds"));
			final Future<Object> future1 =
					ask(drsQueryBus, new GetIvsShippingCosts(shipmentId), timeout);
			try {
				return (String) Await.result(future1, timeout.duration());

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return "";
	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('INVENTORY_SHIPMENTS_VIEW'))")
	@RequestMapping(value = "/InventoryShipments/update", method = RequestMethod.POST)
	public String updateInventoryShipment(HttpServletRequest request,
										  @ModelAttribute("InventoryShipment") SaveIvsRequest saveIvsRequest,
										  Model model){

		/*
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm Z");
		Date date = new Date();
		String dateCreated = dateFormat.format(date);
		inventoryShipment.setDateCreated(dateCreated);
		*/

		String shipmentId = this.getUco().update(saveIvsRequest);

		return "redirect:/InventoryShipments/"+shipmentId;

	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('INVENTORY_SHIPMENTS_ACCEPT'))")
	@RequestMapping(value = "/InventoryShipments/{shipmentId}/accept")
	public String acceptInventoryShipment(Model model, @PathVariable String shipmentId,final RedirectAttributes redirectAttributes,Locale locale){
		String ivsName = this.getUco().accept(shipmentId);
		MessageSource source = (MessageSource)SpringAppCtx.get().getBean("messageSource");
		String message  = source.getMessage("inventoryShipment.acceptIS", null,locale);
		redirectAttributes.addFlashAttribute("message",message);
		return "redirect:/InventoryShipments/"+ivsName;
	}


	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('INVENTORY_SHIPMENTS_CONFIRM'))")
	@RequestMapping(value = "/InventoryShipments/{shipmentId}/confirm")
	public String confirmInventoryShipment(Model model, @PathVariable String shipmentId,
										   final RedirectAttributes redirectAttributes,Locale locale){

		this.getUco().confirm(shipmentId);
		MessageSource source = (MessageSource)SpringAppCtx.get().getBean("messageSource");
		String message  = source.getMessage("inventoryShipment.confirmIS", null,locale);
		redirectAttributes.addFlashAttribute("message",message);

		return "redirect:/InventoryShipments/"+shipmentId;
	}


	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('INVENTORY_SHIPMENTS_DELETE'))")
	@RequestMapping(value = "/InventoryShipments/{shipmentId}/delete")
	public String deleteInventoryShipment(Model model, @PathVariable String shipmentId,final RedirectAttributes redirectAttributes,Locale locale){

		this.getUco().delete(shipmentId);
		MessageSource source = (MessageSource)SpringAppCtx.get().getBean("messageSource");
		String message  = source.getMessage("inventoryShipment.deleteIS", null,locale);
		redirectAttributes.addFlashAttribute("message",message);

		return "redirect:/InventoryShipments";
	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('INVENTORY_SHIPMENTS_VIEW'))")
	@RequestMapping(value = "/InventoryShipments/{shipmentId}")
	public String showInventoryShipment(Model model, @PathVariable String shipmentId){

		ShipmentIvs shipmentOfInventory = this.getUco().get(shipmentId);

		Map<String, String> allCompanyKcodeToNameMap = this.getUco().getAllCompanyKcodeToNameMap();
		Map<String, String> activeSkuCodeToNameMap = this.getUco().getActiveAndOnboardingSkuCodeToSupplierNameMap();

		String shippingCost = getShippingCostMessage(shipmentOfInventory, shipmentId);

		ObjectMapper om  = new ObjectMapper();
		try {
			String j= om.writeValueAsString(shipmentOfInventory);

			model.addAttribute("InventoryShipmentJson", j);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		model.addAttribute("FCADeliveryLocationIdToLocationMap",this.getDeliveryLocationIdToLocationMap());
		model.addAttribute("ActiveSkuCodeToNameMap",activeSkuCodeToNameMap);
		model.addAttribute("AllCompanyKcodeToNameMap",allCompanyKcodeToNameMap);
		model.addAttribute("InventoryShipment",shipmentOfInventory);
		
//		model.addAttribute("InventoryShipmentJson",JsonHelper.toJson(shipmentOfInventory));
		model.addAttribute("shippingCost", shippingCost);
		model.addAttribute("shipmentId", shipmentId);
		model.addAttribute("SupplierKcode",shipmentOfInventory.getSellerCompanyKcode());
		return "th/inventoryShipment/InventoryShipmentView";
	}


	//============================


	//=====================================================
	//GUI


	@RequestMapping(value = "/InventoryShipments/isGUIInvoiceIsRequired", method = RequestMethod.GET)
	public @ResponseBody String isGUIInvoiceIsRequired(@RequestParam("sku") String sku){

		return JsonHelper.toJson(this.getUco().isGuiInvoiceRequired(sku));
	}


	@RequestMapping(value="/InventoryShipments/uploadGUIInvoiceFile/{supplierKcode}",method=RequestMethod.POST)
	public @ResponseBody String uploadGUIInvoiceFile(MultipartHttpServletRequest request,
													 HttpServletResponse response , @PathVariable String supplierKcode) {
		Iterator<String> itr = request.getFileNames();
		MultipartFile mpf = request.getFile(itr.next());
		String uuid = new String();
		try {
			String originalFileName = mpf.getOriginalFilename();
			byte[] fileBytes = mpf.getBytes();
			uuid=this.getUco().saveGuiInvoiceFile(supplierKcode,originalFileName, fileBytes);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return JsonHelper.toJson(uuid);
	}


	@RequestMapping(value="/InventoryShipments/removeGUIInvoiceFile/{supplierKcode}",method=RequestMethod.POST)
	public @ResponseBody String removeGUIInvoiceFile(@RequestParam("GUIInvoiceFileName") String GUIInvoiceFileName,
													   @RequestParam("GUIuuid") String uuid,
													   @PathVariable String supplierKcode){
		String fullFilename= uuid+GUIInvoiceFileName;
		this.getUco().removeGuiInvoiceFile(supplierKcode,fullFilename);
		return JsonHelper.toJson(GUIInvoiceFileName);
	}
	private static final int BUFFER_SIZE = 4096;

@RequestMapping(value = "/InventoryShipments/downloadGUIInvoiceFile", method = RequestMethod.GET)
public void downloadGUIInvoiceFile(@RequestParam("SupplierKcode") String supplierKcode,
								   @RequestParam("GUIuuid") String uuid,
								   @RequestParam("GUIInvoiceFileName") String GUIInvoiceFileName,
								   HttpServletResponse response) throws IOException{
	String fullFileName = uuid+GUIInvoiceFileName;
	File downloadFile = this.getUco().getGuiInvoiceFile(supplierKcode, fullFileName);

	// MIME type of the file
	String mimeType= URLConnection.guessContentTypeFromName(GUIInvoiceFileName);
	if (mimeType == null) {
		// Set to binary type if MIME mapping not found
		mimeType = "application/octet-stream";
	}
	// set content attributes for the response object
	response.setContentType(mimeType);
	response.setContentLength((int) downloadFile.length());
	// set headers for the response object
	String headerKey = "Content-Disposition";
//	String headerValue = String.format("attachment; filename=\"%s\"",
//			downloadFile.getName());
	String headerValue = String.format("attachment; filename=\"%s\"",
			GUIInvoiceFileName);
	response.setHeader(headerKey, headerValue);
	// get output stream of the response
	try (OutputStream outStream = response.getOutputStream();
		 FileInputStream inputStream = new FileInputStream(downloadFile)) {
		byte[] buffer = new byte[BUFFER_SIZE];
		int bytesRead = -1;

		// write each byte of data  read from the input stream into the output stream
		while ((bytesRead = inputStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, bytesRead);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}


}


	//=====================================================



	//=========================================
	// V4

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('INVENTORY_SHIPMENTS_LIST'))")
	@RequestMapping(value = "/InventoryShipmentsV4",method = RequestMethod.GET)
	public String listOfInventoryShipmentsV4(@ModelAttribute("ShipmentIvsSearchCondition") ShipmentIvsSearchConditionImpl conditions ,@RequestParam(value="page",defaultValue="1",required=false) Integer pageIndex,Model model) {
		DtoList<ShipmentIvs> shipmentOfInventoryList = this.getUco().retrieveList(conditions,pageIndex);
		Pager page = shipmentOfInventoryList.getPager();
		String kcode = Context.getCurrentUser().getCompanyKcode();
		Map<String, String> allCompanyKcodeToNameMap = this.getUco().getAllCompanyKcodeToNameMap();
		Map<String, String> sellerKcodeToNameMap = this.getUco().getSellerKcodeToNameMap();
		List<String> destinationCountryList = this.getUco().getDestinationCountries();
		List<ShippingMethod> shippingMethodList = this.getUco().getShippingMethods();
		List<ShipmentStatus> shipmentStatusList = this.getUco().getShipmentStatusList();
		model.addAttribute("FCADeliveryLocationIdToLocationMap",this.getDeliveryLocationIdToLocationMap());
		model.addAttribute("AllCompanyKcodeToNameMap",allCompanyKcodeToNameMap);
		model.addAttribute("sellerKcodeToNameMap",sellerKcodeToNameMap);
		model.addAttribute("destinationCountryList",destinationCountryList);
		model.addAttribute("shippingMethodList",shippingMethodList);
		model.addAttribute("shipmentStatusList",shipmentStatusList);
		model.addAttribute("totalPages",page.getTotalPages());
		model.addAttribute("currentPageIndex",page.getCurrentPageIndex());
		model.addAttribute("startPage",page.getStartPage());
		model.addAttribute("endPage",page.getEndPage());
		model.addAttribute("ShipmentOfInventoryList", shipmentOfInventoryList.getItems());
		model.addAttribute("IvsSearchConditions", this.getIvsSearchConditions(conditions));
		return "ListOfInventoryShipmentsV4";
	}
	


	//@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('INVENTORY_SHIPMENTS_CREATE'))")
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('INVENTORY_SHIPMENTS_VIEW'))")
	@RequestMapping(value = "/InventoryShipmentsV4/create", method = RequestMethod.GET)
	public String createInventoryShipmentV4(@ModelAttribute("InventoryShipment") InventoryShipmentImpl inventoryShipment,Model model){
		Map<String, String> activeSkuCodeToNameMap = this.getUco().getActiveAndOnboardingSkuCodeToSupplierNameMap();
		inventoryShipment.lineItems.add(new InventoryShipmentLineItemImpl());
		String kcode = Context.getCurrentUser().getCompanyKcode();
		List<String> destinationCountryList = this.getUco().getApprovedDestinationCountries(kcode);
		String defaultSalesTaxPercentage = this.getUco().getDefaultSalesTaxPercentage();
		model.addAttribute("ActiveSkuCodeToNameMap",activeSkuCodeToNameMap);
		model.addAttribute("ActiveSkuCodeToNameMapJson",JsonHelper.toJson(activeSkuCodeToNameMap));
		model.addAttribute("destinationCountryList",destinationCountryList);
		model.addAttribute("defaultSalesTaxPercentage", JsonHelper.toJson(defaultSalesTaxPercentage));
		model.addAttribute("InventoryShipment",inventoryShipment);
		model.addAttribute("type", "Create");
		model.addAttribute("status","null");
		model.addAttribute("isDrsUser",JsonHelper.toJson(Context.getCurrentUser().isDrsUser()));
		model.addAttribute("isSupplier",JsonHelper.toJson(Context.getCurrentUser().isSupplier()));
		model.addAttribute("InventoryShipmentJson",JsonHelper.toJson(null));
		return "InventoryShipmentV4";
	}


	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('INVENTORY_SHIPMENTS_VIEW'))")
	@RequestMapping(value = "/InventoryShipmentsV4/{shipmentId}")
	public String showInventoryShipmentV4(Model model, @PathVariable String shipmentId){


		ShipmentIvs shipmentOfInventory = this.getUco().get(shipmentId);
		Map<String, String> allCompanyKcodeToNameMap = this.getUco().getAllCompanyKcodeToNameMap();
		Map<String, String> activeSkuCodeToNameMap = this.getUco().getActiveAndOnboardingSkuCodeToSupplierNameMap();

		String shippingCost = getShippingCostMessage(shipmentOfInventory, shipmentId);


		model.addAttribute("FCADeliveryLocationIdToLocationMap",this.getDeliveryLocationIdToLocationMap());
		model.addAttribute("ActiveSkuCodeToNameMap",activeSkuCodeToNameMap);
		model.addAttribute("AllCompanyKcodeToNameMap",allCompanyKcodeToNameMap);
		model.addAttribute("InventoryShipment",shipmentOfInventory);
		model.addAttribute("InventoryShipmentJson",JsonHelper.toJson(shipmentOfInventory));
		model.addAttribute("shippingCost", shippingCost);
		model.addAttribute("shipmentId", shipmentId);
		return "InventoryShipmentViewV4";
	}


	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('INVENTORY_SHIPMENTS_EDIT'))")
	@RequestMapping(value = "/InventoryShipmentsV4/{shipmentId}/edit")
	public String editInventoryShipmentV4(Model model, @PathVariable String shipmentId) throws IOException{

		//todo arthur
		/*
		Ivs shipmentOfInventory = this.getUco().get(shipmentId);
		InventoryShipmentImpl ivs = new InventoryShipmentImpl(shipmentOfInventory);
		Map<String, String> activeSkuCodeToNameMap = this.getUco().getActiveAndOnboardingSkuCodeToSupplierNameMap(ivs.getSellerCompanyKcode());
		Map<String, String> allCompanyKcodeToNameMap = this.getUco().getAllCompanyKcodeToNameMap();
		String kcode = Context.getCurrentUser().getCompanyKcode();
		List<String> destinationCountryList = this.getUco().getDestinationCountries(kcode);

		String shippingCost = null;
		if (ivs.getStatus() != ShipmentStatus.SHPT_DRAFT &&
				ivs.getStatus() != ShipmentStatus.SHPT_AWAIT_PLAN &&
				ivs.getStatus() != ShipmentStatus.SHPT_PLANNING) {

			Timeout timeout = new Timeout(Duration.create(10, "seconds"));
			final Future<Object> future1 =
					ask(drsCmdBus, new CalculateInventoryShipmentCost(shipmentOfInventory), timeout);
			try {
				shippingCost = (String) Await.result(future1, timeout.duration());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		model.addAttribute("type", "Edit");
		model.addAttribute("status", "noDRAFT");
		model.addAttribute("isDrsUser",JsonHelper.toJson(Context.getCurrentUser().isDrsUser()));
		model.addAttribute("isSupplier",JsonHelper.toJson(Context.getCurrentUser().isSupplier()));
		model.addAttribute("AllCompanyKcodeToNameMap",allCompanyKcodeToNameMap);
		model.addAttribute("ActiveSkuCodeToNameMap",activeSkuCodeToNameMap);
		model.addAttribute("ActiveSkuCodeToNameMapJson",JsonHelper.toJson(activeSkuCodeToNameMap));
		model.addAttribute("destinationCountryList",destinationCountryList);
		model.addAttribute("defaultSalesTaxPercentage", JsonHelper.toJson(null));
		model.addAttribute("InventoryShipment",ivs);
		model.addAttribute("InventoryShipmentJson",JsonHelper.toJson(ivs));
		model.addAttribute("FCADeliveryLocationIdToLocationMap",this.getDeliveryLocationIdToLocationMap());
		model.addAttribute("shippingCost", shippingCost);
		*/


		return "InventoryShipmentV4";
	}

	//end V4
	//=====================================
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('INVENTORY_SHIPMENTS_PAYMENT_RECORD'))")	
	@RequestMapping(value = "/InventoryShipments/{shipmentId}/InventoryPayment")		
	public String showPaymentRecordForInventoryShipment(@PathVariable String shipmentId,Model model){
		model.addAttribute("statementRootUrl", Ss2spStatementController.rootUrl);
		model.addAttribute("report",this.getInventoryPaymentReportUco().getInventoryPaymentReport(shipmentId));
		return "th/inventoryShipment/InventoryPaymentReport";
	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('INVENTORY_SHIPMENTS_PAYMENT_RECORD'))")
	@RequestMapping(value = "/InventoryShipmentsV4/{shipmentId}/InventoryPayment")
	public String showPaymentRecordForInventoryShipmentV4(@PathVariable String shipmentId,Model model){
		model.addAttribute("statementRootUrl", Ss2spStatementController.rootUrl);
		model.addAttribute("report",this.getInventoryPaymentReportUco().getInventoryPaymentReport(shipmentId));
		return "InventoryPaymentReportV4";
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('INVENTORY_SHIPMENTS_PURCHASE_ORDER'))")
	@RequestMapping(value = "/InventoryShipments/{shipmentId}/{poNumber}/PurchaseOrder")
	public String showPurchaseOrder (@PathVariable String shipmentId, @PathVariable String poNumber,Model model){
		PurchaseOrderInfo orderInfo = getUco().getPurchaseOrderInfo(shipmentId);
		List<PurchaseOrderSkuInfo> orderSkuList = getUco().getPurchaseOrderInfoList(shipmentId);
		model.addAttribute("orderInfo",orderInfo);
		model.addAttribute("orderSkuList",orderSkuList);
		model.addAttribute("poNumber",poNumber);
		return "th/inventoryShipment/PurchaseOrder";
	}
		

	@RequestMapping(value = "/InventoryShipments/getFcaDeliveryDate", method = RequestMethod.GET)		
	public @ResponseBody String getFcaDeliveryDate(
			@RequestParam("destinationCountry") String destinationCountry,
			@RequestParam("shippingMethod") String shippingMethod,
			@RequestParam("FCADeliveryLocationId") int FCADeliveryLocationId,
			@RequestParam("expectedExportDate") String expectedExportDate) {			
		String FCADeliveryDate = this.getUco().getFcaDeliveryDate(destinationCountry, shippingMethod, FCADeliveryLocationId, expectedExportDate);				
		return JsonHelper.toJson(FCADeliveryDate);		
	}
	
	@RequestMapping(value = "/InventoryShipments/getShippingMethodList", method = RequestMethod.GET)			
	public @ResponseBody String getShippingMethodList(@RequestParam("destinationCountry") String destinationCountry){		
		List<ShippingMethod> shippingMethodList = this.getUco().getShippingMethods(destinationCountry);
		return JsonHelper.toJson(shippingMethodList);		
	}
		
	@RequestMapping(value = "/InventoryShipments/getDaysToPrepare", method = RequestMethod.GET)			
	public @ResponseBody String getDaysToPrepare(@RequestParam("shippingMethod") String shippingMethod){								
		return JsonHelper.toJson(this.getUco().getDaysToPrepare(shippingMethod));		
	}

	@RequestMapping(value = "/InventoryShipments/getSkuList", method = RequestMethod.GET)
	public @ResponseBody String getSkuList(@RequestParam("destinationCountry") String destinationCountry ,String kcode){
		List<String> skuList = this.getUco().getActiveAndOnboardingSkuCode(kcode,destinationCountry);
		return JsonHelper.toJson(skuList);
	}

	
	private Map<String, String> getDeliveryLocationIdToLocationMap(){		
		Map<String, String> FCADeliveryLocationIdToLocationMap = new HashMap<String, String>();
		FCADeliveryLocationIdToLocationMap.put("1","供應商地點");
		FCADeliveryLocationIdToLocationMap.put("2","DRS 辦公室");
		FCADeliveryLocationIdToLocationMap.put("3","集貨櫃場(請用卡車派送)");		
		FCADeliveryLocationIdToLocationMap.put("4","國外倉庫");
		return FCADeliveryLocationIdToLocationMap;		
	}
	
	private Map<String,Object> getIvsSearchConditions(ShipmentIvsSearchConditionImpl conditions){
		Map<String,Object> ivsSearchConditions = new TreeMap<String,Object>();
		ivsSearchConditions.put("sellerCompanyKcode",conditions.getSellerCompanyKcode());
		ivsSearchConditions.put("destinationCountry",conditions.getDestinationCountry());
		ivsSearchConditions.put("shippingMethod",conditions.getShippingMethod());
		ivsSearchConditions.put("status",conditions.getStatus());		
		return ivsSearchConditions;
	}




	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('IMPORT_IVS_DCP'))")
	@RequestMapping(value="/InventoryShipments/ImportIVS",method=RequestMethod.POST)
    public String uploadIVSFile(@RequestParam("file") MultipartFile file,
                                final RedirectAttributes redirectAttributes) {

		UserInfo user = Context.getCurrentUser();
		String companyCode = user.getCompanyKcode();
		String importMessage = "Import failed. ";

		try {
			byte[] fileBytes = file.getBytes();
			if (companyCode.equals("K101")) {
				importMessage = getUco().importRetailIVS(fileBytes);
			} else if (user.isSupplier()) {
				importMessage = getUco().importDCPIVS(fileBytes, companyCode);
			} else {
				importMessage = "User: " + user.getUsername() + " is not authorized";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

        redirectAttributes.addFlashAttribute("importMessage", importMessage);

        return "redirect:/InventoryShipments";
	}

	
}