package com.kindminds.drs.service.usecase.report.amazon;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.data.access.usecase.report.amazon.ImportAmazonSettlementReportDao;
import com.kindminds.drs.api.data.access.usecase.report.amazon.ImportAmazonSettlementReportDao.SettlementReportColumn;
import com.kindminds.drs.api.usecase.report.amazon.ImportAmazonSettlementReportUco;
import com.kindminds.drs.api.v1.model.accounting.AmazonSettlementReportInfo;
import com.kindminds.drs.api.v1.model.accounting.AmazonSettlementReportRawLine;

import com.kindminds.drs.v1.model.impl.amazon.AmazonSettlementReportInfoImpl;
import com.kindminds.drs.v1.model.impl.amazon.AmazonSettlementReportInfoImplForFile;
import com.kindminds.drs.v1.model.impl.amazon.AmazonSettlementReportRawLineImpl;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class ImportAmazonSettlementReportUcoImpl implements ImportAmazonSettlementReportUco {
	
	@Autowired private ImportAmazonSettlementReportDao dao;
	
	private final String dateFormatDashedYYYYMMDD = "yyyy-MM-dd";
	private final String dateFormatDottedDDMMYYYY = "dd.MM.yyyy";
	private final String dateTimeFormatDashedYYYYMMDD = "yyyy-MM-dd HH:mm:ss z";
	private final String dateTimeFormatDottedDDMMYYYY = "dd.MM.yyyy HH:mm:ss z";
	
	private final CSVFormat settlementReportFormat = CSVFormat.TDF.withHeader(
			SettlementReportColumn.SETTLEMENT_ID.getName(),
			SettlementReportColumn.SETTLEMENT_START_DATE.getName(),
			SettlementReportColumn.SETTLEMENT_END_DATE.getName(),
			SettlementReportColumn.DEPOSIT_DATE.getName(),
			SettlementReportColumn.TOTAL_AMOUNT.getName(),
			SettlementReportColumn.CURRENCY.getName(),
			SettlementReportColumn.TRANSACTION_TYPE.getName(),
			SettlementReportColumn.ORDER_ID.getName(),
			SettlementReportColumn.MERCHANT_ORDER_ID.getName(),
			SettlementReportColumn.ADJUSTMENT_ID.getName(),
			SettlementReportColumn.SHIPMENT_ID.getName(),
			SettlementReportColumn.MARKETPLACE_NAME.getName(),
			SettlementReportColumn.AMOUNT_TYPE.getName(),
			SettlementReportColumn.AMOUNT_DESCRIPTION.getName(),
			SettlementReportColumn.AMOUNT.getName(),
			SettlementReportColumn.FULFILLMENT_ID.getName(),
			SettlementReportColumn.POSTED_DATE.getName(),
			SettlementReportColumn.POSTED_DATE_TIME.getName(),
			SettlementReportColumn.ORDER_ITEM_CODE.getName(),
			SettlementReportColumn.MERCHANT_ORDER_ITEM_ID.getName(),
			SettlementReportColumn.MERCHANT_ADJUSTMENT_ITEM_ID.getName(),
			SettlementReportColumn.SKU.getName(),
			SettlementReportColumn.QUANTITY_PURCHASED.getName(),
			SettlementReportColumn.PROMOTION_ID.getName());
	
	@Override
	public List<Marketplace> getMarketplaces(){
		return Marketplace.getAmazonMarketplaces();
	}
	
	@SuppressWarnings("resource")
	private AmazonSettlementReportInfo getSettlementInfoFromFile(Marketplace sourceMarketplace,byte[] fileBytes){
		try {
			Reader fileReader = new StringReader(new String(fileBytes));
			CSVParser parser = new CSVParser(fileReader,this.settlementReportFormat);
			int i=0;
			AmazonSettlementReportInfoImplForFile info = null;
			for (CSVRecord record : parser) {
				i+=1;
				if(i==1){ continue; } // Skip Header
				else if(i==2){
					info = new AmazonSettlementReportInfoImplForFile(
						record.get(SettlementReportColumn.SETTLEMENT_ID.getName()),
						record.get(SettlementReportColumn.SETTLEMENT_START_DATE.getName()),
						record.get(SettlementReportColumn.SETTLEMENT_END_DATE.getName()),
						record.get(SettlementReportColumn.DEPOSIT_DATE.getName()),
						record.isSet(SettlementReportColumn.TOTAL_AMOUNT.getName())?this.getAmountInValidFormat(sourceMarketplace,record.get(SettlementReportColumn.TOTAL_AMOUNT.getName())):null,
						record.get(SettlementReportColumn.CURRENCY.getName()),
						sourceMarketplace
					);
					break;
				}
			}
			this.checkSettlementReportInfo(info);
			return info;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void checkSettlementReportInfo(AmazonSettlementReportInfoImplForFile info) {
		Assert.notNull(info);
		Assert.notNull(info.getSettlementId());
		Assert.notNull(info.getDateStart());
		Assert.notNull(info.getDateEnd());
		Assert.notNull(info.getDateDeposit());
		Assert.notNull(info.getTotalAmount());
		Assert.notNull(info.getCurrency());
		return;
	}
	
	@SuppressWarnings("resource")
	private List<AmazonSettlementReportRawLine> getReportRawLineFromFile(byte[] fileBytes,Marketplace sourceMarketplace, String settlementId, String currency) {
		try {
			Reader fileReader = new StringReader(new String(fileBytes));
			CSVParser parser = new CSVParser(fileReader,this.settlementReportFormat);
			int i=0;
			List<AmazonSettlementReportRawLine> lines = new ArrayList<AmazonSettlementReportRawLine>();
			List<String> marketplaceNamesNoNeedToCheck = Arrays.asList("Non-Amazon", "Beanworthy", "SI UK Prod Marketplace","SI CA Prod Marketplace","SI Prod IT Marketplace");
			for (CSVRecord record : parser) {
				i+=1;
				if(i==1||i==2){ continue; } // Skip Header and Information Rows 
				else {// Report Lines Starts from Line 3
					Assert.isTrue(settlementId.equals(record.get(SettlementReportColumn.SETTLEMENT_ID.getName())));
					Assert.isTrue(!StringUtils.hasText(record.get(SettlementReportColumn.SETTLEMENT_START_DATE.getName())));
					Assert.isTrue(!StringUtils.hasText(record.get(SettlementReportColumn.SETTLEMENT_END_DATE.getName())));
					Assert.isTrue(!StringUtils.hasText(record.get(SettlementReportColumn.DEPOSIT_DATE.getName())));
					Assert.isTrue(!StringUtils.hasText(record.get(SettlementReportColumn.TOTAL_AMOUNT.getName())));
					String marketplaceName = StringUtils.hasText(record.get(SettlementReportColumn.MARKETPLACE_NAME.getName()))?record.get(SettlementReportColumn.MARKETPLACE_NAME.getName()):null;
					String amount = record.isSet(SettlementReportColumn.AMOUNT.getName())?this.getAmountInValidFormat(sourceMarketplace,record.get(SettlementReportColumn.AMOUNT.getName())):null;
					String promotionId = record.isSet(SettlementReportColumn.PROMOTION_ID.getName())?record.get(SettlementReportColumn.PROMOTION_ID.getName()):null;
					String postedDate = this.getPostedDate(record);
					if(marketplaceName!=null){
						if(!marketplaceNamesNoNeedToCheck.contains(marketplaceName)) Assert.isTrue(sourceMarketplace.getName().equals(marketplaceName),"Marketplace mismatch.");
					}
					AmazonSettlementReportRawLineImpl line = new AmazonSettlementReportRawLineImpl(
						settlementId, 
						currency, 
						StringUtils.hasText(record.get(SettlementReportColumn.TRANSACTION_TYPE.getName()))?record.get(SettlementReportColumn.TRANSACTION_TYPE.getName()):null,
						StringUtils.hasText(record.get(SettlementReportColumn.ORDER_ID.getName()))?record.get(SettlementReportColumn.ORDER_ID.getName()):null,
						StringUtils.hasText(record.get(SettlementReportColumn.MERCHANT_ORDER_ID.getName()))?record.get(SettlementReportColumn.MERCHANT_ORDER_ID.getName()):null,
						StringUtils.hasText(record.get(SettlementReportColumn.ADJUSTMENT_ID.getName()))?record.get(SettlementReportColumn.ADJUSTMENT_ID.getName()):null,
						StringUtils.hasText(record.get(SettlementReportColumn.SHIPMENT_ID.getName()))?record.get(SettlementReportColumn.SHIPMENT_ID.getName()):null,
						marketplaceName,
						StringUtils.hasText(record.get(SettlementReportColumn.AMOUNT_TYPE.getName()))?record.get(SettlementReportColumn.AMOUNT_TYPE.getName()):null,
						StringUtils.hasText(record.get(SettlementReportColumn.AMOUNT_DESCRIPTION.getName()))?record.get(SettlementReportColumn.AMOUNT_DESCRIPTION.getName()):null, 
						amount, 
						StringUtils.hasText(record.get(SettlementReportColumn.FULFILLMENT_ID.getName()))?record.get(SettlementReportColumn.FULFILLMENT_ID.getName()):null,
						postedDate,
						StringUtils.hasText(record.get(SettlementReportColumn.POSTED_DATE_TIME.getName()))?record.get(SettlementReportColumn.POSTED_DATE_TIME.getName()):null,
						StringUtils.hasText(record.get(SettlementReportColumn.ORDER_ITEM_CODE.getName()))?record.get(SettlementReportColumn.ORDER_ITEM_CODE.getName()):null,
						StringUtils.hasText(record.get(SettlementReportColumn.MERCHANT_ORDER_ITEM_ID.getName()))?record.get(SettlementReportColumn.MERCHANT_ORDER_ITEM_ID.getName()):null, 
						StringUtils.hasText(record.get(SettlementReportColumn.MERCHANT_ADJUSTMENT_ITEM_ID.getName()))?record.get(SettlementReportColumn.MERCHANT_ADJUSTMENT_ITEM_ID.getName()):null, 
						StringUtils.hasText(record.get(SettlementReportColumn.SKU.getName()))?record.get(SettlementReportColumn.SKU.getName()):null, 
						StringUtils.hasText(record.get(SettlementReportColumn.QUANTITY_PURCHASED.getName()))?record.get(SettlementReportColumn.QUANTITY_PURCHASED.getName()):null,
						promotionId
					);
					lines.add(line);
				}
			}
			return lines;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private String getPostedDate(CSVRecord record) {
		String postedDate = record.get(SettlementReportColumn.POSTED_DATE.getName());
		String postedDateTime = record.get(SettlementReportColumn.POSTED_DATE_TIME.getName());
		if(postedDate.length()>10) return postedDateTime.substring(0,10);
		Assert.isTrue(postedDate.length()==10);
		return postedDate;
	}

	private String getAmountInValidFormat(Marketplace sourceMarketplace,String origin){
		if(sourceMarketplace==Marketplace.AMAZON_DE
		 ||sourceMarketplace==Marketplace.AMAZON_FR
		 ||sourceMarketplace==Marketplace.AMAZON_ES
		 ||sourceMarketplace==Marketplace.AMAZON_IT)
			return origin.replace(",", ".");
		return origin;
	}
	
	private String getDateFormat(Marketplace sourceMarketplace){
		if(sourceMarketplace==Marketplace.AMAZON_COM)   return this.dateFormatDashedYYYYMMDD;
		if(sourceMarketplace==Marketplace.AMAZON_CO_UK) return this.dateFormatDottedDDMMYYYY;
		if(sourceMarketplace==Marketplace.AMAZON_CA)    return this.dateFormatDottedDDMMYYYY;
		if(sourceMarketplace==Marketplace.AMAZON_DE)    return this.dateFormatDottedDDMMYYYY;
		if(sourceMarketplace==Marketplace.AMAZON_FR)    return this.dateFormatDottedDDMMYYYY;
		if(sourceMarketplace==Marketplace.AMAZON_IT)    return this.dateFormatDottedDDMMYYYY;
		if(sourceMarketplace==Marketplace.AMAZON_ES)    return this.dateFormatDottedDDMMYYYY;
		if(sourceMarketplace==Marketplace.AMAZON_COM_MX)    return this.dateFormatDottedDDMMYYYY;
		Assert.isTrue(false);
		return null;
	}
	
	private String getDateTimeFormat(Marketplace sourceMarketplace){
		if(sourceMarketplace==Marketplace.AMAZON_COM)   return this.dateTimeFormatDashedYYYYMMDD;
		if(sourceMarketplace==Marketplace.AMAZON_CO_UK) return this.dateTimeFormatDottedDDMMYYYY;
		if(sourceMarketplace==Marketplace.AMAZON_CA)    return this.dateTimeFormatDottedDDMMYYYY;
		if(sourceMarketplace==Marketplace.AMAZON_DE)    return this.dateTimeFormatDottedDDMMYYYY;
		if(sourceMarketplace==Marketplace.AMAZON_FR)    return this.dateTimeFormatDottedDDMMYYYY;
		if(sourceMarketplace==Marketplace.AMAZON_IT)    return this.dateTimeFormatDottedDDMMYYYY;
		if(sourceMarketplace==Marketplace.AMAZON_ES)    return this.dateTimeFormatDottedDDMMYYYY;
		if(sourceMarketplace==Marketplace.AMAZON_COM_MX)    return this.dateTimeFormatDottedDDMMYYYY;
		Assert.isTrue(false);
		return null;
	}

	@Override
	public List<AmazonSettlementReportInfo> getSettlementReportInfoList() {
		List<Object []> columnsList = this.dao.querySettlementReportInfoList();
		List<AmazonSettlementReportInfo> list = new ArrayList<>();
		for(Object[] columns:columnsList){
			Date dateStart = (Date)columns[0];
			Date dateEnd = (Date)columns[1];
			String settlementId = (String)columns[2];
			String currencyName = (String)columns[3];
			Integer sourceMarketplaceId = (Integer)columns[4];
			list.add(new AmazonSettlementReportInfoImpl(dateStart, dateEnd, settlementId, currencyName, sourceMarketplaceId));
		}


		this.checkSkuNotDuplicateInLine(list);
		return list;
	}
	
	private void checkSkuNotDuplicateInLine(List<AmazonSettlementReportInfo> infoList){
		Set<String> settlementIdSet = new HashSet<String>();
		for(AmazonSettlementReportInfo info:infoList){
			settlementIdSet.add(info.getSettlementId());
		}
		Assert.isTrue(settlementIdSet.size()==infoList.size(),"Duplicated Settlement Id");
	}

	@Override
	public String uploadFileAndImport(String marketplaceId,byte[] fileBytes) {
		Marketplace sourceMarketplace = Marketplace.fromKey(Integer.valueOf(marketplaceId));
		AmazonSettlementReportInfo info = this.getSettlementInfoFromFile(sourceMarketplace,fileBytes);
		if(this.dao.isReportImported(info.getSettlementId())) return "ALREADY IMPORTED";
		this.dao.clearUncompletedInfoAndLines(info.getSettlementId());
		List<AmazonSettlementReportRawLine> lines = this.getReportRawLineFromFile(fileBytes,sourceMarketplace,info.getSettlementId(), info.getCurrency());
		String dateFormat = this.getDateFormat(sourceMarketplace);
		String dateTimeFormat = this.getDateTimeFormat(sourceMarketplace);
		this.dao.insertReportInfo(info,dateTimeFormat);
		this.dao.insertReportRawLines(lines,dateFormat,dateTimeFormat,sourceMarketplace.getKey());
		this.dao.updateReportAsImported(info.getSettlementId());
		return "Imported.";
	}
	
	@Override
	public String importFile(String marketplaceId,String fullPath) {
		Path p = Paths.get(fullPath);
		try {
			byte[] data = Files.readAllBytes(p);
			return this.uploadFileAndImport(marketplaceId,data);
		} catch (IOException e) {
			e.printStackTrace();
			return "Encounter path problem.";
		}
	}

	@Override
	public void insertReportInfoForTest(String settlementId, String settlementStartDate, String settlementEndDate,String depositDate, String totalAmount, String currency, Marketplace sourceMarketplace) {
		this.dao.insertReportInfoAsImported(settlementId, settlementStartDate, settlementEndDate, depositDate, totalAmount, currency, sourceMarketplace.getKey());
	}

	@Override
	public void saveReportRawLine(Marketplace sourceMarketplace,List<AmazonSettlementReportRawLine> lines) {
		Assert.isTrue(!this.dao.settlementIdExist(lines.get(0).getSettlementId()));
		String dateFormat = this.getDateFormat(sourceMarketplace);
		String dateTimeFormat = this.getDateTimeFormat(sourceMarketplace);
		this.dao.insertReportRawLine(lines,dateFormat,dateTimeFormat,sourceMarketplace.getKey());
	}

}
