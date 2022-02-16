package com.kindminds.drs.service.usecase.report.amazon;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.usecase.report.amazon.ImportAmazonCampaignPerformanceReportUco;
import com.kindminds.drs.api.v1.model.accounting.AmazonCampaignPerformanceReportRawLine;
import com.kindminds.drs.api.v1.model.common.DtoList;
import com.kindminds.drs.api.v1.model.common.Pager;
import com.kindminds.drs.v1.model.impl.amazon.AmazonCampaignPerformanceReportDateCurrencyInfoImpl;
import com.kindminds.drs.api.v1.model.report.AmazonCampaignPerformanceReportBriefLineItem;
import com.kindminds.drs.api.v1.model.report.AmazonCampaignPerformanceReportDateCurrencyInfo;
import com.kindminds.drs.api.data.access.usecase.report.amazon.ImportAmazonCampaignPerformanceReportDao;
import com.kindminds.drs.v1.model.impl.AmazonCampaignPerformanceReportRawLineImpl;
import com.kindminds.drs.util.BigDecimalHelper;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class ImportAmazonCampaignPerformanceReportUcoImpl implements ImportAmazonCampaignPerformanceReportUco {
	
	@Autowired private ImportAmazonCampaignPerformanceReportDao dao;
	
	private boolean isProcessingFlag = false;
	
	public enum AmazonCampaignPerformanceReportColumn{
		CAMPAIGN_NAME(0),
		AD_GROUP_NAME(1),
		ADVERTISED_SKU(2),
		KEYWORD(3),
		MATCH_TYPE(4),
		START_DATE(5),
		END_DATE(6),
		CLICKS(7),
		IMPRESSIONS(8),
		CTR(9),
		TOTAL_SPEND(10),
		AVERAGE_CPC(11),
		CURRENCY(12),
		ONE_DAY_ORDERS_PLACED(13),
		ONE_DAY_ORDERED_PRODUCT_SALES(14),
		ONE_DAY_CONVERSION_RATE(15),
		ONE_DAY_SAME_SKU_UNITS_ORDERED(16),
		ONE_DAY_OTHER_SKU_UNITS_ORDERED(17),
		ONE_DAY_SAME_SKU_UNITS_ORDERED_PRODUCT_SALES(18),
		ONE_DAY_OTHER_SKU_UNITS_ORDERED_PRODUCT_SALES(19),
		ONE_WEEK_ORDERS_PLACED(20),
		ONE_WEEK_ORDERED_PRODUCT_SALES(21),
		ONE_WEEK_CONVERSION_RATE(22),
		ONE_WEEK_SAME_SKU_UNITS_ORDERED(23),
		ONE_WEEK_OTHER_SKU_UNITS_ORDERED(24),
		ONE_WEEK_SAME_SKU_UNITS_ORDERED_PRODUCT_SALES(25),
		ONE_WEEK_OTHER_SKU_UNITS_ORDERED_PRODUCT_SALES(26),
		ONE_MONTH_ORDERS_PLACED(27),
		ONE_MONTH_ORDERED_PRODUCT_SALES(28),
		ONE_MONTH_CONVERSION_RATE(29),
		ONE_MONTH_SAME_SKU_UNITS_ORDERED(30),
		ONE_MONTH_OTHER_SKU_UNITS_ORDERED(31),
		ONE_MONTH_SAME_SKU_UNITS_ORDERED_PRODUCT_SALES(32),
		ONE_MONTH_OTHER_SKU_UNITS_ORDERED_PRODUCT_SALES(33);
		private int index;
		AmazonCampaignPerformanceReportColumn(int index){this.index=index;}
		public int getIndex(){return this.index;}
	}

	private void setIsProcessing(boolean value) {this.isProcessingFlag=value;}
	private boolean isProcessing() {return this.isProcessingFlag;}
	
	@Override
	public List<Marketplace> getMarketplaces(){
		return Marketplace.getAmazonMarketplaces();
	}
	
	@Override
	public String importFile(String marketplaceId,byte[] bytes) {
		String resultMessage = "";
		try {
			if(this.isProcessing()) return null;
			else{
				Marketplace marketplace = Marketplace.fromKey(Integer.parseInt(marketplaceId));
				this.setIsProcessing(true);
				List<AmazonCampaignPerformanceReportRawLine> rawLineList =
						this.createReportRawLines(marketplace,bytes);

				this.dao.clearStagingArea(marketplace);
				this.dao.insertRawLinesToStagingArea(marketplace,rawLineList);
				int importedLineCounts = this.importFromStagingArea(marketplace);
				this.dao.clearStagingArea(marketplace);

				this.setIsProcessing(false);
				resultMessage = importedLineCounts + " Line(s) imported."; 
			}
		} catch (Exception e) {
			this.setIsProcessing(false);
			resultMessage = e.getMessage();
			e.printStackTrace();
		}
		return resultMessage;
	}
	
	@Override
	public String importFile(String marketplaceId,String fullPath) {
		Path p = Paths.get(fullPath);
		try {
			byte[] data = Files.readAllBytes(p);
			return this.importFile(marketplaceId,data);
		} catch (IOException e) {
			e.printStackTrace();
			return "Encounter path problem.";
		}
	}
	
	@SuppressWarnings({ "resource" })
	private List<AmazonCampaignPerformanceReportRawLine> createReportRawLines(Marketplace marketplace, byte[] fileBytes) throws IOException, ParseException{
		
		Reader reader = new StringReader(new String(fileBytes));
		CSVParser parser = new CSVParser(reader, CSVFormat.TDF);
		List<CSVRecord> records = parser.getRecords();
		List<AmazonCampaignPerformanceReportRawLine> lines = new ArrayList<AmazonCampaignPerformanceReportRawLine>();
		List<String> noValueRepresentations = Arrays.asList("N/A","Nicht zutreffend","s/o","N/D");
		String dateFormat = this.getDateFormat(marketplace);
		String timezoneText = "UTC";
		for(int i=0; i<records.size();i++){
			if(i==0) continue; // skip header
			else{
				this.checkCurrency(marketplace, records.get(i));
				BigDecimal totalSpend = BigDecimalHelper.generateBigDecimal(marketplace.getLocale(),records.get(i).get(AmazonCampaignPerformanceReportColumn.TOTAL_SPEND.getIndex()));
				String rawAverageCpc = records.get(i).get(AmazonCampaignPerformanceReportColumn.AVERAGE_CPC.getIndex());
				BigDecimal averageCpc = noValueRepresentations.contains(rawAverageCpc)?null:BigDecimalHelper.generateBigDecimal(marketplace.getLocale(),rawAverageCpc);
				BigDecimal oneDayOrderedProductSales = BigDecimalHelper.generateBigDecimal(marketplace.getLocale(),records.get(i).get(AmazonCampaignPerformanceReportColumn.ONE_DAY_ORDERED_PRODUCT_SALES.getIndex()));
				BigDecimal oneDaySameSkuUnitsOrderedProductSales = BigDecimalHelper.generateBigDecimal(marketplace.getLocale(),records.get(i).get(AmazonCampaignPerformanceReportColumn.ONE_DAY_SAME_SKU_UNITS_ORDERED_PRODUCT_SALES.getIndex()));
				BigDecimal oneDayOtherSkuUnitsOrderedProductSales = BigDecimalHelper.generateBigDecimal(marketplace.getLocale(),records.get(i).get(AmazonCampaignPerformanceReportColumn.ONE_DAY_OTHER_SKU_UNITS_ORDERED_PRODUCT_SALES.getIndex()));
				BigDecimal oneWeekOrderedProductSales = BigDecimalHelper.generateBigDecimal(marketplace.getLocale(),records.get(i).get(AmazonCampaignPerformanceReportColumn.ONE_WEEK_ORDERED_PRODUCT_SALES.getIndex()));
				BigDecimal oneWeekSameSkuUnitsOrderedProductSales = BigDecimalHelper.generateBigDecimal(marketplace.getLocale(),records.get(i).get(AmazonCampaignPerformanceReportColumn.ONE_WEEK_SAME_SKU_UNITS_ORDERED_PRODUCT_SALES.getIndex()));
				BigDecimal oneWeekOtherSkuUnitsOrderedProductSales = BigDecimalHelper.generateBigDecimal(marketplace.getLocale(),records.get(i).get(AmazonCampaignPerformanceReportColumn.ONE_WEEK_OTHER_SKU_UNITS_ORDERED_PRODUCT_SALES.getIndex()));
				BigDecimal oneMonthOrderedProductSales = BigDecimalHelper.generateBigDecimal(marketplace.getLocale(),records.get(i).get(AmazonCampaignPerformanceReportColumn.ONE_MONTH_ORDERED_PRODUCT_SALES.getIndex()));
				BigDecimal oneMonthSameSkuUnitsOrderedProductSales = BigDecimalHelper.generateBigDecimal(marketplace.getLocale(),records.get(i).get(AmazonCampaignPerformanceReportColumn.ONE_MONTH_SAME_SKU_UNITS_ORDERED_PRODUCT_SALES.getIndex()));
				BigDecimal oneMonthOtherSkuUnitsOrderedProductSales = BigDecimalHelper.generateBigDecimal(marketplace.getLocale(),records.get(i).get(AmazonCampaignPerformanceReportColumn.ONE_MONTH_OTHER_SKU_UNITS_ORDERED_PRODUCT_SALES.getIndex()));				
				AmazonCampaignPerformanceReportRawLineImpl line = new AmazonCampaignPerformanceReportRawLineImpl(
						dateFormat,
						timezoneText,
						records.get(i).get(AmazonCampaignPerformanceReportColumn.CAMPAIGN_NAME.getIndex()),
						records.get(i).get(AmazonCampaignPerformanceReportColumn.AD_GROUP_NAME.getIndex()),
						records.get(i).get(AmazonCampaignPerformanceReportColumn.ADVERTISED_SKU.getIndex()),
						records.get(i).get(AmazonCampaignPerformanceReportColumn.KEYWORD.getIndex()),
						records.get(i).get(AmazonCampaignPerformanceReportColumn.MATCH_TYPE.getIndex()),
						records.get(i).get(AmazonCampaignPerformanceReportColumn.START_DATE.getIndex()),
						records.get(i).get(AmazonCampaignPerformanceReportColumn.END_DATE.getIndex()),
						records.get(i).get(AmazonCampaignPerformanceReportColumn.CLICKS.getIndex()),
						records.get(i).get(AmazonCampaignPerformanceReportColumn.IMPRESSIONS.getIndex()),
						records.get(i).get(AmazonCampaignPerformanceReportColumn.CTR.getIndex()),
						totalSpend,
						averageCpc,
						records.get(i).get(AmazonCampaignPerformanceReportColumn.CURRENCY.getIndex()),
						records.get(i).get(AmazonCampaignPerformanceReportColumn.ONE_DAY_ORDERS_PLACED.getIndex()),
						oneDayOrderedProductSales,
						records.get(i).get(AmazonCampaignPerformanceReportColumn.ONE_DAY_CONVERSION_RATE.getIndex()),
						records.get(i).get(AmazonCampaignPerformanceReportColumn.ONE_DAY_SAME_SKU_UNITS_ORDERED.getIndex()),
						records.get(i).get(AmazonCampaignPerformanceReportColumn.ONE_DAY_OTHER_SKU_UNITS_ORDERED.getIndex()),
						oneDaySameSkuUnitsOrderedProductSales,
						oneDayOtherSkuUnitsOrderedProductSales,
						records.get(i).get(AmazonCampaignPerformanceReportColumn.ONE_WEEK_ORDERS_PLACED.getIndex()),
						oneWeekOrderedProductSales,
						records.get(i).get(AmazonCampaignPerformanceReportColumn.ONE_WEEK_CONVERSION_RATE.getIndex()),
						records.get(i).get(AmazonCampaignPerformanceReportColumn.ONE_WEEK_SAME_SKU_UNITS_ORDERED.getIndex()),
						records.get(i).get(AmazonCampaignPerformanceReportColumn.ONE_WEEK_OTHER_SKU_UNITS_ORDERED.getIndex()),
						oneWeekSameSkuUnitsOrderedProductSales,
						oneWeekOtherSkuUnitsOrderedProductSales,
						records.get(i).get(AmazonCampaignPerformanceReportColumn.ONE_MONTH_ORDERS_PLACED.getIndex()),
						oneMonthOrderedProductSales,
						records.get(i).get(AmazonCampaignPerformanceReportColumn.ONE_MONTH_CONVERSION_RATE.getIndex()),
						records.get(i).get(AmazonCampaignPerformanceReportColumn.ONE_MONTH_SAME_SKU_UNITS_ORDERED.getIndex()),
						records.get(i).get(AmazonCampaignPerformanceReportColumn.ONE_MONTH_OTHER_SKU_UNITS_ORDERED.getIndex()),
						oneMonthSameSkuUnitsOrderedProductSales,
						oneMonthOtherSkuUnitsOrderedProductSales
				);
				lines.add(line);
			}
		}
		return lines;
	}
	
	private void checkCurrency(Marketplace marketplace,CSVRecord record){
		Assert.isTrue(marketplace.getCurrency().name().equals(record.get(AmazonCampaignPerformanceReportColumn.CURRENCY.getIndex())),"Marketplace currency and report currency mismatch.");
	}
	
	private String getDateFormat(Marketplace marketplace) {
		if(marketplace==Marketplace.AMAZON_COM) return "MM/dd/yyyy H:mm Z";
		if(marketplace==Marketplace.AMAZON_CO_UK) return "dd/MM/yyyy H:mm Z";
		if(marketplace==Marketplace.AMAZON_CA) return "MM/dd/yyyy H:mm Z";
		if(marketplace==Marketplace.AMAZON_DE) return "dd/MM/yyyy H:mm Z";
		if(marketplace==Marketplace.AMAZON_FR) return "dd/MM/yyyy H:mm Z";
		if(marketplace==Marketplace.AMAZON_ES) return "dd/MM/yyyy H:mm Z";
		if(marketplace==Marketplace.AMAZON_IT) return "dd/MM/yyyy H:mm Z";		
		Assert.isTrue(false,"Unknown date format.");
		return null;
	}
	
	private int importFromStagingArea(Marketplace marketplace){
		int importedLineCounts = 0;
		List<Object []> resultColumnsList = this.dao.queryDistinctDateCurrencyInfoFromStagingArea(marketplace);

		List<AmazonCampaignPerformanceReportDateCurrencyInfo> infoList = new ArrayList<AmazonCampaignPerformanceReportDateCurrencyInfo>();
		for(Object[] columns:resultColumnsList){
			infoList.add(new AmazonCampaignPerformanceReportDateCurrencyInfoImpl((Date)columns[0],(Date)columns[1],(String)columns[2]));
		}

		for(AmazonCampaignPerformanceReportDateCurrencyInfo info:infoList){
			if(!this.dao.isDateCurrencyExist(marketplace,info)){
				importedLineCounts += this.dao.insertFromStagingAreaByInfo(marketplace,info);
			}else {
				importedLineCounts += this.dao.updateFromStagingAreaByInfo(marketplace,info);
			}
		}
		return importedLineCounts;
	}

	@Override
	public DtoList<AmazonCampaignPerformanceReportBriefLineItem> getBriefLineItem(int pageIndex, String marketplaceId, String dateStartUtc, String dateEndUtc, String marketSku) {
		Marketplace marketplace = Marketplace.fromKey(Integer.parseInt(marketplaceId));
		Date start = DateHelper.toDate(dateStartUtc+ " UTC", "yyyy-MM-dd z");
		Date end = DateHelper.toDate(dateEndUtc+ " UTC", "yyyy-MM-dd z");
		DtoList<AmazonCampaignPerformanceReportBriefLineItem> dtoList = new DtoList<AmazonCampaignPerformanceReportBriefLineItem>();
		dtoList.setTotalRecords(this.dao.queryBriefLineItemCount(marketplace,start,end,marketSku));
		dtoList.setPager(new Pager(pageIndex, dtoList.getTotalRecords(),20));
		dtoList.setItems(this.dao.queryBriefLineItem(dtoList.getPager().getStartRowNum(),dtoList.getPager().getPageSize(),marketplace,start,end,marketSku));
		return dtoList;
	}

}
