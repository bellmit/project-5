package com.kindminds.drs.api.data.access.nosql.mongo;

import com.kindminds.drs.api.data.row.logistics.IvsProdDocRequirementRow;

import com.mongodb.client.result.DeleteResult;
import org.bson.Document;

import java.util.List;

public interface IvsProductDocRequirementDao{

    String find(String ivsName, int boxNum , int mixedBoxLineSeq) ;

    String findById(String id ) ;

    String create(String json ) ;

    String update(String id , String json ) ;

    DeleteResult delete(String id ) ;

}