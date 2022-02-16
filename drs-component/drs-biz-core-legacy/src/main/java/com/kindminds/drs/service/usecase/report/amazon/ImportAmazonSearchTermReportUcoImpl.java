package com.kindminds.drs.service.usecase.report.amazon;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.usecase.report.amazon.ImportAmazonSearchTermReportUco;
import com.kindminds.drs.api.v1.model.amazon.AmazonSearchTermReportRawLine;
import com.kindminds.drs.api.data.access.rdb.CompanyDao;
import com.kindminds.drs.api.data.access.usecase.report.amazon.ImportAmazonSearchTermReportDao;
import com.kindminds.drs.v1.model.impl.AmazonSearchTermReportRawLineImpl;
import com.kindminds.drs.util.BigDecimalHelper;
import com.kindminds.drs.util.DateHelper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.*;

@Service
public class ImportAmazonSearchTermReportUcoImpl implements ImportAmazonSearchTermReportUco {
	
	@Autowired private ImportAmazonSearchTermReportDao dao;
	@Autowired private CompanyDao companyRepo;
	
	public enum AmazonSearchTermReportColumn{
		CAMPAIGN_NAME(0),
		AD_GROUP_NAME(1),
		CUSTOMER_SEARCH_TERM(2),
		KEYWORD(3),
		MATCH_TYPE(4),
		FIRST_DAY_OF_IMPRESSION(5),
		LAST_DAY_OF_IMPRESSION(6),
		IMPRESSIONS(7),
		CLICKS(8),
		CTR(9),
		TOTAL_SPEND(10),
		AVERAGE_CPC(11),
		ACOS(12),
		CURRENCY(13),
		ORDERS_PLACED_WITHIN_ONE_WEEK_OF_A_CLICK(14),
		PRODUCT_SALES_WITHIN_ONE_WEEK_OF_A_CLICK(15),
		CONVERSION_RATE_WITHIN_ONE_WEEK_OF_A_CLICK(16),
		SAME_SKU_UNITS_ORDERED_WITHIN_ONE_WEEK_OF_CLICK(17),
		OTHER_SKU_UNITS_ORDERED_WITHIN_ONE_WEEK_OF_CLICK(18),
		SAME_SKU_UNITS_PRODUCT_SALES_WITHIN_ONE_WEEK_OF_CLICK(19),
		OTHER_SKU_UNITS_PRODUCT_SALES_WITHIN_ONE_WEEK_OF_CLICK(20);
		private int index;
		AmazonSearchTermReportColumn(int index){this.index=index;}
		public int getIndex(){return this.index;}
	}

	@Override
	public List<Marketplace> getMarketplaceList() {
		return Marketplace.getAmazonMarketplaces();
	}
	
	@Override
	public String importFile(String marketplaceKeyId,byte[] fileBytes) {
		try {
			Assert.isTrue(!StringUtils.isEmpty(marketplaceKeyId));
			Marketplace marketplace = Marketplace.fromKey(Integer.parseInt(marketplaceKeyId));
			String reportDateFormat = this.getReportDateFormat(marketplace);
			List<AmazonSearchTermReportRawLine> rawLineList =
					this.createReportRawLines(marketplace,reportDateFormat,fileBytes , false);
			if(rawLineList.isEmpty()) return "Empty content.";
			this.dao.deleteDCPExistingReportLine(marketplace);
			int insertedRowCount = this.dao.importReportRawLines(marketplace,rawLineList);
			this.updateCampaignName(marketplace);
			return insertedRowCount+" line(s) processed";
		} catch (Exception e) {
			e.printStackTrace();
			return "There may be some problems with uploaded file, please try again.";
		}
	}

	@Override
	public String importECMFile(String marketplaceKeyId,byte[] fileBytes) {
		try {
			Assert.isTrue(!StringUtils.isEmpty(marketplaceKeyId));
			Marketplace marketplace = Marketplace.fromKey(Integer.parseInt(marketplaceKeyId));
			String reportDateFormat = this.getReportDateFormat(marketplace);
			List<AmazonSearchTermReportRawLine> rawLineList =
					this.createReportRawLines(marketplace,reportDateFormat,fileBytes , true);
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
		Map<String,Integer> campaignNameToSupplierCompanyIdMap=
				this.createCampaignNameToSupplierCompanyIdMap(campaignNames);
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
		return this.dao.queryCompanyId(probablyKcode);
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
	
	private String getReportDateFormat(Marketplace marketplace){
		String dateFormat = null;
		if(marketplace==Marketplace.AMAZON_COM) dateFormat = "MM/dd/yyyy z";
		if(marketplace==Marketplace.AMAZON_CO_UK) dateFormat = "dd/MM/yyyy z";
		if(marketplace==Marketplace.AMAZON_CA) dateFormat = "MM/dd/yyyy z";
		if(marketplace==Marketplace.AMAZON_DE) dateFormat = "dd/MM/yyyy z";
		if(marketplace==Marketplace.AMAZON_FR) dateFormat = "dd/MM/yyyy z";
		if(marketplace==Marketplace.AMAZON_ES) dateFormat = "dd/MM/yyyy z";
		if(marketplace==Marketplace.AMAZON_IT) dateFormat = "dd/MM/yyyy z";
		Assert.notNull(dateFormat,"Invalid Marketplace: "+ marketplace.getName());
		return dateFormat;
	}
	
	private List<AmazonSearchTermReportRawLine>
		createReportRawLines(Marketplace marketplace, String reportDateFormat,
							 byte[] fileBytes  , boolean isECM) throws IOException, ParseException{
		Reader reader = new StringReader(new String(fileBytes));
		Iterable<CSVRecord> records = CSVFormat.TDF.withHeader().parse(reader);
		List<AmazonSearchTermReportRawLine> lines = new ArrayList<AmazonSearchTermReportRawLine>();
		for(CSVRecord record:records){
			this.assertAllColumnsAreNotEmpty(record);
			Date firstDayOfImpression = DateHelper.toDate(record.get(AmazonSearchTermReportColumn.FIRST_DAY_OF_IMPRESSION.getIndex())+" UTC",reportDateFormat); 
			Date lastDayOfImpression = DateHelper.toDate(record.get(AmazonSearchTermReportColumn.LAST_DAY_OF_IMPRESSION.getIndex())+" UTC", reportDateFormat);
			BigDecimal totalSpend = BigDecimalHelper.generateBigDecimal(marketplace.getLocale(),record.get(AmazonSearchTermReportColumn.TOTAL_SPEND.getIndex()));
			BigDecimal averageCpc = BigDecimalHelper.generateBigDecimal(marketplace.getLocale(),record.get(AmazonSearchTermReportColumn.AVERAGE_CPC.getIndex()));
			BigDecimal productSalesWithinOneWeekOfAClick = BigDecimalHelper.generateBigDecimal(marketplace.getLocale(),record.get(AmazonSearchTermReportColumn.PRODUCT_SALES_WITHIN_ONE_WEEK_OF_A_CLICK.getIndex()));
			BigDecimal sameSkuUnitsProductSalesWithinOneWeekOfClick = BigDecimalHelper.generateBigDecimal(marketplace.getLocale(),record.get(AmazonSearchTermReportColumn.SAME_SKU_UNITS_PRODUCT_SALES_WITHIN_ONE_WEEK_OF_CLICK.getIndex()));
			BigDecimal otherSkuUnitsProductSalesWithinOneWeekOfClick = BigDecimalHelper.generateBigDecimal(marketplace.getLocale(),record.get(AmazonSearchTermReportColumn.OTHER_SKU_UNITS_PRODUCT_SALES_WITHIN_ONE_WEEK_OF_CLICK.getIndex()));
			Assert.isTrue(record.get(AmazonSearchTermReportColumn.CURRENCY.getIndex()).equals(marketplace.getCurrency().name()));
			AmazonSearchTermReportRawLineImpl line = new AmazonSearchTermReportRawLineImpl(
					this.processCampaignName(record.get(AmazonSearchTermReportColumn.CAMPAIGN_NAME.getIndex()),isECM),
					record.get(AmazonSearchTermReportColumn.AD_GROUP_NAME.getIndex()),
					record.get(AmazonSearchTermReportColumn.CUSTOMER_SEARCH_TERM.getIndex()),
					record.get(AmazonSearchTermReportColumn.KEYWORD.getIndex()),
					record.get(AmazonSearchTermReportColumn.MATCH_TYPE.getIndex()),
					firstDayOfImpression,
					lastDayOfImpression,
					record.get(AmazonSearchTermReportColumn.IMPRESSIONS.getIndex()),
					record.get(AmazonSearchTermReportColumn.CLICKS.getIndex()),
					record.get(AmazonSearchTermReportColumn.CTR.getIndex()),
					totalSpend,
					averageCpc,
					record.get(AmazonSearchTermReportColumn.ACOS.getIndex()),
					record.get(AmazonSearchTermReportColumn.CURRENCY.getIndex()),
					record.get(AmazonSearchTermReportColumn.ORDERS_PLACED_WITHIN_ONE_WEEK_OF_A_CLICK.getIndex()),
					productSalesWithinOneWeekOfAClick,
					record.get(AmazonSearchTermReportColumn.CONVERSION_RATE_WITHIN_ONE_WEEK_OF_A_CLICK.getIndex()),
					record.get(AmazonSearchTermReportColumn.SAME_SKU_UNITS_ORDERED_WITHIN_ONE_WEEK_OF_CLICK.getIndex()),
					record.get(AmazonSearchTermReportColumn.OTHER_SKU_UNITS_ORDERED_WITHIN_ONE_WEEK_OF_CLICK.getIndex()),
					sameSkuUnitsProductSalesWithinOneWeekOfClick,
					otherSkuUnitsProductSalesWithinOneWeekOfClick);
			lines.add(line);
		}
		return lines;
	}

	private String processCampaignName(String name , boolean isECM){

		if(isECM){

			name = "[K621 SecuX Tech] " + name;
		}

		return name;
	}
	
	private void assertAllColumnsAreNotEmpty(CSVRecord record) {
		for(AmazonSearchTermReportColumn column: AmazonSearchTermReportColumn.values()){
			Assert.isTrue(StringUtils.hasText(record.get(column.getIndex())));
		}
	}

	@Override
	public Map<String,String> getCampaignNameToSupplierKcodeMap(Marketplace marketplace) {
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
	public void updateCampaignNameSupplierMap(Marketplace marketplace,Map<String,String> campaignNameSupplierMap) {
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
