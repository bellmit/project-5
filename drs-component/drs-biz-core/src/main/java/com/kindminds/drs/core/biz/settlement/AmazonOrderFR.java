package com.kindminds.drs.core.biz.settlement;

import com.kindminds.drs.api.v1.model.amazon.AmazonTransaction;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public class AmazonOrderFR extends AmazonOrder {

    public AmazonOrderFR(Integer id, Date transactionDate, String type, String source, String sourceId, String sku) {
        super(id, transactionDate, type, source, sourceId, sku);
    }

    public AmazonOrderFR(Integer id, Date transactionDate, String type, String source, String sourceId, String sku, String description) {
        super(id, transactionDate, type, source, sourceId, sku, description);
    }

    @Override
    protected BigDecimal getPretaxPrincipalPrice(Map<AmazonTransaction.AmazonTransactionLineItem.AmzAmountTypeDesc,BigDecimal> unitAmount) {
        BigDecimal principal = unitAmount.containsKey(AmazonTransaction.AmazonTransactionLineItem.AmzAmountTypeDesc.PRICE_PRINCIPAL)?unitAmount.get(AmazonTransaction.AmazonTransactionLineItem.AmzAmountTypeDesc.PRICE_PRINCIPAL):BigDecimal.ZERO;
        BigDecimal promotion = unitAmount.containsKey(AmazonTransaction.AmazonTransactionLineItem.AmzAmountTypeDesc.PROMOTION_PRINCIPAL)?unitAmount.get(AmazonTransaction.AmazonTransactionLineItem.AmzAmountTypeDesc.PROMOTION_PRINCIPAL):BigDecimal.ZERO;
        BigDecimal result = principal.subtract(promotion);
        Assert.isTrue(result.signum()>=0,"result.signum()>=0");
        return result;
    }
}
