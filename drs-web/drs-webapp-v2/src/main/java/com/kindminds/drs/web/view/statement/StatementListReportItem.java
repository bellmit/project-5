package com.kindminds.drs.web.view.statement;



import java.io.Serializable;
import com.kindminds.drs.Currency;

public class StatementListReportItem implements Serializable{

	private static final long serialVersionUID = 5546735735318988902L;
	private String statementId;
	private String periodStartUtc;
	private String periodEndUtc;
	protected Currency currency;
	protected String total;
	protected String balance;
	protected String invoiceFromSsdc;
	protected String invoiceFromSupplier;

	
	public StatementListReportItem() {
		
	}
	
	
	public String getStatementId() {
		return this.statementId;
	}
	
	
	public String getPeriodStartUtc() {
		return this.periodStartUtc;
	}
	
	
	public String getPeriodEndUtc() {
		return this.periodEndUtc;
	}
	
	
	public String getCurrency() {
		return this.currency.name();
	}
	
	
	public String getTotal() {
		return total ;
	}

	
	public String getBalance() {
		return balance;
	}

	public String getInvoiceFromSsdc() {
		return invoiceFromSsdc ;
	}

	
	public String getInvoiceFromSupplier() {
		return invoiceFromSupplier;
	}
	
	

}

