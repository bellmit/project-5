package com.kindminds.drs.web.data.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.kindminds.drs.api.v1.model.report.ProfitShareSubtractionAdvertisingCostReport;
import com.kindminds.drs.web.data.dto.ProfitShareSubtractionAdvertisingCostReportLineItemImpl;


public class AdvertisingCost {//implements com.kindminds.drs.data.dto.report.ProfitShareSubtractionAdvertisingCostReport{
	private String dateStart;
    private String dateEnd;
    private String isurKcode;
    private String rcvrKcode;
    private BigDecimal total;
    private List<ProfitShareSubtractionAdvertisingCostReportLineItemImpl> advertistingCostReportLineItems = new ArrayList<>();

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
    public List<ProfitShareSubtractionAdvertisingCostReportLineItemImpl> getLineItems() {
        return advertistingCostReportLineItems;
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

    public void setLineItems(List<ProfitShareSubtractionAdvertisingCostReportLineItemImpl> advertistingCostReportLineItems) {
        this.advertistingCostReportLineItems = advertistingCostReportLineItems;
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
		builder.append(", advertistingCostReportLineItems=");
		builder.append(advertistingCostReportLineItems);
		builder.append("]");
		return builder.toString();
	}
    
}

