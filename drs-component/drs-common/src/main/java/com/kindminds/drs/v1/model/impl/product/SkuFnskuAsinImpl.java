package com.kindminds.drs.v1.model.impl.product;

import com.kindminds.drs.api.v1.model.product.SkuFnskuAsin;

import java.io.Serializable;

public class SkuFnskuAsinImpl implements SkuFnskuAsin, Serializable {
	private String sku;
	private String marketplaceSku;
	private String fnsku;
	private String asin;
	private Boolean afn;
	private Boolean mfn;
	private Boolean storage;
	
	public SkuFnskuAsinImpl(String sku, String marketplaceSku, String fnsku, String asin, Boolean afn, Boolean mfn, Boolean storage) {
		this.sku = sku;
		this.marketplaceSku = marketplaceSku;
		this.fnsku = fnsku;
		this.asin = asin;
		this.afn = afn;
		this.mfn = mfn;
		this.storage = storage;
	}

	public String getSku() {
		return sku;
	}

	public String getMarketplaceSku() {
		return marketplaceSku;
	}

	public String getFnsku() {
		return fnsku;
	}

	public String getAsin() {
		return asin;
	}

	public Boolean getAfn() {
		return afn;
	}

	public Boolean getMfn() {
		return mfn;
	}

	public Boolean getStorage() {
		return storage;
	}
	
}
