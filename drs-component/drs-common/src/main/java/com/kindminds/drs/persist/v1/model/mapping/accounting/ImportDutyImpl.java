package com.kindminds.drs.persist.v1.model.mapping.accounting;

import java.math.BigDecimal;
import java.util.List;

import com.kindminds.drs.Currency;
import com.kindminds.drs.api.v1.model.accounting.ImportDutyTransaction;


public class ImportDutyImpl implements ImportDutyTransaction {
	
	//@Id ////@Column(name="id")
	private int id;
	//@Column(name="shipment_uns_name")
	private String unsName;
	//@Column(name="transaction_date_utc")
	private String utcDate;
	//@Column(name="country")
	private String country;
	//@Column(name="currency_id")
	private Integer currencyId;
	//@Column(name="amount_total")
	private BigDecimal amountTotal;
	//@Transient
	private List<ImportDutyTransactionLineItem> lineItems;

	public ImportDutyImpl() {
	}

	public ImportDutyImpl(int id, String unsName, String utcDate, String country, Integer currencyId, BigDecimal amountTotal
						  ) {
		this.id = id;
		this.unsName = unsName;
		this.utcDate = utcDate;
		this.country = country;
		this.currencyId = currencyId;
		this.amountTotal = amountTotal;

	}

	public void setLineItems(List<ImportDutyTransactionLineItem> items){
		this.lineItems = items;
	}

	@Override
	public String toString() {
		return "ImportDutyImpl [getUnsName()=" + getUnsName() + ", getUtcDate()=" + getUtcDate() + ", getDstCountry()="
				+ getDstCountry() + ", getCurrency()=" + getCurrency() + ", getTotal()=" + getTotal()
				+ ", isEditable()=" + isEditable() + ", getLineItems()=" + getLineItems() + "]";
	}

	@Override
	public String getUnsName() {
		return this.unsName;
	}

	@Override
	public String getUtcDate() {
		return this.utcDate;
	}

	@Override
	public String getDstCountry() {
		return this.country;
	}

	@Override
	public String getCurrency() {
		return Currency.fromKey(this.currencyId).name();
	}

	@Override
	public String getTotal() {
		return this.amountTotal.stripTrailingZeros().toPlainString();
	}

	@Override
	public Boolean isEditable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ImportDutyTransactionLineItem> getLineItems() {
		return this.lineItems;
	}

}
