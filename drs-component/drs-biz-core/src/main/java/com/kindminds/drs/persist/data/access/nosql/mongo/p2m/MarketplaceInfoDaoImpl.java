package com.kindminds.drs.persist.data.access.nosql.mongo.p2m;


import com.kindminds.drs.api.data.access.nosql.mongo.p2m.MarketplaceInfoDao;
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

import java.util.List;

import static com.mongodb.client.model.Filters.*;


public class MarketplaceInfoDaoImpl implements MarketplaceInfoDao {

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
                        .getCollection("p2mMarketplaceInfo");

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

                MongoCollection<Document> p2mApplication = DrsMongoClient.getInstance().getDatabase()
                        .getCollection("p2mMarketplaceInfo");

                MongoCollection<Document> p2mMarketplaceInfo_revisions = DrsMongoClient.getInstance().getDatabase()
                        .getCollection("p2mMarketplaceInfo_revisions");

                Document doc = Document.parse(json);

                String p2mApplicationId = doc.get("p2mApplicationId").toString();

                Bson filter = eq("p2mApplicationId", new ObjectId(p2mApplicationId));

                Document docori = p2mApplication.find(eq("p2mApplicationId", new ObjectId(p2mApplicationId))).first();

                final UpdateResult updateResult = p2mApplication.replaceOne(filter , doc);
                if(updateResult.getModifiedCount() == 0){
                    doc.append("version","1");
                    p2mApplication.insertOne(doc);
                }
                else{
                    String version = docori.get("version").toString();
                    int count = Integer.parseInt(version);
                    String id = docori.get("_id").toString();
                    doc.append("version",count+1);
                    docori.append("p2mMarketplaceInfoId",id);
                    docori.remove("_id");
                    p2mMarketplaceInfo_revisions.insertOne(docori);

                    p2mApplication.replaceOne(filter , doc);
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


        MongoCollection<Document> p2mMarketplaceInfo = DrsMongoClient.getInstance().getDatabase()
                .getCollection("p2mMarketplaceInfo");

        Document doc = p2mMarketplaceInfo.find(eq("p2mApplicationId", new ObjectId(p2mApplicationId))).first();
       // System.out.println(doc.toJson());

        return doc !=null ? doc.toJson() : "{}";

    }

    @Override
    public Document findDocumentByP2MApplicationId(String p2mApplicationId){


        MongoCollection<Document> p2mMarketplaceInfo = DrsMongoClient.getInstance().getDatabase()
                .getCollection("p2mMarketplaceInfo");

        Document doc = p2mMarketplaceInfo.find(eq("p2mApplicationId", new ObjectId(p2mApplicationId))).first();
        // System.out.println(doc.toJson());

        return doc;
    }

    @Override
    public String findCommentByP2MId(String p2mApplicationId){

        MongoCollection<Document> p2mMarketplaceInfo = DrsMongoClient.getInstance().getDatabase()
                .getCollection("p2mMarketplaceInfo");

        Document doc = p2mMarketplaceInfo.find(eq("p2mApplicationId", new ObjectId(p2mApplicationId))).first();
        String m = "\"marketPlaceInfo\" : {" +
                "\"competitorInfo\" : \"\"," +
                "\"main\": \"\"," +
                "\"appliedSku\": []" +
                "},";


        if(doc != null){
        if(doc.get("comment") != null){
            Document comment =(Document) doc.get("comment");
            String competitorInfo = comment.get("competitorInfo").toString();
            Document main =(Document) doc.get("main");
            Document mcomment =(Document) main.get("comment");

            Document advanced =(Document) doc.get("advanced");
            Document acomment =(Document) advanced.get("comment");

            List<Object> appliedSku = (List<Object>)doc.get("appliedSku");

            String appliedSkuData = "";

            for(Object x :appliedSku){
                Document sku = (Document) x;
                String sellerSku = sku.get("sellerSku").toString();
                Document scomment =(Document) sku.get("comment");
                scomment.append("sellerSku",sellerSku);
                appliedSkuData = appliedSkuData + scomment.toJson() + ",";
            }
            if(appliedSkuData.length()>0) {
                appliedSkuData = appliedSkuData.substring(0, appliedSkuData.length() - 1);
            }

            m = "\"marketPlaceInfo\" : {" +
                    "\"competitorInfo\" : \"" + competitorInfo+ "\"," +
                    "\"main\": " + mcomment.toJson() + "," +
                    "\"advanced\": " + acomment.toJson() + "," +
                    "\"appliedSku\": [" + appliedSkuData + "]" +
                    "},";
        }}



        return m;

    }


}
