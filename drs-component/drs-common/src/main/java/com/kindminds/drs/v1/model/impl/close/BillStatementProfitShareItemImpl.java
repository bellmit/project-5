package com.kindminds.drs.v1.model.impl.close;

import com.kindminds.drs.api.v1.model.close.BillStatementProfitShareItem;

public class BillStatementProfitShareItemImpl implements BillStatementProfitShareItem {
	
	private Integer statementId;
	private Integer lineSeq;
	private String sourceCountry;
	private String sourceCurrency;
	private String sourceAmountUntaxed;
	private String destinationCurrency;
	private String destinationAmountUntaxed;
	private String exchangeRate;
	
	public BillStatementProfitShareItemImpl(
			Integer statementId,
			Integer lineSeq,
			String sourceCountry,
			String sourceCurrency,
			String sourceAmountUntaxed,
			String destinationCurrency,
			String destinationAmountUntaxed,
			String exchangeRate
			){
		this.statementId=statementId;
		this.lineSeq=lineSeq;
		this.sourceCountry=sourceCountry;
		this.sourceCurrency=sourceCurrency;
		this.sourceAmountUntaxed=sourceAmountUntaxed;
		this.destinationCurrency=destinationCurrency;
		this.destinationAmountUntaxed=destinationAmountUntaxed;
		this.exchangeRate=exchangeRate;
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj instanceof BillStatementProfitShareItem){
			BillStatementProfitShareItem bsps = (BillStatementProfitShareItem)obj;
			return this.getStatementId().equals(bsps.getStatementId())
				&& this.getLineSeq().equals(bsps.getLineSeq())
				&& this.getSourceCountry().equals(bsps.getSourceCountry())
				&& this.getSourceCurrency().equals(bsps.getSourceCurrency())
				&& this.getSourceAmountUntaxed().equals(bsps.getSourceAmountUntaxed())
				&& this.getDestinationCurrency().equals(bsps.getDestinationCurrency())
				&& this.getDestinationAmountUntaxed().equals(bsps.getDestinationAmountUntaxed())
				&& this.getExchangeRate().equals(bsps.getExchangeRate());
		}
		return false;
	}

	@Override
	public String toString() {
		return "BillStatementProfitShareItemImpl [getStatementId()=" + getStatementId() + ", getLineSeq()="
				+ getLineSeq() + ", getSourceCountry()=" + getSourceCountry() + ", getSourceCurrency()="
				+ getSourceCurrency() + ", getSourceAmountUntaxed()=" + getSourceAmountUntaxed()
				+ ", getDestinationCurrency()=" + getDestinationCurrency() + ", getDestinationAmountUntaxed()="
				+ getDestinationAmountUntaxed() + ", getExchangeRate()=" + getExchangeRate() + "]";
	}

	@Override
	public Integer getStatementId() {
		return this.statementId;
	}

	@Override
	public Integer getLineSeq() {
		return this.lineSeq;
	}

	@Override
	public String getSourceCountry() {
		return this.sourceCountry;
	}

	@Override
	public String getSourceCurrency() {
		return this.sourceCurrency;
	}

	@Override
	public String getSourceAmountUntaxed() {
		return this.sourceAmountUntaxed;
	}

	@Override
	public String getDestinationCurrency() {
		return this.destinationCurrency;
	}

	@Override
	public String getDestinationAmountUntaxed() {
		return this.destinationAmountUntaxed;
	}

	@Override
	public String getExchangeRate() {
		return this.exchangeRate;
	}

}
