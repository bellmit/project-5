package com.kindminds.drs.service.usecase.report.amazon;


import com.kindminds.drs.Marketplace;

import com.kindminds.drs.api.data.access.usecase.report.amazon.ImportAmazonDateRangeReportDao;
import com.kindminds.drs.api.usecase.report.amazon.ImportAmazonDateRangeReportUco;
import com.kindminds.drs.api.v1.model.report.DateRangeImportStatus;
import com.kindminds.drs.api.v1.model.report.DateRangeReportItem;
import com.kindminds.drs.v1.model.impl.report.DateRangeImportStatusImpl;
import com.kindminds.drs.v1.model.impl.report.DateRangeReportItemImpl;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.*;

@Service
public class ImportAmazonDateRangeReportUcoImpl implements ImportAmazonDateRangeReportUco {

    @Autowired
    private ImportAmazonDateRangeReportDao dao;

//    public enum columnUS{
//        DATE_TIME(0),
//        SETTLEMENT_ID(1),
//        TYPE(2),
//        ORDER_ID(3),
//        SKU(4),
//        DESCRIPTION(5),
//        QUANTITY(6),
//        MARKETPLACE(7),
//        FULFILLMENT(8),
//        ORDER_CITY(9),
//        ORDER_STATE(10),
//        ORDER_POSTAL(11),
//        TAX_COLLECTION_MODEL(12),
//        PRODUCT_SALES(13),
//        PRODUCT_SALES_TAX(14),
//        SHIPPING_CREDITS(15),
//        SHIPPING_CREDITS_TAX(16),
//        GIFT_WRAP_CREDITS(17),
//        GIFTWRAP_CREDITS_TAX(18),
//        PROMOTIONAL_REBATES(19),
//        PROMOTIONAL_REBATES_TAX(20),
//        MARKETPLACE_WITHHELD_TAX(21),
//        SELLING_FEES(22),
//        FBA_FEES(23),
//        OTHER_TRANSACTION_FEES(24),
//        OTHER(25),
//        TOTAL(26);
//        private int index;
//        columnUS(int index){this.index=index;}
//        public int getIndex(){return this.index;}
//    }
//
//    public enum columnCA{
//        DATE_TIME(0),
//        SETTLEMENT_ID(1),
//        TYPE(2),
//        ORDER_ID(3),
//        SKU(4),
//        DESCRIPTION(5),
//        QUANTITY(6),
//        MARKETPLACE(7),
//        FULFILLMENT(8),
//        ORDER_CITY(9),
//        ORDER_STATE(10),
//        ORDER_POSTAL(11),
//        TAX_COLLECTION_MODEL(12),
//        PRODUCT_SALES(13),
//        PRODUCT_SALES_TAX(14),
//        SHIPPING_CREDITS(15),
//        SHIPPING_CREDITS_TAX(16),
//        GIFT_WRAP_CREDITS(17),
//        GIFTWRAP_CREDITS_TAX(18),
//        PROMOTIONAL_REBATES(19),
//        PROMOTIONAL_REBATES_TAX(20),
//        SELLING_FEES(21),
//        FBA_FEES(22),
//        OTHER_TRANSACTION_FEES(23),
//        OTHER(24),
//        TOTAL(25);
//        private int index;
//        columnCA(int index){this.index=index;}
//        public int getIndex(){return this.index;}
//    }
//
//    public enum columnEU{
//        DATE_TIME(0),
//        SETTLEMENT_ID(1),
//        TYPE(2),
//        ORDER_ID(3),
//        SKU(4),
//        DESCRIPTION(5),
//        QUANTITY(6),
//        MARKETPLACE(7),
//        FULFILLMENT(8),
//        ORDER_CITY(9),
//        ORDER_STATE(10),
//        ORDER_POSTAL(11),
//        PRODUCT_SALES(12),
//        SHIPPING_CREDITS(13),
//        GIFT_WRAP_CREDITS(14),
//        PROMOTIONAL_REBATES(15),
//        SELLING_FEES(16),
//        FBA_FEES(17),
//        OTHER_TRANSACTION_FEES(18),
//        OTHER(19),
//        TOTAL(20);
//        private int index;
//        columnEU(int index){this.index=index;}
//        public int getIndex(){return this.index;}
//    }

    private static final int NUM_COLUMNS_US = 28;
    private static final int NUM_COLUMNS_CA = 27;
    private static final int NUM_COLUMNS_EU = 27;
    private static final int NUM_COLUMNS_MX = 27;

    private static Map<Integer, Integer> marketplaceColumnsMap = initializeMPCol();
    private static Map<Integer, Integer> initializeMPCol() {
        Map<Integer, Integer> columnsByMarketplace = new HashMap<>();
        columnsByMarketplace.put(1, NUM_COLUMNS_US);	//US
        columnsByMarketplace.put(4, NUM_COLUMNS_EU);	//UK
        columnsByMarketplace.put(5, NUM_COLUMNS_CA);	//CA
        columnsByMarketplace.put(6, NUM_COLUMNS_EU);	//DE
        columnsByMarketplace.put(7, NUM_COLUMNS_EU);	//FR
        columnsByMarketplace.put(8, NUM_COLUMNS_EU);	//IT
        columnsByMarketplace.put(9, NUM_COLUMNS_EU);	//ES
        columnsByMarketplace.put(16, NUM_COLUMNS_MX);	//MX
        return columnsByMarketplace;
    }

    @Override
    public List<Marketplace> getMarketplaces() {
        return Marketplace.getAmazonMarketplaces();
    }

    @Override
    public List<DateRangeImportStatus> getImportStatus() {
        List<Object []> results = dao.queryImportStatus();

        List<DateRangeImportStatus> importStatus = new ArrayList<>();
        for (Object[] result : results) {
            importStatus.add(new DateRangeImportStatusImpl(
                    Marketplace.fromKey((Integer)result[0]).getName(),
                    (Date) result[1], (Date) result[2]));
        }
        return importStatus;
    }

    @Override
    public String importReport(int marketplaceId, byte[] fileBytes) {
        List<CSVRecord> records = getRecordsFromFile(fileBytes);

        //check number of columns for report format
        if (records.get(7).size() != marketplaceColumnsMap.get(marketplaceId)) {
            return "Report format error. numColumns: " + records.get(7).size();
        }

        //remove header rows
        for (; records.size() > 0; ) {
            int numColumns = records.get(0).size();
            records.remove(0);
            if (numColumns == marketplaceColumnsMap.get(marketplaceId)) break;

        }
        List<DateRangeReportItem> itemList = new ArrayList<>();
        for (CSVRecord record : records) {
            DateRangeReportItem item = new DateRangeReportItemImpl(marketplaceId, record);
            itemList.add(item);
        }
        if (itemList.isEmpty()) return "There is no data found to import.";
        int deleted = dao.deleteExistingEntriesByMarketplace(marketplaceId, itemList.get(0).getDateTime());
        int inserted = dao.insertRecord(itemList);
        return deleted + " records deleted.<br>" + inserted + " record(s) inserted. ";
    }

    private static List<CSVRecord> getRecordsFromFile(byte[] fileData) {
        try {
            Reader reader = new StringReader(new String(fileData));
            CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT);
            List<CSVRecord> records = parser.getRecords();
            parser.close();
            return records;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<CSVRecord>();
        }
    }

}
