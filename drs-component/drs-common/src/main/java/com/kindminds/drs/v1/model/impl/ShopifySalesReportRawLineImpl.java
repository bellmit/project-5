package com.kindminds.drs.v1.model.impl;

import com.kindminds.drs.api.v1.model.shopify.ShopifySalesReportRawLine;
import com.kindminds.drs.util.DateHelper;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

public class ShopifySalesReportRawLineImpl implements ShopifySalesReportRawLine{

	private String orderId;
	private String salesId;
	private Date date;
	private String orderName;
	private String transactionType;
	private String saleType;
	private String salesChannel;
	private String posLocation;
	private String billingCountry;
	private String billingRegion;
	private String billingCity;
	private String shippingCountry;
	private String shippingRegion;
	private String shippingCity;
	private String productType;
	private String productVendor;
	private String productTitle;
	private String productVariantTitle;
	private String productVariantSku;
	private int netQuantity;
	private BigDecimal grossSales;
	private BigDecimal lineItemDiscount;
	private BigDecimal discounts;
	private BigDecimal returns;
	private BigDecimal netSales;
	private BigDecimal shipping;
	private BigDecimal taxes;
	private BigDecimal totalSales;
	
	public ShopifySalesReportRawLineImpl(
			String orderId,
			String salesId,
			String date,
			String orderName,
			String transactionType,
			String saleType,
			String salesChannel,
			String posLocation,
			String billingCountry,
			String billingRegion,
			String billingCity,
			String shippingCountry,
			String shippingRegion,
			String shippingCity,
			String productType,
			String productVendor,
			String productTitle,
			String productVariantTitle,
			String productVariantSku,
			String netQuantity,
			String grossSales,
			String discounts,
			String returns,
			String netSales,
			String shipping,
			String taxes,
			String totalSales
			){
		this.orderId = orderId;
		this.salesId = salesId;
		this.date = this.toDate(date);
		this.orderName = orderName;
		this.transactionType = transactionType;
		this.saleType = saleType;
		this.salesChannel = salesChannel;
		this.posLocation = this.replaceEmptyStringWithNull(posLocation);
		this.billingCountry = this.replaceEmptyStringWithNull(billingCountry);
		this.billingRegion = this.replaceEmptyStringWithNull(billingRegion);
		this.billingCity = this.replaceEmptyStringWithNull(billingCity);
		this.shippingCountry = this.replaceEmptyStringWithNull(shippingCountry);
		this.shippingRegion = this.replaceEmptyStringWithNull(shippingRegion);
		this.shippingCity = this.replaceEmptyStringWithNull(shippingCity); 
		this.productType = this.replaceEmptyStringWithNull(productType);
		this.productVendor = this.replaceEmptyStringWithNull(productVendor);
		this.productTitle = this.replaceEmptyStringWithNull(productTitle);
		this.productVariantTitle = this.replaceEmptyStringWithNull(productVariantTitle);
		this.productVariantSku = this.replaceEmptyStringWithNull(productVariantSku);
		this.netQuantity = Integer.parseInt(netQuantity);
		this.grossSales = this.toBigDecimal(grossSales); //gross sales
		this.discounts = this.toBigDecimal(discounts); //discounts
		this.returns = this.toBigDecimal(returns);
		this.netSales = this.toBigDecimal(netSales); //net sales
		this.shipping = this.toBigDecimal(shipping);
		this.taxes = this.toBigDecimal(taxes);
		this.totalSales = this.toBigDecimal(totalSales); //total sales
	}
	
	private String replaceEmptyStringWithNull(String str){ return StringUtils.isEmpty(str)?null:str; }
	private Date toDate(String dateStr){ return StringUtils.isEmpty(dateStr)?null:DateHelper.toDate(dateStr,"yyyy-MM-dd'T'HH:mm:ssXXX"); }
	private BigDecimal toBigDecimal(String str){ return StringUtils.isEmpty(str)?null:new BigDecimal(str); }


	@Override
	public String getOrderId() {
		return this.orderId;
	}

	@Override
	public String getSalesId(){
		return this.salesId;
	}
	
	@Override
    public Date getDate(){
		return this.date;		
	}
    
    @Override
    public String getOrderName(){
    	return this.orderName;
    }
    
    @Override
    public String getTransactionType(){
    	return this.transactionType;
    }
    
    @Override
    public String getSaleType(){
    	return this.saleType;
    }
    
    @Override
    public String getSalesChannel(){
    	return this.salesChannel;
    }
    
    @Override
    public String getPosLocation(){
    	return this.posLocation;
    }
    
    @Override
    public String getBillingCountry(){
    	return this.billingCountry;
    }
    
    @Override
    public String getBillingRegion(){
    	return this.billingRegion;
    }
    
    @Override
    public String getBillingCity(){
    	return this.billingCity;
    }
    
    @Override
    public String getShippingCountry(){
    	return this.shippingCountry;
    }
    
    @Override
    public String getShippingRegion(){
    	return this.shippingRegion;
    }
    
    @Override
    public String getShippingCity(){
    	return this.shippingCity;
    }
    
    @Override
    public String getProductType(){
    	return this.productType;
    }
    
    @Override
    public String getProductVendor(){
    	return this.productVendor;
    }
    
    @Override
    public String getProductTitle(){
    	return this.productTitle;
    }
    
    @Override
    public String getProductVariantTitle(){
    	return this.productVariantTitle;
    }
    
    @Override
    public String getProductVariantSku(){
    	return this.productVariantSku;
    }
    
    @Override
    public int getNetQuantity(){
    	return this.netQuantity;
    }
    
    @Override
    public BigDecimal getGrossSales(){
    	return this.grossSales;
    }
    
    @Override
    public BigDecimal getLineItemDiscount(){
    	return this.lineItemDiscount;
    }
    
    @Override
    public BigDecimal getDiscounts(){
    	return this.discounts;
    }

    @Override
	public BigDecimal getReturns() {
		return this.returns;
	}
    
    @Override
    public BigDecimal getNetSales(){
    	return this.netSales;
    }

    @Override
	public BigDecimal getShipping() {
		return this.shipping;
	}
    
    @Override
    public BigDecimal getTaxes(){
    	return this.taxes;
    }
    
    @Override
    public BigDecimal getTotalSales(){
    	return this.totalSales;
    }

}