package com.kindminds.drs.service.usecase.report.amazon;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.data.access.usecase.report.amazon.ImportAmazonSponsoredProductsAdvertisedProductReportDao;
import com.kindminds.drs.api.usecase.report.amazon.ImportAmazonSponsoredProductsAdvertisedProductReportUco;
import com.kindminds.drs.api.v1.model.amazon.AmazonSponsoredProductsAdvertisedProductReportLineItem;
import com.kindminds.drs.api.v1.model.common.DtoList;
import com.kindminds.drs.api.v1.model.common.Pager;
import com.kindminds.drs.v1.model.impl.amazon.AmazonSponsoredProductsAdvertisedProductReportDateCurrencyInfoImpl;
import com.kindminds.drs.api.v1.model.report.AmazonSponsoredProductsAdvertisedProductReportBriefLineItem;
import com.kindminds.drs.api.v1.model.report.AmazonSponsoredProductsAdvertisedProductReportDateCurrencyInfo;

import com.kindminds.drs.v1.model.impl.AmazonSponsoredProductsAdvertisedProductReportLineItemImpl;
import com.kindminds.drs.util.DateHelper;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ImportAmazonSponsoredProductsAdvertisedProductReportUcoImpl
		implements ImportAmazonSponsoredProductsAdvertisedProductReportUco {
	
	@Autowired private ImportAmazonSponsoredProductsAdvertisedProductReportDao dao;

	private boolean isProcessingFlag = false;
	//private String dateFormat = "dd-MMM-yyyy Z";
	private String dateFormat = "yyyy/MM/dd";
	
	public enum AmazonSponsoredProductsAdvertisedProductReportColumn{
		DATE(0),
		PORTFOLIO_NAME(1),
		CURRENCY(2),
		CAMPAIGN_NAME(3),
		AD_GROUP_NAME(4),
		ADVERTISED_SKU(5),
		ADVERTISED_ASIN(6),
		IMPRESSIONS(7),
		CLICKS(8),
		CTR(9),
		CPC(10),
		SPEND(11),
		SEVEN_DAY_TOTAL_SALES(12),
		ACOS(13),
		ROAS(14),
		SEVEN_DAY_TOTAL_ORDERS(15),
		SEVEN_DAY_TOTAL_UNITS(16),
		SEVEN_DAY_CONVERSION_RATE(17),
		SEVEN_DAY_ADVERTISED_SKU_UNITS(18),
		SEVEN_DAY_OTHER_SKU_UNITS(19),
		SEVEN_DAY_ADVERTISED_SKU_SALES(20),
		SEVEN_DAY_OTHER_SKU_SALES(21);
		private int index;
		AmazonSponsoredProductsAdvertisedProductReportColumn(int index){this.index=index;}
		public int getIndex(){return this.index;}				
	}
	
	private void setIsProcessing(boolean value) {this.isProcessingFlag=value;}
	private boolean isProcessing() {return this.isProcessingFlag;}
		
	@Override
	public List<Marketplace> getMarketplaces() {
		return Marketplace.getAmazonMarketplaces();
	}

	@Override
	public String importFile(String marketplaceId, byte[] fileBytes) {
		String resultMessage = "";
		try {
			if(this.isProcessing()) return null;
			else{
				Marketplace marketplace = Marketplace.fromKey(Integer.parseInt(marketplaceId));
				this.setIsProcessing(true);

				List<AmazonSponsoredProductsAdvertisedProductReportLineItem> rawLineList =
						this.createReportRawLines(marketplace, fileBytes , false);

				this.dao.clearStagingArea(marketplace);
				this.dao.insertRawLinesToStagingArea(marketplace, rawLineList);
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
	public String importECMFile(String marketplaceId, byte[] fileBytes) {
		String resultMessage = "";
		try {
			if(this.isProcessing()) return null;
			else{
				Marketplace marketplace = Marketplace.fromKey(Integer.parseInt(marketplaceId));
				this.setIsProcessing(true);
				List<AmazonSponsoredProductsAdvertisedProductReportLineItem> rawLineList =
						this.createReportRawLines(marketplace, fileBytes , true);

				this.dao.clearStagingArea(marketplace);
				this.dao.insertRawLinesToStagingArea(marketplace, rawLineList);
				int importedLineCounts = this.importECMFromStagingArea(marketplace);
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
	public String importFile(String marketplaceId, String fullPath) {
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
	private List<AmazonSponsoredProductsAdvertisedProductReportLineItem>
			createReportRawLines(Marketplace marketplace, byte [] bytes , boolean isECM) throws IOException, ParseException{
		InputStream fileInputStream = new ByteArrayInputStream(bytes);		
		XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
	    XSSFSheet sheet = workbook.getSheetAt(0);
	    List<AmazonSponsoredProductsAdvertisedProductReportLineItem> lines = new ArrayList<AmazonSponsoredProductsAdvertisedProductReportLineItem>();
	    String timezoneText = "UTC";  	    	        
	    for(int i=0; i<sheet.getPhysicalNumberOfRows();i++){
	    	if(i==0) continue;
	    	else{
	    		this.checkCurrency(marketplace, sheet.getRow(i));
	    		AmazonSponsoredProductsAdvertisedProductReportLineItemImpl line =
						new AmazonSponsoredProductsAdvertisedProductReportLineItemImpl(
						this.dateFormat,
						timezoneText,
						sheet.getRow(i).getCell(AmazonSponsoredProductsAdvertisedProductReportColumn.DATE.getIndex()).getDateCellValue(),
						sheet.getRow(i).getCell(AmazonSponsoredProductsAdvertisedProductReportColumn.PORTFOLIO_NAME.getIndex()).toString(),
						sheet.getRow(i).getCell(AmazonSponsoredProductsAdvertisedProductReportColumn.CURRENCY.getIndex()).toString(),
								 this.processCampaingName(sheet.getRow(i).getCell(AmazonSponsoredProductsAdvertisedProductReportColumn.CAMPAIGN_NAME.getIndex()).toString() , isECM),
						sheet.getRow(i).getCell(AmazonSponsoredProductsAdvertisedProductReportColumn.AD_GROUP_NAME.getIndex()).toString(),
						sheet.getRow(i).getCell(AmazonSponsoredProductsAdvertisedProductReportColumn.ADVERTISED_SKU.getIndex()).toString(),
						sheet.getRow(i).getCell(AmazonSponsoredProductsAdvertisedProductReportColumn.ADVERTISED_ASIN.getIndex()).toString(),
						sheet.getRow(i).getCell(AmazonSponsoredProductsAdvertisedProductReportColumn.IMPRESSIONS.getIndex()).toString(),
						sheet.getRow(i).getCell(AmazonSponsoredProductsAdvertisedProductReportColumn.CLICKS.getIndex()).toString(),
						sheet.getRow(i).getCell(AmazonSponsoredProductsAdvertisedProductReportColumn.CTR.getIndex()) == null?"":sheet.getRow(i).getCell(AmazonSponsoredProductsAdvertisedProductReportColumn.CTR.getIndex()).toString(),
						sheet.getRow(i).getCell(AmazonSponsoredProductsAdvertisedProductReportColumn.CPC.getIndex()) == null?"":sheet.getRow(i).getCell(AmazonSponsoredProductsAdvertisedProductReportColumn.CPC.getIndex()).toString(),
						sheet.getRow(i).getCell(AmazonSponsoredProductsAdvertisedProductReportColumn.SPEND.getIndex()).toString(),
						sheet.getRow(i).getCell(AmazonSponsoredProductsAdvertisedProductReportColumn.SEVEN_DAY_TOTAL_SALES.getIndex()).toString(),
						sheet.getRow(i).getCell(AmazonSponsoredProductsAdvertisedProductReportColumn.SEVEN_DAY_TOTAL_ORDERS.getIndex()).toString(),
						sheet.getRow(i).getCell(AmazonSponsoredProductsAdvertisedProductReportColumn.SEVEN_DAY_TOTAL_UNITS.getIndex()).toString(),
						sheet.getRow(i).getCell(AmazonSponsoredProductsAdvertisedProductReportColumn.SEVEN_DAY_CONVERSION_RATE.getIndex()) == null?"":sheet.getRow(i).getCell(AmazonSponsoredProductsAdvertisedProductReportColumn.SEVEN_DAY_CONVERSION_RATE.getIndex()).toString(),
						sheet.getRow(i).getCell(AmazonSponsoredProductsAdvertisedProductReportColumn.SEVEN_DAY_ADVERTISED_SKU_UNITS.getIndex()).toString(),
						sheet.getRow(i).getCell(AmazonSponsoredProductsAdvertisedProductReportColumn.SEVEN_DAY_OTHER_SKU_UNITS.getIndex()).toString(),
						sheet.getRow(i).getCell(AmazonSponsoredProductsAdvertisedProductReportColumn.SEVEN_DAY_ADVERTISED_SKU_SALES.getIndex()).toString(),
						sheet.getRow(i).getCell(AmazonSponsoredProductsAdvertisedProductReportColumn.SEVEN_DAY_OTHER_SKU_SALES.getIndex()).toString()						
				);
				lines.add(line);			
	    	}	    	
	    }	    
	    return lines;	    		
	}

	private String processCampaingName(String name , boolean isECM){

		//todo
		/*
		if(isECM){
			name = "[K621 SecuX Tech] " + name;
		}*/

		return name;

	}
	
	private void checkCurrency(Marketplace marketplace,XSSFRow row){
		System.out.println("marketplace: " + marketplace);
		System.out.println("row: " + row);
		System.out.println("cell: " + row.getCell(AmazonSponsoredProductsAdvertisedProductReportColumn.CURRENCY.getIndex()));
		Assert.isTrue(marketplace.getCurrency().name().equals(row.getCell(AmazonSponsoredProductsAdvertisedProductReportColumn.CURRENCY.getIndex()).getStringCellValue()),"Marketplace currency and report currency mismatch.");			
	}
	
	private int importFromStagingArea(Marketplace marketplace){
		int importedLineCounts = 0;
		List<Object []> resultColumnsList =
				this.dao.queryDistinctDateCurrencyInfoFromStagingArea(marketplace);

		List<AmazonSponsoredProductsAdvertisedProductReportDateCurrencyInfo> infoList =  new ArrayList<AmazonSponsoredProductsAdvertisedProductReportDateCurrencyInfo>();
		for(Object[] columns:resultColumnsList){
			infoList.add(new AmazonSponsoredProductsAdvertisedProductReportDateCurrencyInfoImpl((Date)columns[0],(Date)columns[1],(String)columns[2]));
		}


		for(AmazonSponsoredProductsAdvertisedProductReportDateCurrencyInfo info:infoList){
			if(!this.dao.isDateCurrencyExist(marketplace,info)){
				importedLineCounts += this.dao.insertFromStagingAreaByInfo(marketplace,info);
			}
		}
		return importedLineCounts;	
	}

	private int importECMFromStagingArea(Marketplace marketplace){
		int importedLineCounts = 0;
		List<Object []> resultColumnsList =
				this.dao.queryDistinctDateCurrencyInfoFromStagingArea(marketplace);

		List<AmazonSponsoredProductsAdvertisedProductReportDateCurrencyInfo> infoList =  new ArrayList<AmazonSponsoredProductsAdvertisedProductReportDateCurrencyInfo>();
		for(Object[] columns:resultColumnsList){
			infoList.add(new AmazonSponsoredProductsAdvertisedProductReportDateCurrencyInfoImpl((Date)columns[0],(Date)columns[1],(String)columns[2]));
		}

		for(AmazonSponsoredProductsAdvertisedProductReportDateCurrencyInfo info:infoList){
			//if(!this.dao.isDateCurrencyExist(marketplace,info)){
				importedLineCounts += this.dao.insertFromStagingAreaByInfo(marketplace,info);
			//}
		}
		return importedLineCounts;
	}
	
	@Override
	public DtoList<AmazonSponsoredProductsAdvertisedProductReportBriefLineItem> getBriefLineItem(int pageIndex,
																								 String marketplaceId, String dateStartUtc, String dateEndUtc, String marketSku) {
		Marketplace marketplace = Marketplace.fromKey(Integer.parseInt(marketplaceId));
		Date start = DateHelper.toDate(dateStartUtc+ " UTC", "yyyy-MM-dd z");
		Date end = DateHelper.toDate(dateEndUtc+ " UTC", "yyyy-MM-dd z");
		DtoList<AmazonSponsoredProductsAdvertisedProductReportBriefLineItem> dtoList = new DtoList<AmazonSponsoredProductsAdvertisedProductReportBriefLineItem>();				
		dtoList.setTotalRecords(this.dao.queryBriefLineItemCount(marketplace,start,end,marketSku));
		dtoList.setPager(new Pager(pageIndex, dtoList.getTotalRecords(),20));
		dtoList.setItems(this.dao.queryBriefLineItem(dtoList.getPager().getStartRowNum(),dtoList.getPager().getPageSize(),marketplace,start,end,marketSku));
		return dtoList;
	}
			
}