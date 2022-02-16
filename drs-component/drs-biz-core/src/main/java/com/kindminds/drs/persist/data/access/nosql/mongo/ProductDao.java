package com.kindminds.drs.persist.data.access.nosql.mongo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.*;
import com.mongodb.client.ClientSession;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.TransactionBody;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.descending;


public class ProductDao {

    ObjectMapper om = new ObjectMapper();

    private DrsMongoClient drsMongoClient =  DrsMongoClient.getInstance();

    private MongoCollection<Document> product = drsMongoClient.getDatabase().getCollection("product");

    private MongoCollection<Document> productException = drsMongoClient.getDatabase().getCollection("product_exception");

    public Document findByBaseCode(String baseCode){

        Document doc = product.find(eq("baseCode", baseCode)).first();
        
        return doc;
    }

    public String findByProductName(String kcode, String productName) {

        Document doc = product.find(eq("productNameEN", productName)).first();

//        BaseProduct baseProduct = mongoTemplate.findOne(
//                Query.query(Criteria.where("productNameEN").is(productName)), BaseProduct.class);

        return doc !=null ? doc.toJson() : "{}";

//        BaseProduct baseProduct = mongoTemplate.findOne(
//                Query.query(Criteria.where("productNameEN").is(productName)), BaseProduct.class);
//
//        try {
//            return om.writeValueAsString(baseProduct);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
    }

    public Document findByProductNameEN(String productName) {

        Document doc = product.find(eq("productNameEN", productName)).first();

        return doc;
    }


    public Document findById(String id) {

        /*
        if (productId.equals("All")) {
            return findByKcode(kcode, 1);
        }
        */

        Document doc = product.find(eq("_id", new ObjectId(id))).first();

        return doc;

//        try {
//            if (productId.equals("All")) {
//                return findByKcode(kcode, 0, 3);
//            }
//
//            BaseProduct baseProduct = mongoTemplate.findById(productId, BaseProduct.class);
//
//            return om.writeValueAsString(baseProduct);
//
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        return null;
    }




    public String findByKcode(String kcode, Integer pageIndex) {

        String baseProductList = findByIndexLimit(kcode, pageIndex);

        //ObjectNode response = om.createObjectNode();
        //response.set("bpList", createResponseBpList(kcode));
        //createResponseBpList(kcode);
        //response.set("products", om.convertValue(baseProductList, JsonNode.class));

        return baseProductList;
    }

    public List<Document> findByKcodeList(String kcode){

        FindIterable<Document> result = product.find(and(eq("supplierId",kcode),
                ne("bpStatus","cancelled")));

        List<Document> resultList = result.into(new ArrayList<Document>());

        return resultList;

    }

    public long countByKcode(String kcode){

        if(kcode.equals("K2")){
            return product.countDocuments();
        }else{
            return product.countDocuments(eq("supplierId", kcode));
        }

    }


    public String findNextpage(String kcode, Integer pageIndex) {

        String baseProductList = findByIndexLimit(kcode, pageIndex);

        //ObjectNode response = om.createObjectNode();
        //response.set("products", om.convertValue(baseProductList, JsonNode.class));


        return baseProductList;
    }

    private String findByIndexLimit(String kcode, Integer pageIndex) {

        Bson sort = descending("createdDateTime");

        FindIterable<Document> result = kcode.equals("K2") ? product.find(
                ne("bpStatus" ,"cancelled")).sort(sort).skip((pageIndex -1 )* 10).limit(10) :
                product.find(and(eq("supplierId",kcode),
                        ne("bpStatus","cancelled"))).sort(sort).
                        skip((pageIndex -1 )* 10).limit(10);


        String collect = StreamSupport.stream(result
                .spliterator(), false)
                .map(Document::toJson)
                .collect(Collectors.joining(", ", "[", "]"));

        return collect;


    }

    public List<Document> findSimpleProductList(String kcode){

        FindIterable<Document> result = kcode.equals("K2") ? product.find(
                ne("bpStatus" ,"cancelled")) :
                product.find(and(eq("supplierId",kcode),
                        ne("bpStatus","cancelled")));

        List<Document> resultList = result.into(new ArrayList<Document>());

        return resultList;

    }

    public String findList(String kcode) {


        FindIterable<Document> documents = product.find();

        String collect = StreamSupport.stream(product.find(eq("supplierId", kcode)).spliterator(), false)
                .map(Document::toJson)
                .collect(Collectors.joining(", ","[","]"));

        return collect;
    }



    public void createProduct(Document document) {

        //bpStatus
        //applyingMarketplace
        //sellingMarketplace


//        Document doc = Document.parse(json);
//
//        doc.append("supplierId",kcode);

        

        final ClientSession clientSession = DrsMongoClient.getInstance().getClient().startSession();
        /* Step 2: Optional. Define options to use for the transaction. */
        TransactionOptions txnOptions = TransactionOptions.builder()
                .readPreference(ReadPreference.primary())
                .readConcern(ReadConcern.LOCAL)
                .writeConcern(WriteConcern.MAJORITY)
                .build();

        TransactionBody txnBody = new TransactionBody<String>() {
            public String execute() {

                MongoCollection<Document> product = drsMongoClient.getDatabase().getCollection("product");

                product.insertOne(document);

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

//        BaseProduct baseProduct = null;

//        try {
//            baseProduct = om.readValue(json, BaseProduct.class);
//            baseProduct.setSupplierId(kcode);

//            Integer maxIndex = mongoTemplate.findOne(
//                    Query.query(Criteria.where("supplierId").is(supplierId).and("bpStatus").ne("cancelled"))
//                            .with(Sort.by("currentIndex").descending()), BaseProduct.class, "product")
//                    .getCurrentIndex();
//
//            baseProduct.setCurrentIndex(maxIndex + 1);

//            BaseProduct result = mongoTemplate.insert(baseProduct,"product");
//
//
//            ObjectNode response = om.createObjectNode();
//            response.set("bpList", createResponseBpList(kcode));
//            response.set("products", om.convertValue(result, JsonNode.class));

//            return om.writeValueAsString(response);
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }


    }

    public Integer findMaxSerialNumber(String kcode){

        FindIterable<Document> result = product.find(eq("supplierId", kcode))
                .sort(new BasicDBObject("serial_num", -1)).limit(1);;

        List<Document> resultList = result.into(new ArrayList<Document>());


        return resultList.size() == 0 ? 0 :
                resultList.get(0).get("serial_num") == null ? 0 : (Integer) resultList.get(0).get("serial_num");
    }

    public String intoMongodb(String json){

        Document doc = Document.parse(json);

        return product.insertOne(doc).getInsertedId().toString().
                replace("BsonObjectId{value=","").replace("}","");
    }

    public void productintoMongodb(String json){

        Document doc = Document.parse(json);

        product.insertOne(doc);
    }



    public void productExceptionintoMongodb(String json){

        Document doc = Document.parse(json);

        productException.insertOne(doc);
    }



    public void updateProduct(String id, String json) {


        Document doc = Document.parse(json);
        Bson filter = eq("_id", new ObjectId(id));

        doc.remove("_id");
        //doc.put("supplierId",kcode);
        product.replaceOne(filter , doc);

//        BaseProduct baseProduct =null;
//        try {
//            baseProduct = om.readValue(json, BaseProduct.class);
//            baseProduct.setBpId(id);
//            baseProduct.setSupplierId(kcode);
//            mongoTemplate.save(baseProduct, "product");
//
//
//            ObjectNode response = om.createObjectNode();
////            response.set("bpList", createResponseBpList(kcode));
//            response.set("products", om.convertValue(baseProduct, JsonNode.class));
//
//            return om.writeValueAsString(response);
//        }catch (Exception e){
//            e.printStackTrace();
//        }


    }

    public void updateProduct(Document doc) {


        String id = doc.get("_id").toString();
        Bson filter = eq("_id", new ObjectId(id));

        System.out.println(id);
        System.out.println(doc.toJson());

        UpdateResult updateResult = product.replaceOne(filter, doc);
        System.out.println(updateResult.getMatchedCount());
        System.out.println(updateResult.getModifiedCount());

    }


    public String deleteProduct(String kcode, String productId) {


        Document doc = product.find(eq("_id", new ObjectId(productId))).first();

        Bson filter = eq("_id", new ObjectId(productId));

        doc.remove("bpStatus");
        doc.append("bpStatus","cancelled");

        product.replaceOne(filter , doc);

//        BaseProduct baseProduct = mongoTemplate.findById(productId, BaseProduct.class);
//        try {
//            baseProduct.setBpStatus("cancelled");
//            baseProduct.setSupplierId(kcode);
//            mongoTemplate.save(baseProduct, "product");
//
////            JsonNode product = om.convertValue(baseProduct, JsonNode.class);
////            ObjectNode response = om.createObjectNode();
////            response.set("products", product);
//
//            return findByKcode(kcode, 0, 3);
//        }catch (Exception e){
//            e.printStackTrace();
//        }

        return null;
//        DeleteResult   delete = mongoTemplate.remove(
//                new Query(Criteria.where("_id").is(productId)), "product");

    }

    public Integer findTotalProductNumber(String kCode){
       int count=(int)product.countDocuments(and(eq("supplierId", kCode),
               ne("bpStatus","cancelled")));
       return count;
    }

    public Integer findTotalSkuNumber(String kCode){
      int count=0;
       FindIterable<Document> subProduct= product.find(eq("supplierId", kCode));
       for(Document doc : subProduct) {
           List<Object> skus = (List<Object>)doc.get("skus");
           count+= skus.size();
       }

        return count;
    }

    public String findSkuByEAN(String ean){
        FindIterable<Document> subProduct = product.find();
        String result = "";

        TOP:for(Document doc : subProduct) {
            List<Object> skus = (List<Object>)doc.get("skus");
            for(Object x :skus){
                Document sku = (Document) x;
                Document productId =(Document) sku.get("productId");
                String skuEAN = productId.get("value") != null ? productId.get("value").toString() : "";
                if(skuEAN.equals(ean)){
                    result=sku.get("sellerSku").toString();
                    break TOP;
                }
            }
        }
        return result;
    }

    public String findBySku(String skuCode){
        FindIterable<Document> subProduct = product.find();
        String result = "";

        TOP:for(Document doc : subProduct) {
            System.out.println(doc.toJson()+"8888888888888888888888888888");
            List<Object> skus = (List<Object>)doc.get("skus");
            for(Object x :skus){
                Document sku = (Document) x;
                String sellerSku =sku.get("sellerSku").toString();
                if (sellerSku.equals(skuCode)){
                    result=sellerSku;
                    break TOP;
                }
            }
        }
        return result;
    }

}