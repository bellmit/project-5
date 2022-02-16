package com.kindminds.drs.persist.v1.model.mapping.customercare;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;






import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseDto;
import org.springframework.util.StringUtils;


public class CustomerCareCaseDtoImpl implements CustomerCareCaseDto {

	//@Id ////@Column(name="id")
	private int id;
	//@Column(name="type")
	private String type;
	//@Column(name="drs_company_kcode")
	private String drsCompanyKcode;
	//@Column(name="supplier_kcode")
	private String supplierKcode;
	//@Column(name="issue_type_category_id")
	private Integer issueTypeCategoryId;
	//@Column(name="marketplace_id")
	private Integer marketplaceId;
	//@Column(name="marketplace_order_id")
	private String marketPlaceOrderId;
	//@Column(name="marketplace_order_date")
	private String marketPlaceOrderDate;
	//@Column(name="customer_name")
	private String customerName;
	//@Column(name="date_create")
	private String dateCreate;
	//@Column(name="status")
	private String status;
	//@Column(name="seconds_from_last_activity")
	private Integer secondsFromLastActivity;

	private List<Integer> relatedIssueIdList=null;

	private List<String> relatedProductBaseList=null;

	private List<String> relatedProductSkuList=null;

	private List<CustomerCareCaseMessage> msgList=null;

	private String marketplaceName;



	public CustomerCareCaseDtoImpl(int id, String type, String drsCompanyKcode, String supplierKcode, Integer issueTypeCategoryId, Integer marketplaceId, String marketPlaceOrderId, String marketPlaceOrderDate, String customerName, String dateCreate, String status, Integer secondsFromLastActivity) {
		this.id = id;
		this.type = type;
		this.drsCompanyKcode = drsCompanyKcode;
		this.supplierKcode = supplierKcode;
		this.issueTypeCategoryId = issueTypeCategoryId;
		this.marketplaceId = marketplaceId;
		this.marketPlaceOrderId = marketPlaceOrderId;
		this.marketPlaceOrderDate = marketPlaceOrderDate;
		this.customerName = customerName;
		this.dateCreate = dateCreate;
		this.status = status;
		this.secondsFromLastActivity = secondsFromLastActivity;
	}

	public CustomerCareCaseDtoImpl(Integer id, String type,
								   Integer issueTypeCategoryId, String marketplaceName,
								   String customerName, Long dateCreate,
								   String status, Integer secondsFromLastActivity,
								   Integer issueId, String skuCode, String baseCode) {
		this.id = id;
		this.type = type;
		this.issueTypeCategoryId = issueTypeCategoryId;
		this.marketplaceName = marketplaceName;
		this.marketplaceId = Marketplace.fromName(marketplaceName).getKey();
		this.customerName = customerName;
		this.dateCreate = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss").format(
						new Date(dateCreate - 28800000));	//28800000ms = 8hrs
		this.status = status;
		this.secondsFromLastActivity = secondsFromLastActivity;
		this.relatedIssueIdList = new ArrayList<>();
		relatedIssueIdList.add(issueId);
		if (StringUtils.hasText(baseCode)) {
			this.relatedProductBaseList = new ArrayList<>();
			supplierKcode = baseCode.substring(3, 7);
			relatedProductBaseList.add(baseCode);
		}
		if (StringUtils.hasText(skuCode)) {
			this.relatedProductSkuList = new ArrayList<>();
			supplierKcode = skuCode.substring(0, 4);
			relatedProductSkuList.add(skuCode);
		}
	}

	@Override
	public String toString() {
		return "CustomerCareCaseDtoImpl [getCaseId()=" + getCaseId() + ", getCaseType()=" + getCaseType()
				+ ", getIssueTypeCategoryId()=" + getIssueTypeCategoryId() + ", getRelatedIssueIds()="
				+ getRelatedIssueIds() + ", getStatus()=" + getStatus() + ", getDrsCompanyKcode()="
				+ getDrsCompanyKcode() + ", getMarketplace()=" + getMarketplace() + ", getMarketplaceOrderId()="
				+ getMarketplaceOrderId() + ", getMarketplaceOrderDate()=" + getMarketplaceOrderDate()
				+ ", getSupplierKcode()=" + getSupplierKcode() + ", getLatestActivityDays()=" + getLatestActivityDays()
				+ ", getLatestActivityHours()=" + getLatestActivityHours() + ", getLatestActivityMinutes()="
				+ getLatestActivityMinutes() + ", getRelatedProductBaseCodeList()=" + getRelatedProductBaseCodeList()
				+ ", getRelatedProductSkuCodeList()=" + getRelatedProductSkuCodeList() + ", getCustomerName()="
				+ getCustomerName() + ", getDateCreated()=" + getDateCreated() + ", getMessages()=" + getMessages()
				+ "]";
	}

	@Override
	public Integer getCaseId() {
		return this.id;
	}

	@Override
	public String getCaseType() {
		return CustomerCareCaseType.valueOf(this.type).name();
	}

	@Override
	public Integer getIssueTypeCategoryId() {
		return this.issueTypeCategoryId;
	}

	@Override
	public List<Integer> getRelatedIssueIds() {
		return this.relatedIssueIdList;
	}

	@Override
	public String getStatus() {
		return this.status;
	}

	@Override
	public String getDrsCompanyKcode() {
		return this.drsCompanyKcode;
	}
	
	@Override
	public Marketplace getMarketplace() {
		return this.marketplaceId != null ?  Marketplace.fromKey(this.marketplaceId) : Marketplace.AMAZON_COM;
	}
	
	@Override
	public String getMarketplaceOrderId() {
		return this.marketPlaceOrderId;
	}

	@Override
	public String getMarketplaceOrderDate() {
		return this.marketPlaceOrderDate;
	}

	@Override
	public String getSupplierKcode() {
		return this.supplierKcode;
	}

	@Override
	public Integer getLatestActivityDays() {
		return this.secondsFromLastActivity / (60 * 60 *24);
	}

	@Override
	public Integer getLatestActivityHours() {
		return this.secondsFromLastActivity / (60 * 60 ) % 24;
	}
	
	@Override
	public Integer getLatestActivityMinutes() {
		return this.secondsFromLastActivity / ( 60 ) % 60;
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
	public String getCustomerName() {
		return this.customerName;
	}

	@Override
	public String getDateCreated() {
		return this.dateCreate;
	}

	@Override
	public List<CustomerCareCaseMessage> getMessages() {
		return this.msgList;
	}
	
	public void setRelatedIssueIdList(List<Integer> list){
		this.relatedIssueIdList = list;
	}
	
	public void setRelatedProductBaseList(List<String> list){
		this.relatedProductBaseList=list;
	}
	
	public void setRelatedProductSkuList(List<String> list){
		this.relatedProductSkuList=list;
	}
	
	public void setMessages(List<CustomerCareCaseMessage>msgs){
		this.msgList=msgs;
	}

}
