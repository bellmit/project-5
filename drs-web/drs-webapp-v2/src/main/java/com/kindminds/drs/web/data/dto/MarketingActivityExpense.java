package com.kindminds.drs.web.data.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class MarketingActivityExpense {//implements com.kindminds.drs.data.dto.report.ProfitShareSubtractionAdvertisingCostReport{
	private String dateStart;
    private String dateEnd;
    private String isurKcode;
    private String rcvrKcode;
    private BigDecimal total;
    private List<ProfitShareSubtractionMarketingActivityExpenseReportLineItemImpl> marketingActivityExpenseReportLineItems = new ArrayList<>();

    //@Override
    public String getDateStart() {
        return dateStart;
    }

    //@Override
    public String getDateEnd() {
        return dateEnd;
    }

    //@Override
    public String getIsurKcode() {
        return isurKcode;
    }

    //@Override
    public String getRcvrKcode() {
        return rcvrKcode;
    }

    //@Override
    public BigDecimal getTotal() {
        return total;
    }

    //@Override
    public List<ProfitShareSubtractionMarketingActivityExpenseReportLineItemImpl> getLineItems() {
        return marketingActivityExpenseReportLineItems;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public void setIsurKcode(String isurKcode) {
        this.isurKcode = isurKcode;
    }

    public void setRcvrKcode(String rcvrKcode) {
        this.rcvrKcode = rcvrKcode;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public void setLineItems(List<ProfitShareSubtractionMarketingActivityExpenseReportLineItemImpl> marketingActivityExpenseReporttLineItems) {
        this.marketingActivityExpenseReportLineItems = marketingActivityExpenseReporttLineItems;
    }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AdvertisingCost [dateStart=");
		builder.append(dateStart);
		builder.append(", dateEnd=");
		builder.append(dateEnd);
		builder.append(", isurKcode=");
		builder.append(isurKcode);
		builder.append(", rcvrKcode=");
		builder.append(rcvrKcode);
		builder.append(", total=");
		builder.append(total);
		builder.append(", marketingActivityExpenseReportLineItems=");
		builder.append(marketingActivityExpenseReportLineItems);
		builder.append("]");
		return builder.toString();
	}
    
}

