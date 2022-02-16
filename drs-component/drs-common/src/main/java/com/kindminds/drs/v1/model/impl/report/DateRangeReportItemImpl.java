package com.kindminds.drs.v1.model.impl.report;

import com.kindminds.drs.api.v1.model.report.DateRangeReportItem;
import org.apache.commons.csv.CSVRecord;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

public class DateRangeReportItemImpl implements DateRangeReportItem {
	
	private Integer id;
	private Integer marketplaceId;
	private Date importDate;
	private String dateTime;
	private String settlementId;
	private String type;
	private String orderId;
	private String sku;
	private String description; 
	private Integer quantity = 0;
	private String marketplace;
	private String accountType = "";
	private String fulfillment;
	private String orderCity;
	private String orderState;
	private String orderPostal;
	private String taxCollectionModel = null;
	private BigDecimal productSales;
	private BigDecimal productSalesTax = null;
	private BigDecimal shippingCredits;
	private BigDecimal shippingCreditsTax = null;
	private BigDecimal giftWrapCredits;
	private BigDecimal giftWrapCreditsTax = null;
	private BigDecimal promotionalRebates;
	private BigDecimal promotionalRebatesTax = null;
	private BigDecimal salesTaxCollected = null;
	private BigDecimal marketplaceFacilitatorTax = null;
	private BigDecimal marketplaceWithheldTax;
	private BigDecimal sellingFees;
	private BigDecimal fbaFees;
	private BigDecimal otherTransactionFees;
	private BigDecimal other;
	private BigDecimal total;
	
	public DateRangeReportItemImpl(int marketplaceId, CSVRecord record) {
        this.marketplaceId = marketplaceId;
        this.dateTime = record.get(0);
        this.settlementId = record.get(1);
        this.type = record.get(2);
        this.orderId = record.get(3);
        this.sku = record.get(4);
        this.description = record.get(5);
        if (StringUtils.hasText(record.get(6))) {
            this.quantity = Integer.valueOf(record.get(6));
        }

        this.marketplace = record.get(7);
        this.fulfillment = record.get(8);
        this.orderCity = record.get(9);
        this.orderState = record.get(10);
        this.orderPostal = record.get(11);

	    if (marketplaceId == 1) {   //US report
	    	this.accountType = record.get(8);
			this.fulfillment = record.get(9);
			this.orderCity = record.get(10);
			this.orderState = record.get(11);
			this.orderPostal = record.get(12);
            this.taxCollectionModel = record.get(13);
            this.productSales = new BigDecimal(formatCurrency(record.get(14)));
            this.productSalesTax = new BigDecimal(formatCurrency(record.get(15)));
            this.shippingCredits = new BigDecimal(formatCurrency(record.get(16)));
            this.shippingCreditsTax = new BigDecimal(formatCurrency(record.get(17)));
            this.giftWrapCredits = new BigDecimal(formatCurrency(record.get(18)));
            this.giftWrapCreditsTax = new BigDecimal(formatCurrency(record.get(19)));
            this.promotionalRebates = new BigDecimal(formatCurrency(record.get(20)));
            this.promotionalRebatesTax = new BigDecimal(formatCurrency(record.get(21)));
            this.marketplaceFacilitatorTax = new BigDecimal(formatCurrency(record.get(22)));
            this.sellingFees = new BigDecimal(formatCurrency(record.get(23)));
            this.fbaFees = new BigDecimal(formatCurrency(record.get(24)));
            this.otherTransactionFees = new BigDecimal(formatCurrency(record.get(25)));
            this.other = new BigDecimal(formatCurrency(record.get(26)));
            this.total = new BigDecimal(formatCurrency(record.get(27)));

        } else if(marketplaceId == 5){    //CA report
            this.taxCollectionModel = record.get(12);
            this.productSales = new BigDecimal(formatCurrency(record.get(13)));
            this.productSalesTax = new BigDecimal(formatCurrency(record.get(14)));
            this.shippingCredits = new BigDecimal(formatCurrency(record.get(15)));
            this.shippingCreditsTax = new BigDecimal(formatCurrency(record.get(16)));
            this.giftWrapCredits = new BigDecimal(formatCurrency(record.get(17)));
            this.giftWrapCreditsTax = new BigDecimal(formatCurrency(record.get(18)));
            this.promotionalRebates = new BigDecimal(formatCurrency(record.get(19)));
            this.promotionalRebatesTax = new BigDecimal(formatCurrency(record.get(20)));
            this.sellingFees = new BigDecimal(formatCurrency(record.get(21)));
            this.fbaFees = new BigDecimal(formatCurrency(record.get(22)));
            this.otherTransactionFees = new BigDecimal(formatCurrency(record.get(23)));
            this.other = new BigDecimal(formatCurrency(record.get(24)));
            this.total = new BigDecimal(formatCurrency(record.get(25)));

        } else {    //UK and EU reports
			this.taxCollectionModel = record.get(12);
			this.productSales = new BigDecimal(formatCurrency(record.get(13)));
			this.productSalesTax = new BigDecimal(formatCurrency(record.get(14)));
			this.shippingCredits = new BigDecimal(formatCurrency(record.get(15)));
			this.shippingCreditsTax = new BigDecimal(formatCurrency(record.get(16)));
			this.giftWrapCredits = new BigDecimal(formatCurrency(record.get(17)));
			this.giftWrapCreditsTax = new BigDecimal(formatCurrency(record.get(18)));
			this.promotionalRebates = new BigDecimal(formatCurrency(record.get(19)));
			this.promotionalRebatesTax = new BigDecimal(formatCurrency(record.get(20)));
			this.marketplaceWithheldTax = new BigDecimal(formatCurrency(record.get(21)));
			this.sellingFees = new BigDecimal(formatCurrency(record.get(22)));
			this.fbaFees = new BigDecimal(formatCurrency(record.get(23)));
			this.otherTransactionFees = new BigDecimal(formatCurrency(record.get(24)));
			this.other = new BigDecimal(formatCurrency(record.get(25)));
			this.total = new BigDecimal(formatCurrency(record.get(26)));

		}  //else {    //old EU reports
//            this.productSales = new BigDecimal(formatCurrency(record.get(12)));
//
//            this.shippingCredits = new BigDecimal(formatCurrency(record.get(13)));
//            this.giftWrapCredits = new BigDecimal(formatCurrency(record.get(14)));
//            this.promotionalRebates = new BigDecimal(formatCurrency(record.get(15)));
//
//            this.sellingFees = new BigDecimal(formatCurrency(record.get(16)));
//            this.otherTransactionFees = new BigDecimal(formatCurrency(record.get(17)));
//            this.fbaFees = new BigDecimal(formatCurrency(record.get(18)));
//            this.other = new BigDecimal(formatCurrency(record.get(19)));
//            this.total = new BigDecimal(formatCurrency(record.get(20)));
//        }
	}

    private String formatCurrency(String currency) {

        //account for different currency format in other countries that might use "," as decimal
		String amount = currency.replaceAll("[^-0-9]", "");


		if (amount.length() > 2) {
			int decimalIndex = amount.length() - 2;
			amount = amount.substring(0, decimalIndex) + "." + amount.substring(decimalIndex);
		}
		return amount;
	}

	@Override
	public Integer getId() {
		return id;
	}
	
	@Override
	public Integer getMarketplaceId() {
		return marketplaceId;
	}

	@Override
	public Date getImportDate() {
		return importDate;
	}

	@Override
	public String getDateTime() {
		return dateTime;
	}

	@Override
	public String getSettlementId() {
		return settlementId;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public String getOrderId() {
		return orderId;
	}

	@Override
	public String getSku() {
		return sku;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public Integer getQuantity() {
		return quantity;
	}

	@Override
	public String getMarketplace() {
		return marketplace;
	}

	@Override
	public String getAccountType() {
		return accountType;
	}

	@Override
	public String getFulfillment() {
		return fulfillment;
	}

	@Override
	public String getOrderCity() {
		return orderCity;
	}

	@Override
	public String getOrderState() {
		return orderState;
	}

	@Override
	public String getOrderPostal() {
		return orderPostal;
	}

	@Override
	public BigDecimal getProductSales() {
		return productSales;
	}

	@Override
	public BigDecimal getShippingCredits() {
		return shippingCredits;
	}

	@Override
	public BigDecimal getGiftWrapCredits() {
		return giftWrapCredits;
	}

	@Override
	public BigDecimal getPromotionalRebates() {
		return promotionalRebates;
	}

	@Override
	public BigDecimal getSalesTaxCollected() {
		return salesTaxCollected;
	}

	@Override
	public BigDecimal getMarketplaceFacilitatorTax() {
		return marketplaceFacilitatorTax;
	}

	@Override
	public BigDecimal getSellingFees() {
		return sellingFees;
	}

	@Override
	public BigDecimal getFbaFees() {
		return fbaFees;
	}

	@Override
	public BigDecimal getOtherTransactionFees() {
		return otherTransactionFees;
	}

	@Override
	public BigDecimal getOther() {
		return other;
	}

	@Override
	public BigDecimal getTotal() {
		return total;
	}

    @Override
    public String getTaxCollectionModel() {
        return taxCollectionModel;
    }

    @Override
    public BigDecimal getProductSalesTax() {
        return productSalesTax;
    }

    @Override
    public BigDecimal getShippingCreditsTax() {
        return shippingCreditsTax;
    }

    @Override
    public BigDecimal getGiftWrapCreditsTax() {
        return giftWrapCreditsTax;
    }

    @Override
    public BigDecimal getPromotionalRebatesTax() {
        return promotionalRebatesTax;
    }
}
