package com.kindminds.drs.core.biz.sales.service;

import com.fasterxml.uuid.Generators;
import com.kindminds.drs.api.v2.biz.domain.model.sales.service.Fee;
import com.kindminds.drs.api.v2.biz.domain.model.sales.service.FeeType;


import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class FeeImpl implements Fee {


    private String id;
    private OffsetDateTime createTime;
    private Integer supplierCompanyId;
    private FeeType type;
    private BigDecimal amount;
    private Boolean activated;

    public static FeeImpl valueOf(Integer supplierCompanyId , FeeType type , BigDecimal amount){
        return new FeeImpl(supplierCompanyId,type , amount);
    }

    private FeeImpl(Integer supplierCompanyId , FeeType type , BigDecimal amount){

        this.id =  Generators.randomBasedGenerator().generate().toString();
        this.createTime = OffsetDateTime.now();
        this.supplierCompanyId = supplierCompanyId;
        this.type = type;
        this.amount = amount;


    }

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
    public FeeType getType() {
        return this.type;
    }

    @Override
    public BigDecimal getAmount() {
        return this.amount;
    }

    @Override
    public Boolean isActivated() {
        return this.activated;
    }

    @Override
    public void activate() {
        this.createTime  = OffsetDateTime.now();
        this.activated = true;
    }

    @Override
    public void deactivate() {
        this.createTime  = OffsetDateTime.now();
        this.activated = false;
    }
}
