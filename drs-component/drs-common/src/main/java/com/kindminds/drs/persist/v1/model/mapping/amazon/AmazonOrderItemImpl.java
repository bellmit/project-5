package com.kindminds.drs.persist.v1.model.mapping.amazon;

import java.math.BigDecimal;
import java.util.List;





import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.amazon.AmazonOrder.AmazonOrderItem;


public class AmazonOrderItemImpl implements AmazonOrderItem {
	
	//@Id ////@Column(name="id")
	private int id;
	//@Column(name="asin")
	private String asin;
	//@Column(name="seller_sku")
	private String sellerSku;
	//@Column(name="order_item_id")
	private String orderItemId;
	//@Column(name="quantity_ordered")
	private Integer quantityOrdered;
	//@Column(name="quantity_shipped")
	private Integer quantityShipped;

	public AmazonOrderItemImpl() {
	}

	public AmazonOrderItemImpl(int id, String asin, String sellerSku, String orderItemId, Integer quantityOrdered, Integer quantityShipped) {
		this.id = id;
		this.asin = asin;
		this.sellerSku = sellerSku;
		this.orderItemId = orderItemId;
		this.quantityOrdered = quantityOrdered;
		this.quantityShipped = quantityShipped;
	}

	@Override
	public String getOrderItemId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getQuantityOrdered() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Integer getQuantityShipped() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getASIN() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSellerSKU() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Currency getItemPriceCurrency() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getItemPriceAmount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Currency getShippingPriceCurrency() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getShippingPriceAmount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Currency getGiftWrapPriceCurrency() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getGiftWrapPriceAmount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Currency getItemTaxCurrency() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getItemTaxAmount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Currency getShippingTaxCurrency() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getShippingTaxAmount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Currency getGiftWrapTaxCurrency() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getGiftWrapTaxAmount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Currency getShippingDiscountCurrency() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getShippingDiscountAmount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Currency getPromotionDiscountCurrency() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getPromotionDiscountAmount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getPromotionIds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Currency getCODFeeCurrency() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getCODFeeAmount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Currency getCODFeeDiscountCurrency() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getCODFeeDiscountAmount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getGiftMessageText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getGiftWrapLevel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getInvoiceTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getInvoiceRequirement() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getInvoiceCategoryBuyerSelected() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getInvoiceInformation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getConditionNote() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getConditionId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getConditionSubtypeId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getScheduledDeliveryStartDate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getScheduledDeliveryEndDate() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
