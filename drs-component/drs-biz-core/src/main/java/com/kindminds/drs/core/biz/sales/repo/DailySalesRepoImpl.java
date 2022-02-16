package com.kindminds.drs.core.biz.sales.repo;

import com.kindminds.drs.api.data.row.sales.DailySalesRow;
import com.kindminds.drs.api.v2.biz.domain.model.DailySalesRepo;
import com.kindminds.drs.Filter;
import com.kindminds.drs.api.v2.biz.domain.model.sales.DailySales;
import com.kindminds.drs.core.biz.sales.DailySalesImpl;

import com.kindminds.drs.api.data.access.rdb.sales.DailySalesDao;
import com.kindminds.drs.service.util.SpringAppCtx;

import java.util.*;

public class DailySalesRepoImpl implements DailySalesRepo {

    private DailySalesDao dao = SpringAppCtx.get().getBean(DailySalesDao.class);


    @Override
    public void add(DailySales item) {

    }

    @Override
    public void add(List<DailySales> items) {

    }

    @Override
    public void edit(DailySales item) {

    }

    @Override
    public void remove(DailySales item) {

    }

    @Override
    public Optional<DailySales> findById(String id) {
        return Optional.empty();
    }

    @Override
    public Optional<DailySales> findOne(Filter filter) {
        return Optional.empty();
    }

    public List<DailySales> find(Filter filter) {

        List r = new ArrayList<DailySales>();
        List<DailySalesRow> rList = dao.queryDailySalesByFilter(filter);

        rList.forEach(it -> {
            if(it != null){
                DailySales ds = new DailySalesImpl(it);
                r.add(ds);
            }
        });


        return r;

    }



}