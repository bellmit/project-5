package com.kindminds.drs.persist.data.access.usecase.customercare;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.lang.Short;


import com.kindminds.drs.api.data.access.usecase.customercare.MaintainCustomerCareCaseIssueDao;
import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseIssue;
import com.kindminds.drs.persist.data.access.rdb.Dao;


import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.kindminds.drs.Locale;
import com.kindminds.drs.Marketplace;




import com.kindminds.drs.persist.v1.model.mapping.customercare.CustomerCareCaseIssueCommentImpl;
import com.kindminds.drs.persist.v1.model.mapping.customercare.CustomerCareCaseIssueImpl;
import com.kindminds.drs.persist.v1.model.mapping.customercare.CustomerCareCaseIssueTemplateImpl;
import com.kindminds.drs.persist.data.access.rdb.util.PostgreSQLHelper;

@Repository
public class MaintainCustomerCareCaseIssueDaoImpl extends Dao implements MaintainCustomerCareCaseIssueDao {
	
	
	private static String listIssueSelectSql = "select "
			+ "     ci.id as id, "
			+ "      NULL as category_id,"
			+ "ci.type_id as type_id, "
			+ " ci.status as status, "
			+ "com.k_code as supplier_kcode, "
			+ "      NULL as date_create, "
			+ "cast( extract(epoch from (now()-date_last_updated)) as int) as seconds_from_last_update "
			+ "from customercarecase_issue ci ";
	private static String listIssueJoinTypeSql = "inner join customercarecase_issue_type cit on cit.id = ci.type_id ";
	private static String listIssueJoinCompanySql = "left join company com on com.id = ci.supplier_company_id ";
	private static String listIssueSqlWhereBegin = "where true ";
	private static String listIssueSql_order_page = "order by seconds_from_last_update, id limit :size offset :startIndex ";
	
	private final String queryIssueToOccurenceCaseSql = 
			"select issue_id, case_id from customercarecase_issue_rel ";
	
	@Override @SuppressWarnings("unchecked")
	public Map<Integer,Date> queryNeedRenotifyIssueIdToLastCommentDateTimeMap(String status) {
		String sql = "select ci.id, max(cic.date_create) from customercarecase_issue ci "
				+ "inner join customercarecase_issue_comment cic on ci.id = cic.issue_id "
				+ "where ci.status = :status and ci.need_renotify_for_new_comment_to_supplier = :needRenotify "
				+ "group by ci.id order by ci.id ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("status", status);
		q.addValue("needRenotify",true);
		Map<Integer,Date> issueToLastCommentDateTimeMap = new HashMap<Integer,Date>();
		List<Object[]> resultObjectArrayList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		for(Object[] items:resultObjectArrayList)
			issueToLastCommentDateTimeMap.put((Integer)items[0], (Date)items[1]);
		return issueToLastCommentDateTimeMap;
	}

	@Override
	public int queryIssueCount(String kcodeToFilter) {
		String sql = "select count(*) from customercarecase_issue ci " 
				+ listIssueJoinCompanySql + listIssueSqlWhereBegin + this.assembleSupplierKcodeSqlCondition(kcodeToFilter);

		Integer o = getJdbcTemplate().queryForObject(sql,Integer.class);
		if (o == null) return 0;
		return o;
	}

	@Override
	public int queryIssueCount(String kcodeToFilter,Integer categoryId) {
		String sql = "select count(*) from customercarecase_issue ci "
				+ listIssueJoinTypeSql
				+ listIssueJoinCompanySql + listIssueSqlWhereBegin + this.assembleSupplierKcodeSqlCondition(kcodeToFilter) 
				+ "and cit.category_id = :categoryId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("categoryId", categoryId);
		Integer o = getNamedParameterJdbcTemplate().queryForObject(sql,q,Integer.class);
		if (o == null) return 0;
		return Integer.parseInt(o.toString());
	}

	@Override
	public int queryIssueCount(String kcodeToFilter,Integer categoryId, Integer typeId) {
		String sql = "select count(*) from customercarecase_issue ci "
				+ listIssueJoinTypeSql 
				+ listIssueJoinCompanySql + listIssueSqlWhereBegin + this.assembleSupplierKcodeSqlCondition(kcodeToFilter)
				+ "and cit.category_id = :categoryId "
				+ "and ci.type_id = :typeId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("categoryId", categoryId);
		q.addValue("typeId", typeId);
		Integer o = getNamedParameterJdbcTemplate().queryForObject(sql,q,Integer.class);
		if (o == null) return 0;
		return o;
	}

	@Override @SuppressWarnings("unchecked")
	public List<CustomerCareCaseIssue> queryIssueList(String supplierKcode,int startIndex,int size) {
		String sql = listIssueSelectSql 
				+ listIssueJoinTypeSql 
				+ listIssueJoinCompanySql + listIssueSqlWhereBegin + this.assembleSupplierKcodeSqlCondition(supplierKcode)
				+ listIssueSql_order_page;
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("size", size);
		q.addValue("startIndex", startIndex-1);


		List<CustomerCareCaseIssue> resultList = (List) getNamedParameterJdbcTemplate().
				query(sql,q, (rs,rowNum) -> new CustomerCareCaseIssueImpl(rs.getInt("id"),rs.getInt("category_id"),
						rs.getInt("type_id"),rs.getString("status"),rs.getString("supplier_kcode"),
						rs.getString("date_create"),rs.getInt("seconds_from_last_update")));
		this.setExtraIssueInfo(resultList);
		return resultList;
	}

	/*
	@SuppressWarnings("unchecked")
	private Map<Integer,Set<Integer>> queryIssueIdtoOccurenceCaseIdSetMap(){
		MapSqlParameterSource q = new MapSqlParameterSource();
		List<Object[]> result = q.getResultList();
		Map<Integer,Set<Integer>> issueIdtoOccurenceCaseIdSetMap = new HashMap<Integer,Set<Integer>>();
		for(Object[] items:result){
			Integer issueId = (Integer)items[0];
			Integer caseId = (Integer)items[1];
			if(!issueIdtoOccurenceCaseIdSetMap.containsKey(issueId))
				issueIdtoOccurenceCaseIdSetMap.put(issueId,new HashSet<Integer>());
			issueIdtoOccurenceCaseIdSetMap.get(issueId).add(caseId);
		}
		return issueIdtoOccurenceCaseIdSetMap;
	}
	*/

	@Override @SuppressWarnings("unchecked")
	public List<CustomerCareCaseIssue> queryIssueList(String supplierKcode, Integer categoryId, int startIndex, int size) {
		String sql = listIssueSelectSql 
				+ listIssueJoinTypeSql
				+ listIssueJoinCompanySql + listIssueSqlWhereBegin + this.assembleSupplierKcodeSqlCondition(supplierKcode)
				+ "and cit.category_id = :categoryId "
				+ listIssueSql_order_page;
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("categoryId", categoryId);
		q.addValue("size", size);
		q.addValue("startIndex", startIndex-1);
		List<CustomerCareCaseIssue> resultList =(List) getNamedParameterJdbcTemplate().query(sql,q,
				(rs,rowNum) -> new CustomerCareCaseIssueImpl(rs.getInt("id"),rs.getInt("category_id"),
				rs.getInt("type_id"),rs.getString("status"),rs.getString("supplier_kcode"),
				rs.getString("date_create"),rs.getInt("seconds_from_last_update")));
		this.setExtraIssueInfo(resultList);
		return resultList;
	}
	
	@Override @SuppressWarnings("unchecked")
	public List<CustomerCareCaseIssue> queryIssueList(String supplierKcode,Integer categoryId,Integer typeId,int startIndex,int size) {
		String sql = listIssueSelectSql 
				+ listIssueJoinTypeSql
				+ listIssueJoinCompanySql + listIssueSqlWhereBegin + this.assembleSupplierKcodeSqlCondition(supplierKcode)
				+ "and cit.category_id = :categoryId and ci.type_id = :typeId "
				+ listIssueSql_order_page;
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("categoryId", categoryId);
		q.addValue("typeId", typeId);
		q.addValue("size", size);
		q.addValue("startIndex", startIndex-1);
		List<CustomerCareCaseIssue> resultList = (List) getNamedParameterJdbcTemplate().query(sql,q,
				(rs,rowNum) -> new CustomerCareCaseIssueImpl(rs.getInt("id"),rs.getInt("category_id"),
				rs.getInt("type_id"),rs.getString("status"),rs.getString("supplier_kcode"),
				rs.getString("date_create"),rs.getInt("seconds_from_last_update")));
		this.setExtraIssueInfo(resultList);
		return resultList;
	}
	
	private String assembleSupplierKcodeSqlCondition(String supplierKcode){
		return supplierKcode==null?"":"and com.k_code = '"+supplierKcode+"' ";
	}
	
	@Override @SuppressWarnings("unchecked")
	public CustomerCareCaseIssue queryIssue(int id) {
		String sql = "select ci.id as id, "
				+ "cit.category_id as category_id,"
				+ "     ci.type_id as type_id, "
				+ "      ci.status as status, "
				+ "     com.k_code as supplier_kcode, "
				+ " to_char(ci.date_create at time zone 'UTC', :dateTimeFormatText ) as date_create, "
				+ "           NULL as seconds_from_last_update "
				+ "from customercarecase_issue ci "
				+ "inner join customercarecase_issue_type cit on cit.id = ci.type_id "
				+ "left join company com on com.id = ci.supplier_company_id "
				+ "where ci.id = :id ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("id",id);
		q.addValue("dateTimeFormatText","YYYY-MM-DD HH24:MI:SS");
		List<CustomerCareCaseIssueImpl> resultList = (List) getNamedParameterJdbcTemplate().query(sql,q,
				(rs,rowNum) -> new CustomerCareCaseIssueImpl(rs.getInt("id"),rs.getInt("category_id"),
						rs.getInt("type_id"),rs.getString("status"),rs.getString("supplier_kcode"),
						rs.getString("date_create"),rs.getInt("seconds_from_last_update")));

		Assert.isTrue(resultList.size()==0||resultList.size()==1);
		if(resultList.size()==0) return null;
		CustomerCareCaseIssueImpl issue = resultList.get(0);
		issue.setLocaleToNameMap(this.queryLocaleToNameMap(id));
		issue.setRelatedProductBaseList(this.queryRelatedProductBasesOfIssue(id));
		issue.setRelatedProductSkuList(this.queryRelatedProductSkusOfIssue(id));
		issue.setOccurrences(this.queryTemplateOccurrencesOfIssue(id));
		issue.setTemplateList(this.queryTemplateListOfIssue(id));
		issue.setCommentList(this.queryCommentListOfIssue(id));
		return issue;
	}

	@Override
	public void setExtraIssueInfo(List<CustomerCareCaseIssue> issueList){
//		Map<Integer,Set<Integer>> issueIdtoOccurenceCaseIdSetMap = this.queryIssueIdtoOccurenceCaseIdSetMap();
		for(CustomerCareCaseIssue issue:issueList){
			issue.setLocaleToNameMap(this.queryEnUsLocaleToNameMap(issue.getId()));
			issue.setRelatedProductBaseList(this.queryRelatedProductBasesOfIssue(issue.getId()));
			issue.setRelatedProductSkuList(this.queryRelatedProductSkusOfIssue(issue.getId()));
//			if(issueIdtoOccurenceCaseIdSetMap.containsKey(issue.getId()))
//				issue.setOccurrences(issueIdtoOccurenceCaseIdSetMap.get(issue.getId()).size());
//			else
//				issue.setOccurrences(0);
		}
	}
	
	@Override @Transactional("transactionManager")
	public String insertIssueTypeCategory(String name) {
		String sql = "insert into customercarecase_issue_type_category (name) values (:categoryName) ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("categoryName", name);
		getNamedParameterJdbcTemplate().update(sql,q);
		return name;
	}
	
	@Override @Transactional("transactionManager")
	public Integer insertIssueType(Integer categoryId, String typeName) {
		int id = PostgreSQLHelper.getNextVal(getNamedParameterJdbcTemplate(), "customercarecase_issue_type", "id");
		String sql = "insert into customercarecase_issue_type "
				+ "(  id, category_id,      name ) values "
				+ "( :id, :categoryId, :typeName )";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("id", id);
		q.addValue("categoryId", categoryId);
		q.addValue("typeName", typeName);
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
		return id;
	}
	
	@Override @Transactional("transactionManager")
	public Integer insertIssue(CustomerCareCaseIssue issue) {
		int id = PostgreSQLHelper.getNextVal(getNamedParameterJdbcTemplate(),"customercarecase_issue","id");
		Integer supplierCompanyId = issue.getSupplierKcode()==null?null:this.queryCompanyIdByKcode(issue.getSupplierKcode());
		String sql = "insert into customercarecase_issue"
				+ "(  id, type_id,  status, supplier_company_id, date_create, date_last_updated ) values "
				+ "( :id, :typeId, :status,  :supplierCompanyId, :dateCreate,  :dateLastUpdated )";
		MapSqlParameterSource q = new MapSqlParameterSource();
		Date now = new Date();
		q.addValue("id", id);
		q.addValue("status", issue.getStatus());
		q.addValue("dateCreate", now);
		q.addValue("dateLastUpdated", now);
		q.addValue("typeId", issue.getTypeId());
		q.addValue("supplierCompanyId", supplierCompanyId==null?null:supplierCompanyId);
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1, "executeUpdate not equal to 1");
		this.insertIssueLanguageNames(id, issue.getLocaleCodeToNameMap());
		this.insertRelatedProductBasesForIssue(id, issue.getRelatedProductBaseCodeList());
		this.insertRelatedProductSkussForIssue(id, issue.getRelatedProductSkuCodeList());
		this.updateLastUpdatedDate(id,now);
		return id;
	}

	@SuppressWarnings("unchecked")
	private Integer queryCompanyIdByKcode(String kcode) {
		String sql = "select c.id from company c where c.k_code = :kcode ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("kcode", kcode);
		List<Integer> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,Integer.class);
		if(resultList.size()==0) return null;
		Assert.isTrue(resultList.size()==1&&resultList.get(0)!=null);
		return resultList.get(0);
	}
	
	@Override @Transactional("transactionManager")
	public CustomerCareCaseIssue update(CustomerCareCaseIssue issue) {
		String sql = "update customercarecase_issue ci set "
				+ "            type_id = :typeId, "
				+ "             status = :status "
				+ "where ci.id = :id ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("id", issue.getId());
		q.addValue("status", issue.getStatus());		
		q.addValue("typeId", issue.getTypeId());
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
		this.deleteLocaleNames(issue.getId());
		this.deleteRelatedProductBaseList(issue.getId());
		this.deleteRelatedProductSkuList(issue.getId());
		this.insertIssueLanguageNames(issue.getId(), issue.getLocaleCodeToNameMap());
		this.insertRelatedProductBasesForIssue(issue.getId(), issue.getRelatedProductBaseCodeList());
		this.insertRelatedProductSkussForIssue(issue.getId(), issue.getRelatedProductSkuCodeList());
		this.updateLastUpdatedDate(issue.getId(),new Date());
		return this.queryIssue(issue.getId());
	}
	
	@Override @Transactional("transactionManager")
	public boolean delete(int id) {
		this.deleteLocaleNames(id);
		this.deleteRelatedProductBaseList(id);
		this.deleteRelatedProductSkuList(id);
		String sql = "delete from customercarecase_issue where id = :id ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("id", id);
		getNamedParameterJdbcTemplate().update(sql,q);
		return true;
	}

	@Override @Transactional("transactionManager")
	public Integer insertComment(int userId, int issueId, CustomerCareCaseIssue.CustomerCareCaseIssueComment comment) {
		String sql = "insert into customercarecase_issue_comment (issue_id, line_seq, creator_id, date_create, content) "
				+ "values(:issueId, :lineSeq, :userId, :dateCreate, :content)";
		int maxLineSeq = this.queryMaxLineSeqOfIssueComment(issueId);
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("userId", userId);
		q.addValue("issueId", issueId);
		q.addValue("lineSeq", maxLineSeq+1);
		q.addValue("dateCreate", new Date());
		q.addValue("content", comment.getContents());
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
		return issueId;
	}
	
	private Integer queryMaxLineSeqOfIssueComment(int issueId) {
		String sql = "select max(line_seq) from customercarecase_issue_comment cic "
				+ "where cic.issue_id = :issueId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("issueId", issueId);
		Integer o = getNamedParameterJdbcTemplate().queryForObject(sql,q,Integer.class);
		if (o == null) return 0;
		return o;
	}

	private void deleteLocaleNames(int id){
		String sql = "delete from customercarecase_issue_locale_name where issue_id = :id ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("id", id);
		getNamedParameterJdbcTemplate().update(sql,q);
	}
	
	private void deleteRelatedProductBaseList(int id){
		String sql = "delete from customercarecase_issue_product_base_rel where issue_id = :id ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("id", id);
		getNamedParameterJdbcTemplate().update(sql,q);
	}
	
	private void deleteRelatedProductSkuList(int id){
		String sql = "delete from customercarecase_issue_product_sku_rel where issue_id = :id ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("id", id);
		getNamedParameterJdbcTemplate().update(sql,q);
	}
	
	@Transactional("transactionManager")
	private void insertIssueLanguageNames(int issueId,Map<Locale,String> languageToNameMap ){
		Assert.isTrue(languageToNameMap.containsKey(Locale.EN_US));
		String sql = "insert into customercarecase_issue_locale_name "
				+ "(issue_id, locale_id,  name) select "
				+ " :issueId, locale.id, :name from locale where locale.code = :localeCode ";
		for(Locale locale:languageToNameMap.keySet()){
			MapSqlParameterSource q = new MapSqlParameterSource();
			q.addValue("issueId", issueId);
			q.addValue("name", languageToNameMap.get(locale));
			q.addValue("localeCode", locale.getCode());
			Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
		}
	}
	
	@Transactional("transactionManager")
	private void insertRelatedProductBasesForIssue(int issueId,List<String> baseCodeList ){
		if(baseCodeList!=null){
			String sql = "insert into customercarecase_issue_product_base_rel ( issue_id, product_base_id  ) "
					+ "select                                      :issueId,           pb.id "
					+ "from product_base pb where pb.code_by_drs = :baseCode ";
			for(String baseCode: baseCodeList){
				MapSqlParameterSource q = new MapSqlParameterSource();
				q.addValue( "issueId", issueId);
				q.addValue("baseCode", baseCode);
				Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
			}
		}
	}
	
	@Transactional("transactionManager")
	private void insertRelatedProductSkussForIssue(int issueId,List<String> skuCodeList ){
		if(skuCodeList!=null){
			String sql = "insert into customercarecase_issue_product_sku_rel ( issue_id, product_sku_id  ) "
					+ "select                                      :issueId,         ps.id "
					+ "from product_sku ps where ps.code_by_drs = :skuCode ";
			for(String skuCode:skuCodeList){
				MapSqlParameterSource q = new MapSqlParameterSource();
				q.addValue("issueId", issueId);
				q.addValue("skuCode", skuCode);
				Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
			}
		}
	}

	private void updateLastUpdatedDate(int issueId,Date date) {
		String sql = "update customercarecase_issue ci set date_last_updated = :date "
				+ "from customercarecase_issue_type cit, company com where ci.id = :id ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("id", issueId);
		q.addValue("date", date);
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
	}

	@Override @Transactional("transactionManager")
	public String deleteIssueTypeCategory(String name) {
		try {
			String sql = "delete from customercarecase_issue_type_category where name = :name ";
			MapSqlParameterSource q = new MapSqlParameterSource();
			q.addValue("name", name);
			Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
			return name;
		} catch(Exception e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	private List<String> queryRelatedProductBasesOfIssue(int issueId){
		String sql = "select pb.code_by_drs from customercarecase_issue_product_base_rel cipbr "
				+ "inner join product_base pb on pb.id = cipbr.product_base_id "
				+ "where cipbr.issue_id = :issueId order by pb.code_by_drs ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("issueId", issueId);
		List<String> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		return resultList.size()==0?null:resultList;
	}
	
	@SuppressWarnings("unchecked")
	private List<String> queryRelatedProductSkusOfIssue(int issueId){
		String sql = "select ps.code_by_drs from customercarecase_issue_product_sku_rel cipsr "
				+ "inner join product_sku ps on ps.id = cipsr.product_sku_id "
				+ "where cipsr.issue_id = :issueId order by ps.code_by_drs ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("issueId", issueId);
		List<String> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		return resultList.size()==0?null:resultList;
	}
	
	private Integer queryTemplateOccurrencesOfIssue(int issueId){
		String sql = "select count(*) from ( " + this.queryIssueToOccurenceCaseSql + ") as subquery where issue_id = :issueId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("issueId", issueId);
		Integer o = getNamedParameterJdbcTemplate().queryForObject(sql,q,Integer.class);
		if (o==null) return 0;
		return o;
	}
	
	@SuppressWarnings("unchecked")
	private List<CustomerCareCaseIssue.CustomerCareCaseIssueTemplate> queryTemplateListOfIssue(int issueId){
		String sql = "select cit.id as id, ciln.name as issue_name, cit.issue_id, cit.content as content "
				+ "from customercarecase_issue_template cit "
				+ "inner join customercarecase_issue ci on ci.id = cit.issue_id "
				+ "inner join customercarecase_issue_locale_name ciln on ciln.issue_id = ci.id "
				+ "inner join locale l on l.id = ciln.locale_id "
				+ "where cit.issue_id = :issueId and l.code = 'en_US' "
				+ "order by cit.id ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("issueId", issueId);
		List<CustomerCareCaseIssueTemplateImpl> resultList = getNamedParameterJdbcTemplate().query(sql,q,(rs,rowNum) ->
				new CustomerCareCaseIssueTemplateImpl(rs.getInt("id"),rs.getInt("issue_id"),
						rs.getString("issue_name"),rs.getString("content")));

		if(resultList.size()==0||resultList==null) return null;
		for(CustomerCareCaseIssueTemplateImpl t:resultList){
			t.setLocales(this.queryTemplateLocaleList(t.getId()));
			t.setCaseTypes(this.queryTemplateCaseTypeList(t.getId()));
			t.setMarketRegions(this.queryTemplateMarketCountryList(t.getId()));
			t.setMarketplaces(this.queryTemplateMarketplaceList(t.getId()));
		}
		return (List<CustomerCareCaseIssue.CustomerCareCaseIssueTemplate>)((List<?>)resultList);
	}
	
	@SuppressWarnings("unchecked")
	private List<Locale> queryTemplateLocaleList(int templateId){
		String sql = "select lc.code from customercarecase_issue_template_locale_rel citlr "
				+ "inner join locale lc on lc.id = citlr.locale_id "
				+ "where citlr.template_id = :templateId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("templateId", templateId);
		List<String> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		if(resultList.size()==0) return null;
		List<Locale> localeList = new ArrayList<Locale>();
		for(String localeCode:resultList){
			localeList.add(Locale.fromCode(localeCode));
		}
		return localeList;
	}
	
	@SuppressWarnings("unchecked")
	private List<String> queryTemplateCaseTypeList(int templateId){
		String sql = "select ct.name from customercarecase_issue_template_case_type_rel citctr "
				+ "inner join customercarecase_type ct on ct.id = citctr.case_type_id "
				+ "where citctr.template_id = :templateId order by ct.name ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("templateId", templateId);
		List<String> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		return resultList.size()==0?null:resultList;
	}

	@SuppressWarnings("unchecked")
	private List<String> queryTemplateMarketCountryList(int templateId){
		String sql = "select c.code from customercarecase_issue_template_country_rel citcr "
				+ "inner join country c on c.id = citcr.country_id "
				+ "where citcr.template_id = :templateId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("templateId", templateId);
		List<String> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		return resultList.size()==0?null:resultList;
	}

	@SuppressWarnings("unchecked")
	private List<Marketplace> queryTemplateMarketplaceList(int templateId){
		String sql = "select mkt.id from customercarecase_issue_template_marketplace_rel citmr "
				+ "inner join marketplace mkt on mkt.id = citmr.marketplace_id "
				+ "where citmr.template_id = :templateId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("templateId", templateId);
		List<Integer> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,Integer.class);
		if(resultList.size()==0)return null;
		List<Marketplace> marketplaceList = new ArrayList<Marketplace>();
		for(Integer key:resultList){
			marketplaceList.add(Marketplace.fromKey(key));
		}
		return marketplaceList;
	}
	
	@SuppressWarnings("unchecked")
	private List<CustomerCareCaseIssue.CustomerCareCaseIssueComment> queryCommentListOfIssue(int issueId){
		String sql = "select     cic.id as id, "
				+ "        cic.line_seq as line_seq, "
				+ " u.user_display_name as creator, "
				+ " to_char(cic.date_create at time zone 'UTC', :dateTimeFormat1Text ) as date_create, "
				+ "         cic.content as content "
				+ "from customercarecase_issue_comment cic "
				+ "inner join user_info u on u.user_id = cic.creator_id "
				+ "where cic.issue_id = :issueId order by cic.date_create desc";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("issueId", issueId);
		q.addValue("dateTimeFormat1Text","YYYY-MM-DD HH24:MI:SS OF00");
		List<CustomerCareCaseIssue.CustomerCareCaseIssueComment> resultList = (List) getNamedParameterJdbcTemplate().query(sql,q
				, (rs,rowNum) -> new CustomerCareCaseIssueCommentImpl(rs.getInt("id"),rs.getInt("line_seq"),
						rs.getString("creator"),rs.getString("date_create"),rs.getString("content")));
		return resultList.size()==0?null:resultList;
	}

	@Override @Transactional("transactionManager")
	public String deleteIssueType(String categoryName, String name) {
		String sql = "delete from customercarecase_issue_type cit using customercarecase_issue_type_category citc "
				+ "where cit.name = :name "
				+ "and citc.name = :categoryName "
				+ "and citc.id = cit.category_id ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("name", name);
		q.addValue("categoryName", categoryName);
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
		return name;
	}
	
	@Override @SuppressWarnings("unchecked")
	public Map<String,Map<Integer,String>> queryIssueTypeToIssuesMap(Integer categoryId){
		String sql = "select cit.name as type_name, ci.id, ciln.name as issue_name from customercarecase_issue_type cit "
				+ "inner join customercarecase_issue ci on ci.type_id = cit.id "
				+ "inner join customercarecase_issue_locale_name ciln on ciln.issue_id = ci.id "
				+ "inner join locale l on l.id = ciln.locale_id "
				+ "where l.code = 'en_US' ";
		if(categoryId!=null) sql += "and cit.category_id = :categoryId ";
				sql += "order by cit.name, ciln.name ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		if(categoryId!=null) q.addValue("categoryId", categoryId);
		List<Object[]> resultList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		Map<String,Map<Integer,String>> result = new HashMap<String,Map<Integer,String>>();
		for(Object[] items:resultList){
			String typeName = (String)items[0];
			if(!result.containsKey(typeName))
				result.put(typeName, new HashMap<Integer,String>());
			result.get(typeName).put((Integer)items[1], (String)items[2]);
		}
		return result;
	}
	
	@Override @SuppressWarnings("unchecked")
	public Map<String,Map<Integer,String>> queryGeneralIssueTypeToIssuesMap(Integer categoryId) {
		String sql = "select cit.name as type_name, ci.id, ciln.name as issue_name from customercarecase_issue_type cit "
				+ "inner join customercarecase_issue ci on ci.type_id = cit.id "
				+ "inner join customercarecase_issue_locale_name ciln on ciln.issue_id = ci.id "
				+ "inner join locale l on l.id = ciln.locale_id "
				+ "where l.code = 'en_US' and ci.supplier_company_id is NULL ";
		if(categoryId!=null) sql += "and cit.category_id = :categoryId ";
				sql += "order by cit.name, ciln.name ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		if(categoryId!=null) q.addValue("categoryId", categoryId);
		List<Object[]> resultList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		Map<String,Map<Integer,String>> result = new HashMap<String,Map<Integer,String>>();
		for(Object[] items:resultList){
			String typeName = (String)items[0];
			if(!result.containsKey(typeName))
				result.put(typeName, new HashMap<Integer,String>());
			result.get(typeName).put((Integer)items[1], (String)items[2]);
		}
		return result;
	}

	@Override
	public Map<String,Map<Integer,String>> queryIssueTypeToIssuesMapByProductBase(Integer categoryId, List<String> baseList){
		List<Integer> baseIdList = this.queryBaseIdList(baseList);
		return this.queryIssueTypeToIssuesMapByProductBaseId(categoryId, baseIdList);
	}
	
	@Override
	public Map<String,Map<Integer,String>> queryIssueTypeToIssuesMapByProductSku(Integer categoryId, List<String> skuList){
		List<Integer> baseIdList = this.queryBaseIdListBySkuList(skuList);
		return this.queryIssueTypeToIssuesMapByProductBaseId(categoryId, baseIdList);
	}

	@SuppressWarnings("unchecked")
	private Map<String,Map<Integer,String>> queryIssueTypeToIssuesMapByProductBaseId(Integer categoryId, List<Integer> baseIdList){
		String sql = "select cit.name as type_name, ci.id, ciln.name as issue_name from customercarecase_issue_type cit "
				+ "inner join customercarecase_issue ci on ci.type_id = cit.id "
				+ "inner join customercarecase_issue_locale_name ciln on ciln.issue_id = ci.id "
				+ "inner join locale l on l.id = ciln.locale_id "
				+ "inner join customercarecase_issue_product_base_rel cipbr on cipbr.issue_id = ci.id "
				+ "inner join product_base pb on pb.id = cipbr.product_base_id "
				+ "where l.code = 'en_US' "
				+ "and cipbr.product_base_id in (:baseIdList) ";
		if(categoryId!=null) sql += "and cit.category_id = :categoryId ";
		sql +=  "order by cit.name, ciln.name ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		if(categoryId!=null) q.addValue("categoryId", categoryId);
		q.addValue("baseIdList", baseIdList);
		List<Object[]> resultList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		Map<String,Map<Integer,String>> result = new HashMap<String,Map<Integer,String>>();
		for(Object[] items:resultList){
			String typeName = (String)items[0];
			if(!result.containsKey(typeName))
				result.put(typeName, new HashMap<Integer,String>());
			result.get(typeName).put((Integer)items[1], (String)items[2]);
		}
		List<Integer> skuIdList = this.querySkuIdList(baseIdList);
		this.appendIssueTypeToIssuesMapByProductSku(result, categoryId, skuIdList);
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private void appendIssueTypeToIssuesMapByProductSku(Map<String,Map<Integer,String>> resultFromBase,Integer categoryId, List<Integer> skuIdList){
		String sql = "select cit.name as type_name, ci.id, ciln.name as issue_name from customercarecase_issue_type cit "
				+ "inner join customercarecase_issue ci on ci.type_id = cit.id "
				+ "inner join customercarecase_issue_locale_name ciln on ciln.issue_id = ci.id "
				+ "inner join locale l on l.id = ciln.locale_id "
				+ "inner join customercarecase_issue_product_sku_rel cipsr on cipsr.issue_id = ci.id "
				+ "where l.code = 'en_US' ";
		if(categoryId!=null) sql += "and cit.category_id = :categoryId ";
		sql +=   "and cipsr.product_sku_id in (:skuIdList) "
				+ "order by cit.name, ciln.name ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		if(categoryId!=null) q.addValue("categoryId", categoryId);
		q.addValue("skuIdList", skuIdList);
		List<Object[]> resultList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		for(Object[] items:resultList){
			String typeName = (String)items[0];
			if(!resultFromBase.containsKey(typeName))resultFromBase.put(typeName, new HashMap<Integer,String>());
			if(!resultFromBase.get(typeName).containsKey((Integer)items[1])){
				resultFromBase.get(typeName).put((Integer)items[1], (String)items[2]);
			}
		}
		return;
	}

	@SuppressWarnings("unchecked")
	private List<Integer> queryBaseIdList(List<String> baseList){
		String sql = "select pb.id from product_base pb where pb.code_by_drs in (:baseList) ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("baseList", baseList);
		return getNamedParameterJdbcTemplate().queryForList(sql,q,Integer.class);
	}
	
	@SuppressWarnings("unchecked")
	private List<Integer> querySkuIdList(List<Integer> baseIdList){
		String sql = "select ps.id from product_sku ps where ps.product_base_id in (:baseIdList) ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("baseIdList", baseIdList);
		return getNamedParameterJdbcTemplate().queryForList(sql,q,Integer.class);
	}
	
	@SuppressWarnings("unchecked")
	private List<Integer> queryBaseIdListBySkuList(List<String> skuList){
		String sql = "select pb.id from product_base pb inner join product_sku ps on pb.id = ps.product_base_id "
				+ "where ps.code_by_drs in (:skuList) ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("skuList", skuList);
		return getNamedParameterJdbcTemplate().queryForList(sql,q,Integer.class);
	}
	
	@Override @SuppressWarnings("unchecked")
	public Map<Integer,String> queryCategoryIdToNameMap() {
		String sql = "select citc.id, citc.name from customercarecase_issue_type_category citc ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		Map<Integer,String> resultMap = new TreeMap<Integer,String>();
		for(Object[] columns:columnsList){
			Integer tempI = (Integer) columns[0];
			Short id = tempI.shortValue();
			String name = (String)columns[1];
			resultMap.put(id.intValue(), name);
		}
		return resultMap;
	}

	@Override @SuppressWarnings("unchecked")
	public List<String> queryIssueTypeList(Integer categoryId) {
		String sql = "select cit.name from customercarecase_issue_type cit "
				+ "inner join customercarecase_issue_type_category citc on citc.id = cit.category_id "
				+ "where citc.id = :categoryId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("categoryId", categoryId);
		return getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
	}
	
	@SuppressWarnings("unchecked")
	private Map<Locale,String> queryLocaleToNameMap(int issueId){
		String sql = "select ciln.locale_id, ciln.name "
				+ "from customercarecase_issue_locale_name ciln "
				+ "where ciln.issue_id = :issueId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("issueId", issueId);
		Map<Locale,String> resultMap = new HashMap<Locale,String>();
		List<Object[]> columnsList =  getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		for(Object[] columns:columnsList){
			Integer tempI=(Integer) columns[0];
			Short localeKey = tempI.shortValue();
			String name = (String)columns[1];
			resultMap.put(Locale.fromKey(localeKey),name);
		}
		return resultMap;
	}
	
	@SuppressWarnings("unchecked")
	private Map<Locale,String> queryEnUsLocaleToNameMap(int issueId){
		String sql = "select lc.id, ciln.name "
				+ "from customercarecase_issue_locale_name ciln "
				+ "inner join locale lc on lc.id = ciln.locale_id "
				+ "where ciln.issue_id = :issueId and lc.code = :localeCode ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("issueId", issueId);
		q.addValue("localeCode", Locale.EN_US.getCode());
		Map<Locale,String> resultMap = new HashMap<Locale,String>(1);
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
//		Assert.isTrue(columnsList.size()==1);
		System.out.println("issueId: " + issueId);
		System.out.println("queryEnUsLocaleToNameMap: size=" + columnsList.size());
		if (!columnsList.isEmpty()) {
			Integer tempI =(Integer) columnsList.get(0)[0];
			Short localeKey = tempI.shortValue();
			String name = (String) columnsList.get(0)[1];
			resultMap.put(Locale.fromKey(localeKey), name);
		}
		return resultMap;
	}

	@Override @SuppressWarnings("unchecked")
	public String queryEnUsIssueName(int issueId) {
		String sql = "select name from customercarecase_issue_locale_name ciln "
				+ "inner join customercarecase_issue ci on ci.id = ciln.issue_id "
				+ "inner join locale l on l.id = ciln.locale_id "
				+ "where ci.id = :issueId and l.code = 'en_US' ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("issueId", issueId);
		List<String> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		Assert.isTrue(resultList.size()==1);
		return resultList.get(0);
	}

	@Override @Transactional("transactionManager")
	public void updateIssueStatus(int issueId, String status) {
		String sql = "update customercarecase_issue set status = :status where id = :issueId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("issueId", issueId);
		q.addValue("status", status);
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
	}

	@Override @Transactional("transactionManager")
	public void setNeedRenotifyForCommentToSupplier(int issueId,boolean value) {
		String sql = "update customercarecase_issue set need_renotify_for_new_comment_to_supplier = :value where id = :issueId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("issueId", issueId);
		q.addValue("value", value);
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
	}

	@Override @SuppressWarnings("unchecked")
	public String queryKcodeOfSupplierInIssue(int issueId) {
		String sql = "select com.k_code from company com "
				+ "inner join customercarecase_issue ci on ci.supplier_company_id = com.id "
				+ "where com.is_supplier = TRUE "
				+ "and ci.id = :issueId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("issueId", issueId);
		List<String> kcodeList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		Assert.isTrue(kcodeList.size()==1);
		return kcodeList.get(0);
	}

	@Override @SuppressWarnings("unchecked")
	public String querySupplierKcodeOfIssue(int issueId) {
		String sql = "select com.k_code from company com "
				+ "inner join customercarecase_issue ci on ci.supplier_company_id = com.id "
				+ "where com.is_supplier = TRUE "
				+ "and ci.id = :issueId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("issueId", issueId);
		List<String> resultList =  getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		if(resultList.size()==0) return null;
		Assert.isTrue(resultList.size()==1&&resultList.get(0)!=null);
		return resultList.get(0);
	}

	@Override @SuppressWarnings("unchecked")
	public String queryEmailOfNewestDrsCommenter(int issueId) {
		String sql = "select ui.user_email from user_info ui "
				+ "inner join company com on ui.company_id = com.id "
				+ "inner join customercarecase_issue_comment cic on cic.creator_id = ui.user_id "
				+ "inner join customercarecase_issue ci on cic.issue_id = ci.id "
				+ "where ci.id = :issueId "
				+ "and com.is_drs_company = TRUE "
				+ "order by cic.date_create desc offset 0 limit 1";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("issueId", issueId);
		List<String> resultList = getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		if(resultList.size()==0) return null;
		Assert.isTrue(resultList.size()==1);
		return resultList.get(0);
	}

	@Override @SuppressWarnings("unchecked")
	public Map<Integer,Map<Integer,String>> queryCategoryIdToIssueTypeAndIdMap() {
		String sql = "select cit.category_id as category_name, "
				+ "                   cit.id as type_id, "
				+ "                 cit.name as type_name "
				+ "from customercarecase_issue_type cit "
				+ "order by cit.name ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		Map<Integer,Map<Integer,String>> resultMap = new TreeMap<Integer,Map<Integer,String>>();
		for(Object[] columns:columnsList){
			Integer categoryId = (Integer)columns[0];
			Integer typeId = (Integer)columns[1];
			String typeName = (String)columns[2];
			if(!resultMap.containsKey(categoryId)) resultMap.put(categoryId, new TreeMap<Integer,String>());
			resultMap.get(categoryId).put(typeId, typeName); 
		}
		return resultMap;
	}

	@Override @Transactional("transactionManager")
	public void updateType(Integer id, String name) {
		String sql = "update customercarecase_issue_type set name = :name where id = :id ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("id", id);
		q.addValue("name", name);
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
		return;
	}

	@Override @SuppressWarnings("unchecked")
	public Map<Integer,String> queryTypeIdToNameMap(Integer categoryId) {
		String sql = "select cit.id, cit.name from customercarecase_issue_type cit "
				+ "where cit.category_id = :categoryId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("categoryId", categoryId);
		List<Object[]> columnsList =  getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		Map<Integer,String> resultMap = new TreeMap<Integer,String>();
		for(Object [] columns:columnsList){
			Integer id = (Integer)columns[0];
			String name = (String)columns[1];
			resultMap.put(id, name);
		}
		return resultMap;
	}

	@Override @SuppressWarnings("unchecked")
	public Map<Integer,String> queryTypeIdToNameMap() {
		String sql = "select cit.id, cit.name from customercarecase_issue_type cit ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		List<Object[]> columnsList = getNamedParameterJdbcTemplate().query(sql,q,objArrayMapper);
		Map<Integer,String> resultMap = new TreeMap<Integer,String>();
		for(Object [] columns:columnsList){
			Integer id = (Integer)columns[0];
			String name = (String)columns[1];
			resultMap.put(id, name);
		}
		return resultMap;
	}

	@Override
	public Integer queryMaxIssueId() {
		String sql = "SELECT max(id) from customercarecase_issue";

		return getJdbcTemplate().queryForObject(sql, Integer.class);
	}
}
