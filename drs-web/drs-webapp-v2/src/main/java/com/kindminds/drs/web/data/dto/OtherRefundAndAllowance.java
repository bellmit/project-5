package com.kindminds.drs.web.data.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class OtherRefundAndAllowance {
    private String dateStart;
    private String dateEnd;
    private String isurKcode;
    private String rcvrKcode;
    private BigDecimal total;
    private List<ProfitShareSubtractionOtherRefundReportLineItemImpl> lineItems = new ArrayList<>();

    public String getDateStart() {
        return dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public String getIsurKcode() {
        return isurKcode;
    }

    public String getRcvrKcode() {
        return rcvrKcode;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public List<ProfitShareSubtractionOtherRefundReportLineItemImpl> getLineItems() {
        return lineItems;
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

    public void setLineItems(List<ProfitShareSubtractionOtherRefundReportLineItemImpl> lineItems) {
        this.lineItems = lineItems;
    }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("OtherRefundAndAllowance [dateStart=");
		builder.append(dateStart);
		builder.append(", dateEnd=");
		builder.append(dateEnd);
		builder.append(", isurKcode=");
		builder.append(isurKcode);
		builder.append(", rcvrKcode=");
		builder.append(rcvrKcode);
		builder.append(", total=");
		builder.append(total);
		builder.append(", lineItems=");
		builder.append(lineItems);
		builder.append("]");
		return builder.toString();
	}
	
}
