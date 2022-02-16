package com.kindminds.drs.core.biz.repo.product;

import com.kindminds.drs.Country;


import com.kindminds.drs.api.v2.biz.domain.model.product.ProductVariation;
import com.kindminds.drs.api.v2.biz.domain.model.repo.product.ProductVariationRepo;
import com.kindminds.drs.core.biz.product.ProductVariationImpl;
import com.kindminds.drs.api.data.cqrs.store.event.dao.productV2.ProductVariationDao;
import com.kindminds.drs.service.util.SpringAppCtx;


import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class ProductVariationRepoImpl implements ProductVariationRepo {

    private ProductVariationDao variationDao = (ProductVariationDao) SpringAppCtx.get().
            getBean(ProductVariationDao.class);

    @Override
    public List<ProductVariation> findByProductId(String productId,
                                                  OffsetDateTime productCreateTime, Country marketSide) {


        List<Object[]> resultList = variationDao.get(productId,productCreateTime,marketSide);
        List<ProductVariation> pvList = new ArrayList<ProductVariation>();
        if(resultList.size() > 0){

            for (Object [] obj  :resultList) {
                String vid = obj[0].toString();
                OffsetDateTime ct =
                        OffsetDateTime.ofInstant(((Timestamp)obj[1]).toInstant(), ZoneId.of("Asia/Taipei"));

                String vcode = obj[2].toString();
                String data = obj[3].toString();
                String pid = obj[4].toString();

                OffsetDateTime pct =
                        OffsetDateTime.ofInstant(((Timestamp)obj[5]).toInstant(), ZoneId.of("Asia/Taipei"));

                Country pms =Country.fromKey(Integer.parseInt(obj[6].toString()));

                ProductVariation variation = new
                        ProductVariationImpl(vid , vcode,ct,pid ,pct,pms,data);

                pvList.add(variation);

            }

        }

        return pvList;
    }

    @Override
    public ProductVariation find(String id,
                                                  OffsetDateTime createTime, Country marketSide) {


        List<Object[]> resultList = variationDao.get(id,createTime,marketSide);
        List<ProductVariation> pvList = new ArrayList<ProductVariation>();
        if(resultList.size() > 0){
            for (Object [] obj  :resultList) {
                String vid = obj[0].toString();
                OffsetDateTime ct =
                        OffsetDateTime.ofInstant(((Timestamp)obj[1]).toInstant(), ZoneId.of("Asia/Taipei"));

                String vcode = obj[2].toString();
                String data = obj[3].toString();
                String pid = obj[4].toString();

                OffsetDateTime pct =
                        OffsetDateTime.ofInstant(((Timestamp)obj[5]).toInstant(), ZoneId.of("Asia/Taipei"));

                Country pms =Country.fromKey(Integer.parseInt(obj[6].toString()));

                ProductVariation variation = new
                        ProductVariationImpl(vid , vcode,ct,pid ,pct,pms,data);

                pvList.add(variation);
            }

        }

        return pvList.get(0);
    }
}
