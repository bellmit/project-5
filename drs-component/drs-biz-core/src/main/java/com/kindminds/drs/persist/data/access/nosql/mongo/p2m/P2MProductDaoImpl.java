package com.kindminds.drs.persist.data.access.nosql.mongo.p2m;

import com.kindminds.drs.api.data.access.nosql.mongo.p2m.P2MProductInfoDao;
import com.kindminds.drs.persist.data.access.nosql.mongo.DrsMongoClient;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.TransactionOptions;
import com.mongodb.WriteConcern;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.TransactionBody;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import static com.mongodb.client.model.Filters.eq;


public class P2MProductDaoImpl implements P2MProductInfoDao {

    private DrsMongoClient dao =  DrsMongoClient.getInstance();

    @Override
    public void intoMongodb(String json){


        final ClientSession clientSession = DrsMongoClient.getInstance().getClient().startSession();
        /* Step 2: Optional. Define options to use for the transaction. */
        TransactionOptions txnOptions = TransactionOptions.builder()
                .readPreference(ReadPreference.primary())
                .readConcern(ReadConcern.LOCAL)
                .writeConcern(WriteConcern.MAJORITY)
                .build();

        TransactionBody txnBody = new TransactionBody<String>() {
            public String execute() {

                MongoCollection<Document> p2mApplication = DrsMongoClient.getInstance().getDatabase()
                        .getCollection("p2mProductAdvancedInfo");

                Document doc = Document.parse(json);

                p2mApplication.insertOne(doc);

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

                MongoCollection<Document> p2mProductAdvancedInfo = DrsMongoClient.getInstance().getDatabase()
                        .getCollection("p2mProductAdvancedInfo");

                MongoCollection<Document> p2mProductAdvancedInfo_revisions = DrsMongoClient.getInstance().getDatabase()
                        .getCollection("p2mProductAdvancedInfo_revisions");

                Document doc = Document.parse(json);

                String p2mApplicationId = doc.get("p2mApplicationId").toString();

                Bson filter = eq("p2mApplicationId", new ObjectId(p2mApplicationId));

                Document docori = p2mProductAdvancedInfo.find(eq("p2mApplicationId", new ObjectId(p2mApplicationId))).first();

                final UpdateResult updateResult = p2mProductAdvancedInfo.replaceOne(filter , doc);
                if(updateResult.getModifiedCount() == 0){
                    doc.append("version","1");
                    p2mProductAdvancedInfo.insertOne(doc);
                }
                else{
                    String version = docori.get("version").toString();
                    int count = Integer.parseInt(version);
                    String id = docori.get("_id").toString();
                    doc.append("version",count+1);
                    docori.append("p2mProductAdvancedInfoId",id);
                    docori.remove("_id");
                    p2mProductAdvancedInfo_revisions.insertOne(docori);

                    p2mProductAdvancedInfo.replaceOne(filter , doc);
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
    public String findByP2MApplicationId(String p2mApplicationId){


        MongoCollection<Document> p2mProductAdvancedInfo = dao.getDatabase().getCollection("p2mProductAdvancedInfo");

        Document doc = p2mProductAdvancedInfo.find(eq("p2mApplicationId", new ObjectId(p2mApplicationId))).first();
       // System.out.println(doc.toJson());

        return doc !=null ? doc.toJson() : "{}";

    }

    @Override
    public Document findDocumentByP2MApplicationId(String p2mApplicationId){
        MongoCollection<Document> p2mProductAdvancedInfo = dao.getDatabase().getCollection("p2mProductAdvancedInfo");

        Document doc = p2mProductAdvancedInfo.find(eq("p2mApplicationId", new ObjectId(p2mApplicationId))).first();
        // System.out.println(doc.toJson());

        return doc;
    }

    @Override
    public String findCommentByP2MId(String p2mApplicationId){

        MongoCollection<Document> p2mProductAdvancedInfo = DrsMongoClient.getInstance().getDatabase()
                .getCollection("p2mProductAdvancedInfo");

        Document doc = p2mProductAdvancedInfo.find(eq("p2mApplicationId", new ObjectId(p2mApplicationId))).first();

        String p = "\"productInfo\" : \"\"";
        if(doc != null){
            Document comment =(Document) doc.get("comment");

             p = "\"productInfo\" : " + comment.toJson();
        }

        return p;

    }
}
