package com.kindminds.drs.core.biz.repo.sales;

import com.fasterxml.uuid.Generators;

import com.kindminds.drs.api.v2.biz.domain.model.repo.sales.QuotationRepo;
import com.kindminds.drs.api.v2.biz.domain.model.sales.Quotation;
import com.kindminds.drs.api.v2.biz.domain.model.sales.service.FeeType;
import com.kindminds.drs.api.data.cqrs.store.read.queries.QuoteRequestViewQueries;
import com.kindminds.drs.service.util.SpringAppCtx;


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;


public class QuotationImpl implements Quotation {

    private String id;
    private OffsetDateTime createTime;

    private String serialNumber;
    private Integer supplierCompanyId;
    private FeeType feeType;

    private BigDecimal salePrice;
    private BigDecimal preferentialPrice;
    private BigDecimal finalPrice;

    private String summary;
    private Boolean accepted;
    private Boolean rejected;

    private QuotationRepo repo = new QuotationRepoImpl();
    private QuoteRequestViewQueries qrQueries =
            SpringAppCtx.get().getBean(QuoteRequestViewQueries.class);

    public static QuotationImpl createInitialQuote(
            Integer supplierCompanyId, FeeType feeType) {

        return new QuotationImpl(supplierCompanyId, feeType);
    }

    public static QuotationImpl createFromQuery(Object[] result) {
        String id = (String) result[0];
        OffsetDateTime createTime =
                OffsetDateTime.ofInstant(((Timestamp)result[1]).toInstant(), ZoneId.of("Asia/Taipei"));

        String serialNumber = (String) result[2];
        Integer supplierCompanyId = (Integer) result[3];
        FeeType feeType = FeeType.fromKey((Integer) result[4]);
        BigDecimal salePrice = (BigDecimal) result[5];
        BigDecimal preferentialPrice = (BigDecimal) result[6];
        BigDecimal finalPrice = (BigDecimal) result[7];
        String summary = (String) result[8];
        Boolean isAccepted = (Boolean) result[9];
        Boolean isRejected = (Boolean) result[10];

        return new QuotationImpl(id, createTime, serialNumber, supplierCompanyId,
                feeType, salePrice, preferentialPrice, finalPrice, summary, isAccepted, isRejected);
    }

//    public static QuotationImpl createFromValues(
//            String id, OffsetDateTime createTime, String serialNumber,
//            Integer supplierCompanyId, FeeType type, BigDecimal salePrice,
//            BigDecimal preferentialPrice, BigDecimal finalPrice,
//            String summary, Boolean accepted, Boolean rejected) {
//
//        return new QuotationImpl(id, createTime, serialNumber, supplierCompanyId,
//                type, salePrice, preferentialPrice, finalPrice, summary, accepted, rejected);
//    }

    private QuotationImpl(String id, OffsetDateTime createTime, String serialNumber,
                          Integer supplierCompanyId, FeeType type, BigDecimal salePrice,
                          BigDecimal preferentialPrice, BigDecimal finalPrice,
                          String summary, Boolean accepted, Boolean rejected){

        this.id =  id;
        this.createTime = createTime;
        this.serialNumber = serialNumber;
        this.supplierCompanyId = supplierCompanyId;
        this.feeType = type;
        this.salePrice = salePrice;
        this.preferentialPrice = preferentialPrice;
        this.finalPrice = finalPrice;
        this.summary = summary;
        this.accepted = accepted;
        this.rejected = rejected;

    }

    private QuotationImpl(Integer supplierCompanyId , FeeType feeType){

        this.id =  Generators.randomBasedGenerator().generate().toString();
        this.createTime = OffsetDateTime.now();

        this.supplierCompanyId = supplierCompanyId;
        this.feeType = feeType;

        this.summary = repo.getSummary(supplierCompanyId);
        this.accepted = false;
        this.rejected = false;
    }


    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getSerialNumber() {
        return this.serialNumber;
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
    public FeeType getFeeType() {
        return this.feeType;
    }

    @Override
    public BigDecimal getSalePrice() {
        return this.salePrice;
    }

    @Override
    public BigDecimal getPreferentialPrice() {
        return this.preferentialPrice;
    }

    @Override
    public BigDecimal getFinalPrice() {
        return this.finalPrice;
    }

    @Override
    public String getSummary() {
        return this.summary;
    }

    @Override
    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public void generateQuotationNumber() {
        Integer nextSerialSeq = repo.getSerialNumber(supplierCompanyId);
        Object[] companyInfo = qrQueries.getCompanyInfoByCode(supplierCompanyId);

        serialNumber = String.format("QT-%s-PPR-%02d", companyInfo[0], nextSerialSeq);

    }

    @Override
    public void generatePrice() {
        //calc sale price
        if (feeType == FeeType.MONTHLY_SERVICE_FEE) {
            String[] bpSummary = this.summary.split("x");

            if (bpSummary.length != 2) return;

            String bp = bpSummary[0].trim();
            Integer bpCount = Integer.valueOf(bp.split(" ")[0]);
            String regions = bpSummary[1].trim();

            salePrice = repo.getMonthlyServiceFee(regions, bpCount);
        }
    }

    @Override
    public void reducePrice(BigDecimal reducedPrice) {
        if (reducedPrice == null
                || reducedPrice.compareTo(BigDecimal.ZERO) == 0
                || reducedPrice.compareTo(salePrice) > 0) {
            this.preferentialPrice = salePrice;
        } else {
            this.preferentialPrice = reducedPrice;
        }
        this.finalPrice = null;

        this.accepted = false;
        this.rejected = false;
        this.createTime = OffsetDateTime.now();
    }

    @Override
    public void finalizePrice() {
        if (preferentialPrice == null || preferentialPrice.compareTo(BigDecimal.ZERO) == 0) {
            this.finalPrice = this.salePrice;
        } else {
            this.finalPrice = this.preferentialPrice;
        }
        this.createTime = OffsetDateTime.now();
    }

    @Override
    public void accept() {
        this.accepted = true;
        this.rejected = false;
        this.createTime = OffsetDateTime.now();
    }

    @Override
    public void reject() {
        this.rejected = true;
        this.accepted = false;
        this.createTime = OffsetDateTime.now();
    }

    @Override
    public boolean isAccepted() {
        return this.accepted;
    }

    @Override
    public boolean isRejected() {
        return this.rejected;
    }
}
