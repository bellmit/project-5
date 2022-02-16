package com.kindminds.drs.service.usecase.product;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.kindminds.drs.Context;
import com.kindminds.drs.Country;


import com.kindminds.drs.api.data.transfer.productV2.ProductDetail;
import com.kindminds.drs.api.data.transfer.productV2.ProductDto;
import com.kindminds.drs.api.usecase.product.MaintainProductOnboardingUco;
import com.kindminds.drs.api.v1.model.common.DtoList;
import com.kindminds.drs.api.v1.model.common.Pager;
import com.kindminds.drs.api.v1.model.product.BaseProductOnboardingWithSKU;
import com.kindminds.drs.api.v1.model.product.BaseProductOnboardingWithSKU.BaseProductOnboardingSKUItem;

import com.kindminds.drs.api.data.access.rdb.CompanyDao;
import com.kindminds.drs.api.data.access.rdb.UserDao;
import com.kindminds.drs.api.data.access.usecase.product.MaintainProductOnboardingDao;

import com.kindminds.drs.api.v2.biz.domain.model.product.ProductEditingStatusType;
import com.kindminds.drs.v1.model.impl.BaseProductOnboardingSKUItemImpl;
import com.kindminds.drs.v1.model.impl.BaseProductOnboardingWithSKUImpl;
import com.kindminds.drs.service.util.MailUtil;
import com.kindminds.drs.service.util.MailUtil.SignatureType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service("maintainProductOnboardingUco")
public class MaintainProductOnboardingUcoImpl implements MaintainProductOnboardingUco{

	@Autowired private MaintainProductOnboardingDao dao;
	@Autowired private CompanyDao companyRepo;
	@Autowired private UserDao userRepo;
	@Autowired private MailUtil mailUtil;
	@Autowired private MessageSource messageSource;	
	@Autowired @Qualifier("envProperties") private Properties env;

	private final int sizePerPage = 20;
	private static final String ADDRESS_NO_REPLY = "DRS.network <drs-noreply@tw.drs.network>";
	private static final String ACCOUNT_MANAGERS = "account.managers@tw.drs.network";
	private static final String SOFTWARE_ENGINEERS = "software.engineering@drs.network";
	
	private List<Country> getCountries(){
		return Country.getAmazonCountryList();
	}
	
	/*
	@Override
	public DtoList<BaseProductOnboarding> retrieveBaseProductOnboardingList(String supplierKcode,int pageIndex) {
		
		
		Boolean isDrsUser = Context.getCurrentUser().isDrsUser();
		if(isDrsUser){
			if(supplierKcode.equals("All")){				
				return this.retrieveBaseProductOnboardingListByDrsUser(pageIndex);				
			}else{				
				DtoList<BaseProductOnboarding> list = new DtoList<BaseProductOnboarding>();
				list.setTotalRecords(this.dao.queryBaseProductOnboardingCount(supplierKcode));
				list.setPager(new Pager(pageIndex,list.getTotalRecords(),this.sizePerPage));
				list.setItems(this.dao.queryBaseProductOnboardingList(list.getPager().getStartRowNum(), 
						list.getPager().getPageSize(), supplierKcode));		
				return list;				
			}						
		}	
		
		
		
		return this.retrieveBaseProductOnboardingListBySupplier(pageIndex);
	}*/
	
	private DtoList<ProductDto> retrieveBaseProductOnboardingListByDrsUser(int pageIndex){
		DtoList<ProductDto> list = new DtoList<ProductDto>();
		list.setTotalRecords(this.dao.queryBaseProductOnboardingCount());
		list.setPager(new Pager(pageIndex,list.getTotalRecords(),this.sizePerPage));
		list.setItems(this.dao.queryBaseProductOnboardingList(list.getPager().getStartRowNum(), list.getPager().getPageSize()));				
		return list;
	}
		
	private DtoList<ProductDto> retrieveBaseProductOnboardingListBySupplier(int pageIndex){
		String companyKcode = Context.getCurrentUser().getCompanyKcode();
		DtoList<ProductDto> list = new DtoList<ProductDto>();
		list.setTotalRecords(this.dao.queryBaseProductOnboardingCount(companyKcode));
		list.setPager(new Pager(pageIndex,list.getTotalRecords(),this.sizePerPage));
		list.setItems(this.dao.queryBaseProductOnboardingList(list.getPager().getStartRowNum(), list.getPager().getPageSize(), companyKcode));		
		return list;
	}
	
	/*
	@Override
	public BaseProductOnboarding getDetailOfBaseProductOnboarding(String productBaseCode) {
		
		
		String userCompanyKcode = Context.getCurrentUser().getCompanyKcode();
		if(this.companyRepo.isSupplier(userCompanyKcode)){
			String supplierKcode = this.dao.querySupplierKcodeOfBaseProductOnboarding(productBaseCode);
			Assert.isTrue(userCompanyKcode.equals(supplierKcode));
		}						
		
		
		return this.dao.queryBaseProductOnboarding(productBaseCode);
	}

	@Override
	public BaseProductOnboardingDetail getProductInfoMarketSide(String supplierKcode, String productBaseCode, String country) {
		String userKcode = Context.getCurrentUser().getCompanyKcode();
		Boolean isDrsUser = Context.getCurrentUser().isDrsUser();
		if(isDrsUser){
			Assert.notNull(supplierKcode);
			return this.dao.queryProductInfoMarketSide(supplierKcode, productBaseCode, country);			
		}		
		return this.dao.queryProductInfoMarketSide(userKcode, productBaseCode, country);
	}

	@Override
	public BaseProductOnboardingDetail getProductMarketingMaterialSource(String supplierKcode, String productBaseCode) {
		String userKcode = Context.getCurrentUser().getCompanyKcode();
		Boolean isDrsUser = Context.getCurrentUser().isDrsUser();
		if(isDrsUser){
			Assert.notNull(supplierKcode);
			return this.dao.queryProductMarketingMaterialSource(supplierKcode, productBaseCode);					
		}		
		return this.dao.queryProductMarketingMaterialSource(userKcode, productBaseCode);
	}
	*/

	/*
	@Override
	public BaseProductOnboardingDetail getProductMarketingMaterialMarketSide(String supplierKcode,
			String productBaseCode, String country) {
		String userKcode = Context.getCurrentUser().getCompanyKcode();
		Boolean isDrsUser = Context.getCurrentUser().isDrsUser();
		if(isDrsUser){
			Assert.notNull(supplierKcode);
			return this.dao.queryProductMarketingMaterialMarketSide(supplierKcode, productBaseCode, country);			
		}			
		return this.dao.queryProductMarketingMaterialMarketSide(userKcode, productBaseCode, country);					
	}

	@Override
	public String saveDevelopingBaseProduct(String productInfoSource, String productInfoMarketSide,
		String productMarketingMaterialSource, String productMarketingMaterialMarketSide) throws JsonParseException, JsonMappingException, IOException {		
		
		
		//init save
		
		ObjectMapper objectMapper = new ObjectMapper();			
		JsonNode productInfoSourceJsonNode = objectMapper.readValue(productInfoSource, JsonNode.class);			
		String bpCode = productInfoSourceJsonNode.get("baseProductCode").asText();
		
	
		JsonNode productInfoMarketSideJsonNode = objectMapper.readValue(productInfoMarketSide, JsonNode.class);
		JsonNode productMarketingMaterialSourceJsonNode = objectMapper.readValue(productMarketingMaterialSource, JsonNode.class);
		JsonNode productMarketingMaterialMarketSideJsonNode = objectMapper.readValue(productMarketingMaterialMarketSide, JsonNode.class);			
						
		
		//add status to this method
		this.saveProductInfoSource(productInfoSourceJsonNode.get("supplierKcode").asText(),
				productInfoSourceJsonNode.get("baseProductCode").asText(),
				productInfoSourceJsonNode.toString());				
		
		
		this.updateStatusForProductInfoSource(productInfoSourceJsonNode.get("supplierKcode").asText(), 
			productInfoSourceJsonNode.get("baseProductCode").asText(), StatusType.PENDING_SUPPLIER_ACTION);			
		
		
	
		for(JsonNode marketSide: productInfoMarketSideJsonNode){				
			this.saveProductInfoMarketSide(marketSide.get("supplierKcode").asText(),marketSide.get("productBaseCode").asText(),
					marketSide.get("country").asText(),marketSide.get("jsonData").asText());			
			
			this.updateStatusForProductInfoMarketSideByRegion(marketSide.get("supplierKcode").asText(),
					marketSide.get("productBaseCode").asText(), marketSide.get("country").asText(), StatusType.PENDING_SUPPLIER_ACTION);
		}			
		
		
		
	
	/*
	@Override
	public String saveDraftForProductInfoSource(String productInfoSource, String productInfoMarketSide,
		String productMarketingMaterialSource, String productMarketingMaterialMarketSide) 
				throws JsonParseException, JsonMappingException, IOException {				
		
			//Draft Draft Draft Draft Draft Draft
		
		ObjectMapper objectMapper = new ObjectMapper();			
		JsonNode productInfoSourceJsonNode = objectMapper.readValue(productInfoSource, JsonNode.class);			
		JsonNode productInfoMarketSideJsonNode = objectMapper.readValue(productInfoMarketSide, JsonNode.class);
		JsonNode productMarketingMaterialSourceJsonNode = objectMapper.readValue(productMarketingMaterialSource, JsonNode.class);
		JsonNode productMarketingMaterialMarketSideJsonNode = objectMapper.readValue(productMarketingMaterialMarketSide, JsonNode.class);			
		
		this.deleteProductInfoSource(productInfoSourceJsonNode.get("supplierKcode").asText(), productInfoSourceJsonNode.get("baseProductCode").asText());
		this.deleteProductInfoMarketSide(productInfoSourceJsonNode.get("supplierKcode").asText(), productInfoSourceJsonNode.get("baseProductCode").asText());			
		this.deleteProductMarketingMaterialSource(productInfoSourceJsonNode.get("supplierKcode").asText(), productInfoSourceJsonNode.get("baseProductCode").asText());
		this.deleteProductMarketingMaterialMarketSide(productInfoSourceJsonNode.get("supplierKcode").asText(), productInfoSourceJsonNode.get("baseProductCode").asText());			
		
		this.saveProductInfoSource(productInfoSourceJsonNode.get("supplierKcode").asText(),productInfoSourceJsonNode.get("baseProductCode").asText(),productInfoSourceJsonNode.toString());				
		
		this.updateStatusForProductInfoSource(productInfoSourceJsonNode.get("supplierKcode").asText(), productInfoSourceJsonNode.get("baseProductCode").asText(), StatusType.PENDING_SUPPLIER_ACTION);						
		
		for(JsonNode marketSide: productInfoMarketSideJsonNode){
			this.saveProductInfoMarketSide(marketSide.get("supplierKcode").asText(),marketSide.get("productBaseCode").asText(),marketSide.get("country").asText(),marketSide.get("jsonData").asText());						
			this.updateStatusForProductInfoMarketSideByRegion(marketSide.get("supplierKcode").asText(), marketSide.get("productBaseCode").asText(), marketSide.get("country").asText(), StatusType.fromValue(marketSide.get("status").asText()));				
		}	
		
		this.saveProductMarketingMaterialSource(productMarketingMaterialSourceJsonNode.get("supplierKcode").asText(),productMarketingMaterialSourceJsonNode.get("productBaseCode").asText(),productMarketingMaterialSourceJsonNode.get("jsonData").asText());				
		this.updateStatusForProductMarketingMaterialSource(productMarketingMaterialSourceJsonNode.get("supplierKcode").asText(), productMarketingMaterialSourceJsonNode.get("productBaseCode").asText(), StatusType.fromValue(productMarketingMaterialSourceJsonNode.get("status").asText()));						
		for(JsonNode marketingMaterialMarketSide: productMarketingMaterialMarketSideJsonNode){
			this.saveProductMarketingMaterialMarketSide(marketingMaterialMarketSide.get("supplierKcode").asText(),marketingMaterialMarketSide.get("productBaseCode").asText(),marketingMaterialMarketSide.get("country").asText(),marketingMaterialMarketSide.get("jsonData").asText());				
		}		
																		
		return "/CoreProductInformation/"+productInfoSourceJsonNode.get("baseProductCode").asText();
		
	}
	*/
	
	
/*
	@Override
	public String submitProductInfoSource(String productInfoSource, String productInfoMarketSide,
		
			
		//submit submit submit submit submit submit submit submit submit 
			
		String productMarketingMaterialSource, String productMarketingMaterialMarketSide) throws JsonParseException, JsonMappingException, IOException {				
		ObjectMapper objectMapper = new ObjectMapper();			
		JsonNode productInfoSourceJsonNode = objectMapper.readValue(productInfoSource, JsonNode.class);			
		JsonNode productInfoMarketSideJsonNode = objectMapper.readValue(productInfoMarketSide, JsonNode.class);
		JsonNode productMarketingMaterialSourceJsonNode = objectMapper.readValue(productMarketingMaterialSource, JsonNode.class);
		JsonNode productMarketingMaterialMarketSideJsonNode = objectMapper.readValue(productMarketingMaterialMarketSide, JsonNode.class);			
		this.deleteProductInfoSource(productInfoSourceJsonNode.get("supplierKcode").asText(), productInfoSourceJsonNode.get("baseProductCode").asText());
		this.deleteProductInfoMarketSide(productInfoSourceJsonNode.get("supplierKcode").asText(), productInfoSourceJsonNode.get("baseProductCode").asText());			
		this.deleteProductMarketingMaterialSource(productInfoSourceJsonNode.get("supplierKcode").asText(), productInfoSourceJsonNode.get("baseProductCode").asText());
		this.deleteProductMarketingMaterialMarketSide(productInfoSourceJsonNode.get("supplierKcode").asText(), productInfoSourceJsonNode.get("baseProductCode").asText());			
		this.saveProductInfoSource(productInfoSourceJsonNode.get("supplierKcode").asText(),productInfoSourceJsonNode.get("baseProductCode").asText(),productInfoSourceJsonNode.toString());				
		this.updateStatusForProductInfoSource(productInfoSourceJsonNode.get("supplierKcode").asText(), productInfoSourceJsonNode.get("baseProductCode").asText(), this.getNextStatusToTransfer(StatusType.PENDING_SUPPLIER_ACTION));			
		for(JsonNode marketSide: productInfoMarketSideJsonNode){
			this.saveProductInfoMarketSide(marketSide.get("supplierKcode").asText(),marketSide.get("productBaseCode").asText(),marketSide.get("country").asText(),marketSide.get("jsonData").asText());				
			this.updateStatusForProductInfoMarketSideByRegion(marketSide.get("supplierKcode").asText(), marketSide.get("productBaseCode").asText(), marketSide.get("country").asText(), StatusType.fromValue(marketSide.get("status").asText()));								
		}			
		this.saveProductMarketingMaterialSource(productMarketingMaterialSourceJsonNode.get("supplierKcode").asText(),productMarketingMaterialSourceJsonNode.get("productBaseCode").asText(),productMarketingMaterialSourceJsonNode.get("jsonData").asText());				
		this.updateStatusForProductMarketingMaterialSource(productMarketingMaterialSourceJsonNode.get("supplierKcode").asText(), productMarketingMaterialSourceJsonNode.get("productBaseCode").asText(), StatusType.fromValue(productMarketingMaterialSourceJsonNode.get("status").asText()));							
		for(JsonNode marketingMaterialMarketSide: productMarketingMaterialMarketSideJsonNode){
			this.saveProductMarketingMaterialMarketSide(marketingMaterialMarketSide.get("supplierKcode").asText(),marketingMaterialMarketSide.get("productBaseCode").asText(),marketingMaterialMarketSide.get("country").asText(),marketingMaterialMarketSide.get("jsonData").asText());				
						
	}

	@Override
	public String approveProductInfoSource(String productInfoSource, String productInfoMarketSide,
		String productMarketingMaterialSource, String productMarketingMaterialMarketSide) throws JsonParseException, JsonMappingException, IOException {				
		
		System.out.println("ApproveApproveApproveApproveApprove");
		
		ObjectMapper objectMapper = new ObjectMapper();			
		JsonNode productInfoSourceJsonNode = objectMapper.readValue(productInfoSource, JsonNode.class);			
		JsonNode productInfoMarketSideJsonNode = objectMapper.readValue(productInfoMarketSide, JsonNode.class);
		JsonNode productMarketingMaterialSourceJsonNode = objectMapper.readValue(productMarketingMaterialSource, JsonNode.class);
		JsonNode productMarketingMaterialMarketSideJsonNode = objectMapper.readValue(productMarketingMaterialMarketSide, JsonNode.class);			
		
		this.deleteProductInfoSource(productInfoSourceJsonNode.get("supplierKcode").asText(), productInfoSourceJsonNode.get("baseProductCode").asText());
		this.deleteProductInfoMarketSide(productInfoSourceJsonNode.get("supplierKcode").asText(), productInfoSourceJsonNode.get("baseProductCode").asText());			
		this.deleteProductMarketingMaterialSource(productInfoSourceJsonNode.get("supplierKcode").asText(), productInfoSourceJsonNode.get("baseProductCode").asText());
		this.deleteProductMarketingMaterialMarketSide(productInfoSourceJsonNode.get("supplierKcode").asText(), productInfoSourceJsonNode.get("baseProductCode").asText());			
		this.saveProductInfoSource(productInfoSourceJsonNode.get("supplierKcode").asText(),productInfoSourceJsonNode.get("baseProductCode").asText(),productInfoSourceJsonNode.toString());				
		this.updateStatusForProductInfoSource(productInfoSourceJsonNode.get("supplierKcode").asText(), productInfoSourceJsonNode.get("baseProductCode").asText(), this.getNextStatusToTransfer(StatusType.PENDING_DRS_REVIEW));			
		
		for(JsonNode marketSide: productInfoMarketSideJsonNode){
			this.saveProductInfoMarketSide(marketSide.get("supplierKcode").asText(),marketSide.get("productBaseCode").asText(),marketSide.get("country").asText(),marketSide.get("jsonData").asText());				
			this.updateStatusForProductInfoMarketSideByRegion(marketSide.get("supplierKcode").asText(), marketSide.get("productBaseCode").asText(), marketSide.get("country").asText(), StatusType.fromValue(marketSide.get("status").asText()));				
		}			
		this.saveProductMarketingMaterialSource(productMarketingMaterialSourceJsonNode.get("supplierKcode").asText(),productMarketingMaterialSourceJsonNode.get("productBaseCode").asText(),productMarketingMaterialSourceJsonNode.get("jsonData").asText());				
		this.updateStatusForProductMarketingMaterialSource(productMarketingMaterialSourceJsonNode.get("supplierKcode").asText(), productMarketingMaterialSourceJsonNode.get("productBaseCode").asText(), StatusType.fromValue(productMarketingMaterialSourceJsonNode.get("status").asText()));						
		for(JsonNode marketingMaterialMarketSide: productMarketingMaterialMarketSideJsonNode){
			this.saveProductMarketingMaterialMarketSide(marketingMaterialMarketSide.get("supplierKcode").asText(),marketingMaterialMarketSide.get("productBaseCode").asText(),marketingMaterialMarketSide.get("country").asText(),marketingMaterialMarketSide.get("jsonData").asText());				
		}	
		
		return "/CoreProductInformation/"+productInfoSourceJsonNode.get("baseProductCode").asText();										
	}
	*/
	
	/*
	@Override
	public String returnProductInfoSource(String productInfoSource, String productInfoMarketSide,
		String productMarketingMaterialSource, String productMarketingMaterialMarketSide) throws IOException {		
		
		
		System.out.println("return return return return");
		
		ObjectMapper objectMapper = new ObjectMapper();			
		JsonNode productInfoSourceJsonNode = objectMapper.readValue(productInfoSource, JsonNode.class);			
		JsonNode productInfoMarketSideJsonNode = objectMapper.readValue(productInfoMarketSide, JsonNode.class);
		JsonNode productMarketingMaterialSourceJsonNode = objectMapper.readValue(productMarketingMaterialSource, JsonNode.class);
		JsonNode productMarketingMaterialMarketSideJsonNode = objectMapper.readValue(productMarketingMaterialMarketSide, JsonNode.class);		
		this.deleteProductInfoSource(productInfoSourceJsonNode.get("supplierKcode").asText(), productInfoSourceJsonNode.get("baseProductCode").asText());
		this.deleteProductInfoMarketSide(productInfoSourceJsonNode.get("supplierKcode").asText(), productInfoSourceJsonNode.get("baseProductCode").asText());			
		this.deleteProductMarketingMaterialSource(productInfoSourceJsonNode.get("supplierKcode").asText(), productInfoSourceJsonNode.get("baseProductCode").asText());
		this.deleteProductMarketingMaterialMarketSide(productInfoSourceJsonNode.get("supplierKcode").asText(), productInfoSourceJsonNode.get("baseProductCode").asText());		
		this.saveProductInfoSource(productInfoSourceJsonNode.get("supplierKcode").asText(),productInfoSourceJsonNode.get("baseProductCode").asText(),productInfoSourceJsonNode.toString());				
		this.updateStatusForProductInfoSource(productInfoSourceJsonNode.get("supplierKcode").asText(), productInfoSourceJsonNode.get("baseProductCode").asText(), this.getPreviousStatusToTransfer(StatusType.PENDING_DRS_REVIEW));		
		for(JsonNode marketSide: productInfoMarketSideJsonNode){
			this.saveProductInfoMarketSide(marketSide.get("supplierKcode").asText(),marketSide.get("productBaseCode").asText(),marketSide.get("country").asText(),marketSide.get("jsonData").asText());				
			this.updateStatusForProductInfoMarketSideByRegion(marketSide.get("supplierKcode").asText(), marketSide.get("productBaseCode").asText(), marketSide.get("country").asText(), StatusType.fromValue(marketSide.get("status").asText()));				
		}		
		this.saveProductMarketingMaterialSource(productMarketingMaterialSourceJsonNode.get("supplierKcode").asText(),productMarketingMaterialSourceJsonNode.get("productBaseCode").asText(),productMarketingMaterialSourceJsonNode.get("jsonData").asText());				
		this.updateStatusForProductMarketingMaterialSource(productMarketingMaterialSourceJsonNode.get("supplierKcode").asText(), productMarketingMaterialSourceJsonNode.get("productBaseCode").asText(), StatusType.fromValue(productMarketingMaterialSourceJsonNode.get("status").asText()));		
		for(JsonNode marketingMaterialMarketSide: productMarketingMaterialMarketSideJsonNode){
			this.saveProductMarketingMaterialMarketSide(marketingMaterialMarketSide.get("supplierKcode").asText(),marketingMaterialMarketSide.get("productBaseCode").asText(),marketingMaterialMarketSide.get("country").asText(),marketingMaterialMarketSide.get("jsonData").asText());				
		}	
		
		
		return "/CoreProductInformation/"+productInfoSourceJsonNode.get("baseProductCode").asText();		
	}
		
	@Override
	public String updateProductInfoSource(String productInfoSource, String productInfoMarketSide,
		String productMarketingMaterialSource, String productMarketingMaterialMarketSide) throws IOException {		
		ObjectMapper objectMapper = new ObjectMapper();			
		JsonNode productInfoSourceJsonNode = objectMapper.readValue(productInfoSource, JsonNode.class);			
		JsonNode productInfoMarketSideJsonNode = objectMapper.readValue(productInfoMarketSide, JsonNode.class);
		JsonNode productMarketingMaterialSourceJsonNode = objectMapper.readValue(productMarketingMaterialSource, JsonNode.class);
		JsonNode productMarketingMaterialMarketSideJsonNode = objectMapper.readValue(productMarketingMaterialMarketSide, JsonNode.class);		
		this.deleteProductInfoSource(productInfoSourceJsonNode.get("supplierKcode").asText(), productInfoSourceJsonNode.get("baseProductCode").asText());
		this.deleteProductInfoMarketSide(productInfoSourceJsonNode.get("supplierKcode").asText(), productInfoSourceJsonNode.get("baseProductCode").asText());			
		this.deleteProductMarketingMaterialSource(productInfoSourceJsonNode.get("supplierKcode").asText(), productInfoSourceJsonNode.get("baseProductCode").asText());
		this.deleteProductMarketingMaterialMarketSide(productInfoSourceJsonNode.get("supplierKcode").asText(), productInfoSourceJsonNode.get("baseProductCode").asText());		
		this.saveProductInfoSource(productInfoSourceJsonNode.get("supplierKcode").asText(),productInfoSourceJsonNode.get("baseProductCode").asText(),productInfoSourceJsonNode.toString());						
		this.updateStatusForProductInfoSource(productInfoSourceJsonNode.get("supplierKcode").asText(), productInfoSourceJsonNode.get("baseProductCode").asText(), StatusType.fromValue(productInfoSourceJsonNode.get("status").asText()));		
		for(JsonNode marketSide: productInfoMarketSideJsonNode){
			this.saveProductInfoMarketSide(marketSide.get("supplierKcode").asText(),marketSide.get("productBaseCode").asText(),marketSide.get("country").asText(),marketSide.get("jsonData").asText());				
			this.updateStatusForProductInfoMarketSideByRegion(marketSide.get("supplierKcode").asText(), marketSide.get("productBaseCode").asText(), marketSide.get("country").asText(), StatusType.fromValue(marketSide.get("status").asText()));								
		}		
		this.saveProductMarketingMaterialSource(productMarketingMaterialSourceJsonNode.get("supplierKcode").asText(),productMarketingMaterialSourceJsonNode.get("productBaseCode").asText(),productMarketingMaterialSourceJsonNode.get("jsonData").asText());				
		this.updateStatusForProductMarketingMaterialSource(productMarketingMaterialSourceJsonNode.get("supplierKcode").asText(), productMarketingMaterialSourceJsonNode.get("productBaseCode").asText(), StatusType.fromValue(productMarketingMaterialSourceJsonNode.get("status").asText()));						
		for(JsonNode marketingMaterialMarketSide: productMarketingMaterialMarketSideJsonNode){
			this.saveProductMarketingMaterialMarketSide(marketingMaterialMarketSide.get("supplierKcode").asText(),marketingMaterialMarketSide.get("productBaseCode").asText(),marketingMaterialMarketSide.get("country").asText(),marketingMaterialMarketSide.get("jsonData").asText());				
		}																				
		return "/CoreProductInformation/"+productInfoSourceJsonNode.get("baseProductCode").asText();				
	}
	
		
	private String saveProductInfoSource(String supplierKcode,String productBaseCode,String jsonData){
		
		
		String userKcode = Context.getCurrentUser().getCompanyKcode();
		Boolean isDrsUser = Context.getCurrentUser().isDrsUser();
		if(isDrsUser){			
			Assert.notNull(supplierKcode);
			return this.dao.insertProductInfoSource(supplierKcode, productBaseCode, jsonData);
		}				
		Assert.isTrue(this.companyRepo.isSupplier(userKcode));
		
		
		return this.dao.insertProductInfoSource(userKcode, productBaseCode, jsonData);					
	}
	
	private String saveProductInfoMarketSide(String supplierKcode,String productBaseCode,String country,String jsonData){
		String userKcode = Context.getCurrentUser().getCompanyKcode();
		Boolean isDrsUser = Context.getCurrentUser().isDrsUser();
		if(isDrsUser){
			Assert.notNull(supplierKcode);
			return this.dao.insertProductInfoMarketSide(supplierKcode, productBaseCode, country, jsonData);
		}		
		Assert.isTrue(this.companyRepo.isSupplier(userKcode));
		return this.dao.insertProductInfoMarketSide(userKcode, productBaseCode, country, jsonData);				
	}
	
	private String saveProductMarketingMaterialSource(String supplierKcode,String productBaseCode,String jsonData){
			String userKcode = Context.getCurrentUser().getCompanyKcode();
		Boolean isDrsUser = Context.getCurrentUser().isDrsUser();
		if(isDrsUser){
			Assert.notNull(supplierKcode);
			return this.dao.insertProductMarketingMaterialSource(supplierKcode, productBaseCode, jsonData);
		}	
		Assert.isTrue(this.companyRepo.isSupplier(userKcode));
		return this.dao.insertProductMarketingMaterialSource(userKcode, productBaseCode, jsonData);				
	}
	
	private String saveProductMarketingMaterialMarketSide(String supplierKcode,String productBaseCode,String country,String jsonData){
		String userKcode = Context.getCurrentUser().getCompanyKcode();
		Boolean isDrsUser = Context.getCurrentUser().isDrsUser();
		if(isDrsUser){
			Assert.notNull(supplierKcode);
			return this.dao.insertProductMarketingMaterialMarketSide(supplierKcode, productBaseCode, country, jsonData);
		}		
		Assert.isTrue(this.companyRepo.isSupplier(userKcode));
		return this.dao.insertProductMarketingMaterialMarketSide(userKcode, productBaseCode, country, jsonData);						
	}
	
	private String updateStatusForProductInfoSource(String supplierKcode,String productBaseCode,StatusType status){
		String userKcode = Context.getCurrentUser().getCompanyKcode();
		Boolean isDrsUser = Context.getCurrentUser().isDrsUser();
		if(isDrsUser){
			Assert.notNull(supplierKcode);
			this.dao.updateStatusForProductInfoSource(supplierKcode, productBaseCode, status);
			return null;					
		}
		Assert.isTrue(this.companyRepo.isSupplier(userKcode));
		this.dao.updateStatusForProductInfoSource(userKcode, productBaseCode, status);
		return null;
	}
	
	private String updateStatusForProductInfoMarketSideByRegion(String supplierKcode,String productBaseCode,String country,StatusType status){
		String userKcode = Context.getCurrentUser().getCompanyKcode();
		Boolean isDrsUser = Context.getCurrentUser().isDrsUser();
		if(isDrsUser){
			Assert.notNull(supplierKcode);
			this.dao.updateStatusForProductInfoMarketSideByRegion(supplierKcode, productBaseCode, country, status);					
			return null;
		}
		Assert.isTrue(this.companyRepo.isSupplier(userKcode));
		this.dao.updateStatusForProductInfoMarketSideByRegion(userKcode, productBaseCode, country, status);										
		return null;
	} 
	
	
	private String updateStatusForProductMarketingMaterialSource(String supplierKcode,String productBaseCode,StatusType status){
		String userKcode = Context.getCurrentUser().getCompanyKcode();
		Boolean isDrsUser = Context.getCurrentUser().isDrsUser();
		if(isDrsUser){
			Assert.notNull(supplierKcode);
			this.dao.updateStatusForProductMarketingMaterialSource(supplierKcode, productBaseCode, status);
			return null;	
		}
		Assert.isTrue(this.companyRepo.isSupplier(userKcode));
		this.dao.updateStatusForProductMarketingMaterialSource(userKcode, productBaseCode, status);			
		return null;
	}*/
		
	
	/*
	private void deleteProductInfoSource(String supplierKcode,String productBaseCode){
		String userKcode = Context.getCurrentUser().getCompanyKcode();
		Boolean isDrsUser = Context.getCurrentUser().isDrsUser();
		if(isDrsUser){
			Assert.notNull(supplierKcode);
			this.dao.deleteProductInfoSource(supplierKcode, productBaseCode);			
		}else{
			Assert.isTrue(this.companyRepo.isSupplier(userKcode));
			this.dao.deleteProductInfoSource(userKcode, productBaseCode);
		}
	}
	
	private void deleteProductInfoMarketSide(String supplierKcode,String productBaseCode){
		String userKcode = Context.getCurrentUser().getCompanyKcode();
		Boolean isDrsUser = Context.getCurrentUser().isDrsUser();
		if(isDrsUser){
			Assert.notNull(supplierKcode);
			this.dao.deleteProductInfoMarketSide(supplierKcode, productBaseCode);
		}else{
			Assert.isTrue(this.companyRepo.isSupplier(userKcode));
			this.dao.deleteProductInfoMarketSide(userKcode, productBaseCode);
		}
	}
	
	private void deleteProductMarketingMaterialSource(String supplierKcode,String productBaseCode){
		String userKcode = Context.getCurrentUser().getCompanyKcode();
		Boolean isDrsUser = Context.getCurrentUser().isDrsUser();
		if(isDrsUser){
			Assert.notNull(supplierKcode);
			this.dao.deleteProductMarketingMaterialSource(supplierKcode, productBaseCode);
		}else{
			Assert.isTrue(this.companyRepo.isSupplier(userKcode));
			this.dao.deleteProductMarketingMaterialSource(userKcode, productBaseCode);
		}
	}
	
	private void deleteProductMarketingMaterialMarketSide(String supplierKcode,String productBaseCode){
		String userKcode = Context.getCurrentUser().getCompanyKcode();
		Boolean isDrsUser = Context.getCurrentUser().isDrsUser();
		if(isDrsUser){
			Assert.notNull(supplierKcode);
			this.dao.deleteProductMarketingMaterialMarketSide(supplierKcode, productBaseCode);
		}else{
			Assert.isTrue(this.companyRepo.isSupplier(userKcode));
			this.dao.deleteProductMarketingMaterialMarketSide(userKcode, productBaseCode);
		}
	}
		
	@Override
	public String saveDraftForProductInfoMarketSide(String supplierKcode, String productBaseCode, String country,
			String jsonData) {
		String userKcode = Context.getCurrentUser().getCompanyKcode();
		Boolean isDrsUser = Context.getCurrentUser().isDrsUser();
		if(isDrsUser){
			Assert.notNull(supplierKcode);
			return this.dao.updateProductInfoMarketSide(supplierKcode, productBaseCode, country, jsonData);					
		}
		Assert.isTrue(this.companyRepo.isSupplier(userKcode));		
		return this.dao.updateProductInfoMarketSide(userKcode, productBaseCode, country, jsonData);
	}

	@Override
	public String submitProductInfoMarketSide(String supplierKcode, String productBaseCode, String country,
			String jsonData) {
		String userKcode = Context.getCurrentUser().getCompanyKcode();
		Boolean isDrsUser = Context.getCurrentUser().isDrsUser();
		if(isDrsUser){
			Assert.notNull(supplierKcode);
			this.dao.updateProductInfoMarketSide(supplierKcode, productBaseCode, country, jsonData);
			this.dao.updateStatusForProductInfoMarketSideByRegion(supplierKcode, productBaseCode, country, this.getNextStatusToTransfer(StatusType.PENDING_SUPPLIER_ACTION));			
			return null;					
		}
		Assert.isTrue(this.companyRepo.isSupplier(userKcode));
		this.dao.updateProductInfoMarketSide(userKcode, productBaseCode, country, jsonData);
		this.dao.updateStatusForProductInfoMarketSideByRegion(userKcode, productBaseCode, country, this.getNextStatusToTransfer(StatusType.PENDING_SUPPLIER_ACTION));		
		processProductSubmissionNotification(supplierKcode, productBaseCode + "/" + country, "", "Product information", "MarketSideProductInformation");
		return null;
	}
	
	@Override
	public String approveProductInfoMarketSide(String supplierKcode, String productBaseCode, String country, String jsonData) {
		Boolean isDrsUser = Context.getCurrentUser().isDrsUser();
		if(isDrsUser){
			this.dao.updateProductInfoMarketSide(supplierKcode, productBaseCode, country, jsonData);			
			this.dao.updateStatusForProductInfoMarketSideByRegion(supplierKcode, productBaseCode, country, this.getNextStatusToTransfer(StatusType.PENDING_DRS_REVIEW));						
			return null;
		}
		this.dao.updateProductInfoMarketSide(supplierKcode, productBaseCode, country, jsonData);		
		this.dao.updateStatusForProductInfoMarketSideByRegion(supplierKcode, productBaseCode, country, this.getNextStatusToTransfer(StatusType.PENDING_DRS_REVIEW));		
		return null;
	}
	
	@Override
	public String returnProductInfoMarketSide(String supplierKcode, String productBaseCode, String country, String jsonData) {
		Boolean isDrsUser = Context.getCurrentUser().isDrsUser();
		if(isDrsUser){
			this.dao.updateProductInfoMarketSide(supplierKcode, productBaseCode, country, jsonData);			
			this.dao.updateStatusForProductInfoMarketSideByRegion(supplierKcode, productBaseCode, country, this.getPreviousStatusToTransfer(StatusType.PENDING_DRS_REVIEW));						
			return null;
		}
		this.dao.updateProductInfoMarketSide(supplierKcode, productBaseCode, country, jsonData);		
		this.dao.updateStatusForProductInfoMarketSideByRegion(supplierKcode, productBaseCode, country, this.getPreviousStatusToTransfer(StatusType.PENDING_DRS_REVIEW));						
		return null;
	}
	
	@Override
	public String updateProductInfoMarketSide(String supplierKcode, String productBaseCode, String country,
			String jsonData) {		
		String userKcode = Context.getCurrentUser().getCompanyKcode();
		Boolean isDrsUser = Context.getCurrentUser().isDrsUser();
		if(isDrsUser){
			Assert.notNull(supplierKcode);
			return this.dao.updateProductInfoMarketSide(supplierKcode, productBaseCode, country, jsonData);					
		}
		Assert.isTrue(this.companyRepo.isSupplier(userKcode));		
		return this.dao.updateProductInfoMarketSide(userKcode, productBaseCode, country, jsonData);		
	}
	
	@Override
	public String saveDraftForProductMarketingMaterialSource(String supplierKcode, String productBaseCode,
			String jsonData) {
		String userKcode = Context.getCurrentUser().getCompanyKcode();
		Boolean isDrsUser = Context.getCurrentUser().isDrsUser();
		if(isDrsUser){
			Assert.notNull(supplierKcode);
			return this.dao.updateProductMarketingMaterialSource(supplierKcode, productBaseCode, jsonData);								
		}
		Assert.isTrue(this.companyRepo.isSupplier(userKcode));
		return this.dao.updateProductMarketingMaterialSource(userKcode, productBaseCode, jsonData);
	}

	@Override
	public String submitProductMarketingMaterialSource(String supplierKcode, String productBaseCode, String jsonData) {
		String userKcode = Context.getCurrentUser().getCompanyKcode();
		Boolean isDrsUser = Context.getCurrentUser().isDrsUser();
		if(isDrsUser){
			Assert.notNull(supplierKcode);
			this.dao.updateProductMarketingMaterialSource(supplierKcode, productBaseCode, jsonData);								
			this.dao.updateStatusForProductMarketingMaterialSource(supplierKcode, productBaseCode, this.getNextStatusToTransfer(StatusType.PENDING_SUPPLIER_ACTION));						
			return null;
		}
		Assert.isTrue(this.companyRepo.isSupplier(userKcode));
		this.dao.updateProductMarketingMaterialSource(userKcode, productBaseCode, jsonData);		
		this.dao.updateStatusForProductMarketingMaterialSource(userKcode, productBaseCode, this.getNextStatusToTransfer(StatusType.PENDING_SUPPLIER_ACTION));							
		try {
			ObjectMapper objectMapper = new ObjectMapper();			
			JsonNode productMarketingMaterialSourceJsonNode;
			productMarketingMaterialSourceJsonNode = objectMapper.readValue(jsonData, JsonNode.class);
			processProductSubmissionNotification(supplierKcode, productBaseCode, productMarketingMaterialSourceJsonNode.get("name").asText(), "Product marketing material", "SourceProductMarketingMaterial");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String approveProductMarketingMaterialSource(String supplierKcode, String productBaseCode, String jsonData) {		
		String userKcode = Context.getCurrentUser().getCompanyKcode();
		Boolean isDrsUser = Context.getCurrentUser().isDrsUser();
		if(isDrsUser){
			Assert.notNull(supplierKcode);
			this.dao.updateProductMarketingMaterialSource(supplierKcode, productBaseCode, jsonData);								
			this.dao.updateStatusForProductMarketingMaterialSource(supplierKcode, productBaseCode, this.getNextStatusToTransfer(StatusType.PENDING_DRS_REVIEW));						
			return null;
		}
		Assert.isTrue(this.companyRepo.isSupplier(userKcode));
		this.dao.updateProductMarketingMaterialSource(userKcode, productBaseCode, jsonData);		
		this.dao.updateStatusForProductMarketingMaterialSource(userKcode, productBaseCode, this.getNextStatusToTransfer(StatusType.PENDING_DRS_REVIEW));									
		return null;
	}
	
	@Override
	public String returnProductMarketingMaterialSource(String supplierKcode, String productBaseCode, String jsonData) {
		String userKcode = Context.getCurrentUser().getCompanyKcode();
		Boolean isDrsUser = Context.getCurrentUser().isDrsUser();
		if(isDrsUser){
			Assert.notNull(supplierKcode);
			this.dao.updateProductMarketingMaterialSource(supplierKcode, productBaseCode, jsonData);								
			this.dao.updateStatusForProductMarketingMaterialSource(supplierKcode, productBaseCode, this.getPreviousStatusToTransfer(StatusType.PENDING_DRS_REVIEW));						
			return null;
		}
		Assert.isTrue(this.companyRepo.isSupplier(userKcode));
		this.dao.updateProductMarketingMaterialSource(userKcode, productBaseCode, jsonData);		
		this.dao.updateStatusForProductMarketingMaterialSource(userKcode, productBaseCode, this.getPreviousStatusToTransfer(StatusType.PENDING_DRS_REVIEW));									
		return null;
	}
	
	@Override
	public String updateProductMarketingMaterialSource(String supplierKcode, String productBaseCode, String jsonData) {		
		String userKcode = Context.getCurrentUser().getCompanyKcode();
		Boolean isDrsUser = Context.getCurrentUser().isDrsUser();
		if(isDrsUser){
			Assert.notNull(supplierKcode);
			return this.dao.updateProductMarketingMaterialSource(supplierKcode, productBaseCode, jsonData);								
		}
		Assert.isTrue(this.companyRepo.isSupplier(userKcode));
		return this.dao.updateProductMarketingMaterialSource(userKcode, productBaseCode, jsonData);					
	}
		
	@Override
	public String updateProductMarketingMaterialMarketSide(String supplierKcode, String productBaseCode,
			String country, String jsonData) {
		String userKcode = Context.getCurrentUser().getCompanyKcode();
		Boolean isDrsUser = Context.getCurrentUser().isDrsUser();
		if(isDrsUser){
			Assert.notNull(supplierKcode);
			return this.dao.updateProductMarketingMaterialMarketSide(supplierKcode, productBaseCode, country, jsonData);		
		}
		Assert.isTrue(this.companyRepo.isSupplier(userKcode));
		return this.dao.updateProductMarketingMaterialMarketSide(userKcode, productBaseCode, country, jsonData);							
	}
	*/
	
	@Override
	public String updateDangerousGoodsCode(String productInfoSource) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNodePIS = objectMapper.readValue(productInfoSource, JsonNode.class);
		String batteries = jsonNodePIS.get("batteries").asText();
		JsonNode batteriesNode = objectMapper.readValue(batteries, JsonNode.class);
		String dangerousGoodsCode = "none";


		if(batteriesNode != null){
			for(JsonNode batteryNode : batteriesNode){

				String packingWay = batteryNode.get("packingWay") != null ?
						batteryNode.get("packingWay").asText(): "";

				if ((batteryNode.get("batteryType") != null ? batteryNode.get("batteryType").asText() :"").equals("LithiumIon")) {

					BigDecimal votage =
							StringUtils.hasText(batteryNode.get("votage").asText()) ?
							new BigDecimal(batteryNode.get("votage").asText().contains("V") ?
									batteryNode.get("votage").asText().substring(0,
											batteryNode.get("votage").asText().length() -1) :
									batteryNode.get("votage").asText()) : new BigDecimal(1);

					BigDecimal capacity =
							StringUtils.hasText(batteryNode.get("capacity").asText()) ?
							new BigDecimal(batteryNode.get("capacity").asText()) : new BigDecimal(1);

					BigDecimal watts = votage.multiply(capacity);
					if (watts.compareTo(new BigDecimal(100)) <= 0) {
						if (packingWay.equals("packedWithEquipment")) {
							dangerousGoodsCode = "UN3481-P1966";
						} else if (packingWay.equals("packedInEquipment")) {
							dangerousGoodsCode = "UN3481-P1967";
						}
					}
				} else if ((batteryNode.get("batteryType") != null ? batteryNode.get("batteryType").asText() :"").equals("LithiumMetal")) {

					BigDecimal weight =  StringUtils.hasText(batteryNode.get("weight").asText()) ?
							new BigDecimal(batteryNode.get("weight").asText()) : new BigDecimal(1);

					if (weight.compareTo(new BigDecimal(2)) <= 0) {
						if (packingWay.equals("packedWithEquipment")) {
							dangerousGoodsCode = "UN3091-P1966";
						} else if (packingWay.equals("packedInEquipment")) {
							dangerousGoodsCode = "UN3091-P1967";
						}
					}
				}

				((ObjectNode) batteryNode).set("dangerousGoodsCode",
						objectMapper.convertValue(dangerousGoodsCode, JsonNode.class));
			}
		}

		((ObjectNode) jsonNodePIS).set("batteries", 
				objectMapper.convertValue(batteriesNode.toString(), JsonNode.class));

		return jsonNodePIS.toString();
	}
	
	@Override
	public BaseProductOnboardingWithSKU getBaseProductOnboardingWithSKU(String productBaseCode) throws JsonParseException, JsonMappingException, IOException{		
		ProductDto baseProduct = this.dao.queryBaseProductOnboarding(productBaseCode);
		ObjectMapper objectMapper = new ObjectMapper();
		ProductDetail productInfoSource = baseProduct.getProductInfoSource();		
		String productInfoSourceData = productInfoSource.getData();		
		JsonNode productInfoSourceJsonNode = objectMapper.readValue(productInfoSourceData, JsonNode.class);								
		String products = productInfoSourceJsonNode.get("products").asText();
		JsonNode productsJsonNode = objectMapper.readValue(products, JsonNode.class);		
		BaseProductOnboardingWithSKUImpl baseProductOnboardingWithSKU = new BaseProductOnboardingWithSKUImpl();
		List<BaseProductOnboardingSKUItemImpl> skuList = new ArrayList<BaseProductOnboardingSKUItemImpl>();		
		for(int i = 0; i < productsJsonNode.size() ; i++){
			BaseProductOnboardingSKUItemImpl sku = new BaseProductOnboardingSKUItemImpl();								
			sku.setIndex(i);			
			sku.setOriginalSKU(((ArrayNode) productsJsonNode).get(i).get("SKU").asText());
			sku.setUpdatedSKU(((ArrayNode) productsJsonNode).get(i).get("SKU").asText());
			sku.setType1(((ArrayNode) productsJsonNode).get(i).get("type1").asText());
			sku.setType1Value(((ArrayNode) productsJsonNode).get(i).get("type1Value").asText());
			sku.setType2(((ArrayNode) productsJsonNode).get(i).get("type2").asText());
			sku.setType2Value(((ArrayNode) productsJsonNode).get(i).get("type2Value").asText());
			sku.setGTINValue(((ArrayNode) productsJsonNode).get(i).get("GTINValue").asText());						
			Collection<String> applicableRegionList = new TreeSet<String>();			
			Iterator<JsonNode> applicableRegions = ((ArrayNode) productsJsonNode).get(i).get("applicableRegionList").elements();			
			while (applicableRegions.hasNext()) {   				
				applicableRegionList.add(applicableRegions.next().asText());									
			}
			sku.setApplicableRegionList(applicableRegionList);
			skuList.add(sku);			
		}				
		baseProductOnboardingWithSKU.setProductBaseCode(productInfoSourceJsonNode.get("baseProductCode").asText());
		baseProductOnboardingWithSKU.setSupplierKcode(productInfoSourceJsonNode.get("supplierKcode").asText());
		baseProductOnboardingWithSKU.setStatus(productInfoSourceJsonNode.get("status").asText());
		baseProductOnboardingWithSKU.setVariationType1(productInfoSourceJsonNode.get("variationType1").asText());
		baseProductOnboardingWithSKU.setVariationType2(productInfoSourceJsonNode.get("variationType2").asText());
		baseProductOnboardingWithSKU.setProductWithVariation(productInfoSourceJsonNode.get("productWithVariation").asText());				
		baseProductOnboardingWithSKU.setSKULineItem(skuList);				
		return baseProductOnboardingWithSKU;		
	}
		
	@Override
	public String updateBaseProductOnboardingWithSKU(BaseProductOnboardingWithSKU baseProduct) throws JsonParseException, JsonMappingException, IOException{				
		List<Country> countries = this.getCountries();
		String userKcode = Context.getCurrentUser().getCompanyKcode();
		Boolean isDrsUser = Context.getCurrentUser().isDrsUser();
		if(isDrsUser){
			Assert.notNull(baseProduct.getSupplierKcode());
			for(BaseProductOnboardingSKUItem skuItem:baseProduct.getSKULineItems()){
				this.updateSKUForProductInfoSource(skuItem.getIndex(),baseProduct.getSupplierKcode(), baseProduct.getProductBaseCode(), skuItem.getOriginalSKU(), skuItem.getUpdatedSKU());				
				this.updateSkuForProductMarketingMaterialSource(skuItem.getIndex(),baseProduct.getSupplierKcode(), baseProduct.getProductBaseCode(), skuItem.getOriginalSKU(), skuItem.getUpdatedSKU());				
				for(Country country : countries){
					this.updateSkuForProductInfoMarketSide(skuItem.getIndex(),baseProduct.getSupplierKcode(), baseProduct.getProductBaseCode(), country.toString(), skuItem.getOriginalSKU(), skuItem.getUpdatedSKU());
					this.updateSkuForProductMarketingMaterialmarketSide(skuItem.getIndex(),baseProduct.getSupplierKcode(), baseProduct.getProductBaseCode(), country.toString(), skuItem.getOriginalSKU(), skuItem.getUpdatedSKU());
					}	
			}
			return null;
		}
		Assert.isTrue(this.companyRepo.isSupplier(userKcode));		
		for(BaseProductOnboardingSKUItem skuItem:baseProduct.getSKULineItems()){
			this.updateSKUForProductInfoSource(skuItem.getIndex(),userKcode, baseProduct.getProductBaseCode(), skuItem.getOriginalSKU(), skuItem.getUpdatedSKU());			
			this.updateSkuForProductMarketingMaterialSource(skuItem.getIndex(),userKcode, baseProduct.getProductBaseCode(), skuItem.getOriginalSKU(), skuItem.getUpdatedSKU());
			for(Country country : countries){
				this.updateSkuForProductInfoMarketSide(skuItem.getIndex(),userKcode, baseProduct.getProductBaseCode(), country.toString(), skuItem.getOriginalSKU(), skuItem.getUpdatedSKU());
				this.updateSkuForProductMarketingMaterialmarketSide(skuItem.getIndex(),userKcode, baseProduct.getProductBaseCode(), country.toString(), skuItem.getOriginalSKU(), skuItem.getUpdatedSKU());
				}	
		}
		return null;
	}
		
	private void updateSKUForProductInfoSource(int index, String companyKcode, String productBaseCode, String originalSKU,String updatedSKU) throws JsonParseException, JsonMappingException, IOException{		
		ProductDto baseProduct = this.dao.queryBaseProductOnboarding(productBaseCode);
		ObjectMapper objectMapper = new ObjectMapper();				
		ProductDetail productInfoSource = baseProduct.getProductInfoSource();		
		String productInfoSourceData = productInfoSource.getData();		
		JsonNode productInfoSourceJsonNode = objectMapper.readValue(productInfoSourceData, JsonNode.class);		
		String productInfoSourceDataString = productInfoSourceData.toString();
		String products = productInfoSourceJsonNode.get("products").asText();				
		JsonNode productsJsonNode = objectMapper.readValue(products, JsonNode.class);				
		JsonNode elemSKU = null;		
		if(originalSKU.equals(((ArrayNode) productsJsonNode).get(index).get("SKU").asText())){
			 elemSKU = ((ArrayNode) productsJsonNode).get(index);
			((ObjectNode) elemSKU).put("SKU", updatedSKU);
		}				
		products = products.replace("\"", "\\\"");
		String productsJsonNodeStr = productsJsonNode.toString();
		productsJsonNodeStr = productsJsonNodeStr.replace("\"", "\\\"");		
		productInfoSourceDataString = productInfoSourceDataString.replace(products, productsJsonNodeStr);	    		
		String referenceFiles = productInfoSourceJsonNode.get("referenceFiles").asText();		
		JsonNode referenceFilesJsonNode = objectMapper.readValue(referenceFiles, JsonNode.class);	
				
		for(int j = 0; j < referenceFilesJsonNode.size() ; j++){		
			JsonNode appliedVariationProductJsonNode = referenceFilesJsonNode.get(j).get("appliedVariationProduct");			
			if(appliedVariationProductJsonNode.size() > 0){
				if(originalSKU.equals(((ArrayNode) appliedVariationProductJsonNode).get(index).asText())){	    			    				    			
					((ArrayNode) appliedVariationProductJsonNode).set(index, new TextNode(updatedSKU));
				}						
			}
		}		
	    referenceFiles = referenceFiles.replace("\"", "\\\"");
	    String referenceFilesJsonNodeStr = referenceFilesJsonNode.toString();
	    referenceFilesJsonNodeStr = referenceFilesJsonNodeStr.replace("\"", "\\\"");	    	    
	    productInfoSourceDataString = productInfoSourceDataString.replace(referenceFiles, referenceFilesJsonNodeStr);	    
	    String batteries = productInfoSourceJsonNode.get("batteries").asText();
	    JsonNode batteriesJsonNode = objectMapper.readValue(batteries, JsonNode.class);			
	    for(int k = 0; k < batteriesJsonNode.size() ; k++){
	    	JsonNode appliedVariationProductJsonNodeForBatteries = batteriesJsonNode.get(k).get("appliedVariationProduct");		    		    	
	    	if(appliedVariationProductJsonNodeForBatteries.size() > 0){
	    		if(originalSKU.equals(((ArrayNode) appliedVariationProductJsonNodeForBatteries).get(index).asText())){	    			    				    			
					((ArrayNode) appliedVariationProductJsonNodeForBatteries).set(index, new TextNode(updatedSKU));
				}		    	
	    	}
	    }	    
	    batteries = batteries.replace("\"", "\\\"");
	    String batteriesJsonNodeStr = batteriesJsonNode.toString(); 
	    batteriesJsonNodeStr = batteriesJsonNodeStr.replace("\"", "\\\"");
	    productInfoSourceDataString = productInfoSourceDataString.replace(batteries, batteriesJsonNodeStr);	    	    
	    this.dao.updateJsonDataForProductInfoSource(companyKcode, productBaseCode, productInfoSourceDataString);				
	}
	
	private void updateSkuForProductMarketingMaterialSource(int index, String companyKcode, String productBaseCode, String originalSKU,String updatedSKU)throws JsonParseException, JsonMappingException, IOException{		
		ProductDto baseProduct = this.dao.queryBaseProductOnboarding(productBaseCode);
		ObjectMapper objectMapper = new ObjectMapper();		
		ProductDetail productMarketingMaterialSource = baseProduct.getProductMarketingMaterialSource();
		String productMarketingMaterialSourceData = productMarketingMaterialSource.getData();		
		JsonNode productMarketingMaterialSourceJsonNode = objectMapper.readValue(productMarketingMaterialSourceData, JsonNode.class);		
		String productMarketingMaterialSourceDataString = productMarketingMaterialSourceData.toString();
		String products = productMarketingMaterialSourceJsonNode.get("products").asText();		
		JsonNode productsJsonNode = objectMapper.readValue(products, JsonNode.class);		
		JsonNode elemSKU = null;		
		if(originalSKU.equals(((ArrayNode) productsJsonNode).get(index).get("SKU").asText())){
			 elemSKU = ((ArrayNode) productsJsonNode).get(index);
			((ObjectNode) elemSKU).put("SKU", updatedSKU);
		}				
		products = products.replace("\"", "\\\"");
		String productsJsonNodeStr = productsJsonNode.toString();
		productsJsonNodeStr = productsJsonNodeStr.replace("\"", "\\\"");		
		productMarketingMaterialSourceDataString = productMarketingMaterialSourceDataString.replace(products, productsJsonNodeStr);	    
		this.dao.updateProductMarketingMaterialSource(companyKcode, productBaseCode, productMarketingMaterialSourceDataString);		
	}
	
	private void updateSkuForProductInfoMarketSide(int index, String companyKcode, String productBaseCode, String country,String originalSKU,String updatedSKU)throws JsonParseException, JsonMappingException, IOException{		
		ProductDetail productInfoMarketSide = this.dao.queryProductInfoMarketSide(companyKcode,productBaseCode,country);
		ObjectMapper objectMapper = new ObjectMapper();		
		String productInfoMarketSideData = productInfoMarketSide.getData();		
		JsonNode productInfoMarketSideJsonNode = objectMapper.readValue(productInfoMarketSideData, JsonNode.class);				
		String productInfoMarketSideDataString = productInfoMarketSideData.toString();		
		String products = productInfoMarketSideJsonNode.get("products").asText();				
		JsonNode productsJsonNode = objectMapper.readValue(products, JsonNode.class);				
		JsonNode elemSKU = null;				
		if(productsJsonNode.size() != 0){
			if(originalSKU.equals(((ArrayNode) productsJsonNode).get(index).get("SKU").asText())){
				elemSKU = ((ArrayNode) productsJsonNode).get(index);
				((ObjectNode) elemSKU).put("SKU", updatedSKU);
			}
		}				
		products = products.replace("\"", "\\\"");
		String productsJsonNodeStr = productsJsonNode.toString();
		productsJsonNodeStr = productsJsonNodeStr.replace("\"", "\\\"");		
		productInfoMarketSideDataString = productInfoMarketSideDataString.replace(products, productsJsonNodeStr);	     
		this.dao.updateProductInfoMarketSide(companyKcode, productBaseCode, country, productInfoMarketSideDataString);					
	}
	
	private void updateSkuForProductMarketingMaterialmarketSide(int index, String companyKcode, String productBaseCode, String country,String originalSKU,String updatedSKU)throws JsonParseException, JsonMappingException, IOException{		
		ProductDetail productMarketingMaterialMarketSide = this.dao.queryProductMarketingMaterialMarketSide(companyKcode,productBaseCode,country);
		ObjectMapper objectMapper = new ObjectMapper();	
		String productMarketingMaterialMarketSideData = productMarketingMaterialMarketSide.getData();		
		JsonNode productMarketingMaterialMarketSideJsonNode = objectMapper.readValue(productMarketingMaterialMarketSideData, JsonNode.class);				
		String productMarketingMaterialMarketSideString = productMarketingMaterialMarketSideData.toString();		
		String products = productMarketingMaterialMarketSideJsonNode.get("products").asText();				
		JsonNode productsJsonNode = objectMapper.readValue(products, JsonNode.class);		
		JsonNode elemSKU = null;		
		if(productsJsonNode.size() != 0){
			if(originalSKU.equals(((ArrayNode) productsJsonNode).get(index).get("SKU").asText())){
				elemSKU = ((ArrayNode) productsJsonNode).get(index);
				((ObjectNode) elemSKU).put("SKU", updatedSKU);
			}
		}				
		products = products.replace("\"", "\\\"");
		String productsJsonNodeStr = productsJsonNode.toString();
		productsJsonNodeStr = productsJsonNodeStr.replace("\"", "\\\"");		
		productMarketingMaterialMarketSideString = productMarketingMaterialMarketSideString.replace(products, productsJsonNodeStr);	    
		this.dao.updateProductMarketingMaterialMarketSide(companyKcode, productBaseCode, country, productMarketingMaterialMarketSideString);		
	}
		
	private ProductEditingStatusType getPreviousStatusToTransfer(ProductEditingStatusType status){
		return status.getPreviousStatusType(status);		
	}
	
	private ProductEditingStatusType getNextStatusToTransfer(ProductEditingStatusType status){		
		return status.getNextStatusType(status);		
	}
		
	

	@Override
	public Map<String,String> getSupplierKcodeToShortEnUsNameMap() {
		return this.companyRepo.querySupplierKcodeToShortEnUsNameMap();
	}

	@Override
	public boolean isDrsUser() {
		return Context.getCurrentUser().isDrsUser();
	}

	@Override
	public String getUserCompanyKcode() {		
		return Context.getCurrentUser().getCompanyKcode();
	}

	@Override
	public boolean isBaseProductCodeExist(String supplierKcode, String productBaseCode) {		
		return this.dao.isProductBaseCodeExist(supplierKcode, productBaseCode);
	}
	
	/*
	@Override
	public boolean isExecutable(StatusType status) {
		String userKcode = Context.getCurrentUser().getCompanyKcode();			
		Boolean isSupplier = this.companyRepo.isSupplier(userKcode);		
		if(isSupplier){
			switch (status) {
			 case PENDING_SUPPLIER_ACTION:
				return true;			 			
			 case PENDING_DRS_REVIEW:
				return false;			 	
			 case FINALIZED:
				return false;			 	
			 default:
		 		return false;		          
		  	}				
		}else{
			switch (status) {
			 case PENDING_SUPPLIER_ACTION:
				return false;						
			 case PENDING_DRS_REVIEW:
				return true;			 	
			 case FINALIZED:
				return true;			 	
			 default: 
				return false; 
		  	}			
		}				
	}*/
	
	@Override
	public boolean isExecutableForUpdatingSKUs(ProductEditingStatusType status) {		
		String userKcode = Context.getCurrentUser().getCompanyKcode();			
		Boolean isSupplier = this.companyRepo.isSupplier(userKcode);		
		if(isSupplier){
			if(status == ProductEditingStatusType.PENDING_SUPPLIER_ACTION)
				return true;
			else if(status == ProductEditingStatusType.PENDING_DRS_REVIEW)
				return false;
			else if(status == ProductEditingStatusType.FINALIZED)
				return false;
			else
				return false;
		}else{
			if(status == ProductEditingStatusType.PENDING_SUPPLIER_ACTION)
				return false;
			else if(status == ProductEditingStatusType.PENDING_DRS_REVIEW)
				return true;
			else if(status == ProductEditingStatusType.FINALIZED)
				return false;
			else
				return false;
		}		
	}
		
	/*
	@Override
	public Map<String,String> getSupplierKcodeToNameMap() {
		return this.companyRepo.querySupplierKcodeToShortEnUsNameMap();
	}*/

	@Override
	public String getPriceWithTax(String countryCode, BigDecimal price) {
		BigDecimal priceWithTax = price.add(price.multiply(this.getVateRate(Country.valueOf(countryCode))));
		return priceWithTax.divide(new BigDecimal("1"),2,RoundingMode.HALF_UP).toString();
	}
	
	private BigDecimal getVateRate(Country country){
		BigDecimal vatRate = country.getVatRate();
		return vatRate==null?BigDecimal.ZERO:vatRate;
	}	
	
	private void processProductSubmissionNotification(String supplierKcode, String baseProductCode, String productNameEnglish, String tabName, String marketSide){
		boolean sendNotify = Boolean.parseBoolean(((String)this.env.get("SEND_NOTIFY")));
		if(sendNotify){
			try{
				if(supplierKcode == null || supplierKcode.isEmpty()) {
					supplierKcode = Context.getCurrentUser().getCompanyKcode();
				}
				Locale locale = Context.getCurrentUser().getLocale();
				String subject = this.messageSource.getMessage("mail.product.change.subject", new Object[] {supplierKcode,baseProductCode,productNameEnglish,tabName,marketSide},locale);
				String body = this.messageSource.getMessage("mail.product.change.notification", new Object[] {supplierKcode,baseProductCode,productNameEnglish,tabName,marketSide,marketSide,baseProductCode,baseProductCode},locale);
				String signature = this.mailUtil.getSignature(SignatureType.NO_REPLY);
				body = this.mailUtil.appendSignature(body,signature);
				String [] mailTo = {ACCOUNT_MANAGERS};
				Assert.isTrue(!this.userRepo.isDrsUser(Context.getCurrentUser().getUserId()));
				String [] bcc = {};
				this.mailUtil.SendMimeWithBcc(mailTo,bcc, ADDRESS_NO_REPLY,subject,body);
			}
			catch(Exception e){
				this.mailUtil.Send(SOFTWARE_ENGINEERS, ADDRESS_NO_REPLY, "Create product modification notification email ERROR", e.getMessage()+"\n\n"+e.toString()+"\n\n"+e.fillInStackTrace());
			}
		}
	}

}