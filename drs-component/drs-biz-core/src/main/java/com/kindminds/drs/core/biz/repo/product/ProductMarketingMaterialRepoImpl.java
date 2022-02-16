package com.kindminds.drs.core.biz.repo.product;

import com.kindminds.drs.Filter;

import com.kindminds.drs.api.v2.biz.domain.model.product.ProductMarketingMaterial;
import com.kindminds.drs.api.v2.biz.domain.model.repo.product.ProductMarketingMaterialRepo;
import com.kindminds.drs.core.biz.product.ProductMarketingMaterialImpl;
import com.kindminds.drs.api.data.cqrs.store.event.dao.productV2.ProductMarketingMaterialDao;

import com.kindminds.drs.service.util.SpringAppCtx;
import org.apache.commons.lang.NotImplementedException;


import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;


public class ProductMarketingMaterialRepoImpl implements ProductMarketingMaterialRepo {



    private ProductMarketingMaterialDao dao =(ProductMarketingMaterialDao) SpringAppCtx.get().
            getBean(ProductMarketingMaterialDao.class);

    @Override
    public Optional<ProductMarketingMaterial> findById(String id) {

        List<Object[]> result = dao.queryById(id);

        for(Object[] columns:result){
            String mmId = (String)columns[0];
            OffsetDateTime createTime =
                    OffsetDateTime.ofInstant(((Timestamp)columns[1]).toInstant(),ZoneId.of("Asia/Taipei"));

            String data = (String)columns[2];
            String vId = (String)columns[3];
            OffsetDateTime vCreateTime =
                    OffsetDateTime.ofInstant(((Timestamp)columns[4]).toInstant(),ZoneId.of("Asia/Taipei"));


           return Optional.of(new ProductMarketingMaterialImpl(mmId,createTime,data,vId,vCreateTime));


        }


        return Optional.empty();
    }

    @Override
    public void add(ProductMarketingMaterial productMarketingMaterial) {

        dao.insert(productMarketingMaterial);
    }


    @Override
    public void edit(ProductMarketingMaterial productMarketingMaterial) {
        throw new NotImplementedException();
    }

    @Override
    public void remove(ProductMarketingMaterial productMarketingMaterial) {
        throw new NotImplementedException();
    }

    @Override
    public void add(List<ProductMarketingMaterial> items) {

    }

    @Override
    public List<ProductMarketingMaterial> find( Filter filter) {
        return null;
    }


    @Override
    public Optional<ProductMarketingMaterial> findOne( Filter filter) {
        return Optional.empty();
    }
}
