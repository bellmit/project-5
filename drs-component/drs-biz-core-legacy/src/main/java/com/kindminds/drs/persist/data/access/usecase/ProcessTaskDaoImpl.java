package com.kindminds.drs.persist.data.access.usecase;


import com.kindminds.drs.api.data.access.rdb.bizProcess.ProcessTaskDao;

import com.kindminds.drs.api.v2.biz.domain.model.bizProcess.ProcessTask;
import org.springframework.stereotype.Repository;

@Repository
public class ProcessTaskDaoImpl implements ProcessTaskDao {





    public void insert(ProcessTask processTask) {
        /*
        println(processTask.processInstanceId)
        println(processTask.processKey)
        println(processTask.taskId)
        println(processTask.id)

         */

    }

    public void getProcessTask(String id) {


       // println("CCCCCCCCCCCCCcc")

        String  sql = "select * " +
                " from biz_process.process_task   " +
                " where  id = ?   ";


        /*

        filter.getCriteriaList().forEach {
            if(it.field == CustomerCareCaseQueryField.KCode){
                bindValues.add(it.value)
            }

            if(it.field == CustomerCareCaseQueryField.StartDate){
                bindValues.add(Timestamp.valueOf( LocalDateTime.parse(it.value,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))))
            }

            if(it.field == CustomerCareCaseQueryField.EndDate){
                bindValues.add(Timestamp.valueOf( LocalDateTime.parse(it.value,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))))
            }
        }
        */


        //List result = dsl.fetch(sql ,bindValues.get(0) );

        //println("DDDDDDDDDDDDDDDDd")
        //println(result?.size)


        /*
        return if(result?.toMutableList() != null){
            result.toMutableList()
        }else{
            emptyList<CustomerCareCaseRow?>().toMutableList()
        }
        */





    }




}