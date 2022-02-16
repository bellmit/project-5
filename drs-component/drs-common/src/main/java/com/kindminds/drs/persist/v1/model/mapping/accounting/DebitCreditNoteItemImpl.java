package com.kindminds.drs.persist.v1.model.mapping.accounting;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;






import com.kindminds.drs.api.v1.model.accounting.DebitCreditNote.DebitCreditNoteItem;


public class DebitCreditNoteItemImpl implements DebitCreditNoteItem {
	
	//@Id //@Column(name="invoice_number")
	private String invoiceNumber;
	//@Column(name="statement_name")
	private String statementName;
	//@Column(name="period_start")
	private String periodStart;
	//@Column(name="period_end")
	private String periodEnd;
	//@Column(name="invoice_total")
	private BigDecimal total;

	private List<DebitCreditNoteSkuLine> skuLines=null;

	public DebitCreditNoteItemImpl() {
	}

	public DebitCreditNoteItemImpl(String invoiceNumber, String statementName, String periodStart, String periodEnd, BigDecimal total) {
		this.invoiceNumber = invoiceNumber;
		this.statementName = statementName;
		this.periodStart = periodStart;
		this.periodEnd = periodEnd;
		this.total = total;
	}

	public void setSkuLines(List<DebitCreditNoteSkuLine> lines){
		this.skuLines = lines;
	}

	@Override
	public String toString() {
		return "DebitCreditNoteItemImpl [getDescription()=" + getDescription()
				+ ", getInvoiceNumber()=" + getInvoiceNumber()
				+ ", getTotal()=" + getTotal() + ", getSkuLines()="
				+ getSkuLines() + "]";
	}

	@Override
	public String getDescription() {
		return this.statementName+", Period(UTC): "+this.periodStart+"~"+this.periodEnd;
	}

	@Override
	public String getInvoiceNumber() {
		return this.invoiceNumber;
	}

	@Override
	public String getTotal() {
		return this.total.setScale(2, RoundingMode.HALF_UP).toPlainString();
	}

	@Override
	public List<DebitCreditNoteSkuLine> getSkuLines() {
		return this.skuLines;
	}

}
