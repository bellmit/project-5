package com.kindminds.drs.web.ctrl.productV2;

import static akka.pattern.Patterns.ask;

import java.io.IOException;
import java.util.List;


import com.kindminds.drs.api.data.transfer.productV2.ProductDetail;
import com.kindminds.drs.api.data.transfer.productV2.ProductDto;
import com.kindminds.drs.web.config.DrsActorSystem;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindminds.drs.Context;
import com.kindminds.drs.Country;
import com.kindminds.drs.api.message.command.onboardingApplication.ApproveMarketingMaterialEditingRequest;



import com.kindminds.drs.api.message.command.onboardingApplication.RejectMarketingMaterialEditingRequest;
import com.kindminds.drs.api.message.command.onboardingApplication.SaveMarketingMaterialEditingRequest;
import com.kindminds.drs.api.message.command.onboardingApplication.SubmitMarketingMaterialEditingRequest;
import com.kindminds.drs.api.message.query.onboardingApplication.GetProductVariationarketSides;
import com.kindminds.drs.api.message.query.onboardingApplication.IsExecutable;
import com.kindminds.drs.api.message.query.product.GetProduct;
import com.kindminds.drs.api.message.query.product.GetProductMarketingMaterial;
import com.kindminds.drs.api.v2.biz.domain.model.product.ProductEditingStatusType;

import com.kindminds.drs.util.JsonHelper;
import com.kindminds.drs.web.data.dto.product.ProductMarketingMaterialMarketSide;
import com.kindminds.drs.web.data.dto.product.ProductMarketingMaterialSource;

import akka.actor.ActorRef;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

@Controller
public class ProductMarketingMaterialController {

	ActorRef drsCmdBus =  DrsActorSystem.drsCmdBus();

	ActorRef drsQueryBus =  DrsActorSystem.drsQueryBus();

	private ObjectMapper mapper = new ObjectMapper();
	
	
	// pmmi
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_VIEW'))")			
	@RequestMapping(value = "/pmmi/{baseProductCode}")	
	public String showSourceProductMarketingMaterialIndex(
			@PathVariable String baseProductCode,
			@RequestParam(value="supplierKcode", defaultValue="",required=false) String supplierKcode,
			@RequestParam(value="prevSkuCode", defaultValue="",required=false) String prevSkuCode,
			@RequestParam(value="prevType", defaultValue="",required=false) String prevType,
			Model model
		) throws JsonParseException,JsonMappingException,IOException{
		
				
		Timeout timeout = new Timeout(Duration.create(180, "seconds"));
				
		final Future<Object> f =
                ask(drsQueryBus, 
                		new GetProduct(Context.getCurrentUser().isDrsUser(),
    	                				Context.getCurrentUser().getCompanyKcode(),baseProductCode),timeout);
		
	 
	    try {
	    
	    	String str  = (String)  Await.result(f, timeout.duration());

			ProductDto p = mapper.readValue(str,ProductDto.class);
	    	
	    	JsonNode productInfoSourceJsonNode = mapper.readValue(p.getProductInfoSource().getData(), JsonNode.class);										
			
			 	
			model.addAttribute("breadcrumbProduct",baseProductCode + " " + productInfoSourceJsonNode.get("productNameEnglish").asText());				
			model.addAttribute("baseProductCode", baseProductCode);
			model.addAttribute("supplierKcode", supplierKcode);
			
			model.addAttribute("isDrsUser", Context.getCurrentUser().isDrsUser());
			
			model.addAttribute("region", "soure");
			
			model.addAttribute("type","source");
			
			model.addAttribute("baseProduct",JsonHelper.toJson(p.getProductInfoSource()));				
			model.addAttribute("productName",productInfoSourceJsonNode.get("productNameEnglish").asText());
			
			model.addAttribute("prevSkuCode", prevSkuCode);
			model.addAttribute("prevType", prevType);
			
	    	
	    	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 
		
		return "ProductMarketingMaterialIndex";

	}
	
	
	
	// core information
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_VIEW'))")			
	@RequestMapping(value = "/pmm/{baseProductCode}/{variationCode}")	
	public String showSourceProductVariationsMarketingMaterialApplyRegion(@PathVariable String baseProductCode,@PathVariable String variationCode ,
			@RequestParam(value="supplierKcode",defaultValue="",required=false) String supplierKcode,Model model) throws JsonParseException,JsonMappingException,IOException{
		
		
			Timeout timeout = new Timeout(Duration.create(180, "seconds"));
		
			final Future<Object> f1 =
	                ask(drsQueryBus, 
	                		new GetProductMarketingMaterial(Country.CORE, supplierKcode , baseProductCode , variationCode)
	                	,timeout);
		 
		    try {
		    	
		    	String productMarketingMaterial  = (String)  Await.result(f1, timeout.duration());
		    	
		    	System.out.println(productMarketingMaterial);
		    	
		    	ProductDetail product = mapper.readValue(productMarketingMaterial,ProductDetail.class);
		    	
		    	model.addAttribute("product", JsonHelper.toJson(product));		
				model.addAttribute("status", ProductEditingStatusType.fromText(product.getStatus()));		
		    	
				model.addAttribute("breadcrumbProduct",baseProductCode+" " + this.getProductNameEng(baseProductCode));				
		
				model.addAttribute("baseProductCode", baseProductCode);
				model.addAttribute("variationCode", variationCode);
				model.addAttribute("supplierKcode", supplierKcode);
				model.addAttribute("region", "soure");
				model.addAttribute("type","source");
				
				model.addAttribute("marketSideList",this.getMarketSides(baseProductCode, variationCode));
		    	
		    	
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			return "ProductMarketingMaterialView";
		
	}
	

	
	// market information with marketplace
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_VIEW'))")			
	@RequestMapping(value = "/pmm/{baseProductCode}/{variationCode}/{marketSideRegion}")	
	public String showMarketSideProductVariationsMarketingMaterialApplyRegion(@PathVariable String baseProductCode,@PathVariable String variationCode ,
			@PathVariable String marketSideRegion ,
			@RequestParam(value="supplierKcode",defaultValue="",required=false) String supplierKcode,Model model) throws JsonParseException,JsonMappingException,IOException{
		
		
		
		Timeout timeout = new Timeout(Duration.create(180, "seconds"));
		
		 final Future<Object> f1 =
	                ask(drsQueryBus, 
	                		new GetProductMarketingMaterial(
	                			Country.valueOf(marketSideRegion),supplierKcode , baseProductCode , variationCode)
	                	,timeout);
		 
		
	
		    try {
		    	
		    	
		    	String str  = (String)  Await.result(f1, timeout.duration());
		    	
		    	ProductDetail pm = mapper.readValue(str,ProductDetail.class);
		    	
		    	model.addAttribute("product", JsonHelper.toJson(pm));		
				model.addAttribute("status",ProductEditingStatusType.fromText(pm.getStatus()));		
				
				model.addAttribute("breadcrumbProduct",baseProductCode+" "+ this.getProductNameEng(baseProductCode));				
				model.addAttribute("baseProductCode", baseProductCode);
				model.addAttribute("variationCode", variationCode);
				model.addAttribute("supplierKcode", supplierKcode);
				model.addAttribute("region", marketSideRegion);		
				model.addAttribute("type","marketSide");
				
			
				model.addAttribute("marketSideList",this.getMarketSides(baseProductCode, variationCode));
		    	
		    	
		    	
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
				
		return "ProductMarketingMaterialView";
		
	}

		

	
	
	// new get core-information
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_VIEW'))")			
	@RequestMapping(value = "/getSkuCoreInformation/{supplierKcode}/{baseProductCode}/{variationCode}", method = RequestMethod.GET)	
	public @ResponseBody byte[] showSourceProductVariationsMarketingMaterial(
				@PathVariable String supplierKcode, 
				@PathVariable String baseProductCode, 
				@PathVariable String variationCode,		// variation code = SKU code	
				Model model
			) throws JsonParseException,JsonMappingException,IOException{
		
		
		
			Timeout timeout = new Timeout(Duration.create(180, "seconds"));
		
			final Future<Object> f1 =
	                ask(drsQueryBus, 
	                		new GetProductMarketingMaterial(Country.CORE, supplierKcode , baseProductCode , variationCode)
	                	,timeout);
		 
			String productMarketingMaterial = "";
			
		    try {
		    	
		    	productMarketingMaterial  = (String)  Await.result(f1, timeout.duration());
		    	
		    		
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		    byte[] productMarketingMaterialEncoded = productMarketingMaterial.getBytes("UTF-8");
		
			return productMarketingMaterialEncoded;
		
	}
	
	
	
	
	// get market information with region
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_VIEW'))")			
	@RequestMapping(value = "/getSkuMarketplaceInformation/{marketSideRegion}/{supplierKcode}/{baseProductCode}/{skuCode}")	
	public @ResponseBody byte[] showMarketSideProductVariationsMarketingMaterial(
				@PathVariable String marketSideRegion,
				@PathVariable String supplierKcode,
				@PathVariable String baseProductCode,
				@PathVariable String skuCode,
				Model model
			) throws JsonParseException,JsonMappingException,IOException{
		
		
			Timeout timeout = new Timeout(Duration.create(180, "seconds"));
			
			final Future<Object> f1 =
			            ask(drsQueryBus, 
			            		new GetProductMarketingMaterial(
			            			Country.valueOf(marketSideRegion),supplierKcode , baseProductCode , skuCode)
			            	,timeout);
			 
			String productMarketingMaterial = "";
			
			
			try {
				
				
				productMarketingMaterial  = (String)  Await.result(f1, timeout.duration());
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
					e.printStackTrace();
			}



			byte[] productMarketingMaterialEncoded = productMarketingMaterial.getBytes("UTF-8");
			
			System.out.println("foooo");

			System.out.println(productMarketingMaterial);


		return productMarketingMaterialEncoded;
		
	
	}
	
	
	
	
		
	// edit core information
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_EDIT'))")	
	@RequestMapping(value = "/EditSourceProductMarketingMaterial/{baseProductCode}/{variationCode}")	
	public String editSourceProductMarketingMaterial(
			@PathVariable String baseProductCode,
			@PathVariable String variationCode ,
			@RequestParam(value="supplierKcode",defaultValue="",required=false) String supplierKcode,
			Model model) throws JsonParseException,JsonMappingException,IOException{		
		
	
		
		Timeout timeout = new Timeout(Duration.create(180, "seconds"));
		
		String userKcode = Context.getCurrentUser().getCompanyKcode();
		if(Context.getCurrentUser().isDrsUser()){
			supplierKcode = userKcode;
		}	
		
		 final Future<Object> f1 = ask(drsQueryBus, new  GetProductMarketingMaterial(Country.CORE , supplierKcode ,
	           baseProductCode,variationCode),timeout);
		
		 
		 ObjectMapper mapper = new ObjectMapper();
		 
		    try {
		    	
		    	String str  = (String)  Await.result(f1, timeout.duration());

		    	ProductDetail baseProduct = mapper.readValue(str,ProductDetail.class);
		    	
				
				 final Future<Object> f3 =
			                ask(drsQueryBus, 
			                		new IsExecutable(
			                				supplierKcode, ProductEditingStatusType.fromText(
			                						baseProduct.getStatus())) ,timeout);
				 
				 Boolean isExecutable  = (Boolean)  Await.result(f3, timeout.duration());
				 
				 
				if(isExecutable){			
					
					model.addAttribute("breadcrumbProduct", baseProductCode+" "+ this.getProductNameEng(baseProductCode));
					
					JsonNode pData = mapper.readValue(baseProduct.getData(), JsonNode.class);
					
					model.addAttribute("desc", pData.get("description").asText());
					
					model.addAttribute("product", JsonHelper.toJson(baseProduct));		
					model.addAttribute("baseProductCode", baseProductCode);
					model.addAttribute("variationCode", variationCode);
					
					model.addAttribute("mmId", baseProduct.getId());
					
					model.addAttribute("supplierKcode", supplierKcode);		
					model.addAttribute("region", "soure");
					model.addAttribute("type","source");				
					
					
					model.addAttribute("marketSideList", this.getMarketSides(baseProductCode, variationCode));	
					
					return "ProductMarketingMaterial";

				}		
		    	
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
		
		return "accessDeny";	
		
	}
	
	
	
	

	// edit region marketing material
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_EDIT_MARKETING_MARKET_SIDE'))")	
	@RequestMapping(value = "/EditMarketSideProductMarketingMaterial/{baseProductCode}/{variationCode}/{marketSideRegion}")	
	public String editMarketSideProductMarketingMaterial(
			@PathVariable String baseProductCode,
			@PathVariable String variationCode,
			@PathVariable String marketSideRegion,
			@RequestParam(value="supplierKcode",defaultValue="",required=false) String supplierKcode,
			Model model) throws JsonParseException,JsonMappingException,IOException{		
		

		Timeout timeout = new Timeout(Duration.create(180, "seconds"));
		
		String userKcode = Context.getCurrentUser().getCompanyKcode();
		if(Context.getCurrentUser().isDrsUser()){
			supplierKcode = userKcode;
		}	
		
		 final Future<Object> f1 =
	                ask(drsQueryBus, 
	                		new  GetProductMarketingMaterial(Country.valueOf(marketSideRegion) , supplierKcode ,
	                				baseProductCode,variationCode),timeout);
		
		    try {
		    	
		    	String str  = (String)  Await.result(f1, timeout.duration());
		    	
		    	
		    	ProductDetail baseProduct = mapper.readValue(str,ProductDetail.class);
					
					model.addAttribute("breadcrumbProduct",baseProductCode+" "+ this.getProductNameEng(baseProductCode));
					
					model.addAttribute("product", JsonHelper.toJson(baseProduct));		
					model.addAttribute("baseProductCode", baseProductCode);
					model.addAttribute("variationCode", variationCode);
					model.addAttribute("mmId", baseProduct.getId());
					
					model.addAttribute("supplierKcode", supplierKcode);	
					
					model.addAttribute("marketSideRegion",marketSideRegion);	
					model.addAttribute("region", marketSideRegion);		
					model.addAttribute("type","marketSide");		
					
					model.addAttribute("marketSideList",this.getMarketSides(baseProductCode, variationCode));	
					
					return "ProductMarketingMaterial";
						
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return "accessDeny";		
		
	}
	
		
		
		

	// save draft 
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_SAVE_DRAFT'))")	
	@RequestMapping(value = "/saveDraftForSourceProductMarketingMaterial",method = RequestMethod.POST,headers = {"Content-type=application/json"})		
	public @ResponseBody String saveDraftForSourceProductMarketingMaterial(
			@RequestBody ProductMarketingMaterialSource productMarketingMaterialSource){				
		
		
			
		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
		
		
	    final Future<Object> f1 =
                ask(drsCmdBus, new SaveMarketingMaterialEditingRequest(
                		productMarketingMaterialSource.getSupplierKcode(),
                		productMarketingMaterialSource.getProductBaseCode(),
                		Country.CORE,
                		productMarketingMaterialSource.getMmId(),
                		productMarketingMaterialSource.getJsonData()),timeout);
	
		
	    return "/pmmi/" + productMarketingMaterialSource.getProductBaseCode() + 
	    		"?supplierKcode=" + productMarketingMaterialSource.getSupplierKcode() +
	    		"&prevSkuCode=" + productMarketingMaterialSource.getVariationCode() + "&prevType=core";
		
	
	
	}
	

	
	// save core information 
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_UPDATE'))")
	@RequestMapping(value = "/updateSourceProductMarketingMaterial",method = RequestMethod.POST,headers = {"Content-type=application/json"})
	public @ResponseBody String updateSourceProductMarketingMaterial(
			@RequestBody ProductMarketingMaterialSource productMarketingMaterialSource){		
		
		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
		
	    final Future<Object> f1 =
                ask(drsCmdBus, new SaveMarketingMaterialEditingRequest(
                		productMarketingMaterialSource.getSupplierKcode(),
                		productMarketingMaterialSource.getProductBaseCode(),
                		Country.CORE,
                		productMarketingMaterialSource.getMmId(),
                		productMarketingMaterialSource.getJsonData()),timeout);
	    
	    
	    
	    return "/pmmi/" + productMarketingMaterialSource.getProductBaseCode() + 
	    		"?supplierKcode=" + productMarketingMaterialSource.getSupplierKcode() +
	    		"&prevSkuCode=" + productMarketingMaterialSource.getVariationCode() + "&prevType=core";
		
	}	
		
	
		

	
	
	
	// submit source marketing material
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_SUBMIT'))")
	@RequestMapping(value = "/submitSourceProductMarketingMaterial",method = RequestMethod.POST,headers = {"Content-type=application/json"})			
	public @ResponseBody String submitSourceProductMarketingMaterial(@RequestBody ProductMarketingMaterialSource productMarketingMaterialSource){		
		
			
		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
		
		
	    final Future<Object> f1 =
                ask(drsCmdBus, new SubmitMarketingMaterialEditingRequest(
                		productMarketingMaterialSource.getSupplierKcode(),
                		productMarketingMaterialSource.getProductBaseCode(),
                		productMarketingMaterialSource.getMmId(),
                		productMarketingMaterialSource.getJsonData()),timeout);
		
		
	    return "/pmmi/" + productMarketingMaterialSource.getProductBaseCode() + 
	    		"?supplierKcode=" + productMarketingMaterialSource.getSupplierKcode() +
	    		"&prevSkuCode=" + productMarketingMaterialSource.getVariationCode() + "&prevType=core";
	    
	    
	}
	
	
	
	// approve source marketing material
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_APPROVE'))")
	@RequestMapping(value = "/approveSourceProductMarketingMaterial",method = RequestMethod.POST,headers = {"Content-type=application/json"})
	public @ResponseBody String approveSourceProductMarketingMaterial(@RequestBody ProductMarketingMaterialSource productMarketingMaterialSource){		
		
		
		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
		
		
	    final Future<Object> f1 =
                ask(drsCmdBus, new ApproveMarketingMaterialEditingRequest(
                		productMarketingMaterialSource.getSupplierKcode(),
                		productMarketingMaterialSource.getProductBaseCode(),
                		productMarketingMaterialSource.getMmId(),
                		productMarketingMaterialSource.getJsonData()),timeout);
	    
	    
	    return "/pmmi/" + productMarketingMaterialSource.getProductBaseCode() + 
	    		"?supplierKcode=" + productMarketingMaterialSource.getSupplierKcode() +
	    		"&prevSkuCode=" + productMarketingMaterialSource.getVariationCode() + "&prevType=core";
	    
	} 
	

	
	// return source marketing material
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_RETURN'))")
	@RequestMapping(value = "/returnSourceProductMarketingMaterial",method = RequestMethod.POST,headers = {"Content-type=application/json"})	
	public @ResponseBody String returnSourceProductMarketingMaterial(@RequestBody ProductMarketingMaterialSource productMarketingMaterialSource){	
		
		
		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
		
		
	    final Future<Object> f1 =
                ask(drsCmdBus, new RejectMarketingMaterialEditingRequest(
                		productMarketingMaterialSource.getSupplierKcode(),
                		productMarketingMaterialSource.getProductBaseCode(),
                		productMarketingMaterialSource.getMmId(),
                		productMarketingMaterialSource.getJsonData()),timeout);
	    
	    
	    return "/pmmi/" + productMarketingMaterialSource.getProductBaseCode() + 
	    		"?supplierKcode=" + productMarketingMaterialSource.getSupplierKcode() +
	    		"&prevSkuCode=" + productMarketingMaterialSource.getVariationCode() + "&prevType=core";
	    
	}
	
	
	

	
	
	// update market side marketing material 
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_EDIT_MARKETING_MARKET_SIDE'))")	
	@RequestMapping(value = "/updateMarketSideProductMarketingMaterial",method = RequestMethod.POST,headers = {"Content-type=application/json"})
	public @ResponseBody String updateMarketSideProductMarketingMaterial(
			@RequestBody ProductMarketingMaterialMarketSide productMarketingMaterialMarketSide){		
		
		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
		
		
	    final Future<Object> f1 =
                ask(drsCmdBus, new SaveMarketingMaterialEditingRequest(
                		productMarketingMaterialMarketSide.getSupplierKcode(),
                		productMarketingMaterialMarketSide.getProductBaseCode(),
                		Country.valueOf(productMarketingMaterialMarketSide.getCountry()),
                		productMarketingMaterialMarketSide.getMmId(),
                		productMarketingMaterialMarketSide.getJsonData()),timeout);
	    
	    return "/pmmi/" + productMarketingMaterialMarketSide.getProductBaseCode() + 
	    		"?supplierKcode=" + productMarketingMaterialMarketSide.getSupplierKcode() +
	    		"&prevSkuCode=" + productMarketingMaterialMarketSide.getVariationCode() + 
	    		"&prevType=" + productMarketingMaterialMarketSide.getCountry();
	
	}	
	
		

	
	//===========================================================================================
	
	
	
	
	
	private String getProductNameEng(String baseProductCode ) {
		
		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
		
		
		final Future<Object> f =
                ask(drsQueryBus, 
                		new GetProduct(Context.getCurrentUser().isDrsUser() ,
    	                				Context.getCurrentUser().getCompanyKcode(),baseProductCode),timeout);


		ProductDto p;
		try {
			
			String str  = (String)  Await.result(f, timeout.duration());
			
			p = mapper.readValue(str,ProductDto.class);
			
			JsonNode productInfoSourceJsonNode = mapper.readValue(p.getProductInfoSource().getData(), JsonNode.class);
			
			return productInfoSourceJsonNode.get("productNameEnglish").asText();
			
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    											
		return "";
		 	
	}
	
	
	
	private List<String> getMarketSides(String baseProductCode , String variationCode) {
		
		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
		
		final Future<Object> f3 =
                ask(drsQueryBus, 
                		new GetProductVariationarketSides(baseProductCode,variationCode),timeout);
		
		
		try {
			List<String> marketSides  = (List<String>)  Await.result(f3, timeout.duration());
			return marketSides;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}

}

