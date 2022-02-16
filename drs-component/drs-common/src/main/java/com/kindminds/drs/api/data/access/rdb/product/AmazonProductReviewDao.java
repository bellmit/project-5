package com.kindminds.drs.api.data.access.rdb.product;

import java.util.List;


public interface AmazonProductReviewDao {

    List getAmazonProductReviews( String kcode, String marketPlace,  String bpCode, String skuCode);


}

