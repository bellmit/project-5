package com.kindminds.drs.api.data.access.nosql.mongo.userTask;

import com.kindminds.drs.api.v1.model.process.UserTask;

public interface UserTaskDao {

    void save(UserTask userTask);

}
