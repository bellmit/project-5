package com.kindminds.drs.api.adapter;



import com.amazonaws.mws.model.IdList;
import com.amazonaws.mws.model.TypeList;
import com.kindminds.drs.Marketplace;
import com.kindminds.drs.MwsFeedType;


public interface AmazonMwsFeedAdapter {

    String submitFeed(String xml, Marketplace marketplace, MwsFeedType feedType);

    String submitFeedWithFile(String filePath, Marketplace marketplace, MwsFeedType feedType);

    String getFeedSubmissionList(Marketplace marketplace,
                                 IdList feedSubmissionIdList);

    String getFeedSubmissionList(Marketplace marketplace,
                                 Integer maxCount, TypeList feedTypeList);

    String getFeedSubmissionResult(Marketplace marketplace, String feedSubmissionId);

//    GetFeedSubmissionCountResponse getFeedSubmissionCount(Marketplace marketplace, TypeList feedTypeList);
//
//    CancelFeedSubmissionsResponse cancelFeedSubmissions(Marketplace marketplace, IdList feedIdList);


}
