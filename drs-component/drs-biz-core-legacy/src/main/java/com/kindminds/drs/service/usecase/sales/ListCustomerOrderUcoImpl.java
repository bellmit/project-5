package com.kindminds.drs.service.usecase.sales;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindminds.drs.Context;
import com.kindminds.drs.Marketplace;
import com.kindminds.drs.UserInfo;

import com.kindminds.drs.api.usecase.sales.ListCustomerOrderUco;
import com.kindminds.drs.api.v1.model.accounting.AmazonOrderDetail;
import com.kindminds.drs.api.v1.model.common.DtoList;
import com.kindminds.drs.api.v1.model.common.Pager;
import com.kindminds.drs.api.v1.model.sales.CustomerOrder;
import com.kindminds.drs.api.v1.model.sales.CustomerOrderExporting;
import com.kindminds.drs.api.v1.model.sales.ListCustomerOrderCondition;
import com.kindminds.drs.api.v1.model.sales.ShopifyOrderItemDetail;
import com.kindminds.drs.api.data.access.rdb.CompanyDao;
import com.kindminds.drs.api.data.access.usecase.sales.ListCustomerOrderDao;
import com.kindminds.drs.api.data.access.usecase.sales.ListCustomerOrderDao.ShopifyJsonDetailType;
import com.kindminds.drs.api.v2.biz.domain.model.SalesChannel;
import com.kindminds.drs.enums.SkuDisplayOption;
import com.kindminds.drs.persist.v1.model.mapping.sales.CustomerOrderImpl;
import com.kindminds.drs.v1.model.impl.ListCustomerOrderConditionImplSvc;
import com.kindminds.drs.v1.model.impl.ShopifyOrderItemDetailImpl;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Service
public class ListCustomerOrderUcoImpl implements ListCustomerOrderUco {
	
	@Autowired private ListCustomerOrderDao dao;
	@Autowired private CompanyDao companyRepo;

	@Value("${eshost}")
	private String esHost;

	@Value("${orders_si}")
	private String searchIndex;

	private final static int PAGE_SIZE = 20;

	@Override
	public boolean isCurrentUserDrsUser() {
		return Context.getCurrentUser().isDrsUser();
	}

	@Override
	public boolean conditionsAreAllNull(ListCustomerOrderCondition condition){
		return condition.getSupplierKcode()==null
			&& condition.getOrderDateStart()==null
			&& condition.getOrderDateEnd()==null
			&& condition.getTransactionDateStart()==null
			&& condition.getTransactionDateEnd()==null
			&& condition.getRelatedBaseProductCode()==null
			&& condition.getRelatedSkuCode()==null
			&& condition.getAmazonMerchantSKU()==null
			&& condition.getSalesChannelId()==null
			&& condition.getMarketplaceOrderId()==null
			&& condition.getCustomerName()==null
			&& condition.getBuyerEmail()==null
			&& condition.getPromotionId()==null
			&& condition.getAsin()==null
			&& condition.getOrderStatus()==null
			&& condition.getSearchTerms()==null;
	}

	@Override
	public List<SalesChannel> getSalesChannels() {
		List<SalesChannel> salesChannels = new ArrayList<>();
		salesChannels.add(SalesChannel.AMAZON_COM);
		salesChannels.add(SalesChannel.TRUETOSOURCE);
		salesChannels.add(SalesChannel.AMAZON_CO_UK);
		salesChannels.add(SalesChannel.AMAZON_CA);
		salesChannels.add(SalesChannel.AMAZON_DE);
		salesChannels.add(SalesChannel.AMAZON_FR);
		salesChannels.add(SalesChannel.AMAZON_IT);
		salesChannels.add(SalesChannel.AMAZON_ES);
		salesChannels.add(SalesChannel.EBAY);
		salesChannels.add(SalesChannel.ALL_CHANNEL);
		salesChannels.add(SalesChannel.NON_ORDER);
		return salesChannels;
	}

	//TODO
	@Override
	public Map<String,String> getSupplierKcodeToNameMap() {
		//return this.companyRepo.querySupplierKcodeToShortEnUsNameMap();
		return this.companyRepo.querySupplierKcodeToShortEnUsNameWithRetailMap();
	}

	@Override
	public Map<String,String> getProductBaseCodeToNameMap(String companyKcode) {
		if(this.companyRepo.isSupplier(companyKcode)){
			return this.dao.queryProductBaseCodeToNameMap(companyKcode);
		}
		return new HashMap<String,String>(1);
	}

	@Override
	public Map<String, String> getProductSkuCodeToNameMap(String productBaseCode) {
		return this.dao.queryProductSkuCodeToNameMap(productBaseCode);
	}

	@Override
	public DtoList<CustomerOrder> getList(ListCustomerOrderCondition condition, int pageIndex) {

		String userCompanyKcode = Context.getCurrentUser().getCompanyKcode();
		boolean isDrsUser = Context.getCurrentUser().isDrsUser();
		if(this.conditionsAreAllNull(condition)) return this.getDefaultList(pageIndex, isDrsUser,userCompanyKcode);
		ListCustomerOrderCondition actualCondition = this.getActualCondition(isDrsUser, condition, userCompanyKcode);

		SkuDisplayOption option = this.getSkuDisplayOption(isDrsUser);
		int count = this.dao.queryListCount(actualCondition);
		Pager pager = new Pager(pageIndex,count,20);
		DtoList<CustomerOrder> dtoList = new DtoList<CustomerOrder>();
		dtoList.setTotalRecords(count);
		dtoList.setPager(pager);
		dtoList.setItems(this.dao.queryList(actualCondition, option, pager.getStartRowNum(), pager.getPageSize()));
		return dtoList;

	}

	@Override
	public List<CustomerOrder> getList(ListCustomerOrderCondition condition) {
		String userCompanyKcode = Context.getCurrentUser().getCompanyKcode();
		boolean isDrsUser = Context.getCurrentUser().isDrsUser();
		if(this.conditionsAreAllNull(condition)) return this.getDefaultAllList(isDrsUser,userCompanyKcode);
		ListCustomerOrderCondition actualCondition = this.getActualCondition(isDrsUser, condition, userCompanyKcode);
		SkuDisplayOption option = this.getSkuDisplayOption(isDrsUser);
		return this.dao.queryList(actualCondition, option, null, null);
	}
	
	@Override
	public List<CustomerOrderExporting> getListForExporting(ListCustomerOrderCondition condition) {

		String userCompanyKcode = Context.getCurrentUser().getCompanyKcode();
		boolean isDrsUser = Context.getCurrentUser().isDrsUser();
		if(this.conditionsAreAllNull(condition)) return this.getDefaultAllListForExporting(isDrsUser,userCompanyKcode);
		ListCustomerOrderCondition actualCondition = this.getActualCondition(isDrsUser, condition, userCompanyKcode);

		SkuDisplayOption option = this.getSkuDisplayOption(isDrsUser);

		return this.dao.queryExportingList(actualCondition, option);

	}

	public DtoList<CustomerOrder> getListElastic(String searchTerms, Integer pageIndex, Long startTime, Long endTime) {

		UserInfo user = Context.getCurrentUser();

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
				.query(buildSearchQuery(searchTerms, startTime, endTime, user))
				.sort("order_time", SortOrder.DESC)
				.size(0).trackTotalHits(true);

		SearchRequest searchRequest = new SearchRequest(this.searchIndex)
				.source(searchSourceBuilder);


		DtoList<CustomerOrder> list = new DtoList<>();
		try (RestHighLevelClient restClient = new RestHighLevelClient(
				RestClient.builder(
						new HttpHost(this.esHost, 9200, "http")))) {

			SearchHits hits = restClient.search(searchRequest, RequestOptions.DEFAULT)
					.getHits();

			list.setTotalRecords((int) hits.getTotalHits().value);
			list.setPager(new Pager(pageIndex, list.getTotalRecords(),PAGE_SIZE));
			searchSourceBuilder.from(PAGE_SIZE * (pageIndex - 1)).size(PAGE_SIZE);

			hits = restClient.search(searchRequest, RequestOptions.DEFAULT).getHits();
			List<CustomerOrder> orderList = new ArrayList<>();

			int count = 1;
			for (SearchHit hit : hits.getHits()) {
				Map<String, Object> source = hit.getSourceAsMap();

				orderList.add(getOrderFromHitSource(source, user));
			}
			list.setItems(orderList);
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("esHost: " + this.esHost);
		System.out.println("searchIndex: " + this.searchIndex);
		return list;
	}

	private QueryBuilder buildSearchQuery(String searchWords, Long startTime, Long endTime, UserInfo user) {
		BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
		String userCompanyKcode = user.getCompanyKcode();

		if(user.isSupplier()) {
			boolQueryBuilder.must(includeCompanyQuery(userCompanyKcode));
		}
		if (startTime != null) {
			boolQueryBuilder.must(new RangeQueryBuilder("order_time").gte(startTime));
		}
		if (endTime != null) {
			boolQueryBuilder.must(new RangeQueryBuilder("order_time").lte(endTime));
		}
		if (!StringUtils.hasText(searchWords)) {
			return boolQueryBuilder.must(new MatchAllQueryBuilder());
		}
		return boolQueryBuilder.must(includeSearchWordsDisMaxQuery(searchWords));
	}

	private MatchPhraseQueryBuilder includeCompanyQuery(String kcode) {
		return new MatchPhraseQueryBuilder("com_code", kcode).slop(0);
	}

	private DisMaxQueryBuilder includeSearchWordsDisMaxQuery(String searchWords) {
		DisMaxQueryBuilder disMaxQuery = new DisMaxQueryBuilder();
		disMaxQuery.tieBreaker(0.7f);

		MultiMatchQueryBuilder multiMatch = new MultiMatchQueryBuilder(searchWords,
				"marketplace_order_id", "shopify_order_id", "promotion_id", "asin",
				"com_code", "base_code", "sku_code", "product_name", "buyer", "buyer_email", "sales_channel",
				"fulfillment_center");
		multiMatch.type(MultiMatchQueryBuilder.Type.PHRASE_PREFIX);
		disMaxQuery.add(multiMatch);

		String singleWord = searchWords.replace(" ", "_");
		MatchPhrasePrefixQueryBuilder queryStatus =
				new MatchPhrasePrefixQueryBuilder("order_status", singleWord);
		disMaxQuery.add(queryStatus);
		return disMaxQuery;
	}

	private CustomerOrderImpl getOrderFromHitSource(Map<String, Object> source, UserInfo user) {
//		System.out.println("order_time: " + source.get("order_time"));
//		System.out.println("actual_retail_price: " + source.get("actual_retail_price"));
//		System.out.println("transaction_time: " + source.get("transaction_time"));
		Long order_time = (Long) source.get("order_time");
		String marketplace_order_id = (String) source.get("marketplace_order_id");
		String shopify_order_id = (String) source.get("shopify_order_id");
		String order_status = (String) source.get("order_status");
		String sku_code;
		if (user.isDrsUser()) {
			sku_code = (String) source.get("sku_code_by_drs");
		} else {
			sku_code = (String) source.get("sku_code_by_supplier");
		}
		String base_code = (String) source.get("base_code");
		if (!StringUtils.hasText(sku_code)) {
			sku_code = base_code;
		}
		String asin = (String) source.get("asin");
		String com_code = (String) source.get("com_code");
		String product_name = (String) source.get("product_name");
		Double item_price = Double.parseDouble(source.get("item_price").toString());
		Double actual_retail_price =  Double.parseDouble(source.get("actual_retail_price").toString());
		Integer qty = (Integer) source.get("qty");
		String buyer = (String) source.get("buyer");
		String buyer_email = (String) source.get("buyer_email");
		String sales_channel = (String) source.get("sales_channel");
		String fulfillment_center = (String) source.get("fulfillment_center");
		String refund_dt_id = (String) source.get("refund_dt_id");
		Long transaction_time = (Long) source.get("transaction_time");
		String promotion_id = (String) source.get("promotion_id");
		String city = (String) source.get("city");
		String state = (String) source.get("state");
		String country = (String) source.get("country");
		return new CustomerOrderImpl(
				order_time,
				marketplace_order_id,
				shopify_order_id,
				order_status,
				sku_code,
				base_code,
				asin,
				com_code,
				product_name,
				item_price,
				actual_retail_price,
				qty,
				buyer,
				buyer_email,
				sales_channel,
				fulfillment_center,
				refund_dt_id,
				transaction_time,
				promotion_id,
				city,
				state,
				country
		);
	}

	@Override
	public AmazonOrderDetail getAmazonOrderDetail(String salesChannel, String orderId, String sku) {
		Assert.isTrue(StringUtils.hasText(salesChannel));
		Marketplace marketplace = Marketplace.fromName(salesChannel);
		return this.dao.queryAmazonOrderDetail(marketplace,orderId,sku);
	}
	
	@Override
	public ShopifyOrderItemDetail getShopifyOrderDetail(String orderId, String sku) {
		Map<String,String> addressDataMap = this.getAddressDataMap(this.dao.queryShopifyJsonData(ShopifyJsonDetailType.DESTINATION_LOCATION, orderId, sku));				
		ShopifyOrderItemDetailImpl shopifyOrderDetail = new ShopifyOrderItemDetailImpl(
				this.dao.queryShopifyFieldData("sosi.code",orderId,sku),
				addressDataMap.get("shippingCountry"),
				addressDataMap.get("shippingState"),
				addressDataMap.get("shippingCity"),
				addressDataMap.get("shippingPostalCode"),
				this.dao.queryShopifyFieldData("soi.fulfillment_service",orderId,sku),
				this.getTax(this.dao.queryShopifyJsonData(ShopifyJsonDetailType.TAX_LINES, orderId, sku)),
				this.dao.queryShopifyFieldData("soi.total_discount",orderId,sku)
				);			
		return shopifyOrderDetail;
	}
	
	private Map<String,String> getAddressDataMap(String jsonDestinationLocation){
		Map<String,String> addressDataMap = new TreeMap<String,String>();
		ObjectMapper mapper = new ObjectMapper();		
		try {
			JsonNode node = mapper.readTree(jsonDestinationLocation);
			addressDataMap.put("shippingCountry", node.get("country_code").asText());
			addressDataMap.put("shippingState", node.get("province_code").asText());			
			addressDataMap.put("shippingCity", node.get("city").asText());			
			addressDataMap.put("shippingPostalCode", node.get("zip").asText());	
			return addressDataMap;			
		} catch (Exception e) {
			//logger.info("Exception in ListCustomerOrderUcoImpl method getAddressDataMap: ", e);
		} 		
		return new TreeMap<String,String>();
	}
		
	private String getTax(String jsonTaxLines){
		BigDecimal tax = new BigDecimal("0");					
		ObjectMapper mapper = new ObjectMapper();		
		try {
			JsonNode jsonTaxLinesNode = mapper.readTree(jsonTaxLines);
			for(JsonNode jsonTaxLineNode:jsonTaxLinesNode){				
				tax = tax.add(new BigDecimal(jsonTaxLineNode.get("price").asText()));								
			}	
			return tax.toString();					
		} catch (Exception e) {			
			e.printStackTrace();
		} 		
		return null;
	}
		
	private DtoList<CustomerOrder> getDefaultList(int pageIndex,boolean isDrsUser,String userCompanyKcode) {
		if(isDrsUser){
			return this.getDtoListForDrsUser(pageIndex);
		} else {
			return this.getDtoListForSupplier(pageIndex, userCompanyKcode);
		}
	}
	
	private List<CustomerOrder> getDefaultAllList(boolean isDrsUser,String userCompanyKcode) {		
		if(isDrsUser){
			return this.dao.queryList(7,SkuDisplayOption.DRS, null, null);
		} else {
			return this.dao.queryList(28,userCompanyKcode,SkuDisplayOption.SUPPLIER,null,null);
		}
	}
	
	private List<CustomerOrderExporting> getDefaultAllListForExporting(boolean isDrsUser,String userCompanyKcode) {
		if(isDrsUser){
			return this.dao.queryExportingList(7,SkuDisplayOption.DRS);
		} else {
			return this.dao.queryExportingList(28,userCompanyKcode,SkuDisplayOption.SUPPLIER);
		}
	
	}
	
	private DtoList<CustomerOrder> getDtoListForDrsUser(int pageIndex){
		int lastDays = 7;
		int count = this.dao.queryListCount(lastDays);
		Pager pager = new Pager(pageIndex,count,20);
		DtoList<CustomerOrder> dtoList = new DtoList<CustomerOrder>();
		dtoList.setTotalRecords(count);
		dtoList.setPager(pager);
		dtoList.setItems(this.dao.queryList(lastDays, SkuDisplayOption.DRS, pager.getStartRowNum(),pager.getPageSize()));
		return dtoList;
	}
		
	private DtoList<CustomerOrder> getDtoListForSupplier(int pageIndex,String userCompanyKcode){
		int lastDays = 28;
		int count = this.dao.queryListCount(lastDays,userCompanyKcode);
		Pager pager = new Pager(pageIndex,count,20);
		DtoList<CustomerOrder> dtoList = new DtoList<CustomerOrder>();
		dtoList.setTotalRecords(count);
		dtoList.setPager(pager);
		dtoList.setItems(this.dao.queryList(lastDays,userCompanyKcode,SkuDisplayOption.SUPPLIER, pager.getStartRowNum(),pager.getPageSize()));
		return dtoList;
	}
	
	private ListCustomerOrderCondition getActualCondition(boolean isDrsUser,ListCustomerOrderCondition originCondition, String userCompanyKcode){
		if(isDrsUser) return originCondition;
		else return new ListCustomerOrderConditionImplSvc(originCondition,userCompanyKcode);
	}
	
	private SkuDisplayOption getSkuDisplayOption(boolean isDrsUser){
		if(isDrsUser) return SkuDisplayOption.DRS;
		else return SkuDisplayOption.SUPPLIER;
	}

}
