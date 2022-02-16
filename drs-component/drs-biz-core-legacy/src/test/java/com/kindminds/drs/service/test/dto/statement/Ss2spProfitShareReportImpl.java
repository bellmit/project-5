package com.kindminds.drs.service.test.dto.statement;

import com.kindminds.drs.api.v1.model.report.Ss2spProfitShareReport;
import com.kindminds.drs.util.TestUtil;

import java.util.List;

public class Ss2spProfitShareReportImpl implements Ss2spProfitShareReport {

	private int versionNumber;
	private String dateStart;
	private String dateEnd;
	private String isurKcode;
	private String rcvrKcode;
	private String amountSubTotal;
	private String amountTax;
	private String amountTotal;
	private String statementCurrency;
	private List<Ss2spProfitShareReportLineItem> lineItems;

	public Ss2spProfitShareReportImpl(
			int versionNumber,
			String dateStart,
			String dateEnd,
			String isurKcode,
			String rcvrKcode,
			String amountSubTotal,
			String amountTax,
			String amountTotal,
			String statementCurrency,
			List<Ss2spProfitShareReportLineItem> lineItems){
		this.versionNumber = versionNumber;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		this.isurKcode = isurKcode;
		this.rcvrKcode = rcvrKcode;
		this.amountSubTotal = amountSubTotal;
		this.amountTax = amountTax;
		this.amountTotal = amountTotal;
		this.statementCurrency = statementCurrency;
		this.lineItems = lineItems;
	}

	@Override
	public boolean equals(Object obj){
		if(obj instanceof Ss2spProfitShareReport){
			Ss2spProfitShareReport report = (Ss2spProfitShareReport)obj;
			return this.getVersionNumber()==report.getVersionNumber()
				&& this.getDateStart().equals(report.getDateStart())
				&& this.getDateEnd().equals(report.getDateEnd())
				&& this.getIsurKcode().equals(report.getIsurKcode())
				&& this.getRcvrKcode().equals(report.getRcvrKcode())
				&& TestUtil.nullableEquals(this.getAmountSubTotal(),report.getAmountSubTotal())
				&& TestUtil.nullableEquals(this.getAmountTax(),report.getAmountTax())
				&& this.getAmountTotal().equals(report.getAmountTotal())
				&& this.getStatementCurrency().equals(report.getStatementCurrency())
				&& this.getLineItems().equals(report.getLineItems());
		}
		return false;
	}

	@Override
	public String toString() {
		return "Ss2spProfitShareReportImpl [getVersionNumber()=" + getVersionNumber() + ", getDateStart()="
				+ getDateStart() + ", getDateEnd()=" + getDateEnd() + ", getIsurKcode()=" + getIsurKcode()
				+ ", getRcvrKcode()=" + getRcvrKcode() + ", getAmountSubTotal()=" + getAmountSubTotal()
				+ ", getAmountTax()=" + getAmountTax() + ", getAmountTotal()=" + getAmountTotal()
				+ ", getStatementCurrency()=" + getStatementCurrency() + ", getLineItems()=" + getLineItems() + "]";
	}

	@Override
	public int getVersionNumber() {
		return this.versionNumber;
	}

	@Override
	public String getDateStart() {
		return this.dateStart;
	}

	@Override
	public String getDateEnd() {
		return this.dateEnd;
	}

	@Override
	public String getIsurKcode() {
		return this.isurKcode;
	}

	@Override
	public String getRcvrKcode() {
		return this.rcvrKcode;
	}

	@Override
	public String getAmountSubTotal() {
		return this.amountSubTotal;
	}

	@Override
	public String getAmountTax() {
		return this.amountTax;
	}

	@Override
	public String getAmountTotal() {
		return this.amountTotal;
	}

	@Override
	public String getStatementCurrency() {
		return this.statementCurrency;
	}

	@Override
	public List<Ss2spProfitShareReportLineItem> getLineItems() {
		return this.lineItems;
	}

}
