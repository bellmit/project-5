package com.kindminds.drs.persist.v1.model.mapping.settleable;

import java.math.BigDecimal;





import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.accounting.DrsTransaction.DrsTransactionLineItemSource;


public class DrsTransactionLineItemSourceImpl implements DrsTransactionLineItemSource {
	
	//@Id ////@Column(name="id")
	private int id;
	//@Column(name="currency_id")
	private Integer currencyId;
	//@Column(name="pretax_principal_price")
	private BigDecimal pretax_principal_price;
	//@Column(name="msdc_retainment")
	private BigDecimal msdc_retainment;
	//@Column(name="marketplace_fee")
	private BigDecimal marketplace_fee;
	//@Column(name="marketplace_fee_non_refundable")
	private BigDecimal marketplace_fee_non_refundable;
	//@Column(name="fulfillment_fee")
	private BigDecimal fulfillment_fee;
	//@Column(name="ssdc_retainment")
	private BigDecimal ssdc_retainment;
	//@Column(name="fca_in_marketside_currency")
	private BigDecimal fca_in_marketside_currency;
	//@Column(name="sp_profit_share")
	private BigDecimal sp_profit_share;
	//@Column(name="refund_fee")
	private BigDecimal refund_fee;

	public DrsTransactionLineItemSourceImpl() {
	}

	public DrsTransactionLineItemSourceImpl(int id, Integer currencyId, BigDecimal pretax_principal_price, BigDecimal msdc_retainment, BigDecimal marketplace_fee, BigDecimal marketplace_fee_non_refundable, BigDecimal fulfillment_fee, BigDecimal ssdc_retainment, BigDecimal fca_in_marketside_currency, BigDecimal sp_profit_share, BigDecimal refund_fee) {
		this.id = id;
		this.currencyId = currencyId;
		this.pretax_principal_price = pretax_principal_price;
		this.msdc_retainment = msdc_retainment;
		this.marketplace_fee = marketplace_fee;
		this.marketplace_fee_non_refundable = marketplace_fee_non_refundable;
		this.fulfillment_fee = fulfillment_fee;
		this.ssdc_retainment = ssdc_retainment;
		this.fca_in_marketside_currency = fca_in_marketside_currency;
		this.sp_profit_share = sp_profit_share;
		this.refund_fee = refund_fee;
	}

	@Override
	public String toString() {
		return "DrsTransactionSettleableItemElementsImpl [getCurrency()=" + getCurrency()
				+ ", getPretaxPrincipalPrice()=" + getPretaxPrincipalPrice() + ", getMsdcRetainment()="
				+ getMsdcRetainment() + ", getMarketplaceFee()=" + getMarketplaceFee()
				+ ", getMarketplaceFeeNonRefundable()=" + getMarketplaceFeeNonRefundable() + ", getFulfillmentFee()="
				+ getFulfillmentFee() + ", getSsdcRetainment()=" + getSsdcRetainment()
				+ ", getFcaInMarketsideCurrency()=" + getFcaInMarketsideCurrency() + ", getSpProfitShare()="
				+ getSpProfitShare() + ", getRefundFee()=" + getRefundFee() + "]";
	}

	@Override
	public Currency getCurrency() {
		return Currency.fromKey(this.currencyId);
	}

	@Override
	public BigDecimal getPretaxPrincipalPrice() {
		return this.pretax_principal_price;
	}
	
	@Override
	public BigDecimal getMsdcRetainment() {
		return this.msdc_retainment;
	}
	
	@Override
	public BigDecimal getMarketplaceFee() {
		return this.marketplace_fee;
	}
	
	@Override
	public BigDecimal getMarketplaceFeeNonRefundable() {
		return this.marketplace_fee_non_refundable;
	}
	
	@Override
	public BigDecimal getFulfillmentFee() {
		return this.fulfillment_fee;
	}
	
	@Override
	public BigDecimal getSsdcRetainment() {
		return this.ssdc_retainment;
	}
	
	@Override
	public BigDecimal getFcaInMarketsideCurrency() {
		return this.fca_in_marketside_currency;
	}
	
	@Override
	public BigDecimal getSpProfitShare() {
		return this.sp_profit_share;
	}
	
	@Override
	public BigDecimal getRefundFee() {
		return this.refund_fee;
	}

}
