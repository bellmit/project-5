package com.kindminds.drs.core.biz.product;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.uuid.Generators;
import com.kindminds.drs.api.v2.biz.domain.model.product.ProductMarketingMaterial;

import java.time.OffsetDateTime;

public class ProductMarketingMaterialImpl implements ProductMarketingMaterial {


    private String id;

    private String data;
    private OffsetDateTime createTime;

    private String productVariationId;
    private OffsetDateTime productVariationCreateTime;

    public static ProductMarketingMaterialImpl CreateProductMarketingMaterialImpl(String productVariationId ,
                                                                                  OffsetDateTime productVariationCreateTime
            , JsonNode data){

        return new ProductMarketingMaterialImpl(productVariationId,productVariationCreateTime, data);
    }

    public ProductMarketingMaterialImpl(String id  ,
                                        OffsetDateTime createTime ,
                                        String data, String productVariationId  ,
                                        OffsetDateTime productVariationCreateTime
                                       ){
        this.id = id;
        this.createTime = createTime;
        this.data = data;
        this.productVariationId = productVariationId;
        this.productVariationCreateTime = productVariationCreateTime;


    }

    private ProductMarketingMaterialImpl(String productVariationId  ,
                                         OffsetDateTime productVariationCreateTime,
                                         JsonNode data){

        this.id = Generators.randomBasedGenerator().generate().toString();
        this.init( productVariationId ,productVariationCreateTime , data);

    }

    private void init( String productVariationId  , OffsetDateTime productVariationCreateTime,
                       JsonNode data){
        this.createTime = OffsetDateTime.now();
        this.data  = data.get("jsonData").asText();

        this.productVariationId = productVariationId;
        this.productVariationCreateTime = productVariationCreateTime;
    }


    @Override
    public OffsetDateTime getCreateTime() {
        return this.createTime;
    }

    @Override
    public String getData() {
        return this.data;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getProductVariationId() {
        return this.productVariationId;
    }

    @Override
    public OffsetDateTime getProductVariationCreateTime() {
        return this.productVariationCreateTime;
    }

    @Override
    public void update(String data) {

        this.data = data;
        this.createTime = OffsetDateTime.now();

    }

    @Override
    public void edit() {
        this.createTime = OffsetDateTime.now();
    }

}
