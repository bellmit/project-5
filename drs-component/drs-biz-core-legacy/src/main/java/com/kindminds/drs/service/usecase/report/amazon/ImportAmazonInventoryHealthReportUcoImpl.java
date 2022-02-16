package com.kindminds.drs.service.usecase.report.amazon;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.usecase.report.amazon.ImportAmazonInventoryHealthReportUco;
import com.kindminds.drs.api.v1.model.amazon.AmazonInventoryHealthReportImportInfo;
import com.kindminds.drs.api.v1.model.amazon.AmazonInventoryHealthReportLineItem;
import com.kindminds.drs.api.data.access.usecase.report.amazon.ImportAmazonInventoryHealthReportDao;
import com.kindminds.drs.v1.model.impl.amazon.AmazonInventoryHealthReportImportInfoImpl;
import com.kindminds.drs.v1.model.impl.amazon.report.AmazonInventoryHealthReportLineItemImpl;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ImportAmazonInventoryHealthReportUcoImpl implements ImportAmazonInventoryHealthReportUco {
	
	@Autowired private ImportAmazonInventoryHealthReportDao dao;
	
	enum ReportColumn{
		SNAPSHOT_DATE(0),
		SKU(1),
		FNSKU(2),
		ASIN(3),
		PRODUCT_NAME(4),
		CONDITION(5),
		SALES_RANK(6),
		PRODUCT_GROUP(7),
		TOTAL_QUANTITY(8),
		SELLABLE_QUANTITY(9),
		UNSELLABLE_QUANTITY(10),
		INV_AGE_0_TO_90_DAYS(11),
		INV_AGE_91_TO_180_DAYS(12),
		INV_AGE_181_TO_270_DAYS(13),
		INV_AGE_271_TO_365_DAYS(14),
		INV_AGE_365_PLUS_DAYS(15),
		UNITS_SHIPPED_LAST_24_HRS(16),
		UNITS_SHIPPED_LAST_7_DAYS(17),
		UNITS_SHIPPED_LAST_30_DAYS(18),
		UNITS_SHIPPED_LAST_90_DAYS(19),
		UNITS_SHIPPED_LAST_180_DAYS(20),
		UNITS_SHIPPED_LAST_365_DAYS(21),
		WEEKS_OF_COVER_T7(22),
		WEEKS_OF_COVER_T30(23),
		WEEKS_OF_COVER_T90(24),
		WEEKS_OF_COVER_T180(25),
		WEEKS_OF_COVER_T365(26),
		NUM_AFN_NEW_SELLERS(27),
		NUM_AFN_USED_SELLERS(28),
		CURRENCY(29),
		YOUR_PRICE(30),
		SALES_PRICE(31),
		LOWEST_AFN_NEW_PRICE(32),
		LOWEST_AFN_USED_PRICE(33),
		LOWEST_MFN_NEW_PRICE(34),
		LOWEST_MFN_USED_PRICE(35),
		QTY_TO_BE_CHARGED_LTSF_12_MO(36),
		QTY_IN_LONG_TERM_STORAGE_PROGRAM(37),
		QTY_WITH_REMOVALS_IN_PROGRESS(38),
		PROJECTED_LTSF_12_MO(39),
		PER_UNIT_VOLUME(40),
		IS_HAZMAT(41),
		IN_BOUND_QUANTITY(42),
		ASIN_LIMIT(43),
		INBOUND_RECOMMEND_QUANTITY(44),
		QTY_TO_BE_CHARGED_LTSF_6_MO(45),
		PROJECTED_LTSF_6_MO(46);
		private int index;
		ReportColumn(int index){ this.index = index; }
		public int getIndex(){ return this.index; }
	}

	@Override
	public List<Marketplace> getMarketplaceList() {
		List<Marketplace> marketplaces = new ArrayList<>();
		marketplaces.add(Marketplace.AMAZON_COM);
		marketplaces.add(Marketplace.AMAZON_CO_UK);
		marketplaces.add(Marketplace.AMAZON_CA);
		marketplaces.add(Marketplace.AMAZON_DE);
		return marketplaces;
	}

	@Override
	public String importReport(String marketplaceId, byte[] fileBytes) {
		try {
			Assert.isTrue(!StringUtils.isEmpty(marketplaceId));
			Marketplace marketplace = Marketplace.fromKey(Integer.parseInt(marketplaceId));
			String dateFormat = "yyyy-MM-dd'T'HH:mm:ssX";
			List<AmazonInventoryHealthReportLineItem> lineItems = this.createLineItems(marketplace,dateFormat,fileBytes);
			this.dao.deleteLineItems(marketplace.getKey());
			this.dao.insertLineItems(marketplace.getKey(),lineItems);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private List<AmazonInventoryHealthReportLineItem> createLineItems(Marketplace marketplace, String dateFormat,byte[] fileBytes) throws IOException {
		Reader reader = new StringReader(new String(fileBytes));
		Iterable<CSVRecord> records = CSVFormat.TDF.withHeader().parse(reader);
		List<AmazonInventoryHealthReportLineItem> lineItems = new ArrayList<>();
		for(CSVRecord record:records){
			String currency = this.generateString(record.get(ReportColumn.CURRENCY.getIndex()));
//			Assert.isTrue(currency.equals(marketplace.getCurrency().name()),"Currency mismatch");
			if(currency.equals(marketplace.getCurrency().name())){
				lineItems.add(new AmazonInventoryHealthReportLineItemImpl(
						DateHelper.toDate(record.get(ReportColumn.SNAPSHOT_DATE.getIndex()),dateFormat),
						this.generateString(record.get(ReportColumn.SKU.getIndex())),
						this.generateString(record.get(ReportColumn.FNSKU.getIndex())),
						this.generateString(record.get(ReportColumn.ASIN.getIndex())),
						this.generateString(record.get(ReportColumn.PRODUCT_NAME.getIndex())),
						this.generateString(record.get(ReportColumn.CONDITION.getIndex())),
						this.generateInteger(record.get(ReportColumn.SALES_RANK.getIndex())),
						this.generateString(record.get(ReportColumn.PRODUCT_GROUP.getIndex())),
						this.generateInteger(record.get(ReportColumn.TOTAL_QUANTITY.getIndex())),
						this.generateInteger(record.get(ReportColumn.SELLABLE_QUANTITY.getIndex())),
						this.generateInteger(record.get(ReportColumn.UNSELLABLE_QUANTITY.getIndex())),
						this.generateInteger(record.get(ReportColumn.INV_AGE_0_TO_90_DAYS.getIndex())),
						this.generateInteger(record.get(ReportColumn.INV_AGE_91_TO_180_DAYS.getIndex())),
						this.generateInteger(record.get(ReportColumn.INV_AGE_181_TO_270_DAYS.getIndex())),
						this.generateInteger(record.get(ReportColumn.INV_AGE_271_TO_365_DAYS.getIndex())),
						this.generateInteger(record.get(ReportColumn.INV_AGE_365_PLUS_DAYS.getIndex())),
						this.generateInteger(record.get(ReportColumn.UNITS_SHIPPED_LAST_24_HRS.getIndex())),
						this.generateInteger(record.get(ReportColumn.UNITS_SHIPPED_LAST_7_DAYS.getIndex())),
						this.generateInteger(record.get(ReportColumn.UNITS_SHIPPED_LAST_30_DAYS.getIndex())),
						this.generateInteger(record.get(ReportColumn.UNITS_SHIPPED_LAST_90_DAYS.getIndex())),
						this.generateInteger(record.get(ReportColumn.UNITS_SHIPPED_LAST_180_DAYS.getIndex())),
						this.generateInteger(record.get(ReportColumn.UNITS_SHIPPED_LAST_365_DAYS.getIndex())),
						this.generateBigDecimal(record.get(ReportColumn.WEEKS_OF_COVER_T7.getIndex())),
						this.generateBigDecimal(record.get(ReportColumn.WEEKS_OF_COVER_T30.getIndex())),
						this.generateBigDecimal(record.get(ReportColumn.WEEKS_OF_COVER_T90.getIndex())),
						this.generateBigDecimal(record.get(ReportColumn.WEEKS_OF_COVER_T180.getIndex())),
						this.generateBigDecimal(record.get(ReportColumn.WEEKS_OF_COVER_T365.getIndex())),
						this.generateInteger(record.get(ReportColumn.NUM_AFN_NEW_SELLERS.getIndex())),
						this.generateInteger(record.get(ReportColumn.NUM_AFN_USED_SELLERS.getIndex())),
						currency,
						new BigDecimal(record.get(ReportColumn.YOUR_PRICE.getIndex())),
						new BigDecimal(record.get(ReportColumn.SALES_PRICE.getIndex())),
						new BigDecimal(record.get(ReportColumn.LOWEST_AFN_NEW_PRICE.getIndex())),
						new BigDecimal(record.get(ReportColumn.LOWEST_AFN_USED_PRICE.getIndex())),
						new BigDecimal(record.get(ReportColumn.LOWEST_MFN_NEW_PRICE.getIndex())),
						new BigDecimal(record.get(ReportColumn.LOWEST_MFN_USED_PRICE.getIndex())),
						this.generateInteger(record.get(ReportColumn.QTY_TO_BE_CHARGED_LTSF_12_MO.getIndex())),
						this.generateInteger(record.get(ReportColumn.QTY_IN_LONG_TERM_STORAGE_PROGRAM.getIndex())),
						this.generateInteger(record.get(ReportColumn.QTY_WITH_REMOVALS_IN_PROGRESS.getIndex())),
						new BigDecimal(record.get(ReportColumn.PROJECTED_LTSF_12_MO.getIndex())),
						new BigDecimal(record.get(ReportColumn.PER_UNIT_VOLUME.getIndex())),
						this.generateString(record.get(ReportColumn.IS_HAZMAT.getIndex())),
						this.generateInteger(record.get(ReportColumn.IN_BOUND_QUANTITY.getIndex())),
						this.generateString(record.get(ReportColumn.ASIN_LIMIT.getIndex())),
						this.generateInteger(record.get(ReportColumn.INBOUND_RECOMMEND_QUANTITY.getIndex())),
						this.generateInteger(record.get(ReportColumn.QTY_TO_BE_CHARGED_LTSF_6_MO.getIndex())),
						new BigDecimal(record.get(ReportColumn.PROJECTED_LTSF_6_MO.getIndex()))));
			}
		}

		return lineItems;
	}
	
	private String generateString(String value){
		if(StringUtils.hasText(value)) return value;
		return null;
	}
	
	private Integer generateInteger(String value){
	    try { 
	        return Integer.parseInt(value); 
	    } catch(Exception e) { 
	        return null; 
	    }
	}
	
	private BigDecimal generateBigDecimal(String value) {
		try {
			return new BigDecimal(value);
		}catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<AmazonInventoryHealthReportImportInfo> getImportStatuses() {
		List<Object []> columnsList = this.dao.queryImportInfoList();

		List<AmazonInventoryHealthReportImportInfo> infoList = new ArrayList<>();
		for(Object[] columns:columnsList){
			int marketplaceId = (Integer)columns[0];
			Date snapshotDate = (Date)columns[1];
			infoList.add(new AmazonInventoryHealthReportImportInfoImpl(marketplaceId, snapshotDate));
		}
		return infoList;
	}

}
