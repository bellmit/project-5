package com.kindminds.drs.api.v1.model.amazon;

import com.kindminds.drs.Currency;
import com.kindminds.drs.enums.AmazonTransactionType;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface AmazonTransaction {
	public String getOrderId();
	public String getMerchantOrderId();
	public String getMarketplaceName();
	public AmazonTransactionType getType();
	public Date getPostedDateTime();
	public List<AmazonTransactionLineItem> getLineItems();
	
	public enum AmazonReportMarketPlace{
		AMAZON_COM("Amazon.com"),
		AMAZON_CO_UK("Amazon.co.uk"),
		AMAZON_CA("Amazon.ca"),
		AMAZON_DE("Amazon.de"),
		AMAZON_IT("Amazon.it"),
		AMAZON_FR("Amazon.fr"),
		AMAZON_ES("Amazon.es"),
		NON_AMAZON("Non-Amazon"),
		SI_UK_PROD("SI UK Prod Marketplace"),
		SI_CA_PROD("SI CA Prod Marketplace"),
		NULL(null);
		private String name;
		AmazonReportMarketPlace(String name){
			this.name = name;
		}
		public String getName(){ return this.name; }
		public static AmazonReportMarketPlace fromName(String s){
			for(AmazonReportMarketPlace m: AmazonReportMarketPlace.values()){
				if(m.getName()==null){
					if(s==null) return m;
					continue;
				}
				if(m.getName().equals(s)) return m;
			}
			return null;
		}
	}
	
	public interface AmazonTransactionLineItem{
		public String getSku();
		public Currency getCurrency();
		public String getAmountType();
		public String getAmountDescription();
		public BigDecimal getAmount();
		public Integer getQtyPurchased(); 
		public enum AmzAmountTypeDesc {
			PRICE_PRINCIPAL("ItemPrice","Principal"),
			PRICE_TAX("ItemPrice","Tax"),
			PRICE_GIFTWRAP("ItemPrice","GiftWrap"),
			PRICE_GIFTWRAPTAX("ItemPrice","GiftWrapTax"),
			PRICE_SHIPPING("ItemPrice","Shipping"),
			PRICE_SHIPPINGTAX("ItemPrice","ShippingTax"),
			PRICE_GOODWILL("ItemPrice","Goodwill"),
			PRICE_RESTOCKINGFEE("ItemPrice","RestockingFee"),
			FEE_SHIPPINGHB("ItemFees","ShippingHB"),
			FEE_TRANSACTION("ItemFees","TransactionFee"),
			FEE_PERUNIT("ItemFees","FBAPerUnitFulfillmentFee"),
			FEE_SHIPPING_CHARGEBACK("ItemFees","ShippingChargeback"),
			FEE_REFUND_COMMISSION("ItemFees","RefundCommission"),
			FEE_GIFTWRAP_CHARGEBACK("ItemFees","GiftwrapChargeback"),
			FEE_FBA_WEIGHTBASED("ItemFees","FBAWeightBasedFee"),
			FEE_COMMISSION("ItemFees","Commission"),
			FEE_SALESTAXSERVICE("ItemFees","SalesTaxServiceFee"),
			FEE_PERORDER("ItemFees","FBAPerOrderFulfillmentFee"),
			FEE_FIXEDCLOSING("ItemFees","FixedClosingFee"),
			FBA_DAMAGE("FBA Inventory Reimbursement","WAREHOUSE_DAMAGE"),
			FBA_INCORRECTFEE("FBA Inventory Reimbursement","INCORRECT_FEES_ITEMS"),
			FBA_LOST("FBA Inventory Reimbursement","WAREHOUSE_LOST"),
			FBA_LOST_MANUAL("FBA Inventory Reimbursement","WAREHOUSE_LOST_MANUAL"),
			FBA_REMOVAL_ORDER_LOST("FBA Inventory Reimbursement","REMOVAL_ORDER_LOST"),
			FREE_REPLACEMENT_REFUND_ITEMS("FBA Inventory Reimbursement","FREE_REPLACEMENT_REFUND_ITEMS"),
			COMPENSATED_CLAWBACK("FBA Inventory Reimbursement", "COMPENSATED_CLAWBACK"),
			FBA_RETURN("FBA Inventory Reimbursement","Customer Return"),
			FBA_REVERSAL_REIMBURSEMENT("FBA Inventory Reimbursement","REVERSAL_REIMBURSEMENT"),
			FBA_CS_ERROR_NON_ITEMIZED("FBA Inventory Reimbursement","CS_ERROR_NON_ITEMIZED"),
			FBA_MISSING_FROM_INBOUND("FBA Inventory Reimbursement","MISSING_FROM_INBOUND"),
			FBA_RE_EVALUATION("FBA Inventory Reimbursement","RE_EVALUATION"),
			OTHER_STORAGE("other-transaction","Storage Fee"),
			OTHER_SUBSCRIPTION("other-transaction","Subscription Fee"),
			OTHER_PLACEMENT("other-transaction","Inventory Placement Service Fee"),
			PROMOTION_PRINCIPAL("Promotion","Principal"),
			PROMOTION_SHIPPING("Promotion","Shipping"),
			PROMOTION_TAXDISCOUNT("Promotion","TaxDiscount"),
			SHIPMENTFEES_TRANSPORTATION("ShipmentFees","FBA transportation fee"),
			ITEM_WITH_HELD_TAX_SHIPPING("ItemWithheldTax","MarketplaceFacilitatorTax-Shipping"),
			ITEM_WITH_HELD_TAX_PRINCIPAL("ItemWithheldTax","MarketplaceFacilitatorTax-Principal"),
			ITEM_WITH_HELD_OTHER("ItemWithheldTax","MarketplaceFacilitatorTax-Other"),
			VARIABLE_CLOSING_FEE("ItemFees", "VariableClosingFee"),
			ITEM_WITH_HELD_TAX_RESTOCKING("ItemWithheldTax","MarketplaceFacilitatorTax-RestockingFee"),
			LOW_VALUE_GOODS_SHIPPING("ItemWithheldTax","LowValueGoods-Shipping"),
			LOW_VALUE_GOODS_PRINCIPAL("ItemWithheldTax","LowValueGoods-Principal"),
			LOW_VALUE_GOODS_TAX_PRINCIPAL("ItemWithheldTax","LowValueGoodsTax-Principal"),
			LOW_VALUE_GOODS_TAX_SHIPPING("ItemWithheldTax","LowValueGoodsTax-Shipping"),
			ITEM_WITH_HELD_VAT_SHIPPING("ItemWithheldTax", "MarketplaceFacilitatorVAT-Shipping"),
			ITEM_WITH_HELD_VAT_PRINCIPAL("ItemWithheldTax", "MarketplaceFacilitatorVAT-Principal");

			private String type;
			private String description;
			AmzAmountTypeDesc(String type,String description) {
				this.type=type;
				this.description=description;
			}
			public String getType() {
				return this.type;
			}
			public String getDesc() {
				return this.description;
			}
		    public static AmzAmountTypeDesc fromValue(String type,String desc) {
				System.out.println("DESCRIPTioN: " + desc + ", " + type);
		    	AmzAmountTypeDesc result = null;
		    	for (AmzAmountTypeDesc typeEnum : AmzAmountTypeDesc.values()) {
		    		if (typeEnum.getType().equals(type)&&typeEnum.getDesc().equals(desc)) result = typeEnum;
		    	}
		    	Assert.notNull(result,"AmazonTransactionLineItem null");
		        return result;
		    }
		}
	}
}