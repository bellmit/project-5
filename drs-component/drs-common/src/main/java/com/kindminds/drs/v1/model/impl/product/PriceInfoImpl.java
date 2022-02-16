package com.kindminds.drs.v1.model.impl.product;

import com.kindminds.drs.api.data.access.usecase.logistics.ViewUnsRecognizeRevenueReportDao.PriceInfo;

import java.math.BigDecimal;

public class PriceInfoImpl implements PriceInfo {
	
	private BigDecimal ddpPrice;
	private BigDecimal cifPrice;

	public PriceInfoImpl(BigDecimal ddpPrice, BigDecimal cifPrice) {
		this.ddpPrice = ddpPrice;
		this.cifPrice = cifPrice;
	}

	@Override
	public BigDecimal getDdpPrice() {
		return ddpPrice;
	}

	@Override
	public BigDecimal getCifPrice() {
		return cifPrice;
	}

}
