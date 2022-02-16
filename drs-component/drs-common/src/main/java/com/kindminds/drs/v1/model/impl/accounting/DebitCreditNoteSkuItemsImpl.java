package com.kindminds.drs.v1.model.impl.accounting;


import java.math.BigDecimal;
import java.math.RoundingMode;

import com.kindminds.drs.api.v1.model.accounting.DebitCreditNote.DebitCreditNoteSkuItems;

public class DebitCreditNoteSkuItemsImpl implements DebitCreditNoteSkuItems{

    private String skuCode;
    private String invoiceNumber;
    private BigDecimal sum;

    public DebitCreditNoteSkuItemsImpl(String skuCode,String invoiceNumber,BigDecimal sum){
        super();
        this.skuCode = skuCode;
        this.invoiceNumber = invoiceNumber;
        this.sum = sum;
    }

    @Override
    public String toString() {
        return "DebitCreditNoteSkuItemsImpl [getSkuCode()=" + getSkuCode() + ", getInvoiceNumber()="
                + getInvoiceNumber() + ", getSumForSku()=" + getSumForSku() + "]";
    }

    @Override
    public String getSkuCode() {
        return this.skuCode;
    }

    @Override
    public String getInvoiceNumber() {
        return this.invoiceNumber;
    }

    @Override
    public String getSumForSku() {
        return this.sum.setScale(2,RoundingMode.HALF_UP).toPlainString();
    }

}
