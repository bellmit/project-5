package com.kindminds.drs;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneId;

public class Notification {

    public Notification(String content){

        this.content = content;
        this.dateTime = OffsetDateTime.now();
    }

    public String content;

    public OffsetDateTime dateTime;

    public String getContent() {
        return content;
    }

    public OffsetDateTime getDateTime() {
        return dateTime;
    }

    public String toJson(){

        String msg =  "{\"userId\":1 ,\"name\":\""+ this.content +"\",\"createdDateTime\": \"" +
                Timestamp.valueOf(this.dateTime.atZoneSameInstant(ZoneId.of("Asia/Taipei")).toLocalDateTime()) + "\"}";
        return msg;
    }

}
