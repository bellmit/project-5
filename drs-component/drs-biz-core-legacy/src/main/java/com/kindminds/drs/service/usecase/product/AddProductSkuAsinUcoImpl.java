package com.kindminds.drs.service.usecase.product;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.usecase.AddProductSkuAsinUco;
import com.kindminds.drs.v1.model.impl.product.SkuFnskuAsinImpl;
import com.kindminds.drs.v1.model.impl.product.SkuWithoutAsinImpl;
import com.kindminds.drs.api.v1.model.product.SkuFnskuAsin;
import com.kindminds.drs.api.v1.model.product.SkuWithoutAsin;
import com.kindminds.drs.api.data.access.rdb.CompanyDao;
import com.kindminds.drs.api.data.access.usecase.product.AddProductSkuAsinDao;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class AddProductSkuAsinUcoImpl implements AddProductSkuAsinUco {
	
	@Autowired private AddProductSkuAsinDao dao;
	@Autowired private CompanyDao companyRepo;

	private static final String FBA = "FBA";
	private static final String INVENTORY = "INVENTORY";

	
	@Override
	public List<Marketplace> getMarketplaces(){
		return Marketplace.getAmazonMarketplaces();
	}
	
	@Override
	public Map<String,String> getKcodeToSupplierName(){
		return this.companyRepo.querySupplierKcodeToShortEnUsNameMap();
	}

	@Override
	public List<SkuFnskuAsin> getSkuToAsin(String marketplaceIdText, String splrKcode) {
		Assert.isTrue(StringUtils.hasText(marketplaceIdText));
		Marketplace marketplace = Marketplace.fromKey(Integer.parseInt(marketplaceIdText));
		Assert.isTrue(marketplace.isAmazonMarketplace());
		List<Object []> columnsList=  this.dao.querySkuAsins(splrKcode, marketplace.getKey());

		List<SkuFnskuAsin> skuAsins = new ArrayList<>();
		for(Object[] columns:columnsList){
			String sku = (String)columns[0];
			String marketplaceSku = (String)columns[1];
			String asin = (String)columns[2];
			String fnsku = (String)columns[3];
			Boolean afn = (Boolean)columns[4];
			Boolean mfn = (Boolean)columns[5];
			Boolean storage = (Boolean)columns[6];
			skuAsins.add(new SkuFnskuAsinImpl(sku, marketplaceSku, fnsku, asin, afn, mfn, storage));
		}

		return  skuAsins;
	}
	
	@Override
	public List<SkuWithoutAsin> getMarketplaceToSku() {
		List<Object []> columnsList =  this.dao.queryMarketplaceSkuWithoutAsin();

		List<SkuWithoutAsin> infoList =  new ArrayList<SkuWithoutAsin>();
		for(Object[] columns:columnsList){
			infoList.add(new SkuWithoutAsinImpl((String)columns[0],(String)columns[1]));
		}

		return infoList;


	}
	
	@Override
	public String addFbaData(byte[] fileData, int marketplaceId) {
		int insertCount = 0;
		int updateCount = 0;
		int skuIndex;
		int fnskuIndex;
		int asinIndex;
		int mfnIndex;
		int afnIndex;

		Map<String, Integer> headerNames = getHeaderNameMap(fileData, FBA);

		if (!headerNames.containsKey("fnsku") ||
				!headerNames.containsKey("asin") ||
				!headerNames.containsKey("mfn-listing-exists") ||
				!headerNames.containsKey("afn-listing-exists") ||
				!headerNames.containsKey("sku")) {
//			for (String header : headerNames.keySet()) {
//				System.out.println(header);
//			}
			return "Format Error: Required header missing. <br>";
		}

		skuIndex = headerNames.get("sku");
		fnskuIndex = headerNames.get("fnsku");
		asinIndex = headerNames.get("asin");
		mfnIndex = headerNames.get("mfn-listing-exists");
		afnIndex = headerNames.get("afn-listing-exists");

		for(CSVRecord record : getRecordsFromFile(fileData, FBA)){
			if ("Yes".equalsIgnoreCase(record.get(afnIndex)) || "No".equalsIgnoreCase(record.get(afnIndex))) {
				String marketplaceSku = record.get(skuIndex);
				String fnsku = record.get(fnskuIndex);
				String asin = record.get(asinIndex);
				String afn = record.get(afnIndex);
				String mfn = record.get(mfnIndex);
				if (!dao.queryPmiIdExist(marketplaceId, marketplaceSku)) {
					if (dao.insertFbaRecord(marketplaceId, marketplaceSku, asin, fnsku, afn, mfn) == 1) insertCount++;
				} else {
					if (dao.updateFbaRecord(marketplaceId, marketplaceSku, asin, fnsku, afn, mfn) == 1) updateCount++;
				}
			}
		}
		return insertCount + " record(s) inserted. " + updateCount +" ASIN(s) updated.";
	}

	private Map<String, Integer> getHeaderNameMap(byte[] fileData, String format) {
		try {
			CSVParser parser = createParser(fileData, format);
			Map<String, Integer> headerNames = parser.getHeaderMap();
			parser.close();
			return headerNames;
		} catch (IOException e) {
			e.printStackTrace();
			return new HashMap<>();
		}
	}

	private List<CSVRecord> getRecordsFromFile(byte[] fileData, String format) {
		try {
			CSVParser parser = createParser(fileData, format);
			List<CSVRecord> records = parser.getRecords();
			parser.close();
			return records;
		} catch (IOException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	private CSVParser createParser(byte[] fileData, String format) throws IOException {
		Reader reader = new InputStreamReader(
				new BOMInputStream(new ByteArrayInputStream(fileData)));

		return CSVParser.parse(reader, CSVFormat.TDF.withFirstRecordAsHeader());
	}
	
	@Override
	public String addInventoryData(byte[] fileData, int marketplaceId) {
		int insertCount = 0;
		int skuIndex;
		int asinIndex;
		String result = "";

		Map<String, Integer> headerNames = getHeaderNameMap(fileData, INVENTORY);

		if (!headerNames.containsKey("sku") ||
				!headerNames.containsKey("asin")) {
			for (String header : headerNames.keySet()) {
				System.out.println(header);
			}
			return "Format Error: Required header missing. <br>";
		}
		skuIndex = headerNames.get("sku");
		asinIndex = headerNames.get("asin");

		for(CSVRecord record : getRecordsFromFile(fileData, INVENTORY)){

			String marketplaceSku = record.get(skuIndex);
			String asin = record.get(asinIndex);
			if (!dao.queryPmiIdExist(marketplaceId, marketplaceSku)) {
				if (dao.insertInventoryRecord(marketplaceId, marketplaceSku, asin) == 1) {
					result += "(marketplaceId: " + marketplaceId + ", " + marketplaceSku + " " + asin + ") ";
					insertCount++;
				}
			}
		}
		return result + insertCount + " record(s) inserted. ";
	}

	@Override
	public void toggleStorageFeeFlag(int marketplaceId, String marketplaceSku) {
		dao.toggleStorageFeeFlag(marketplaceId, marketplaceSku);
	}

}
