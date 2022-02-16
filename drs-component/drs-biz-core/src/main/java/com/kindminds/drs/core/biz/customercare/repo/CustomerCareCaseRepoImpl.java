package com.kindminds.drs.core.biz.customercare.repo;


import com.kindminds.drs.Filter;

import com.kindminds.drs.api.v2.biz.domain.model.CustomerCareCaseRepo;
import com.kindminds.drs.api.v2.biz.domain.model.customercare.CustomerCareCase;
import com.kindminds.drs.core.biz.customercare.CustomerCareCaseImpl;
import com.kindminds.drs.api.data.access.rdb.customercare.CustomerCareCaseDao;
import com.kindminds.drs.service.util.SpringAppCtx;

import java.util.*;

public  class CustomerCareCaseRepoImpl implements CustomerCareCaseRepo {


    private CustomerCareCaseDao dao = SpringAppCtx.get().getBean(CustomerCareCaseDao.class);


    @Override
    public void add(CustomerCareCase item) {

    }

    @Override
    public void add(List<CustomerCareCase> items) {

    }

    @Override
    public void edit(CustomerCareCase item) {

    }

    @Override
    public void remove(CustomerCareCase item) {

    }

    @Override
    public Optional<CustomerCareCase> findById(String id) {
        return Optional.empty();
    }

    @Override
    public Optional<CustomerCareCase> findOne(Filter filter) {
        return Optional.empty();
    }

    public List<CustomerCareCase>  find(Filter filter ) {


        List<CustomerCareCase> r = new ArrayList<CustomerCareCase>();
        dao.getCustomerCareCaseByFilter(filter).forEach(it ->{
            if(it != null){
                CustomerCareCaseImpl ds = new CustomerCareCaseImpl(it);
                r.add(ds);
            }
        });

        return r;

    }


}