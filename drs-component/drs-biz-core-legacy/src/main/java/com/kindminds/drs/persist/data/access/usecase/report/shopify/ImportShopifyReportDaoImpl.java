package com.kindminds.drs.persist.data.access.usecase.report.shopify;

import java.util.List;


import com.kindminds.drs.api.data.access.usecase.report.shopify.ImportShopifyReportDao;
import com.kindminds.drs.api.v1.model.shopify.ShopifyOrderReportRawLine;
import com.kindminds.drs.api.v1.model.shopify.ShopifyPaymentTransactionReportRawLine;
import com.kindminds.drs.api.v1.model.shopify.ShopifySalesReportRawLine;
import com.kindminds.drs.persist.data.access.rdb.Dao;



import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;





@Repository
public class ImportShopifyReportDaoImpl extends Dao implements ImportShopifyReportDao {



	@Override @Transactional("transactionManager")
	public int insertOrderReportLineItems(List<ShopifyOrderReportRawLine> lineItems) {
		String sql = "insert into shopify_order_report "
				+ "(  name,  email, financial_status, paid_at, fulfillment_status, fulfilled_at, accepts_marketing,  currency,  subtotal,  shipping,  taxes,  total, discount_code, discount_amount, shipping_method, created_at, lineitem_quantity, lineitem_name, lineitem_price, lineitem_compare_at_price, lineitem_sku, lineitem_requires_shipping, lineitem_taxable, lineitem_fulfillment_status, billing_name, billing_street, billing_address1, billing_address2, billing_company, billing_city, billing_zip, billing_province, billing_country, billing_phone, shipping_name, shipping_street, shipping_address1, shipping_address2, shipping_company, shipping_city, shipping_zip, shipping_province, shipping_country, shipping_phone,  notes, note_attributes, cancelled_at, payment_method, payment_reference, refunded_amount,  vendor,  id,  tags, risk_level,  source, lineitem_discount, tax_1_name, tax_1_value, tax_2_name, tax_2_value, tax_3_name, tax_3_value, tax_4_name, tax_4_value, tax_5_name, tax_5_value,  phone ) values "
				+ "( :name, :email, :financialStatus, :paidAt, :fulfillmentStatus, :fulfilledAt, :acceptsMarketing, :currency, :subtotal, :shipping, :taxes, :total, :discountCode, :discountAmount, :shippingMethod, :createdAt, :lineitemQuantity, :lineitemName, :lineitemPrice,   :lineitemCompareAtPrice, :lineitemSku,  :lineitemRequiresShipping, :lineitemTaxable,  :lineitemFulfillmentStatus, :billingName, :billingStreet, :billingAddress1, :billingAddress2, :billingCompany, :billingCity, :billingZip, :billingProvince, :billingCountry, :billingPhone, :shippingName, :shippingStreet, :shippingAddress1, :shippingAddress2, :shippingCompany, :shippingCity, :shippingZip, :shippingProvince, :shippingCountry, :shippingPhone, :notes, :noteAttributes, :cancelledAt, :paymentMethod, :paymentReference, :refundedAmount, :vendor, :id, :tags, :riskLevel, :source, :lineitemDiscount,  :tax1Name,  :tax1Value,  :tax2Name,  :tax2Value,  :tax3Name,  :tax3Value,  :tax4Name,  :tax4Value,  :tax5Name,  :tax5Value, :phone ) ";
		int insertedRows = 0;
		MapSqlParameterSource q = new MapSqlParameterSource();
		for(ShopifyOrderReportRawLine line:lineItems){

			q.addValue("name",line.getName());
			q.addValue("email",line.getEmail());
			q.addValue("financialStatus",line.getFinancialStatus());
			q.addValue("paidAt",line.getPaidAt());
			q.addValue("fulfillmentStatus",line.getFulfillmentStatus());
			q.addValue("fulfilledAt",line.getFulfilledAt());
			q.addValue("acceptsMarketing",line.getAcceptsMarketing());
			q.addValue("currency",line.getCurrency());
			q.addValue("subtotal",line.getSubtotal());
			q.addValue("shipping",line.getShipping());
			q.addValue("taxes",line.getTaxes());
			q.addValue("total",line.getTotal());
			q.addValue("discountCode",line.getDiscountCode());
			q.addValue("discountAmount",line.getDiscountAmount());
			q.addValue("shippingMethod",line.getShippingMethod());
			q.addValue("createdAt",line.getCreatedAt());
			q.addValue("lineitemQuantity",line.getLineitemQuantity());
			q.addValue("lineitemName",line.getLineitemName());
			q.addValue("lineitemPrice",line.getLineitemPrice());
			q.addValue("lineitemCompareAtPrice",line.getLineitemCompareAtPrice());
			q.addValue("lineitemSku",line.getLineitemSku());
			q.addValue("lineitemRequiresShipping",line.getLineitemRequiresShipping());
			q.addValue("lineitemTaxable",line.getLineitemTaxable());
			q.addValue("lineitemFulfillmentStatus",line.getLineitemFulfillmentStatus());
			q.addValue("billingName",line.getBillingName());
			q.addValue("billingStreet",line.getBillingStreet());
			q.addValue("billingAddress1",line.getBillingAddress1());
			q.addValue("billingAddress2",line.getBillingAddress2());
			q.addValue("billingCompany",line.getBillingCompany());
			q.addValue("billingCity",line.getBillingCity());
			q.addValue("billingZip",line.getBillingZip());
			q.addValue("billingProvince",line.getBillingProvince());
			q.addValue("billingCountry",line.getBillingCountry());
			q.addValue("billingPhone",line.getBillingPhone());
			q.addValue("shippingName",line.getShippingName());
			q.addValue("shippingStreet",line.getShippingStreet());
			q.addValue("shippingAddress1",line.getShippingAddress1());
			q.addValue("shippingAddress2",line.getShippingAddress2());
			q.addValue("shippingCompany",line.getShippingCompany());
			q.addValue("shippingCity",line.getShippingCity());
			q.addValue("shippingZip",line.getShippingZip());
			q.addValue("shippingProvince",line.getShippingProvince());
			q.addValue("shippingCountry",line.getShippingCountry());
			q.addValue("shippingPhone",line.getShippingPhone());
			q.addValue("notes",line.getNotes());
			q.addValue("noteAttributes",line.getNoteAttributes());
			q.addValue("cancelledAt",line.getCancelledAt());
			q.addValue("paymentMethod",line.getPaymentMethod());
			q.addValue("paymentReference",line.getPaymentReference());
			q.addValue("refundedAmount",line.getRefundedAmount());
			q.addValue("vendor",line.getVendor());
			q.addValue("id",line.getId());
			q.addValue("tags",line.getTags());
			q.addValue("riskLevel",line.getRiskLevel());
			q.addValue("source",line.getSource());
			q.addValue("lineitemDiscount",line.getLineitemDiscount());
			q.addValue("tax1Name",line.getTax1Name());
			q.addValue("tax1Value",line.getTax1Value());
			q.addValue("tax2Name",line.getTax2Name());
			q.addValue("tax2Value",line.getTax2Value());
			q.addValue("tax3Name",line.getTax3Name());
			q.addValue("tax3Value",line.getTax3Value());
			q.addValue("tax4Name",line.getTax4Name());
			q.addValue("tax4Value",line.getTax4Value());
			q.addValue("tax5Name",line.getTax5Name());
			q.addValue("tax5Value",line.getTax5Value());
			q.addValue("phone", line.getPhone());
			Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
			insertedRows++;
		}
		return insertedRows;
	}

	@Override @Transactional("transactionManager")
	public int insertPaymentTransactionReportLineItems(List<ShopifyPaymentTransactionReportRawLine> lineItems) {
		String sql = "insert into shopify_payment_transaction_report "
				+ "( transaction_date,  type, order_name, card_brand, card_source, payout_status, payout_date,  amount,  fee,  net, "
				+" checkout, payment_method_name, presentment_amount, presentment_currency, currency ) values "
				+ "( :transactionDate, :type, :orderName, :cardBrand, :cardSource, :payoutStatus, :payoutDate, :amount, :fee, :net, "
				+" :checkout, :paymentMethodName, :presentmentAmount, :presentmentCurrency, :currency) ";
		int insertedRows = 0;
		for(ShopifyPaymentTransactionReportRawLine line:lineItems){
			MapSqlParameterSource q = new MapSqlParameterSource();
			q.addValue("transactionDate", line.getTransactionDate());
			q.addValue("type", line.getType());
			q.addValue("orderName", line.getOrderName());
			q.addValue("cardBrand", line.getCardBrand());
			q.addValue("cardSource", line.getCardSource());
			q.addValue("payoutStatus", line.getPayoutStatus());
			q.addValue("payoutDate",  line.getPayoutDate());
			q.addValue("amount", line.getAmount());
			q.addValue("fee", line.getFee());
			q.addValue("net", line.getNet());
			q.addValue("checkout",line.getCheckout());
			q.addValue("paymentMethodName",line.getPaymentMethodName());
			q.addValue("presentmentAmount",line.getPresentmentAmount());
			q.addValue("presentmentCurrency",line.getPresentmentAmount());
			q.addValue("currency",line.getCurrency());
			Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
			insertedRows++;
		}
		return insertedRows;
	}

	@Override @Transactional("transactionManager")
	public int insertSalesReportLineItems(List<ShopifySalesReportRawLine> lineItems){
		String sql = "insert into shopify_sales_report "
				+ "(order_id,  sale_id, sale_date,   order_name,  transaction_type,  sale_type, sale_channel, pos_location, billing_country, billing_region, billing_city, shipping_country, shipping_region, shipping_city, product_type, product_vendor, product_title, product_variant_title, product_variant_sku, net_quantity, amount_before_discount_taxes, order_discount, returns,   amount_after_discount_before_tax, shipping, taxes, total_sales ) values "
				+ "(:orderId, :saleId, :saleDate,   :orderName,  :transactionType,  :saleType, :saleChannel, :posLocation, :billingCountry, :billingRegion, :billingCity, :shippingCountry, :shippingRegion, :shippingCity, :productType, :productVendor, :productTitle,  :productVariantTitle,  :productVariantSku, :netQuantity,   :amountBeforeDiscountTaxes, :orderDiscount, :returns,   :amountAfterDiscountBeforeTax,  :shipping, :taxes,   :totalSales ) ";
		int insertedRows = 0;
		for(ShopifySalesReportRawLine line:lineItems) {
			MapSqlParameterSource q = new MapSqlParameterSource();
			q.addValue("orderId", line.getOrderId());
			q.addValue("saleId", line.getSalesId());
			q.addValue("saleDate", line.getDate());
			q.addValue("orderName", line.getOrderName());
			q.addValue("transactionType", line.getTransactionType());
			q.addValue("saleType", line.getSaleType());
			q.addValue("saleChannel", line.getSalesChannel());
			q.addValue("posLocation", line.getPosLocation());
			q.addValue("billingCountry", line.getBillingCountry());
			q.addValue("billingRegion", line.getBillingRegion());
			q.addValue("billingCity", line.getBillingCity());
			q.addValue("shippingCountry", line.getShippingRegion());
			q.addValue("shippingRegion", line.getShippingRegion());
			q.addValue("shippingCity", line.getShippingCity());
			q.addValue("productType", line.getProductType());
			q.addValue("productVendor", line.getProductVendor());
			q.addValue("productTitle", line.getProductTitle());
			q.addValue("productVariantTitle", line.getProductVariantTitle());
			q.addValue("productVariantSku", line.getProductVariantSku());
			q.addValue("netQuantity", line.getNetQuantity());
			q.addValue("amountBeforeDiscountTaxes", line.getGrossSales());
			q.addValue("orderDiscount", line.getDiscounts());
			q.addValue("returns", line.getReturns());
			q.addValue("amountAfterDiscountBeforeTax", line.getNetSales());
			q.addValue("shipping", line.getShipping());
			q.addValue("taxes", line.getTaxes());
			q.addValue("totalSales", line.getTotalSales());
			Assert.isTrue(getNamedParameterJdbcTemplate().update(sql,q)==1);
			insertedRows++;
		}
		return insertedRows;
	}

	@Override @Transactional("transactionManager")
	public int deleteOrderReportLineItems() {
		String sql = "delete from shopify_order_report ";

		return getJdbcTemplate().update(sql);
	}

	@Override @Transactional("transactionManager")
	public int deletePaymentTransactionReportLineItems() {
		String sql = "delete from shopify_payment_transaction_report ";
		return getJdbcTemplate().update(sql);
	}

	@Override @Transactional("transactionManager")
	public int deleteSalesReportLineItems(){
		String sql = "delete from shopify_sales_report ";
		return getJdbcTemplate().update(sql);
	}

}