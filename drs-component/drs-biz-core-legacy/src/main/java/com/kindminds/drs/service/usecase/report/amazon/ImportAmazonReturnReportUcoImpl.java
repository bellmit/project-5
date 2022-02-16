package com.kindminds.drs.service.usecase.report.amazon;

import com.kindminds.drs.api.adapter.FileAdapter;
import com.kindminds.drs.api.usecase.report.amazon.ImportAmazonReturnReportUco;
import com.kindminds.drs.api.v1.model.amazon.AmazonReturnReportRawLine;
import com.kindminds.drs.api.data.access.usecase.report.amazon.ImportAmazonReturnReportDao;
import com.kindminds.drs.v1.model.impl.AmazonReturnReportRawLineImpl;
import com.kindminds.drs.util.DateHelper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ImportAmazonReturnReportUcoImpl implements ImportAmazonReturnReportUco {
	
	@Autowired private ImportAmazonReturnReportDao dao;
	@Autowired private FileAdapter fileAdapter;
	
	private String folderPath = "amazon_us_return_report";

	@Override
	public String save(String fileName, byte[] bytes) {
		this.fileAdapter.save(this.folderPath, fileName, bytes);
		return "File "+fileName+" has been uploaded.";
	}

	@Override
	public List<String> getFileList() {
		return this.fileAdapter.getFileList(this.folderPath);
	}

	@Override
	public String delete(String fileName) {
		this.fileAdapter.delete(this.folderPath, fileName);
		return "File "+fileName+" has been deleted.";
	}

	@Override
	public String importFile(String fileName) {
		Reader reader = this.getFileReader(fileName);
		return this.importFile(reader);
	}
	
	private Reader getFileReader(String fileName) {
		try {
			File file = this.fileAdapter.get(this.folderPath, fileName);
			Reader fileReader = new FileReader(file);
			return fileReader;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public String importFileByFullPath(String fullPath){
		Path p = Paths.get(fullPath);
		try {
			byte[] data = Files.readAllBytes(p);
			Reader reader = new StringReader(new String(data));
			return this.importFile(reader);
		} catch (IOException e) {
			e.printStackTrace();
			return "Encounter path problem.";
		}
	}
	
	@Override
	public String importFile(byte [] bytes){
		Reader reader = new StringReader(new String(bytes));
		return this.importFile(reader);
	}
	
	private String importFile(Reader reader){
		try {
			Map<String,Date> fulfillmentCenterIdToLatestReturnEventDateMap = this.dao.queryFulfillmentCenterIdToLatestReturnEventDateMap();
			List<AmazonReturnReportRawLine> lineList = this.createAmazonReturnReportRawLineList(reader);
			if(fulfillmentCenterIdToLatestReturnEventDateMap!=null) this.removeOutOfDateLine(fulfillmentCenterIdToLatestReturnEventDateMap, lineList);
			this.makeListOrderByReturnDateAsc(lineList);
			int insertedRows = this.dao.insertLineTimes(lineList);
			return insertedRows + " new Line(s) inserted at "+LocalDateTime.now().withNano(0);
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	private void makeListOrderByReturnDateAsc(List<AmazonReturnReportRawLine> lineList){
		Collections.reverse(lineList);
	}
	
	private void removeOutOfDateLine(Map<String,Date> fulfillmentCenterIdToLatestReturnEventDateMap,List<AmazonReturnReportRawLine> lineList){
		Assert.notNull(fulfillmentCenterIdToLatestReturnEventDateMap);
		Iterator<AmazonReturnReportRawLine> i = lineList.iterator();
		boolean hasOutOfDateLine = false;		
		while(i.hasNext()){
			AmazonReturnReportRawLine line = i.next();
			Date latestReturnEventDateOfCurrentFulfillmentCenter = fulfillmentCenterIdToLatestReturnEventDateMap.get(line.getFulfillmentCenterId());
			if(latestReturnEventDateOfCurrentFulfillmentCenter==null) continue;
			if(!line.getReturnDate().after(latestReturnEventDateOfCurrentFulfillmentCenter)){
				i.remove();
				hasOutOfDateLine = true;
			}
		}
		Assert.isTrue(hasOutOfDateLine,"No out of date data means that data might be interrupted, or not continuous, regenerate report and import again.");
	}
	
	private List<AmazonReturnReportRawLine> createAmazonReturnReportRawLineList(Reader reader) throws IOException{
		List<AmazonReturnReportRawLine> lines = new ArrayList<AmazonReturnReportRawLine>();
		Iterable<CSVRecord> records = CSVFormat.TDF.withHeader().withQuote(null).parse(reader);
		for(CSVRecord record:records){
			this.assertAllColumnsAreWithValue(record);
			AmazonReturnReportRawLineImpl line = new AmazonReturnReportRawLineImpl(
					this.generateDate(record.get(AmazonReturnReportColumn.RETURN_DATE.getName())),
					record.get(AmazonReturnReportColumn.ORDER_ID.getName()),
					record.get(AmazonReturnReportColumn.SKU.getName()),
					record.get(AmazonReturnReportColumn.ASIN.getName()),
					record.get(AmazonReturnReportColumn.FNSKU.getName()),
					record.get(AmazonReturnReportColumn.PRODUCT_NAME.getName()),
					Integer.valueOf(record.get(AmazonReturnReportColumn.QUANTITY.getName())),
					record.get(AmazonReturnReportColumn.FULFILLMENT_CENTER_ID.getName()),
					record.get(AmazonReturnReportColumn.DETAILED_DISPOSITION.getName()),
					record.get(AmazonReturnReportColumn.REASON.getName()),
					record.isSet(AmazonReturnReportColumn.STATUS.getName())?record.get(AmazonReturnReportColumn.STATUS.getName()):null,
					StringUtils.hasText(record.get(AmazonReturnReportColumn.LICENSE_PLATE_NUMBER.getName()))?record.get(AmazonReturnReportColumn.LICENSE_PLATE_NUMBER.getName()):null,
					StringUtils.hasText(record.get(AmazonReturnReportColumn.CUSTOMER_COMMENTS.getName()))?record.get(AmazonReturnReportColumn.CUSTOMER_COMMENTS.getName()):null
			);
			lines.add(line);
		}
		return lines;
	}
	
	private void assertAllColumnsAreWithValue(CSVRecord record){
		for(AmazonReturnReportColumn column:AmazonReturnReportColumn.values()){
			if(column==AmazonReturnReportColumn.STATUS) continue;               //TODO: Remove because this is temporary for import UK old Data.
			if(column==AmazonReturnReportColumn.LICENSE_PLATE_NUMBER) continue; //TODO: Remove because this is temporary for import UK old Data.
			if(column==AmazonReturnReportColumn.CUSTOMER_COMMENTS) continue;    //TODO: Remove because this is temporary for import UK old Data.
			Assert.isTrue(StringUtils.hasText(record.get(column.getName())));
		}
	}

	private Date generateDate(String returnDateStr){
		/*
		ORIGINAL REPORT DATETIME FORMAT: 2016-03-31T19:39:02-07:00
		ORIGINAL REPORT DATETIME FORMAT: 2016-03-31T19:39:02-08:00
		ORIGINAL REPORT DATETIME FORMAT: 2016-03-31T19:39:02+01:00
		ORIGINAL REPORT DATETIME FORMAT: 2016-03-31T19:39:02+00:00
		*/		
		String parsableStr = null;
		if(returnDateStr.substring(19).equals("-07:00"))parsableStr = returnDateStr.replaceAll("\\-07:00", "\\-0700");
		if(returnDateStr.substring(19).equals("-08:00"))parsableStr = returnDateStr.replaceAll("\\-08:00", "\\-0800");
		if(returnDateStr.substring(19).equals("+01:00"))parsableStr = returnDateStr.replaceAll("\\+01:00", "\\+0100");		
		if(returnDateStr.substring(19).equals("+00:00"))parsableStr = returnDateStr.replaceAll("\\+00:00", "\\+0000");
		if(returnDateStr.substring(19).equals("+02:00"))parsableStr = returnDateStr.replaceAll("\\+02:00", "\\+0200");
		if(returnDateStr.substring(19).equals("+04:00"))parsableStr = returnDateStr.replaceAll("\\+04:00", "\\+0400");
		if(returnDateStr.substring(19).equals("+05:30"))parsableStr = returnDateStr.replaceAll("\\+05:30", "\\+0530");
		String dateTimeFormat = "yyyy-MM-dd'T'HH:mm:ssZ";
		return DateHelper.toDate(parsableStr,dateTimeFormat);
	}

}
