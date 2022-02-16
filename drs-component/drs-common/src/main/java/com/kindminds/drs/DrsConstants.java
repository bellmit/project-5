package com.kindminds.drs;

import java.math.BigDecimal;

public class DrsConstants {
	public static final BigDecimal interbankRateForMs2ssSettlement = new BigDecimal("0.01");
	public static final BigDecimal interbankRateForSs2spSettlement = new BigDecimal("0.01");
	public static final BigDecimal interbankRateForCalculatingDrsRetainmentRate = BigDecimal.ZERO;
	public static final BigDecimal interbankRateForCalculatingCustomerCaseMsdcCharge = BigDecimal.ZERO;
	public static final BigDecimal interbankRateToUsdForActualMs2ssUnitInventoryPayment = BigDecimal.ZERO;
}
