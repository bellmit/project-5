package com.kindminds.drs.service.usecase.report.amazon;

import com.kindminds.drs.api.data.access.usecase.report.amazon.ImportAmazonHeadlineSearchAdReportDao;
import com.kindminds.drs.api.usecase.report.amazon.ImportAmazonHeadlineSearchAdReportUco;
import com.kindminds.drs.api.v1.model.amazon.AmazonHeadlineSearchAdReportItem;
import com.kindminds.drs.persist.v1.model.mapping.amazon.AmazonHeadlineSearchAdReportItemImpl;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImportAmazonHeadlineSearchAdReportUcoImpl implements ImportAmazonHeadlineSearchAdReportUco {

    private static final String CAMPAIGN = "campaign";
    private static final String CAMPAIGN_VIDEO = "campaign_video";
    private static final String DISPLAY = "display";
    private static final String KEYWORD = "keyword";
    private static final String KEYWORD_VIDEO = "keyword_video";

    @Autowired
    private ImportAmazonHeadlineSearchAdReportDao dao;

    public enum KeywordCol{
        REPORTDATE(0),
        PORTFOLIO_NAME(1),
        CURRENCY(2),
        CAMPAIGNNAME(3),
        TARGETING(4),
        MATCHTYPE(5),
        IMPRESSIONS(6),
        CLICKS(7),
        CLICKCTHRURATE(8),
        COSTPERCLICK(9),
        SPEND(10),
        TOTALACOS(11),
        TOTALROAS(12),
        TOTALSALES14DAYS(13),
        TOTALORDERS14DAYS(14),
        TOTALUNITS14DAYS(15),
        CONVERSIONRATE14DAYS(16),
    	ORDERS_NEWTOBRAND(17), 
    	PCT_ORDERS_NEWTOBRAND(18), 
    	SALES_NEWTOBRAND(19),
		PCT_SALES_NEWTOBRAND(20), 
		UNITS_NEWTOBRAND(21), 
		PCT_UNITS_NEWTOBRAND(22),
		ORDERRATE_NEWTOBRAND(23);
        private int index;
        KeywordCol(int index){this.index=index;}
        public int getIndex(){return this.index;}
    }

    public enum CampaignCol{
        REPORTDATE(0),
        PORTFOLIO_NAME(1),
        CURRENCY(2),
        CAMPAIGNNAME(3),
        IMPRESSIONS(4),
        CLICKS(5),
        CLICKCTHRURATE(6),
        COSTPERCLICK(7),
        SPEND(8),
        TOTALACOS(9),
        TOTALROAS(10),
        TOTALSALES14DAYS(11),
        TOTALORDERS14DAYS(12),
        TOTALUNITS14DAYS(13),
        CONVERSIONRATE14DAYS(14),
    	ORDERS_NEWTOBRAND(15), 
    	PCT_ORDERS_NEWTOBRAND(16), 
    	SALES_NEWTOBRAND(17),
		PCT_SALES_NEWTOBRAND(18), 
		UNITS_NEWTOBRAND(19), 
		PCT_UNITS_NEWTOBRAND(20),
		ORDERRATE_NEWTOBRAND(21);
        private int index;
        CampaignCol(int index){this.index=index;}
        public int getIndex(){return this.index;}
    }

    public enum CampaignVideoCol{
        REPORTDATE(0),
        PORTFOLIO_NAME(1),
        CURRENCY(2),
        CAMPAIGNNAME(3),
        IMPRESSIONS(4),
        CLICKS(5),
        CLICKCTHRURATE(6),
        COSTPERCLICK(7),
        SPEND(8),
        TOTALACOS(9),
        TOTALROAS(10),
        TOTALSALES14DAYS(11),
        TOTALORDERS14DAYS(12),
        TOTALUNITS14DAYS(13),
        CONVERSIONRATE14DAYS(14),
        VIEWABLEIMPRESSIONS(15),
        VIEWTHRURATE(16),
        CLICKTHRURATEFORVIEWS(17),
        VIDEOFIRSTQUARTILEVIEWS(18),
        VIDEOMIDPOINTVIEWS(19),
        VIDEOTHIRDQUARTILEVIEWS(20),
        VIDEOCOMPLETEVIEWS(21),
        VIDEOUNMUTES(22),
        VIEWS5SECOND(23),
        VIEWRATE5SECOND(24);
        private int index;
        CampaignVideoCol(int index){this.index=index;}
        public int getIndex(){return this.index;}
    }

    public enum DisplayCol{
        REPORTDATE(0),
        STATUS(1),
        CURRENCY(2),
        BUDGET(3),
        CAMPAIGNNAME(4),
        PORTFOLIO_NAME(5),
        COSTTYPE(6),
        IMPRESSIONS(7),
        VIEWABLEIMPRESSIONS(8),
        CLICKS(9),
        CLICKCTHRURATE(10),
        DETAILPAGEVIEWS14DAYS(11),
        SPEND(12),
        COSTPERCLICK(13),
        TOTALACOS(14),
        COSTPER1000VIEWABLEIMPRESSIONS(15),
        TOTALROAS(16),
        TOTALORDERS14DAYS(17),
        TOTALUNITS14DAYS(18),
        TOTALSALES14DAYS(19),
        CONVERSIONRATE14DAYS(20),
        NEWTOBRANDORDERS14DAYS(21),
        NEWTOBRANDSALES14DAYS(22),
        NEWTOBRANDUNITS14DAYS(23);
        private int index;
        DisplayCol(int index){this.index=index;}
        public int getIndex(){return this.index;}
    }

    @Override
    public String importReport(int marketplaceId, byte[] fileBytes, String reportType) {

        InputStream fileInputStream = new ByteArrayInputStream(fileBytes);
        try (XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream)) {
            XSSFSheet sheet = workbook.getSheetAt(0);
            if (sheet == null) return "XSSFSheet sheet is null";
            if (sheet.getPhysicalNumberOfRows() < 2) return "num of rows less than 2.";

            List<AmazonHeadlineSearchAdReportItem> records = new ArrayList<>();
            XSSFRow firstRow = sheet.getRow(0);
            if (reportType.equals(KEYWORD) &&
                    (firstRow.getPhysicalNumberOfCells() != 24 ||
                    !firstRow.getCell(KeywordCol.IMPRESSIONS.getIndex()).toString().equals("Impressions"))) {
                return "Keyword report format does not match";

            } else if (reportType.equals(CAMPAIGN) &&
                    (firstRow.getPhysicalNumberOfCells() != 22 ||
                    !firstRow.getCell(CampaignCol.IMPRESSIONS.getIndex()).toString().equals("Impressions"))) {
                return "Campaign report format does not match";

            } else if (reportType.equals(KEYWORD_VIDEO) &&
                    (firstRow.getPhysicalNumberOfCells() != 17 ||
                    !firstRow.getCell(KeywordCol.IMPRESSIONS.getIndex()).toString().equals("Impressions"))) {
                return "Keyword Video report format does not match";

            } else if (reportType.equals(CAMPAIGN_VIDEO) &&
                    (firstRow.getPhysicalNumberOfCells() != 25 ||
                    !firstRow.getCell(CampaignCol.IMPRESSIONS.getIndex()).toString().equals("Impressions"))) {
                return "Campaign Video report format does not match";

            } else if (reportType.equals(DISPLAY) &&
                    (firstRow.getPhysicalNumberOfCells() != 24 ||
                    !firstRow.getCell(DisplayCol.IMPRESSIONS.getIndex()).toString().equals("Impressions"))) {
                return "Display report format does not match";
            }


            switch (reportType) {
                case KEYWORD:
                    for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                        AmazonHeadlineSearchAdReportItem item = new AmazonHeadlineSearchAdReportItemImpl(
                                marketplaceId,
                                sheet.getRow(i).getCell(KeywordCol.REPORTDATE.getIndex()).getDateCellValue(),
                                sheet.getRow(i).getCell(KeywordCol.PORTFOLIO_NAME.getIndex()).toString(),
                                sheet.getRow(i).getCell(KeywordCol.CURRENCY.getIndex()).toString(),
                                sheet.getRow(i).getCell(KeywordCol.CAMPAIGNNAME.getIndex()).toString(),
                                sheet.getRow(i).getCell(KeywordCol.TARGETING.getIndex()).toString(),
                                sheet.getRow(i).getCell(KeywordCol.MATCHTYPE.getIndex()).toString(),
                                sheet.getRow(i).getCell(KeywordCol.IMPRESSIONS.getIndex()).toString(),
                                sheet.getRow(i).getCell(KeywordCol.CLICKS.getIndex()).toString(),
                                sheet.getRow(i).getCell(KeywordCol.CLICKCTHRURATE.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(KeywordCol.COSTPERCLICK.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(KeywordCol.SPEND.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(KeywordCol.TOTALACOS.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(KeywordCol.TOTALROAS.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(KeywordCol.TOTALSALES14DAYS.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(KeywordCol.TOTALORDERS14DAYS.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(KeywordCol.TOTALUNITS14DAYS.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(KeywordCol.CONVERSIONRATE14DAYS.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(KeywordCol.ORDERS_NEWTOBRAND.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(KeywordCol.PCT_ORDERS_NEWTOBRAND.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(KeywordCol.SALES_NEWTOBRAND.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(KeywordCol.PCT_SALES_NEWTOBRAND.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(KeywordCol.UNITS_NEWTOBRAND.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(KeywordCol.PCT_UNITS_NEWTOBRAND.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(KeywordCol.ORDERRATE_NEWTOBRAND.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString()
                        );
                        records.add(item);
                    }
                    break;
                case CAMPAIGN:
                    for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                        AmazonHeadlineSearchAdReportItem item = new AmazonHeadlineSearchAdReportItemImpl(
                                marketplaceId,
                                sheet.getRow(i).getCell(CampaignCol.REPORTDATE.getIndex()).getDateCellValue(),
                                sheet.getRow(i).getCell(CampaignCol.PORTFOLIO_NAME.getIndex()).toString(),
                                sheet.getRow(i).getCell(CampaignCol.CURRENCY.getIndex()).toString(),
                                sheet.getRow(i).getCell(CampaignCol.CAMPAIGNNAME.getIndex()).toString(),
                                sheet.getRow(i).getCell(CampaignCol.IMPRESSIONS.getIndex()).toString(),
                                sheet.getRow(i).getCell(CampaignCol.CLICKS.getIndex()).toString(),
                                sheet.getRow(i).getCell(CampaignCol.CLICKCTHRURATE.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(CampaignCol.COSTPERCLICK.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(CampaignCol.SPEND.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(CampaignCol.TOTALACOS.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(CampaignCol.TOTALROAS.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(CampaignCol.TOTALSALES14DAYS.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(CampaignCol.TOTALORDERS14DAYS.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(CampaignCol.TOTALUNITS14DAYS.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(CampaignCol.CONVERSIONRATE14DAYS.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(CampaignCol.ORDERS_NEWTOBRAND.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(CampaignCol.PCT_ORDERS_NEWTOBRAND.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(CampaignCol.SALES_NEWTOBRAND.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(CampaignCol.PCT_SALES_NEWTOBRAND.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(CampaignCol.UNITS_NEWTOBRAND.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(CampaignCol.PCT_UNITS_NEWTOBRAND.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(CampaignCol.ORDERRATE_NEWTOBRAND.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString()
                        );
                        records.add(item);
                    }
                    break;
                case CAMPAIGN_VIDEO:
                    for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                        AmazonHeadlineSearchAdReportItem item = new AmazonHeadlineSearchAdReportItemImpl(
                                marketplaceId,
                                sheet.getRow(i).getCell(CampaignVideoCol.REPORTDATE.getIndex()).getDateCellValue(),
                                sheet.getRow(i).getCell(CampaignVideoCol.PORTFOLIO_NAME.getIndex()).toString(),
                                sheet.getRow(i).getCell(CampaignVideoCol.CURRENCY.getIndex()).toString(),
                                sheet.getRow(i).getCell(CampaignVideoCol.CAMPAIGNNAME.getIndex()).toString(),
                                sheet.getRow(i).getCell(CampaignVideoCol.IMPRESSIONS.getIndex()).toString(),
                                sheet.getRow(i).getCell(CampaignVideoCol.CLICKS.getIndex()).toString(),
                                sheet.getRow(i).getCell(CampaignVideoCol.CLICKCTHRURATE.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(CampaignVideoCol.COSTPERCLICK.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(CampaignVideoCol.SPEND.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(CampaignVideoCol.TOTALACOS.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(CampaignVideoCol.TOTALROAS.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(CampaignVideoCol.TOTALSALES14DAYS.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(CampaignVideoCol.TOTALORDERS14DAYS.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(CampaignVideoCol.TOTALUNITS14DAYS.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(CampaignVideoCol.CONVERSIONRATE14DAYS.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(CampaignVideoCol.VIEWABLEIMPRESSIONS.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(CampaignVideoCol.VIEWTHRURATE.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(CampaignVideoCol.CLICKTHRURATEFORVIEWS.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(CampaignVideoCol.VIDEOFIRSTQUARTILEVIEWS.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(CampaignVideoCol.VIDEOMIDPOINTVIEWS.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(CampaignVideoCol.VIDEOTHIRDQUARTILEVIEWS.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(CampaignVideoCol.VIDEOCOMPLETEVIEWS.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(CampaignVideoCol.VIDEOUNMUTES.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(CampaignVideoCol.VIEWS5SECOND.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(CampaignVideoCol.VIEWRATE5SECOND.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString()
                        );
                        records.add(item);
                    }
                    break;
                case KEYWORD_VIDEO:
                    for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                        AmazonHeadlineSearchAdReportItem item = new AmazonHeadlineSearchAdReportItemImpl(
                                marketplaceId,
                                sheet.getRow(i).getCell(KeywordCol.REPORTDATE.getIndex()).getDateCellValue(),
                                sheet.getRow(i).getCell(KeywordCol.PORTFOLIO_NAME.getIndex()).toString(),
                                sheet.getRow(i).getCell(KeywordCol.CURRENCY.getIndex()).toString(),
                                sheet.getRow(i).getCell(KeywordCol.CAMPAIGNNAME.getIndex()).toString(),
                                sheet.getRow(i).getCell(KeywordCol.TARGETING.getIndex()).toString(),
                                sheet.getRow(i).getCell(KeywordCol.MATCHTYPE.getIndex()).toString(),
                                sheet.getRow(i).getCell(KeywordCol.IMPRESSIONS.getIndex()).toString(),
                                sheet.getRow(i).getCell(KeywordCol.CLICKS.getIndex()).toString(),
                                sheet.getRow(i).getCell(KeywordCol.CLICKCTHRURATE.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(KeywordCol.COSTPERCLICK.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(KeywordCol.SPEND.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(KeywordCol.TOTALACOS.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(KeywordCol.TOTALROAS.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(KeywordCol.TOTALSALES14DAYS.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(KeywordCol.TOTALORDERS14DAYS.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(KeywordCol.TOTALUNITS14DAYS.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(KeywordCol.CONVERSIONRATE14DAYS.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString()
                        );
                        records.add(item);
                    }
                    break;
                case DISPLAY:
                    for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                        AmazonHeadlineSearchAdReportItem item = new AmazonHeadlineSearchAdReportItemImpl(
                                marketplaceId,
                                sheet.getRow(i).getCell(DisplayCol.REPORTDATE.getIndex()).getDateCellValue(),
                                sheet.getRow(i).getCell(DisplayCol.CURRENCY.getIndex()).toString(),
                                sheet.getRow(i).getCell(DisplayCol.CAMPAIGNNAME.getIndex()).toString(),
                                sheet.getRow(i).getCell(DisplayCol.IMPRESSIONS.getIndex()).toString(),
                                sheet.getRow(i).getCell(DisplayCol.CLICKS.getIndex()).toString(),
                                sheet.getRow(i).getCell(DisplayCol.CLICKCTHRURATE.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(DisplayCol.SPEND.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(DisplayCol.COSTPERCLICK.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(DisplayCol.TOTALACOS.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(DisplayCol.TOTALROAS.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(DisplayCol.TOTALORDERS14DAYS.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(DisplayCol.TOTALUNITS14DAYS.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(DisplayCol.TOTALSALES14DAYS.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString(),
                                sheet.getRow(i).getCell(DisplayCol.CONVERSIONRATE14DAYS.getIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK).toString()
                        );
                        records.add(item);
                    }
                    break;
            }


            if (records.isEmpty()) {
                return "No data imported from the file.";
            }
            
            int inserted = 0;
            switch (reportType) {
                case CAMPAIGN:
                    System.out.println("CAMPAIGN REPORT");
                    inserted = dao.insertCampaignRecord(records);
                    break;
                case CAMPAIGN_VIDEO:
                    System.out.println("CAMPAIGN VIDEO");
                    inserted = dao.insertCampaignVideoRecord(records);
                    break;
                case KEYWORD:
                    System.out.println("KEYWORD ");
                    inserted = dao.insertKeywordRecord(records);
                    break;
                case KEYWORD_VIDEO:
                    System.out.println("KEYWORD VIDEO");
                    inserted = dao.insertKeywordVideoRecord(records);
                    break;
                case DISPLAY:
                    System.out.println("DISPLAY REPORT");
                    inserted = dao.insertDisplayRecord(records);
                    break;
            }

            return inserted + " inserted.";
        } catch (IOException e) {
            e.printStackTrace();
            return "IOException occurred";
        }
    }

    @Override
    public int deleteReport(int marketplaceId, String reportDate, String reportType) {
        return dao.deleteByDateAndMarketplace(marketplaceId, reportDate, reportType);
    }

}