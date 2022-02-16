package com.kindminds.drs.web.ctrl.customercare;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import akka.actor.ActorRef;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindminds.drs.api.message.command.*;
import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseDto;
import com.kindminds.drs.web.config.*;
import com.kindminds.drs.web.data.dto.*;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kindminds.drs.Locale;
import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.usecase.customercare.MaintainCustomerCareCaseIssueTemplateUco;
import com.kindminds.drs.api.usecase.customercare.MaintainCustomerCareCaseIssueUco;
import com.kindminds.drs.api.usecase.customercare.MaintainCustomerCareCaseUco;

import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseDto.CustomerCareCaseMessage;
import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseIssue;
import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseIssue.CustomerCareCaseIssueTemplate;
import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseOrderInfo;
import com.kindminds.drs.api.v1.model.common.DtoList;
import com.kindminds.drs.api.v1.model.common.Pager;
import com.kindminds.drs.service.util.SpringAppCtx;
import com.kindminds.drs.util.JsonHelper;
import com.kindminds.drs.web.ctrl.accounting.Ss2spStatementController;
import com.kindminds.drs.web.data.dto.CustomerCareCaseDtoImpl;

@Controller
public class CustomerCareCaseController {


	ActorRef drsCmdBus = DrsActorSystem.drsCmdBus();

	private MaintainCustomerCareCaseUco getMaintainCustomerCareCaseUco(){		
		return (MaintainCustomerCareCaseUco)(SpringAppCtx.get().getBean("maintainCustomerCareCaseUco"));		
	}
	
	private MaintainCustomerCareCaseIssueUco getMaintainCustomerCareCaseIssueUco(){		
		return (MaintainCustomerCareCaseIssueUco)(SpringAppCtx.get().getBean("maintainCustomerCareCaseIssueUco"));			
	}
		
	private MaintainCustomerCareCaseIssueTemplateUco getMaintainCustomerCareCaseIssueTemplateUco(){		
		return (MaintainCustomerCareCaseIssueTemplateUco)(SpringAppCtx.get().getBean("maintainCustomerCareCaseIssueTemplateUco"));				
	}
		
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('CUSTOMER_CARE_CASE_LIST'))")
	@RequestMapping(value = "/CustomerCareCases")
	public String listOfCustomerCareCases(@RequestParam(value="page",defaultValue="1",required=false) Integer pageIndex,Model model){										
		DtoList<CustomerCareCaseDto> customerCareCaseList =
				this.getMaintainCustomerCareCaseUco().getListElastic(pageIndex, "");
		Pager page = customerCareCaseList.getPager();	
		Map<String,Map<Integer,String>> issueTypeToIssuesMap = this.getMaintainCustomerCareCaseIssueUco().getIssueTypeToIssuesMap(null,null,null);				
		Map<Integer,String> issueNameMap = new HashMap<Integer,String>();
		for (Entry<String, Map<Integer, String>> issue : issueTypeToIssuesMap.entrySet()){ 			  						
			issueNameMap.putAll(issue.getValue());
		}
		//TODO
		List<Map<String,String>> relatedProduct = new ArrayList<Map<String,String>>();				
		for(CustomerCareCaseDto item: customerCareCaseList.getItems()){
			if(item.getSupplierKcode() != null){		
				if(item.getRelatedProductBaseCodeList() == null){	    	
					Map<String,String> productSkuCodeToSupplierNameMap = this.getMaintainCustomerCareCaseUco().getProductSkuCodeToSupplierNameMap(item.getSupplierKcode());						    	
					relatedProduct.add(productSkuCodeToSupplierNameMap);	    	   			    	
				}else{				    	
					Map<String,String> productBaseCodeToSupplierNameMap = this.getMaintainCustomerCareCaseUco().getProductBaseCodeToSupplierNameMap(item.getSupplierKcode());
					relatedProduct.add(productBaseCodeToSupplierNameMap);	    			    			    	
				}
			}  
		}	
		model.addAttribute("totalPages",page.getTotalPages());
		model.addAttribute("currentPageIndex",page.getCurrentPageIndex());
		model.addAttribute("startPage",page.getStartPage());
		model.addAttribute("endPage",page.getEndPage());
		model.addAttribute("CustomerCareCaseList", customerCareCaseList.getItems());
		model.addAttribute("issueNameMap",issueNameMap);
		model.addAttribute("type","list");
		model.addAttribute("relatedProduct", relatedProduct);
		return "th/customerCareCase/ListOfCustomerCareCases";
	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('CUSTOMER_CARE_CASE_LIST'))")
	@RequestMapping(value = "/CustomerCareCasesV4")
	public String listOfCustomerCareCasesV4(@RequestParam(value="page",defaultValue="1",required=false) Integer pageIndex,Model model){
		DtoList<CustomerCareCaseDto> customerCareCaseList =
				this.getMaintainCustomerCareCaseUco().getListElastic(pageIndex, "");
		Pager page = customerCareCaseList.getPager();
		Map<String,Map<Integer,String>> issueTypeToIssuesMap = this.getMaintainCustomerCareCaseIssueUco().getIssueTypeToIssuesMap(null,null,null);
		Map<Integer,String> issueNameMap = new HashMap<Integer,String>();
		for (Entry<String, Map<Integer, String>> issue : issueTypeToIssuesMap.entrySet()){
			issueNameMap.putAll(issue.getValue());
		}
		//TODO
		List<Map<String,String>> relatedProduct = new ArrayList<Map<String,String>>();
		for(CustomerCareCaseDto item: customerCareCaseList.getItems()){
			if(item.getSupplierKcode() != null){
				if(item.getRelatedProductBaseCodeList() == null){
					Map<String,String> productSkuCodeToSupplierNameMap = this.getMaintainCustomerCareCaseUco().getProductSkuCodeToSupplierNameMap(item.getSupplierKcode());
					relatedProduct.add(productSkuCodeToSupplierNameMap);
				}else{
					Map<String,String> productBaseCodeToSupplierNameMap = this.getMaintainCustomerCareCaseUco().getProductBaseCodeToSupplierNameMap(item.getSupplierKcode());
					relatedProduct.add(productBaseCodeToSupplierNameMap);
				}
			}
		}
		model.addAttribute("totalPages",page.getTotalPages());
		model.addAttribute("currentPageIndex",page.getCurrentPageIndex());
		model.addAttribute("startPage",page.getStartPage());
		model.addAttribute("endPage",page.getEndPage());
		model.addAttribute("CustomerCareCaseList", customerCareCaseList.getItems());
		model.addAttribute("issueNameMap",issueNameMap);
		model.addAttribute("type","list");
		model.addAttribute("relatedProduct", relatedProduct);
		return "ListOfCustomerCareCasesV4";
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('CUSTOMER_CARE_CASE_LIST'))")	
	@RequestMapping(value = "/CustomerCareCases/search")
	public String searchCustomerCareCases(
			@RequestParam(value="searchWords") String searchWords,
			@RequestParam(value="page",defaultValue="1",required=false) Integer pageIndex,
			Model model) throws UnsupportedEncodingException {
		DtoList<CustomerCareCaseDto> customerCareCaseList =
				this.getMaintainCustomerCareCaseUco().getListElastic(pageIndex, searchWords);
		Pager page = customerCareCaseList.getPager();
		Map<String,Map<Integer,String>> issueTypeToIssuesMap = this.getMaintainCustomerCareCaseIssueUco().getIssueTypeToIssuesMap(null,null,null);				
		Map<Integer,String> issueNameMap = new HashMap<Integer,String>();	
		for (Entry<String, Map<Integer, String>> issue : issueTypeToIssuesMap.entrySet()){			   			  						
			issueNameMap.putAll(issue.getValue());
		}
		List<Map<String,String>> relatedProduct = new ArrayList<Map<String,String>>();			
		for(CustomerCareCaseDto item: customerCareCaseList.getItems()){
			if(item.getSupplierKcode() != null){
				if(item.getRelatedProductBaseCodeList() == null){
					Map<String,String> productSkuCodeToSupplierNameMap = this.getMaintainCustomerCareCaseUco().getProductSkuCodeToSupplierNameMap(item.getSupplierKcode());			    	
					relatedProduct.add(productSkuCodeToSupplierNameMap);
				}else{				
					Map<String,String> productBaseCodeToSupplierNameMap = this.getMaintainCustomerCareCaseUco().getProductBaseCodeToSupplierNameMap(item.getSupplierKcode());
					relatedProduct.add(productBaseCodeToSupplierNameMap);
				}	
			}		    
		}
		model.addAttribute("totalPages",page.getTotalPages());
		model.addAttribute("currentPageIndex",page.getCurrentPageIndex());
		model.addAttribute("startPage",page.getStartPage());
		model.addAttribute("endPage",page.getEndPage());	
		model.addAttribute("CustomerCareCaseList", customerCareCaseList.getItems());
		model.addAttribute("issueNameMap",issueNameMap);
		model.addAttribute("type","search");
		
		model.addAttribute("searchWords",searchWords);
		model.addAttribute("relatedProduct", relatedProduct);				
		return "th/customerCareCase/ListOfCustomerCareCases";
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('CUSTOMER_CARE_CASE_CREATE'))")
	@RequestMapping(value = "/CustomerCareCases/create", method = RequestMethod.GET)	
	public String createCustomerCareCases(@ModelAttribute("CustomerCareCase") CustomerCareCaseDtoImpl customerCareCase, Model model){
		customerCareCase.messages.add(new CustomerCareCaseBaseProductMessageImpl());							
		List<String> caseTypeList = this.getMaintainCustomerCareCaseUco().getTypeList();	
		Map<String,String> supplierKcodeToShortEnUsNameMap = this.getMaintainCustomerCareCaseUco().getSupplierKcodeToShortEnUsNameMap();	
		List<Marketplace> marketplaceList = this.getMaintainCustomerCareCaseUco().getMarketplaceList();
		Map<Integer,String> marketplaceMap = this.generateMarketplaceMap(marketplaceList);
		Map<Integer,String> marketplaceIdToDrsCompanyMap = this.getMaintainCustomerCareCaseUco().getMarketplaceIdToDrsCompanyMap();
		Map<String, String> marketplaceToDrsCompanyMap = this.getMaintainCustomerCareCaseUco().getMarketplaceToDrsCompanyMap();
		Map<Integer,String> categoryIdToNameMap = this.getMaintainCustomerCareCaseIssueUco().getCategoryIdToNameMap();
		model.addAttribute("CustomerCareCase", customerCareCase);
		//model.addAttribute("CustomerCareCaseJson", JsonHelper.toJson(customerCareCase));
		model.addAttribute("CustomerCareCaseJson", customerCareCase);
		model.addAttribute("caseTypeList", caseTypeList);		
		model.addAttribute("supplierKcodeToShortEnUsNameMap", supplierKcodeToShortEnUsNameMap);			
		model.addAttribute("marketplaceList", marketplaceList);			
		//model.addAttribute("marketplaceMapJson",JsonHelper.toJson(marketplaceMap));
		model.addAttribute("marketplaceMapJson",marketplaceMap);
		//model.addAttribute("marketplaceIdToDrsCompanyMapJson", JsonHelper.toJson(marketplaceIdToDrsCompanyMap));
		//model.addAttribute("marketplaceToDrsCompanyMapJson", JsonHelper.toJson(marketplaceToDrsCompanyMap));
		model.addAttribute("marketplaceIdToDrsCompanyMapJson", marketplaceIdToDrsCompanyMap);
		model.addAttribute("marketplaceToDrsCompanyMapJson", marketplaceToDrsCompanyMap);
		model.addAttribute("categoryIdToNameMap",categoryIdToNameMap);
		model.addAttribute("type", "Create");
		return "th/customerCareCase/CustomerCareCase";
	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('CUSTOMER_CARE_CASE_CREATE'))")
	@RequestMapping(value = "/CustomerCareCasesV4/create", method = RequestMethod.GET)
	public String createCustomerCareCasesV4(@ModelAttribute("CustomerCareCase") CustomerCareCaseDtoImpl customerCareCase, Model model){
		customerCareCase.messages.add(new CustomerCareCaseBaseProductMessageImpl());
		List<String> caseTypeList = this.getMaintainCustomerCareCaseUco().getTypeList();
		Map<String,String> supplierKcodeToShortEnUsNameMap = this.getMaintainCustomerCareCaseUco().getSupplierKcodeToShortEnUsNameMap();
		List<Marketplace> marketplaceList = this.getMaintainCustomerCareCaseUco().getMarketplaceList();
		Map<Integer,String> marketplaceMap = this.generateMarketplaceMap(marketplaceList);
		Map<Integer,String> marketplaceIdToDrsCompanyMap = this.getMaintainCustomerCareCaseUco().getMarketplaceIdToDrsCompanyMap();
		Map<String, String> marketplaceToDrsCompanyMap = this.getMaintainCustomerCareCaseUco().getMarketplaceToDrsCompanyMap();
		Map<Integer,String> categoryIdToNameMap = this.getMaintainCustomerCareCaseIssueUco().getCategoryIdToNameMap();
		model.addAttribute("CustomerCareCase", customerCareCase);
		model.addAttribute("CustomerCareCaseJson", JsonHelper.toJson(customerCareCase));
		model.addAttribute("caseTypeList", caseTypeList);
		model.addAttribute("supplierKcodeToShortEnUsNameMap", supplierKcodeToShortEnUsNameMap);
		model.addAttribute("marketplaceList", marketplaceList);
		model.addAttribute("marketplaceMapJson",JsonHelper.toJson(marketplaceMap));
		model.addAttribute("marketplaceIdToDrsCompanyMapJson", JsonHelper.toJson(marketplaceIdToDrsCompanyMap));
		model.addAttribute("marketplaceToDrsCompanyMapJson", JsonHelper.toJson(marketplaceToDrsCompanyMap));
		model.addAttribute("categoryIdToNameMap",categoryIdToNameMap);
		model.addAttribute("type", "Create");
		return "CustomerCareCaseV4";
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('CUSTOMER_CARE_CASE_CREATE'))")
	@RequestMapping(value = "/CustomerCareCases/save")
	public String saveCustomerCareCases(@ModelAttribute("CustomerCareCase") CustomerCareCaseDtoImpl customerCareCase, Model model){
		Map<String,String> DrsCompanyKcodeToShortEnUsNameMap = this.getMaintainCustomerCareCaseUco().getDrsCompanyKcodeToShortEnUsNameMap();
	    String drsCompanyKcode = null;
		String drsCompany = customerCareCase.getDrsCompanyKcode();
		for(Entry<String,String> entry: DrsCompanyKcodeToShortEnUsNameMap.entrySet()){
            if(drsCompany.equals(entry.getValue())){
            	drsCompanyKcode = (String) entry.getKey();
                break;
            }
        }
		
		if(StringUtils.hasText(customerCareCase.getMarketplaceOrderId())){
			CustomerCareCaseOrderInfo orderInfo	= this.getMaintainCustomerCareCaseUco().getOrderDateById(customerCareCase.getMarketplaceOrderId());	
			customerCareCase.setMarketplace(Marketplace.fromKey(orderInfo.getMarketpaceId()));
		}
		
		
		customerCareCase.setDrsCompanyKcode(drsCompanyKcode);
		
		if(customerCareCase.getIssueTypeCategoryId() != null)
			if(customerCareCase.getIssueTypeCategoryId() == 0) customerCareCase.setIssueTypeCategoryId(null);
		if(customerCareCase.getRelatedProductSkuCodeList()!=null)
				if(customerCareCase.getRelatedProductSkuCodeList().isEmpty()) customerCareCase.setRelatedProductSkuCodeList(null);
		if(customerCareCase.getRelatedProductBaseCodeList()!=null)
				if(customerCareCase.getRelatedProductBaseCodeList().isEmpty()) customerCareCase.setRelatedProductBaseCodeList(null);		
		
		System.out.println("***: " + customerCareCase);
		int caseId = this.getMaintainCustomerCareCaseUco().save(customerCareCase);

		//todo move above code to backend
		drsCmdBus.tell(new SaveCustomercareCase(caseId) , ActorRef.noSender());

		return "redirect:/CustomerCareCases/"+caseId;
		
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('CUSTOMER_CARE_CASE_LIST'))")	
	@RequestMapping(value = "/CustomerCareCases/{caseId}")
	public String showCustomerCareCase(@ModelAttribute("Message") CustomerCareCaseBaseProductMessageImpl Message,Model model, @PathVariable int caseId){
		CustomerCareCaseDto customerCareCase = this.getMaintainCustomerCareCaseUco().get(caseId);
			CustomerCareCaseDtoImpl CustomerCareCaseDtoImpl = new CustomerCareCaseDtoImpl(customerCareCase);
			Map<String,String> DrsCompanyKcodeToShortEnUsNameMap = this.getMaintainCustomerCareCaseUco().getDrsCompanyKcodeToShortEnUsNameMap();
			Map<String,String> supplierKcodeToShortEnUsNameMap = this.getMaintainCustomerCareCaseUco().getSupplierKcodeToShortEnUsNameMap();				
			Map<String,Map<Integer,String>> issueTypeToIssuesMap = this.getMaintainCustomerCareCaseIssueUco().getIssueTypeToIssuesMap(null,null,null);	
			List<Marketplace> marketplaceList = this.getMaintainCustomerCareCaseUco().getMarketplaceList();	
			Map<Integer,String> issueNameMap = new HashMap<Integer,String>();
			Map<Integer,String> issueIdToTypeMap = new HashMap<Integer,String>();			
			for (Entry<String, Map<Integer, String>> issue : issueTypeToIssuesMap.entrySet()){			   			  						
				issueNameMap.putAll(issue.getValue());
			}
			for (Entry<String, Map<Integer, String>> issue : issueTypeToIssuesMap.entrySet()){
				for( Entry<Integer,String> issueIdToNameMap:issue.getValue().entrySet()){					
					issueIdToTypeMap.put(issueIdToNameMap.getKey(), issue.getKey());									
				}			
			}		
			if(CustomerCareCaseDtoImpl.getRelatedProductSkuCodeList() == null){
				Map<String,String> productBaseCodeToSupplierNameMap = this.getMaintainCustomerCareCaseUco().getProductBaseCodeToSupplierNameMap(CustomerCareCaseDtoImpl.getSupplierKcode());
				model.addAttribute("productBaseCodeToSupplierNameMap", productBaseCodeToSupplierNameMap);
				Map<String, String> productSkuCodeToSupplierNameMapUnderBases = this.getMaintainCustomerCareCaseUco().getProductSkuCodeToSupplierNameMapUnderBases(CustomerCareCaseDtoImpl.getRelatedProductBaseCodeList());
				model.addAttribute("productSkuCodeToSupplierNameMapUnderBases", productSkuCodeToSupplierNameMapUnderBases);																											
			}			
			if(CustomerCareCaseDtoImpl.getRelatedProductBaseCodeList() == null){
				Map<String,String> productSkuCodeToSupplierNameMap = this.getMaintainCustomerCareCaseUco().getProductSkuCodeToSupplierNameMap(CustomerCareCaseDtoImpl.getSupplierKcode());
				model.addAttribute("productSkuCodeToSupplierNameMap", productSkuCodeToSupplierNameMap);												
			}			
			Map<Integer,String> applicableTemplates = this.getMaintainCustomerCareCaseUco().getApplicableTemplate(caseId);			
			Map<Integer,String> issueCategoryIdToNameMap = this.getMaintainCustomerCareCaseUco().getIssueCategoryIdToNameMap();
			Map<Integer,String> typeIdToNameMap = this.getMaintainCustomerCareCaseIssueUco().getTypeIdToNameMap();						
			model.addAttribute("issueNameMap",issueNameMap);
			model.addAttribute("issueIdToTypeMap", issueIdToTypeMap);
			model.addAttribute("CustomerCareCase", CustomerCareCaseDtoImpl);
			model.addAttribute("applicableTemplates",applicableTemplates);
			model.addAttribute("DrsCompanyKcodeToShortEnUsNameMap",DrsCompanyKcodeToShortEnUsNameMap);
			model.addAttribute("supplierKcodeToShortEnUsNameMap",supplierKcodeToShortEnUsNameMap);
			model.addAttribute("marketplaceList", marketplaceList);
			model.addAttribute("issueCategoryIdToNameMap",issueCategoryIdToNameMap);
			model.addAttribute("typeIdToNameMap",typeIdToNameMap);				
			return "th/customerCareCase/CustomerCareCaseView";
	}


	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('CUSTOMER_CARE_CASE_LIST'))")
	@RequestMapping(value = "/CustomerCareCasesV4/{caseId}")
	public String showCustomerCareCaseV4(@ModelAttribute("Message") CustomerCareCaseBaseProductMessageImpl Message,Model model, @PathVariable int caseId){
		CustomerCareCaseDto customerCareCase = this.getMaintainCustomerCareCaseUco().get(caseId);
		CustomerCareCaseDtoImpl CustomerCareCaseDtoImpl = new CustomerCareCaseDtoImpl(customerCareCase);
		Map<String,String> DrsCompanyKcodeToShortEnUsNameMap = this.getMaintainCustomerCareCaseUco().getDrsCompanyKcodeToShortEnUsNameMap();
		Map<String,String> supplierKcodeToShortEnUsNameMap = this.getMaintainCustomerCareCaseUco().getSupplierKcodeToShortEnUsNameMap();
		Map<String,Map<Integer,String>> issueTypeToIssuesMap = this.getMaintainCustomerCareCaseIssueUco().getIssueTypeToIssuesMap(null,null,null);
		List<Marketplace> marketplaceList = this.getMaintainCustomerCareCaseUco().getMarketplaceList();
		Map<Integer,String> issueNameMap = new HashMap<Integer,String>();
		Map<Integer,String> issueIdToTypeMap = new HashMap<Integer,String>();
		for (Entry<String, Map<Integer, String>> issue : issueTypeToIssuesMap.entrySet()){
			issueNameMap.putAll(issue.getValue());
		}
		for (Entry<String, Map<Integer, String>> issue : issueTypeToIssuesMap.entrySet()){
			for( Entry<Integer,String> issueIdToNameMap:issue.getValue().entrySet()){
				issueIdToTypeMap.put(issueIdToNameMap.getKey(), issue.getKey());
			}
		}
		if(CustomerCareCaseDtoImpl.getRelatedProductSkuCodeList() == null){
			Map<String,String> productBaseCodeToSupplierNameMap = this.getMaintainCustomerCareCaseUco().getProductBaseCodeToSupplierNameMap(CustomerCareCaseDtoImpl.getSupplierKcode());
			model.addAttribute("productBaseCodeToSupplierNameMap", productBaseCodeToSupplierNameMap);
			Map<String, String> productSkuCodeToSupplierNameMapUnderBases = this.getMaintainCustomerCareCaseUco().getProductSkuCodeToSupplierNameMapUnderBases(CustomerCareCaseDtoImpl.getRelatedProductBaseCodeList());
			model.addAttribute("productSkuCodeToSupplierNameMapUnderBases", productSkuCodeToSupplierNameMapUnderBases);
		}
		if(CustomerCareCaseDtoImpl.getRelatedProductBaseCodeList() == null){
			Map<String,String> productSkuCodeToSupplierNameMap = this.getMaintainCustomerCareCaseUco().getProductSkuCodeToSupplierNameMap(CustomerCareCaseDtoImpl.getSupplierKcode());
			model.addAttribute("productSkuCodeToSupplierNameMap", productSkuCodeToSupplierNameMap);
		}
		Map<Integer,String> applicableTemplates = this.getMaintainCustomerCareCaseUco().getApplicableTemplate(caseId);
		Map<Integer,String> issueCategoryIdToNameMap = this.getMaintainCustomerCareCaseUco().getIssueCategoryIdToNameMap();
		Map<Integer,String> typeIdToNameMap = this.getMaintainCustomerCareCaseIssueUco().getTypeIdToNameMap();
		model.addAttribute("issueNameMap",issueNameMap);
		model.addAttribute("issueIdToTypeMap", issueIdToTypeMap);
		model.addAttribute("CustomerCareCase", CustomerCareCaseDtoImpl);
		model.addAttribute("applicableTemplates",applicableTemplates);
		model.addAttribute("DrsCompanyKcodeToShortEnUsNameMap",DrsCompanyKcodeToShortEnUsNameMap);
		model.addAttribute("supplierKcodeToShortEnUsNameMap",supplierKcodeToShortEnUsNameMap);
		model.addAttribute("marketplaceList", marketplaceList);
		model.addAttribute("issueCategoryIdToNameMap",issueCategoryIdToNameMap);
		model.addAttribute("typeIdToNameMap",typeIdToNameMap);
		return "CustomerCareCaseViewV4";
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('CUSTOMER_CARE_CASE_VIEW'))")
	@RequestMapping(value = "/CustomerCareCases/{caseId}/{msg}")
	public String showDetailsOfCustomerCareCase(@ModelAttribute("Message") CustomerCareCaseBaseProductMessageImpl Message,Model model, @PathVariable int caseId,@PathVariable String msg){
		CustomerCareCaseDto customerCareCase =  this.getMaintainCustomerCareCaseUco().get(caseId);
		CustomerCareCaseMessage message = this.getMaintainCustomerCareCaseUco().getMessage(caseId, Integer.parseInt(msg));				
		if(customerCareCase.getRelatedProductSkuCodeList() == null){
			Map<String,String> productBaseCodeToSupplierNameMap = this.getMaintainCustomerCareCaseUco().getProductBaseCodeToSupplierNameMap(customerCareCase.getSupplierKcode());				
			model.addAttribute("productBaseCodeToSupplierNameMap", productBaseCodeToSupplierNameMap);
			model.addAttribute("productSkuCodeToSupplierNameMap", "");
		}		
		if(customerCareCase.getRelatedProductBaseCodeList() == null){			
			Map<String,String> productSkuCodeToSupplierNameMap = this.getMaintainCustomerCareCaseUco().getProductSkuCodeToSupplierNameMap(customerCareCase.getSupplierKcode());				
			model.addAttribute("productBaseCodeToSupplierNameMap", "");
			model.addAttribute("productSkuCodeToSupplierNameMap", productSkuCodeToSupplierNameMap);						
		}			
		model.addAttribute("caseId",caseId);
		model.addAttribute("Message", message);
		model.addAttribute("statementRootUrl",Ss2spStatementController.rootUrl);			
		return "th/customerCareCase/CustomerCareCaseMessageWorkDetails";
	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('CUSTOMER_CARE_CASE_VIEW'))")
	@RequestMapping(value = "/CustomerCareCasesV4/{caseId}/{msg}")
	public String showDetailsOfCustomerCareCaseV4(@ModelAttribute("Message") CustomerCareCaseBaseProductMessageImpl Message,Model model, @PathVariable int caseId,@PathVariable String msg){
		CustomerCareCaseDto customerCareCase =  this.getMaintainCustomerCareCaseUco().get(caseId);
		CustomerCareCaseMessage message = this.getMaintainCustomerCareCaseUco().getMessage(caseId, Integer.parseInt(msg));
		if(customerCareCase.getRelatedProductSkuCodeList() == null){
			Map<String,String> productBaseCodeToSupplierNameMap = this.getMaintainCustomerCareCaseUco().getProductBaseCodeToSupplierNameMap(customerCareCase.getSupplierKcode());
			model.addAttribute("productBaseCodeToSupplierNameMap", productBaseCodeToSupplierNameMap);
			model.addAttribute("productSkuCodeToSupplierNameMap", "");
		}
		if(customerCareCase.getRelatedProductBaseCodeList() == null){
			Map<String,String> productSkuCodeToSupplierNameMap = this.getMaintainCustomerCareCaseUco().getProductSkuCodeToSupplierNameMap(customerCareCase.getSupplierKcode());
			model.addAttribute("productBaseCodeToSupplierNameMap", "");
			model.addAttribute("productSkuCodeToSupplierNameMap", productSkuCodeToSupplierNameMap);
		}
		model.addAttribute("caseId",caseId);
		model.addAttribute("Message", message);
		model.addAttribute("statementRootUrl",Ss2spStatementController.rootUrl);
		return "CustomerCareCaseMessageWorkDetailsV4";
	}
			
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('CUSTOMER_CARE_CASE_EDIT'))")
	@RequestMapping(value = "/CustomerCareCases/{caseId}/edit")
	public String editCustomerCareCases(Model model, @PathVariable int caseId){
		CustomerCareCaseDto customerCareCase = this.getMaintainCustomerCareCaseUco().get(caseId);

		CustomerCareCaseDtoImpl customerCareCaseDtoImpl = new CustomerCareCaseDtoImpl(customerCareCase);
		List<String> caseTypeList = this.getMaintainCustomerCareCaseUco().getTypeList();	
		Map<String,String> supplierKcodeToShortEnUsNameMap = this.getMaintainCustomerCareCaseUco().getSupplierKcodeToShortEnUsNameMap();	
		List<Marketplace> marketplaceList = this.getMaintainCustomerCareCaseUco().getMarketplaceList();
		Map<Integer,String> marketplaceMap = this.generateMarketplaceMap(marketplaceList);			
		Map<Integer,String> marketplaceIdToDrsCompanyMap = this.getMaintainCustomerCareCaseUco().getMarketplaceIdToDrsCompanyMap();
		Map<String, String> marketplaceToDrsCompanyMap = this.getMaintainCustomerCareCaseUco().getMarketplaceToDrsCompanyMap();			
		Map<Integer,String> categoryIdToNameMap = this.getMaintainCustomerCareCaseIssueUco().getCategoryIdToNameMap();		
		model.addAttribute("CustomerCareCase", customerCareCaseDtoImpl);

		//model.addAttribute("CustomerCareCaseJson", JsonHelper.toJson(customerCareCaseDtoImpl));
		model.addAttribute("CustomerCareCaseJson", customerCareCaseDtoImpl);
		model.addAttribute("caseTypeList", caseTypeList);
		model.addAttribute("supplierKcodeToShortEnUsNameMap", supplierKcodeToShortEnUsNameMap);
		model.addAttribute("marketplaceList", marketplaceList);
		//model.addAttribute("marketplaceMapJson",JsonHelper.toJson(marketplaceMap));
		model.addAttribute("marketplaceMapJson",marketplaceMap);
		//model.addAttribute("marketplaceIdToDrsCompanyMapJson", JsonHelper.toJson(marketplaceIdToDrsCompanyMap));
		model.addAttribute("marketplaceIdToDrsCompanyMapJson", marketplaceIdToDrsCompanyMap);
		//model.addAttribute("marketplaceToDrsCompanyMapJson", JsonHelper.toJson(marketplaceToDrsCompanyMap));
		model.addAttribute("marketplaceToDrsCompanyMapJson", marketplaceToDrsCompanyMap);
		model.addAttribute("categoryIdToNameMap",categoryIdToNameMap);
		model.addAttribute("type", "Edit");
		System.out.println(JsonHelper.toJson(customerCareCaseDtoImpl));
		return "th/customerCareCase/CustomerCareCase";
	}


	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('CUSTOMER_CARE_CASE_EDIT'))")
	@RequestMapping(value = "/CustomerCareCasesV4/{caseId}/edit")
	public String editCustomerCareCasesV4(Model model, @PathVariable int caseId){
		CustomerCareCaseDto customerCareCase = this.getMaintainCustomerCareCaseUco().get(caseId);
		CustomerCareCaseDtoImpl customerCareCaseDtoImpl = new CustomerCareCaseDtoImpl(customerCareCase);
		List<String> caseTypeList = this.getMaintainCustomerCareCaseUco().getTypeList();
		Map<String,String> supplierKcodeToShortEnUsNameMap = this.getMaintainCustomerCareCaseUco().getSupplierKcodeToShortEnUsNameMap();
		List<Marketplace> marketplaceList = this.getMaintainCustomerCareCaseUco().getMarketplaceList();
		Map<Integer,String> marketplaceMap = this.generateMarketplaceMap(marketplaceList);
		Map<Integer,String> marketplaceIdToDrsCompanyMap = this.getMaintainCustomerCareCaseUco().getMarketplaceIdToDrsCompanyMap();
		Map<String, String> marketplaceToDrsCompanyMap = this.getMaintainCustomerCareCaseUco().getMarketplaceToDrsCompanyMap();
		Map<Integer,String> categoryIdToNameMap = this.getMaintainCustomerCareCaseIssueUco().getCategoryIdToNameMap();
		model.addAttribute("CustomerCareCase", customerCareCaseDtoImpl);
		model.addAttribute("CustomerCareCaseJson", JsonHelper.toJson(customerCareCaseDtoImpl));
		model.addAttribute("caseTypeList", caseTypeList);
		model.addAttribute("supplierKcodeToShortEnUsNameMap", supplierKcodeToShortEnUsNameMap);
		model.addAttribute("marketplaceList", marketplaceList);
		model.addAttribute("marketplaceMapJson",JsonHelper.toJson(marketplaceMap));
		model.addAttribute("marketplaceIdToDrsCompanyMapJson", JsonHelper.toJson(marketplaceIdToDrsCompanyMap));
		model.addAttribute("marketplaceToDrsCompanyMapJson", JsonHelper.toJson(marketplaceToDrsCompanyMap));
		model.addAttribute("categoryIdToNameMap",categoryIdToNameMap);
		model.addAttribute("type", "Edit");
		return "CustomerCareCaseV4";
	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('CUSTOMER_CARE_CASE_EDIT'))")
	@RequestMapping(value = "/CustomerCareCases/updateStatus")	    
	public String updateCustomerCareCaseStatus(@ModelAttribute("CustomerCareCase") CustomerCareCaseDtoImpl customerCareCase, Model model){

		int caseId = this.getMaintainCustomerCareCaseUco().updateStatus(customerCareCase);
		drsCmdBus.tell(new UpdateCustomercareCase(caseId) , ActorRef.noSender());
		return "redirect:/CustomerCareCases/"+caseId;
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('CUSTOMER_CARE_CASE_EDIT'))")
	@RequestMapping(value = "/CustomerCareCases/update")	    
	public String updateCustomerCareCase(@ModelAttribute("CustomerCareCase") CustomerCareCaseDtoImpl customerCareCase, Model model){
		Map<String,String> DrsCompanyKcodeToShortEnUsNameMap = this.getMaintainCustomerCareCaseUco().getDrsCompanyKcodeToShortEnUsNameMap();
	    String drsCompanyKcode = null;
		String drsCompany = customerCareCase.getDrsCompanyKcode();		
		for(Entry<String,String> entry: DrsCompanyKcodeToShortEnUsNameMap.entrySet()){
            if(drsCompany.equals(entry.getValue())){
            	drsCompanyKcode = (String) entry.getKey();
                break;
            }
        }
		
		if(StringUtils.hasText(customerCareCase.getMarketplaceOrderId())){
			CustomerCareCaseOrderInfo orderInfo	= this.getMaintainCustomerCareCaseUco().getOrderDateById(customerCareCase.getMarketplaceOrderId());	
			customerCareCase.setMarketplace(Marketplace.fromKey(orderInfo.getMarketpaceId()));
		}
		if(drsCompanyKcode != null) customerCareCase.setDrsCompanyKcode(drsCompanyKcode);
		if(customerCareCase.getIssueTypeCategoryId() != null)
			if(customerCareCase.getIssueTypeCategoryId() == 0)customerCareCase.setIssueTypeCategoryId(null);
		if(customerCareCase.getRelatedProductSkuCodeList()!=null)
				if(customerCareCase.getRelatedProductSkuCodeList().isEmpty()) customerCareCase.setRelatedProductSkuCodeList(null);
		if(customerCareCase.getRelatedProductBaseCodeList()!=null)
				if(customerCareCase.getRelatedProductBaseCodeList().isEmpty()) customerCareCase.setRelatedProductBaseCodeList(null);	

		int caseId = this.getMaintainCustomerCareCaseUco().update(customerCareCase);

		//todo move above code to backend
		drsCmdBus.tell(new UpdateCustomercareCase(caseId) , ActorRef.noSender());

		return "redirect:/CustomerCareCases/"+caseId;	
		
		
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('CUSTOMER_CARE_CASE_CREATEMSG'))")
	@RequestMapping(value = "/CustomerCareCases/postMsg", method = RequestMethod.POST)	   
	public String postMessageForCustomerCareCase(@ModelAttribute("Message") CustomerCareCaseBaseProductMessageImpl Message,@RequestParam("caseId") int caseId,Model model){								
		this.getMaintainCustomerCareCaseUco().addMessage(caseId, Message);	
		return "redirect:/CustomerCareCases/"+caseId;
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('CUSTOMER_CARE_CASE_CREATEMSG'))")
	@RequestMapping(value = "/CustomerCareCases/updateMsg",method = RequestMethod.POST)
	public @ResponseBody String updateMessageForCustomerCareCase(@ModelAttribute("Message") CustomerCareCaseBaseProductMessageImpl Message,@RequestParam("caseId") int caseId,Model model){				
		CustomerCareCaseMessage updateMessage = this.getMaintainCustomerCareCaseUco().updateMessage(caseId, Message);		
		return JsonHelper.toJson(updateMessage);
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('CUSTOMER_CARE_CASE_DELETE'))")
	@RequestMapping(value = "/CustomerCareCases/{caseId}/delete")
    public String deleteCustomerCareCases(Model model, @PathVariable int caseId,final RedirectAttributes redirectAttributes, java.util.Locale locale){


		int id = this.getMaintainCustomerCareCaseUco().delete(caseId);

		//todo move above code to backend
		drsCmdBus.tell(new DeleteCustomercareCase(caseId) , ActorRef.noSender());

		MessageSource source = (MessageSource)SpringAppCtx.get().getBean("messageSource");
		String message = source.getMessage("ccc.deleteCase", new Object[] {String.valueOf(id)}, locale);
		redirectAttributes.addFlashAttribute("message",message);	
		return "redirect:/CustomerCareCases";    	
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('CUSTOMER_CARE_CASE_CREATEMSG'))")
	@RequestMapping(value = "/CustomerCareCases/{caseId}/{lineSeq}/deleteMsg")	
    public String deleteDetailsOfCustomerCareCase(Model model, @PathVariable int caseId, @PathVariable int lineSeq,final RedirectAttributes redirectAttributes, java.util.Locale locale){		
		this.getMaintainCustomerCareCaseUco().deleteMessage(caseId, lineSeq);
		MessageSource source = (MessageSource)SpringAppCtx.get().getBean("messageSource");
		String message = source.getMessage("ccc.deleteMsg", null, locale);
		redirectAttributes.addFlashAttribute("message",message);			
		return "redirect:/CustomerCareCases/"+caseId;		
    }
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('ISSUES_VIEW'))")
	@RequestMapping(value = "/Issues")	
	public String listOfIssue(@RequestParam(value="page",defaultValue="1",required=false) Integer pageIndex,Model model){						
		Map<Integer,String> categoryIdToNameMap = this.getMaintainCustomerCareCaseIssueUco().getCategoryIdToNameMap();
		Map<Integer,String> typeIdToNameMap = this.getMaintainCustomerCareCaseIssueUco().getTypeIdToNameMap();
		DtoList<CustomerCareCaseIssue> issueList = 
				this.getMaintainCustomerCareCaseIssueUco().getListElastic("",pageIndex);
		Pager page = issueList.getPager();		
		Map<String,String> supplierKcodeToShortEnUsNameMap = this.getMaintainCustomerCareCaseUco().getSupplierKcodeToShortEnUsNameMap();							
		List<Map<String,String>> relatedProduct = new ArrayList<Map<String,String>>();		
		for(CustomerCareCaseIssue issue: issueList.getItems()){				
			if(issue.getSupplierKcode() != null){				
				if(issue.getRelatedProductBaseCodeList() == null){		    			    	
					Map<String,String> productSkuCodeToSupplierNameMap = this.getMaintainCustomerCareCaseUco().getProductSkuCodeToSupplierNameMap(issue.getSupplierKcode());						    	
					relatedProduct.add(productSkuCodeToSupplierNameMap);		    		    	
		    	}else{		    			    	
		    		Map<String,String> productBaseCodeToSupplierNameMap = this.getMaintainCustomerCareCaseUco().getProductBaseCodeToSupplierNameMap(issue.getSupplierKcode());
		    		relatedProduct.add(productBaseCodeToSupplierNameMap);		    			    	
		    	}				
			}		   					  		    							
		}		
		model.addAttribute("totalPages",page.getTotalPages());
		model.addAttribute("currentPageIndex",page.getCurrentPageIndex());
		model.addAttribute("startPage",page.getStartPage());
		model.addAttribute("endPage",page.getEndPage());		
		model.addAttribute("issueList",issueList.getItems());
		model.addAttribute("categoryIdToNameMap",categoryIdToNameMap);
		model.addAttribute("typeIdToNameMap",typeIdToNameMap);	
		model.addAttribute("supplierKcodeToShortEnUsNameMap", supplierKcodeToShortEnUsNameMap);
		model.addAttribute("dateTimeNowUtc",this.getMaintainCustomerCareCaseIssueUco().getDateTimeNowUtc());
		model.addAttribute("type","list");
		model.addAttribute("categoryName","categoryName");
		model.addAttribute("categoryType","categoryType");
		model.addAttribute("relatedProduct", relatedProduct);		
		return "th/customerCareCase/ListOfIssues";
	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('ISSUES_VIEW'))")
	@RequestMapping(value = "/IssuesV4")
	public String listOfIssueV4(@RequestParam(value="page",defaultValue="1",required=false) Integer pageIndex,Model model){
		Map<Integer,String> categoryIdToNameMap = this.getMaintainCustomerCareCaseIssueUco().getCategoryIdToNameMap();
		Map<Integer,String> typeIdToNameMap = this.getMaintainCustomerCareCaseIssueUco().getTypeIdToNameMap();
		DtoList<CustomerCareCaseIssue> issueList =
				this.getMaintainCustomerCareCaseIssueUco().getListElastic("",pageIndex);
		Pager page = issueList.getPager();
		Map<String,String> supplierKcodeToShortEnUsNameMap = this.getMaintainCustomerCareCaseUco().getSupplierKcodeToShortEnUsNameMap();
		List<Map<String,String>> relatedProduct = new ArrayList<Map<String,String>>();
		for(CustomerCareCaseIssue issue: issueList.getItems()){
			if(issue.getSupplierKcode() != null){
				if(issue.getRelatedProductBaseCodeList() == null){
					Map<String,String> productSkuCodeToSupplierNameMap = this.getMaintainCustomerCareCaseUco().getProductSkuCodeToSupplierNameMap(issue.getSupplierKcode());
					relatedProduct.add(productSkuCodeToSupplierNameMap);
				}else{
					Map<String,String> productBaseCodeToSupplierNameMap = this.getMaintainCustomerCareCaseUco().getProductBaseCodeToSupplierNameMap(issue.getSupplierKcode());
					relatedProduct.add(productBaseCodeToSupplierNameMap);
				}
			}
		}
		model.addAttribute("totalPages",page.getTotalPages());
		model.addAttribute("currentPageIndex",page.getCurrentPageIndex());
		model.addAttribute("startPage",page.getStartPage());
		model.addAttribute("endPage",page.getEndPage());
		model.addAttribute("issueList",issueList.getItems());
		model.addAttribute("categoryIdToNameMap",categoryIdToNameMap);
		model.addAttribute("typeIdToNameMap",typeIdToNameMap);
		model.addAttribute("supplierKcodeToShortEnUsNameMap", supplierKcodeToShortEnUsNameMap);
		model.addAttribute("dateTimeNowUtc",this.getMaintainCustomerCareCaseIssueUco().getDateTimeNowUtc());
		model.addAttribute("type","list");
		model.addAttribute("categoryName","categoryName");
		model.addAttribute("categoryType","categoryType");
		model.addAttribute("relatedProduct", relatedProduct);
		return "ListOfIssuesV4";
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('ISSUES_VIEW'))")
	@RequestMapping(value = "/Issues/search")	
	public String searchIssues(@RequestParam(value="searchWords") String searchWords,
			@RequestParam(value="page",defaultValue="1",required=false) Integer pageIndex,
			Model model) throws UnsupportedEncodingException{
		Map<Integer,String> categoryIdToNameMap = this.getMaintainCustomerCareCaseUco().getIssueCategoryIdToNameMap();
		Map<Integer,String> typeIdToNameMap = this.getMaintainCustomerCareCaseIssueUco().getTypeIdToNameMap();
		DtoList<CustomerCareCaseIssue> issueList = 
				this.getMaintainCustomerCareCaseIssueUco().getListElastic(searchWords,pageIndex);
		Pager page = issueList.getPager();		
		Map<String,String> supplierKcodeToShortEnUsNameMap = this.getMaintainCustomerCareCaseUco().getSupplierKcodeToShortEnUsNameMap();			
		List<Map<String,String>> relatedProduct = new ArrayList<Map<String,String>>();		
		for(CustomerCareCaseIssue issue: issueList.getItems()){			
			if(issue.getSupplierKcode() != null){
				if(issue.getRelatedProductBaseCodeList() == null){		    	
		    		Map<String,String> productSkuCodeToSupplierNameMap = this.getMaintainCustomerCareCaseUco().getProductSkuCodeToSupplierNameMap(issue.getSupplierKcode());						    	
		    		relatedProduct.add(productSkuCodeToSupplierNameMap);		    	    			    	
		    	}else{		    		
		    		Map<String,String> productBaseCodeToSupplierNameMap = this.getMaintainCustomerCareCaseUco().getProductBaseCodeToSupplierNameMap(issue.getSupplierKcode());
		    		relatedProduct.add(productBaseCodeToSupplierNameMap);		    			    			    	
		    	}
			}		   					  		    							
		}
		model.addAttribute("totalPages",page.getTotalPages());
		model.addAttribute("currentPageIndex",page.getCurrentPageIndex());
		model.addAttribute("startPage",page.getStartPage());
		model.addAttribute("endPage",page.getEndPage());				
		model.addAttribute("issueList",issueList.getItems());		
		model.addAttribute("categoryIdToNameMap",categoryIdToNameMap);
		model.addAttribute("typeIdToNameMap",typeIdToNameMap);
		model.addAttribute("relatedProduct", relatedProduct);
		model.addAttribute("supplierKcodeToShortEnUsNameMap", supplierKcodeToShortEnUsNameMap);		
		model.addAttribute("dateTimeNowUtc",this.getMaintainCustomerCareCaseIssueUco().getDateTimeNowUtc());
		model.addAttribute("type","search");
		model.addAttribute("searchWords", searchWords);
		return "th/customerCareCase/ListOfIssues";
	}
		
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('ISSUES_VIEW'))")
	@RequestMapping(value = "/Issues/{issueId}")
	public String showIssue(@ModelAttribute("Comment") IssueCommentImpl comment,@PathVariable int issueId,Model model){										
		CustomerCareCaseIssue issue = this.getMaintainCustomerCareCaseIssueUco().getIssue(issueId);
		Map<String,String> supplierKcodeToShortEnUsNameMap = this.getMaintainCustomerCareCaseUco().getSupplierKcodeToShortEnUsNameMap();
		List<Marketplace> marketplaceList = this.getMaintainCustomerCareCaseIssueTemplateUco().getMarketplaceList();
		Map<Integer,String> categoryIdToNameMap = this.getMaintainCustomerCareCaseIssueUco().getCategoryIdToNameMap();
		Map<Integer,String> typeIdToNameMap = this.getMaintainCustomerCareCaseIssueUco().getTypeIdToNameMap();				
		if(issue.getRelatedProductSkuCodeList() == null){		
			if(issue.getSupplierKcode() !=null){
				Map<String,String> productBaseCodeToSupplierNameMap = this.getMaintainCustomerCareCaseUco().getProductBaseCodeToSupplierNameMap(issue.getSupplierKcode());
				model.addAttribute("productBaseCodeToSupplierNameMap", productBaseCodeToSupplierNameMap);
			}			
		}		
		if(issue.getRelatedProductBaseCodeList() == null){
			if(issue.getSupplierKcode() !=null){
				Map<String,String> productSkuCodeToSupplierNameMap = this.getMaintainCustomerCareCaseUco().getProductSkuCodeToSupplierNameMap(issue.getSupplierKcode());
				model.addAttribute("productSkuCodeToSupplierNameMap", productSkuCodeToSupplierNameMap);
			}								
		}				
		model.addAttribute("issue", issue);
		model.addAttribute("supplierKcodeToShortEnUsNameMap", supplierKcodeToShortEnUsNameMap);				
		model.addAttribute("marketplaceList",marketplaceList);
		model.addAttribute("categoryIdToNameMap",categoryIdToNameMap);
		model.addAttribute("typeIdToNameMap",typeIdToNameMap);
		return "th/customerCareCase/IssueView";
	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('ISSUES_VIEW'))")
	@RequestMapping(value = "/IssuesV4/{issueId}")
	public String showIssueV4(@ModelAttribute("Comment") IssueCommentImpl comment,@PathVariable int issueId,Model model){
		CustomerCareCaseIssue issue = this.getMaintainCustomerCareCaseIssueUco().getIssue(issueId);
		Map<String,String> supplierKcodeToShortEnUsNameMap = this.getMaintainCustomerCareCaseUco().getSupplierKcodeToShortEnUsNameMap();
		List<Marketplace> marketplaceList = this.getMaintainCustomerCareCaseIssueTemplateUco().getMarketplaceList();
		Map<Integer,String> categoryIdToNameMap = this.getMaintainCustomerCareCaseIssueUco().getCategoryIdToNameMap();
		Map<Integer,String> typeIdToNameMap = this.getMaintainCustomerCareCaseIssueUco().getTypeIdToNameMap();
		if(issue.getRelatedProductSkuCodeList() == null){
			if(issue.getSupplierKcode() !=null){
				Map<String,String> productBaseCodeToSupplierNameMap = this.getMaintainCustomerCareCaseUco().getProductBaseCodeToSupplierNameMap(issue.getSupplierKcode());
				model.addAttribute("productBaseCodeToSupplierNameMap", productBaseCodeToSupplierNameMap);
			}
		}
		if(issue.getRelatedProductBaseCodeList() == null){
			if(issue.getSupplierKcode() !=null){
				Map<String,String> productSkuCodeToSupplierNameMap = this.getMaintainCustomerCareCaseUco().getProductSkuCodeToSupplierNameMap(issue.getSupplierKcode());
				model.addAttribute("productSkuCodeToSupplierNameMap", productSkuCodeToSupplierNameMap);
			}
		}
		model.addAttribute("issue", issue);
		model.addAttribute("supplierKcodeToShortEnUsNameMap", supplierKcodeToShortEnUsNameMap);
		model.addAttribute("marketplaceList",marketplaceList);
		model.addAttribute("categoryIdToNameMap",categoryIdToNameMap);
		model.addAttribute("typeIdToNameMap",typeIdToNameMap);
		return "IssueViewV4";
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('ISSUES_MAINTAIN'))")
	@RequestMapping(value = "/Issues/create", method = RequestMethod.GET)	
	public String createIssue(@ModelAttribute("Issue") IssueImpl Issue,Model model){				
		Map<String,String> supplierKcodeToShortEnUsNameMap = this.getMaintainCustomerCareCaseUco().getSupplierKcodeToShortEnUsNameMap();
		Map<Integer,String> categoryIdToNameMap = this.getMaintainCustomerCareCaseIssueUco().getCategoryIdToNameMap();
		List<String> issueStatusList = this.getMaintainCustomerCareCaseIssueUco().getIssueStatusList();
		List<Locale> localeCodeList = this.getMaintainCustomerCareCaseIssueUco().getLocaleCodeList(); 
//		model.addAttribute("IssueJson", JsonHelper.toJson(Issue));
		model.addAttribute("IssueJson", Issue);
		model.addAttribute("supplierKcodeToShortEnUsNameMap", supplierKcodeToShortEnUsNameMap);			
		model.addAttribute("categoryIdToNameMap",categoryIdToNameMap);
		model.addAttribute("issueStatusList",issueStatusList);
		model.addAttribute("localeCodeList",localeCodeList);
		model.addAttribute("type", "Create");
		return "th/customerCareCase/Issue";
	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('ISSUES_MAINTAIN'))")
	@RequestMapping(value = "/IssuesV4/create", method = RequestMethod.GET)
	public String createIssueV4(@ModelAttribute("Issue") IssueImpl Issue,Model model){
		Map<String,String> supplierKcodeToShortEnUsNameMap = this.getMaintainCustomerCareCaseUco().getSupplierKcodeToShortEnUsNameMap();
		Map<Integer,String> categoryIdToNameMap = this.getMaintainCustomerCareCaseIssueUco().getCategoryIdToNameMap();
		List<String> issueStatusList = this.getMaintainCustomerCareCaseIssueUco().getIssueStatusList();
		List<Locale> localeCodeList = this.getMaintainCustomerCareCaseIssueUco().getLocaleCodeList();
		model.addAttribute("IssueJson", JsonHelper.toJson(Issue));
		model.addAttribute("supplierKcodeToShortEnUsNameMap", supplierKcodeToShortEnUsNameMap);
		model.addAttribute("categoryIdToNameMap",categoryIdToNameMap);
		model.addAttribute("issueStatusList",issueStatusList);
		model.addAttribute("localeCodeList",localeCodeList);
		model.addAttribute("type", "Create");
		return "IssueV4";
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('ISSUES_MAINTAIN'))")
	@RequestMapping(value = "/Issues/save")
	public String saveIssue(@ModelAttribute("Issue") IssueImpl Issue,Model model){		
		Assert.isTrue(Issue.getLanguage().size()==Issue.getIssueName().size());
		List<String> localeCodeList = Issue.getLanguage();
		List<String> issueNameList = Issue.getIssueName();
		Map<Locale,String> languageToNameMap = new HashMap<Locale,String>();
		for (int i=0; i<issueNameList.size(); i++) {
			Locale locale = Locale.fromCode(localeCodeList.get(i));
			languageToNameMap.put(locale, issueNameList.get(i));		
        }				
		Issue.setLocaleCodeToNameMap(languageToNameMap);
		//TODO:move to web DTO
		if(Issue.getCategoryId()!=null)
			if(Issue.getCategoryId() == 0 )Issue.setCategoryId(null);
		if(Issue.getTypeId()!=null)
			if(Issue.getTypeId() == 0)Issue.setTypeId(null);
		if(Issue.getRelatedProductBaseCodeList() !=null)
			if(Issue.getRelatedProductBaseCodeList().isEmpty())Issue.setRelatedProductBaseCodeList(null);	
		if(Issue.getRelatedProductSkuCodeList() !=null)
			if(Issue.getRelatedProductSkuCodeList().isEmpty())Issue.setRelatedProductSkuCodeList(null);			
		Integer issueId = this.getMaintainCustomerCareCaseIssueUco().createIssue(Issue);

		//todo move above code to backend
		drsCmdBus.tell(new SaveCustomercareCaseIssue(issueId) , ActorRef.noSender());

		return "redirect:/Issues/"+issueId;		
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('ISSUES_MAINTAIN'))")
	@RequestMapping(value = "/Issues/{issueId}/edit")	
	public String editIssue(Model model,@PathVariable int issueId){										
		Map<String,String> supplierKcodeToShortEnUsNameMap = this.getMaintainCustomerCareCaseUco().getSupplierKcodeToShortEnUsNameMap();			
		Map<Integer,String> categoryIdToNameMap = this.getMaintainCustomerCareCaseIssueUco().getCategoryIdToNameMap();
		List<String> issueStatusList = this.getMaintainCustomerCareCaseIssueUco().getIssueStatusList();
		List<Locale> localeCodeList = this.getMaintainCustomerCareCaseIssueUco().getLocaleCodeList();
		CustomerCareCaseIssue issue = this.getMaintainCustomerCareCaseIssueUco().getIssue(issueId);		
		IssueImpl issueImpl = new IssueImpl();		
		issueImpl.setId(issue.getId());
		issueImpl.setCategoryId(issue.getCategoryId());
		issueImpl.setTypeId(issue.getTypeId());
		List<String> language = new ArrayList<String>();			
		List<String> issueName = new ArrayList<String>();							
		for (Entry<Locale,String> entry : issue.getLocaleCodeToNameMap().entrySet()){
			language.add(entry.getKey().getCode());
			issueName.add(entry.getValue());
		}		
		issueImpl.setLanguage(language);
		issueImpl.setIssueName(issueName);						
		issueImpl.setStatus(issue.getStatus());
		issueImpl.setSupplierKcode(issue.getSupplierKcode());
		issueImpl.setRelatedProductBaseCodeList(issue.getRelatedProductBaseCodeList());
		issueImpl.setRelatedProductSkuCodeList(issue.getRelatedProductSkuCodeList());	
		issueImpl.setTemplateOccurrences(issue.getTemplateOccurrences());
		issueImpl.setCreatedDate(issue.getCreatedDate());
		System.out.println(issueImpl);

		ObjectMapper om  = new ObjectMapper();
		try {
			String j= om.writeValueAsString(issueImpl);
			System.out.println(j);
			model.addAttribute("IssueJson", j);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		model.addAttribute("Issue", issueImpl);
		//model.addAttribute("IssueJson",JsonHelper.toJson(issueImpl));
		model.addAttribute("supplierKcodeToShortEnUsNameMap", supplierKcodeToShortEnUsNameMap);						
		model.addAttribute("categoryIdToNameMap",categoryIdToNameMap);
		model.addAttribute("issueStatusList",issueStatusList);
		model.addAttribute("localeCodeList",localeCodeList);
		model.addAttribute("type", "Edit");
		return "th/customerCareCase/Issue";
	}


	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('ISSUES_MAINTAIN'))")
	@RequestMapping(value = "/IssuesV4/{issueId}/edit")
	public String editIssueV4(Model model,@PathVariable int issueId){
		Map<String,String> supplierKcodeToShortEnUsNameMap = this.getMaintainCustomerCareCaseUco().getSupplierKcodeToShortEnUsNameMap();
		Map<Integer,String> categoryIdToNameMap = this.getMaintainCustomerCareCaseIssueUco().getCategoryIdToNameMap();
		List<String> issueStatusList = this.getMaintainCustomerCareCaseIssueUco().getIssueStatusList();
		List<Locale> localeCodeList = this.getMaintainCustomerCareCaseIssueUco().getLocaleCodeList();
		CustomerCareCaseIssue issue = this.getMaintainCustomerCareCaseIssueUco().getIssue(issueId);
		IssueImpl issueImpl = new IssueImpl();
		issueImpl.setId(issue.getId());
		issueImpl.setCategoryId(issue.getCategoryId());
		issueImpl.setTypeId(issue.getTypeId());
		List<String> language = new ArrayList<String>();
		List<String> issueName = new ArrayList<String>();
		for (Entry<Locale,String> entry : issue.getLocaleCodeToNameMap().entrySet()){
			language.add(entry.getKey().getCode());
			issueName.add(entry.getValue());
		}
		issueImpl.setLanguage(language);
		issueImpl.setIssueName(issueName);
		issueImpl.setStatus(issue.getStatus());
		issueImpl.setSupplierKcode(issue.getSupplierKcode());
		issueImpl.setRelatedProductBaseCodeList(issue.getRelatedProductBaseCodeList());
		issueImpl.setRelatedProductSkuCodeList(issue.getRelatedProductSkuCodeList());
		issueImpl.setTemplateOccurrences(issue.getTemplateOccurrences());
		issueImpl.setCreatedDate(issue.getCreatedDate());
		model.addAttribute("Issue", issueImpl);
		model.addAttribute("IssueJson",JsonHelper.toJson(issueImpl));
		model.addAttribute("supplierKcodeToShortEnUsNameMap", supplierKcodeToShortEnUsNameMap);
		model.addAttribute("categoryIdToNameMap",categoryIdToNameMap);
		model.addAttribute("issueStatusList",issueStatusList);
		model.addAttribute("localeCodeList",localeCodeList);
		model.addAttribute("type", "Edit");
		return "IssueV4";
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('ISSUES_MAINTAIN'))")
	@RequestMapping(value = "/Issues/update")
	public String updateIssue(@ModelAttribute("Issue") IssueImpl Issue,Model model){		
		Assert.isTrue(Issue.getLanguage().size()==Issue.getIssueName().size());
		List<String> localeCodeList = Issue.getLanguage();
		List<String> issueNameList = Issue.getIssueName();
		Map<Locale,String> languageToNameMap = new HashMap<Locale,String>();		
		for (int i=0; i<issueNameList.size(); i++) {
			Locale locale = Locale.fromCode(localeCodeList.get(i));
			languageToNameMap.put(locale,issueNameList.get(i));
        }		
		Issue.setLocaleCodeToNameMap(languageToNameMap);
		if(Issue.getRelatedProductBaseCodeList() != null)
			if(Issue.getRelatedProductBaseCodeList().isEmpty())Issue.setRelatedProductBaseCodeList(null);
		if(Issue.getRelatedProductSkuCodeList()!=null)
				if(Issue.getRelatedProductSkuCodeList().isEmpty())Issue.setRelatedProductSkuCodeList(null);		
		
		CustomerCareCaseIssue issue = this.getMaintainCustomerCareCaseIssueUco().update(Issue);

		//todo move below code to backend
		drsCmdBus.tell(new UpdateCustomercareCaseIssue(Issue.getId()) , ActorRef.noSender());

		return "redirect:/Issues/"+issue.getId();				
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('ISSUES_POST_COMMENT'))")	
	@RequestMapping(value = "/Issues/postComment",method = RequestMethod.POST)
	public String postComment(@ModelAttribute("Comment") IssueCommentImpl comment,@RequestParam("issueId") int issueId,Model model){				
		this.getMaintainCustomerCareCaseIssueUco().addComment(issueId,comment.getPendingSupplierAction() ,comment);				
		return "redirect:/Issues/"+issueId;	
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('ISSUES_MAINTAIN'))")
	@RequestMapping(value = "/Issues/{issueId}/delete")
	public String deleteIssue(@PathVariable int issueId,Model model,final RedirectAttributes redirectAttributes,java.util.Locale locale){

		this.getMaintainCustomerCareCaseIssueUco().deleteIssue(issueId);

		//todo move code to backend
		drsCmdBus.tell(new DeleteCustomercareCaseIssue(issueId) , ActorRef.noSender());

		MessageSource source = (MessageSource)SpringAppCtx.get().getBean("messageSource");
		String message = source.getMessage("issue.deleteIssue", null, locale);
		redirectAttributes.addFlashAttribute("message",message);				
		return "redirect:/Issues";		
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('ISSUES_ISSUE_TYPE'))")	
	@RequestMapping(value = "/Issues/IssueTypes")
	public String ListOfIssueType(Model model){		
		Map<Integer,Map<Integer,String>> IssueCategoryToIssueTypeAndIdMap = this.getMaintainCustomerCareCaseIssueUco().getCategoryIdToIssueTypeAndIdMap();	
		Map<Integer,String> categoryIdToNameMap = this.getMaintainCustomerCareCaseIssueUco().getCategoryIdToNameMap();		
		model.addAttribute("IssueCategoryToIssueTypeAndIdMap", IssueCategoryToIssueTypeAndIdMap);
		model.addAttribute("categoryIdToNameMap",categoryIdToNameMap);		
		return "th/customerCareCase/ListOfIssueTypes";
	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('ISSUES_ISSUE_TYPE'))")
	@RequestMapping(value = "/IssuesV4/IssueTypes")
	public String ListOfIssueTypeV4(Model model){
		Map<Integer,Map<Integer,String>> IssueCategoryToIssueTypeAndIdMap = this.getMaintainCustomerCareCaseIssueUco().getCategoryIdToIssueTypeAndIdMap();
		Map<Integer,String> categoryIdToNameMap = this.getMaintainCustomerCareCaseIssueUco().getCategoryIdToNameMap();
		model.addAttribute("IssueCategoryToIssueTypeAndIdMap", IssueCategoryToIssueTypeAndIdMap);
		model.addAttribute("categoryIdToNameMap",categoryIdToNameMap);
		return "ListOfIssueTypeV4";
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('ISSUES_ISSUE_TYPE'))")	
	@RequestMapping(value = "/Issues/createIssueType", method = RequestMethod.GET)		
	public String createIssueType(@ModelAttribute("IssueType") IssueType issueType,Model model){		
		Map<Integer,String> categoryIdToNameMap = this.getMaintainCustomerCareCaseIssueUco().getCategoryIdToNameMap();		
		model.addAttribute("categoryIdToNameMap",categoryIdToNameMap);
		model.addAttribute("type", "Create");		
		return "th/customerCareCase/IssueType";
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('ISSUES_ISSUE_TYPE'))")	
	@RequestMapping(value = "/Issues/saveIssueType")
	public String saveIssueType(@ModelAttribute("IssueType") IssueType issueType,Model model){							
		this.getMaintainCustomerCareCaseIssueUco().createIssueType(issueType.getCategoryId(), issueType.getName());		
		return "redirect:/Issues/IssueTypes";		
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('ISSUES_ISSUE_TYPE'))")	
	@RequestMapping(value = "/Issues/editIssueType")
	public String editIssueType(@RequestParam("categoryId") int categoryId,@RequestParam("typeId") int typeId,Model model){		
		Map<Integer,Map<Integer,String>> IssueCategoryToIssueTypeAndIdMap = this.getMaintainCustomerCareCaseIssueUco().getCategoryIdToIssueTypeAndIdMap();			
		Map<Integer,String> IssueCategoryToIssueTypeAndId = IssueCategoryToIssueTypeAndIdMap.get(categoryId);		
		String name = IssueCategoryToIssueTypeAndId.get(typeId);	
		Map<Integer,String> categoryIdToNameMap = this.getMaintainCustomerCareCaseIssueUco().getCategoryIdToNameMap();
		IssueType issueType = new IssueType();
		issueType.setCategoryId(categoryId);
		issueType.setTypeId(typeId);
		issueType.setName(name);				
		model.addAttribute("categoryIdToNameMap",categoryIdToNameMap);		
		model.addAttribute("IssueType",issueType);		
		model.addAttribute("name",name);					
		model.addAttribute("type", "Edit");		
		return "th/customerCareCase/IssueType";
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('ISSUES_ISSUE_TYPE'))")
	@RequestMapping(value = "/Issues/updateIssueType")
	public String updateIssueType(@ModelAttribute("IssueType") IssueType issueType,Model model){		
		this.getMaintainCustomerCareCaseIssueUco().updateType(issueType.getTypeId(), issueType.getName());		
		return "redirect:/Issues/IssueTypes";		
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('ISSUES_ISSUE_TYPE'))")	
	@RequestMapping(value = "/Issues/deleteIssueType")
	public String deleteIssueType(Model model,final RedirectAttributes redirectAttributes,java.util.Locale locale){						
		MessageSource source = (MessageSource)SpringAppCtx.get().getBean("messageSource");
		String message = source.getMessage("issue.deleteIssueType", null, locale);
		redirectAttributes.addFlashAttribute("message",message);
		return "redirect:/Issues/IssueTypes";		
	}
		
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('TEMPLATE_MAINTAIN'))")	
	@RequestMapping(value = "/Issues/{issueId}/createResponseTemplate")
	public String createResponseTemplate(@ModelAttribute("Template") CustomerCareCaseTemplateImpl Template,@PathVariable int issueId,Model model){													
		List<Locale> localeCodeList = this.getMaintainCustomerCareCaseIssueTemplateUco().getLocaleCodeList();
		List<String> caseTypeList = this.getMaintainCustomerCareCaseIssueTemplateUco().getCaseTypeList();
		List<String> marketRegionList = this.getMaintainCustomerCareCaseIssueTemplateUco().getMarketRegionList();
		List<Marketplace> marketplaceList = this.getMaintainCustomerCareCaseIssueTemplateUco().getMarketplaceList();								
		Template.setIssueName(this.getMaintainCustomerCareCaseIssueUco().getEnUsIssueName(issueId));
		Template.setIssueId(issueId);								
		model.addAttribute("localeCodeList",localeCodeList);
		model.addAttribute("caseTypeList",caseTypeList);
		model.addAttribute("marketRegionList",marketRegionList);
		model.addAttribute("marketplaceList",marketplaceList);
		model.addAttribute("type", "Create");		
		return "th/customerCareCase/Template";
	}


	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('TEMPLATE_MAINTAIN'))")
	@RequestMapping(value = "/IssuesV4/{issueId}/createResponseTemplate")
	public String createResponseTemplateV4(@ModelAttribute("Template") CustomerCareCaseTemplateImpl Template,@PathVariable int issueId,Model model){
		List<Locale> localeCodeList = this.getMaintainCustomerCareCaseIssueTemplateUco().getLocaleCodeList();
		List<String> caseTypeList = this.getMaintainCustomerCareCaseIssueTemplateUco().getCaseTypeList();
		List<String> marketRegionList = this.getMaintainCustomerCareCaseIssueTemplateUco().getMarketRegionList();
		List<Marketplace> marketplaceList = this.getMaintainCustomerCareCaseIssueTemplateUco().getMarketplaceList();
		Template.setIssueName(this.getMaintainCustomerCareCaseIssueUco().getEnUsIssueName(issueId));
		Template.setIssueId(issueId);
		model.addAttribute("localeCodeList",localeCodeList);
		model.addAttribute("caseTypeList",caseTypeList);
		model.addAttribute("marketRegionList",marketRegionList);
		model.addAttribute("marketplaceList",marketplaceList);
		model.addAttribute("type", "Create");
		return "TemplateV4";
	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('TEMPLATE_MAINTAIN'))")	
	@RequestMapping(value = "/Issues/saveResponseTemplate")
	public String saveResponseTemplate(@ModelAttribute("Template") CustomerCareCaseTemplateImpl Template,Model model){						
		int templateId = this.getMaintainCustomerCareCaseIssueTemplateUco().create(Template);			
		return "redirect:/Template/"+templateId;		
	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('TEMPLATE_VIEW'))")		
	@RequestMapping(value = "/Template/{templateId}")	
	public String showResponseTemplate(@PathVariable int templateId,Model model){				
		CustomerCareCaseIssueTemplate customerCareCaseIssueTemplate = this.getMaintainCustomerCareCaseIssueTemplateUco().get(templateId);
		List<Marketplace> marketplaceList = this.getMaintainCustomerCareCaseIssueTemplateUco().getMarketplaceList();		
		model.addAttribute("customerCareCaseIssueTemplate",customerCareCaseIssueTemplate);
		model.addAttribute("marketplaceList",marketplaceList);		
		return "th/customerCareCase/TemplateView";
	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('TEMPLATE_VIEW'))")
	@RequestMapping(value = "/TemplateV4/{templateId}")
	public String showResponseTemplateV4(@PathVariable int templateId,Model model){
		CustomerCareCaseIssueTemplate customerCareCaseIssueTemplate = this.getMaintainCustomerCareCaseIssueTemplateUco().get(templateId);
		List<Marketplace> marketplaceList = this.getMaintainCustomerCareCaseIssueTemplateUco().getMarketplaceList();
		model.addAttribute("customerCareCaseIssueTemplate",customerCareCaseIssueTemplate);
		model.addAttribute("marketplaceList",marketplaceList);
		return "TemplateViewV4";
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('TEMPLATE_MAINTAIN'))")		
	@RequestMapping(value = "/Template/{templateId}/edit")		
	public String editResponseTemplate(@PathVariable int templateId,Model model){							
		List<Locale> localeCodeList = this.getMaintainCustomerCareCaseIssueTemplateUco().getLocaleCodeList();
		List<String> caseTypeList = this.getMaintainCustomerCareCaseIssueTemplateUco().getCaseTypeList();
		List<String> marketRegionList = this.getMaintainCustomerCareCaseIssueTemplateUco().getMarketRegionList();
		List<Marketplace> marketplaceList = this.getMaintainCustomerCareCaseIssueTemplateUco().getMarketplaceList();		
		CustomerCareCaseIssueTemplate Template = this.getMaintainCustomerCareCaseIssueTemplateUco().get(templateId);														
		model.addAttribute("localeCodeList",localeCodeList);
		model.addAttribute("caseTypeList",caseTypeList);
		model.addAttribute("marketRegionList",marketRegionList);
		model.addAttribute("marketplaceList",marketplaceList);		
		model.addAttribute("type", "Edit");
		model.addAttribute("Template", Template);				
		return "th/customerCareCase/Template";
	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('TEMPLATE_MAINTAIN'))")
	@RequestMapping(value = "/TemplateV4/{templateId}/edit")
	public String editResponseTemplateV4(@PathVariable int templateId,Model model){
		List<Locale> localeCodeList = this.getMaintainCustomerCareCaseIssueTemplateUco().getLocaleCodeList();
		List<String> caseTypeList = this.getMaintainCustomerCareCaseIssueTemplateUco().getCaseTypeList();
		List<String> marketRegionList = this.getMaintainCustomerCareCaseIssueTemplateUco().getMarketRegionList();
		List<Marketplace> marketplaceList = this.getMaintainCustomerCareCaseIssueTemplateUco().getMarketplaceList();
		CustomerCareCaseIssueTemplate Template = this.getMaintainCustomerCareCaseIssueTemplateUco().get(templateId);
		model.addAttribute("localeCodeList",localeCodeList);
		model.addAttribute("caseTypeList",caseTypeList);
		model.addAttribute("marketRegionList",marketRegionList);
		model.addAttribute("marketplaceList",marketplaceList);
		model.addAttribute("type", "Edit");
		model.addAttribute("Template", Template);
		return "TemplateV4";
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('TEMPLATE_MAINTAIN'))")	
	@RequestMapping(value = "/Issues/updateResponseTemplate")		
	public String updateResponseTemplate(@ModelAttribute("Template") CustomerCareCaseTemplateImpl Template,Model model){				
		CustomerCareCaseIssueTemplate template = this.getMaintainCustomerCareCaseIssueTemplateUco().update(Template);				
		return "redirect:/Template/"+template.getId();		
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('TEMPLATE_MAINTAIN'))")		
	@RequestMapping(value = "/Issues/{issueId}/{templateId}/delete")			
	public String deleteResponseTemplate(Model model,@PathVariable int issueId,@PathVariable int templateId,final RedirectAttributes redirectAttributes,java.util.Locale locale){	
		this.getMaintainCustomerCareCaseIssueTemplateUco().delete(templateId);
		MessageSource source = (MessageSource)SpringAppCtx.get().getBean("messageSource");
		String message = source.getMessage("template.deleteTemplate", null, locale);
		redirectAttributes.addFlashAttribute("message",message);				
		return "redirect:/Issues/"+issueId;		
	}
	
	@RequestMapping(value = "/CustomerCareCases/getProductBaseCodeToSupplierNameMapInIssue", method = RequestMethod.GET)			
	public @ResponseBody String getProductBaseCodeToSupplierNameMapInIssue(@RequestParam("supplierKcode") String supplierKcode){				
		Map<String,String> productBaseCodeToSupplierNameMap = this.getMaintainCustomerCareCaseUco().getProductBaseCodeToSupplierNameMap(supplierKcode);
		return JsonHelper.toJson(productBaseCodeToSupplierNameMap);	
	}
	
	@RequestMapping(value = "/CustomerCareCases/getProductSkuCodeToSupplierNameMapInIssue", method = RequestMethod.GET)			
	public @ResponseBody String getProductSkuCodeToSupplierNameMapInIssue(@RequestParam("supplierKcode") String supplierKcode){		
		Map<String,String> productSkuCodeToSupplierNameMap = this.getMaintainCustomerCareCaseUco().getProductSkuCodeToSupplierNameMap(supplierKcode);		
		return JsonHelper.toJson(productSkuCodeToSupplierNameMap);			
	}
	
	@RequestMapping(value = "/CustomerCareCases/getProductBaseCodeToSupplierNameMap", method = RequestMethod.GET)				
	public @ResponseBody String getProductBaseCodeToSupplierNameMap(@RequestParam("supplierKcode") String supplierKcode){
		Map<String,String> productBaseCodeToSupplierNameMap = this.getMaintainCustomerCareCaseUco().getProductBaseCodeToSupplierNameMap(supplierKcode);
		return JsonHelper.toJson(productBaseCodeToSupplierNameMap);	
	}
	
	@RequestMapping(value = "/CustomerCareCases/getProductSkuCodeToSupplierNameMap", method = RequestMethod.GET , produces = "application/json; charset=utf-8")				
	public @ResponseBody String getProductSkuCodeToSupplierNameMap(@RequestParam("supplierKcode") String supplierKcode){
		Map<String,String> productSkuCodeToSupplierNameMap = this.getMaintainCustomerCareCaseUco().getProductSkuCodeToSupplierNameMap(supplierKcode);

		return JsonHelper.toJson(productSkuCodeToSupplierNameMap);		
	}
	
	@RequestMapping(value = "/CustomerCareCases/geOrderInfo", method = RequestMethod.GET)
	public @ResponseBody String getOrderInfo(@RequestParam("orderId") String orderId){				
		CustomerCareCaseOrderInfo orderInfo	= this.getMaintainCustomerCareCaseUco().getOrderDateById(orderId);		
		return JsonHelper.toJson(orderInfo);		
	}
	
	@RequestMapping(value = "/CustomerCareCases/getIssueTypeList", method = RequestMethod.GET)
	public @ResponseBody String getIssueTypeListForCustomerCareCases(@RequestParam("categoryId") Integer categoryId){
		if(categoryId == 0) return JsonHelper.toJson(null);
		Map<Integer,String> issueTypeList = this.getMaintainCustomerCareCaseUco().getTypeIdToNameMap(categoryId);		
		return JsonHelper.toJson(issueTypeList);				
	}
	
	@RequestMapping(value = "/CustomerCareCases/getIssues", method = RequestMethod.GET)	
	public @ResponseBody String getIssues(@RequestParam("categoryId") Integer categoryId,@RequestParam("typeId") Integer typeId){				
		if(categoryId == 0)categoryId = null;											
		if(typeId == 0)typeId = null;		
		Map<Integer,String> IssueIdToEnUsNameMap = this.getMaintainCustomerCareCaseUco().getIssueIdToEnUsNameMap(categoryId,typeId);	
		return JsonHelper.toJson(IssueIdToEnUsNameMap);		
	} 
	
	@RequestMapping(value = "/CustomerCareCases/getTemplates", method = RequestMethod.GET)		
	public @ResponseBody String getTemplates(@RequestParam("issueId") int issueId){				
		Map<Integer,String> templates = this.getMaintainCustomerCareCaseUco().getTemplates(issueId);				
		return JsonHelper.toJson(templates);		
	}
	
	@RequestMapping(value = "/CustomerCareCases/getTemplateContentForCustomerCareCases", method = RequestMethod.GET)			
	public @ResponseBody String getTemplateContentForCustomerCareCases(@RequestParam("templateId") int templateId){			
		String templateContent = this.getMaintainCustomerCareCaseUco().getTemplateContent(templateId);
		return JsonHelper.toJson(templateContent);		
	}
		
	@RequestMapping(value = "/Issues/getIssueTypeList", method = RequestMethod.GET)
	public @ResponseBody String getIssueTypeList(@RequestParam("categoryId") Integer categoryId){

		Map<Integer,String> issueTypeList = this.getMaintainCustomerCareCaseIssueUco().getTypeIdToNameMap(categoryId);
		return JsonHelper.toJson(issueTypeList);		
	}
	
	@RequestMapping(value = "/CustomerCareCases/getIssueTypeToIssuesMap", method = RequestMethod.GET ,produces = "application/json; charset=utf-8")
	public @ResponseBody String getIssueTypeToIssuesMap(@RequestParam(value="baseList" , required = false) List<String> baseList,
			@RequestParam(value="skuList",required = false) List<String> skuList,@RequestParam("category") Integer category){								
	
		Map<String,Map<Integer,String>> issueTypeToIssuesMap = null;
		if((skuList != null|| baseList != null)) {
			if(skuList != null) if(skuList.isEmpty())skuList=null;
			if(baseList != null) if(baseList.isEmpty())baseList=null;
			if(category != null) if(category == 0)category = null;		
			issueTypeToIssuesMap = this.getMaintainCustomerCareCaseIssueUco().getIssueTypeToIssuesMap(baseList,skuList,category);
		}
	
		
		return JsonHelper.toJson(issueTypeToIssuesMap);	
	}
	
	
	
	@RequestMapping(value = "/CustomerCareCases/getTemplateContent", method = RequestMethod.GET)
	public @ResponseBody String getTemplateContent(@RequestParam("templateId") int templateId){				
		String templateContent = this.getMaintainCustomerCareCaseUco().getTemplateContent(templateId);				
		return JsonHelper.toJson(templateContent);		
	}
	
	private Map<Integer,String> generateMarketplaceMap(List<Marketplace> marketplaceList){
		Map<Integer,String> marketplaceMap = new HashMap<Integer,String>();
		for(Marketplace marketplace:marketplaceList){
			marketplaceMap.put(marketplace.getKey(), marketplace.toString());		
		}
		return marketplaceMap;			
	}
					
}