package com.kindminds.drs.core.biz.repo;

import com.kindminds.drs.Filter;

import com.kindminds.drs.api.data.access.rdb.CompanyDao;
import com.kindminds.drs.api.v1.model.Company;
import com.kindminds.drs.api.v2.biz.domain.model.repo.CompanyRepo;
import com.kindminds.drs.service.util.SpringAppCtx;

import java.util.*;


class CompanyRepoImpl implements CompanyRepo {

    CompanyDao  dao = SpringAppCtx.get().getBean(CompanyDao.class);


    @Override
    public void add(Company item) {

    }

    @Override
    public void add(List<Company> items) {

    }

    @Override
    public void edit(Company item) {

    }

    @Override
    public void remove(Company item) {

    }

    @Override
    public Optional<Company> findById(String id) {
        return Optional.empty();
    }

    @Override
    public Optional<Company> findOne(Filter filter) {
        return Optional.empty();
    }

    @Override
    public List<Company> find(Filter filter) {
        return null;
    }
}