package com.kindminds.drs.persist.data.access.usecase.customercare;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;





import com.kindminds.drs.persist.data.access.rdb.Dao;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.kindminds.drs.Locale;
import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.data.access.usecase.customercare.MaintainCustomerCareCaseIssueTemplateDao;
import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseIssue.CustomerCareCaseIssueStatus;
import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseIssue.CustomerCareCaseIssueTemplate;
import com.kindminds.drs.persist.v1.model.mapping.customercare.CustomerCareCaseIssueTemplateImpl;
import com.kindminds.drs.persist.data.access.rdb.util.PostgreSQLHelper;

@Repository
public class MaintainCustomerCareCaseIssueTemplateDaoImpl extends Dao implements MaintainCustomerCareCaseIssueTemplateDao {
	

	private static String templateLocaleRelTableName = "customercarecase_issue_template_locale_rel";
	private static String templateCaseTypeRelTableName = "customercarecase_issue_template_case_type_rel";
	private static String templateCountryRelTableName = "customercarecase_issue_template_country_rel";
	private static String templateMarketplaceRelTableName = "customercarecase_issue_template_marketplace_rel";

	@Override @Transactional("transactionManager")
	public int insert(CustomerCareCaseIssueTemplate template) {
		String sql = "insert into customercarecase_issue_template (id, issue_id, content) values (:id, :issueId, :content) ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		int id = PostgreSQLHelper.getNextVal(getNamedParameterJdbcTemplate(),"customercarecase_issue_template","id");
		q.addValue("id", id);
		q.addValue("issueId", template.getIssueId());
		q.addValue("content", template.getContents());
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
		this.insertApplicableLocales(id,template.getApplicableLanguages());
		this.insertApplicableCaseTypes(id,template.getApplicableCaseTypes());
		this.insertApplicableMarketRegions(id,template.getApplicableMarketRegions());
		this.insertApplicableMarketplaces(id,template.getApplicableMarketplaceList());
		this.updateIssueLastUpdatedDateOfTemplate(id, new Date());
		return id;
	}
	
	private void updateIssueLastUpdatedDateOfTemplate(int templateId,Date date) {
		String sql = "update customercarecase_issue ci set date_last_updated = :date "
				+ "from customercarecase_issue_template cit "
				+ "where ci.id = cit.issue_id and cit.id = :id ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("id", templateId);
		q.addValue("date", date);
		Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
	}
	
	@Override @Transactional("transactionManager")
	public CustomerCareCaseIssueTemplate update(CustomerCareCaseIssueTemplate template) {
		String sql = "update customercarecase_issue_template set "
				+ " content = :content where id = :templateId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("content", template.getContents());
		q.addValue("templateId", template.getId());
		getNamedParameterJdbcTemplate().update(sql,q);
		System.out.println(template.getId());
		this.deleteRelatedData(template.getId(),templateLocaleRelTableName);
		this.deleteRelatedData(template.getId(),templateCaseTypeRelTableName);
		this.deleteRelatedData(template.getId(),templateCountryRelTableName);
		this.deleteRelatedData(template.getId(),templateMarketplaceRelTableName);
		this.insertApplicableLocales(template.getId(),template.getApplicableLanguages());
		this.insertApplicableCaseTypes(template.getId(),template.getApplicableCaseTypes());
		this.insertApplicableMarketRegions(template.getId(),template.getApplicableMarketRegions());
		this.insertApplicableMarketplaces(template.getId(),template.getApplicableMarketplaceList());
		this.updateIssueLastUpdatedDateOfTemplate(template.getId(), new Date());
		return this.query(template.getId());
	}
	
	private void insertApplicableLocales(int templateId,List<Locale> localeList){
		if(localeList!=null){
			String sql = "insert into customercarecase_issue_template_locale_rel "
					+ "( template_id, locale_id  ) select :templateId, locale.id "
					+ "from locale where locale.code = :localeCode ";
			for(Locale locale: localeList){
				MapSqlParameterSource q = new MapSqlParameterSource();
				q.addValue( "templateId", templateId);
				q.addValue("localeCode", locale.getCode());
				Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
			}
		}
	}
	
	private void insertApplicableCaseTypes(int templateId,List<String> caseTypeList){
		if(caseTypeList!=null){
			String sql = "insert into customercarecase_issue_template_case_type_rel "
					+ "( template_id, case_type_id  ) select :templateId, ct.id "
					+ "from customercarecase_type ct where ct.name = :caseType ";
			for(String caseType: caseTypeList){
				MapSqlParameterSource q = new MapSqlParameterSource();
				q.addValue( "templateId", templateId);
				q.addValue("caseType", caseType);
				Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
			}
		}
	}
	
	private void insertApplicableMarketRegions(int templateId,List<String> countryList){
		if(countryList!=null){
			String sql = "insert into customercarecase_issue_template_country_rel "
					+ "( template_id, country_id  ) select :templateId, country.id "
					+ "from country where country.code = :countryCode ";
			for(String countryCode: countryList){
				MapSqlParameterSource q = new MapSqlParameterSource();
				q.addValue( "templateId", templateId);
				q.addValue("countryCode", countryCode);
				Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
			}
		}
	}
	
	private void insertApplicableMarketplaces(int templateId,List<Marketplace> marketplaceList){
		if(marketplaceList!=null){
			String sql = "insert into customercarecase_issue_template_marketplace_rel (template_id,marketplace_id) select :templateId, m.id "
					+ "from marketplace m where m.id = :marketplaceId ";
			for(Marketplace marketplace: marketplaceList){
				MapSqlParameterSource q = new MapSqlParameterSource();
				q.addValue("templateId", templateId);
				q.addValue("marketplaceId", marketplace.getKey());
				Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
			}
		}
	}
	
	private void deleteRelatedData(int templateId, String tableName){
		String sql = "delete from "+tableName+" where template_id = :templateId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("templateId", templateId);
		getNamedParameterJdbcTemplate().update(sql,q);
	}

	@Override @SuppressWarnings("unchecked")
	public CustomerCareCaseIssueTemplate query(int id) {
		String sql = "select cit.id as id, "
				+ "    cit.issue_id as issue_id, "
				+ "       ciln.name as issue_name, "
				+ "     cit.content as content "
				+ "from customercarecase_issue_template cit "
				+ "inner join customercarecase_issue ci on ci.id = cit.issue_id "
				+ "inner join customercarecase_issue_locale_name ciln on ciln.issue_id = ci.id "
				+ "inner join locale l on l.id = ciln.locale_id "
				+ "where cit.id = :id and l.code = 'en_US' ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("id", id);
		List<CustomerCareCaseIssueTemplateImpl> resultList = getNamedParameterJdbcTemplate().query(
				sql,q,(rs,rowNum)->	new CustomerCareCaseIssueTemplateImpl(rs.getInt("id"),rs.getInt("issue_id"),
						rs.getString("issue_name"),rs.getString("content")));
		Assert.isTrue(resultList.size()==0||resultList.size()==1);
		if(resultList.size()==0) return null;
		CustomerCareCaseIssueTemplateImpl result = resultList.get(0);
		result.setLocales(this.queryLocaleList(id));
		result.setCaseTypes(this.queryCaseTypeList(id));
		result.setMarketRegions(this.queryMarketCountryList(id));
		result.setMarketplaces(this.queryMarketplaceList(id));
		return resultList.get(0);
	}
	
	@SuppressWarnings("unchecked")
	private List<Locale> queryLocaleList(int templateId){
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
	private List<String> queryCaseTypeList(int templateId){
		String sql = "select ct.name from customercarecase_issue_template_case_type_rel citctr "
				+ "inner join customercarecase_type ct on ct.id = citctr.case_type_id "
				+ "where citctr.template_id = :templateId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("templateId", templateId);
		List<String> resultList =  getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		return resultList.size()==0?null:resultList;
	}

	@SuppressWarnings("unchecked")
	private List<String> queryMarketCountryList(int templateId){
		String sql = "select c.code from customercarecase_issue_template_country_rel citcr "
				+ "inner join country c on c.id = citcr.country_id "
				+ "where citcr.template_id = :templateId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("templateId", templateId);
		List<String> resultList =  getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
		return resultList.size()==0?null:resultList;
	}

	@SuppressWarnings("unchecked")
	private List<Marketplace> queryMarketplaceList(int templateId){
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

	@Override @Transactional("transactionManager")
	public boolean delete(int id) {
		this.deleteRelatedData(id,templateLocaleRelTableName);
		this.deleteRelatedData(id,templateCaseTypeRelTableName);
		this.deleteRelatedData(id,templateCountryRelTableName);
		this.deleteRelatedData(id,templateMarketplaceRelTableName);
		String sql = "delete from customercarecase_issue_template where id = :templateId";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("templateId", id);
		getNamedParameterJdbcTemplate().update(sql,q);
		return true;
	}

	@Override @SuppressWarnings("unchecked")
	public List<String> queryCaseTypeList() {
		String sql = "select name from customercarecase_type ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		return getNamedParameterJdbcTemplate().queryForList(sql,q,String.class);
	}

	@Override @SuppressWarnings("unchecked")
	public List<Integer> queryMarketplaceIdList() {
		String sql = "select id from marketplace order by id ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		return getNamedParameterJdbcTemplate().queryForList(sql,q,Integer.class);
	}

	@Override @Transactional("transactionManager")
	public void updateIssueStatus(int issueId, CustomerCareCaseIssueStatus status) {
		String sql = "update customercarecase_issue set status = :statusName where id = :issueId ";
		MapSqlParameterSource q = new MapSqlParameterSource();
		q.addValue("statusName", status.name());
		q.addValue("issueId", issueId);
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
}
