package com.kindminds.drs.api.usecase.report.amazon;

import java.util.List;

public interface ImportAmazonReturnReportUco {
	public enum AmazonReturnReportColumn{
		RETURN_DATE("return-date"),
		ORDER_ID("order-id"),
		SKU("sku"),
		ASIN("asin"),
		FNSKU("fnsku"),
		PRODUCT_NAME("product-name"),
		QUANTITY("quantity"),
		FULFILLMENT_CENTER_ID("fulfillment-center-id"),
		DETAILED_DISPOSITION("detailed-disposition"),
		REASON("reason"),
		STATUS("status"),
		LICENSE_PLATE_NUMBER("license-plate-number"),
		CUSTOMER_COMMENTS("customer-comments"),;
		private String name = null;
		AmazonReturnReportColumn(String name){this.name= name;}
		public String getName(){return this.name;}
	}
	public String save(String fileName, byte[] bytes);
	public List<String> getFileList();
	public String delete(String fileName);
	public String importFile(String fileName);
	public String importFileByFullPath(String fullPath);
	public String importFile(byte [] bytes);
}
