package com.kindminds.drs.web.ctrl.sales;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kindminds.drs.Context;
import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.usecase.sales.ListCustomerOrderUco;
import com.kindminds.drs.api.v1.model.sales.CustomerOrder;
import com.kindminds.drs.api.v1.model.sales.CustomerOrderExporting;
import com.kindminds.drs.api.v1.model.common.DtoList;
import com.kindminds.drs.api.v1.model.common.Pager;
import com.kindminds.drs.service.util.SpringAppCtx;
import com.kindminds.drs.util.JsonHelper;
import com.kindminds.drs.web.data.dto.ListCustomerOrderConditionImpl;

@Controller
@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('CUSTOMER_ORDER'))")
@RequestMapping(value = "/CustomerOrderList")
public class CustomerOrderController {
		
	@Autowired private ListCustomerOrderUco listCustomerOrderUco;

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	@SuppressWarnings("serial")
	private final Map<String,String> marketplaceToOrderUrlPrefixMap = new HashMap<String,String>()
	{{
		put(Marketplace.AMAZON_COM.getName(),"https://sellercentral.amazon.com/gp/orders-v2/details/ref=ag_orddet_cont_myo?ie=UTF8&orderID=");
		put(Marketplace.TRUETOSOURCE.getName(), "https://true-to-source.myshopify.com/admin/orders/");		
		put(Marketplace.AMAZON_CO_UK.getName(), "https://sellercentral.amazon.co.uk/hz/orders/details?_encoding=UTF8&orderId=");
		put(Marketplace.AMAZON_CA.getName(), "https://sellercentral.amazon.ca/hz/orders/details?_encoding=UTF8&orderId=");
		put(Marketplace.AMAZON_DE.getName(), "https://sellercentral.amazon.de/hz/orders/details?_encoding=UTF8&orderId=");	
		put(Marketplace.AMAZON_IT.getName(), "https://sellercentral.amazon.it/hz/orders/details?_encoding=UTF8&orderId=");
		put(Marketplace.AMAZON_FR.getName(), "https://sellercentral.amazon.fr/hz/orders/details?_encoding=UTF8&orderId=");
		put(Marketplace.AMAZON_ES.getName(), "https://sellercentral.amazon.es/hz/orders/details?_encoding=UTF8&orderId=");			
	}};
	
	
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String listOfCustomerOrders(
			@ModelAttribute("CustomerOrder") ListCustomerOrderConditionImpl listCustomerOrderCondition,
			Model model,
			@RequestParam(value="page",defaultValue="1",required=false) Integer pageIndex,
			Locale locale) {


		ObjectMapper om  = new ObjectMapper();
		try {
			String j= om.writeValueAsString(listCustomerOrderCondition);
			model.addAttribute("ListCustomerOrderCondition", j);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}


		if (listCustomerOrderCondition.getIsSearch() == null) {
			listCustomerOrderCondition.setIsSearch("true");
		}
		DtoList<CustomerOrder> customerOrders;
		if ("true".equals(listCustomerOrderCondition.getIsSearch())) {
			String orderStart = listCustomerOrderCondition.getOrderDateStart();
			String orderEnd = listCustomerOrderCondition.getOrderDateEnd();
			Long startTime = null, endTime = null;
			try {
				if (orderStart != null) {
					startTime = sdf.parse(orderStart).getTime() + 57600000L; //16 hours
					System.out.println("orderStart: " + orderStart);
					System.out.println("startTime: " + startTime);
				}
				if (orderEnd != null) {
					endTime = sdf.parse(orderEnd).getTime() + 144000000L; //40 hours
					System.out.println("orderEnd: " + orderEnd);
					System.out.println("endTime: " + endTime);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}

			customerOrders = listCustomerOrderUco.getListElastic(
					listCustomerOrderCondition.getSearchTerms(), pageIndex, startTime, endTime);
			listCustomerOrderCondition.makeFilterFieldsNull();
		} else {
			customerOrders = listCustomerOrderUco.getList(listCustomerOrderCondition, pageIndex);
		}
		Pager page = customerOrders.getPager();


		model.addAttribute("salesChannels",listCustomerOrderUco.getSalesChannels());
		model.addAttribute("orderStatus", listCustomerOrderCondition.getOrderStatus());
		model.addAttribute("supplierKcodeToNameMap",listCustomerOrderUco.getSupplierKcodeToNameMap());
		model.addAttribute("productBaseCodeToNameMap",listCustomerOrderUco.getProductBaseCodeToNameMap(Context.getCurrentUser().getCompanyKcode()));						
		model.addAttribute("customerOrders",customerOrders.getItems());
		model.addAttribute("startCountPerPage",this.getStartCountPerPage(page.getCurrentPageIndex(), customerOrders.getItems().size(), page.getPageSize()));
		model.addAttribute("endCountPerPage",this.getEndCountPerPage(page.getCurrentPageIndex(), customerOrders.getItems().size(), page.getPageSize(), customerOrders.getTotalRecords()));
		model.addAttribute("totalRecords",customerOrders.getTotalRecords());		
		model.addAttribute("totalPages",page.getTotalPages());
		model.addAttribute("currentPageIndex",page.getCurrentPageIndex());
		model.addAttribute("startPage",page.getStartPage());
		model.addAttribute("endPage",page.getEndPage());		
		//model.addAttribute("ListCustomerOrderCondition",JsonHelper.toJson(listCustomerOrderCondition));
		//model.addAttribute("ListCustomerOrderCondition", listCustomerOrderCondition);
		model.addAttribute("searchResultTitle",this.getSearchResultTitle(listCustomerOrderCondition, locale));
		model.addAttribute("customerOrderConditions",this.getCustomerOrderConditions(listCustomerOrderCondition));
		model.addAttribute("marketplaceToOrderUrlPrefixMap",this.marketplaceToOrderUrlPrefixMap);

		return "th/customerOrder/ListOfCustomerOrders";
		
	}

	@RequestMapping(value = "cv4", method = RequestMethod.GET)
	public String listOfCustomerOrdersV4(
			@ModelAttribute("CustomerOrder") ListCustomerOrderConditionImpl listCustomerOrderCondition,
			Model model,
			@RequestParam(value="page",defaultValue="1",required=false) Integer pageIndex,
			Locale locale) {


		if (listCustomerOrderCondition.getIsSearch() == null) {
			listCustomerOrderCondition.setIsSearch("true");
		}
		DtoList<CustomerOrder> customerOrders;
		if ("true".equals(listCustomerOrderCondition.getIsSearch())) {
			String orderStart = listCustomerOrderCondition.getOrderDateStart();
			String orderEnd = listCustomerOrderCondition.getOrderDateEnd();
			Long startTime = null, endTime = null;
			try {
				if (orderStart != null) {
					startTime = sdf.parse(orderStart).getTime() + 57600000L; //16 hours
					System.out.println("orderStart: " + orderStart);
					System.out.println("startTime: " + startTime);
				}
				if (orderEnd != null) {
					endTime = sdf.parse(orderEnd).getTime() + 144000000L; //40 hours
					System.out.println("orderEnd: " + orderEnd);
					System.out.println("endTime: " + endTime);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}

			customerOrders = listCustomerOrderUco.getListElastic(
					listCustomerOrderCondition.getSearchTerms(), pageIndex, startTime, endTime);
			listCustomerOrderCondition.makeFilterFieldsNull();
		} else {
			customerOrders = listCustomerOrderUco.getList(listCustomerOrderCondition, pageIndex);
		}
		Pager page = customerOrders.getPager();

		model.addAttribute("salesChannels",listCustomerOrderUco.getSalesChannels());
		model.addAttribute("orderStatus", listCustomerOrderCondition.getOrderStatus());
		model.addAttribute("supplierKcodeToNameMap",listCustomerOrderUco.getSupplierKcodeToNameMap());
		model.addAttribute("productBaseCodeToNameMap",listCustomerOrderUco.getProductBaseCodeToNameMap(Context.getCurrentUser().getCompanyKcode()));

		model.addAttribute("customerOrders",customerOrders.getItems());
		model.addAttribute("startCountPerPage",this.getStartCountPerPage(page.getCurrentPageIndex(), customerOrders.getItems().size(), page.getPageSize()));
		model.addAttribute("endCountPerPage",this.getEndCountPerPage(page.getCurrentPageIndex(), customerOrders.getItems().size(), page.getPageSize(), customerOrders.getTotalRecords()));
		model.addAttribute("totalRecords",customerOrders.getTotalRecords());
		model.addAttribute("totalPages",page.getTotalPages());
		model.addAttribute("currentPageIndex",page.getCurrentPageIndex());
		model.addAttribute("startPage",page.getStartPage());
		model.addAttribute("endPage",page.getEndPage());
		model.addAttribute("ListCustomerOrderCondition",JsonHelper.toJson(listCustomerOrderCondition));
		model.addAttribute("searchResultTitle",this.getSearchResultTitle(listCustomerOrderCondition, locale));
		model.addAttribute("customerOrderConditions",this.getCustomerOrderConditions(listCustomerOrderCondition));
		model.addAttribute("marketplaceToOrderUrlPrefixMap",this.marketplaceToOrderUrlPrefixMap);

		return "ListOfCustomerOrdersV4";

	}


	@RequestMapping(value = "export", method = RequestMethod.GET)
	public String exportCustomerOrders(@ModelAttribute("CustomerOrder") ListCustomerOrderConditionImpl ListCustomerOrderCondition,Model model){	
		List<CustomerOrderExporting> customerOrders = listCustomerOrderUco.getListForExporting(ListCustomerOrderCondition);
		model.addAttribute("customerOrders",customerOrders);
		return "ListOfCustomerOrdersToExport";		
	}
	
	@RequestMapping(value = "/getProductBaseCodeToNameMap", method = RequestMethod.GET) 	
	public @ResponseBody String getProductBaseCodeToNameMap(@RequestParam("supplierKcode") String supplierKcode){		
		Map<String,String> productBaseCodeToNameMap = listCustomerOrderUco.getProductBaseCodeToNameMap(supplierKcode);
		return JsonHelper.toJson(productBaseCodeToNameMap);		
	}


	@RequestMapping(value = "/getProductSkuCodeToNameMap", method = RequestMethod.GET ,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = {"application/json; charset=UTF-8"})
	public @ResponseBody String getProductSkuCodeToNameMap(@RequestParam("productBaseCode") String productBaseCode){		
		Map<String,String> productSkuCodeToNameMap = listCustomerOrderUco.getProductSkuCodeToNameMap(productBaseCode);
		return JsonHelper.toJson(productSkuCodeToNameMap);
	}
	
	//TODO move
	private String getStartCountPerPage(int currentPageIndex,int currentPageCount,int pageSize){		
		Integer startCountPerPage = (currentPageIndex-1)*pageSize+1;											
		return startCountPerPage.toString();				
	}
	
	//TODO move
	private String getEndCountPerPage(int currentPageIndex,int currentPageCount,int pageSize,int totalRecords){		
		Integer endCountPerPage = (currentPageCount==20)?currentPageIndex*pageSize:totalRecords;						
		return endCountPerPage.toString();		
	}
	
	private Map<String,Object> getCustomerOrderConditions(ListCustomerOrderConditionImpl ListCustomerOrderCondition){
		Map<String,Object> customerOrderConditions = new TreeMap<String,Object>();				
		boolean isDrsUser  = this.listCustomerOrderUco.isCurrentUserDrsUser();		
		if(isDrsUser){	
			customerOrderConditions.put("supplierKcode",ListCustomerOrderCondition.getSupplierKcode());
			customerOrderConditions.put("amazonMerchantSKU",ListCustomerOrderCondition.getAmazonMerchantSKU());
			customerOrderConditions.put("buyerEmail",ListCustomerOrderCondition.getBuyerEmail());		
		}		
		customerOrderConditions.put("relatedBaseProductCode",ListCustomerOrderCondition.getRelatedBaseProductCode());
		customerOrderConditions.put("relatedSkuCode",ListCustomerOrderCondition.getRelatedSkuCode());
		customerOrderConditions.put("asin",ListCustomerOrderCondition.getAsin());
		customerOrderConditions.put("salesChannelId",ListCustomerOrderCondition.getSalesChannelId());
		customerOrderConditions.put("orderStatus",ListCustomerOrderCondition.getOrderStatus());
		customerOrderConditions.put("orderDateStart",ListCustomerOrderCondition.getOrderDateStart());
		customerOrderConditions.put("orderDateEnd",ListCustomerOrderCondition.getOrderDateEnd());
		customerOrderConditions.put("transactionDateStart",ListCustomerOrderCondition.getTransactionDateStart());
		customerOrderConditions.put("transactionDateEnd",ListCustomerOrderCondition.getTransactionDateEnd());
		customerOrderConditions.put("marketplaceOrderId",ListCustomerOrderCondition.getMarketplaceOrderId());		
		customerOrderConditions.put("customerName",ListCustomerOrderCondition.getCustomerName());
		customerOrderConditions.put("promotionId",ListCustomerOrderCondition.getPromotionId());
		customerOrderConditions.put("searchTerms", ListCustomerOrderCondition.getSearchTerms());
		customerOrderConditions.put("isSearch", ListCustomerOrderCondition.getIsSearch());
		return customerOrderConditions;				
	}

	private String getSearchResultTitle(ListCustomerOrderConditionImpl ListCustomerOrderCondition,Locale locale){		
		ApplicationContext ctx = SpringAppCtx.get();
		MessageSource source = (MessageSource) ctx.getBean("messageSource");
		String searchResultTitle = null;
		if(!"true".equals(ListCustomerOrderCondition.getIsSearch()) &&
				this.listCustomerOrderUco.conditionsAreAllNull(ListCustomerOrderCondition)){
			boolean isDrsUser  = this.listCustomerOrderUco.isCurrentUserDrsUser();		
			if(isDrsUser){
				searchResultTitle = source.getMessage("customerOrder.searchCustomerOrderResultForDRSStaff", null, locale);			
			} else {
				searchResultTitle = source.getMessage("customerOrder.searchCustomerOrderResultForSupplier", null, locale);												
			}		
		}else{	
			searchResultTitle = "";						
		}				
		return searchResultTitle;	
	}
	
	@RequestMapping(value = "/getAmazonOrderItemDetail", method = RequestMethod.GET)
	public @ResponseBody String getAmazonOrderItemDetail(
			@RequestParam("salesChannel") String salesChannel,
			@RequestParam("orderId") String orderId,
			@RequestParam("sku") String sku){
		return JsonHelper.toJson(this.listCustomerOrderUco.getAmazonOrderDetail(salesChannel, orderId, sku));		
	}
	
	@RequestMapping(value = "/getShopifyOrderItemDetail", method = RequestMethod.GET)	
	public @ResponseBody String getShopifyOrderItemDetail(
			@RequestParam("orderId") String orderId,
			@RequestParam("sku") String sku){
		return JsonHelper.toJson(this.listCustomerOrderUco.getShopifyOrderDetail(orderId, sku));		
	}
	
}