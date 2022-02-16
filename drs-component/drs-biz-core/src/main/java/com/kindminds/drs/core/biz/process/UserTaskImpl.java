package com.kindminds.drs.core.biz.process;

import com.kindminds.drs.UserInfo;
import com.kindminds.drs.UserTaskType;
import com.kindminds.drs.persist.data.access.nosql.mongo.p2m.P2MApplicationDao;
import com.kindminds.drs.util.Encryptor;
import com.kindminds.drs.api.v1.model.process.UserTask;
import org.springframework.security.core.userdetails.User;

import javax.swing.text.Document;

public class UserTaskImpl implements UserTask {

    private UserTaskType type;
    private int assignee;
    private String refId;
    private String taskUrl;
    private String taskKcode;

    public UserTaskImpl(){

    }


    public UserTaskImpl(UserTaskType type , String refId  , String kcode){
        this.type =  type;
        this.refId = refId;
        this.generateTaskUrl();
        this.taskKcode = kcode;
    }

    private void generateTaskUrl(){

        if(type == UserTaskType.ReviewP2MApplication){
            this.taskUrl = "/p2m/p/a?i=" + Encryptor.encrypt(refId);
        }
    }

    public void assign(UserInfo user){
        this.assignee = user.getUserId();
    }

    public String generateNotification(){

        String message = "";

        if(type == UserTaskType.ReviewP2MApplication){

            P2MApplicationDao dao = new P2MApplicationDao();
            org.bson.Document doc  = dao.findById(refId);

            //message =  this.taskKcode  + " " +   doc.get("name")  + " Approval";
            message = doc.get("name")  + " Approval";
        }

        return message;
    }


    @Override
    public UserTaskType getType() {
        return this.type;
    }

    @Override
    public int getAssignee() {
        return this.assignee;
    }

    @Override
    public String getTaskUrl() {
        return this.taskUrl;
    }
}
