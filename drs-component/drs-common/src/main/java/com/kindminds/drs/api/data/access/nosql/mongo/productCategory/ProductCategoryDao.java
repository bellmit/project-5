package com.kindminds.drs.api.data.access.nosql.mongo.productCategory;

public interface ProductCategoryDao {

    void save(String json);

    String findByParent(String parent);

    String findList(String parent);
}
