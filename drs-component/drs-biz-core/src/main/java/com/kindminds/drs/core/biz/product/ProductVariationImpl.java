package com.kindminds.drs.core.biz.product;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.uuid.Generators;
import com.kindminds.drs.Country;

import com.kindminds.drs.api.data.cqrs.store.event.dao.productV2.ProductMarketingMaterialDao;
import com.kindminds.drs.api.v2.biz.domain.model.product.ProductMarketingMaterial;
import com.kindminds.drs.api.v2.biz.domain.model.product.ProductVariation;
import com.kindminds.drs.service.util.SpringAppCtx;


import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class ProductVariationImpl implements ProductVariation {


    private String id;

    private Boolean active;
    private String data;
    private OffsetDateTime createTime;

    private String variationCode;
    private String productId;
    private OffsetDateTime prodcutCreateTime;
    private Country marketside;

    private List<ProductMarketingMaterial> productMarketingMaterials;

    private ProductMarketingMaterialDao pmmRepo = (ProductMarketingMaterialDao)
            SpringAppCtx.get().getBean(ProductMarketingMaterialDao.class);

    public ProductVariationImpl(String id , String variationCode , OffsetDateTime createTime ,
                                String productId , OffsetDateTime productCreateTime,
                                Country marketside , String variationData){
        this.id = id;
        this.data = variationData;
        this.variationCode  = variationCode;
        this.createTime = createTime;
        this.productId = productId ;
        this.prodcutCreateTime = productCreateTime;
        this.marketside = marketside;

    }


    public static ProductVariationImpl createProductVariation(String productId , OffsetDateTime productCreateTime,
                                                              Country marketside ,
                                                              Map variationData , JsonNode marketingData) {

        return new ProductVariationImpl(productId , productCreateTime ,marketside , variationData , marketingData);
    }

    public static ProductVariationImpl createProductVariation(String id , String productId , OffsetDateTime productCreateTime,
                                                              Country marketside ,
                                                              Map variationData , JsonNode marketingData) {

        return new ProductVariationImpl(productId , productCreateTime ,marketside , variationData , marketingData);
    }


    private ProductVariationImpl(String productId ,  OffsetDateTime productCreateTime , Country marketside ,
                                 Map variationData ,JsonNode marketingData) {

        this.id = Generators.randomBasedGenerator().generate().toString();

        this.init(productId , productCreateTime ,marketside , variationData , marketingData);

    }


    private ProductVariationImpl(String id , String productId ,  OffsetDateTime productCreateTime , Country marketside ,
                                 Map variationData ,JsonNode marketingData) {

        this.id = id;

        this.init(productId , productCreateTime ,marketside , variationData , marketingData);

    }

    private void init(String productId ,  OffsetDateTime productCreateTime , Country marketside ,
                      Map variationData ,JsonNode marketingData){

        this.createTime = OffsetDateTime.now();

        this.variationCode = variationData.get("SKU").toString();

        try {
            this.data = new ObjectMapper().writeValueAsString(variationData);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        this.productId = productId;
        this.prodcutCreateTime = productCreateTime;



        this.marketside = marketside;


        this.createMarketingMaterial(marketingData);

    }

    private void createMarketingMaterial(JsonNode marketingData) {

        this.productMarketingMaterials = new ArrayList<ProductMarketingMaterial>();

        if(marketingData != null){
            Optional<List<String>> pmmIds = pmmRepo.getIds(this.id);

            if(!pmmIds.isPresent()){
                this.productMarketingMaterials.add(
                        ProductMarketingMaterialImpl.CreateProductMarketingMaterialImpl(this.id , this.createTime,
                                marketingData));
            }else{

                this.productMarketingMaterials.add(
                        ProductMarketingMaterialImpl.CreateProductMarketingMaterialImpl(this.id , this.createTime,
                                marketingData));
            }
        }

    }


    @Override
    public OffsetDateTime getCreateTime() {
        return this.createTime;
    }

    @Override
    public String getVariationCode() {
        return this.variationCode;
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
    public List<ProductMarketingMaterial> getProductMarketingMaterials() {
        return productMarketingMaterials;
    }


    @Override
    public String getProductId() {
        return this.productId;
    }

    @Override
    public OffsetDateTime getProductCreatetime() {
        return this.prodcutCreateTime;
    }

    @Override
    public Country getMarketside() {
        return this.marketside;
    }

    @Override
    public void edit() {
        this.createTime = OffsetDateTime.now();
    }

}
