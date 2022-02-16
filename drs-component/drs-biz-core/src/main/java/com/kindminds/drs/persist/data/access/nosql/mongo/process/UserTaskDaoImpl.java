package com.kindminds.drs.persist.data.access.nosql.mongo.process;


import com.kindminds.drs.api.data.access.nosql.mongo.userTask.UserTaskDao;
import com.kindminds.drs.api.v1.model.process.UserTask;
import com.kindminds.drs.persist.data.access.nosql.mongo.DrsMongoClient;

import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.TransactionOptions;
import com.mongodb.WriteConcern;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.TransactionBody;
import org.bson.Document;
import org.springframework.stereotype.Repository;

import static com.mongodb.client.model.Filters.eq;


public class UserTaskDaoImpl implements UserTaskDao {

    @Override
    public void save(UserTask userTask) {

        final ClientSession clientSession = DrsMongoClient.getInstance().getClient().startSession();
        /* Step 2: Optional. Define options to use for the transaction. */
        TransactionOptions txnOptions = TransactionOptions.builder()
                .readPreference(ReadPreference.primary())
                .readConcern(ReadConcern.LOCAL)
                .writeConcern(WriteConcern.MAJORITY)
                .build();

        TransactionBody txnBody = new TransactionBody<String>() {
            public String execute() {

                MongoCollection<Document> userTaskCol = DrsMongoClient.getInstance().getDatabase()
                        .getCollection("userTask");

                Document doc = new Document();
                doc.append("type",userTask.getType().getName());
                doc.append("assignee",userTask.getAssignee());
                doc.append("taskUrl",userTask.getTaskUrl());

                userTaskCol.insertOne(doc);

                return "";
            }
        };

        try {

            final String trxResultActual =  clientSession.withTransaction(txnBody, txnOptions).toString();

        } catch (RuntimeException e) {
            // some error handling
        } finally {
            clientSession.close();
        }

    }

}
