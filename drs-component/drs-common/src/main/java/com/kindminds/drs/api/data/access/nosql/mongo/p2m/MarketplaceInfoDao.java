package com.kindminds.drs.api.data.access.nosql.mongo.p2m;

import org.bson.Document;

public interface MarketplaceInfoDao {

    void intoMongodb(String json);

    void save(String json);

    String findByP2MApplicationId(String p2mApplicationId);

    Document findDocumentByP2MApplicationId(String p2mApplicationId);

    String findCommentByP2MId(String p2mApplicationId);
}
