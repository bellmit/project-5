package com.kindminds.drs.service.usecase.report.amazon;

import com.kindminds.drs.Warehouse;
import com.kindminds.drs.api.usecase.report.amazon.ImportAmazonMonthlyStorageFeeReportUco;
import com.kindminds.drs.api.v1.model.accounting.AmazonMonthlyStorageFeeReportRawLine;
import com.kindminds.drs.api.data.access.usecase.report.amazon.ImportAmazonMonthlyStorageFeeReportDao;
import com.kindminds.drs.v1.model.impl.AmazonMonthlyStorageFeeReportRawLineImpl;
import com.kindminds.drs.util.DateHelper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class ImportAmazonMonthlyStorageFeeReportUcoImpl implements ImportAmazonMonthlyStorageFeeReportUco{

	@Autowired private ImportAmazonMonthlyStorageFeeReportDao dao;	
	private String dateFormat = "yyyy-MM";


//	asin	fnsku	product-name
//	fulfillment-center	country-code	longest-side
//	median-side	shortest-side	measurement-units
//	weight	weight-units	item-volume	volume-units
//	average-quantity-on-hand	average-quantity-pending-removal
//	estimated-total-item-volume
//	month-of-charge	storage-rate	currency	estimated-monthly-storage-fee

	public enum AmazonMonthlyStorageFeeReportColumnUS{
		ASIN(0),
		FNSKU(1),
		PRODUCT_NAME(2),
		FULFILLMENT_CENTER(3),
		COUNTRY_CODE(4),
		LONGEST_SIDE(5),
		MEDIAN_SIDE(6),
		SHORTEST_SIDE(7),
		MEASUREMENT_UNITS(8),
		WEIGHT(9),
		WEIGHT_UNITS(10),
		ITEM_VOLUME(11),
		VOLUME_UNITS(12),
		PRODUCT_SIZE_TIER(13),
		AVERAGE_QUANTITY_ON_HAND(14),
		AVERAGE_QUANTITY_PENDING_REMOVAL(15),
		ESTIMATED_TOTAL_ITEM_VOLUME(16),
		MONTH_OF_CHARGE(17),
		STORAGE_RATE(18),
		CURRENCY(19),
		ESTIMATED_MONTHLY_STORAGE_FEE(20);
		//DANGEROUS_GOODS_STORAGE_TYPE(20);
		private int index;
		AmazonMonthlyStorageFeeReportColumnUS(int index){this.index=index;}
		public int getIndex(){return this.index;}		
	}

	public enum AmazonMonthlyStorageFeeReportColumnCA{
		ASIN(0),
		FNSKU(1),
		PRODUCT_NAME(2),
		FULFILLMENT_CENTER(3),
		COUNTRY_CODE(4),
		LONGEST_SIDE(5),
		MEDIAN_SIDE(6),
		SHORTEST_SIDE(7),
		MEASUREMENT_UNITS(8),
		WEIGHT(9),
		WEIGHT_UNITS(10),
		ITEM_VOLUME(11),
		VOLUME_UNITS(12),
		AVERAGE_QUANTITY_ON_HAND(13),
		AVERAGE_QUANTITY_PENDING_REMOVAL(14),
		ESTIMATED_TOTAL_ITEM_VOLUME(15),
		MONTH_OF_CHARGE(16),
		STORAGE_RATE(17),
		CURRENCY(18),
		ESTIMATED_MONTHLY_STORAGE_FEE(19);
		private int index;
		AmazonMonthlyStorageFeeReportColumnCA(int index){this.index=index;}
		public int getIndex(){return this.index;}
	}

	public enum AmazonMonthlyStorageFeeReportColumnUK{
		ASIN(0),
		FNSKU(1),
		PRODUCT_NAME(2),
		FULFILLMENT_CENTER(3),
		COUNTRY_CODE(4),
		LONGEST_SIDE(5),
		MEDIAN_SIDE(6),
		SHORTEST_SIDE(7),
		MEASUREMENT_UNITS(8),
		WEIGHT(9),
		WEIGHT_UNITS(10),
		ITEM_VOLUME(11),
		VOLUME_UNITS(12),
		PRODUCT_SIZE_TIER(13),
		AVERAGE_QUANTITY_ON_HAND(14),
		AVERAGE_QUANTITY_PENDING_REMOVAL(15),
		ESTIMATED_TOTAL_ITEM_VOLUME(16),
		MONTH_OF_CHARGE(17),
		STORAGE_RATE(18),
		CURRENCY(19),
		ESTIMATED_MONTHLY_STORAGE_FEE(20),
		CATEGORY(21);
		private int index;
		AmazonMonthlyStorageFeeReportColumnUK(int index){this.index=index;}
		public int getIndex(){return this.index;}
	}
	
	@Override
	public List<Warehouse> getWarehouses() {
		List<Warehouse> warehouses = new ArrayList<>();
		warehouses.add(Warehouse.FBA_US);
		warehouses.add(Warehouse.FBA_CA);
		warehouses.add(Warehouse.FBA_UK);
		warehouses.add(Warehouse.FBA_DE);
		warehouses.add(Warehouse.FBA_FR);
		return warehouses;
	}
		
	@Override
	public String importFile(String warehouseId, byte[] bytes) {
		String message = "";
		Warehouse warehouse = Warehouse.fromKey(Integer.parseInt(warehouseId));						
		String monthOfCharge = this.getMonthOfChargeByReport(warehouse, bytes);	
		if(isDenyToImportByReportDateAfterCurrentDate(warehouse, monthOfCharge)) return "disable imported";
		if(isImported(warehouse, monthOfCharge)) return "duplicated imported";		
			try {
				//TODO: remove soon
				//List<AmazonMonthlyStorageFeeReportRawLine> rawLineList = this.createReportRawLines(warehouse,bytes);
				List<AmazonMonthlyStorageFeeReportRawLine> rawLineList = this.createReportRawLines(warehouse,bytes);
				int insertedRows = this.dao.insertRawLines(warehouse, rawLineList);
				message =  insertedRows + " new Line(s) inserted";										
			} catch (Exception e) {
				message = e.getMessage();
				e.printStackTrace();
			}		
		return message;
	}
	
	@SuppressWarnings({ "resource" })
	private List<AmazonMonthlyStorageFeeReportRawLine> createReportRawLines(Warehouse warehouse, byte[] fileBytes) throws IOException, ParseException{	
		Reader reader = new StringReader(new String(fileBytes));
		CSVParser parser = new CSVParser(reader, CSVFormat.TDF);
		List<CSVRecord> records = parser.getRecords();
		List<AmazonMonthlyStorageFeeReportRawLine> lines = new ArrayList<AmazonMonthlyStorageFeeReportRawLine>();
		for(int i=0; i<records.size();i++){
			if(i==0){ continue;
			}else{
				this.checkBigDecimalFormat(warehouse,records.get(i));
			 if (warehouse == Warehouse.FBA_CA){
					lines.add(new AmazonMonthlyStorageFeeReportRawLineImpl(
							records.get(i).get(AmazonMonthlyStorageFeeReportColumnCA.ASIN.getIndex()),
							records.get(i).get(AmazonMonthlyStorageFeeReportColumnCA.FNSKU.getIndex()),
							records.get(i).get(AmazonMonthlyStorageFeeReportColumnCA.PRODUCT_NAME.getIndex()),
							records.get(i).get(AmazonMonthlyStorageFeeReportColumnCA.FULFILLMENT_CENTER.getIndex()),
							records.get(i).get(AmazonMonthlyStorageFeeReportColumnCA.COUNTRY_CODE.getIndex()),
							new BigDecimal(records.get(i).get(AmazonMonthlyStorageFeeReportColumnCA.LONGEST_SIDE.getIndex())),
							new BigDecimal(records.get(i).get(AmazonMonthlyStorageFeeReportColumnCA.MEDIAN_SIDE.getIndex())),
							new BigDecimal(records.get(i).get(AmazonMonthlyStorageFeeReportColumnCA.SHORTEST_SIDE.getIndex())),
							records.get(i).get(AmazonMonthlyStorageFeeReportColumnCA.MEASUREMENT_UNITS.getIndex()),
							new BigDecimal(records.get(i).get(AmazonMonthlyStorageFeeReportColumnCA.WEIGHT.getIndex())),
							records.get(i).get(AmazonMonthlyStorageFeeReportColumnCA.WEIGHT_UNITS.getIndex()),
							new BigDecimal(records.get(i).get(AmazonMonthlyStorageFeeReportColumnCA.ITEM_VOLUME.getIndex())),
							records.get(i).get(AmazonMonthlyStorageFeeReportColumnCA.VOLUME_UNITS.getIndex()),
							null,
							new BigDecimal(records.get(i).get(AmazonMonthlyStorageFeeReportColumnCA.AVERAGE_QUANTITY_ON_HAND.getIndex())),
							new BigDecimal(records.get(i).get(AmazonMonthlyStorageFeeReportColumnCA.AVERAGE_QUANTITY_PENDING_REMOVAL.getIndex())),
							new BigDecimal(records.get(i).get(AmazonMonthlyStorageFeeReportColumnCA.ESTIMATED_TOTAL_ITEM_VOLUME.getIndex())),
							records.get(i).get(AmazonMonthlyStorageFeeReportColumnCA.MONTH_OF_CHARGE.getIndex()),
							new BigDecimal(records.get(i).get(AmazonMonthlyStorageFeeReportColumnCA.STORAGE_RATE.getIndex())),
							records.get(i).get(AmazonMonthlyStorageFeeReportColumnCA.CURRENCY.getIndex()),
							new BigDecimal(records.get(i).get(AmazonMonthlyStorageFeeReportColumnCA.ESTIMATED_MONTHLY_STORAGE_FEE.getIndex())),
							null,
							null
					));

				} else if (warehouse == Warehouse.FBA_US) {
						lines.add(new AmazonMonthlyStorageFeeReportRawLineImpl(
								records.get(i).get(AmazonMonthlyStorageFeeReportColumnUS.ASIN.getIndex()),
								records.get(i).get(AmazonMonthlyStorageFeeReportColumnUS.FNSKU.getIndex()),
								records.get(i).get(AmazonMonthlyStorageFeeReportColumnUS.PRODUCT_NAME.getIndex()),
								records.get(i).get(AmazonMonthlyStorageFeeReportColumnUS.FULFILLMENT_CENTER.getIndex()),
								records.get(i).get(AmazonMonthlyStorageFeeReportColumnUS.COUNTRY_CODE.getIndex()),
								new BigDecimal(records.get(i).get(AmazonMonthlyStorageFeeReportColumnUS.LONGEST_SIDE.getIndex())),
								new BigDecimal(records.get(i).get(AmazonMonthlyStorageFeeReportColumnUS.MEDIAN_SIDE.getIndex())),
								new BigDecimal(records.get(i).get(AmazonMonthlyStorageFeeReportColumnUS.SHORTEST_SIDE.getIndex())),
								records.get(i).get(AmazonMonthlyStorageFeeReportColumnUS.MEASUREMENT_UNITS.getIndex()),
								new BigDecimal(records.get(i).get(AmazonMonthlyStorageFeeReportColumnUS.WEIGHT.getIndex())),
								records.get(i).get(AmazonMonthlyStorageFeeReportColumnUS.WEIGHT_UNITS.getIndex()),
								new BigDecimal(records.get(i).get(AmazonMonthlyStorageFeeReportColumnUS.ITEM_VOLUME.getIndex())),
								records.get(i).get(AmazonMonthlyStorageFeeReportColumnUS.VOLUME_UNITS.getIndex()),
								null,
								new BigDecimal(records.get(i).get(AmazonMonthlyStorageFeeReportColumnUS.AVERAGE_QUANTITY_ON_HAND.getIndex())),
								new BigDecimal(records.get(i).get(AmazonMonthlyStorageFeeReportColumnUS.AVERAGE_QUANTITY_PENDING_REMOVAL.getIndex())),
								new BigDecimal(records.get(i).get(AmazonMonthlyStorageFeeReportColumnUS.ESTIMATED_TOTAL_ITEM_VOLUME.getIndex())),
								records.get(i).get(AmazonMonthlyStorageFeeReportColumnUS.MONTH_OF_CHARGE.getIndex()),
								new BigDecimal(records.get(i).get(AmazonMonthlyStorageFeeReportColumnUS.STORAGE_RATE.getIndex())),
								records.get(i).get(AmazonMonthlyStorageFeeReportColumnUS.CURRENCY.getIndex()),
								new BigDecimal(records.get(i).get(AmazonMonthlyStorageFeeReportColumnUS.ESTIMATED_MONTHLY_STORAGE_FEE.getIndex())),
								null,
								null
						));

				} else if (warehouse == Warehouse.FBA_UK) {
					lines.add(new AmazonMonthlyStorageFeeReportRawLineImpl(
							records.get(i).get(AmazonMonthlyStorageFeeReportColumnUK.ASIN.getIndex()),
							records.get(i).get(AmazonMonthlyStorageFeeReportColumnUK.FNSKU.getIndex()),
							records.get(i).get(AmazonMonthlyStorageFeeReportColumnUK.PRODUCT_NAME.getIndex()),
							records.get(i).get(AmazonMonthlyStorageFeeReportColumnUK.FULFILLMENT_CENTER.getIndex()),
							records.get(i).get(AmazonMonthlyStorageFeeReportColumnUK.COUNTRY_CODE.getIndex()),
							new BigDecimal(records.get(i).get(AmazonMonthlyStorageFeeReportColumnUK.LONGEST_SIDE.getIndex())),
							new BigDecimal(records.get(i).get(AmazonMonthlyStorageFeeReportColumnUK.MEDIAN_SIDE.getIndex())),
							new BigDecimal(records.get(i).get(AmazonMonthlyStorageFeeReportColumnUK.SHORTEST_SIDE.getIndex())),
							records.get(i).get(AmazonMonthlyStorageFeeReportColumnUK.MEASUREMENT_UNITS.getIndex()),
							new BigDecimal(records.get(i).get(AmazonMonthlyStorageFeeReportColumnUK.WEIGHT.getIndex())),
							records.get(i).get(AmazonMonthlyStorageFeeReportColumnUK.WEIGHT_UNITS.getIndex()),
							new BigDecimal(records.get(i).get(AmazonMonthlyStorageFeeReportColumnUK.ITEM_VOLUME.getIndex())),
							records.get(i).get(AmazonMonthlyStorageFeeReportColumnUK.VOLUME_UNITS.getIndex()),
							records.get(i).get(AmazonMonthlyStorageFeeReportColumnUK.PRODUCT_SIZE_TIER.getIndex()),
							new BigDecimal(records.get(i).get(AmazonMonthlyStorageFeeReportColumnUK.AVERAGE_QUANTITY_ON_HAND.getIndex())),
							new BigDecimal(records.get(i).get(AmazonMonthlyStorageFeeReportColumnUK.AVERAGE_QUANTITY_PENDING_REMOVAL.getIndex())),
							new BigDecimal(records.get(i).get(AmazonMonthlyStorageFeeReportColumnUK.ESTIMATED_TOTAL_ITEM_VOLUME.getIndex())),
							records.get(i).get(AmazonMonthlyStorageFeeReportColumnUK.MONTH_OF_CHARGE.getIndex()),
							new BigDecimal(records.get(i).get(AmazonMonthlyStorageFeeReportColumnUK.STORAGE_RATE.getIndex())),
							records.get(i).get(AmazonMonthlyStorageFeeReportColumnUK.CURRENCY.getIndex()),
							new BigDecimal(records.get(i).get(AmazonMonthlyStorageFeeReportColumnUK.ESTIMATED_MONTHLY_STORAGE_FEE.getIndex())),
							null,
							records.get(i).get(AmazonMonthlyStorageFeeReportColumnUK.CATEGORY.getIndex())
					));
				}
			}						
		}
		return lines;		
	}
	
	@SuppressWarnings("resource")
	private String getMonthOfChargeByReport(Warehouse warehouse, byte[] fileBytes){
		try {

			Reader fileReader = new StringReader(new String(fileBytes));
			CSVParser parser = new CSVParser(fileReader,CSVFormat.TDF);
			List<CSVRecord> records = parser.getRecords();
			String monthOfCharge = null;
			System.out.println(warehouse.getCountry() + "" + warehouse.getDisplayName());
			System.out.println(records.size());
			for(int i=0; i<records.size();i++){
				System.out.println(i);
				if(i==0){ continue;
				}else if(i==2){
					this.checkBigDecimalFormat(warehouse,records.get(i));
					if (warehouse == Warehouse.FBA_US) {
						monthOfCharge = records.get(i).get(AmazonMonthlyStorageFeeReportColumnUS.MONTH_OF_CHARGE.getIndex());
					} else if (warehouse == Warehouse.FBA_CA) {
						monthOfCharge = records.get(i).get(AmazonMonthlyStorageFeeReportColumnCA.MONTH_OF_CHARGE.getIndex());
					} else {
						monthOfCharge = records.get(i).get(AmazonMonthlyStorageFeeReportColumnUK.MONTH_OF_CHARGE.getIndex());
					}
					break;
				}
			}

			return monthOfCharge;
		} catch (IOException e) {

			e.printStackTrace();
		}
		return null;		
	}
	
	private boolean isImported(Warehouse warehouse, String monthOfCharge) {
		return this.dao.queryExist(warehouse, monthOfCharge);
	}
	
	private boolean isDenyToImportByReportDateAfterCurrentDate(Warehouse warehouse, String monthOfCharge){
		String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
		String currentMonth = String.format("%02d",Calendar.getInstance().get(Calendar.MONTH)+1);
		Calendar currentDate = Calendar.getInstance();
		currentDate.setTime(DateHelper.toDate(currentYear+"-"+currentMonth, this.dateFormat));
		Calendar reportDate = Calendar.getInstance();
		reportDate.setTime(DateHelper.toDate(monthOfCharge, this.dateFormat));						
		return reportDate.after(currentDate) || reportDate.equals(currentDate);		
	}
	
	private void checkBigDecimalFormat(Warehouse warehouse, CSVRecord record){
		Assert.doesNotContain(",", record.get(AmazonMonthlyStorageFeeReportColumnUS.LONGEST_SIDE.getIndex()));
		Assert.doesNotContain(",", record.get(AmazonMonthlyStorageFeeReportColumnUS.MEDIAN_SIDE.getIndex()));
		Assert.doesNotContain(",", record.get(AmazonMonthlyStorageFeeReportColumnUS.SHORTEST_SIDE.getIndex()));
		Assert.doesNotContain(",", record.get(AmazonMonthlyStorageFeeReportColumnUS.WEIGHT.getIndex()));
		Assert.doesNotContain(",", record.get(AmazonMonthlyStorageFeeReportColumnUS.ITEM_VOLUME.getIndex()));
		if (warehouse == Warehouse.FBA_US) {
			Assert.doesNotContain(",", record.get(AmazonMonthlyStorageFeeReportColumnUS.AVERAGE_QUANTITY_ON_HAND.getIndex()));
			Assert.doesNotContain(",", record.get(AmazonMonthlyStorageFeeReportColumnUS.AVERAGE_QUANTITY_PENDING_REMOVAL.getIndex()));
			Assert.doesNotContain(",", record.get(AmazonMonthlyStorageFeeReportColumnUS.ESTIMATED_TOTAL_ITEM_VOLUME.getIndex()));
			Assert.doesNotContain(",", record.get(AmazonMonthlyStorageFeeReportColumnUS.STORAGE_RATE.getIndex()));
			Assert.doesNotContain(",", record.get(AmazonMonthlyStorageFeeReportColumnUS.ESTIMATED_MONTHLY_STORAGE_FEE.getIndex()));
		} else if (warehouse == Warehouse.FBA_CA) {
			Assert.doesNotContain(",", record.get(AmazonMonthlyStorageFeeReportColumnCA.AVERAGE_QUANTITY_ON_HAND.getIndex()));
			Assert.doesNotContain(",", record.get(AmazonMonthlyStorageFeeReportColumnCA.AVERAGE_QUANTITY_PENDING_REMOVAL.getIndex()));
			Assert.doesNotContain(",", record.get(AmazonMonthlyStorageFeeReportColumnCA.ESTIMATED_TOTAL_ITEM_VOLUME.getIndex()));
			Assert.doesNotContain(",", record.get(AmazonMonthlyStorageFeeReportColumnCA.STORAGE_RATE.getIndex()));
			Assert.doesNotContain(",", record.get(AmazonMonthlyStorageFeeReportColumnCA.ESTIMATED_MONTHLY_STORAGE_FEE.getIndex()));
		} else {
			Assert.doesNotContain(",", record.get(AmazonMonthlyStorageFeeReportColumnUK.AVERAGE_QUANTITY_ON_HAND.getIndex()));
			Assert.doesNotContain(",", record.get(AmazonMonthlyStorageFeeReportColumnUK.AVERAGE_QUANTITY_PENDING_REMOVAL.getIndex()));
			Assert.doesNotContain(",", record.get(AmazonMonthlyStorageFeeReportColumnUK.ESTIMATED_TOTAL_ITEM_VOLUME.getIndex()));
			Assert.doesNotContain(",", record.get(AmazonMonthlyStorageFeeReportColumnUK.STORAGE_RATE.getIndex()));
			Assert.doesNotContain(",", record.get(AmazonMonthlyStorageFeeReportColumnUK.ESTIMATED_MONTHLY_STORAGE_FEE.getIndex()));
		}
	}
			
}