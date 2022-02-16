package com.kindminds.drs.api.data.access.rdb.bizProcess;

import com.kindminds.drs.api.v2.biz.domain.model.bizProcess.ProcessTask;

public  interface ProcessTaskDao {

    void insert(ProcessTask processTask );

    void getProcessTask(String id);
}