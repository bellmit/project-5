package com.kindminds.drs.core.biz.repo.product;


import com.kindminds.drs.Filter;

import com.kindminds.drs.api.v2.biz.domain.model.product.MarketingMaterialEditingRequest;
import com.kindminds.drs.api.v2.biz.domain.model.product.ProductEditingStatusType;
import com.kindminds.drs.api.v2.biz.domain.model.repo.product.MarketingMaterialEditingRequestRepo;
import com.kindminds.drs.core.biz.product.MarketingMaterialEditingRequestImpl;
import com.kindminds.drs.api.data.cqrs.store.event.dao.productV2.MarketingMaterialEditingRequestDao;

import com.kindminds.drs.service.util.SpringAppCtx;
import org.apache.commons.lang.NotImplementedException;


import java.util.List;
import java.util.Optional;


public class MarketingMaterialEditingRequestRepoImpl implements
        MarketingMaterialEditingRequestRepo {



    private MarketingMaterialEditingRequestDao dao = (MarketingMaterialEditingRequestDao) SpringAppCtx.get().
            getBean(MarketingMaterialEditingRequestDao.class);

    /*
    @Overrides
    public Optional<String> getId(String productMarketingMaterialId) {

        return dao.findId(productMarketingMaterialId);
    }*/

    @Override
    public void add(MarketingMaterialEditingRequest marketingMaterialEditingRequest) {

        dao.insert(marketingMaterialEditingRequest);

    }



    @Override
    public void edit(MarketingMaterialEditingRequest marketingMaterialEditingRequest) {
        throw new NotImplementedException();
    }

    @Override
    public void remove(MarketingMaterialEditingRequest marketingMaterialEditingRequest) {
        throw new NotImplementedException();
    }

    @Override
    public Optional<MarketingMaterialEditingRequest> findById(String productMarketingMaterialId) {

        List<Object []> resutlList =  dao.find(productMarketingMaterialId);

        if(resutlList.size()>0){

            Object[] res = resutlList.get(0);
            String id = (String)res[0];
            ProductEditingStatusType status = ProductEditingStatusType.fromKey((Integer)res[5]);

            return Optional.of(new MarketingMaterialEditingRequestImpl(id,status));

        }

        return Optional.empty();
    }

    @Override
    public void add(List<MarketingMaterialEditingRequest> items) {

    }


    @Override
    public List<MarketingMaterialEditingRequest> find( Filter filter) {
        return null;
    }




    @Override
    public Optional<MarketingMaterialEditingRequest> findOne( Filter filter) {
        return Optional.empty();
    }


    /*
    @NotNull
    @Override
    public List<MarketingMaterialEditingRequest> find(@NotNull Criteria criteria) {
        return null;
    }*/
}
