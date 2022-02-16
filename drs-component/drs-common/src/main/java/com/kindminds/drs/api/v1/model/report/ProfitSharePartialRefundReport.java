package com.kindminds.drs.api.v1.model.report;

import java.math.BigDecimal;
import java.util.List;

public interface ProfitSharePartialRefundReport {

    String getDateStart();

    String getDateEnd();

    String getIsurKcode();

    String getRcvrKcode();

    BigDecimal getTotal();

    List<ProfitSharePartialRefundLineItem> getLineItems();

    public interface ProfitSharePartialRefundLineItem {
        String getItemNote();

        String getCurrency();

        BigDecimal getAmount();
    }
}
