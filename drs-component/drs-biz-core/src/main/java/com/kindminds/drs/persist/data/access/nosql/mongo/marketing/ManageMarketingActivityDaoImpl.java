package com.kindminds.drs.persist.data.access.nosql.mongo.marketing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindminds.drs.api.data.access.nosql.mongo.ManageMarketingActivityDao;
import com.kindminds.drs.api.data.transfer.maketing.MarketingActivity;
import com.kindminds.drs.api.v1.model.common.DtoList;
import com.kindminds.drs.api.v1.model.common.Pager;

import com.kindminds.drs.persist.data.access.nosql.mongo.dto.MarketingActivityImpl;
import com.mongodb.client.result.DeleteResult;
import org.bson.types.ObjectId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;



public class ManageMarketingActivityDaoImpl implements ManageMarketingActivityDao {


    //@Autowired
    // MongoTemplate mongoTemplate;

    ObjectMapper om = new ObjectMapper().setDateFormat(
            new SimpleDateFormat("yyyy-MM-dd'T'HHmmss.SSS'Z'"));

    Date emptyDate;

    {
        try {
            emptyDate = (new SimpleDateFormat("yyyy-MM-dd")).parse("1900-01-02");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    String COLLECTION   = "MarketingActivity";

    public  DtoList<MarketingActivity> find(int pageIndex) {

 /*       DtoList<MarketingActivity> result = new DtoList<MarketingActivity>();

        Query query = new Query().with(Sort.by("startDate").descending());
        //query.addCriteria(Criteria.where("country").`in`("UK"))
        long totalRecords = mongoTemplate.count(query,MarketingActivityImpl.class);
        Pager pager = new Pager(pageIndex,(int)totalRecords);

        query.skip(pager.getStartRowNum() -1);
        query.limit(pager.getPageSize());

        result.setTotalRecords((int)totalRecords);
        result.setPager(pager);



        List activities = mongoTemplate.find(query, MarketingActivityImpl.class);
        result.setItems((List<MarketingActivity>)activities);

        return  result;*/

        return null;
    }

    public DtoList<MarketingActivity> find(String marketplace ,  String supplierCode,
                                           Date startDate ,Date  endDate , int pageIndex )   {

     /*   DtoList<MarketingActivity> result = new DtoList<MarketingActivity>();

        Query query = new Query().with(Sort.by("startDate").descending());
        if (StringUtils.hasText(marketplace)) {
            query.addCriteria(Criteria.where("country").is(marketplace));
        }
        if (StringUtils.hasText(supplierCode)) {
            query.addCriteria(Criteria.where("code").regex(".*$supplierCode.*"));
        }

        if (startDate != null && endDate != null) {
            if (startDate.compareTo(endDate) <=0 ){
                query.addCriteria(Criteria.where("startDate").gte(startDate).lte(endDate));
                query.addCriteria(new Criteria().orOperator(
                        Criteria.where("endDate").gte(startDate).lte(endDate),
                        Criteria.where("endDate").lte(emptyDate)));
            } else {
                result.setTotalRecords(0);
                Pager emptyPager = new Pager(1, 0);
                result.setPager( emptyPager);
                return result;
            }
        } else if (startDate != null && endDate == null) {
            query.addCriteria(Criteria.where("startDate").gte(startDate));
        } else if (startDate == null && endDate != null) {
            query.addCriteria(new Criteria().orOperator(
                    Criteria.where("endDate").lte(endDate),
                    Criteria.where("endDate").lte(emptyDate)));
        }
       // println("query $query")
        long totalRecords = mongoTemplate.count(query,MarketingActivityImpl.class);
        Pager pager = new Pager(pageIndex,(int)totalRecords);

        query.skip(pager.getStartRowNum() -1);
        query.limit(pager.getPageSize());

        result.setTotalRecords((int)totalRecords);
        result.setPager(pager);

        List activities = mongoTemplate.find(query, MarketingActivityImpl.class);

        result.setItems((List<MarketingActivity>)activities);

        result.getItems().forEach(it->{
            if (emptyDate.after(it.getEndDate())) {
                it.setEndDate( null);
            }
        });

        return result;*/
        return null;
    }

    public MarketingActivity findActivityById(String activityId  )   {

        //return mongoTemplate.findById(activityId, MarketingActivityImpl.class);
        return null;
    }

    public MarketingActivity create(String activityJson  )   {

//        MarketingActivityImpl activity = null;
//        try {
//            activity = om.readValue(activityJson, MarketingActivityImpl.class);
//            activity.set_id( ObjectId.get());
//
//            MarketingActivityImpl insert = mongoTemplate.insert(activity, COLLECTION);
//            return insert;
//            //println("insert result $insert")
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return null;
    }

    public MarketingActivity update(String activityJson  )   {

/*        MarketingActivityImpl activity = null;
        try {
            activity = om.readValue(activityJson, MarketingActivityImpl.class);
            MarketingActivity update   = mongoTemplate.save(activity, COLLECTION);
            //println("update result $update")

            return update;
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        return null;

    }

    public DeleteResult delete(String activityId  )   {
  /*      DeleteResult delete = mongoTemplate.remove(new Query(Criteria.where("_id").is(activityId)), COLLECTION);
        //println("delete result $delete")

        return delete;*/
        return null;
    }


}