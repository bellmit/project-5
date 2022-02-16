package com.kindminds.drs.data.pipelines.core.inventory;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.data.pipelines.core.inventory.dal.ManageFbaInventoryReportDao;
import com.kindminds.drs.api.usecase.report.amazon.ManageFbaInventoryFileImporter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ManageFbaInventoryRptImporterImpl implements ManageFbaInventoryFileImporter {

    private ManageFbaInventoryReportDao dao = new ManageFbaInventoryReportDao();


    private Map<String, Integer> headerNames;



    public String importFbaInventoryFile (byte[] fileData, String fileName) {
        int insertCount = 0;
        String message = "";

        headerNames = getHeaderNameMap(fileData);
        if (headerNames.size() > 21 || headerNames.size() < 18) {
            message += "Possible report format change detected. <br>";
        }

        if (!headerNames.containsKey("fnsku") ||
                !headerNames.containsKey("asin") ||
                !headerNames.containsKey("mfn-listing-exists") ||
                !headerNames.containsKey("afn-listing-exists") ||
                !headerNames.containsKey("sku")) {
//			for (String header : headerNames.keySet()) {
//				System.out.println(header);
//			}
            return message + "Format Error: Required header missing. <br>";
        }

        System.out.println("fileName: " + fileName);
        System.out.println("countryCode: " + fileName.substring(19, 21));
        System.out.println("date: " + fileName.substring(22));

        Date snapshotDate = null;
        try {
            snapshotDate = new SimpleDateFormat("yyyyMMdd").parse(fileName.substring(22));
        } catch (ParseException e) {
            e.printStackTrace();
            return "Parse date exception: " + fileName.substring(22);
        }
        String region;
        String countryCode = fileName.substring(19, 21);
        if (countryCode.equals("US") || countryCode.equals("CA")){
             region ="NA";
        }else if(countryCode.equals("UK")){
             region ="UK";
        }else{
             region  = "EU";
        }
        Integer marketplaceId = Marketplace.getIdFromCountry(countryCode);

        if (dao.queryDataExists(snapshotDate, countryCode)) {
//            System.out.println("Data for this date already exists. snapshotDate: " + snapshotDate
//                    + ", countryCode: " + countryCode );
            return "Data for this date already exists. snapshotDate: " + snapshotDate
                    + ", countryCode: " + countryCode ;
        }

        Map<MarketplaceIdSku, String> marketplaceIdSkuMap = dao.queryDrsSkuByMarketplaceSku();
        if (marketplaceIdSkuMap == null) {
            return "Error: MarketplaceId SKU map is null";
        }

        for(CSVRecord record : getRecordsFromFile(fileData)){

            String fnsku = record.get(headerNames.get("fnsku"));
            String marketplaceSku = record.get(headerNames.get("sku"));

            String codeByDrs = marketplaceIdSkuMap.get(new MarketplaceIdSku(marketplaceId, marketplaceSku));
            if (codeByDrs == null) {
//                System.out.println("not found : " + marketplaceId + ", " + marketplaceSku);
                continue;
            }
//            System.out.println("codeBYdrs: " + codeByDrs);

            ManageFbaInventoryReportItem item = new ManageFbaInventoryReportItemImpl(
                    snapshotDate,
                    countryCode,
                    marketplaceSku,
                    fnsku,
                    record.get(headerNames.get("asin")),
                    record.get(headerNames.get("product-name")),
                    record.get(headerNames.get("condition")),
                    parseBigDecimal(record.get(headerNames.get("your-price"))),
                    parseBool(record.get(headerNames.get("mfn-listing-exists"))),
                    parseInteger(record.get(headerNames.get("mfn-fulfillable-quantity"))),
                    parseBool(record.get(headerNames.get("afn-listing-exists"))),
                    parseInteger(record.get(headerNames.get("afn-warehouse-quantity"))),
                    parseInteger(record.get(headerNames.get("afn-fulfillable-quantity"))),
                    parseInteger(record.get(headerNames.get("afn-unsellable-quantity"))),
                    parseInteger(record.get(headerNames.get("afn-reserved-quantity"))),
                    parseInteger(record.get(headerNames.get("afn-total-quantity"))),
                    parseBigDecimal(record.get(headerNames.get("per-unit-volume"))),
                    parseInteger(record.get(headerNames.get("afn-inbound-working-quantity"))),
                    parseInteger(record.get(headerNames.get("afn-inbound-shipped-quantity"))),
                    parseInteger(record.get(headerNames.get("afn-inbound-receiving-quantity"))),
                    parseOptionalField("afn-researching-quantity", record),
                    parseOptionalField("afn-reserved-future-supply", record),
                    parseOptionalField("afn-future-supply-buyable", record),
                    region,
                    codeByDrs
            );

            insertCount += dao.insertManageFbaInventoryItem(item);

        }
        return message + insertCount + " record(s) inserted. ";
    }


    private BigDecimal parseBigDecimal(String value) {

        return StringUtils.hasText(value) ?
                new BigDecimal(value) : null;
    }

    private Integer parseInteger(String value) {

        BigDecimal decimalVal = parseBigDecimal(value);

        return decimalVal == null ?
                null : decimalVal.intValueExact();
    }

    private Integer parseOptionalField(String field, CSVRecord record) {
        return headerNames.containsKey(field) ?
                parseInteger(record.get(headerNames.get(field))) : null;
    }

    private Boolean parseBool(String value) {

        return StringUtils.hasText(value) && BooleanUtils.toBoolean(value);
    }


    private Map<String, Integer> getHeaderNameMap(byte[] fileData) {
        try {
            CSVParser parser = createParser(fileData);
            Map<String, Integer> headerNames = parser.getHeaderMap();
            parser.close();
            return headerNames;
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    private List<CSVRecord> getRecordsFromFile(byte[] fileData) {
        try {
            CSVParser parser = createParser(fileData);
            List<CSVRecord> records = parser.getRecords();
            parser.close();
            return records;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private CSVParser createParser(byte[] fileData) throws IOException {
        Reader reader = new InputStreamReader(
                new BOMInputStream(new ByteArrayInputStream(fileData)));

        return CSVParser.parse(reader, CSVFormat.TDF.withFirstRecordAsHeader());
    }

}