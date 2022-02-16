package com.kindminds.drs.service.helper;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.v1.model.amazon.AmazonDetailPageSalesTrafficByChildItemReportRawLine;
import com.kindminds.drs.v1.model.impl.AmazonDetailPageSalesTrafficByChildItemReportRawLineImpl;
import com.kindminds.drs.util.IntegerHelper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class AmazonDetailPageSalesTrafficByChildItemReportHelper {
	
	enum ReportColumn{
		PARENT_ASIN("(Parent) ASIN"),
		CHILD_ASIN("(Child) ASIN"),
		TITLE("Title"),
		SESSIONS("Sessions"),
		SESSION_PERCENTAGE("Session percentage"),
		PAGE_VIEWS("Page views"),
		PAGE_VIEWS_PERCENTAGE("Page views percentage"),
		BUY_BOX_PERCENTAGE("Featured Offer (Buy Box) Percentage"),
		UNITS_ORDERED("Units ordered"),
		UNIT_SESSION_PERCENTAGE("Unit session percentage"),
		ORDERED_PRODUCT_SALES("Ordered product sales"),
		TOTAL_ORDER_ITEMS(("Total order items"));
		private String name = null;
		ReportColumn(String name){this.name=name;}
		public String getName(){return this.name;}
	}
	
	public List<AmazonDetailPageSalesTrafficByChildItemReportRawLine> createReportRawLines(Marketplace marketplace,byte[] fileBytes) throws IOException, ParseException{
		String fileStringWithouUTF8BOM = this.removeUTF8BOM(new String(fileBytes));
		Reader reader = new StringReader(fileStringWithouUTF8BOM);
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader().withIgnoreHeaderCase().parse(reader);
		List<AmazonDetailPageSalesTrafficByChildItemReportRawLine> lines = new ArrayList<AmazonDetailPageSalesTrafficByChildItemReportRawLine>();
		for(CSVRecord record:records){
			this.assertNecessaryColumnsAreNotEmpty(record);
			Integer sessions = Integer.parseInt(record.get(ReportColumn.SESSIONS.getName()).replace(",", ""));
			Integer pageViews = Integer.parseInt(record.get(ReportColumn.PAGE_VIEWS.getName()).replace(",", ""));
			Integer unitsOrdered = IntegerHelper.generateInteger(marketplace.getLocale(),record.get(ReportColumn.UNITS_ORDERED.getName()));
			String rawOrderedProductSales = this.removeMoneyPrefix(marketplace,record.get(ReportColumn.ORDERED_PRODUCT_SALES.getName()));
			BigDecimal orderedProductSales = this.generateBigDecimal(rawOrderedProductSales);
			Integer totalOrderItems = IntegerHelper.generateInteger(marketplace.getLocale(),record.get(ReportColumn.TOTAL_ORDER_ITEMS.getName()));
			AmazonDetailPageSalesTrafficByChildItemReportRawLineImpl line = new AmazonDetailPageSalesTrafficByChildItemReportRawLineImpl(
					record.get(ReportColumn.PARENT_ASIN.getName()),
					record.get(ReportColumn.CHILD_ASIN.getName()),
					record.get(ReportColumn.TITLE.getName()),
					sessions,
					record.get(ReportColumn.SESSION_PERCENTAGE.getName()),
					pageViews,
					record.get(ReportColumn.PAGE_VIEWS_PERCENTAGE.getName()),
					record.get(ReportColumn.BUY_BOX_PERCENTAGE.getName()),
					unitsOrdered,
					record.get(ReportColumn.UNIT_SESSION_PERCENTAGE.getName()),
					orderedProductSales,
					totalOrderItems);
			lines.add(line);
		}
		return lines;
	}
	
	private String removeUTF8BOM(String s) {
		String UTF8_BOM = "\uFEFF";
	    return s.startsWith(UTF8_BOM)?s.substring(1):s;
	}
	
	private void assertNecessaryColumnsAreNotEmpty(CSVRecord record) {
		for(ReportColumn column: ReportColumn.values()){
			if(column== ReportColumn.TITLE) continue;
			if(column== ReportColumn.PARENT_ASIN) continue;
			Assert.isTrue(StringUtils.hasText(record.get(column.getName())));
		}
	}
	
	private String removeMoneyPrefix(Marketplace marketplace, String money) {
		String moneyPrefix = this.getMoneyPrefix(marketplace);
		Assert.isTrue(money.startsWith(moneyPrefix),"Report currency and marketplace currency mismatch");
		// This is for that dollar sign conflicts with regular expression operator
		String moneyPreficRegex = moneyPrefix.contains("$")? moneyPrefix.replace("$", "\\$"):moneyPrefix;
		String result = money.replaceFirst(moneyPreficRegex,"");
		Assert.isTrue(result.length()<money.length());
		return result;
	}
	
	private String getMoneyPrefix(Marketplace marketplace){
		if(marketplace==Marketplace.AMAZON_COM)   return "$";
		if(marketplace==Marketplace.AMAZON_CO_UK) return "£";
		if(marketplace==Marketplace.AMAZON_CA)    return "$";
		if(marketplace==Marketplace.AMAZON_DE)    return "€";
		if(marketplace==Marketplace.AMAZON_FR)    return "€";
		if(marketplace==Marketplace.AMAZON_ES)    return "€";
		if(marketplace==Marketplace.AMAZON_IT)    return "€";
		if(marketplace==Marketplace.AMAZON_COM_MX)   return "$";
		Assert.isTrue(false,"Invalid money format");
		return null;
	}
	
	private BigDecimal generateBigDecimal(String valueWithComma){
		Assert.isTrue(valueWithComma.contains("."));
		return new BigDecimal(valueWithComma.replace(",", ""));
	}
	
}
