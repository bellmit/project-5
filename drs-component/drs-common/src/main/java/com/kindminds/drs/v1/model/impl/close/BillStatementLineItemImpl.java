package com.kindminds.drs.v1.model.impl.close;

import com.kindminds.drs.TransactionLineType;
import com.kindminds.drs.api.v1.model.accounting.StatementLineType;
import com.kindminds.drs.api.v1.model.close.BillStatement.BillStatementLineItem;
import com.kindminds.drs.util.TestUtil;

import java.math.BigDecimal;
import java.util.Date;

public class BillStatementLineItemImpl implements BillStatementLineItem {

	private Integer statementId;
	private int lineSeq;
	private StatementLineType statementLineType;
	private String originCurrency;
	private BigDecimal originAmount;
	private String statementCurrency;
	private BigDecimal statementAmount;
	private String productBase;
	private String productName;
	private Integer quantity;
	private TransactionLineType settleableItem;
	private String sourcePoId;
	private String countryName;
	
	public BillStatementLineItemImpl(
			Integer statementId,
			int lineSeq,
			StatementLineType statementLineType,
			String originCurrency,
			String originAmount,
			String statementCurrency,
			String statementAmount,
			String productBaseCode,
			String productName,
			Integer quantity,
			TransactionLineType settleableItem,
			String sourcePoId,
			String countryName) {
		this.statementId = statementId;
		this.lineSeq = lineSeq;
		this.statementLineType = statementLineType;
		this.originCurrency = originCurrency;
		this.originAmount = originAmount==null?null:new BigDecimal(originAmount);
		this.statementCurrency = statementCurrency;
		this.statementAmount = statementAmount==null?null:new BigDecimal(statementAmount);
		this.productBase = productBaseCode;
		this.productName = productName;
		this.quantity = quantity;
		this.settleableItem = settleableItem;
		this.sourcePoId = sourcePoId;
		this.countryName = countryName;
	}
	
	@Override
	public boolean equals(Object obj){
		if ( obj instanceof BillStatementLineItem ){
			BillStatementLineItem lineItem = ((BillStatementLineItem) obj);
			boolean srcPoIdEqual = false;
			if(this.getSourcePoId()==null&&lineItem.getSourcePoId()==null) srcPoIdEqual=true;
			else if(this.getSourcePoId()==null) srcPoIdEqual=false;
			else if(this.getSourcePoId().equals(lineItem.getSourcePoId())) srcPoIdEqual=true;
			return this.getStatementId().equals(lineItem.getStatementId())
				&& this.getLineSeq()==lineItem.getLineSeq()
				&& this.getStatementLineType()==lineItem.getStatementLineType()
				&& TestUtil.nullableEquals(this.getOriginalCurrency(),lineItem.getOriginalCurrency())
				&& TestUtil.nullableEquals(this.getOriginalAmount(),lineItem.getOriginalAmount())
				&& TestUtil.nullableEquals(this.getStatementCurrency(),lineItem.getStatementCurrency())
				&& TestUtil.nullableEquals(this.getStatementAmount(),lineItem.getStatementAmount())
				&& TestUtil.nullableEquals(this.getProductBase(),lineItem.getProductBase())
				&& TestUtil.nullableEquals(this.getProductName(),lineItem.getProductName())
				&& TestUtil.nullableEquals(this.getQunatity(),lineItem.getQunatity())
				&& TestUtil.nullableEquals(this.getSettleableItem(),lineItem.getSettleableItem())
				&& srcPoIdEqual
				&& this.getCountry().equals(lineItem.getCountry());
		}
		else {
	      return false;
	    }
	}
	
	@Override
	public String toString() {
		return "BillStatementLineItemImpl [getStatementId()=" + getStatementId() + ", getLineSeq()="
				+ getLineSeq() + ", getStatementLineType()=" + getStatementLineType() + ", getOriginalCurrency()="
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
		return this.statementLineType;
	}
	
	@Override
	public String getOriginalCurrency() {
		return this.originCurrency;
	}
	
	@Override
	public BigDecimal getOriginalAmount() {
		return this.originAmount;
	}
	
	@Override
	public String getStatementCurrency() {
		return this.statementCurrency;
	}
	
	@Override
	public BigDecimal getStatementAmount() {
		return this.statementAmount;
	}
	
	@Override
	public String getProductBase() {
		return this.productBase;
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
		return this.settleableItem;
	}
	
	@Override
	public String getSourcePoId() {
		return this.sourcePoId;
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
