package com.kindminds.drs.service.usecase.report.shopify;

import com.kindminds.drs.api.adapter.FileAdapter;
import com.kindminds.drs.api.usecase.report.shopify.ImportShopifyReportUco;
import com.kindminds.drs.api.v1.model.shopify.ShopifyOrderReportRawLine;
import com.kindminds.drs.api.v1.model.shopify.ShopifyPaymentTransactionReportRawLine;
import com.kindminds.drs.api.v1.model.shopify.ShopifySalesReportRawLine;
import com.kindminds.drs.api.data.access.usecase.report.shopify.ImportShopifyReportDao;
import com.kindminds.drs.v1.model.impl.ShopifyOrderReportRawLineImpl;
import com.kindminds.drs.v1.model.impl.ShopifyPaymentTransactionReportRawLineImpl;
import com.kindminds.drs.v1.model.impl.ShopifySalesReportRawLineImpl;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class ImportShopifyReportUcoImpl implements ImportShopifyReportUco {

	private enum ShopifyReportType{
		ORDER("order"),
		PAYMENT_TRANSACTION("payment_transaction"),
		SALES("sales");
		private String reportFileSubPath;
		ShopifyReportType(String subDirectory){ this.reportFileSubPath = subDirectory; }
		public String getReportFileSubPath(){return this.reportFileSubPath;}
	}

	@Autowired private ImportShopifyReportDao dao;
	@Autowired private FileAdapter fileAdapter;

	private final String reportFileRootPath = "shopify_report";

	private final List<String> necessaryColumnNameListForPaymentTransactionReport = Arrays.asList(
			ShopifyPaymentTransactionReportColumn.TRANSACTION_DATE.getName(),
			ShopifyPaymentTransactionReportColumn.TYPE.getName(),
			ShopifyPaymentTransactionReportColumn.ORDER.getName(),
			ShopifyPaymentTransactionReportColumn.CARD_BRAND.getName(),
			ShopifyPaymentTransactionReportColumn.CARD_SOURCE.getName(),
			ShopifyPaymentTransactionReportColumn.PAYOUT_STATUS.getName(),
			ShopifyPaymentTransactionReportColumn.PAYOUT_DATE.getName(),
			ShopifyPaymentTransactionReportColumn.AMOUNT.getName(),
			ShopifyPaymentTransactionReportColumn.FEE.getName(),
			ShopifyPaymentTransactionReportColumn.NET.getName());

	private final List<String> necessaryColumnNameListForOrderReport = Arrays.asList(
			ShopifyOrderReportColumn.NAME.getName());

	private final List<String> necessaryColumnNameListForSalesReport = Arrays.asList(
			ShopifySalesReportColumn.ORDER_NAME.getName(),
			ShopifySalesReportColumn.DISCOUNTS.getName());

	@Override
	public String save(String fileName, byte[] bytes) {
		ShopifyReportType type = this.recognizeReportType(bytes);
		if(type==null) return "Unrecognizable Shopify report type for "+fileName;
		String filePath = this.reportFileRootPath+File.separator+type.getReportFileSubPath();
		this.fileAdapter.save(filePath, fileName, bytes);
		return fileName + "uploaded.";
	}

	private ShopifyReportType recognizeReportType(byte[] bytes){
		try {
			Reader reader = new StringReader(new String(bytes));
			Map<String,Integer> headerToIndexMap = CSVFormat.DEFAULT.withHeader().parse(reader).getHeaderMap();
			if(this.isHeaderMatchOrderReportType(headerToIndexMap)) return ShopifyReportType.ORDER;
			if(this.isHeaderMatchPaymentTransactionReportType(headerToIndexMap)) return ShopifyReportType.PAYMENT_TRANSACTION;
			if(this.isHeaderMatchSalesReportType(headerToIndexMap)) return ShopifyReportType.SALES;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private boolean isHeaderMatchOrderReportType(Map<String,Integer> headerToIndexMap){
		if(headerToIndexMap.size()!=ShopifyOrderReportColumn.values().length) return false;
		for(ShopifyOrderReportColumn column:ShopifyOrderReportColumn.values()){
			String headerName = column.getName();
			Integer expectHeaderIndex = column.ordinal();
			if(!headerToIndexMap.containsKey(headerName)) return false;
			if(!headerToIndexMap.get(headerName).equals(expectHeaderIndex)) return false;
		}
		return true;
	}

	private boolean isHeaderMatchPaymentTransactionReportType(Map<String,Integer> headerToIndexMap){
		if(headerToIndexMap.size()!=ShopifyPaymentTransactionReportColumn.values().length) return false;
		for(ShopifyPaymentTransactionReportColumn column:ShopifyPaymentTransactionReportColumn.values()){
			String headerName = column.getName();
			Integer expectHeaderIndex = column.ordinal();
			if(!headerToIndexMap.containsKey(headerName)) return false;
			if(!headerToIndexMap.get(headerName).equals(expectHeaderIndex)) return false;
		}
		return true;
	}

	private boolean isHeaderMatchSalesReportType(Map<String,Integer> headerToIndexMap){
		if(headerToIndexMap.size()!=ShopifySalesReportColumn.values().length) return false;
		for(ShopifySalesReportColumn column:ShopifySalesReportColumn.values()){
			String headerName = column.getName();
			Integer expectHeaderIndex = column.ordinal();
			if(!headerToIndexMap.containsKey(headerName)) return false;
			if(!headerToIndexMap.get(headerName).equals(expectHeaderIndex)) return false;
		}
		return true;
	}

	@Override
	public List<String> getOrderReportFileList() {
		return this.fileAdapter.getFileList(this.getOrderReportFullPath());
	}

	@Override
	public List<String> getPaymentTransactionReportFileList() {
		return this.fileAdapter.getFileList(this.getPaymentTransactionReportFullPath());
	}

	@Override
	public List<String> getSalesReportFileList() {
		return this.fileAdapter.getFileList(this.getSalesReportFullPath());
	}

	@Override
	public String importPaymentTransactionReportFile(String fileName) {
		try {
			Reader reader = this.getFileReader(this.getPaymentTransactionReportFullPath(),fileName);
			List<ShopifyPaymentTransactionReportRawLine> lines = this.createShopifyPaymentTransactionReportLineList(reader);
			this.dao.deletePaymentTransactionReportLineItems();
			this.dao.insertPaymentTransactionReportLineItems(lines);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String importOrderReportFile(String fileName) {
		try {
			Reader reader = this.getFileReader(this.getOrderReportFullPath(),fileName);
			List<ShopifyOrderReportRawLine> lineList = this.createShopifyOrderReportLineList(reader);
			this.dao.deleteOrderReportLineItems();
			this.dao.insertOrderReportLineItems(lineList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String importSalesReportFile(String fileName) {
		try {
			Reader reader = this.getFileReader(this.getSalesReportFullPath(),fileName);
			List<ShopifySalesReportRawLine> lineList = this.createShopifySalesReportLineList(reader);
			this.dao.deleteSalesReportLineItems();
			this.dao.insertSalesReportLineItems(lineList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String deletePaymentTransactionReportFile(String fileName) {
		this.fileAdapter.delete(this.getPaymentTransactionReportFullPath(), fileName);
		return fileName + "has been deleted.";
	}

	@Override
	public String deleteOrderReportFile(String fileName) {
		this.fileAdapter.delete(this.getOrderReportFullPath(), fileName);
		return fileName + "has been deleted.";
	}

	@Override
	public String deleteSalesReportFile(String fileName) {
		this.fileAdapter.delete(this.getSalesReportFullPath(), fileName);
		return fileName + "has been deleted.";
	}

	private String getPaymentTransactionReportFullPath(){ return this.reportFileRootPath+File.separator+ ShopifyReportType.PAYMENT_TRANSACTION.getReportFileSubPath(); }

	private String getOrderReportFullPath(){ return this.reportFileRootPath+File.separator+ ShopifyReportType.ORDER.getReportFileSubPath(); }

	private String getSalesReportFullPath(){ return this.reportFileRootPath+File.separator+ ShopifyReportType.SALES.getReportFileSubPath(); }

	private Reader getFileReader(String path, String fileName) {
		try {
			File file = this.fileAdapter.get(path, fileName);
			return new FileReader(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private List<ShopifyPaymentTransactionReportRawLine> createShopifyPaymentTransactionReportLineList(Reader reader) throws IOException{
		List<ShopifyPaymentTransactionReportRawLine> lines = new ArrayList<ShopifyPaymentTransactionReportRawLine>();
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader().parse(reader);
		for(CSVRecord record:records){
			this.assertNecessaryColumnsAreNotEmpty(record,this.necessaryColumnNameListForPaymentTransactionReport);
			ShopifyPaymentTransactionReportRawLineImpl line = new ShopifyPaymentTransactionReportRawLineImpl(
					record.get(ShopifyPaymentTransactionReportColumn.TRANSACTION_DATE.getName()),
					record.get(ShopifyPaymentTransactionReportColumn.TYPE.getName()),
					record.get(ShopifyPaymentTransactionReportColumn.ORDER.getName()),
					record.get(ShopifyPaymentTransactionReportColumn.CARD_BRAND.getName()),
					record.get(ShopifyPaymentTransactionReportColumn.CARD_SOURCE.getName()),
					record.get(ShopifyPaymentTransactionReportColumn.PAYOUT_STATUS.getName()),
					record.get(ShopifyPaymentTransactionReportColumn.PAYOUT_DATE.getName())+" -0500",
					record.get(ShopifyPaymentTransactionReportColumn.AMOUNT.getName()),
					record.get(ShopifyPaymentTransactionReportColumn.FEE.getName()),
					record.get(ShopifyPaymentTransactionReportColumn.NET.getName()),
					record.get(ShopifyPaymentTransactionReportColumn.CHECKOUT.getName()),
					record.get(ShopifyPaymentTransactionReportColumn.PAYMENT_METHOD_NAME.getName()),
					record.get(ShopifyPaymentTransactionReportColumn.PRESENTMENT_AMOUNT.getName()),
					record.get(ShopifyPaymentTransactionReportColumn.PRESENTMENT_CURRENCY.getName()),
					record.get(ShopifyPaymentTransactionReportColumn.CURRENCY.getName()));

			lines.add(line);
		}
		return lines;
	}

	private List<ShopifyOrderReportRawLine> createShopifyOrderReportLineList(Reader reader) throws IOException{
		List<ShopifyOrderReportRawLine> lines = new ArrayList<ShopifyOrderReportRawLine>();
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader().parse(reader);
		for(CSVRecord record:records){
			this.assertNecessaryColumnsAreNotEmpty(record,this.necessaryColumnNameListForOrderReport);
			ShopifyOrderReportRawLineImpl line = new ShopifyOrderReportRawLineImpl(
					record.get(ShopifyOrderReportColumn.NAME.getName()),
					record.get(ShopifyOrderReportColumn.EMAIL.getName()),
					record.get(ShopifyOrderReportColumn.FINANCIAL_STATUS.getName()),
					record.get(ShopifyOrderReportColumn.PAID_AT.getName()),
					record.get(ShopifyOrderReportColumn.FULFILLMENT_STATUS.getName()),
					record.get(ShopifyOrderReportColumn.FULFILLED_AT.getName()),
					record.get(ShopifyOrderReportColumn.ACCEPTS_MARKETING.getName()),
					record.get(ShopifyOrderReportColumn.CURRENCY.getName()),
					record.get(ShopifyOrderReportColumn.SUBTOTAL.getName()),
					record.get(ShopifyOrderReportColumn.SHIPPING.getName()),
					record.get(ShopifyOrderReportColumn.TAXES.getName()),
					record.get(ShopifyOrderReportColumn.TOTAL.getName()),
					record.get(ShopifyOrderReportColumn.DISCOUNT_CODE.getName()),
					record.get(ShopifyOrderReportColumn.DISCOUNT_AMOUNT.getName()),
					record.get(ShopifyOrderReportColumn.SHIPPING_METHOD.getName()),
					record.get(ShopifyOrderReportColumn.CREATED_AT.getName()),
					record.get(ShopifyOrderReportColumn.LINEITEM_QUANTITY.getName()),
					record.get(ShopifyOrderReportColumn.LINEITEM_NAME.getName()),
					record.get(ShopifyOrderReportColumn.LINEITEM_PRICE.getName()),
					record.get(ShopifyOrderReportColumn.LINEITEM_COMPARE_AT_PRICE.getName()),
					record.get(ShopifyOrderReportColumn.LINEITEM_SKU.getName()),
					record.get(ShopifyOrderReportColumn.LINEITEM_REQUIRES_SHIPPING.getName()),
					record.get(ShopifyOrderReportColumn.LINEITEM_TAXABLE.getName()),
					record.get(ShopifyOrderReportColumn.LINEITEM_FULFILLMENT_STATUS.getName()),
					record.get(ShopifyOrderReportColumn.BILLING_NAME.getName()),
					record.get(ShopifyOrderReportColumn.BILLING_STREET.getName()),
					record.get(ShopifyOrderReportColumn.BILLING_ADDRESS1.getName()),
					record.get(ShopifyOrderReportColumn.BILLING_ADDRESS2.getName()),
					record.get(ShopifyOrderReportColumn.BILLING_COMPANY.getName()),
					record.get(ShopifyOrderReportColumn.BILLING_CITY.getName()),
					record.get(ShopifyOrderReportColumn.BILLING_ZIP.getName()),
					record.get(ShopifyOrderReportColumn.BILLING_PROVINCE.getName()),
					record.get(ShopifyOrderReportColumn.BILLING_COUNTRY.getName()),
					record.get(ShopifyOrderReportColumn.BILLING_PHONE.getName()),
					record.get(ShopifyOrderReportColumn.SHIPPING_NAME.getName()),
					record.get(ShopifyOrderReportColumn.SHIPPING_STREET.getName()),
					record.get(ShopifyOrderReportColumn.SHIPPING_ADDRESS1.getName()),
					record.get(ShopifyOrderReportColumn.SHIPPING_ADDRESS2.getName()),
					record.get(ShopifyOrderReportColumn.SHIPPING_COMPANY.getName()),
					record.get(ShopifyOrderReportColumn.SHIPPING_CITY.getName()),
					record.get(ShopifyOrderReportColumn.SHIPPING_ZIP.getName()),
					record.get(ShopifyOrderReportColumn.SHIPPING_PROVINCE.getName()),
					record.get(ShopifyOrderReportColumn.SHIPPING_COUNTRY.getName()),
					record.get(ShopifyOrderReportColumn.SHIPPING_PHONE.getName()),
					record.get(ShopifyOrderReportColumn.NOTES.getName()),
					record.get(ShopifyOrderReportColumn.NOTE_ATTRIBUTES.getName()),
					record.get(ShopifyOrderReportColumn.CANCELLED_AT.getName()),
					record.get(ShopifyOrderReportColumn.PAYMENT_METHOD.getName()),
					record.get(ShopifyOrderReportColumn.PAYMENT_REFERENCE.getName()),
					record.get(ShopifyOrderReportColumn.REFUNDED_AMOUNT.getName()),
					record.get(ShopifyOrderReportColumn.VENDOR.getName()),
					record.get(ShopifyOrderReportColumn.ID.getName()),
					record.get(ShopifyOrderReportColumn.TAGS.getName()),
					record.get(ShopifyOrderReportColumn.RISK_LEVEL.getName()),
					record.get(ShopifyOrderReportColumn.SOURCE.getName()),
					record.get(ShopifyOrderReportColumn.LINEITEM_DISCOUNT.getName()),
					record.get(ShopifyOrderReportColumn.TAX_1_NAME.getName()),
					record.get(ShopifyOrderReportColumn.TAX_1_VALUE.getName()),
					record.get(ShopifyOrderReportColumn.TAX_2_NAME.getName()),
					record.get(ShopifyOrderReportColumn.TAX_2_VALUE.getName()),
					record.get(ShopifyOrderReportColumn.TAX_3_NAME.getName()),
					record.get(ShopifyOrderReportColumn.TAX_3_VALUE.getName()),
					record.get(ShopifyOrderReportColumn.TAX_4_NAME.getName()),
					record.get(ShopifyOrderReportColumn.TAX_4_VALUE.getName()),
					record.get(ShopifyOrderReportColumn.TAX_5_NAME.getName()),
					record.get(ShopifyOrderReportColumn.TAX_5_VALUE.getName()),
					record.get(ShopifyOrderReportColumn.PHONE.getName()),
					record.get(ShopifyOrderReportColumn.Receipt_NUMBER.getName()),
					record.get(ShopifyOrderReportColumn.BILLING_PROVINCE_NAME.getName()),
					record.get(ShopifyOrderReportColumn.SHIPPING_PROVINCE_NAME.getName()));
			lines.add(line);
		}
		return lines;
	}

	private List<ShopifySalesReportRawLine> createShopifySalesReportLineList(Reader reader) throws IOException{
		List<ShopifySalesReportRawLine> lines = new ArrayList<ShopifySalesReportRawLine>();
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader().parse(reader);
		for(CSVRecord record:records){
			this.assertNecessaryColumnsAreNotEmpty(record,this.necessaryColumnNameListForSalesReport);
			ShopifySalesReportRawLineImpl line = new ShopifySalesReportRawLineImpl(
					record.get(ShopifySalesReportColumn.ORDER_ID.getName()),
					record.get(ShopifySalesReportColumn.SALE_ID.getName()),
					record.get(ShopifySalesReportColumn.DATE.getName()),
					record.get(ShopifySalesReportColumn.ORDER_NAME.getName()),
					record.get(ShopifySalesReportColumn.TRANSACTION_TYPE.getName()),
					record.get(ShopifySalesReportColumn.SALE_TYPE.getName()),
					record.get(ShopifySalesReportColumn.SALES_CHANNEL.getName()),
					record.get(ShopifySalesReportColumn.POS_LOCATION.getName()),
					record.get(ShopifySalesReportColumn.BILLING_COUNTRY.getName()),
					record.get(ShopifySalesReportColumn.BILLING_REGION.getName()),
					record.get(ShopifySalesReportColumn.BILLING_CITY.getName()),
					record.get(ShopifySalesReportColumn.SHIPPING_COUNTRY.getName()),
					record.get(ShopifySalesReportColumn.SHIPPING_REGION.getName()),
					record.get(ShopifySalesReportColumn.SHIPPING_CITY.getName()),
					record.get(ShopifySalesReportColumn.PRODUCT_TYPE.getName()),
					record.get(ShopifySalesReportColumn.PRODUCT_VENDOR.getName()),
					record.get(ShopifySalesReportColumn.PRODUCT_TITLE.getName()),
					record.get(ShopifySalesReportColumn.PRODUCT_VARIANT_TITLE.getName()),
					record.get(ShopifySalesReportColumn.PRODUCT_VARIANT_SKU.getName()),
					record.get(ShopifySalesReportColumn.NET_QUANTITY.getName()),
					record.get(ShopifySalesReportColumn.GROSS_SALES.getName()),
					record.get(ShopifySalesReportColumn.DISCOUNTS.getName()),
					record.get(ShopifySalesReportColumn.RETURNS.getName()),
					record.get(ShopifySalesReportColumn.NET_SALES.getName()),
					record.get(ShopifySalesReportColumn.SHIPPING.getName()),
					record.get(ShopifySalesReportColumn.TAXES.getName()),
					record.get(ShopifySalesReportColumn.TOTAL_SALES.getName()));
			lines.add(line);
		}
		return lines;
	}

	private void assertNecessaryColumnsAreNotEmpty(CSVRecord record,List<String> necessaryColumnNameList) {
		for(String columnName:necessaryColumnNameList){
			Assert.isTrue(StringUtils.hasText(record.get(columnName)));
		}
	}

}