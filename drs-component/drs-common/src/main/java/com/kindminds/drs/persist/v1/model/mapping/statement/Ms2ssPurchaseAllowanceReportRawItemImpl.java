package com.kindminds.drs.persist.v1.model.mapping.statement;

import java.math.BigDecimal;


import com.kindminds.drs.api.v1.model.report.Ms2ssPurchaseAllowanceReport;
import org.springframework.util.Assert;

import com.kindminds.drs.Currency;



public class Ms2ssPurchaseAllowanceReportRawItemImpl implements Ms2ssPurchaseAllowanceReport.Ms2ssPurchaseAllowanceReportRawItem {

	//@Id ////@Column(name="id")
	private int id;	
	//@Column(name="sku")
	private String sku;
	//@Column(name="item_name")
	private String itemName;
	//@Column(name="currency_name")
	private String currencyName;
	//@Column(name="amount")
	private BigDecimal amount;
	//@Column(name="settleable_item_id")
	private Integer settleableItemId;

	public Ms2ssPurchaseAllowanceReportRawItemImpl() {
	}

	public Ms2ssPurchaseAllowanceReportRawItemImpl(int id, String sku, String itemName, String currencyName, BigDecimal amount, Integer settleableItemId) {
		this.id = id;
		this.sku = sku;
		this.itemName = itemName;
		this.currencyName = currencyName;
		this.amount = amount;
		this.settleableItemId = settleableItemId;
	}

	@Override
	public String getSku() {
		return this.sku;
	}

	@Override
	public String getItemName() {
		return this.itemName;
	}

	@Override
	public Currency getStatementCurrency() {
		Currency c = Currency.valueOf(this.currencyName);
		Assert.notNull(c);
		return c;
	}

	@Override
	public String getStatementAmountStr() {
		return this.amount.setScale(this.getStatementCurrency().getScale()).toString();
	}

	@Override
	public Integer getSettleableItemId() {
		return this.settleableItemId;
	}

	@Override
	public BigDecimal getStatementAmount() {
		return this.amount;
	}

	@Override
	public String toString() {
		return "Ms2ssPurchaseAllowanceReportRawItemImpl [getSku()=" + getSku()
				+ ", getItemName()=" + getItemName()
				+ ", getStatementCurrency()=" + getStatementCurrency()
				+ ", getStatementAmountStr()=" + getStatementAmountStr()
				+ ", getSettleableItemId()=" + getSettleableItemId()
				+ ", getStatementAmount()=" + getStatementAmount() + "]";
	}
	
	

}
