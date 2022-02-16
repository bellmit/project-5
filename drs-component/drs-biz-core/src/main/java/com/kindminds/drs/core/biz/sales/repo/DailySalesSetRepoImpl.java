package com.kindminds.drs.core.biz.sales.repo;


import com.kindminds.drs.Filter;

import com.kindminds.drs.api.v2.biz.domain.model.DailySalesRepo;
import com.kindminds.drs.api.v2.biz.domain.model.DailySalesSetRepo;
import com.kindminds.drs.api.v2.biz.domain.model.sales.DailySalesSet;
import com.kindminds.drs.core.biz.sales.DailySalesSetImpl;

import java.util.*;

public  class DailySalesSetRepoImpl implements DailySalesSetRepo {


    DailySalesRepo dailySalesRepo =  new DailySalesRepoImpl();


    @Override
    public void add(DailySalesSet item) {

    }

    @Override
    public void add(List<DailySalesSet> items) {

    }

    @Override
    public void edit(DailySalesSet item) {

    }

    @Override
    public void remove(DailySalesSet item) {

    }

    @Override
    public Optional<DailySalesSet> findById(String id) {
        return Optional.empty();
    }

    public Optional<DailySalesSet> findOne(Filter filter) {

        List dsList = dailySalesRepo.find(filter);
        DailySalesSet dsSet = new DailySalesSetImpl(dsList);

        return Optional.of(dsSet);

    }

    @Override
    public List<DailySalesSet> find(Filter filter) {
        return null;
    }


}