package com.kindminds.drs.core.biz.repo.product;

import com.kindminds.drs.Country;
import com.kindminds.drs.Filter;


import com.kindminds.drs.api.v2.biz.domain.model.product.Product;
import com.kindminds.drs.api.v2.biz.domain.model.product.ProductEditingStatusType;
import com.kindminds.drs.api.v2.biz.domain.model.product.ProductVariation;
import com.kindminds.drs.api.v2.biz.domain.model.repo.product.ProductRepo;
import com.kindminds.drs.core.biz.product.ProductImpl;
import com.kindminds.drs.api.data.cqrs.store.event.dao.productV2.ProductDao;

import com.kindminds.drs.service.util.SpringAppCtx;


import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductRepoImpl implements ProductRepo {


    private ProductDao productDao = (ProductDao) SpringAppCtx.get().
            getBean(ProductDao.class);

    @Override
    public void add(Product product) {
        productDao.save(product);
    }



    @Override
    public void edit(Product product) {

    }

    @Override
    public void remove(Product product) {

    }

    @Override
    public Optional<Product> findById(String id) {

        List<Object[]> resultList = productDao.get(id , Country.CORE);
        if(resultList.size() >0 ){

            Object [] obj = resultList.get(0);
            String pid = obj[0].toString();
            OffsetDateTime ct =
                    OffsetDateTime.ofInstant(((Timestamp)obj[1]).toInstant(), ZoneId.of("Asia/Taipei"));

            Country pms =Country.fromKey(Integer.parseInt(obj[2].toString()));


            String baseCode = obj[3].toString();
            String data = obj[4].toString();
            Boolean activiate = obj[5].toString() == "1" ? true:false;


            ProductEditingStatusType status =
                    ProductEditingStatusType.fromKey(Integer.parseInt(obj[6].toString()));

            ProductImpl p = new ProductImpl(pid , ct , pms ,baseCode,data,activiate  , status);

            return Optional.of(p);
        }

        return Optional.empty();
    }

    @Override
    public Optional<Product> find(String id, Country marketSide) {

        ProductVariationRepoImpl vRepo = new ProductVariationRepoImpl();
        List<Object[]> resultList = productDao.get(id , marketSide);
        if(resultList.size() >0 ){

            Object [] obj = resultList.get(0);
            String pid = obj[0].toString();
            OffsetDateTime ct =
                    OffsetDateTime.ofInstant(((Timestamp)obj[1]).toInstant(), ZoneId.of("Asia/Taipei"));

            Country pms =Country.fromKey(Integer.parseInt(obj[2].toString()));

            String baseCode = obj[3].toString();
            String data = obj[4].toString();
            Boolean activiate = obj[5].toString() == "1" ? true:false;


            ProductEditingStatusType status =
                    ProductEditingStatusType.fromKey(Integer.parseInt(obj[6].toString()));

            ProductImpl p = new ProductImpl(pid , ct , pms ,baseCode,data,activiate  , status);

            List<ProductVariation> vList = vRepo.findByProductId(p.getId(),p.getCreateTime(),p.getMarketside());
            vList.forEach(v ->{
                p.getProductVariations().add(v);
            });


            return Optional.of(p);
        }

        return Optional.empty();
    }

    @Override
    public List<Product> findAllMarketSide(String id) {

        List<Object[]> resultList = productDao.get(id );
        ProductVariationRepoImpl vRepo = new ProductVariationRepoImpl();

        List<Product> pList = new ArrayList<Product>();
        resultList.forEach(obj->{
            String pid = obj[0].toString();
            OffsetDateTime ct =
                    OffsetDateTime.ofInstant(((Timestamp)obj[1]).toInstant(), ZoneId.of("Asia/Taipei"));

            Country pms =Country.fromKey(Integer.parseInt(obj[2].toString()));


            String baseCode = obj[3].toString();
            String data = obj[4].toString();
            Boolean activiate = obj[5].toString() == "1" ? true:false;


            ProductEditingStatusType status =
                    ProductEditingStatusType.fromKey(Integer.parseInt(obj[6].toString()));

            ProductImpl p = new ProductImpl(pid , ct , pms ,baseCode,data,activiate  , status);
            List<ProductVariation> vList = vRepo.findByProductId(p.getId(),p.getCreateTime(),p.getMarketside());
            vList.forEach(v ->{
                p.getProductVariations().add(v);
            });

            pList.add(p);

        });


        return pList;

    }

    @Override
    public void add(List<Product> items) {

    }

    @Override
    public List<Product> find(Filter filter) {
        return null;
    }

    @Override
    public Optional<Product> findOne( Filter filter) {
        return Optional.empty();
    }
}
