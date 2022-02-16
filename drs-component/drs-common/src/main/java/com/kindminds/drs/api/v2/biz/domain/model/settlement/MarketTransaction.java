package com.kindminds.drs.api.v2.biz.domain.model.settlement;

import com.kindminds.drs.api.v1.model.accounting.DrsTransaction;

import java.util.Date;

//todo arhtur change it to interface
public abstract class MarketTransaction {

    private Integer id;
    private Date transactionDate;
    private String type;
    private String source;
    private String sourceId;
    private String sku;
    private String description;

    public abstract DrsTransaction createDrsTransaction();

    public Integer getId() {
        return id;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public String getType() {
        return type;
    }

    public String getSource() {
        return source;
    }

    public String getSourceId() {
        return sourceId;
    }

    public String getSku() {
        return sku;
    }

    public String getDescription() {
        return description;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
