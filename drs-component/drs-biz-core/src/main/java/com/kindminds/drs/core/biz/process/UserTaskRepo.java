package com.kindminds.drs.core.biz.process;

import com.kindminds.drs.api.data.access.nosql.mongo.userTask.UserTaskDao;
import com.kindminds.drs.api.data.access.rdb.bizProcess.ProcessTaskDao;
import com.kindminds.drs.persist.data.access.nosql.mongo.process.UserTaskDaoImpl;
import com.kindminds.drs.service.util.SpringAppCtx;
import com.kindminds.drs.api.v1.model.process.UserTask;

public class UserTaskRepo {

    private UserTaskDao dao = new UserTaskDaoImpl();

    public void save(UserTask userTask){

       dao.save(userTask);

    }

}
