package com.kindminds.drs.core.biz.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.uuid.Generators;
import com.kindminds.drs.Country;


import com.kindminds.drs.api.data.cqrs.store.event.dao.productV2.ProductDao;
import com.kindminds.drs.api.data.cqrs.store.event.dao.productV2.ProductVariationDao;
import com.kindminds.drs.api.v2.biz.domain.model.product.Product;
import com.kindminds.drs.api.v2.biz.domain.model.product.ProductEditingStatusType;
import com.kindminds.drs.api.v2.biz.domain.model.product.ProductVariation;
import com.kindminds.drs.service.util.SpringAppCtx;


import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.*;

public class ProductImpl implements Product {


    private String id;
    private OffsetDateTime createTime;

    private Boolean activate = false;
    private JsonNode jsonNode;
    private JsonNode marketingJsonNode;
    private Country marketside;
    private String supplierKCode;
    private String baseCode;

    private List<ProductVariation> varList = null;

    private String data;
    private String editingStatusText;

    private ProductEditingStatusType editingStatus;


    private ObjectMapper objectMapper;


    private ProductDao repo = (ProductDao) SpringAppCtx.get().getBean(ProductDao.class);
    private ProductVariationDao pvRepo = (ProductVariationDao)SpringAppCtx.get()
            .getBean(ProductVariationDao.class);

    public ProductImpl() {

    }

    public ProductImpl(String id , OffsetDateTime createTime , Country marketside ,
            String baseCode , String data , Boolean activate
            , ProductEditingStatusType editingStatus) {


        this.id = id;
        this.createTime = createTime;
        this.marketside = marketside;
        this.baseCode = baseCode;
        this.data = data;
        this.activate = activate;
        this.editingStatus = editingStatus;

        if(marketside == Country.CORE){
            this.objectMapper = new ObjectMapper();
            TypeReference<HashMap<String,Object>> typeRef
                    = new TypeReference<HashMap<String,Object>>() {};
            try{
                Map map = objectMapper.readValue(data, typeRef);
                this.supplierKCode = map.get("supplierKcode").toString();
            }catch (IOException e){

            }


        }



    }

    public static ProductImpl createLeagcyProduct(String supplierKCode , String baseCode,
                                                  JsonNode data , JsonNode marketingData , String leagcyStatus) {
        return new ProductImpl(supplierKCode, baseCode ,data,marketingData , leagcyStatus, Optional.empty());
    }


    public static ProductImpl createCoreProduct(String supplierKCode , String baseCode,
                                            JsonNode data , JsonNode marketingData) {
        return new ProductImpl(supplierKCode, baseCode ,data,marketingData , Optional.empty());
    }


    public static ProductImpl valueOf(String supplierKCode , String baseCode, Country marketSide ,
                                            JsonNode data ) {
        return new ProductImpl(supplierKCode, baseCode  ,data , Optional.of(marketSide));
    }

    public static ProductImpl createMarketSideProduct(String supplierKCode , String baseCode,
                                                      String id , OffsetDateTime createTime ,
                                                      JsonNode data , JsonNode marketingData ,
                                                      Country marksetSide) {
        return new ProductImpl( supplierKCode ,  baseCode,
                id , createTime , data,marketingData , Optional.of(marksetSide));
    }

    public static ProductImpl createLeagcyMarketSideProduct(String supplierKCode , String baseCode,
                                                            String id , OffsetDateTime createTime ,
                                                            JsonNode data , JsonNode marketingData ,
                                                            Country marksetSide , String leagcyStatus) {
        return new ProductImpl( supplierKCode ,  baseCode,
                id , createTime , data,marketingData , leagcyStatus,Optional.of(marksetSide));
    }


    private ProductImpl(String supplierKCode , String baseCode ,
                        JsonNode data , JsonNode marketingData , Optional<Country> marketplaceRegion) {


        this.id = repo.getId(baseCode , Country.CORE ).
                orElseGet(()-> Generators.randomBasedGenerator().generate().toString());

        this.createTime = OffsetDateTime.now();
        this.marketside = marketplaceRegion.isPresent() ?
                marketplaceRegion.get(): Country.CORE;

        this.init(supplierKCode ,  baseCode, data ,  marketingData);



    }

    //migrated
    private ProductImpl(String supplierKCode , String baseCode ,
                        JsonNode data , JsonNode marketingData , String editingStatusText ,
                        Optional<Country> marketplaceRegion) {


        this.id = repo.getId(baseCode , Country.CORE ).
                orElseGet(()-> Generators.randomBasedGenerator().generate().toString());

        this.createTime = OffsetDateTime.now();
        this.marketside = marketplaceRegion.isPresent() ?
                marketplaceRegion.get(): Country.CORE;

        this.editingStatus = ProductEditingStatusType.fromText(editingStatusText);
        this.activate = true;


        this.init(supplierKCode ,  baseCode, data ,  marketingData);



    }

    //migrated
    private ProductImpl(String supplierKCode , String baseCode ,
                        String id , OffsetDateTime createTime,
                        JsonNode data , JsonNode marketingData , String editingStatusText,
                        Optional<Country> marketplaceRegion) {


        this.id = id;
        this.createTime = createTime;
        this.marketside = marketplaceRegion.isPresent() ?
                marketplaceRegion.get(): Country.CORE;

        this.editingStatus = ProductEditingStatusType.fromText(editingStatusText);
        this.activate = true;

        this.init(supplierKCode ,  baseCode, data ,  marketingData);



    }

    private ProductImpl(String supplierKCode , String baseCode ,
                        String id , OffsetDateTime createTime,
            JsonNode data , JsonNode marketingData , Optional<Country> marketplaceRegion) {



        this.id = id;
        this.createTime = createTime;
        this.marketside = marketplaceRegion.isPresent() ?
              marketplaceRegion.get(): Country.CORE;


        this.init(supplierKCode ,  baseCode, data ,  marketingData);



    }


    private ProductImpl(String supplierKCode , String baseCode ,
                        JsonNode data  , Optional<Country> marketplaceRegion) {


        this.id = repo.getId(baseCode , Country.CORE ).
                orElseGet(()-> Generators.randomBasedGenerator().generate().toString());

        this.createTime = OffsetDateTime.now();
        this.marketside = marketplaceRegion.isPresent() ?
                marketplaceRegion.get(): Country.CORE;


        this.init(supplierKCode ,  baseCode, data ,  null);



    }

    private void init(String supplierKCode , String baseCode , JsonNode jsonNode ,
                      JsonNode marketingJsonNode ){

        this.objectMapper = new ObjectMapper();


        this.supplierKCode = supplierKCode;
        this.baseCode = baseCode;
        this.jsonNode = jsonNode;
        this.marketingJsonNode = marketingJsonNode;

        try {

            this.data = objectMapper.writeValueAsString(this.jsonNode);


        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }



        this.createVariation();


    }

    private void createVariation() {

        if(varList == null) this.varList = new ArrayList<ProductVariation>();


        try {

            List pList = objectMapper.readValue(this.jsonNode.get("products").asText() , List.class);

            //todo temp 1-1
            Optional<List<String>> pvIds = pvRepo.getIds(this.id, this.marketside);
            if(!pvIds.isPresent()){
                pList.forEach(x ->{
                    varList.add(ProductVariationImpl.createProductVariation(this.id , this.createTime ,
                            this.marketside, (Map)x , this.marketingJsonNode));
                });

            }else{
                int idsMaxIndex = pvIds.get().size() -1;
                for (int i = 0; i < pList.size() ; i++) {
                    if(i > idsMaxIndex){
                        varList.add(ProductVariationImpl.createProductVariation(this.id , this.createTime ,
                                this.marketside, (Map)pList.get(i) , this.marketingJsonNode));
                    }else{
                        varList.add(ProductVariationImpl.createProductVariation(pvIds.get().get(i) ,this.id , this.createTime ,
                                this.marketside, (Map)pList.get(i) , this.marketingJsonNode));
                    }
                }

            }


        } catch (IOException e) {

            e.printStackTrace();
        }



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
    public Country getMarketside() {
        return this.marketside;
    }

    @Override
    public String getSupplierKcode() {
        return this.supplierKCode;
    }

    @Override
    public String getProductBaseCode() {
        return this.baseCode;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public List<ProductVariation> getProductVariations() {
        if(this.varList == null) this.varList = new ArrayList<ProductVariation>();
        return this.varList;
    }


    @Override
    public ProductEditingStatusType getEditingStatus() {
        return this.editingStatus;
    }

    @Override
    public Boolean isActivated() {
        return this.activate;
    }

    @Override
    public void edit(ProductEditingStatusType productEditingStatusType) {
        this.editingStatus = productEditingStatusType;
        this.activate = true;
        this.createTime = OffsetDateTime.now();

        if(this.getProductVariations() != null){
            this.getProductVariations().forEach(x->{
                x.edit();
                if(x.getProductMarketingMaterials() !=null){
                    x.getProductMarketingMaterials().forEach(m->{
                        m.edit();
                    });
                }

            });
        }

    }

    @Override
    public void clone(Product product) {
        this.createTime = product.getCreateTime();
        this.baseCode = product.getProductBaseCode();

        //products
        this.objectMapper = new ObjectMapper();
        try {

            TypeReference<HashMap<String,Object>> typeRef
                    = new TypeReference<HashMap<String,Object>>() {};

            HashMap<String,Object> prevData = objectMapper.readValue(this.data, typeRef);
            HashMap<String,Object> newData = objectMapper.readValue(product.getData(), typeRef);

            prevData.put("products" , newData.get("products"));

            this.data = objectMapper.writeValueAsString(prevData);


        } catch (IOException e) {
            e.printStackTrace();
        }

        if(this.getProductVariations() != null){
            this.getProductVariations().forEach(x->{
                x.edit();
                if(x.getProductMarketingMaterials() !=null){
                    x.getProductMarketingMaterials().forEach(m->{
                        m.edit();
                    });
                }
            });
        }

    }


    @Override
    public String getEditingStatusText() {
        return this.editingStatus.getText();
    }


}
