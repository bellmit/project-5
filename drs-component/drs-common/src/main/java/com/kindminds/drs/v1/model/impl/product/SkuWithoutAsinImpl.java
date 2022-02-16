package com.kindminds.drs.v1.model.impl.product;

import com.kindminds.drs.api.v1.model.product.SkuWithoutAsin;

import java.io.Serializable;

public class SkuWithoutAsinImpl implements SkuWithoutAsin, Serializable {

	private String marketplace; 
	private String sku; 

	public SkuWithoutAsinImpl(
			String marketplace,
			String sku){
		this.marketplace = marketplace;
		this.sku = sku;
	};
		
	@Override
	public String getMarketplace() {
		return this.marketplace;
	}

	@Override
	public String getSku() {
		return this.sku;
	}
	
}