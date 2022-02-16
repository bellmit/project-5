package com.kindminds.drs.core.biz.repo.sales.service;


import com.kindminds.drs.Filter;


import com.kindminds.drs.api.data.es.dao.sales.service.FeeDao;

import com.kindminds.drs.api.v2.biz.domain.model.repo.sales.service.FeeRepo;
import com.kindminds.drs.api.v2.biz.domain.model.sales.service.Fee;
import com.kindminds.drs.service.util.SpringAppCtx;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class FeeRepoImpl implements FeeRepo {

    @Autowired
    private FeeDao dao = (FeeDao)
            SpringAppCtx.get().getBean(FeeDao.class);

    @Override
    public void add(Fee fee) {
        dao.insert(fee );
    }



    @Override
    public void edit(Fee fee) {

    }

    @Override
    public void remove(Fee fee) {

    }

    @Override
    public Optional<Fee> findById(String feeId) {
        return Optional.empty();
    }

    @Override
    public void add(List<Fee> items) {

    }


    @Override
    public List<Fee> find( Filter filter) {
        return null;
    }


    @Override
    public Optional<Fee> findOne( Filter filter) {
        return Optional.empty();
    }
}
