package com.kindminds.drs.service.usecase.report;

import com.kindminds.drs.Context;
import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.usecase.ViewSalesAndPageTrafficReportUco;
import com.kindminds.drs.api.v1.model.common.DtoList;
import com.kindminds.drs.api.v1.model.common.Pager;
import com.kindminds.drs.v1.model.impl.report.SalesAndPageTrafficReportSummaryImpl;
import com.kindminds.drs.api.v1.model.report.HistoryLine;
import com.kindminds.drs.api.v1.model.report.SalesAndPageTrafficReport;
import com.kindminds.drs.api.data.access.rdb.CompanyDao;
import com.kindminds.drs.api.data.access.usecase.report.ViewSalesAndPageTrafficReportDao;
import com.kindminds.drs.v1.model.impl.HistoryLineImpl;
import com.kindminds.drs.v1.model.impl.ChartDataImpl;
import com.kindminds.drs.v1.model.impl.SalesAndPageTrafficReportImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;

/*
 * UC059 View Sales And Page Traffic Report
 */
@Service
public class ViewSalesAndPageTrafficReportUcoImpl implements ViewSalesAndPageTrafficReportUco {
	
	@Autowired private ViewSalesAndPageTrafficReportDao dao;
	@Autowired private CompanyDao companyRepo;
	
	private static final int PAGESIZE = 50;
	private static final int AVERAGE = 2;
	private static final String DAY = "1";
	private static final String WEEK = "2";
	private static final String MONTH = "3";
	private static final String SESSIONS = "1";
	private static final String ORDERED_PRODUCT_SALES = "2";
	
	private Date beginDate;
	private Date endDate;
	private Marketplace marketplace;
	private List<String> asins;
	private List<HistoryLine> historyData;
	
	private Date getBeginDate() {
		return beginDate;
	}
	
	private Date getEndDate() {
		return endDate;
	}
	
	private Marketplace getMarketplace() {
		return this.marketplace;
	}
	
	public void setMarketplace(String marketplaceId) {
		if(StringUtils.hasText(marketplaceId)) {
			this.marketplace = Marketplace.fromKey(Integer.valueOf(marketplaceId));
		} else {
			this.marketplace = null;
		}
	}
	
	private List<String> getAsins() {
		return this.asins;
	}

	private void setAsins(String productBase, String productSku) {
		this.asins = generateAsins(productBase, productSku);
	}
	
	private List<String> generateAsins(String productBase, String productSku) {
		if (!StringUtils.hasText(productBase)) {
			return null;
		} else if (!StringUtils.hasText(productSku)) {
			return this.generateAsinsByProduct(productBase);
		} else {
			return this.generateAsinsBySku(productBase, productSku);
		}
	}
	
	private List<String> generateAsinsBySku(String productBase, String productSku) {
		List<String> productSkuList = this.getSelectedSkusList(productBase, productSku);
		return this.dao.queryAsinBySku(getMarketplace().getKey(), productSkuList);
	}
	
	
	private List<String> generateAsinsByProduct(String productBase) {
		List<String> productBaseList = this.getSelectedProductList(productBase);
		return this.dao.queryAsinByProduct(getMarketplace().getKey(), productBaseList);
	}
	
	@Override
	public List<String> getSelectedSkusList(String productBase, String productSku) {
		List<String> skus = new ArrayList<>();
		if(StringUtils.hasText(productSku)) {
			skus.addAll(Arrays.asList(productSku.split(",(?!\\s)"))); // splits only on single comma with no spaces
		} else if (StringUtils.hasText(productBase)) {
			skus.addAll(this.dao.queryProductSkus(productBase));
		}
		return skus;
	}
	
	@Override
	public List<String> getSelectedProductList(String productBase) {
		List<String> products = new ArrayList<>();
		if(StringUtils.hasText(productBase)) {
			products.addAll(Arrays.asList(productBase.split(",(?!\\s)"))); // splits only on single comma with no spaces
		}
		return products;
	}
	
	private List<HistoryLine> getHistoryData() {
		return this.historyData;
	}
	
	private void setHistoryData(List<HistoryLine> historyData) {
		this.historyData = historyData;
	}

	@Override
	public Map<String,String> getSupplierKcodeNames(){
		if(Context.getCurrentUser().isDrsUser()){
			String drsUserCompanyKcode = Context.getCurrentUser().getCompanyKcode();

			Map<String,String> map =
					this.companyRepo.querySupplierKcodeToShortEnUsNameMap(drsUserCompanyKcode);

			map.put("K101","Kindminds Internal Supplier");

			return map;
		}
		return null;
	}
	
	@Override
	public Map<String, String> getProductBases(String supplierKcode){
		if(Context.getCurrentUser().isSupplier()) supplierKcode = Context.getCurrentUser().getCompanyKcode();
		if(!StringUtils.hasText(supplierKcode)) return new TreeMap<String, String>();
		return this.dao.queryProductBaseNames(supplierKcode);
	}
	
	@Override
	public Map<String, String> getProductSkus(String productBase){
		if(!StringUtils.hasText(productBase)) return new TreeMap<String, String>();
		return this.dao.queryProductSkuNames(productBase);
	}

	@Override
	public List<Marketplace> getMarketplaces() {
		return Marketplace.getAmazonMarketplaces();
	}

	@Override
	public List<TimeScale> getTimeScales() {
		return Arrays.asList(TimeScale.values());
	}
	
	@Override
	public List<TrafficType> getTrafficTypes() {
		return Arrays.asList(TrafficType.values());
	}
	
	@Override
	public List<DateRange> getDateRanges() {
		return Arrays.asList(DateRange.values());
	}
	
	private boolean fieldsAreEmpty() {
		return (getMarketplace() == null
				|| getAsins() == null || getAsins().size() == 0
				|| getEndDate() == null || getBeginDate() == null);
	}

	@Override
	public SalesAndPageTrafficReport getReport(String marketplaceId, String productBase,
						String productSku, String dateRange, Date startDate, Date endDate) {
		setMarketplace(marketplaceId);
		if (getMarketplace() == null) return null;
		setAsins(productBase, productSku);
		if (getAsins() == null || getAsins().isEmpty()) return null;
		calculateDateRange(dateRange, startDate, endDate);
		if (fieldsAreEmpty()) return null;
		if (getBeginDate().after(getEndDate())) return null;
		return this.generateReport(getBeginDate(), getEndDate(), getAsins());
	}

	private SalesAndPageTrafficReport generateReport(Date start, Date end, List<String> asins) {
		SalesAndPageTrafficReportImpl report = new SalesAndPageTrafficReportImpl();
		report.setCurrency(getMarketplace().getCurrency());
		report.setStartDate(start);
		report.setEndDate(end);

		Object [] columns = this.dao.querySummary(start, end, getMarketplace().getKey(), asins);
		Integer totalOrderItems = columns[0]==null?null:BigInteger.valueOf(Long.parseLong(columns[0].toString())).intValue();
		BigDecimal totalOrderedProductSales = columns[1]==null?null:(BigDecimal)columns[1];
		Integer totalSessions = columns[2]==null?null:BigInteger.valueOf(Long.parseLong(columns[2].toString())).intValue();
		Integer totalUnitsOrdered = columns[3]==null?null:BigInteger.valueOf(Long.parseLong(columns[3].toString())).intValue();

		report.setSummary( new SalesAndPageTrafficReportSummaryImpl(
				totalOrderItems, totalOrderedProductSales, totalSessions, totalUnitsOrdered));
		return report;
	}
	
	private void calculateDateRange(String dateRange, Date startDate, Date endDate) {
		DateRange range = stringToDateRange(dateRange);
		if (range == DateRange.LAST_WEEK) {
			setDateRange(getStartOfLastWeek(), getEndOfLastWeek());
		} else if (range == DateRange.MONTH_TO_DATE) {
			setDateRange(getUtcFirstDayOfMonth(), getLatestDate());
		} else if (range == DateRange.LAST_MONTH) {
			setDateRange(getFirstOfLastMonth(), getEndOfLastMonth());
		} else if (range == DateRange.YEAR_TO_DATE) {
			setDateRange(getFirstDayOfYear(), getLatestDate());
		} else if (range == DateRange.CUSTOMIZED) {
			setCustomizedDateRange(startDate, endDate);
		} else { // LIFETIME dateRange
			setDateRange(dao.queryStartDate(getMarketplace().getKey(), getAsins()), getLatestDate());
		}
	}
	
	private DateRange stringToDateRange(String dateRange) {
		if (!StringUtils.hasText(dateRange)) {
			return DateRange.LIFETIME;
		} else {
			return DateRange.fromKey(Integer.valueOf(dateRange));
		}		
	}
	
	private void setDateRange(Date start, Date end) {
		this.beginDate = start;
		this.endDate = end;
	}
	
	private void setCustomizedDateRange(Date startDate, Date endDate) {
		Date start = (startDate == null) ? dao.queryStartDate(getMarketplace().getKey(), getAsins()) : addDateOffset(startDate);
		Date end = (endDate == null) ? getLatestDate() : addDateOffset(endDate);
		setDateRange(start, end);
	}
	
	private Date addDateOffset(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MILLISECOND,c.getTimeZone().getRawOffset());
		return c.getTime();
	}
	
	private Calendar getTodayMinusOffset() {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.MILLISECOND,-now.getTimeZone().getRawOffset());
		return now;
	}
	
	private Date getDateWithUtcOffset(Calendar c) {
		this.setCalendarTimePartZero(c);
		c.add(Calendar.MILLISECOND,c.getTimeZone().getRawOffset());
		return c.getTime();
	}
	
	private Date getLatestDate(){
		Calendar now = getTodayMinusOffset();
		now.add(Calendar.DATE, -2);
		return this.getDateWithUtcOffset(now);
	}
	
	private Date getStartOfLastWeek() {
		Calendar now = getTodayMinusOffset();
		now.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		now.add(Calendar.WEEK_OF_YEAR, -1);
		return this.getDateWithUtcOffset(now);
	}
	
	private Date getEndOfLastWeek() {
		Calendar now = getTodayMinusOffset();
		now.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		now.add(Calendar.WEEK_OF_YEAR, -1);
		return this.getDateWithUtcOffset(now);
	}
	
	private Date getUtcFirstDayOfMonth(){
		Calendar now = getTodayMinusOffset();
		now.set(Calendar.DAY_OF_MONTH,1);
		return this.getDateWithUtcOffset(now);
	}
	
	private Date getFirstOfLastMonth() {
		Calendar now = getTodayMinusOffset();
		now.set(Calendar.DAY_OF_MONTH,1);
		now.add(Calendar.MONTH, -1);
		return this.getDateWithUtcOffset(now);
	}
	
	private Date getEndOfLastMonth() {
		Calendar now = getTodayMinusOffset();
		now.set(Calendar.DAY_OF_MONTH, 1);
		now.add(Calendar.DAY_OF_MONTH, -1);
		return this.getDateWithUtcOffset(now);
	}
	
	private Date getFirstDayOfYear() {
		Calendar now = getTodayMinusOffset();
		now.set(Calendar.MONTH, 0);
		now.set(Calendar.DAY_OF_MONTH, 1);
		return this.getDateWithUtcOffset(now);
	}
	
	private void setCalendarTimePartZero(Calendar c){
		c.set(Calendar.HOUR_OF_DAY,0);
		c.set(Calendar.MINUTE,0);
		c.set(Calendar.SECOND,0);
		c.set(Calendar.MILLISECOND,0);
	}
	
	
	@Override
	public List<HistoryLine> exportSalesHistory(){
		if (fieldsAreEmpty()) return null;
		return this.getHistoryData();
	}
	
	@Override
	public DtoList<HistoryLine> getSalesHistory (int pageIndex, String timeScale, int totalOrAverage){
		List<ChartData> chartPoints = createChartDataPoints(
				timeScale, getBeginDate(), getEndDate());
		this.setHistoryData(generateSalesHistory(chartPoints, timeScale, totalOrAverage));
		return getPageHistory(pageIndex);
	}
	
	private DtoList<HistoryLine> getPageHistory(int pageIndex) {
		int totalRecordCount = getHistoryData().size();
		int fromIndex = PAGESIZE * (pageIndex - 1);
		int toIndex = PAGESIZE * pageIndex;
		if (fromIndex < 0) fromIndex = 0;
		if (toIndex > totalRecordCount) toIndex = totalRecordCount;
		DtoList<HistoryLine> list = new DtoList<>();
		list.setTotalRecords(totalRecordCount);
		list.setPager(new Pager(pageIndex, totalRecordCount, PAGESIZE));
		list.setItems(getHistoryData().subList(fromIndex, toIndex));
		return list;
	}
	
	private List<ChartData> createChartDataPoints(String timeScale, Date startDate, Date endDate){
		Calendar currentDateCalendar = Calendar.getInstance();
		Calendar endDateCalendar = Calendar.getInstance();
		currentDateCalendar.setTime(startDate);
		endDateCalendar.setTime(endDate);
		int calendarUnit = TimeScale.fromKey(Integer.valueOf(timeScale)).getCalendarUnit();
		List<ChartData> chartDatas = new ArrayList<>();
		if (WEEK.equals(timeScale)) {
			chartDatas.add(new ChartDataImpl(currentDateCalendar.getTime()));
			currentDateCalendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			currentDateCalendar.add(Calendar.WEEK_OF_YEAR, 1);
		}
		if (MONTH.equals(timeScale)) {
			chartDatas.add(new ChartDataImpl(currentDateCalendar.getTime()));
			currentDateCalendar.set(Calendar.DAY_OF_MONTH, 1);
			currentDateCalendar.add(Calendar.MONTH, 1);
		}
		while(currentDateCalendar.before(endDateCalendar)){
			chartDatas.add(new ChartDataImpl(currentDateCalendar.getTime()));
			currentDateCalendar.add(calendarUnit, 1);
		}
		return chartDatas;
	}
	
	private List<HistoryLine> generateSalesHistory (List<ChartData> chartPoints, String timeScale, int totalOrAverage) {
		List<Object []> columnsList = dao.queryHistoryLines(getBeginDate(), getEndDate(),
				getMarketplace().getKey(), getAsins());

		List<HistoryLine> salesHistory = new ArrayList<>();
		for(Object[] columns:columnsList){
			Date date = (Date)columns[0];
			BigDecimal sessions = columns[1]==null? BigDecimal.ZERO : new BigDecimal((Long) columns[1]);
			BigDecimal pageViews = columns[2]==null?BigDecimal.ZERO : new BigDecimal((Long) columns[2]);
			BigDecimal sessionsMultiplyBuyBoxPercentage = columns[3]==null? BigDecimal.ZERO : (BigDecimal)columns[3];
			BigDecimal unitsOrdered = columns[4]==null?BigDecimal.ZERO : new BigDecimal((Long) columns[4]);
			BigDecimal orderedProductSales = columns[5]==null? BigDecimal.ZERO : (BigDecimal)columns[5];
			BigDecimal totalOrderItems = columns[6]==null?BigDecimal.ZERO : new BigDecimal((Long) columns[6]);
			salesHistory.add(new HistoryLineImpl(date, sessions, pageViews, sessionsMultiplyBuyBoxPercentage, unitsOrdered, orderedProductSales, totalOrderItems));
		}


		if (DAY.equals(timeScale)) {
			return salesHistory;
		}
		List<HistoryLine> sumSalesHistory = new ArrayList<HistoryLine>();
		for(int i = chartPoints.size() - 1; i >= 0; i--) {	// Starts from the latest chart data point
			int daysOfInterval = 0;
			HistoryLine intervalSum = new HistoryLineImpl();
			Date currentChartPoint = chartPoints.get(i).getDateObj();
			intervalSum.setDate(currentChartPoint);
			for (int historyIndex = salesHistory.size() - 1; historyIndex >= 0; historyIndex--) {
				HistoryLine salesRecord = salesHistory.get(historyIndex);
			    if (salesRecord.getDateObj().compareTo(currentChartPoint) < 0) break;
			    addToTotal(intervalSum, salesRecord);
		    	daysOfInterval++;
		    	salesHistory.remove(historyIndex);
			}
			if (totalOrAverage == AVERAGE && daysOfInterval != 0) {
				calculateAverage(intervalSum, daysOfInterval);
			}
			sumSalesHistory.add(0, intervalSum);
		}
		return sumSalesHistory;
	}
	
	private void addToTotal(HistoryLine intervalSum, HistoryLine salesRecord) {
	    intervalSum.setSessions(intervalSum.getSessionsDec()
	    				.add(salesRecord.getSessionsDec()));
	    intervalSum.setPageViews(intervalSum.getPageViewsDec()
	    				.add(salesRecord.getPageViewsDec()));
	    intervalSum.setSessionsMultiplyBuyboxPercentage(intervalSum.getSessionsXBuyBoxPercentDec()
	    				.add(salesRecord.getSessionsXBuyBoxPercentDec()));
	    intervalSum.setUnitsOrdered(intervalSum.getUnitsOrderedDec()
	    				.add(salesRecord.getUnitsOrderedDec()));
	    intervalSum.setOrderedProductSales(intervalSum.getOrderedProductSalesDec()
	    				.add(salesRecord.getOrderedProductSalesDec()));
	    intervalSum.setTotalOrderItems(intervalSum.getTotalOrderItemsDec()
	    				.add(salesRecord.getTotalOrderItemsDec()));
	}
	
	private void calculateAverage(HistoryLine intervalSum, int daysOfInterval) {
		intervalSum.setSessions(intervalSum.getSessionsDec()
						.divide(new BigDecimal(daysOfInterval), 2, RoundingMode.HALF_UP));
	    intervalSum.setPageViews(intervalSum.getPageViewsDec()
	    				.divide(new BigDecimal(daysOfInterval), 2, RoundingMode.HALF_UP));
	    intervalSum.setSessionsMultiplyBuyboxPercentage(intervalSum.getSessionsXBuyBoxPercentDec()
	    				.divide(new BigDecimal(daysOfInterval), 2, RoundingMode.HALF_UP));
	    intervalSum.setUnitsOrdered(intervalSum.getUnitsOrderedDec()
	    				.divide(new BigDecimal(daysOfInterval), 2, RoundingMode.HALF_UP));
	    intervalSum.setOrderedProductSales(intervalSum.getOrderedProductSalesDec()
	    				.divide(new BigDecimal(daysOfInterval), 2, RoundingMode.HALF_UP));
	    intervalSum.setTotalOrderItems(intervalSum.getTotalOrderItemsDec()
	    				.divide(new BigDecimal(daysOfInterval), 2, RoundingMode.HALF_UP));
	}
	
	@Override
	public List<ChartData> getChartDatas(String timeScaleId, String trafficTypeId) {
		List<ChartData> chartRecords = new ArrayList<ChartData>();
		for (HistoryLine record : getHistoryData()) {
			Date date = record.getDateObj();
			BigDecimal amount = record.getUnitsOrderedDec();
			if (SESSIONS.equals(trafficTypeId)) {
				amount = record.getSessionsDec();
			} else if (ORDERED_PRODUCT_SALES.equals(trafficTypeId)) {
				amount = record.getOrderedProductSalesDec();
			}
			chartRecords.add(new ChartDataImpl(date, amount));
		}
		return chartRecords;
	}
	
}
