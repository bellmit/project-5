package com.kindminds.drs.api.usecase.report.shopify;

import java.util.List;

public interface ImportShopifyReportUco {
	
	public String save(String fileName, byte[] bytes);
	public List<String> getOrderReportFileList();
	public List<String> getPaymentTransactionReportFileList();
	public List<String> getSalesReportFileList();
	public String importPaymentTransactionReportFile(String fileName);
	public String importOrderReportFile(String fileName);
	public String importSalesReportFile(String fileName);
	public String deletePaymentTransactionReportFile(String fileName);
	public String deleteOrderReportFile(String fileName);
	public String deleteSalesReportFile(String fileName);

	public enum ShopifyPaymentTransactionReportColumn{
		TRANSACTION_DATE("Transaction Date"),
		TYPE("Type"),
		ORDER("Order"),
		CARD_BRAND("Card Brand"),
		CARD_SOURCE("Card Source"),
		PAYOUT_STATUS("Payout Status"),
		PAYOUT_DATE("Payout Date"),
		AVAILABLE_ON("Available On"),
		AMOUNT("Amount"),
		FEE("Fee"),
		NET("Net"),
		CHECKOUT("Checkout"),
		PAYMENT_METHOD_NAME("Payment Method Name"),
		PRESENTMENT_AMOUNT("Presentment Amount"),
		PRESENTMENT_CURRENCY("Presentment Currency"),
		CURRENCY("Currency");
		private String name = null;
		ShopifyPaymentTransactionReportColumn(String name){this.name= name;}
		public String getName(){return this.name;}
	}
	
	public enum ShopifyOrderReportColumn{
		NAME("Name"),
		EMAIL("Email"),
		FINANCIAL_STATUS("Financial Status"),
		PAID_AT("Paid at"),
		FULFILLMENT_STATUS("Fulfillment Status"),
		FULFILLED_AT("Fulfilled at"),
		ACCEPTS_MARKETING("Accepts Marketing"),
		CURRENCY("Currency"),
		SUBTOTAL("Subtotal"),
		SHIPPING("Shipping"),
		TAXES("Taxes"),
		TOTAL("Total"),
		DISCOUNT_CODE("Discount Code"),
		DISCOUNT_AMOUNT("Discount Amount"),
		SHIPPING_METHOD("Shipping Method"),
		CREATED_AT("Created at"),
		LINEITEM_QUANTITY("Lineitem quantity"),
		LINEITEM_NAME("Lineitem name"),
		LINEITEM_PRICE("Lineitem price"),
		LINEITEM_COMPARE_AT_PRICE("Lineitem compare at price"),
		LINEITEM_SKU("Lineitem sku"),
		LINEITEM_REQUIRES_SHIPPING("Lineitem requires shipping"),
		LINEITEM_TAXABLE("Lineitem taxable"),
		LINEITEM_FULFILLMENT_STATUS("Lineitem fulfillment status"),
		BILLING_NAME("Billing Name"),
		BILLING_STREET("Billing Street"),
		BILLING_ADDRESS1("Billing Address1"),
		BILLING_ADDRESS2("Billing Address2"),
		BILLING_COMPANY("Billing Company"),
		BILLING_CITY("Billing City"),
		BILLING_ZIP("Billing Zip"),
		BILLING_PROVINCE("Billing Province"),
		BILLING_COUNTRY("Billing Country"),
		BILLING_PHONE("Billing Phone"),
		SHIPPING_NAME("Shipping Name"),
		SHIPPING_STREET("Shipping Street"),
		SHIPPING_ADDRESS1("Shipping Address1"),
		SHIPPING_ADDRESS2("Shipping Address2"),
		SHIPPING_COMPANY("Shipping Company"),
		SHIPPING_CITY("Shipping City"),
		SHIPPING_ZIP("Shipping Zip"),
		SHIPPING_PROVINCE("Shipping Province"),
		SHIPPING_COUNTRY("Shipping Country"),
		SHIPPING_PHONE("Shipping Phone"),
		NOTES("Notes"),
		NOTE_ATTRIBUTES("Note Attributes"),
		CANCELLED_AT("Cancelled at"),
		PAYMENT_METHOD("Payment Method"),
		PAYMENT_REFERENCE("Payment Reference"),
		REFUNDED_AMOUNT("Refunded Amount"),
		VENDOR("Vendor"),
		ID("Id"),
		TAGS("Tags"),
		RISK_LEVEL("Risk Level"),
		SOURCE("Source"),
		LINEITEM_DISCOUNT("Lineitem discount"),
		TAX_1_NAME("Tax 1 Name"),
		TAX_1_VALUE("Tax 1 Value"),
		TAX_2_NAME("Tax 2 Name"),
		TAX_2_VALUE("Tax 2 Value"),
		TAX_3_NAME("Tax 3 Name"),
		TAX_3_VALUE("Tax 3 Value"),
		TAX_4_NAME("Tax 4 Name"),
		TAX_4_VALUE("Tax 4 Value"),
		TAX_5_NAME("Tax 5 Name"),
		TAX_5_VALUE("Tax 5 Value"),
		PHONE("Phone"),
		Receipt_NUMBER("Receipt Number"),
		DUTIES("Duties"),
		BILLING_PROVINCE_NAME("Billing Province Name"),
		SHIPPING_PROVINCE_NAME("Shipping Province Name");
		private String name = null;
		ShopifyOrderReportColumn(String name){this.name= name;}
		public String getName(){return this.name;}
	}

	public enum ShopifySalesReportColumn{
		ORDER_ID("Order ID"),
		SALE_ID("Sale ID"),
		DATE("Date"),
		ORDER_NAME("Order"),
		TRANSACTION_TYPE("Transaction type"),
		SALE_TYPE("Sale type"),
		SALES_CHANNEL("Sales channel"),
		POS_LOCATION("POS location"),
		BILLING_COUNTRY("Billing country"),
		BILLING_REGION("Billing region"),
		BILLING_CITY("Billing city"),
		SHIPPING_COUNTRY("Shipping country"),
		SHIPPING_REGION("Shipping region"),
		SHIPPING_CITY("Shipping city"),
		PRODUCT_TYPE("Product type"),
		PRODUCT_VENDOR("Product vendor"),
		PRODUCT_TITLE("Product"),
		PRODUCT_VARIANT_TITLE("Variant"),
		PRODUCT_VARIANT_SKU("Variant SKU"),
		NET_QUANTITY("Net quantity"),
		GROSS_SALES("Gross sales"),
//		LINE_ITEM_DISCOUNT("Line item discounts"),
		DISCOUNTS("Discounts"),
		RETURNS("Returns"),
		NET_SALES("Net sales"),
		SHIPPING("Shipping"),
		TAXES("Taxes"),
		TOTAL_SALES("Total sales");
		private String name = null;
		ShopifySalesReportColumn(String name){this.name= name;}
		public String getName(){return this.name;}
	}

}