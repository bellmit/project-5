package com.kindminds.drs.api.v1.model.close;

import com.kindminds.drs.TransactionLineType;
import com.kindminds.drs.api.v1.model.accounting.StatementLineType;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface BillStatement {

	public Integer getId();
	public String getDisplayName();
	public Date getDateStart();
	public Date getDateEnd();
	public String getIssuerKcode();
	public String getReceiverKcode();
	public String getCurrency();
	public BigDecimal getTotal();
	public BigDecimal getPreviousBalance();
	public BigDecimal getRemittanceIsurToRcvr();
	public BigDecimal getRemittanceRcvrToIsur();
	public BigDecimal getBalance();
	public List<BillStatementProfitShareItem> getProfitShareItems();
	public List<BillStatementLineItem> getLineItems();
	
	public interface BillStatementLineItem{
		public Integer getStatementId();
		public int getLineSeq();
		public StatementLineType getStatementLineType();
		public String getOriginalCurrency();
		public BigDecimal getOriginalAmount();
		public String getStatementCurrency();
		public BigDecimal getStatementAmount();
		public String getProductBase();
		public String getProductName();
		public Integer getQunatity();
		public TransactionLineType getSettleableItem();
		public String getSourcePoId();
		public String getCountry();
		public Date getTransactionDate();
	}
}

