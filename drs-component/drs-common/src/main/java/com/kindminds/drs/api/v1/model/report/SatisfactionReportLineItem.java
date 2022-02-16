package com.kindminds.drs.api.v1.model.report;

import java.math.BigDecimal;

public interface SatisfactionReportLineItem {
	
	String getProductSku();
	String getProductName();
	DashboardStatistics getLastOnePeriodData();
	DashboardStatistics getLastTwoPeriodData();
	DashboardStatistics getLastSixPeriodData();
	
	public interface DashboardStatistics {
		String getPercentage();
		String getOrderQuantity();
		String getReturnQuantity();
		String getStatisticsDescription();
	}
	
	public enum Statistics {
		Good(1, 0.92), Okay(0.92, 0.85), Bad(0.85, 0.8), Unacceptable(0.8, 0);
		private BigDecimal upperLevel;
		private BigDecimal lowerLevel;
		
		Statistics(double upperLevel, double lowerLevel) {
			this.upperLevel = BigDecimal.valueOf(upperLevel);
			this.lowerLevel = BigDecimal.valueOf(lowerLevel);
		}
		
		public static Statistics getStatistics(BigDecimal returnRate) {
			for(Statistics s: Statistics.values()){
				if ((returnRate.compareTo(s.lowerLevel)>=0)&&(returnRate.compareTo(s.upperLevel) <= 0))
					return s;
			}
			return null;
		}
	}
}
