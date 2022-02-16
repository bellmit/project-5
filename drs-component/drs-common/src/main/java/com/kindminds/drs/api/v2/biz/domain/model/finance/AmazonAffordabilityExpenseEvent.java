package com.kindminds.drs.api.v2.biz.domain.model.finance;

import java.math.BigDecimal;
import java.util.Date;

public class AmazonAffordabilityExpenseEvent {
    private int id;
    private String amazonOrderId;
    private Date postedDate;
    private String marketplaceId;
    private String transactionType;
    private String expenseCurrency;
    private BigDecimal baseExpense;
    private BigDecimal taxTypeCGST;
    private BigDecimal taxTypeSGST;
    private BigDecimal taxTypeIGST;
    private BigDecimal totalExpense;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAmazonOrderId() {
        return amazonOrderId;
    }

    public void setAmazonOrderId(String amazonOrderId) {
        this.amazonOrderId = amazonOrderId;
    }

    public Date getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
    }

    public String getMarketplaceId() {
        return marketplaceId;
    }

    public void setMarketplaceId(String marketplaceId) {
        this.marketplaceId = marketplaceId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getExpenseCurrency() {
        return expenseCurrency;
    }

    public void setExpenseCurrency(String expenseCurrency) {
        this.expenseCurrency = expenseCurrency;
    }

    public BigDecimal getBaseExpense() {
        return baseExpense;
    }

    public void setBaseExpense(BigDecimal baseExpense) {
        this.baseExpense = baseExpense;
    }

    public BigDecimal getTaxTypeCGST() {
        return taxTypeCGST;
    }

    public void setTaxTypeCGST(BigDecimal taxTypeCGST) {
        this.taxTypeCGST = taxTypeCGST;
    }

    public BigDecimal getTaxTypeSGST() {
        return taxTypeSGST;
    }

    public void setTaxTypeSGST(BigDecimal taxTypeSGST) {
        this.taxTypeSGST = taxTypeSGST;
    }

    public BigDecimal getTaxTypeIGST() {
        return taxTypeIGST;
    }

    public void setTaxTypeIGST(BigDecimal taxTypeIGST) {
        this.taxTypeIGST = taxTypeIGST;
    }

    public BigDecimal getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(BigDecimal totalExpense) {
        this.totalExpense = totalExpense;
    }
}
