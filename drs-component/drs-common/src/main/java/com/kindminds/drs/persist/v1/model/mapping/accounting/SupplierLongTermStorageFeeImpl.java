package com.kindminds.drs.persist.v1.model.mapping.accounting;

import java.math.BigDecimal;





import com.kindminds.drs.api.v1.model.accounting.SupplierLongTermStorageFee;


public class SupplierLongTermStorageFeeImpl implements SupplierLongTermStorageFee {
	
	//@Id
	//@Column(name="k_code")
	private String kcode;
	
	//@Column(name="code")
	private String marketplace;
	
	//@Column(name="projected_ltsf_6_mo")
	private BigDecimal sixMonthStorageFee;
	
	//@Column(name="projected_ltsf_12_mo")
	private BigDecimal oneYearStorageFee;
	
	//@Column(name="currency")
	private String currency;
	
	public SupplierLongTermStorageFeeImpl() {}
	
	public SupplierLongTermStorageFeeImpl(String kcode, String marketplace, 
					BigDecimal sixMonthStorageFee, BigDecimal oneYearStorageFee, String currency) {
		this.kcode = kcode;
		this.marketplace = marketplace;
		this.sixMonthStorageFee = sixMonthStorageFee;
		this.oneYearStorageFee = oneYearStorageFee;
		this.currency = currency;
	}

	@Override
	public String getKcode() {
		return kcode;
	}
	
	@Override
	public String getMarketplace() {
		return marketplace;
	}

	@Override
	public BigDecimal getSixMonthStorageFee() {
		return sixMonthStorageFee;
	}

	@Override
	public BigDecimal getOneYearStorageFee() {
		return oneYearStorageFee;
	}

	@Override
	public String getCurrency() {
		return currency;
	}
	
}
