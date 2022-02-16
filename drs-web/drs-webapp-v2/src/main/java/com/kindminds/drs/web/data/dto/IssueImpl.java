package com.kindminds.drs.web.data.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang.StringUtils;

import com.kindminds.drs.Locale;
import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseIssue;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class IssueImpl implements CustomerCareCaseIssue {

	private Integer id;
	private Integer categoryId;
	private Integer typeId;
	private List<String> language;
	private List<String> issueName;
	private Map<Locale,String> localeCodeToNameMap; 
	private String status;	
	private String supplierKcode;
	private List<String> relatedProductBaseCodeList;
	private List<String> relatedProductSkuCodeList;
	private String templateOccurrences;
	private String createdDate;
	private List<IssueCommentImpl> comments = new ArrayList<IssueCommentImpl>();
	private List<CustomerCareCaseTemplateImpl> templates = new ArrayList<CustomerCareCaseTemplateImpl>();
	private Integer daysFromLastUpdate;
	private Integer hoursFromLastUpdate;
	private Integer minsFromLastUpdate;
	
	@Override
	public String toString() {
		return "IssueImpl [getId()=" + getId() + ", getCategoryId()=" + getCategoryId() + ", getTypeId()=" + getTypeId()
				+ ", getLanguage()=" + getLanguage() + ", getIssueName()=" + getIssueName()
				+ ", getLocaleCodeToNameMap()=" + getLocaleCodeToNameMap() + ", getStatus()=" + getStatus()
				+ ", getSupplierKcode()=" + getSupplierKcode() + ", getRelatedProductBaseCodeList()="
				+ getRelatedProductBaseCodeList() + ", getRelatedProductSkuCodeList()=" + getRelatedProductSkuCodeList()
				+ ", getTemplateOccurrences()=" + getTemplateOccurrences() + ", getCreatedDate()=" + getCreatedDate()
				+ ", getComments()=" + getComments() + ", getComment()=" + getComment() + ", getTemplates()="
				+ getTemplates() + ", getTemplate()=" + getTemplate() + ", getDaysFromLastUpdate()="
				+ getDaysFromLastUpdate() + ", getHoursFromLastUpdate()=" + getHoursFromLastUpdate()
				+ ", getMinsFromLastUpdate()=" + getMinsFromLastUpdate() + "]";
	}

	@Override
	public Integer getId() {
		return this.id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Override
	public Integer getCategoryId() {		
		return this.categoryId;
	}

	public void setCategoryId(Integer categoryId){
		this.categoryId = categoryId;		
	}
		
	@Override
	public Integer getTypeId() {		
		return this.typeId;
	}

	public void setTypeId(Integer typeId){
		this.typeId = typeId;				
	}
	
	public List<String> getLanguage() {		
		return this.language;
	}

	public void setLanguage(List<String> language) {
		this.language = language; 
	}
	
	
	public List<String> getIssueName() {		
		return this.issueName;
	}

	public void setIssueName(List<String> issueName) {
		this.issueName = issueName; 		
	}
	
	
	@Override
	public Map<Locale,String> getLocaleCodeToNameMap() {
		return this.localeCodeToNameMap;
	}
	
	public void setLocaleCodeToNameMap(Map<Locale,String> localeCodeToNameMap) {
		this.localeCodeToNameMap = localeCodeToNameMap;		
	}
			
	@Override
	public String getStatus() {		
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;		
	}
	
	@Override
	public String getSupplierKcode() {
		return StringUtils.isEmpty(this.supplierKcode)?null:this.supplierKcode;
	}
	
	public void setSupplierKcode(String supplierKcode) {
		this.supplierKcode = supplierKcode;		
	}
	
	@Override
	public List<String> getRelatedProductBaseCodeList() {		
		return this.relatedProductBaseCodeList;
	}

	public void setRelatedProductBaseCodeList(List<String> relatedProductBaseCodeList) {
		this.relatedProductBaseCodeList = relatedProductBaseCodeList;
	}
		
	@Override
	public List<String> getRelatedProductSkuCodeList() {		
		return this.relatedProductSkuCodeList;
	}

	public void setRelatedProductSkuCodeList(List<String> relatedProductSkuCodeList) {
		this.relatedProductSkuCodeList = relatedProductSkuCodeList; 		
	}
	
	@Override
	public String getTemplateOccurrences() {		
		return this.templateOccurrences;
	}

	public void setTemplateOccurrences(String templateOccurrences) {
		this.templateOccurrences = templateOccurrences; 
	}
	
	@Override
	public String getCreatedDate() {		
		return this.createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	
	@Override
	public List<CustomerCareCaseIssueComment> getComments() {
		List<CustomerCareCaseIssueComment> comments = new ArrayList<CustomerCareCaseIssueComment>();
		for(CustomerCareCaseIssueComment comment:this.comments){
			comments.add(comment);			
		}
		return comments;
	}
	
	public List<IssueCommentImpl> getComment() {		
		return this.comments;
	}
	
	public void setComment(List<IssueCommentImpl> comment) {
		this.comments = comment;		
	}

	@Override
	public List<CustomerCareCaseIssueTemplate> getTemplates() {
		List<CustomerCareCaseIssueTemplate> templates = new ArrayList<CustomerCareCaseIssueTemplate>();
		for(CustomerCareCaseIssueTemplate template:this.templates){
			templates.add(template);			
		}		
		return templates;
	}
	
	public List<CustomerCareCaseTemplateImpl> getTemplate() {
		return this.templates;
	} 
	
	public void setTemplate(List<CustomerCareCaseTemplateImpl> template){
		this.templates = template;		
	}

	@Override
	public Integer getDaysFromLastUpdate() {		
		return this.daysFromLastUpdate;
	}
	
	public void setDaysFromLastUpdate(Integer daysFromLastUpdate) {
		this.daysFromLastUpdate = daysFromLastUpdate; 		
	}
	
	@Override
	public Integer getHoursFromLastUpdate() {		
		return this.hoursFromLastUpdate;
	}

	public void setHoursFromLastUpdate(Integer hoursFromLastUpdate) {
		this.hoursFromLastUpdate = hoursFromLastUpdate; 		
	}
		
	@Override
	public Integer getMinsFromLastUpdate() {		
		return this.minsFromLastUpdate;
	}
	
	public void setMinsFromLastUpdate(Integer minsFromLastUpdate) {
		this.minsFromLastUpdate = minsFromLastUpdate;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public String getIssueTypeName() {
		return null;
	}

	@Override
	public String getSupplierEnName() {
		return null;
	}

	@Override
	public void setLocaleToNameMap(Map<Locale, String> map) {
		
	}

	@Override
	public void setRelatedProductBaseList(List<String> baseList) {
		
	}

	@Override
	public void setRelatedProductSkuList(List<String> skuList) {
		
	}

	@Override
	public void setOccurrences(Integer i) {
		
	}
	
}