package com.kindminds.drs.api.data.access.usecase.sales;

import java.util.List;
import java.util.Map;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.enums.SkuDisplayOption;
import com.kindminds.drs.api.v1.model.accounting.AmazonOrderDetail;
import com.kindminds.drs.api.v1.model.sales.CustomerOrder;
import com.kindminds.drs.api.v1.model.sales.CustomerOrderExporting;
import com.kindminds.drs.api.v1.model.sales.ListCustomerOrderCondition;

public interface ListCustomerOrderDao {

	public Map<String,String> queryProductBaseCodeToNameMap(String supplierKcode);
	public Map<String,String> queryProductSkuCodeToNameMap(String productBaseCode);


	public int queryListCount(int lastDays);
	public List<CustomerOrder> queryList(int lastDays, SkuDisplayOption option, Integer startIndex, Integer size);
	public List<CustomerOrderExporting> queryExportingList(int lastDays, SkuDisplayOption option);
	public int queryListCount(int lastDays, String userCompanyKcode);
	public List<CustomerOrder> queryList(int lastDays, String userCompanyKcode, SkuDisplayOption option, Integer startIndex, Integer size);
	public List<CustomerOrderExporting> queryExportingList(int lastDays, String userCompanyKcode, SkuDisplayOption option);

	public int queryListCount(ListCustomerOrderCondition actualCondition);
	public List<CustomerOrder> queryList(ListCustomerOrderCondition condition, SkuDisplayOption option, Integer startIndex, Integer size);
	public List<CustomerOrderExporting> queryExportingList(ListCustomerOrderCondition condition, SkuDisplayOption option);

	public AmazonOrderDetail queryAmazonOrderDetail(Marketplace marketplace,String orderId,String sku);
	public Map<String,String> queryShopifyOrderDetail(String orderId);
	public String queryShopifyFieldData(String field,String orderId,String sku);
	public String queryShopifyJsonData(ShopifyJsonDetailType type,String orderId,String sku);
	
	public enum ShopifyJsonDetailType{
		DESTINATION_LOCATION("json_destination_location"),
		TAX_LINES("json_tax_lines");		
		private String dbTableColumnName;
		ShopifyJsonDetailType(String value){
			this.dbTableColumnName = value;
		}
		public String getDbTableColumnName(){
			return this.dbTableColumnName;
		}
				
	}
	
}