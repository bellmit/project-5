package com.kindminds.drs.impl;

import com.kindminds.drs.Locale;
import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseIssue;
import com.kindminds.drs.util.TestUtil;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerCareCaseIssueImpl implements CustomerCareCaseIssue {
	
	private Integer id;
	private Integer categoryId;
	private Integer typeId;
	private Map<Locale,String> localeCodeToNameMap;
	private String status;
	private String supplierKcode;
	private List<String> relatedProductBaseCodeList;
	private List<String> relatedProductSkuCodeList;
	private String occurrences;
	private String createdDate;
	private List<CustomerCareCaseIssueTemplate> templateList = null;
	private List<CustomerCareCaseIssueComment> commentList=null;

	private String name;
	private String issueTypeName; 
	private String supplierEnName;
	private List<String> relatedProductBaseList;
	private List<String> relatedProductSkuList;
	
	public CustomerCareCaseIssueImpl(
			Integer id, 
			Integer categoryId,
			Integer typeId,
			Map<String,String> languageToNameMap, 
			String status,
			String supplierKcode, 
			List<String> relatedProductBaseCodeList,
			List<String> relatedProductSkuCodeList, 
			String occurrences,
			String createdDate,
			List<CustomerCareCaseIssueTemplate> templateList,
			List<CustomerCareCaseIssueComment> comments) {
		this.id = id;
		this.categoryId = categoryId;
		this.typeId = typeId;
		this.localeCodeToNameMap = new HashMap<Locale,String>();
		for(String localeCode:languageToNameMap.keySet()){
			this.localeCodeToNameMap.put(Locale.fromCode(localeCode),languageToNameMap.get(localeCode));
		}
			
		this.status = status;
		this.supplierKcode = supplierKcode;
		this.relatedProductBaseCodeList = relatedProductBaseCodeList;
		this.relatedProductSkuCodeList = relatedProductSkuCodeList;
		this.occurrences = occurrences;
		this.createdDate = createdDate;
		this.templateList = templateList;
		this.commentList = comments;
	}
	
	@Override
	public String toString() {
		return "CustomerCareCaseIssueImpl [getId()=" + getId() + ", getCategoryId()=" + getCategoryId()
				+ ", getTypeId()=" + getTypeId() + ", getLocaleCodeToNameMap()=" + getLocaleCodeToNameMap()
				+ ", getStatus()=" + getStatus() + ", getSupplierKcode()=" + getSupplierKcode()
				+ ", getRelatedProductBaseCodeList()=" + getRelatedProductBaseCodeList()
				+ ", getRelatedProductSkuCodeList()=" + getRelatedProductSkuCodeList() + ", getTemplateOccurrences()="
				+ getTemplateOccurrences() + ", getCreatedDate()=" + getCreatedDate() + ", getDaysFromLastUpdate()="
				+ getDaysFromLastUpdate() + ", getHoursFromLastUpdate()=" + getHoursFromLastUpdate()
				+ ", getMinsFromLastUpdate()=" + getMinsFromLastUpdate() + ", getTemplates()=" + getTemplates()
				+ ", getComments()=" + getComments() + "]";
	}

	@Override
	public boolean equals(Object obj){
		if ( obj instanceof CustomerCareCaseIssue ){
			CustomerCareCaseIssue issue = ((CustomerCareCaseIssue) obj);
			return this.getId().equals(issue.getId()) 
				&& this.getCategoryId().equals(issue.getCategoryId())
				&& this.getTypeId().equals(issue.getTypeId())
				&& this.getLocaleCodeToNameMap().equals(issue.getLocaleCodeToNameMap())
				&& this.getStatus().equals(issue.getStatus())
				&& TestUtil.nullableEquals(this.getSupplierKcode(),issue.getSupplierKcode())
				&& TestUtil.nullableEquals(this.getTemplates(),issue.getTemplates())
				&& TestUtil.nullableEquals(this.getComments(),issue.getComments())
				&& TestUtil.nullableEquals(this.getRelatedProductBaseCodeList(), issue.getRelatedProductBaseCodeList())
				&& TestUtil.nullableEquals(this.getRelatedProductSkuCodeList(), this.getRelatedProductSkuCodeList())
				&& this.getTemplateOccurrences().equals(issue.getTemplateOccurrences());
		}
	    return false;
	}
	
	public void addTemplate(CustomerCareCaseIssueTemplate t){
		this.templateList.add(t);
	}
	
	public void addComment(CustomerCareCaseIssueComment c){
		Assert.notNull(this.commentList);
		this.commentList.add(c);
	}

	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public Integer getCategoryId() {
		return this.categoryId;
	}

	@Override
	public Integer getTypeId() {
		return this.typeId;
	}

	@Override
	public Map<Locale,String> getLocaleCodeToNameMap(){
		return this.localeCodeToNameMap;
	}

	@Override
	public String getStatus() {
		return this.status;
	}

	@Override
	public String getSupplierKcode() {
		return this.supplierKcode;
	}

	@Override
	public List<String> getRelatedProductBaseCodeList() {
		return this.relatedProductBaseCodeList;
	}

	@Override
	public List<String> getRelatedProductSkuCodeList() {
		return this.relatedProductSkuCodeList;
	}

	@Override
	public String getTemplateOccurrences() {
		return this.occurrences;
	}

	@Override
	public String getCreatedDate() {
		return this.createdDate;
	}

	@Override
	public Integer getDaysFromLastUpdate() {
		return null;
	}

	@Override
	public Integer getHoursFromLastUpdate() {
		return null;
	}

	@Override
	public Integer getMinsFromLastUpdate() {
		return null;
	}

	@Override
	public List<CustomerCareCaseIssueTemplate> getTemplates() {
		return this.templateList;
	}
	
	@Override
	public List<CustomerCareCaseIssueComment> getComments() {
		return this.commentList;
	}
	
	

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getIssueTypeName() {
		return issueTypeName;
	}

	@Override
	public String getSupplierEnName() {
		return supplierEnName;
	}

	@Override
	public void setLocaleToNameMap(Map<Locale, String> map) {
		localeCodeToNameMap = map;
	}

	@Override
	public void setRelatedProductBaseList(List<String> baseList) {
		relatedProductBaseList = baseList;
	}

	@Override
	public void setRelatedProductSkuList(List<String> skuList) {
		relatedProductSkuList = skuList;
	}

	@Override
	public void setOccurrences(Integer occurrences) {
		this.occurrences = Integer.toString(occurrences);
		
	}

}
