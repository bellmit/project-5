package com.kindminds.drs.v1.model.impl.stock;

import java.math.BigDecimal;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v2.biz.domain.model.product.SkuShipmentAllocationInfo;


public class IvsInfoImpl implements SkuShipmentAllocationInfo.IvsInfo {
	
	private String name;
	private int currencyKey;
	private BigDecimal fcaPrice;
	private BigDecimal salesTaxRate;

	public IvsInfoImpl(String name, int currencyKey, BigDecimal fcaPrice, BigDecimal salesTaxRate) {
		super();
		this.name = name;
		this.currencyKey = currencyKey;
		this.fcaPrice = fcaPrice;
		this.salesTaxRate = salesTaxRate;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Currency getFcaCurrency() {
		return Currency.fromKey(this.currencyKey);
	}

	@Override
	public BigDecimal getFcaPrice() {
		return this.fcaPrice;
	}

	@Override
	public BigDecimal getSalesTaxRate() {
		return this.salesTaxRate;
	}

}
