package com.kindminds.drs.core.biz.repo.sales;

import com.fasterxml.uuid.Generators;

import com.kindminds.drs.api.v2.biz.domain.model.repo.sales.QuoteRequestRepo;
import com.kindminds.drs.api.v2.biz.domain.model.sales.Quotation;
import com.kindminds.drs.api.v2.biz.domain.model.sales.QuoteRequest;
import com.kindminds.drs.api.v2.biz.domain.model.sales.QuoteRequestStatusType;
import com.kindminds.drs.api.v2.biz.domain.model.sales.service.FeeType;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;

public class QuoteRequestImpl implements QuoteRequest {

    private String id;
    private OffsetDateTime createTime;

    private Integer supplierCompanyId;
    private QuoteRequestStatusType status;
    private FeeType feeType;
    private Quotation quotation;


    private QuoteRequestRepo qrRepo = new QuoteRequestRepoImpl();

    public static QuoteRequestImpl createInitialRequest(Integer supplierCompanyId,
                                                        FeeType feeType) {
        return new QuoteRequestImpl(supplierCompanyId, feeType);
    }

    private QuoteRequestImpl(Integer supplierCompanyId, FeeType type) {

        this.id =  Generators.randomBasedGenerator().generate().toString();
        this.createTime = OffsetDateTime.now();
        this.supplierCompanyId = supplierCompanyId;

        this.status = QuoteRequestStatusType.QUOTE_REQUEST_STARTED;
        this.feeType = type;
    }

    public static QuoteRequestImpl createFromQuery(
            String id, OffsetDateTime createTime, Integer supplierCompanyId,
            Quotation quotation, QuoteRequestStatusType status , FeeType type ) {

        return new QuoteRequestImpl(id, createTime, supplierCompanyId, quotation, status, type);
    }

    private QuoteRequestImpl(String id, OffsetDateTime createTime,
                             Integer supplierCompanyId , Quotation quotation,
                             QuoteRequestStatusType status, FeeType type ){

        this.id =  id;
        this.createTime = createTime;
        this.supplierCompanyId = supplierCompanyId;
        this.quotation = quotation;
        this.status = status;
        this.feeType = type;

    }

//    public static QuoteRequestImpl valueOf(QuoteRequestStatusType status, Quotation quotation,
//                                       Integer supplierCompanyId , FeeType type ) {
//        return new QuoteRequestImpl(status, quotation, supplierCompanyId, type);
//    }

//    private QuoteRequestImpl(QuoteRequestStatusType status, Quotation quotation,
//                            Integer supplierCompanyId , FeeType type ){
//
//        this.id =  Generators.randomBasedGenerator().generate().toString();
//        this.createTime = OffsetDateTime.now();
//
//        this.status = status;
//        this.quotation = quotation;
//        this.supplierCompanyId = supplierCompanyId;
//        this.feeType = type;
//
//    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public OffsetDateTime getCreateTime() {
        return this.createTime;
    }

    @Override
    public Integer getSupplierCompanyId() {
        return this.supplierCompanyId;
    }

    @Override
    public QuoteRequestStatusType getStatus() {
        return this.status;
    }

    @Override
    public FeeType getFeeType() {
        return feeType;
    }

    @Override
    public Optional<Quotation> getQuotation() {

        return this.quotation != null ? Optional.of(this.quotation) : Optional.empty();
    }



    @Override
    public Quotation generateQuotation() {

        this.quotation = QuotationImpl.createInitialQuote(
                this.supplierCompanyId, this.feeType);

        this.quotation.generateQuotationNumber();

        this.quotation.generatePrice();

        return this.quotation;
    }

    @Override
    public void create(String preferentialPrice) {

        this.quotation.reducePrice(new BigDecimal(preferentialPrice));

        this.status = QuoteRequestStatusType.QUOTE_CREATED;
        this.createTime = OffsetDateTime.now();
    }

    @Override
    public void modify(String preferentialPrice) {

        this.quotation.reducePrice(new BigDecimal(preferentialPrice));

        this.status = QuoteRequestStatusType.QUOTE_MODIFIED;
        this.createTime = OffsetDateTime.now();

    }

    @Override
    public void accept() {
        this.quotation.accept();

        this.status = QuoteRequestStatusType.QUOTE_ACCEPTED;
        this.createTime = OffsetDateTime.now();
    }

    @Override
    public void reject() {
        this.quotation.reject();

        this.status = QuoteRequestStatusType.QUOTE_REJECTED;
        this.createTime = OffsetDateTime.now();
    }

    @Override
    public void confirm() {

        this.quotation.finalizePrice();

        this.status = QuoteRequestStatusType.QUOTE_CONFIRMED;
        this.createTime = OffsetDateTime.now();

    }
}
