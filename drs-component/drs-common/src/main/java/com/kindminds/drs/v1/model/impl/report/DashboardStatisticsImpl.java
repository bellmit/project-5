package com.kindminds.drs.v1.model.impl.report;

import com.kindminds.drs.util.BigDecimalHelper;
import com.kindminds.drs.api.v1.model.report.SatisfactionReportLineItem;
import com.kindminds.drs.api.v1.model.report.SatisfactionReportLineItem.DashboardStatistics;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DashboardStatisticsImpl implements DashboardStatistics{

	private int orderQuantity;
	private int returnQuantity;

	private BigDecimal percentage;

	public DashboardStatisticsImpl(){}

	public DashboardStatisticsImpl(DashboardStatistics proxy) {
		this.orderQuantity = Integer.valueOf(proxy.getOrderQuantity());
		this.returnQuantity = Integer.valueOf(proxy.getReturnQuantity());

		if (BigDecimal.valueOf(orderQuantity).compareTo(BigDecimal.ZERO) == 0)
			this.percentage = BigDecimal.ONE.negate();
		else
			this.percentage = BigDecimal.ONE.subtract(
					BigDecimal.valueOf(returnQuantity).divide(BigDecimal.valueOf(orderQuantity), 2, RoundingMode.HALF_UP));
	}

	@Override
	public String getPercentage() {
		if (percentage.signum() == -1)
			return "N/A";
		return BigDecimalHelper.toPercentageString(percentage,2);
	}



	@Override
	public String getStatisticsDescription() {
		if (percentage.signum() == -1)
			return "N/A";
		return SatisfactionReportLineItem.Statistics.getStatistics(this.percentage).toString();
	}


	@Override
	public String getOrderQuantity() {
		return Integer.toString(this.orderQuantity);
	}

	@Override
	public String getReturnQuantity() {
		return Integer.toString(this.returnQuantity);
	}




	public void setOrderQuantity(int orderQuantity) {
		this.orderQuantity = orderQuantity;
	}

	public void setReturnQuantity(int returnQuantity) {
		this.returnQuantity = returnQuantity;
	}


}
