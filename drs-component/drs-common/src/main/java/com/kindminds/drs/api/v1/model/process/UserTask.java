package com.kindminds.drs.api.v1.model.process;

import com.kindminds.drs.UserInfo;
import com.kindminds.drs.UserTaskType;

public interface UserTask {

    UserTaskType getType();
     int getAssignee();
     String getTaskUrl();

     void assign(UserInfo userInfo);

}
