package com.kindminds.drs.core.biz.repo.process;

import com.kindminds.drs.Filter;

import com.kindminds.drs.api.data.access.rdb.bizProcess.ProcessTaskDao;
import com.kindminds.drs.api.v2.biz.domain.model.bizProcess.ProcessTask;
import com.kindminds.drs.api.v2.biz.domain.model.repo.bizProcess.ProcessTaskRepo;
import com.kindminds.drs.service.util.SpringAppCtx;


import java.util.*;

public class ProcessTaskRepoImpl implements ProcessTaskRepo {

    private ProcessTaskDao dao = SpringAppCtx.get().getBean(ProcessTaskDao.class);

    @Override
    public void add( ProcessTask item) {

        this.dao.insert(item);
    }

    @Override
    public void add(List<ProcessTask> items) {


    }

    @Override
    public void edit( ProcessTask item) {

    }

    @Override
    public void remove( ProcessTask item) {

    }

    @Override
    public Optional findById( String id) {

        this.dao.getProcessTask(id);
        Optional var10000 = Optional.empty();

        return var10000;
    }

    @Override
    public Optional<ProcessTask> findOne(Filter filter) {
        return Optional.empty();
    }

    @Override
    public List<ProcessTask> find(Filter filter) {
        return null;
    }


}

