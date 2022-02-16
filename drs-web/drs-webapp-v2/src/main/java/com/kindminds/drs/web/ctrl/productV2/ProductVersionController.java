package com.kindminds.drs.web.ctrl.productV2;

import static akka.pattern.Patterns.ask;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import com.kindminds.drs.api.data.transfer.productV2.ProductDetail;
import com.kindminds.drs.api.data.transfer.productV2.ProductDto;
import com.kindminds.drs.persist.v1.model.mapping.product.ProductDtoImpl;
import com.kindminds.drs.web.config.DrsActorSystem;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
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
import com.kindminds.drs.api.message.command.ProductSource;
import com.kindminds.drs.api.message.command.manageProduct.SaveProduct;
import com.kindminds.drs.api.message.command.manageProduct.SubmitProduct;
import com.kindminds.drs.api.message.query.product.GetProduct;
import com.kindminds.drs.api.message.query.product.GetProductInfoMarketSide;
import com.kindminds.drs.api.message.query.product.GetProductList;
import com.kindminds.drs.api.message.query.product.GetProductList4DRS;
import com.kindminds.drs.api.message.query.onboardingApplication.GetSupplierKcodeToShortEnUsNameMap;
import com.kindminds.drs.api.message.query.onboardingApplication.IsExecutable;
import com.kindminds.drs.api.usecase.product.MaintainProductOnboardingUco;
import com.kindminds.drs.api.v2.biz.domain.model.product.ProductEditingStatusType;


import com.kindminds.drs.api.v1.model.product.BaseProductOnboardingWithSKU;
import com.kindminds.drs.api.v1.model.common.DtoList;
import com.kindminds.drs.api.v1.model.common.Pager;

import com.kindminds.drs.service.util.SpringAppCtx;
import com.kindminds.drs.util.JsonHelper;
import com.kindminds.drs.web.data.dto.BaseProductOnboardingWithSKUImpl;
import com.kindminds.drs.web.data.dto.DevelopingProductListWeb;
import com.kindminds.drs.web.data.dto.DevelopingProductWeb;
import com.kindminds.drs.web.data.dto.product.ProductInfoMarketSide;
import com.kindminds.drs.api.message.command.manageProduct.ApproveProduct;
import com.kindminds.drs.api.message.command.manageProduct.CreateProduct;
import com.kindminds.drs.api.message.command.manageProduct.RejectProduct;

import akka.actor.ActorRef;
import akka.util.Timeout;
import scala.Option;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

@Controller
public class ProductVersionController {

	ActorRef drsCmdBus =  DrsActorSystem.drsCmdBus();

	ActorRef drsQueryBus =  DrsActorSystem.drsQueryBus();
	
	private  ObjectMapper mapper = new ObjectMapper();
	
	private MaintainProductOnboardingUco uco  = null ;
	private MaintainProductOnboardingUco getMaintainProductOnboardingUco(){
		if(uco == null)
		 uco = (MaintainProductOnboardingUco)SpringAppCtx.get().getBean("maintainProductOnboardingUco");
		return uco;
	}
	
	/*
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_EDIT'))")			
	@RequestMapping(value = "/EditCoreProductInformation/{baseProductCode}")
	public String editCoreProductInformation(@PathVariable String baseProductCode,@RequestParam(value="supplierKcode",defaultValue="",required=false) String supplierKcode,Model model) throws JsonParseException,JsonMappingException,IOException{		
		List<String> marketSideRegionList = this.getMarketSideRegionList();	
		Map<String,List<String>> regionToMarketplaceMap = this.getRegionToMarketplaceMap();				
		Map<String,String> supplierKcodeToShortEnUsNameMap = this.getMaintainProductOnboardingUco().getSupplierKcodeToShortEnUsNameMap();
		boolean isDrsUser = this.getMaintainProductOnboardingUco().isDrsUser();
		String userCompanyKcode = this.getMaintainProductOnboardingUco().getUserCompanyKcode();		
		BaseProductOnboarding baseProduct = this.getMaintainProductOnboardingUco().getDetailOfBaseProductOnboarding(baseProductCode);
		BaseProductOnboardingDetail baseProductSource = baseProduct.getProductInfoSource();			
		// for marketSideList, add throws exceptions at method
		ObjectMapper objectMapper = new ObjectMapper();
		BaseProductOnboardingDetail productInfoSource = baseProduct.getProductInfoSource();
		String productInfoSourceData = productInfoSource.getData();		
		JsonNode productInfoSourceJsonNode = objectMapper.readValue(productInfoSourceData, JsonNode.class);								
		JsonNode applicableRegionBPJsonNode = objectMapper.readValue(productInfoSourceJsonNode.get("applicableRegionBP"), JsonNode.class);
		List<String> marketSideList  = new ArrayList<String>();
		for(JsonNode applicableRegionBP:applicableRegionBPJsonNode){			
			marketSideList.add(applicableRegionBP.asText());				
		}								
		//end of marketSideList				
		boolean isExecutable = this.getMaintainProductOnboardingUco().isExecutable(StatusType.fromValue(baseProductSource.getStatus()));		
		if(isExecutable){
			model.addAttribute("marketSideRegionList",JsonHelper.toJson(marketSideRegionList));				
			model.addAttribute("regionToMarketplaceMap",JsonHelper.toJson(regionToMarketplaceMap));
			model.addAttribute("baseProduct",JsonHelper.toJson(baseProduct));		
			model.addAttribute("productName",productInfoSourceJsonNode.get("productNameEnglish").asText());			
			model.addAttribute("supplierKcodeToShortEnUsNameMap", supplierKcodeToShortEnUsNameMap);							
			model.addAttribute("isDrsUser", isDrsUser);
			model.addAttribute("isDrsUserJson", JsonHelper.toJson(isDrsUser));
			model.addAttribute("userCompanyKcode", userCompanyKcode);
			model.addAttribute("userCompanyKcodeJson", JsonHelper.toJson(userCompanyKcode));				
			model.addAttribute("type", "Edit");
			model.addAttribute("typeJson", JsonHelper.toJson("Edit"));			
			model.addAttribute("marketSideList",marketSideList);			
			return "ProductInfoSourceVersion";
		}		
		return "accessDeny";		
	}*/
	
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_LIST'))")
	@RequestMapping(value = "/NewDevelopingProductList", method = RequestMethod.GET)
	public String listNewDevelopingProducts(@RequestParam(value="kcode",defaultValue="All",required=false) 
		String companyKcode,@RequestParam(value="page",defaultValue="1",required=false) Integer pageIndex,Model model) throws JsonParseException, JsonMappingException, IOException{		
		
		
		Timeout timeout = new Timeout(Duration.create(30, "seconds"));
		
		
		 final Future<Object> futureResult = (Context.getCurrentUser().isDrsUser()) ? 
	                ask(drsQueryBus, new GetProductList4DRS(companyKcode,pageIndex),timeout) :
	                		     ask(drsQueryBus, new GetProductList(Context.getCurrentUser().getCompanyKcode()
	         	                				,pageIndex),timeout);
		
		    try {
		    	
		    	String str  = (String)  Await.result(futureResult, timeout.duration());
		    	
		    	
		    	DtoList baseProducts = mapper.readValue(str,DtoList.class);
		    	
		    	Pager page = baseProducts.getPager();	
				
				model.addAttribute("totalPages",page.getTotalPages());
				model.addAttribute("currentPageIndex",page.getCurrentPageIndex());
				model.addAttribute("startPage",page.getStartPage());
				model.addAttribute("endPage",page.getEndPage());
				model.addAttribute("companyKcode",companyKcode);	
				
				model.addAttribute("baseProducts",this.generateBaseProductList(baseProducts.getItems()));

				String json = JsonHelper.toJson(this.generateBaseProductList(baseProducts.getItems()));
				model.addAttribute("baseProductsJson",json);
			
				model.addAttribute("supplierKcodeToNameMap",this.getSupplierKcodeToShortEnUsNameMap());
		    	
		    	
				
			} catch (Exception e) {
				// TODO Auto-generated catch blockc
				e.printStackTrace();
			}
		
		return "ListOfProductInfoSourceVersions";	
		
	}


	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_LIST'))")
	@RequestMapping(value = "/NewDevelopingProductListV4", method = RequestMethod.GET)
	public String listNewDevelopingProductsV4(@RequestParam(value="kcode",defaultValue="All",required=false)
													String companyKcode,@RequestParam(value="page",defaultValue="1",required=false) Integer pageIndex,Model model) throws JsonParseException, JsonMappingException, IOException{


		Timeout timeout = new Timeout(Duration.create(30, "seconds"));


		final Future<Object> futureResult = (Context.getCurrentUser().isDrsUser()) ?
				ask(drsQueryBus, new GetProductList4DRS(companyKcode,pageIndex),timeout) :
				ask(drsQueryBus, new GetProductList(Context.getCurrentUser().getCompanyKcode()
						,pageIndex),timeout);

		try {

			String str  = (String)  Await.result(futureResult, timeout.duration());


			DtoList baseProducts = mapper.readValue(str,DtoList.class);

			Pager page = baseProducts.getPager();

			model.addAttribute("totalPages",page.getTotalPages());
			model.addAttribute("currentPageIndex",page.getCurrentPageIndex());
			model.addAttribute("startPage",page.getStartPage());
			model.addAttribute("endPage",page.getEndPage());
			model.addAttribute("companyKcode",companyKcode);

			model.addAttribute("baseProducts",this.generateBaseProductList(baseProducts.getItems()));

			String json = JsonHelper.toJson(this.generateBaseProductList(baseProducts.getItems()));
			model.addAttribute("baseProductsJson",json);

			model.addAttribute("supplierKcodeToNameMap",this.getSupplierKcodeToShortEnUsNameMap());



		} catch (Exception e) {
			// TODO Auto-generated catch blockc
			e.printStackTrace();
		}

		return "ListOfProductInfoSourceVersionsV4";

	}
	
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


				ProductDto baseProduct = mapper.readValue(str, ProductDto.class);
		    	
		    	ProductDetail baseProductSource = baseProduct.getProductInfoSource();
				
				String productInfoSourceData = baseProductSource.getData();
				JsonNode productInfoSourceJsonNode = mapper.readValue(productInfoSourceData, JsonNode.class);
				
				model.addAttribute("baseProduct",JsonHelper.toJson(baseProduct));				
				model.addAttribute("productName",
						productInfoSourceJsonNode.get("productNameEnglish") != null ?
				productInfoSourceJsonNode.get("productNameEnglish").asText() : "");
				
				model.addAttribute("status",ProductEditingStatusType.fromText(baseProductSource.getStatus()));
		    	
				
			} catch (Exception e) {
				// TODO Auto-generated catch blockc
				e.printStackTrace();
			}

		return "ProductInfoSourceVersionView";

	}
	
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_CREATE'))")
	@RequestMapping(value = "/CreateCoreProductInformation")
	public String CreateCoreProductInformation(Model model){	
				
		List<String> marketSideRegionList = this.getMarketSideRegionList();	
		Map<String,List<String>> regionToMarketplaceMap = this.getRegionToMarketplaceMap();		
		Map<String,String> supplierKcodeToShortEnUsNameMap = this.getMaintainProductOnboardingUco().getSupplierKcodeToShortEnUsNameMap();
		boolean isDrsUser = this.getMaintainProductOnboardingUco().isDrsUser();
		String userCompanyKcode = this.getMaintainProductOnboardingUco().getUserCompanyKcode();		
		
		model.addAttribute("marketSideRegionList",JsonHelper.toJson(marketSideRegionList));		
		model.addAttribute("regionToMarketplaceMap",JsonHelper.toJson(regionToMarketplaceMap));
		model.addAttribute("baseProduct",JsonHelper.toJson(null));	
		model.addAttribute("developingProduct",JsonHelper.toJson(null));		
		model.addAttribute("supplierKcodeToShortEnUsNameMap", supplierKcodeToShortEnUsNameMap);		
		model.addAttribute("isDrsUser", isDrsUser);
		model.addAttribute("isDrsUserJson", JsonHelper.toJson(isDrsUser));
		model.addAttribute("userCompanyKcode", userCompanyKcode);
		model.addAttribute("userCompanyKcodeJson", JsonHelper.toJson(userCompanyKcode));	
		model.addAttribute("type", "Create");
		model.addAttribute("typeJson", JsonHelper.toJson("Create"));		
		
		return "ProductInfoSourceVersion";		
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_CREATE'))")	
	@RequestMapping(value = "/saveCoreProductInformation",method = RequestMethod.POST,headers = {"Content-type=application/json"})		
	public @ResponseBody String saveCoreProductInformation(@RequestBody DevelopingProductWeb developingProduct) throws IOException{

		String productInfoSource = getMaintainProductOnboardingUco()
				.updateDangerousGoodsCode(developingProduct.getProductInfoSource());
		developingProduct.setProductInfoSource(productInfoSource);
		
		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
		
		Boolean isDrsUser = Context.getCurrentUser().isDrsUser();
		String userKCode = Context.getCurrentUser().getCompanyKcode();
				
	    final Future<Object> futureResult =
	                ask(drsCmdBus, 
	                		new CreateProduct(
	                		isDrsUser,
	                		userKCode,
	                		developingProduct.getProductInfoSource(),
	                		developingProduct.getProductInfoMarketSide(), 
	                		developingProduct.getProductMarketingMaterialSource(),
	                		developingProduct.getProductMarketingMaterialMarketSide()), // Serial Number
	                		timeout);
	    
		   try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
	    return  "/CoreProductInformation/" + developingProduct.getProductBaseCode();
		
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_SAVE_DRAFT'))")		
	@RequestMapping(value = "/saveDraftForCoreProductInformation",method = RequestMethod.POST,headers = {"Content-type=application/json"})	
	public @ResponseBody String saveDraftForCoreProductInformation(@RequestBody DevelopingProductWeb developingProduct) throws IOException{
		
		/*
		String productInfoSource = getMaintainProductOnboardingUco()
				.updateDangerousGoodsCode(developingProduct.getProductInfoSource());
		developingProduct.setProductInfoSource(productInfoSource);

		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
		
		Boolean isDrsUser = Context.getCurrentUser().isDrsUser();
		String userKCode = Context.getCurrentUser().getCompanyKcode();
		
	    final Future<Object> futureResult =
                ask(drsCmdBus, 
                		new SaveProduct(
                		isDrsUser,userKCode,
                		scala.Option.empty(),
                		Country.CORE, new ProductSource(
                		scala.Option.apply(developingProduct.getProductInfoSource()),
                		scala.Option.apply(developingProduct.getProductMarketingMaterialSource()), 
                		scala.Option.apply(developingProduct.getProductInfoMarketSide()), 
                		scala.Option.apply(developingProduct.getProductMarketingMaterialMarketSide())), null),
                		timeout);
	    
		
	  
		   try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   
		   */

		   String productInfoSource = getMaintainProductOnboardingUco()
					.updateDangerousGoodsCode(developingProduct.getProductInfoSource());

			developingProduct.setProductInfoSource(productInfoSource);
			
			Timeout timeout = new Timeout(Duration.create(10, "seconds"));
			
			Boolean isDrsUser = Context.getCurrentUser().isDrsUser();
			String userKCode = Context.getCurrentUser().getCompanyKcode();
					
		    final Future<Object> futureResult =
		                ask(drsCmdBus, 
		                		new CreateProduct(
		                		isDrsUser,
		                		userKCode,
		                		developingProduct.getProductInfoSource(),
		                		developingProduct.getProductInfoMarketSide(), 
		                		developingProduct.getProductMarketingMaterialSource(),
		                		developingProduct.getProductMarketingMaterialMarketSide()), // Serial Number
		                		timeout);
		    
			   try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
	    


	    return  "/CoreProductInformation/" + developingProduct.getProductBaseCode();
		
				
								
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_SUBMIT'))")	
	@RequestMapping(value = "/submitCoreProductInformation",method = RequestMethod.POST,headers = {"Content-type=application/json"})		
	public @ResponseBody String submitCoreProductInformation(@RequestBody DevelopingProductWeb developingProduct) throws IOException{		

		String productInfoSource = getMaintainProductOnboardingUco()
				.updateDangerousGoodsCode(developingProduct.getProductInfoSource());
		developingProduct.setProductInfoSource(productInfoSource);
			
		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
		
		Boolean isDrsUser = Context.getCurrentUser().isDrsUser();
		String userKCode = Context.getCurrentUser().getCompanyKcode();
		


	    final Future<Object> futureResult =
	                ask(drsCmdBus, new SubmitProduct(
	                		isDrsUser,userKCode,scala.Option.empty(),
	                		Country.CORE, new ProductSource(
	                				scala.Option.apply(developingProduct.getProductInfoSource()),
	                				scala.Option.apply(developingProduct.getProductMarketingMaterialSource()), 
	                				scala.Option.apply(developingProduct.getProductInfoMarketSide()), 
	                				scala.Option.apply(developingProduct.getProductMarketingMaterialMarketSide())), 
	                		scala.Option.empty()),
	                		timeout);
	    
		   try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


	    return  "/CoreProductInformation/" + developingProduct.getProductBaseCode();
		
					
	}


	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_APPROVE'))")	
	@RequestMapping(value = "/approveCoreProductInformation",method = RequestMethod.POST,headers = {"Content-type=application/json"})	
	public @ResponseBody String approveCoreProductInformation(@RequestBody DevelopingProductWeb developingProduct) throws IOException{		

		String productInfoSource = getMaintainProductOnboardingUco()
				.updateDangerousGoodsCode(developingProduct.getProductInfoSource());
		developingProduct.setProductInfoSource(productInfoSource);
	
		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
		
	
		
	    final Future<Object> futureResult =
	                ask(drsCmdBus, new ApproveProduct(
	                		scala.Option.empty(),
	                		Country.CORE, new ProductSource(
	                				Option.apply(developingProduct.getProductInfoSource()),
	                				Option.apply(developingProduct.getProductMarketingMaterialSource()), 
	                				Option.apply(developingProduct.getProductInfoMarketSide()), 
	                				Option.apply(developingProduct.getProductMarketingMaterialMarketSide())), null),
	                		timeout);
	    
		   try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    
		
	    return  "/CoreProductInformation/" + developingProduct.getProductBaseCode();
		
		
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_RETURN'))")		
	@RequestMapping(value = "/returnCoreProductInformation",method = RequestMethod.POST,headers = {"Content-type=application/json"})		
	public @ResponseBody String returnCoreProductInformation(@RequestBody DevelopingProductWeb developingProduct) throws IOException{		

		String productInfoSource = getMaintainProductOnboardingUco()
				.updateDangerousGoodsCode(developingProduct.getProductInfoSource());
		developingProduct.setProductInfoSource(productInfoSource);
		
	
		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
		
	
		
	    final Future<Object> futureResult =
	                ask(drsCmdBus, new 
	                		RejectProduct(
	                        	scala.Option.empty(),
	                        		Country.CORE, new ProductSource(
	                        				scala.Option.apply(developingProduct.getProductInfoSource()),
	                        				scala.Option.apply(developingProduct.getProductMarketingMaterialSource()), 
	                        				scala.Option.apply(developingProduct.getProductInfoMarketSide()), 
	                        				scala.Option.apply(developingProduct.getProductMarketingMaterialMarketSide())) , null),timeout);
		
	    
	   try {
		Thread.sleep(2000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
		
		return  "/CoreProductInformation/" + developingProduct.getProductBaseCode();
	}
		
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_UPDATE'))")	
	@RequestMapping(value = "/updateCoreProductInformation",method = RequestMethod.POST,headers = {"Content-type=application/json"})	
	public @ResponseBody String updateCoreProductInformation(@RequestBody DevelopingProductWeb developingProduct) throws IOException{		

		String productInfoSource = getMaintainProductOnboardingUco()
				.updateDangerousGoodsCode(developingProduct.getProductInfoSource());
		developingProduct.setProductInfoSource(productInfoSource);
				
		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
		
		Boolean isDrsUser = Context.getCurrentUser().isDrsUser();
		String userKCode = Context.getCurrentUser().getCompanyKcode();
		
		
	    final Future<Object> futureResult =
                ask(drsCmdBus, 
                		new SaveProduct(
                		isDrsUser,userKCode,
                		scala.Option.empty(),
                		Country.CORE, new ProductSource(
                		scala.Option.apply(developingProduct.getProductInfoSource()),
                		scala.Option.apply(developingProduct.getProductMarketingMaterialSource()), 
                		scala.Option.apply(developingProduct.getProductInfoMarketSide()), 
                		scala.Option.apply(developingProduct.getProductMarketingMaterialMarketSide())), null),timeout);
	   
		   try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	    


	    return  "/CoreProductInformation/" + developingProduct.getProductBaseCode();
		
		
		
	}
		
	


	

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_EDIT'))")			
	@RequestMapping(value = "/ep/{baseProductCode}")
	public String editCoreProduct(@PathVariable String baseProductCode,@RequestParam(value="supplierKcode",defaultValue="",required=false) String supplierKcode,Model model) throws JsonParseException,JsonMappingException,IOException{		
		
		
		List<String> marketSideRegionList =  this.getMarketSideRegionList();	
		Map<String,List<String>> regionToMarketplaceMap = this.getRegionToMarketplaceMap();			
		//Map sMap = this.getSupplierKcodeToShortEnUsNameMap();
		
		Timeout timeout = new Timeout(Duration.create(180, "seconds"));
		
		 boolean isDrsUser = Context.getCurrentUser().isDrsUser();
		 String userCompanyKcode = Context.getCurrentUser().getCompanyKcode();		
			
		 final Future<Object> futureResult =
	                ask(drsQueryBus, new GetProduct(isDrsUser,  userCompanyKcode,
	                				baseProductCode) ,timeout);
		 				 
		    try {
		    	
		    	
		    	String pStr  = (String)  Await.result(futureResult, timeout.duration());

				ProductDto p = mapper.readValue(pStr,ProductDto.class);
		    	
		    	ProductDetail productInfoSource =  p.getProductInfoSource();
				String productInfoSourceData = productInfoSource.getData();		
						
				JsonNode productInfoSourceJsonNode = 
						mapper.readValue(productInfoSourceData, JsonNode.class);								
					
						
				 final Future<Object> futureResult2 =
			                ask(drsQueryBus, 
			                		new IsExecutable(userCompanyKcode, 
			                				ProductEditingStatusType.fromText(productInfoSource.getStatus())) ,timeout);
				 
				 Boolean isExecutable  = (Boolean)  Await.result(futureResult2, timeout.duration());
				
							
				if(isExecutable){
					
					model.addAttribute("marketSideRegionList",JsonHelper.toJson(marketSideRegionList));				
					model.addAttribute("regionToMarketplaceMap",JsonHelper.toJson(regionToMarketplaceMap));
					
					model.addAttribute("baseProduct",JsonHelper.toJson(p));	
					
					JsonNode pData = mapper.readValue(p.getProductInfoSource().getData(), JsonNode.class);
					
					model.addAttribute("comment", pData.get("note").asText());
					String iComment = pData.get("innerNote") != null ? pData.get("innerNote").asText() : "";

					model.addAttribute("internalComment", iComment);

					String nameEn = productInfoSourceJsonNode.get("productNameEnglish") != null ? productInfoSourceJsonNode.get("productNameEnglish").asText():"";
					model.addAttribute("productName" , nameEn);
			
					//model.addAttribute("supplierKcodeToShortEnUsNameMap", sMap );		
				
					model.addAttribute("isDrsUser", isDrsUser);
					model.addAttribute("isDrsUserJson", JsonHelper.toJson(isDrsUser));
					model.addAttribute("userCompanyKcode", userCompanyKcode);
					model.addAttribute("supplierKcode", p.getSupplierKcode());
					
					model.addAttribute("userCompanyKcodeJson", JsonHelper.toJson(userCompanyKcode));				
					model.addAttribute("type", "Edit");
					model.addAttribute("typeJson", JsonHelper.toJson("Edit"));			
					model.addAttribute("marketSideList",this.createMarketSides(productInfoSourceJsonNode));	
					
					return "ProductInfoSourceVersion";
				}		
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch blockc
				e.printStackTrace();
			}
		    
		    
		
		return "accessDeny";		
	}
	
	
	
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_VIEW'))")			
	@RequestMapping(value = "/MarketSideProductInformation/{baseProductCode}/{marketSideRegion}")			
	public String showMarketSideProductInformation(@PathVariable String baseProductCode,@PathVariable String marketSideRegion,@RequestParam(value="supplierKcode",defaultValue="",required=false) String supplierKcode,Model model) throws JsonParseException,JsonMappingException,IOException{		
		
		
		
		Timeout timeout = new Timeout(Duration.create(180, "seconds"));
		
		String userKcode = Context.getCurrentUser().getCompanyKcode();
		if(!Context.getCurrentUser().isDrsUser()){
			supplierKcode = userKcode;
		}		
		
		
		 final Future<Object> futureResult =
	                ask(drsQueryBus, 
	                		new GetProductInfoMarketSide(marketSideRegion , supplierKcode, baseProductCode),timeout);
		 
		 final Future<Object> futureResult2 =
	                ask(drsQueryBus, 
	                		new GetProduct(
	                				Context.getCurrentUser().isDrsUser() ,
	    	                				Context.getCurrentUser().getCompanyKcode(),baseProductCode),timeout);
		 
		 
		    try {
		    	
		    	String str  = (String)  Await.result(futureResult, timeout.duration());
		    	
		    	
		    	ProductDetail  pDtl =
		    			mapper.readValue(str,ProductDetail.class);
		    	
		    	String pStr  = (String)  Await.result(futureResult2, timeout.duration());


				ProductDto p = mapper.readValue(pStr,ProductDto.class);
		    	
		    	JsonNode productInfoSourceJsonNode = 
		    			mapper.readValue(p.getProductInfoSource().getData(), JsonNode.class);										
				
		    	
		    	model.addAttribute("product", JsonHelper.toJson(pDtl));		

		    	String nameEn = productInfoSourceJsonNode.get("productNameEnglish") != null ? productInfoSourceJsonNode.get("productNameEnglish").asText() :"";
				model.addAttribute("breadcrumbProduct",baseProductCode+" "+
						nameEn);
				
				model.addAttribute("baseProductCode", baseProductCode);
				model.addAttribute("supplierKcode", supplierKcode);
				model.addAttribute("marketSideRegion",marketSideRegion);
				model.addAttribute("regionToCurrencyMap",this.getRegionToCurrencyMap());
				
				model.addAttribute("status",ProductEditingStatusType.fromText(pDtl.getStatus()));	
				
				
				model.addAttribute("marketSideList",this.createMarketSides(productInfoSourceJsonNode));
				
			
			} catch (Exception e) {
				// TODO Auto-generated catch blockc
				e.printStackTrace();
			}
		 
		 
		
		return "ProductInfoMarketSideVersionView";		
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_EDIT'))")			
	@RequestMapping(value = "/EditMarketSideProductInformation/{baseProductCode}/{marketSideRegion}")		
	public String editMarketSideProductInformation(@PathVariable String baseProductCode,@PathVariable String marketSideRegion,@RequestParam(value="supplierKcode",defaultValue="",required=false) String supplierKcode,Model model) throws JsonParseException,JsonMappingException,IOException{				
		
		
		Map<String,List<String>> regionToMarketplaceMap = this.getRegionToMarketplaceMap();
		
		Timeout timeout = new Timeout(Duration.create(180, "seconds"));
		
		String userKcode = Context.getCurrentUser().getCompanyKcode();
		if(!Context.getCurrentUser().isDrsUser()){
			supplierKcode = userKcode;
		}		
		
		
		
		 final Future<Object> futureResult =
	                ask(drsQueryBus, 
	                		new GetProductInfoMarketSide(marketSideRegion , supplierKcode, baseProductCode),timeout);
		 
		 
		 final Future<Object> futureResult2 =
	                ask(drsQueryBus, 
	                		new GetProduct(Context.getCurrentUser().isDrsUser() ,
	    	                				Context.getCurrentUser().getCompanyKcode(),baseProductCode),timeout);
	
		
		
		 ObjectMapper mapper = new ObjectMapper();
		    try {
		    	
		    	String str  = (String)  Await.result(futureResult, timeout.duration());
		    	
		    	
		    	ProductDetail baseProduct =
		    			mapper.readValue(str,ProductDetail.class);
		    	
		    
				String pStr  = (String)  Await.result(futureResult2, timeout.duration());

				ProductDto baseProductInfo = mapper.readValue(pStr,ProductDto.class);
		    	
		    	JsonNode productInfoSourceJsonNode = 
		    			mapper.readValue(baseProductInfo.getProductInfoSource().getData(), JsonNode.class);										
				
		    
			
				 final Future<Object> futureResult3 =
			                ask(drsQueryBus, 
			                		new IsExecutable(userKcode, 
			                				ProductEditingStatusType.fromText(baseProduct.getStatus())) ,timeout);
				 
				 Boolean isExecutable  = (Boolean)  Await.result(futureResult3, timeout.duration());
				 
				 
					if(isExecutable){			

						String nameEn = productInfoSourceJsonNode.get("productNameEnglish") != null ? productInfoSourceJsonNode.get("productNameEnglish").asText() : "";
						model.addAttribute("breadcrumbProduct",baseProductCode+" "+
								nameEn);
						
						
						model.addAttribute("product", JsonHelper.toJson(baseProduct));
						model.addAttribute("baseProductCode", baseProductCode);
						
						model.addAttribute("supplierKcode", supplierKcode);
						model.addAttribute("marketSideRegion",marketSideRegion);
						model.addAttribute("regionToCurrencyMap",this.getRegionToCurrencyMap());
						model.addAttribute("marketplaceList",regionToMarketplaceMap.get(marketSideRegion));			
						model.addAttribute("marketSideList",this.createMarketSides(productInfoSourceJsonNode));		
						
						JsonNode pData = mapper.readValue(baseProduct.getData(), JsonNode.class);
						model.addAttribute("comment", pData.get("note").asText());

						
						return "ProductInfoMarketSideVersion";						
					}						
		    	
				
			} catch (Exception e) {
				// TODO Auto-generated catch blockc
				e.printStackTrace();
			}
		 
			return "accessDeny";		
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_SAVE_DRAFT'))")	
	@RequestMapping(value = "/saveDraftForMarketSideProductInformation",method = RequestMethod.POST,headers = {"Content-type=application/json"})			
	public @ResponseBody String saveDraftForMarketSideProductInformation(@RequestBody 
			ProductInfoMarketSide productInfoMarketSide){	
		
	
		Boolean isDrsUser = Context.getCurrentUser().isDrsUser();
		
		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
		
	
		
	    final Future<Object> futureResult =
	                ask(drsCmdBus, 
	                		new SaveProduct(
	                				isDrsUser,Context.getCurrentUser().getCompanyKcode(),
	                				Option.apply(productInfoMarketSide.getSupplierKcode()),
	                        		Country.valueOf(productInfoMarketSide.getCountry()),
	                        		new ProductSource(
	                        		Option.empty(),Option.empty(),
	                        	    Option.apply(productInfoMarketSide.getJsonData()),
	                        	    Option.empty()), Option.apply(productInfoMarketSide.getProductBaseCode())),timeout);
		
	
	    try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		return "/MarketSideProductInformation/"+productInfoMarketSide.getProductBaseCode()+"/"+productInfoMarketSide.getCountry()+"?supplierKcode="+productInfoMarketSide.getSupplierKcode();
		
		
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_SUBMIT'))")	
	@RequestMapping(value = "/submitMarketSideProductInformation",method = RequestMethod.POST,headers = {"Content-type=application/json"})			
	public @ResponseBody String submitMarketSideProductInformation(@RequestBody ProductInfoMarketSide productInfoMarketSide){		

		Boolean isDrsUser = Context.getCurrentUser().isDrsUser();

		Timeout timeout = new Timeout(Duration.create(10, "seconds"));

	    final Future<Object> futureResult =
                ask(drsCmdBus,
                		new  SubmitProduct(
                				isDrsUser,Context.getCurrentUser().getCompanyKcode(),
                				scala.Option.apply(productInfoMarketSide.getSupplierKcode()),
                		Country.valueOf(productInfoMarketSide.getCountry()),
                		new ProductSource(
                				Option.empty(),
                				Option.empty(),
                				scala.Option.apply(productInfoMarketSide.getJsonData()),
                	    Option.empty()),
                		Option.apply(productInfoMarketSide.getProductBaseCode())),timeout);

	    try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		return "/MarketSideProductInformation/"+productInfoMarketSide.getProductBaseCode()+"/"+productInfoMarketSide.getCountry()+"?supplierKcode="+productInfoMarketSide.getSupplierKcode();
		
		
	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_APPROVE'))")	
	@RequestMapping(value = "/approveMarketSideProductInformation",method = RequestMethod.POST,headers = {"Content-type=application/json"})	
	public @ResponseBody String approveMarketSideProductInformation(@RequestBody ProductInfoMarketSide productInfoMarketSide){	
		
				Timeout timeout = new Timeout(Duration.create(10, "seconds"));
		
	    final Future<Object> futureResult =
                ask(drsCmdBus, new  ApproveProduct(
                				Option.apply(productInfoMarketSide.getSupplierKcode()),
                        		Country.valueOf(productInfoMarketSide.getCountry()),
                        		new ProductSource(
                        				Option.empty(),Option.empty(),
                        	    Option.apply(productInfoMarketSide.getJsonData()),
                        	    Option.empty()),  Option.apply(productInfoMarketSide.getProductBaseCode())),timeout);
		
		
	    try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    return "/MarketSideProductInformation/"+productInfoMarketSide.getProductBaseCode()+"/"+productInfoMarketSide.getCountry()+"?supplierKcode="+productInfoMarketSide.getSupplierKcode();
		
		
	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_RETURN'))")		
	@RequestMapping(value = "/returnMarketSideProductInformation",method = RequestMethod.POST,headers = {"Content-type=application/json"})		
	public @ResponseBody String returnMarketSideProductInformation(@RequestBody ProductInfoMarketSide productInfoMarketSide){		
		
				
		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
		
	    final Future<Object> futureResult =
                ask(drsCmdBus, new RejectProduct(
                				Option.apply(productInfoMarketSide.getSupplierKcode()),
                        		Country.valueOf(productInfoMarketSide.getCountry()),
                        		new ProductSource(Option.empty(),Option.empty(),
                        	    Option.apply(productInfoMarketSide.getJsonData()),
                        	    Option.empty()), Option.apply( productInfoMarketSide.getProductBaseCode())),
                		timeout);
		
	
	    try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    
	    return "/MarketSideProductInformation/"+productInfoMarketSide.getProductBaseCode()+"/"+productInfoMarketSide.getCountry()+"?supplierKcode="+productInfoMarketSide.getSupplierKcode();
		
		
	}
		
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_UPDATE'))")	
	@RequestMapping(value = "/updateMarketSideProductInformation",method = RequestMethod.POST,headers = {"Content-type=application/json"})	
	public @ResponseBody String updateMarketSideProductInformation(@RequestBody ProductInfoMarketSide productInfoMarketSide){	
		
			
		Timeout timeout = new Timeout(Duration.create(10, "seconds"));
		
		Boolean isDrsUser = Context.getCurrentUser().isDrsUser();
		String userKCode = Context.getCurrentUser().getCompanyKcode();
		
		

		  final Future<Object> futureResult =
	                ask(drsCmdBus, 
	                		new  SubmitProduct(
	                				isDrsUser,Context.getCurrentUser().getCompanyKcode(),
	                				scala.Option.apply(productInfoMarketSide.getSupplierKcode()),
	                		Country.valueOf(productInfoMarketSide.getCountry()),
	                		new ProductSource(
	                				Option.empty(),
	                				Option.empty(),
	                				scala.Option.apply(productInfoMarketSide.getJsonData()),
	                	    Option.empty()), 
	                		Option.apply(productInfoMarketSide.getProductBaseCode())),timeout);
	   
		   try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
		
		return "/MarketSideProductInformation/"+productInfoMarketSide.getProductBaseCode()+"/"+productInfoMarketSide.getCountry()+"?supplierKcode="+productInfoMarketSide.getSupplierKcode();		
	}
		


	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_VIEW_SKU_UPDATE'))")		
	@RequestMapping(value = "/CoreProductInformation/SKUs/{baseProductCode}")	
	public String editCoreProductInformationSKU(Model model, @PathVariable String baseProductCode) throws JsonParseException, JsonMappingException, IOException{		
		
		//todo here
		BaseProductOnboardingWithSKU baseProductOnboardingWithSKU = 
				this.getMaintainProductOnboardingUco().getBaseProductOnboardingWithSKU(baseProductCode);	
		
		
		boolean isExecutable = this.getMaintainProductOnboardingUco().isExecutableForUpdatingSKUs(ProductEditingStatusType.fromText(baseProductOnboardingWithSKU.getStatus()));		
		if(isExecutable){
			BaseProductOnboardingWithSKUImpl bpOnboardingWithSKU = new BaseProductOnboardingWithSKUImpl(baseProductOnboardingWithSKU);
			model.addAttribute("BaseProductOnboardingWithSKU",bpOnboardingWithSKU);
			model.addAttribute("BaseProductOnboardingWithSKUJson",JsonHelper.toJson(bpOnboardingWithSKU));
			return "ProductInfoSKU";
		}		
		return "accessDeny";		
	}

	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_VIEW_SKU_UPDATE'))")		
	@RequestMapping(value = "/CoreProductInformation/SKUs/update", method = RequestMethod.POST)			
	public String updateCoreProductInformationSKU(@ModelAttribute("BaseProductOnboardingWithSKU") BaseProductOnboardingWithSKUImpl baseProductOnboardingWithSKU,Model model) throws JsonParseException, JsonMappingException, IOException{
		this.getMaintainProductOnboardingUco().updateBaseProductOnboardingWithSKU(baseProductOnboardingWithSKU);		
		return "redirect:/CoreProductInformation/"+baseProductOnboardingWithSKU.getProductBaseCode();		
	}
				
	@RequestMapping(value = "/ProductInfoSourceVersion/isBaseProductCodeExist", method = RequestMethod.GET)	
	public @ResponseBody String isBaseProductCodeExist(@RequestParam("supplierKcode") String supplierKcode, @RequestParam("productBaseCode") String productBaseCode){			
		boolean baseProductCodeExist = this.getMaintainProductOnboardingUco().isBaseProductCodeExist(supplierKcode, productBaseCode);
		return JsonHelper.toJson(baseProductCodeExist);
	}
	
	@RequestMapping(value = "/ProductInfoMarketSide/getPriceWithTax", method = RequestMethod.GET)		
	public @ResponseBody String getPriceWithTax(@RequestParam("country") String country, @RequestParam("price") BigDecimal price){							
		return this.getMaintainProductOnboardingUco().getPriceWithTax(country, price);
	}
		
	@RequestMapping(value = "/ProductMarketingMaterial/avoidSessionTimeout", method = RequestMethod.GET)		
	public @ResponseBody String avoidSessionTimeout(){		
		return "session";
	}
	
	/*
	private String getProductBaseCodeAndName(String baseProductCode) throws JsonParseException, JsonMappingException, IOException{	
		
		ObjectMapper objectMapper = new ObjectMapper();
		BaseProductOnboarding baseProduct = this.getMaintainProductOnboardingUco().getDetailOfBaseProductOnboarding(baseProductCode);
		
		BaseProductOnboardingDetail baseProductSource = baseProduct.getProductInfoSource();
		String productInfoSourceData = baseProductSource.getData();		
		JsonNode productInfoSourceJsonNode = objectMapper.readValue(productInfoSourceData, JsonNode.class);										
		
		return baseProductCode+" "+productInfoSourceJsonNode.get("productNameEnglish").asText();		
	}*/
	
	
	private List<String> createMarketSides(JsonNode productInfoSourceJsonNode ) {
		
		List<String> marketSideList  = new ArrayList<String>();
		JsonNode applicableRegionBPJsonNode = productInfoSourceJsonNode.get("applicableRegionBP");
		//try {
			
		
			//applicableRegionBPJsonNode = mapper.readValue(
				//	productInfoSourceJsonNode.get("applicableRegionBP").asText(), JsonNode.class);
		
		if(applicableRegionBPJsonNode.isArray()) {
			if(applicableRegionBPJsonNode.size()>0) {
				for(JsonNode applicableRegionBP:applicableRegionBPJsonNode){			
					marketSideList.add(applicableRegionBP.asText());				
				}		
			
			}
		}
			
		
		return marketSideList;
		
	}
	
	private Map getSupplierKcodeToShortEnUsNameMap() {
		
		Timeout timeout = new Timeout(Duration.create(180, "seconds"));
		
		
		 final Future<Object> futureResult =
	                ask(drsQueryBus, new GetSupplierKcodeToShortEnUsNameMap() 
	                		,timeout);
		 
		  
		try {
			String	str = (String)  Await.result(futureResult, timeout.duration());
			Map supplierKcodeToShortEnUsNameMap =  mapper.readValue(str,Map.class);
			return supplierKcodeToShortEnUsNameMap;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    	
	    	
		 return null;
		
		
	}
	
	
	
	private List<String> getMarketSideRegionList(){
		//TODO : refactoring
		List<String> marketSideRegionList = new ArrayList<String>(); 		
		marketSideRegionList.add("US");
		marketSideRegionList.add("UK");
		marketSideRegionList.add("CA");
		marketSideRegionList.add("DE");
		marketSideRegionList.add("IT");
		marketSideRegionList.add("FR");
		marketSideRegionList.add("ES");		
		return marketSideRegionList;
	}
	
	private Map<String,String> getRegionToCurrencyMap(){
		//TODO : refactoring
		Map<String,String> regionToCurrencyMap = new HashMap<String,String>();
		regionToCurrencyMap.put("US", "USD");
		regionToCurrencyMap.put("UK", "GBP");
		regionToCurrencyMap.put("CA", "CAD");
		regionToCurrencyMap.put("DE", "EUR");
		regionToCurrencyMap.put("IT", "EUR");
		regionToCurrencyMap.put("FR", "EUR");
		regionToCurrencyMap.put("ES", "EUR");		
		return regionToCurrencyMap;
	}
	
	private Map<String,List<String>> getRegionToMarketplaceMap(){
		//TODO : refactoring
		Map<String,List<String>> regionToMarketplaceMap = new HashMap<String,List<String>>();
		List<String> marketplaceUS = new ArrayList<String>();
		marketplaceUS.add("Amazon US");
		List<String> marketplaceUK = new ArrayList<String>();
		marketplaceUK.add("Amazon UK");
		List<String> marketplaceCA = new ArrayList<String>();
		marketplaceCA.add("Amazon CA");
		List<String> marketplaceDE = new ArrayList<String>();
		marketplaceDE.add("Amazon DE");
		List<String> marketplaceIT = new ArrayList<String>();
		marketplaceIT.add("Amazon IT");
		List<String> marketplaceFR = new ArrayList<String>();
		marketplaceFR.add("Amazon FR");
		List<String> marketplaceES = new ArrayList<String>();
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
	
	private List<DevelopingProductListWeb> generateBaseProductList(List<LinkedHashMap> bpList) throws JsonParseException, JsonMappingException, IOException{		
		List<DevelopingProductListWeb> baseProductList = new ArrayList<DevelopingProductListWeb>();		
		
		
		
		for(LinkedHashMap dp:bpList){			
			DevelopingProductListWeb baseProduct = new DevelopingProductListWeb();
			List<Map<String,Object>> variationProductList = new ArrayList<Map<String,Object>>();	
			
			String productInfoSource = dp.get("jsonData").toString();		

			if(!StringUtils.isEmpty(productInfoSource)){
				JsonNode productInfoSourceJsonNode = mapper.readValue(productInfoSource, JsonNode.class);
				String products = productInfoSourceJsonNode.get("products").asText();

				JsonNode productsJsonNode = mapper.readValue(products, JsonNode.class);
				for(JsonNode productJsonNode:productsJsonNode){
					Collection<String> applicableRegionList = new TreeSet<String>();
					Iterator<JsonNode> applicableRegions = productJsonNode.get("applicableRegionList").elements();
					while (applicableRegions.hasNext()) {
						applicableRegionList.add(applicableRegions.next().toString());
					}

					Map<String,Object> variationProduct = new HashMap<String,Object>();
					variationProduct.put("SKU",productInfoSourceJsonNode.get("supplierKcode").asText()+"-"+productJsonNode.get("SKU").asText());
					variationProduct.put("type1", productJsonNode.get("type1").asText());
					variationProduct.put("type1Value", productJsonNode.get("type1Value").asText());
					variationProduct.put("type2", productJsonNode.get("type2").asText());
					variationProduct.put("type2Value", productJsonNode.get("type2Value").asText());
					variationProduct.put("productWithVariation",productInfoSourceJsonNode.get("productWithVariation").asText());
					variationProduct.put("applicableRegionList", applicableRegionList);
					variationProductList.add(variationProduct);
				}

				baseProduct.setBaseProduct(productInfoSourceJsonNode.get("baseProductCode").asText());
				baseProduct.setProductName(productInfoSourceJsonNode.get("productNameEnglish") == null ?"":productInfoSourceJsonNode.get("productNameEnglish").asText());
				baseProduct.setSupplierKcode(productInfoSourceJsonNode.get("supplierKcode").asText());
				baseProduct.setVariationProducts(variationProductList);
				baseProductList.add(baseProduct);
			}

			

		}				
		
		return baseProductList;		
	}
		

				
}