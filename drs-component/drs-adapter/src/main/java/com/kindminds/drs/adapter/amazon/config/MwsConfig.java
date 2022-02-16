package com.kindminds.drs.adapter.amazon.config;

import java.util.List;

import com.amazonaws.mws.model.IdList;
import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersAsyncClient;

public interface MwsConfig<T>{

	 String getSellerId();

	 String getMarketplaceId();
	 List<String>  getMarketplaceIds();
	 IdList getMarketplaceIdList();

	 T getClient();
}
