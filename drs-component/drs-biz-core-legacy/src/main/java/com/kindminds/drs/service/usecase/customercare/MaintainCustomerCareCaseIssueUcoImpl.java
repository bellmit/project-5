package com.kindminds.drs.service.usecase.customercare;

import com.kindminds.drs.Context;
import com.kindminds.drs.Locale;
import com.kindminds.drs.api.usecase.customercare.MaintainCustomerCareCaseIssueUco;
import com.kindminds.drs.api.usecase.customercare.MaintainCustomerCareCaseUco;
import com.kindminds.drs.api.v1.model.common.DtoList;
import com.kindminds.drs.api.v1.model.common.Pager;
import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseIssue;
import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseIssue.CustomerCareCaseIssueComment;
import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseIssue.CustomerCareCaseIssueStatus;
import com.kindminds.drs.api.data.access.rdb.CompanyDao;
import com.kindminds.drs.api.data.access.rdb.UserDao;
import com.kindminds.drs.api.data.access.usecase.customercare.MaintainCustomerCareCaseIssueDao;
import com.kindminds.drs.persist.v1.model.mapping.customercare.CustomerCareCaseIssueImpl;
import com.kindminds.drs.service.usecase.builder.CustomerIssueDtoListBuilder;
import com.kindminds.drs.service.usecase.builder.CustomerIssueDtoListBuilderFilterByCategory;
import com.kindminds.drs.service.usecase.builder.CustomerIssueDtoListBuilderFilterByNon;
import com.kindminds.drs.service.usecase.builder.CustomerIssueDtoListBuilderFilterByType;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service("maintainCustomerCareCaseIssueUco")
public class MaintainCustomerCareCaseIssueUcoImpl implements MaintainCustomerCareCaseIssueUco {
	
	@Autowired private MaintainCustomerCareCaseIssueDao dao;
	@Autowired private MaintainCustomerCareCaseUco caseUco;
	@Autowired private UserDao userRepo;
	@Autowired private CompanyDao companyRepo;
	
	@Autowired private CustomerIssueDtoListBuilderFilterByNon builderFilterByNon;
	@Autowired private CustomerIssueDtoListBuilderFilterByCategory builderFilterByCategory;
	@Autowired private CustomerIssueDtoListBuilderFilterByType builderFilterByType;
	
	@Autowired private MailUtil mailUtil;
	@Autowired private MessageSource messageSource;	
	@Autowired @Qualifier("envProperties") private Properties env;
	
	private static final String ADDRESS_NO_REPLY = "DRS.network <drs-noreply@tw.drs.network>";
	private static final int PAGE_SIZE = 50;
	private final long hoursToRenotify = 24;

	private static final String SOFTWARE_ENGINEERS = "software.engineering@drs.network";

	private SearchSourceBuilder searchSourceBuilder;
	private SearchRequest searchRequest;
	
	
	@Value("${eshost}")
	private String esHost;
	
	@Value("${customer_care_cases_issues_si}")
	private String searchIndex;
	
	
	@Override
	public void doRenotify() {
		Map<Integer,Date> pendingSupplierIssueIdToLastCommentDateTimeMap = this.dao.queryNeedRenotifyIssueIdToLastCommentDateTimeMap(CustomerCareCaseIssueStatus.PENDING_SUPPLIER_ACTION.name());
		Date now = new Date();
		for(Integer issueId:pendingSupplierIssueIdToLastCommentDateTimeMap.keySet()){
			Date lastCommentDateTime = pendingSupplierIssueIdToLastCommentDateTimeMap.get(issueId);
			if(this.needToRenotifyNow(now, lastCommentDateTime, this.hoursToRenotify)){
				this.sendRenotifyForCommentToSupplier(issueId);
				this.dao.setNeedRenotifyForCommentToSupplier(issueId,false);
			}
		}
	}
	
	private boolean needToRenotifyNow(Date now, Date lastCommentDateTime, long hoursToRenotify){
		long hoursFromLastComment = TimeUnit.MILLISECONDS.toHours(now.getTime()-lastCommentDateTime.getTime());
		return (hoursFromLastComment>=hoursToRenotify);
	}
	
	private void sendRenotifyForCommentToSupplier(int issueId){
		boolean sendNotify = Boolean.parseBoolean(((String)this.env.get("SEND_NOTIFY")));
		if(sendNotify){
			try{				
				String issueName = this.dao.queryEnUsIssueName(issueId);								
				String subject = this.messageSource.getMessage("mail.Issue-reminderToSupplier-Subject", new Object[] {issueName}, java.util.Locale.TAIWAN);		
				String body = this.messageSource.getMessage("mail.Issue-reminderToSupplier-Body", new Object[] {Integer.toString(issueId),issueName},java.util.Locale.TAIWAN);								
				String signature = this.mailUtil.getSignature(SignatureType.NO_REPLY);		
				body = this.mailUtil.appendSignature(body,signature);

				List<String> serviceEmailList = this.companyRepo.querySupplierServiceEmailAddressList(this.dao.queryKcodeOfSupplierInIssue(issueId));
				String [] mailTo = serviceEmailList.toArray(new String[serviceEmailList.size()]);

				//String emailOfNewestDrsCommenter = this.dao.queryEmailOfNewestDrsCommenter(issueId);
				//Assert.notNull(emailOfNewestDrsCommenter);
				//String [] bcc = {emailOfNewestDrsCommenter};
				//this.mailUtil.SendMimeWithBcc(mailTo,bcc,this.mailAddressNoReply,subject,body);
				
				this.mailUtil.SendMime(mailTo, ADDRESS_NO_REPLY,subject,body);

			}
			catch(Exception e){
				this.mailUtil.Send(SOFTWARE_ENGINEERS, ADDRESS_NO_REPLY, "Renotify for comment to supplier email ERROR", e.getMessage()+"\n\n"+e.toString()+"\n\n"+e.fillInStackTrace());
			}
		}	
	}

	private CustomerIssueDtoListBuilder getBuilder(Integer categoryId, Integer typeId){
		if(categoryId==null&&typeId==null) return this.builderFilterByNon;
		if(categoryId!=null&&typeId==null) return this.builderFilterByCategory;
		if(categoryId!=null&&typeId!=null) return this.builderFilterByType;
		throw new IllegalArgumentException("method CustomerIssueDtoListBuilder: invalid Arguments");
	}

	@Override
	public String getDateTimeNowUtc() {
		OffsetDateTime offsetDateTime = OffsetDateTime.now(ZoneOffset.ofOffset("", ZoneOffset.ofHours(0)));     
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return offsetDateTime.format(formatter)+" "+"UTC";
	}

	@Override
	public DtoList<CustomerCareCaseIssue> getList(Integer categoryId,Integer typeId,int pageIndex) {
		String filterKcode = this.getFilterKcode();
		CustomerIssueDtoListBuilder builder = this.getBuilder(categoryId, typeId);
		builder.initialize(pageIndex, filterKcode, categoryId, typeId);
		builder.setTotalRecordCounts();
		builder.setPager();
		builder.setItems();
		return builder.getDtoList();
	}
	
	@Override
	public DtoList<CustomerCareCaseIssue> getListElastic(String searchWords, int pageIndex) {

    	searchRequest = createSearchRequest(searchWords);

		DtoList<CustomerCareCaseIssue> list = new DtoList<CustomerCareCaseIssue>();

		try (RestHighLevelClient restClient = new RestHighLevelClient(
				RestClient.builder(
    	                new HttpHost(this.esHost, 9200, "http")))) {
			SearchHits hits = restClient.search(searchRequest, RequestOptions.DEFAULT).getHits();
			list.setTotalRecords((int) hits.getTotalHits().value);
			list.setPager(new Pager(pageIndex, list.getTotalRecords(),PAGE_SIZE));
	    	searchSourceBuilder.from(PAGE_SIZE * (pageIndex - 1)).size(PAGE_SIZE);
	    	
	    	hits = restClient.search(searchRequest, RequestOptions.DEFAULT).getHits();
	    	List<CustomerCareCaseIssue> cccIssues = new ArrayList<>();
	    	Integer maxIssueId = dao.queryMaxIssueId();
			for (SearchHit hit : hits.getHits()) {
				Map<String, Object> source = hit.getSourceAsMap();
				Integer issueId = (Integer)source.get("id");
				if (issueId > maxIssueId) continue;
				if (containsId(cccIssues, issueId)) continue;

				cccIssues.add(issueFromHitSource(source));
			}
			dao.setExtraIssueInfo(cccIssues);
			list.setItems(cccIssues);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	private SearchRequest createSearchRequest(String searchWords) {

        searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder.query(buildSearchQuery(searchWords))
                .sort("date_last_updated", SortOrder.DESC)
                .size(0).trackTotalHits(true);

        return new SearchRequest(this.searchIndex)
                .source(searchSourceBuilder);

	}
	
	private QueryBuilder buildSearchQuery(String searchWords) {
    	BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
		String userCompanyKcode = Context.getCurrentUser().getCompanyKcode();
		if(this.companyRepo.isSupplier(userCompanyKcode)) {
	    	boolQueryBuilder.must(new MatchPhraseQueryBuilder("supplier_kcode", userCompanyKcode).slop(0));
		}
		if (!StringUtils.hasText(searchWords)) {
			boolQueryBuilder.must(new MatchAllQueryBuilder());
		} else {
	    	boolQueryBuilder.must(includeSearchWordsDisMaxQuery(searchWords));
		}
		return boolQueryBuilder;
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
				"name", "supplier_en_name", "product_base_code", 
				"product_sku_code", "base_product_name", "sku_product_name",
				"supplier_kcode", "issue_type_category_name", "issue_type_name");
		multiMatch.type(Type.PHRASE_PREFIX);
		disMaxQuery.add(multiMatch);
		
		String singleWord = searchWords.replace(" ", "_");
		MatchPhrasePrefixQueryBuilder queryStatus = 
				new MatchPhrasePrefixQueryBuilder("status", singleWord);
		disMaxQuery.add(queryStatus);
		return disMaxQuery;
	}
	
	private boolean containsId(List<CustomerCareCaseIssue> issues, Integer id) {
		for (CustomerCareCaseIssue issue : issues) {
			if (issue.getId().equals(id)) return true;
		}
		return false;
	}
	
	private CustomerCareCaseIssue issueFromHitSource(Map<String, Object> source) {
		Integer secondsFromLastUpdate = 
				(int) ((new Date().getTime() - (Long) source.get("date_last_updated")) / 1000);
		CustomerCareCaseIssue issue = new CustomerCareCaseIssueImpl(
				(Integer)source.get("id"), 
				(String) source.get("name"), 
				(String) source.get("issue_type_name"), 
				(String) source.get("status"), 
				(String) source.get("supplier_kcode"), 
				(String) source.get("supplier_en_name"), 
				(Long) source.get("date_create"), 
				secondsFromLastUpdate);
		issue.setOccurrences(getIssueOccurences((Integer)source.get("id")));
		return issue;
	}

	private Integer getIssueOccurences(Integer id) {
		String idSearch = "issueId:" + id;
		return caseUco.getElasticHitCount(idSearch);
	}
	
	private String getFilterKcode(){
		String supplierKcodeToFilter = Context.getCurrentUser().getCompanyKcode();
		if(Context.getCurrentUser().isDrsUser()) supplierKcodeToFilter = null;
		return supplierKcodeToFilter;
	}
	
	@Override
	public Map<String,Map<Integer,String>> getIssueTypeToIssuesMap(List<String> baseList,List<String> skuList,Integer categoryId) {
		if(baseList==null&&skuList==null){
			return this.dao.queryIssueTypeToIssuesMap(categoryId);
		}
		if(baseList!=null&&skuList==null){
			Map<String,Map<Integer,String>> issueTypeToGeneralIssueMap = this.dao.queryGeneralIssueTypeToIssuesMap(categoryId);
			Map<String,Map<Integer,String>> issueTypeToIssuesMapByProductBase = this.dao.queryIssueTypeToIssuesMapByProductBase(categoryId, baseList);
			return this.mergeIssueTypeToIssuesMap(issueTypeToGeneralIssueMap, issueTypeToIssuesMapByProductBase);
		}
		if(baseList==null&&skuList!=null){
			Map<String,Map<Integer,String>> issueTypeToGeneralIssueMap = this.dao.queryGeneralIssueTypeToIssuesMap(categoryId);
			Map<String,Map<Integer,String>> issueTypeToIssuesMapByProductSku = this.dao.queryIssueTypeToIssuesMapByProductSku(categoryId, skuList); 
			return this.mergeIssueTypeToIssuesMap(issueTypeToGeneralIssueMap, issueTypeToIssuesMapByProductSku);
		}
		return null;
	}
	
	private Map<String,Map<Integer,String>> mergeIssueTypeToIssuesMap(Map<String,Map<Integer,String>> a,Map<String,Map<Integer,String>> b){
		for(String issueTypeInB:b.keySet()){
			if(!a.containsKey(issueTypeInB)) a.put(issueTypeInB, b.get(issueTypeInB));
			else{
				for(Integer issueIdOfTypeInB:b.get(issueTypeInB).keySet()){
					if(!a.get(issueTypeInB).containsKey(issueIdOfTypeInB)) a.get(issueTypeInB).put(issueIdOfTypeInB, b.get(issueTypeInB).get(issueIdOfTypeInB));
				}
			}
		}
		return a;
	}
	
	@Override
	public String createIssueTypeCategory(String name) {
		return this.dao.insertIssueTypeCategory(name);
	}

	@Override
	public Integer createIssueType(Integer categoryId, String typeName) {
		return this.dao.insertIssueType(categoryId, typeName);
	}

	@Override
	public Integer createIssue(CustomerCareCaseIssue issue) {
		this.checkCorrectness(issue);
		int id  = this.dao.insertIssue(issue);
		return id;
	}
	
	private void checkCorrectness(CustomerCareCaseIssue issue){
		Assert.notNull(CustomerCareCaseIssueStatus.valueOf(issue.getStatus()), "CustomerCareCaseIssueStatus.valueOf(issue.getStatus()) must not be null");
		if(issue.getSupplierKcode()==null) Assert.isTrue(issue.getRelatedProductBaseCodeList()==null&&issue.getRelatedProductSkuCodeList()==null, "kcode is null, rest must be null");
		if(issue.getSupplierKcode()!=null) Assert.isTrue(issue.getRelatedProductBaseCodeList()!=null^issue.getRelatedProductSkuCodeList()!=null, "kcode is not null");
	}
		
	@Override
	public CustomerCareCaseIssue update(CustomerCareCaseIssue issue) {
		return this.dao.update(issue);
	}
	
	@Override
	public CustomerCareCaseIssue getIssue(int id) {
		CustomerCareCaseIssue issue = dao.queryIssue(id);
		issue.setOccurrences(getIssueOccurences(id));
		return issue;
	}
	
	@Override
	public List<Locale> getLocaleCodeList() {
		return Arrays.asList(Locale.values());
	}

	@Override
	public List<String> getIssueStatusList() {
		List<String> statusList = new ArrayList<String>();
		for(CustomerCareCaseIssueStatus status:CustomerCareCaseIssueStatus.values()){
			statusList.add(status.name());
		}
		return statusList;
	}

	@Override
	public Map<Integer,String> getCategoryIdToNameMap() {
		return this.dao.queryCategoryIdToNameMap();
	}

	@Override
	public String deleteIssueTypeCategory(String name) {
		return this.dao.deleteIssueTypeCategory(name);
	}

	@Override
	public String deleteIssueType(String categoryName, String name) {
		return this.dao.deleteIssueType(categoryName, name);
	}
	
	@Override
	public boolean deleteIssue(int id) {
		return this.dao.delete(id);
	}

	@Override @Transactional("transactionManager")
	public Integer addComment(int issueId, Boolean pendingSupplierAction, CustomerCareCaseIssueComment comment) {
		int userId = Context.getCurrentUser().getUserId();
		this.dao.insertComment(userId,issueId,comment);
		Boolean isSupplier = this.userRepo.isSupplier(userId);
		Boolean isDrsUser = this.userRepo.isDrsUser(userId);
		this.processStatusChange(isSupplier, isDrsUser, issueId, pendingSupplierAction);
		this.processNotification(isSupplier, isDrsUser, issueId, pendingSupplierAction);
		return issueId; 
	}
	
	private void processStatusChange(Boolean isSupplier, Boolean isDrsUser, int issueId, Boolean pendingSupplierAction){
		Assert.isTrue(isDrsUser^isSupplier, "should be either DRS user or supplier"); // should be either DRS user or supplier
		if(isSupplier){
			this.dao.updateIssueStatus(issueId, CustomerCareCaseIssueStatus.PENDING_DRS_ACTION.name());
		}
		if(isDrsUser&&pendingSupplierAction){
			this.dao.updateIssueStatus(issueId, CustomerCareCaseIssueStatus.PENDING_SUPPLIER_ACTION.name());
		}
	}

	private void processNotification(boolean isSupplier,boolean isDrsUser,int issueId,Boolean pendingSupplierAction){
		if(this.isGeneralIssue(issueId)) return;
		Assert.isTrue(isDrsUser^isSupplier, "should be either DRS user or supplier"); // should be either DRS user or supplier
		if(isSupplier){
			this.processNewDiscussionNotificationToDRS(issueId);
			this.dao.setNeedRenotifyForCommentToSupplier(issueId, false);
		}
		if(isDrsUser&&pendingSupplierAction){
			this.processNewDiscussionNotificationToSupplier(issueId);
			this.dao.setNeedRenotifyForCommentToSupplier(issueId, true);
		}
	}
	
	private boolean isGeneralIssue(int issueId){
		String issueSupplierKcode = this.dao.querySupplierKcodeOfIssue(issueId);
		return issueSupplierKcode==null;
	}
	
	private void processNewDiscussionNotificationToSupplier(int issueId){
		boolean sendNotify = Boolean.parseBoolean(((String)this.env.get("SEND_NOTIFY")));
		if(sendNotify){
			try{
				String issueName = this.dao.queryEnUsIssueName(issueId);
				java.util.Locale locale = Context.getCurrentUser().getLocale();			
				String subject = this.messageSource.getMessage("mail.Issues-discussionToSupplier-Subject", new Object[] {issueName}, locale);
				String body = this.messageSource.getMessage("mail.Issues-discussionToSupplier-Body", new Object[] {issueName,Integer.toString(issueId),issueName},locale);								
				String signature = this.mailUtil.getSignature(SignatureType.NO_REPLY);		
				body = this.mailUtil.appendSignature(body,signature);
				List<String> serviceEmailList = this.companyRepo.querySupplierServiceEmailAddressList(this.dao.queryKcodeOfSupplierInIssue(issueId));											
				String [] mailTo = serviceEmailList.toArray(new String[serviceEmailList.size()]);									
				Assert.isTrue(this.userRepo.isDrsUser(Context.getCurrentUser().getUserId()), "user is not DRS user");
				String currentUserEmail = this.userRepo.queryUserMail(Context.getCurrentUser().getUserId());				
				String [] bcc = {currentUserEmail};								
				this.mailUtil.SendMimeWithBcc(mailTo,bcc, ADDRESS_NO_REPLY,subject,body);
			}
			catch(Exception e){
				this.mailUtil.Send(SOFTWARE_ENGINEERS, ADDRESS_NO_REPLY, "New Discussion to supplier email ERROR", e.getMessage()+"\n\n"+e.toString()+"\n\n"+e.fillInStackTrace());
			}
		}				
	}
	
	private void processNewDiscussionNotificationToDRS(int issueId){
		boolean sendNotify = Boolean.parseBoolean(((String)this.env.get("SEND_NOTIFY")));
		if(sendNotify){
			try{
				String kCode = this.dao.querySupplierKcodeOfIssue(issueId); 
				String issueName = this.dao.queryEnUsIssueName(issueId);
				java.util.Locale locale = Context.getCurrentUser().getLocale();				

				String subject = this.messageSource.getMessage("mail.Issues-discussionToDRS-Subject", new Object[] {kCode,issueName}, locale);
				String body = this.messageSource.getMessage("mail.Issues-discussionToDRS-Body", new Object[] {kCode,issueName,Integer.toString(issueId),issueName},locale);								
				String signature = this.mailUtil.getSignature(SignatureType.NO_REPLY);		
				body = this.mailUtil.appendSignature(body,signature);				 

				String emailOfNewestDrsCommenter = this.dao.queryEmailOfNewestDrsCommenter(issueId);
				Assert.notNull(emailOfNewestDrsCommenter, "emailOfNewestDrsCommenter null");
				String [] mailTo = {emailOfNewestDrsCommenter};
				String mailFrom = ADDRESS_NO_REPLY;
				this.mailUtil.SendMime(mailTo,mailFrom,subject,body);

			}
			catch(Exception e){
				this.mailUtil.Send(SOFTWARE_ENGINEERS, ADDRESS_NO_REPLY, "New Discussion to DRS email ERROR", e.getMessage()+"\n\n"+e.toString()+"\n\n"+e.fillInStackTrace());
			}
		}				
	}
	
	@Override
	public String getEnUsIssueName(int issueId) {
		return this.dao.queryEnUsIssueName(issueId);
	}

	@Override
	public Map<Integer,Map<Integer,String>> getCategoryIdToIssueTypeAndIdMap() {
		return this.dao.queryCategoryIdToIssueTypeAndIdMap();
	}

	@Override
	public void updateType(Integer id, String name) {
		this.dao.updateType(id,name);
	}

	@Override
	public Map<Integer,String> getTypeIdToNameMap() {
		return this.dao.queryTypeIdToNameMap();
	}

	@Override
	public Map<Integer,String> getTypeIdToNameMap(Integer categoryId) {
		return this.dao.queryTypeIdToNameMap(categoryId);
	}

}
