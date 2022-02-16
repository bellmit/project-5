package com.kindminds.drs.web.ctrl.productV2;

import static akka.pattern.Patterns.ask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.kindminds.drs.api.data.transfer.productV2.ProductDetail;
import com.kindminds.drs.api.data.transfer.productV2.ProductDto;
import com.kindminds.drs.web.config.DrsActorSystem;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kindminds.drs.Context;
import com.kindminds.drs.Country;
import com.kindminds.drs.Marketplace;

import com.kindminds.drs.api.message.query.onboardingApplication.GetProductVariationarketSides;
import com.kindminds.drs.api.message.query.product.GetProduct;
import com.kindminds.drs.api.message.query.product.GetProductMarketingMaterial;
import com.kindminds.drs.api.usecase.product.MaintainProductOnboardingUco;

import com.kindminds.drs.api.v1.model.product.ConfirmMarketingMaterialComment;


import com.kindminds.drs.service.util.SpringAppCtx;
import com.kindminds.drs.util.JsonHelper;
import com.kindminds.drs.web.data.dto.IssueCommentImpl;

import akka.actor.ActorRef;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

@Controller
public class ConfirmMarketingMaterialController {
	

	ActorRef drsCmdBus =  DrsActorSystem.drsCmdBus();

	ActorRef drsQueryBus =  DrsActorSystem.drsQueryBus();
	
	private ObjectMapper mapper = new ObjectMapper();
	private static final int DEFAULT_CONFIRM = 0;
	
	private MaintainProductOnboardingUco getMaintainProductOnboardingUco(){
		return (MaintainProductOnboardingUco)SpringAppCtx.get().getBean("maintainProductOnboardingUco");
	}
	
//	@Autowired private ConfirmMarketingMaterialUco uco;
	
	/*
	private String getProductBaseCodeAndName(String baseProductCode) throws JsonParseException, JsonMappingException, IOException{	
	
		BaseProductOnboarding baseProduct = this.getMaintainProductOnboardingUco().getDetailOfBaseProductOnboarding(baseProductCode);
		
		BaseProductOnboardingDetail baseProductSource = baseProduct.getProductInfoSource();
		String productInfoSourceData = baseProductSource.getData();		
		JsonNode productInfoSourceJsonNode = mapper.readValue(productInfoSourceData, JsonNode.class);										
		return baseProductCode+" "+productInfoSourceJsonNode.get("productNameEnglish").asText();		
	}
	*/
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_VIEW'))")			
	@RequestMapping(value = "/ConfirmMarketingMaterial/{baseProductCode}/{variationCode}/{marketSideRegion}")		
	public String showMarketSideProductMarketingMaterial(
			@PathVariable String baseProductCode, @PathVariable String variationCode , @PathVariable String marketSideRegion,
			@RequestParam(value="supplierKcode",defaultValue="",required=false) String supplierKcode,
			Model model) throws JsonParseException,JsonMappingException,IOException{		
		
		
		Timeout timeout = new Timeout(Duration.create(180, "seconds"));
		
		 final Future<Object> f1 =
	                ask(drsQueryBus, 
	                		new  GetProductMarketingMaterial(Country.CORE , supplierKcode ,
	                				baseProductCode,variationCode),timeout);
		 
	 
		 ObjectMapper mapper = new ObjectMapper();
		    try {
		    	
		    	String str  = (String)  Await.result(f1, timeout.duration());
		    	
		    	ProductDetail pm =
		    			mapper.readValue(str, ProductDetail.class);
		    	
		    	model.addAttribute("product", JsonHelper.toJson(pm));		
		    	model.addAttribute("breadcrumbProduct",baseProductCode+" "+ this.getProductNameEng(baseProductCode));
				
					
				model.addAttribute("baseProductCode", baseProductCode);
				model.addAttribute("supplierKcode", supplierKcode);
				model.addAttribute("marketSideRegion",marketSideRegion);
				model.addAttribute("region", marketSideRegion);
				model.addAttribute("type","marketSide");
				
				model.addAttribute("marketSideList",this.getMarketSides(baseProductCode, variationCode));
				
				 final Future<Object> f3 =
			                ask(drsCmdBus, 
			                	new com.kindminds.drs.api.message.confirmMarketingMaterialUco.GetComments(
			                	Marketplace.getIdFromCountry(marketSideRegion), baseProductCode),
			                	timeout);
				    List<ConfirmMarketingMaterialComment> comments = new ArrayList<>();
					comments = (List<ConfirmMarketingMaterialComment>) Await.result(f1, timeout.duration());
					model.addAttribute("comments", comments);
		    	
			} catch (Exception e) {
				// TODO Auto-generated catch blockc
				e.printStackTrace();
			}
		 
				
		return "ConfirmMarketingMaterialView";		
	}
	
	
	
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCT_ONBOARDING_VIEW'))")			
	@RequestMapping(value = "/sendDefaultEmail/{baseProductCode}/{marketSideRegion}")		
	public @ResponseBody String sendNotificationToSupplier(
			@PathVariable String baseProductCode, 
			@PathVariable String marketSideRegion,
			@RequestParam(value="supplierKcode",defaultValue="",required=false) String supplierKcode
		) throws JsonParseException, JsonMappingException, IOException {

		String baseCodeAndName = baseProductCode+" "+ this.getProductNameEng(baseProductCode);
		
	    drsCmdBus.tell(new com.kindminds.drs.api.message.confirmMarketingMaterialUco.SendEmail(
	    		DEFAULT_CONFIRM, marketSideRegion, baseProductCode, baseCodeAndName, supplierKcode), ActorRef.noSender());
	    
	    return "email sent";
	    
	    
	}
	
	
	
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('ISSUES_POST_COMMENT'))")	
	@RequestMapping(value = "/ConfirmMarketingMaterial/{baseProductCode}/{marketSideRegion}/postComment",method = RequestMethod.POST)
	public String postComment(@ModelAttribute("Comment") IssueCommentImpl comment,
			@PathVariable String baseProductCode, @PathVariable String marketSideRegion,
			@RequestParam(value="supplierKcode",defaultValue="",required=false) String supplierKcode,
			Model model) throws JsonParseException, JsonMappingException, IOException{
		String baseCodeAndName = baseProductCode+" "+ this.getProductNameEng(baseProductCode);
		String contents = comment.getContents();
		
		int userId = Context.getCurrentUser().getUserId();
		drsCmdBus.tell(new com.kindminds.drs.api.message.confirmMarketingMaterialUco.AddComment(
				userId, marketSideRegion, baseProductCode, supplierKcode, contents, baseCodeAndName),
				ActorRef.noSender());
		
		return "redirect:/ConfirmMarketingMaterial/" + baseProductCode + "/"
				+ marketSideRegion + "?supplierKcode=" + supplierKcode;	
	}
	
	
	private List<String> createMarketSides(JsonNode productInfoSourceJsonNode ) {
		
		List<String> marketSideList  = new ArrayList<String>();
		JsonNode applicableRegionBPJsonNode;
		try {
			applicableRegionBPJsonNode = mapper.readValue(productInfoSourceJsonNode.get("applicableRegionBP"), JsonNode.class);
		
			for(JsonNode applicableRegionBP:applicableRegionBPJsonNode){			
				marketSideList.add(applicableRegionBP.asText());				
			}		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		return marketSideList;
		
	}
	
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
