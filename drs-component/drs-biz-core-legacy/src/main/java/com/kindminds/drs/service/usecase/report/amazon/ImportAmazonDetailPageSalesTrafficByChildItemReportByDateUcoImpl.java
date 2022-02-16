package com.kindminds.drs.service.usecase.report.amazon;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.usecase.report.amazon.ImportAmazonDetailPageSalesTrafficByChildItemReportByDateUco;
import com.kindminds.drs.api.v1.model.amazon.AmazonDetailPageSalesTrafficByChildItemReportRawLine;
import com.kindminds.drs.api.data.access.usecase.report.amazon.ImportAmazonDetailPageSalesTrafficByChildItemReportByDateDao;
import com.kindminds.drs.service.helper.AmazonDetailPageSalesTrafficByChildItemReportHelper;
import com.kindminds.drs.util.DateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class ImportAmazonDetailPageSalesTrafficByChildItemReportByDateUcoImpl implements ImportAmazonDetailPageSalesTrafficByChildItemReportByDateUco {
	
	@Autowired private ImportAmazonDetailPageSalesTrafficByChildItemReportByDateDao dao;
	
	@Override
	public List<Marketplace> getMarketplaces() {
		return Marketplace.getAmazonMarketplaces();
	}

	@Override
	public String importFile(String marketplaceId, String utcDate, byte[] fileBytes) {
		if(StringUtils.isEmpty(marketplaceId)) return "Please select marketplace";
		Marketplace marketplace = Marketplace.fromKey(Integer.parseInt(marketplaceId));
		Date reportDate = DateHelper.toDate(utcDate+" UTC", "yyyy-MM-dd z");
		if(this.isImported(marketplace,reportDate)) return "Report from "+marketplace.getName()+" at "+utcDate+" has been imported.";
		try {
			AmazonDetailPageSalesTrafficByChildItemReportHelper reportHelper = new AmazonDetailPageSalesTrafficByChildItemReportHelper();
			List<AmazonDetailPageSalesTrafficByChildItemReportRawLine> rawLines = reportHelper.createReportRawLines(marketplace, fileBytes);
			Integer insertedLines = this.dao.insertReportLines(marketplace.getKey(),reportDate,rawLines);
			return insertedLines+" line(s) imported.";
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	@Override
	public String importECMFile(String marketplaceId, String utcDate, byte[] fileBytes) {
		if(StringUtils.isEmpty(marketplaceId)) return "Please select marketplace";
		Marketplace marketplace = Marketplace.fromKey(Integer.parseInt(marketplaceId));
		Date reportDate = DateHelper.toDate(utcDate+" UTC", "yyyy-MM-dd z");
		//if(this.isImported(marketplace,reportDate)) return "Report from "+marketplace.getName()+" at "+utcDate+" has been imported.";
		try {
			AmazonDetailPageSalesTrafficByChildItemReportHelper reportHelper =
					new AmazonDetailPageSalesTrafficByChildItemReportHelper();
			List<AmazonDetailPageSalesTrafficByChildItemReportRawLine> rawLines =
					reportHelper.createReportRawLines(marketplace, fileBytes);
			Integer insertedLines = this.dao.insertReportLines(marketplace.getKey(),reportDate,rawLines);
			return insertedLines+" line(s) imported.";
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	@Override
	public String importFile(String marketplaceId, String utcDate, String fullPath) {
		Path p = Paths.get(fullPath);
		try {
			byte[] data = Files.readAllBytes(p);
			return this.importFile(marketplaceId, utcDate, data);
		} catch (IOException e) {
			e.printStackTrace();
			return "Encounter path problem.";
		}
	}
	
	@Override
	public Map<String,Map<Marketplace,Boolean>> getImportStatus(String yearStr,String monthStr){
		int year = Integer.valueOf(yearStr);
		int month = Integer.valueOf(monthStr);
		Map<String,Map<Marketplace,Boolean>> importStatus = this.createEmptyStatus(year, month);
		Date startPoint = this.getBeginOfUtcYearMonth(year, month);
		Date endPoint = this.getEndOfUtcYearMonth(year, month);
		this.appendImportStatus(importStatus,startPoint,endPoint);
		return importStatus;
	}
	
	private void appendImportStatus(Map<String, Map<Marketplace, Boolean>> importStatus, Date startPoint, Date endPoint) {
		List<Object[]> importedReportDateMarketplaceIdList = this.dao.queryDistinctReportDateMarketplace(startPoint, endPoint);
		for(Object[] importedReportDateMarketplace:importedReportDateMarketplaceIdList){
			String date = (String)importedReportDateMarketplace[0];
			Integer marketplaceId = (Integer)importedReportDateMarketplace[1];
			Marketplace marketplace = Marketplace.fromKey(marketplaceId);
			Assert.isTrue(importStatus.containsKey(date));
			Assert.isTrue(importStatus.get(date).containsKey(marketplace));
			importStatus.get(date).put(marketplace, true);
		}
	}

	private Map<String,Map<Marketplace,Boolean>> createEmptyStatus(int year,int month){
		Map<String,Map<Marketplace,Boolean>> status = new LinkedHashMap<>();
		int zeroBasedMonth=month-1;
		Calendar calendar = new GregorianCalendar(year,zeroBasedMonth,1,0,0,0);
		List<Marketplace> availableMarketplaces = this.getMarketplaces();
		while(calendar.get(Calendar.MONTH)==zeroBasedMonth){
			int currentMonth = calendar.get(Calendar.MONTH)+1;
			String dateKey = year+"-"+String.format("%02d",currentMonth)+"-"+String.format("%02d",calendar.get(Calendar.DAY_OF_MONTH));
			status.put(dateKey,this.createStatusRowWithAllNonImportedRow(availableMarketplaces));
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		return status;
	}
	
	private Map<Marketplace,Boolean> createStatusRowWithAllNonImportedRow(List<Marketplace> availableMarketplaces){
		Map<Marketplace,Boolean> statusRow = new TreeMap<>();
		for(Marketplace marketplace:availableMarketplaces){
			statusRow.put(marketplace, false);
		}
		return statusRow;
	}
	
	private Date getBeginOfUtcYearMonth(int year,int month){
		return DateHelper.toDate(year+"-"+month+" UTC","yyyy-MM z");
	}
	
	private Date getEndOfUtcYearMonth(int year,int month){
		int actualYear = (month+1)>12?year+1:year;
		int actualMonth = (month+1)%12 ;
		if(actualMonth == 0)actualMonth = 12;
		return DateHelper.toDate(actualYear+"-"+actualMonth+" UTC","yyyy-MM z");
	}
	
	@Override
	public String delete(String utcDate,String marketplaceId){
		Date reportDate = DateHelper.toDate(utcDate+" UTC","yyyy-MM-dd z");
		Marketplace marketplace = Marketplace.fromKey(Integer.valueOf(marketplaceId));
		int deleteRows = this.dao.delete(reportDate,marketplace.getKey());
		return deleteRows + " rows has been deleted.";
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
	
	private boolean isImported(Marketplace marketplace, Date reportDate) {
		return this.dao.queryExist(marketplace.getKey(), reportDate);
	}

}
