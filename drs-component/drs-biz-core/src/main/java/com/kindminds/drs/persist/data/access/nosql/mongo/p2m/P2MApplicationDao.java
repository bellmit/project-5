package com.kindminds.drs.persist.data.access.nosql.mongo.p2m;


import com.kindminds.drs.persist.data.access.nosql.mongo.DrsMongoClient;
import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Sorts.descending;

public class P2MApplicationDao  {


    public String intoMongodb(String json){


        Document doc = Document.parse(json);

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
                        .getCollection("p2mApplication");

                return   p2mApplication.insertOne(doc).getInsertedId().toString().
                        replace("BsonObjectId{value=","").replace("}","");
            }
        };

        try {

            final String trxResultActual =  clientSession.withTransaction(txnBody, txnOptions).toString();

            return  trxResultActual;
        } catch (RuntimeException e) {
            // some error handling
        } finally {
            clientSession.close();
        }


        return "";
    }


    public void intoMongodbException(String json){

        Document doc = Document.parse(json);

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
                        .getCollection("p2mApplication_exception");

                p2mApplication.insertOne(doc);

                return "done";
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


    public String save(Document document) {

        String status = document.get("status").toString();

        if(status.equals("Pending")){

            if(document.containsKey("_id")){
                return this.update(document );
            }else{

                final ClientSession clientSession = DrsMongoClient.getInstance().getClient().startSession();
                /* Step 2: Optional. Define options to use for the transaction. */
                TransactionOptions txnOptions = TransactionOptions.builder()
                        .readPreference(ReadPreference.primary())
                        .readConcern(ReadConcern.LOCAL)
                        .writeConcern(WriteConcern.MAJORITY)
                        .build();

                TransactionBody txnBody = new TransactionBody<String>() {
                    public String execute() {

                        MongoCollection<Document> p2mApplicationCol = DrsMongoClient.getInstance().getDatabase()
                                .getCollection("p2mApplication");

                        InsertOneResult insertOneResult = p2mApplicationCol.insertOne(document);
                        return insertOneResult.getInsertedId().asObjectId().getValue().toString();
                    }
                };

                try {
            /*
               Step 4: Use .withTransaction() to start a transaction,
               execute the callback, and commit (or abort on error).
            */
                    final String trxResultActual =  clientSession.withTransaction(txnBody, txnOptions).toString();

                    return  trxResultActual;
                } catch (RuntimeException e) {
                    // some error handling
                } finally {
                    clientSession.close();
                }

                return "";

            }
        }
        else{
            return this.update(document );
        }

    }

    private String update(Document document ){

        String p2mApplicationId = document.get("_id").toString();

        final ClientSession clientSession = DrsMongoClient.getInstance().getClient().startSession();
        /* Step 2: Optional. Define options to use for the transaction. */
        TransactionOptions txnOptions = TransactionOptions.builder()
                .readPreference(ReadPreference.primary())
                .readConcern(ReadConcern.LOCAL)
                .writeConcern(WriteConcern.MAJORITY)
                .build();

        TransactionBody txnBody = new TransactionBody<String>() {
            public String execute() {

                MongoCollection<Document> p2mApplicationReversions = DrsMongoClient.getInstance().getDatabase()
                        .getCollection("p2mApplication_revisions");

                MongoCollection<Document> p2mApplicationCol = DrsMongoClient.getInstance().getDatabase()
                        .getCollection("p2mApplication");

                Bson filter = eq("_id", new ObjectId(p2mApplicationId));

                Document docori = p2mApplicationCol.find(eq("_id",
                        new ObjectId(p2mApplicationId))).first();

                final long timestamp = System.currentTimeMillis();
                String version = docori.get("version").toString();
                Long createdDateTime = Long.parseLong(docori.get("createdDateTime").toString());
                String id = docori.get("_id").toString();

                int count = Integer.parseInt(version);
                document.put("createdDateTime",createdDateTime);
                document.put("version",count+1);

                docori.append("p2mApplicationId",id);
                docori.remove("_id");
                p2mApplicationReversions.insertOne(clientSession , docori);
                p2mApplicationCol.replaceOne(clientSession , filter , document);

                return "Inserted into collections ";
            }
        };

        try {
            /*
               Step 4: Use .withTransaction() to start a transaction,
               execute the callback, and commit (or abort on error).
            */
            clientSession.withTransaction(txnBody, txnOptions);
        } catch (RuntimeException e) {
            // some error handling
        } finally {
            clientSession.close();
        }


        return p2mApplicationId;

    }

    public String updateSku(Document document ){

        String p2mApplicationId = document.get("_id").toString();

        final ClientSession clientSession = DrsMongoClient.getInstance().getClient().startSession();
        /* Step 2: Optional. Define options to use for the transaction. */
        TransactionOptions txnOptions = TransactionOptions.builder()
                .readPreference(ReadPreference.primary())
                .readConcern(ReadConcern.LOCAL)
                .writeConcern(WriteConcern.MAJORITY)
                .build();

        TransactionBody txnBody = new TransactionBody<String>() {
            public String execute() {

                MongoCollection<Document> p2mApplicationReversions = DrsMongoClient.getInstance().getDatabase()
                        .getCollection("p2mApplication_revisions");

                MongoCollection<Document> p2mApplicationCol = DrsMongoClient.getInstance().getDatabase()
                        .getCollection("p2mApplication");

                Bson filter = eq("_id", new ObjectId(p2mApplicationId));

                Document docori = p2mApplicationCol.find(eq("_id",
                        new ObjectId(p2mApplicationId))).first();

                final long timestamp = System.currentTimeMillis();
                String version = docori.get("version").toString();
                String id = docori.get("_id").toString();

                int count = Integer.parseInt(version);
                document.put("version",count+1);
                document.put("updatedDateTime", timestamp);

                docori.append("p2mApplicationId",id);
                docori.remove("_id");

                /*
                   Important:: You must pass the session to the operations.
                 */

                p2mApplicationReversions.insertOne(clientSession , docori);
                p2mApplicationCol.replaceOne(clientSession , filter , document);

                return "Inserted into collections ";
            }
        };

        try {
            /*
               Step 4: Use .withTransaction() to start a transaction,
               execute the callback, and commit (or abort on error).
            */
            clientSession.withTransaction(txnBody, txnOptions);
        } catch (RuntimeException e) {
            // some error handling
        } finally {
            clientSession.close();
        }


        return p2mApplicationId;

    }

    public String delete(Document document){

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
                        .getCollection("p2mApplication");

                MongoCollection<Document> p2mApplicationReversions = DrsMongoClient.getInstance().getDatabase()
                        .getCollection("p2mApplication_revisions");

                String id = document.get("_id").toString();

                Bson filter = eq("_id", new ObjectId(id));

                p2mApplicationReversions.insertOne(document);

                p2mApplication.deleteOne(filter);

                return "done";
            }
        };

        try {

            final String trxResultActual =  clientSession.withTransaction(txnBody, txnOptions).toString();


        } catch (RuntimeException e) {
            // some error handling
        } finally {
            clientSession.close();
        }



        return null;

    }


    public Document findById(String id){

        MongoCollection<Document> p2mApplication = DrsMongoClient.getInstance().getDatabase()
                .getCollection("p2mApplication");

        Document doc = p2mApplication.find(eq("_id", new ObjectId(id))).first();
        //System.out.println(doc.toJson());

        return doc ;
    }


    public List<Document> findByProductNameList(String productNameEN){

        MongoCollection<Document> p2mApplication = DrsMongoClient.getInstance().getDatabase()
                .getCollection("p2mApplication");

        FindIterable<Document> result = p2mApplication.find(and(eq("productNameEN",productNameEN),
                eq("status","Approved")));

        List<Document> resultList = result.into(new ArrayList<Document>());

        return resultList;

    }

    public long countByKcode(String kcode){

        MongoCollection<Document> p2mApplication = DrsMongoClient.getInstance().getDatabase()
                .getCollection("p2mApplication");

        if(kcode.equals("K2")){
            return p2mApplication.countDocuments();
        }else{
            return p2mApplication.countDocuments(eq("supplierId", kcode));
        }

    }

    public long countByFilter(String supplierId , String country , String status , String kcode , String product){

        MongoCollection<Document> p2mApplication = DrsMongoClient.getInstance().getDatabase()
                .getCollection("p2mApplication");

        long size = 0;

        if(supplierId.equals("K2")){
            size = p2mApplication.countDocuments(and(checkFilterIsNull("supplierId",kcode),checkFilterIsNull("selectedCountry",country),checkFilterIsNull("selectedProduct",product),checkFilterIsNull("status",status)));
        } else{
            size = p2mApplication.countDocuments(and(eq("supplierId", supplierId),checkFilterIsNull("selectedCountry",country),checkFilterIsNull("status",status)));
        }

        return size;

    }

    public String findList(String supplierId ,  int pageIndex , String country , String status , String kcode , String product) {

        MongoCollection<Document> p2mApplication = DrsMongoClient.getInstance().getDatabase()
                .getCollection("p2mApplication");

        Bson sort = descending("createdDateTime");

        FindIterable<Document> result = null;

        if(supplierId.equals("K2")){
            result = p2mApplication.find(and(checkFilterIsNull("supplierId",kcode),checkFilterIsNull("selectedCountry",country),checkFilterIsNull("selectedProduct",product),checkFilterIsNull("status",status))).sort(sort)
                            .skip((pageIndex -1 )* 10).limit(10);
        } else{
            result = p2mApplication.find(and(eq("supplierId", supplierId),checkFilterIsNull("selectedCountry",country),checkFilterIsNull("status",status))).sort(sort)
                            .skip((pageIndex -1 )* 10).limit(10);
        }



        String collect = StreamSupport.stream(result
                .spliterator(), false)
                .map(Document::toJson)
                .collect(Collectors.joining(", ", "[", "]"));


        return collect;

    }

    private Bson checkFilterIsNull(String fieldName,String f){
        if(f.equals("")) {
            return ne(fieldName,f);
        } else{
            return eq(fieldName,f);
        }
    }

    public String findAllList(String kcode, String selectedKcode, String selectedProduct) {

        MongoCollection<Document> p2mApplication = DrsMongoClient.getInstance().getDatabase()
                .getCollection("p2mApplication");

        Bson sort1 = ascending("supplierId");
        Bson sort2 = ascending("_id");

        FindIterable<Document> result = null;

        if(kcode.equals("K2")){
            if(selectedKcode.equals("")){
                result = p2mApplication.find().sort(sort1);
            } else{
                if(selectedProduct.equals("All") || selectedProduct.equals("")){
                    result = p2mApplication.find(eq("supplierId", selectedKcode)).sort(sort1);
                } else {
                    result = p2mApplication.find(and(eq("supplierId", selectedKcode),eq("selectedProduct",selectedProduct))).sort(sort1);
                }
            }
        } else{
            result = p2mApplication.find(eq("supplierId", kcode)).sort(sort2);
        }

        String newresult = "";

        for(Document doc : result) {
            String id = doc.get("_id").toString();
            String name = doc.get("name").toString();
            String supplierId = doc.get("supplierId").toString();
            String country = doc.get("selectedCountry").toString();
            String status = doc.get("status").toString();
            String result2 = "{\"_id\":\"" + id + "\"," + "\"name\":\"" + name + "\"," + "\"kcode\":\"" + supplierId + "\"," + "\"selectedCountry\":\"" + country + "\"," + "\"status\":\"" + status + "\"},";
            newresult = newresult + result2;
        }
        if(newresult.length()>0) {
            newresult = "[" + newresult.substring(0, newresult.length() - 1) + "]";
        }else{
            newresult = "[]";
        }

        return newresult;
    }

    public List<Document> findApprovedCountryList(String kcode){

        MongoCollection<Document> p2mApplication = DrsMongoClient.getInstance().getDatabase()
                .getCollection("p2mApplication");

        FindIterable<Document> result = p2mApplication.find(and(eq("supplierId",kcode),
                eq("status","Approved")));

        List<Document> resultList = result.into(new ArrayList<Document>());

        return resultList;

    }

    public List<String> findApprovedSkuByCountryList(String kcode, String selectedCountry){

        MongoCollection<Document> p2mApplication = DrsMongoClient.getInstance().getDatabase()
                .getCollection("p2mApplication");

        FindIterable<Document> result = p2mApplication.find(and(eq("supplierId",kcode),
                eq("status","Approved"),eq("selectedCountry",selectedCountry)));

        List<String> list = new ArrayList<String>();

        for(Document doc : result) {
            List<String> skus = (List<String>)doc.get("appliedSkuCode");

            String productNameEN = doc.get("productNameEN").toString();

            for(String i :skus){

                i = kcode + "-" + productNameEN + "-" + i;

                if (!list.contains(i)) {
                    list.add(i);
                }
            }
        }

        return list;

    }

    public Integer findMaxSerialNumber(String kcode){


        MongoCollection<Document> p2mApplication = DrsMongoClient.getInstance().getDatabase()
                .getCollection("p2mApplication");

        FindIterable<Document> result = p2mApplication.find(eq("supplierId", kcode))
                .sort(new BasicDBObject("serial_num", -1)).limit(1);;

        List<Document> resultList = result.into(new ArrayList<Document>());


        return resultList.size() == 0 ? 0 :
                resultList.get(0).get("serial_num") == null ? 0 : (Integer) resultList.get(0).get("serial_num");
    }

    public void findCanBeChanged(String kcode){

    }


    public Integer findTotalAppliedSkuNumber(String kCode){
        int count=0;
        MongoCollection<Document> p2mApplication = DrsMongoClient.getInstance().getDatabase().getCollection("p2mApplication");
        BasicDBObject filterPending =new BasicDBObject();
        filterPending.append("supplierId", kCode);
        filterPending.append("status","Pending");
        BasicDBObject filterInReview =new BasicDBObject();
        filterInReview.append("supplierId", kCode);
        filterInReview.append("status","In Review");

        FindIterable<Document> p2mPending =p2mApplication.find(filterPending);
        FindIterable<Document> p2mInReview =p2mApplication.find(filterInReview);

        count= count+ getAppliedSkuCount(p2mPending)+ getAppliedSkuCount(p2mInReview);

        return count;
    }

    public Integer findTotalOnSaleSkuNumber(String kCode){
        int count=0;
        MongoCollection<Document> p2mApplication = DrsMongoClient.getInstance().getDatabase().getCollection("p2mApplication");
        BasicDBObject filter =new BasicDBObject();
        filter.append("supplierId", kCode);
        filter.append("status","Approved");

        FindIterable<Document> p2mApproved =p2mApplication.find(filter);
        count+= getAppliedSkuCount(p2mApproved);
        return count;
    }

    private Integer getAppliedSkuCount(FindIterable<Document> p2mList){
        int count =0;
        for(Document doc : p2mList) {
            List<String> sku =(List<String>) doc.get("appliedSkuCode");
            count += sku.size();
//            List<Object> skuLine =(List<Object>) doc.get("appliedSku");
//            count += skuLine.size();
        }

        return count;
    }

    public List<Document> findP2mApplicationList(){

        MongoCollection<Document> p2mApplication = DrsMongoClient.getInstance().getDatabase()
                .getCollection("p2mApplication");

        FindIterable<Document> result = p2mApplication.find();

        List<Document> resultList = result.into(new ArrayList<Document>());

        return resultList;

    }

}