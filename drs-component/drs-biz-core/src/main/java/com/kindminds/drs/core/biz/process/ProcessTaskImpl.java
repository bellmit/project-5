package com.kindminds.drs.core.biz.process;

import com.fasterxml.uuid.Generators;
import com.kindminds.drs.api.v2.biz.domain.model.bizProcess.ProcessTask;


public  class ProcessTaskImpl implements ProcessTask {
  
    private final String id;
  
    private final String processKey;
  
    private final String processInstanceId;
  
    private final String taskId;

  
    public String getId() {
        return this.id;
    }

  
    public String getProcessKey() {
        return this.processKey;
    }

  
    public String getProcessInstanceId() {
        return this.processInstanceId;
    }

  
    public String getTaskId() {
        return this.taskId;
    }

    public ProcessTaskImpl( String id,  String processKey,  String processInstanceId,  String taskId) {

        this.id = id;
        this.processKey = processKey;
        this.processInstanceId = processInstanceId;
        this.taskId = taskId;
    }
}
