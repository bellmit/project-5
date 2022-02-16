package com.kindminds.drs.web.ctrl.productV2;

import static akka.pattern.Patterns.ask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kindminds.drs.web.config.DrsActorSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.message.AddProductSkuAsinUco.*;
import com.kindminds.drs.api.v1.model.product.SkuFnskuAsin;
import com.kindminds.drs.api.v1.model.product.SkuWithoutAsin;

import akka.actor.ActorRef;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

@Controller
@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_SKU_ASIN'))")
@RequestMapping(value = "/asin")
public class ProductSkuAsinController {
	private static final int US = 1;
	private static final int UK = 4;
	private static final int CA = 5;
	private static final int DE = 6;
	private static final int FR = 7;
	private static final int IT = 8;
	private static final int ES = 9;

	ActorRef drsCmdBus =  DrsActorSystem.drsCmdBus();
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "")		
	public String showProductSkuAsinPage(Model model,
			@RequestParam(value="supplierKcode",defaultValue="") String supplierKcode,
			@RequestParam(value="marketplaceId",defaultValue="") String marketplaceId){
		String actualMarketplaceId = StringUtils.hasText(marketplaceId)?
				marketplaceId : String.valueOf(Marketplace.AMAZON_COM.getKey());

		Timeout timeout = new Timeout(Duration.create(20, "seconds"));
	    final Future<Object> futureSkuToAsin =
                ask(drsCmdBus, new GetSkuToAsin(actualMarketplaceId, supplierKcode), timeout);
	    
	    final Future<Object> futureMPtoSku =
                ask(drsCmdBus, new GetMarketplaceToSku(), timeout);
	    
	    final Future<Object> futureMPs =
                ask(drsCmdBus, new GetMarketplaces(), timeout);
	    
	    final Future<Object> futureKCode =
                ask(drsCmdBus, new GetKcodeToSupplierName(), timeout);
	    
	    List<SkuFnskuAsin> skuFnskuAsinList = new ArrayList<>();
	    List<SkuWithoutAsin> marketplaceToSkuList = new ArrayList<>();
	    List<Marketplace> marketplaces = new ArrayList<>();
	    Map<String, String> kCodeToSupplier = new HashMap<>();
	    try {
	    	skuFnskuAsinList = (List<SkuFnskuAsin>) Await.result(futureSkuToAsin, timeout.duration());
	    	marketplaceToSkuList = (List<SkuWithoutAsin>) Await.result(futureMPtoSku, timeout.duration());
	    	marketplaces = (List<Marketplace>) Await.result(futureMPs, timeout.duration());
	    	kCodeToSupplier = (Map<String, String>) Await.result(futureKCode, timeout.duration());
	    } catch (Exception e) {
			e.printStackTrace();
	    }
		model.addAttribute("skuFnskuAsinList", skuFnskuAsinList);
		model.addAttribute("marketplaceToSkuList", marketplaceToSkuList);
		model.addAttribute("supplierKcode", supplierKcode);	
		model.addAttribute("marketplaceId", actualMarketplaceId);		
		model.addAttribute("marketplaces", marketplaces);
		model.addAttribute("kcodeToSupplierNameMap", kCodeToSupplier);
		return "SkuAsin";
	}
	
	@RequestMapping(value = "/storageFeeFlag", method = RequestMethod.POST)
	public void toggleStorageFeeFlag(
			@RequestParam(value="marketplaceSku",defaultValue="") String marketplaceSku,
			@RequestParam(value="marketplaceId",defaultValue="") int marketplaceId) {
		drsCmdBus.tell(new ToggleStorageFeeFlag(marketplaceId, marketplaceSku), ActorRef.noSender());
	}
	
	@RequestMapping(value = "/importFbaFile")
	public String importFBAFile(
			@RequestParam("fileReport") MultipartFile file,
			@RequestParam("marketplaceReport") int marketplaceId,
			RedirectAttributes redirectAttributes) throws Exception {
		String fileName = file.getOriginalFilename();
		String result = "Imported file: " + fileName + "<br>";
	    
		if (nameMatchesMarketplace(fileName, marketplaceId)) {
			Timeout timeout = new Timeout(Duration.create(30, "seconds"));
		    final Future<Object> resultMsg =
	                ask(drsCmdBus, new AddFbaData(getFileData(file), marketplaceId), timeout);
		    
			result += (String) Await.result(resultMsg, timeout.duration());
		} else {
			result += "File name does not match the selected marketplace or type. ";
		}
		redirectAttributes.addFlashAttribute("message", result);
		return "redirect:/asin";
	}
	
	@RequestMapping(value = "/importInvFile")
	public String importInventoryFile(
			@RequestParam("fileReport") MultipartFile file, 
			@RequestParam("marketplaceReport") int marketplaceId,
			RedirectAttributes redirectAttributes) throws Exception {
		String fileName = file.getOriginalFilename();
		String result = "Imported file: " + fileName + "<br>";
		
		if (nameMatchesMarketplace(fileName, marketplaceId)) {
			Timeout timeout = new Timeout(Duration.create(10, "seconds"));
		    final Future<Object> resultMsg =
	                ask(drsCmdBus, new AddInventoryData(getFileData(file), marketplaceId), timeout);
		    
			result += (String) Await.result(resultMsg, timeout.duration());
		} else {
			result += "File name does not match the selected marketplace or type. ";
		}
		redirectAttributes.addFlashAttribute("message", result);
		return "redirect:/asin";
	}
	
	private byte[] getFileData(MultipartFile file) {
		try {
			return file.getBytes();
		} catch (IOException e) {
			e.printStackTrace();
			return new byte[0];
		}
	}
		
	private static boolean nameMatchesMarketplace(String fileName, int marketplaceId) {
		return (fileName.contains("US") && marketplaceId == US)
				|| (fileName.contains("UK") && marketplaceId == UK)
				|| (fileName.contains("CA") && marketplaceId == CA) 
				|| (fileName.contains("DE") && marketplaceId == DE)
				|| (fileName.contains("FR") && marketplaceId == FR)
				|| (fileName.contains("IT") && marketplaceId == IT) 
				|| (fileName.contains("ES") && marketplaceId == ES);
	}
}