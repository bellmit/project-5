package com.kindminds.drs.api.usecase;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kindminds.drs.api.v1.model.report.HistoryLine;
import org.springframework.util.Assert;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.v1.model.report.SalesAndPageTrafficReport;
import com.kindminds.drs.api.v1.model.common.DtoList;

public interface ViewSalesAndPageTrafficReportUco {
	
	public enum TimeScale{
		DAY(1,"Day",Calendar.DATE),
		WEEK(2,"Week",Calendar.WEEK_OF_MONTH),
		MONTH(3,"Month",Calendar.MONTH);
		private int key;
		private String displayName;
		private int calendarUnit;
		static private final Map<Integer,TimeScale> keyToSelfMap = new HashMap<>();
		static {for(TimeScale ts:TimeScale.values()) keyToSelfMap.put(ts.getKey(),ts);}
		TimeScale(int key,String displayName,int calendarUnit) {
			this.key=key;
			this.displayName=displayName;
			this.calendarUnit=calendarUnit;
		}
		public Integer getKey(){return this.key;}
		public static String getDisplayName(String key) {
			TimeScale timeScale = keyToSelfMap.get(Integer.valueOf(key));
			return timeScale.displayName;
		}
		public int getCalendarUnit() {return this.calendarUnit;}
		public static TimeScale fromKey(int value){
			TimeScale timeScale = keyToSelfMap.get(value);
			Assert.notNull(timeScale);
			return timeScale;
		}
	}
	
	public enum TrafficType{
		SESSIONS(1,"Sessions"),
		ORDERED_PRODUCT_SALES(2,"Ordered product sales"),
		TOTAL_UNITS_ORDERED(3,"Total units ordered");
		private int key;
		private String displayName;
		static private final Map<Integer,TrafficType> keyToSelfMap = new HashMap<>();
		static {for(TrafficType ds:TrafficType.values()) keyToSelfMap.put(ds.getKey(),ds);}
		TrafficType(int key,String displayName) {this.key=key;this.displayName=displayName;}
		public Integer getKey(){return this.key;}
		public String getDisplayName() {return this.displayName;}
		public static TrafficType fromKey(int value){
			TrafficType trafficType = keyToSelfMap.get(value);
			Assert.notNull(trafficType);
			return trafficType;
		}
	}
	
	public enum DateRange{
		LAST_WEEK(1,"Last Week"),
		MONTH_TO_DATE(2,"Month to date"),
		LAST_MONTH(3,"Last month"),
		YEAR_TO_DATE(4,"Year to date"),
		LIFETIME(5,"Lifetime"),
		CUSTOMIZED(6,"Customized");
		private int key;
		private String displayName;
		static private final Map<Integer,DateRange> keyToSelfMap = new HashMap<>();
		static {for(DateRange dr:DateRange.values()) keyToSelfMap.put(dr.getKey(),dr);}
		DateRange(int key,String displayName) {
			this.key=key;
			this.displayName=displayName;
		}
		public Integer getKey(){return this.key;}
		public String getDisplayName() {return this.displayName;}
		public static DateRange fromKey(int value){
			DateRange dateRange = keyToSelfMap.get(value);
			Assert.notNull(dateRange);
			return dateRange;
		}
	}
	

	
	public interface ChartData{
		String getDate();
		String getTotal();
		Date getDateObj();
		BigDecimal getTotalBigDec();
	}
	
	Map<String,String> getSupplierKcodeNames();
	Map<String, String> getProductBases(String supplierKcode);
	Map<String, String> getProductSkus(String productBase);
	List<Marketplace> getMarketplaces();
	List<TimeScale> getTimeScales();
	List<TrafficType> getTrafficTypes();
	List<DateRange> getDateRanges();
	List<String> getSelectedProductList(String productBase);
	List<String> getSelectedSkusList(String productBase, String productSku);
	SalesAndPageTrafficReport getReport(String marketplaceId, String productBase,
			String productSku, String dateRange, Date startDate, Date endDate);
	List<ChartData> getChartDatas(String timeScaleId,String trafficTypeId);
	DtoList<HistoryLine> getSalesHistory(int pageIndex, String timeScale, int totalOrAverage);
	List<HistoryLine> exportSalesHistory();
}
