package com.kindminds.drs.persist.data.access.nosql.mongo.notification;


import com.kindminds.drs.persist.data.access.nosql.mongo.DrsMongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.mongodb.client.model.Filters.eq;

public class NotificationDao  {

    MongoCollection<Document> notificationCol = DrsMongoClient.getInstance().getCollection("notification");

    public void save(Document document){

        if(document.get("_id") != null){
            String id = document.get("_id").toString();
            Bson filter = eq("_id", new ObjectId(id));
            notificationCol.replaceOne(filter , document);
        }else{

            notificationCol.insertOne(document);
        }


    }

    public void delete(String id ){


        MongoCollection<Document> notificationRevisions = DrsMongoClient.getInstance().getDatabase()
                .getCollection("notification_revisions");

        Document doc = notificationCol.find(eq("_id",
                new ObjectId(id))).first();

        notificationRevisions.insertOne(doc);
        notificationCol.deleteOne(doc);


    }

    public Document findById(String id){

        Document doc = notificationCol.find(eq("_id",
                new ObjectId(id))).first();

        return doc;

    }

    public List<Document> findUnread(int userId){


        Bson query = Filters.and(eq("userId",userId) , eq("read",false));
        FindIterable<Document> result = notificationCol.find(query);

       List<Document> resultList = StreamSupport.stream(result
                .spliterator(), false)
                .collect(Collectors.toList());


        return resultList;

    }

    public long countByUserId(int userId){

        final long size = notificationCol.countDocuments(eq("userId", userId));

        return size ;
    }

    public String findByUserId(int userId , int pageIndex){

        FindIterable<Document> result = notificationCol.find(eq("userId", userId))
                .skip((pageIndex -1 )* 5).limit(5);


        String collect = StreamSupport.stream(result
                .spliterator(), false)
                .map(Document::toJson)
                .collect(Collectors.joining(", ", "[", "]"));

        return collect;

    }

}
