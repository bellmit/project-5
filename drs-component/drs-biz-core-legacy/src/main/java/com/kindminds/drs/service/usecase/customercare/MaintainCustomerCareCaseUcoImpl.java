package com.kindminds.drs.service.usecase.customercare;

import com.kindminds.drs.Context;
import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.usecase.customercare.MaintainCustomerCareCaseUco;
import com.kindminds.drs.api.v1.model.common.DtoList;
import com.kindminds.drs.api.v1.model.common.Pager;
import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseDto;
import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseDto.CustomerCareCaseMessage;
import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseDto.CustomerCareCaseMessage.MessageType;
import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseOrderInfo;
import com.kindminds.drs.api.data.access.rdb.CompanyDao;
import com.kindminds.drs.api.data.access.rdb.UserDao;
import com.kindminds.drs.api.data.access.usecase.customercare.MaintainCustomerCareCaseDao;
import com.kindminds.drs.api.data.access.usecase.customercare.MaintainCustomerCareCaseIssueDao;
import com.kindminds.drs.persist.v1.model.mapping.customercare.CustomerCareCaseDtoImpl;
import com.kindminds.drs.v1.model.impl.CustomerCaseSearchConditionImpl;
import com.kindminds.drs.service.util.MailUtil;
import com.kindminds.drs.service.util.MailUtil.SignatureType;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.query.MultiMatchQueryBuilder.Type;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;


@Service("maintainCustomerCareCaseUco")
public class MaintainCustomerCareCaseUcoImpl implements MaintainCustomerCareCaseUco {
	
	@Autowired private MaintainCustomerCareCaseDao dao;
	@Autowired private MaintainCustomerCareCaseIssueDao issueDao;
	@Autowired private CompanyDao companyRepo;
	@Autowired private UserDao userRepo;

	@Autowired private MailUtil mailUtil;
	@Autowired private MessageSource messageSource;	
	@Autowired @Qualifier("envProperties") private Properties env;
		
	private static final int PAGE_SIZE = 50;
	private final BigDecimal MSG_PER_WORD_FEE_USD = new BigDecimal("0.07");
	private final BigDecimal MSG_TRANSLATION_PER_WORD_FEE_USD = new BigDecimal("0.14");
	private final BigDecimal MSG_PER_STAND_ACTION_FEE_USD = new BigDecimal("1.4");
	private static final String ADDRESS_NO_REPLY = "DRS.network <drs-noreply@tw.drs.network>";
	private static final String SOFTWARE_ENGINEERS = "software.engineering@drs.network";


	private SearchSourceBuilder searchSourceBuilder;
	private SearchRequest searchRequest;
	
	@Value("${eshost}")
	private String esHost;

	@Value("${customer_care_cases_si}")
	private String searchIndex;



	@Override
	public int save(CustomerCareCaseDto ccc) {
		Assert.isTrue(ccc.getRelatedProductBaseCodeList()==null||ccc.getRelatedProductSkuCodeList()==null);
		int userId = Context.getCurrentUser().getUserId();
		Assert.isTrue(Context.getCurrentUser().isDrsUser());
		int caseId = this.dao.insert(userId,ccc);
		this.dao.updateLastUpdateTime(caseId,new Date());
		return caseId;
	}
	
	@Override
	public DtoList<CustomerCareCaseDto> getList(int pageIndex, String customerName) {
		CustomerCaseSearchConditionImpl condition = new CustomerCaseSearchConditionImpl();
		String userCompanyKcode = Context.getCurrentUser().getCompanyKcode();
		if(this.companyRepo.isSupplier(userCompanyKcode)) condition.setSupplierKcode(userCompanyKcode);
		if(customerName!=null) condition.setCustomerName(customerName);
		DtoList<CustomerCareCaseDto> list = new DtoList<CustomerCareCaseDto>();
		list.setTotalRecords(this.dao.queryCounts(condition));
		list.setPager(new Pager(pageIndex, list.getTotalRecords(),PAGE_SIZE));
		list.setItems(this.dao.queryList(list.getPager().getStartRowNum(), list.getPager().getPageSize(),condition));
		return list;
	}

	@Override
	public Integer getElasticHitCount(String searchWords) {

		searchRequest = createSearchRequest(searchWords);

		try (RestHighLevelClient restClient = new RestHighLevelClient(
				RestClient.builder(new HttpHost(this.esHost, 9200, "http")))) {

			SearchHits hits = restClient.search(searchRequest, RequestOptions.DEFAULT).getHits();
			return (int) hits.getTotalHits().value;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	@Override
	public DtoList<CustomerCareCaseDto> getListElastic(int pageIndex, String searchWords) {

    	searchRequest = createSearchRequest(searchWords);

		DtoList<CustomerCareCaseDto> list = new DtoList<CustomerCareCaseDto>();

		list.setTotalRecords(getElasticHitCount(searchWords));
		list.setPager(new Pager(pageIndex, list.getTotalRecords(),PAGE_SIZE));
		searchSourceBuilder.from(PAGE_SIZE * (pageIndex - 1)).size(PAGE_SIZE);

		try (RestHighLevelClient restClient = new RestHighLevelClient(
				RestClient.builder(new HttpHost(this.esHost, 9200, "http")))) {

			SearchHits hits = restClient.search(searchRequest, RequestOptions.DEFAULT).getHits();
	    	List<CustomerCareCaseDto> ccclist = new ArrayList<>();
	    	Integer maxCaseId = dao.queryMaxCaseId();
			for (SearchHit hit : hits.getHits()) {
				Map<String, Object> source = hit.getSourceAsMap();
				if ((Integer)source.get("id") > maxCaseId) continue;
				if (containsId(ccclist, (Integer) source.get("id"))) continue;
				
				ccclist.add(issueFromHitSource(source));
			}
			list.setItems(ccclist);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("esHost: " + this.esHost);
		System.out.println("searchIndex: " + this.searchIndex);
		return list;
	}

	private boolean containsId(List<CustomerCareCaseDto> customerCaseList, Integer id) {
		for (CustomerCareCaseDto customerCase : customerCaseList) {
			if (customerCase.getCaseId().equals(id)) return true;
		}
		return false;
	}

	private SearchRequest createSearchRequest(String searchWords) {

		searchSourceBuilder = new SearchSourceBuilder();

		searchSourceBuilder.query(buildSearchQuery(searchWords))
				.sort("last_update_time", SortOrder.DESC)
				.size(0).trackTotalHits(true);

		return new SearchRequest(this.searchIndex)
				.source(searchSourceBuilder);
	}
	
	private QueryBuilder buildSearchQuery(String searchWords) {
    	BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
		String userCompanyKcode = Context.getCurrentUser().getCompanyKcode();
		if(this.companyRepo.isSupplier(userCompanyKcode)) {
	    	boolQueryBuilder.must(includeCompanyQuery(userCompanyKcode));
		}
		if (!StringUtils.hasText(searchWords)) {
			return boolQueryBuilder.must(new MatchAllQueryBuilder());
		}
		if (searchWords.startsWith("issueId:")) {
			return boolQueryBuilder.must(new TermQueryBuilder("issue_id", searchWords.substring(8)));
		}
	    return boolQueryBuilder.must(includeSearchWordsDisMaxQuery(searchWords));
	}
	
	private MatchPhraseQueryBuilder includeCompanyQuery(String kcode) {
		String companyEnglishShortName = dao.queryCompanyEnglishShortName(kcode);
    	return new MatchPhraseQueryBuilder("supplier_en_name", companyEnglishShortName).slop(0);
	}
	
	private DisMaxQueryBuilder includeSearchWordsDisMaxQuery(String searchWords) {
		DisMaxQueryBuilder disMaxQuery = new DisMaxQueryBuilder();
		disMaxQuery.tieBreaker(0.7f);
		if (searchWords.matches("(0|[1-9]\\d*)")) {
			TermQueryBuilder queryId = 
					new TermQueryBuilder("id", searchWords).boost(1.5f);
			disMaxQuery.add(queryId);
		}
		MultiMatchQueryBuilder multiMatch = new MultiMatchQueryBuilder(searchWords, 
				"customer_name", "supplier_en_name", "product_base_code", 
				"product_sku_code", "base_product_name", "sku_product_name",
				"marketplace_order_id", "issue_name", "issue_type_name");
		multiMatch.type(Type.PHRASE_PREFIX);
		disMaxQuery.add(multiMatch);
		
		String singleWord = searchWords.replace(" ", "_");
		MatchPhrasePrefixQueryBuilder queryIssueCategory = 
				new MatchPhrasePrefixQueryBuilder("issue_type_category_name", singleWord);
		MatchPhrasePrefixQueryBuilder queryCaseType = 
				new MatchPhrasePrefixQueryBuilder("type", singleWord.replace("&", "N"));
		if (singleWord.equals("close") || singleWord.equals("closed")) {
			singleWord = "case" + singleWord;
		}
		MatchPhrasePrefixQueryBuilder queryStatus = 
				new MatchPhrasePrefixQueryBuilder("status", singleWord);
		disMaxQuery.add(queryIssueCategory);
		disMaxQuery.add(queryStatus);
		disMaxQuery.add(queryCaseType);
		return disMaxQuery;
	}
	
	private CustomerCareCaseDtoImpl issueFromHitSource(Map<String, Object> source) {
		Integer secondsFromLastActivity = 
				(int) ((new Date().getTime() - (Long) source.get("last_update_time")) / 1000);
		return new CustomerCareCaseDtoImpl((Integer) source.get("id"),
				(String) source.get("type"), 
				(Integer) source.get("issue_type_category_id"), 
				(String) source.get("marketplace_name"), 
				(String) source.get("customer_name"), 
				(Long) source.get("date_create"), 
				(String) source.get("status"), 
				secondsFromLastActivity, 
				(Integer) source.get("issue_id"), 
				(String) source.get("product_sku_code"), 
				(String) source.get("product_base_code"));
	}
	
	@Override
	public CustomerCareCaseDto get(int id) {
		String userCompanyKcode = Context.getCurrentUser().getCompanyKcode();
		if(this.companyRepo.isSupplier(userCompanyKcode)){
			String supplierKcode = this.dao.querySupplierKcodeOfCase(id);
			Assert.isTrue(userCompanyKcode.equals(supplierKcode));
		}
		return this.dao.query(id);
	}
	
	@Override
	public int update(CustomerCareCaseDto customerCareCaseDto) {
		int caseId = this.dao.update(customerCareCaseDto);
		this.dao.updateLastUpdateTime(caseId,new Date());
		return caseId;
	}

	@Override
	public int updateStatus(CustomerCareCaseDto customerCareCaseDto) {
		int caseId = this.dao.updateStatus(customerCareCaseDto);
		this.dao.updateLastUpdateTime(caseId,new Date());
		return caseId;
	}
	
	@Override
	public int delete(int id) {
		if(this.isCaseDeletable(id)) return this.dao.delete(id);
		return 0;
	}

	private boolean isCaseDeletable(int id) {
		List<CustomerCareCaseMessage> msgs = this.dao.queryMessages(id);
		for(CustomerCareCaseMessage msg:msgs){
			if(msg.getMs2ssStatementId()!=null||msg.getSs2spStatementId()!=null) return false;
		}
		return true;
	}

	@Override
	public CustomerCareCaseOrderInfo getOrderDateById(String orderId) {
		return this.dao.queryOrderInfoById(orderId);
	}

	@Override
	public CustomerCareCaseMessage getMessage(int caseId, int lineSeq) {
		return this.dao.queryMessage(caseId, lineSeq);
	}

	@Override
	public int addMessage(int caseId, CustomerCareCaseMessage msg) {
		Assert.notNull(MessageType.fromValue(msg.getMessageType()));
		int userId = Context.getCurrentUser().getUserId();
		Assert.isTrue(Context.getCurrentUser().isDrsUser());
		Assert.isTrue(msg.getMessageType().equals(MessageType.CUSTOMER.getValue())||msg.getMessageType().equals(MessageType.MSDC.getValue()));
		int lineSeq = 0;
		if(msg.getMessageType().equals(MessageType.CUSTOMER.getValue())){
			lineSeq = this.dao.insertCustomerMessage(userId,caseId, msg);
		}
		if(msg.getMessageType().equals(MessageType.MSDC.getValue())){
			lineSeq = this.dao.insertMsdcMessage(userId,caseId, msg, this.calculateChargeByDrs(msg));
			this.processCreateMessageFromMSDCNotificationToSupplier(caseId);			
		}
		this.dao.updateLastUpdateTime(caseId,new Date());
		return lineSeq;
	}
	
	private void processCreateMessageFromMSDCNotificationToSupplier(int caseId){
		boolean sendNotify = Boolean.parseBoolean(((String)this.env.get("SEND_NOTIFY")));
		//todo ralph testing
		if(sendNotify){
			try{
				Locale locale = Context.getCurrentUser().getLocale();
				String subject = this.messageSource.getMessage("mail.CustomerCareCases-SendMessageFromMSDC-Subject", new Object[] {String.valueOf(caseId)}, locale);
				String body = this.messageSource.getMessage("mail.CustomerCareCases-SendMessageFromMSDC-Body", new Object[] {String.valueOf(caseId),String.valueOf(caseId),String.valueOf(caseId)},locale);
				String signature = this.mailUtil.getSignature(SignatureType.NO_REPLY);
				body = this.mailUtil.appendSignature(body,signature);

				List<String> supplierEmailList = this.companyRepo.querySupplierServiceEmailAddressList(this.dao.querySupplierKcodeOfCase(caseId));
				String [] mailTo = supplierEmailList.toArray(new String[supplierEmailList.size()]);
				Assert.isTrue(this.userRepo.isDrsUser(Context.getCurrentUser().getUserId()));

				String currentUserEmail = this.userRepo.queryUserMail(Context.getCurrentUser().getUserId());
				String [] bcc = {currentUserEmail};
				this.mailUtil.SendMimeWithBcc(mailTo,bcc, ADDRESS_NO_REPLY,subject,body);
			}
			catch(Exception e){
				this.mailUtil.Send(SOFTWARE_ENGINEERS, ADDRESS_NO_REPLY, "Create message from MSDC confirmation email ERROR", e.getMessage()+"\n\n"+e.toString()+"\n\n"+e.fillInStackTrace());
			}
		}
	}
		
	private BigDecimal calculateChargeByDrs(CustomerCareCaseMessage msg) {
		BigDecimal total = BigDecimal.ZERO;
//		if(msg.getReplacementRefundInvoices()!=null){
//			BigDecimal rplcRfndInv = new BigDecimal(msg.getReplacementRefundInvoices());
//			total = total.add(rplcRfndInv.multiply( new BigDecimal("1.5") ));
//		}
		if(msg.getWordCount()!=null){
			BigDecimal wc = new BigDecimal(msg.getWordCount());
			if(msg.getIncludesTranslationFee())
				total = total.add(wc.multiply(this.MSG_TRANSLATION_PER_WORD_FEE_USD));
			else
				total = total.add(wc.multiply(this.MSG_PER_WORD_FEE_USD));
		}
		if(msg.getStandardActionCount()!=null){
			BigDecimal ea = new BigDecimal(msg.getStandardActionCount());
			total = total.add(ea.multiply(this.MSG_PER_STAND_ACTION_FEE_USD));
		}
		return total;
	}

	@Override
	public CustomerCareCaseMessage updateMessage(int caseId, CustomerCareCaseMessage msg) {
		Assert.notNull(MessageType.fromValue(msg.getMessageType()));
		int userId = Context.getCurrentUser().getUserId();
		Assert.isTrue(Context.getCurrentUser().isDrsUser());
		Integer lineSeq=null;
		if(msg.getMessageType().equals(MessageType.CUSTOMER.getValue())){
			lineSeq = this.dao.updateCustomerMessage(userId,caseId, msg);
			this.dao.updateLastUpdateTime(caseId,new Date());
		}
		else if(msg.getMessageType().equals(MessageType.MSDC.getValue())){
			if(this.isMsdcMessageModifiable(caseId, msg.getLineSeq())){
				lineSeq =  this.dao.updateMsdcMessage(userId,caseId, msg, this.calculateChargeByDrs(msg));
				this.dao.updateLastUpdateTime(caseId,new Date());
			}
		}
		return lineSeq==null?null:this.dao.queryMessage(caseId, lineSeq);
	}
	
	@Override
	public boolean deleteMessage(int caseId, int lineSeq) {
		if(!this.isMsdcMessageModifiable(caseId, lineSeq)) return false;
		return this.dao.deleteMessage(caseId, lineSeq);
	}
	
	private boolean isMsdcMessageModifiable(int caseId, int msgLineSeq){
		String ss2spStatementName = this.dao.queryMsdcMsgSs2spStatementName(caseId, msgLineSeq);
		String ms2ssStatementName = this.dao.queryMsdcMsgMs2ssStatementName(caseId, msgLineSeq);
		if(ss2spStatementName==null&&ms2ssStatementName==null) return true;
		return false;
	}



	@Override
	public List<String> getTypeList() {
		return this.dao.queryTypeList();
	}
	
	@Override
	public List<Marketplace> getMarketplaceList() {
		return Marketplace.getMarketplaceList();
	}
			
	@Override
	public Map<Integer, String> getMarketplaceIdToDrsCompanyMap() {
		Map<Integer,String> marketplaceIdToDrsCompanyMap = new HashMap<Integer,String>();
		Map<String,String> contactChannelNameToDrsCompanyShortEnUsNameMap =  this.dao.queryContactChannelNameToDrsCompanyShortEnUsNameMap();		
		for (Map.Entry<String, String> entry : contactChannelNameToDrsCompanyShortEnUsNameMap.entrySet())
		{			
			marketplaceIdToDrsCompanyMap.put(Marketplace.fromName(entry.getKey()).getKey(), entry.getValue());		    
		}
		return marketplaceIdToDrsCompanyMap;
	}

	@Override
	public Map<String, String> getMarketplaceToDrsCompanyMap() {
		Map<String, String> marketplaceToDrsCompanyMap = new HashMap<>();
		Map<String,String> contactChannelNameToDrsCompanyShortEnUsNameMap =  this.dao.queryContactChannelNameToDrsCompanyShortEnUsNameMap();		
		for (Map.Entry<String, String> entry : contactChannelNameToDrsCompanyShortEnUsNameMap.entrySet())
		{
			marketplaceToDrsCompanyMap.put(Marketplace.fromName(entry.getKey()).toString(), entry.getValue());			
		}
		return marketplaceToDrsCompanyMap;
	}
		
	@Override
	public Map<String,String> getProductBaseCodeToSupplierNameMap(String supplierKcode) {
		Assert.notNull(supplierKcode);
		return this.dao.queryProductBaseCodeToSupplierNameMap(supplierKcode);
	}

	@Override
	public Map<String,String> getProductSkuCodeToSupplierNameMap(String supplierKcode) {
		return this.dao.queryProductSkuCodeToSupplierNameMap(supplierKcode);
	}
	
	@Override
	public Map<String, String> getProductSkuCodeToSupplierNameMapUnderBases(List<String> baseCodes) {
		return this.dao.queryProductSkuCodeToSupplierNameMapUnderBases(baseCodes);
	}

	@Override
	public Map<String,String> getSupplierKcodeToShortEnUsNameMap() {
		return this.companyRepo.querySupplierKcodeToShortEnUsNameMap();
	}

	@Override
	public Map<String, String> getDrsCompanyKcodeToShortEnUsNameMap() {
		return this.companyRepo.queryDrsCompanyKcodeToShortEnUsNameMap();
	}

	@Override
	public Map<String, String> getProductBaseCodeToSupplierNameMapByMarketplace(String supplierKcode,
			Marketplace marketplace) {
		Assert.notNull(supplierKcode);
		Assert.notNull(marketplace);
		return this.dao.queryProductBaseCodeToSupplierNameMapByMarketplace(supplierKcode,marketplace.getKey())				;
	}

	@Override
	public Map<String, String> getProductSkuCodeToSupplierNameMapByMarketplace(String supplierKcode,
			Marketplace marketplace) {
		Assert.notNull(supplierKcode);
		Assert.notNull(marketplace);
		return this.dao.queryProductSkuCodeToSupplierNameMapByMarketplace(supplierKcode, marketplace.getKey());
	}
	
	@Override
	public Map<Integer, String> getApplicableTemplate(int caseId) {
		return this.dao.queryApplicableTemplate(caseId);
	}

	@Override
	public String getTemplateContent(int templateId) {
		return this.dao.queryTemplateContent(templateId);
	}
	
	@Override
	public Map<Integer,String> getIssueCategoryIdToNameMap() {
		return this.issueDao.queryCategoryIdToNameMap();
	}

	@Override
	public Map<Integer,String> getTypeIdToNameMap(Integer categoryId) {
		return this.issueDao.queryTypeIdToNameMap(categoryId);
	}

	@Override
	public Map<Integer,String> getIssueIdToEnUsNameMap(Integer typeCategoryId,Integer typeId) {
		return this.dao.queryAllIssueIdToEnUsNameMap(typeCategoryId,typeId);
	}

	@Override
	public Map<Integer,String> getTemplates(int issueId) {
		return this.dao.queryTemplateIdToDisplayName(issueId);
	}

}
