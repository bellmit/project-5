package com.kindminds.drs.persist.v1.model.mapping.customercare;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;






import com.kindminds.drs.Locale;
import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseIssue;



public class CustomerCareCaseIssueImpl implements CustomerCareCaseIssue {

	//@Id ////@Column(name="id")
	private Integer id;
	//@Column(name="category_id")
	private Integer categoryId;
	//@Column(name="type_id")
	private Integer typeId;
	//@Column(name="status")
	private String status;
	//@Column(name="supplier_kcode")
	private String supplierKcode;
	//@Column(name="date_create")
	private String dateCreate;
	//@Column(name="seconds_from_last_update")
	private Integer secondsFromLastUpdate;

	public CustomerCareCaseIssueImpl(Integer id, Integer categoryId,
									 Integer typeId, String status,
									 String supplierKcode ,
									 String dateCreate, Integer secondsFromLastUpdate) {
		this.id = id;
		this.categoryId = categoryId;
		this.typeId = typeId;
		this.status = status;
		this.supplierKcode = supplierKcode;
		this.dateCreate = dateCreate;

	/*	this.dateCreate = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss").format(
				new Date(dateCreate - 28800000));	//28800000ms = 8hrs
	*/
		this.secondsFromLastUpdate = secondsFromLastUpdate;
	}

	private String name; //"Sized small",

	private String issueTypeName; //"ProductDto details",

	private String supplierEnName;

	private Map<Locale,String> localeCodeToNameMap;

	private List<String> relatedProductBaseList;

	private List<String> relatedProductSkuList;

	private Integer occurrences;

	private List<CustomerCareCaseIssue.CustomerCareCaseIssueTemplate> templateList=null;

	private List<CustomerCareCaseIssueComment> commentList=null;

	public CustomerCareCaseIssueImpl() {}



	public CustomerCareCaseIssueImpl(Integer id, String name,
									 String issueTypeName, String status,
									 String supplierKcode, String supplierEnName,
									 Long dateCreate, Integer secondsFromLastUpdate) {
		this.id = id;
		this.name = name;
		this.issueTypeName = issueTypeName;
		this.status = status;
		this.supplierKcode = supplierKcode;
		this.supplierEnName = supplierEnName;
		this.dateCreate = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss").format(
				new Date(dateCreate - 28800000));	//28800000ms = 8hrs
		this.secondsFromLastUpdate = secondsFromLastUpdate;
	}

	public void setLocaleToNameMap(Map<Locale,String> map){
		this.localeCodeToNameMap = map;
	}
	
	public void setRelatedProductBaseList(List<String> baseList){
		this.relatedProductBaseList = baseList;
	}
	
	public void setRelatedProductSkuList(List<String> skuList){
		this.relatedProductSkuList = skuList;
	}
	
	public void setOccurrences(Integer i){
		this.occurrences = i;
	}
	
	public void setTemplateList(List<CustomerCareCaseIssueTemplate> t){
		this.templateList = t;
	}

	public void setCommentList(List<CustomerCareCaseIssueComment> comments){
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
	public Map<Locale,String> getLocaleCodeToNameMap() {
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
	public List<String> getRelatedProductBaseCodeList() {
		return this.relatedProductBaseList;
	}

	@Override
	public List<String> getRelatedProductSkuCodeList() {
		return this.relatedProductSkuList;
	}

	@Override
	public String getTemplateOccurrences() {
		return this.occurrences.toString();
	}

	@Override
	public String getCreatedDate() {
		return this.dateCreate;
	}

	@Override
	public Integer getDaysFromLastUpdate() {
		if(this.secondsFromLastUpdate==null) return null;
		return this.secondsFromLastUpdate/(60*60*24);
	}

	@Override
	public Integer getHoursFromLastUpdate() {
		if(this.secondsFromLastUpdate==null) return null;
		return this.secondsFromLastUpdate/(60*60)%24;
	}

	@Override
	public Integer getMinsFromLastUpdate() {
		if(this.secondsFromLastUpdate==null) return null;
		return this.secondsFromLastUpdate/(60)%60;
	}
	
	@Override
	public List<CustomerCareCaseIssueTemplate> getTemplates() {
		return this.templateList;
	}

	@Override
	public List<CustomerCareCaseIssueComment> getComments() {
		return this.commentList;
	}

	

}
