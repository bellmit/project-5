package com.kindminds.drs.api.data.access.nosql.mongo;

import com.kindminds.drs.api.data.row.product.ProductListingRow;
import com.mongodb.client.result.DeleteResult;

import java.util.List;

public interface ProductListingDao {


    List<ProductListingRow> find() ;

    List<ProductListingRow> findById(String id) ;

    String create(String json) ;

    String update(String id, String json) ;

    DeleteResult delete(String id) ;

}