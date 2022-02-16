package com.kindminds.drs.core.biz.product;


import com.fasterxml.uuid.Generators;
import com.kindminds.drs.Country;

import com.kindminds.drs.api.v2.biz.domain.model.product.MarketingMaterialEditingRequest;
import com.kindminds.drs.api.v2.biz.domain.model.product.ProductEditingStatusType;
import com.kindminds.drs.api.v2.biz.domain.model.product.ProductMarketingMaterial;
import com.kindminds.drs.api.v2.biz.domain.model.repo.product.MarketingMaterialEditingRequestRepo;
import com.kindminds.drs.core.biz.repo.product.MarketingMaterialEditingRequestRepoImpl;

import java.time.OffsetDateTime;

public class MarketingMaterialEditingRequestImpl implements MarketingMaterialEditingRequest {

    private String id;
    private OffsetDateTime createTime;
    private ProductMarketingMaterial productMarketingMaterial;
    private ProductEditingStatusType status;

    private MarketingMaterialEditingRequestRepo repo = new
            MarketingMaterialEditingRequestRepoImpl();

    public static MarketingMaterialEditingRequestImpl createRequest(
            Country marketSide , ProductMarketingMaterial productMarketingMaterial) {

        return new MarketingMaterialEditingRequestImpl(marketSide , productMarketingMaterial);
    }

    public static MarketingMaterialEditingRequestImpl createRequest(ProductMarketingMaterial productMarketingMaterial) {

        return new MarketingMaterialEditingRequestImpl(productMarketingMaterial);
    }

    public  MarketingMaterialEditingRequestImpl(String id , ProductEditingStatusType status){

        this.id = id;
        this.status = status;

    }


    private MarketingMaterialEditingRequestImpl(Country marketSide , ProductMarketingMaterial productMarketingMaterial ) {

        this.id = Generators.randomBasedGenerator().generate().toString();

        this.createTime = OffsetDateTime.now();

        this.productMarketingMaterial = productMarketingMaterial;

        if(marketSide == Country.CORE){
            this.status = ProductEditingStatusType.PENDING_SUPPLIER_ACTION;
        }else{
            this.status = ProductEditingStatusType.FINALIZED;
        }

    }

    private MarketingMaterialEditingRequestImpl(ProductMarketingMaterial productMarketingMaterial ) {

        this.id = Generators.randomBasedGenerator().generate().toString();

        this.createTime = OffsetDateTime.now();

        this.productMarketingMaterial = productMarketingMaterial;

        this.status = ProductEditingStatusType.PENDING_SUPPLIER_ACTION;


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
    public ProductEditingStatusType getStatus() {
        return this.status;
    }

    @Override
    public ProductMarketingMaterial getProductMarketingMaterial() {
        return this.productMarketingMaterial;
    }

    @Override
    public void submit() {

        this.status = ProductEditingStatusType.PENDING_DRS_REVIEW;
        this.createTime = OffsetDateTime.now();

    }

    @Override
    public void approve() {

        this.status = ProductEditingStatusType.FINALIZED;
        this.createTime = OffsetDateTime.now();
    }

    @Override
    public void reject() {
        this.status = ProductEditingStatusType.PENDING_SUPPLIER_ACTION;
        this.createTime = OffsetDateTime.now();
    }

    @Override
    public void update(ProductMarketingMaterial productMarketingMaterial) {

        this.productMarketingMaterial = productMarketingMaterial;
        this.createTime = OffsetDateTime.now();

    }
}
