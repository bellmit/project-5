package com.kindminds.drs.persist.v1.model.mapping.close;

import java.math.BigDecimal;
import java.util.Date;





import com.kindminds.drs.Currency;
import com.kindminds.drs.TransactionLineType;
import com.kindminds.drs.api.v1.model.accounting.StatementLineType;
import com.kindminds.drs.api.v1.model.close.BillStatement;


public class BillStatementLineItemImpl implements BillStatement.BillStatementLineItem {
	
	//@Id ////@Column(name="id")
	private int id;
	//@Column(name="statement_id")
	private Integer statementId;
	//@Column(name="line_seq")
	private int lineSeq;
	//@Column(name="statement_line_type")
	private String statementLineType;
	//@Column(name="original_currency")
	private String originCurrency;
	//@Column(name="original_amount")
	private BigDecimal originAmount;
	//@Column(name="statement_currency")
	private String statementCurrency;
	//@Column(name="statement_amount")
	private BigDecimal statementAmount;
	//@Column(name="product_base_code")
	private String productBaseCode;
	//@Column(name="product_name")
	private String productName;
	//@Column(name="quantity")
	private Integer quantity;
	//@Column(name="settleable_item")
	private String settleableItem;
	//@Column(name="reference")
	private String reference;
	//@Column(name="country_name")
	private String countryName;

	public BillStatementLineItemImpl(){

	}

	public BillStatementLineItemImpl(int id, Integer statementId, int lineSeq, String statementLineType, String originCurrency, BigDecimal originAmount, String statementCurrency, BigDecimal statementAmount, String productBaseCode, String productName, Integer quantity, String settleableItem, String reference, String countryName) {
		this.id = id;
		this.statementId = statementId;
		this.lineSeq = lineSeq;
		this.statementLineType = statementLineType;
		this.originCurrency = originCurrency;
		this.originAmount = originAmount;
		this.statementCurrency = statementCurrency;
		this.statementAmount = statementAmount;
		this.productBaseCode = productBaseCode;
		this.productName = productName;
		this.quantity = quantity;
		this.settleableItem = settleableItem;
		this.reference = reference;
		this.countryName = countryName;
	}


	public void setStatementCurrency(Currency stmntCurrency) {
		this.statementCurrency=stmntCurrency.name();
	}

	public void setStatementAmount(BigDecimal stmntAmount) {
		this.statementAmount=stmntAmount;
	}

	public void setReference(String reference){
		this.reference = reference;
	}

	@Override
	public String toString() {
		return "BillStatementLineItemImpl [getStatementId()=" + getStatementId() + ", getLineSeq()=" + getLineSeq()
				+ ", getStatementLineType()=" + getStatementLineType() + ", getOriginalCurrency()="
				+ getOriginalCurrency() + ", getOriginalAmount()=" + getOriginalAmount() + ", getStatementCurrency()="
				+ getStatementCurrency() + ", getStatementAmount()=" + getStatementAmount() + ", getProductBase()="
				+ getProductBase() + ", getProductName()=" + getProductName() + ", getQunatity()=" + getQunatity()
				+ ", getSettleableItem()=" + getSettleableItem() + ", getSourcePoId()=" + getSourcePoId()
				+ ", getCountry()=" + getCountry() + ", getTransactionDate()=" + getTransactionDate() + "]";
	}
	
	@Override
	public Integer getStatementId() {
		return this.statementId;
	}
	
	@Override
	public int getLineSeq() {
		return this.lineSeq;
	}
	
	@Override
	public StatementLineType getStatementLineType() {
		return StatementLineType.fromName(this.statementLineType);
	}
	
	@Override
	public String getOriginalCurrency() {
		return this.originCurrency==null?null:Currency.valueOf(this.originCurrency).name();
	}
	
	@Override
	public BigDecimal getOriginalAmount() {
		return this.originAmount;
	}
	
	@Override
	public String getStatementCurrency() {
		return this.statementCurrency==null?null:Currency.valueOf(this.statementCurrency).name();
	}
	
	@Override
	public BigDecimal getStatementAmount() {
		return this.statementAmount;
	}
	
	@Override
	public String getProductBase() {
		return this.productBaseCode;
	}
	
	@Override
	public String getProductName() {
		return this.productName;
	}
	
	@Override
	public Integer getQunatity() {
		return this.quantity;
	}
	
	@Override
	public TransactionLineType getSettleableItem() {
		return this.settleableItem==null?null:TransactionLineType.fromName(this.settleableItem);
	}
	
	@Override
	public String getSourcePoId() {
		return this.reference;
	}
	
	@Override
	public String getCountry() {
		return this.countryName;
	}
	
	@Override
	public Date getTransactionDate() {
		return null;
	}

}
