package com.kindminds.drs.api.v1.model.amazon;

import java.math.BigDecimal;
import java.util.Date;

public interface AmazonReimbursementInfo {

    Date getApprovalDate();
    String getReimbursementId();
    String getSku();
    String getCurrencyUnit();
    String getAmazonOrderId();
    String getReason();
    BigDecimal getAmountPerUnit();
    BigDecimal getAmountTotal();
    BigDecimal getQuantityReimbursementCash();
    BigDecimal getQuantityReimbursementInventory();
    String getOriginalReimbursementId();
    String getOriginalReimbursementType();



}
