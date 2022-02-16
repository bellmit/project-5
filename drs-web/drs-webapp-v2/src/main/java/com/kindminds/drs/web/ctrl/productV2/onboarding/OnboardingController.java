package com.kindminds.drs.web.ctrl.productV2.onboarding;

import akka.pattern.Patterns;

import com.kindminds.drs.api.data.transfer.productV2.ProductDetail;
import com.kindminds.drs.web.config.DrsActorSystem;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import akka.actor.ActorRef;

import org.springframework.security.access.prepost.PreAuthorize;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindminds.drs.Context;
import com.kindminds.drs.api.message.query.onboardingApplication.GetApplicationSerialNumbersBySupplier;
import com.kindminds.drs.api.message.query.onboardingApplication.GetOnboardingApplicationList;
import com.kindminds.drs.api.message.query.onboardingApplication.GetOnboardingApplicationLineitem;
import com.kindminds.drs.api.message.query.onboardingApplication.GetOnboardingApplicationListBySupplier;
import com.kindminds.drs.api.message.query.onboardingApplication.GetSupplierKcodeToShortEnUsNameMap;
import com.kindminds.drs.api.message.command.onboardingApplication.CreateApplicationLineitem;


import com.kindminds.drs.api.usecase.product.MaintainProductOnboardingUco;

import com.kindminds.drs.service.util.SpringAppCtx;
import com.kindminds.drs.util.JsonHelper;
import com.kindminds.drs.web.view.onboardingApplication.Product;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.kindminds.drs.api.message.command.onboardingApplication.ApplyApplication;
import com.kindminds.drs.util.Encryptor;
import com.kindminds.drs.api.message.command.onboardingApplication.AcceptApplication;
import com.kindminds.drs.api.v2.biz.domain.model.product.ProductEditingStatusType;
import com.kindminds.drs.data.transfer.productV2.onboarding.OnboardingApplicationLineitem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;



@Controller
@RequestMapping("/oa")
public class OnboardingController {
	
	//@Autowired @Qualifier("drsCmdBus")
	ActorRef drsCmdBus = DrsActorSystem.drsCmdBus();
	
	
	//@Autowired @Qualifier("drsQueryBus")
	ActorRef drsQueryBus  = DrsActorSystem.drsQueryBus();


	MaintainProductOnboardingUco uco  = null;

	private	 MaintainProductOnboardingUco getMaintainProductOnboardingUco(){
		MaintainProductOnboardingUco localUco;
		if(uco == null){
			 localUco = (MaintainProductOnboardingUco)SpringAppCtx.get().getBean("maintainProductOnboardingUco");
			this.uco = localUco;
		}
		
		return uco;
	}
	

	

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_LIST'))")
	@RequestMapping(value = "" , method = RequestMethod.GET)
	public String listOnboardingApplication(@RequestParam(value="kcode",defaultValue="All",required=false)
		 String companyKcode ,@RequestParam(value="page",defaultValue="1",required=false)  Integer pageIndex ,
											Model model )  {
		ObjectMapper  mapper = new ObjectMapper();

		Timeout timeout = new Timeout(Duration.create(30, "seconds"));
		String userKCode  = Context.getCurrentUser().getCompanyKcode();


		 Future<Object> futureResult = null;


		 if (Context.getCurrentUser().isDrsUser())
			 futureResult =  Patterns.ask(drsQueryBus, new GetOnboardingApplicationList(), timeout);
          else
			 futureResult = Patterns.ask(drsQueryBus, new GetOnboardingApplicationListBySupplier(
                  Context.getCurrentUser().getCompanyKcode()), timeout);


			 String oaListJson = null ;

			 try {
				 oaListJson = (String)Await.result(futureResult, timeout.duration());
			 } catch (InterruptedException e) {
				 e.printStackTrace();
			 } catch (TimeoutException e) {
				 e.printStackTrace();
			 }

			/*
			 try {
				 List<OnboardingApplication> oaList   =
				  mapper.readValue(oaListJson,   new TypeReference<List<OnboardingApplication>>() {});
			 } catch (IOException e) {
				 e.printStackTrace();
			 }
			 */


          /*
                   Pager page = baseProducts.getPager();	
                   
                   model.addAttribute("totalPages",page.getTotalPages());
                   model.addAttribute("currentPageIndex",page.getCurrentPageIndex());
                   model.addAttribute("startPage",page.getStartPage());
                   model.addAttribute("endPage",page.getEndPage());
                   */
          model.addAttribute("companyKcode", companyKcode);

          //model.addAttribute("baseProducts",this.generateBaseProductList(baseProducts.getItems()));
          model.addAttribute("baseProductsJson", oaListJson);

          model.addAttribute("supplierKcodeToNameMap", this.getSupplierKcodeToShortEnUsNameMap());

		
				
	
		
		return "OnboardingApplicationList";	
		
	
	}
	
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_CREATE'))")
	@RequestMapping("/c")
	public String createOnboardingApplicationLineitem(Model model)  {

		ObjectMapper mapper =  new ObjectMapper();
		mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

		Map supplierKcodeToShortEnUsNameMap  = this.getSupplierKcodeToShortEnUsNameMap();
		
		Boolean isDrsUser = this.getMaintainProductOnboardingUco().isDrsUser();
		String userCompanyKcode = this.getMaintainProductOnboardingUco().getUserCompanyKcode();

		Timeout timeout = new  Timeout(Duration.create(30, "seconds"));

		final Future<Object> snResult =
	                Patterns.ask(drsQueryBus,  new GetApplicationSerialNumbersBySupplier(userCompanyKcode),timeout);
		
		 try {
			String snListJson  = (String)Await.result(snResult, timeout.duration()) ;

			 List<String> snList = mapper.readValue(snListJson,new TypeReference<List<String>>() { });
			
			model.addAttribute("applicationSnList",snList);
			 
			
			model.addAttribute("marketSideRegionList",OnboardingUtil.getMarketSideRegionListJson());
			model.addAttribute("regionToMarketplaceMap",OnboardingUtil.getRegionToMarketplaceMapJson());
			model.addAttribute("baseProduct",JsonHelper.toJson(null));	
			model.addAttribute("developingProduct",JsonHelper.toJson(null));		
			model.addAttribute("supplierKcodeToShortEnUsNameMap", supplierKcodeToShortEnUsNameMap);		
			model.addAttribute("isDrsUser", isDrsUser);
			model.addAttribute("isDrsUserJson", JsonHelper.toJson(isDrsUser));
			model.addAttribute("userCompanyKcode", userCompanyKcode);
			model.addAttribute("userCompanyKcodeJson", JsonHelper.toJson(userCompanyKcode));	
			model.addAttribute("type", "Create");
			model.addAttribute("typeJson", JsonHelper.toJson("Create"));
			 
			 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		
		
		return "OnboardingApplication";		
	}
	
	/*
 
 @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_VIEW'))")
	@RequestMapping(value = "/CoreProductInformation/{baseProductCode}")
	public String showCoreProductInformation(@PathVariable String baseProductCode,@RequestParam(value="supplierKcode",defaultValue="",required=false) String supplierKcode,Model model) throws JsonParseException, JsonMappingException, IOException{		
		
		
		
		Timeout timeout = new Timeout(Duration.create(180, "seconds"));
		
		 final Future<Object> futureResult =
	                ask(drsQueryBus, 
	                		new GetProduct(Context.getCurrentUser().isDrsUser() ,
	                				Context.getCurrentUser().getCompanyKcode(),baseProductCode),timeout);
		 
		 
		    try {
		    	
		    	String str  = (String)  Await.result(futureResult, timeout.duration());
		    	
		    
		    	com.kindminds.drs.web.view.product.BaseProductOnboarding baseProduct = mapper.readValue(str,
		    			com.kindminds.drs.web.view.product.BaseProductOnboarding.class);
		    	
		    
		    	com.kindminds.drs.web.view.product.BaseProductOnboardingDetail baseProductSource = baseProduct.getProductInfoSource();
				
				String productInfoSourceData = baseProductSource.getData();
				JsonNode productInfoSourceJsonNode = mapper.readValue(productInfoSourceData, JsonNode.class);
				
				model.addAttribute("baseProduct",JsonHelper.toJson(baseProduct));				
				model.addAttribute("productName",productInfoSourceJsonNode.get("productNameEnglish").asText());
				
				model.addAttribute("status",ProductEditingStatusType.fromText(baseProductSource.getStatus()));
		    	
				
			} catch (Exception e) {
				// TODO Auto-generated catch blockc
				e.printStackTrace();
			}
		
		
		return "ProductInfoSourceVersionView";		
	}
 
 */
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_CREATE'))")
	@RequestMapping("/vi")
	public String viewLineItem(@RequestParam String i , Model model) {



		ObjectMapper  mapper  = new ObjectMapper();
		mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

		Map supplierKcodeToShortEnUsNameMap  = this.getSupplierKcodeToShortEnUsNameMap();
		
		Boolean isDrsUser = this.getMaintainProductOnboardingUco().isDrsUser();
		String userCompanyKcode = this.getMaintainProductOnboardingUco().getUserCompanyKcode();


		Timeout timeout =  new Timeout(Duration.create(30, "seconds"));
		final Future<Object>  snResult =
	                Patterns.ask(drsQueryBus, new GetApplicationSerialNumbersBySupplier(userCompanyKcode),timeout);

		final Future<Object>  itemResult =
	                Patterns.ask(drsQueryBus, new GetOnboardingApplicationLineitem(Encryptor.decrypt(i,true)),timeout);
		
		 try {
			String snListJson  = (String)  Await.result(snResult, timeout.duration()) ;
			String itemJson  =  (String) Await.result(itemResult, timeout.duration());

			List<String> snList = mapper.readValue(snListJson, new TypeReference<List<String>>() { });
			 
			 /*
			  	val baseProduct = mapper.readValue(itemJson,
		    			com.kindminds.drs.web.view.product.BaseProductOnboarding::class.java);
 */

			 OnboardingApplicationLineitem item = mapper.readValue(itemJson, OnboardingApplicationLineitem.class);

			 	ProductDetail baseProductSource = item.getProduct().getProductInfoSource();

			 	String productInfoSourceData = baseProductSource.getData();
				JsonNode productInfoSourceJsonNode = mapper.readValue(productInfoSourceData, JsonNode.class);
				
				model.addAttribute("baseProduct",JsonHelper.toJson(item.getProduct()));
				model.addAttribute("productName",productInfoSourceJsonNode.get("productNameEnglish").asText());
				
				model.addAttribute("status",ProductEditingStatusType.fromText(baseProductSource.getStatus()));
		    	
		 
			
				model.addAttribute("applicationSnList",snList);
			 
		
			 
			//model.addAttribute("marketSideRegionList",OnboardingUtil.getMarketSideRegionListJson())	
			//model.addAttribute("regionToMarketplaceMap",OnboardingUtil.getRegionToMarketplaceMapJson())
			 
			
			 /*
			model.addAttribute("developingProduct",JsonHelper.toJson(null));		
			model.addAttribute("supplierKcodeToShortEnUsNameMap", supplierKcodeToShortEnUsNameMap);		
			model.addAttribute("isDrsUser", isDrsUser);
			model.addAttribute("isDrsUserJson", JsonHelper.toJson(isDrsUser));
			model.addAttribute("userCompanyKcode", userCompanyKcode);
			model.addAttribute("userCompanyKcodeJson", JsonHelper.toJson(userCompanyKcode));
 			*/
			
			 //model.addAttribute("type", "Create");
			//model.addAttribute("typeJson", JsonHelper.toJson("Create"));
			 
			 /*
 				model.addAttribute("baseProduct",JsonHelper.toJson(baseProduct));				
				model.addAttribute("productName",productInfoSourceJsonNode.get("productNameEnglish").asText());
				
				model.addAttribute("status",ProductEditingStatusType.fromText(baseProductSource.getStatus()));
		    	
 */
			 
			 
		} catch ( Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		
		
		return "ViewOnboardingApplicationLineitem";		
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_CREATE'))")
	@RequestMapping("/ei")
	public String editLineItem(@RequestParam String i , Model model)  {

		ObjectMapper  mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

		Map  supplierKcodeToShortEnUsNameMap  = this.getSupplierKcodeToShortEnUsNameMap();
		
		Boolean isDrsUser = this.getMaintainProductOnboardingUco().isDrsUser();
		String userCompanyKcode = this.getMaintainProductOnboardingUco().getUserCompanyKcode();
		
		Timeout timeout = new Timeout(Duration.create(30, "seconds"));
		final Future<Object> snResult =
	                Patterns.ask(drsQueryBus,  new GetApplicationSerialNumbersBySupplier(userCompanyKcode),timeout);

		final Future<Object> itemResult =
	                Patterns.ask(drsQueryBus, new GetOnboardingApplicationLineitem(Encryptor.decrypt(i,true)),timeout);
		
		 try {
			String snListJson  =  (String) Await.result(snResult, timeout.duration()) ;
			String itemJson  =  (String) Await.result(itemResult, timeout.duration()) ;

			List<String> snList= mapper.readValue(snListJson,new TypeReference<List<String>>() { });
			
			model.addAttribute("applicationSnList",snList);
			 
			
			model.addAttribute("marketSideRegionList",OnboardingUtil.getMarketSideRegionListJson());
			model.addAttribute("regionToMarketplaceMap",OnboardingUtil.getRegionToMarketplaceMapJson());
			model.addAttribute("baseProduct",JsonHelper.toJson(null));	
			model.addAttribute("developingProduct",JsonHelper.toJson(null));		
			model.addAttribute("supplierKcodeToShortEnUsNameMap", supplierKcodeToShortEnUsNameMap);		
			model.addAttribute("isDrsUser", isDrsUser);
			model.addAttribute("isDrsUserJson", JsonHelper.toJson(isDrsUser));
			model.addAttribute("userCompanyKcode", userCompanyKcode);
			model.addAttribute("userCompanyKcodeJson", JsonHelper.toJson(userCompanyKcode));	
			model.addAttribute("type", "Create");
			model.addAttribute("typeJson", JsonHelper.toJson("Create"));
			 
			 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		
		
		return "EditOnboardingApplicationLineitem";		
	}
	

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_CREATE'))")	
	@RequestMapping(value ="/sd",method =RequestMethod.POST,headers = "Content-type=application/json")
	public @ResponseBody String saveLineitemDraft(@RequestBody Product product)  {


		String productInfoSource = "";
		try {
			productInfoSource = getMaintainProductOnboardingUco().updateDangerousGoodsCode(product.productInfoSource);
		} catch (IOException e) {
			e.printStackTrace();
		}

		product.productInfoSource = productInfoSource;
		
		//developingProduct.getSerialNumber()

		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
		
		Boolean isDrsUser = Context.getCurrentUser().isDrsUser();
		String userKCode = Context.getCurrentUser().getCompanyKcode();

		final Future<Object> futureResult =
	                Patterns.ask(drsCmdBus,
	                		 new CreateApplicationLineitem (
	                		isDrsUser,
	                		userKCode,
	                		product.productInfoSource,
	                		product.productInfoMarketSide, 
	                		product.productMarketingMaterialSource,
	                		product.productMarketingMaterialMarketSide,
	                		new scala.Some(product.serialNumber)), // Serial Number
	                		timeout);

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    //return  "/CoreProductInformation/" + developingProduct.getProductBaseCode();
		return "/oa";
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_CREATE'))")	
	@RequestMapping("/a")	
	public String applyOnboardingApplication(@RequestParam String i  ) {

		Timeout timeout = new Timeout(Duration.create(10, "seconds"));

		final Future<Object> futureResult =
	                Patterns.ask(drsCmdBus,
	                		 new ApplyApplication (Encryptor.decrypt(i , true)),timeout);
	    
		   try {
				Thread.sleep(2000);
		} catch (InterruptedException e ) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	
		return "/oa";
		
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_CREATE'))")	
	@RequestMapping("/ac")	
	public String acceptOnboardingApplication(@RequestParam String i )  {

		Timeout timeout = new Timeout(Duration.create(10, "seconds"));

		final Future<Object> futureResult =
	                Patterns.ask(drsCmdBus,
	                		new AcceptApplication (Encryptor.decrypt(i , true)),timeout);
	    
		   try {
				Thread.sleep(2000);
		} catch ( InterruptedException e ) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	
		return "/oa";
		
	}
	
	
	private Map getSupplierKcodeToShortEnUsNameMap() {

		Timeout timeout =  new  Timeout(Duration.create(180, "seconds"));
		ObjectMapper mapper = new ObjectMapper();

		final Future<Object>  futureResult = Patterns.ask(drsQueryBus, new  GetSupplierKcodeToShortEnUsNameMap(), timeout);

        try {
             String str = (String) Await.result(futureResult, timeout.duration());
            return mapper.readValue(str, Map.class);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
		}


        return null;


    }

	
	private List<String> getMarketSideRegionList(){

		List marketSideRegionList  =  new ArrayList<String>();
			
		marketSideRegionList.add("US");
		marketSideRegionList.add("UK");
		marketSideRegionList.add("CA");
		marketSideRegionList.add("DE");
		marketSideRegionList.add("IT");
		marketSideRegionList.add("FR");
		marketSideRegionList.add("ES");		
		return marketSideRegionList;
	}
	
	
	private Map<String,ArrayList<String>> getRegionToMarketplaceMap() {
		//TODO : refactoring
		 Map regionToMarketplaceMap =  new HashMap<String,ArrayList<String>>();
		List marketplaceUS = new ArrayList<String>();
		marketplaceUS.add("Amazon US");
		List marketplaceUK =  new ArrayList<String>();
		marketplaceUK.add("Amazon UK");
		List marketplaceCA =  new ArrayList<String>();
		marketplaceCA.add("Amazon CA");
		List marketplaceDE =  new ArrayList<String>();
		marketplaceDE.add("Amazon DE");
		List marketplaceIT = new ArrayList<String>();
		marketplaceIT.add("Amazon IT");
		List marketplaceFR =  new ArrayList<String>();
		marketplaceFR.add("Amazon FR");
		List marketplaceES = new ArrayList<String>();
		marketplaceES.add("Amazon ES");
		
		regionToMarketplaceMap.put("US", marketplaceUS);
		regionToMarketplaceMap.put("UK", marketplaceUK);
		regionToMarketplaceMap.put("CA", marketplaceCA);
		regionToMarketplaceMap.put("DE", marketplaceDE);
		regionToMarketplaceMap.put("IT", marketplaceIT);
		regionToMarketplaceMap.put("FR", marketplaceFR);
		regionToMarketplaceMap.put("ES", marketplaceES);					
		return regionToMarketplaceMap;
	}
	
	
}