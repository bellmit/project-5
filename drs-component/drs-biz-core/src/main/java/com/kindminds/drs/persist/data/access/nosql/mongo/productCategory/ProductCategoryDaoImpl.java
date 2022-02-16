package com.kindminds.drs.persist.data.access.nosql.mongo.productCategory;

import com.kindminds.drs.api.data.access.nosql.mongo.productCategory.ProductCategoryDao;
import com.kindminds.drs.persist.data.access.nosql.mongo.DrsMongoClient;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.TransactionOptions;
import com.mongodb.WriteConcern;
import com.mongodb.client.ClientSession;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.TransactionBody;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.mongodb.client.model.Filters.eq;


public class ProductCategoryDaoImpl implements ProductCategoryDao {

    @Override
    public void save(String json) {



        final ClientSession clientSession = DrsMongoClient.getInstance().getClient().startSession();
        /* Step 2: Optional. Define options to use for the transaction. */
        TransactionOptions txnOptions = TransactionOptions.builder()
                .readPreference(ReadPreference.primary())
                .readConcern(ReadConcern.LOCAL)
                .writeConcern(WriteConcern.MAJORITY)
                .build();

        TransactionBody txnBody = new TransactionBody<String>() {
            public String execute() {

                MongoCollection<Document> productCategory = DrsMongoClient.getInstance().getDatabase()
                        .getCollection("productCategory");

                Document doc = Document.parse(json);
                String parent = doc.get("parent").toString();

                Bson filter = eq("parent",  parent);
                final UpdateResult updateResult = productCategory.replaceOne(filter , doc);
                if(updateResult.getModifiedCount() == 0){
                    productCategory.insertOne(doc);
                }

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

    @Override
    public String findByParent(String parent) {

        MongoCollection<Document> productCategory = DrsMongoClient.getInstance().getDatabase()
                .getCollection("productCategory");

        Document doc = productCategory.find(eq("parent", parent)).first();

        return doc !=null ? doc.toJson() : "{}";
    }

    @Override
    public String findList(String parent) {

        MongoCollection<Document> productCategory = DrsMongoClient.getInstance().getDatabase()
                .getCollection("productCategory");

        List<String> resultList = new ArrayList<>();
        FindIterable<Document> documents = productCategory.find();

        String collect = StreamSupport.stream(productCategory.find(eq("parent", parent)).spliterator(), false)
                .map(Document::toJson)
                .collect(Collectors.joining(", ","[","]"));

        return collect;
    }

}
