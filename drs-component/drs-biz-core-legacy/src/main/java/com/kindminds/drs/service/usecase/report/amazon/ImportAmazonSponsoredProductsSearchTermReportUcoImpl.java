package com.kindminds.drs.service.usecase.report.amazon;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.usecase.report.amazon.ImportAmazonSponsoredProductsSearchTermReportUco;
import com.kindminds.drs.api.v1.model.amazon.AmazonSponsoredProductsSearchTermReportRawItem;
import com.kindminds.drs.api.data.access.rdb.CompanyDao;
import com.kindminds.drs.api.data.access.usecase.report.amazon.ImportAmazonSponsoredProductsSearchTermReportDao;
import com.kindminds.drs.v1.model.impl.AmazonSponsoredProductsSearchTermReportRawItemImpl;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.*;

@Service
public class ImportAmazonSponsoredProductsSearchTermReportUcoImpl implements ImportAmazonSponsoredProductsSearchTermReportUco{

	@Autowired private ImportAmazonSponsoredProductsSearchTermReportDao dao;
	@Autowired private CompanyDao companyRepo;
	
	private String dateFormat = "dd-MMM-yyyy Z";
	
	public enum AmazonSponsoredProductsSearchTermReportColumn{
		START_DATE(0),
		//END_DATE(1),
		PORTFOLIO_NAME(1),
		CURRENCY(2),
		CAMPAIGN_NAME(3),
		AD_GROUP_NAME(4),
		KEYWORD(5),
		MATCH_TYPE(6),
		CUSTOMER_SEARCH_TERM(7),
		IMPRESSIONS(8),
		CLICKS(9),
		CTR(10),
		CPC(11),
		SPEND(12),
		SEVEN_DAY_TOTAL_SALES(13),
		ACOS(14),
		ROAS(15),
		SEVEN_DAY_TOTAL_ORDERS(16),
		SEVEN_DAY_TOTAL_UNITS(17),
		SEVEN_DAY_CONVERSION_RATE(18),
		SEVEN_DAY_ADVERTISED_SKU_UNITS(19),
		SEVEN_DAY_OTHER_SKU_UNITS(20),
		SEVEN_DAY_ADVERTISED_SKU_SALES(21),
		SEVEN_DAY_OTHER_SKU_SALES(22);
		private int index;
		AmazonSponsoredProductsSearchTermReportColumn(int index){this.index=index;}
		public int getIndex(){return this.index;}
	}
		
	@Override
	public List<Marketplace> getMarketplaceList() {
		return Marketplace.getAmazonMarketplaces();
	}

	@Override
	public String importFile(String marketplaceKeyId, byte[] fileBytes) {
		try {
			Assert.isTrue(!StringUtils.isEmpty(marketplaceKeyId));
			Marketplace marketplace = Marketplace.fromKey(Integer.parseInt(marketplaceKeyId));

			List<AmazonSponsoredProductsSearchTermReportRawItem> rawLineList =
					this.createReportRawLines(marketplace,fileBytes , false);
			if(rawLineList.isEmpty()) return "Empty content.";

			Comparator<AmazonSponsoredProductsSearchTermReportRawItem> cmpSd =
					Comparator.comparing(AmazonSponsoredProductsSearchTermReportRawItem::getStartDate);

			Comparator<AmazonSponsoredProductsSearchTermReportRawItem> cmpEd =
					Comparator.comparing(AmazonSponsoredProductsSearchTermReportRawItem::getEndDate);

			AmazonSponsoredProductsSearchTermReportRawItem minSDateItem =
					Collections.min(rawLineList,cmpSd);

			AmazonSponsoredProductsSearchTermReportRawItem maxEDateItem =
					Collections.max(rawLineList,cmpEd);

			this.dao.deleteDCPExistingReportLineByDate(marketplace , minSDateItem.getStartDate() ,
					maxEDateItem.getEndDate() );

			int insertedRowCount = this.dao.importReportRawLines(marketplace,rawLineList);
			this.updateCampaignName(marketplace);
			return insertedRowCount+" line(s) processed";
		} catch (Exception e) {
			e.printStackTrace();
			return "There may be some problems with uploaded file, please try again.";
		}
	}

	@Override
	public String importECMFile(String marketplaceKeyId, byte[] fileBytes) {
		try {
			Assert.isTrue(!StringUtils.isEmpty(marketplaceKeyId));
			Marketplace marketplace = Marketplace.fromKey(Integer.parseInt(marketplaceKeyId));
			List<AmazonSponsoredProductsSearchTermReportRawItem> rawLineList =
					this.createReportRawLines(marketplace,fileBytes,false);
			if(rawLineList.isEmpty()) return "Empty content.";

			//todo kcode
			this.dao.deleteExistingReportLine(marketplace , "K621");

			int insertedRowCount = this.dao.importReportRawLines(marketplace,rawLineList);
			this.updateCampaignName(marketplace);
			return insertedRowCount+" line(s) processed";
		} catch (Exception e) {
			e.printStackTrace();
			return "There may be some problems with uploaded file, please try again.";
		}
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

	private void updateCampaignName(Marketplace marketplace){
		this.dao.deleteExistingCampaignNameSupplierMap(marketplace);
		List<String> campaignNames = this.dao.queryDistinctCampaignNameList(marketplace);
		Map<String,Integer> campaignNameToSupplierCompanyIdMap=this.createCampaignNameToSupplierCompanyIdMap(campaignNames);
		this.dao.insertCampaignNameList(marketplace,campaignNameToSupplierCompanyIdMap);
	}
	
	private Map<String,Integer> createCampaignNameToSupplierCompanyIdMap(List<String> campaignNames){
		Map<String,Integer> campaignNameToSupplierCompanyIdMap=new HashMap<>();
		for(String campaignName:campaignNames){
			Integer companyId=this.getSupplierCompanyIdFromCampaignName(campaignName);
			campaignNameToSupplierCompanyIdMap.put(campaignName,companyId);
		}
		return campaignNameToSupplierCompanyIdMap;
	}
	
	private Integer getSupplierCompanyIdFromCampaignName(String campaignName){
		String probablyKcode = this.getProbablyKcode(campaignName);
		return this.dao.queryCompanyId(probablyKcode.toUpperCase());
	}
	
	private String getProbablyKcode(String campaignName){
		String firstToken=this.getFirstTokenSplitBySpace(campaignName);
		return this.removeLeftSquareBracket(firstToken);
	}
	
	private String removeLeftSquareBracket(String token){
		if(token.startsWith("[")) return token.substring(1);
		return token;
	}
	
	private String getFirstTokenSplitBySpace(String fullString){
		return fullString.split("\\s+")[0];
	}
	
	@SuppressWarnings({ "resource" })
	private List<AmazonSponsoredProductsSearchTermReportRawItem>
			createReportRawLines(Marketplace marketplace,byte[] fileBytes , boolean isECM) throws IOException, ParseException{
		InputStream fileInputStream = new ByteArrayInputStream(fileBytes);		
		XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
		XSSFSheet sheet = workbook.getSheetAt(0);			
		List<AmazonSponsoredProductsSearchTermReportRawItem> lines = new ArrayList<AmazonSponsoredProductsSearchTermReportRawItem>();
		String timezoneText = "UTC";
		for(int i=0; i<sheet.getPhysicalNumberOfRows();i++){
			if(i==0) continue;
	    	else{
	    	this.checkCurrency(marketplace,sheet.getRow(i));	
	    	AmazonSponsoredProductsSearchTermReportRawItemImpl line  = new AmazonSponsoredProductsSearchTermReportRawItemImpl(
	    				this.dateFormat,
	    				timezoneText,
	    				sheet.getRow(i).getCell(AmazonSponsoredProductsSearchTermReportColumn.START_DATE.getIndex()).toString(),
	    				sheet.getRow(i).getCell(AmazonSponsoredProductsSearchTermReportColumn.START_DATE.getIndex()).toString(),
	    				sheet.getRow(i).getCell(AmazonSponsoredProductsSearchTermReportColumn.PORTFOLIO_NAME.getIndex()).toString(),
	    				sheet.getRow(i).getCell(AmazonSponsoredProductsSearchTermReportColumn.CURRENCY.getIndex()).toString(),
	    				this.processCampaignName(sheet.getRow(i).getCell(AmazonSponsoredProductsSearchTermReportColumn.CAMPAIGN_NAME.getIndex()).toString(),isECM),
	    				sheet.getRow(i).getCell(AmazonSponsoredProductsSearchTermReportColumn.AD_GROUP_NAME.getIndex()).toString(),
	    				sheet.getRow(i).getCell(AmazonSponsoredProductsSearchTermReportColumn.KEYWORD.getIndex()).toString(),
	    				sheet.getRow(i).getCell(AmazonSponsoredProductsSearchTermReportColumn.MATCH_TYPE.getIndex()).toString(),
	    				sheet.getRow(i).getCell(AmazonSponsoredProductsSearchTermReportColumn.CUSTOMER_SEARCH_TERM.getIndex()).toString(),
	    				sheet.getRow(i).getCell(AmazonSponsoredProductsSearchTermReportColumn.IMPRESSIONS.getIndex()).toString(),
	    				sheet.getRow(i).getCell(AmazonSponsoredProductsSearchTermReportColumn.CLICKS.getIndex()).toString(),
	    				sheet.getRow(i).getCell(AmazonSponsoredProductsSearchTermReportColumn.CTR.getIndex()) == null?"":sheet.getRow(i).getCell(AmazonSponsoredProductsSearchTermReportColumn.CTR.getIndex()).toString(),
	    				sheet.getRow(i).getCell(AmazonSponsoredProductsSearchTermReportColumn.CPC.getIndex()) == null?"":sheet.getRow(i).getCell(AmazonSponsoredProductsSearchTermReportColumn.CPC.getIndex()).toString(),
	    				sheet.getRow(i).getCell(AmazonSponsoredProductsSearchTermReportColumn.SPEND.getIndex()).toString(),
	    				sheet.getRow(i).getCell(AmazonSponsoredProductsSearchTermReportColumn.SEVEN_DAY_TOTAL_SALES.getIndex()).toString(),
	    				sheet.getRow(i).getCell(AmazonSponsoredProductsSearchTermReportColumn.ACOS.getIndex()) == null?"":sheet.getRow(i).getCell(AmazonSponsoredProductsSearchTermReportColumn.ACOS.getIndex()).toString(),
	    				sheet.getRow(i).getCell(AmazonSponsoredProductsSearchTermReportColumn.SEVEN_DAY_TOTAL_ORDERS.getIndex()).toString(),
	    				sheet.getRow(i).getCell(AmazonSponsoredProductsSearchTermReportColumn.SEVEN_DAY_CONVERSION_RATE.getIndex()) == null?"":sheet.getRow(i).getCell(AmazonSponsoredProductsSearchTermReportColumn.SEVEN_DAY_CONVERSION_RATE.getIndex()).toString(),
	    				sheet.getRow(i).getCell(AmazonSponsoredProductsSearchTermReportColumn.SEVEN_DAY_ADVERTISED_SKU_UNITS.getIndex()).toString(),
	    				sheet.getRow(i).getCell(AmazonSponsoredProductsSearchTermReportColumn.SEVEN_DAY_OTHER_SKU_UNITS.getIndex()).toString(),
	    				sheet.getRow(i).getCell(AmazonSponsoredProductsSearchTermReportColumn.SEVEN_DAY_ADVERTISED_SKU_SALES.getIndex()).toString(),
	    				sheet.getRow(i).getCell(AmazonSponsoredProductsSearchTermReportColumn.SEVEN_DAY_OTHER_SKU_SALES.getIndex()).toString()										
	    			);
	    			lines.add(line);
	    	}							
		}			
		return lines;
	}

	private String processCampaignName(String name , boolean isECM){

		if(isECM){

			name = "[K621 SecuX Tech] " + name;
		}

		return name;
	}
	
	private void checkCurrency(Marketplace marketplace,XSSFRow row){
		Assert.isTrue(marketplace.getCurrency().name().equals(
				row.getCell(AmazonSponsoredProductsSearchTermReportColumn.CURRENCY.getIndex()).getStringCellValue()),"Marketplace currency and report currency mismatch.");
	}
	
	@Override
	public Map<String, String> getCampaignNameToSupplierKcodeMap(Marketplace marketplace) {
		return this.dao.queryCampaignNameToSupplierKcodeMap(marketplace);
	}

	@Override
	public Map<String, String> getSupplierKcodeNameMap() {
		return this.companyRepo.querySupplierKcodeToShortEnUsNameMap();
	}

	@Override
	public Map<String, String> getSupplierKcodeToShortEnUsNameWithRetailMap() {
		return this.companyRepo.querySupplierKcodeToShortEnUsNameWithRetailMap();
	}

	@Override
	public void updateCampaignNameSupplierMap(Marketplace marketplace, Map<String, String> campaignNameSupplierMap) {
		this.dao.clearBelongedSupplierOfCampaignName(marketplace);
		Map<String,String> mapWithoutEmptySupplier = this.removeMapEntryWithEmptySupplier(campaignNameSupplierMap);
		for (Map.Entry<String,String> entry : mapWithoutEmptySupplier.entrySet()) {
		    String campaignName = entry.getKey();
		    String supplierKcode = entry.getValue();
		    this.dao.updateSupplierOfCampaignName(marketplace,campaignName,supplierKcode);
		}		
	}
	
	private Map<String,String> removeMapEntryWithEmptySupplier(Map<String,String> campaignNameSupplierMap){
		for(Iterator<Map.Entry<String,String>> it = campaignNameSupplierMap.entrySet().iterator(); it.hasNext(); ) {
			Map.Entry<String,String> entry = it.next();
			if(!StringUtils.hasText(entry.getValue())) {
				it.remove();
			}
		}
		return campaignNameSupplierMap;
	}
	
}
