package com.kindminds.drs.persist.data.access.nosql.mongo.logistics;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindminds.drs.api.data.access.nosql.mongo.IvsProductDocRequirementDao;
import com.kindminds.drs.api.data.row.logistics.IvsProdDocRequirementRow;
import com.kindminds.drs.persist.data.access.nosql.mongo.DrsMongoClient;
import com.kindminds.drs.persist.data.row.logistics.IvsProdDocRequirementRowImpl;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.util.StringUtils;

import javax.print.Doc;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;


public class IvsProductDocRequirementDaoImpl implements IvsProductDocRequirementDao {


    ObjectMapper om = new ObjectMapper().setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HHmmss.SSS'Z'"));

    Date emptyDate;

    {
        try {
            emptyDate = (new SimpleDateFormat("yyyy-MM-dd")).parse("1900-01-02");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    String COLLECTION   = "IvsProdDocReq";
    MongoCollection<Document> ivsProdDocReqCol = DrsMongoClient.getInstance().getCollection(COLLECTION);

    public String find(String ivsName , int boxNum ,int mixedBoxLineSeq )   {

/*        Query query = new Query();
        query.addCriteria(Criteria.where("ivsName").is(ivsName));
        query.addCriteria(Criteria.where("boxNum").is(boxNum));
        query.addCriteria(Criteria.where("mixedBoxLineSeq").is(mixedBoxLineSeq));*/


        FindIterable<Document> result = ivsProdDocReqCol.find(and(eq("ivsName", ivsName)
                , eq("boxNum", boxNum), eq("mixedBoxLineSeq", mixedBoxLineSeq)));


        String collect = StreamSupport.stream(result
                .spliterator(), false)
                .map(Document::toJson)
                .collect(Collectors.joining(", ", "[", "]"));

        return collect;

    }

    @Override
    public String findById(String id) {
        return null;
    }

    public String create(String json )  {

        if(! StringUtils.isEmpty(json)){
//            IvsProdDocRequirementRowImpl docReq = null;
//            try {
//                docReq = om.readValue(json, IvsProdDocRequirementRowImpl.class);
//                docReq.set_id(ObjectId.get());
//
//               // mongoTemplate.insert(docReq, COLLECTION);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            InsertOneResult insertOneResult = ivsProdDocReqCol.insertOne(Document.parse(json));
            insertOneResult.getInsertedId().asObjectId().getValue().toString();

        }

        return json;
    }

    public String update(String id ,String json )  {

        if(! StringUtils.isEmpty(json)){
        //    try {

        //IvsProdDocRequirementRowImpl docReq =
        //om.readValue(json, IvsProdDocRequirementRowImpl.class);
        //
        //docReq.set_id( new ObjectId(id));
        //mongoTemplate.save(docReq, COLLECTION);
         //  } catch (IOException e) {
           //     e.printStackTrace();
            //}

            Document document = Document.parse(json);
            Bson filter = eq("_id", new ObjectId(id));
            document.put("_id",id);

            ivsProdDocReqCol.replaceOne(filter , document);

        }


        return json;
    }

    public DeleteResult delete(String id )  {

        Bson filter = eq("_id", new ObjectId(id));
        DeleteResult  delete = ivsProdDocReqCol.deleteOne(filter);
                //mongoTemplate.remove(new Query(Criteria.where("_id").is(id)), COLLECTION);

        return delete;
    }



}