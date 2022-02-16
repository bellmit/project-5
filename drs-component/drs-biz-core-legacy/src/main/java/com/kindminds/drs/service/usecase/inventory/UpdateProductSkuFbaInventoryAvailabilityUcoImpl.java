package com.kindminds.drs.service.usecase.inventory;

import com.kindminds.drs.Warehouse;
import com.kindminds.drs.api.usecase.inventory.UpdateProductSkuFbaInventoryAvailabilityUco;
import com.kindminds.drs.api.v1.model.product.ProductSkuFbaInventoryAvailabilityLine;
import com.kindminds.drs.api.data.access.usecase.inventory.UpdateProductSkuFbaInventoryAvailabilityDao;
import com.kindminds.drs.v1.model.impl.ProductSkuFbaInventoryAvailabilityLineImpl;
import com.kindminds.drs.util.DateHelper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.Reader;
import java.io.StringReader;
import java.util.*;

@Service
public class UpdateProductSkuFbaInventoryAvailabilityUcoImpl implements UpdateProductSkuFbaInventoryAvailabilityUco {
	
	@Autowired private UpdateProductSkuFbaInventoryAvailabilityDao dao;
	
	enum Column{
		SKU("SKU"),
		WAREHOUSE("Warehouse"),
		DATE("Date"),
		INVENTORY_AVAILABLE("Inventory Available");
		private String columnName;
		private Column(String columnName) { this.columnName = columnName; }
		public String getName(){ return this.columnName; } 
	}

	@Override
	public String uploadFileAndUpdate(String utcDate, byte[] fileBytes) {
		if(!StringUtils.hasText(utcDate)) return "Please select date.";
		Date date = DateHelper.toDate(utcDate+" UTC", "yyyy-MM-dd z");
		if(this.hasBeenUpdated(date)) return "Data of "+utcDate+", has already been updated, select another date please. "; 
		try {
			List<ProductSkuFbaInventoryAvailabilityLine> lines = this.createReportRawLines(utcDate,fileBytes);
			int updatedRows = this.dao.updateProductSkuInventoryHistoryAvailability(date,lines);
			updatedRows += this.dao.updateProductSkuInventoryHistoryAvailabilityWithNullValue(date,false);
			return updatedRows+" record(s) have been successfully updated. ";
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	private boolean hasBeenUpdated(Date date){
		return !this.dao.selectIfNullDataExist(date);
	}
	
	private List<ProductSkuFbaInventoryAvailabilityLine> createReportRawLines(String utcDateStr,byte[] fileBytes) throws Exception{
		Reader reader = new StringReader(new String(fileBytes));
		CSVParser parser = CSVFormat.TDF.withHeader().parse(reader);
		this.checkHeader(parser.getHeaderMap());
		Iterable<CSVRecord> records = parser.getRecords();
		List<ProductSkuFbaInventoryAvailabilityLine> lines = new ArrayList<ProductSkuFbaInventoryAvailabilityLine>();
		for(CSVRecord record:records){
			String sku = record.get(Column.SKU.getName());
			Warehouse warehouse = Warehouse.fromDisplayName(record.get(Column.WAREHOUSE.getName()));
			String dateStr = record.get(Column.DATE.getName());
			if(!dateStr.equals(utcDateStr)) throw new Exception("Date mismatch, "+utcDateStr+" and "+dateStr+".");
			Boolean isAvailable = Boolean.valueOf(record.get(Column.INVENTORY_AVAILABLE.getName()));
			lines.add(new ProductSkuFbaInventoryAvailabilityLineImpl(sku,warehouse.getKey(),isAvailable));
		}
		return lines;
	}
	
	private void checkHeader(Map<String,Integer> headerMap) throws Exception{
		for(Column column: Column.values()){
			if(!headerMap.containsKey(column.getName())) throw new Exception("Can't find header column, \""+column.getName()+"\". Please check header in file.");
		}
	}
	
	@Override
	public List<String> getYears() {
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		int length = 3;
		List<String> years = new ArrayList<>();
		for(int i=0;i<length;i++) years.add(String.valueOf(currentYear-i));
		return years;
	}

	@Override
	public List<String> getMonths() {
		return Arrays.asList("01","02","03","04","05","06","07","08","09","10","11","12");
	}

	@Override
	public String getDefaultYear() {
		return String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
	}

	@Override
	public String getDefaultMonth() {
		int zeroBasedMonth = Calendar.getInstance().get(Calendar.MONTH);
		return String.format("%02d",zeroBasedMonth+1);
	}
		
	@Override
	public Map<String,Boolean> getUpdateStatus(String yearStr, String monthStr) {
		int year = Integer.valueOf(yearStr);
		int month = Integer.valueOf(monthStr);
		Map<String,Boolean> status = this.createStatus(year,month,false);
		Date start = this.getUtcStartPointOfYearMonth(year,month);
		Date end = this.getUtcEndPointOfYearMonth(year,month);
		List<Date> datesWithAvailabilityValueNotNull = this.dao.queryDatesWithAvailabilityNotNull(start, end);
		for(Date date:datesWithAvailabilityValueNotNull){
			String utcDateStr = DateHelper.toString(date,"yyyy-MM-dd", "UTC");
			status.put(utcDateStr,true);
		}
		List<Date> datesWithAvailabilityValueNull = this.dao.queryDatesWithAvailabilityNull(start, end);
		for(Date date:datesWithAvailabilityValueNull){
			String utcDateStr = DateHelper.toString(date,"yyyy-MM-dd", "UTC");
			status.put(utcDateStr,false);
		}
		return status;
	}
	
	private Map<String,Boolean> createStatus(int year,int month,Boolean defaultValue){
		Map<String,Boolean> statusMap = new LinkedHashMap<>();
		int zeroBasedMonth=month-1;
		Calendar calendar = new GregorianCalendar(year,zeroBasedMonth,1,0,0,0);
		while(calendar.get(Calendar.MONTH)==zeroBasedMonth){
			String dateKey = year+"-"+String.format("%02d",month)+"-"+String.format("%02d",calendar.get(Calendar.DAY_OF_MONTH));
			statusMap.put(dateKey,defaultValue);
			calendar.add(Calendar.DAY_OF_MONTH,1);
		}
		return statusMap;
	}
	
	private Date getUtcStartPointOfYearMonth(int year,int month){
		return DateHelper.toDate(year+"-"+month+" UTC","yyyy-MM z");
	}
	
	private Date getUtcEndPointOfYearMonth(int year,int month){
		int zeroBasedMonth=month-1;
		Calendar calendar = new GregorianCalendar(year,zeroBasedMonth,1,0,0,0);
		calendar.add(Calendar.MONTH, 1);
		return DateHelper.toDate(calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+" UTC","yyyy-MM z");
	}

	@Override
	public String clearAvailabilityData(String utcDateStr) {
		Date date = DateHelper.toDate(utcDateStr+" UTC","yyyy-MM-dd z");
		int clearedRecordCounts = this.dao.updateProductSkuInventoryHistoryAvailabilityAsNull(date);
		return clearedRecordCounts+" record(s) have been successfully cleared. ";
	}

}
