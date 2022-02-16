package com.kindminds.drs.api.usecase.sales;

import java.util.List;
import java.util.Map;


import com.kindminds.drs.api.v2.biz.domain.model.SalesChannel;
import com.kindminds.drs.api.v1.model.sales.CustomerOrder;
import com.kindminds.drs.api.v1.model.sales.CustomerOrderExporting;
import com.kindminds.drs.api.v1.model.sales.ListCustomerOrderCondition;
import com.kindminds.drs.api.v1.model.sales.ShopifyOrderItemDetail;

import com.kindminds.drs.api.v1.model.accounting.AmazonOrderDetail;
import com.kindminds.drs.api.v1.model.common.DtoList;

public interface ListCustomerOrderUco {


	public boolean isCurrentUserDrsUser();
	public boolean conditionsAreAllNull(ListCustomerOrderCondition condition);
	public List<SalesChannel> getSalesChannels();
	public Map<String,String> getSupplierKcodeToNameMap();
	public Map<String,String> getProductBaseCodeToNameMap(String supplierKcode);
	public Map<String,String> getProductSkuCodeToNameMap(String productBaseCode);
	public DtoList<CustomerOrder> getList(ListCustomerOrderCondition condition,int pageIndex);
	public List<CustomerOrder> getList(ListCustomerOrderCondition condition);
	public List<CustomerOrderExporting> getListForExporting(ListCustomerOrderCondition condition);
	DtoList<CustomerOrder> getListElastic(String searchTerms, Integer pageIndex, Long start, Long end);
	public AmazonOrderDetail getAmazonOrderDetail(String marketplaceId,String orderId,String sku);
	public ShopifyOrderItemDetail getShopifyOrderDetail(String orderId, String sku);
}
